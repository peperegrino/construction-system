private void carregarImagemAsync(ProdutoCard p) {
    new Thread(() -> {
        try {
            String urlFinal = p.imagemUrl;

            if (urlFinal == null || urlFinal.isBlank() || !urlFinal.startsWith("http")) {
                long idFoto = (p.codigo != null) ? Math.abs(p.codigo.hashCode()) % 1000 : 1;
                urlFinal = "https://picsum.photos/id/" + (idFoto + 10) + "/" + CARD_W + "/" + IMG_H;
            }

            URL url = URI.create(urlFinal).toURL();
            BufferedImage img = ImageIO.read(url);
            if (img != null) {
                Image scaled = img.getScaledInstance(CARD_W, IMG_H, Image.SCALE_SMOOTH);
                SwingUtilities.invokeLater(() -> {
                    p.labelImagem.setText("");
                    p.labelImagem.setIcon(new ImageIcon(scaled));
                    p.painelImagem.revalidate();
                    p.painelImagem.repaint();
                });
            } else {
                throw new Exception("Falha ao renderizar imagem");
            }
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                p.labelImagem.setText("📦");
                p.labelImagem.setFont(new Font("Segoe UI", Font.PLAIN, 36));
            });
        }
    }).start();
}