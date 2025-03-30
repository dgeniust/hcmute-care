package vn.edu.hcmute.utecare.util.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.edu.hcmute.utecare.dto.request.DoctorScheduleRequest; // Import DTO cần validate


public class ValidSlotsValidator implements ConstraintValidator<ValidSlots, DoctorScheduleRequest> {

    @Override
    public void initialize(ValidSlots constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(DoctorScheduleRequest request, ConstraintValidatorContext context) {
        if (request.getMaxSlots() == null || request.getBookedSlots() == null) {
            return true;
        }
        return request.getBookedSlots() <= request.getMaxSlots();
    }
}