package com.example.backend.database;

public class AppSqlException extends RuntimeException {

    private static final long serialVersionUID = -3561672956921806857L;
    private String appFullMessage = null;

    public AppSqlException(String message) {
        this(new Exception(message));
    }

    public AppSqlException(Exception exception) {
        super(exception.getMessage(), exception.getCause());
    }

    public String getAppFullMessage() {
        Throwable throwable = this.getCause();
        if (throwable != null) {
            if (throwable instanceof java.sql.SQLException) {
                String[] msgLines = throwable.getMessage().split("\n");
                if (msgLines.length > 0)
                    appFullMessage = msgLines[0];
                if (msgLines.length > 1)
                    if (msgLines[1].startsWith("PLS-"))
                        appFullMessage = appFullMessage.trim() + " " + msgLines[1];
            } else {
                appFullMessage = this.getMessage();
            }
        } else {
            appFullMessage = this.getMessage();
        }
        return appFullMessage;
    }

    public String getAppMessage() {
        String appMessage = this.getAppFullMessage();
        if (appMessage != null) {
            if (appMessage.contains(":"))
                if (appMessage.length() > appMessage.indexOf(":") + 1)
                    return appMessage.substring(appMessage.indexOf(":") + 1).trim();
        }
        return appMessage;
    }

    public String getAppMessageCode() {
        String appMessage = this.getAppFullMessage();
        if (appMessage != null) {
            if (appMessage.contains(":"))
                return appMessage.substring(0, appMessage.indexOf(":")).trim();
        }
        return appMessage;
    }
}
