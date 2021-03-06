/*
 * LineToolPanel.java
 *
 * Created on 2007/11/07, 14:19
 */

package jtools.jcontrol;

import java.awt.Container;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import jtools.JAbstractTool;
import jscreen.JEnvironment;

/**
 *
 * @author  i002060
 */
public class TranslateToolPanel extends javax.swing.JPanel implements ChangeListener{
    //private  static final int WIDTH=330,HEIGHT=131;
    //
    private JDialog dialog;
    private JAbstractTool dragger=null;
    private boolean cancel=true;
    private boolean cancelEvent=false;
    /**
     * Creates new form LineToolPanel
     */
    private static Window getRootWindow(Container c){
        if (c instanceof Window) return (Window)c;
        return getRootWindow(c.getParent());
    }
    public TranslateToolPanel(Window frame){
        dialog=new JDialog(frame);
        dialog.setContentPane(this);
        initComponents();
        //this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        //Insets inset=dialog.getInsets();
        //dialog.setSize(new Dimension(WIDTH+inset.left+inset.right,HEIGHT+inset.top+inset.bottom));
        dialog.setLocationByPlatform(true);
        dialog.setResizable(false);
        dialog.getRootPane().setDefaultButton(okButton);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModal(true);
        dialog.setTitle("平行移動");
        //
        double dx=JEnvironment.DEFAULT_TRANSLATE_X;
        double dy=JEnvironment.DEFAULT_TRANSLATE_Y;
        distX.setValue(dx);
        distY.setValue(dy);
        double theta=Math.atan2(dy,dx);
        angleSlider1.setValue(-theta);
        angleText.setValue(theta);
        distance.setValue(Math.sqrt(dx*dx+dy*dy));
        dialog.pack();
        //
        distX.addChangeListener(this);
        distY.addChangeListener(this);
        angleSlider1.addChangeListener(this);
        angleText.addChangeListener(this);
        distance.addChangeListener(this);
        //
        int centerX=frame.getX()+(frame.getWidth()-dialog.getWidth())/2;
        int centerY=frame.getY()+(frame.getHeight()-dialog.getHeight())/2;
        dialog.setLocation(centerX,centerY);
        okButton.requestFocus();
        dialog.setVisible(true);
        
    }
    public TranslateToolPanel(JAbstractTool dragger) {
        this(getRootWindow(dragger.getViewer().getRootPane().getParent()));
        this.dragger=dragger;
        dialog.setTitle(dragger.presentationName() +"オプション");
    }
    public boolean isCanceled(){
        return cancel;
    }
    public boolean isCopyed(){
        return copy.isSelected();
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
        copy = new javax.swing.JCheckBox();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        distX = new jtools.jcontrol.JDLengthTextField();
        distY = new jtools.jcontrol.JDLengthTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        angleSlider1 = new jtools.jcontrol.AngleSlider();
        angleText = new jtools.jcontrol.JDAngleTextField();
        distance = new jtools.jcontrol.JDLengthTextField();

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

        copy.setText("コピー");
        copy.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        copy.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel2.setText("Y方向:");

        jLabel1.setText("X方向:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(distY, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(distX, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(91, 91, 91))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(distX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(distY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("移動距離", jPanel1);

        jLabel3.setText("角度");

        jLabel4.setText("移動距離");

        javax.swing.GroupLayout angleSlider1Layout = new javax.swing.GroupLayout(angleSlider1);
        angleSlider1.setLayout(angleSlider1Layout);
        angleSlider1Layout.setHorizontalGroup(
            angleSlider1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );
        angleSlider1Layout.setVerticalGroup(
            angleSlider1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(angleSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(angleText, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(distance, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(angleText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(angleSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(distance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("角度と距離", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(okButton, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                            .addComponent(cancelButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(copy)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(copy))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(okButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        JEnvironment.DEFAULT_TRANSLATE_X=((Number)distX.getValue()).doubleValue();
        JEnvironment.DEFAULT_TRANSLATE_Y=((Number)distY.getValue()).doubleValue();
        dialog.setVisible(false);
        dialog.dispose();
    }//GEN-LAST:event_okButtonActionPerformed
    
    @Override
    public void stateChanged(ChangeEvent e) {
        if (cancelEvent){
            cancelEvent=false;
            return;
        }
        Object o=e.getSource();
        if (o == distX || o==distY){
            double dy=((Number)distY.getValue()).doubleValue();
            double dx=((Number)distX.getValue()).doubleValue();
            double theta=Math.atan2(dy,dx);
            angleSlider1.setValue(-theta);
            angleText.setValue(theta);
            distance.setValue(Math.sqrt(dx*dx+dy*dy));
        }else{
            double theta;
            if (o==angleSlider1){
                theta=-((Number)angleSlider1.getValue()).doubleValue();
                angleText.setValue(theta);
            }else if (o==angleText){
                theta=((Number)angleText.getValue()).doubleValue();
                angleSlider1.setValue(-theta);
            }else{
                theta=((Number)angleText.getValue()).doubleValue();
            }
            double dist=((Number)(distance.getValue())).doubleValue();
            distX.setValue(dist*Math.cos(theta));
            distY.setValue(-dist*Math.sin(theta));
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private jtools.jcontrol.AngleSlider angleSlider1;
    private jtools.jcontrol.JDAngleTextField angleText;
    private javax.swing.JButton cancelButton;
    private javax.swing.JCheckBox copy;
    private jtools.jcontrol.JDLengthTextField distX;
    private jtools.jcontrol.JDLengthTextField distY;
    private jtools.jcontrol.JDLengthTextField distance;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    
}
