package carmelo.json;

public enum ResponseType {
	
	SUCCESS(1),

	FAIL(2),

	PUSH(3),
	
	EXCEPTION(4),
	
	UNLOGIN(5);
	
	private int type;
	
	ResponseType(int type){
		this.type = type;
	}
	
	public int getType(){
		return this.type;
	}

}
