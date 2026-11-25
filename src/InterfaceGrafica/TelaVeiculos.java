package InterfaceGrafica;

import controller.SistemaController;
import exception.LocacaoException;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Tela de consulta e edição tabular da frota de veículos da locadora.
 * Permite filtrar veículos por categoria (carro ou moto) e persistir
 * alterações feitas diretamente nas células da tabela.
 */
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

    /**
     * Constrói a janela de gerenciamento de veículos e carrega os registros iniciais na tabela.
     *
     * @param anterior A janela pai que abriu esta tela, utilizada para centralização.
     */
    public TelaVeiculos(JFrame anterior) {
        setTitle("Veiculos");
        setSize(760, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(anterior);

        JPanel painel = new JPanel(new BorderLayout(8, 8));
        painel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton buscar = new JButton("Buscar");
        JButton salvar = new JButton("Salvar alteracoes");

        buscar.addActionListener(event -> atualizarTabela());
        salvar.addActionListener(event -> salvarAlteracoes());

        topo.add(filtro);
        topo.add(buscar);
        topo.add(salvar);

        painel.add(topo, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        setContentPane(painel);
        atualizarTabela();
    }

    private void atualizarTabela() {
        modelo.setRowCount(0);
        String tipo = "todos".equals(filtro.getSelectedItem()) ? null : (String) filtro.getSelectedItem();

        try {
            veiculosAtuais = controller.buscarVeiculos(tipo, null);
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
        } catch (LocacaoException erro) {
            UiSupport.aviso(this, erro.getMessage());
        } catch (RuntimeException erro) {
            UiSupport.erro(this, "Nao foi possivel consultar os veiculos.", erro);
        }
    }

    private void salvarAlteracoes() {
        if (tabela.isEditing()) {
            tabela.getCellEditor().stopCellEditing();
        }

        try {
            List<Map<String, Object>> alteracoesPorLinha = new ArrayList<>();

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

            for (int linha = 0; linha < veiculosAtuais.size(); linha++) {
                controller.editarVeiculo(
                        veiculosAtuais.get(linha).getId(),
                        alteracoesPorLinha.get(linha)
                );
            }

            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Alteracoes salvas com sucesso.");
        } catch (IllegalArgumentException erro) {
            UiSupport.aviso(this, erro.getMessage());
        } catch (LocacaoException erro) {
            UiSupport.aviso(this, erro.getMessage());
        } catch (RuntimeException erro) {
            UiSupport.erro(this, "Nao foi possivel salvar as alteracoes.", erro);
        }
    }

    private String valorDaCelula(int linha, int coluna) {
        Object valor = modelo.getValueAt(linha, coluna);
        return valor == null ? "" : valor.toString().trim();
    }
}
