package com.tma.vlhau.ecommercecommon.entity.validate.Image;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ImageFileValidator.class})
public @interface ValidImage {
    String message() default "Allowed mime types are image/png, image/jpeg, image/jpg";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}