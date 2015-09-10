import java.util.ArrayList;
import java.util.Calendar;


public class Date {

	/*Argument: a date to start at. (YYYY-MM-DD)

	Returns: ArrayList of date ranges (YYYY-MM-DD,YYYY-MM-DD)
	starting at provided date until the current date
	such that each entry is a range no larger than 1 year. This is done to
	buffer the Yahoo Finance queries.
	
	If the start date is 2013-03-15 and the current date is July 7 2015, 
	then the following is returned in the ArrayList:

	2013-03-15,2013-12-31
	2014-01-01,2014-12-31
	2015-01-01,2015-07-05
	 */

	public static ArrayList<String> genRanges(String fromDate){

		ArrayList<String> arr = new ArrayList<String>();
		String[] theDate = fromDate.split("-");
		int theYear = Integer.parseInt(theDate[0]);
		String theMonth = theDate[1];
		String theDay = theDate[2];
		
		Calendar cal = Calendar.getInstance();

		int myYear = cal.get(Calendar.YEAR);
		String myMonth = String.format("%02d", cal.get(Calendar.MONTH) + 1);
		String myDay =  String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
		if(theYear==myYear)
			arr.add(theYear + "-" + theMonth + "-" + theDay + "," + myYear + "-" + myMonth + "-" + myDay);
		else{
			arr.add(theYear + "-" + theMonth + "-" + theDay + "," + theYear  + "-12-31");
			theYear++;
			while(theYear<myYear){
				arr.add(theYear + "-01-01" + "," + theYear + "-12-31");
				theYear++;
			}
			arr.add(myYear + "-01-01" + "," + myYear + "-" + myMonth + "-" + myDay);
		}
		return arr;
	}
	
	//return today's date
	public static String getToday(){
		Calendar cal = Calendar.getInstance();

		int myYear = cal.get(Calendar.YEAR);
		String myMonth = String.format("%02d", cal.get(Calendar.MONTH) + 1);
		String myDay =  String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
		return myYear + "-" + myMonth + "-" + myDay;
	}
	
	
	public static String getLastWeek(){
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -7);
		int myYear = cal.get(Calendar.YEAR);
		String myMonth = String.format("%02d", cal.get(Calendar.MONTH) + 1);
		String myDay =  String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
		return myYear + "-" + myMonth + "-" + myDay;
	}
	

}
