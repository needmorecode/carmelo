package carmelo.examples.server.rpc.dto;

public class TestArg1Dto {
	
	public TestArg1Dto() {
		
	}
	
	public TestArg1Dto(int num) {
		this.num = num;
	}
	
	private int num;

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
	

}
