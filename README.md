# AppChallenge

This is an integration app used as a proof of concept, targeting AppDirect Marketplace, as well as for learning
spring mvc.

This application uses:
Back-end: SpringMVC, Hibernate, Postgresl, OAuth, OpenId;
Front-end: Thymeleaf.

Deployed at Heroku.

## AppDirect

Heroku test in progress:
- Subscription order

Future tests:
- Subscription change
- Subscription cancel
- Subscription notice


## Heroku
https://appchallenge-trzan.herokuapp.com/


## Local use
### Create environment variables

For database connection
Name: DATABASE_URL 
Value: postgres://username:password@127.0.0.1:5432/appchallenge

For consumer OAuth resource
Name: consumer-key
Value: <<your key>>

Name: consumer-secret
Value: <<your secret>>

### Create database tables
Run the Sql create-database.sql script in misc\database.

### Running application (one of the following)
1 - mvn spring-boot:run

2 - mvn clean install
    java -jar target/dependency/webapp-runner-7.0.57.2.jar --port 8080 target/appchallenge-1.0.0.war
    
3 - run as java application the class: ca.appdirect.appchallenge.AppChallengeApplication.java