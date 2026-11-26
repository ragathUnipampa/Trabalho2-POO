package service;

import exception.LocacaoException;
import model.Veiculo;

import java.util.List;
import java.util.Map;

/**
 * Interface de caso de uso que define as regras de negócio e operações de gerenciamento
 * da frota de veículos ({@link Veiculo}).
 */
public interface VeiculoServicePort {

    /**
     * Consulta veículos da frota filtrando por tipo e/ou status de disponibilidade.
     *
     * @param tipoVeiculo   O tipo do veículo ({@code "carro"} ou {@code "moto"}), opcional.
     * @param statusVeiculo O status do veículo ({@code "disponivel"} ou {@code "indisponivel"}), opcional.
     * @return Uma {@link List} de {@link Veiculo} que atendem aos critérios informados.
     * @throws LocacaoException Se ocorrer erro durante a consulta no repositório.
     */
    List<Veiculo> buscarVeiculos(String tipoVeiculo, String statusVeiculo) throws LocacaoException;

    /**
     * Busca um veículo específico pela sua placa de identificação.
     *
     * @param placa A placa do veículo a ser localizado.
     * @return O {@link Veiculo} correspondente à placa informada.
     * @throws LocacaoException Se a placa for nula/vazia ou nenhum veículo for encontrado com essa placa.
     */
    Veiculo buscarVeiculo(String placa) throws LocacaoException;

    /**
     * Valida os dados obrigatórios e a unicidade da placa antes de cadastrar um novo veículo.
     *
     * @param veiculo O objeto {@link Veiculo} contendo os dados do novo veículo.
     * @return O {@link Veiculo} cadastrado com seu identificador gerado.
     * @throws LocacaoException Se os dados do veículo forem inválidos, a placa já estiver cadastrada
     *                          ou ocorrer falha de persistência.
     */
    Veiculo cadastrarVeiculo(Veiculo veiculo) throws LocacaoException;

    /**
     * Valida e aplica alterações parciais nos dados de um veículo existente.
     *
     * @param id               O identificador único do veículo no banco de dados.
     * @param dadosAtualizados Um {@link Map} contendo os campos e valores a serem atualizados.
     * @return O {@link Veiculo} atualizado.
     * @throws LocacaoException Se o mapa de alterações estiver vazio/nulo ou ocorrer falha na atualização.
     */
    Veiculo editarVeiculo(long id, Map<String, Object> dadosAtualizados) throws LocacaoException;

    /**
     * Verifica se um veículo identificado pela placa está livre para ser alugado.
     *
     * @param placa A placa do veículo a ser consultado.
     * @return {@code true} se o veículo existir e estiver com status {@code DISPONIVEL}; {@code false} caso contrário.
     * @throws LocacaoException Se a placa for inválida ou o veículo não for encontrado.
     */
    boolean verificarDisponibilidade(String placa) throws LocacaoException;
}
