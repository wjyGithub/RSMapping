package com.wjy.rsmapping.constant;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by jianyuan.wei@hand-china.com
 * on 2019/4/25 0:46.
 */
public enum ClassMapEnum {


    STRING("STRING", String.class),
    LONG("LONG", Long.class),
    DEFAULT("OBJECT", Object.class);

    ClassMapEnum(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    private String name;

    private Class<?> clazz;

    public String getName() {
        return name;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public static final ClassMapEnum getClassMapEnum(String name) {
        try {
            String[] types = StringUtils.upperCase(name).split("\\.");
            return ClassMapEnum.valueOf(types[types.length -1]);
        }catch (Exception e) {
            return DEFAULT;
        }
    }
}
