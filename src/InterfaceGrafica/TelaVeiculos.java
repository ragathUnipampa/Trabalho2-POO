package InterfaceGrafica;

import controller.SistemaController;
import exception.LocacaoException;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TelaVeiculos extends JFrame {
    private static final int COLUNA_PLACA = 0;
    private static final int COLUNA_TIPO = 1;
    private static final int COLUNA_MARCA = 2;
    private static final int COLUNA_MODELO = 3;
    private static final int COLUNA_STATUS = 4;
    private static final int COLUNA_VALOR_DIARIO = 5;


    private final SistemaController controller = new SistemaController();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Placa", "Tipo", "Marca", "Modelo", "Status", "Valor diario"}, 0);
    private final JTable tabela = new JTable(modelo);
    private final JComboBox<String> filtro = new JComboBox<>(new String[]{"todos", "carro", "moto"});
    private List<model.Veiculo> veiculosAtuais = new ArrayList<>();
    private JButton botaoBuscar;
    private JButton botaoSalvar;

    public TelaVeiculos(JFrame anterior, boolean permiteEdicao) {
        setTitle("Autobots");
        setSize(900, 560);
        setMinimumSize(new Dimension(700, 440));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(anterior);
        if (!permiteEdicao) {
            tabela.setDefaultEditor(Object.class, null);
        }

        JPanel conteudo = new JPanel(new BorderLayout(0, 14));
        conteudo.setOpaque(false);

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.setOpaque(false);
        topo.add(new javax.swing.JLabel("Categoria"));
        UiSupport.estilizarCampo(filtro);
        botaoBuscar = UiSupport.botaoSecundario("Atualizar lista");

        botaoBuscar.addActionListener(event -> atualizarTabela());

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
        conteudo.add(UiSupport.criarScrollTabela(tabela), BorderLayout.CENTER);
        setContentPane(UiSupport.criarPagina(
                "Veiculos da locadora.",
                conteudo));
        atualizarTabela();
    }

    private void atualizarTabela() {
        String tipo = "todos".equals(filtro.getSelectedItem()) ? null : (String) filtro.getSelectedItem();
        filtro.setEnabled(false);
        tabela.setEnabled(false);
        if (botaoSalvar != null) {
            botaoSalvar.setEnabled(false);
        }
        UiSupport.executarEmSegundoPlano(
                botaoBuscar,
                "Carregando...",
                () -> controller.buscarVeiculos(tipo, null),
                veiculos -> {
                    filtro.setEnabled(true);
                    tabela.setEnabled(true);
                    exibirVeiculos(veiculos);
                    if (botaoSalvar != null) {
                        botaoSalvar.setEnabled(!veiculosAtuais.isEmpty());
                    }
                },
                erro -> {
                    filtro.setEnabled(true);
                    tabela.setEnabled(true);
                    if (botaoSalvar != null) {
                        botaoSalvar.setEnabled(!veiculosAtuais.isEmpty());
                    }
                    if (erro instanceof LocacaoException) {
                        UiSupport.aviso(this, erro.getMessage());
                    } else {
                        UiSupport.erro(this, "Nao foi possivel consultar os veiculos.", erro);
                    }
                });
    }

    private void exibirVeiculos(List<model.Veiculo> veiculos) {
        veiculosAtuais = veiculos;
        modelo.setRowCount(0);
        for (model.Veiculo veiculo : veiculosAtuais) {
            modelo.addRow(new Object[]{
                    veiculo.getPlaca(),
                    veiculo.getTipoVeiculo(),
                    veiculo.getMarca(),
                    veiculo.getModelo(),
                    veiculo.getStatusVeiculo(),
                    veiculo.getValorDiario()
            });
        }
    }

    private void salvarAlteracoes() {
        if (botaoSalvar == null) {
            return;
        }
        if (tabela.isEditing()) {
            tabela.getCellEditor().stopCellEditing();
        }

        List<Map<String, Object>> alteracoesPorLinha = new ArrayList<>();
        try {
            for (int linha = 0; linha < modelo.getRowCount(); linha++) {
                String placa = valorDaCelula(linha, COLUNA_PLACA);
                String tipo = valorDaCelula(linha, COLUNA_TIPO).toLowerCase();
                String marca = valorDaCelula(linha, COLUNA_MARCA);
                String nomeModelo = valorDaCelula(linha, COLUNA_MODELO);
                String status = valorDaCelula(linha, COLUNA_STATUS).toLowerCase();
                BigDecimal valorDiario = new BigDecimal(valorDaCelula(linha, COLUNA_VALOR_DIARIO).replace(",", "."));

                if (placa.isEmpty()) {
                    throw new IllegalArgumentException("A placa nao pode ficar vazia.");
                }
                if (!"carro".equals(tipo) && !"moto".equals(tipo)) {
                    throw new IllegalArgumentException("O tipo deve ser carro ou moto.");
                }
                if (!"disponivel".equals(status) && !"indisponivel".equals(status)) {
                    throw new IllegalArgumentException("O status deve ser disponivel ou indisponivel.");
                }
                if (valorDiario.compareTo(BigDecimal.ZERO) < 0) {
                    throw new IllegalArgumentException("O valor diario nao pode ser negativo.");
                }

                Map<String, Object> alteracoes = new HashMap<>();
                alteracoes.put("placa", placa);
                alteracoes.put("tipo_veiculo", tipo);
                alteracoes.put("marca", marca);
                alteracoes.put("modelo", nomeModelo);
                alteracoes.put("statusVeiculo", status);
                alteracoes.put("valor_diario", valorDiario);
                alteracoesPorLinha.add(alteracoes);
            }

        } catch (IllegalArgumentException erro) {
            UiSupport.aviso(this, erro.getMessage());
            return;
        }

        tabela.setEnabled(false);
        botaoBuscar.setEnabled(false);
        filtro.setEnabled(false);
        UiSupport.executarEmSegundoPlano(
                botaoSalvar,
                "Salvando...",
                () -> {
                    for (int linha = 0; linha < veiculosAtuais.size(); linha++) {
                        controller.editarVeiculo(veiculosAtuais.get(linha).getId(), alteracoesPorLinha.get(linha));
                    }
                    return null;
                },
                resultado -> {
                    tabela.setEnabled(true);
                    botaoBuscar.setEnabled(true);
                    filtro.setEnabled(true);
                    atualizarTabela();
                    JOptionPane.showMessageDialog(this, "Alteracoes salvas com sucesso.");
                },
                erro -> {
                    tabela.setEnabled(true);
                    botaoBuscar.setEnabled(true);
                    filtro.setEnabled(true);
                    if (erro instanceof LocacaoException) {
                        UiSupport.aviso(this, erro.getMessage());
                    } else {
                        UiSupport.erro(this, "Nao foi possivel salvar as alteracoes.", erro);
                    }
                });
    }

    private String valorDaCelula(int linha, int coluna) {
        Object valor = modelo.getValueAt(linha, coluna);
        return valor == null ? "" : valor.toString().trim();
    }
}
