/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.MenuComponent;

import java.awt.Color;

/**
 *
 * @author Adam
 */
public class FlowLayoutButton1 extends javax.swing.JPanel {

    /**
     * Creates new form FlowLayoutButton
     */
    public FlowLayoutButton1() {
        initComponents();
        setBackground(new Color(0,0,0,0));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        HotButton = new GUI.Components.pnlBorder();
        jLabel1 = new javax.swing.JLabel();
        IcedButton = new GUI.Components.pnlBorder();
        jLabel2 = new javax.swing.JLabel();
        SmoothieButton = new GUI.Components.pnlBorder();
        jLabel3 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(574, 45));

        HotButton.setBackground(new java.awt.Color(144, 111, 118));
        HotButton.setPreferredSize(new java.awt.Dimension(175, 35));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Hot");

        javax.swing.GroupLayout HotButtonLayout = new javax.swing.GroupLayout(HotButton);
        HotButton.setLayout(HotButtonLayout);
        HotButtonLayout.setHorizontalGroup(
            HotButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HotButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                .addContainerGap())
        );
        HotButtonLayout.setVerticalGroup(
            HotButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HotButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(HotButton);

        IcedButton.setBackground(new java.awt.Color(144, 111, 118));
        IcedButton.setPreferredSize(new java.awt.Dimension(175, 35));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Iced");

        javax.swing.GroupLayout IcedButtonLayout = new javax.swing.GroupLayout(IcedButton);
        IcedButton.setLayout(IcedButtonLayout);
        IcedButtonLayout.setHorizontalGroup(
            IcedButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IcedButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                .addContainerGap())
        );
        IcedButtonLayout.setVerticalGroup(
            IcedButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IcedButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(IcedButton);

        SmoothieButton.setBackground(new java.awt.Color(144, 111, 118));
        SmoothieButton.setPreferredSize(new java.awt.Dimension(175, 35));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Smoothie");

        javax.swing.GroupLayout SmoothieButtonLayout = new javax.swing.GroupLayout(SmoothieButton);
        SmoothieButton.setLayout(SmoothieButtonLayout);
        SmoothieButtonLayout.setHorizontalGroup(
            SmoothieButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SmoothieButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                .addContainerGap())
        );
        SmoothieButtonLayout.setVerticalGroup(
            SmoothieButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SmoothieButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(SmoothieButton);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private GUI.Components.pnlBorder HotButton;
    private GUI.Components.pnlBorder IcedButton;
    private GUI.Components.pnlBorder SmoothieButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration//GEN-END:variables
}
