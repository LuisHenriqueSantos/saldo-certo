package br.com.meusaldomensal.adapters.in.web;

import br.com.meusaldomensal.adapters.in.web.dto.MonthlySummaryCloseRequest;
import br.com.meusaldomensal.adapters.in.web.dto.MonthlySummaryResponse;
import br.com.meusaldomensal.adapters.in.web.mapper.ApiMapper;
import br.com.meusaldomensal.application.ports.in.MonthlySummaryUseCase;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/monthly-summaries")
public class MonthlySummaryController {

    private final MonthlySummaryUseCase monthlySummaryUseCase;

    public MonthlySummaryController(MonthlySummaryUseCase monthlySummaryUseCase) {
        this.monthlySummaryUseCase = monthlySummaryUseCase;
    }

    @PostMapping("/close")
    @ResponseStatus(HttpStatus.CREATED)
    public MonthlySummaryResponse closeMonth(@Valid @RequestBody MonthlySummaryCloseRequest request) {
        return ApiMapper.toResponse(monthlySummaryUseCase.closeMonth(request.month(), request.year()));
    }

    @GetMapping
    public List<MonthlySummaryResponse> findAll(@RequestParam(required = false) Integer year) {
        return monthlySummaryUseCase.findAll(year).stream()
                .map(ApiMapper::toResponse)
                .toList();
    }
}
