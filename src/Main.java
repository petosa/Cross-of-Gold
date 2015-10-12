import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Main{

	/* Stock list should be derived from:
	 * http://www.nasdaq.com/screening/companies-by-name.aspx?letter=0&exchange=all&render=download
	 */

	public static boolean smaThreadingActive = false;

	public static void commands(){
		System.out.println("[Enter a command]");
		System.out.println("[V]erify stock list");
		System.out.println("[S]ync stocks");
		System.out.println("Simple Moving [A]verage (followed by # days)");
		System.out.println("[Q]uit");
	}
	
	@SuppressWarnings("resource")
	public static void main(String[] args){
		System.out.println("Cross of Gold Server v1.0\n(cog-server v1.0)\n");

		//Config
		if(!Config.configExists())
			Config.createConfig();		
		Config.loadConfig();

		if(!Log.logExists())
			Log.createLog();

		//Stock list
		if(!StockList.SLExists())
			StockList.createSL();	
		StockList.loadSL();	

		Log.write("\nApplication opened and initialized.");

		for(;;){
			
			if(!smaThreadingActive){
				commands();
			}
			Scanner in = new Scanner(System.in);
			String cmd = in.nextLine();
			
			if(smaThreadingActive)
				for(SMAThread s:SMA.threads){
					System.out.println(s.sym);
					System.out.println(s.days);
					System.out.println(s.next);
					System.out.println(s.stuck);
					System.out.println("******");
				}
				System.out.println(SMA.threads);

			if(!smaThreadingActive){
				//CMD = s
				if(cmd.equalsIgnoreCase("S")){
					if(Config.HELP){
						System.out.println("\n[Sync Stocks]\nSyncs all stocks listed in the stock list"
								+ " to current market data.\nIf data for a symbol in the stock list does"
								+ " not yet exist, a file will be created.\nAll stock data is saved in the"
								+ " path directory specified in the config file.\n\nContinue? [Y/N]");

						if((cmd = in.nextLine()).equalsIgnoreCase("Y"))
							Sync.run(StockList.SYMBOLS);
					}
					else
						Sync.run(StockList.SYMBOLS);
				}

				//CMD = v
				if(cmd.equalsIgnoreCase("V")){
					if(Config.HELP){
						System.out.println("\n[Verify Stock List]\nIterates through stock list entries"
								+ " and checks each symbol for availability.\nIf a stock's data is unavailable,"
								+ " its symbol is deleted from the stock list.\nA summary of deletions is generated."
								+ "\n\nContinue? [Y/N]");

						if((cmd = in.nextLine()).equalsIgnoreCase("Y"))
							Verify.run(StockList.SYMBOLS);
					}
					else
						Verify.run(StockList.SYMBOLS);
				}

				//CMD = a
				if(cmd.contains("A ")||cmd.contains("a ")){

					//for(SMAThread t:SMA.threads)
						//t.suspend();

					//Try to create a thread-sized batch to initiate recursion
					ArrayList<String> firstBatch = new ArrayList<String>();
					
					try{
						
					List<String> firstBatchList = (StockList.SYMBOLS).subList(0, Config.MAXTHREADS);
					
					for(String s:firstBatchList)
						firstBatch.add(s);
					
					}catch(IndexOutOfBoundsException ioobe){
						firstBatch = StockList.SYMBOLS;
					}			
					
					SMA.numThreads = firstBatch.size();
					int days = Integer.parseInt(cmd.substring(2));

					if(Config.HELP){
						System.out.println("\n[Simple Moving Average]\nA stock market indicator derived"
								+ " by averaging the closing price of a stock\nover X days for each day."
								+ " The second argument specifies what X equals.\nAll data is put in text"
								+ " files under the sma directory.\n\nContinue? [Y/N]");


						if((cmd = in.nextLine()).equalsIgnoreCase("Y")){
							SMA.run(days, firstBatch);
							
							//Log it
							Log.write("Running SMA " + days + " command.");
							
							System.out.println("Using " + SMA.numThreads + " threads.");
						}
							

					}
					else{
						SMA.run(days, firstBatch);
						
						//Log it
						Log.write("Running SMA " + days + " command.");
						
						System.out.println("Using " + SMA.numThreads + " threads.");
					}

				}

				//CMD = q
				if(cmd.equalsIgnoreCase("Q")){
					System.exit(0);
				}
			}
		}
	}
}