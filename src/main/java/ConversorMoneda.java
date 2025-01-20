import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.Scanner;
import org.json.JSONObject;

public class ConversorMoneda {

    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("¡Bienvenido al Conversor de Monedas!");

        while (running) {
            System.out.println("\nSelecciona una opción de conversión:");
            System.out.println("1. USD a ARS (Dólar a Peso Argentino)");
            System.out.println("2. ARS a USD (Peso Argentino a Dólar)");
            System.out.println("3. BRL a USD (Real Brasileño a Dólar)");
            System.out.println("4. USD a BRL (Dólar a Real Brasileño)");
            System.out.println("5. EUR a USD (Euro a Dólar)");
            System.out.println("6. Salir");

            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 6) {
                System.out.println("¡Gracias por usar el conversor de monedas!");
                running = false;
            } else {
                System.out.print("Ingresa el valor que deseas convertir: ");
                double amount = scanner.nextDouble();

                String fromCurrency = "";
                String toCurrency = "";

                switch (option) {
                    case 1 -> {
                        fromCurrency = "USD";
                        toCurrency = "ARS";
                    }
                    case 2 -> {
                        fromCurrency = "ARS";
                        toCurrency = "USD";
                    }
                    case 3 -> {
                        fromCurrency = "BRL";
                        toCurrency = "USD";
                    }
                    case 4 -> {
                        fromCurrency = "USD";
                        toCurrency = "BRL";
                    }
                    case 5 -> {
                        fromCurrency = "EUR";
                        toCurrency = "USD";
                    }
                    default -> {
                        System.out.println("Opción inválida. Por favor, intenta nuevamente.");
                        continue;
                    }
                }

                double convertedAmount = ConversorMoneda(fromCurrency, toCurrency, amount);

                if (convertedAmount != -1) {
                    System.out.printf("El valor de %.2f %s corresponde a %.2f %s%n", amount, fromCurrency, convertedAmount, toCurrency);
                } else {
                    System.out.println("Hubo un error al obtener las tasas de cambio. Intenta más tarde.");
                }
            }
        }

        scanner.close();
    }

    public static double ConversorMoneda(String fromCurrency, String toCurrency, double amount) {
        try {
            // Construir la solicitud a la API
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + fromCurrency))
                    .GET()
                    .build();

            // Enviar la solicitud y obtener la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Procesar la respuesta JSON
            JSONObject jsonResponse = new JSONObject(response.body());
            double exchangeRate = jsonResponse.getJSONObject("rates").getDouble(toCurrency);

            // Calcular el monto convertido
            return amount * exchangeRate;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return -1; // Indicar que ocurrió un error
        }
    }
}