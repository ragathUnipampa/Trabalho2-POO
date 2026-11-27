package InterfaceGrafica;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Window;
import java.util.LinkedHashMap;
import java.util.Map;
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

/**
 * Classe utilitária responsável pela identidade visual, paleta de cores,
 * fábrica de componentes estilizados e execução assíncrona de tarefas na interface Swing.
 */
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

    /**
     * Inicializa o tema visual FlatLaf Light e configura as propriedades globais
     * de fontes, cores e arredondamento de componentes no {@link UIManager}.
     *
     * @throws IllegalStateException Se o FlatLaf falhar ao configurar o tema claro.
     */
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

    /**
     * Cria a estrutura padrão de página contendo margens, cabeçalho institucional
     * e área central de conteúdo.
     *
     * @param titulo   O título principal exibido no cabeçalho da página.
     * @param conteudo O componente central a ser exibido na página, ou {@code null}.
     * @return Um {@link JPanel} configurado como contêiner principal da tela.
     */
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

    /**
     * Cria um contêiner tipo cartão com fundo branco, borda sutil e espaçamento interno.
     *
     * @param layout O gerenciador de layout a ser aplicado ao cartão.
     * @return O {@link JPanel} estilizado como cartão.
     */
    static JPanel criarCartao(LayoutManager layout) {
        JPanel cartao = new JPanel(layout);
        cartao.setBackground(SUPERFICIE);
        cartao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDA),
                BorderFactory.createEmptyBorder(22, 24, 22, 24)));
        return cartao;
    }

    /**
     * Cria um agrupamento vertical contendo um rótulo textual e um componente de entrada estilizado.
     *
     * @param rotulo O texto descritivo exibido acima do campo.
     * @param campo  O componente de entrada de dados.
     * @return Um {@link JPanel} agrupando o rótulo e o campo.
     */
    static JPanel criarCampo(String rotulo, JComponent campo) {
        JLabel label = new JLabel(rotulo);
        return criarCampo(label, campo);
    }

    /**
     * Cria um agrupamento vertical associando um {@link JLabel} existente a um componente de entrada estilizado.
     *
     * @param label O rótulo já instanciado a ser exibido acima do campo.
     * @param campo O componente de entrada de dados.
     * @return Um {@link JPanel} agrupando o rótulo e o campo.
     */
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

    /**
     * Aplica fonte, cores, bordas e dimensões padrão a um componente de formulário.
     *
     * @param campo O {@link JComponent} a ser estilizado.
     */
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

    /**
     * Cria um botão de ação principal com fundo na cor primária e texto em branco.
     *
     * @param texto O rótulo do botão.
     * @return O {@link JButton} primário estilizado.
     */
    static JButton botaoPrimario(String texto) {
        JButton botao = criarBotao(texto);
        botao.setBackground(PRIMARIA);
        botao.setForeground(Color.WHITE);
        botao.setBorder(BorderFactory.createEmptyBorder(11, 18, 11, 18));
        return botao;
    }

    /**
     * Cria um botão de ação secundária com fundo branco, borda definida e texto escuro.
     *
     * @param texto O rótulo do botão.
     * @return O {@link JButton} secundário estilizado.
     */
    static JButton botaoSecundario(String texto) {
        JButton botao = criarBotao(texto);
        botao.setBackground(SUPERFICIE);
        botao.setForeground(PRIMARIA_ESCURO);
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDA),
                BorderFactory.createEmptyBorder(10, 17, 10, 17)));
        return botao;
    }

    /**
     * Executa uma tarefa em segundo plano utilizando {@link javax.swing.SwingWorker},
     * desabilitando temporariamente o botão acionado e os controles informados,
     * além de alterar o texto do botão durante o processamento.
     *
     * @param <T>                  O tipo do resultado retornado pela tarefa.
     * @param botao                O botão que disparou a ação.
     * @param textoDuranteExecucao O texto exibido no botão enquanto a tarefa está em andamento.
     * @param tarefa               A operação a ser executada fora da thread de interface (EDT).
     * @param aoConcluir           O callback executado na EDT quando a tarefa termina com sucesso.
     * @param aoFalhar             O callback executado na EDT caso ocorra uma exceção.
     * @param controles            Componentes adicionais que devem ficar desabilitados durante a execução.
     */
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
        executarEmSegundoPlano(
                tarefa, aoConcluir, aoFalhar, botao, textoDuranteExecucao, controles);
    }

    /**
     * Executa uma tarefa em segundo plano sem vínculo com um botão específico,
     * desabilitando temporariamente os controles fornecidos durante o processamento.
     *
     * @param <T>        O tipo do resultado retornado pela tarefa.
     * @param tarefa     A operação a ser executada fora da thread de interface (EDT).
     * @param aoConcluir O callback executado na EDT quando a tarefa termina com sucesso.
     * @param aoFalhar   O callback executado na EDT caso ocorra uma exceção.
     * @param controles  Componentes que devem ficar desabilitados durante a execução.
     */
    static <T> void executarEmSegundoPlano(
            Callable<T> tarefa,
            Consumer<T> aoConcluir,
            Consumer<Throwable> aoFalhar,
            JComponent... controles) {
        executarEmSegundoPlano(tarefa, aoConcluir, aoFalhar, null, null, controles);
    }

    private static <T> void executarEmSegundoPlano(
            Callable<T> tarefa,
            Consumer<T> aoConcluir,
            Consumer<Throwable> aoFalhar,
            JButton botao,
            String textoDuranteExecucao,
            JComponent... controles) {
        Map<JComponent, Boolean> estadosHabilitacao = new LinkedHashMap<>();
        String textoOriginal = null;
        if (botao != null) {
            textoOriginal = botao.getText();
            estadosHabilitacao.put(botao, botao.isEnabled());
            botao.setEnabled(false);
            botao.setText(textoDuranteExecucao);
        }
        for (JComponent controle : controles) {
            estadosHabilitacao.putIfAbsent(controle, controle.isEnabled());
            controle.setEnabled(false);
        }

        String textoRestauracao = textoOriginal;
        new javax.swing.SwingWorker<T, Void>() {
            @Override
            protected T doInBackground() throws Exception {
                return tarefa.call();
            }

            @Override
            protected void done() {
                estadosHabilitacao.forEach(JComponent::setEnabled);
                if (botao != null) {
                    botao.setText(textoRestauracao);
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

    /**
     * Aplica estilos de fonte, altura de linha, cores de seleção e cabeçalho a uma {@link JTable}.
     *
     * @param tabela A tabela a ser estilizada.
     */
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

    /**
     * Estiliza uma tabela e a envolve em um painel de rolagem ({@link JScrollPane}) com borda padrão.
     *
     * @param tabela A tabela a ser inserida no painel de rolagem.
     * @return O {@link JScrollPane} contendo a tabela estilizada.
     */
    static JScrollPane criarScrollTabela(JTable tabela) {
        estilizarTabela(tabela);
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(BORDA));
        scroll.getViewport().setBackground(SUPERFICIE);
        return scroll;
    }

    /**
     * Exibe uma caixa de diálogo de aviso modal ao usuário.
     *
     * @param parent   A janela pai sobre a qual o diálogo será centralizado.
     * @param mensagem A mensagem de aviso a ser exibida.
     */
    static void aviso(Window parent, String mensagem) {
        JOptionPane.showMessageDialog(parent, mensagem, "Atencao", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Exibe uma caixa de diálogo de erro modal contendo uma descrição amigável
     * acompanhada da mensagem técnica da exceção ocorrida.
     *
     * @param parent   A janela pai sobre a qual o diálogo será centralizado.
     * @param mensagem A mensagem principal explicando a falha.
     * @param erro     A exceção capturada que originou o erro.
     */
    static void erro(Window parent, String mensagem, Throwable erro) {
        String detalhe = erro.getMessage() == null ? "Erro desconhecido." : erro.getMessage();
        JOptionPane.showMessageDialog(parent, mensagem + "\n" + detalhe, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
