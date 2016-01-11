package uml2;

public class AssociationClass {
	String classMain , classChild , multiplicity ;
	public AssociationClass(String classMain , String classChild , String multiplicity){
		this.classMain = classMain;
		this.classChild = classChild;
		this.multiplicity = multiplicity;
	}
	

	public String getClassMain(){
		return classMain;
	}
	public String getClassChild(){
		return classChild;
	}
	public String getMultiplicity(){
		return multiplicity;
	}
}
