package bo.dynamicworkflow.dynamicworkflowbackend.app.services.impl;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Action;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.ActionCode;
import bo.dynamicworkflow.dynamicworkflowbackend.app.repositories.ActionRepository;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.ActionService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.ActionDto;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.mappers.ActionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActionServiceImpl implements ActionService {

    private final ActionRepository actionRepository;

    private final ActionMapper actionMapper = new ActionMapper();

    @Autowired
    public ActionServiceImpl(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    @Override
    public List<ActionDto> getAllActions() {
        List<Action> actions = actionRepository.findAll();
        purgeActionsWithoutAuth(actions);
        return actionMapper.toDto(actions);
    }

    private void purgeActionsWithoutAuth(List<Action> actions) {
        List<ActionCode> actionsCodeWithoutAuth = ActionCode.getActionsCodeWithoutAuth();
        actions.removeIf(action -> actionsCodeWithoutAuth.contains(action.getCode()));
    }

}