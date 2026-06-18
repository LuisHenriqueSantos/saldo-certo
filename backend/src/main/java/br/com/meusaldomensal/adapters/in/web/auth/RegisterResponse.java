package br.com.meusaldomensal.adapters.in.web.auth;

import java.util.UUID;

public record RegisterResponse(String message, UUID userId, UUID tenantId) {
}
