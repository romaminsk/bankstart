package by.bank.bankstart.model.enums;

/**
 * Организационно-правовая форма бизнеса.
 * 
 * <p><b>ОБУЧЕНИЕ: ФОРМЫ ВЕДЕНИЯ БИЗНЕСА В БЕЛАРУСИ</b></p>
 * <ul>
 *   <li><b>ИП:</b> Индивидуальный предприниматель - проще регистрация, но ответственность всем имуществом</li>
 *   <li><b>ООО:</b> Общество с ограниченной ответственностью - ответственность в пределах уставного капитала</li>
 * </ul>
 *
 * @author BankStart Team
 * @version 1.0.0
 */
public enum OrganizationForm {
    
    /** Индивидуальный предприниматель */
    IP("ИП"),
    
    /** Общество с ограниченной ответственностью */
    OOO("ООО");
    
    private final String displayName;
    
    OrganizationForm(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
