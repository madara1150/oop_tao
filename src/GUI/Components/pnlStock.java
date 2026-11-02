package GUI.Components;

import GUI.AdminUI.AdminStock;
import Model.Stock;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;

public class pnlStock extends javax.swing.JPanel{
    private AdminStock stock;

    public pnlStock() {
        initComponents();
        setOpaque(false);
        
        
    }
    public void sendData(Stock data){
        lbl_Values.setText(String.valueOf(data.getItems()));

    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblIcon = new javax.swing.JLabel();
        lbl2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lbl_Values = new javax.swing.JLabel();
        lbl3 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(245, 245, 245));
        setMinimumSize(new java.awt.Dimension(670, 155));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblIcon.setFont(new java.awt.Font("Tahoma", 0, 32)); // NOI18N
        lblIcon.setForeground(new java.awt.Color(124, 124, 124));
        lblIcon.setText("-->");
        add(lblIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 55, -1, -1));

        lbl2.setFont(new java.awt.Font("Tahoma", 0, 32)); // NOI18N
        lbl2.setForeground(new java.awt.Color(124, 124, 124));
        lbl2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/icons/database-solid 3.png"))); // NOI18N
        lbl2.setText("  Stock");
        add(lbl2, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 15, -1, -1));

        jPanel1.setBackground(new java.awt.Color(245, 245, 245));

        jPanel2.setBackground(new java.awt.Color(245, 245, 245));

        lbl_Values.setFont(new java.awt.Font("Tahoma", 0, 120)); // NOI18N
        lbl_Values.setForeground(new java.awt.Color(102, 102, 102));
        lbl_Values.setText("0");
        lbl_Values.setPreferredSize(new java.awt.Dimension(66, 140));
        lbl_Values.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        lbl3.setBackground(new java.awt.Color(245, 245, 245));
        lbl3.setFont(new java.awt.Font("Tahoma", 0, 32)); // NOI18N
        lbl3.setForeground(new java.awt.Color(124, 124, 124));
        lbl3.setText("Items");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_Values, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl3)
                .addGap(48, 48, 48))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(lbl_Values, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(lbl3)
                        .addGap(16, 16, 16))))
        );

        jPanel1.add(jPanel2);

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 350, 160));
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked

    }//GEN-LAST:event_formMouseClicked

    @Override
    protected void paintChildren(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        super.paintChildren(g);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lbl2;
    private javax.swing.JLabel lbl3;
    private javax.swing.JLabel lblIcon;
    private javax.swing.JLabel lbl_Values;
    // End of variables declaration//GEN-END:variables

}