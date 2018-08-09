package carmelo.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONWriter;
import com.alibaba.fastjson.serializer.JSONSerializer;

public class JsonUtil {

	public static byte[] buildJson(ResponseType responseType, Object data) {
		JsonBuilder ja = new JsonBuilder();
		ja.startObject();
		ja.writeKey("responseType");
		ja.writeValue(responseType.getType());
		ja.writeKey("data");
		ja.writeValue(data);
		ja.endObject();
		return ja.toBytes();
	}
	
/*	public static byte[] buildJson(ResponseType responseType, Object data) {
		ResponseDto response = new ResponseDto();
		response.setResponeType(responseType.getType());
		response.setData(data);
		String json = JSON.toJSONString(response);
		return json.getBytes();
	}*/
	
	public static JsonBuilder initResponseJsonBuilder() {
		JsonBuilder ja = new JsonBuilder();
		ja.startObject();
		ja.writeKey("responseType");
		ja.writeValue(ResponseType.SUCCESS.getType());
		ja.writeKey("data");
		return ja;
	}
	
	public static JsonBuilder initPushJsonBuilder(String moduleName) {
		JsonBuilder ja = new JsonBuilder();
		ja.startObject();
		ja.writeKey("responseType");
		ja.writeValue(ResponseType.PUSH.getType());
		ja.writeKey("module");
		ja.writeValue(moduleName);
		ja.writeKey("data");
		return ja;
	}
	
	public static byte[] buildJsonSuccess() {
		JsonBuilder ja = new JsonBuilder();
		ja.startObject();
		ja.writeKey("responseType");
		ja.writeValue(ResponseType.SUCCESS.getType());
		ja.writeKey("data");
		ja.writeValue("");
		ja.endObject();
		return ja.toBytes();
	}
	
	public static byte[] buildJsonFail(String msg) {
		JsonBuilder ja = new JsonBuilder();
		ja.startObject();
		ja.writeKey("responseType");
		ja.writeValue(ResponseType.FAIL.getType());
		ja.writeKey("data");
		ja.writeValue(msg);
		ja.endObject();
		return ja.toBytes();
	}
	
	
	
	public static void main(String[] args) {
		JsonBuilder ja1 = new JsonBuilder();
		ja1.startObject();
		ja1.endObject();
		
		JsonBuilder ja = new JsonBuilder();
		ja.startObject();
		ja.writeKey("responseType");
		ja.writeValue(1);
		ja.writeKey("data");
		ja.writeValue(new String(ja1.toBytes()));
		ja.endObject();
		System.out.println(new String(ja.toBytes()));
	}

}
