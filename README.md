# Uwaga! Demo! 
Projekt jest demem kwalifikacyjnym stworzonym w mniej niż 8h roboczych i nigdy **nie powinien być wykorzystywany**, w tej formie, **na produkcji**

# Task Manager API
## Uruchomienie

```bash
# budowa + testy
./gradlew clean test

# start z wbudowaną bazą H2
./gradlew bootRun

# gotowy jar
./gradlew bootJar
java -jar build/libs/taskmanager-*.jar

```
---

## Endpointy

| Metoda | Ścieżka              | Opis                                |
| ------ | -------------------- | ----------------------------------- |
| POST   | /tasks               | utwórz zadanie                      |
| PATCH  | /tasks/{id}          | edytuj tytuł / opis / parentId      |
| PUT    | /tasks/{id}/complete | oznacz jako COMPLETED               |
| DELETE | /tasks/{id}          | rekurencyjne usunięcie              |
| GET    | /tasks               | lista z paginacją `after` / `limit` |
| GET    | /tasks/{id}/changes  | historia zmian                      |
| GET    | /tasks/export        | eksport **csv** / **jsonl**         |

---

## Przykłady cURL

```bash
# 1. utwórz zadanie
curl -X POST http://localhost:8080/tasks -H 'Content-Type: application/json' -H 'X-User-Id: alice' -d '{"title":"README","description":"-","visibility":"PUBLIC"}'

# 2. pobierz listę
curl http://localhost:8080/tasks?limit=5 -H 'X-User-Id: alice'

# 3. zakończ zadanie o ID 1
curl -X PUT http://localhost:8080/tasks/1/complete -H 'X-User-Id: alice'

# 4. eksport CSV
curl http://localhost:8080/tasks/export?format=csv -H 'X-User-Id: alice' -o tasks.csv
```

