
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportWriter {

    public static void exportReport(
            List<LogEntry> logs,
            String startTime,
            String endTime) {

        try {
            java.io.File directory = new java.io.File("reports");

            if (!directory.exists()) {
                directory.mkdirs();
            }

            String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
            );

            String filePath = "reports/LogLens_Report_"
                    + timestamp + ".txt";

            try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter(filePath))) {

                writer.write("========== LOGLENS REPORT ==========");
                writer.newLine();
                writer.write("Generated: " + LocalDateTime.now());
                writer.newLine();
                writer.write("Records analyzed: " + logs.size());
                writer.newLine();

                if (startTime != null && endTime != null) {
                    writer.write("Time range: " + startTime
                            + " to " + endTime);
                    writer.newLine();
                }

                writer.newLine();
                writer.write("========== STATUS CODE COUNTS ==========");
                writer.newLine();

                java.util.Map<Integer, Integer> statusCounts =
                        new java.util.TreeMap<>();

                for (LogEntry log : logs) {
                    int status = log.getStatusCode();

                    statusCounts.put(
                        status,
                        statusCounts.getOrDefault(status, 0) + 1
                    );
                }

                for (java.util.Map.Entry<Integer, Integer> entry
                        : statusCounts.entrySet()) {
                    writer.write(
                        entry.getKey() + ": " + entry.getValue()
                    );
                    writer.newLine();
                }

                writer.newLine();
                writer.write("========== SLOWEST ENDPOINTS ==========");
                writer.newLine();

                java.util.Map<String, Double> totals =
                        new java.util.HashMap<>();
                java.util.Map<String, Integer> counts =
                        new java.util.HashMap<>();

                for (LogEntry log : logs) {
                    String endpoint = log.getEndpoint();

                    totals.put(
                        endpoint,
                        totals.getOrDefault(endpoint, 0.0)
                                + log.getResponseTime()
                    );

                    counts.put(
                        endpoint,
                        counts.getOrDefault(endpoint, 0) + 1
                    );
                }

                totals.keySet().stream()
                    .map(endpoint -> java.util.Map.entry(
                        endpoint,
                        totals.get(endpoint) / counts.get(endpoint)
                    ))
                   .sorted(
    java.util.Map.Entry.<String, Double>comparingByValue()
        .reversed()
)
                    .limit(5)
                    .forEach(entry -> {
                        try {
                            writer.write(
                                String.format(
                                    "%s: %.2f ms average",
                                    entry.getKey(),
                                    entry.getValue()
                                )
                            );
                            writer.newLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

                writer.newLine();
                writer.write("========== END OF REPORT ==========");
            }

            System.out.println("Report exported successfully.");
            System.out.println("File: " + filePath);

        } catch (IOException e) {
            System.out.println(
                "Could not export report: " + e.getMessage()
            );
        }
    }
}
