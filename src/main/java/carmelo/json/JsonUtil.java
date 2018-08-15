package carmelo.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONWriter;
import com.alibaba.fastjson.serializer.JSONSerializer;

public class JsonUtil {

	public static byte[] buildJson(ResponseType responseType, Object data) {
		JsonBuilder jb = new JsonBuilder();
		jb.startObject();
		jb.writeKey("responseType");
		jb.writeValue(responseType.getType());
		jb.writeKey("data");
		jb.writeValue(data);
		jb.endObject();
		return jb.toBytes();
	}
	
/*	public static byte[] buildJson(ResponseType responseType, Object data) {
		ResponseDto response = new ResponseDto();
		response.setResponeType(responseType.getType());
		response.setData(data);
		String json = JSON.toJSONString(response);
		return json.getBytes();
	}*/
	
	public static JsonBuilder initResponseJsonBuilder() {
		JsonBuilder jb = new JsonBuilder();
		jb.startObject();
		jb.writeKey("responseType");
		jb.writeValue(ResponseType.SUCCESS.getType());
		jb.writeKey("data");
		return jb;
	}
	
	public static JsonBuilder initPushJsonBuilder(String moduleName) {
		JsonBuilder jb = new JsonBuilder();
		jb.startObject();
		jb.writeKey("responseType");
		jb.writeValue(ResponseType.PUSH.getType());
		jb.writeKey("module");
		jb.writeValue(moduleName);
		jb.writeKey("data");
		return jb;
	}
	
	public static byte[] buildJsonSuccess() {
		JsonBuilder jb = new JsonBuilder();
		jb.startObject();
		jb.writeKey("responseType");
		jb.writeValue(ResponseType.SUCCESS.getType());
		jb.writeKey("data");
		jb.writeValue("");
		jb.endObject();
		return jb.toBytes();
	}
	
	public static byte[] buildJsonFail(String msg) {
		JsonBuilder jb = new JsonBuilder();
		jb.startObject();
		jb.writeKey("responseType");
		jb.writeValue(ResponseType.FAIL.getType());
		jb.writeKey("data");
		jb.writeValue(msg);
		jb.endObject();
		return jb.toBytes();
	}
	
	
	
	public static void main(String[] args) {
		JsonBuilder jb1 = new JsonBuilder();
		jb1.startObject();
		jb1.endObject();
		
		JsonBuilder jb = new JsonBuilder();
		jb.startObject();
		jb.writeKey("responseType");
		jb.writeValue(1);
		jb.writeKey("data");
		jb.writeValue(new String(jb1.toBytes()));
		jb.endObject();
		System.out.println(new String(jb.toBytes()));
	}

}
