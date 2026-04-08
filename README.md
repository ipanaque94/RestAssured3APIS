# REST Assured API Testing — Suite de Automatización

> Proyecto de automatización de pruebas de API construido con Java, RestAssured y TestNG,
> con pipeline CI/CD en GitHub Actions y reportes Allure publicados automáticamente.

🔗 **[Ver reporte Allure en vivo →](https://ipanaque94.github.io/RestAssured3APIS)**

---

## ¿Por qué existe este proyecto?

Cuando empecé a aprender QA Automation me di cuenta de que la mayoría de los proyectos
de práctica prueban una sola API con 5 tests. Quería construir algo más cercano a lo que
se hace en una empresa real: múltiples APIs, suites organizadas por propósito, pipeline
CI/CD que corre automáticamente y reportes que cualquier stakeholder pueda leer sin
entrar al código.

Este proyecto prueba **4 APIs distintas** (REST y SOAP), tiene **8 suites independientes**
y un pipeline que publica el reporte consolidado en GitHub Pages en cada push.

---

## ¿Qué se prueba y por qué?

| API | Protocolo | ¿Qué valida? |
|---|---|---|
| [Simple Books API](https://simple-books-api.glitch.me) | REST + Auth | Flujo completo con autenticación, CRUD de órdenes, validación de schema JSON |
| [Rick and Morty API](https://rickandmortyapi.com) | REST público | Validación de campos simples y anidados con JsonPath |
| [JSONPlaceholder](https://jsonplaceholder.typicode.com) | REST público | Operaciones CRUD: GET, POST, PUT, DELETE |
| [DataAccess](https://www.dataaccess.com) | SOAP/XML | Consumo de servicio SOAP con validación de respuesta XML |

La elección de estas APIs no fue casual. Simple Books tiene autenticación real con tokens,
lo que permitió cubrir seguridad, manejo de errores y flujos autenticados. JSONPlaceholder
cubre las operaciones HTTP básicas. SOAP está presente porque muchas empresas todavía
tienen servicios legacy con ese protocolo.

---

## Stack técnico

| Herramienta | Versión | Uso |
|---|---|---|
| Java | 17 | Lenguaje base |
| RestAssured | 5.3.1 | Cliente HTTP para pruebas de API |
| TestNG | 7.10.2 | Framework de pruebas con soporte de grupos y DataProviders |
| Allure | 2.25.0 | Reportes interactivos con trazabilidad completa |
| Jackson | 2.17.1 | Serialización/deserialización de objetos Java ↔ JSON |
| Gradle | 9.1.0 | Build y gestión de dependencias |
| GitHub Actions | — | CI/CD con jobs independientes por suite |

---

## Estructura del proyecto

```
src/test/java/APITests/
├── auth/              → Registro de clientes y obtención de token
├── books/             → CRUD de libros y órdenes (Simple Books API)
│   ├── BooksTest.java
│   ├── OrdersTest.java
│   ├── BooksSchemaValidation.java
│   └── IdempotenciaTest.java
├── rickandmorty/      → Validación de personajes
├── jsonplaceholder/   → Operaciones CRUD
├── dataaccess/        → Servicio SOAP
├── baseUrl/           → Clases base reutilizables (una por API)
└── utils/
    ├── Config.java    → Lee URLs desde env vars, -D flags o config.properties
    ├── Order.java
    └── OrderResponse.java

src/test/resources/
├── TestNG.xml                  → Suite completa
├── TestngSmoke.xml
├── TestngRegression.xml
├── TestngContract.xml
├── TestngSecurity.xml
├── TestngNegative.xml
├── TestngIdempotencia.xml
├── TestngPerformance.xml
├── TestsngSoap.xml
├── config.properties
└── schemas/
    └── books-schemas.json      → Schema JSON para Contract Testing
```

---

## Suites de prueba

Cada suite tiene un propósito claro y corre de forma independiente:

| Suite | Propósito | Qué valida |
|---|---|---|
| 🔥 **Smoke** | Verificación rápida post-deploy | El sistema responde y los flujos críticos funcionan |
| 🔁 **Regression** | Cobertura funcional completa | Que nada se rompió con los últimos cambios |
| 📋 **Contract** | Validación de esquema JSON | La estructura de la respuesta no cambió sin aviso |
| 🔒 **Security** | Autenticación y autorización | Tokens inválidos retornan 401, acceso correcto retorna 200 |
| ❌ **Negative** | Manejo de errores | El sistema responde correctamente ante datos inválidos |
| 🔄 **Idempotencia** | Métodos HTTP idempotentes | PATCH repetido = mismo resultado, DELETE repetido = 404 |
| ⚡ **Performance** | Tiempos de respuesta | El endpoint responde en menos de 2000ms |
| 🧼 **SOAP** | Servicios legacy XML | El servicio SOAP convierte números a palabras correctamente |

---

## Cómo ejecutar

**Requisitos:** Java 17, Gradle 9+

```bash
# Todas las suites
./gradlew clean test

# Suite específica
./gradlew clean test -Dsuite=Smoke
./gradlew clean test -Dsuite=Regression
./gradlew clean test -Dsuite=Security
./gradlew clean test -Dsuite=Negative
./gradlew clean test -Dsuite=Contract
./gradlew clean test -Dsuite=Idempotencia
./gradlew clean test -Dsuite=Performance
./gradlew clean test -Dsuite=SOAP
```

**Ver el reporte Allure localmente:**
```bash
allure serve build/allure-results
```

---

## Pipeline CI/CD

El pipeline corre automáticamente en cada push a `main`. Los jobs tienen dependencias
para que Smoke sea el primer gate — si falla, los demás no corren innecesariamente:

```
Push → 🔥 Smoke
           ├── 🔁 Regression ──→ 🔄 Idempotencia
           │                 └──→ ⚡ Performance
           ├── 📋 Contract
           ├── 🔒 Security
           ├── ❌ Negative
           └── 🧼 SOAP
                    └── 📊 Reporte consolidado → GitHub Pages
```

Cada job publica sus resultados como artefacto. Al final, el job `publish-report`
descarga todos los resultados, genera un reporte Allure consolidado y lo publica
automáticamente en GitHub Pages.

---

## Decisiones de diseño

Estas decisiones las tomé pensando en mantenibilidad y escalabilidad — no solo en
hacer que los tests pasen:

**Clase base por API (`BaseBooksTest`, `BaseRickMorty`, etc.)**
Cada API tiene su propia clase base con la configuración de URL y el `RequestSpec`.
Si cambia la URL de una API, se modifica en un solo lugar.

**`Config.java` con prioridad de configuración**
Lee la URL en este orden: variable de entorno → system property → `config.properties`.
Esto permite que CI inyecte URLs distintas por ambiente sin tocar el código.

**`@BeforeClass(alwaysRun = true)` con flag `initialized`**
TestNG crea una nueva instancia de la clase por cada suite que la incluye. Sin el flag
`static initialized`, el setup se ejecutaría múltiples veces y agotaría el rate limit
de la API al obtener el token.

**Un XML por suite en lugar de grupos en un solo XML**
Permite correr cada suite con un comando simple (`-Dsuite=Smoke`) y que el CI lance
jobs completamente independientes sin configuración adicional.

---

## Qué aprendí construyendo esto

Más allá del código, este proyecto me enseñó cosas que no están en los tutoriales:

- El `@BeforeClass` sin `alwaysRun = true` se salta cuando TestNG corre grupos específicos,
  dejando el `spec` nulo y fallando con un error confuso de "Specification cannot be null"
- `simple-books-api.click` está bloqueado por los runners de GitHub Actions — tuve que
  cambiar a `simple-books-api.glitch.me`, que es la misma API en un host diferente
- El `doFirst { delete }` de Gradle 9 es incompatible con el configuration cache —
  la solución es usar `./gradlew clean test` en lugar de borrar el directorio manualmente
- Los headers duplicados entre el `spec` y el `given()` pueden causar errores 400
  silenciosos en algunas APIs

---

## Autor

**Enoc Ipanaque**
[LinkedIn](www.linkedin.com/in/enoc-isaac-ipanaque-rodas-b3729a283) · [GitHub](https://github.com/ipanaque94)

Estudiante de QA Automation en proceso de certificación ISTQB CTFL.
Construido sobre los conocimientos del curso **REST Assured Avanzado**
de [Free Range Testers](https://www.freerangetesters.com).
