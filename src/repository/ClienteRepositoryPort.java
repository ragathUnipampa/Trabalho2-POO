package repository;

import model.Cliente;

import java.util.List;

/**
 * Interface de porta (contrato) que define as operações de acesso e persistência
 * de dados para a entidade {@link Cliente}, isolando a camada de negócio dos detalhes
 * de infraestrutura (Princípio da Inversão de Dependência - DIP).
 */
public interface ClienteRepositoryPort {

    /**
     * Consulta clientes cadastrados aplicando os filtros fornecidos.
     *
     * @param nome         Filtro pelo nome do cliente (opcional).
     * @param telefone     Filtro pelo telefone do cliente (opcional).
     * @param cnh          Filtro exato pelo número da CNH (opcional).
     * @param categoriaCnh Filtro pela categoria da CNH (opcional).
     * @param cep          Filtro pelo CEP do cliente (opcional).
     * @return Uma lista de {@link Cliente} que correspondem aos critérios informados.
     */
    List<Cliente> buscarClientes(String nome, String telefone, String cnh, String categoriaCnh, String cep);

    /**
     * Verifica as credenciais de acesso de um cliente com base na CNH e senha.
     *
     * @param cnh   O número da CNH utilizado como identificador de login.
     * @param senha A senha de acesso informada.
     * @return O objeto {@link Cliente} autenticado se as credenciais forem válidas, ou {@code null} caso contrário.
     */
    Cliente autenticarCliente(String cnh, String senha);

    /**
     * Persiste um novo registro de cliente na base de dados.
     *
     * @param novoCliente O objeto {@link Cliente} contendo os dados a serem salvos.
     * @return O {@link Cliente} cadastrado com seu identificador gerado.
     */
    Cliente cadastrarCliente(Cliente novoCliente);
}
