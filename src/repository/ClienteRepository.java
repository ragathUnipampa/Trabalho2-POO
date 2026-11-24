package repository;

import model.Cliente;

import java.util.List;
import java.util.Set;

public class ClienteRepository extends BaseRepository<Cliente> implements ClienteRepositoryPort {
    private static final String BASE_URL = SupabaseClient.getBaseUrl() + "/rest/v1/cliente";
    private static final Set<String> FILTER_FIELDS = Set.of("cnh");
    private static final Set<String> WRITE_FIELDS = Set.of(
            "nome", "telefone", "cnh", "categoria_cnh", "cep", "senha_cliente"
    );

    public ClienteRepository() {
        super(BASE_URL);
    }

    public List<Cliente> buscarClientes(String nome, String telefone, String cnh, String categoriaCnh, String cep) {
        StringBuilder query = new StringBuilder();
        appendFilter(query, FILTER_FIELDS, "cnh", cnh);
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
        return salvar(baseUrl, novoCliente, WRITE_FIELDS, Cliente.class);
    }
}
