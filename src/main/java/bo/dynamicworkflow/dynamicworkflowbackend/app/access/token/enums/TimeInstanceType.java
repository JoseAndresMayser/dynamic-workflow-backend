package bo.dynamicworkflow.dynamicworkflowbackend.app.access.token.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Calendar;

@AllArgsConstructor
public enum TimeInstanceType {

    MINUTE(Calendar.MINUTE),
    HOUR(Calendar.HOUR),
    DAY(Calendar.DAY_OF_MONTH),
    MONTH(Calendar.MONTH),
    YEAR(Calendar.YEAR);

    @Getter
    private final Integer value;

}