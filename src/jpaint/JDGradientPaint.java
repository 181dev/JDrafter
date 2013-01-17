/*
 * JDGradientPaint.java
 *
 * Created on 2007/02/09, 19:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package jpaint;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

/**
 *JDGradientPaint�́A�J���[�̐��`�y�ѕ��ˏ�O���f�[�V�����p�^�[����Shape��h��Ԃ���i��񋟂��܂�.
 * @author TK
 */
public class JDGradientPaint implements Paint,Transparency,Cloneable{
    /**���`�̃O���f�[�V�����p�^�[����\�����܂�.*/
    public static final int LINEAR=0;
    /**���ˏ�̃O���f�[�V�����p�^�[����\�����܂�.*/
    public static final int RADIAL=1;
    Point2D p1,p2;
    Color c1,c2;
    float[] controlPoints;
    Color[] colors;
    int gType;
    ColorModel colorModel;
    float dx,dy,distance;
    float dr,dg,db,da;
    float[] dp=new float[4];
    float[] startC=new float[4];
    float[] endC=new float[4];
 
    
    /** �w�肵��������JDGradientPaint���\�z���܂�.
     *@param p1 ���[�U�[��Ԃōŏ��Ɏw�肳�ꂽPoint<br>
     *p2 ���[�U�[��Ԃ�2�ԖڂɎw�肳�ꂽPoint<br>;
     *c1 �|�C���gp1�̃J���[<br>
     *c2 �|�C���gp2�̃J���[<br>
     *cp p1����p2�Ԃ̐���_
     *cols ����_�̃J���[
     *gTyp �h��̃^�C�v
     */
    public JDGradientPaint(Point2D p1,Point2D p2,Color c1,Color c2,float[] cp,Color[] cols,int gType) {
        this.p1=p1;
        this.p2=p2;
        this.c1=c1;
        this.c2=c2;
        this.controlPoints=cp;
        this.colors=cols;
        this.gType=gType;
        dx=(float)(p2.getX()-p1.getX());
        dy=(float)(p2.getY()-p1.getY());
        distance=(float)Math.sqrt(dx*dx+dy*dy);
        startC=new float[4];
        endC=new float[4];
        startC=c1.getComponents(startC);
        endC=c2.getComponents(endC);
    }
    /** �w�肵��������JDGradientPaint���\�z���܂�.
     *@param p1 ���[�U�[��Ԃōŏ��Ɏw�肳�ꂽPoint<br>
     *p2 ���[�U�[��Ԃ�2�ԖڂɎw�肳�ꂽPoint<br>;
     *c1 �|�C���gp1�̃J���[<br>
     *c2 �|�C���gp2�̃J���[<br>
     *gTyp �h��̃^�C�v
     */
    public JDGradientPaint(Point2D p1,Point2D p2,Color c1,Color c2,int gType){
        this(p1,p2,c1,c2,null,null,gType);
    }
    /**�w�肵��������JDGradientPaint���\�z���܂�.
     */
    public JDGradientPaint(float x1,float y1,float x2,float y2,Color c1,Color c2,int gType){
        this(x1,y1,x2,y2,c1,c2,null,null,gType);
    }
    public JDGradientPaint(float x1,float y1,float x2,float y2,Color c1,Color c2,float[] ctrl,Color[] cols,int gType){
        this(new Point2D.Float(x1,y1),new Point2D.Float(x2,y2),c1,c2,ctrl,cols,gType);
    }
    public Point2D getPoint1(){
        return this.p1;
    }
    public Point2D getPoint2(){
        return this.p2;
    }
    public Color getColor1(){
        return this.c1;
    }
    public Color getColor2(){
        return this.c2;
    }
    public float[] getControls(){
        return this.controlPoints;
    }
    public Color[] getColors(){
        return this.colors;
    }
    public int getGradientType(){
        return this.gType;
    }
    public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform xform, RenderingHints hints) {
        return new JDGradientPaintContext(cm, p1, p2, xform,
					c1, c2,controlPoints,colors,gType);
    }
    public int getTransparency() {
	int a1 = c1.getAlpha();
	int a2 = c2.getAlpha();
	return (((a1 & a2) == 0xff) ? OPAQUE : TRANSLUCENT);
    }
    /**����JDGradientPaint�ƑΏ̂��r���܂�.
     *�@�R���g���[���|�C���g�ɂ��ẮADragger�ɂ��Transform����Ă���\�������邽�߁A��r�Ώ̂ɂ͊܂߂܂���.
     *@return ��r����
     */
    public boolean equals(Object o){
        if (!(o instanceof JDGradientPaint)) return false;
        JDGradientPaint tg=(JDGradientPaint) o;
//        if (!this.getPoint1().equals(tg.getPoint1())) return false;
//        if (!this.getPoint2().equals(tg.getPoint2())) return false;
        if (!this.getColor1().equals(tg.getColor1())) return false;
        if (!this.getColor2().equals(tg.getColor2())) return false;
        if (this.colors !=null){
            if (tg.getColors()==null) return false;
            if (tg.getColors().length != this.colors.length) return false;
            for (int i=0;i<this.colors.length;i++){
                if (!this.colors[i].equals(tg.getColors()[i])) return false;
            }
        }else if (tg.getColors()!=null){
            return false;
        }
        if (this.controlPoints != null){
            if (tg.getControls()==null) return false;
            if (tg.getControls().length != this.controlPoints.length) return false;
            for (int i=0;i<this.controlPoints.length-1;i++){
                if (this.getControls()[i] != tg.getControls()[i])
                    return false;
            }
        }else if (tg.getControls()!=null){
            return false;
        }
        return true;
    }
    /**JDGradientPaint�̕������쐬���܂�.*/
    public JDGradientPaint clone(){
        return new JDGradientPaint((Point2D)p1.clone(),(Point2D)p2.clone(),
                c1,c2,controlPoints.clone(),colors.clone(),gType);
    }
}
