package by.bank.bankstart.model.enums;

/**
 * Уровень риска бизнеса.
 * 
 * <p><b>ОБУЧЕНИЕ: КЛАССИФИКАЦИЯ РИСКОВ</b></p>
 * <p>Риск определяется на основе вероятности успеха:</p>
 * <ul>
 *   <li><b>LOW:</b> 80%+ - бизнес стабилен, можно кредитовать</li>
 *   <li><b>MEDIUM:</b> 60-80% - есть риски, нужны дополнительные гарантии</li>
 *   <li><b>HIGH:</b> <60% - высокий риск, кредитование нежелательно</li>
 * </ul>
 *
 * @author BankStart Team
 * @version 1.0.0
 */
public enum RiskLevel {
    
    /** Низкий риск (вероятность успеха 80%+) */
    LOW("Низкий", "#28a745", 80),
    
    /** Средний риск (вероятность 60-79%) */
    MEDIUM("Средний", "#ffc107", 60),
    
    /** Высокий риск (вероятность <60%) */
    HIGH("Высокий", "#dc3545", 0);
    
    private final String displayName;
    private final String colorCode;
    private final int minSuccessRate;
    
    RiskLevel(String displayName, String colorCode, int minSuccessRate) {
        this.displayName = displayName;
        this.colorCode = colorCode;
        this.minSuccessRate = minSuccessRate;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Цвет для отображения в UI (Bootstrap colors).
     * @return HEX код цвета
     */
    public String getColorCode() {
        return colorCode;
    }
    
    /**
     * Определяет уровень риска по вероятности успеха.
     * 
     * @param successProbability вероятность успеха (0-100)
     * @return соответствующий уровень риска
     */
    public static RiskLevel fromSuccessProbability(double successProbability) {
        if (successProbability >= 80) {
            return LOW;
        } else if (successProbability >= 60) {
            return MEDIUM;
        } else {
            return HIGH;
        }
    }
}
