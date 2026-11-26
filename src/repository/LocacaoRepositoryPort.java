package repository;

import model.Locacao;

import java.util.List;

/**
 * Interface de porta (contrato) que especifica as operações de consulta e registro
 * de locações de veículos ({@link Locacao}).
 */
public interface LocacaoRepositoryPort {

    /**
     * Consulta locações registradas no sistema de acordo com os parâmetros de filtro fornecidos.
     *
     * @param clienteId      O identificador do cliente locatário (opcional).
     * @param dataInicio     A data de início da locação (opcional).
     * @param dataFim        A data de término da locação (opcional).
     * @param formaPagamento A modalidade de pagamento utilizada (opcional).
     * @param veiculoPlaca   A placa do veículo locado (opcional).
     * @return Uma lista de {@link Locacao} que atendem aos filtros aplicados.
     */
    List<Locacao> buscarLocacoes(
            Long clienteId,
            String dataInicio,
            String dataFim,
            String formaPagamento,
            String veiculoPlaca
    );

    /**
     * Persiste uma nova locação no banco de dados.
     *
     * @param novaLocacao O objeto {@link Locacao} contendo os dados da contratação.
     * @return A {@link Locacao} registrada com seu identificador persistido.
     */
    Locacao cadastrarLocacao(Locacao novaLocacao);
}
