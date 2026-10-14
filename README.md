# LogLens — Server Log Analytics Tool

## 1. Project Overview

LogLens is a Java-based, menu-driven console application that analyzes server log records. It helps identify request patterns, HTTP errors, slow endpoints, and suspicious login activity.

## 2. Objectives

- Analyze server log records stored in a CSV file.
- Count requests by HTTP status code.
- Identify the most frequent IP addresses and endpoints.
- Analyze errors by hour.
- Find endpoints with the highest average response time.
- Detect IP addresses with repeated failed login attempts.

## 3. Technologies Used

- **Programming Language:** Java
- **Data Format:** CSV
- **Development Environment:** Visual Studio Code
- **Java Libraries:** Collections Framework, File I/O, Scanner

## 4. Core Features

1. Request counts by status code.
2. Top five IP addresses and endpoints.
3. Hourly error analysis.
4. Slowest endpoints by average response time.
5. Suspicious IP detection based on failed login attempts.

## 5. Project Structure

```text
ProjectCP093/
├── src/
│   ├── Main.java
│   ├── LogEntry.java
│   ├── LogReader.java
│   └── LogAnalyzer.java
├── data/
│   └── logs.csv
├── reports/
└── README.md
```

## 6. How to Run

Open the terminal in the project root directory.

Compile the Java files:

```bash
javac src/Main.java src/LogEntry.java src/LogReader.java src/LogAnalyzer.java
```

Run the application:

```bash
java -cp src Main
```

Ensure that `data/logs.csv` exists before running the application.

## 7. Input Data

The application reads CSV records containing:

- Time
- IP address
- Endpoint
- HTTP status code
- Response time

The current dataset contains synthetic log records for testing and demonstration.

## 8. Future Enhancements

- Filter logs by a selected time window.
- Export analysis results to a report file.
- Add date-aware analysis for logs spanning multiple days.
- Improve validation and error reporting for malformed records.