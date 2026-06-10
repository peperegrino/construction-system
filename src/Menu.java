
import javax.swing.*;
import java.awt.*;

public class Menu extends JFrame {

    public Menu() {
        setTitle("Menu - Global Materiais");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();

        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> {
            new Inicial().setVisible(true);
            dispose();
        });
        menuArquivo.add(itemSair);

        JMenu menuEstoque = new JMenu("Estoque");
        JMenuItem itemListagem = new JMenuItem("Listagem de Produtos");
        itemListagem.addActionListener(e -> new InicialPage().setVisible(true));
        menuEstoque.add(itemListagem);

        menuBar.add(menuArquivo);
        menuBar.add(menuEstoque);
        setJMenuBar(menuBar);

        JLabel lblBemVindo = new JLabel("Selecione uma opção no menu acima.", SwingConstants.CENTER);
        lblBemVindo.setFont(new Font("Arial", Font.PLAIN, 14));

        add(lblBemVindo, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menu().setVisible(true));
    }
}