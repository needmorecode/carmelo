package carmelo.json;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.zip.DeflaterInputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.alibaba.fastjson.JSONWriter;

/**
 * json appender
 * @author needmorecode
 *
 */
public class JsonBuilder{
	
	private ByteArrayOutputStream out = new ByteArrayOutputStream();
	
	//private DeflaterOutputStream dout;
	
	private JSONWriter writer;
	
	public JsonBuilder(){
		try {
			//dout = new DeflaterOutputStream(out);
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
    		return JsonUtil.compress(out.toByteArray());
    	} catch (IOException e) {
			e.printStackTrace();
	    }	
    	return null;
    }
    
//    public String toString() {
//    	return new String(this.toBytes());
//    }

	public static void main(String[] args) throws IOException {
		JsonBuilder builder = new JsonBuilder();
		for (int i = 1; i <= 100; i++) {
			builder.startObject();
			builder.writeKey("sessionId");
			builder.writeValue("123456");
			builder.endObject();
		}
		byte[] bytes = builder.toBytes();
		System.out.println(new String(bytes, "utf-8"));
		System.out.println(JsonUtil.decompress(bytes));
	}
	

}
