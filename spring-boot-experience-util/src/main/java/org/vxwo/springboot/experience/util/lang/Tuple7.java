package org.vxwo.springboot.experience.util.lang;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author vxwo-team
 */

@Getter
@AllArgsConstructor
public class Tuple7<T1, T2, T3, T4, T5, T6, T7> {
    private T1 t1;
    private T2 t2;
    private T3 t3;
    private T4 t4;
    private T5 t5;
    private T6 t6;
    private T7 t7;

    public static <T1, T2, T3, T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> of(T1 t1, T2 t2,
            T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        return new Tuple7<>(t1, t2, t3, t4, t5, t6, t7);
    }
}
