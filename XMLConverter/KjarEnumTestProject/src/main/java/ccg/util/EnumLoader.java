package ccg.util;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.io.Reader;
//import java.io.StringWriter;
//import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;

import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.MethodInfo;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.*;

public class EnumLoader{
	
	private String localRepository;
	private String baseDirectory;
	private final String baseUrl = "/repos/pcotten/Enumerations/contents/";
	private ArrayList<String> projectFiles;
	private ArrayList<String> ruleFiles;
	private String whiteListPath;
	private String pomPath;
	
	public EnumLoader() {
		super();
		localRepository = getlocalRepo();
	}
	
	private String getlocalRepo(){
		String repo = System.getenv("M2_REPO");
		if (repo == null){
			repo = "/home/ec2-user/drools/server/tomcat/bin/repositories/kie";
		}
		System.out.println("EnumLoader: local repository at: " + repo);
		return repo;
	}
	
	public Map<String, List<String>> loadData(String repositoryName, String projectName) throws IOException{
		baseDirectory = "./.niogit/" + repositoryName + "/" + projectName;
		projectFiles = getProjectFiles(baseDirectory + "/src/main/resources");
		ruleFiles = getRulesList(projectFiles);
		whiteListPath = getWhiteListPath(baseDirectory);
		pomPath = getPomPath(baseDirectory);
//		System.out.println("EnumLoader: Attempting to load enumeration Map");
		Map<String, List<String>> data = new HashMap<String, List<String>>();
		List<String> jarList = getJarPathList();
		Map<String, String> avLookupMap = getAVTables(jarList);
		if (avLookupMap != null){
			for (String s :avLookupMap.keySet()){
				List<String> context = loadContext(avLookupMap.get(s));
				data.put(s, context);
			}
		}
		else {
			ArrayList<String> emptyList = new ArrayList<String>();
			emptyList.add("Empty");
			data.put("empty", emptyList);
		}
		return data;
	}
		
	public List<String> loadContext(String lookup) throws IOException{
		List<String> context = new ArrayList<String>();
		BufferedReader inFile = new BufferedReader(new InputStreamReader(getContentWithGithubClient(lookup)));
		String line = null;
		if ((line = inFile.readLine()) != null){
			{
				line = line.trim();
				context = getContext(line);
			}
		}
		else context.add("No context");
		System.out.println("EnumLoader : Context for " + lookup + " loaded.");
		return context;
	}
	

	public InputStream getContentWithGithubClient(String lookup) throws IOException{
		
		GitHubClient client = new GitHubClient("github.csc.com");
		UserService service = new UserService(client);
		client.setCredentials("pcotten", "57cae0d20b4c82e35fe06ae3f8f6b8c3827e2a8d");
		GitHubRequest request = new GitHubRequest();
		
//		RepositoryService repoService = new RepositoryService(client);
//		for (Repository repo : repoService.getRepositories()){
//			System.out.println(repo.getName());
//		}
//		Repository enumRepo = repoService.getRepository("pcotten", "Enumerations");
//		System.out.println(enumRepo.getUrl());
//		
		request.setUri(baseUrl + lookup +".enumeration");
//		System.out.println("URI: " + request.getUri());
//		InputStream content = client.getStream(request);
		client.setBufferSize(20000);
		InputStream response = client.getStream(request);
		BufferedReader reader = new BufferedReader(new InputStreamReader(response));
		String stream = "";
		String line = "";
		while ((line = reader.readLine()) != null){
			stream = stream + line;
		}
		String downloadUrl = stream.substring((stream.indexOf("download_url") + 15), (stream.indexOf("type") - 3));
//		System.out.println(downloadUrl);
		InputStream content = this.getGithubContentUsingURLConnection(downloadUrl);
//		System.out.println("\n" + content);
		return content;
		
	}
	


	public ArrayList<String> getProjectFiles(String filename){
//		System.out.println("EnumLoader: Looking for project files in: " + filename);
		ArrayList<String> fileList = new ArrayList<String>();
		File folder = new File(filename);
		File[] files = folder.listFiles();
		if (fileList != null){
			if (!filename.contains(".\\target")){
				for (File f : files){
	
					if (f.isDirectory()){
						ArrayList<String> subList = getProjectFiles(f.getPath());
						fileList.addAll(subList);
					}
					else {
						fileList.add(f.getPath());
					}	
				}
			}
		}
		return fileList;
	}
	
	public String getWhiteListPath(String baseDirectory) throws IOException{
//		System.out.println("EnumLoader: Getting the path to the white list");
		String filepath = null;
//		File folder = new File(filename);
		File[] fileList = new File(baseDirectory).listFiles();
		if (fileList != null){
			for (File f : fileList){
				if (f.getName().contains("package-names-white-list")){
					filepath = f.getPath();
//					System.out.println("Path to whitelist: " + filepath);
					break;
				}
			}
		}
		if (filepath == null){
			System.out.println("EnumLoader: White list not found in " + baseDirectory);
		}
		return filepath;
	}
	
	public String getPomPath(String baseDirectory) throws IOException{
//		System.out.println("EnumLoader: Getting the path to the pom file");
		String filepath = null;
		File[] fileList = new File(baseDirectory).listFiles();
		if (fileList != null){
			for (File f : fileList){
				if (f.getName().endsWith("pom.xml")){
					filepath = f.getPath();
//					System.out.println("Path to pom: " + filepath);
					break;
				}
			}
			if (filepath == null){
				System.out.println("EnumLoader: Pom file not found in " + baseDirectory);
			}
		}
		return filepath;
	}
	private List<String> getContext(String line) {
//		System.out.println("EnumLoader: Getting context information");
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
	
	private static InputStream getGithubContentUsingURLConnection(String url) throws IOException
	{
		InputStream is = null;
		try {
		URL myUrl = new URL(url);
		URLConnection connection = myUrl.openConnection();
//		token = token + ":x-oauth-basic";
//		String authString = "Basic " + Base64.encodeBase64String((token).getBytes());
//		authString = authString.replace("\n", "");
		//connection.setRequestProperty("Authorization", authString);
		System.out.println("EnumLoader: Enumeration data acquired from URL: " + myUrl);
		is = connection.getInputStream();
		
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}

	public ArrayList<String> getPackageList(String whiteListFile) throws IOException {
//		System.out.println("EnumLoader: Attempting to get package list from white list");
		ArrayList<String> list = new ArrayList<String>();
		String content = "";
		BufferedReader inFile = new BufferedReader(new FileReader(whiteListFile));
		int counter = 0;
		try{
			String line;
//			System.out.print("Reading File ");
			while ((line = inFile.readLine()) != null){
				list.add(line);
			}
		}
		catch (FileNotFoundException fnfe){
			System.out.println("File does not exist:" + whiteListFile);
		}
		finally {
			if (inFile != null){
				inFile.close();
			}
		}		
			return list;
	}

	public ArrayList<String> getRulesList(ArrayList<String> projectFiles){
		ArrayList<String> ruleList = new ArrayList<String>();
		try{
//		System.out.println("Attempting to acquire list of rules in the project");

		ArrayList<File> fileList = new ArrayList<File>();
		if (projectFiles != null){
			for (String s : projectFiles){
				fileList.add(new File(s));
//				System.out.println("EnumLoader: Adding " + s + " to search list for rules");
			}
			for (File f : fileList){
//				System.out.println("EnumLoader: Checking file: " + f.getName());
				if ((f.getName().endsWith(".gdst")) || (f.getName().endsWith(".rdrl"))){
//					System.out.println("EnumLoader: Found rule: " + f.getName());
					boolean exists = false;
					for (int i = 0; i < ruleList.size(); i++){
//						System.out.println("EnumLoader: Checking to see if rule exists");
						String rule = ruleList.get(i);
						if(rule.contains("\\")){
							String sub = rule.substring(rule.lastIndexOf("\\"));
							if (rule.substring(rule.lastIndexOf("\\") + 1).equals(f.getName()))
								exists = true;
						}
					}
					if (!exists){
//						System.out.println("EnumLoader: Adding " + f.getName() + " to ruleList");
						ruleList.add(f.getPath());
					}
				}
			}
		}

		}
		catch (Exception e){
			System.out.println("EnumLoader: Exception occurred while adding rules");
			e.printStackTrace();
		}
		return ruleList;
		
	}
	
	
	public ArrayList<String> getDataTypeList(ArrayList<String> ruleFiles) throws IOException{ 
//		System.out.println("EnumLoader: Attempting to acquire list of data objects");
		ArrayList<String> dataTypeList = new ArrayList<String>();
//		ArrayList<String> fileNames = new ArrayList<String>();
//		for(File f : ruleFiles){
//			fileNames.add(f.getName());
//		}
		for (String f : ruleFiles){
			if (f.endsWith(".rdrl"))
				dataTypeList.addAll(parseRule(f));
			if (f.endsWith(".gdst"))
				dataTypeList.addAll(parseTable(f));
		}
		dataTypeList = removeDuplicates(dataTypeList);
		return dataTypeList;
	}

	private ArrayList<String> removeDuplicates(ArrayList<String> dataTypeList) {
//		System.out.println("EnumLoader: Removing duplicate objects");
//		ArrayList<String> workingList = (ArrayList<String>)dataTypeList.clone();
		for (int i = 0; i < dataTypeList.size(); i++){
			for (int j = i + 1; j < dataTypeList.size(); j++)
				if (dataTypeList.get(i).equals(dataTypeList.get(j))){
//					System.out.println("Removing duplicate: " + dataTypeList.get(j));
					dataTypeList.remove(j);
			}
		}
		return dataTypeList;
	}
	private List<String> parseRule(String filename) throws IOException {
//		System.out.println("EnumLoader: Parsing rule: " + filename);
		List<String> imports = new ArrayList<String>();
		String content = "";
		try {
			BufferedReader inFile = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = inFile.readLine()) != null){
				if (line.trim().startsWith("import")){
					line = line.replace("import", "").trim();
					if (!line.startsWith("java")){
						line = line.substring(line.lastIndexOf(".") + 1).replace(";", "");
						imports.add(line);
//						System.out.println("Adding: " + line.trim());
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return imports;
	}
	
	private List<String> parseTable(String filename) throws IOException {
//		System.out.println("EnumLoader: Parsing table: " + filename);
		ArrayList<String> imports = new ArrayList<String>();
		String content = "";
		try {
			BufferedReader inFile = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = inFile.readLine()) != null){
				if (line.trim().startsWith("<type>")){
					line = line.replace("<type>", "");
					line = line.replace("</type>", "").trim();
					if (!line.startsWith("java")){
						line = line.substring(line.lastIndexOf(".")+1);
						imports.add(line);
//						System.out.println("Adding: " + line.trim());
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("EnumLoader: Exception occurred while parsing rule table");
			e.printStackTrace();
		}
		
		return imports;
	}
	
	public ArrayList<HashMap<String, String>> getDependencyInfo(String pomFile){
		ArrayList<HashMap<String, String>> dependencyMap = new ArrayList<HashMap<String, String>>();
		try{
//			System.out.println("EnumLoader: Attempting to acquire dependency info");
			BufferedReader inFile = new BufferedReader(new FileReader(pomFile));
			String content = "";
			String line;
//			System.out.print("Reading pom File ");
			while ((line = inFile.readLine()) != null){
				content = content + line;
			}
			if (inFile != null){
				inFile.close();
			}
			dependencyMap = processPom(content);
			}
		catch (Exception e){
			System.out.println("EnumLoader: Exception occurred while getting dependency info");
			e.printStackTrace();
		}

//			System.out.println("");
		return dependencyMap;
	}

	
	private ArrayList<HashMap<String, String>> processPom(String content) {
//		System.out.println("EnumLoader: Processing the Pom file");
		ArrayList<HashMap<String, String>> mapList = new ArrayList<HashMap<String, String>>();
		int dependencyIndex = 0;
		int endIndex = 0;
		if (content.contains("</dependency>"))
			endIndex = content.lastIndexOf("</dependency>");
		else
			endIndex = -1;
		if (content.contains("<dependency>")){
			dependencyIndex = content.indexOf("<dependency>", dependencyIndex);
			while ((dependencyIndex < endIndex) && (dependencyIndex != -1)){
				int groupIndex = content.indexOf("<groupId>", dependencyIndex) + 9;
				String groupId = content.substring(groupIndex, content.indexOf("<", groupIndex));
//				System.out.println("GroupID: " + groupId);
				int artifactIndex = content.indexOf("<artifactId>", dependencyIndex)+ 12;
				String artifactId = content.substring(artifactIndex,  content.indexOf("<", artifactIndex));
//				System.out.println("ArtifactID: " + artifactId);
				int versionIndex = content.indexOf("<version>", dependencyIndex) + 9;
				String version = content.substring(versionIndex, content.indexOf("<", versionIndex));
//				System.out.println("Version: " + version);
				HashMap<String, String> dependency = new HashMap<String, String>();
				dependency.put("groupId", groupId);
				dependency.put("artifactId", artifactId);
				dependency.put("version", version);
//				System.out.println("EnumLoader: found " + groupId + "." + artifactId + "." + version + "dependency");
				mapList.add(dependency);
				dependencyIndex = content.indexOf("<dependency>", dependencyIndex + 12); ;
			}
		}
		return mapList;
	}
	

	public List<String> getJarPathList(){
		List<String> jarPathList = new ArrayList<String>();
		
		try {
//			System.out.println("EnumLoader: Attempting to produce JarPathList");
			ArrayList<HashMap<String, String>> dependencyMapList = getDependencyInfo(pomPath);
			if (dependencyMapList == null){
				System.out.println("EnumLoader: No dependencies in map to search for jar files!");
			}
			for( HashMap hm : dependencyMapList ){
				String groupId = toPath((String) hm.get("groupId"));
				String artifactId = toPath((String) hm.get("artifactId"));
				String version = (String) hm.get("version");
				
				String dependencyPath = (localRepository + "/" + groupId + "/" + artifactId + "/" + version);
//				System.out.println("EnumLoader: Looking for jar in " + dependencyPath);
				File[] files = new File(dependencyPath).listFiles();
				if (files != null){
					for (File f : files){
						String name = f.getName();
						if (name.endsWith(".jar") && !name.endsWith("sources.jar") && !name.endsWith("javadoc.jar")){
							if (isPackagePresent(f.getPath()))
//								System.out.println("EnumLoader: Adding " + f.getName() + " to jarList");
								jarPathList.add(f.getPath());
							break;
						}
					}
				}
				else {
					System.out.println("EnumLoader: " + dependencyPath + " does not exist");
				}
			}

		
		} catch (Exception e) {
			System.out.println("Exception encountered while getting jarPathList");
			e.printStackTrace();
		}
//		System.out.println(jarPathList);
		return jarPathList;
	}
	
	private String toPath(String s) {
		if (s.contains(".")){
			s = s.replace(".", "/");
		}
		return s;
	}
	
	private boolean isPackagePresent(String path){
		
		
		boolean packageIsPresent = false;	
		try {
			ArrayList<String> packageList = getPackageList(whiteListPath);
//			System.out.println("EnumLoader: Checking to see if package is in jar");
			JarFile jarFile = new JarFile(path);
			for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ){
				JarEntry entry = entries.nextElement();
				String entryName = entry.getName().replaceAll("/", ".").replaceAll(".class", "").trim();
//				System.out.println(entryName);
				if (entryName.contains(".")){
					entryName = entryName.substring(0, entryName.lastIndexOf("."));
				}
				for (String packageName : packageList){
//					String packagePath = toPath(packageName);
					if (entryName.equals(packageName)){
//						System.out.println("Found! " + entryName);
						packageIsPresent = true;
					}
				}
				if (packageIsPresent)
					break;
				}
			}
		 	catch (IOException e) {
				System.out.println("Unable to find jar: " + path);
				e.printStackTrace();
			}
			return packageIsPresent;
		}
		

	public Map<String, String> getAVTables(List<String> jarList) throws IOException{
		
		ArrayList<String> dataObjects = getDataTypeList(ruleFiles);
		ArrayList<String> classPaths = new ArrayList<String>();
		HashMap<String, String> lookupTables = new HashMap<String, String>();
		
		for (String jarPath : jarList){
			JarFile jarFile = new JarFile(jarPath);
			for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ){
				JarEntry entry = entries.nextElement();
				String entryName = entry.getName().replaceAll("/", ".").replaceAll(".class", "").trim();
//				System.out.println(entryName);
				for (String s : dataObjects){
					entryName = entryName.substring(entryName.lastIndexOf(".")+1);
					if (s.equals(entryName)){
						classPaths.add(entryName);
						JarEntry jarEntry = new JarEntry(entry.getName());
						InputStream jarIn = jarFile.getInputStream(jarEntry);
						DataInputStream dstream = new DataInputStream(new BufferedInputStream(jarIn));
						ClassFile cf = new ClassFile(dstream);
//						System.out.println(cf.getName());
						List<MethodInfo> methodInfos = cf.getMethods();
						
						for (MethodInfo mi : methodInfos){
							AnnotationsAttribute visible = (AnnotationsAttribute) mi.getAttribute(AnnotationsAttribute.visibleTag);
							if (visible != null){
								for (javassist.bytecode.annotation.Annotation ann : visible.getAnnotations()){
									String lookupTable = ann.toString();
									if (lookupTable.contains("->") && lookupTable.contains("OLI")){
										lookupTable = lookupTable.substring(lookupTable.indexOf("OLI"), lookupTable.indexOf("->"));
									}
									if (lookupTable.contains("OLI_LU")){
										if (lookupTable.contains("\"")){
											lookupTable = lookupTable.substring(lookupTable.indexOf('"') + 1, lookupTable.lastIndexOf('"'));
											String factField = mi.getName().replaceAll("get", "");
											String firstLetter = factField.substring(0,1);
											factField = factField.trim().replace(factField.substring(0, 1), factField.substring(0,1).toLowerCase());
											lookupTables.put(entryName + "." + factField, lookupTable);
											
										}
									}
								}
							}
						}
					}
				}
			}
		}
//		System.out.println(lookupTables);
		return lookupTables;
	}
}
