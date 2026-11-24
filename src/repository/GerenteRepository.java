package repository;

import model.Gerente;

import java.util.List;
import java.util.Set;

public class GerenteRepository extends BaseRepository<Gerente> implements GerenteRepositoryPort {
    private static final String BASE_URL = SupabaseClient.getBaseUrl() + "/rest/v1/gerente";
    private static final Set<String> FILTER_FIELDS = Set.of("email");

    public GerenteRepository() {
        super(BASE_URL);
    }

    public List<Gerente> buscarGerentes(String email) {
        StringBuilder query = new StringBuilder();
        appendFilter(query, FILTER_FIELDS, "email", email);
        return buscarLista(query.toString(), Gerente.class);
    }

    public Gerente autenticarGerente(String email, String senha) {
        List<Gerente> gerentes = buscarGerentes(email);
        if (gerentes.isEmpty()) {
            return null;
        }

        Gerente gerente = gerentes.get(0);
        if (gerente.getSenhaGerente() != null && gerente.getSenhaGerente().equals(senha)) {
            return gerente;
        }

        return null;
    }
}
