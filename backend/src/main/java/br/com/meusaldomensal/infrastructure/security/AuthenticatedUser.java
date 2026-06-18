package br.com.meusaldomensal.infrastructure.security;

import java.util.UUID;

public record AuthenticatedUser(
        UUID userId,
        String name,
        String email,
        UUID tenantId,
        String accountName,
        String schemaName,
        String role) {
}
