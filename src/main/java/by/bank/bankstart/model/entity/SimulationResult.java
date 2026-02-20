package by.bank.bankstart.model.entity;

import by.bank.bankstart.model.enums.RiskLevel;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════════════╗
 * ║                   SimulationResult Entity (Результат симуляции)                    ║
 * ╠═══════════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                    ║
 * ║  ОБУЧЕНИЕ: ПОЧЕМУ ОТДЕЛЬНАЯ ТАБЛИЦА ДЛЯ РЕЗУЛЬТАТОВ?                               ║
 * ║  ─────────────────────────────────────────────────────────────────────────────     ║
 * ║                                                                                    ║
 * ║  1. РАЗДЕЛЕНИЕ ОТВЕТСТВЕННОСТИ (Separation of Concerns)                            ║
 * ║     - Application хранит ВХОДНЫЕ данные                                            ║
 * ║     - SimulationResult хранит ВЫХОДНЫЕ данные                                      ║
 * ║                                                                                    ║
 * ║  2. ГИБКОСТЬ                                                                       ║
 * ║     - Можно пересчитать результат без изменения заявки                             ║
 * ║     - Можно хранить историю расчётов (убрав UNIQUE constraint)                     ║
 * ║                                                                                    ║
 * ║  3. ПРОИЗВОДИТЕЛЬНОСТЬ                                                             ║
 * ║     - Можно загрузить только Application без тяжёлых результатов                   ║
 * ║     - Индексы можно настроить отдельно                                             ║
 * ║                                                                                    ║
 * ║  СВЯЗЬ: Application 1:1 SimulationResult                                           ║
 * ║  Каждая заявка имеет ровно один результат симуляции.                               ║
 * ║                                                                                    ║
 * ╚═══════════════════════════════════════════════════════════════════════════════════╝
 *
 * @author BankStart Team
 * @version 1.0.0
 * @see Application
 */
@Entity
@Table(name = "simulation_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimulationResult {
    
    /*
     * ╔═══════════════════════════════════════════════════════════════════════════════╗
     * ║                           ИДЕНТИФИКАЦИЯ                                        ║
     * ╚═══════════════════════════════════════════════════════════════════════════════╝
     */
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Связь с заявкой (1:1).
     * 
     * <p><b>ОБУЧЕНИЕ: @JoinColumn</b></p>
     * <ul>
     *   <li><b>name</b> - имя колонки внешнего ключа в ЭТОЙ таблице</li>
     *   <li><b>unique = true</b> - обеспечивает связь 1:1 (а не 1:N)</li>
     *   <li><b>nullable = false</b> - результат не может существовать без заявки</li>
     * </ul>
     * 
     * <p><b>ОБУЧЕНИЕ: FetchType</b></p>
     * <ul>
     *   <li><b>EAGER</b> - загружать сразу (по умолчанию для @ManyToOne)</li>
     *   <li><b>LAZY</b> - загружать по требованию (рекомендуется)</li>
     * </ul>
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    private Application application;
    
    /*
     * ╔═══════════════════════════════════════════════════════════════════════════════╗
     * ║                       ПАРАМЕТРЫ СИМУЛЯЦИИ                                      ║
     * ╚═══════════════════════════════════════════════════════════════════════════════╝
     */
    
    /**
     * Количество итераций Monte Carlo.
     * 
     * <p><b>ОБУЧЕНИЕ: ПОЧЕМУ 1000 ИТЕРАЦИЙ?</b></p>
     * <ul>
     *   <li>Слишком мало (100) - результаты нестабильны</li>
     *   <li>Слишком много (100000) - долгий расчёт</li>
     *   <li>1000 - баланс точности и скорости</li>
     *   <li>Статистически достаточно для 95% доверительного интервала</li>
     * </ul>
     */
    @Column(nullable = false)
    @Builder.Default
    private Integer iterations = 1000;
    
    /** Seed для генератора случайных чисел (для воспроизводимости) */
    @Column(name = "random_seed")
    private Long randomSeed;
    
    /** Время расчёта в миллисекундах */
    @Column(name = "calculation_time_ms")
    private Integer calculationTimeMs;
    
    /*
     * ╔═══════════════════════════════════════════════════════════════════════════════╗
     * ║                      ОСНОВНЫЕ МЕТРИКИ (АГРЕГАТЫ)                               ║
     * ╚═══════════════════════════════════════════════════════════════════════════════╝
     */
    
    /**
     * Вероятность успеха бизнеса (0-100%).
     * 
     * <p><b>ОБУЧЕНИЕ: ДЕНОРМАЛИЗАЦИЯ</b></p>
     * <p>Эти данные можно вычислить из monthlyProjections, но мы их дублируем:</p>
     * <ul>
     *   <li>Быстрый доступ без JOIN и агрегации</li>
     *   <li>Простые запросы: WHERE success_probability > 80</li>
     *   <li>Trade-off: немного лишнего места vs производительность</li>
     * </ul>
     */
    @Column(name = "success_probability", nullable = false, precision = 5, scale = 2)
    private BigDecimal successProbability;
    
    /*
     * ╔═══════════════════════════════════════════════════════════════════════════════╗
     * ║                    ПЕРЦЕНТИЛИ (P10, P50, P90)                                   ║
     * ║                                                                                 ║
     * ║  ОБУЧЕНИЕ: ЧТО ТАКОЕ ПЕРЦЕНТИЛИ?                                                ║
     * ║  ──────────────────────────────────────────────────────────────                 ║
     * ║                                                                                 ║
     * ║  P10 = 10% результатов ХУЖЕ этого значения                                      ║
     * ║        (пессимистичный сценарий)                                                ║
     * ║                                                                                 ║
     * ║  P50 = 50% результатов хуже, 50% лучше                                          ║
     * ║        (медиана, "типичный" сценарий)                                           ║
     * ║                                                                                 ║
     * ║  P90 = 90% результатов ХУЖЕ этого значения                                      ║
     * ║        (оптимистичный сценарий)                                                 ║
     * ║                                                                                 ║
     * ║  Пример: [100, 200, 300, 400, 500, 600, 700, 800, 900, 1000]                    ║
     * ║  P10 ≈ 100, P50 ≈ 550, P90 ≈ 900                                                ║
     * ║                                                                                 ║
     * ╚═══════════════════════════════════════════════════════════════════════════════╝
     */
    
    /** Финальный cash flow - пессимистичный сценарий (10-й перцентиль) */
    @Column(name = "final_p10", nullable = false, precision = 15, scale = 2)
    private BigDecimal finalP10;
    
    /** Финальный cash flow - медиана (50-й перцентиль) */
    @Column(name = "final_p50", nullable = false, precision = 15, scale = 2)
    private BigDecimal finalP50;
    
    /** Финальный cash flow - оптимистичный сценарий (90-й перцентиль) */
    @Column(name = "final_p90", nullable = false, precision = 15, scale = 2)
    private BigDecimal finalP90;
    
    /*
     * ╔═══════════════════════════════════════════════════════════════════════════════╗
     * ║                       КРИТИЧЕСКИЙ МЕСЯЦ                                        ║
     * ╚═══════════════════════════════════════════════════════════════════════════════╝
     */
    
    /** Месяц с минимальным cash flow (критическая точка) */
    @Column(name = "min_cash_month")
    private Integer minCashMonth;
    
    /** Минимальный cash flow в пессимистичном сценарии */
    @Column(name = "min_cash_p10", precision = 15, scale = 2)
    private BigDecimal minCashP10;
    
    /** Минимальный cash flow в медианном сценарии */
    @Column(name = "min_cash_p50", precision = 15, scale = 2)
    private BigDecimal minCashP50;
    
    /** Общая налоговая нагрузка за год */
    @Column(name = "total_tax_burden", precision = 15, scale = 2)
    private BigDecimal totalTaxBurden;
    
    /*
     * ╔═══════════════════════════════════════════════════════════════════════════════╗
     * ║                          AI-АНАЛИЗ                                             ║
     * ╚═══════════════════════════════════════════════════════════════════════════════╝
     */
    
    /** Текстовое резюме от LLM (Ollama/другой провайдер) */
    @Column(name = "ai_summary", columnDefinition = "TEXT")
    private String aiSummary;
    
    /** Провайдер AI (ollama, fallback, none) */
    @Column(name = "ai_provider", length = 50)
    private String aiProvider;
    
    /** Время генерации AI-резюме в мс */
    @Column(name = "ai_generation_time_ms")
    private Integer aiGenerationTimeMs;
    
    /*
     * ╔═══════════════════════════════════════════════════════════════════════════════╗
     * ║                         КАЧЕСТВО И РИСКИ                                       ║
     * ╚═══════════════════════════════════════════════════════════════════════════════╝
     */
    
    /**
     * Уровень риска (LOW/MEDIUM/HIGH).
     * 
     * <p>Вычисляется на основе successProbability:</p>
     * <ul>
     *   <li>LOW: 80%+</li>
     *   <li>MEDIUM: 60-79%</li>
     *   <li>HIGH: <60%</li>
     * </ul>
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false, length = 20)
    private RiskLevel riskLevel;
    
    /**
     * Предупреждения (JSON массив).
     * 
     * <p><b>ПРИМЕЧАНИЕ:</b> В MVP храним как строку. 
     * В production можно использовать JSONB через Hibernate Types.</p>
     */
    @Column(name = "warnings", columnDefinition = "TEXT")
    private String warnings;
    
    /*
     * ╔═══════════════════════════════════════════════════════════════════════════════╗
     * ║                           МЕТА-ДАННЫЕ                                          ║
     * ╚═══════════════════════════════════════════════════════════════════════════════╝
     */
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
