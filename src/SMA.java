import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;


public class SMA{

	public static CopyOnWriteArrayList<SMAThread> threads = new CopyOnWriteArrayList<SMAThread>();
	public static int numThreads = -1;

	//Execution logic for SMA
	@SuppressWarnings("deprecation")
	public static void run(int days,ArrayList<String> symb){

		
		//For each symbol in the provided arrlist of all symbols...
		for(String sym:symb){
			
			Main.smaThreadingActive = true;

				SMAThread st = new SMAThread();
				ArrayList<String> all = StockList.SYMBOLS;
				int curIndex = all.indexOf(sym);
				String nextSym = "";
				try{
				nextSym = all.get(curIndex+numThreads);
				}catch(Exception e){}
				threads.add(st);
				st.sym = sym;
				st.days = days;
				st.next = nextSym;
				st.start();
			
		}
		//end of symbol foreach
	}

	public static double getSMA(ArrayList<String> stockData, ArrayList<String> allDates, String validDate, int days){
		int startIndex = allDates.indexOf(validDate);
		double acc = 0;
		for(int ind = startIndex;ind<startIndex + days;ind++){
			String[] arr = stockData.get(ind).split(",");
			//adj close
			acc+=Double.parseDouble(arr[6]);
		}
		return acc/days;
	}

}
