package uml2;

import java.awt.image.BufferedImage;
import java.beans.Statement;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import net.sourceforge.plantuml.SourceStringReader;

public class MyUMLParser {

	// VARIABLES  FOR ARRAYS AND COUNT
	static String[] attributes = new String[300];
	static int attributeCount = 0;
	static String[] methods = new String[300];
	static int methodCount = 0;
	//ARRAYLISTS 
	public static ArrayList<ClassValues> classVal = new ArrayList<ClassValues>();
	public static ArrayList<MethodValues> methodVal = new ArrayList<MethodValues>();
	public static ArrayList<VarValues> varVal = new ArrayList<VarValues>();
	public static ArrayList<DependencyClass> depVal = new ArrayList<DependencyClass>();
	public static ArrayList<AssociationClass> assoVal = new ArrayList<AssociationClass>();
	public static ArrayList<ConstructorClass> constVal = new ArrayList<ConstructorClass>();

	public static String multiplicity = "1";
	public static String plantUMLInput2 = "";
	public static String plantUMLInput3 = "";
	//PLANTUML STARTS
	public static String plantUMLInput = "@startuml\n skinparam classAttributeIconSize 0\n";

	public static void main(String[] args) throws Exception {
		//GETTING THE FILES WITH .JAVA EXTENSION
		FilenameFilter myFile = new FilenameFilter() {
			public boolean accept(File directory, String fileName) {
				return fileName.endsWith(".java");
			}    		
		};
		File folderName = new File(args[0]);    	
		File[] listOfMyFiles = folderName.listFiles(myFile);

		for (int i = 0; i < listOfMyFiles.length; i++) {
			File finalFile = listOfMyFiles[i];
			CompilationUnit cu;
			cu = JavaParser.parse(finalFile);
			new ClassVisitor().visit(cu, null);              
		}
		//GENERATE PLANTUML GRAMMAR
		generatePlantUMLGrammar();
		plantUMLInput = plantUMLInput+ plantUMLInput2 + plantUMLInput3+ "\n@enduml";
		//System.out.println(plantUMLInput);


		ByteArrayOutputStream UMLfile = new ByteArrayOutputStream();
		SourceStringReader umlread = new SourceStringReader(plantUMLInput);
		String plantuml_input = umlread.generateImage(UMLfile);
		byte[] umlData = UMLfile.toByteArray();
		InputStream Image1 = new ByteArrayInputStream(umlData);
		BufferedImage umlImage = ImageIO.read(Image1);
		ImageIO.write(umlImage, "png", new File(args[1]));

	}
	//FUNCTION FOR GENERATING THE GRAMMAR
	private static void generatePlantUMLGrammar() {
		// LOOP FOR CLASS********************************************************************************************************************************
		for(int i=0; i < classVal.size(); i++){
			ClassValues classNames = classVal.get(i);
			String names = classNames.getClassNames().toString();
			List<ClassOrInterfaceType> extendNames = classNames.getExtendsNames();
			List<ClassOrInterfaceType> implementNames = classNames.getImplementsNames();
			if(names.contains("interface")){
				plantUMLInput=plantUMLInput+(" "+names+"{ \n");
			}
			else{
				plantUMLInput=plantUMLInput+("Class"+" "+names+" {\n");
			}
			// LOOP FOR METHODS***************************************************************************************************************************
			for(int j = 0 ; j<methodVal.size();j++){
				MethodValues methodName = methodVal.get(j);
				List<Parameter> methodParams = methodName.getParameterList1();
				for(int q=0 ; q<methodName.getParameterList1().size();q++){
					for(int s=0; s < classVal.size(); s++){
						//DEPENDENCY FOR INTERFACES
						if(names.contains("interface")){
							String classN = names.substring(10);
							if(classN.equals(methodName.getParameterList1().get(q).getType().toString())){
								if(!methodName.getClassNames().contains("interface")){
									DependencyClass depNames = new DependencyClass(names, methodName.getClassNames() );
									depVal.add(depNames);
								}
							}
						}
					}
				}
				String methods =methodName.getMethods().toString();
				String dTypes = methodName.getDataT().toString();
				String modifier = methodName.getModifier().toString();
				//PRINTING METHOD AS PLANTUML OUTPUT
				if(classNames.classNames.equals(methodName.classNames)){
					if(methodName.getParameterList1().size()>0){
						for(int q=0 ; q<methodName.getParameterList1().size();q++){
							String paramType = methodName.getParameterList1().get(q).getType().toString();
							String paramName = methodName.getParameterList1().get(q).getId().toString();
							plantUMLInput=plantUMLInput+(modifier + methods+"("+paramName+" : "+paramType+")"+" : "+dTypes+"\n");
						}	
					}
					else
						plantUMLInput=plantUMLInput+(modifier + methods+"()"+" : "+dTypes+"\n");				
				}
			}
			// LOOP FOR CONSTRUCTORS**********************************************************************************************************************
			for(int n = 0 ; n<constVal.size(); n++){
				ConstructorClass constClassNames = constVal.get(n);
				List<Parameter> methodConstParams = constClassNames.getConstParameterList();
				for(int q=0 ; q<constClassNames.getConstParameterList().size();q++){
					for(int s=0; s < classVal.size(); s++){
						//DEPENDENCY FOR INTERFACES
						if(names.contains("interface")){
							String classN = names.substring(10);
							if(classN.equals(constClassNames.getConstParameterList().get(q).getType().toString())){
								DependencyClass depNames = new DependencyClass(names, constClassNames.getClassConstNames());
								depVal.add(depNames);
							}
						}
					}
				}
				//PRINTING PLANTUML OUTPUT FOR CONSTRUCTOR
				if(classNames.classNames.equals(constClassNames.getClassConstNames()))
				{
					if(constClassNames.getConstParameterList().size()>0){
						for(int q=0 ; q<constClassNames.getConstParameterList().size();q++){
							String paramType = constClassNames.getConstParameterList().get(q).getType().toString();
							String paramName = constClassNames.getConstParameterList().get(q).getId().toString();
							plantUMLInput=plantUMLInput+("+" + constClassNames.getClassConstNames()+"("+paramName+" : "+paramType+")"+"\n");
						}	}
					else
						plantUMLInput=plantUMLInput+("+" + constClassNames.getClassConstNames()+"()"+"\n");
				}
			}
			// LOOP FOR VARIABLES*************************************************************************************************************************
			for(int k = 0 ; k<varVal.size();k++)
			{
				VarValues variableName = varVal.get(k);
				String dataTypes =variableName.getDataTypes();
				String variables = variableName.getVariables();
				String classN = variableName.getClassNames();
				//LOOPS FOR DIFFERENT CONDITION FOR ASSOCIATION
				if(dataTypes.equals(names)){
					multiplicity = "1";
					AssociationClass assoNames = new AssociationClass(classN, names , multiplicity);
					assoVal.add(assoNames);
				}else if(dataTypes.contains("Collection")){
					multiplicity ="*";
					String newDataType = dataTypes.substring(dataTypes.indexOf("<")+1, dataTypes.indexOf(">"));
					if(names.contains("interface")){
						String classConstN = names.substring(10);
						if(newDataType.equals(classConstN)){
							AssociationClass assoNames = new AssociationClass(classN, classConstN , multiplicity);
							assoVal.add(assoNames);
						}	
						else if(newDataType.equals(names)){
							AssociationClass assoNames = new AssociationClass(classN, names , multiplicity);
							assoVal.add(assoNames);
						}
					}
				}else if(dataTypes.contains("[]")){
					multiplicity ="*";
					String newDataType1 = dataTypes.substring(0 , dataTypes.indexOf("["));
					if(newDataType1.equals(names)){
						AssociationClass assoNames = new AssociationClass(classN, names , multiplicity);
						assoVal.add(assoNames);
					}
				}
				else if(names.contains("interface")){
					String classConstN = names.substring(10);
					if(dataTypes.equals(classConstN)){
						AssociationClass assoNames = new AssociationClass(classN, classConstN , multiplicity);
						assoVal.add(assoNames);
					}
				}

				if(classNames.classNames.equals(variableName.classNames)){
					if(variableName.getModifiers()=="+" || variableName.getModifiers()=="-"){
						if(dataTypes.contains("Collection")){
							String newDataType = dataTypes.substring(dataTypes.indexOf("<")+1, dataTypes.indexOf(">"));
							plantUMLInput=plantUMLInput+( variableName.getModifiers() +" "+ variables+" "+":"+" "+newDataType+"[*]"+"\n");
						}else
							plantUMLInput=plantUMLInput+( variableName.getModifiers() +" "+ variables+" "+":"+" "+dataTypes+"\n");
					}
				}
			}
			plantUMLInput=plantUMLInput+("}\n");
			//PRINTING PLANTUML INPUT FOR EXTENDS
			if(extendNames!=null){
				for(int p = 0 ; p<extendNames.size();p++){
					plantUMLInput=plantUMLInput+("Class "+""+names+" --|> "+extendNames.get(p)+"\n");
				}
			}
			//PRINTING PLANTUML INPUT FOR IMPLEMENTS
			if(implementNames!=null){
				for(int q = 0 ; q<implementNames.size();q++){
					plantUMLInput=plantUMLInput+("Class "+""+names+" ..|> "+implementNames.get(q)+"\n");
				}
			}
		}
		//PRINTING PLANTUML INPUT FOR DEPENDENCY
		if(depVal!=null){
			for(int x = 0; x < depVal.size(); x++){
				DependencyClass depValName = depVal.get(x);
				String plantU = depValName.getParentClass() +" <.. "+ depValName.getChildClass() + "\n";
				if(plantUMLInput3.indexOf(plantU)==-1)
					plantUMLInput3+=(depValName.getParentClass() +" <.. "+ depValName.getChildClass() + "\n");
			}
		}
		//PRINTING PLANTUML INPUT FOR ASSOCIATION
		if(assoVal!=null){
			for(int x = 0; x < assoVal.size(); x++){
				AssociationClass assoValName = assoVal.get(x);
				String plantAssoU =  assoValName.getClassMain() +" -- "+'"'+assoValName.multiplicity+'"'+assoValName.getClassChild() + "\n";
				String plantAssoU1 =  assoValName.getClassChild() +" -- "+'"'+assoValName.multiplicity+'"'+ assoValName.getClassMain() + "\n";
				if(plantUMLInput2.indexOf(plantAssoU)==-1 && plantUMLInput2.indexOf(plantAssoU1)==-1 && !assoValName.getClassMain().equals(assoValName.getClassChild()))
					plantUMLInput2+=(assoValName.getClassMain() +" -- "+'"'+assoValName.multiplicity+'"'+assoValName.getClassChild() + "\n");			}
		}
	}

	//CLASS VISITOR********************************************************************************************************************************************

	private static class ClassVisitor extends VoidVisitorAdapter {

		public void visit(ClassOrInterfaceDeclaration n, Object arg) {
			// IF: INTERFACE,  ELSE: CLASS
			if(n.isInterface()){
				String tempClassName = "interface"+" "+n.getName().toString();
				List<ClassOrInterfaceType> tempExtendsName =  n.getExtends();
				List<ClassOrInterfaceType> tempImplementsName = n.getImplements();
				ClassValues classNames1 = new ClassValues(tempClassName, tempExtendsName , tempImplementsName);
				classVal.add(classNames1);
				attributes[attributeCount]= classNames1.getClassNames();

			}else{
				ClassValues classNames = new ClassValues(n.getName().toString(), n.getExtends(), n.getImplements());
				classVal.add(classNames);
				attributes[attributeCount]= classNames.getClassNames();
			}

			new MethodVisitor().visit(n, null);
			new VariableVisitor().visit(n, null);
			new ConstructorVisitor().visit(n, null);
		}
	}

	//METHOD VISITOR********************************************************************************************************************************************
	private static class MethodVisitor extends VoidVisitorAdapter {

		@Override
		public void visit(MethodDeclaration n, Object arg) {
			int modCount=n.getModifiers();
			String modifiers=" ";
			// CHECKING FOR STATIC KEYWORD
			if((n.toString().contains("static"))){
				modifiers = "+";
				MethodValues methodNames = new MethodValues(attributes[attributeCount] ,n.getName().toString() , n.getType().toString() , modifiers, n.getParameters());
				methodVal.add(methodNames);
				methods[methodCount]=methodNames.getMethods();
			}
			// CHECKING FOR ABSTRACT KEYWORD
			if((n.toString().contains("abstract")))
			{
				modifiers = "+";
				MethodValues methodNames = new MethodValues(attributes[attributeCount] ,n.getName().toString() , n.getType().toString() , modifiers);
				methodVal.add(methodNames);
				methods[methodCount]=methodNames.getMethods();
			}
			// CHECKING FOR PARAMETERS
			if(modCount==1){ 
				modifiers = "+";

				if(n.getParameters()!=null){
					MethodValues methodNames = new MethodValues(attributes[attributeCount] ,n.getName().toString() , n.getType().toString() , modifiers, n.getParameters());
					methodVal.add(methodNames);
					methods[methodCount]=methodNames.getMethods();
				}
				else
				{
					MethodValues methodNames = new MethodValues(attributes[attributeCount] ,n.getName().toString() , n.getType().toString() , modifiers);
					methodVal.add(methodNames);
					methods[methodCount]=methodNames.getMethods();
				}
			}
			// METHOD BODY PARSING 
			BlockStmt methodbody=new BlockStmt();
			methodbody = n.getBody();
			String stmtString[];
			if(methodbody!=null)
			{
				List<japa.parser.ast.stmt.Statement> methodst = methodbody.getStmts();
				if(methodst!=null){
					for(japa.parser.ast.stmt.Statement st1 : methodst)
					{
						if(st1.toString().contains("= new"))
						{
							stmtString = st1.toString().split(" ");
							String stmt1 = stmtString[0].toString();
							DependencyClass dependencyNames = new DependencyClass(stmt1, attributes[attributeCount]);
							depVal.add(dependencyNames);
						}
					}
				}
			}
		}
	}

	//VARIABLE VISITOR********************************************************************************************************************************************
	private static class VariableVisitor extends VoidVisitorAdapter {

		@Override
		public void visit(FieldDeclaration n, Object arg) {
			//MODIFIERS
			int modCount=n.getModifiers();
			String modifiers="+";
			switch(modCount)
			{
			case 0:modifiers="-";
			break;
			case 1:modifiers="+";
			break;
			case 2:modifiers="-";
			break;
			case 4:modifiers="#";
			break;
			default:modifiers="+";
			break;
			}
			//CHECKING FOR DATA TYPE
			if(n.getType()!=null){

				int count=n.getVariables().size();
				VarValues variableNames = null;
				for(int i=0;i<count;i++)
				{
					if(n.getVariables().get(i).getId().toString().contains("["))
					{
						variableNames = new VarValues(attributes[attributeCount] ,methods[methodCount], n.getVariables().get(i).getId().getName().toString() , n.getType().toString()+"[]", modifiers);
					}
					else
					{
						variableNames = new VarValues(attributes[attributeCount] ,methods[methodCount], n.getVariables().get(i).getId().getName().toString() , n.getType().toString(), modifiers);
					}
					varVal.add(variableNames);
				}
			}
		}
	}
	//CONSTRUCTOR VISITOR*************************************************************************************************************************************************
	private static class ConstructorVisitor extends VoidVisitorAdapter {

		@Override
		public void visit(ConstructorDeclaration n, Object arg) {

			int modCount=n.getModifiers();
			String modifiers=" ";
			//CHECKING FOR MODIFIERS
			if(modCount==1){
				modifiers = "+";
				//CHECKING PARAMETERS IS NOT NULL
				if(n.getParameters()!=null){
					ConstructorClass methodConstNames = new ConstructorClass(attributes[attributeCount] ,n.getName().toString() , modifiers, n.getParameters());
					constVal.add(methodConstNames);
				}
				else
				{
					ConstructorClass methodConstNames = new ConstructorClass(attributes[attributeCount] ,n.getName().toString() , modifiers);
					constVal.add(methodConstNames);				
				}
			}
		}
	}
}//END of CLASS