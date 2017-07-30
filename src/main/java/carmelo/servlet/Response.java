package carmelo.servlet;

/**
 * an output from an action invocation 
 * 
 * @author needmorecode
 *
 */
public class Response {
	
	private byte[] contents;
	
	private int id;
	
	public Response(int id, byte[] contents){
		this.id = id;
		this.contents = contents;
	}

	public byte[] getContents() {
		return contents;
	}

	public void setContents(byte[] contents) {
		this.contents = contents;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

}
