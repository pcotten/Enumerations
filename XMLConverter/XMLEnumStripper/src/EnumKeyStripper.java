import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.*;
import java.util.HashMap;

public class EnumKeyStripper {
	
	static final Pattern p = Pattern.compile("<xsd:element name=.+type=.+>");
	static Path path = Paths.get("C:\\Users\\pcotton\\Documents\\My Received Files\\Maste_DD_Full\\Maste_DD_Full.xsd");
	static HashMap<String, String> typeMap = new HashMap<String, String>();
	
	public static HashMap<String, String> doTypeMapping(String fileBuffer) throws IOException{
	
		int startIndex = 0;
		int currentIndex = 0;
		fileBuffer=FileIO.readFile(path.toString());
		Matcher matcher = p.matcher(fileBuffer);
		boolean endOfFile = false;
		
		while (!endOfFile){
			if(matcher.find(currentIndex)){
				currentIndex=matcher.start();
				typeMap.put(parseKey(currentIndex, fileBuffer), parseValue(currentIndex, fileBuffer));
				currentIndex=matcher.end();
			}
			else endOfFile = true;
		}
		return typeMap;
	}

	private static String parseValue(int currentIndex, String fileBuffer) {
		int valueIndex = fileBuffer.indexOf("type=", currentIndex) + 6;
		int endValueIndex = fileBuffer.indexOf('"', valueIndex);
		String value = fileBuffer.substring(valueIndex, endValueIndex);
		System.out.println("Value: " +value);
		return value;
	}
	
	private static String parseKey(int currentIndex, String fileBuffer) {
		int keyIndex = fileBuffer.indexOf("name=" ,currentIndex) + 6;
		int endKeyIndex = fileBuffer.indexOf('"', keyIndex);
		String key = fileBuffer.substring(keyIndex, endKeyIndex);
		System.out.print("Key: " +key + ", ");
		return key;
	}

	public static HashMap<String, String> doReverseMapping(String fileBuffer) throws IOException {
		int startIndex = 0;
		int currentIndex = 0;
		fileBuffer=FileIO.readFile(path.toString());
		Matcher matcher = p.matcher(fileBuffer);
		boolean endOfFile = false;
		
		while (!endOfFile){
			if(matcher.find(currentIndex)){
				currentIndex=matcher.start();
				typeMap.put(parseValue(currentIndex, fileBuffer), parseKey(currentIndex, fileBuffer));
				currentIndex=matcher.end();
			}
			else endOfFile = true;
		}
		return typeMap;
	}

}

