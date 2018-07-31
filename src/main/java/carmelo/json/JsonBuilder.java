package carmelo.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSONWriter;

/**
 * json appender
 * @author needmorecode
 *
 */
public class JsonBuilder{
	
	private ByteArrayOutputStream out = new ByteArrayOutputStream();
	
	private JSONWriter writer;
	
	public JsonBuilder(){
		try {
			writer = new JSONWriter(new OutputStreamWriter(out, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
    public void startObject() {
        writer.startObject();
    }

    public void endObject() {
        writer.endObject();
    }

    public void writeKey(String key) {
        writer.writeKey(key);
    }

    public void writeValue(Object object) {
        writer.writeValue(object);
    }

    public void writeObject(String object) {
        writer.writeObject(object);
    }

    public void writeObject(Object object) {
        writer.writeObject(object);
    }

    public void startArray() {
        writer.startArray();
    }

    public void endArray() {
        writer.endArray();
    }
    
    public byte[] toBytes() {
    	try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return out.toByteArray();
    }
    
    public String toString() {
    	return new String(this.toBytes());
    }

	public static void main(String[] args) throws IOException {
//		JsonBuilder ja1 = new JsonBuilder();
//		ja1.startObject();
//		ja1.endObject();
		
//		JsonBuilder ja = new JsonBuilder();
//		ja.startObject();
//		ja.writeKey("responseType");
//		ja.writeValue(1);
//		ja.writeKey("data");
//		//ja.writeValue(new String(ja1.toBytes()));
//		ja.endObject();
//		String s = new String(ja.toBytes());
//		System.out.println(s);
		
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		JSONWriter writer = new JSONWriter(new OutputStreamWriter(out, "UTF-8"));
//		writer.startObject();
//		writer.endObject();
//		writer.close();
//		String s = new String(out.toByteArray());
//		System.out.println(s);
		
		JsonBuilder builder = new JsonBuilder();
		builder.startObject();
		builder.writeKey("sessionId");
		builder.writeValue("123456");
		builder.endObject();
		//System.out.println(new String(JsonUtil.buildJson(ResponseType.SUCCESS, builder.toString())));
	}

}
