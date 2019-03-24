package carmelo.examples.server.rpc.dto;

public class TestArg2Dto {
	
	public TestArg2Dto() {
		
	}
	
	public TestArg2Dto(int num) {
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
