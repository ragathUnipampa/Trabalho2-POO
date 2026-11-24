package repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;

public abstract class BaseRepository<T> {
    protected final Gson gson;
    protected final String baseUrl;

    protected BaseRepository(String baseUrl) {
        this.baseUrl = baseUrl;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    protected void appendFilter(
            StringBuilder query,
            Set<String> allowedFields,
            String field,
            Object value
    ) {
        if (value == null) {
            return;
        }

        if (value instanceof String valorString && valorString.isBlank()) {
            return;
        }

        if (!allowedFields.contains(field)) {
            throw new IllegalArgumentException("Campo de consulta nao permitido: " + field);
        }

        query.append("&")
                .append(encodeQueryComponent(field))
                .append("=eq.")
                .append(encodeQueryComponent(String.valueOf(value)));
    }

    protected Map<String, Object> sanitizePatchPayload(
            Map<String, Object> payload,
            Set<String> allowedFields
    ) {
        if (payload == null || payload.isEmpty()) {
            throw new IllegalArgumentException("Nenhum dado foi informado para atualizacao.");
        }

        Map<String, Object> sanitized = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : payload.entrySet()) {
            if (!allowedFields.contains(entry.getKey())) {
                throw new IllegalArgumentException("Campo de atualizacao nao permitido: " + entry.getKey());
            }
            if (entry.getValue() == null) {
                throw new IllegalArgumentException("O campo '" + entry.getKey() + "' nao pode ser nulo.");
            }
            sanitized.put(entry.getKey(), entry.getValue());
        }
        return sanitized;
    }

    protected List<T> buscarLista(String query, Class<T> tipo) {
        try {
            String url = baseUrl + "?select=*";
            if (query != null && !query.isBlank()) {
                url += query;
            }
            String responseBody = SupabaseClient.send(url, "GET", null, null).body();
            Type tipoLista = TypeToken.getParameterized(List.class, tipo).getType();
            return gson.fromJson(responseBody, tipoLista);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao consultar registros: " + e.getMessage(), e);
        }
    }

    protected T salvar(
            String url,
            Object payload,
            Set<String> allowedFields,
            Class<T> tipo
    ) {
        try {
            JsonElement payloadElement = JsonParser.parseString(gson.toJson(payload));
            if (!payloadElement.isJsonObject()) {
                throw new IllegalArgumentException("O corpo da requisicao deve ser um objeto JSON.");
            }

            JsonObject payloadObject = payloadElement.getAsJsonObject();
            for (String field : payloadObject.keySet()) {
                if (!allowedFields.contains(field)) {
                    throw new IllegalArgumentException("Campo de cadastro nao permitido: " + field);
                }
            }

            String responseBody = SupabaseClient.send(
                    url,
                    "POST",
                    gson.toJson(payloadObject),
                    "return=representation"
            ).body();
            JsonElement resposta = JsonParser.parseString(responseBody);
            if (!resposta.isJsonArray() || resposta.getAsJsonArray().isEmpty()) {
                return null;
            }
            return gson.fromJson(resposta.getAsJsonArray().get(0), tipo);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao persistir registro: " + e.getMessage(), e);
        }
    }

    protected T atualizar(
            String url,
            Map<String, Object> payload,
            Set<String> allowedFields,
            Class<T> tipo
    ) {
        try {
            Map<String, Object> sanitizedPayload = sanitizePatchPayload(payload, allowedFields);
            String json = gson.toJson(sanitizedPayload);
            String responseBody = SupabaseClient.send(url, "PATCH", json, "return=representation").body();
            JsonElement resposta = JsonParser.parseString(responseBody);
            if (!resposta.isJsonArray() || resposta.getAsJsonArray().isEmpty()) {
                return null;
            }
            return gson.fromJson(resposta.getAsJsonArray().get(0), tipo);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar registro: " + e.getMessage(), e);
        }
    }

    protected String encodeQueryComponent(String valor) {
        if (valor == null) {
            return "";
        }

        StringBuilder encoded = new StringBuilder();
        try {
            String urLEncoded = java.net.URLEncoder.encode(valor, StandardCharsets.UTF_8);
            encoded.append(urLEncoded.replace("+", "%20"));
        } catch (Exception e) {
            throw new IllegalArgumentException("Nao foi possivel codificar o parametro.", e);
        }
        return encoded.toString();
    }
}
