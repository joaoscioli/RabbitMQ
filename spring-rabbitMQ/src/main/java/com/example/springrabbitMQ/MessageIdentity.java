package com.example.springrabbitMQ;

final class MessageIdentity {
    private MessageIdentity() { }

    static boolean isValid(String value) {
        return value != null && value.length() <= 128 && value.matches("[A-Za-z0-9._:-]+");
    }
}
