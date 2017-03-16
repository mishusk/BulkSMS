package bulksms.tdd.tddbulksms.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by ThirdEye-lll on 2/26/2017.
 */

public class InfoModel implements Parcelable {
    private int id;
    private String phoneNumber;
    private String operatorName;
    private String status;
    private String message;
    private String sendTime;

    private InfoModel(Parcel in) {
        id = in.readInt();
        phoneNumber = in.readString();
        operatorName = in.readString();
    }

    public InfoModel() {

    }

    public InfoModel(String phoneNumber, String operatorName) {
        this.phoneNumber = phoneNumber;
        this.operatorName = operatorName;
    }

    public InfoModel(String phoneNumber, String message, String status, String sendTime) {
        this.phoneNumber = phoneNumber;
        this.message = message;
        this.status = status;
        this.sendTime = sendTime;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public static final Creator<InfoModel> CREATOR = new Creator<InfoModel>() {
        @Override
        public InfoModel createFromParcel(Parcel in) {
            return new InfoModel(in);
        }

        @Override
        public InfoModel[] newArray(int size) {
            return new InfoModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(phoneNumber);
        dest.writeString(operatorName);
        dest.writeString(message);
        dest.writeString(sendTime);
    }
}
