package ru.evgenii.mapper.reflection.annotations;

import java.lang.annotation.*;

@Retention(value= RetentionPolicy.RUNTIME)
public @interface JSONField {
    String name();

    boolean required() default true;
}
