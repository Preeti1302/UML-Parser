package uml2;

public class DependencyClass {
	
	String parentClass , childClass;
	
	public DependencyClass(String parentClass , String childClass ){
		this.parentClass = parentClass;
		this.childClass = childClass; 
		
	}
	public String getParentClass(){
		return parentClass;
	}
	public String getChildClass(){
		return childClass;
	}
	
}
