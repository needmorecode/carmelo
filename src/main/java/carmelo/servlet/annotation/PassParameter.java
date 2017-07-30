package carmelo.servlet.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;  
import java.lang.annotation.RetentionPolicy;  

/**
 * a parameter passing by the client
 * @author needmore
 *
 */
@Target({ElementType.PARAMETER})     
@Retention(RetentionPolicy.RUNTIME) 
public @interface PassParameter {  

    String name();    
}  
