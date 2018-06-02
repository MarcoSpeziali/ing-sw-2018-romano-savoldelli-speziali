package it.polimi.ingsw.net.utils;

public class RequestFields {
    private RequestFields() {}

    public enum Authentication {
        CHALLENGE_RESPONSE("challenge-response"),
        SESSION_ID("session-id"),
        USERNAME("username"),
        PASSWORD("password");

        private final String fieldName;

        Authentication(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFieldName() {
            return fieldName;
        }
    }
}
