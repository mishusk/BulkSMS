package bulksms.tdd.tddbulksms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by y34h1a on 3/13/17.
 */

public class ServerResponse {

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("error")
    @Expose
    private String error;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
