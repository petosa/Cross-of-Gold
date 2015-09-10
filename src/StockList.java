import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class StockList {

	//A list of all stock symbols to be processed

	//Global variables, initiated with loadSL()
	public static ArrayList<String> SYMBOLS;

	//Returns a boolean indicating whether stocklist.txt exists in root
	public static boolean SLExists(){
		File f = new File("stocklist.txt");
		if(f.exists() && !f.isDirectory()) 
			return true;		
		return false;
	}

	//Creates stocklist.txt in root
	public static void createSL() {
		System.out.println("Creating stock list...");
		PrintWriter writer;

		try {

			writer = new PrintWriter("stocklist.txt", "UTF-8");
			//Populate with know working symbols
			Utils u = new Utils();
			ArrayList<String> arr = u.readFromResources("stocklist.txt");
			for(String s: arr)
				writer.println(s);
			writer.close();	

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	//Stores all StockList values in a global variable
	public static void loadSL() {
		SYMBOLS = parseSL();
		System.out.println("Stock list loaded (" + SYMBOLS.size() + " symbols).\n");
	}

	//Returns a <String> ArrayList of stock symbols
	public static ArrayList<String> parseSL() {
		ArrayList<String> arr = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader("stocklist.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				if(!line.equals(""))
					arr.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return arr;
	}

}
