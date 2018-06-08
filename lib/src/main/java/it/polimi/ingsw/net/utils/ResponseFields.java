package it.polimi.ingsw.net.utils;

public enum ResponseFields {
    RESPONSE("response"),
    HEADER("header"),
    BODY("body"),
    ERROR("error");

    private final String fieldName;

    ResponseFields(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String toString() {
        return this.fieldName;
    }

    public enum Body {
        BODY("body"),
        ENDPOINT("endpoint");

        private final String fieldName;

        Body(String fieldName) {
            this.fieldName = fieldName;
        }

        @Override
        public String toString() {
            return this.fieldName;
        }

        public enum Authentication {
            CHALLENGE("challenge"),
            SESSION_ID("session-id"),
            TOKEN("token"),
            CREATED("created");

            private final String fieldName;

            Authentication(String fieldName) {
                this.fieldName = fieldName;
            }

            @Override
            public String toString() {
                return fieldName;
            }
        }
    }

    public enum Error {
        ERROR(-1, "error"),
        UNAUTHORIZED(401, "unauthorized"),
        INTERNAL_SERVER_ERROR(500, "internal-server-error"),
        TIMEOUT(408, "request-timeout"),
        ALREADY_EXISTS(409, "already-exists");

        private final int code;
        private final String name;

        Error(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }
}
