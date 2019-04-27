package com.wjy.rsmapping.annotation;

import java.lang.annotation.*;

/**
 * Created by jianyuan.wei@hand-china.com
 * on 2019/4/20 23:43.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD})
public @interface RSMap {

    /**
     * 默认为三列映射
     * @return
     */
    int num() default 3;
}
