package model;

public class CafeVariable {

	private String sort;
	private String name;
	private String type;
	
	public CafeVariable(){
		type = "var";
	}
	
	public void setName(String n){this.name = n;}
	public String getName(){return this.name;}
	
	
	public void setSort(String s){this.sort = s;}
	public String getSort(){return this.sort;}
	
	public String getType(){return this.type;}
	
}//end of CafeVariable
