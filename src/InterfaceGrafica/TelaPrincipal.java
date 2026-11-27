package InterfaceGrafica;

import controller.SistemaController;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.*;

/**
 * Tela inicial e ponto de entrada visual da aplicação Autobots.
 * Permite realizar login tanto como {@code Cliente} (via CNH) quanto como
 * {@code Gerente} (via e-mail), além de oferecer acesso ao cadastro de novos clientes.
 */
public class TelaPrincipal extends JFrame {
    private final SistemaController controller = new SistemaController();
    private final JTextField campoUsuario = new JTextField(20);
    private final JPasswordField campoSenha = new JPasswordField(20);
    private final JComboBox<String> comboPerfil = new JComboBox<>(new String[]{"Cliente", "Gerente"});
    private final JLabel labelUsuario = new JLabel("CNH:");
    private JButton botaoEntrar;
    private JButton botaoCadastro;
    private JPanel botoes;

    /**
     * Constrói e inicializa a janela principal de autenticação do sistema.
     */
    public TelaPrincipal() {
        setTitle("Autobots");
        setSize(560, 570);
        setMinimumSize(new Dimension(500, 530));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(criarPainel());
        configurarSelecaoDePerfil();
    }

    private void configurarSelecaoDePerfil() {
        comboPerfil.addActionListener(event -> atualizarPerfil());
        atualizarLabelUsuario();
        atualizarVisibilidadeCadastro();
    }

    private JPanel criarPainel() {
        JPanel cartao = UiSupport.criarCartao(new BorderLayout(0, 18));
        JPanel formulario = new JPanel(new GridLayout(0, 1, 0, 14));
        formulario.setOpaque(false);
        JLabel chamada = new JLabel("Acesse sua conta");
        chamada.setFont(new Font("SansSerif", Font.BOLD, 17));
        formulario.add(chamada);
        formulario.add(UiSupport.criarCampo("Perfil de acesso", comboPerfil));
        formulario.add(UiSupport.criarCampo(labelUsuario, campoUsuario));
        formulario.add(UiSupport.criarCampo("Senha", campoSenha));
        cartao.add(formulario, BorderLayout.CENTER);

        botoes = new JPanel(new GridLayout(1, 2, 10, 0));
        botoes.setOpaque(false);
        botaoEntrar = UiSupport.botaoPrimario("Entrar");
        botaoCadastro = UiSupport.botaoSecundario("Criar cadastro");
        botaoEntrar.addActionListener(event -> entrar());
        campoUsuario.addActionListener(event -> entrar());
        campoSenha.addActionListener(event -> entrar());
        botaoCadastro.addActionListener(event -> abrir(new TelaCadastroCliente()));
        botoes.add(botaoEntrar);
        botoes.add(botaoCadastro);
        cartao.add(botoes, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(botaoEntrar);
        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.setOpaque(false);
        conteudo.add(cartao, BorderLayout.NORTH);
        return UiSupport.criarPagina(
                "Sua próxima rota começa aqui.",
                conteudo);
    }

    private void atualizarLabelUsuario() {
        labelUsuario.setText("Gerente".equals(comboPerfil.getSelectedItem()) ? "E-mail:" : "CNH:");
    }

    private void atualizarPerfil() {
        atualizarLabelUsuario();
        atualizarVisibilidadeCadastro();
    }

    private void atualizarVisibilidadeCadastro() {
        boolean clienteSelecionado = "Cliente".equals(comboPerfil.getSelectedItem());
        if (clienteSelecionado && botaoCadastro.getParent() == null) {
            botoes.add(botaoCadastro);
        } else if (!clienteSelecionado && botaoCadastro.getParent() == botoes) {
            botoes.remove(botaoCadastro);
        }
        botoes.setLayout(new GridLayout(1, clienteSelecionado ? 2 : 1, 10, 0));
        botoes.revalidate();
        botoes.repaint();
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
        if ("Gerente".equals(comboPerfil.getSelectedItem())) {
            UiSupport.executarEmSegundoPlano(
                    botaoEntrar,
                    "Validando...",
                    () -> controller.autenticarGerente(usuario, senha),
                    gerente -> {
                        if (gerente == null) {
                            UiSupport.aviso(this, "Email ou senha invalidos.");
                            return;
                        }
                        abrir(new TelaGerente(gerente));
                    },
                    this::tratarFalhaDeAcesso,
                    comboPerfil, campoUsuario, campoSenha);
        } else {
            UiSupport.executarEmSegundoPlano(
                    botaoEntrar,
                    "Validando...",
                    () -> controller.autenticarCliente(usuario, senha),
                    cliente -> {
                        if (cliente == null) {
                            UiSupport.aviso(this, "CNH ou senha invalidas.");
                            return;
                        }
                        abrir(new TelaVeiculos(this, cliente));
                    },
                    this::tratarFalhaDeAcesso,
                    comboPerfil, campoUsuario, campoSenha);
        }
    }

    private void tratarFalhaDeAcesso(Throwable erro) {
        if (erro instanceof exception.LocacaoException) {
            UiSupport.aviso(this, erro.getMessage());
        } else {
            UiSupport.erro(this, "Nao foi possivel acessar o banco de dados.", erro);
        }
    }

    private void abrir(JFrame tela) {
        tela.setVisible(true);
        dispose();
    }
}
