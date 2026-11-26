package InterfaceGrafica;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;

public final class UiSupport {
    static final Color FUNDO = new Color(244, 247, 255);
    static final Color SUPERFICIE = Color.WHITE;
    static final Color TEXTO = new Color(31, 44, 105);
    static final Color TEXTO_SECUNDARIO = new Color(88, 105, 153);
    static final Color PRIMARIA = new Color(45, 62, 139);
    static final Color PRIMARIA_ESCURO = new Color(31, 44, 105);
    static final Color DESTAQUE = new Color(126, 155, 215);
    static final Color BORDA = new Color(219, 228, 246);

    private UiSupport() {
    }

    public static void aplicarTema() {
        if (!FlatLightLaf.setup()) {
            throw new IllegalStateException("O FlatLaf nao conseguiu inicializar o tema claro.");
        }

        UIManager.put("defaultFont", new Font("SansSerif", Font.PLAIN, 14));
        UIManager.put("Component.accentColor", PRIMARIA);
        UIManager.put("Component.focusColor", DESTAQUE);
        UIManager.put("Component.borderColor", BORDA);
        UIManager.put("Component.arc", 10);
        UIManager.put("Button.arc", 12);
        UIManager.put("TextComponent.arc", 8);
        UIManager.put("OptionPane.background", FUNDO);
        UIManager.put("Panel.background", FUNDO);
        UIManager.put("Label.foreground", TEXTO);
        UIManager.put("TextField.background", SUPERFICIE);
        UIManager.put("PasswordField.background", SUPERFICIE);
        UIManager.put("ComboBox.background", SUPERFICIE);
    }

    static JPanel criarPagina(String titulo, Component conteudo) {
        JPanel pagina = new JPanel(new BorderLayout(0, 22));
        pagina.setBackground(FUNDO);
        pagina.setBorder(BorderFactory.createEmptyBorder(28, 34, 30, 34));
        pagina.add(criarCabecalho(titulo), BorderLayout.NORTH);
        if (conteudo != null) {
            pagina.add(conteudo, BorderLayout.CENTER);
        }
        return pagina;
    }

    private static JPanel criarCabecalho(String titulo) {
        JPanel cabecalho = new JPanel(new BorderLayout(0, 7));
        cabecalho.setOpaque(false);

        JLabel marca = new JLabel("AUTOBOTS");
        marca.setFont(new Font("SansSerif", Font.BOLD, 12));
        marca.setForeground(PRIMARIA);
        cabecalho.add(marca, BorderLayout.NORTH);

        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(new Font("SansSerif", Font.BOLD, 25));
        tituloLabel.setForeground(TEXTO);
        cabecalho.add(tituloLabel, BorderLayout.CENTER);

        JPanel linha = new JPanel();
        linha.setBackground(DESTAQUE);
        linha.setPreferredSize(new Dimension(44, 3));
        cabecalho.add(linha, BorderLayout.SOUTH);
        return cabecalho;
    }

    static JPanel criarCartao(LayoutManager layout) {
        JPanel cartao = new JPanel(layout);
        cartao.setBackground(SUPERFICIE);
        cartao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDA),
                BorderFactory.createEmptyBorder(22, 24, 22, 24)));
        return cartao;
    }

    static JPanel criarCampo(String rotulo, JComponent campo) {
        JLabel label = new JLabel(rotulo);
        return criarCampo(label, campo);
    }

    static JPanel criarCampo(JLabel label, JComponent campo) {
        JPanel grupo = new JPanel(new BorderLayout(0, 7));
        grupo.setOpaque(false);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(TEXTO_SECUNDARIO);
        label.setLabelFor(campo);
        grupo.add(label, BorderLayout.NORTH);
        estilizarCampo(campo);
        grupo.add(campo, BorderLayout.CENTER);
        return grupo;
    }

    static void estilizarCampo(JComponent campo) {
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setForeground(TEXTO);
        campo.setBackground(SUPERFICIE);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDA),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        campo.setPreferredSize(new Dimension(180, 40));
        if (campo instanceof JComboBox) {
            campo.setBorder(BorderFactory.createLineBorder(BORDA));
        }
    }

    static JButton botaoPrimario(String texto) {
        JButton botao = criarBotao(texto);
        botao.setBackground(PRIMARIA);
        botao.setForeground(Color.WHITE);
        botao.setBorder(BorderFactory.createEmptyBorder(11, 18, 11, 18));
        return botao;
    }

    static JButton botaoSecundario(String texto) {
        JButton botao = criarBotao(texto);
        botao.setBackground(SUPERFICIE);
        botao.setForeground(PRIMARIA_ESCURO);
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDA),
                BorderFactory.createEmptyBorder(10, 17, 10, 17)));
        return botao;
    }

    static <T> void executarEmSegundoPlano(
            JButton botao,
            String textoDuranteExecucao,
            Callable<T> tarefa,
            Consumer<T> aoConcluir,
            Consumer<Throwable> aoFalhar,
            JComponent... controles) {
        if (!botao.isEnabled()) {
            return;
        }
        String textoOriginal = botao.getText();
        botao.setEnabled(false);
        botao.setText(textoDuranteExecucao);
        for (JComponent controle : controles) {
            controle.setEnabled(false);
        }

        new javax.swing.SwingWorker<T, Void>() {
            @Override
            protected T doInBackground() throws Exception {
                return tarefa.call();
            }

            @Override
            protected void done() {
                botao.setEnabled(true);
                botao.setText(textoOriginal);
                for (JComponent controle : controles) {
                    controle.setEnabled(true);
                }
                try {
                    aoConcluir.accept(get());
                } catch (InterruptedException erro) {
                    Thread.currentThread().interrupt();
                    aoFalhar.accept(erro);
                } catch (ExecutionException erro) {
                    aoFalhar.accept(erro.getCause());
                }
            }
        }.execute();
    }

    static <T> void executarEmSegundoPlano(
            Callable<T> tarefa,
            Consumer<T> aoConcluir,
            Consumer<Throwable> aoFalhar,
            JComponent... controles) {
        for (JComponent controle : controles) {
            controle.setEnabled(false);
        }

        new javax.swing.SwingWorker<T, Void>() {
            @Override
            protected T doInBackground() throws Exception {
                return tarefa.call();
            }

            @Override
            protected void done() {
                for (JComponent controle : controles) {
                    controle.setEnabled(true);
                }
                try {
                    aoConcluir.accept(get());
                } catch (InterruptedException erro) {
                    Thread.currentThread().interrupt();
                    aoFalhar.accept(erro);
                } catch (ExecutionException erro) {
                    aoFalhar.accept(erro.getCause());
                }
            }
        }.execute();
    }

    private static JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("SansSerif", Font.BOLD, 13));
        botao.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        botao.setOpaque(true);
        return botao;
    }

    static void estilizarTabela(JTable tabela) {
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabela.setForeground(TEXTO);
        tabela.setBackground(SUPERFICIE);
        tabela.setSelectionBackground(new Color(225, 233, 250));
        tabela.setSelectionForeground(TEXTO);
        tabela.setRowHeight(32);
        tabela.setShowVerticalLines(false);
        tabela.setGridColor(BORDA);
        tabela.setFillsViewportHeight(true);

        JTableHeader cabecalho = tabela.getTableHeader();
        cabecalho.setFont(new Font("SansSerif", Font.BOLD, 12));
        cabecalho.setForeground(TEXTO);
        cabecalho.setBackground(new Color(237, 242, 252));
        cabecalho.setPreferredSize(new Dimension(cabecalho.getPreferredSize().width, 36));
    }

    static JScrollPane criarScrollTabela(JTable tabela) {
        estilizarTabela(tabela);
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(BORDA));
        scroll.getViewport().setBackground(SUPERFICIE);
        return scroll;
    }

    static void aviso(JFrame parent, String mensagem) {
        JOptionPane.showMessageDialog(parent, mensagem, "Atencao", JOptionPane.WARNING_MESSAGE);
    }

    static void erro(JFrame parent, String mensagem, Throwable erro) {
        String detalhe = erro.getMessage() == null ? "Erro desconhecido." : erro.getMessage();
        JOptionPane.showMessageDialog(parent, mensagem + "\n" + detalhe, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
