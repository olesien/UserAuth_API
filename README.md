
### db.properties

```
db.url=jdbc:postgresql://localhost:5432/news_website
db.username=postgres
db.password=postgres
```

### Db (postgres):
```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(255),
    age SMALLINT,
    gender VARCHAR(5),
    password TEXT
);

ALTER TABLE users ADD COLUMN cookie_consent BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE users ADD COLUMN data_consent BOOLEAN NOT NULL DEFAULT FALSE;
```

