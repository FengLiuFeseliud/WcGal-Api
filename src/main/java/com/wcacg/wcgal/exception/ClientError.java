package com.wcacg.wcgal.exception;

public class ClientError {

    public static class NotTokenException extends RuntimeException{
        private final String message;

        public NotTokenException(String message){
            super(message);
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }


    public static class NotPermissionsException extends RuntimeException{
        private final String message;

        public NotPermissionsException(String message){
            super(message);
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class HaveExistedException extends RuntimeException{
        private final String message;

        public HaveExistedException(String message){
            super(message);
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class NotFindException extends RuntimeException{
        private final String message;

        public NotFindException(String message){
            super(message);
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class MaxCreateException extends RuntimeException{
        private final String message;

        public MaxCreateException(String message){
            super(message);
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
