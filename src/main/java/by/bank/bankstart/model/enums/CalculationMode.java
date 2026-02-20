package by.bank.bankstart.model.enums;

/**
 * Режим расчёта калькулятора.
 * 
 * <p><b>ОБУЧЕНИЕ: ПОЧЕМУ ENUM?</b></p>
 * <ul>
 *   <li><b>Типобезопасность:</b> Компилятор проверяет значения, невозможно передать "QUIK" вместо "QUICK"</li>
 *   <li><b>Документация:</b> Все возможные значения видны в IDE</li>
 *   <li><b>JPA совместимость:</b> Автоматический маппинг в строку через @Enumerated</li>
 *   <li><b>Расширяемость:</b> Можно добавлять методы и поля</li>
 * </ul>
 *
 * @author BankStart Team
 * @version 1.0.0
 */
public enum CalculationMode {
    
    /**
     * Быстрый режим - 5 полей ввода.
     * <p>Для первичной оценки бизнеса.</p>
     */
    QUICK("Быстрый", 5),
    
    /**
     * Расширенный режим - 15+ полей ввода.
     * <p>Для детального анализа с сезонностью, налогами и т.д.</p>
     */
    EXTENDED("Расширенный", 15);
    
    /** Отображаемое имя на русском */
    private final String displayName;
    
    /** Примерное количество полей */
    private final int fieldCount;
    
    /**
     * Конструктор enum.
     * 
     * <p><b>ОБУЧЕНИЕ:</b> Enum может иметь конструкторы, поля и методы.</p>
     *
     * @param displayName отображаемое имя
     * @param fieldCount количество полей
     */
    CalculationMode(String displayName, int fieldCount) {
        this.displayName = displayName;
        this.fieldCount = fieldCount;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getFieldCount() {
        return fieldCount;
    }
}
