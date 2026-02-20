package by.bank.bankstart.model.enums;

import java.math.BigDecimal;

/**
 * Система налогообложения в Беларуси.
 * 
 * <p><b>ОБУЧЕНИЕ: НАЛОГОВАЯ СИСТЕМА БЕЛАРУСИ</b></p>
 * <ul>
 *   <li><b>УСН (упрощённая):</b> 6% от выручки - для ИП и малого бизнеса</li>
 *   <li><b>ОСН (общая):</b> 18% налог на прибыль + 20% НДС</li>
 * </ul>
 * 
 * <p><b>ВАЖНО:</b> Ставки упрощены для MVP, в реальности много нюансов.</p>
 *
 * @author BankStart Team
 * @version 1.0.0
 */
public enum TaxSystem {
    
    /**
     * Упрощённая система налогообложения.
     * <p>Налог 6% от выручки, без НДС.</p>
     */
    USN("УСН (упрощённая)", new BigDecimal("0.06"), false),
    
    /**
     * Общая система налогообложения.
     * <p>18% налог на прибыль + 20% НДС.</p>
     */
    OSN("ОСН (общая)", new BigDecimal("0.18"), true);
    
    private final String displayName;
    private final BigDecimal taxRate;
    private final boolean hasVat;
    
    TaxSystem(String displayName, BigDecimal taxRate, boolean hasVat) {
        this.displayName = displayName;
        this.taxRate = taxRate;
        this.hasVat = hasVat;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Получить базовую ставку налога.
     * @return ставка в десятичной форме (0.06 = 6%)
     */
    public BigDecimal getTaxRate() {
        return taxRate;
    }
    
    /**
     * Проверяет, есть ли НДС.
     * @return true если система включает НДС
     */
    public boolean hasVat() {
        return hasVat;
    }
}
