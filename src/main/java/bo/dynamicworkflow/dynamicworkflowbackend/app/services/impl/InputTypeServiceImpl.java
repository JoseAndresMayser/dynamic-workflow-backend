package bo.dynamicworkflow.dynamicworkflowbackend.app.services.impl;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.InputType;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Restriction;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.RestrictionDefinedValue;
import bo.dynamicworkflow.dynamicworkflowbackend.app.repositories.InputTypeRepository;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.InputTypeService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.InputTypeDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.RestrictionDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers.InputTypeMapper;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers.RestrictionDefinedValueMapper;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers.RestrictionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InputTypeServiceImpl implements InputTypeService {

    private final InputTypeRepository inputTypeRepository;

    private final InputTypeMapper inputTypeMapper = new InputTypeMapper();
    private final RestrictionMapper restrictionMapper = new RestrictionMapper();
    private final RestrictionDefinedValueMapper restrictionDefinedValueMapper = new RestrictionDefinedValueMapper();

    @Autowired
    public InputTypeServiceImpl(InputTypeRepository inputTypeRepository) {
        this.inputTypeRepository = inputTypeRepository;
    }

    @Override
    public List<InputTypeDto> getAllInputTypes() {
        List<InputType> inputTypes = inputTypeRepository.findAll();
        List<InputTypeDto> inputTypeDtoList = new ArrayList<>();
        inputTypes.forEach(inputType -> {
            InputTypeDto inputTypeDto = inputTypeMapper.toDto(inputType);
            List<Restriction> restrictions = inputType.getRestrictions();
            if (restrictions != null && !restrictions.isEmpty()) {
                List<RestrictionDto> restrictionDtoList = new ArrayList<>();
                restrictions.forEach(restriction -> {
                    RestrictionDto restrictionDto = restrictionMapper.toDto(restriction);
                    List<RestrictionDefinedValue> restrictionDefinedValues = restriction.getRestrictionDefinedValues();
                    if (restrictionDefinedValues != null && !restrictionDefinedValues.isEmpty())
                        restrictionDto.setDefinedValues(restrictionDefinedValueMapper.toDto(restrictionDefinedValues));
                    restrictionDtoList.add(restrictionDto);
                });
                inputTypeDto.setRestrictions(restrictionDtoList);
            }
            inputTypeDtoList.add(inputTypeDto);
        });
        return inputTypeDtoList;
    }

}