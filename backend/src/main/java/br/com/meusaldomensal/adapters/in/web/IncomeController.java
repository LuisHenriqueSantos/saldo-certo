package br.com.meusaldomensal.adapters.in.web;

import br.com.meusaldomensal.adapters.in.web.dto.IncomeRequest;
import br.com.meusaldomensal.adapters.in.web.dto.IncomeResponse;
import br.com.meusaldomensal.adapters.in.web.mapper.ApiMapper;
import br.com.meusaldomensal.application.ports.in.IncomeUseCase;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/incomes")
public class IncomeController {

    private final IncomeUseCase incomeUseCase;

    public IncomeController(IncomeUseCase incomeUseCase) {
        this.incomeUseCase = incomeUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IncomeResponse create(@Valid @RequestBody IncomeRequest request) {
        return ApiMapper.toResponse(incomeUseCase.create(ApiMapper.toCommand(request)));
    }

    @GetMapping
    public List<IncomeResponse> findByMonthAndYear(@RequestParam Integer month, @RequestParam Integer year) {
        return incomeUseCase.findByMonthAndYear(month, year).stream()
                .map(ApiMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public IncomeResponse findById(@PathVariable UUID id) {
        return ApiMapper.toResponse(incomeUseCase.findById(id));
    }

    @PutMapping("/{id}")
    public IncomeResponse update(@PathVariable UUID id, @Valid @RequestBody IncomeRequest request) {
        return ApiMapper.toResponse(incomeUseCase.update(id, ApiMapper.toCommand(request)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        incomeUseCase.delete(id);
    }
}
