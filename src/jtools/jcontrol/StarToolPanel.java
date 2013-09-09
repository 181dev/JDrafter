/*
 * LineToolPanel.java
 *
 * Created on 2007/11/07, 14:19
 */

package jtools.jcontrol;

import java.awt.Container;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.SpinnerNumberModel;
import jtools.JAbstractTool;
import jscreen.JEnvironment;

/**
 *
 * @author  i002060
 */
public class StarToolPanel extends javax.swing.JPanel{
    //private  static final int WIDTH=272,HEIGHT=125;
    //
    private JDialog dialog;
    private JAbstractTool dragger=null;
    private boolean cancel=true;
    /**
     * Creates new form LineToolPanel
     */
    private static Window getRootWindow(Container c){
        if (c instanceof Window) return (Window)c;
        return getRootWindow(c.getParent());
    }
    public StarToolPanel(JAbstractTool dragger) {
        Window frame=getRootWindow(dragger.getViewer().getRootPane().getParent());
        this.dragger=dragger;
        dialog=new JDialog(frame);
        dialog.setContentPane(this);
        initComponents();
        JEnvironment env=dragger.getEnvironment();
        //this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        Insets inset=dialog.getInsets();
        //dialog.setSize(new Dimension(WIDTH+inset.left+inset.right,HEIGHT+inset.top+inset.bottom));
        dialog.setLocationByPlatform(true);
        dialog.setResizable(false);
        dialog.getRootPane().setDefaultButton(okButton);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModal(true);
        dialog.setTitle(dragger.presentationName() +"オプション");
        //
        ratio.setMaxValue(100d);
        ratio.setMinValue(0);
        vertexes.setModel(new SpinnerNumberModel(5,3,100,1));
        radius.setValue(JEnvironment.DEFAULT_RADIUS);
        vertexes.setValue(JEnvironment.DEFAULT_STAR_VERTEX);
        ratio.setValue(JEnvironment.DEFAULT_STAR_RADIUS_RATIO*100);
        dialog.pack();
        int centerX=frame.getX()+(frame.getWidth()-dialog.getWidth())/2;
        int centerY=frame.getY()+(frame.getHeight()-dialog.getHeight())/2;
        dialog.setLocation(centerX,centerY);
        okButton.requestFocus();
        dialog.setVisible(true);
    }
    public boolean isCanceled(){
        return cancel;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        radius = new jtools.jcontrol.JDLengthTextField();
        ratio = new jtools.jcontrol.JDNumericTextField();
        vertexes = new javax.swing.JSpinner();

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("キャンセル");
        cancelButton.setDefaultCapable(false);
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("半径:");

        jLabel2.setText("頂点数:");

        jLabel3.setText("比率:");

        jLabel4.setText("%");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radius, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(vertexes, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ratio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE))
                        .addGap(14, 14, 14)
                        .addComponent(jLabel4)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(okButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(radius, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(okButton))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ratio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(vertexes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
// TODO add your handling code here:
        cancel=true;
        dialog.setVisible(false);
        dialog.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed
    
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
// TODO add your handling code here:
        cancel=false;
        JEnvironment env=dragger.getEnvironment();
        env.DEFAULT_RADIUS=((Number)radius.getValue()).doubleValue();
        env.DEFAULT_STAR_VERTEX=((Number)vertexes.getValue()).intValue();
        env.DEFAULT_STAR_RADIUS_RATIO=((Number)ratio.getValue()).doubleValue()/100;
        dialog.setVisible(false);
        dialog.dispose();
    }//GEN-LAST:event_okButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JButton okButton;
    private jtools.jcontrol.JDLengthTextField radius;
    private jtools.jcontrol.JDNumericTextField ratio;
    private javax.swing.JSpinner vertexes;
    // End of variables declaration//GEN-END:variables
    
}
