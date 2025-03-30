package vn.edu.hcmute.utecare.util.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ValidSlotsValidator.class)
@Target({ElementType.TYPE}) // Applied to TYPE (class/interface)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSlots {
    String message() default "The number of booked slots cannot exceed the maximum slots";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}