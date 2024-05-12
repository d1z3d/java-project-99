### Hexlet tests and linter status:
[![Actions Status](https://github.com/d1z3d/java-project-99/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/d1z3d/java-project-99/actions)
[![Maintainability](https://api.codeclimate.com/v1/badges/67fc23206d876cac512e/maintainability)](https://codeclimate.com/github/d1z3d/java-project-99/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/67fc23206d876cac512e/test_coverage)](https://codeclimate.com/github/d1z3d/java-project-99/test_coverage)

### Описание проекта
[Task manager](https://task-manager-jp0k.onrender.com) - система управления задачами, подобная http://www.redmine.org/. Она позволяет ставить задачи, назначать исполнителей и менять их статусы.

### Используемые инструменты
* ЯП: Java 21
* Сборщик: Gradle 8.6
* Backend: Spring Boot
* Frontend: React, TypeScript, Vite
* ОРМ: Hibernate
* БД:
  * DEV: H2
  * PROD: PostgreSQL
* Тесты: JUnit 5
* Тест репортер: Jacoco
* Мониторинг [sentry.io](https://sentry.io)
* Деплой: PaaS на [render.com](https://render.com)
* Документация API: [swagger](https://task-manager-jp0k.onrender.com/swagger-ui/index.html)

Также в проекте используется Docker при деплое на PRODUCTION.

### Сборка проекта
Вам необходимо установить утилиту [make](https://guides.hexlet.io/ru/makefile-as-task-runner/?_gl=1*1b2sh59*_ga*NzQ5MzAxNTIzLjE2OTkyOTM2MTc.*_ga_PM3R85EKHN*MTcwMjIyNTQ0MS4xMDguMS4xNzAyMjI3OTYzLjYwLjAuMA..*_ga_WWGZ6EVHEY*MTcwMjIyNTQ0MS4xMTEuMS4xNzAyMjI3OTYzLjYwLjAuMA..)
```
make build
```

### Запуск проекта
```
make run-dist
```

### Пользователь для входа в приложение
```
login    - hexlet@example.com
password - qwerty
```

