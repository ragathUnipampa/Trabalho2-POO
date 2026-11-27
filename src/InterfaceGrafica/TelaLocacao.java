package InterfaceGrafica;

import controller.SistemaController;

import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Tela responsável pelo registro de uma nova locação de veículo por um cliente.
 * Permite informar a placa do veículo, o período desejado e a forma de pagamento,
 * realizando o cálculo prévio de diárias e delegando a efetivação ao controlador.
 */
public class TelaLocacao extends JFrame {
    private final SistemaController controller = new SistemaController();
    private final model.Cliente cliente;
    private final JTextField placa = new JTextField();
    private final JTextField dataInicio = new JTextField();
    private final JTextField dataFim = new JTextField();
    private final JComboBox<String> pagamento = new JComboBox<>(new String[]{"dinheiro", "cartao", "pix"});
    private JButton botaoConfirmar;

    /**
     * Constrói o formulário de nova locação vinculado a um cliente e centralizado
     * em relação à janela anterior.
     *
     * @param anterior A janela pai que invocou esta tela, usada como referência de posicionamento.
     * @param cliente  O cliente autenticado que está realizando a locação.
     */
    public TelaLocacao(JFrame anterior, model.Cliente cliente) {
        this(anterior, cliente, null);
    }

    /**
     * Constrói o formulário de nova locação com possibilidade de pré-preencher
     * e bloquear a placa de um veículo selecionado previamente.
     *
     * @param anterior     A janela pai que invocou esta tela, usada como referência de posicionamento.
     * @param cliente      O cliente autenticado que está realizando a locação.
     * @param placaVeiculo A placa do veículo pré-selecionado, ou {@code null} para digitação livre.
     */
    public TelaLocacao(JFrame anterior, model.Cliente cliente, String placaVeiculo) {
        this.cliente = cliente;
        setTitle("Autobots");
        setSize(600, 650);
        setMinimumSize(new Dimension(530, 590));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(anterior);
        if (placaVeiculo != null && !placaVeiculo.isBlank()) {
            placa.setText(placaVeiculo);
            placa.setEditable(false);
        }
        restringirData(dataInicio);
        restringirData(dataFim);
        construirTela();
    }

    private void restringirData(JTextField campo) {
        campo.setToolTipText("Digite os numeros; os hifens da data sao inseridos automaticamente.");
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            private String digitos(String texto) {
                StringBuilder resultado = new StringBuilder();
                for (int indice = 0; indice < texto.length(); indice++) {
                    char caractere = texto.charAt(indice);
                    if (caractere >= '0' && caractere <= '9') {
                        resultado.append(caractere);
                    } else if (caractere != '-') {
                        return null;
                    }
                }
                return resultado.toString();
            }

            private String formatar(String numeros) {
                StringBuilder resultado = new StringBuilder(numeros);
                if (numeros.length() >= 4) {
                    resultado.insert(4, '-');
                }
                if (numeros.length() >= 6) {
                    resultado.insert(7, '-');
                }
                return resultado.toString();
            }

            private int contarDigitos(String texto) {
                int quantidade = 0;
                for (int indice = 0; indice < texto.length(); indice++) {
                    if (texto.charAt(indice) != '-') {
                        quantidade++;
                    }
                }
                return quantidade;
            }

            private int posicaoFormatada(int quantidadeDigitos) {
                int posicao = quantidadeDigitos;
                if (quantidadeDigitos >= 4) {
                    posicao++;
                }
                if (quantidadeDigitos >= 6) {
                    posicao++;
                }
                return posicao;
            }

            private void atualizar(
                    FilterBypass bypass, int offset, int comprimento, String texto, AttributeSet atributos)
                    throws BadLocationException {
                String inseridos = digitos(texto == null ? "" : texto);
                if (inseridos == null) {
                    return;
                }

                String atual = bypass.getDocument().getText(0, bypass.getDocument().getLength());
                String numerosAtuais = digitos(atual);
                int inicio = contarDigitos(atual.substring(0, offset));
                int fim = contarDigitos(atual.substring(0, offset + comprimento));
                if (inicio == fim && comprimento > 0 && inicio > 0) {
                    inicio--;
                }

                String novosNumeros = numerosAtuais.substring(0, inicio)
                        + inseridos
                        + numerosAtuais.substring(fim);
                if (novosNumeros.length() > 8) {
                    return;
                }

                String textoFormatado = formatar(novosNumeros);
                bypass.replace(0, bypass.getDocument().getLength(), textoFormatado, atributos);
                int caret = posicaoFormatada(inicio + inseridos.length());
                SwingUtilities.invokeLater(() ->
                        campo.setCaretPosition(Math.min(caret, campo.getDocument().getLength())));
            }

            @Override
            public void insertString(
                    FilterBypass bypass, int offset, String texto, AttributeSet atributos)
                    throws BadLocationException {
                replace(bypass, offset, 0, texto, atributos);
            }

            @Override
            public void replace(
                    FilterBypass bypass, int offset, int comprimento, String texto, AttributeSet atributos)
                    throws BadLocationException {
                atualizar(bypass, offset, comprimento, texto, atributos);
            }

            @Override
            public void remove(FilterBypass bypass, int offset, int comprimento)
                    throws BadLocationException {
                atualizar(bypass, offset, comprimento, "", null);
            }
        });
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

            if (inicio.isBefore(LocalDate.now())) {
                UiSupport.aviso(this, "Data invalida.");
                dataInicio.requestFocusInWindow();
                return;
            }

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
        } catch (DateTimeParseException erro) {
            UiSupport.aviso(this, "Data invalida.");
        } catch (RuntimeException erro) {
            UiSupport.erro(this, "Nao foi possivel registrar a locacao.", erro);
        }
    }
}