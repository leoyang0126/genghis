package com.leoyang.genghis.server.annotations;

import java.lang.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yang.liu on 2018/11/15
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {
     String value() default "";
}
