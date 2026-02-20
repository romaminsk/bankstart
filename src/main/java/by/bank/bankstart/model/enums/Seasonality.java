package by.bank.bankstart.model.enums;

import java.math.BigDecimal;

/**
 * Тип сезонности бизнеса.
 * 
 * <p><b>ОБУЧЕНИЕ: ЗАЧЕМ СЕЗОННОСТЬ?</b></p>
 * <p>Многие бизнесы имеют сезонные колебания выручки:</p>
 * <ul>
 *   <li><b>Зимний пик:</b> Новый год (подарки, еда, развлечения)</li>
 *   <li><b>Летний пик:</b> Туризм, строительство, мороженое</li>
 *   <li><b>Весна-осень:</b> 8 марта, 1 сентября (цветы, школьные товары)</li>
 * </ul>
 * 
 * <p>Сезонность влияет на cash flow и риски бизнеса.</p>
 *
 * @author BankStart Team
 * @version 1.0.0
 */
public enum Seasonality {
    
    /** Без ярко выраженной сезонности */
    NONE("Без сезонности", new BigDecimal[] {
        BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
        BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
        BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE
    }),
    
    /** Зимний пик (XII-I) */
    WINTER("Зимний пик", new BigDecimal[] {
        new BigDecimal("1.20"), new BigDecimal("0.80"), new BigDecimal("0.85"),
        new BigDecimal("0.90"), new BigDecimal("0.95"), new BigDecimal("0.95"),
        new BigDecimal("0.95"), new BigDecimal("0.95"), new BigDecimal("1.00"),
        new BigDecimal("1.05"), new BigDecimal("1.15"), new BigDecimal("1.30")
    }),
    
    /** Летний пик (VI-VIII) */
    SUMMER("Летний пик", new BigDecimal[] {
        new BigDecimal("0.85"), new BigDecimal("0.85"), new BigDecimal("0.90"),
        new BigDecimal("0.95"), new BigDecimal("1.05"), new BigDecimal("1.25"),
        new BigDecimal("1.30"), new BigDecimal("1.20"), new BigDecimal("1.00"),
        new BigDecimal("0.90"), new BigDecimal("0.85"), new BigDecimal("0.90")
    }),
    
    /** Весенне-осенний пик (III, IX) */
    SPRING_AUTUMN("Весна-осень", new BigDecimal[] {
        new BigDecimal("0.90"), new BigDecimal("0.95"), new BigDecimal("1.15"),
        new BigDecimal("1.00"), new BigDecimal("0.95"), new BigDecimal("0.85"),
        new BigDecimal("0.85"), new BigDecimal("0.90"), new BigDecimal("1.20"),
        new BigDecimal("1.05"), new BigDecimal("0.95"), new BigDecimal("1.00")
    });
    
    private final String displayName;
    
    /** Коэффициенты для каждого месяца (январь-декабрь) */
    private final BigDecimal[] monthlyFactors;
    
    Seasonality(String displayName, BigDecimal[] monthlyFactors) {
        this.displayName = displayName;
        this.monthlyFactors = monthlyFactors;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Получить коэффициент для месяца.
     * 
     * @param month номер месяца (1-12)
     * @return коэффициент (1.0 = без изменений, 1.2 = +20%)
     * @throws IllegalArgumentException если месяц вне диапазона 1-12
     */
    public BigDecimal getFactorForMonth(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Месяц должен быть от 1 до 12");
        }
        return monthlyFactors[month - 1];
    }
}
