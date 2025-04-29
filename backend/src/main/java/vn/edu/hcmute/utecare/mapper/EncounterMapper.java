package vn.edu.hcmute.utecare.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import vn.edu.hcmute.utecare.dto.request.EncounterRequest;
import vn.edu.hcmute.utecare.dto.response.EncounterResponse;
import vn.edu.hcmute.utecare.model.Encounter;
import vn.edu.hcmute.utecare.model.Prescription;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EncounterMapper {

    EncounterMapper INSTANCE = Mappers.getMapper(EncounterMapper.class);

    Encounter toEntity(EncounterRequest request);

    @Mapping(target = "medicalRecordId", source = "medicalRecord.id")
    @Mapping(target = "prescriptionId", source = "prescriptions", qualifiedByName = "mapPrescriptionIds")
    EncounterResponse toResponse(Encounter encounter);

    void update(EncounterRequest request, @MappingTarget Encounter encounter);

    @Named("mapPrescriptionIds")
    default List<Long> mapPrescriptionIds(Collection<Prescription> prescriptions) {
        if(prescriptions == null || prescriptions.isEmpty())
            return null;
        return prescriptions.stream().map(Prescription::getId).collect(Collectors.toList());
    }
}
