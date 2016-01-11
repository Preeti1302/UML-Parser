package uml2;

public class VarValues {

	public String variables;
	public String methods;
	public String classNames;
	public String dataType;
	public String modifier;
	public VarValues(String classNames ,String methods , String variables , String dataType, String modifier)
	{
		super();
		this.classNames = classNames;
		this.methods = methods;
		this.variables = variables;
		this.dataType = dataType;
		this.modifier = modifier;
	}

	public String getVariables(){
		return variables;
	}
	public String getClassNames(){
		return classNames;
	}
	public String getMethods(){
		return methods;
	}
	public String getDataTypes(){
		return dataType;
	}
	public String getModifiers(){
		return modifier;
	}
}
