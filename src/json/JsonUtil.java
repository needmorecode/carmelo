package json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;

public class JsonUtil {

	public static byte[] buildJson(EnumResponseType responseType, Object dataDto) {
		// JSONSerializer js = new JSONSerializer();
		ResponseDto responseDto = new ResponseDto(responseType.getType(),
				dataDto);
		// js.write(responseDto);
		String responseStr = JSON.toJSONString(responseDto);
		return responseStr.getBytes();
	}

}
