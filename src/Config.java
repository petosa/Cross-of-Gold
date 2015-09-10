import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;


public class Config {

	//Global variables, initiated with loadConfig()
	public static String DIRECTORY;
	public static String STARTDATE;
	public static int MAXTHREADS = 100;
	public static boolean HELP;
	public static HashMap<String,String> HASHMAP;

	//Returns a boolean indicating whether config.txt exists in root
	public static boolean configExists(){
		File f = new File("config.txt");
		if(f.exists() && !f.isDirectory()) 
			return true;		
		return false;
	}

	//Creates config.txt in root and populates it with default values
	public static void createConfig() {
		System.out.println("Creating config...");
		PrintWriter writer;
		try {
			writer = new PrintWriter("config.txt", "UTF-8");
			writer.println("directory=\"\"");
			writer.println("startDate=\"1970-01-01\"");
			writer.print("help=\"true\"");
			writer.close();	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	//Stores all config values in global variables
	public static void loadConfig() {
		HASHMAP = parseConfig();
		DIRECTORY = HASHMAP.get("directory");
		STARTDATE = HASHMAP.get("startDate");
		HELP = Boolean.parseBoolean(HASHMAP.get("help"));
		
		System.out.println("Config loaded. Loaded values:\nDIRECTORY = \"" + DIRECTORY + "\"" +
				"\nSTARTDATE = " + STARTDATE + "\nHELP = " + HELP + "\n");
	}

	//Returns a <String,String> hashmap of config keys and values
	public static HashMap<String,String> parseConfig() {
		HashMap<String,String> hash = new HashMap<String,String>();
		try (BufferedReader br = new BufferedReader(new FileReader("config.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] arr = line.split("=");
				hash.put(arr[0], arr[1].replace("\"",""));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return hash;
	}

}
