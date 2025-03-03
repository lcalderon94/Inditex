# INFORME DE SOLUCIÓN TÉCNICA
## API de Consulta de Precios - Prueba Técnica Inditex

**Autor:** Luis Calderón  
**Fecha:** 3 de marzo de 2025  
**Versión:** 1.0

---

## 1. Resumen Ejecutivo

Este documento presenta la solución técnica implementada para el requerimiento de Inditex, consistente en el desarrollo de una API REST para consulta de precios aplicables a productos según fecha, producto y marca. La solución se ha diseñado siguiendo una arquitectura hexagonal (Domain-Driven Design), implementada con Spring Boot y una base de datos en memoria H2.

La API proporciona un endpoint principal que permite consultar el precio aplicable para un producto de una marca en una fecha determinada, considerando reglas de prioridad para desambiguación en caso de solapamiento de fechas. Adicionalmente, se han implementado operaciones CRUD completas para la gestión de precios.

La solución cumple con todos los requisitos especificados en la prueba técnica, con énfasis en calidad de código, testabilidad y correcta separación de responsabilidades.

---

## 2. Requerimientos del Proyecto

### 2.1 Requerimientos Funcionales

1. Desarrollar un servicio en Spring Boot que proporcione un endpoint REST para consulta de precios.
2. El endpoint debe aceptar como parámetros: fecha de aplicación, identificador de producto e identificador de cadena.
3. El servicio debe devolver: identificador de producto, identificador de cadena, tarifa aplicable, fechas de aplicación y precio final.
4. En caso de existir varias tarifas coincidentes en fechas, se debe aplicar la de mayor prioridad.
5. El sistema debe ser inicializado con los datos de ejemplo proporcionados en la tabla PRICES.

### 2.2 Requerimientos No Funcionales

1. Utilizar una base de datos en memoria tipo H2.
2. Implementar tests que validen las 5 peticiones específicas detalladas en la prueba.
3. Código limpio y bien estructurado.
4. Diseño y construcción adecuados del servicio.

---

## 3. Arquitectura de la Solución

### 3.1 Patrón Arquitectónico

La solución se ha implementado siguiendo una **Arquitectura Hexagonal** (también conocida como "Ports and Adapters" o "Domain-Driven Design"), que permite una clara separación de responsabilidades y facilita tanto el mantenimiento como el testing. Esta arquitectura organiza el código en capas bien definidas:

- **Capa de Dominio** (core): Contiene la lógica de negocio, modelos y puertos.
- **Capa de Aplicación**: Implementa los casos de uso del sistema.
- **Capa de Infraestructura**: Contiene los adaptadores que interactúan con el exterior (API REST, base de datos).

### 3.2 Diagrama de Componentes

```
               ┌───────────────────────────────────────────────────┐
               │                  Infraestructura                   │
┌───────────┐  │  ┌───────────┐        ┌───────────────────────┐   │
│  Cliente  │──┼─▶│ REST API  │        │   Base de Datos H2    │   │
└───────────┘  │  └─────┬─────┘        └───────────┬───────────┘   │
               │        │                          │               │
               │  ┌─────▼─────┐          ┌─────────▼───────────┐   │
               │  │  Adapter  │          │       Adapter       │   │
               │  └─────┬─────┘          └───────────┬─────────┘   │
               └────────┼────────────────────────────┼─────────────┘
                        │                            │
               ┌────────▼────────────────────────────▼─────────────┐
               │                   Aplicación                       │
               │  ┌───────────────────────────────────────────┐    │
               │  │           Servicios de Aplicación         │    │
               │  └───────────────────────┬───────────────────┘    │
               └──────────────────────────┼────────────────────────┘
                                          │
               ┌──────────────────────────▼────────────────────────┐
               │                    Dominio                         │
               │  ┌───────────────────────────────────────────┐    │
               │  │         Modelos y Lógica de Negocio       │    │
               │  └───────────────────────────────────────────┘    │
               │  ┌────────────────┐      ┌────────────────────┐   │
               │  │  Puertos de    │      │     Puertos de     │   │
               │  │    Entrada     │      │       Salida       │   │
               │  └────────────────┘      └────────────────────┘   │
               └───────────────────────────────────────────────────┘
```

### 3.3 Estructura del Proyecto

El proyecto sigue una estructura organizativa basada en la arquitectura hexagonal:

```
src/main/java/com/pruebatecnica/Inditex/
├── application
│   ├── dto           # Objetos de transferencia de datos
│   └── service       # Implementaciones de servicios
├── domain
│   ├── exception     # Excepciones de dominio
│   ├── model         # Modelos de dominio
│   └── port          # Puertos (interfaces)
│       ├── input     # Puertos de entrada (API)
│       └── output    # Puertos de salida (Repositorios)
└── infrastructure
    ├── adapter       # Adaptadores
    │   ├── input     # Adaptadores de entrada (REST)
    │   └── output    # Adaptadores de salida (Repositorios)
    ├── config        # Configuración
    ├── entity        # Entidades JPA
    ├── exception     # Manejo de excepciones
    ├── mapper        # Conversores entre entidades y modelos
    └── repository    # Repositorios JPA
```

---

## 4. Diseño de la Solución

### 4.1 Modelo de Dominio

El modelo de dominio está centrado en la entidad Price, que representa un precio aplicable a un producto de una marca en un rango de fechas determinado. Se ha implementado como un POJO puro, independiente de tecnologías externas como JPA:

- **Price**: Modelo de dominio para precios, con atributos como brandId, startDate, endDate, priceList, productId, priority, price y currency.

### 4.2 Puertos e Interfaces

Los puertos definen el contrato entre el dominio y la infraestructura:

- **PriceService** (puerto de entrada): Define los casos de uso relacionados con precios.
- **PriceRepository** (puerto de salida): Define operaciones para acceso a datos de precios.

### 4.3 Servicios de Aplicación

La capa de aplicación implementa la lógica de negocio a través de servicios:

- **PriceServiceImpl**: Implementa el puerto PriceService y encapsula la lógica para encontrar el precio aplicable según fecha, producto y marca, considerando reglas de prioridad.

### 4.4 Adaptadores

Los adaptadores conectan la aplicación con el mundo exterior:

- **PriceController** (adaptador de entrada): Implementa la API REST.
- **PriceRepositoryAdapter** (adaptador de salida): Implementa el puerto PriceRepository utilizando JPA.

### 4.5 Mapeo de Datos

Se utilizan mappers para convertir entre diferentes representaciones:

- **PriceMapper**: Convierte entre entidades JPA, modelos de dominio y DTOs.

### 4.6 Gestión de Excepciones

Se ha implementado un manejo centralizado de excepciones mediante un controlador de asesoramiento (ControllerAdvice):

- **GlobalExceptionHandler**: Maneja excepciones de forma centralizada y proporciona respuestas HTTP apropiadas.

---

## 5. Aspectos Técnicos

### 5.1 Tecnologías Utilizadas

- **Java 17**: Lenguaje de programación base.
- **Spring Boot 3.2.2**: Framework para desarrollo de aplicaciones Java.
- **Spring Data JPA**: Para persistencia de datos.
- **H2 Database**: Base de datos en memoria.
- **Lombok**: Para reducir código boilerplate.
- **OpenAPI/Swagger**: Para documentación de la API.
- **JUnit 5**: Para testing.
- **JaCoCo**: Para análisis de cobertura de código.
- **Docker**: Para containerización de la aplicación.
- **Maven**: Para gestión de dependencias y construcción.
- **GitHub Actions**: Para integración continua.

### 5.2 Base de Datos

La solución utiliza una base de datos H2 en memoria, inicializada mediante scripts SQL:

- **schema.sql**: Define la estructura de la tabla PRICES.
- **data.sql**: Inicializa la base de datos con los datos de ejemplo proporcionados.

### 5.3 API REST

La API REST está implementada mediante controladores Spring MVC:

- **GET /api/prices/applicable**: Consulta el precio aplicable según fecha, producto y marca.
- **GET /api/prices**: Obtiene todos los precios.
- **GET /api/prices/{id}**: Obtiene un precio por su ID.
- **POST /api/prices**: Crea un nuevo precio.
- **PUT /api/prices/{id}**: Actualiza un precio existente.
- **PATCH /api/prices/{id}**: Actualiza parcialmente un precio.
- **DELETE /api/prices/{id}**: Elimina un precio.

### 5.4 Seguridad

La solución actual no implementa mecanismos de seguridad ya que no se especificaba en los requisitos. Sin embargo, el diseño permite incorporar seguridad (autenticación, autorización) en futuras iteraciones.

### 5.5 Validación de Datos

Se utiliza validación de datos de entrada mediante control de excepciones para garantizar la integridad de los datos:

- Validación de formato de fecha.
- Validación de existencia de productos y marcas.
- Control de campos requeridos.

### 5.6 Monitorización y Logging

Se ha implementado logging mediante SLF4J para permitir un seguimiento adecuado de la ejecución y facilitar la depuración:

- Log de errores en caso de excepciones.
- Log de operaciones relevantes.

---

## 6. Testing

### 6.1 Estrategia de Testing

Se ha seguido una estrategia de testing exhaustiva que incluye:

- **Tests Unitarios**: Para componentes individuales.
- **Tests de Integración**: Para interacciones entre componentes.
- **Tests de API**: Para validar comportamiento end-to-end.

### 6.2 Casos de Prueba

Se han implementado las pruebas requeridas en la especificación:

1. **Test 1**: Petición a las 10:00 del día 14 del producto 35455 para la brand 1 (ZARA).
2. **Test 2**: Petición a las 16:00 del día 14 del producto 35455 para la brand 1 (ZARA).
3. **Test 3**: Petición a las 21:00 del día 14 del producto 35455 para la brand 1 (ZARA).
4. **Test 4**: Petición a las 10:00 del día 15 del producto 35455 para la brand 1 (ZARA).
5. **Test 5**: Petición a las 21:00 del día 16 del producto 35455 para la brand 1 (ZARA).

Adicionalmente, se han implementado pruebas para casos límite y escenarios de error:

- Tests de fechas límite (exactamente al inicio y fin de periodos).
- Tests de prioridades (selección correcta en caso de solapamiento).
- Tests de manejo de excepciones.
- Tests de operaciones CRUD.

### 6.3 Cobertura de Código

Se ha utilizado JaCoCo para análisis de cobertura de código, con un requisito mínimo del 80% de cobertura en líneas de código.

La configuración de JaCoCo excluye clases que no requieren testeo, como DTOs, clases de configuración y clases de entidades.

---

## 7. Despliegue y Operación

### 7.1 Containerización

La aplicación puede ser containerizada mediante Docker utilizando el Dockerfile proporcionado:

- Fase de build basada en Maven
- Fase de runtime basada en una imagen JRE

### 7.2 Integración Continua

Se ha configurado un pipeline de CI/CD en GitHub Actions que incluye:

1. Build y ejecución de tests
2. Análisis de cobertura con JaCoCo
3. Merge automático a la rama de integración en caso de éxito
4. Construcción de imagen Docker

### 7.3 Monitorización

La solución puede integrarse con herramientas de monitorización como:

- Spring Boot Actuator para health checks
- Prometheus para métricas
- Grafana para visualización

### 7.4 Escalabilidad

El diseño permite escalabilidad horizontal mediante:

- Arquitectura stateless
- Containerización con Docker
- Posibilidad de despliegue en plataformas de orquestación como Kubernetes

---

## 8. Decisiones Técnicas

### 8.1 Arquitectura Hexagonal

Se eligió una arquitectura hexagonal para:
- Lograr bajo acoplamiento entre componentes
- Facilitar testing mediante inversión de dependencias
- Separar claramente la lógica de negocio de aspectos técnicos

### 8.2 DTOs vs Modelos de Dominio

Se utilizaron DTOs separados de los modelos de dominio para:
- Evitar exposición directa de modelos internos en la API
- Permitir evolución independiente de la API y el dominio
- Controlar la serialización/deserialización de datos

### 8.3 Mapper Centralizado

Se implementó un mapper centralizado para:
- Mantener la coherencia en las conversiones entre entidades y modelos
- Facilitar futuros cambios en las estructuras de datos
- Mejorar legibilidad y mantenibilidad

### 8.4 Control de Excepciones Centralizado

Se utilizó un manejador global de excepciones para:
- Proporcionar respuestas consistentes ante errores
- Centralizar la lógica de traducción de excepciones a códigos HTTP
- Facilitar el logging y monitorización de errores

### 8.5 Spring Data JPA vs JDBC

Se eligió Spring Data JPA para:
- Reducir código boilerplate en operaciones CRUD
- Aumentar productividad en el desarrollo
- Aprovechar características como paginación y ordenación

---

## 9. Mejoras Propuestas

### 9.1 Mejoras Técnicas

1. **Implementación de Caché**: Implementar caché para mejorar rendimiento en consultas frecuentes.
2. **Validación más Exhaustiva**: Ampliar las reglas de validación de entrada.
3. **Documentación API Mejorada**: Expandir documentación Swagger con más ejemplos y descripciones.
4. **Implementación de Seguridad**: Añadir autenticación y autorización mediante OAuth2/JWT.
5. **Estrategia de Paginación**: Implementar paginación para endpoints que retornan colecciones grandes.

### 9.2 Mejoras Funcionales

1. **Historial de Cambios**: Implementar auditoría de cambios en precios.
2. **Búsqueda Avanzada**: Añadir capacidades de búsqueda con múltiples criterios.
3. **Gestión de Divisas**: Ampliar funcionalidad para soportar conversión entre divisas.
4. **Notificaciones**: Implementar sistema de notificaciones para cambios relevantes.
5. **Programación de Precios**: Permitir programación anticipada de cambios de precios.

### 9.3 Mejoras de Calidad

1. **Ampliación de Tests**: Incrementar cobertura y tipos de tests.
2. **Mejora del Logging**: Implementar logging estructurado para mejor análisis.
3. **Análisis Estático de Código**: Integrar herramientas como SonarQube.
4. **Pruebas de Rendimiento**: Implementar pruebas de carga y estrés.
5. **Documentación Técnica**: Generar documentación técnica automática.

---

## 10. Conclusiones

La solución implementada cumple con todos los requisitos especificados para la prueba técnica de Inditex. Se ha desarrollado siguiendo buenas prácticas de ingeniería de software, con énfasis en:

- Diseño limpio y mantenible mediante arquitectura hexagonal
- Separación clara de responsabilidades
- Testing exhaustivo con alta cobertura
- Código legible y bien documentado
- CI/CD mediante GitHub Actions
- Containerización con Docker

La arquitectura elegida permite extensibilidad y mantenibilidad a largo plazo, facilitando la incorporación de nuevas funcionalidades o la modificación de las existentes sin impactar negativamente en otras partes del sistema.

El sistema es robusto, probado y listo para producción, proporcionando una base sólida para futuras expansiones.

---

## 11. Anexos

### 11.1 Referencias

- Spring Boot: https://spring.io/projects/spring-boot
- Arquitectura Hexagonal: https://alistair.cockburn.us/hexagonal-architecture/
- JaCoCo: https://www.jacoco.org/jacoco/
- Docker: https://www.docker.com/
- GitHub Actions: https://github.com/features/actions

### 11.2 Glosario

- **API REST**: Interfaz de Programación de Aplicaciones basada en REST
- **DDD**: Domain-Driven Design (Diseño Guiado por el Dominio)
- **DTO**: Data Transfer Object (Objeto de Transferencia de Datos)
- **JPA**: Java Persistence API
- **CI/CD**: Continuous Integration/Continuous Deployment
