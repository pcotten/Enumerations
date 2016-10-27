package ccg.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.io.Reader;
//import java.io.StringWriter;
//import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;


public class OLI_LU_STATE_helper {
	
	public String token = "AAAGgZPfXHPf0SNXxU0UqcrL-AbIFWeuks5YBqKuwA%3D%3D";
	public String baseUrl = "github.csc.com/raw/pcotten/Enumerations/master/";
	// ?token=
	public Map<String, List<String>> loadData(String lookup) throws IOException{
		Map<String, List<String>> data = new HashMap<String, List<String>>();
		List<String> d = new ArrayList<String>();
		String url = baseUrl + lookup + ".enumeration" + "?token=" + token;
//		InputStream is = this.getClass().getResourceAsStream("data/OLI_LU_STATE.enumeration");
		BufferedReader inFile = new BufferedReader(new InputStreamReader(OLI_LU_STATE_helper.getGithubContentUsingURLConnection(token, url)));
		String line = null;
		while ((line = inFile.readLine()) != null){
			String key = getKey(line);
			d = getContext(line);
			data.put(key, d);
		}
		inFile.close();
		return data;
	}
	
	public List<String> loadContext(String lookup) throws IOException{
		List<String> context = new ArrayList<String>();
		//InputStream is = this.getClass().getResourceAsStream("data/OLI_LU_STATE.enumeration");
		String url = baseUrl + lookup + ".enumeration" + "?token=" + token;
		BufferedReader inFile = new BufferedReader(new InputStreamReader(OLI_LU_STATE_helper.getGithubContentUsingURLConnection(token, url)));
		String line = null;
		if ((line = inFile.readLine()) != null)
		{
			line = line.trim();
			context = getContext(line);
		}
		else context.add("No context");
//		context = getContext(line);
		inFile.close();
		return context;
	}
	
	public OLI_LU_STATE_helper(){
		
	}

//	public String getContentfromGitHub(){
//		return OLI_LU_STATE_helper.getGithubContentUsingHttpClient(token, url);
//		//this.getGithubContentUsingURLConnection(token, url);
//	}
	
	private List<String> getContext(String line) {
		List<String> context = new ArrayList<String>();
		int index = line.indexOf("\"", line.indexOf("[")) + 1;
		int endIndex = line.indexOf("\"", index);
		do {
			String value = line.substring(index, endIndex);
			context.add(value);
			if (line.charAt(endIndex +1) != ']'){
				index = line.indexOf("\"", endIndex + 1) + 1;
				endIndex = line.indexOf("\"", index);
			}
			else index = line.lastIndexOf("]");
		}
		while (index < line.lastIndexOf("]"));
		return context;
	}

	private String getKey(String line) {
		String key = line.substring(line.indexOf("'", 0) + 1, line.indexOf("'", line.indexOf("'",0)+1));
		
		return key;
	}
	
	private static String getGithubContentUsingHttpClient(String token, String url)
	{
		String newUrl = "https://" + token + ":x-oauth-basic@" + url;
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(newUrl);
		String responseString = "";
		try {
			HttpResponse response = client.execute(request);
			responseString = new BasicResponseHandler().handleResponse(response);
		}
		catch (IOException e){
			e.printStackTrace();
		}
		if (responseString.length() > 0)
			return responseString;
		else
			return "No content";
	}
	
	private static InputStream getGithubContentUsingURLConnection(String token, String url) throws IOException
	{
		String newUrl = "https://" + url;
		InputStream is = null;
		try {
		URL myUrl = new URL(newUrl);
		URLConnection connection = myUrl.openConnection();
		token = token + ":x-oauth-basic";
		String authString = "Basic " + Base64.encodeBase64String((token).getBytes());
		authString = authString.replace("\n", "");
		//connection.setRequestProperty("Authorization", authString);
		System.out.println("from URL: " + myUrl);
		is = connection.getInputStream();
		
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}

//	private static String getStringFromStream(InputStream inputStream) throws IOException {
//		if (inputStream != null){
//			Writer writer = new StringWriter();
//			
//			char[] buffer = new char[2048];
//			try {
//				Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
//				int counter;
//				while ((counter = reader.read(buffer)) != 1){
//					writer.write(buffer, 0, counter);
//				}
//			}
//			finally {
//				inputStream.close();
//			}
//			return writer.toString();
//		}
//		else {
//			return "No Contents";
//		}
//	}
	
	
}
