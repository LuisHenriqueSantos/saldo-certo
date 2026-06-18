package br.com.meusaldomensal.infrastructure.tenant;

import java.text.Normalizer;
import java.util.Locale;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class SchemaNameGenerator {

    public String generate(String accountName, UUID tenantId) {
        String base = Normalizer.normalize(accountName == null ? "conta" : accountName, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_+|_+$", "");
        if (base.isBlank()) {
            base = "conta";
        }
        if (base.length() > 36) {
            base = base.substring(0, 36).replaceAll("_+$", "");
        }
        return "tenant_" + base + "_" + tenantId.toString().substring(0, 8).replace("-", "");
    }
}
