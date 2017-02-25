package bulksms.tdd.tddbulksms.model;

/**
 * Created by ThirdEye-lll on 2/26/2017.
 */

public class InfoModel {
    private String id;
    private String name;
    private String message;

    public InfoModel(String id, String name, String message) {
        this.id = id;
        this.name = name;
        this.message = message;
    }

    public InfoModel(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
