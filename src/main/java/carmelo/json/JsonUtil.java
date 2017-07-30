package carmelo.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;

public class JsonUtil {

	public static byte[] buildJson(ResponseType responseType, Object dataDto) {
		ResponseDto responseDto = new ResponseDto(responseType.getType(),
				dataDto);
		String responseStr = JSON.toJSONString(responseDto);
		return responseStr.getBytes();
	}

}
