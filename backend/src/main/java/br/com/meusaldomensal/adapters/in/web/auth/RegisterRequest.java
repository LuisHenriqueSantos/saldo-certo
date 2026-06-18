package br.com.meusaldomensal.adapters.in.web.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Nome e obrigatorio.") String name,
        @NotBlank(message = "E-mail e obrigatorio.") @Email(message = "E-mail invalido.") String email,
        @NotBlank(message = "Senha e obrigatoria.") @Size(min = 8, message = "Senha deve ter pelo menos 8 caracteres.") String password,
        @NotBlank(message = "Confirmacao de senha e obrigatoria.") String confirmPassword,
        @NotBlank(message = "Nome da conta financeira e obrigatorio.") String accountName) {
}
