package br.com.meusaldomensal.infrastructure.security;

public class JwtValidationException extends RuntimeException {
    public JwtValidationException(String message) {
        super(message);
    }
}
