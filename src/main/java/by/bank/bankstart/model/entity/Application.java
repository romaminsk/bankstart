package by.bank.bankstart.model.entity;

import by.bank.bankstart.model.enums.CalculationMode;
import by.bank.bankstart.model.enums.OrganizationForm;
import by.bank.bankstart.model.enums.Seasonality;
import by.bank.bankstart.model.enums.TaxSystem;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════════════╗
 * ║                       Application Entity (Заявка)                                  ║
 * ╠═══════════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                    ║
 * ║  ОБУЧЕНИЕ: JPA ENTITY                                                              ║
 * ║  ─────────────────────────────────────────────────────────────────────────────     ║
 * ║                                                                                    ║
 * ║  Entity - это Java класс, который маппится на таблицу в базе данных.               ║
 * ║  JPA (Java Persistence API) - стандарт для ORM (Object-Relational Mapping).        ║
 * ║                                                                                    ║
 * ║  Ключевые аннотации:                                                               ║
 * ║  ────────────────────────────────────────────────────────────────────────          ║
 * ║                                                                                    ║
 * ║  @Entity        - Маркирует класс как JPA Entity                                   ║
 * ║  @Table         - Указывает имя таблицы в БД                                       ║
 * ║  @Id            - Первичный ключ                                                   ║
 * ║  @GeneratedValue - Стратегия генерации ID                                          ║
 * ║  @Column        - Настройки колонки (имя, nullable, unique)                        ║
 * ║  @Enumerated    - Маппинг enum в БД (STRING или ORDINAL)                           ║
 * ║                                                                                    ║
 * ║  Связи между таблицами:                                                            ║
 * ║  ────────────────────────────────────────────────────────────────────────          ║
 * ║  @OneToOne      - Один к одному (Application -> SimulationResult)                  ║
 * ║  @OneToMany     - Один ко многим                                                   ║
 * ║  @ManyToOne     - Многие к одному                                                  ║
 * ║  @ManyToMany    - Многие ко многим                                                 ║
 * ║                                                                                    ║
 * ╚═══════════════════════════════════════════════════════════════════════════════════╝
 *
 * <p>Данный класс представляет заявку пользователя на расчёт финансовой устойчивости.</p>
 * 
 * @author BankStart Team
 * @version 1.0.0
 * @see SimulationResult
 */
@Entity
@Table(name = "applications")
@Data                       // Lombok: генерирует getters, setters, equals, hashCode, toString
@NoArgsConstructor          // Lombok: конструктор без аргументов (требуется JPA)
@AllArgsConstructor         // Lombok: конструктор со всеми аргументами
@Builder                    // Lombok: паттерн Builder для удобного создания объектов
public class Application {
    
    /*
     * ╔═══════════════════════════════════════════════════════════════════════════════╗
     * ║                        ПЕРВИЧНЫЙ КЛЮЧ И ИДЕНТИФИКАЦИЯ                          ║
     * ╚═══════════════════════════════════════════════════════════════════════════════╝
     */
    
    /**
     * Первичный ключ.
     * 
     * <p><b>ОБУЧЕНИЕ: СТРАТЕГИИ ГЕНЕРАЦИИ ID</b></p>
     * <ul>
     *   <li><b>IDENTITY</b> - автоинкремент в БД (PostgreSQL SERIAL)</li>
     *   <li><b>SEQUENCE</b> - через последовательность БД (рекомендуется для PostgreSQL)</li>
     *   <li><b>TABLE</b> - через отдельную таблицу (переносимо, но медленно)</li>
     *   <li><b>AUTO</b> - провайдер выбирает сам</li>
     * </ul>
     * 
     * <p><b>ПОЧЕМУ IDENTITY?</b> Простота для MVP, в production лучше SEQUENCE.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Публичный UUID для ссылок.
     * 
     * <p><b>ПОЧЕМУ UUID?</b></p>
     * <ul>
     *   <li>Безопасность: нельзя угадать ID другой заявки (например, /application/1)</li>
     *   <li>Ссылки можно шарить: /results/a1b2c3d4-e5f6-...</li>
     *   <li>Не раскрывает количество заявок</li>
     * </ul>
     */
    @Column(name = "public_id", nullable = false, unique = true, updatable = false)
    @Builder.Default
    private UUID publicId = UUID.randomUUID();
    
    /*
     * ╔═══════════════════════════════════════════════════════════════════════════════╗
     * ║                           РЕЖИМ РАСЧЁТА                                        ║
     * ╚═══════════════════════════════════════════════════════════════════════════════╝
     */
    
    /**
     * Режим расчёта (быстрый или расширенный).
     * 
     * <p><b>ОБУЧЕНИЕ: @Enumerated</b></p>
     * <ul>
     *   <li><b>EnumType.STRING</b> - сохраняет имя enum ("QUICK", "EXTENDED")</li>
     *   <li><b>EnumType.ORDINAL</b> - сохраняет порядковый номер (0, 1) - ОПАСНО!</li>
     * </ul>
     * 
     * <p><b>ПОЧЕМУ STRING?</b> При добавлении нового значения в середину enum, 
     * ORDINAL сломает все данные!</p>
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "calculation_mode", nullable = false, length = 20)
    private CalculationMode calculationMode;
    
    /*
     * ╔═══════════════════════════════════════════════════════════════════════════════╗
     * ║                           БИЗНЕС-ДАННЫЕ                                        ║
     * ╚═══════════════════════════════════════════════════════════════════════════════╝
     */
    
    /** Отрасль бизнеса (например, "Общепит", "Розничная торговля") */
    @NotBlank(message = "Отрасль обязательна")
    @Column(nullable = false, length = 100)
    private String industry;
    
    /** Организационно-правовая форма (ИП или ООО) */
    @Enumerated(EnumType.STRING)
    @Column(name = "organization_form", length = 10)
    private OrganizationForm organizationForm;
    
    /** Сколько месяцев работает бизнес */
    @Min(value = 0, message = "Месяцев работы не может быть отрицательным")
    @Column(name = "months_operating")
    @Builder.Default
    private Integer monthsOperating = 0;
    
    /** Количество сотрудников */
    @Min(value = 0, message = "Количество сотрудников не может быть отрицательным")
    @Column(name = "employee_count")
    @Builder.Default
    private Integer employeeCount = 0;
    
    /*
     * ╔═══════════════════════════════════════════════════════════════════════════════╗
     * ║                         ФИНАНСОВЫЕ ПОКАЗАТЕЛИ                                  ║
     * ╚═══════════════════════════════════════════════════════════════════════════════╝
     */
    
    /**
     * Ежемесячная выручка (BYN).
     * 
     * <p><b>ОБУЧЕНИЕ: ПОЧЕМУ BigDecimal, А НЕ double?</b></p>
     * <pre>
     * // ПРОБЛЕМА с double:
     * double a = 0.1;
     * double b = 0.2;
     * System.out.println(a + b); // 0.30000000000000004 (!!)
     * 
     * // BigDecimal - точные вычисления:
     * BigDecimal a = new BigDecimal("0.1");
     * BigDecimal b = new BigDecimal("0.2");
     * System.out.println(a.add(b)); // 0.3
     * </pre>
     * 
     * <p><b>ПРАВИЛО:</b> Для финансовых данных ВСЕГДА используйте BigDecimal!</p>
     * 
     * <p><b>ОБУЧЕНИЕ: precision и scale</b></p>
     * <ul>
     *   <li><b>precision = 15</b> - всего цифр (до 999,999,999,999.99)</li>
     *   <li><b>scale = 2</b> - цифр после запятой</li>
     * </ul>
     */
    @NotNull(message = "Выручка обязательна")
    @Positive(message = "Выручка должна быть положительной")
    @Column(name = "monthly_revenue", nullable = false, precision = 15, scale = 2)
    private BigDecimal monthlyRevenue;
    
    /**
     * Волатильность (разброс) выручки в процентах.
     * <p>Например, 15% означает, что выручка может колебаться на ±15%.</p>
     */
    @Min(value = 0, message = "Волатильность не может быть отрицательной")
    @Max(value = 100, message = "Волатильность не может превышать 100%")
    @Column(name = "revenue_volatility", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal revenueVolatility = new BigDecimal("15.0");
    
    /** Постоянные расходы (аренда, зарплаты, коммуналка) */
    @NotNull(message = "Постоянные расходы обязательны")
    @PositiveOrZero(message = "Расходы не могут быть отрицательными")
    @Column(name = "fixed_costs", nullable = false, precision = 15, scale = 2)
    private BigDecimal fixedCosts;
    
    /** Переменные расходы в % от выручки (закупка товара, материалы) */
    @NotNull(message = "Переменные расходы обязательны")
    @Min(value = 0, message = "Переменные расходы не могут быть отрицательными")
    @Max(value = 100, message = "Переменные расходы не могут превышать 100%")
    @Column(name = "variable_costs_percent", nullable = false, precision = 5, scale = 2)
    private BigDecimal variableCostsPercent;
    
    /** Начальный капитал (деньги на счетах + наличные) */
    @NotNull(message = "Начальный капитал обязателен")
    @PositiveOrZero(message = "Капитал не может быть отрицательным")
    @Column(name = "initial_capital", nullable = false, precision = 15, scale = 2)
    private BigDecimal initialCapital;
    
    /** Ожидаемый рост выручки в % за год */
    @Min(value = -50, message = "Снижение не может быть больше 50%")
    @Max(value = 200, message = "Рост не может превышать 200%")
    @Column(name = "expected_growth_rate", precision = 6, scale = 2)
    @Builder.Default
    private BigDecimal expectedGrowthRate = BigDecimal.ZERO;
    
    /** Тип сезонности бизнеса */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Seasonality seasonality = Seasonality.NONE;
    
    /** Система налогообложения */
    @NotNull(message = "Система налогообложения обязательна")
    @Enumerated(EnumType.STRING)
    @Column(name = "tax_system", nullable = false, length = 10)
    private TaxSystem taxSystem;
    
    /** Существующий ежемесячный платёж по кредиту */
    @PositiveOrZero(message = "Платёж по кредиту не может быть отрицательным")
    @Column(name = "existing_loan_payment", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal existingLoanPayment = BigDecimal.ZERO;
    
    /** Планируемая сумма кредита */
    @PositiveOrZero(message = "Сумма кредита не может быть отрицательной")
    @Column(name = "planned_loan_amount", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal plannedLoanAmount = BigDecimal.ZERO;
    
    /*
     * ╔═══════════════════════════════════════════════════════════════════════════════╗
     * ║                          КОНТАКТНЫЕ ДАННЫЕ                                     ║
     * ╚═══════════════════════════════════════════════════════════════════════════════╝
     */
    
    /** Email для отправки результатов */
    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    @Column(nullable = false)
    private String email;
    
    /** Телефон (опционально) */
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Некорректный формат телефона")
    private String phone;
    
    /** Имя контактного лица */
    private String name;
    
    /** Город */
    private String city;
    
    /*
     * ╔═══════════════════════════════════════════════════════════════════════════════╗
     * ║                           МЕТА-ДАННЫЕ                                          ║
     * ╚═══════════════════════════════════════════════════════════════════════════════╝
     */
    
    /**
     * Дата создания заявки.
     * 
     * <p><b>ОБУЧЕНИЕ: @PrePersist</b></p>
     * <p>Callback, вызываемый перед INSERT в БД.
     * Альтернатива: @CreationTimestamp от Hibernate.</p>
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /** Дата последнего обновления */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    /** IP-адрес пользователя (для аналитики и безопасности) */
    @Column(name = "ip_address")
    private String ipAddress;
    
    /** User-Agent браузера */
    @Column(name = "user_agent", length = 500)
    private String userAgent;
    
    /*
     * ╔═══════════════════════════════════════════════════════════════════════════════╗
     * ║                              СВЯЗИ                                             ║
     * ╚═══════════════════════════════════════════════════════════════════════════════╝
     */
    
    /**
     * Результат симуляции (связь 1:1).
     * 
     * <p><b>ОБУЧЕНИЕ: @OneToOne связь</b></p>
     * <ul>
     *   <li><b>mappedBy</b> - указывает поле в связанной сущности</li>
     *   <li><b>cascade</b> - каскадные операции (удаление Application удалит SimulationResult)</li>
     *   <li><b>orphanRemoval</b> - удаляет "осиротевшие" записи</li>
     *   <li><b>fetch = LAZY</b> - ленивая загрузка (не загружать, пока не нужно)</li>
     * </ul>
     */
    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL, 
              orphanRemoval = true, fetch = FetchType.LAZY)
    private SimulationResult simulationResult;
    
    /*
     * ╔═══════════════════════════════════════════════════════════════════════════════╗
     * ║                         LIFECYCLE CALLBACKS                                    ║
     * ╚═══════════════════════════════════════════════════════════════════════════════╝
     */
    
    /**
     * Вызывается перед первым сохранением в БД.
     * 
     * <p><b>ОБУЧЕНИЕ: JPA Lifecycle Callbacks</b></p>
     * <ul>
     *   <li><b>@PrePersist</b> - перед INSERT</li>
     *   <li><b>@PostPersist</b> - после INSERT</li>
     *   <li><b>@PreUpdate</b> - перед UPDATE</li>
     *   <li><b>@PostUpdate</b> - после UPDATE</li>
     *   <li><b>@PreRemove</b> - перед DELETE</li>
     *   <li><b>@PostLoad</b> - после загрузки из БД</li>
     * </ul>
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.publicId == null) {
            this.publicId = UUID.randomUUID();
        }
    }
    
    /** Вызывается перед каждым обновлением */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
