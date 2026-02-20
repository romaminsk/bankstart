# 🏦 BankStart MVP

**Калькулятор финансовой устойчивости для малого бизнеса**

Веб-приложение для оценки жизнеспособности бизнеса на 12 месяцев вперёд с использованием метода Monte Carlo.

---

## 📋 Оглавление

- [О проекте](#о-проекте)
- [Технологии](#технологии)
- [Быстрый старт](#быстрый-старт)
- [Структура проекта](#структура-проекта)
- [Архитектура](#архитектура)
- [API Reference](#api-reference)
- [Roadmap](#roadmap)

---

## 🎯 О проекте

BankStart помогает предпринимателям:

1. **Оценить вероятность успеха** — какова вероятность того, что бизнес не уйдёт в минус за год?
2. **Найти критические месяцы** — когда cash flow будет минимальным?
3. **Получить рекомендации** — нужна ли финансовая подушка или кредит?

### Как это работает?

```
Пользователь вводит данные → Monte Carlo симуляция (1000 сценариев) → Результаты
```

**Monte Carlo** — это статистический метод, который:
- Прогоняет 1000 "вариантов будущего"
- В каждом варианте выручка случайно колеблется
- Считает, в скольких случаях бизнес "выжил"

---

## 🛠 Технологии

| Компонент | Технология | Почему выбрана |
|-----------|------------|----------------|
| **Backend** | Java 21, Spring Boot 3.2 | LTS версия, enterprise-grade |
| **Frontend** | Thymeleaf, Bootstrap 5 | Server-side rendering, быстрая разработка |
| **Database** | PostgreSQL 15 / H2 | Надёжность + удобство разработки |
| **Build** | Maven | Банковский стандарт |
| **Graphs** | Chart.js | Легковесный, современный |

---

## 🚀 Быстрый старт

### Требования

- Java 21+ ([скачать](https://adoptium.net/))
- Maven 3.9+ ([скачать](https://maven.apache.org/download.cgi))

### Запуск

```bash
# Клонировать/перейти в директорию
cd /home/ubuntu/bankstart_mvp

# Запустить приложение
mvn spring-boot:run

# Или собрать JAR и запустить
mvn clean package
java -jar target/bankstart-mvp-1.0.0-SNAPSHOT.jar
```

### Доступ

- **Приложение:** http://localhost:8080
- **H2 Console:** http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:bankstart`
  - Username: `sa`
  - Password: (пусто)

---

## 📁 Структура проекта

```
bankstart_mvp/
├── src/
│   ├── main/
│   │   ├── java/by/bank/bankstart/
│   │   │   ├── BankStartApplication.java    # Точка входа
│   │   │   ├── controller/                  # HTTP Handlers
│   │   │   │   └── HomeController.java
│   │   │   ├── service/                     # Бизнес-логика
│   │   │   │   └── CalculationService.java
│   │   │   ├── repository/                  # Data Access
│   │   │   │   ├── ApplicationRepository.java
│   │   │   │   └── SimulationResultRepository.java
│   │   │   └── model/
│   │   │       ├── entity/                  # JPA Entities
│   │   │       │   ├── Application.java
│   │   │       │   └── SimulationResult.java
│   │   │       ├── dto/                     # Data Transfer Objects
│   │   │       │   ├── QuickInputDTO.java
│   │   │       │   └── ExtendedInputDTO.java
│   │   │       └── enums/                   # Enumerations
│   │   │           ├── CalculationMode.java
│   │   │           ├── TaxSystem.java
│   │   │           ├── Seasonality.java
│   │   │           └── RiskLevel.java
│   │   └── resources/
│   │       ├── application.properties       # Конфигурация
│   │       ├── templates/                   # Thymeleaf шаблоны
│   │       │   ├── index.html
│   │       │   ├── fragments/layout.html
│   │       │   └── calculator/quick.html
│   │       └── static/                      # CSS, JS, Images
│   └── test/                                # Тесты
├── pom.xml                                  # Maven dependencies
├── README.md                                # Этот файл
└── EXPLANATIONS.md                          # Подробные объяснения
```

---

## 🏗 Архитектура

### Layered Architecture (Слоистая архитектура)

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                        │
│                                                              │
│  ┌─────────────────┐  ┌─────────────────────────────────┐   │
│  │   Controller    │  │       Thymeleaf Templates        │   │
│  │  (HomeController)│  │   (index.html, quick.html)      │   │
│  └────────┬────────┘  └─────────────────────────────────┘   │
│           │                                                  │
├───────────┼──────────────────────────────────────────────────┤
│           ▼                                                  │
│                      SERVICE LAYER                           │
│                                                              │
│  ┌───────────────────────────────────────────────────────┐  │
│  │              CalculationService                        │  │
│  │     - Бизнес-логика                                   │  │
│  │     - Monte Carlo симуляция                           │  │
│  │     - Валидация данных                                │  │
│  └────────────────────────┬──────────────────────────────┘  │
│                           │                                  │
├───────────────────────────┼──────────────────────────────────┤
│                           ▼                                  │
│                 REPOSITORY LAYER (DAO)                       │
│                                                              │
│  ┌──────────────────────┐  ┌─────────────────────────────┐  │
│  │ ApplicationRepository│  │ SimulationResultRepository  │  │
│  └──────────────────────┘  └─────────────────────────────┘  │
│                                                              │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│                    DATABASE (H2 / PostgreSQL)                │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

### Почему Layered Architecture?

| Преимущества | Описание |
|--------------|----------|
| **Разделение ответственности** | Каждый слой делает своё дело |
| **Тестируемость** | Service можно тестировать без HTTP |
| **Гибкость** | Можно заменить слой без изменения других |
| **Понятность** | Стандарт для Java enterprise |

---

## 📡 API Reference

### Страницы (Thymeleaf)

| URL | Метод | Описание |
|-----|-------|----------|
| `/` | GET | Главная страница |
| `/calculator/quick` | GET | Форма быстрого расчёта |
| `/calculator/quick/calculate` | POST | Выполнение расчёта |
| `/about` | GET | О проекте |
| `/disclaimer` | GET | Отказ от ответственности |

### REST API (планируется)

```json
POST /api/v1/calculate
{
  "industry": "Общепит",
  "monthlyRevenue": 10000,
  "fixedCosts": 3000,
  "variableCostsPercent": 30,
  "initialCapital": 5000,
  "taxSystem": "USN",
  "email": "user@example.com"
}
```

---

## 🗺 Roadmap

### Этап 1: Базовая структура ✅
- [x] Spring Boot проект
- [x] JPA Entities
- [x] Thymeleaf шаблоны
- [x] Landing page

### Этап 2: Monte Carlo Engine (В работе)
- [x] Базовая симуляция
- [ ] Сезонность
- [ ] Расширенный режим

### Этап 3: Результаты
- [ ] Страница результатов
- [ ] Графики Chart.js
- [ ] PDF экспорт

### Этап 4: AI Summary
- [ ] Интеграция Ollama
- [ ] Fallback шаблоны

### Этап 5: Production
- [ ] Docker Compose
- [ ] PostgreSQL
- [ ] Security

---

## 📚 Дополнительно

- **EXPLANATIONS.md** — подробные объяснения всех архитектурных решений
- **bankstart_design/** — проектная документация

---

## 👥 Команда

BankStart MVP разработан как образовательный проект для демонстрации:
- Spring Boot архитектуры
- Паттернов проектирования
- Best practices Java разработки

---

## ⚠️ Disclaimer

Результаты калькулятора носят **исключительно информационный характер** и не являются:
- Финансовой рекомендацией
- Гарантией получения кредита
- Обещанием результата

---

*© 2026 BankStart MVP. Образовательный проект.*
