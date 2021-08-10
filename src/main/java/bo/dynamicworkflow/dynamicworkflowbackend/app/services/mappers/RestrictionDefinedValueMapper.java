package bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.RestrictionDefinedValue;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.RestrictionDefinedValueDto;

public class RestrictionDefinedValueMapper
        extends BaseMapper<RestrictionDefinedValueDto, RestrictionDefinedValueDto, RestrictionDefinedValue> {

    @Override
    public RestrictionDefinedValue toEntity(RestrictionDefinedValueDto request) {
        throw new UnsupportedOperationException("Function not supported.");
    }

    @Override
    public RestrictionDefinedValueDto toDto(RestrictionDefinedValue restrictionDefinedValue) {
        return new RestrictionDefinedValueDto(
                restrictionDefinedValue.getId(),
                restrictionDefinedValue.getValue(),
                restrictionDefinedValue.getRestrictionId()
        );
    }

}