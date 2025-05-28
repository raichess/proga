package system;


import models.LabWork;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    private String message = null;
    private LabWork labWork = null;

    private String key = null;
    private String name = null;
    private char[] passwd = null;

    public Request(String message, LabWork labWork, String key, String name, char[] passwd) {
        this.message = message;
        this.labWork = labWork;
        this.key = key;
        this.name = name;
        this.passwd = passwd;
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

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public char[] getPasswd() {
        return passwd;
    }
    public void setPasswd(char[] passwd) {
        this.passwd = passwd;
    }
}
