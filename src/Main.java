
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String filePath = "data/logs.csv";
        List<LogEntry> logs = LogReader.readLogs(filePath);

        if (logs.isEmpty()) {
            System.out.println("No logs loaded. Check data/logs.csv.");
            return;
        }

        System.out.println("Loaded " + logs.size() + " log records.");

        Scanner scanner = new Scanner(System.in);
       int choice = 0;

        do {
            System.out.println("\n========== LOGLENS ==========");
            System.out.println("1. Count requests by status code");
            System.out.println("2. Show top five IPs and endpoints");
            System.out.println("3. Show errors per hour");
            System.out.println("4. Show slowest endpoints");
            System.out.println("5. Detect suspicious IP addresses");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Enter a number from 1 to 6.");
                scanner.nextLine();
                continue;
            }

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    LogAnalyzer.countStatusCodes(logs);
                    break;

                case 2:
                    LogAnalyzer.showTopFive(logs);
                    break;

                case 3:
                    LogAnalyzer.showHourlyErrors(logs);
                    break;

                case 4:
                    LogAnalyzer.showSlowestEndpoints(logs);
                    break;

                case 5:
                    LogAnalyzer.detectSuspiciousIPs(logs);
                    break;

                case 6:
                    System.out.println("Exiting LogLens. Goodbye!");
                    break;

                default:
                    System.out.println(
                        "Invalid choice. Please select 1 to 6."
                    );
            }

        } while (choice != 6);

        scanner.close();
    }
}