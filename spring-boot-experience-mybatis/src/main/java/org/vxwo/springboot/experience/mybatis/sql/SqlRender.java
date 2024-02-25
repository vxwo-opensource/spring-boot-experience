package org.vxwo.springboot.experience.mybatis.sql;


/**
 * @author vxwo-team
 */

public interface SqlRender {
    /**
     * Wrap reserved name in a generated SQL statement
     *
     * @param reserved  the reserved name
     * @return  the generated string
     */
    String reserved(String reserved);

    /**
     * Generates a binding for a property to a placeholder in a generated SQL statement
     *
     * @param property  the property name
     * @return  the generated binding
     */
    String property(String property);
}
