package carmelo.examples.server.login.dto;

public class TestDto {
	
	private int id;
	
	private String name;
	
	public TestDto(){
		
	}
	
	public TestDto(int id, String name){
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
