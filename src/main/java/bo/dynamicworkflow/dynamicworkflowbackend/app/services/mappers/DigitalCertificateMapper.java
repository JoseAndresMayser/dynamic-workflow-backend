package bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.DigitalCertificate;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.DigitalCertificateResponseDto;

public class DigitalCertificateMapper extends BaseMapper<Object, DigitalCertificateResponseDto, DigitalCertificate> {

    @Override
    public DigitalCertificate toEntity(Object request) {
        throw new UnsupportedOperationException("Function not supported.");
    }

    @Override
    public DigitalCertificateResponseDto toDto(DigitalCertificate digitalCertificate) {
        return new DigitalCertificateResponseDto(
                digitalCertificate.getId(),
                digitalCertificate.getPath(),
                digitalCertificate.getUploadTimestamp(),
                digitalCertificate.getModificationTimestamp(),
                digitalCertificate.getDepartmentMemberId()
        );
    }

}