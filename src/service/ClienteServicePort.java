package service;

import exception.LocacaoException;
import model.Cliente;

import java.util.List;

public interface ClienteServicePort {
    Cliente autenticarCliente(String cnh, String senha) throws LocacaoException;

    Cliente cadastrarCliente(Cliente cliente) throws LocacaoException;

    List<Cliente> buscarClientes();
}
