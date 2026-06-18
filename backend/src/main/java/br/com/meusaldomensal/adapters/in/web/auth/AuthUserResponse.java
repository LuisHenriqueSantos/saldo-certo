package br.com.meusaldomensal.adapters.in.web.auth;

import java.util.UUID;

public record AuthUserResponse(UUID id, String name, String email) {
}
