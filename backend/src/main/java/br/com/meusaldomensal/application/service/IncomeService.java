package br.com.meusaldomensal.application.service;

import br.com.meusaldomensal.application.exception.NotFoundException;
import br.com.meusaldomensal.application.exception.ValidationException;
import br.com.meusaldomensal.application.ports.in.IncomeUseCase;
import br.com.meusaldomensal.application.ports.in.command.IncomeCommand;
import br.com.meusaldomensal.application.ports.out.IncomeRepositoryPort;
import br.com.meusaldomensal.domain.model.Income;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IncomeService implements IncomeUseCase {

    private final IncomeRepositoryPort incomeRepository;

    public IncomeService(IncomeRepositoryPort incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    @Override
    @Transactional
    public Income create(IncomeCommand command) {
        validate(command);
        LocalDateTime now = LocalDateTime.now();
        return incomeRepository.save(new Income(
                UUID.randomUUID(),
                command.description().trim(),
                command.amount(),
                command.incomeDate(),
                command.type(),
                command.month(),
                command.year(),
                now,
                now));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Income> findByMonthAndYear(Integer month, Integer year) {
        ValidationUtils.requireMonthYear(month, year);
        return incomeRepository.findByMonthAndYear(month, year).stream()
                .sorted(Comparator.comparing(Income::incomeDate).reversed())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Income findById(UUID id) {
        return incomeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Receita nao encontrada."));
    }

    @Override
    @Transactional
    public Income update(UUID id, IncomeCommand command) {
        validate(command);
        Income current = findById(id);
        return incomeRepository.save(new Income(
                current.id(),
                command.description().trim(),
                command.amount(),
                command.incomeDate(),
                command.type(),
                command.month(),
                command.year(),
                current.createdAt(),
                LocalDateTime.now()));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        findById(id);
        incomeRepository.deleteById(id);
    }

    private void validate(IncomeCommand command) {
        ValidationUtils.requireText(command.description(), "Descricao da receita");
        ValidationUtils.requirePositive(command.amount(), "Valor da receita");
        if (command.incomeDate() == null) {
            throw new ValidationException("Data da receita e obrigatoria.");
        }
        if (command.type() == null) {
            throw new ValidationException("Tipo da receita e obrigatorio.");
        }
        ValidationUtils.requireMonthYear(command.month(), command.year());
    }
}
