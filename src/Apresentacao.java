import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class Apresentacao extends JFrame {

    public Apresentacao() {
        setTitle("Bem-vindo - Global Materiais");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(640, 480);
        setLocationRelativeTo(null);
        setResizable(false);

        PainelFundo fundo = new PainelFundo();
        fundo.setLayout(new GridBagLayout());
        setContentPane(fundo);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(8, 0, 8, 0);

        LogoCirculo logo = new LogoCirculo();
        logo.setPreferredSize(new Dimension(110, 110));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        fundo.add(logo, gbc);

        TituloComSombra titulo = new TituloComSombra("GLOBAL MATERIAIS");
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 6, 0);
        fundo.add(titulo, gbc);

        JLabel subtitulo = new JLabel("Gestão inteligente de estoque e materiais");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitulo.setForeground(new Color(255, 255, 255, 200));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 40, 0);
        fundo.add(subtitulo, gbc);

        BotaoArredondado btnContinuar = new BotaoArredondado("ACESSAR O SISTEMA  →");
        btnContinuar.setPreferredSize(new Dimension(260, 48));
        btnContinuar.addActionListener(e -> {
            new Menu().setVisible(true);
            dispose();
        });
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        fundo.add(btnContinuar, gbc);

        // Animação de fade-in usando o glass pane do JFrame
        PainelFade overlay = new PainelFade();
        setGlassPane(overlay);
        overlay.setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                overlay.iniciarFade();
            }
        });
    }


    private static class PainelFundo extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            GradientPaint gp = new GradientPaint(
                    0, 0, new Color(18, 28, 68),
                    w, h, new Color(0, 150, 156)
            );
            g2.setPaint(gp);
            g2.fillRect(0, 0, w, h);

            g2.setColor(new Color(255, 255, 255, 18));
            g2.fillOval(-80, -80, 260, 260);
            g2.fillOval(w - 150, h - 180, 280, 280);

            g2.setColor(new Color(255, 255, 255, 10));
            g2.fillOval(w / 2 - 60, h - 60, 220, 220);

            g2.dispose();
        }
    }

    private static class LogoCirculo extends JPanel {
        LogoCirculo() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int d = Math.min(getWidth(), getHeight());
            int x = (getWidth() - d) / 2;
            int y = (getHeight() - d) / 2;

            GradientPaint gp = new GradientPaint(x, y, new Color(0, 200, 200), x + d, y + d, new Color(0, 110, 160));
            g2.setPaint(gp);
            g2.fillOval(x, y, d, d);

            g2.setStroke(new BasicStroke(3f));
            g2.setColor(new Color(255, 255, 255, 160));
            g2.drawOval(x + 3, y + 3, d - 6, d - 6);

            g2.setFont(new Font("Segoe UI", Font.BOLD, 34));
            g2.setColor(Color.WHITE);
            FontMetrics fm = g2.getFontMetrics();
            String texto = "GM";
            int tx = x + (d - fm.stringWidth(texto)) / 2;
            int ty = y + (d - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(texto, tx, ty);

            g2.dispose();
        }
    }

   private static class TituloComSombra extends JLabel {
        TituloComSombra(String texto) {
            super(texto);
            setFont(new Font("Segoe UI", Font.BOLD, 30));
            setForeground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(getFont());

            FontMetrics fm = g2.getFontMetrics();
            int textX = (getWidth() - fm.stringWidth(getText())) / 2;
            int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

            g2.setColor(new Color(0, 0, 0, 90));
            g2.drawString(getText(), textX + 2, textY + 2);

            g2.setColor(getForeground());
            g2.drawString(getText(), textX, textY);

            g2.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            FontMetrics fm = getFontMetrics(getFont());
            return new Dimension(fm.stringWidth(getText()) + 10, fm.getHeight() + 10);
        }
    }

    private static class BotaoArredondado extends JButton {
        private final Color corNormal = new Color(0, 168, 168);
        private final Color corHover = new Color(0, 200, 200);
        private Color corAtual = corNormal;

        BotaoArredondado(String texto) {
            super(texto);
            setFont(new Font("Segoe UI", Font.BOLD, 15));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    corAtual = corHover;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    corAtual = corNormal;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(corAtual);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 26, 26);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class PainelFade extends JPanel {
        private float alpha = 1f;
        private Timer timer;

        PainelFade() {
            setOpaque(false);
        }

        void iniciarFade() {
            timer = new Timer(15, e -> {
                alpha -= 0.04f;
                if (alpha <= 0f) {
                    alpha = 0f;
                    timer.stop();
                    setVisible(false);
                }
                repaint();
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }
}