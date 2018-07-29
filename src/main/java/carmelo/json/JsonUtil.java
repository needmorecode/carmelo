package carmelo.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONWriter;
import com.alibaba.fastjson.serializer.JSONSerializer;

public class JsonUtil {

	public static byte[] buildJson(ResponseType responseType, String data) {
		JsonBuilder ja = new JsonBuilder();
		ja.startObject();
		ja.writeKey("responseType");
		ja.writeValue(responseType.getType());
		ja.writeKey("data");
		ja.writeValue(data);
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
