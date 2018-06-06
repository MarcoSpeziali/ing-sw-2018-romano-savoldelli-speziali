package it.polimi.ingsw.net.utils;

public enum RequestFields {
    REQUEST("request");

    private final String fieldName;

    RequestFields(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String toString() {
        return this.fieldName;
    }

    public enum Header {
        HEADER("header"),
        CLIENT_TOKEN("client-token");

        private final String fieldName;

        Header(String fieldName) {
            this.fieldName = fieldName;
        }

        @Override
        public String toString() {
            return this.fieldName;
        }
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
            CHALLENGE_RESPONSE("challenge-response"),
            SESSION_ID("session-id"),
            USERNAME("username"),
            PASSWORD("password");

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
}
