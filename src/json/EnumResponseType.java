package json;

public enum EnumResponseType {
	
	/**
	 * ³É¹¦
	 */
	SUCCESS(1),
	/**
	 * Ê§°Ü
	 */
	FAIL(2),
	/**
	 * ÍÆËÍ
	 */
	PUSH(3);
	
	private int type;
	
	EnumResponseType(int type){
		this.type = type;
	}
	
	public int getType(){
		return this.type;
	}

}
