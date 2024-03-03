package org.vxwo.springboot.experience.mybatis.sql;

import org.springframework.util.StringUtils;

/**
 * @author vxwo-team
 */

@SuppressWarnings("PMD")
public abstract class BaseSqlRender {
    /**
     * Wrap reserved name in a generated SQL statement
     *
     * @param reserved  the reserved name
     * @return  the generated string
     */
    public abstract String renderReserved(String reserved);

    public String renderProperty(String property, String typeHandler) {
        if (!StringUtils.hasText(typeHandler)) {
            return "#{" + property + "}";
        }

        return "#{" + property + ",typeHandler=" + typeHandler + "}";
    }
}
