package br.com.meusaldomensal.adapters.in.web;

import br.com.meusaldomensal.application.ports.in.DashboardUseCase;
import br.com.meusaldomensal.application.query.DashboardView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardUseCase dashboardUseCase;

    public DashboardController(DashboardUseCase dashboardUseCase) {
        this.dashboardUseCase = dashboardUseCase;
    }

    @GetMapping
    public DashboardView getDashboard(@RequestParam Integer month, @RequestParam Integer year) {
        return dashboardUseCase.getDashboard(month, year);
    }
}
