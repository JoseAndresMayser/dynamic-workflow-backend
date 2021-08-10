package bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Restriction;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.RestrictionDto;

public class RestrictionMapper extends BaseMapper<RestrictionDto, RestrictionDto, Restriction> {

    @Override
    public Restriction toEntity(RestrictionDto request) {
        throw new UnsupportedOperationException("Function not supported.");
    }

    @Override
    public RestrictionDto toDto(Restriction restriction) {
        RestrictionDto restrictionDto = new RestrictionDto();
        restrictionDto.setId(restriction.getId());
        restrictionDto.setName(restriction.getName());
        restrictionDto.setHasDefinedValues(restriction.getHasDefinedValues());
        restrictionDto.setInputTypeId(restriction.getInputTypeId());
        return restrictionDto;
    }

}