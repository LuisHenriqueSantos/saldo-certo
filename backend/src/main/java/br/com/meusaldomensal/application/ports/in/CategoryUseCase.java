package br.com.meusaldomensal.application.ports.in;

import br.com.meusaldomensal.application.ports.in.command.CategoryCommand;
import br.com.meusaldomensal.domain.model.Category;
import java.util.List;
import java.util.UUID;

public interface CategoryUseCase {
    Category create(CategoryCommand command);
    List<Category> findAll();
    Category findById(UUID id);
    Category update(UUID id, CategoryCommand command);
    void delete(UUID id);
}
