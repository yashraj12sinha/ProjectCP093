public class LogEntry {
    private String time;
    private String ipAddress;
    private String endpoint;
    private int statusCode;
    private double responseTime;

    public LogEntry(String time, String ipAddress,
                    String endpoint, int statusCode,
                    double responseTime) {
        this.time = time;
        this.ipAddress = ipAddress;
        this.endpoint = endpoint;
        this.statusCode = statusCode;
        this.responseTime = responseTime;
    }

    public String getTime() {
        return time;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public double getResponseTime() {
        return responseTime;
    }
}