
public class Yahoo {
	
	//Get stock JSON for a specific date interval (YYYY-MM-DD)
	public static String getHistoricalQuoteJSON(String s, String startDate, String endDate){
		String out = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20in%20(";
			out = out + "\""+s+"\",";
		out = out.substring(0,out.length()-1);
		out = out + ")%20and%20startDate%20=%20%22" + startDate + "%22%20and%20endDate%20=%20%22" 
				+ endDate + "%22&diagnostics=true&env=store://datatables.org/alltableswithkeys&format=json";
		return Utils.getPageSource(out);
	}
	
	//Checks symbol availability
	public static boolean ping(String s){
		//Looks for a specific JSON pattern in stock quotes within the past week
		String json = getHistoricalQuoteJSON(s,Date.getLastWeek(),Date.getToday());
		if(json.contains("Not Found")){
			return false;
		}
		else
			return true;
	}
	
}
