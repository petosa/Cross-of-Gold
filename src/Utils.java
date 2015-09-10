import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class Utils {

	//Given a stock data file, parse data to a string arraylist
	public static ArrayList<String> parseToArray(Path p){
		ArrayList<String> arr = new ArrayList<String>();
		List<String> lines = null;
		try {
			lines = Files.readAllLines(p, StandardCharsets.UTF_8);

			for(String s:lines)
				arr.add(s);

		} catch (IOException e) {
		}
		
		return arr;	
	}

	//Get text from txt file in resources
	public ArrayList<String> readFromResources(String fileName) {

		InputStream in = getClass().getResourceAsStream("/" + fileName); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		try {
			ArrayList<String> arr = new ArrayList<String>();
			String line = reader.readLine();
			while(line!=null){
				if(!line.equals(""))
					arr.add(line);
				line = reader.readLine();
			}

			return arr;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}

	//Given a URL, return that pages source code as a String
	public static String getPageSource(String myUrl){
		boolean tryAgain = true;
		while(tryAgain){
			tryAgain = false;
			URL url;
			InputStream is = null;
			BufferedReader br;
			String line;
			String output = "";

			try {
				url = new URL(myUrl);
				is = url.openStream(); 
				br = new BufferedReader(new InputStreamReader(is));

				while ((line = br.readLine()) != null) {
					output+=(line);
				}
				return output;
			} catch (Exception e) {
				System.out.print("!");
				tryAgain = true;
			} finally {
				try {
					if (is != null) is.close();
				} catch (IOException ioe) {
				}
			}
		}
		return null;
	}

}
