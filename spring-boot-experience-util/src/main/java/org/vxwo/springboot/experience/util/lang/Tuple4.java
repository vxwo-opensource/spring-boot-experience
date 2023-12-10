package org.vxwo.springboot.experience.util.lang;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author vxwo-team
 */

@Getter
@AllArgsConstructor
public class Tuple4<T1, T2, T3, T4> {
    private T1 t1;
    private T2 t2;
    private T3 t3;
    private T4 t4;

    public static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> of(T1 t1, T2 t2, T3 t3, T4 t4) {
        return new Tuple4<>(t1, t2, t3, t4);
    }
}
