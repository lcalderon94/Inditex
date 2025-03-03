# Inditex
Aquí tienes el README completo con el enlace al informe técnico al final:

# Prueba Técnica Inditex

## Índice de Contenidos
- [Descripción del Proyecto](#descripción-del-proyecto)
- [Requisitos Técnicos](#requisitos-técnicos)
- [Arquitectura](#arquitectura)
- [Tecnologías](#tecnologías)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Documentación de la API](#documentación-de-la-api)
- [Ejecución de la Aplicación](#ejecución-de-la-aplicación)
  - [Requisitos Previos](#requisitos-previos)
  - [Usando Maven](#usando-maven)
  - [Usando Docker](#usando-docker)
- [Testing](#testing)
  - [Casos de Prueba](#casos-de-prueba)
  - [Cobertura de Código](#cobertura-de-código)
- [Integración Continua](#integración-continua)
- [Decisiones de Diseño](#decisiones-de-diseño)
- [Mejoras Futuras](#mejoras-futuras)
- [Documentación Adicional](#documentación-adicional)

## Descripción del Proyecto

Este proyecto es una prueba técnica para Inditex implementada para Capitole Consulting. Consiste en una API REST que proporciona información de precios para productos basándose en la fecha, ID del producto e ID de la marca. El sistema maneja superposiciones de precios utilizando reglas de prioridad para determinar la tarifa aplicable.

La funcionalidad principal gira en torno a la tabla `PRICES` que almacena información de precios con los siguientes campos:

- **BRAND_ID**: Clave foránea de la cadena del grupo (1 = ZARA)
- **START_DATE, END_DATE**: Rango de fechas en el que aplica el precio tarifa indicado
- **PRICE_LIST**: Identificador de la tarifa de precios aplicable
- **PRODUCT_ID**: Identificador código de producto
- **PRIORITY**: Desambiguador de aplicación de precios. Si dos tarifas coinciden en un rango de fechas se aplica la de mayor prioridad (mayor valor numérico)
- **PRICE**: Precio final de venta
- **CURR**: ISO de la moneda

La API permite consultar el precio aplicable según la fecha de aplicación, identificador de producto e identificador de cadena, devolviendo como resultado el identificador de producto, identificador de cadena, tarifa a aplicar, fechas de aplicación y precio final.

## Requisitos Técnicos

Para el desarrollo de este proyecto se establecieron los siguientes requisitos:

1. Implementar una aplicación/servicio en Spring Boot con un endpoint REST de consulta.
2. Utilizar una base de datos en memoria H2.
3. Inicializar la base de datos con los datos de ejemplo proporcionados.
4. Implementar tests para validar las siguientes peticiones al servicio:
   - Test 1: Petición a las 10:00 del día 14 del producto 35455 para la brand 1 (ZARA)
   - Test 2: Petición a las 16:00 del día 14 del producto 35455 para la brand 1 (ZARA)
   - Test 3: Petición a las 21:00 del día 14 del producto 35455 para la brand 1 (ZARA)
   - Test 4: Petición a las 10:00 del día 15 del producto 35455 para la brand 1 (ZARA)
   - Test 5: Petición a las 21:00 del día 16 del producto 35455 para la brand 1 (ZARA)

## Arquitectura

El proyecto sigue una **Arquitectura Hexagonal (DDD - Domain Driven Design)**, lo que facilita la separación de responsabilidades y permite un diseño más limpio y mantenible. Esta arquitectura consta de:

1. **Dominio**: Contiene la lógica de negocio core, modelos y puertos.
2. **Aplicación**: Implementa los casos de uso a través de servicios.
3. **Infraestructura**: Adaptadores para interactuar con el exterior (API REST, base de datos).

Esta estructura permite:
- Alta cohesión y bajo acoplamiento
- Facilidad para testing unitario
- Independencia de frameworks y bases de datos
- Mayor claridad en la separación de responsabilidades

## Tecnologías

Las principales tecnologías utilizadas en este proyecto son:

- **Java 17**: Aprovechando las últimas características del lenguaje como streams, optionals y LocalDateTime.
- **Spring Boot 3.2.2**: Framework para simplificar el desarrollo de aplicaciones Java.
- **Spring Data JPA**: Para la capa de persistencia.
- **H2 Database**: Base de datos en memoria.
- **Lombok**: Para reducir código boilerplate.
- **JUnit 5 & Spring Test**: Para testing unitario y de integración.
- **JaCoCo**: Para análisis de cobertura de código.
- **Swagger/OpenAPI**: Para documentación de la API.
- **Docker**: Para containerización de la aplicación.
- **Maven**: Para gestión de dependencias y build.
- **GitHub Actions**: Para CI/CD.

## Estructura del Proyecto

El proyecto sigue una estructura de paquetes basada en la arquitectura hexagonal:

```
src/main/java/com/pruebatecnica/Inditex/
├── application
│   ├── dto
│   └── service
├── domain
│   ├── exception
│   ├── model
│   └── port
│       ├── input
│       └── output
└── infrastructure
    ├── adapter
    │   ├── input
    │   │   └── rest
    │   └── output
    │       └── persistence
    ├── config
    ├── entity
    ├── exception
    ├── mapper
    └── repository
```

## Documentación de la API

La API está documentada usando OpenAPI (Swagger). Una vez iniciada la aplicación, puedes acceder a la documentación interactiva en:

```
http://localhost:8080/swagger-ui.html
```

### Endpoints Principales

#### Consultar Precio Aplicable

```
GET /api/prices/applicable?date={date}&productId={productId}&brandId={brandId}
```

Parámetros:
- `date`: Fecha de consulta (formato ISO: yyyy-MM-ddTHH:mm:ss)
- `productId`: ID del producto
- `brandId`: ID de la marca

Respuesta:
```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 1,
  "startDate": "2020-06-14T00:00:00",
  "endDate": "2020-12-31T23:59:59",
  "price": 35.50
}
```

Además, se implementaron otros endpoints para gestión completa (CRUD) de precios.

## Ejecución de la Aplicación

### Requisitos Previos

- Java 17 o superior
- Maven 3.6 o superior
- Docker (opcional)

### Usando Maven

Para ejecutar la aplicación usando Maven:

```bash
# Compilar el proyecto
mvn clean package

# Ejecutar la aplicación
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

### Usando Docker

Para ejecutar la aplicación usando Docker:

```bash
# Construir la imagen Docker
docker build -t inditex-prices-api .

# Ejecutar el contenedor
docker run -p 8080:8080 inditex-prices-api
```

Alternativamente, puedes usar Docker Compose:

```bash
docker-compose up
```

## Testing

El proyecto incluye pruebas unitarias, de integración y tests de contrato para asegurar su correcto funcionamiento.

### Casos de Prueba

Se implementaron los 5 casos de prueba solicitados que validan diferentes escenarios:

1. **Test 1**: Precio a las 10:00 del día 14 - Debe devolver tarifa 1 (35.50€)
2. **Test 2**: Precio a las 16:00 del día 14 - Debe devolver tarifa 2 (25.45€)
3. **Test 3**: Precio a las 21:00 del día 14 - Debe devolver tarifa 1 (35.50€)
4. **Test 4**: Precio a las 10:00 del día 15 - Debe devolver tarifa 3 (30.50€)
5. **Test 5**: Precio a las 21:00 del día 16 - Debe devolver tarifa 4 (38.95€)

Adicionalmente, se implementaron:
- Tests unitarios para la capa de servicio
- Tests de integración para los repositorios
- Tests de integración para los controladores
- Tests de casos límite y escenarios de error

Para ejecutar los tests:

```bash
mvn test
```

### Cobertura de Código

Se utiliza JaCoCo para medir la cobertura de código. Se ha configurado para exigir un mínimo de 80% de cobertura en líneas de código. Para generar el informe de cobertura:

```bash
mvn verify
```

El informe de cobertura se genera en `target/site/jacoco/index.html`

## Integración Continua

El proyecto utiliza GitHub Actions para CI/CD. El flujo configurado realiza las siguientes tareas:

1. **Build and Test**: Compila el proyecto y ejecuta las pruebas
2. **JaCoCo Coverage**: Analiza y reporta la cobertura de código
3. **Merge to Integration**: Si los tests pasan, hace merge automático a la rama de integración
4. **Build Docker Image**: Construye la imagen Docker de la aplicación

El flujo está configurado para ejecutarse en cada push a la rama `dev` y también puede ejecutarse manualmente.

## Decisiones de Diseño

### Arquitectura Hexagonal

Se optó por una arquitectura hexagonal para:
- Separar claramente las capas de la aplicación
- Facilitar el testing unitario mediante interfaces (puertos)
- Minimizar el acoplamiento entre componentes

### DTOs y Mappers

Se utilizan DTOs para separar la representación externa (API) del modelo de dominio, junto con mappers para la conversión entre ellos.

### Manejo de Excepciones

Se implementó un manejador global de excepciones para proporcionar respuestas consistentes y significativas ante errores.

### Inicialización de Datos

Los datos se inicializan mediante scripts SQL (`schema.sql` y `data.sql`) para garantizar la consistencia en la estructura y datos iniciales.

## Mejoras Futuras

Posibles mejoras para futuras versiones:

1. **Implementación de Caché**: Para mejorar el rendimiento en consultas frecuentes.
2. **Validación Más Robusta**: Ampliar la validación de entrada con más reglas de negocio.
3. **Paginación y Filtrado**: Para endpoints que devuelven múltiples registros.
4. **Seguridad**: Implementar autenticación y autorización.
5. **Auditoría**: Registro de cambios en precios.
6. **Monitorización y Métricas**: Integración con sistemas de monitorización.
7. **Documentación Adicional**: Expandir la documentación técnica y de usuario.

## Documentación Adicional

Para una explicación más detallada de las decisiones técnicas y la implementación de la solución, consulte el [Informe de Solución Técnica](externos/InformeSolucionTecnica.md).
