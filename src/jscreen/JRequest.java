/*
 * JRequest.java
 *
 * Created on 2007/08/27, 11:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package jscreen;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;
import jobject.JLeaf;
import jobject.JObject;
import jobject.JPage;

/**
 *�I�u�W�F�N�g�̑I����Ԃ�ێ�����Class�ł��B
 * 
 * @author i002060
 */
public class JRequest {
    /**
     * �`��I�u�W�F�N�g�̂ǂ��ɂ�Hit���Ȃ���Ԃł��B
     */
    public static final int HIT_NON=0;
    /**
     * �`��I�u�W�F�N�g�S�̂�Hit������Ԃł��B
     */
    public static final int HIT_OBJECT=1;
    /**
     * �I�u�W�F�N�g�̃p�X��Hit������Ԃł��B
     */
    public static final int HIT_PATH=2;
    /**
     * �A���J�[�|�C���g��Hit������Ԃł�.
     */
    public static final int HIT_ANCUR=3;
    /**
     * �����̃R���g���[���n���h����Hit������Ԃł��B
     */
    public static final int HIT_L_CONTROL=4;
    /**
     * �E���̃R���g���[���n���h����hit������Ԃł��B
     */
    public static final int HIT_R_CONTROL=5;
    /**
     * �_�C���N�g�I�����[�h������킵�܂�.
     */
    public static final int DIRECT_MODE=1;
    /**
     * �O���[�v�I�����[�h������킵�܂��B
     */
    public static final int GROUP_MODE=2;
    /**
     * �q�b�g���،��ʂ��i�[���܂�.
     */
    public int hitResult;
    private int selectionMode;    
    public  Vector hitObjects;
    /**
     * �q�b�g���؎���Alt�L�[�̉�����Ԃ������܂�.
     */
    public boolean isAltDown;
    /**
     * �q�b�g���؎���Ctrl�L�[�̉�����Ԃ������܂�.
     */
    public boolean isCtlDown;
    /**
     * �q�b�g���؎���Shift�L�[�̉�����Ԃ������܂�.
     */
    public boolean isShiftDown;
    private JPage jpage;
    private  Vector selectedObjects;
    private Vector<ItemListener> listener;
    /**
     * �w�肷��JPge�I�u�W�F�N�g�̑I����Ԃ�\��JRequest�̃C���X�^���X���\�z���܂�.
     * @param page �w�肷��JPage�I�u�W�F�N�g
     */
    public JRequest(JPage page) {
        hitResult=HIT_NON;
        selectionMode=GROUP_MODE;
        selectedObjects=new Vector();
        hitObjects=new Vector();
        listener=new Vector<ItemListener>();
        isAltDown=false;
        isCtlDown=false;
        isShiftDown=false;
        this.jpage=page;
    }
    /**
     * �q�b�g���؎��̑I�����[�h��ݒ肵�܂��B
     * @param mode �q�b�g���؎��̑I�����[�h(DIRECT_MODE����GROUP_MODE)
     */
    public void setSelectionMode(int mode){
        if (mode != DIRECT_MODE && mode != GROUP_MODE) return;
        if (mode==selectionMode) return;
        if (mode ==DIRECT_MODE){
            Vector svect=new Vector();
            for (int i=0;i<size();i++){
                Object o=get(i);
                if (o instanceof JObject){
                   JObject jo=(JObject)o;
                   Vector v=jo.getLeafs();
                   for (int j=0;j<v.size();j++){
                       svect.add(v.get(j));
                   }
                }else if (o instanceof JLeaf){
                    svect.add(o);
                }
            }
            clear();
            for (int i=0;i<svect.size();i++){
                add(svect.get(i));
            }
        }else{
            for (int i=0;i<size();i++){
                Object o=get(i);
                if (!(o instanceof JLeaf)) {
                    remove(o);
                }
            }
        }
        selectionMode=mode;
    }
    /**
     * ���ݐݒ肳��Ă���q�b�g���؎��̑I�����[�h���擾���܂�.
     * @return �q�b�g���؎��̑I�����[�h
     */
    public int getSelectionMode(){
        return selectionMode;
    }
    /**
     * �w�肷��Object���I����Ԃɂ���ꍇ��true��Ԃ��܂�.
     * @param o �w�肷��Object
     * @return �w�肷��Object���I����Ԃɂ���ꍇ��true����ȊO��false
     */
    public boolean contains(Object o){
        return selectedObjects.contains(o);
    }
    /**
     * �w�肷��Object��I����Ԃɂ��܂�.
     * @param o �w�肷��Object
     */
    public void add(Object o){
        if (o instanceof JLeaf){
            if (!isSelectable((JLeaf)o)) return;
        }
        if(!selectedObjects.contains(o)){
            selectedObjects.add(o);
            fireChangeEvent(o,ItemEvent.SELECTED);
        }
        
    }
    /**
     * �w�肷��JLeaf�̃C���X�^���X���I���\�ł���ꍇ��true��Ԃ��܂�.
     * @param jl �w�肷��JLeaf�̃C���X�^���X�B
     * @return �w�肷��JLeaf�̃C���X�^���X���I���\�ȏꍇtrue����ȊO��false
     */
    private boolean isSelectable(JLeaf jl){
        if (!jl.isVisible() || jl.isLocked()) return false;
        JLeaf parent=jl.getParent();
        if (parent==null) return true;
        return isSelectable(parent);
    }
    /**
     * �w�肷��Object�̑I����Ԃ��������܂�.
     * @param o �I������������Object
     */
    public void remove(Object o){
        selectedObjects.remove(o);
        fireChangeEvent(o,ItemEvent.DESELECTED);
    }
    /**
     * �w�肷��C���f�b�N�X��Object�̑I����Ԃ��������܂�.
     * @param i �I����Ԃ���������Object�̃C���f�b�N�X.
     * @return �I����Ԃ���������Object
     */
    public Object remove(int i){
        return selectedObjects.remove(i);
    }
    /**
     * �S�Ă̑I��Object�̑I����Ԃ��������܂�.
     */
    public void clear(){
        selectedObjects.clear();
        fireChangeEvent(null,ItemEvent.DESELECTED);
    }
    /**
     * �I�����ꂽ�I�u�W�F�N�g�̐���Ԃ��܂�.
     * @return �I�����ꂽ�I�u�W�F�N�g��
     */
    public int size(){
        return selectedObjects.size();
    }
    /**
     * �����I������Ă��Ȃ��ꍇ��true��Ԃ��܂��B
     * @return�@�����I������Ă��Ȃ��ꍇtrue�A����ȊO��false
     */
    public boolean isEmpty(){
        return selectedObjects.isEmpty();
    }
    /**
     * �w�肷��C���f�b�N�X�̑I���I�u�W�F�N�g��Ԃ��܂�.
     * @param i �w�肷��C���f�b�N�X.
     * @return �w�肵���C���f�N�X�̑I���I�u�W�F�N�g
     */
    public Object get(int i){
        return selectedObjects.get(i);
    }
    /**
     * �I���I�u�W�F�N�g��v�f�Ƃ���Vector��Ԃ��܂�.
     * @return �I���I�u�W�F�N�g��v�f�Ƃ���Vector
     */
    public Vector getSelectedVector(){
        return selectedObjects;
    }
    /**
     * �w�肷��Vector�̗v�f�̃I�u�W�F�N�g��I����Ԃɂ��܂�.
     * @param v �I����ԂƂ���I�u�W�F�N�g��v�f�Ƃ���Vector
     */
    public void setSelectedVector(Vector v){
        selectedObjects=v;
        fireChangeEvent(v,ItemEvent.SELECTED);
    }
    /**
     * �I����Ԃ��ύX���ꂽ���Ƃ��AJRequest�����L����JPae�I�u�W�F�N�g�ɑ��M���܂�.
     * @param o �I����Ԃ��ύX���ꂽ�ΏۃI�u�W�F�N�g
     * @param stateChange �I�����.
     */
    public void fireChangeEvent(Object o,int stateChange){
        jpage.fireChangeEvent(o,stateChange);
    }
}
