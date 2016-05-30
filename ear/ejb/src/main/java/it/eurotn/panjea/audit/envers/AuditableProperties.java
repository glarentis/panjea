package it.eurotn.panjea.audit.envers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * Indica quali sono le proprietà del bean visualizzabili nell'audit.<br/>
 *
 * @author giangi
 * @version 1.0, 26/mag/2011
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AuditableProperties {
    /**
     *
     * proprietà da escludere
     */
    String[]excludeProperties() default {};

    /**
     * array delle proprietà auditable per il bean..
     */
    String[]properties() default {};
}
