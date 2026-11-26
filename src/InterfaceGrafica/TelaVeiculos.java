package InterfaceGrafica;

import controller.SistemaController;
import exception.LocacaoException;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class TelaVeiculos extends JFrame {
    private final SistemaController controller = new SistemaController();
    private final boolean permiteEdicao;
    private final JComboBox<String> filtro = new JComboBox<>(new String[]{"todos", "carro", "moto"});
    private final JPanel listaVeiculos = new JPanel();
    private final List<CartaoVeiculo> cartoes = new ArrayList<>();
    private JButton botaoBuscar;
    private JButton botaoSalvar;

    public TelaVeiculos(JFrame anterior, boolean permiteEdicao) {
        this.permiteEdicao = permiteEdicao;
        setTitle("Autobots");
        setSize(900, 620);
        setMinimumSize(new Dimension(700, 460));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(anterior);

        JPanel conteudo = new JPanel(new BorderLayout(0, 14));
        conteudo.setOpaque(false);

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topo.setOpaque(false);
        JLabel filtroLabel = new JLabel("Categoria");
        filtroLabel.setLabelFor(filtro);
        topo.add(filtroLabel);
        UiSupport.estilizarCampo(filtro);
        botaoBuscar = UiSupport.botaoSecundario("Atualizar lista");
        botaoBuscar.addActionListener(event -> atualizarLista());
        topo.add(filtro);
        topo.add(botaoBuscar);

        if (permiteEdicao) {
            botaoSalvar = UiSupport.botaoPrimario("Salvar alteracoes");
            botaoSalvar.setEnabled(false);
            botaoSalvar.addActionListener(event -> salvarAlteracoes());
            topo.add(botaoSalvar);
        }

        JPanel ferramentas = UiSupport.criarCartao(new BorderLayout());
        ferramentas.add(topo, BorderLayout.CENTER);
        conteudo.add(ferramentas, BorderLayout.NORTH);

        listaVeiculos.setLayout(new javax.swing.BoxLayout(listaVeiculos, javax.swing.BoxLayout.Y_AXIS));
        listaVeiculos.setBackground(UiSupport.FUNDO);
        listaVeiculos.setBorder(BorderFactory.createEmptyBorder(2, 2, 8, 2));
        JScrollPane rolagem = new JScrollPane(listaVeiculos);
        rolagem.setBorder(BorderFactory.createEmptyBorder());
        rolagem.getViewport().setBackground(UiSupport.FUNDO);
        rolagem.getVerticalScrollBar().setUnitIncrement(18);
        rolagem.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        conteudo.add(rolagem, BorderLayout.CENTER);

        setContentPane(UiSupport.criarPagina("Veiculos da locadora.", conteudo));
        atualizarLista();
    }

    private void atualizarLista() {
        String tipo = "todos".equals(filtro.getSelectedItem()) ? null : (String) filtro.getSelectedItem();
        filtro.setEnabled(false);
        cartoes.forEach(cartao -> cartao.setEdicaoAtiva(false));
        if (botaoSalvar != null) {
            botaoSalvar.setEnabled(false);
        }

        UiSupport.executarEmSegundoPlano(
                botaoBuscar,
                "Carregando...",
                () -> controller.buscarVeiculos(tipo, null),
                veiculos -> {
                    filtro.setEnabled(true);
                    exibirVeiculos(veiculos);
                    if (botaoSalvar != null) {
                        botaoSalvar.setEnabled(!cartoes.isEmpty());
                    }
                },
                erro -> {
                    filtro.setEnabled(true);
                    cartoes.forEach(cartao -> cartao.setEdicaoAtiva(true));
                    if (botaoSalvar != null) {
                        botaoSalvar.setEnabled(!cartoes.isEmpty());
                    }
                    if (erro instanceof LocacaoException) {
                        UiSupport.aviso(this, erro.getMessage());
                    } else {
                        UiSupport.erro(this, "Nao foi possivel consultar os veiculos.", erro);
                    }
                });
    }

    private void exibirVeiculos(List<model.Veiculo> veiculos) {
        listaVeiculos.removeAll();
        cartoes.clear();

        if (veiculos.isEmpty()) {
            JLabel vazio = new JLabel("Nenhum veiculo encontrado para este filtro.");
            vazio.setForeground(UiSupport.TEXTO_SECUNDARIO);
            vazio.setFont(new Font("SansSerif", Font.PLAIN, 14));
            vazio.setBorder(BorderFactory.createEmptyBorder(22, 8, 22, 8));
            listaVeiculos.add(vazio);
        } else {
            for (model.Veiculo veiculo : veiculos) {
                CartaoVeiculo cartao = new CartaoVeiculo(veiculo);
                cartoes.add(cartao);
                listaVeiculos.add(cartao.painel);
                listaVeiculos.add(javax.swing.Box.createVerticalStrut(12));
            }
        }

        listaVeiculos.revalidate();
        listaVeiculos.repaint();
    }

    private void salvarAlteracoes() {
        if (!permiteEdicao || botaoSalvar == null) {
            return;
        }

        List<Map<String, Object>> alteracoesPorVeiculo = new ArrayList<>();
        try {
            for (CartaoVeiculo cartao : cartoes) {
                alteracoesPorVeiculo.add(cartao.obterAlteracoes());
            }
        } catch (IllegalArgumentException erro) {
            UiSupport.aviso(this, erro.getMessage());
            return;
        }

        cartoes.forEach(cartao -> cartao.setEdicaoAtiva(false));
        UiSupport.executarEmSegundoPlano(
                botaoSalvar,
                "Salvando...",
                () -> {
                    for (int indice = 0; indice < cartoes.size(); indice++) {
                        controller.editarVeiculo(
                                cartoes.get(indice).veiculo.getId(),
                                alteracoesPorVeiculo.get(indice));
                    }
                    return null;
                },
                resultado -> {
                    cartoes.forEach(cartao -> cartao.setEdicaoAtiva(true));
                    botaoBuscar.setEnabled(true);
                    filtro.setEnabled(true);
                    atualizarLista();
                    JOptionPane.showMessageDialog(this, "Alteracoes salvas com sucesso.");
                },
                erro -> {
                    cartoes.forEach(cartao -> cartao.setEdicaoAtiva(true));
                    botaoBuscar.setEnabled(true);
                    filtro.setEnabled(true);
                    if (erro instanceof LocacaoException) {
                        UiSupport.aviso(this, erro.getMessage());
                    } else {
                        UiSupport.erro(this, "Nao foi possivel salvar as alteracoes.", erro);
                    }
                },
                botaoBuscar,
                filtro);
    }

    private final class CartaoVeiculo {
        private final model.Veiculo veiculo;
        private final JPanel painel;
        private final JTextField placa;
        private final JComboBox<String> tipo;
        private final JTextField marca;
        private final JTextField modelo;
        private final JTextField ano;
        private final JComboBox<String> status;
        private final JTextField valorDiario;
        private final List<java.awt.Component> camposEditaveis = new ArrayList<>();

        private CartaoVeiculo(model.Veiculo veiculo) {
            this.veiculo = veiculo;
            painel = UiSupport.criarCartao(new BorderLayout(0, 15));
            painel.setAlignmentX(LEFT_ALIGNMENT);

            JPanel cabecalho = new JPanel(new BorderLayout(12, 0));
            cabecalho.setOpaque(false);
            JPanel identidade = new JPanel(new BorderLayout(0, 4));
            identidade.setOpaque(false);
            JLabel titulo = new JLabel(texto(veiculo.getMarca()) + " " + texto(veiculo.getModelo()));
            titulo.setFont(new Font("SansSerif", Font.BOLD, 19));
            titulo.setForeground(UiSupport.TEXTO);
            JLabel subtitulo = new JLabel(texto(veiculo.getTipoVeiculo()).toUpperCase()
                    + "  /  ANO " + texto(veiculo.getAno()));
            subtitulo.setFont(new Font("SansSerif", Font.BOLD, 10));
            subtitulo.setForeground(UiSupport.TEXTO_SECUNDARIO);
            identidade.add(titulo, BorderLayout.CENTER);
            identidade.add(subtitulo, BorderLayout.SOUTH);
            cabecalho.add(identidade, BorderLayout.CENTER);

            JLabel etiquetaStatus = new JLabel(texto(veiculo.getStatusVeiculo()).toUpperCase());
            etiquetaStatus.setFont(new Font("SansSerif", Font.BOLD, 10));
            etiquetaStatus.setForeground(UiSupport.PRIMARIA_ESCURO);
            etiquetaStatus.setOpaque(true);
            etiquetaStatus.setBackground(new java.awt.Color(232, 238, 251));
            etiquetaStatus.setBorder(BorderFactory.createEmptyBorder(7, 10, 7, 10));
            cabecalho.add(etiquetaStatus, BorderLayout.EAST);
            painel.add(cabecalho, BorderLayout.NORTH);

            JPanel detalhes = new JPanel(new GridLayout(0, 3, 12, 12));
            detalhes.setOpaque(false);
            placa = campoTexto(veiculo.getPlaca());
            tipo = campoOpcao(new String[]{"carro", "moto"}, veiculo.getTipoVeiculo());
            marca = campoTexto(veiculo.getMarca());
            modelo = campoTexto(veiculo.getModelo());
            ano = campoTexto(veiculo.getAno());
            status = campoOpcao(new String[]{"disponivel", "indisponivel"}, veiculo.getStatusVeiculo());
            valorDiario = campoTexto(veiculo.getValorDiario());

            detalhes.add(criarDetalhe("Placa", placa));
            detalhes.add(criarDetalhe("Tipo", tipo));
            detalhes.add(criarDetalhe("Status", status));
            detalhes.add(criarDetalhe("Marca", marca));
            detalhes.add(criarDetalhe("Modelo", modelo));
            detalhes.add(criarDetalhe("Ano", ano));
            detalhes.add(criarDetalhe("Valor diario", valorDiario));
            painel.add(detalhes, BorderLayout.CENTER);
            setEdicaoAtiva(permiteEdicao);
        }

        private JTextField campoTexto(Object valor) {
            JTextField campo = new JTextField(texto(valor));
            UiSupport.estilizarCampo(campo);
            camposEditaveis.add(campo);
            return campo;
        }

        private JComboBox<String> campoOpcao(String[] opcoes, String selecionado) {
            JComboBox<String> campo = new JComboBox<>(opcoes);
            campo.setSelectedItem(selecionado);
            UiSupport.estilizarCampo(campo);
            camposEditaveis.add(campo);
            return campo;
        }

        private JPanel criarDetalhe(String rotulo, javax.swing.JComponent campo) {
            if (permiteEdicao) {
                return UiSupport.criarCampo(rotulo, campo);
            }

            JPanel grupo = new JPanel(new BorderLayout(0, 7));
            grupo.setOpaque(false);
            JLabel label = new JLabel(rotulo);
            label.setFont(new Font("SansSerif", Font.BOLD, 12));
            label.setForeground(UiSupport.TEXTO_SECUNDARIO);
            grupo.add(label, BorderLayout.NORTH);

            JLabel valor = new JLabel(campo instanceof JComboBox
                    ? texto(((JComboBox<?>) campo).getSelectedItem())
                    : ((JTextField) campo).getText());
            valor.setFont(new Font("SansSerif", Font.BOLD, 14));
            valor.setForeground(UiSupport.TEXTO);
            valor.setOpaque(true);
            valor.setBackground(new java.awt.Color(247, 249, 255));
            valor.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UiSupport.BORDA),
                    BorderFactory.createEmptyBorder(9, 10, 9, 10)));
            grupo.add(valor, BorderLayout.CENTER);
            return grupo;
        }

        private Map<String, Object> obterAlteracoes() {
            String placaInformada = placa.getText().trim();
            String tipoInformado = texto(tipo.getSelectedItem()).toLowerCase();
            String marcaInformada = marca.getText().trim();
            String modeloInformado = modelo.getText().trim();
            String statusInformado = texto(status.getSelectedItem()).toLowerCase();
            int anoInformado = Integer.parseInt(ano.getText().trim());
            BigDecimal valorInformado = new BigDecimal(valorDiario.getText().trim().replace(",", "."));

            if (placaInformada.isEmpty() || marcaInformada.isEmpty() || modeloInformado.isEmpty()) {
                throw new IllegalArgumentException("Placa, marca e modelo sao obrigatorios.");
            }
            if (anoInformado <= 0) {
                throw new IllegalArgumentException("O ano deve ser maior que zero.");
            }
            if (valorInformado.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("O valor diario nao pode ser negativo.");
            }

            Map<String, Object> alteracoes = new HashMap<>();
            alteracoes.put("placa", placaInformada);
            alteracoes.put("tipo_veiculo", tipoInformado);
            alteracoes.put("marca", marcaInformada);
            alteracoes.put("modelo", modeloInformado);
            alteracoes.put("ano", anoInformado);
            alteracoes.put("statusVeiculo", statusInformado);
            alteracoes.put("valor_diario", valorInformado);
            return alteracoes;
        }

        private void setEdicaoAtiva(boolean ativa) {
            camposEditaveis.forEach(campo -> campo.setEnabled(ativa));
        }

        private String texto(Object valor) {
            return valor == null ? "" : valor.toString();
        }
    }
}
