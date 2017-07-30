package carmelo.servlet.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;  
import java.lang.annotation.RetentionPolicy;  

/**
 * a paramater stored in the session
 * @author needmorecode
 *
 */
@Target({ElementType.PARAMETER})     
@Retention(RetentionPolicy.RUNTIME)   
public @interface SessionParameter {  
    String name();    
}  
