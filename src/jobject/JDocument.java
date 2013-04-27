/*
 * JDocument.java
 *
 * Created on 2007/08/27, 14:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package jobject;

import jactions.JUndoRedoListener;
import jactions.JUndoRedoEvent;
import java.awt.Graphics2D;
import java.awt.ItemSelectable;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoableEdit;
import jscreen.JDocumentViewer;
import jscreen.JEnvironment;
import jscreen.JRequest;

/**
 *�S�Ă̕`��I�u�W�F�N�g�̃��[�g�ƂȂ�`��I�u�W�F�N�g�ł��B
 * �����y�[�W�����L����h�L�������g�������܂�.
 * @author i002060
 */
public class JDocument extends JObject<JObject,JPage>implements ItemSelectable,Pageable,ChangeListener{
    /**
     * �J�����g�̃v�����^�[�W���u��ێ����܂�.
     */
    public static  final PrinterJob printerJob=PrinterJob.getPrinterJob();
    private transient Vector<UndoableEditListener> undoListener;
    private transient Vector<JUndoRedoListener> undoRedoListeners;
    private transient Vector<ItemListener> itemListeners;
    private transient Vector<ChangeListener> environmentChangeListener;
    private transient JPage currentPage=null;
    //
    private static final long serialVersionUID=110l;
    //
    private transient JDocumentViewer viewer=null;
    /** �f�t�H���g�̃R���X�g���N�^�[�ł��B
     * ��̋󔒂̃y�[�W��ێ�����JDocument���\�z���܂�.
     */
    public JDocument() {
        this(new JPage());
    }
    /**
     * �w�肷��y�[�W��ێ�����JDocument���\�z���܂�.
     * @param page �w�肷��y�[�W.
     */
    public JDocument(JPage page){
        //printerJob=PrinterJob.getPrinterJob();
        add(page);
        undoListener=new Vector<UndoableEditListener>();
        undoRedoListeners=new Vector<JUndoRedoListener>();
        itemListeners=new Vector<ItemListener>();
        environmentChangeListener=new Vector<ChangeListener>();
        viewer=null;
    }
    /**
     * ����JDocument��\������JDocumentViewer���w�肵�܂�.
     * @param v ����JDocument��\������JDocumentViewer
     */
    public void setViewer(JDocumentViewer v){
        viewer=v;
    }
    /**
     * ����JDocument��\������JDocumentViewer��Ԃ��܂�.
     * @return
     */
    public JDocumentViewer getViewer(){
        return viewer;
    }
    /**
     * ����JDocument�Ɏ������\�ȕύX��������ꂽ�ۂ�UndoableEditEvent���󂯎�邽�߂ɁA�w�肳�ꂽ
     * UndobaleEditListener��ǉ����܂�.
     * @param u �w�肷��UndoableEditListener
     */
    public void addUndoableEditListener(UndoableEditListener u){
        if (undoListener.contains(u)) return;
        undoListener.add(u);
    }
    /**
     * ����JDocument����UndoableEditEvent���󂯎��Ȃ��悤�ɁA�w�肳�ꂽ
     * UnodoableEditListener���폜���܂�.
     * @param u �폜����UndoableEditListener
     */
    public void removeUndoableEditListener(UndoableEditListener u){
        undoListener.remove(u);
    }
    /**
     * ����JDocument�Ɏ������\�ȕύX���͕ύX�̎������������ꂽ�ۂ�JUndoRedoEvent���󂯎�邽�߂ɁA
     * JUndoRedoListener��ǉ����܂�.
     * @param l �ǉ�����JUndoRedoListener
     */
    public void addUndoRedoListener(JUndoRedoListener l){
        if (undoRedoListeners.contains(l)) return;
        undoRedoListeners.add(l);
        
    }
    /**
     * ����JDocument�Ɏ������\�ȕύX���͕ύX�̎������������ꂽ�ۂ�JUndoRedoEvent���󂯎��Ȃ��悤��
     * JUndoRedoListener���폜���܂�.
     * @param l
     */
    public void removeUndoRedoListener(JUndoRedoListener l){
        undoRedoListeners.remove(l);
    }
    /**
     * UndoableEditEvent��ʒm���܂�.
     * @param e ������ꂽ�ύX��ێ�����UndoableEdit
     */
    public void fireUndoEvent(UndoableEdit e){
        JPage p=getCurrentPage();
        if (e!=null)
            p.getUndoManager().addEdit(e);
        UndoableEditEvent ue=new UndoableEditEvent(p,e);
        if (undoListener==null) return;
        for(int i=0;i<undoListener.size();i++){
            undoListener.get(i).undoableEditHappened(ue);
        }
        fireUndoRedoEvent(new JUndoRedoEvent(this,JUndoRedoEvent.REDO));
        
    }
    /**
     * UnodoRedoEvent��ʒm���܂�.
     * @param e ������ꂽ�ύX���͎������ꂽ�ύX��ێ�����JUndoRedoEvent
     */
    public void fireUndoRedoEvent(JUndoRedoEvent e){
        if (undoRedoListeners==null) return;
        for (int i=0;i<undoRedoListeners.size();i++){
            undoRedoListeners.get(i).undoRedoEventHappened(e);
        }
    }
    /**
     * ���݂̕`������ύX���ꂽ�ۂ�ChangeEvent���󂯎�邽�߂�ChangeListener��
     * �ǉ����܂�.
     * @param ls �ǉ�����ChangeListener
     */
    public void addenvironmentChangeListener(ChangeListener ls){
        if (ls !=null && !environmentChangeListener.contains(ls))
            environmentChangeListener.add(ls);
    }
    /**
     * ���݂̕`������ύX���ꂽ�ۂ�ChangeEvent���󂯎��Ȃ��悤��ChangeListener��
     * �폜���܂��B
     * @param ls �폜����ChangeLIstener
     */
    public void removeenvironmentChangeListener(ChangeListener ls){
        environmentChangeListener.remove(ls);
    }
    /**
     * �`������ύX���ꂽ���Ƃ�ʒm���܂�.
     * @param e �`����̕ύX��ێ�����ChangeEvent
     */
    public void fireEnvironmentChange(ChangeEvent e){
        Iterator<ChangeListener> it=environmentChangeListener.iterator();
        while(it.hasNext())
            it.next().stateChanged(e);
    }
    /**
     * ���݂̗L����printerJobj��Ԃ��܂�.
     * @return
     */
    public PrinterJob getPrinterJob(){
        return printerJob;
    }
    /**
     * �J�����g�̃y�[�W�C���f�b�N�X��ݒ肵�܂��B
     * @param index�@�J�����g�ɐݒ肷��y�[�W�C���f�b�N�X.
     */
    public void setPageIndex(int index){
        if (index>=0 && index<size() && indexOf(getCurrentPage()) !=index){
            setCurrentPage(get(index));
        }
        return;
    }
    /**
     * �J�����g�̃y�[�W�C���f�b�N�X��Ԃ��܂�.
     * @return �J�����g�̃y�[�W�C���f�b�N�X.
     */
    public int getPageIndex(){
        return indexOf(getCurrentPage());
    }
    /**
     * �w�肷��y�[�W���J�����g�y�[�W�ɐݒ肵�܂�.
     * @param page �J�����g�Ɏw�肷��y�[�W
     */
    public void setCurrentPage(JPage page){
        if (contains(page) && currentPage !=page){
            currentPage=page;
            fireItemEvent(getCurrentPage().getRequest().getSelectedVector(),ItemEvent.SELECTED);
            fireUndoEvent(null);
        }
    }
    /**
     * �J�����g�y�[�W���擾���܂��B
     * @return�@�J�����g�y�[�W.
     */
    public JPage getCurrentPage(){
        if (currentPage==null)
            currentPage=get(0);
        return currentPage;
    }
    /**
     * ���݂̕`������擾���܂�.
     * @return ���݂̕`���
     */
    public JEnvironment getEnvironment(){
        if (getCurrentPage()==null) return null;
        return getCurrentPage().getEnvironment();
    }
    /**
     * �h�L�������g�̖����Ɏw�肷��y�[�W��ǉ����܂��B
     * @param p �ǉ�����JPage
     */
    @Override
    public void add(JPage p){
        if (children.contains(p)) return;
        if(children.add(p))
            p.setParent(this);
        setCurrentPage(p);
        p.getEnvironment().addChangeListener(this);
    }
    /**
     * �w�肷��ʒu�Ƀy�[�W��ǉ����܂�.
     * @param index �ǉ��w��ʒu.
     * @param p �ǉ�����y�[�W
     */
    @Override
    public void add(int index,JPage p){
        if (children.contains(p)) return;
        children.add(index,p);
        p.setParent(this);
        setCurrentPage(p);
        p.getEnvironment().addChangeListener(this);
    }
    /**
     * �w�肷��y�[�W�����̃h�L�������g����폜���܂�.
     * @param p �폜����y�[�W
     */
    @Override
    public void remove(JPage p){
        if (p==getCurrentPage()){
            int index=indexOf(p);
            children.remove(p);
            if(index>=size()){
                index--;
            }
            setCurrentPage(get(index));
        }else{
            children.remove(p);
        }
        p.getEnvironment().removeChangeListener(this);
    }
    /**
     * �w��C���f�b�N�X�ɂ���y�[�W���폜���܂�.
     * @param idx �w�肷��C���f�b�N�X.
     * @return �폜���ꂽ�y�[�W
     */
    @Override
    public JPage remove(int idx){
        JPage cPage=getCurrentPage();
        JPage ret=remove(idx);
        if (cPage==ret){
            if (idx>=size())
                setCurrentPage(get(idx-1)); 
        }
        ret.getEnvironment().removeChangeListener(this);
        return ret;
    }
    /**
     * ���̕`��I�u�W�F�N�g��ێ�����JDocument��
     * �Ԃ��܂�.
     * @return ���̕`��I�u�W�F�N�g��ێ�����JDocument
     */
    @Override
    public JDocument getDocument(){
        return this;
    }
    /**
     * ���̕`��I�u�W�F�N�g��ێ�����Jpage
     * @return�@���null��Ԃ��܂�.
     */
    @Override
    public JPage getPage(){
        return null;
    }
    /**
     * �p���̂��߂̎����ł��B�����ύX���܂���B
     * 
     * @param tr 
     * @param req
     * @param p
     */
    @Override
    public void transform(AffineTransform tr,JRequest req,Point p) {
        //Do Nothing
    }
    @Override
    public void transform(AffineTransform tr){
        //DO Nothing
    }
    /**
     * �w�肷��_��Point2D�Ƀq�b�g����I�u�W�F�N�g��T�����A�q�b�g�����I�u�W�F�N�g��JRequest�Ɋi�[���܂��B
     * @param env �`�����ێ�����Jevironment
     * @param req ���݂̑I����e�y�уq�b�g�����I�u�W�F�N�g���i�[����JRequest
     * @param point �q�b�g�𔻒肷��Point2D
     * @return �T������ JRequest.HIT_NON,JRequest.HIT_OBJECT,JRequew.HIT_PATH,JRequest.HIT_ANCUR,JRequest.HIT_LCONTROL,JRequest.HIT_RCONTROL
     */
    @Override
    public int hitByPoint(JEnvironment env, JRequest req, Point2D point) {
        if (getCurrentPage() != null)
            return getCurrentPage().hitByPoint(env,req,point);
        else
            return req.HIT_NON;
    }
    /**
     * �w�肷��_��Rectangle2D�Ƀq�b�g����I�u�W�F�N�g��T�����A�q�b�g�����I�u�W�F�N�g��JRequest�Ɋi�[���܂��B
     * @param env �`�����ێ�����Jevironment
     * @param req ���݂̑I����e�y�уq�b�g�����I�u�W�F�N�g���i�[����JRequest
     * @param rect �q�b�g�𔻒肷��Rectangle2D
     */    
    @Override
    public void hitByRect(JEnvironment env, JRequest req, Rectangle2D rect) {
        if (getCurrentPage()!=null)
            getCurrentPage().hitByRect(env,req,rect);
    }
    /**
     * �p���̂��߂̎����ł�.�������܂���B
     * @param env
     * @return
     */
    @Override
    public UndoableEdit updateTransform(JEnvironment env) {
        //Do Nothing;
        return null;
    }
    /**
    * �p���̂��߂̎����ł��������܂���B
    * @param env
    * @param rotation
    * @return
    */
    @Override
    public UndoableEdit updateRotate(JEnvironment env,double rotation){
        return null;
    }
    /**
     * ���̃I�u�W�F�N�g��`�悵�܂�.
     * @param clip�@�`��̃N���b�v�̂��߂̃o�E���f�B���O�{�b�N�X
     * @param g�@�O���t�B�b�N�X�R���e�L�X�g
     */
    @Override
    public void paintThis(Rectangle2D clip, Graphics2D g) {
    }
    /**
     * ���̃I�u�W�F�N�g�̃v���r���[��`�悵�܂�.
     * @param env �`�����ێ�����JEnvironment
     * @param req �I����e��ێ�����JRequest
     * @param g �O���t�B�b�N�R���e�L�X�g
     */
    @Override
    public void paintPreview(JEnvironment env, JRequest req, Graphics2D g) {
        //Do Nothing;
    }
    /**
     * ���̃I�u�W�F�N�g�̕�����Ԃ��܂�.
     * @return ���̃I�u�W�F�N�g�̕���.
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException{
        throw new CloneNotSupportedException();
    }
    /**
     * ���̃I�u�W�F�N�g�̗L���ȑI��͈͂��܂���ŏ���Rectangle2D��Ԃ��܂��B
     * @return�@���̃I�u�W�F�N�g�̗L���ȑI��͈͂��܂���ŏ���Rectangle2D
     */
    @Override
    public Rectangle2D getSelectionBounds() {
        return null;
    }
    /**
     * ���̃I�u�W�F�N�g�̗ݐς��ꂽ��]�ϊ����Ȃ������ꍇ�̑I��͈͂��܂���ŏ���
     * Rectangle2D��Ԃ��܂��B
     * @param x �ݐς��ꂽ��]�����Z�b�g����ۂ̒��S��X���W
     * @param y �ݐς��ꂽ��]�����Z�b�g����ۂ̒��S��Y���W
     * @return �ݐς��ꂽ��]�ϊ����Ȃ������ꍇ�̑I��͈͂��܂���ŏ���Rectangle2D
     */
    @Override
    public Rectangle2D getOriginalSelectionBounds(double x,double y) {
        return null;
    }
    /**
     * ���̃I�u�W�F�N�g��������������ۂ̊�����Ԃ��܂�.
     * @return ���̃I�u�W�F�N�g��������������ۂ̊���.
     */
    @Override
    public String getPrefixer(){
        return "Document";
    }
    /**
     * ���̕`��I�u�W�F�N�g�̕`��͈͂��܂���ŏ���Rectangle2D��Ԃ��܂��B
     * @return ���̕`��I�u�W�F�N�g�̕`��͈͂��܂���ŏ���Rectangle2D
     */
    @Override
    public Rectangle2D getBounds() {
        return null;
    }
    /**
     *  ���̕`��I�u�W�F�N�g�Ɋ܂܂��I�����ꂽ�I�u�W�F�N�g��v�f�Ƃ���z���Ԃ��܂��B
     * @return�@���̕`��I�u�W�F�N�g�Ɋ܂܂��I�����ꂽ�I�u�W�F�N�g��v�f�Ƃ���z��.
     */
    @Override
    public Object[] getSelectedObjects() {
        JRequest req=getCurrentPage().getRequest();
        return req.getSelectedVector().toArray();
    }
    /**
     * ���̕`��I�u�W�F�N�g�Ɋ܂܂��`��I�u�W�F�N�g�̑I�����ύX���ꂽ�ꍇ�ɁAItemEvent���󂯎�邽�߂�
     * �w�肳�ꂽItemListener��ǉ����܂�.
     * @param l �ǉ�����ItemListener
     */
    @Override
    public void addItemListener(ItemListener l) {
        if (!itemListeners.contains(l))
            itemListeners.add(l);
    }
    /**
     * ���̕`��I�u�W�F�N�g�Ɋ܂܂��`��I�u�W�F�N�g�̑I�����ύX���ꂽ�ꍇ�ɁAItemEvent���󂯎��Ȃ��悤�ɂ��邽�߂�
     * �w�肳�ꂽItemListener���폜���܂��B
     * @param l �폜����ItemListener
     */    
    @Override
    public void removeItemListener(ItemListener l) {
        itemListeners.remove(l);
    }
    /**
     * ItemListener��ItemEvent��ʒm���܂�.
     * @param o ItemEvent�����������I�u�W�F�N�g
     * @param stateChange �C�x���g�̎��.
     */
    public void fireItemEvent(Object o,int stateChange){
        if (itemListeners==null) return;
        ItemEvent e=new ItemEvent(this,ItemEvent.ITEM_STATE_CHANGED,o,stateChange);
        for (int i=0;i<itemListeners.size();i++){
            itemListeners.get(i).itemStateChanged(e);
        }
    }
    /**
     * ���̕`��I�u�W�F�N�g���`�悷��Shape��Ԃ��܂�.
     * @return
     */
    @Override
    public Shape getShape() {
        return null;
    }
    private void readObject(java.io.ObjectInputStream in)
    throws IOException, ClassNotFoundException{
        in.defaultReadObject();
        for (int i=0;i<size();i++){
            get(i).setParent(this);
        }
        //printerJob=PrinterJob.getPrinterJob();
        environmentChangeListener =new Vector<ChangeListener>();
        undoListener=new Vector<UndoableEditListener>();
        undoRedoListeners=new Vector<JUndoRedoListener>();
        itemListeners=new Vector<ItemListener>();
        viewer=null;
        for (int i=0;i<size();i++){
            get(i).getEnvironment().addChangeListener(this);
        }
    }
    /**
     * ����JDocument���ێ�����y�[�W�̐���Ԃ��܂��B
     * @return�@����JDocument���ێ�����y�[�W�̐�
     */
    @Override
    public int getNumberOfPages() {
        return size();
    }
    /**
     * ����JDocument�̎w��ʒu�̃y�[�W��PageFormat��Ԃ��܂�.
     * @param pageIndex �w�肷��y�[�W.
     * @return �w��y�[�W��PageFormat
     * @throws java.lang.IndexOutOfBoundsException
     */
    @Override
    public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
        return get(pageIndex).getPageFormat();
    }
    /**
     * �w�肷��C���f�b�N�X�̃y�[�W��Printable�I�u�W�F�N�g��Ԃ��܂�.
     * @param pageIndex �w�肷��C���f�b�N�X.
     * @return �w�肷��C���f�b�N�X�̃y�[�W��Printable�I�u�W�F�N�g
     * @throws java.lang.IndexOutOfBoundsException
     */
    @Override
    public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
        return get(pageIndex);
    }
    /**
     * �\���{�����ύX���ꂽ�ۂ�JEnvironment�I�u�W�F�N�g����Ăяo����܂�.
     * @param e
     */
    @Override
    public final void stateChanged(ChangeEvent e) {
        fireEnvironmentChange(e);
    }
    
}
