package ui;


import javax.swing.SwingUtilities;

// iniciar aplicacao
public class GameMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameFrame gf = new GameFrame();
            gf.setVisible(true); // <-- mostra a janela principal
        });
    }
}