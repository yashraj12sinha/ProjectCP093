# LogLens — Test Cases

| Test ID | Test Scenario | Expected Result |
|---|---|---|
| TC01 | Load the provided CSV file | Valid records are loaded |
| TC02 | Count requests by status code | Correct count for each status code |
| TC03 | Display top five IP addresses | Up to five most frequent IPs are displayed |
| TC04 | Display top five endpoints | Up to five most frequent endpoints are displayed |
| TC05 | Analyze errors per hour | Error counts are displayed for each hour |
| TC06 | Find slowest endpoints | Endpoints are ranked by average response time |
| TC07 | Detect repeated failed logins | IPs with at least five failed login attempts are flagged |
| TC08 | Enter an invalid menu number | An invalid-choice message is displayed |
| TC09 | Enter text instead of a menu number | An invalid-input message is displayed |
| TC10 | Exit the application | The program terminates normally |

## Test Data

The dataset contains synthetic server logs. Five additional failed login records were added for IP `192.0.2.10` to test suspicious-IP detection.

## Testing Status

The main menu and all five core features have been tested manually and are working in the current environment.