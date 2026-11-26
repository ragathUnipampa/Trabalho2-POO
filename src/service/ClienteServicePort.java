package service;

import exception.LocacaoException;
import model.Cliente;

import java.util.List;

/**
 * Interface de caso de uso que define as operações de negócio relacionadas à gestão
 * e autenticação de clientes ({@link Cliente}).
 */
public interface ClienteServicePort {

    /**
     * Valida as credenciais de um cliente e realiza sua autenticação no sistema.
     *
     * @param cnh   O número da CNH do cliente.
     * @param senha A senha de acesso informada.
     * @return O {@link Cliente} autenticado, ou {@code null} caso as credenciais não correspondam.
     * @throws LocacaoException Se a CNH ou senha estiverem vazias/nulas ou ocorrer erro de acesso aos dados.
     */
    Cliente autenticarCliente(String cnh, String senha) throws LocacaoException;

    /**
     * Valida os dados cadastrais e registra um novo cliente, garantindo que não haja
     * duplicidade de CNH na base de dados.
     *
     * @param cliente O objeto {@link Cliente} contendo os dados do novo cadastro.
     * @return O {@link Cliente} persistido com seu identificador gerado.
     * @throws LocacaoException Se o objeto for nulo, campos obrigatórios (nome/CNH) estiverem em branco
     *                          ou já existir um cliente cadastrado com a mesma CNH.
     */
    Cliente cadastrarCliente(Cliente cliente) throws LocacaoException;

    /**
     * Lista todos os clientes cadastrados no sistema.
     *
     * @return Uma {@link List} contendo todos os {@link Cliente} encontrados.
     */
    List<Cliente> buscarClientes();
}
