package br.com.meusaldomensal.application.service.auth;

import br.com.meusaldomensal.adapters.in.web.auth.AuthTenantResponse;
import br.com.meusaldomensal.adapters.in.web.auth.AuthUserResponse;
import br.com.meusaldomensal.adapters.in.web.auth.LoginRequest;
import br.com.meusaldomensal.adapters.in.web.auth.LoginResponse;
import br.com.meusaldomensal.adapters.in.web.auth.MeResponse;
import br.com.meusaldomensal.adapters.in.web.auth.RegisterRequest;
import br.com.meusaldomensal.adapters.in.web.auth.RegisterResponse;
import br.com.meusaldomensal.adapters.out.persistence.entity.auth.TenantAccountJpaEntity;
import br.com.meusaldomensal.adapters.out.persistence.entity.auth.UserJpaEntity;
import br.com.meusaldomensal.adapters.out.persistence.entity.auth.UserTenantJpaEntity;
import br.com.meusaldomensal.adapters.out.persistence.repository.auth.TenantAccountJpaRepository;
import br.com.meusaldomensal.adapters.out.persistence.repository.auth.UserJpaRepository;
import br.com.meusaldomensal.adapters.out.persistence.repository.auth.UserTenantJpaRepository;
import br.com.meusaldomensal.application.exception.ValidationException;
import br.com.meusaldomensal.infrastructure.security.AuthenticatedUser;
import br.com.meusaldomensal.infrastructure.security.JwtService;
import br.com.meusaldomensal.infrastructure.tenant.SchemaNameGenerator;
import br.com.meusaldomensal.infrastructure.tenant.TenantProvisioningService;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final String ROLE_OWNER = "OWNER";
    private static final String STATUS_ACTIVE = "ACTIVE";

    private final UserJpaRepository userRepository;
    private final TenantAccountJpaRepository tenantRepository;
    private final UserTenantJpaRepository userTenantRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SchemaNameGenerator schemaNameGenerator;
    private final TenantProvisioningService tenantProvisioningService;

    public AuthService(
            UserJpaRepository userRepository,
            TenantAccountJpaRepository tenantRepository,
            UserTenantJpaRepository userTenantRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            SchemaNameGenerator schemaNameGenerator,
            TenantProvisioningService tenantProvisioningService) {
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.userTenantRepository = userTenantRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.schemaNameGenerator = schemaNameGenerator;
        this.tenantProvisioningService = tenantProvisioningService;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (!request.password().equals(request.confirmPassword())) {
            throw new ValidationException("As senhas não conferem.");
        }

        String email = normalizeEmail(request.email());
        if (userRepository.existsByEmail(email)) {
            throw new ValidationException("E-mail já cadastrado.");
        }

        UUID userId = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        String schemaName = schemaNameGenerator.generate(request.accountName(), tenantId);
        tenantProvisioningService.provision(schemaName);

        LocalDateTime now = LocalDateTime.now();
        UserJpaEntity user = new UserJpaEntity();
        user.setId(userId);
        user.setName(request.name().trim());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setActive(true);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        userRepository.save(user);

        TenantAccountJpaEntity tenant = new TenantAccountJpaEntity();
        tenant.setId(tenantId);
        tenant.setOwnerUserId(userId);
        tenant.setAccountName(request.accountName().trim());
        tenant.setSchemaName(schemaName);
        tenant.setStatus(STATUS_ACTIVE);
        tenant.setCreatedAt(now);
        tenant.setUpdatedAt(now);
        tenantRepository.save(tenant);

        UserTenantJpaEntity userTenant = new UserTenantJpaEntity();
        userTenant.setId(UUID.randomUUID());
        userTenant.setUserId(userId);
        userTenant.setTenantId(tenantId);
        userTenant.setRole(ROLE_OWNER);
        userTenant.setCreatedAt(now);
        userTenantRepository.save(userTenant);

        return new RegisterResponse("Conta criada com sucesso.", userId, tenantId);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        String email = normalizeEmail(request.email());
        UserJpaEntity user = userRepository.findByEmail(email)
                .filter(UserJpaEntity::getActive)
                .orElseThrow(() -> new BadCredentialsException("E-mail ou senha inválidos."));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("E-mail ou senha inválidos.");
        }

        TenantAccountJpaEntity tenant = tenantRepository.findByOwnerUserId(user.getId())
                .filter(account -> STATUS_ACTIVE.equals(account.getStatus()))
                .orElseThrow(() -> new BadCredentialsException("E-mail ou senha inválidos."));
        UserTenantJpaEntity userTenant = userTenantRepository.findByUserIdAndTenantId(user.getId(), tenant.getId())
                .orElseThrow(() -> new BadCredentialsException("E-mail ou senha inválidos."));

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                user.getId(),
                user.getName(),
                user.getEmail(),
                tenant.getId(),
                tenant.getAccountName(),
                tenant.getSchemaName(),
                userTenant.getRole());

        return new LoginResponse(
                jwtService.generateToken(authenticatedUser),
                "Bearer",
                new AuthUserResponse(user.getId(), user.getName(), user.getEmail()),
                new AuthTenantResponse(tenant.getId(), tenant.getAccountName()));
    }

    public MeResponse me(AuthenticatedUser user) {
        return new MeResponse(
                new AuthUserResponse(user.userId(), user.name(), user.email()),
                new AuthTenantResponse(user.tenantId(), user.accountName()));
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
