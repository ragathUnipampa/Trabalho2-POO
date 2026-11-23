package service;

import exception.LocacaoException;
import model.Cliente;
import repository.ClienteRepository;
import repository.ClienteRepositoryPort;

import java.util.List;

public class ClienteService implements ClienteServicePort {
    private final ClienteRepositoryPort clienteRepository;

    public ClienteService() {
        this(new ClienteRepository());
    }

    public ClienteService(ClienteRepositoryPort clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente autenticarCliente(String cnh, String senha) throws LocacaoException {
        if (cnh == null || cnh.isBlank()) {
            throw new LocacaoException("CNH inválida para autenticação.");
        }
        if (senha == null || senha.isBlank()) {
            throw new LocacaoException("Senha inválida para autenticação.");
        }

        try {
            return clienteRepository.autenticarCliente(cnh, senha);
        } catch (RuntimeException e) {
            throw new LocacaoException("Erro ao autenticar cliente: " + e.getMessage());
        }
    }

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
        } catch (RuntimeException e) {
            throw new LocacaoException("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    public List<Cliente> buscarClientes() {
        try {
            return clienteRepository.buscarClientes(null, null, null, null, null);
        } catch (RuntimeException e) {
            throw new IllegalStateException("Erro ao buscar clientes: " + e.getMessage());
        }
    }
}
