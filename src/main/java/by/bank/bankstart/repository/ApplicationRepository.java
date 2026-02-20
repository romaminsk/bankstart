package by.bank.bankstart.repository;

import by.bank.bankstart.model.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║                     ApplicationRepository                                        ║
 * ╠══════════════════════════════════════════════════════════════════════════════╣
 * ║                                                                                   ║
 * ║  ОБУЧЕНИЕ: SPRING DATA JPA REPOSITORY                                              ║
 * ║  ─────────────────────────────────────────────────────────────────────────          ║
 * ║                                                                                   ║
 * ║  ПАТТЕРН REPOSITORY (Data Access Layer)                                           ║
 * ║                                                                                   ║
 * ║  Наследуя JpaRepository, мы АВТОМАТИЧЕСКИ получаем:                                ║
 * ║                                                                                   ║
 * ║  CRUD операции:                                                                  ║
 * ║  ────────────────────────────────────────────────────────────────────────          ║
 * ║  - save(entity)           - сохранить/обновить                                    ║
 * ║  - findById(id)           - найти по ID                                           ║
 * ║  - findAll()              - получить все                                           ║
 * ║  - deleteById(id)         - удалить по ID                                         ║
 * ║  - count()                - подсчитать количество                                    ║
 * ║  - existsById(id)         - проверить существование                                ║
 * ║                                                                                   ║
 * ║  Пагинация и сортировка:                                                         ║
 * ║  ────────────────────────────────────────────────────────────────────────          ║
 * ║  - findAll(Pageable)      - с пагинацией                                          ║
 * ║  - findAll(Sort)          - с сортировкой                                          ║
 * ║                                                                                   ║
 * ║  Query Methods (магия Spring Data):                                                 ║
 * ║  ────────────────────────────────────────────────────────────────────────          ║
 * ║  Spring автоматически генерирует SQL на основе имени метода!                ║
 * ║                                                                                   ║
 * ║  findByEmail(email)       → SELECT * FROM applications WHERE email = ?           ║
 * ║  findByPublicId(uuid)     → SELECT * FROM applications WHERE public_id = ?       ║
 * ║  findByIndustry(industry) → SELECT * FROM applications WHERE industry = ?        ║
 * ║                                                                                   ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 *
 * @author BankStart Team
 * @version 1.0.0
 */
@Repository  // Маркер для Spring - это Data Access компонент
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    
    /**
     * Найти заявку по публичному UUID.
     * 
     * <p><b>ОБУЧЕНИЕ: Query Method Naming</b></p>
     * <p>Spring парсит имя метода и генерирует запрос:</p>
     * <pre>
     * findByPublicId(uuid)
     *   │    │
     *   │    └── Имя поля в Entity (publicId)
     *   └─────── findBy = SELECT ... WHERE
     * 
     * Генерируется:
     * SELECT * FROM applications WHERE public_id = :uuid
     * </pre>
     *
     * @param publicId публичный UUID заявки
     * @return Optional с заявкой или пустой
     */
    Optional<Application> findByPublicId(UUID publicId);
    
    /**
     * Найти все заявки по email.
     * 
     * @param email email пользователя
     * @return список заявок
     */
    java.util.List<Application> findByEmail(String email);
    
    /**
     * Проверить, существует ли заявка с данным UUID.
     *
     * @param publicId публичный UUID
     * @return true если существует
     */
    boolean existsByPublicId(UUID publicId);
}
