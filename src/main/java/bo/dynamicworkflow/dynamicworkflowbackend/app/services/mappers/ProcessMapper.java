package bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Process;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.requests.ProcessRequestDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.ProcessResponseDto;

public class ProcessMapper extends BaseMapper<ProcessRequestDto, ProcessResponseDto, Process> {

    @Override
    public Process toEntity(ProcessRequestDto request) {
        Process process = new Process();
        process.setName(request.getName());
        process.setDescription(request.getDescription());
        process.setStatus(request.getStatus());
        process.setDepartmentId(request.getDepartmentId());
        return process;
    }

    @Override
    public ProcessResponseDto toDto(Process process) {
        return new ProcessResponseDto(
                process.getId(),
                process.getName(),
                process.getDescription(),
                process.getCreationTimestamp(),
                process.getModificationTimestamp(),
                process.getStatus(),
                process.getUserId(),
                process.getDepartmentId()
        );
    }

}