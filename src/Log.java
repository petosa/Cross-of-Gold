import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Calendar;


public class Log {

	//Returns whether a current log file exists
	public static boolean logExists(){
		File f = new File("logs//" + getCurrentLog() + ".txt");
		if(f.exists() && !f.isDirectory()) 
			return true;		
		return false;
	}
	
	//Returns the name of the current log
	public static String getCurrentLog(){
		Calendar c = Calendar.getInstance();
		return "Log " + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + "-" + c.get(Calendar.YEAR);
	}

	//Creates a current log file
	public static void createLog(){
		System.out.println("Creating log...");
		new File("logs").mkdir();
		PrintWriter writer;
		try {
			writer = new PrintWriter("logs//" + getCurrentLog() + ".txt", "UTF-8");
			writer.close();	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	//Appends to current log file
	public static void write(String s){
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("logs//" + getCurrentLog() + ".txt", true));
			writer.write("[" + new Timestamp(System.currentTimeMillis()) + "] " + s);
			writer.newLine();
			writer.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
