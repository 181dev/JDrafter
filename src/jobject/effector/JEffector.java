/*
 * JEffector.java
 *
 * Created on 2007/12/23, 18:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package jobject.effector;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import jobject.JLeaf;
import jobject.text.TextLocater;
import jpaint.JPaint;
import jpaint.JStroke;

/**
 * �`��I�u�W�F�N�g�̃G�t�F�N�g�̃|���V�[������킵�܂��B
 * @author takashi
 */
public interface JEffector extends Cloneable, Serializable {
    /**
     * �w���Shape��`�悵�܂��B
     * @param g �O���t�B�b�N�X�R���e�L�X�g
     * @param s �`�悷��Shape
     * @param fillPaint �h��Ԃ��̂��s��Paint
     * @param border ���E��`�悷��Paint
     * @param stroke ���E�̐���
     */
    public void paint(Graphics2D g, Shape s, JPaint fillPaint, JPaint border, JStroke stroke);
     /**
     * �w���TextObject��`�悵�܂��B
     * @param g �O���t�B�b�N�X�R���e�L�X�g
     * @param s �`�悷��Shape
     * @param fillPaint �h��Ԃ��̂��s��Paint
     * @param border ���E��`�悷��Paint
     * @param stroke ���E�̐���
     */   
    public void paintText(Graphics2D g,TextLocater locater,AffineTransform tx,JPaint fillPaint,JPaint border,JStroke stroke);
    /**
     * �w�肷��f�t�H���g�̕`��̈���g�����A����JEffector�̕`��̈�܂Ŋg�����܂��B
     * @param r �w�肷��f�t�H���g�̕`��̈�
     * @return ����JEffector�ɂ��g�����ꂽ�`��̈�
     */
    public Rectangle2D culcBounds(Rectangle2D r,JLeaf jl);
   /**
    * ����JEffector�̕������쐬���܂�.
    * @return
    */
    public JEffector clone();
}
