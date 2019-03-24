package carmelo.examples.server.rpc.dto;

public class TestReturnDto {
	
	public TestReturnDto() {
		
	}
	
	public TestReturnDto(int num) {
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
