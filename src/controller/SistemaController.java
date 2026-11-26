package controller;

import exception.LocacaoException;
import model.Cliente;
import model.Gerente;
import model.Locacao;
import model.Veiculo;
import pagamento.FormaPagamento;
import service.AppServices;

import java.util.List;
import java.util.Map;

/**
 * Controlador principal da aplicação responsável por intermediar as requisições
 * da interface gráfica e a camada de casos de uso ({@link AppServices}),
 * mantendo o desacoplamento entre a apresentação e as regras de negócio.
 */
public class SistemaController {
    private final AppServices appServices;

    /**
     * Constrói o controlador utilizando a instância padrão de {@link AppServices}.
     */
    public SistemaController() {
        this(new AppServices());
    }

    /**
     * Constrói o controlador recebendo a fachada de serviços por injeção de dependência.
     *
     * @param appServices A instância de {@link AppServices} que orquestra os casos de uso do sistema.
     */
    public SistemaController(AppServices appServices) {
        this.appServices = appServices;
    }

    /**
     * Solicita a autenticação de um cliente no sistema.
     *
     * @param cnh   O número da CNH do cliente.
     * @param senha A senha de acesso informada.
     * @return O {@link Cliente} autenticado, ou {@code null} se as credenciais forem inválidas.
     * @throws LocacaoException Se os dados informados forem inválidos ou ocorrer falha na autenticação.
     */
    public Cliente autenticarCliente(String cnh, String senha) throws LocacaoException {
        return appServices.autenticarCliente(cnh, senha);
    }

    /**
     * Solicita a autenticação de um gerente administrativo no sistema.
     *
     * @param email O e-mail institucional do gerente.
     * @param senha A senha de acesso informada.
     * @return O {@link Gerente} autenticado, ou {@code null} se as credenciais forem inválidas.
     * @throws LocacaoException Se os dados informados forem inválidos ou ocorrer falha na autenticação.
     */
    public Gerente autenticarGerente(String email, String senha) throws LocacaoException {
        return appServices.autenticarGerente(email, senha);
    }

    /**
     * Encaminha o cadastro de um novo cliente para validação e persistência.
     *
     * @param cliente O objeto {@link Cliente} preenchido na interface.
     * @return O {@link Cliente} cadastrado com seu identificador gerado.
     * @throws LocacaoException Se houver campos obrigatórios ausentes, CNH duplicada ou erro no banco.
     */
    public Cliente cadastrarCliente(Cliente cliente) throws LocacaoException {
        return appServices.cadastrarCliente(cliente);
    }

    /**
     * Obtém a lista de todos os clientes cadastrados no sistema.
     *
     * @return Uma {@link List} contendo os registros de {@link Cliente}.
     */
    public List<Cliente> buscarClientes() {
        return appServices.buscarClientes();
    }

    /**
     * Consulta veículos da frota aplicando filtros opcionais de tipo e disponibilidade.
     *
     * @param tipo   O tipo do veículo ({@code "carro"} ou {@code "moto"}), ou {@code null} para todos.
     * @param status O status de disponibilidade ({@code "disponivel"} ou {@code "indisponivel"}), ou {@code null} para todos.
     * @return Uma {@link List} de {@link Veiculo} correspondentes aos critérios informados.
     * @throws LocacaoException Se ocorrer falha durante a consulta.
     */
    public List<Veiculo> buscarVeiculos(String tipo, String status) throws LocacaoException {
        return appServices.buscarVeiculos(tipo, status);
    }

    /**
     * Busca um veículo específico a partir de sua placa de identificação.
     *
     * @param placa A placa do veículo consultado.
     * @return O objeto {@link Veiculo} correspondente à placa.
     * @throws LocacaoException Se a placa for inválida ou o veículo não for encontrado.
     */
    public Veiculo buscarVeiculo(String placa) throws LocacaoException {
        return appServices.buscarVeiculo(placa);
    }

    /**
     * Verifica se um determinado veículo está disponível para nova locação.
     *
     * @param placa A placa do veículo a ser verificado.
     * @return {@code true} se o veículo estiver disponível; {@code false} caso contrário.
     * @throws LocacaoException Se a placa for inválida ou o veículo não existir.
     */
    public boolean verificarDisponibilidade(String placa) throws LocacaoException {
        return appServices.verificarDisponibilidade(placa);
    }

    /**
     * Encaminha o cadastro de um novo veículo para a frota da locadora.
     *
     * @param veiculo O objeto {@link Veiculo} com os dados a serem registrados.
     * @return O {@link Veiculo} persistido.
     * @throws LocacaoException Se os dados forem inválidos, a placa já existir ou ocorrer falha no banco.
     */
    public Veiculo cadastrarVeiculo(Veiculo veiculo) throws LocacaoException {
        return appServices.cadastrarVeiculo(veiculo);
    }

    /**
     * Solicita a atualização parcial dos dados de um veículo existente.
     *
     * @param id               O identificador único do veículo.
     * @param dadosAtualizados Um {@link Map} contendo os campos e novos valores a serem aplicados.
     * @return O {@link Veiculo} atualizado.
     * @throws LocacaoException Se nenhuma alteração for informada ou ocorrer erro na atualização.
     */
    public Veiculo editarVeiculo(Long id, Map<String, Object> dadosAtualizados) throws LocacaoException {
        return appServices.editarVeiculo(id, dadosAtualizados);
    }

    /**
     * Orquestra a realização de uma nova locação de veículo por um cliente.
     *
     * @param cliente        O {@link Cliente} locatário.
     * @param veiculo        O {@link Veiculo} escolhido para aluguel.
     * @param locacao        O objeto {@link Locacao} contendo período e valor bruto.
     * @param formaPagamento A estratégia de {@link FormaPagamento} selecionada.
     * @return A {@link Locacao} efetivada com valor final calculado.
     * @throws LocacaoException Se alguma regra de negócio (CNH, disponibilidade, valor) for violada.
     */
    public Locacao realizarLocacao(Cliente cliente, Veiculo veiculo, Locacao locacao, FormaPagamento formaPagamento) throws LocacaoException {
        return appServices.realizarLocacao(cliente, veiculo, locacao, formaPagamento);
    }

    /**
     * Consulta o histórico de locações realizadas por um cliente específico.
     *
     * @param clienteId O identificador único do cliente.
     * @return Uma {@link List} de {@link Locacao} vinculadas ao cliente.
     * @throws LocacaoException Se ocorrer erro ao consultar o histórico.
     */
    public List<Locacao> consultarLocacoesCliente(Long clienteId) throws LocacaoException {
        return appServices.consultarLocacoesCliente(clienteId);
    }

    /**
     * Consulta o histórico de locações associadas a um veículo específico.
     *
     * @param placa A placa do veículo.
     * @return Uma {@link List} de {@link Locacao} vinculadas à placa informada.
     * @throws LocacaoException Se ocorrer erro ao consultar o histórico.
     */
    public List<Locacao> consultarLocacoesVeiculo(String placa) throws LocacaoException {
        return appServices.consultarLocacoesVeiculo(placa);
    }
}
