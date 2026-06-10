
import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

    private JTextField campUsuario;
    private JPasswordField campSenha;

    public Login() {
        setTitle("Login - Global Materiais");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(350, 220);
        setLocationRelativeTo(null);

        JLabel lblTitulo  = new JLabel("Tela de Login", SwingConstants.CENTER);
        JLabel lblUsuario = new JLabel("Usuário:");
        JLabel lblSenha   = new JLabel("Senha:");

        campUsuario = new JTextField();
        campSenha   = new JPasswordField();

        JButton btnEntrar = new JButton("Entrar");
        JButton btnLimpar = new JButton("Limpar");

        btnLimpar.addActionListener(e -> {
            campUsuario.setText("");
            campSenha.setText("");
        });

        btnEntrar.addActionListener(e -> fazerLogin());

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        formPanel.add(lblUsuario);
        formPanel.add(campUsuario);
        formPanel.add(lblSenha);
        formPanel.add(campSenha);

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnLimpar);
        btnPanel.add(btnEntrar);

        setLayout(new BorderLayout(0, 10));
        add(lblTitulo, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void fazerLogin() {
        String usuario = campUsuario.getText().trim();
        String senha   = new String(campSenha.getPassword()).trim();

        if (usuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha usuário e senha.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        new Menu().setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}