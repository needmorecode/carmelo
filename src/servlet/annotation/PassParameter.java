package servlet.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;  
import java.lang.annotation.RetentionPolicy;  

@Target({ElementType.PARAMETER})   // 用于参数  
@Retention(RetentionPolicy.RUNTIME) // 在运行时加载到Annotation到JVM中  
public @interface PassParameter {  
    String name();    // 定义一个没有默认值的String成员  
    //Class<?> type();  // 参数类型
}  
