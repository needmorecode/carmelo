package carmelo.common;

public class ClassUtil {
	
	public static Object stringToObject(String string, Class<?> clazz){
		if (clazz == int.class){
			return Integer.parseInt(string);
		}
		else if (clazz == double.class){
			return Double.parseDouble(string);
		}
		else if (clazz == float.class){
			return Float.parseFloat(string);
		}
		else if (clazz == char.class){
			return Float.parseFloat(string);
		}
		else if (clazz == long.class){
			return Long.parseLong(string);
		}
		else if (clazz == String.class){
			return string;
		}
		else
			return null;
	}

}
