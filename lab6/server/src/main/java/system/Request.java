package system;

import models.*;
import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    private String message;
    private LabWork labWork;
    private String key;

    public Request(String message, LabWork labWork, String key) {
        this.message = message;
        this.labWork = labWork;
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LabWork getLabWork() {
        return labWork;
    }

    public void setLabWork(LabWork labWork) {
        this.labWork = labWork;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
