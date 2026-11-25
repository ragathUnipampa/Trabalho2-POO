import InterfaceGrafica.TelaPrincipal;

public class Main {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            InterfaceGrafica.UiSupport.aplicarTema();
            TelaPrincipal telaPrincipal = new TelaPrincipal();
            telaPrincipal.setVisible(true);
        });
    }
}
