package repository;

import config.SupabaseConfig;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class SupabaseClient {
    private static final String SUPABASE_URL = SupabaseConfig.URL;
    private static final String SUPABASE_KEY = SupabaseConfig.ANON_KEY;

    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    private SupabaseClient() {
    }

    public static String getBaseUrl() {
        return SUPABASE_URL;
    }

    public static HttpResponse<String> send(String url, String method, String body, String prefer) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", SUPABASE_KEY)
                .header("Authorization", "Bearer " + SUPABASE_KEY);

        if (body != null && !body.isBlank()) {
            builder.header("Content-Type", "application/json");
        }

        if (prefer != null && !prefer.isBlank()) {
            builder.header("Prefer", prefer);
        }

        if ("GET".equalsIgnoreCase(method)) {
            builder.GET();
        } else if ("PATCH".equalsIgnoreCase(method)) {
            builder.method("PATCH", HttpRequest.BodyPublishers.ofString(body == null ? "{}" : body));
        } else {
            builder.method(method.toUpperCase(), HttpRequest.BodyPublishers.ofString(body == null ? "{}" : body));
        }

        HttpRequest request = builder.build();
        HttpResponse<String> response =
                HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException(
                    "Requisicao ao Supabase rejeitada (HTTP "
                            + response.statusCode()
                            + "): "
                            + response.body()
            );
        }
        return response;
    }
}
