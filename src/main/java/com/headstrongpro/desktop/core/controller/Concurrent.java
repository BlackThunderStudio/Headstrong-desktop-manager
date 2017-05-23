package com.headstrongpro.desktop.core.controller;

import java.lang.annotation.*;

/**
 * Created by rajmu on 17.05.23.
 */
@Target(ElementType.METHOD)
@Documented
public @interface Concurrent {
    String info() default "Time consuming operation";
}
