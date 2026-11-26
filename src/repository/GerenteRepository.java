package repository;

import model.Gerente;

import java.util.List;
import java.util.Set;

/**
 * Implementação concreta de {@link GerenteRepositoryPort} que realiza consultas
 * e autenticação de administradores na tabela {@code gerente} do Supabase.
 */
public class GerenteRepository extends BaseRepository<Gerente> implements GerenteRepositoryPort {
    private static final String BASE_URL = SupabaseClient.getBaseUrl() + "/rest/v1/gerente";
    private static final Set<String> FILTER_FIELDS = Set.of("email");

    /**
     * Constrói o repositório de gerentes apontando para o endpoint {@code /rest/v1/gerente}.
     */
    public GerenteRepository() {
        super(BASE_URL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Gerente> buscarGerentes(String email) {
        StringBuilder query = new StringBuilder();
        appendFilter(query, FILTER_FIELDS, "email", email);
        return buscarLista(query.toString(), Gerente.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
