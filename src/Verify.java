import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;


public class Verify {

	//Run verify logic
	@SuppressWarnings("resource")
	public static void run(ArrayList<String> symb){
		
		Log.write("\nRunning Verify command.");
		
		ArrayList<String> rejects = new ArrayList<String>();

		//Visually print winners and losers
		for(String s:symb){
			System.out.print(s);
			if(Yahoo.ping(s))
				System.out.println(" - SUCCESS!");
			else{
				System.out.println(" - FAIL!");
				rejects.add(s);
			}
		}

		Log.write("Verify completed.");
		
		//Follow-up cmd
		boolean loop = true;
		while(loop){
			Scanner in = new Scanner(System.in);
			System.out.println("\n" + rejects.size() + " rejects found. <[V]iew/[D]elete/[E]xit>");
			String cmd = in.nextLine();

			if(cmd.equalsIgnoreCase("V"))
				for(String r:rejects)
					System.out.println(r);

			if(cmd.equalsIgnoreCase("D")){
				fixSL(symb,rejects);
				loop = false;
			}

			if(cmd.equalsIgnoreCase("E"))
				loop = false;
		}

	}

	//Given an arraylist of symbols and rejects, goes through the symbol arraylist and
	//removes and contained rejects
	public static void fixSL(ArrayList<String> symb,ArrayList<String> rejects){

		//Remove unavailables from arraylist
		for(String r:rejects)
			symb.remove(r);

		//Delete the existing stock list
		try {
			Files.delete(Paths.get("stocklist.txt"));
		} catch (IOException e) {
		}

		//Create a new stock file
		try {
			PrintWriter writer;
			writer = new PrintWriter("stocklist.txt", "UTF-8");
			for(String str:symb)
				writer.println(str);
			writer.close();	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		//Create a rejects file
		new File("rejects").mkdir();
		String ts = (Calendar.getInstance().getTime().toString()).replace(":","-");
		try {
			PrintWriter writer;
			writer = new PrintWriter("rejects//Rejects " + ts +".txt", "UTF-8");
			for(String re:rejects)
				writer.println(re);
			writer.close();	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		System.out.println("Removed symbols from stock list and created rejects file!\n");
		
		//reload StockList with new data
		StockList.loadSL();
		
	}

}
