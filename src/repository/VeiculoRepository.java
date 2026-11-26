package repository;

import model.Veiculo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementação concreta de {@link VeiculoRepositoryPort} responsável pelo gerenciamento
 * da frota de veículos na tabela {@code veiculo} do Supabase.
 */
public class VeiculoRepository extends BaseRepository<Veiculo> implements VeiculoRepositoryPort {
    private static final String BASE_URL = SupabaseClient.getBaseUrl() + "/rest/v1/veiculo";
    private static final Set<String> FILTER_FIELDS =
            Set.of("tipo_veiculo", "statusVeiculo", "placa");
    private static final Set<String> WRITE_FIELDS =
            Set.of("placa", "tipo_veiculo", "marca", "modelo", "ano", "statusVeiculo", "valor_diario");

    /**
     * Constrói o repositório de veículos apontando para o endpoint {@code /rest/v1/veiculo}.
     */
    public VeiculoRepository() {
        super(BASE_URL);
    }

    /**
     * {@inheritDoc}
     * <p>Nesta implementação, aplica filtros diretos pelos campos {@code tipo_veiculo},
     * {@code statusVeiculo} e {@code placa}.</p>
     */
    @Override
    public List<Veiculo> buscarVeiculos(String tipoVeiculo, String statusVeiculo, String marca, String modelo, String placa) {
        StringBuilder query = new StringBuilder();
        appendFilter(query, FILTER_FIELDS, "tipo_veiculo", tipoVeiculo);
        appendFilter(query, FILTER_FIELDS, "statusVeiculo", statusVeiculo);
        appendFilter(query, FILTER_FIELDS, "placa", placa);
        return buscarLista(query.toString(), Veiculo.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Veiculo cadastrarVeiculo(Veiculo novoVeiculo) {
        return salvar(baseUrl, novoVeiculo, WRITE_FIELDS, Veiculo.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Veiculo editarVeiculo(long id, Map<String, Object> dadosAtualizados) {
        String url = baseUrl + "?id=eq." + encodeQueryComponent(String.valueOf(id));
        return atualizar(url, dadosAtualizados, WRITE_FIELDS, Veiculo.class);
    }
}
