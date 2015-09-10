import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class DataInterface {

	//Returns a boolean indicating whether config.txt exists in root given sub folder
	public static boolean dataExists(String symb, String sf){
		File f = new File(Config.DIRECTORY + "/" + sf + "/" + symb + ".txt");
		if(f.exists() && !f.isDirectory()) 
			return true;		
		return false;
	}

	//Creates a blank data file for a symbol given sub folder
	public static void createData(String symb, String sf){
		PrintWriter writer;
		new File(Config.DIRECTORY + "/" + sf ).mkdirs();
		try {
			writer = new PrintWriter(Config.DIRECTORY + "/" + sf + "/" + symb + ".txt", "UTF-8");
			writer.println();
			writer.close();	

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	//Appends data to a text file given symbol, append text, and sub folder
	@SuppressWarnings("resource")
	public static void append(String symb, String text, String sf){
		PrintWriter writer;
		try {
			File f = new File(Config.DIRECTORY + "/" + sf + "/" + symb + ".txt");
			String content = new Scanner(f).useDelimiter("\\Z").next();
			writer = new PrintWriter(f, "UTF-8");
			writer.println(text);
			writer.print(content);
			writer.close();	

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
	}

	//Returns most recent entry date of a symbol given sub folder
	public static String lastUpdate(String symb, String sf){
		File f = new File(Config.DIRECTORY + "/" + sf + "/" + symb + ".txt");
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line = br.readLine();
			String[] lineArr = line.split(",");
			return lineArr[1];
		} catch (IOException e) {
			e.printStackTrace();

		} 
		return null;
	}

	//Returns an arraylist of all dates contained by a symbol given sub folder
	public static ArrayList<String> allDates(String symb,String sf){
		
		ArrayList<String> arr = new ArrayList<String>();
		Path p = Paths.get(Config.DIRECTORY + "/" + sf + "/" + symb + ".txt");
		ArrayList<String> lines = Utils.parseToArray(p);

		for(String s:lines){
			String[] sarr = s.split(",");
			arr.add(sarr[1]);
		}

		return arr;
	}




























}

