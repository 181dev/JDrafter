/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ArrowPanel.java
 *
 * Created on 2009/06/27, 14:36:22
 */

package jui.color.arrow;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import jobject.effector.JArrowEffect;

/**
 *
 * @author takashi
 */
public class ArrowPanel extends javax.swing.JPanel {
    private boolean resultOk=false;
    /** Creates new form ArrowPanel */
    public ArrowPanel() {
        initComponents();
        jSpinner1.setModel(new SpinnerNumberModel(100,50,1000,10));
        jSpinner2.setModel(new SpinnerNumberModel(100,50,1000,10));
        jSpinner3.setModel(new SpinnerNumberModel(1.0,0,100,0.5));
    }
    private boolean isResultOk(){
        return resultOk;
    }
    public static JArrowEffect showAsDialog(Component cmp,JArrowEffect effect){
        Frame owner=null;
        if (cmp !=null) owner=getRootFrame(cmp);
        JDialog jd=new JDialog(owner,"矢印を作成",true);
        jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        ArrowPanel panel=new ArrowPanel();
        jd.getContentPane().add(panel);
        jd.pack();
        jd.setResizable(false);
        if (effect ==null){
            panel.arrowCombo1.setSelectedIndex(0);
            panel.arrowCombo2.setSelectedIndex(0);
            panel.jSpinner1.setValue(new Float(400));
            panel.jSpinner2.setValue(new Float(400));

        }else{
            panel.arrowCombo1.setSelectedItem(effect.getStartShape());
            panel.arrowCombo2.setSelectedItem(effect.getEndShape());
            panel.jSpinner1.setValue(effect.getStartRatio()*100);
            panel.jSpinner2.setValue(effect.getEndRatio()*100);
        }
        panel.jSpinner3.setValue(4);
        Point cp=new Point(0,0);
        SwingUtilities.convertPointToScreen(cp, cmp);
        Dimension size=jd.getSize();
        jd.setLocation(cp.x+(cmp.getWidth()-size.width)/2,cp.y+(cmp.getHeight()-size.height)/2);
        jd.setVisible(true);
        if (!panel.resultOk) return effect;
        float fs=((Number)panel.jSpinner1.getValue()).floatValue()/100;
        float fe=((Number)panel.jSpinner2.getValue()).floatValue()/100;
        Shape sp=null, ep=null;
        if (panel.arrowCombo1.getSelectedItem() !=null)
            sp=(Shape)panel.arrowCombo1.getSelectedItem();
        if (panel.arrowCombo2.getSelectedItem() !=null)
            ep=(Shape)panel.arrowCombo2.getSelectedItem();
        jd.dispose();
        float sourceRat1=1f;
        float sourceRat2=1f;
        Shape sourceS1=null;
        Shape sourceS2=null;
        if (effect !=null){
            sourceRat1=effect.getStartRatio();
            sourceRat2=effect.getEndRatio();
            sourceS1=effect.getStartShape();
            sourceS2=effect.getEndShape();
        }


        if (sourceRat1==fs && sourceRat2==fe && sp==sourceS1 && ep==sourceS2){
             return effect;
        }
        if (sp ==null && ep==null){
            return null;
        }
        return new JArrowEffect(sp,ep,fs,fe);
    }

    private static Frame getRootFrame(Component c){
        if (c instanceof Frame) return (Frame)c;
        if (c==null) return null;
        return getRootFrame(c.getParent());
    }
    private static Window  getRootWindow(Component c){
        if (c instanceof Window) return(Window)c;
        if (c==null) return null;
        return getRootWindow(c.getParent());
    }



    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        arrowCombo2 = new jui.color.arrow.ArrowCombo();
        jLabel4 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        arrowCombo1 = new jui.color.arrow.ArrowCombo();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jSpinner3 = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        arrowPreviewer1 = new jui.color.arrow.ArrowPreviewer();
        jButton1 = new javax.swing.JButton();

        jButton4.setText("キャンセル");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton3.setText("OK");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("始点:");

        jLabel2.setText("サイズ:");

        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                stateChanged1(evt);
            }
        });

        jLabel3.setText("終点:");

        arrowCombo2.setMaximumRowCount(12);
        arrowCombo2.setDoubleBuffered(true);
        arrowCombo2.setFocusable(false);
        arrowCombo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stateChanged(evt);
            }
        });

        jLabel4.setText("サイズ:");

        jSpinner2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                stateChanged1(evt);
            }
        });

        arrowCombo1.setMaximumRowCount(12);
        arrowCombo1.setDirection(1);
        arrowCombo1.setFocusable(false);
        arrowCombo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stateChanged(evt);
            }
        });

        jLabel7.setText("%");

        jLabel8.setText("%");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(arrowCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(arrowCombo2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jSpinner2)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel7))
                    .addComponent(arrowCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel8))
                    .addComponent(arrowCombo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton2.setText("OK");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("プレビュー"));

        jSpinner3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                stateChanged1(evt);
            }
        });

        jLabel5.setText("線幅:");

        jLabel6.setText("Point");

        javax.swing.GroupLayout arrowPreviewer1Layout = new javax.swing.GroupLayout(arrowPreviewer1);
        arrowPreviewer1.setLayout(arrowPreviewer1Layout);
        arrowPreviewer1Layout.setHorizontalGroup(
            arrowPreviewer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 236, Short.MAX_VALUE)
        );
        arrowPreviewer1Layout.setVerticalGroup(
            arrowPreviewer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addContainerGap(128, Short.MAX_VALUE))
            .addComponent(arrowPreviewer1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(arrowPreviewer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6))
                .addContainerGap())
        );

        jButton1.setText("キャンセル");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                canceled(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void stateChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stateChanged
        // TODO add your handling code here:
           changeArrow();
    }//GEN-LAST:event_stateChanged

    private void stateChanged1(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_stateChanged1
        // TODO add your handling code here:
        changeArrow();
    }//GEN-LAST:event_stateChanged1

    private void canceled(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_canceled
        // TODO add your handling code here:
        resultOk=false;
        getRootWindow(this).dispose();
    }//GEN-LAST:event_canceled

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        resultOk=true;
        getRootWindow(this).dispose();
    }//GEN-LAST:event_jButton2ActionPerformed
    private void changeArrow(){
        arrowPreviewer1.direction=1;
        arrowPreviewer1.start=(Shape)arrowCombo1.getSelectedItem();
        arrowPreviewer1.end=(Shape)arrowCombo2.getSelectedItem();
        arrowPreviewer1.startRatio=((Number)jSpinner1.getValue()).floatValue()/100;
        arrowPreviewer1.endRatio=((Number)jSpinner2.getValue()).floatValue()/100;
        arrowPreviewer1.strokeWidth=((Number)jSpinner3.getValue()).floatValue();
        arrowPreviewer1.repaint();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private jui.color.arrow.ArrowCombo arrowCombo1;
    private jui.color.arrow.ArrowCombo arrowCombo2;
    private jui.color.arrow.ArrowPreviewer arrowPreviewer1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    // End of variables declaration//GEN-END:variables
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                ArrowPanel.showAsDialog(null,null);
            }
        });
    }

}
