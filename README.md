## Project Description

This project is a Java application built with Spring Boot and follows the principles of Hexagonal Architecture. It provides a REST 
API endpoint for querying pricing information based on given parameters. It utilizes an in-memory H2 database 
initialized with example data. The application retrieves and returns details about product pricing from the `PRICES` 
table, which includes fields such as brand ID, start and end dates of the pricing period, product ID, price list 
identifier, priority, final price, and currency etc.

## Project Structure

The project is organized into several packages, each serving distinct roles within the application:

* **`es.kiwi.prices.application`**: contains the implementation of ports for data to flow in and out of the domain layer.

  * **`ports.input`**: Defines input ports (interfaces) for interacting with the domain layer.
  * **`ports.output`**: Defines output ports (interfaces) for sending data out of the application.
* **`es.kiwi.prices.domain`**: Encapsulates domain models, business logic, and exceptions.

  * **`exception`**: Custom exception classes like `PricesNotFoundException`.
  * **`model`**: Domain models such as `Prices`.
  * **`service`**: Service layer implementing business logic related to prices.
* **`es.kiwi.prices.infrastructure`**: the external part of the hexagonal architecture, which can only interact with the domain layer through input and output ports.

  * **`config`**: Spring bean configuration classes (`BeanConfiguration` - Config for registering bins, `OpenApiConfig`).
  * **`input.rest`**: REST adapters for exposing endpoints (`PricesRestAdapter`).
    * **`data.requests`**: Request objects for querying prices via REST (`PricesQueryRequest`).
    * **`data.responses`**: Response objects for returning queried prices via REST (`PricesQueryResponse`).
    * **`mapper`**: Interfaces and implementations for mapping between REST objects and domain objects (`PricesRestMapper`, `PricesRestMapperImpl`).
  * **`output.exception`**: Exception handling Response Entity Exceptions (`CustomizedExceptionAdapter`, 
    `ExceptionResponses`).
  * **`output.persistence`**: Persistence adapters (`PricesPersistenceAdapter`), entity (`PricesEntity`), and mappers (`PricesPersistenceMapper`, `PricesPersistenceMapperImpl`).
    * **`repository`**: Spring Data repositories (`PricesRepository`).

### Prices Table Structure

| Field          | Description                                                                 |
|----------------|-----------------------------------------------------------------------------|
| ID             | Primary key auto-increment.                                                 |
| BRAND_ID       | Brand identifier code.                                                      |
| START_DATE     | Start date of the pricing period.                                           |
| END_DATE       | End date of the pricing period.                                             |
| PRODUCT_ID     | Product identifier code.                                                    |
| PRIORITY       | Determines pricing precedence; higher numeric value means higher priority.  |
| PRICE_LIST     | Pricing applicable identifier code.                                         |
| PRICE          | Final selling price (pvp).                                                  |
| CURR           | ISO currency code.                                                          |
| LAST_UPDATE    | The latest update date.                                                     |
| LAST_UPDATE_BY | The user who did the latest update.                                         |

### API Endpoint

The application exposes a REST endpoint (/v1/prices) that:
- Accepts input parameters:
    - Application date, is a String(text) type, the date format is 'yyyy-MM-dd HH:mm:ss'
    - Product identifier
    - Brand identifier 
- Returns output data:
    - Product identifier
    - Brand identifier
    - Applicable price list identifier
    - Start date for the final price
    - End date for the final price
    - Final price to apply

### Example Requests

The tests validate the endpoint with the following scenarios based on the provided example data:
- **Test 1:** Request at 10:00 on the 14th, 2020 for product 35455 and brand 1 
- **Test 2:** Request at 16:00 on the 14th, 2020 for product 35455 and brand 1 
- **Test 3:** Request at 21:00 on the 14th, 2020 for product 35455 and brand 1 
- **Test 4:** Request at 10:00 on the 15th, 2020 for product 35455 and brand 1 
- **Test 5:** Request at 21:00 on the 16th, 2020 for product 35455 and brand 1 

All these 5 cases have been tested in PricesRestAdapterTest.java

### Technologies Used

* **Spring Boot**: Framework for building and deploying Java applications.
* **Hexagonal Architecture**: Organizes code into layers (ports, domain, adapters) for modularity and separation of concerns.
* **MapStruct**: Simplifies object mapping between layers, reducing boilerplate code (`PricesRestMapper`, `PricesPersistenceMapper`).
* **OpenAPI**: Integrated for documenting RESTful API endpoints (`OpenApiConfig`).
* **JUnit & Mockito**: Testing frameworks for unit testing and mocking dependencies.
* **H2 Database**: In-memory database for storing example data during development and testing.
* **RESTful API**: Exposes endpoints (`PricesRestAdapter`) for interacting with the application.

### How to Run

1. **Clone the repository**:
****
    git clone git@github.com:Nereitay/testJune15.git
    cd testJune15
2. **Build the project using Maven**.
3. **Run the application**.
4. **Access the API using a tool like Postman or Swagger url http://host:port/api**.

### Example API Usage

```http
GET http://localhost:8080/v1/prices?applicationDate=2020-06-16%2021%3A00%3A00&productId=35455&brandId=1
```

### Running Tests

Execute unit tests to verify functionality and coverage:

```bash
mvn test
```

### Future Improvements

- Expand brands-service to retrieve brand information associated with prices-service.
- Add authentication and authorization mechanisms if required.
