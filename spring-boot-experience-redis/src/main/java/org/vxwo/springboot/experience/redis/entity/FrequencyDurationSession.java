package org.vxwo.springboot.experience.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author vxwo-team
 */

@Data
@AllArgsConstructor
public class FrequencyDurationSession {
    private String frequencyKey;
    private String frequencyValue;
}
