# API Yo'riqnomasi

## Kategoriyalar API

### 1. Barcha kategoriyalarni olish

**GET** `/telephone/categories?sort=id,asc`

#### Javob namuna:

```json
[
  {
    "id": 1,
    "name": "Elektronika",
    "parent": null
  },
  {
    "id": 2,
    "name": "Smartfonlar",
    "parent": {
      "id": 1,
      "name": "Elektronika"
    }
  }
]
```

---

### 2. Bitta kategoriyani olish

**GET** `/telephone/categories/{id}`

#### Namuna so'rov:

```
GET /telephone/categories/1
```

#### Javob namuna:

```json
{
  "name": "1 harharakat",
  "_embedded": {
    "parent": {
      "name": "2 harakat",
      "parent": {
        "name": "Electronics"
      },
      "id": 7
    }
  }
}
```

---

### 3. Yangi kategoriya qo'shish

**POST** `/telephone/categories`

#### Namuna so'rov:

```json
{
  "name": "Kadrlar",
  "parent": "categories/1"
}
```

#### Javob namuna:

```json
{
  "id": 3,
  "name": "Kadrlar",
  "parent": {
    "id": 1,
    "name": "Elektronika"
  }
}
```

---

### 4. Kategoriyani o'zgartirish

**PATCH** `/telephone/categories/{{id}}`

#### Namuna so'rov:

`PATCH /telephone/categories/11`

```json
{
  "name": "10 harharakat",
  "parent": "categories/7"
}
```

#### Javob namuna:

```json
{
  "name": "10 harharakat",
  "_embedded": {
    "parent": {
      "name": "2 harakat",
      "parent": {
        "name": "Electronics"
      },
      "id": 7
    }
  }
}
```

---

### 5. Kategoriyani o'chirish

**DELETE** `/telephone/categories/{id}`

#### Namuna so'rov:

```
DELETE /telephone/categories/3
```

#### Javob:

```json
{
  "id": 10,
  "name": "1 harharakat",
  "parent": {
    "id": 7,
    "name": "2 harakat",
    "parent": {
      "id": 1,
      "name": "Electronics",
      "parent": null
    }
  }
}
```

---

## Tablelar API

### 1. Barcha tableslarni olish

**GET** `/telephone/tables?sort=id,asc`

#### Javob namuna:

```json
{
  "_embedded": {
    "tables": [
      {
        "name": "Ali Valiyev",
        "shortNum": "1234",
        "longNum": "+998901234567",
        "dateBirth": "1990-01-01",
        "_embedded": {
          "category": {
            "name": "Electronics",
            "parent": null,
            "id": 1
          }
        }
      }
    ]
  },
  "page": {
    "size": 20,
    "totalElements": 1,
    "totalPages": 1,
    "number": 0
  }
}
```

---

### 2. Bitta tableni olish

**GET** `/telephone/tables/{id}`

#### Namuna so'rov:

```
GET /telephone/tables/1
```

#### Javob namuna:

```json
{
  "name": "Ali Valiyev",
  "shortNum": "1234",
  "longNum": "+998901234567",
  "dateBirth": "1990-01-01",
  "_embedded": {
    "category": {
      "name": "Electronics",
      "parent": null,
      "id": 1
    }
  }
}
```

### 3. table qo'shish

**POST** `/telephone/tables`

#### Namuna so'rov:

```json



{
  "name": "Ali Valiyev",
  "shortNum": "1234",
  "longNum": "+998901234567",
  "dateBirth": "1990-01-01",
  "category": "/api/categories/1"
}
```

#### Javob:

```json
{
  "name": "Ali Valiyev",
  "shortNum": "1234",
  "longNum": "+998901234567",
  "dateBirth": "1990-01-01",
  "_embedded": {
    "name": "Electronics",
    "parent": null,
    "id": 1
  }
}
```

### 4. table o'zgartirish

**PATCH** `/telephone/tables/{id}`

#### Namuna so'rov:

```
PATCH /telephone/tables/1
```

```json

{
  "name": "Ali Valiyev",
  "shortNum": "1234",
  "longNum": "+998901234567",
  "dateBirth": "1990-01-01",
  "category": "/api/categories/1"
}
```

#### Javob:

```json
{
  "name": "Ali Valiyev",
  "shortNum": "1234",
  "longNum": "+998901234567",
  "dateBirth": "1990-01-01",
  "_embedded": {
    "category": {
      "name": "Electronics",
      "parent": null,
      "id": 1
    }
  }
}
```

### 5. table o'chirish

**DELETE** `/telephone/tables/{id}`

#### Namuna so'rov:

```
DELETE /telephone/tables/1
```

#### Javob:

```json
{
  "name": "Ali Valiyev",
  "shortNum": "1234",
  "longNum": "+998901234567",
  "dateBirth": "1990-01-01",
  "_embedded": {
    "category": {
      "name": "Electronics",
      "parent": null,
      "id": 1
    }
  }
}
```
