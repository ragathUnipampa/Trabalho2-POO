package repository;

import model.Veiculo;

import java.util.List;
import java.util.Map;

/**
 * Interface de porta (contrato) que define as operações de consulta, cadastro
 * e atualização de veículos ({@link Veiculo}) na camada de persistência,
 * desacoplando as regras de negócio da implementação do banco de dados.
 */
public interface VeiculoRepositoryPort {

    /**
     * Consulta veículos cadastrados na frota aplicando os filtros informados.
     *
     * @param tipoVeiculo   O tipo do veículo (ex: {@code "carro"} ou {@code "moto"}), opcional.
     * @param statusVeiculo O status de disponibilidade (ex: {@code "disponivel"} ou {@code "indisponivel"}), opcional.
     * @param marca         A fabricante do veículo, opcional.
     * @param modelo        O modelo do veículo, opcional.
     * @param placa         A placa de identificação exata do veículo, opcional.
     * @return Uma lista de {@link Veiculo} que correspondem aos critérios de busca.
     */
    List<Veiculo> buscarVeiculos(
            String tipoVeiculo,
            String statusVeiculo,
            String marca,
            String modelo,
            String placa
    );

    /**
     * Persiste um novo veículo no catálogo da locadora.
     *
     * @param novoVeiculo O objeto {@link Veiculo} contendo os dados a serem registrados.
     * @return O {@link Veiculo} cadastrado com seu identificador gerado pelo banco.
     */
    Veiculo cadastrarVeiculo(Veiculo novoVeiculo);

    /**
     * Atualiza parcialmente os dados de um veículo existente a partir do seu identificador.
     *
     * @param id               O identificador único do veículo no banco de dados.
     * @param dadosAtualizados Um {@link Map} contendo os nomes das colunas e os novos valores a serem aplicados.
     * @return O {@link Veiculo} atualizado retornado pela base de dados, ou {@code null} se não encontrado.
     */
    Veiculo editarVeiculo(long id, Map<String, Object> dadosAtualizados);
}
