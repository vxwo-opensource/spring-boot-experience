package org.vxwo.springboot.experience.util.lang;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Tuple5<T1, T2, T3, T4, T5> {
    private T1 t1;
    private T2 t2;
    private T3 t3;
    private T4 t4;
    private T5 t5;

    public static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> of(T1 t1, T2 t2, T3 t3, T4 t4,
            T5 t5) {
        return new Tuple5<>(t1, t2, t3, t4, t5);
    }
}
