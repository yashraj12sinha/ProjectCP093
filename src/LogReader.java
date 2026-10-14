import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LogReader {

    public static List<LogEntry> readLogs(String filePath) {
        List<LogEntry> logs = new ArrayList<>();

        try (BufferedReader reader =
                 new BufferedReader(new FileReader(filePath))) {

            String line;
            boolean firstLine = true;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    String[] parts = line.split(",", -1);

                    if (parts.length != 5) {
                        throw new IllegalArgumentException(
                            "Expected 5 fields"
                        );
                    }

                    String time = parts[0].trim();
                    String ipAddress = parts[1].trim();
                    String endpoint = parts[2].trim();
                    int statusCode =
                        Integer.parseInt(parts[3].trim());
                    double responseTime =
                        Double.parseDouble(parts[4].trim());

                    if (time.isEmpty()
                            || ipAddress.isEmpty()
                            || endpoint.isEmpty()) {
                        throw new IllegalArgumentException(
                            "Required field is empty"
                        );
                    }

                    if (statusCode < 100 || statusCode > 599) {
                        throw new IllegalArgumentException(
                            "Invalid HTTP status code"
                        );
                    }

                    if (!Double.isFinite(responseTime)
                            || responseTime < 0) {
                        throw new IllegalArgumentException(
                            "Invalid response time"
                        );
                    }

                    logs.add(new LogEntry(
                        time,
                        ipAddress,
                        endpoint,
                        statusCode,
                        responseTime
                    ));

                } catch (IllegalArgumentException e) {
                    System.out.println(
                        "Skipping invalid line " + lineNumber
                        + ": " + e.getMessage()
                    );
                }
            }

        } catch (IOException e) {
            System.out.println(
                "Could not read log file: " + e.getMessage()
            );
        }

        return logs;
    }
}