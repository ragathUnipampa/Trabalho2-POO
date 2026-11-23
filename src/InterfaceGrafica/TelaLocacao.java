package InterfaceGrafica;

import controller.SistemaController;

import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.swing.*;

public class TelaLocacao extends JFrame {
    private final SistemaController controller = new SistemaController();
    private final model.Cliente cliente;
    private final JTextField placa = new JTextField();
    private final JTextField dataInicio = new JTextField();
    private final JTextField dataFim = new JTextField();
    private final JComboBox<String> pagamento = new JComboBox<>(new String[]{"dinheiro", "cartao", "pix"});

    public TelaLocacao(JFrame anterior, model.Cliente cliente) {
        this.cliente = cliente;
        setTitle("Nova locacao");
        setSize(470, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(anterior);
        construirTela();
    }

    private void construirTela() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));
        JPanel campos = new JPanel(new GridLayout(4, 2, 8, 8));
        campos.add(new JLabel("Placa:"));
        campos.add(placa);
        campos.add(new JLabel("Data inicio (AAAA-MM-DD):"));
        campos.add(dataInicio);
        campos.add(new JLabel("Data fim (AAAA-MM-DD):"));
        campos.add(dataFim);
        campos.add(new JLabel("Forma de pagamento:"));
        campos.add(pagamento);

        painel.add(campos, BorderLayout.CENTER);
        JButton confirmar = new JButton("Confirmar locacao");
        confirmar.addActionListener(event -> confirmar());
        painel.add(confirmar, BorderLayout.SOUTH);
        setContentPane(painel);
    }

    private void confirmar() {
        try {
            String placaInformada = placa.getText().trim();
            LocalDate inicio = LocalDate.parse(dataInicio.getText().trim());
            LocalDate fim = LocalDate.parse(dataFim.getText().trim());

            long dias = ChronoUnit.DAYS.between(inicio, fim);
            if (dias <= 0) {
                UiSupport.aviso(this, "A data final deve ser posterior a data inicial.");
                return;
            }

            model.Veiculo veiculo = controller.buscarVeiculo(placaInformada);

            model.Locacao locacao = new model.Locacao();
            locacao.setClienteId(cliente.getId());
            locacao.setVeiculoPlaca(placaInformada);
            locacao.setDataInicio(inicio.toString());
            locacao.setDataFim(fim.toString());
            locacao.setDiasAlugados(Math.toIntExact(dias));
            locacao.setValorTotal(veiculo.calcularValorLocacao(Math.toIntExact(dias)));

            String formaSelecionada = (String) pagamento.getSelectedItem();
            pagamento.FormaPagamento formaPagamento;
            if ("pix".equals(formaSelecionada)) {
                formaPagamento = new pagamento.PagamentoPix();
            } else if ("cartao".equals(formaSelecionada)) {
                formaPagamento = new pagamento.PagamentoCartao();
            } else {
                formaPagamento = new pagamento.PagamentoDinheiro();
            }

            model.Locacao locacaoRealizada = controller.realizarLocacao(cliente, veiculo, locacao, formaPagamento);

            JOptionPane.showMessageDialog(
                    this,
                    "Locacao registrada com sucesso.\nDias alugados: " + locacaoRealizada.getDiasAlugados()
                            + "\nValor total: R$ " + locacaoRealizada.getValorTotal()
            );
            dispose();
        } catch (exception.LocacaoException erro) {
            UiSupport.aviso(this, erro.getMessage());
        } catch (RuntimeException erro) {
            UiSupport.erro(this, "Nao foi possivel registrar a locacao.", erro);
        }
    }
}