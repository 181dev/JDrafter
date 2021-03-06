/*
 * JLayerBrouser.java
 *
 * Created on 2008/05/07, 19:35
 */

package jui.layer;

import jactions.JUndoRedoEvent;
import jactions.JUndoRedoListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DropMode;
import javax.swing.JDialog;
import javax.swing.JPopupMenu;
import javax.swing.undo.UndoableEdit;
import jedit.layeredit.JDeleteLayerEdit;
import jedit.layeredit.NewLayerEdit;
import jobject.*;
import jscreen.JEnvironment;
import jui.*;

/**
 *
 * @author  takashi
 */
public class JLayerBrowser extends JToolWindow implements ItemListener,JUndoRedoListener{
    private JDocument doc=null;
    private JPage currentPage=null;
    private JLayerActions layerActions=null;
    private JPopupMenu popup=null;
    /** Creates new form JLayerBrouser */
    public JLayerBrowser(Frame parent, boolean modal) {
        this(parent,null);
        
    }
    public JLayerBrowser(Frame parent,JDocument doc){
        super(parent,false);
        this.doc=doc;
        layerActions=new JLayerActions();
        initComponents();
        jMenu1.add(layerActions.newLayerAction);
        jMenu1.add(layerActions.duplicatesLayerAction);
        jMenu1.addSeparator();
        jMenu1.add(layerActions.deleteLayerAction);
        popup=new JPopupMenu();
        popup.add(layerActions.newLayerAction);
        popup.add(layerActions.duplicatesLayerAction);
        popup.addSeparator();
        popup.add(layerActions.deleteLayerAction);
        jTreeView1.setComponentPopupMenu(popup);
        setDocument(doc);
    }
    
    private void setCurrentPage(JPage page){
        currentPage=page;
        if (page != null){
            jTreeView1.setTreeModel(page.getTreeModel());
        }else{
            jTreeView1.setTreeModel(null);
        }
    }
    public void setDocument(JDocument doc){
        if (this.doc !=null){
            this.doc.removeItemListener(this);
            this.doc.removeUndoRedoListener(this);
        }
        this.doc=doc;
        if (this.doc!=null){
            this.doc.addItemListener(this);
            this.doc.addUndoRedoListener(this);
            setCurrentPage(this.doc.getCurrentPage());
            
        }else{
            setCurrentPage(null);
        }
    }
    private Frame getFrame(Component c){
        if (c instanceof Frame) return (Frame)c;
        return getFrame(c.getParent());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTreeView1 = new jui.layer.JTreeView();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jScrollPane1ComponentResized(evt);
            }
        });

        jTreeView1.setBackground(new java.awt.Color(255, 102, 102));

        javax.swing.GroupLayout jTreeView1Layout = new javax.swing.GroupLayout(jTreeView1);
        jTreeView1.setLayout(jTreeView1Layout);
        jTreeView1Layout.setHorizontalGroup(
            jTreeView1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        jTreeView1Layout.setVerticalGroup(
            jTreeView1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 240, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jTreeView1);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jMenu1.setText("レイヤー");
        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jScrollPane1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane1ComponentResized
// TODO add your handling code here:
    jTreeView1.adjustSize();
}//GEN-LAST:event_jScrollPane1ComponentResized
    
    public void itemStateChanged(ItemEvent e) {
        if (currentPage !=doc.getCurrentPage()){
            setCurrentPage(doc.getCurrentPage());
        }
        layerActions.setupStates();
    }
    
    public void undoRedoEventHappened(JUndoRedoEvent e) {
        if (currentPage !=doc.getCurrentPage()){
            setCurrentPage(doc.getCurrentPage());
        }
        layerActions.setupStates();
    }
    
    private class JLayerActions{
        public NewLayerAction newLayerAction;
        public DuplicatesLayerAction duplicatesLayerAction;
        public DeleteLayerAction deleteLayerAction;
        public JLayerActions() {
            newLayerAction=new NewLayerAction();
            duplicatesLayerAction=new DuplicatesLayerAction();
            deleteLayerAction=new DeleteLayerAction();
        }
        private void setupStates(){
            if (doc !=null){
                newLayerAction.setEnabled(true);
                duplicatesLayerAction.setEnabled(true);
                if (currentPage.size()<=1){
                    deleteLayerAction.setEnabled(false);
                }else{
                    deleteLayerAction.setEnabled(true);
                }
            }else{
                newLayerAction.setEnabled(false);
                duplicatesLayerAction.setEnabled(false);
                deleteLayerAction.setEnabled(false);
            }
        }
        private class NewLayerAction extends AbstractAction{
            public NewLayerAction(){
                putValue(NAME,"新規レイヤー");
                setEnabled(false);
            }
            public void actionPerformed(ActionEvent e) {
                Frame frame=getFrame(getThis());
                JLayerOption dlg=new JLayerOption(frame,true);
                int x=frame.getX()+(frame.getWidth()-dlg.getWidth())/2;
                int y=frame.getY()+(frame.getHeight()-dlg.getHeight())/2;
                dlg.setLocation(x,y);
                dlg.showDialog(null,currentPage);
                if (dlg.getLayer() !=null){
                    UndoableEdit anEdit=new NewLayerEdit(doc.getViewer(),dlg.getLayer(),currentPage,"新規レイヤー");
                    doc.fireUndoEvent(anEdit);
                    currentPage.setCurrentLayer(dlg.getLayer());
                    jTreeView1.clearSelection();
                    doc.getViewer().repaint();
                }
                setupStates();
            }
        }
        private class DuplicatesLayerAction extends AbstractAction{
            public DuplicatesLayerAction(){
                putValue(NAME,"レイヤー複製");
                setEnabled(false);
            }
            
            public void actionPerformed(ActionEvent e) {
                if (doc==null) return;
                JLayer layer=currentPage.getCurrentLayer();
                JLayer dLayer=null;
                try{
                    dLayer=(JLayer)layer.clone();
                }catch(Exception ex){
                    ex.printStackTrace();
                    return;
                }
                Vector<Color> colors =new Vector<Color>();
                for (int i=0;i<JEnvironment.PREVIEW_COLORS.length;i++){
                    colors.add(JEnvironment.PREVIEW_COLORS[i]);
                }
                for (int i=0;i<currentPage.size();i++){
                    colors.remove(currentPage.get(i).getPreviewColor());
                }
                if (colors.isEmpty())
                    dLayer.setPreviewColor(JEnvironment.PREVIEW_COLORS[0]);
                else
                    dLayer.setPreviewColor(colors.get(0));
                String name=layer.getName()+"copy";
                name=currentPage.getProperName(name);
                dLayer.setName(name);
                UndoableEdit anEdit=new NewLayerEdit(doc.getViewer(),dLayer,currentPage,"レイヤー複製");
                doc.fireUndoEvent(anEdit);
                currentPage.setCurrentLayer(dLayer);
                jTreeView1.clearSelection();
                doc.getViewer().repaint();
                
            }
        }
        private class DeleteLayerAction extends AbstractAction{
            public DeleteLayerAction(){
                putValue(NAME,"レイヤー削除");
                setEnabled(false);
            }
            
            public void actionPerformed(ActionEvent e) {
                if (doc==null) return;
                UndoableEdit anEdit=new JDeleteLayerEdit(doc.getViewer(),currentPage.getCurrentLayer());
                doc.fireUndoEvent(anEdit);
                setupStates();
                doc.getViewer().repaint();
            }
        }
    }
    /**
     * @param args the command line arguments
     */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private jui.layer.JTreeView jTreeView1;
    // End of variables declaration//GEN-END:variables
    
}
