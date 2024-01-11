# projekt_NWTiS - A Project for Managing Airplane Flight Logs

## Summary

This is a project for the "Advanced Web technologies and Services" class. The **projekt_NWTiS** project focuses on managing airplane flight logs, offering a suite of applications with distinct functionalities. Developed using Java and various Jakarta EE features, the project utilizes Eclipse with Maven as the primary development tool.


| Application | Development Tool (IDE) | Java | Server | EE Features | User Interface | Database | Data Access | Purpose |
|---|---|---|---|---|---|---|---|---|
| 1 | Eclipse with Maven | 17 | Custom |  |  |  | | Socket Server |
| 2 | Eclipse with Maven | 17 | Docker/Payara Micro | Jakarta EE10 Web |  | Docker/JRE, HSQLDB, nwtis_bp | JDBC, SQL, CriteriaAPI | RESTful/JAX-RS web service |
| 3 | Eclipse with Maven | 17 | Payara Full | Jakarta EE10 Web || Docker/JRE, HSQLDB, nwtis_bp | JDBC, SQL, CriteriaAPI | Takes data about airplane takeoffs from a chosen airport |
| 4 | Eclipse with Maven | 17 | Payara Full | Jakarta EE10 | | Docker/JRE, HSQLDB, nwtis_bp | JDBC, SQL, CriteriaAPI | JAX-WS web services and a WebSocket Endpoint |
| 5 | Eclipse with Maven | 17 | Payara Full | Jakarta EE10 | Jakarta MVC | | | Views for working with users, JMS messages, airports and flights |
