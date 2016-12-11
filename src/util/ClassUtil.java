package util;

import java.util.LinkedList;
import java.util.List;

public class ClassUtil {
	
	public static void main(String args[]){
		LinkedList<Integer> list = new LinkedList<Integer>();
		for (int i = 1; i <= 3000; i++){
			list.add(1);
		}
		while(true){
			long startTime = System.currentTimeMillis();
			for (int i = 1; i <= 1000; i++){
				list.add(1);
			}
			long endTime = System.currentTimeMillis();
			System.out.println("1:" + (endTime - startTime));
			
			long startTime1 = System.currentTimeMillis();
			for (int i = 1; i <= 1000; i++)
				list.removeFirst();
			long endTime1 = System.currentTimeMillis();
			System.out.println("2:" + (endTime1 - startTime1));
		}
		
	}
	
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
