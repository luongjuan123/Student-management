# Student Management System

## Overview

This is a Java-based Student Management System that allows administrators to manage student records, courses, and scores. The application features a graphical user interface built with Swing and connects to a MySQL database for data persistence.

## Features

- **Student Management**
  - Add, update, and delete student records
  - Store student details including name, date of birth, gender, contact information, and family details
  - Upload and store student images

- **Course Management**
  - Assign courses to students by semester
  - Track up to 5 courses per semester
  - Validate course assignments to prevent duplicates

- **Score Management**
  - Record and update student scores for each course
  - Calculate semester averages and overall GPA
  - View comprehensive mark sheets

- **User Authentication**
  - Secure login system for administrators
  - Input validation for credentials

## Technologies Used

- Java 
- Swing for GUI
- MySQL for database
- JDBC for database connectivity

## Database Schema

The system uses the following tables:
- `student` - Stores student personal information
- `course` - Tracks courses taken by students each semester
- `score` - Records student grades for each course
- `admin` - Stores administrator login credentials

## Installation

### Prerequisites

1. Java Development Kit (JDK) 
2. MySQL Server
3. MySQL Connector/J (JDBC driver)

### Setup Instructions

1. **Database Setup**:
   - Create a MySQL database named `students_management`
   - Import the provided SQL schema (if available)
   - Update database credentials in `MyConnection.java`
   - SQL code :
  ```sql
CREATE TABLE `admin` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO `admin` VALUES (1,'admin','123');

CREATE TABLE `student` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(150) NOT NULL,
  `date_of_birth` date NOT NULL,
  `gender` varchar(10) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `father_name` varchar(150) NOT NULL,
  `mother_name` varchar(150) NOT NULL,
  `address` text NOT NULL,
  `image_path` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
); 


CREATE TABLE `course` (
  `id` int NOT NULL,
  `student_id` int DEFAULT NULL,
  `semester` int DEFAULT NULL,
  `course1` varchar(200) DEFAULT NULL,
  `course2` varchar(200) DEFAULT NULL,
  `course3` varchar(200) DEFAULT NULL,
  `course4` varchar(200) DEFAULT NULL,
  `course5` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_student_id` (`student_id`),
  CONSTRAINT `fk_student_id` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `score` (
  `id` int NOT NULL AUTO_INCREMENT,
  `student_id` int NOT NULL,
  `semester` int NOT NULL,
  `course1` varchar(200) NOT NULL,
  `score1` double NOT NULL,
  `course2` varchar(200) NOT NULL,
  `score2` double NOT NULL,
  `course3` varchar(200) NOT NULL,
  `score3` double NOT NULL,
  `course4` varchar(200) NOT NULL,
  `score4` double NOT NULL,
  `course5` varchar(200) NOT NULL,
  `score5` double NOT NULL,
  `average` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_stu_id` (`student_id`),
  CONSTRAINT `fk_stu_id` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);
  
  ```

2. **Configuration**:
   - Ensure the MySQL JDBC driver is in your classpath
   - Modify connection parameters in `db/MyConnection.java` if needed:
     ```java
     private static final String username = "your_username"; //default is root
     private static final String password = "your_password"; //your sql password
     private static final String dataConn = "jdbc:mysql://localhost:3306/students_management";
     ```

3. **Running the Application**:
   - Run `student.view.Login` as the main class

## Usage

1. **Login**:
   - Launch the application
   - Enter admin credentials (stored in the `admin` table) 

2. **Main Interface**:
   - Navigate between student, course, and score management tabs
   - Use the search functionality to find specific records
   - Add, edit, or delete records as needed

## Security Notes

- The current implementation stores database credentials in plain text in the source code. For production use:
  - Consider using environment variables or a configuration file
  - Implement proper password hashing for admin accounts
  - Restrict database user permissions

## References

- [PCODEP](https://www.procodeplan.com/) (For design)
- [Tutorial Point](https://www.tutorialspoint.com/swing/swing_layouts.htm) (For Java Swing)
- [Database Star](https://www.youtube.com/@DatabaseStar) (For Database)

## Author

Group 13

---

