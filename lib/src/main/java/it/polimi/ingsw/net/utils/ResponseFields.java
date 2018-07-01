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
        ENDPOINT("endpoint"),
        CLASS_TYPE("class-type");

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
        UNAUTHORIZED(1, "unauthorized"),
        INTERNAL_SERVER_ERROR(2, "internal-server-error"),
        TIMEOUT(3, "request-timeout"),
        ALREADY_EXISTS(4, "already-exists"),
        NOT_ENOUGH_TOKENS(5, "not-enough-tokens"),
        CONSTRAINT_EVALUATION(6, "constraint-evaluation");

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
