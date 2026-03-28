# REST Assured API Testing Framework

Framework profesional de automatización de pruebas de API construido con Java + Rest Assured + TestNG + Allure.

## 🔧 Stack técnico

| Herramienta | Versión | Uso |
|---|---|---|
| Java | 17 | Lenguaje base |
| Rest Assured | 5.3.1 | API Testing |
| TestNG | 7.10.2 | Test runner |
| Allure | 2.25.0 | Reportes |
| Gradle | 9.1.0 | Build tool |
| GitHub Actions | - | CI/CD |

## 📁 Estructura del proyecto
```
src/test/java/APITests/
├── auth/          → Tests de autenticación
├── books/         → Tests de Simple Books API
├── dataaccess/    → Tests SOAP
├── jsonplaceholder/ → Tests JsonPlaceholder API
├── rickandmorty/  → Tests Rick & Morty API
├── baseUrl/       → Clases base reutilizables
└── utils/         → Utilidades y configuración
```

## 🧪 Suites de prueba

| Suite | Descripción |
|---|---|
| Smoke | Tests críticos de verificación rápida |
| Regression | Suite completa de regresión |
| Contract | Validación de esquemas JSON |
| Security | Tests de autenticación y autorización |
| Negative | Casos negativos y manejo de errores |
| Idempotencia | Verificación de métodos idempotentes |
| Performance | Validación de tiempos de respuesta |
| SOAP | Tests de servicios SOAP |

## 🚀 Cómo ejecutar

# Todas las suites
.\gradlew clean test --no-configuration-cache
```

## 📊 Reporte Allure
# Ver El Reporte
allure serve build/allure-results
```

Ver reporte online: [GitHub Pages](https://ipanaque94.github.io/RestAssured3APIS)

## ⚙️ CI/CD

El pipeline de GitHub Actions corre automáticamente en cada push:

1. 🔥 Smoke Tests
2. 🔁 Regression + 📋 Contract + 🔒 Security + ❌ Negative + 🧼 SOAP
3. 🔄 Idempotencia + ⚡ Performance
4. 📊 Reporte Consolidado → GitHub Pages

## 📚 Curso base

Esta Suite de test fue construido sobre los conocimientos adquiridos en el curso
**REST Assured Avanzado** de [Free Range Testers](https://www.freerangetesters.com) en Udemy
