package bo.dynamicworkflow.dynamicworkflowbackend.app.utilities;

import java.sql.Timestamp;
import java.util.Date;

public class TimeUtility {

    private TimeUtility() {
        throw new IllegalStateException("Can't instantiate this class");
    }

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(new Date().getTime());
    }

}