
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class InicialPage extends JFrame {

    private DefaultTableModel modelo;

    public InicialPage() {
        setTitle("Listagem de Produtos - Global Materiais");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        String[] colunas = {"ID", "Produto", "Quantidade", "Preço (R$)"};
        modelo = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        carregarDadosMock();

        JTable tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setLayout(new BorderLayout());
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    private void carregarDadosMock() {
        modelo.addRow(new Object[]{1, "Cimento", 50, "35,00"});
        modelo.addRow(new Object[]{2, "Areia", 100, "12,00"});
        modelo.addRow(new Object[]{3, "Tijolo", 500, "0,80"});
        modelo.addRow(new Object[]{4, "Tinta Branca", 20, "45,00"});
        modelo.addRow(new Object[]{5, "Vergalhão", 30, "28,00"});
    }
}