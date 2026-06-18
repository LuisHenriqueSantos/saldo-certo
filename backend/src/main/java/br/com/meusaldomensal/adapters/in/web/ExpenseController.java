package br.com.meusaldomensal.adapters.in.web;

import br.com.meusaldomensal.adapters.in.web.dto.ExpenseRequest;
import br.com.meusaldomensal.adapters.in.web.dto.ExpenseResponse;
import br.com.meusaldomensal.adapters.in.web.dto.PayExpenseRequest;
import br.com.meusaldomensal.adapters.in.web.mapper.ApiMapper;
import br.com.meusaldomensal.application.ports.in.ExpenseUseCase;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private final ExpenseUseCase expenseUseCase;

    public ExpenseController(ExpenseUseCase expenseUseCase) {
        this.expenseUseCase = expenseUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseResponse create(@Valid @RequestBody ExpenseRequest request) {
        return ApiMapper.toResponse(expenseUseCase.create(ApiMapper.toCommand(request)));
    }

    @GetMapping
    public List<ExpenseResponse> findByMonthAndYear(@RequestParam Integer month, @RequestParam Integer year) {
        return expenseUseCase.findByMonthAndYear(month, year).stream()
                .map(ApiMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ExpenseResponse findById(@PathVariable UUID id) {
        return ApiMapper.toResponse(expenseUseCase.findById(id));
    }

    @PutMapping("/{id}")
    public ExpenseResponse update(@PathVariable UUID id, @Valid @RequestBody ExpenseRequest request) {
        return ApiMapper.toResponse(expenseUseCase.update(id, ApiMapper.toCommand(request)));
    }

    @PatchMapping("/{id}/pay")
    public ExpenseResponse markAsPaid(@PathVariable UUID id, @RequestBody(required = false) PayExpenseRequest request) {
        LocalDate paymentDate = request == null ? null : request.paymentDate();
        return ApiMapper.toResponse(expenseUseCase.markAsPaid(id, paymentDate));
    }

    @PatchMapping("/{id}/pending")
    public ExpenseResponse markAsPending(@PathVariable UUID id) {
        return ApiMapper.toResponse(expenseUseCase.markAsPending(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        expenseUseCase.delete(id);
    }
}
