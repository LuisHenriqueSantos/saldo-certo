package br.com.meusaldomensal.application.service;

import br.com.meusaldomensal.application.exception.NotFoundException;
import br.com.meusaldomensal.application.exception.ValidationException;
import br.com.meusaldomensal.application.ports.in.SalaryUseCase;
import br.com.meusaldomensal.application.ports.in.command.SalaryCommand;
import br.com.meusaldomensal.application.ports.out.SalaryRepositoryPort;
import br.com.meusaldomensal.domain.model.Salary;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SalaryService implements SalaryUseCase {

    private final SalaryRepositoryPort salaryRepository;

    public SalaryService(SalaryRepositoryPort salaryRepository) {
        this.salaryRepository = salaryRepository;
    }

    @Override
    @Transactional
    public Salary create(SalaryCommand command) {
        validate(command);
        LocalDateTime now = LocalDateTime.now();
        return salaryRepository.save(new Salary(
                UUID.randomUUID(),
                command.description().trim(),
                command.amount(),
                command.receivedDate(),
                command.month(),
                command.year(),
                now,
                now));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Salary> findByMonthAndYear(Integer month, Integer year) {
        ValidationUtils.requireMonthYear(month, year);
        return salaryRepository.findByMonthAndYear(month, year).stream()
                .sorted(Comparator.comparing(Salary::receivedDate).reversed())
                .toList();
    }

    @Override
    @Transactional
    public Salary update(UUID id, SalaryCommand command) {
        validate(command);
        Salary current = salaryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Salario nao encontrado."));
        return salaryRepository.save(new Salary(
                current.id(),
                command.description().trim(),
                command.amount(),
                command.receivedDate(),
                command.month(),
                command.year(),
                current.createdAt(),
                LocalDateTime.now()));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        salaryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Salario nao encontrado."));
        salaryRepository.deleteById(id);
    }

    private void validate(SalaryCommand command) {
        ValidationUtils.requireText(command.description(), "Descricao do salario");
        ValidationUtils.requirePositive(command.amount(), "Valor do salario");
        if (command.receivedDate() == null) {
            throw new ValidationException("Data de recebimento e obrigatoria.");
        }
        ValidationUtils.requireMonthYear(command.month(), command.year());
    }
}
