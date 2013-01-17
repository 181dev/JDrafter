/*
 * JPagePanel.java
 *
 * Created on 2008/06/12, 22:47
 */
package jdraw;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import jobject.JDocument;
import jscreen.JDocumentViewer;

/**
 *
 * @author  takashi
 */
public class JPagePanel extends javax.swing.JPanel {

    private JDocumentViewer viewer = null;
    /** Creates new form JPagePanel */
    public JPagePanel() {
        initComponents();
        changeStates(null);
        currentPage.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                keyPress(e);
            }
        });
        InnerMouseListener ml=new InnerMouseListener();
        prevPage.addMouseListener(ml);
        nextPage.addMouseListener(ml);
        currentPage.addActionListener(new InnerActionListener());
        
    }

    public void changeStates(JDocumentViewer viewer) {
        this.viewer = viewer;
        if (viewer != null) {
            JDocument doc = viewer.getDocument();
            int currentIndex = doc.indexOf(doc.getCurrentPage());
            pageOf.setText("/" + String.valueOf(doc.size()));
            currentPage.setMinValue(1);
            currentPage.setMaxValue(doc.size());
            currentPage.setIntValue(currentIndex + 1);
            currentPage.setEditable(!viewer.getTextPane().isVisible());
            prevPage.setEnabled(currentIndex > 0  && !viewer.getTextPane().isVisible());
            nextPage.setEnabled(currentIndex < doc.size() - 1 && !viewer.getTextPane().isVisible());
        }else{
            pageOf.setText("/");
            currentPage.setText("");
            currentPage.setEnabled(false);
            prevPage.setEnabled(false);
            nextPage.setEnabled(false);
        }
    }

    private void keyPress(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_ENTER || e.getKeyCode()==KeyEvent.VK_TAB){
            if (viewer !=null){
                viewer.requestFocus();
            }
        }
    }

    public class InnerActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            pageChanged();
        }
    }
    private void pageChanged(){
        if (viewer==null) return;
        JDocument doc=viewer.getDocument();
        doc.setPageIndex(currentPage.getIntValue()-1);
        viewer.isDraftMode=false;
        viewer.repaint();
    }
    public class InnerMouseListener extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e){
            if (e.getSource()==prevPage && prevPage.isEnabled()){
                currentPage.setIntValue(currentPage.getIntValue()-1);
                pageChanged();
                
            }else if(e.getSource()==nextPage && nextPage.isEnabled()){
                currentPage.setIntValue(currentPage.getIntValue()+1);
                pageChanged();
            }
            
        }
    }
    @Override
    public void paint(Graphics g){
        Graphics2D g2=(Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paint(g);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        currentPage = new jtools.jcontrol.JDIntegerTextField();
        pageOf = new javax.swing.JLabel();
        prevPage = new javax.swing.JLabel();
        nextPage = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(40, 32767));
        setMinimumSize(new java.awt.Dimension(16, 0));
        setRequestFocusEnabled(false);

        currentPage.setPreferredSize(new java.awt.Dimension(48, 19));
        currentPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                currentPageActionPerformed(evt);
            }
        });

        pageOf.setText("/");

        prevPage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jui/uipicture/lefttriangle.png"))); // NOI18N
        prevPage.setToolTipText("前のページ");

        nextPage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jui/uipicture/righttriangle.png"))); // NOI18N
        nextPage.setToolTipText("次のページ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addComponent(prevPage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currentPage, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pageOf)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextPage))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(prevPage)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(currentPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(pageOf))
            .addComponent(nextPage)
        );
    }// </editor-fold>//GEN-END:initComponents

private void currentPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_currentPageActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_currentPageActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private jtools.jcontrol.JDIntegerTextField currentPage;
    private javax.swing.JLabel nextPage;
    private javax.swing.JLabel pageOf;
    private javax.swing.JLabel prevPage;
    // End of variables declaration//GEN-END:variables
}
