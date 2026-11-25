package InterfaceGrafica;

import controller.SistemaController;
import exception.LocacaoException;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Tela dedicada à autenticação de gerentes por meio de e-mail e senha.
 * Em caso de sucesso na validação das credenciais, redireciona o usuário
 * para o painel administrativo ({@link TelaGerente}).
 */
public class TelaLoginGerente extends JFrame {
    private final SistemaController controller = new SistemaController();
    private final JTextField email = new JTextField();
    private final JPasswordField senha = new JPasswordField();

    /**
     * Constrói e exibe a janela de login específica para o perfil de gerente.
     */
    public TelaLoginGerente() {
        setTitle("Login do gerente");
        setSize(420, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));
        JPanel campos = new JPanel(new GridLayout(2, 2, 8, 8));
        campos.add(new JLabel("E-mail:"));
        campos.add(email);
        campos.add(new JLabel("Senha:"));
        campos.add(senha);
        painel.add(campos, BorderLayout.CENTER);
        JButton entrar = new JButton("Entrar");
        entrar.addActionListener(event -> entrar());
        painel.add(entrar, BorderLayout.SOUTH);
        setContentPane(painel);
    }

    private void entrar() {
        if (email.getText().trim().isEmpty() || senha.getPassword().length == 0) {
            UiSupport.aviso(this, "Informe e-mail e senha.");
            return;
        }
        try {
            model.Gerente gerente = controller.autenticarGerente(email.getText().trim(), new String(senha.getPassword()));
            if (gerente == null) {
                UiSupport.aviso(this, "Email ou senha invalidos.");
                return;
            }
            new TelaGerente(gerente).setVisible(true);
            dispose();
        } catch (LocacaoException erro) {
            UiSupport.aviso(this, erro.getMessage());
        } catch (RuntimeException erro) {
            UiSupport.erro(this, "Nao foi possivel acessar o banco de dados.", erro);
        }
    }
}