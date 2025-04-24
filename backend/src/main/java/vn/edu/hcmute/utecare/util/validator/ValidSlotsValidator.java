package vn.edu.hcmute.utecare.util.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.edu.hcmute.utecare.dto.request.ScheduleRequest; // Import DTO cần validate


public class ValidSlotsValidator implements ConstraintValidator<ValidSlots, ScheduleRequest> {

    @Override
    public void initialize(ValidSlots constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ScheduleRequest request, ConstraintValidatorContext context) {
        if (request.getMaxSlots() == null || request.getBookedSlots() == null) {
            return true;
        }
        return request.getBookedSlots() <= request.getMaxSlots();
    }
}