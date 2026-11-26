package repository;

import model.Locacao;

import java.util.List;
import java.util.Set;

/**
 * Implementação concreta de {@link LocacaoRepositoryPort} que gerencia a persistência
 * e consulta de registros na tabela {@code locacao} do Supabase.
 */
public class LocacaoRepository extends BaseRepository<Locacao> implements LocacaoRepositoryPort {
    private static final String BASE_URL = SupabaseClient.getBaseUrl() + "/rest/v1/locacao";
    private static final Set<String> FILTER_FIELDS =
            Set.of("cliente_id", "veiculo_placa");
    private static final Set<String> WRITE_FIELDS =
            Set.of("cliente_id", "data_inicio", "data_fim", "dias_alugados",
                    "valor_total", "forma_pagamento", "veiculo_placa");

    /**
     * Constrói o repositório de locações apontando para o endpoint {@code /rest/v1/locacao}.
     */
    public LocacaoRepository() {
        super(BASE_URL);
    }

    /**
     * {@inheritDoc}
     * <p>Nesta implementação, aplica filtros diretos pelos campos {@code cliente_id} e {@code veiculo_placa}.</p>
     */
    @Override
    public List<Locacao> buscarLocacoes(Long clienteId, String dataInicio, String dataFim, String formaPagamento, String veiculoPlaca) {
        StringBuilder query = new StringBuilder();
        appendFilter(query, FILTER_FIELDS, "cliente_id", clienteId);
        appendFilter(query, FILTER_FIELDS, "veiculo_placa", veiculoPlaca);
        return buscarLista(query.toString(), Locacao.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Locacao cadastrarLocacao(Locacao novaLocacao) {
        return salvar(baseUrl, novaLocacao, WRITE_FIELDS, Locacao.class);
    }
}
