/*
 * JStroke.java
 *
 * Created on 2007/08/16, 20:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package jpaint;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;
import java.io.IOException;
import java.io.Serializable;

/**
 *���񉻉\��stroke�ł�
 * @author TI
 */
public class JStroke implements Stroke,Serializable{
    private float width=1;
    private int cap=BasicStroke.CAP_SQUARE;
    private int join=BasicStroke.JOIN_MITER;
    private float miterLimit=10.0f;
    private float[] dash=null;
    private float dash_phase=0;
    private transient BasicStroke stroke=null;
    private static final long serialVersionUID=110l;
    /**���ׂĂ̑����Ƀf�t�H���g�l���g���ĐV���� JStroke ���\�z���܂��B
     * �f�t�H���g�̑����́A�����̕� 1.0�ACAP_SQUARE�AJOIN_MITER�A
     * �g���~���O�����l 10.0 �ł��B 
     */
    public JStroke() {
        width=1;
        cap=BasicStroke.CAP_SQUARE;
        join=BasicStroke.JOIN_MITER;
        miterLimit=10.0f;
        dash=null;
        dash_phase=0;
        stroke=new BasicStroke(width,cap,join,miterLimit,dash,dash_phase);
    }
    /**�w�肳�ꂽ���������V���� BasicStroke ���\�z���܂�]
     *@param w ���� BasicStroke �̕��B�l�� 0.0f �ȏ�łȂ���΂Ȃ�Ȃ��B���� 0.0f �ɐݒ肳��Ă���ꍇ�A �X�g���[�N�͑Ώۂ̃f�o�C�X��̂����Ƃ��ׂ����C���Ƃ��ĕ`�悳���B �܂��A���̂Ƃ��A���`�G�C���A�X�ݒ肪�g�p�����
     *@param cp - BasicStroke �̗��[�̑���
     *@param jn - �֊s���Z�O�����g�̐ڍ����̑���
     *@param mLimit - �ڍ��g���~���O�̐����l�Bmiterlimit �� 1.0f �ȏ�łȂ���΂Ȃ�Ȃ�
     *@param dsh - �j���p�^�[����\���z��
     *@param dphase - �j���p�^�[���J�n�ʒu�̃I�t�Z�b�g 
     */
    public JStroke(float w,int cp,int jn,float mLimit,float[] dsh,float dphase){
        width=w;
        cap=cp;
        join=jn;
        miterLimit=mLimit;
        dash=dsh;
        dash_phase=dphase;
        stroke=new BasicStroke(width,cap,join,miterLimit,dash,dash_phase);
    }
    /**�w�肳�ꂽBasicStroke�̑������R�s�[����JStroke���\�z���܂�*/
    public JStroke(BasicStroke stroke){
        this(stroke.getLineWidth(),stroke.getEndCap(),stroke.getLineJoin(),
                stroke.getMiterLimit(),stroke.getDashArray(),stroke.getDashPhase());
    }
    /**������Ԃ��܂�.*/
    public float getWidth(){
        return width;
    }
    /**���̗��[�̑�����Ԃ��܂�.*/
    public int getEndCap(){
        return cap;
    }
    /**�֊s���Z�O�����g�̐ڍ����̑�����Ԃ��܂�.*/
    public int getLineJoin(){
        return join;
    }
    /**�ڍ��g���~���O�̐����l��Ԃ��܂�.*/
    public float getMiterLimit(){
        return miterLimit;
    }
    /**�j���p�^�[����\���z��̃R�s�[��Ԃ��܂�.*/
    public float[] getDashArray(){
        if (dash==null)
            return null;
        return dash.clone();
    }
    /**�j���p�^�[���J�n�ʒu�̃I�t�Z�b�g��Ԃ��܂�.*/
    public float getDashPhase(){
        return dash_phase;
    }
    /**
     *�w�肳�ꂽ Shape ���X�g���[�N�ŕ`�悵���֊s��\������������ Shape ��Ԃ��܂�.
     * @param p  �X�g���[�N�ŕ`�悳��� Shape �̋��E 
     */
    public Shape createStrokedShape(Shape p) {
        return stroke.createStrokedShape(p);
    }
    /**���݂̃X�g���[�N��Ԃ��܂�.*/
    public BasicStroke getStroke(){
        return stroke;
    }
    public boolean equals(JStroke tg){
        if (tg==null) return false;
        if (width !=tg.width) return false;
        if(cap !=tg.cap) return false;
        if (join != tg.join) return false;
        if (join == BasicStroke.JOIN_MITER && miterLimit !=tg.miterLimit) return false;
        if (dash != null){
            if (tg.dash==null) return false;
            if (dash.length != tg.dash.length) return false;
            for (int i=0;i<dash.length;i++){
                if (dash[i] !=tg.dash[i]) return false;
            }
            if (dash_phase != tg.dash_phase) return false;
        }else{
            if (tg.dash !=null) return false;
        }
        return true;
    }
    public JStroke clone(){
        return new JStroke(width,cap,join,miterLimit,getDashArray(),dash_phase);
    }
    private void readObject(java.io.ObjectInputStream in)
     throws IOException, ClassNotFoundException
     {
         stroke=null;
         in.defaultReadObject();
         stroke=new BasicStroke(width,cap,join,miterLimit,dash,dash_phase);
     }
    
}
