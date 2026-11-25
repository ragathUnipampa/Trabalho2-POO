package InterfaceGrafica;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TelaCliente extends JFrame {
    private final model.Cliente cliente;
    public TelaCliente(model.Cliente cliente) {
        this.cliente = cliente;
        setTitle("Autobots");
        setSize(520, 360);
        setMinimumSize(new Dimension(500, 340));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        construirTela();
    }
    private void construirTela() {
        JPanel conteudo = new JPanel(new BorderLayout(0, 18));
        conteudo.setOpaque(false);
        JPanel boasVindas = UiSupport.criarCartao(new BorderLayout(0, 6));
        JLabel nome = new JLabel("Ola, " + cliente.getNome());
        nome.setFont(new Font("SansSerif", Font.BOLD, 19));
        JLabel detalhe = new JLabel("Escolha uma opcao para continuar sua jornada.");
        detalhe.setForeground(UiSupport.TEXTO_SECUNDARIO);
        boasVindas.add(nome, BorderLayout.NORTH);
        boasVindas.add(detalhe, BorderLayout.CENTER);

        JPanel menu = new JPanel(new GridLayout(1, 3, 10, 0));
        menu.setOpaque(false);
        JButton veiculos = UiSupport.botaoSecundario("Ver veiculos");
        JButton locacao = UiSupport.botaoPrimario("Alugar veiculo");
        JButton sair = UiSupport.botaoSecundario("Sair");
        veiculos.addActionListener(event -> new TelaVeiculos(this).setVisible(true));
        locacao.addActionListener(event -> new TelaLocacao(this, cliente).setVisible(true));
        sair.addActionListener(event -> { dispose(); new TelaPrincipal().setVisible(true); });
        menu.add(veiculos);
        menu.add(locacao);
        menu.add(sair);
        conteudo.add(boasVindas, BorderLayout.NORTH);
        conteudo.add(menu, BorderLayout.CENTER);
        setContentPane(UiSupport.criarPagina(
                "Sua viagem, do seu jeito.",
                conteudo));
    }
}