/*
 * JPaint.java
 *
 * Created on 2007/08/15, 8:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package jpaint;

import java.awt.Color;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.io.IOException;
import java.io.Serializable;
import java.util.Vector;
import jobject.JLeaf;
import jobject.JPathObject;

/**
 *JPaint�N���X�́A�P��̐F�A���`�����ꂽ�O���f�[�V�������͕��ˏ�̃O���f�[�V����
 * �œh��Ԃ���i��񋟂��܂��B
 * @author TI
 */
public class JPaint implements Serializable, Paint, Cloneable {

    /**�P��F�ɂ��h���\���܂�.*/
    public static final int COLOR_MODE = 0;
    /**���`�O���f�[�V�����ɂ��h���\���܂��B*/
    public static final int LINEAR_GRADIENT_MODE = 1;
    /**���ˏ�O���f�[�V�����ɂ��h���\���܂�.*/
    public static final int RADIAL_GRADIENT_MODE = 2;
    /**�p�^�[���ɂ��h���\���܂�.*/    
    public static final int PATTERN_MODE = 3;
    private int paintMode = COLOR_MODE;
    private Color color = null;
    private transient MultipleGradientPaint gradient = null;
    private transient JPatternPaint patternPaint = null;
    private float startX = 0,  startY = 0,  endX = 0,  endY = 0;
    private float[] fractions = null;
    private Color[] colors = null;
    private Rectangle2D.Float clipObject = null;
    private Vector<JLeaf> patternObjects = null;
    private static final long serialVersionUID = 110l;

    /**�J���[�y�C���g���[�h�A�h��FWhite��JPaint���\�z���܂��B
     */
    public JPaint() {
        this(Color.WHITE);
    }

    /**�w�肵��Color����JPaint���\�z���܂�.
     *@param c �w�肷��Color;
     */
    public JPaint(Color c) {
        setPaintColor(c);
    }

    /**�w�肵��MultipleGradientPaint����JPaint���\�z���܂�.
     *@param mg �w�肷��MultipleGradientPaint*/
    public JPaint(MultipleGradientPaint mg) throws Exception {
        float sx, sy, ex, ey;
        if (mg instanceof LinearGradientPaint) {
            LinearGradientPaint lg = (LinearGradientPaint) mg;
            int pmode = LINEAR_GRADIENT_MODE;
            Point2D sp = lg.getStartPoint();
            Point2D ep = lg.getEndPoint();
            setGradient(pmode, (float) sp.getX(), (float) sp.getY(), (float) ep.getX(), (float) ep.getY(), lg.getFractions(), lg.getColors());            
        } else {
            RadialGradientPaint rd = (RadialGradientPaint) mg;
            int pmode = RADIAL_GRADIENT_MODE;
            Point2D sp = rd.getFocusPoint();
            float rad = rd.getRadius();
            setGradient(pmode, (float) sp.getX(), (float) sp.getY(), (float) sp.getX() + rad, (float) sp.getY(), mg.getFractions(), mg.getColors());
        }
        
    }

    /**�w�肵���p�����[�^��Linear����Radial�̃O���f�[�V������Ԃ�JPaint���\�z���܂��B
     *@param pMode LINEAR_GRADIENT_MODE����RADIAL_GRADIENT_MODE
     *@param sX �J�n�_��X���W
     *@param sY �J�n�_��Y���W
     *@param eX �I���_��X���W
     *@param eY �I���X��Y���W
     *@param fracs 0.0 ? 1.0 �͈̔͂̐��l�B �O���f�[�V�����ł̐F���z���w�肷��
     *@param cols �e�����l�ɑΉ�����F�̔z��
     */
    public JPaint(int pMode,
            float sX,
            float sY,
            float eX,
            float eY,
            float[] fracs,
            Color[] cols) throws Exception {
        setGradient(pMode, sX, sY, eX, eY, fracs, cols);        
    }

    /**
     * �w�肵���N���b�v�y�уp�^�[���Ńp�^���̓h���Ԃ�JPaint�I�u�W�F�N�g���\�z���܂��B
     * @param clip �N���b�v�G���A������JPathObject;
     * @param pattern �h��̃p�^�[��
     */    
    public JPaint(Rectangle2D clip, Vector<JLeaf> pt) {
        setPattern(clip, pt);
    }
    public JPaint(JPatternPaint ppt){
        this.patternPaint=ppt;
        paintMode=PATTERN_MODE;
    }
    public void setPattern(Rectangle2D clip, Vector<JLeaf> pt) {
        patternPaint = new JPatternPaint(clip, pt);
        clipObject = new Rectangle2D.Float((float)clip.getX(),(float)clip.getY(),(float)clip.getWidth(),(float)clip.getHeight());
        patternObjects = new Vector<JLeaf>();
        for (JLeaf jl : pt) {
            patternObjects.add(jl);
        }
        paintMode = PATTERN_MODE;
    }

    public Rectangle2D getClip() {
        if (patternPaint == null) {
            return null;
        }
        return patternPaint.getClip();
    }

    public Vector<JLeaf> getPatternObjcets() {
        if (patternPaint == null) {
            return null;
        }
        return patternPaint.getPattern();
    }

    public JPatternPaint getPatternPaint() {
        return patternPaint;
    }

    /**�w�肵��Color��h��F�ɐݒ肵�܂��B
     *@param c �w�肷��h��F
     */
    public void setPaintColor(Color c) {
        color = c;
        paintMode = COLOR_MODE;
    }

    /**�w�肵���p�����[�^��Linear����Radial�̃O���f�[�V������JPaint�ɐݒ肵�܂��B
     *@param pMode LINEAR_GRADIENT_MODE����RADIAL_GRADIENT_MODE
     *@param sX �J�n�_��X���W
     *@param sY �J�n�_��Y���W
     *@param eX �I���_��X���W
     *@param eY �I���X��Y���W
     *@param fracs 0.0 ~ 1.0 �͈̔͂̐��l�B �O���f�[�V�����ł̐F���z���w�肷��
     *@param cols �e�����l�ɑΉ�����F�̔z��
     */
    public void setGradient(
            int pMode,
            float sX,
            float sY,
            float eX,
            float eY,
            float[] fracs,
            Color[] cols) throws Exception {
        if (pMode != LINEAR_GRADIENT_MODE && pMode != RADIAL_GRADIENT_MODE) {
            throw new Exception("PaintMode must be LINEAR_GRADIENT_MODE or RADIAL_GRADIENT_MODE)");            
        }
        startX = sX;
        startY = sY;
        endX = eX;
        endY = eY;
        fractions = fracs;
        colors = cols;
        if (pMode == LINEAR_GRADIENT_MODE) {
            
            gradient = new LinearGradientPaint(startX, startY, endX, endY, fractions, colors, LinearGradientPaint.CycleMethod.NO_CYCLE);
        } else {
            float dx = endX - startX;
            float dy = endY - startY;
            float radius = (float) (Math.sqrt(dx * dx + dy * dy));
            gradient = new RadialGradientPaint(startX, startY, radius, fractions, colors, RadialGradientPaint.CycleMethod.NO_CYCLE);
        }
        paintMode = pMode;
        this.color = null;
    }

    /**�O���f�[�V��������ʒu��v�f�Ƃ���z��̃R�s�[��Ԃ��܂�
     *@return �O���f�[�V��������ʒu��v�f�Ƃ���z��
     */
    public float[] getFracs() {
        float[] ret = new float[fractions.length];
        for (int i = 0; i < fractions.length; i++) {
            ret[i] = fractions[i];
        }
        return ret;
    }

    /**����ʒu��̃J���[��\���z��̃R�s�[��Ԃ��܂�.
     *@return ����ʒu��̃J���[��v�f�Ƃ���z��
     */
    public Color[] getColors() {
        Color[] ret = new Color[colors.length];
        for (int i = 0; i < colors.length; i++) {
            ret[i] = colors[i];
        }
        return ret;
    }

    /**���݂̓h��F��Ԃ��܂�.
     *@return ���݂̓h��F
     */
    public Color getColor() {
        return color;
    }

    /**
     * ����JPaint���O���f�[�V�����̏ꍇ�A�L����MultipleGradientPaint��Ԃ��܂��B
     * @return�@�L����MultipleGradientPaint,�L����GradientPaint���Ȃ��ꍇnull
     */
    public MultipleGradientPaint getGradient() {
        return gradient;
    }

    /**�O���f�[�V�����̊J�n�ʒu����яI���ʒu�̍��W��v�f�Ƃ���z���Ԃ��܂��B
     *@return �O���f�[�V�����̊J�n�ʒu�y�яI���ʒu��v�f�Ƃ���z��
     * �z��v�f�̓��e�͎��̂Ƃ���ł��B<br>
     *[0] �J�n�ʒu��X���W<br>
     *[1] �J�n�ʒu��Y���W<br>
     *[2] �I���ʒu��X���W<br>
     *[3] �I���ʒu��Y���W
     */
    public float[] gradientPoints() {
        return new float[]{
                    startX, startY, endX, endY
                };
    }

    /**���݂̃y�C���g���[�h��Ԃ��܂�
     *@return ���݂̃y�C���g���[�h.LINEAR_GRADIENT_MODE����RADIAL_GRADIENT_MODE
     */
    public int getPaintMode() {
        return paintMode;
    }
    public Paint getPaint(){
        if (paintMode==COLOR_MODE)
            return color;
        else if (paintMode==PATTERN_MODE)
            return patternPaint;
        else
            return gradient;
    }
    @Override
    public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform xform, RenderingHints hints) {
        if (paintMode == COLOR_MODE) {
            return color.createContext(cm, deviceBounds, userBounds, xform, hints);
        }else if(paintMode==LINEAR_GRADIENT_MODE || paintMode==RADIAL_GRADIENT_MODE){
        return gradient.createContext(cm, deviceBounds, userBounds, xform, hints);
        }else if(paintMode==PATTERN_MODE){
            return patternPaint.createContext(cm, deviceBounds, userBounds, xform, hints);
        }
        return null;
    }    
    @Override
    public int getTransparency() {
        if (paintMode == COLOR_MODE) {
            return color.getTransparency();
        }else if (paintMode== PATTERN_MODE){
            return patternPaint.getTransparency();
        }
        return gradient.getTransparency();
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException, Exception {
        gradient = null;
        patternPaint=null;
        in.defaultReadObject();
        if (paintMode == LINEAR_GRADIENT_MODE || paintMode==RADIAL_GRADIENT_MODE) {
            setGradient(paintMode, startX, startY, endX, endY, fractions, colors);
        }else if (paintMode==PATTERN_MODE){
            setPattern(clipObject,patternObjects);
        }
    }

    /**
     * �w�肳�ꂽJPaint������JPaint�Ɠ������ꍇ��true��Ԃ��܂�.
     * @param jp �w�肷��JPaint
     * @return �w�肳�ꂽJPaint�Ƃ���JPaint���������ꍇtrue,����ȊOfalse;
     */
    public boolean equals(JPaint jp) {
        if (jp == null) {
            return false;
        }
        if (this==jp)
            return true;
        if (paintMode != jp.paintMode) {
            return false;
        }
        if (paintMode == COLOR_MODE) {
            if (color == null) {
                if (jp.color != null) {
                    return false;
                }
            } else {
                return (color.equals(jp.color));
            }
        } else  if (paintMode==LINEAR_GRADIENT_MODE || paintMode==RADIAL_GRADIENT_MODE){
            if (fractions.length != jp.fractions.length) {
                return false;
            }
            for (int i = 0; i < fractions.length; i++) {
                if (fractions[i] != jp.fractions[i]) {
                    return false;
                }
                if (!colors[i].equals(jp.colors[i])) {
                    return false;
                }
            }
        } else if (paintMode==PATTERN_MODE){
            if (clipObject.equals(jp.clipObject)){
                if (patternObjects.size() !=jp.patternObjects.size()) 
                    return false;
                for (int i=0;i<patternObjects.size();i++){
                    if (!patternObjects.get(i).equals(jp.getPatternObjcets().get(i))){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * JPaint�Ɏw�肷��Affine�ϊ��������܂��B
     * @param tx �w�肷��AffineTransform
     */
    public void transform(AffineTransform tx) {
        if (paintMode == COLOR_MODE || paintMode==PATTERN_MODE) {
            return;
        }
        Point2D.Double p1 = new Point2D.Double(startX, startY);
        Point2D.Double p2 = new Point2D.Double(endX, endY);
        tx.transform(p1, p1);
        tx.transform(p2, p2);
        try {
            setGradient(paintMode, (float) p1.x, (float) p1.y, (float) p2.x, (float) p2.y, fractions, colors);
        } catch (Exception e) {
        }
    }

    /**
     * �O���f�[�V�����̊J�n�ʒu��Ԃ��܂�.
     * @return �O���f�[�V�����̊J�n�ʒu
     */
    public Point2D.Float getP1() {
        return new Point2D.Float(startX, startY);
    }

    /**
     * �O���f�[�V�����̏I���ʒu��Ԃ��܂�.
     * @return �O���f�[�V�����̏I���ʒu
     */
    public Point2D.Float getP2() {
        return new Point2D.Float(endX, endY);
    }

    @Override
    public JPaint clone() {
        JPaint ret = new JPaint();
        if (this.paintMode == COLOR_MODE) {
            ret.setPaintColor(this.color);
        } else  if (paintMode==LINEAR_GRADIENT_MODE || paintMode==RADIAL_GRADIENT_MODE){
            Color[] cols = getColors();
            float[] frs = getFracs();
            try {
                ret.setGradient(paintMode, startX, startY, endX, endY, frs, cols);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }else {
           ret.clipObject=(Rectangle2D.Float)clipObject.clone();
            ret.patternObjects=patternObjects;
            ret.paintMode=paintMode;
            ret.patternPaint=new JPatternPaint(ret.clipObject,ret.patternObjects);
        }
        return ret;
    }
}
