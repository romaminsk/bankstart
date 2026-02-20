package by.bank.bankstart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║                     BankStart MVP - Main Application                          ║
 * ╠══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                               ║
 * ║  НАЗНАЧЕНИЕ:                                                                  ║
 * ║  Главный класс приложения - точка входа для Spring Boot.                      ║
 * ║                                                                               ║
 * ║  @SpringBootApplication - это мета-аннотация, объединяющая:                   ║
 * ║                                                                               ║
 * ║  1. @Configuration                                                            ║
 * ║     - Класс является источником bean definitions                              ║
 * ║     - Аналог XML-конфигурации в современном стиле                             ║
 * ║                                                                               ║
 * ║  2. @EnableAutoConfiguration                                                  ║
 * ║     - Автоматическая настройка Spring на основе classpath                     ║
 * ║     - Видит spring-boot-starter-web → настраивает Tomcat                      ║
 * ║     - Видит spring-boot-starter-data-jpa → настраивает Hibernate              ║
 * ║                                                                               ║
 * ║  3. @ComponentScan                                                            ║
 * ║     - Сканирует пакет by.bank.bankstart и вложенные                           ║
 * ║     - Находит @Controller, @Service, @Repository, @Component                  ║
 * ║     - Регистрирует их как Spring Beans                                        ║
 * ║                                                                               ║
 * ║  ЗАПУСК ПРИЛОЖЕНИЯ:                                                           ║
 * ║  1. mvn spring-boot:run                                                       ║
 * ║  2. java -jar target/bankstart-mvp-1.0.0-SNAPSHOT.jar                         ║
 * ║                                                                               ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 *
 * @author BankStart Team
 * @version 1.0.0
 * @since 2026-02-20
 */
@SpringBootApplication
public class BankStartApplication {

    /**
     * Точка входа в приложение.
     * 
     * <p>SpringApplication.run() выполняет:</p>
     * <ol>
     *   <li>Создание ApplicationContext (контейнер Spring)</li>
     *   <li>Сканирование компонентов (@Component, @Service, etc.)</li>
     *   <li>Инициализация всех Bean'ов</li>
     *   <li>Запуск встроенного Tomcat сервера</li>
     *   <li>Готовность к обработке HTTP запросов</li>
     * </ol>
     *
     * @param args аргументы командной строки (передаются в Spring Environment)
     */
    public static void main(String[] args) {
        SpringApplication.run(BankStartApplication.class, args);
    }

}
