package br.com.meusaldomensal.infrastructure.tenant;

import br.com.meusaldomensal.infrastructure.security.AuthenticatedUser;
import java.util.Optional;

public final class TenantContext {

    private static final ThreadLocal<AuthenticatedUser> CURRENT_USER = new ThreadLocal<>();

    private TenantContext() {
    }

    public static void set(AuthenticatedUser user) {
        CURRENT_USER.set(user);
    }

    public static Optional<AuthenticatedUser> getUser() {
        return Optional.ofNullable(CURRENT_USER.get());
    }

    public static Optional<String> getSchemaName() {
        return getUser().map(AuthenticatedUser::schemaName);
    }

    public static void clear() {
        CURRENT_USER.remove();
    }
}
