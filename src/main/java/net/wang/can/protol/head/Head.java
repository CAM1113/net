package net.wang.can.protol.head;

public class Head {
    private String status = "";
    private String message = "";
    private String contentType = "";
    private String other = "";

    public Head() {
    }

    public Head(String status, String message, String contentType, String other) {
        this.status = status;
        this.message = message;
        this.contentType = contentType;
        this.other = other;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
