
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InicialPage extends JFrame {

    private static final Color BG          = new Color(18,  18,  18);
    private static final Color SURFACE     = new Color(28,  28,  28);
    private static final Color CARD        = new Color(36,  36,  36);
    private static final Color CARD_HOVER  = new Color(46,  46,  46);
    private static final Color ACCENT      = new Color(255, 165,   0); // Laranja
    private static final Color TEXT_PRI    = new Color(240, 240, 240);
    private static final Color TEXT_SEC    = new Color(160, 160, 160);
    private static final Color LOW_STOCK   = new Color(220,  53,  69);
    private static final Color OK_STOCK    = new Color( 40, 167,  69);

    private static final int CARD_W  = 220;
    private static final int CARD_H  = 310;
    private static final int IMG_H   = 140;
    private static final int GAP     = 18;

    private List<ProdutoCard> todosProdutos = new ArrayList<>();
    private JPanel gridPanel;
    private JTextField campoBusca;
    private JLabel labelContagem;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public InicialPage() {
        inicializarUI();
        carregarProdutosDaApi();
    }

    private void inicializarUI() {
        setTitle("Global Materiais — Produtos");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1060, 720);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());

        add(criarHeader(), BorderLayout.NORTH);
        add(criarAreaProdutos(), BorderLayout.CENTER);
        add(criarFooter(), BorderLayout.SOUTH);
    }

    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout(20, 0));
        header.setBackground(SURFACE);
        header.setBorder(new EmptyBorder(14, 24, 14, 24));

        JLabel titulo = new JLabel("🏗  GLOBAL MATERIAIS");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(ACCENT);

        JPanel buscaPanel = new JPanel(new BorderLayout(8, 0));
        buscaPanel.setBackground(SURFACE);

        campoBusca = new JTextField();
        campoBusca.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoBusca.setBackground(CARD);
        campoBusca.setForeground(TEXT_PRI);
        campoBusca.setCaretColor(ACCENT);
        campoBusca.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70)),
                new EmptyBorder(6, 10, 6, 10)));
        campoBusca.putClientProperty("JTextField.placeholderText", "Pesquisar produto...");
        campoBusca.setPreferredSize(new Dimension(320, 36));

        campoBusca.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { filtrar(); }
            public void removeUpdate(DocumentEvent e)  { filtrar(); }
            public void changedUpdate(DocumentEvent e) { filtrar(); }
        });

        JButton btnLimpar = new JButton("✕");
        btnLimpar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnLimpar.setBackground(CARD);
        btnLimpar.setForeground(TEXT_SEC);
        btnLimpar.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        btnLimpar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLimpar.setFocusPainted(false);
        btnLimpar.addActionListener(e -> campoBusca.setText(""));

        buscaPanel.add(campoBusca, BorderLayout.CENTER);
        buscaPanel.add(btnLimpar, BorderLayout.EAST);

        labelContagem = new JLabel("0 produtos");
        labelContagem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelContagem.setForeground(TEXT_SEC);

        header.add(titulo, BorderLayout.WEST);
        header.add(buscaPanel, BorderLayout.CENTER);
        header.add(labelContagem, BorderLayout.EAST);
        return header;
    }

    private JScrollPane criarAreaProdutos() {
        gridPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, GAP, GAP));
        gridPanel.setBackground(BG);
        gridPanel.setBorder(new EmptyBorder(GAP, GAP, GAP, GAP));

        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.setBackground(BG);
        scroll.getViewport().setBackground(BG);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    private JPanel criarFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 8));
        footer.setBackground(SURFACE);

        JButton btnAtualizar = new JButton("⟳  Atualizar");
        estilizarBotao(btnAtualizar, ACCENT, Color.BLACK);
        btnAtualizar.addActionListener(e -> carregarProdutosDaApi());

        footer.add(btnAtualizar);
        return footer;
    }

    private void carregarProdutosDaApi() {
        labelContagem.setText("Conectando à API...");
        gridPanel.removeAll();
        gridPanel.revalidate();
        gridPanel.repaint();

        new Thread(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/api/produtos"))
                        .GET()
                        .timeout(Duration.ofSeconds(5))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    List<ProdutoCard> lista = parseProdutosSeguro(response.body());
                    SwingUtilities.invokeLater(() -> exibirCards(lista));
                } else {
                    SwingUtilities.invokeLater(() -> mostrarErro("API retornou código de status: " + response.statusCode()));
                }

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> mostrarErro("Falha de comunicação: Verifique se o Backend está rodando."));
            }
        }).start();
    }

    private List<ProdutoCard> parseProdutosSeguro(String jsonBody) {
        List<ProdutoCard> lista = new ArrayList<>();
        if (jsonBody == null || jsonBody.trim().isEmpty()) return lista;

        Pattern pattern = Pattern.compile("\\{[^\\}]+\\}");
        Matcher matcher = pattern.matcher(jsonBody);

        while (matcher.find()) {
            String objetoJson = matcher.group();
            try {
                ProdutoCard p = new ProdutoCard();
                p.codigo = extrairCampoTexto(objetoJson, "codigo");
                p.nome = extrairCampoTexto(objetoJson, "nome");
                p.descricao = extrairCampoTexto(objetoJson, "descricao");
                p.imagemUrl = extrairCampoTexto(objetoJson, "imagemUrl");
                p.precoVenda = extrairCampoNumerico(objetoJson, "precoVenda");
                p.quantidadeEstoque = (int) extrairCampoNumerico(objetoJson, "quantidadeEstoque");
                p.estoqueMinimo = (int) extrairCampoNumerico(objetoJson, "estoqueMinimo");

                p.estoqueBaixo = objetoJson.contains("\"estoqueBaixo\":true") || (p.quantidadeEstoque <= p.estoqueMinimo);

                if (p.nome != null && !p.nome.isEmpty()) {
                    lista.add(p);
                }
            } catch (Exception ignored) {}
        }
        return lista;
    }

    private String extrairCampoTexto(String json, String campo) {
        Pattern pattern = Pattern.compile("\"" + campo + "\":\\s*\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(json); // CORRIGIDO: Removido o .pattern() incorreto
        return matcher.find() ? matcher.group(1) : "";
    }

    private double extrairCampoNumerico(String json, String campo) {
        Pattern pattern = Pattern.compile("\"" + campo + "\":\\s*([0-9\\.]+)");
        Matcher matcher = pattern.matcher(json); // CORRIGIDO: Removido o .pattern() incorreto
        return matcher.find() ? Double.parseDouble(matcher.group(1)) : 0.0;
    }

    private void exibirCards(List<ProdutoCard> cards) {
        todosProdutos = cards;
        gridPanel.removeAll();

        if (cards.isEmpty()) {
            JLabel vazio = new JLabel("Nenhum produto cadastrado na API.");
            vazio.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            vazio.setForeground(TEXT_SEC);
            gridPanel.add(vazio);
        } else {
            for (ProdutoCard p : cards) {
                gridPanel.add(criarCardComponent(p));
            }
        }

        labelContagem.setText(cards.size() + " produto" + (cards.size() != 1 ? "s" : ""));
        gridPanel.revalidate();
        gridPanel.repaint();

        for (ProdutoCard p : cards) {
            if (p.imagemUrl != null && !p.imagemUrl.isBlank() && p.painelImagem != null) {
                carregarImagemAsync(p);
            }
        }
    }

    private JPanel criarCardComponent(ProdutoCard p) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(CARD_W, CARD_H));
        card.setMaximumSize(new Dimension(CARD_W, CARD_H));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(55, 55, 55)),
                new EmptyBorder(0, 0, 10, 0)));

        JPanel imgPanel = new JPanel(new BorderLayout());
        imgPanel.setPreferredSize(new Dimension(CARD_W, IMG_H));
        imgPanel.setMaximumSize(new Dimension(CARD_W, IMG_H));
        imgPanel.setBackground(new Color(50, 50, 50));

        JLabel lblImagem = new JLabel("⏳", SwingConstants.CENTER);
        lblImagem.setForeground(TEXT_SEC);
        lblImagem.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        imgPanel.add(lblImagem, BorderLayout.CENTER);
        p.painelImagem = imgPanel;
        p.labelImagem  = lblImagem;
        card.add(imgPanel);

        JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        badgePanel.setBackground(CARD);
        badgePanel.setMaximumSize(new Dimension(CARD_W, 28));

        JLabel badge = new JLabel(p.estoqueBaixo ? "⚠ Estoque baixo" : "✔ Disponível");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setForeground(p.estoqueBaixo ? LOW_STOCK : OK_STOCK);
        badgePanel.add(badge);

        JLabel lblCod = new JLabel("Cód: " + p.codigo);
        lblCod.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblCod.setForeground(TEXT_SEC);
        badgePanel.add(lblCod);
        card.add(badgePanel);

        JLabel lblNome = new JLabel("<html><body style='width:" + (CARD_W - 20) + "px'>" + p.nome + "</body></html>");
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblNome.setForeground(TEXT_PRI);
        lblNome.setBorder(new EmptyBorder(2, 10, 2, 10));
        card.add(lblNome);

        JLabel lblDesc = new JLabel("<html><body style='width:" + (CARD_W - 20) + "px'>" + truncar(p.descricao, 80) + "</body></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDesc.setForeground(TEXT_SEC);
        lblDesc.setBorder(new EmptyBorder(2, 10, 4, 10));
        card.add(lblDesc);

        card.add(Box.createVerticalGlue());

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(60, 60, 60));
        sep.setMaximumSize(new Dimension(CARD_W, 1));
        card.add(sep);

        // CORRIGIDO: Utilizando construtor não depreciado para Locale
        Locale localePtBr = new Locale.Builder().setLanguage("pt").setRegion("BR").build();
        NumberFormat nf = NumberFormat.getCurrencyInstance(localePtBr);

        JLabel lblPreco = new JLabel(nf.format(p.precoVenda));
        lblPreco.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPreco.setForeground(ACCENT);
        lblPreco.setBorder(new EmptyBorder(6, 10, 0, 10));
        card.add(lblPreco);

        JLabel lblEst = new JLabel("Estoque: " + p.quantidadeEstoque + " un.");
        lblEst.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblEst.setForeground(p.estoqueBaixo ? LOW_STOCK : TEXT_SEC);
        lblEst.setBorder(new EmptyBorder(2, 10, 0, 10));
        card.add(lblEst);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { card.setBackground(CARD_HOVER); repaintCard(card); }
            public void mouseExited (java.awt.event.MouseEvent e) { card.setBackground(CARD);       repaintCard(card); }
        });

        return card;
    }

    private void repaintCard(JPanel card) {
        for (Component c : card.getComponents()) {
            if (c instanceof JPanel) c.setBackground(card.getBackground());
        }
        card.repaint();
    }

    private void carregarImagemAsync(ProdutoCard p) {
        new Thread(() -> {
            try {
                // CORRIGIDO: Instanciando URL de forma segura a partir de URI (padrão Java moderno)
                URL url = URI.create(p.imagemUrl).toURL();
                BufferedImage img = ImageIO.read(url);
                if (img != null) {
                    Image scaled = img.getScaledInstance(CARD_W, IMG_H, Image.SCALE_SMOOTH);
                    SwingUtilities.invokeLater(() -> {
                        p.labelImagem.setText("");
                        p.labelImagem.setIcon(new ImageIcon(scaled));
                        p.painelImagem.revalidate();
                        p.painelImagem.repaint();
                    });
                }
            } catch (Exception ignored) {
                SwingUtilities.invokeLater(() -> {
                    p.labelImagem.setText("🖼");
                    p.labelImagem.setFont(new Font("Segoe UI", Font.PLAIN, 32));
                });
            }
        }).start();
    }

    private void filtrar() {
        String termo = campoBusca.getText().trim().toLowerCase();
        gridPanel.removeAll();
        List<ProdutoCard> filtrados = todosProdutos.stream()
                .filter(p -> termo.isEmpty()
                        || (p.nome != null && p.nome.toLowerCase().contains(termo))
                        || (p.descricao != null && p.descricao.toLowerCase().contains(termo))
                        || (p.codigo != null && p.codigo.toLowerCase().contains(termo)))
                .toList();

        if (filtrados.isEmpty()) {
            JLabel vazio = new JLabel("Nenhum produto encontrado para \"" + campoBusca.getText() + "\".");
            vazio.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            vazio.setForeground(TEXT_SEC);
            gridPanel.add(vazio);
        } else {
            filtrados.forEach(p -> gridPanel.add(criarCardComponent(p)));
            filtrados.stream().filter(p -> p.imagemUrl != null).forEach(this::carregarImagemAsync);
        }

        labelContagem.setText(filtrados.size() + " produto" + (filtrados.size() != 1 ? "s" : ""));
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void mostrarErro(String msg) {
        gridPanel.removeAll();
        JLabel err = new JLabel("<html><center>⚠<br>" + msg + "</center></html>");
        err.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        err.setForeground(LOW_STOCK);
        gridPanel.add(err);
        gridPanel.revalidate();
        gridPanel.repaint();
        labelContagem.setText("Erro");
    }

    private static void estilizarBotao(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(new EmptyBorder(8, 18, 8, 18));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private static String truncar(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }

    private static class ProdutoCard {
        long   id;
        String codigo, nome, descricao, imagemUrl;
        double precoVenda;
        int    quantidadeEstoque, estoqueMinimo;
        boolean estoqueBaixo;
        JPanel painelImagem;
        JLabel labelImagem;
    }

    static class WrapLayout extends FlowLayout {
        WrapLayout(int align, int hgap, int vgap) { super(align, hgap, vgap); }

        @Override
        public Dimension preferredLayoutSize(Container target) { return layoutSize(target, true); }

        @Override
        public Dimension minimumLayoutSize(Container target) {
            Dimension minimum = layoutSize(target, false);
            minimum.width -= (getHgap() + 1);
            return minimum;
        }

        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int targetWidth = target.getSize().width;
                if (targetWidth == 0) targetWidth = Integer.MAX_VALUE;

                int hgap = getHgap(), vgap = getVgap();
                Insets insets = target.getInsets();
                int maxWidth = targetWidth - (insets.left + insets.right + hgap * 2);

                Dimension dim = new Dimension(0, 0);
                int rowWidth = 0, rowHeight = 0;

                int nmembers = target.getComponentCount();
                for (int i = 0; i < nmembers; i++) {
                    Component m = target.getComponent(i);
                    if (m.isVisible()) {
                        Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();
                        if (rowWidth + d.width > maxWidth) {
                            addRow(dim, rowWidth, rowHeight);
                            rowWidth = 0; rowHeight = 0;
                        }
                        if (rowWidth != 0) rowWidth += hgap;
                        rowWidth += d.width;
                        rowHeight = Math.max(rowHeight, d.height);
                    }
                }
                addRow(dim, rowWidth, rowHeight);
                dim.width  += insets.left + insets.right + hgap * 2;
                dim.height += insets.top  + insets.bottom + vgap * 2;
                return dim;
            }
        }

        private void addRow(Dimension dim, int rowWidth, int rowHeight) {
            dim.width  = Math.max(dim.width, rowWidth);
            if (dim.height > 0) dim.height += getVgap();
            dim.height += rowHeight;
        }
    }
}