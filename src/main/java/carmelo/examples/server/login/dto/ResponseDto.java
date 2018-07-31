package carmelo.examples.server.login.dto;

public class ResponseDto {
	
	/**
	 * response
	 */
	private int responseType;
	
	/**
	 * data structure
	 */
	private Object data;

	public int getResponseType() {
		return responseType;
	}

	public void setResponseType(int responseType) {
		this.responseType = responseType;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
