package uml2;

import java.util.List;
import japa.parser.ast.body.Parameter;
import java.util.ArrayList;

public class MethodValues {

	public String methods , dataTypes ;
	public String classNames , modifier;
	List<Parameter> parameterList =new ArrayList<Parameter>();
	
	public MethodValues(String classNames ,String methods , String dataTypes, String modifier,List<Parameter> parameterList)
	{
		super();
		this.classNames = classNames;
		this.methods = methods;
		this.dataTypes =dataTypes;
		this.modifier = modifier;
		this.parameterList = parameterList;
		//this.parameters =parameters;
	}
	
	public MethodValues(String classNames ,String methods , String dataTypes, String modifier)
	{
		super();
		this.classNames = classNames;
		this.methods = methods;
		this.dataTypes =dataTypes;
		this.modifier = modifier;
	}

	public String getClassNames(){
		return classNames;
	}
	public String getMethods(){
		return methods;
	}
	public String getDataT(){
		return dataTypes;
	}
//	public String getParameters(){
//		return parameters;
//	}
	public String getModifier(){
		return modifier;
	}

	public List<Parameter> getParameterList1(){
		return parameterList;
	}
}
