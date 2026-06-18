package br.com.meusaldomensal.adapters.in.web;

import br.com.meusaldomensal.adapters.in.web.dto.SalaryRequest;
import br.com.meusaldomensal.adapters.in.web.dto.SalaryResponse;
import br.com.meusaldomensal.adapters.in.web.mapper.ApiMapper;
import br.com.meusaldomensal.application.ports.in.SalaryUseCase;
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
@RequestMapping("/api/v1/salaries")
public class SalaryController {

    private final SalaryUseCase salaryUseCase;

    public SalaryController(SalaryUseCase salaryUseCase) {
        this.salaryUseCase = salaryUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SalaryResponse create(@Valid @RequestBody SalaryRequest request) {
        return ApiMapper.toResponse(salaryUseCase.create(ApiMapper.toCommand(request)));
    }

    @GetMapping
    public List<SalaryResponse> findByMonthAndYear(@RequestParam Integer month, @RequestParam Integer year) {
        return salaryUseCase.findByMonthAndYear(month, year).stream()
                .map(ApiMapper::toResponse)
                .toList();
    }

    @PutMapping("/{id}")
    public SalaryResponse update(@PathVariable UUID id, @Valid @RequestBody SalaryRequest request) {
        return ApiMapper.toResponse(salaryUseCase.update(id, ApiMapper.toCommand(request)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        salaryUseCase.delete(id);
    }
}
