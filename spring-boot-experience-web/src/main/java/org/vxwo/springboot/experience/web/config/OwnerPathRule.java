package org.vxwo.springboot.experience.web.config;

import java.util.List;
import lombok.Data;

/**
* @author vxwo-team
*/

@Data
public class OwnerPathRule {

    @Data
    public static class KeyOwner {
        private String key;
        private String owner;
    }

    private String path;
    private String tag;
    private List<KeyOwner> owners;
}
