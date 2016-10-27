import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class XMLEnumStripper {
	
	static final Path path = Paths.get("C:\\Users\\pcotton\\Documents\\My Received Files\\Nex_Gen_Allowed_Values_1\\");
	static final Path savePath = Paths.get("C:\\Users\\pcotton\\Documents\\Results\\");
	static final Path masterPath = Paths.get("C:\\Users\\pcotton\\Documents\\My Received Files\\Maste_DD_Full\\Maste_DD_Full.xsd");
	private static HashMap<String, String> typeMap;
	private static HashMap<String, String> typeMapReverse;
	private static ArrayList[][] factFields;
	private static String masterBuffer;
	private static String allowedValuesBuffer;
	
	public static void main(String[] args) throws IOException {
		
		setUpAllowedValuesContexts();
		//masterBuffer = FileIO.readFile(masterPath.toString());
		parseFactsAndFields();
		//typeMap = EnumTypeStripper.doTypeMapping(masterBuffer);
		//typeMapReverse = EnumTypeStripper.doReverseMapping(masterBuffer);
		
		
	}
	
	
	private static void parseFactsAndFields() throws IOException {
		Path factsPath = Paths.get("C:\\Users\\pcotton\\Documents\\My Received Files\\AllowedValuesKeys.txt");
		Path outPathDir = Paths.get("C:\\Users\\pcotton\\Documents\\Enumerations\\EnumerationsFiles");
		Path contextDir = Paths.get("C:\\Users\\pcotton\\Documents\\Results\\");
		
		
		String line;
		File[] files = new File(contextDir.toString()).listFiles();
		
		for (File file : files){
			String contextKey = file.getName().toUpperCase().trim();
			String context = FileIO.readFile(contextDir.toString()+ "\\" + contextKey);
			BufferedReader factFile = new BufferedReader(new FileReader(factsPath.toString()));
			FileWriter outFile = new FileWriter(outPathDir.toString()+ "\\" + contextKey +".enumeration");
			System.out.println("Processing File: " + contextKey);
			while ((line = factFile.readLine()) != null) {
				try{
					String factField = line.substring(0,  line.lastIndexOf("."));
					String keyContext = line.substring(line.lastIndexOf(".")+1).toUpperCase().trim();
					if (contextKey.equals(keyContext)){
						String fact = factField.substring(0,factField.indexOf("."));
						String field = factField.substring(factField.indexOf(".")+1);
						char first = Character.toLowerCase(field.charAt(0));
						field = first + field.substring(1);
						System.out.println("Writing: " + factField );
						outFile.write("'" + fact + "." + field + "': "  + context);
					}
				}
				catch (FileNotFoundException fnfe){
					System.out.println("File does not exist: " + contextKey);
				}
			}
			outFile.close();
			if (factFile != null){
			factFile.close();
			}
		}	
	}


	private static void setUpAllowedValuesContexts() throws IOException{
		File[] files = new File(path.toString()).listFiles();
		for (File file : files){
			ArrayList<String> allowedValuesEnumContext = new ArrayList<String>();
			allowedValuesBuffer = FileIO.readFile(file.getAbsolutePath());
			int avStartIndex = 0;
			int avEndIndex;
			int lastIndex = allowedValuesBuffer.lastIndexOf("fsc:display", allowedValuesBuffer.lastIndexOf("fsc:display"));
			while (avStartIndex < lastIndex){
				avStartIndex = allowedValuesBuffer.indexOf("<fsc:allowedvalue", avStartIndex);
				avEndIndex = allowedValuesBuffer.indexOf("</fsc:allowedvalue>", avStartIndex);
				String allowedValueEntry = allowedValuesBuffer.substring(avStartIndex, avEndIndex);
				allowedValuesEnumContext.add(parseAllowedValueEntry(allowedValueEntry));
				avStartIndex = avEndIndex;
			}
			System.out.println("Writing allowed values context: " + file.getName());
			String saveName = file.getName().substring(0, file.getName().indexOf(".xml"));
			FileIO.writeContextFile(allowedValuesEnumContext, savePath + "\\" + saveName);
		}
	}
	
	public static void showFiles(File[] files){
		for (File file : files){
			if (file.isDirectory()){
				System.out.println("Directory: " + file.getName());
				showFiles(file.listFiles());
			}
			else {
				System.out.println("File: " + file.getName());
			}
		}
	}
	
	private static String parseAllowedValueEntry(String avEntry){	
		int keyIndex, valueIndex;
		keyIndex = avEntry.indexOf('"', avEntry.indexOf("value=", 0)) + 1;
		String key = avEntry.substring(keyIndex, avEntry.indexOf('"', keyIndex + 1));
		valueIndex = avEntry.indexOf('"', avEntry.indexOf("value=", keyIndex)) + 1;
		String value = avEntry.substring(valueIndex, avEntry.indexOf('"', valueIndex + 1));
		
		String keyValuePair = key + "=" + value;
		
		return keyValuePair;
	}

}
