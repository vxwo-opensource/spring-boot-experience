package org.vxwo.springboot.experience.web.config;

import java.util.List;
import lombok.Data;

/**
* @author vxwo-team
*/

@Data
public class GroupPathRule {
    private String path;
    private String tag;
    private List<String> excludes;
    private List<String> optionals;
}
