package org.vxwo.springboot.experience.mybatis.tester;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KvEntity {
    private String key;
    private String value;
}
