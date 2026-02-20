package by.bank.bankstart.controller;

import by.bank.bankstart.model.dto.QuickInputDTO;
import by.bank.bankstart.model.enums.TaxSystem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════════════╗
 * ║                            HomeController                                          ║
 * ╠═══════════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                    ║
 * ║  ОБУЧЕНИЕ: ПАТТЕРН MVC (Model-View-Controller)                                     ║
 * ║  ─────────────────────────────────────────────────────────────────────────────     ║
 * ║                                                                                    ║
 * ║  ┌─────────────┐         ┌─────────────┐         ┌─────────────┐                   ║
 * ║  │   Browser   │ ──────▶ │ Controller  │ ──────▶ │   Service   │                   ║
 * ║  │  (Request)  │         │ (Маршрут)   │         │  (Логика)   │                   ║
 * ║  └─────────────┘         └─────────────┘         └─────────────┘                   ║
 * ║         ▲                       │                       │                          ║
 * ║         │                       ▼                       ▼                          ║
 * ║         │                ┌─────────────┐         ┌─────────────┐                   ║
 * ║         └──────────────  │    View     │ ◀────── │    Model    │                   ║
 * ║           (Response)     │ (Thymeleaf) │         │   (Data)    │                   ║
 * ║                          └─────────────┘         └─────────────┘                   ║
 * ║                                                                                    ║
 * ║  АННОТАЦИИ SPRING MVC:                                                             ║
 * ║  ─────────────────────────────────────────────────────────────────────────         ║
 * ║                                                                                    ║
 * ║  @Controller      - Класс обрабатывает HTTP запросы и возвращает View              ║
 * ║  @RestController  - Возвращает JSON/XML напрямую (без View)                        ║
 * ║  @GetMapping      - Обработка GET запросов                                         ║
 * ║  @PostMapping     - Обработка POST запросов                                        ║
 * ║  @RequestMapping  - Универсальный маппинг (любой метод)                            ║
 * ║                                                                                    ║
 * ╚═══════════════════════════════════════════════════════════════════════════════════╝
 *
 * @author BankStart Team
 * @version 1.0.0
 */
@Controller  // Маркер для Spring - это Web Controller (возвращает имена View)
@Slf4j
public class HomeController {
    
    /**
     * Главная страница (Landing Page).
     * 
     * <p><b>ОБУЧЕНИЕ: @GetMapping</b></p>
     * <ul>
     *   <li><b>value = "/"</b> - URL путь (http://localhost:8080/)</li>
     *   <li><b>Model model</b> - контейнер для данных, передаваемых в View</li>
     *   <li><b>return "index"</b> - имя шаблона (templates/index.html)</li>
     * </ul>
     * 
     * <p><b>FLOW:</b></p>
     * <pre>
     * Браузер GET / → HomeController.index() → return "index"
     *                                             ↓
     *                     ← HTML ← Thymeleaf обрабатывает templates/index.html
     * </pre>
     *
     * @param model контейнер для передачи данных в шаблон
     * @return имя Thymeleaf шаблона
     */
    @GetMapping("/")
    public String index(Model model) {
        log.debug("Запрос главной страницы");
        
        // Передаём данные в шаблон
        model.addAttribute("pageTitle", "BankStart - Калькулятор финансовой устойчивости");
        
        return "index";  // → templates/index.html
    }
    
    /**
     * Страница быстрого калькулятора.
     * 
     * @param model контейнер для данных
     * @return имя шаблона
     */
    @GetMapping("/calculator/quick")
    public String quickCalculator(Model model) {
        log.debug("Запрос страницы быстрого калькулятора");
        
        // Пустая форма для заполнения
        model.addAttribute("quickForm", new QuickInputDTO());
        model.addAttribute("taxSystems", TaxSystem.values());
        model.addAttribute("pageTitle", "Быстрый расчёт | BankStart");
        
        return "calculator/quick";  // → templates/calculator/quick.html
    }
    
    /**
     * Страница с информацией о проекте.
     *
     * @param model контейнер для данных
     * @return имя шаблона
     */
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("pageTitle", "О проекте | BankStart");
        return "about";
    }
    
    /**
     * Страница Disclaimer (отказ от ответственности).
     *
     * @param model контейнер для данных
     * @return имя шаблона
     */
    @GetMapping("/disclaimer")
    public String disclaimer(Model model) {
        model.addAttribute("pageTitle", "Отказ от ответственности | BankStart");
        return "disclaimer";
    }
}
