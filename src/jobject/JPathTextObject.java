/*
 * JPathTextObject.java
 *
 * Created on 2007/10/31, 14:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package jobject;

import jobject.text.JParagraphIterator;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.font.*;
import java.awt.geom.*;
import java.text.AttributedCharacterIterator;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.text.*;
import javax.swing.undo.UndoableEdit;
import jedit.textobjectedit.JPathTextPositionChangeEdit;
import jgeom.*;
import jpaint.*;
import jscreen.JEnvironment;
import jscreen.JRequest;
import jpaint.JStroke;
import jobject.text.OnPathTextLocater;
import jobject.text.TextLocater;
/**
 *�p�X�ɉ������e�L�X�g�̃��C�A�E�g�A�\�������s����I�u�W�F�N�g�ł�.
 *���̃I�u�W�F�N�g�͒P��p�X�I�u�W�F�N�g�݂̂ɑΉ����Ă��܂�.
 * @author T-IKITA
 */
public class JPathTextObject extends JPathObject implements JText,JColorable {
    /**�e�L�X�g�̑����y�ѕ�����ێ�����DefaultStyledDocument*/
    private DefaultStyledDocument document;
    /**�e�L�X�g�̕`��J�n�_�̈ʒu��\���܂�.*/
    private float startPosition=0;
    private float previewPosition=0;
    private static final long serialVersionUID=110l;
    /**�����̊J�n�_�𐧌䂷��R���g���[���|�C���g�ł��B
     *       ��control1<br>
     *       ��<br>
     *-------��TEXT<br>
     *       ��control2<br>
     */
    private transient JSegment control1=null,control2=null;
    private transient GeneralPath lineShape=null;
    private transient GeneralPath previewShape=null;
    
    /** �f�t�H���g�̑�����JPathTextObject���\�z���܂�. */
    public JPathTextObject() {
        super(JEnvironment.currentTextFill,JEnvironment.currentTextBorder,JEnvironment.currentTextStroke);
        document=new DefaultStyledDocument();
    }
    /**�w�肵���h��A����y�ѐ��F�ɂ��JPathTextObject���\�z���܂�.
     *@param fillpaint �h���paint
     *@param strokepaint ���̐F
     *@param stroke ����
     */
    public JPathTextObject(JPaint fillpaint,JPaint strokepaint,JStroke stroke){
        super(fillpaint,strokepaint,stroke);
        document=new DefaultStyledDocument();
    }
    /***�f�o�b�O�p�̃R���X�g���N�^�[�ł�*/
    public JPathTextObject(String t){
        this();
        JPathIterator it=new JPathIterator(new Ellipse2D.Double(100,100,100,100).getPathIterator(null));
        this.setPath(it.getJPath());
        SimpleAttributeSet attr=new SimpleAttributeSet();
        try{
            document.insertString(0,t,attr);
        }catch(Exception ex){}
        setStartPosition(0.2f);
    }
    /**�p�X�̒����̋ߎ��l��Ԃ��܂�.
     *@param path �����𑪒肷��JSimplePath
     *@param flatness �p�X�̕������W��
     *@return path�̒���
     */
    public static float getPathLength(JSimplePath path,float flatness){
        float ret=0f;
        PathIterator pt=path.getShape(PathIterator.WIND_NON_ZERO).getPathIterator(null,flatness);
        float[] coords=new float[6];
        float ox=0,oy=0,bx=0,by=0;
        while (!pt.isDone()){
            int type=pt.currentSegment(coords);
            if (type==PathIterator.SEG_MOVETO){
                bx=ox=coords[0];
                by=oy=coords[1];
            }
            if (type==PathIterator.SEG_LINETO){
                float dx=coords[0]-ox;
                float dy=coords[1]-oy;
                ret +=(float)Math.sqrt(dx*dx+dy*dy);
                ox=coords[0];
                oy=coords[1];
            }
            if (type==PathIterator.SEG_CLOSE){
                float dx=bx-ox;
                float dy=by-oy;
                ret +=(float)Math.sqrt(dx*dx+dy*dy);
                ox=coords[0];
                oy=coords[1];
            }
            pt.next();
        }
        return ret;
    }
    /**�p�X���w�肵�����Έʒu�ŕ������A���ʂ𒸓_��v�f�Ƃ���Vector��Ԃ��܂�.
     *@param path ��������Path
     *@param rpos  �������Έʒu
     *@param flatness �p�X�̕������W��
     *@return �������ʂ̃p�X�̒��_��v�f�Ƃ���Vector
     */
    public static Vector<Point2D> dividePathRelatively(JSimplePath path,float rpos,float flatness){
        float pos=getPathLength(path,flatness)*rpos;
        return dividePath(path,pos,flatness);
        
    }
    
    /**�p�X���w��ʒu�ŕ������A���ʂ𒸓_��v�f�Ƃ���Vector�ŕԂ��܂�.
     *@param path�@��������p�X
     *@param pos ��������p�X�̎n�_����̐�Έʒu
     *@param flatness �p�X�̕������W��.
     *@return �������ʂ̃p�X�̒��_��v�f�Ƃ���Vector
     */
    public static Vector<Point2D> dividePath(JSimplePath path,float pos,float flatness){
        Vector<Point2D> result=new Vector<Point2D>(1);
        Vector<Point2D> saved=new Vector<Point2D>(1);
        PathIterator pt=path.getShape(PathIterator.WIND_NON_ZERO).getPathIterator(null,flatness);
        float[] coords=new float[6];
        float sx=0,sy=0,px=0,py=0;
        boolean isStarted=false;
        boolean isLooped=false;
        while (!pt.isDone()){
            int type=pt.currentSegment(coords);
            if (type==PathIterator.SEG_MOVETO){
                sx=px=coords[0];
                sy=py=coords[1];
                saved.add(new Point2D.Float(px,py));
            }
            if (type==PathIterator.SEG_LINETO){
                if (! isStarted){
                    float dx=coords[0]-px;
                    float dy=coords[1]-py;
                    if (dx==0 && dy==0){
                        pt.next();
                        continue;
                    }
                    float dst=(float)Math.sqrt(dx*dx+dy*dy);
                    if (dst>pos){
                        float x=px+dx*pos/dst;
                        float y=py+dy*pos/dst;
                        result.add(new Point2D.Float(x,y));
                        saved.add(new Point2D.Float(x,y));
                        result.add(new Point2D.Float(coords[0],coords[1]));
                        px=coords[0];
                        py=coords[1];
                        isStarted=true;
                    }else{
                        pos -=dst;
                        saved.add(new Point2D.Float(coords[0],coords[1]));
                        px=coords[0];
                        py=coords[1];
                    }
                } else{
                    if (px!=coords[0] || py!=coords[1]){
                        result.add(new Point2D.Float(coords[0],coords[1]));
                    }
                    px=coords[0];
                    py=coords[1];
                }
            }
            if (type==PathIterator.SEG_CLOSE){
                isLooped=true;
                if (px!=sx || py!=sy){
                    result.add(new Point2D.Float(sx,sy));
                }
            }
            pt.next();
        }
        if (isLooped){
            for (int i=0;i<saved.size();i++){
                result.add(saved.get(i));
            }
        }
        Point2D prep=null;
        for (int i=0;i< result.size();i++){
            if (prep==null){
                prep=result.get(i);
            }else{
                if (prep.equals(result.get(i))){
                    result.remove(i--);
                }else{
                    prep=result.get(i);
                }
                
            }           
        }
        return result;
    }
    /**���p�`�̒��_��v�f�Ƃ���Vector����e�L�X�g�n�_�ړ��p�̃R���g���[���̈ʒu�����肵�܂�.
     *@param poly ���p�`�̒��_��v�f�Ƃ���Vector
     *@retrurn �ʒu���萬���̏ꍇtrue,����ȊO��false
     */
    private boolean setConrolPos(Vector<Point2D> poly,FontRenderContext frc){
        if (poly.size()<2) return false;
        Point2D sp=poly.get(0);
        Point2D ep=null;
        for (int i=1;i<poly.size();i++){
            if (!poly.get(i).equals(sp)){
                ep=poly.get(i);
                break;
            }
        }
        if (ep==null) return false;
        LineMetrics fm=document.getFont(document.getCharacterElement(0).getAttributes()).getLineMetrics(" ",frc);
        float dx=(float)(ep.getX()-sp.getX()),dy=(float)(ep.getY()-sp.getY());
        float dst=(float)(Math.sqrt(dx*dx+dy*dy));
        if (control1==null){
            control1=new JSegment();
            control2=new JSegment();
        }
        control1.setAncur(sp.getX()+dy*fm.getAscent()/dst,sp.getY()-dx*fm.getAscent()/dst);
        control2.setAncur(sp.getX()-dy*fm.getDescent()/dst,sp.getY()+dx*fm.getDescent()/dst);
        return true;
    }
    /**�w�肷��_�ɍł��߂�Path��̓_��T�����A���Y�p�X��̓_�̃p�X�n�_����̑��΋�����Ԃ��܂�.
     *
     *@param path �����ΏۂƂȂ�Path
     *@param x �w��_��X���W
     *@param y �w��_��Y���W
     *@param flatness �p�X�̕������W��
     *@return �p�X�����_�̃p�X�n�_����̋���
     */
    public static float dividePathPt(JSimplePath path,float x,float y,float flatness){
        float minLen=Float.MAX_VALUE;
        int idx=-1;
        PathIterator pt=path.getShape(PathIterator.WIND_NON_ZERO).getPathIterator(null,flatness);
        float[] coords=new float[6];
        float preX=0,preY=0,sX=0,sY=0;
        float totalDist=0;
        float cDist=0;
        while (!pt.isDone()){
            int type=pt.currentSegment(coords);
            if (type==pt.SEG_MOVETO){
                sX=preX=coords[0];
                sY=preY=coords[1];
            }
            if (type==pt.SEG_LINETO){
                float d=(float)Line2D.ptSegDist(preX,preY,coords[0],coords[1],x,y);
                if (d<minLen){
                    cDist=totalDist+perpendDistance(preX,preY,coords[0],coords[1],x,y);
                    minLen=d;
                }
                totalDist+=getDistance(preX,preY,coords[0],coords[1]);
                preX=coords[0];
                preY=coords[1];
            }
            if (type==pt.SEG_CLOSE){
                if (preX !=sX || preY != sY){
                    float d=(float)Line2D.ptSegDist(preX,preY,sX,sY,x,y);
                    if (d<minLen){
                        cDist=totalDist+perpendDistance(preX,preY,sX,sY,x,y);
                        minLen=d;
                    }
                    totalDist+=getDistance(preX,preY,sX,sY);
                }
            }
            pt.next();
        }
        if (totalDist==0) return 0;
        return (cDist/totalDist);
    }
    /**�����Ǝw�肵���_��ʂ鐂���̌�_�̍��W��Ԃ��܂��B��_��������ɂȂ��ꍇ�́A���߂̐����̎n�_
     *�܂��́A�I�_�̍��W��Ԃ��܂�.
     *@param x0 �����̎n�_��X���W
     *@param y0 �����̎n�_��y���W
     *@param x1 �����̏I�_��x���W
     *@param y1 �����̏I�_��y���W
     *@param x �������ʂ�_��x���W
     *@param y �������ʂ�_��y���W
     *@return ��_�̍��W
     */
    public static Point2D perpendIntersection(float x0,float y0,float x1,float y1,float x,float y){
        float dx=x1-x0,dy=y1-y0,dx1=x-x0,dy1=y-y0;
        float dst=dx*dx+dy*dy;//����^2
        float inp=dx*dx1+dy*dy1;//����
        float px=dx*inp/dst;
        float py=dy*inp/dst;
        float di=(px*dx+py*dy)/dst;
        if (di>=1 ) return new Point.Float(x1,y1);
        if (di<=0) return new Point.Float(x0,y0);
        return new Point.Float(px+x0,py+y0);
    }
    public static Point2D perpendInterSection(Point2D p0,Point2D p1,Point2D p){
        return perpendIntersection(
                (float)p0.getX(),(float)p0.getY(),
                (float)p1.getX(),(float)p1.getY(),
                (float)p.getX(),(float)p.getY());
    }
    /**2�_�Ԃ̋�����Ԃ��܂�.
     *@param x0 �_0��x���W
     *@param y0 �_0��y���W
     *@param x1 �_1��x���W
     *@param y1 �_1�̂����W
     *@return 2�_�Ԃ̋���
     */
    public static float getDistance(float x0,float y0,float x1,float y1){
        float dx=x1-x0,dy=y1-y0;
        return (float)Math.sqrt(dx*dx+dy*dy);
    }
    /**
     * �w�肷��2�_�Ԃ̋�����Ԃ��܂�.
     * @param p0 �w�肷��1�Ԗڂ�Point2D
     * @param p1 �w�肷��
     * @return 2�_�Ԃ̋���
     */
    public static float getDistance(Point2D p0,Point2D p1){
        return getDistance(
                (float)p0.getX(),(float)p0.getY(),
                (float)p1.getX(),(float)p1.getY());
        
    }
    /**�����Ǝw��_��ʂ鐂���̌�_�̐����̎n�_����̋�����Ԃ��܂�.
     */
    private static float perpendDistance(float x0,float y0,float x1,float y1,float x,float y){
        Point2D p=perpendIntersection(x0,y0,x1,y1,x,y);
        return getDistance(x0,y0,(float)p.getX(),(float)p.getY());
    }
    /**�����t���e�L�X�g���p�X��ɔz�u���A���ʂ̃e�L�X�g�A�E�g���C����GeneralPath�Ƃ��ĕԂ��܂�.
     *@param poly �z�u����p�X�̊e���_��v�f�Ƃ���Vector
     *@param doc �z�u���鑮���t�e�L�X�g���i�[���ꂽDefaultStyledDocument
     *@param frc �`��̂��߂�FontRenderContext
     *@return �e�L�X�g�̔z�u���ʂ��i�[���ꂽStyledDocument;
     */
    public GeneralPath createLineShape(Vector<Point2D> poly,DefaultStyledDocument doc,FontRenderContext frc){
        GeneralPath ret=new GeneralPath();        
        Iterator<Point2D> it= poly.iterator();
        //
        if (frc==null){
            frc=new FontRenderContext(null,true,true);
        }
        //
        if (!it.hasNext()) return ret;
        Point2D preP=it.next();
        Point2D cP=new Point2D.Float();
        cP.setLocation(preP);
        if (!it.hasNext()) return ret;
        Point2D nP=it.next();
        JParagraphIterator pit=new JParagraphIterator(document);
        AttributedCharacterIterator cit=pit.first();
        AffineTransform af=new AffineTransform();
        char[] ch=new char[1];
        outer:while (cit != null){
            char c=cit.first();
            while (c !=cit.DONE){
                ch[0]=c;
                
                TextLayout layout=new TextLayout(new String(ch),cit.getAttributes(),frc);
                float dst=layout.getAdvance();
                float theta=(float)(Math.atan2(nP.getY()-preP.getY(),nP.getX()-preP.getX()));
                af.setToTranslation(cP.getX(),cP.getY());
                af.rotate(theta);
                ret.append(layout.getOutline(af),false);
                float X=(float)(nP.getX()-cP.getX()),Y=(float)(nP.getY()-cP.getY());
                float pdst=(float)Math.sqrt(X*X+Y*Y);
                while (dst>pdst){
                    if (!it.hasNext()) break outer;
                    preP=nP;
                    nP=it.next();
                    dst-=pdst;
                    cP.setLocation(preP);
                    X=(float)(nP.getX()-cP.getX());Y=(float)(nP.getY()-cP.getY());
                    pdst=(float)Math.sqrt(X*X+Y*Y);
                    
                }
                float t=dst/pdst;
                cP.setLocation((nP.getX()-cP.getX())*t+cP.getX(),(nP.getY()-cP.getY())*t+cP.getY());
                c=cit.next();
            }
            cit=pit.next();
        }
        return ret;
    }
    /**�e�L�X�g�̃X�^�[�g�|�W�V�����̑��Έʒu���w�肵�܂��B
     * @param sp �w�肷��e�L�X�g�J�n�ʒu�̑��Έʒu(0f����1f)
     */
    public void setStartPosition(float sp){
        startPosition=previewPosition=sp;
        previewShape=null;
        lineShape=null;
    }
    /**
     * ���݂̃e�L�X�g�̃X�^�[�g�|�W�V�����̈ʒu��Ԃ��܂��B
     * @return ���݂̃e�L�X�g�̊J�n�ʒu(0f����1f)
     */
    public float getStartPosition(){
        return startPosition;
    }
    /**
     * Point2D�Ŏw�肷��ʒu�Ƀq�b�g����A���̃I�u�W�F�N�g�̕�����Ԃ��܂�.
     * @param env �L���Ȋ����i�[����JEnvironment�I�u�W�F�N�g
     * @param req ���݂̑I����Ԃ��i�[����JRequest�I�u�W�F�N�g
     * @param p �q�b�g����������ʒu.
     * @return ���،��ʂ�����int(JRequest.HIT_NON,JRequest.HIT_OBJECT����JRequest.HIT_ANCUR)
     */
    @Override
    public int hitByPoint(JEnvironment env,JRequest req,Point2D p){
        if (isLocked() || !isVisible()) return JRequest.HIT_NON;
        if (req.getSelectionMode()==JRequest.DIRECT_MODE && control1 !=null ){
            double radius=JEnvironment.PATH_SELECTOR_SIZE;
            Rectangle2D.Double rect=new Rectangle2D.Double(0,0,radius,radius);
            rect.x=control1.getAncur().getX()-radius/2;
            rect.y=control1.getAncur().getY()-radius/2;
            if (rect.contains(p)){
                req.hitResult=JRequest.HIT_ANCUR;
                req.hitObjects.add(control1);
                req.hitObjects.add(this);
                return JRequest.HIT_ANCUR;
            }
        }
        int ret=super.hitByPoint(env,req,p);
        if (ret==JRequest.HIT_OBJECT){
            ret=req.hitResult=JRequest.HIT_NON;
            req.hitObjects.clear();          
        }
        if (ret==JRequest.HIT_NON){
            double rad=env.getToScreenRatio();
            Rectangle2D r=new Rectangle2D.Double(p.getX()-rad,p.getY()-rad,rad*2,rad*2);
            if (lineShape !=null){
                if (lineShape.intersects(r)){
                    ret=req.hitResult=JRequest.HIT_OBJECT;
                    req.hitObjects.add(this);
                }
            }
        }
        return ret;
    }
    /**
     * ���̃I�u�W�F�N�g���w�肷��Rectangle2D�Ɍ�������ꍇ�́A�������邱�̃I�u�W�F�N�g�̕�����
     * �w�肷��JRequest�Ɋi�[���܂�.
     * @param env �L���Ȋ����i�[����JEnvironment�I�u�W�F�N�g
     * @param req ���݂̑I����ԋy�ь������茋�ʂ��i�[����JRequest�I�u�W�F�N�g.
     * @param rect ����������s��Rectangle2D
     */
    @Override
    public void hitByRect(JEnvironment env, JRequest req, Rectangle2D rect) {
        if (isLocked() || !isVisible()) return;
        for (int i=0;i<getPath().segmentSize();i++){
            JSegment seg=getPath().getSegment(i);
            if (rect.contains(seg.getAncur())){
                if (req.getSelectionMode()==JRequest.GROUP_MODE){
                    req.hitObjects.add(this);
                    return;
                }else{
                    req.hitObjects.add(seg);
                    JSimplePath spath=getPath().getOwnerPath(seg);
                    if(!req.hitObjects.contains(spath))
                        req.hitObjects.add(getPath().getOwnerPath(seg));
                    if (!req.hitObjects.contains(this))
                        req.hitObjects.add(this);
                }
                
            }
        }
        
        BasicStroke sStroke=new BasicStroke((float)(JEnvironment.SELECTION_STROKE_SIZE/env.getToScreenRatio()));
        for (int i=0;i<getPath().size();i++){
            Shape ss=sStroke.createStrokedShape(getPath().get(i).getShape(getPath().getWindingRule()));
            if (ss.intersects(rect)){
                req.hitObjects.add(getPath().get(i));
                if (!req.hitObjects.contains(this))
                    req.hitObjects.add(this);
            }
        }
        if (req.hitObjects.contains(this)) return;
        if (lineShape != null && lineShape.intersects(rect))
            req.hitObjects.add(this);
        
    }
    /**
     * ���̃I�u�W�F�N�g��`�悵�܂�.
     * @param clip �`��̃N���b�s���O�o�E���f�B���O�{�b�N�X
     * @param g �`��̃O���t�B�b�N�X�R���e�L�X�g
     */
    @Override
    public void paintThis(Rectangle2D clip,Graphics2D g){
        if (!clip.intersects(getBounds())) return;
        Vector<Point2D> poly=null;
        if (lineShape==null){
            poly=dividePathRelatively(getPath().get(0),startPosition,0.01f);
            lineShape=createLineShape(poly,document,null);
        }
        TextLocater locater=createLocater(null);
        effector.paintText(g, locater,null, fillPaint, strokePaint, stroke);
        //effector.paint(g,lineShape,fillPaint,strokePaint,stroke);
    }
    /**
     * ���̃I�u�W�F�N�g�̃v���r���[��`�悵�܂��B
     * @param env ���݂̕`���������JEnvionment
     * @param req �I����Ԃ��i�[����JRequest
     * @param g �`��̃O���t�B�b�N�X�R���e�L�X�g
     */
    @Override
    public void paintPreview(JEnvironment env,JRequest req,Graphics2D g){
        super.paintPreview(env,req,g);
        if (previewShape==null){
            JSimplePath apath=getTransformedPath(req).get(0);
            Vector<Point2D> poly=dividePathRelatively(apath,previewPosition,0.01f);
            setConrolPos(poly,g.getFontRenderContext());
            previewShape=createLineShape(poly,document,null);
        }
        AffineTransform af=env.getToScreenTransform();
        
        
        if (getTransform() !=null || previewPosition !=startPosition)
            g.draw(af.createTransformedShape(previewShape));
        if (req.getSelectionMode()==JRequest.GROUP_MODE) return;
        double radius=JEnvironment.PATH_SELECTOR_SIZE;
        Rectangle2D.Double fr=new Rectangle2D.Double(0,0,radius,radius);
        if (control1==null) return;
        Point2D.Double p=new Point2D.Double(),p1=new Point2D.Double();
        af.transform(control1.getAncur(),p);
        af.transform(control2.getAncur(),p1);
        Line2D.Double line= new Line2D.Double(p,p1);
        g.draw(line);
        fr.x=p.x-radius/2;
        fr.y=p.y-radius/2;
        g.fill(fr);
    }
    /**
     * ������ꂽ�A�t�B���ϊ����I�u�W�F�N�g�ɓK�p���A�ϊ����������UndoaleEdit��Ԃ��܂�.
     * @param env ���݂̕`���������JEnvironment
     * @return ������ꂽ�ϊ����������UndoableEdit
     */
    @Override
    public UndoableEdit updateTransform(JEnvironment env){
        env.addClip(getBounds());
        lineShape=null;
        previewShape=null;
        UndoableEdit ret;
        if (startPosition !=previewPosition){
            ret=new JPathTextPositionChangeEdit(getDocument().getViewer(),this,previewPosition);
        } else{
            ret= super.updateTransform(env);
        }
        env.addClip(getBounds());
        return ret;
        
    }
    /**
     * ������ꂽ��]�ϊ����܂ރA�t�B���ϊ������̃I�u�W�F�N�g�ɓK�p���A�ϊ����������UndoableEdit��Ԃ��܂�.
     * @param env ���݂̕`���������JEnvironment
     * @param rotation ������ꂽ�A�t�B���ϊ��̉�]�ړ��v�f�̉�]�p
     * @return ������ꂽ�ϊ����������UndoableEdit
     */
    @Override
    public UndoableEdit updateRotate(JEnvironment env,double rotation){
        env.addClip(getBounds());
        lineShape=null;
        previewShape=null;
        UndoableEdit ret= super.updateRotate(env,rotation);
        env.addClip(getBounds());
        return ret;
        
    }
    /**
     * ���̃I�u�W�F�N�g�Ɏw�肷��Affine�ϊ���K�p���A�v���r���[��Ԃ��X�V���܂�.
     * @param tr ���̃I�u�W�F�N�g�ɉ�����A�t�B���ϊ�
     * @param req ���݂̑I����Ԃ��i�[����JRequest�I�u�W�F�N�g
     * @param mp �ϊ��̊�_
     */
    @Override
    public void transform(AffineTransform tr,JRequest req,Point  mp) {
        previewShape=null;
        lineShape=null;
        if (req.getSelectionMode()==JRequest.DIRECT_MODE && req.hitResult==JRequest.HIT_ANCUR && req.hitObjects.contains(control1)){
            Point2D p=new Point2D.Double();
            getDocument().getViewer().getEnvironment().getToAbsoluteTransform().transform(mp,p);
            previewPosition=dividePathPt(getPath().get(0),(float) p.getX(),(float) p.getY(),0.01f);
        }else{
            super.transform(tr,req,mp);
        }
    }
    /**
     * ���̃I�u�W�F�N�g�̕`��͈͂��܂���ŏ���Recntangle2D��Ԃ��܂�.
     * @return ���̃I�u�W�F�N�g�̕`��͈͂��܂���ŏ���Rectangle2D
     */
    @Override
    public Rectangle2D getBounds(){
        Rectangle2D ret=super.getBounds();
        if (lineShape==null ){
            Vector<Point2D> poly=dividePathRelatively(getPath().get(0),startPosition,0.01f);
            lineShape=createLineShape(poly,document,null);
        }
        Rectangle2D r=lineShape.getBounds2D();
        if (strokePaint!=null){
            double rad=stroke.getWidth();
            if (stroke.getLineJoin()==BasicStroke.JOIN_MITER){
                rad+=stroke.getMiterLimit();
            }
            r.setFrame(r.getX()-rad/2,r.getY()-rad/2,r.getWidth()+rad,r.getHeight()+rad);
        }
        if (!r.isEmpty()) ret.add(r);
        return effector.culcBounds(ret,this);
    }
    /**
     * ���̃I�u�W�F�N�g�𕡐����܂�.
     * @return ��������JPathTextObject
     */
    @Override
    public JPathTextObject clone(){
        JPaint fp=null,sp=null;
        if (fillPaint!=null)
            fp=fillPaint.clone();
        if (strokePaint!=null)
            sp=strokePaint.clone();
        JPathTextObject ret=new JPathTextObject(fp,sp,stroke);
        ret.setPath(this.getPath().clone());
        ret.document=getCloneStyledDocument();
        ret.setStartPosition(getStartPosition());
        ret.totalRotation=totalRotation;
        ret.setEffector(getEffector());
        return ret;
    }
    /**
     * ���̃I�u�W�F�N�g�̏����t�e�L�X�g��ێ�����StyledDocument��Ԃ��܂�.
     * @return ���̃I�u�W�F�N�g�̏����t�e�L�X�g��ێ�����StyledDocument
     */
    @Override
    public DefaultStyledDocument getStyledDocument() {
        return document;
    }
    /**
     * ���̃I�u�W�F�N�g�̏����t�e�L�X�g��ێ�����StyledDocument�̕�����Ԃ��܂�.
     * @return ���̃I�u�W�F�N�g�̏����t�e�L�X�g�̕���.
     */
    @Override
    public DefaultStyledDocument getCloneStyledDocument() {
        return JTextObject.cloneDocument(document);
    }
    /**
     * ���̃I�u�W�F�N�g�̏����t�e�L�X�g��ێ�����StyledDocument��ݒ肵�܂��B
     * @param doc�@�w�肷��StyledDocument
     */
    @Override
    public void setStyledDocument(DefaultStyledDocument doc) {
        this.document=doc;
    }
    /**
     * �ύX���X�V���邽�߁A���̃I�u�W�F�N�g�̃e�L�X�g�̃A�E�g���C����ێ�����Shape���X�V���܂��B
     */
    @Override
    public void updatePath(){
        lineShape=null;
        previewShape=null;
        getDocument().getEnvironment().addClip(getBounds());
    }

    /**
   * �e�L�X�g�̕ύX���X�V���邽�߁A���̃I�u�W�F�N�g��`��N���b�v�ɉ����܂�.
   * @param env ���݂̕`������i�[���� JEnvironment
   */
    @Override
    public void textUpdate(JEnvironment env) {
        env.addClip(getBounds());
        updatePath();
        env.addClip(getBounds());
    }
    /**
     * ���̃e�L�X�g�̃A�E�g���C����ێ�����Shape�I�u�W�F�N�g��Ԃ��܂�.
     * @return �e�L�X�g�̃A�E�g���C����ێ�����Shape�I�u�W�F�N�g
     */
    @Override
    public Shape getShape(){
        return lineShape;
    }
    /**
     * ���̃I�u�W�F�N�g�ɗݐς��ꂽ�A�t�B���ϊ���Ԃ��܂�.
     * @return ���̃I�u�W�F�N�g�ɗݐς��ꂽ�A�t�B���ϊ�
     */
    @Override
    public AffineTransform getTotalTransform() {
        return new AffineTransform();
    }
    /**
     * ���̃I�u�W�F�N�g�̃��C�A�E�g�|���V�[��ێ�����TextLocater�I�u�W�F�N�g��Ԃ��܂��B
     * @return ���̃I�u�W�F�N�g�̃��C�A�E�g�|���V�[��ێ�����TextLocater
     */
    @Override
    public TextLocater createLocater(FontRenderContext frc) {
        if (frc==null){
            frc=new FontRenderContext(null,true,true);
        }
        return new OnPathTextLocater(getStyledDocument(),this,frc);
    }
    /**
     * ���̃I�u�W�F�N�g�̃��C�A�E�g�̊�ƂȂ�Shape�I�u�W�F�N�g��Ԃ��܂�.
     * @return ���̃I�u�W�F�N�g�̃��C�A�E�g�̊�ƂȂ�Shape�I�u�W�F�N�g
     */
    @Override
    public Shape getLayoutShape() {
        return this.getPath().getShape();
    }
    /**
     * ���̃I�u�W�F�N�g�̎����������̊�����Ԃ��܂��B
     * @return�@���̃I�u�W�F�N�g��������������ۂ̊���
     */
    @Override
    public String getPrefixer(){
        return "Text";
    }
   
}
