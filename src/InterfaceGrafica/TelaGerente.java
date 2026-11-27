package InterfaceGrafica;

import controller.SistemaController;
import exception.LocacaoException;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Painel administrativo destinado aos gerentes da locadora.
 * Permite consultar os clientes cadastrados, visualizar e editar a frota de veículos,
 * além de registrar novos veículos no sistema.
 */
public class TelaGerente extends JFrame {
    private final SistemaController controller = new SistemaController();

    /**
     * Constrói e exibe a janela do painel de gestão para o gerente autenticado.
     *
     * @param gerente O objeto {@link model.Gerente} correspondente ao usuário logado.
     */
    public TelaGerente(model.Gerente gerente) {
        setTitle("Autobots");
        setSize(850, 500);
        setMinimumSize(new Dimension(680, 420));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        construirTela(gerente);
    }

    private void construirTela(model.Gerente gerente) {
        JPanel conteudo = new JPanel(new BorderLayout(0, 18));
        conteudo.setOpaque(false);
        JPanel resumo = UiSupport.criarCartao(new BorderLayout(0, 6));
        JLabel saudacao = new JLabel("Ola, " + gerente.getEmail());
        saudacao.setFont(new Font("SansSerif", Font.BOLD, 18));
        JLabel contexto = new JLabel("Gerencie clientes, veiculos e a operacao da locadora.");
        contexto.setForeground(UiSupport.TEXTO_SECUNDARIO);
        resumo.add(saudacao, BorderLayout.NORTH);
        resumo.add(contexto, BorderLayout.CENTER);

        JPanel menu = new JPanel(new GridLayout(2, 2, 10, 10));
        menu.setOpaque(false);
        JButton clientes = UiSupport.botaoSecundario("Clientes");
        JButton veiculos = UiSupport.botaoSecundario("Ver frota");
        JButton cadastrar = UiSupport.botaoPrimario("Cadastrar veiculo");
        JButton sair = UiSupport.botaoSecundario("Sair");

        clientes.addActionListener(event -> mostrarClientes(clientes));
        veiculos.addActionListener(event -> new TelaVeiculos(this, true).setVisible(true));
        cadastrar.addActionListener(event -> cadastrarVeiculo());
        sair.addActionListener(event -> { dispose(); new TelaPrincipal().setVisible(true); });
        menu.add(clientes);
        menu.add(veiculos);
        menu.add(cadastrar);
        menu.add(sair);
        conteudo.add(resumo, BorderLayout.NORTH);
        conteudo.add(menu, BorderLayout.CENTER);
        setContentPane(UiSupport.criarPagina(
                "Painel de gestao.",
                conteudo));
    }

    private void mostrarClientes(JButton botaoClientes) {
        UiSupport.executarEmSegundoPlano(
                botaoClientes,
                "Carregando...",
                controller::buscarClientes,
                clientes -> {
                    DefaultTableModel modelo = new DefaultTableModel(
                            new Object[]{"Nome", "CNH", "Categoria", "Telefone"}, 0);
                    for (model.Cliente cliente : clientes) {
                        modelo.addRow(new Object[]{
                                cliente.getNome(),
                                cliente.getCnh(),
                                cliente.getCategoriaCnh(),
                                cliente.getTelefone()
                        });
                    }
                    JPanel painel = new JPanel(new BorderLayout());
                    painel.setBackground(UiSupport.SUPERFICIE);
                    painel.add(UiSupport.criarScrollTabela(new JTable(modelo)), BorderLayout.CENTER);
                    painel.setPreferredSize(new Dimension(680, 320));
                    javax.swing.JOptionPane.showMessageDialog(
                            this, painel, "Clientes cadastrados", javax.swing.JOptionPane.PLAIN_MESSAGE);
                },
                erro -> UiSupport.erro(this, "Nao foi possivel consultar os clientes.", erro));
    }

    private void cadastrarVeiculo() {
        JTextField placa = new JTextField();
        JTextField marca = new JTextField();
        JTextField modelo = new JTextField();
        JTextField ano = new JTextField();
        JComboBox<String> tipo = new JComboBox<>(new String[]{"carro", "moto"});
        JTextField valor = new JTextField();
        JPanel campos = new JPanel(new GridLayout(0, 2, 14, 14));
        campos.setOpaque(false);
        campos.add(UiSupport.criarCampo("Placa", placa));
        campos.add(UiSupport.criarCampo("Tipo", tipo));
        campos.add(UiSupport.criarCampo("Marca", marca));
        campos.add(UiSupport.criarCampo("Modelo", modelo));
        campos.add(UiSupport.criarCampo("Ano", ano));
        campos.add(UiSupport.criarCampo("Valor diario", valor));

        JDialog dialogo = new JDialog(this, "Autobots", true);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.setSize(650, 540);
        dialogo.setMinimumSize(new Dimension(560, 500));

        JPanel cartao = UiSupport.criarCartao(new BorderLayout(0, 20));
        cartao.add(campos, BorderLayout.CENTER);

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        acoes.setOpaque(false);
        JButton cancelar = UiSupport.botaoSecundario("Cancelar");
        JButton salvar = UiSupport.botaoPrimario("Cadastrar veiculo");
        cancelar.addActionListener(event -> dialogo.dispose());
        salvar.addActionListener(event -> {
            try {
                if (placa.getText().trim().isEmpty() || marca.getText().trim().isEmpty()) {
                    UiSupport.aviso(dialogo, "Placa e marca sao obrigatorias.");
                    return;
                }

                model.Veiculo veiculo = new model.Veiculo();
                veiculo.setPlaca(placa.getText().trim());
                veiculo.setTipoVeiculo((String) tipo.getSelectedItem());
                veiculo.setMarca(marca.getText().trim());
                veiculo.setModelo(modelo.getText().trim());
                veiculo.setAno(Integer.valueOf(ano.getText().trim()));
                veiculo.setStatusVeiculo("disponivel");
                veiculo.setValorDiario(new java.math.BigDecimal(valor.getText().trim().replace(",", ".")));

                UiSupport.executarEmSegundoPlano(
                        salvar,
                        "Cadastrando...",
                        () -> {
                            controller.cadastrarVeiculo(veiculo);
                            return null;
                        },
                        resultado -> {
                            dialogo.dispose();
                            javax.swing.JOptionPane.showMessageDialog(
                                    this, "Veiculo cadastrado com sucesso.");
                        },
                        erro -> {
                            if (erro instanceof LocacaoException) {
                                UiSupport.aviso(dialogo, erro.getMessage());
                            } else {
                                UiSupport.erro(dialogo, "Nao foi possivel cadastrar o veiculo.", erro);
                            }
                        },
                        placa, tipo, marca, modelo, ano, valor, cancelar);
            } catch (NumberFormatException erro) {
                UiSupport.aviso(dialogo, "Informe um ano e um valor diario validos.");
            } catch (IllegalArgumentException erro) {
                UiSupport.aviso(dialogo, erro.getMessage());
            }
        });
        acoes.add(cancelar);
        acoes.add(salvar);
        cartao.add(acoes, BorderLayout.SOUTH);

        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.setOpaque(false);
        conteudo.add(cartao, BorderLayout.NORTH);
        dialogo.setContentPane(UiSupport.criarPagina("Cadastrar novo Veiculo", conteudo));
        dialogo.getRootPane().setDefaultButton(salvar);
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }
}