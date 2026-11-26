package repository;

import config.SupabaseConfig;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Cliente HTTP utilitário responsável por encapsular a comunicação com a API REST do Supabase,
 * gerenciando cabeçalhos de autenticação ({@code apikey} e {@code Bearer}) e validação de status HTTP.
 */
public final class SupabaseClient {
    private static final String SUPABASE_URL = SupabaseConfig.URL;
    private static final String SUPABASE_KEY = SupabaseConfig.ANON_KEY;

    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    private SupabaseClient() {
    }

    /**
     * Retorna a URL base configurada para a instância do projeto no Supabase.
     *
     * @return A string contendo a URL raiz do servidor Supabase.
     */
    public static String getBaseUrl() {
        return SUPABASE_URL;
    }

    /**
     * Envia uma requisição HTTP síncrona para o Supabase e valida se o código de resposta
     * indica sucesso (faixa {@code 2xx}).
     *
     * @param url    A URL completa do recurso de destino.
     * @param method O verbo HTTP da requisição ({@code GET}, {@code POST}, {@code PATCH}, etc.).
     * @param body   O corpo da requisição em formato JSON (pode ser {@code null} em requisições {@code GET}).
     * @param prefer O valor do cabeçalho HTTP {@code Prefer} do PostgREST (ex: {@code "return=representation"}), opcional.
     * @return O objeto {@link HttpResponse} contendo o corpo e metadados da resposta bem-sucedida.
     * @throws IOException          Se ocorrer falha de rede ou se o servidor retornar status HTTP fora da faixa {@code 200-299}.
     * @throws InterruptedException Se a thread atual for interrompida enquanto aguarda a resposta HTTP.
     */
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
