package br.com.meusaldomensal.infrastructure.tenant;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.sql.Statement;
import java.util.regex.Pattern;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Component
public class TenantSchemaGuard {

    private static final Pattern SAFE_SCHEMA = Pattern.compile("tenant_[a-z0-9_]{1,56}");

    @PersistenceContext
    private EntityManager entityManager;

    public void applyCurrentTenant() {
        String schemaName = TenantContext.getSchemaName()
                .orElseThrow(() -> new IllegalStateException("Tenant nao identificado."));
        if (!SAFE_SCHEMA.matcher(schemaName).matches()) {
            throw new IllegalStateException("Tenant invalido.");
        }
        entityManager.unwrap(Session.class).doWork(connection -> {
            try (Statement statement = connection.createStatement()) {
                statement.execute("SET LOCAL search_path TO " + schemaName + ", public");
            }
        });
    }
}
