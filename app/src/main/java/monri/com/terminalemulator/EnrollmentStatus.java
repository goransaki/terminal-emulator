package monri.com.terminalemulator;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jasminsuljic on 30/06/2018.
 * TerminalEmulator
 */

class EnrollmentStatus {
    /**
     * Enrollment status is currently: enrolled, not_enrolled.
     * <p>
     * With keeping this as string value, not boolean we ensure ease of adding new statuses based on business requirements.
     */
    @SerializedName("status")
    String status;
    /**
     * UserId for enrolled user, can be whatever our or 3rd party system sends back to us.
     */
    @SerializedName("user_id")
    @Nullable
    String userId;

    public EnrollmentStatus(String status, @Nullable String userId) {
        this.status = status;
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    @Nullable
    public String getUserId() {
        return userId;
    }
}
