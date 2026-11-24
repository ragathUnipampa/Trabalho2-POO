package repository;

import model.Locacao;

import java.util.List;
import java.util.Set;

public class LocacaoRepository extends BaseRepository<Locacao> implements LocacaoRepositoryPort {
    private static final String BASE_URL = SupabaseClient.getBaseUrl() + "/rest/v1/locacao";
    private static final Set<String> FILTER_FIELDS =
            Set.of("cliente_id", "veiculo_placa");
    private static final Set<String> WRITE_FIELDS =
            Set.of("cliente_id", "data_inicio", "data_fim", "dias_alugados",
                    "valor_total", "forma_pagamento", "veiculo_placa");

    public LocacaoRepository() {
        super(BASE_URL);
    }

    public List<Locacao> buscarLocacoes(Long clienteId, String dataInicio, String dataFim, String formaPagamento, String veiculoPlaca) {
        StringBuilder query = new StringBuilder();
        appendFilter(query, FILTER_FIELDS, "cliente_id", clienteId);
        appendFilter(query, FILTER_FIELDS, "veiculo_placa", veiculoPlaca);
        return buscarLista(query.toString(), Locacao.class);
    }

    public Locacao cadastrarLocacao(Locacao novaLocacao) {
        return salvar(baseUrl, novaLocacao, WRITE_FIELDS, Locacao.class);
    }
}
