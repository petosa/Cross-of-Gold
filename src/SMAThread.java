import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class SMAThread extends Thread{

	public String sym = "";
	public int days = -1;
	public String next = "";
	
	//debug
	public boolean stuck = true;

	public void run() {
		String msg = "";

		//Arrlist and list for validDates (list eventually gets put in arrlist, so list is temp)
		ArrayList<String> validDates = new ArrayList<String>();
		List<String> validDatesList = null;

		//Get all dates data is know for this symbol from raw stock folder
		ArrayList<String> allDates = DataInterface.allDates(sym, "raw");

		//Are we going until the stock debuted or until a specific date?
		//If true, goes to specific date, if false, goes to debut date.
		boolean isUpdate = false;

		//Used to determine whether this is an update or not.

		//If a file does not exist for this symbol under the sma # directory, its not an update
		if(!DataInterface.dataExists(sym, "sma/" + days)){
			isUpdate = false;			
		}
		//But if it does exist, determine what dates we need to capture and put them in validDatesList
		//and call this an update. Also Move all dates from validDatesList into validDates.
		//If there's an error, that means that we just had a blank text file for the symbol and that
		//it is essentially not an update.
		else{
			try{
				validDatesList = allDates.subList(0, allDates.indexOf(DataInterface.lastUpdate(sym, "sma/" + days)));
				if(validDatesList != null)
					for(String stri:validDatesList)
						validDates.add(stri);
				isUpdate = true;
				msg += sym + " already exists ...";
				if(validDates.size()==0)
					msg += " but an update is not needed.";
				else
					msg += " and an update was needed since " + validDates.get(validDates.size()-1) + ".";


			}catch(ArrayIndexOutOfBoundsException aioobe){
				isUpdate = false;

			}catch(NullPointerException npe){
				npe.printStackTrace();
			}
		}

		//For a non-update
		if(!isUpdate){
			msg += sym + " has no prior data ...";

			//Make the file, or override existing file with a blank one
			DataInterface.createData(sym, "sma/" + days);

			//Used to determine what date to stop at for the SMA. Needed because SMA # can only be calculated
			//once # data entries exist.
			//If the resultant date range is not negative in size, store these dates in validDatesList and 
			//then move them to validDates
			if((allDates.size() - days + 1)>0){
				msg += " and will parse the SMA ...";
				validDatesList = allDates.subList(0, allDates.size() - days + 1);
				for(String stri:validDatesList)
					validDates.add(stri);
				msg += " from " + validDates.get(0) + " to " + validDates.get(validDates.size()-1)
						+ ", eliminating dates after " + validDates.get(validDates.size()-1) + " to " + allDates.get(allDates.size()-1) + ".";
			}
			else
				msg += " but there is not enough stock data to find the " + days + "-day SMA.";

		}

		if(msg.contains("since"))
		System.out.println(msg);
		ArrayList<String> willAppend = new ArrayList<String>();

		for(String date:validDates){

			try{

				double sma = SMA.getSMA(Utils.parseToArray(Paths.get(Config.DIRECTORY + "/raw/" + sym + ".txt")), allDates, date, days);
				willAppend.add(sym + "," + date + "," + sma);

			}catch(NullPointerException npe){
				npe.printStackTrace();
			}
		}

		Collections.reverse(willAppend);
		for(String str:willAppend)
			DataInterface.append(sym, str, "sma/" + days);	
			
		//Remove self from registry
		//boolean stillIn = true;
		//while(stillIn){
		SMA.threads.remove(this);
		//stillIn=false;
	//	CopyOnWriteArrayList<SMAThread> clone =  SMA.threads;
	//	for(SMAThread smt:clone)
	//		if(smt.equals(this))
	//			stillIn=true;
	//	}
		if(!next.equals("")){
			System.out.println(sym + " thread now used by " + next);
			ArrayList<String> nextA = new ArrayList<String>();
			nextA.add(next);
			SMA.run(days, nextA);
			stuck = false;
		}
		else{

			System.out.println(sym + " has tied off a thread, leaving " + SMA.threads.size() + " to go.");
			if(SMA.threads.size()==0){
				System.out.println("Completed!\n");
				Main.smaThreadingActive = false;
				Main.commands();
				Log.write("SMA " + days + " completed.");
			}
		}
	}

}
