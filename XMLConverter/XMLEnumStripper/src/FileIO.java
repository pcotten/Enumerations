import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileIO {


	static String readFile(String filename) throws IOException{
		String content = "";
		BufferedReader inFile = new BufferedReader(new FileReader(filename));
		int counter = 0;
		try{
			String line;
			System.out.print("Reading File ");
			while ((line = inFile.readLine()) != null){
				content += line +"\n";
				if (counter % 200 == 0){
					System.out.print(".");
				}
				counter++;
			}
		}
		catch (FileNotFoundException fnfe){
			System.out.println("File does not exist:" + filename);
		}
		finally {
			System.out.println("");
			if (inFile != null){
				inFile.close();
			}
		}		
		return content;
	}
	
	static void writeContextFile(ArrayList<String> allowedValuesEnumContext, String filename) throws IOException {
		FileWriter outFile = new FileWriter(filename);
		outFile.write('[');
		int count = 0;
		for (String s : allowedValuesEnumContext){
			outFile.write("\"" + s);
			if (count < allowedValuesEnumContext.size() - 1){
				outFile.write("\", ");
				count++;
			}
			else {
				outFile.write("\"]");
			}
		}
		outFile.close();
	}
	

	
}
