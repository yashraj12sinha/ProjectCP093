import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogAnalyzer {

    // Feature 1: Count requests by HTTP status code
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

        for (Map.Entry<Integer, Integer> entry
                : statusCounts.entrySet()) {

            System.out.println(
                "Status " + entry.getKey()
                + " : " + entry.getValue() + " requests"
            );
        }
    }

    // Feature 2: Display top five IP addresses and endpoints
    public static void showTopFive(List<LogEntry> logs) {

        Map<String, Integer> ipCounts = new HashMap<>();
        Map<String, Integer> endpointCounts = new HashMap<>();

        for (LogEntry log : logs) {
            String ip = log.getIpAddress();
            String endpoint = log.getEndpoint();

            ipCounts.put(
                ip, ipCounts.getOrDefault(ip, 0) + 1
            );

            endpointCounts.put(
                endpoint,
                endpointCounts.getOrDefault(endpoint, 0) + 1
            );
        }

        System.out.println("\n===== TOP 5 IP ADDRESSES =====");

        ipCounts.entrySet().stream()
            .sorted((a, b) ->
                Integer.compare(b.getValue(), a.getValue()))
            .limit(5)
            .forEach(entry -> System.out.println(
                entry.getKey() + " : "
                + entry.getValue() + " requests"
            ));

        System.out.println("\n===== TOP 5 ENDPOINTS =====");

        endpointCounts.entrySet().stream()
            .sorted((a, b) ->
                Integer.compare(b.getValue(), a.getValue()))
            .limit(5)
            .forEach(entry -> System.out.println(
                entry.getKey() + " : "
                + entry.getValue() + " requests"
            ));
    }

    // Feature 3: Count HTTP errors per hour
    public static void showHourlyErrors(List<LogEntry> logs) {

        Map<String, Integer> hourlyErrors = new HashMap<>();

        for (LogEntry log : logs) {
            if (log.getStatusCode() >= 400) {
                String hour = log.getTime().substring(0, 2);

                hourlyErrors.put(
                    hour,
                    hourlyErrors.getOrDefault(hour, 0) + 1
                );
            }
        }

        System.out.println("\n===== ERRORS PER HOUR =====");

        for (int hour = 0; hour < 24; hour++) {
            String key = String.format("%02d", hour);

            System.out.println(
                key + ":00 - "
                + hourlyErrors.getOrDefault(key, 0)
                + " errors"
            );
        }
    }

    // Feature 4: Find the five slowest endpoints by average response time
    public static void showSlowestEndpoints(List<LogEntry> logs) {

        Map<String, Double> totalTime = new HashMap<>();
        Map<String, Integer> requestCounts = new HashMap<>();

        for (LogEntry log : logs) {
            String endpoint = log.getEndpoint();

            totalTime.put(
                endpoint,
                totalTime.getOrDefault(endpoint, 0.0)
                    + log.getResponseTime()
            );

            requestCounts.put(
                endpoint,
                requestCounts.getOrDefault(endpoint, 0) + 1
            );
        }

        System.out.println(
            "\n===== TOP 5 SLOWEST ENDPOINTS (AVERAGE) ====="
        );

        totalTime.entrySet().stream()
            .map(entry -> Map.entry(
                entry.getKey(),
                entry.getValue()
                    / requestCounts.get(entry.getKey())
            ))
            .sorted((a, b) ->
                Double.compare(b.getValue(), a.getValue()))
            .limit(5)
            .forEach(entry -> System.out.printf(
                "%s : %.2f ms%n",
                entry.getKey(),
                entry.getValue()
            ));
    }

    // Feature 5: Detect IP addresses with repeated failed logins

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
            "No IP addresses reached the threshold of 5 failed logins."
        );
    }
}
}