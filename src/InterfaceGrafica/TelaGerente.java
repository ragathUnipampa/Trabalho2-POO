package InterfaceGrafica;

import controller.SistemaController;
import exception.LocacaoException;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class TelaGerente extends JFrame {
    private final SistemaController controller = new SistemaController();

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
        veiculos.addActionListener(event -> new TelaVeiculos(this).setVisible(true));
        cadastrar.addActionListener(event -> cadastrarVeiculo(cadastrar));
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

    private void cadastrarVeiculo(JButton botaoCadastrar) {
        JTextField placa = new JTextField(); JTextField marca = new JTextField();
        JTextField modelo = new JTextField();
        JTextField ano = new JTextField(); JComboBox<String> tipo = new JComboBox<>(new String[]{"carro", "moto"});
        JTextField valor = new JTextField();
        JPanel campos = new JPanel(new GridLayout(0, 2, 12, 12));
        campos.setBackground(UiSupport.SUPERFICIE);
        campos.add(UiSupport.criarCampo("Placa", placa));
        campos.add(UiSupport.criarCampo("Tipo", tipo));
        campos.add(UiSupport.criarCampo("Marca", marca));
        campos.add(UiSupport.criarCampo("Modelo", modelo));
        campos.add(UiSupport.criarCampo("Ano", ano));
        campos.add(UiSupport.criarCampo("Valor diario", valor));
        if (javax.swing.JOptionPane.showConfirmDialog(this, campos, "Cadastrar veiculo", javax.swing.JOptionPane.OK_CANCEL_OPTION)
                != javax.swing.JOptionPane.OK_OPTION) return;
        try {
            if (placa.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("A placa é obrigatória.");
            }

            if (marca.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("A marca é obrigatória.");
            }
            model.Veiculo veiculo = new model.Veiculo();
            veiculo.setPlaca(placa.getText().trim());
            veiculo.setTipoVeiculo((String) tipo.getSelectedItem());
            veiculo.setMarca(marca.getText().trim());
            veiculo.setModelo(modelo.getText().trim());
            veiculo.setAno(Integer.valueOf(ano.getText().trim()));
            veiculo.setStatusVeiculo("disponivel");
            veiculo.setValorDiario(new java.math.BigDecimal(valor.getText().trim()));
            UiSupport.executarEmSegundoPlano(
                    botaoCadastrar,
                    "Cadastrando...",
                    () -> {
                        controller.cadastrarVeiculo(veiculo);
                        return null;
                    },
                    resultado -> javax.swing.JOptionPane.showMessageDialog(
                            this, "Veiculo cadastrado com sucesso."),
                    erro -> {
                        if (erro instanceof LocacaoException) {
                            UiSupport.aviso(this, erro.getMessage());
                        } else {
                            UiSupport.erro(this, "Nao foi possivel cadastrar o veiculo.", erro);
                        }
                    });
        } catch (RuntimeException erro) {
            UiSupport.aviso(this, "Preencha os dados do veiculo corretamente.");
        }
    }
}