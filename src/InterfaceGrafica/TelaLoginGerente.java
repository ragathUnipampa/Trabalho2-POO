package InterfaceGrafica;

import controller.SistemaController;
import exception.LocacaoException;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Tela de autenticação dedicada aos gerentes da locadora.
 * Coleta e-mail e senha e valida as credenciais junto ao controlador do sistema.
 */
public class TelaLoginGerente extends JFrame {
    private final SistemaController controller = new SistemaController();
    private final JTextField email = new JTextField(); private final JPasswordField senha = new JPasswordField();
    private JButton botaoEntrar;

    /**
     * Constrói e exibe o formulário de login exclusivo para gerentes.
     */
    public TelaLoginGerente() {
        setTitle("Autobots");
        setSize(540, 430);
        setMinimumSize(new Dimension(480, 390));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel cartao = UiSupport.criarCartao(new BorderLayout(0, 18));
        JPanel campos = new JPanel(new GridLayout(0, 1, 0, 14));
        campos.setOpaque(false);
        campos.add(UiSupport.criarCampo("E-mail", email));
        campos.add(UiSupport.criarCampo("Senha", senha));
        cartao.add(campos, BorderLayout.CENTER);
        botaoEntrar = UiSupport.botaoPrimario("Entrar no painel");
        botaoEntrar.addActionListener(event -> entrar());
        senha.addActionListener(event -> entrar());
        cartao.add(botaoEntrar, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(botaoEntrar);
        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.setOpaque(false);
        conteudo.add(cartao, BorderLayout.NORTH);
        setContentPane(UiSupport.criarPagina(
                "Bem-vindo de volta.",
                conteudo));
    }
    private void entrar() {
        if (email.getText().trim().isEmpty() || senha.getPassword().length == 0) { UiSupport.aviso(this, "Informe e-mail e senha."); return; }
        String emailInformado = email.getText().trim();
        String senhaInformada = new String(senha.getPassword());
        UiSupport.executarEmSegundoPlano(
                botaoEntrar,
                "Validando...",
                () -> controller.autenticarGerente(emailInformado, senhaInformada),
                gerente -> {
                    if (gerente == null) {
                        UiSupport.aviso(this, "Email ou senha invalidos.");
                        return;
                    }
                    new TelaGerente(gerente).setVisible(true);
                    dispose();
                },
                erro -> {
                    if (erro instanceof LocacaoException) {
                        UiSupport.aviso(this, erro.getMessage());
                    } else {
                        UiSupport.erro(this, "Nao foi possivel acessar o banco de dados.", erro);
                    }
                },
                email, senha);
    }
}