package it.polimi.ingsw.net.utils;

public enum RequestFields {
    REQUEST("request"),
    HEADER("header"),
    BODY("body");

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
        CLIENT_TOKEN("client-token"),
        ENDPOINT("endpoint");

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
        CLASS_TYPE("class-type");

        private final String fieldName;

        Body(String fieldName) {
            this.fieldName = fieldName;
        }

        @Override
        public String toString() {
            return this.fieldName;
        }

        /*public enum Authentication {
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
        }*/
    }
}
