/*
 * TestSVG.java
 *
 * Created on 2008/09/12, 13:05
 */

package svg;

import java.io.StringReader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import svg.oject.SVGDocument;

/**
 *
 * @author  i002060
 */
public class TestSVG extends javax.swing.JFrame {
    
    /** Creates new form TestSVG */
    public TestSVG() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jButton1 = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        testPanel1 = new svg.TestPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jButton1.setText("parce");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jSplitPane1.setTopComponent(jScrollPane1);

        testPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        javax.swing.GroupLayout testPanel1Layout = new javax.swing.GroupLayout(testPanel1);
        testPanel1.setLayout(testPanel1Layout);
        testPanel1Layout.setHorizontalGroup(
            testPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 422, Short.MAX_VALUE)
        );
        testPanel1Layout.setVerticalGroup(
            testPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 236, Short.MAX_VALUE)
        );
        jSplitPane1.setRightComponent(testPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(344, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(21, 21, 21))
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// TODO add your handling code here:
        String regex="(svg)\\s+(PUBLIC)\\s+\"[^\"]+\"\\s+\"[^\"]+\"";
        try {
            // SAXパーサーファクトリを生成
            SAXParserFactory spfactory = SAXParserFactory.newInstance();
            // SAXパーサーを生成
            SAXParser parser = spfactory.newSAXParser();
            // XMLファイルを指定されたデフォルトハンドラーで処理します
            String xml=jTextArea1.getText();
            xml=xml.replaceFirst(regex,"svg");
            StringReader reader=new StringReader(xml);
            SVGHandler handler=new SVGHandler();           
            parser.parse(new InputSource(reader),handler);
            testPanel1.setDocument(handler.getDocument());
            repaint();
            SVGDocument doc=handler.getDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TestSVG().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea jTextArea1;
    private svg.TestPanel testPanel1;
    // End of variables declaration//GEN-END:variables
    
}
