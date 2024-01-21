package org.vxwo.springboot.experience.util.lang;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author vxwo-team
 */

@Getter
@AllArgsConstructor
public class Tuple6<T1, T2, T3, T4, T5, T6> {
    private T1 t1;
    private T2 t2;
    private T3 t3;
    private T4 t4;
    private T5 t5;
    private T6 t6;

    public static <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> of(T1 t1, T2 t2, T3 t3,
            T4 t4, T5 t5, T6 t6) {
        return new Tuple6<>(t1, t2, t3, t4, t5, t6);
    }
}
