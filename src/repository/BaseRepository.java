package repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public abstract class BaseRepository<T> {
    protected final Gson gson;
    protected final String baseUrl;

    protected BaseRepository(String baseUrl) {
        this.baseUrl = baseUrl;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    protected void appendFilter(StringBuilder query, String field, Object value) {
        if (value == null) {
            return;
        }

        if (value instanceof String valorString && valorString.isBlank()) {
            return;
        }

        query.append("&").append(field).append("=eq.").append(encode(String.valueOf(value)));
    }

    protected List<T> buscarLista(String query, Class<T> tipo) {
        try {
            String url = baseUrl + "?select=*" + (query == null ? "" : query);
            String responseBody = SupabaseClient.send(url, "GET", null, null).body();
            Type tipoLista = TypeToken.getParameterized(List.class, tipo).getType();
            return gson.fromJson(responseBody, tipoLista);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao consultar registros: " + e.getMessage(), e);
        }
    }

    protected T salvar(String url, Object payload, Class<T> tipo) {
        try {
            String json = gson.toJson(payload);
            String responseBody = SupabaseClient.send(url, "POST", json, "return=representation").body();
            JsonElement resposta = JsonParser.parseString(responseBody);
            if (!resposta.isJsonArray() || resposta.getAsJsonArray().isEmpty()) {
                return null;
            }
            return gson.fromJson(resposta.getAsJsonArray().get(0), tipo);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao persistir registro: " + e.getMessage(), e);
        }
    }

    protected T atualizar(String url, Map<String, Object> payload, Class<T> tipo) {
        try {
            String json = gson.toJson(payload);
            String responseBody = SupabaseClient.send(url, "PATCH", json, "return=representation").body();
            JsonElement resposta = JsonParser.parseString(responseBody);
            if (!resposta.isJsonArray() || resposta.getAsJsonArray().isEmpty()) {
                return null;
            }
            return gson.fromJson(resposta.getAsJsonArray().get(0), tipo);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar registro: " + e.getMessage(), e);
        }
    }

    protected String encode(String valor) {
        return URLEncoder.encode(valor, StandardCharsets.UTF_8);
    }
}
