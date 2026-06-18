package br.com.meusaldomensal.adapters.in.web.auth;

public record LoginResponse(
        String accessToken,
        String tokenType,
        AuthUserResponse user,
        AuthTenantResponse tenant) {
}
