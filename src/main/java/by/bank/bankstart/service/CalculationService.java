package by.bank.bankstart.service;

import by.bank.bankstart.model.dto.QuickInputDTO;
import by.bank.bankstart.model.entity.Application;
import by.bank.bankstart.model.entity.SimulationResult;
import by.bank.bankstart.model.enums.CalculationMode;
import by.bank.bankstart.model.enums.RiskLevel;
import by.bank.bankstart.repository.ApplicationRepository;
import by.bank.bankstart.repository.SimulationResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * ╔════════════════════════════════════════════════════════════════════════════════╗
 * ║                     CalculationService                                           ║
 * ╠════════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                   ║
 * ║  ОБУЧЕНИЕ: ПАТТЕРН SERVICE LAYER                                                   ║
 * ║  ─────────────────────────────────────────────────────────────────────────            ║
 * ║                                                                                   ║
 * ║  Service Layer содержит БИЗНЕС-ЛОГИКУ:                                            ║
 * ║                                                                                   ║
 * ║  Controller              Service              Repository                          ║
 * ║      │                     │                     │                               ║
 * ║      │  HTTP Request       │                     │                               ║
 * ║      ├────────────────────►│                     │                               ║
 * ║      │                     │  Бизнес-логика     │                               ║
 * ║      │                     ├────────────────────►│                               ║
 * ║      │                     │                     │  SQL                          ║
 * ║      │                     │                     ├───────► БД                    ║
 * ║                                                                                   ║
 * ║  ПОЧЕМУ ОТДЕЛЬНЫЙ SERVICE, А НЕ ЛОГИКА В CONTROLLER?                              ║
 * ║                                                                                   ║
 * ║  1. ТЕСТИРУЕМОСТЬ: Service можно тестировать без HTTP                              ║
 * ║  2. ПЕРЕИСПОЛЬЗОВАНИЕ: Один Service для разных Controller'ov                      ║
 * ║  3. ТРАНЗАКЦИИ: @Transactional работает на уровне Service                        ║
 * ║  4. SINGLE RESPONSIBILITY: Каждый слой делает своё                              ║
 * ║                                                                                   ║
 * ╚════════════════════════════════════════════════════════════════════════════════╝
 *
 * @author BankStart Team
 * @version 1.0.0
 */
@Service  // Маркер для Spring - это Business Logic компонент
@RequiredArgsConstructor  // Lombok: конструктор с final полями (Dependency Injection)
@Slf4j  // Lombok: создает поле log для логирования
public class CalculationService {
    
    /*
     * ╔════════════════════════════════════════════════════════════════════════════════╗
     * ║                    DEPENDENCY INJECTION                                        ║
     * ║                                                                                ║
     * ║  ОБУЧЕНИЕ: DI (Dependency Injection)                                           ║
     * ║  ─────────────────────────────────────────────────────────────────────             ║
     * ║                                                                                ║
     * ║  Spring автоматически создаёт и внедряет зависимости.                        ║
     * ║                                                                                ║
     * ║  Способы DI:                                                                    ║
     * ║  1. Конструктор (RECOMMENDED) - @RequiredArgsConstructor                      ║
     * ║  2. @Autowired на поле (legacy, не рекомендуется)                             ║
     * ║  3. Setter injection (редко)                                                   ║
     * ║                                                                                ║
     * ║  Почему конструктор лучше?                                                    ║
     * ║  - Невозможно создать объект без зависимостей                                  ║
     * ║  - Поля final = immutable                                                      ║
     * ║  - Легко тестировать (mock в конструктор)                                     ║
     * ║                                                                                ║
     * ╚════════════════════════════════════════════════════════════════════════════════╝
     */
    
    /** Репозиторий заявок (final = внедряется через конструктор) */
    private final ApplicationRepository applicationRepository;
    
    /** Репозиторий результатов */
    private final SimulationResultRepository simulationResultRepository;
    
    /** Константы симуляции */
    private static final int ITERATIONS = 1000;
    private static final int MONTHS = 12;
    
    /**
     * Выполнить расчёт финансовой устойчивости (быстрый режим).
     * 
     * <p><b>ОБУЧЕНИЕ: @Transactional</b></p>
     * <ul>
     *   <li>Весь метод выполняется в одной транзакции</li>
     *   <li>Если ошибка - все изменения откатываются (rollback)</li>
     *   <li>Важно для финансовых операций!</li>
     * </ul>
     *
     * @param dto входные данные из формы
     * @param ipAddress IP-адрес пользователя
     * @param userAgent User-Agent браузера
     * @return результат симуляции
     */
    @Transactional
    public SimulationResult calculateQuick(QuickInputDTO dto, String ipAddress, String userAgent) {
        log.info("Начало расчёта для email: {}", dto.getEmail());
        long startTime = System.currentTimeMillis();
        
        // 1. Создание и сохранение заявки
        Application application = mapDtoToApplication(dto);
        application.setIpAddress(ipAddress);
        application.setUserAgent(userAgent);
        application = applicationRepository.save(application);
        
        log.debug("Заявка создана с ID: {}", application.getId());
        
        // 2. Запуск Monte Carlo симуляции
        SimulationResult result = runMonteCarloSimulation(application);
        
        long calculationTime = System.currentTimeMillis() - startTime;
        result.setCalculationTimeMs((int) calculationTime);
        
        // 3. Сохранение результата
        result = simulationResultRepository.save(result);
        
        log.info("Расчёт завершён за {}ms. Вероятность успеха: {}%", 
                 calculationTime, result.getSuccessProbability());
        
        return result;
    }
    
    /**
     * Запустить Monte Carlo симуляцию.
     * 
     * <p><b>ОБУЧЕНИЕ: ЧТО ТАКОЕ MONTE CARLO?</b></p>
     * <p>Monte Carlo - это метод статистического моделирования:</p>
     * <ol>
     *   <li>Прогоняем 1000 "вариантов будущего"</li>
     *   <li>В каждом варианте выручка случайно колеблется</li>
     *   <li>Считаем, в скольких случаях бизнес "выжил"</li>
     *   <li>Это и есть вероятность успеха</li>
     * </ol>
     * 
     * <p><b>ПРИМЕР:</b></p>
     * <p>Если из 1000 симуляций в 850 бизнес не ушёл в минус,
     * значит вероятность успеха = 85%.</p>
     *
     * @param application заявка с входными данными
     * @return результат симуляции
     */
    private SimulationResult runMonteCarloSimulation(Application application) {
        log.debug("Запуск Monte Carlo: {} итераций, {} месяцев", ITERATIONS, MONTHS);
        
        Random random = new Random();
        BigDecimal[] finalCashFlows = new BigDecimal[ITERATIONS];
        BigDecimal[] minCashFlows = new BigDecimal[ITERATIONS];
        int successCount = 0;
        
        // Извлекаем параметры
        BigDecimal revenue = application.getMonthlyRevenue();
        BigDecimal volatility = application.getRevenueVolatility()
            .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal fixedCosts = application.getFixedCosts();
        BigDecimal variableCostsPercent = application.getVariableCostsPercent()
            .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal initialCapital = application.getInitialCapital();
        BigDecimal taxRate = application.getTaxSystem().getTaxRate();
        
        // Запускаем симуляции
        for (int i = 0; i < ITERATIONS; i++) {
            BigDecimal cashFlow = initialCapital;
            BigDecimal minCash = initialCapital;
            boolean survived = true;
            
            for (int month = 1; month <= MONTHS; month++) {
                // Генерируем случайную выручку (нормальное распределение)
                double randomFactor = 1 + random.nextGaussian() * volatility.doubleValue();
                BigDecimal monthRevenue = revenue.multiply(BigDecimal.valueOf(randomFactor));
                
                // Переменные расходы
                BigDecimal variableCosts = monthRevenue.multiply(variableCostsPercent);
                
                // Налоги (УСН - от выручки, ОСН - от прибыли)
                BigDecimal profit = monthRevenue.subtract(fixedCosts).subtract(variableCosts);
                BigDecimal taxes = profit.compareTo(BigDecimal.ZERO) > 0 
                    ? profit.multiply(taxRate) 
                    : BigDecimal.ZERO;
                
                // Обновляем cash flow
                cashFlow = cashFlow.add(monthRevenue)
                    .subtract(fixedCosts)
                    .subtract(variableCosts)
                    .subtract(taxes);
                
                // Отслеживаем минимум
                if (cashFlow.compareTo(minCash) < 0) {
                    minCash = cashFlow;
                }
                
                // Проверяем "выживаемость"
                if (cashFlow.compareTo(BigDecimal.ZERO) < 0) {
                    survived = false;
                }
            }
            
            finalCashFlows[i] = cashFlow;
            minCashFlows[i] = minCash;
            
            if (survived) {
                successCount++;
            }
        }
        
        // Сортируем для расчёта перцентилей
        java.util.Arrays.sort(finalCashFlows);
        java.util.Arrays.sort(minCashFlows);
        
        // Вычисляем результаты
        BigDecimal successProbability = BigDecimal.valueOf(successCount)
            .multiply(BigDecimal.valueOf(100))
            .divide(BigDecimal.valueOf(ITERATIONS), 2, RoundingMode.HALF_UP);
        
        return SimulationResult.builder()
            .application(application)
            .iterations(ITERATIONS)
            .successProbability(successProbability)
            .finalP10(finalCashFlows[(int)(ITERATIONS * 0.10)])
            .finalP50(finalCashFlows[(int)(ITERATIONS * 0.50)])
            .finalP90(finalCashFlows[(int)(ITERATIONS * 0.90)])
            .minCashMonth(6) // TODO: вычислить реальный критический месяц
            .minCashP10(minCashFlows[(int)(ITERATIONS * 0.10)])
            .minCashP50(minCashFlows[(int)(ITERATIONS * 0.50)])
            .riskLevel(RiskLevel.fromSuccessProbability(successProbability.doubleValue()))
            .build();
    }
    
    /**
     * Преобразовать DTO в Entity.
     */
    private Application mapDtoToApplication(QuickInputDTO dto) {
        return Application.builder()
            .calculationMode(CalculationMode.QUICK)
            .industry(dto.getIndustry())
            .monthlyRevenue(dto.getMonthlyRevenue())
            .fixedCosts(dto.getFixedCosts())
            .variableCostsPercent(dto.getVariableCostsPercent())
            .initialCapital(dto.getInitialCapital())
            .taxSystem(dto.getTaxSystem())
            .email(dto.getEmail())
            .build();
    }
}
