package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.PaymentRequest;
import vn.edu.hcmute.utecare.dto.response.PaymentAppointmentResponse;
import vn.edu.hcmute.utecare.dto.response.PaymentResponse;
import vn.edu.hcmute.utecare.model.Payment;

import java.util.Map;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses = {AppointmentMapper.class})
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);
    Payment toEntity(PaymentRequest paymentRequest);

    @Mapping(target = "appointmentId", source = "appointment.id")
    PaymentResponse toResponse(Payment payment);

    @Mapping(target = "appointment", source = "appointment")
    PaymentAppointmentResponse toPaymentAppointmentResponse(Payment payment);
}
