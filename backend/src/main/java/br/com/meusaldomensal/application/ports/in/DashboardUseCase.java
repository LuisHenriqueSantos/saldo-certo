package br.com.meusaldomensal.application.ports.in;

import br.com.meusaldomensal.application.query.DashboardView;

public interface DashboardUseCase {
    DashboardView getDashboard(Integer month, Integer year);
}
