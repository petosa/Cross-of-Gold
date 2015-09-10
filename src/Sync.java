import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Sync {

	//Given a string array of symbols, syncs their data files to current market data
	public static void run(ArrayList<String> symb){

		Log.write("Running Sync command.");
		
		//For each stock symbol
		for(String s:symb){
			System.out.print("\nSyncing " + s);

			//Flag for appends vs new files
			boolean delete = false;
			ArrayList<String> ranges = new ArrayList<String>();

			//Append
			if(DataInterface.dataExists(s, "raw")){
				try{
				ranges = Date.genRanges(DataInterface.lastUpdate(s, "raw"));
				delete = true;
				//blank file
				}catch(Exception e){
					ranges = Date.genRanges(Config.STARTDATE);
					delete = false;
				}
			}
			//New
			else
				ranges = Date.genRanges(Config.STARTDATE);

			//Create a file now, if new
			if(!DataInterface.dataExists(s, "raw"))
				DataInterface.createData(s, "raw");

			//For each date range provided
			for(String ran:ranges){

				String[] splitArr = ran.split(",");
				ArrayList<String> data = getArrayQuote(s,splitArr[0],splitArr[1]);
				if(data.size()!=0)
					System.out.print(".");
				Collections.reverse(data);
				for(String st:data){
					if(!delete)
						DataInterface.append(s, st, "raw");		
					else
						delete= false;
				}
			}
		}
		System.out.println("\n");
		Log.write("Sync completed.");

	}


	//Returns an arraylist of all historical data for the specified symbol
	//within the provided date range.
	public static ArrayList<String> getArrayQuote(String symb, String startDate, String endDate){

		ArrayList<String> arr = new ArrayList<String>();
		boolean tryAgain = true;

		while(tryAgain){
			tryAgain = false;
			String JSONdata = Yahoo.getHistoricalQuoteJSON(symb, startDate, endDate);

			JSONObject obj;
			try {
				obj = new JSONObject(JSONdata);		
				int count = (int) obj.getJSONObject("query").get("count");

				//If there is more than one stock day included (JSONArray needed)
				if(count>1){
					JSONArray quote = new JSONArray();		

					quote = obj.getJSONObject("query").getJSONObject("results").getJSONArray("quote");

					for(int x=0;x<quote.length();x++){
						String s = "";

						s = s + ((String) ((JSONObject) quote.get(x)).get("Symbol"));
						s = s + "," + ((String) ((JSONObject) quote.get(x)).get("Date"));
						s = s + "," + ((String) ((JSONObject) quote.get(x)).get("Close"));
						s = s + "," + ((String) ((JSONObject) quote.get(x)).get("Open"));
						s = s + "," + ((String) ((JSONObject) quote.get(x)).get("High"));
						s = s + "," + ((String) ((JSONObject) quote.get(x)).get("Low"));
						s = s + "," + ((String) ((JSONObject) quote.get(x)).get("Adj_Close"));
						s = s + "," + ((String) ((JSONObject) quote.get(x)).get("Volume"));

						arr.add(s);

					}
					//If there is one stock day included (JSONObject needed)
				}else if(count==1){
					JSONObject quote = new JSONObject();	

					quote = obj.getJSONObject("query").getJSONObject("results").getJSONObject("quote");

					String s = "";

					s = s + ((String) quote.get("Symbol"));
					s = s + "," + ((String) quote.get("Date"));
					s = s + "," + ((String) quote.get("Close"));
					s = s + "," + ((String) quote.get("Open"));
					s = s + "," + ((String) quote.get("High"));
					s = s + "," + ((String) quote.get("Low"));
					s = s + "," + ((String) quote.get("Adj_Close"));
					s = s + "," + ((String) quote.get("Volume"));

					arr.add(s);

				}

			}
			catch (JSONException e) {
				//e.printStackTrace();
				System.out.print("X");
				tryAgain=true;
			}
		}
		return arr;
	}

}
