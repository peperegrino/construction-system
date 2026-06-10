
import javax.swing.*;
import java.awt.*;

public class Inicial extends JFrame {

    public Inicial() {
        setTitle("Global Materiais");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Bem vindo à Global Materiais", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnEntrar);

        setLayout(new BorderLayout());
        add(label, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Inicial().setVisible(true));
    }
}