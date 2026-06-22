import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class Login extends JFrame {

    private JTextField campUsuario;
    private JPasswordField campSenha;
    private JButton btnEntrar;
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public Login() {
        setTitle("Login - Global Materiais");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(350, 220);
        setLocationRelativeTo(null);

        JLabel lblTitulo  = new JLabel("Tela de Login / Cadastro", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel lblUsuario = new JLabel("Usuário:");
        JLabel lblSenha   = new JLabel("Senha:");

        campUsuario = new JTextField();
        campSenha   = new JPasswordField();

        btnEntrar = new JButton("Entrar / Cadastrar");
        JButton btnLimpar = new JButton("Limpar");

        btnLimpar.addActionListener(e -> {
            campUsuario.setText("");
            campSenha.setText("");
        });

        btnEntrar.addActionListener(e -> entrar());

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

    private void entrar() {
        String usuario = campUsuario.getText().trim();
        String senha = new String(campSenha.getPassword()).trim();

        if (usuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnEntrar.setEnabled(false);
        btnEntrar.setText("Conectando...");


        new Thread(() -> {
            try {
                String jsonBody = String.format("{\"usuario\":\"%s\",\"senha\":\"%s\"}", usuario, senha);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/api/usuarios"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                        .timeout(Duration.ofSeconds(5))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                SwingUtilities.invokeLater(() -> {
                    if (response.statusCode() == 200 || response.statusCode() == 201) {
                         new Apresentacao().setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Erro ao criar usuário na API. Status: " + response.statusCode(), "Erro", JOptionPane.ERROR_MESSAGE);
                        resetBotao();
                    }
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "API offline. Entrando em modo de demonstração local.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    new Apresentacao().setVisible(true);
                    dispose();
                });
            }
        }).start();
    }

    private void resetBotao() {
        btnEntrar.setEnabled(true);
        btnEntrar.setText("Entrar / Cadastrar");
    }
}