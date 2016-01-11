package uml2;

import java.util.ArrayList;
import java.util.List;

import japa.parser.ast.body.Parameter;

public class ConstructorClass {
	public String methods ;
	public String classNames , modifier;
	List<Parameter> parameterList =new ArrayList<Parameter>();
	public ConstructorClass(String classConstNames ,String methodsConst , String modifierConst,List<Parameter> parameterConstList)
	{
		super();
		this.classNames = classConstNames;
		this.methods = methodsConst;
		this.modifier = modifierConst;
		this.parameterList = parameterConstList;
		//this.parameters =parameters;
	}
	
	public ConstructorClass(String classNames ,String methods, String modifier)
	{
		super();
		this.classNames = classNames;
		this.methods = methods;
		this.modifier = modifier;
	}

	public String getClassConstNames(){
		return classNames;
	}
	public String getConstMethods(){
		return methods;
	}
	public String getConstModifier(){
		return modifier;
	}
	public List<Parameter> getConstParameterList(){
		return parameterList;
	}
}
