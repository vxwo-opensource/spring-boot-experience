package org.vxwo.springboot.experience.web;


import org.springframework.core.Ordered;

/**
 * The Filter Orders
 *
 * @author vxwo-team
 */

public final class CoreOrdered {
    public static final int LAYER_NEAR = 100;

    public static final int LAYER_BORDER = 1000;

    public static final int PRELOAD_LAYER = Ordered.HIGHEST_PRECEDENCE;

    public static final int FIRST_AUTHORIZATION_LAYER = PRELOAD_LAYER + LAYER_BORDER;

    public static final int FREQUENCY_CONTROL_LAYER = FIRST_AUTHORIZATION_LAYER + LAYER_BORDER;

    public static final int SECONDARY_AUTHORIZATION_LAYER = FREQUENCY_CONTROL_LAYER + LAYER_BORDER;
}
