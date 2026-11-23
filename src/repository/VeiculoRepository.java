package repository;

import model.Veiculo;

import java.util.List;
import java.util.Map;

public class VeiculoRepository extends BaseRepository<Veiculo> implements VeiculoRepositoryPort {
    private static final String BASE_URL = SupabaseClient.getBaseUrl() + "/rest/v1/veiculo";

    public VeiculoRepository() {
        super(BASE_URL);
    }

    public List<Veiculo> buscarVeiculos(String tipoVeiculo, String statusVeiculo, String marca, String modelo, String placa) {
        StringBuilder query = new StringBuilder();
        appendFilter(query, "tipo_veiculo", tipoVeiculo);
        appendFilter(query, "statusVeiculo", statusVeiculo);
        appendFilter(query, "placa", placa);
        return buscarLista(query.toString(), Veiculo.class);
    }

    public Veiculo cadastrarVeiculo(Veiculo novoVeiculo) {
        return salvar(baseUrl, novoVeiculo, Veiculo.class);
    }

    public Veiculo editarVeiculo(long id, Map<String, Object> dadosAtualizados) {
        String url = baseUrl + "?id=eq." + id;
        return atualizar(url, dadosAtualizados, Veiculo.class);
    }
}
