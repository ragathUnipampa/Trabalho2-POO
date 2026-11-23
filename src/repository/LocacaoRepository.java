package repository;

import model.Locacao;

import java.util.List;

public class LocacaoRepository extends BaseRepository<Locacao> implements LocacaoRepositoryPort {
    private static final String BASE_URL = SupabaseClient.getBaseUrl() + "/rest/v1/locacao";

    public LocacaoRepository() {
        super(BASE_URL);
    }

    public List<Locacao> buscarLocacoes(Long clienteId, String dataInicio, String dataFim, String formaPagamento, String veiculoPlaca) {
        StringBuilder query = new StringBuilder();
        appendFilter(query, "cliente_id", clienteId);
        appendFilter(query, "veiculo_placa", veiculoPlaca);
        return buscarLista(query.toString(), Locacao.class);
    }

    public Locacao cadastrarLocacao(Locacao novaLocacao) {
        return salvar(baseUrl, novaLocacao, Locacao.class);
    }
}
