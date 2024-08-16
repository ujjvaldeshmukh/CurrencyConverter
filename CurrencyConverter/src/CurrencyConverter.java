import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Scanner;

public class CurrencyConverter {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter base currency (e.g., USD): ");
        String baseCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Enter target currency (e.g., EUR): ");
        String targetCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Enter amount to convert: ");
        double amount = scanner.nextDouble();

        double exchangeRate = getExchangeRate(baseCurrency, targetCurrency);

        if (exchangeRate != 0.0) {
            double convertedAmount = amount * exchangeRate;
            System.out.printf("Converted amount: %.2f %s\n", convertedAmount, targetCurrency);
        } else {
            System.out.println("Failed to fetch exchange rate. Please check the currency codes and try again.");
        }

        scanner.close();
    }

    @SuppressWarnings("hiding")
    private static <BufferedReader> double getExchangeRate(String baseCurrency, String targetCurrency) {
        String apiUrl = "https://api.exchangerate-api.com/v4/latest/" + baseCurrency;

        try {
            URI url = new URI(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = ((java.io.BufferedReader) reader).readLine()) != null) {
                response.append(line);
            }

            ((java.io.BufferedReader) reader).close();

            String jsonResponse = response.toString();
            int targetIndex = jsonResponse.indexOf("\"" + targetCurrency + "\":");

            if (targetIndex != -1) {
                int startIndex = targetIndex + targetCurrency.length() + 3;
                int endIndex = jsonResponse.indexOf(",", startIndex);
                String rate = jsonResponse.substring(startIndex, endIndex);
                return Double.parseDouble(rate);
            }

        } catch (Exception e) {
            System.out.println("Error fetching data: " + e.getMessage());
        }

        return 0.0;
    }
}