# Library Service
Program for managing online library.
Allows for user registration, browsing through books, borrowing and returning. There is an amount of each book, users have borrowing limits. Each loan is registered and has a return date, after which it is overdue.

## Prerequisites
* MySQL
* Maven
* JDK

## How to run
### Create a new database
Run in MySQL Command Line
```
mysql> create database library;
mysql> create user 'springuser'@'localhost' identified by 'ThePassword';
mysql> grant all on library.* to 'springuser'@'localhost';
```
### Create a jar file
1. Run Command Prompt
2. Change directory to project folder
3. Run command ```mvn clean install```
4. Jar file will be located in "target" folder

### Run program
1. Run Command Prompt
2. Run command ```java -jar PathToJarFile.jar```

## Technologies Used
* Maven
* Spring Boot
* MySQL
* JPA
