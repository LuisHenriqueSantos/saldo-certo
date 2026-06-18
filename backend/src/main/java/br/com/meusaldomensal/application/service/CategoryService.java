package br.com.meusaldomensal.application.service;

import br.com.meusaldomensal.application.exception.NotFoundException;
import br.com.meusaldomensal.application.ports.in.CategoryUseCase;
import br.com.meusaldomensal.application.ports.in.command.CategoryCommand;
import br.com.meusaldomensal.application.ports.out.CategoryRepositoryPort;
import br.com.meusaldomensal.domain.model.Category;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService implements CategoryUseCase {

    private final CategoryRepositoryPort categoryRepository;

    public CategoryService(CategoryRepositoryPort categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Category create(CategoryCommand command) {
        validate(command);
        LocalDateTime now = LocalDateTime.now();
        return categoryRepository.save(new Category(
                UUID.randomUUID(),
                command.name().trim(),
                command.type(),
                command.color().trim(),
                command.icon().trim(),
                now,
                now));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll().stream()
                .sorted(Comparator.comparing(Category::name))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Category findById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria nao encontrada."));
    }

    @Override
    @Transactional
    public Category update(UUID id, CategoryCommand command) {
        validate(command);
        Category current = findById(id);
        return categoryRepository.save(new Category(
                current.id(),
                command.name().trim(),
                command.type(),
                command.color().trim(),
                command.icon().trim(),
                current.createdAt(),
                LocalDateTime.now()));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        findById(id);
        categoryRepository.deleteById(id);
    }

    private void validate(CategoryCommand command) {
        ValidationUtils.requireText(command.name(), "Nome da categoria");
        if (command.type() == null) {
            throw new br.com.meusaldomensal.application.exception.ValidationException("Tipo da categoria e obrigatorio.");
        }
        ValidationUtils.requireText(command.color(), "Cor da categoria");
        ValidationUtils.requireText(command.icon(), "Icone da categoria");
    }
}
