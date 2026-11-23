package repository;

import model.Cliente;

import java.util.List;

public interface ClienteRepositoryPort {
    List<Cliente> buscarClientes(String nome, String telefone, String cnh, String categoriaCnh, String cep);

    Cliente autenticarCliente(String cnh, String senha);

    Cliente cadastrarCliente(Cliente novoCliente);
}
