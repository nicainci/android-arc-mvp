package com.lm.mvp.util;

/**
 * @Author: LM
 * @Date: 2018/3/19
 * @Description:
 */

public class Utils {
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        } else {
            return reference;
        }
    }
}
