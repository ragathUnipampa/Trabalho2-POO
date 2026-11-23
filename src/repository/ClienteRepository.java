package repository;

import model.Cliente;

import java.util.List;

public class ClienteRepository extends BaseRepository<Cliente> implements ClienteRepositoryPort {
    private static final String BASE_URL = SupabaseClient.getBaseUrl() + "/rest/v1/cliente";

    public ClienteRepository() {
        super(BASE_URL);
    }

    public List<Cliente> buscarClientes(String nome, String telefone, String cnh, String categoriaCnh, String cep) {
        StringBuilder query = new StringBuilder();
        appendFilter(query, "cnh", cnh);
        return buscarLista(query.toString(), Cliente.class);
    }

    public Cliente autenticarCliente(String cnh, String senha) {
        List<Cliente> clientes = buscarClientes(null, null, cnh, null, null);
        if (clientes.isEmpty()) {
            return null;
        }

        Cliente cliente = clientes.get(0);
        if (cliente.getSenhaCliente() != null && cliente.getSenhaCliente().equals(senha)) {
            return cliente;
        }

        return null;
    }

    public Cliente cadastrarCliente(Cliente novoCliente) {
        return salvar(baseUrl, novoCliente, Cliente.class);
    }
}
