
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogAnalyzer {

    // 1. Count requests by HTTP status code
    public static void countStatusCodes(List<LogEntry> logs) {
        Map<Integer, Integer> statusCounts = new HashMap<>();

        for (LogEntry log : logs) {
            int status = log.getStatusCode();

            statusCounts.put(
                status,
                statusCounts.getOrDefault(status, 0) + 1
            );
        }

        System.out.println("\n===== REQUESTS BY STATUS CODE =====");

        statusCounts.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry ->
                System.out.println(
                    entry.getKey() + ": " + entry.getValue()
                )
            );
    }

    // 2. Display the top five IP addresses and endpoints
    public static void showTopFive(List<LogEntry> logs) {
        Map<String, Integer> ipCounts = new HashMap<>();
        Map<String, Integer> endpointCounts = new HashMap<>();

        for (LogEntry log : logs) {
            String ip = log.getIpAddress();
            String endpoint = log.getEndpoint();

            ipCounts.put(ip, ipCounts.getOrDefault(ip, 0) + 1);
            endpointCounts.put(
                endpoint,
                endpointCounts.getOrDefault(endpoint, 0) + 1
            );
        }

        System.out.println("\n===== TOP FIVE IP ADDRESSES =====");

        ipCounts.entrySet()
            .stream()
            .sorted(
                Map.Entry.<String, Integer>comparingByValue()
                    .reversed()
            )
            .limit(5)
            .forEach(entry ->
                System.out.println(
                    entry.getKey() + ": " + entry.getValue()
                )
            );

        System.out.println("\n===== TOP FIVE ENDPOINTS =====");

        endpointCounts.entrySet()
            .stream()
            .sorted(
                Map.Entry.<String, Integer>comparingByValue()
                    .reversed()
            )
            .limit(5)
            .forEach(entry ->
                System.out.println(
                    entry.getKey() + ": " + entry.getValue()
                )
            );
    }

    // 3. Count HTTP errors for each hour
    public static void showHourlyErrors(List<LogEntry> logs) {
        Map<Integer, Integer> hourlyErrors = new HashMap<>();

        for (LogEntry log : logs) {
            int status = log.getStatusCode();

            if (status >= 400) {
                int hour = Integer.parseInt(
                    log.getTime().substring(0, 2)
                );

                hourlyErrors.put(
                    hour,
                    hourlyErrors.getOrDefault(hour, 0) + 1
                );
            }
        }

        System.out.println("\n===== ERRORS PER HOUR =====");

        for (int hour = 0; hour < 24; hour++) {
            System.out.printf(
                "%02d:00 - %d errors%n",
                hour,
                hourlyErrors.getOrDefault(hour, 0)
            );
        }
    }

    // 4. Find the five endpoints with the highest average response time
    public static void showSlowestEndpoints(List<LogEntry> logs) {
        Map<String, Double> totalResponseTimes = new HashMap<>();
        Map<String, Integer> endpointCounts = new HashMap<>();

        for (LogEntry log : logs) {
            String endpoint = log.getEndpoint();

            totalResponseTimes.put(
                endpoint,
                totalResponseTimes.getOrDefault(endpoint, 0.0)
                    + log.getResponseTime()
            );

            endpointCounts.put(
                endpoint,
                endpointCounts.getOrDefault(endpoint, 0) + 1
            );
        }

        System.out.println(
            "\n===== TOP FIVE SLOWEST ENDPOINTS ====="
        );

        totalResponseTimes.keySet()
            .stream()
            .map(endpoint -> Map.entry(
                endpoint,
                totalResponseTimes.get(endpoint)
                    / endpointCounts.get(endpoint)
            ))
            .sorted(
                Map.Entry.<String, Double>comparingByValue()
                    .reversed()
            )
            .limit(5)
            .forEach(entry ->
                System.out.printf(
                    "%s: %.2f ms average%n",
                    entry.getKey(),
                    entry.getValue()
                )
            );
    }

    // 5. Detect IP addresses with at least five failed login attempts
    public static void detectSuspiciousIPs(List<LogEntry> logs) {
        Map<String, Integer> failedLogins = new HashMap<>();

        for (LogEntry log : logs) {
            if (log.getEndpoint().equals("/api/login")
                    && log.getStatusCode() == 401) {

                String ip = log.getIpAddress();

                failedLogins.put(
                    ip,
                    failedLogins.getOrDefault(ip, 0) + 1
                );
            }
        }

        System.out.println("\n===== SUSPICIOUS IP ADDRESSES =====");

        boolean found = false;

        for (Map.Entry<String, Integer> entry
                : failedLogins.entrySet()) {

            if (entry.getValue() >= 5) {
                System.out.println(
                    "IP: " + entry.getKey()
                    + " | Failed logins: " + entry.getValue()
                );

                found = true;
            }
        }

        if (!found) {
            System.out.println(
                "No IP addresses reached the threshold "
                + "of 5 failed logins."
            );
        }
    }

    // 6. Filter log records by start and end times
    public static List<LogEntry> filterByTime(
            List<LogEntry> logs,
            String startTime,
            String endTime) {

        DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("HH:mm:ss");

        try {
            LocalTime start = LocalTime.parse(startTime, formatter);
            LocalTime end = LocalTime.parse(endTime, formatter);

            List<LogEntry> filteredLogs = new ArrayList<>();

            for (LogEntry log : logs) {
                LocalTime logTime =
                    LocalTime.parse(log.getTime(), formatter);

                boolean matches;

                if (!end.isBefore(start)) {
                    // Normal range, such as 09:00:00 to 17:00:00
                    matches = !logTime.isBefore(start)
                        && !logTime.isAfter(end);
                } else {
                    // Range crossing midnight
                    matches = !logTime.isBefore(start)
                        || !logTime.isAfter(end);
                }

                if (matches) {
                    filteredLogs.add(log);
                }
            }

            System.out.println(
                "Records matching the time range: "
                + filteredLogs.size()
            );

            return filteredLogs;

        } catch (DateTimeParseException e) {
            System.out.println(
                "Invalid time format. Use HH:mm:ss, "
                + "for example 09:30:00."
            );

            return new ArrayList<>();
        }
    }
}
