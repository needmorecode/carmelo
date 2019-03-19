package carmelo.json;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

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
	
	public static byte[] buildJsonException() {
		JsonBuilder jb = new JsonBuilder();
		jb.startObject();
		jb.writeKey("responseType");
		jb.writeValue(ResponseType.EXCEPTION.getType());
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
	
	public static byte[] buildJsonUnlogin() {
		JsonBuilder jb = new JsonBuilder();
		jb.startObject();
		jb.writeKey("responseType");
		jb.writeValue(ResponseType.UNLOGIN.getType());
		jb.writeKey("data");
		jb.writeValue("");
		jb.endObject();
		return jb.toBytes();
	}
	
	/**
	 * use gzip to compress
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	public static byte[] compress(byte[] bytes) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(bytes);
			gzip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	/**
	 * use gzip to decompress
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	public static String decompress(byte[] bytes) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		GZIPInputStream gzip;
		try {
			gzip = new GZIPInputStream(in);
			int n = 0;
			byte[] buffer = new byte[256];
			while ((n = gzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
			return out.toString("UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
