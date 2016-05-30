package it.eurotn.entity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * Indica quali sono le proprietà del bean visualizzabili nel convertert.<br/>
 *
 * @author giangi
 * @version 1.0, 26/mag/2011
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EntityConverter {
    /**
     *
     * @return string separata da virgole che indica le proprietà da inserire nella conversione in
     *         stringa
     */
    public String properties() default "";
}
