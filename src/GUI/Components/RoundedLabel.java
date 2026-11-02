package GUI.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * A label with a rounded background, used for chips, badges and cards.
 */
public class RoundedLabel extends JLabel {

    private final int arc;

    public RoundedLabel(String text, Color background, Color foreground, int arc) {
        super(text, SwingConstants.CENTER);
        this.arc = arc;
        setOpaque(false);
        setBackground(background);
        setForeground(foreground);
        setFont(new Font("Tahoma", Font.BOLD, 20));
        setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
    }

    public RoundedLabel(String text, Color background, Color foreground, int arc, Dimension size) {
        this(text, background, foreground, arc);
        setPreferredSize(size);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
        g2.dispose();
        super.paintComponent(g);
    }
}
