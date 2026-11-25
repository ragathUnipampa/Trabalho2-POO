package InterfaceGrafica;

import controller.SistemaController;

import java.awt.GridLayout;
import javax.swing.*;

/**
 * Tela inicial e ponto de entrada visual do Sistema de Locação de Veículos.
 * Responsável por autenticar usuários nos perfis de Cliente ou Gerente e
 * redirecionar para o cadastro de novos clientes.
 */
public class TelaPrincipal extends JFrame {
    private final SistemaController controller = new SistemaController();
    private final JTextField campoUsuario = new JTextField(20);
    private final JPasswordField campoSenha = new JPasswordField(20);
    private final JComboBox<String> comboPerfil = new JComboBox<>(new String[]{"Cliente", "Gerente"});
    private final JLabel labelUsuario = new JLabel("CNH:");

    /**
     * Constrói e inicializa a janela principal de acesso ao sistema,
     * configurando dimensões, layout e eventos de seleção de perfil.
     */
    public TelaPrincipal() {
        setTitle("Sistema de Locacao de Veiculos");
        setSize(460, 330);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(criarPainel());
        configurarSelecaoDePerfil();
    }

    private void configurarSelecaoDePerfil() {
        comboPerfil.addActionListener(event -> atualizarLabelUsuario());
        atualizarLabelUsuario();
    }

    private JPanel criarPainel() {
        JPanel painel = new JPanel(new GridLayout(5, 2, 8, 8));
        painel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        painel.add(new JLabel("Acesso ao sistema"));
        painel.add(new JPanel());
        painel.add(labelUsuario);
        painel.add(campoUsuario);
        painel.add(new JLabel("Senha:"));
        painel.add(campoSenha);
        painel.add(new JLabel("Perfil:"));
        painel.add(comboPerfil);
        JButton entrar = new JButton("Entrar");
        JButton cadastrar = new JButton("Criar cadastro");
        entrar.addActionListener(event -> entrar());
        campoUsuario.addActionListener(event -> entrar());
        cadastrar.addActionListener(event -> abrir(new TelaCadastroCliente()));
        painel.add(entrar);
        painel.add(cadastrar);
        return painel;
    }

    private void atualizarLabelUsuario() {
        labelUsuario.setText("Gerente".equals(comboPerfil.getSelectedItem()) ? "E-mail:" : "CNH:");
    }

    private boolean camposDeAcessoEstaoVazios(String usuario, String senha) {
        return usuario.isEmpty() || senha.isEmpty();
    }

    private void entrar() {
        String usuario = campoUsuario.getText().trim();
        String senha = new String(campoSenha.getPassword());
        if (camposDeAcessoEstaoVazios(usuario, senha)) {
            UiSupport.aviso(this, "Informe o usuario e a senha.");
            campoUsuario.requestFocusInWindow();
            return;
        }
        try {
            if ("Gerente".equals(comboPerfil.getSelectedItem())) {
                model.Gerente gerente = controller.autenticarGerente(usuario, senha);
                if (gerente == null) {
                    UiSupport.aviso(this, "Email ou senha invalidos.");
                    return;
                }
                abrir(new TelaGerente(gerente));
            } else {
                model.Cliente cliente = controller.autenticarCliente(usuario, senha);
                if (cliente == null) {
                    UiSupport.aviso(this, "CNH ou senha invalidas.");
                    return;
                }
                abrir(new TelaCliente(cliente));
            }
        } catch (exception.LocacaoException erro) {
            UiSupport.aviso(this, erro.getMessage());
        } catch (RuntimeException erro) {
            UiSupport.erro(this, "Nao foi possivel acessar o banco de dados.", erro);
        }
    }

    private void abrir(JFrame tela) {
        tela.setVisible(true);
        dispose();
    }
}

/**
 * Classe utilitária interna do pacote de interface gráfica para padronizar
 * a exibição de caixas de diálogo de aviso e erro ao usuário.
 */
class UiSupport {

    /**
     * Exibe uma caixa de diálogo de alerta com ícone de aviso.
     *
     * @param parent   A janela pai sobre a qual o diálogo será centralizado.
     * @param mensagem O texto informativo a ser exibido ao usuário.
     */
    static void aviso(JFrame parent, String mensagem) {
        JOptionPane.showMessageDialog(parent, mensagem, "Atencao", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Exibe uma caixa de diálogo de erro crítico concatenando a mensagem da exceção capturada.
     *
     * @param parent   A janela pai sobre a qual o diálogo será centralizado.
     * @param mensagem A mensagem principal explicando a falha ocorrida.
     * @param erro     A exceção em tempo de execução contendo os detalhes técnicos do erro.
     */
    static void erro(JFrame parent, String mensagem, RuntimeException erro) {
        String detalhe = erro.getMessage() == null ? "Erro desconhecido." : erro.getMessage();
        JOptionPane.showMessageDialog(parent, mensagem + "\n" + detalhe, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
