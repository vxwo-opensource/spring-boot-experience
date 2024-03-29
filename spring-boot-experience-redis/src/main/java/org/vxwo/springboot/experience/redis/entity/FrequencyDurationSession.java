package org.vxwo.springboot.experience.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author vxwo-team
 */

@Getter
@AllArgsConstructor
public class FrequencyDurationSession {
    private String frequencyKey;
    private String frequencyValue;
}
