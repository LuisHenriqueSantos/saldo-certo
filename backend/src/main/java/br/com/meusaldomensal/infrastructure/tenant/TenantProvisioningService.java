package br.com.meusaldomensal.infrastructure.tenant;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Service;

@Service
public class TenantProvisioningService {

    private final DataSource dataSource;

    public TenantProvisioningService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void provision(String schemaName) {
        Flyway.configure()
                .dataSource(dataSource)
                .schemas(schemaName)
                .defaultSchema(schemaName)
                .locations("classpath:db/tenant_migration")
                .table("flyway_schema_history")
                .load()
                .migrate();
    }
}
