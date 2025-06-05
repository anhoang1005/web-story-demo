package com.example.webstorydemo.model.payload;

import lombok.Getter;

@Getter
public class ResponseBody<T> {

    private int code;
    private String message;
    private T data;
    private int status;

    public ResponseBody(T data){
        this.data = data;
        this.status = Status.SUCCESS.value;
        this.message = Code.SUCCESS.message;
        this.code = Code.SUCCESS.value;
    }

    public ResponseBody(T data, Status status, Code code) {
        this.data = data;
        this.status = status.getValue();
        this.message = code.getMessage();
        this.code = code.getValue();
    }
    public ResponseBody(T data, Status status, String message, Code code) {
        this.data = data;
        this.status = status.getValue();
        this.message = message;
        this.code = code.getValue();
    }

    public enum Status {
        SUCCESS(1),
        FAILED(0);
        private final int value;
        Status(int value) {
            this.value = value;
        }
        public int getValue() {
            return this.value;
        }
    }

    public enum Code {
        SUCCESS(200, "SUCCESSFUL"),
        BAD_REQUEST(300,"BAD REQUEST"),
        CLIENT_ERROR(400, "CLIENT ERROR"),
        UNAUTHORIZED_REQUEST(401, "UNAUTHORIZED REQUEST"),
        FORBIDDEN(403, "FORBIDDEN"),
        NOT_FOUND(404, "NOT FOUND"),
        TOKEN_NOT_REGISTER(3001, "Token not register"),
        INVALID_REQUEST_FORMAT(4010, "Invalid request format"),
        INTERNAL_ERROR(500, "Internal server error");
        private final int value;
        private final String message;
        Code(int value, String message) {
            this.value = value;
            this.message = message;
        }
        public String getMessage() {
            return this.message;
        }
        public int getValue() {
            return this.value;
        }
    }
}
