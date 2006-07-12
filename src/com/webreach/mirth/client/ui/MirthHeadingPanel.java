package com.webreach.mirth.client.ui;

import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Creates the heading panel that is used for wizards, login, etc.
 */
public class MirthHeadingPanel extends javax.swing.JPanel
{
    
    /** Creates new form MirthHeadingPanel */
    public MirthHeadingPanel()
    {
        initComponents();
        setOpaque( false );
        setPreferredSize( new Dimension(138, 22) );
    }
    
    protected void paintComponent(Graphics g)
    {
        //  Dispaly image at at full size
        if(PlatformUI.BACKGROUND_IMAGE != null)
            g.drawImage(PlatformUI.BACKGROUND_IMAGE.getImage(), 0, 0, null);
        super.paintComponent(g);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(0, 400, Short.MAX_VALUE)
                );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(0, 300, Short.MAX_VALUE)
                );
    }
    // </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
