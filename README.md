# REST Assured API Testing Framework

> Proyecto personal de automatización de pruebas de API construido mientras aprendía QA Automation.
> Incluye 4 APIs, 8 suites de prueba organizadas por propósito, pipeline CI/CD con GitHub Actions
> y reportes Allure publicados automáticamente en GitHub Pages.

**[Ver reporte Allure en vivo](https://ipanaque94.github.io/RestAssured3APIS)**

---

## Por qué construí esto

Cuando empecé a estudiar QA Automation me di cuenta de que la mayoría de proyectos de práctica
consisten en 5 o 6 tests contra una sola API sin ninguna organización real. Quería entender
cómo trabaja un QA en una empresa de verdad, así que me propuse construir algo con la estructura
que tendría un proyecto profesional.

El proceso no fue lineal. Hubo errores que me tomaron días resolver: tokens que se agotaban
porque el `@BeforeClass` corría más veces de las necesarias, tests que pasaban en CI pero
fallaban en local porque una API bloqueaba mi IP, specs que llegaban nulos a los tests por
el orden de ejecución de TestNG. Cada uno de esos errores me enseñó algo que no aparece en
ningún tutorial.

---

## Qué se prueba y por qué elegí cada API

| API | Tipo | Por qué la elegí |
|---|---|---|
| Simple Books API | REST + Auth | Es la única que encontre por el momento con autenticación real con tokens, lo que permite cubrir seguridad, flujos autenticados y manejo de errores de autorización |
| Rick and Morty API | REST público | Tiene respuestas con objetos anidados, ideal para practicar JsonPath en campos como `location.name` y `origin.url` |
| JSONPlaceholder | REST público | API simple y estable, perfecta para cubrir el ciclo completo: GET, POST, PUT, DELETE sin complicaciones de autenticación |
| DataAccess | SOAP/XML | Muchas empresas todavía tienen servicios legacy con SOAP. Quería entender cómo difiere de REST en headers, body y manejo de respuesta |

---

## Análisis de casos de prueba por suite

### Smoke — "¿El sistema responde?"

El propósito del Smoke no es encontrar bugs profundos sino confirmar que después de un
deploy el sistema está vivo. Por eso solo incluye el test más representativo de cada API.


### Regression — "¿Sigue funcionando todo lo que funcionaba antes?"

Aquí están los tests de funcionalidad completa. Incluye el `DataProvider` que prueba
libros con IDs 1, 2 y 3 en una sola definición de test. La lógica detrás es la técnica
de partición de equivalencia del ISTQB: si el libro con ID 1 pasa, hay alta probabilidad
de que el de ID 2 también, pero vale la pena verificarlo explícitamente porque cada ID
podría tener datos diferentes en el servidor.

### Contract — "¿La estructura de la respuesta cambió sin avisarnos?"

Este es uno de los tests más valiosos y menos obvios cuando empiezas en QA. Cuando una
API cambia el nombre de un campo o agrega campos obligatorios, tus tests funcionales
podrían seguir pasando si no validas la estructura completa.

La validación con JSON Schema captura exactamente eso: si el equipo de backend cambia
`"id"` por `"bookId"` en la respuesta, el test de Contract lo detecta aunque el GET
siga devolviendo 200.

### Security — "¿El sistema protege correctamente sus recursos?"

Probé tres escenarios distintos:
- Token completamente inválido (`Bearer TokenInvalido`) → debe dar 401
- Request sin token → debe dar 401
- Token válido pero con cuerpo incorrecto → debe dar 400, no 401

La distinción entre 401 y 403 también importa: 401 significa que no estás autenticado,
403 significa que estás autenticado pero no tienes permisos. Confundirlos en una
implementación es un bug de seguridad real.

### Negative — "¿Qué pasa cuando el usuario hace algo que no debería?"

Los tests negativos son los que los desarrolladores menos prueban y los que más revelan
sobre la calidad real de un sistema. Cubrí:

- Cuerpo vacío en POST `/orders` → ¿Da 400 con mensaje descriptivo o da 500 genérico?
- Libro con ID inexistente (999) → ¿Da 404 o da 200 con body vacío?
- Campos obligatorios faltantes en `/api-clients` → ¿El mensaje de error identifica
  cuál campo falta?

Este último punto es importante: un API bien diseñada no dice solo "error 400", sino
"Invalid or missing client email". Eso le permite al cliente de la API saber exactamente
qué corregir.

### Idempotencia — "¿Los métodos HTTP se comportan como el protocolo dice?"

Esta suite surgió de estudiar el protocolo HTTP. Según la especificación, PATCH y DELETE
son idempotentes: ejecutarlos múltiples veces con los mismos datos debe producir el mismo
resultado que ejecutarlos una vez.

Verifiqué dos casos:
- PATCH dos veces con el mismo body → ambas respuestas deben ser 204
- DELETE dos veces → primera debe ser 204, segunda debe ser 404

El segundo caso es especialmente importante: si el segundo DELETE devolviera 204 también
(como si la orden todavía existiera), indicaría un problema en la lógica del servidor.

### Performance — "¿El sistema responde en tiempo razonable?"

Aunque una prueba de rendimiento real requiere JMeter o k6 con carga concurrente, agregar
una validación de tiempo de respuesta en los tests de API tiene valor: detecta degradaciones
obvias y establece un contrato de SLA mínimo documentado en el código.

El umbral de 2000ms es conservador para una API pública. En un proyecto real, este número
vendría del SLA acordado con el equipo de producto.

### SOAP — "¿Los servicios legacy responden correctamente?"

SOAP fue el estándar de integración entre sistemas durante 15 años. Aunque REST lo reemplazó
en la mayoría de sistemas nuevos, muchas empresas de banca, seguros y gobierno todavía
los usan. Probé la conversión de números a palabras porque es un caso de uso simple que
permite concentrarse en entender las diferencias del protocolo sin complejidad de negocio.

---

## Decisiones técnicas que aprendí en el proceso

**Por qué `Config.java` lee en ese orden: env var → system property → properties file**

Esto permite que el mismo código corra en tres contextos sin cambios:
en local lee del `config.properties`, en CI lee de las variables de entorno inyectadas
como secrets, y si alguien necesita sobreescribir temporalmente usa `-Dproperty=value`.

**Por qué hay una clase base por API en lugar de configurar todo en cada test**

Si la URL de una API cambia, se modifica en un solo lugar. Si el header de autenticación
cambia, se modifica en un solo lugar. Los tests hijos se concentran solo en la lógica
de prueba, no en la configuración.

**Por qué `@BeforeClass(alwaysRun = true)` en las clases base**

Sin `alwaysRun = true`, cuando TestNG ejecuta una suite que filtra por grupos, se salta
el `@BeforeClass` porque técnicamente "no pertenece al grupo". El resultado es que `spec`
llega nulo a los tests y falla con un error confuso de "Specification to merge cannot be null".
Fue uno de los errores que más tiempo me tomó entender.

**Por qué un XML por suite en lugar de un solo XML con grupos**

Permite que el CI lance jobs completamente independientes con un solo parámetro
(`-Dsuite=Smoke`) y que cada job suba sus propios resultados de Allure como artefacto
para el reporte consolidado final.

---

## Cómo ejecutar

Requisitos: Java 17, Gradle 9+

```bash
# Clonar el repositorio
git clone https://github.com/ipanaque94/RestAssured3APIS.git
cd RestAssured3APIS

# Ejecutar todas las suites
./gradlew clean test

# Ejecutar una suite específica
./gradlew clean test -Dsuite=Smoke
./gradlew clean test -Dsuite=Regression
./gradlew clean test -Dsuite=Security
./gradlew clean test -Dsuite=Negative
./gradlew clean test -Dsuite=Contract
./gradlew clean test -Dsuite=Idempotencia
./gradlew clean test -Dsuite=Performance
./gradlew clean test -Dsuite=SOAP

# Ver reporte Allure localmente
allure serve build/allure-results
```

---

## Pipeline CI/CD

GitHub Actions corre automáticamente en cada push a `main`. Los jobs tienen dependencias
para que Smoke funcione como gate de calidad:

```
Push a main
    |
    v
Smoke (si falla, los demás no se bloquean gracias a always())
    |
    +---> Regression ---> Idempotencia
    |               \---> Performance
    +---> Contract
    +---> Security
    +---> Negative
    +---> SOAP
    |
    v
Reporte Consolidado → GitHub Pages (publicado automáticamente)
```

Las URLs de las APIs se configuran como secrets en GitHub para no exponerlas en el código.

---

## Stack

| Herramienta | Versión | Uso |
|---|---|---|
| Java | 17 | Lenguaje base |
| Rest Assured | 5.3.1 | Cliente HTTP para pruebas de API |
| TestNG | 7.10.2 | Test runner con grupos, DataProviders y dependencias entre tests |
| Allure | 2.25.0 | Reportes con severidad, descripción y trazabilidad por test |
| Jackson | 2.17.1 | Serialización de objetos Java a JSON y deserialización de respuestas |
| Gradle | 9.1.0 | Build y gestión de dependencias |
| GitHub Actions | — | CI/CD con jobs independientes por suite |

---

## Autor

**Enoc Ipanaque** — Lima, Perú

QA Automation Engineer en formación. Este proyecto lo construí mientras completaba el
curso de REST Assured Avanzado de [Free Range Testers](https://www.freerangetesters.com)
y me preparaba leyendo la documentación para la certificación ISTQB Foundation Level.

[LinkedIn](https://www.linkedin.com/in/enoc-isaac-ipanaque-rodas-b3729a283) · [GitHub](https://github.com/ipanaque94)
