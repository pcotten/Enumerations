import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;




public class RefNamesToFQNames {

	public static void main(String[] args) throws IOException {
		String sourceDirectory = "C:\\Users\\pcotton\\Documents\\My Received Files\\POJOs\\";
		String targetDirectory = "C:\\Users\\pcotton\\Documents\\Results\\POJOs\\";
		
		File[] files = new File(sourceDirectory.toString()).listFiles();
		
		for (File file : files){
			System.out.println("Processing: " + file.getName() +"\n\n");
			ArrayList<String> importedClasses = new ArrayList<String>();
			BufferedReader inFile = new BufferedReader(new FileReader(sourceDirectory.toString() + "\\" + file.getName()));
			FileWriter outFile = new FileWriter(targetDirectory.toString() + "\\" + file.getName());
			String output = "";
			try {
				String line;
				String pckg = null;
				String newPackage = null;
				while ((line = inFile.readLine()) != null) {
					if (line.contains("package") && (!line.contains("*") && !line.contains(("\\")))){
						pckg = line.substring(line.indexOf("package")+ 8, line.indexOf(";"));
						pckg.trim();
						output += line + "\n";
					}
					else if (line.contains("import")){
						importedClasses.add(line.substring(line.lastIndexOf(".") + 1, line.indexOf(";")));
						output += line + "\n";
					}
					else if (line.contains("protected") && ((!line.contains("*")) && (!line.contains(("\\"))))){
						String clazzName = line.substring(line.indexOf("protected")+ 10, line.lastIndexOf(" "));
						String className = checkForOLUClassName(clazzName);
						if (className.contains("<") && className.contains(">")){
							String aggregate = className.substring(0, className.indexOf("<"));
							String clazzType = className.substring(className.indexOf("<")+ 1, className.indexOf(">"));
							String classType = checkForOLUClassName(clazzType);
						    newPackage = checkForPrimitives(classType, pckg, importedClasses);
						    if (newPackage != ""){
						    	line = line.replace(className, aggregate + "<" + newPackage + "." + classType + ">");
						    	System.out.println("Changed " + className +" to " + aggregate + "<" + newPackage + "." + classType + ">");
						    }
						    else if (classType.equals("Long")){
						    	line = line.replace(className, aggregate + "<" + classType + ">");
								System.out.println("Changed " + className + " to " + aggregate + "<" + classType + ">");
							}
						}
						else {
							newPackage = checkForPrimitives(className, pckg, importedClasses);
							if (newPackage != ""){
								line = line.replace(className, newPackage + "." + className);
								System.out.println("Changed " + className +" to " + newPackage + "." + className);
							}
							else if (className.equals("Long")){
								output += "    @org.kie.api.definition.type.Label(value =  \""+ clazzName + "\")\n";
								line = line.replace(clazzName, className);
								System.out.println("Changed " + clazzName + " to " + className);
							}
						}
						output += line + "\n";
					}
					else if (line.trim().startsWith("public") && !line.contains("class") && (!line.contains("*") || !line.contains(("\\")))){
						String clazzName = line.substring(line.indexOf("public")+ 7, line.indexOf(" ", line.indexOf("public")+8) + 1);
						String className = checkForOLUClassName(clazzName);
						if (className.contains("<") && className.contains(">")){
							String aggregate = className.substring(0, className.indexOf("<"));
							String clazzType = className.substring(className.indexOf("<")+ 1, className.indexOf(">"));
							String classType = checkForOLUClassName(clazzType);
						    newPackage = checkForPrimitives(classType, pckg, importedClasses);
						    if (newPackage != ""){
						    	line = line.replace(className, aggregate + "<" + newPackage + "." + classType + "> ");
						    	System.out.println("Changed " + className +" to " + aggregate + "<" + newPackage + "." + classType + ">");
						    }
						    else if (classType.equals("Long")){
						    	line = line.replace(className, aggregate + "<" + classType + ">");
								System.out.println("Changed " + className + " to " + aggregate + "<" + classType + ">");
							}
						}
						else {
							newPackage = checkForPrimitives(className, pckg, importedClasses);
							if (newPackage != ""){
								line = line.replace(className, newPackage + "." + className);
								System.out.println("Changed " + className +" to " + newPackage + "." + className);
							}
							else if (className.equals("Long") || className.contains(newPackage)){
								clazzName = clazzName.trim();
								line = line.replace(clazzName, className);
								System.out.println("Changed " + clazzName + " to " + className);
							}
						}
						String paramType = null;
						if ((line.contains("(") && line.contains(")")) && line.substring(line.indexOf("("),line.indexOf(")")).length() > 1){
							paramType = line.substring(line.indexOf("(") +1 , line.indexOf(" ", line.indexOf("(")));
							String parameterType = checkForOLUClassName(paramType);
							if (parameterType.contains("<") && parameterType.contains(">")){
								String aggregate = parameterType.substring(0, parameterType.indexOf("<"));
								String clazzType = parameterType.substring(parameterType.indexOf("<")+ 1, parameterType.indexOf(">"));
								String classType = checkForOLUClassName(clazzType);
							    newPackage = checkForPrimitives(classType, pckg, importedClasses);
							    if (newPackage != ""){
							    	line = line.replace(parameterType, aggregate + "<" + newPackage + "." + classType + "> ");
							    	System.out.println("Changed " + parameterType +" to " + aggregate + "<" + newPackage + "." + classType + ">");
							    }
							    else if (classType.equals("Long")){
							    	line = line.replace(parameterType, aggregate + "<" + classType + ">");
									System.out.println("Changed " + parameterType + " to " + aggregate + "<" + classType + ">");
								}
							}
							else {
								newPackage = checkForPrimitives(parameterType, pckg, importedClasses);
								if (newPackage != ""){
									line = line.replace("(" + parameterType, "(" + newPackage + "." + parameterType);
									System.out.println("Changed " + parameterType +" to " + newPackage + "." + parameterType + " in parameter.");
								}
								else if (parameterType.equals("Long")){
									line = line.replace(paramType, parameterType);
									System.out.println("Changed " + paramType + " to " + parameterType + " in parameter.");
								}
							}
						}
						output += line + "\n";
					}
					
					else if ((!line.contains("*")) && (line.contains("OLILU") || line.contains("OLIEXT"))){
						String clazzType = null;
						String classType = null;
						while (line.contains("OLILU") || line.contains("OLIEXT")){
							if (line.contains("<") && line.contains(">")){
								clazzType = line.substring(line.indexOf("OLI"), line.indexOf(">", line.indexOf("OLI")));
								classType = checkForOLUClassName(clazzType);
								
							}
							else {
								clazzType = line.substring(line.indexOf("OLI"), line.indexOf(" ", line.indexOf("OLI")));
								classType = checkForOLUClassName(clazzType);
							}
							line = line.replace(clazzType, classType);
							System.out.println("Changed " + clazzType + " to " + classType + " in method.");
						}
						output += line + "\n";
					}
					else {
						if ((line.contains("<") && line.contains(">")) && (!line.trim().startsWith("*")) && (!line.trim().startsWith("//"))){
							String clazzType = line.substring(line.indexOf("<")+ 1, line.indexOf(">"));
							String classType = checkForOLUClassName(clazzType);
						    newPackage = checkForPrimitives(classType, pckg, importedClasses);
						    if (newPackage != ""){
					    	line = line.replace(clazzType, newPackage +"." + classType);
					    	System.out.println("Changed " + clazzType + " to " + pckg + "." + classType + " in aggregate.");
						    }
						 
						}
						
						output += line + "\n";
					}
				}
			}
			catch (FileNotFoundException fnfe){
				System.out.println("File does not exist:" + file.getName());
			}
			finally {
				if (inFile != null){
					inFile.close();
				}
			}
			
			outFile.write(output);
			outFile.close();
		}
	}

	private static String checkForOLUClassName(String className) {
		if (((className.length()>= 6) && (className.substring(0, 5).equals("OLILU"))) || ((className.length()>= 7) && (className.substring(0,6).equals("OLIEXT")))){
			className = "Long";
		}
		return className;
	}

	private static String checkForPrimitives(String className, String newPackage, ArrayList<String> importedClasses) {
		if (className != null && newPackage != null){
		
		className = className.trim();
		for (String s : importedClasses){
			if (className.equals(s)){
				newPackage = "";
				break;
			}
		}
		
		if (className.contains(newPackage)){
			newPackage = "";
		}
		
		switch (className.trim()) {
			
			case "int":
			case "long":
			case "short":
			case "double":
			case "float":
			case "boolean":
			case "char":
			case "byte":
			case "void":
			case "String":
			case "Integer":
			case "Long":
			case "Short":
			case "Double":
			case "Float":
			case "Boolean":
			case "Character":
			case "Byte":
			case "Object":
			case "Date":
			case "BigDecimal":
			case "XmlBuilder":
			{
				newPackage = "";
			}	
		}
		
		
		
		
		
		}
		return newPackage;
	}

}
