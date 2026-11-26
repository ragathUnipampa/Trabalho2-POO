package service;

import exception.LocacaoException;
import model.Cliente;
import repository.ClienteRepository;
import repository.ClienteRepositoryPort;

import java.util.List;

/**
 * Implementação concreta de {@link ClienteServicePort} contendo as regras de negócio
 * para autenticação, validação cadastral e consulta de clientes.
 */
public class ClienteService implements ClienteServicePort {
    private final ClienteRepositoryPort clienteRepository;

    /**
     * Constrói o serviço de clientes utilizando a implementação padrão de {@link ClienteRepository}.
     */
    public ClienteService() {
        this(new ClienteRepository());
    }

    /**
     * Constrói o serviço de clientes recebendo a porta do repositório por injeção de dependência.
     *
     * @param clienteRepository A implementação de {@link ClienteRepositoryPort} a ser utilizada.
     */
    public ClienteService(ClienteRepositoryPort clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Cliente autenticarCliente(String cnh, String senha) throws LocacaoException {
        if (cnh == null || cnh.isBlank()) {
            throw new LocacaoException("CNH inválida para autenticação.");
        }
        if (senha == null || senha.isBlank()) {
            throw new LocacaoException("Senha inválida para autenticação.");
        }

        try {
            return clienteRepository.autenticarCliente(cnh, senha);
        } catch (RuntimeException erro) {
            throw new LocacaoException("Erro ao autenticar cliente: " + erro.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Cliente cadastrarCliente(Cliente cliente) throws LocacaoException {
        if (cliente == null) {
            throw new LocacaoException("Dados do usuário inválidos!");
        }
        if (cliente.getNome() == null || cliente.getNome().isBlank()) {
            throw new LocacaoException("Nome inválido! Insira um nome para continuar.");
        }
        if (cliente.getCnh() == null || cliente.getCnh().isBlank()) {
            throw new LocacaoException("A CNH do cliente é obrigatória!");
        }

        try {
            List<Cliente> existentes = clienteRepository.buscarClientes(null, null, cliente.getCnh(), null, null);
            if (!existentes.isEmpty()) {
                throw new LocacaoException("Já existe um cliente cadastrado com esta CNH.");
            }
            return clienteRepository.cadastrarCliente(cliente);
        } catch (RuntimeException erro) {
            throw new LocacaoException("Erro ao cadastrar cliente: " + erro.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException Se ocorrer falha inesperada na camada de persistência ao consultar os registros.
     */
    @Override
    public List<Cliente> buscarClientes() {
        try {
            return clienteRepository.buscarClientes(null, null, null, null, null);
        } catch (RuntimeException erro) {
            throw new IllegalStateException("Erro ao buscar clientes: " + erro.getMessage());
        }
    }
}
