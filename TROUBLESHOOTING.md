# Руководство по устранению неполадок BankStart MVP

## Ошибка 404 NOT_FOUND для статических ресурсов

### Описание проблемы

При обращении к приложению DispatcherServlet возвращал ошибку 404. В логах были видны запросы с Accept-заголовками:
```
Accept: image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8
```

### Причина ошибки

**Основная причина**: В HTML-шаблоне `layout.html` была ссылка на файл `/images/favicon.png`, но сам файл отсутствовал в папке `src/main/resources/static/images/`.

Браузеры автоматически запрашивают favicon при загрузке каждой страницы. Когда файл не найден, Spring Boot возвращает 404.

### Как проблема была диагностирована

1. **Проверка структуры static/**:
   ```bash
   ls -la src/main/resources/static/
   find src/main/resources/static -type f
   ```
   
2. **Поиск ссылок на статические ресурсы в шаблонах**:
   ```bash
   grep -rn "images/" src/main/resources/templates/
   ```

3. **Анализ Accept-заголовка в логах** — указывал на запрос изображения

### Исправление

1. Создан файл `favicon.ico` в `src/main/resources/static/`
2. Создан файл `favicon.png` в `src/main/resources/static/images/`
3. Обновлён `layout.html` для поддержки разных форматов favicon

---

## Работа со статическими ресурсами в Spring Boot

### Расположение файлов

Spring Boot автоматически обслуживает статические ресурсы из следующих папок:

```
src/main/resources/
├── static/           ← Основная папка для статики
│   ├── css/
│   ├── js/
│   ├── images/
│   └── favicon.ico
├── public/           ← Альтернативная папка
└── templates/        ← Только для Thymeleaf шаблонов!
```

### Правильные ссылки в Thymeleaf

```html
<!-- ✅ Правильно — использование th:href и @{} -->
<link th:href="@{/css/custom.css}" rel="stylesheet">
<script th:src="@{/js/app.js}"></script>
<img th:src="@{/images/logo.png}" alt="Logo">

<!-- ❌ Неправильно — жёстко прописанные пути -->
<link href="/css/custom.css" rel="stylesheet">
<img src="/images/logo.png" alt="Logo">
```

### Почему использовать `@{}`?

1. **Context-path** — автоматически добавляет контекстный путь приложения
2. **URL encoding** — корректно кодирует специальные символы
3. **Кеширование** — можно добавить версионирование

---

## Чек-лист для диагностики 404

### Шаг 1: Проверить наличие файла
```bash
ls -la src/main/resources/static/путь/к/файлу
```

### Шаг 2: Проверить шаблоны на битые ссылки
```bash
# Найти все ссылки на изображения
grep -rn "images/" src/main/resources/templates/

# Найти все th:src и th:href
grep -rn "th:src\|th:href" src/main/resources/templates/
```

### Шаг 3: Проверить логи приложения
```bash
# Смотрим логи в реальном времени
tail -f logs/app.log

# Или в консоли при запуске
./mvnw spring-boot:run
```

### Шаг 4: Проверить через браузер DevTools
1. Открыть DevTools (F12)
2. Вкладка Network
3. Перезагрузить страницу
4. Искать запросы со статусом 404

---

## Частые причины 404 в Spring Boot

| Проблема | Решение |
|----------|---------|
| Файл favicon.ico отсутствует | Создать файл в `static/` |
| Неправильный путь в шаблоне | Использовать `@{/path}` |
| Файл в неправильной папке | Переместить в `static/` |
| Typo в имени файла | Проверить регистр букв |
| Кеширование браузера | Очистить кеш / Ctrl+Shift+R |

---

## Конфигурация статических ресурсов

### application.properties

```properties
# Изменить путь к статическим ресурсам (по умолчанию /static)
spring.web.resources.static-locations=classpath:/static/,classpath:/public/

# Включить кеширование для продакшена
spring.web.resources.cache.period=31536000

# Версионирование ресурсов
spring.web.resources.chain.strategy.content.enabled=true
spring.web.resources.chain.strategy.content.paths=/**
```

### Кастомная конфигурация (WebMvcConfigurer)

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/assets/")
                .setCachePeriod(3600);
    }
}
```

---

## Полезные команды для отладки

```bash
# Проверить все статические ресурсы
find src/main/resources/static -type f | sort

# Проверить размеры файлов (0 байт = пустой файл)
find src/main/resources/static -type f -exec ls -la {} \;

# Найти дубликаты
find src/main/resources/static -type f -name "*.css" -o -name "*.js"

# Проверить права доступа
ls -la src/main/resources/static/

# Тест доступности ресурса через curl
curl -I http://localhost:8080/images/favicon.png
```

---

## Резюме

1. **Всегда проверяйте наличие favicon** — браузеры запрашивают его автоматически
2. **Используйте синтаксис Thymeleaf** `@{/path}` для всех ссылок на статику
3. **Проверяйте DevTools/Network** для быстрой диагностики 404
4. **Держите структуру static/ организованной** — css/, js/, images/
5. **Добавляйте логирование** в application.properties для отладки:
   ```properties
   logging.level.org.springframework.web=DEBUG
   ```
