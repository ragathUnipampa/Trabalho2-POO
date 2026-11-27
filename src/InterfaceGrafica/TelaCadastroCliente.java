package InterfaceGrafica;

import controller.SistemaController;
import enums.CategoriaCNH;
import exception.LocacaoException;

import javax.swing.*;
import java.awt.*;

/**
 * Tela de formulário destinada ao cadastro de novos clientes no sistema.
 * Coleta dados pessoais, informações da CNH e credenciais de acesso,
 * delegando a validação de negócio e persistência ao {@link SistemaController}.
 */
public class TelaCadastroCliente extends JFrame {
    private final SistemaController controller = new SistemaController();
    private final JTextField nome = new JTextField();
    private final JTextField telefone = new JTextField();
    private final JTextField cnh = new JTextField();
    private final JComboBox<String> categoria = new JComboBox<>(new String[]{"A", "B", "AB"});
    private final JTextField cep = new JTextField();
    private final JPasswordField senha = new JPasswordField();
    private JButton botaoSalvar;

    /**
     * Constrói e exibe a janela de cadastro de clientes com seus respectivos
     * campos de entrada e botões de ação.
     */
    public TelaCadastroCliente() {
        setTitle("Autobots");
        setSize(650, 600);
        setMinimumSize(new Dimension(560, 540));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        construirTela();
    }

    private void construirTela() {
        JPanel cartao = UiSupport.criarCartao(new BorderLayout(0, 18));
        JPanel campos = new JPanel(new GridLayout(0, 2, 14, 14));
        campos.setOpaque(false);
        campos.add(UiSupport.criarCampo("Nome completo", nome));
        campos.add(UiSupport.criarCampo("Telefone", telefone));
        campos.add(UiSupport.criarCampo("CNH", cnh));
        campos.add(UiSupport.criarCampo("Categoria CNH", categoria));
        campos.add(UiSupport.criarCampo("CEP", cep));
        campos.add(UiSupport.criarCampo("Senha", senha));
        cartao.add(campos, BorderLayout.CENTER);

        JPanel botoes = new JPanel(new GridLayout(1, 2, 10, 0));
        botoes.setOpaque(false);
        botaoSalvar = UiSupport.botaoPrimario("Criar cadastro");
        JButton voltar = UiSupport.botaoSecundario("Voltar");
        botaoSalvar.addActionListener(event -> salvar());
        voltar.addActionListener(event -> dispose());
        botoes.add(botaoSalvar);
        botoes.add(voltar);
        cartao.add(botoes, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(botaoSalvar);
        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.setOpaque(false);
        conteudo.add(cartao, BorderLayout.NORTH);
        setContentPane(UiSupport.criarPagina(
                "Vamos preparar seu acesso.",
                conteudo));
    }

    private model.Cliente criarClienteDoFormulario() {
        model.Cliente cliente = new model.Cliente();
        cliente.setNome(nome.getText().trim());
        cliente.setTelefone(telefone.getText().trim());
        cliente.setCnh(cnh.getText().trim());
        cliente.setCategoriaCnh(CategoriaCNH.valueOf((String) categoria.getSelectedItem()));
        cliente.setCep(cep.getText().trim());
        cliente.setSenhaCliente(new String(senha.getPassword()));
        return cliente;
    }

    private void salvar() {
        if (nome.getText().trim().isEmpty() || cnh.getText().trim().isEmpty() || senha.getPassword().length == 0) {
            UiSupport.aviso(this, "Nome, CNH e senha sao obrigatorios.");
            return;
        }

        model.Cliente cliente = criarClienteDoFormulario();

        UiSupport.executarEmSegundoPlano(
                botaoSalvar,
                "Cadastrando...",
                () -> {
                    controller.cadastrarCliente(cliente);
                    return null;
                },
                resultado -> {
                    JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso.");
                    dispose();
                },
                erro -> {
                    if (erro instanceof LocacaoException) {
                        UiSupport.aviso(this, erro.getMessage());
                    } else {
                        UiSupport.erro(this, "Nao foi possivel cadastrar o cliente.", erro);
                    }
                },
                nome, telefone, cnh, categoria, cep, senha);
    }
}