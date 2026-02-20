package by.bank.bankstart.repository;

import by.bank.bankstart.model.entity.SimulationResult;
import by.bank.bankstart.model.enums.RiskLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для результатов симуляции.
 *
 * @author BankStart Team
 * @version 1.0.0
 */
@Repository
public interface SimulationResultRepository extends JpaRepository<SimulationResult, Long> {
    
    /**
     * Найти результат по ID заявки.
     *
     * @param applicationId ID заявки
     * @return результат симуляции
     */
    Optional<SimulationResult> findByApplicationId(Long applicationId);
    
    /**
     * Найти все результаты по уровню риска.
     *
     * @param riskLevel уровень риска
     * @return список результатов
     */
    List<SimulationResult> findByRiskLevel(RiskLevel riskLevel);
}
