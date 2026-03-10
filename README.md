# Bank Transfer Service

Реализация REST-сервиса для внутренних банковских переводов между счетами. Проект выполнен в рамках тестового задания на позицию Java Backend Developer.

---
## Технологии

* **Backend**
    * Java 21 (LTS)
    * Spring Boot 4.0.3
    * Spring Data JPA
* **Database & Migrations**
    * PostgreSQL
    * Flyway (управление схемами БД)
* **Tools**
    * Maven
    * Docker & Docker Compose
   
---

## Архитектура проекта

Система построена по многослойной архитектуре (Layered Architecture):

* **Controller** — принимает HTTP-запросы и возвращает ответы.
* **Service** — бизнес-логика переводов и выписок, проверка правил, транзакции.
* **Repository** — работа с базой данных через JPA.
* **Entity** — данные для БД (`Account`, `Transaction`).
* **DTO** — объекты для передачи данных между клиентом и сервером.
* **GlobalExceptionHandler** — обработка ошибок и возвращение корректных HTTP-статусов.  

---

## Функционал / API

### 1. Перевод средств

**POST** `/api/transfers`  
**Описание:** перевод с одного счета на другой внутри банка.

**Пример запроса:**

```json
{
    "senderAccountNumber": "ACC00000000000000002",
    "receiverAccountNumber": "ACC00000000000000001",
    "amount": 20.00
}
```

**Пример ответа (успешно)**
```json
{
    "status": "SUCCESS",
    "amount": 20.00,
    "executedAt": "2026-03-10T17:19:06.26905+06:00"
}
```

### 2. Выписка по счету

**GET** `/api/accounts/{accountNumber}/statement`  
**Описание:** получить операции по счету с фильтром по дате и пагинацией.

**Path параметр:**

- `accountNumber` — номер счета (строка, 20 символов)

**Query параметры:**

- `from` — дата начала периода (yyyy-MM-dd)
- `to` — дата конца периода (yyyy-MM-dd)
- `page` — номер страницы (default=0)
- `size` — количество записей на странице (default=10, max=50)

**Пример запроса:**

**http://localhost:8080/api/accounts/99999999999999999999/statement?from=2026-03-01&to=2026-03-10&page=0&size=10**

**Пример ответа (успешно)**

```json
{
  "accountNumber": "99999999999999999999",
  "transactions": [
    {
      "operationDate": "2026-03-09T23:47:03.878146Z",
      "status": "FAILED",
      "operationType": "TRANSFER",
      "amount": 2000.00,
      "balanceAfter": 35.73
    },
    {
      "operationDate": "2026-03-09T23:46:59.610732Z",
      "status": "SUCCESS",
      "operationType": "TRANSFER",
      "amount": 2000.00,
      "balanceAfter": 35.73
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 2,
  "totalPages": 1
}
```
---

## Установка и запуск

### Требования

- Docker
- Docker Compose

### Запуск приложения

Запустите приложение и базу данных PostgreSQL одной командой:  docker compose up 

После запуска сервис будет доступен по адресу: http://localhost:8080

---

## Автор: Усенов  Бактияр



