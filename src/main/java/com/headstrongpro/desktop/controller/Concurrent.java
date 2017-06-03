package com.headstrongpro.desktop.controller;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by rajmu on 17.05.23.
 */
@Target(ElementType.METHOD)
@Documented
public @interface Concurrent {
    String info() default "Time consuming operation";
}
