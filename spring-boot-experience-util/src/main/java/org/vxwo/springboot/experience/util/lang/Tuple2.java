package org.vxwo.springboot.experience.util.lang;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author vxwo-team
 */

@Getter
@AllArgsConstructor
public class Tuple2<T1, T2> {
    private T1 t1;
    private T2 t2;

    public static <T1, T2> Tuple2<T1, T2> of(T1 t1, T2 t2) {
        return new Tuple2<>(t1, t2);
    }
}
