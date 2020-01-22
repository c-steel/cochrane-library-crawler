package io.rosensteel.HTTP;

public class WebResult {
    private boolean isSuccess;
    private String body;

    public WebResult(boolean isSuccess, String body) {
        this.isSuccess = isSuccess;
        this.body = body;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getBody() {
        return body;
    }
}
