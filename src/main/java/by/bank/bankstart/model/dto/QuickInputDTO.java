package by.bank.bankstart.model.dto;

import by.bank.bankstart.model.enums.TaxSystem;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════════════╗
 * ║                     QuickInputDTO - Быстрый режим ввода                              ║
 * ╠═══════════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                          ║
 * ║  ОБУЧЕНИЕ: ПАТТЕРН DTO (Data Transfer Object)                                             ║
 * ║  ──────────────────────────────────────────────────────────────────────────────         ║
 * ║                                                                                          ║
 * ║  ПОЧЕМУ DTO, А НЕ СРАЗУ ENTITY?                                                         ║
 * ║                                                                                          ║
 * ║  1. РАЗДЕЛЕНИЕ ОТВЕТСТВЕННОСТИ (Separation of Concerns)                                  ║
 * ║     - DTO: что приходит с формы                                                       ║
 * ║     - Entity: что хранится в БД                                                        ║
 * ║                                                                                          ║
 * ║  2. БЕЗОПАСНОСТЬ                                                                        ║
 * ║     - DTO не содержит чувствительных полей (id, createdAt, ipAddress)                  ║
 * ║     - Защита от Mass Assignment: пользователь не может установить id=1               ║
 * ║                                                                                          ║
 * ║  3. ВАЛИДАЦИЯ                                                                           ║
 * ║     - DTO валидирует входные данные С ТОЧКИ ЗРЕНИЯ ПОЛЬЗОВАТЕЛЯ                     ║
 * ║     - Entity валидирует с точки зрения БИЗНЕС-ЛОГИКИ                                 ║
 * ║                                                                                          ║
 * ║  4. ГИБКОСТЬ                                                                            ║
 * ║     - Форма может иметь другую структуру, чем БД                                  ║
 * ║     - Можно добавить confirmEmail, которого нет в Entity                             ║
 * ║                                                                                          ║
 * ║  FLOW ДАННЫХ:                                                                          ║
 * ║  Форма HTML → QuickInputDTO → Валидация → Service → Application Entity → БД          ║
 * ║                                                                                          ║
 * ╚═══════════════════════════════════════════════════════════════════════════════════╝
 *
 * <p>Быстрый режим - 5 основных полей + email для первичной оценки.</p>
 *
 * @author BankStart Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuickInputDTO {
    
    /*
     * ╔═════════════════════════════════════════════════════════════════════════════════╗
     * ║                    5 ОСНОВНЫХ ПОЛЕЙ БЫСТРОГО РЕЖИМА                           ║
     * ╚═════════════════════════════════════════════════════════════════════════════════╝
     */
    
    /**
     * Отрасль бизнеса.
     * 
     * <p><b>ОБУЧЕНИЕ: Bean Validation</b></p>
     * <p>Аннотации из jakarta.validation.constraints:</p>
     * <ul>
     *   <li><b>@NotBlank</b> - не null, не пустая строка, не пробелы</li>
     *   <li><b>@NotNull</b> - не null (" " - валидно)</li>
     *   <li><b>@NotEmpty</b> - не null, не пустая строка (" " - валидно)</li>
     * </ul>
     * 
     * <p><b>message</b> - сообщение об ошибке для пользователя.</p>
     */
    @NotBlank(message = "Выберите отрасль бизнеса")
    private String industry;
    
    /**
     * Среднемесячная выручка (BYN).
     * 
     * <p><b>ОБУЧЕНИЕ: @Positive vs @PositiveOrZero</b></p>
     * <ul>
     *   <li><b>@Positive</b> - строго > 0</li>
     *   <li><b>@PositiveOrZero</b> - >= 0</li>
     *   <li><b>@Negative</b> - строго < 0</li>
     *   <li><b>@NegativeOrZero</b> - <= 0</li>
     * </ul>
     */
    @NotNull(message = "Укажите выручку")
    @Positive(message = "Выручка должна быть положительной")
    @DecimalMin(value = "100", message = "Минимальная выручка - 100 BYN")
    @DecimalMax(value = "10000000", message = "Максимальная выручка - 10,000,000 BYN")
    private BigDecimal monthlyRevenue;
    
    /** Постоянные расходы в месяц (BYN) */
    @NotNull(message = "Укажите постоянные расходы")
    @PositiveOrZero(message = "Расходы не могут быть отрицательными")
    private BigDecimal fixedCosts;
    
    /**
     * Переменные расходы в % от выручки.
     * 
     * <p>Пример: 30% = на каждые 100 BYN выручки, 30 BYN уходит на закупку товара.</p>
     */
    @NotNull(message = "Укажите переменные расходы")
    @Min(value = 0, message = "Переменные расходы не могут быть отрицательными")
    @Max(value = 100, message = "Переменные расходы не могут превышать 100%")
    private BigDecimal variableCostsPercent;
    
    /** Начальный капитал (деньги на руках + счета) */
    @NotNull(message = "Укажите начальный капитал")
    @PositiveOrZero(message = "Капитал не может быть отрицательным")
    private BigDecimal initialCapital;
    
    /*
     * ╔═════════════════════════════════════════════════════════════════════════════════╗
     * ║                        ДОПОЛНИТЕЛЬНЫЕ ПОЛЯ                                      ║
     * ╚═════════════════════════════════════════════════════════════════════════════════╝
     */
    
    /** Система налогообложения (УСН/ОСН) */
    @NotNull(message = "Выберите систему налогообложения")
    private TaxSystem taxSystem;
    
    /** Email для отправки результатов */
    @NotBlank(message = "Укажите email")
    @Email(message = "Некорректный формат email")
    private String email;
}
