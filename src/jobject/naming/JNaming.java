/*
 * JNaming.java
 *
 * Created on 2008/05/05, 8:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package jobject.naming;

import java.util.regex.Pattern;

/**
 *�����������J�j�Y���ɂ�鏇���t����ݒ肵�܂�.
 * @author takashi
 */
public class JNaming implements Comparable<JNaming>{
    private String prefixer;
    private int index;
    private static final String regex=".*\\D\\d{1,8}\\z";
    private static final String indexer="\\d{1,8}\\z"; 
    /**
     * �������w�肵�AJNaming�N���X�̃C���X�^���X���\�z���܂�.
     * @param name �w�肷�銥��. 
     */
    public JNaming(String name) {
        if (Pattern.matches(regex,name)){
            prefixer=name.replaceAll(indexer,"");
            index=Integer.parseInt(name.replace(prefixer,""));
        }else{
            prefixer=name;
            index=0;
        }        
    }
    /**
     * �����y�сA��A�ԍ����w�肵�AJNamin�N���X�̃C���X�^���X���\�z���܂�.
     * @param prefixer �w�肷�銥��.
     * @param index �w�肷���A�ԍ�.
     */
    public JNaming(String prefixer,int index){
        this.prefixer=prefixer;
        this.index=index;
    }
    /**
     * ����JNaming�Ŏw��\�Ȗ��̂̂����ŏ��̃C���X�^���X��Ԃ��܂�.
     * @return
     */
    public JNaming minimumName(){
        return new JNaming(prefixer,-1);
    }
    /**
     * ����JNaming�Ŏw��\�Ȗ��̂̂����ő�̃C���X�^���X��Ԃ��܂��B
     * @return
     */
    public JNaming maximumName(){
        return new JNaming(prefixer,Integer.MAX_VALUE);
    }
    /**
     * ����JNaming�̊�����Ԃ��܂�.
     * @return
     */
    public String getPrefixer(){
        return prefixer;
    }
    /**
     * ����JNaming�̈�A�ԍ���Ԃ��܂�.
     * @return
     */
    public int getIndex(){
        return index;
    }
    /**
     * �w�肳�ꂽJNaming�Ƃ���JNaming���g�̏����t���̔�r���s���܂�.
     * @param o ��r�Ώۂ�JNaming
     * @return �����t���̔�r����
     */
    @Override
    public int compareTo(JNaming o) {
        int result=prefixer.compareTo(o.prefixer);
        if (result !=0) return result;
        return (int)Math.signum(index-o.index); 
    }
    /**
     * ����JNaming�̖��̂�Ԃ��܂�.
     * @return
     */
    @Override
    public String toString(){
        return prefixer+String.valueOf(index).trim();
    }
}
