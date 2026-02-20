package by.bank.bankstart.model.dto;

import by.bank.bankstart.model.enums.OrganizationForm;
import by.bank.bankstart.model.enums.Seasonality;
import by.bank.bankstart.model.enums.TaxSystem;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Расширенный режим ввода данных (15+ полей).
 * 
 * <p>Включает все поля из QuickInputDTO + дополнительные:</p>
 * <ul>
 *   <li>Сезонность</li>
 *   <li>Ожидаемый рост</li>
 *   <li>Волатильность выручки</li>
 *   <li>Существующие кредиты</li>
 *   <li>Контактные данные</li>
 * </ul>
 *
 * @author BankStart Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExtendedInputDTO {
    
    /*
     * БАЗОВЫЕ ПОЛЯ (как в QuickInputDTO)
     */
    
    @NotBlank(message = "Выберите отрасль бизнеса")
    private String industry;
    
    @NotNull(message = "Укажите выручку")
    @Positive(message = "Выручка должна быть положительной")
    private BigDecimal monthlyRevenue;
    
    @NotNull(message = "Укажите постоянные расходы")
    @PositiveOrZero(message = "Расходы не могут быть отрицательными")
    private BigDecimal fixedCosts;
    
    @NotNull(message = "Укажите переменные расходы")
    @Min(value = 0, message = "Переменные расходы не могут быть отрицательными")
    @Max(value = 100, message = "Переменные расходы не могут превышать 100%")
    private BigDecimal variableCostsPercent;
    
    @NotNull(message = "Укажите начальный капитал")
    @PositiveOrZero(message = "Капитал не может быть отрицательным")
    private BigDecimal initialCapital;
    
    @NotNull(message = "Выберите систему налогообложения")
    private TaxSystem taxSystem;
    
    /*
     * РАСШИРЕННЫЕ ПОЛЯ
     */
    
    /** Организационная форма (ИП/ООО) */
    private OrganizationForm organizationForm;
    
    /** Сколько месяцев работает бизнес */
    @Min(value = 0, message = "Месяцев работы не может быть отрицательным")
    @Builder.Default
    private Integer monthsOperating = 0;
    
    /** Количество сотрудников */
    @Min(value = 0, message = "Количество сотрудников не может быть отрицательным")
    @Builder.Default
    private Integer employeeCount = 0;
    
    /** Волатильность выручки в % */
    @Min(value = 0, message = "Волатильность не может быть отрицательной")
    @Max(value = 100, message = "Волатильность не может превышать 100%")
    @Builder.Default
    private BigDecimal revenueVolatility = new BigDecimal("15");
    
    /** Ожидаемый рост выручки за год в % */
    @Min(value = -50, message = "Снижение не может быть больше 50%")
    @Max(value = 200, message = "Рост не может превышать 200%")
    @Builder.Default
    private BigDecimal expectedGrowthRate = BigDecimal.ZERO;
    
    /** Тип сезонности */
    @Builder.Default
    private Seasonality seasonality = Seasonality.NONE;
    
    /** Существующий ежемесячный платеж по кредиту */
    @PositiveOrZero(message = "Платеж по кредиту не может быть отрицательным")
    @Builder.Default
    private BigDecimal existingLoanPayment = BigDecimal.ZERO;
    
    /** Планируемая сумма кредита */
    @PositiveOrZero(message = "Сумма кредита не может быть отрицательной")
    @Builder.Default
    private BigDecimal plannedLoanAmount = BigDecimal.ZERO;
    
    /*
     * КОНТАКТНЫЕ ДАННЫЕ
     */
    
    @NotBlank(message = "Укажите email")
    @Email(message = "Некорректный формат email")
    private String email;
    
    @Pattern(regexp = "^$|^\\+?[0-9]{10,15}$", message = "Некорректный формат телефона")
    private String phone;
    
    /** Имя контактного лица */
    private String name;
    
    /** Город */
    private String city;
}
