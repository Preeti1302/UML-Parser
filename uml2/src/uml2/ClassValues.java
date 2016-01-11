package uml2;

import java.util.ArrayList;
import java.util.List;
import japa.parser.ast.type.ClassOrInterfaceType;

public class ClassValues {
	
	public String classNames;
	List<ClassOrInterfaceType> extendsList =new ArrayList<ClassOrInterfaceType>();
	List<ClassOrInterfaceType> implementsList = new ArrayList<ClassOrInterfaceType>();
	
	public ClassValues(String classNames, List<ClassOrInterfaceType> extendsList, List<ClassOrInterfaceType> implementsList)
	{
		super();
		this.classNames = classNames;
		this.extendsList = extendsList;
		this.implementsList = implementsList;
	}

	public String getClassNames(){
		return classNames;
	}
	
	public List<ClassOrInterfaceType> getExtendsNames(){
		return extendsList;
	}
	
	public List<ClassOrInterfaceType> getImplementsNames(){
		return implementsList;
	}
}


