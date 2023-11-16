package org.vxwo.springboot.experience.web;


import org.springframework.core.Ordered;

/**
 * The Filter Orders
 *
 * @author vxwo-team
 */

public final class CoreOrdered {
    private static final int LAYER_BORDER = 1000;

    public static final int PRELOAD_LAYER = Ordered.HIGHEST_PRECEDENCE;

    public static final int AUTHORIZATION_LAYER = PRELOAD_LAYER + LAYER_BORDER;
}
