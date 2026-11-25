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
    private JButton botaoConfirmar;

    public TelaLocacao(JFrame anterior, model.Cliente cliente) {
        this.cliente = cliente;
        setTitle("Autobots");
        setSize(600, 650);
        setMinimumSize(new Dimension(530, 590));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(anterior);
        construirTela();
    }

    private void construirTela() {
        JPanel cartao = UiSupport.criarCartao(new BorderLayout(0, 18));
        JPanel campos = new JPanel(new GridLayout(0, 1, 0, 13));
        campos.setOpaque(false);
        campos.add(UiSupport.criarCampo("Placa do veiculo", placa));
        campos.add(UiSupport.criarCampo("Data de retirada (AAAA-MM-DD)", dataInicio));
        campos.add(UiSupport.criarCampo("Data de devolucao (AAAA-MM-DD)", dataFim));
        campos.add(UiSupport.criarCampo("Forma de pagamento", pagamento));
        cartao.add(campos, BorderLayout.CENTER);
        botaoConfirmar = UiSupport.botaoPrimario("Confirmar locacao");
        botaoConfirmar.addActionListener(event -> confirmar());
        cartao.add(botaoConfirmar, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(botaoConfirmar);
        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.setOpaque(false);
        conteudo.add(cartao, BorderLayout.NORTH);
        setContentPane(UiSupport.criarPagina(
                "Detalhes da sua viagem.",
                conteudo));
    }

    private pagamento.FormaPagamento criarFormaPagamentoSelecionada() {
        String formaSelecionada = (String) pagamento.getSelectedItem();
        if ("pix".equals(formaSelecionada)) {
            return new pagamento.PagamentoPix();
        }
        if ("cartao".equals(formaSelecionada)) {
            return new pagamento.PagamentoCartao();
        }
        return new pagamento.PagamentoDinheiro();
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

            pagamento.FormaPagamento formaPagamento = criarFormaPagamentoSelecionada();
            int diasAlugados = Math.toIntExact(dias);
            UiSupport.executarEmSegundoPlano(
                    botaoConfirmar,
                    "Registrando...",
                    () -> {
                        model.Veiculo veiculo = controller.buscarVeiculo(placaInformada);
                        model.Locacao locacao = new model.Locacao();
                        locacao.setClienteId(cliente.getId());
                        locacao.setVeiculoPlaca(placaInformada);
                        locacao.setDataInicio(inicio.toString());
                        locacao.setDataFim(fim.toString());
                        locacao.setDiasAlugados(diasAlugados);
                        locacao.setValorTotal(veiculo.calcularValorLocacao(diasAlugados));
                        return controller.realizarLocacao(cliente, veiculo, locacao, formaPagamento);
                    },
                    locacaoRealizada -> {
                        JOptionPane.showMessageDialog(
                                this,
                                "Locacao registrada com sucesso.\nDias alugados: " + locacaoRealizada.getDiasAlugados()
                                        + "\nValor total: R$ " + locacaoRealizada.getValorTotal());
                        dispose();
                    },
                    erro -> {
                        if (erro instanceof exception.LocacaoException) {
                            UiSupport.aviso(this, erro.getMessage());
                        } else {
                            UiSupport.erro(this, "Nao foi possivel registrar a locacao.", erro);
                        }
                    },
                    placa, dataInicio, dataFim, pagamento);
        } catch (RuntimeException erro) {
            UiSupport.erro(this, "Nao foi possivel registrar a locacao.", erro);
        }
    }
}