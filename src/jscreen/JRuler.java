/*
 * JRuler.java
 *
 * Created on 2006/12/16, 11:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package jscreen;


import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
/**
 *JDrawScroller��ClumnHeader����RowHeader�ɖڐ���\�����܂�.
 * @author TK
 */
public class JRuler extends JComponent{
    /**
     *ColumnHeader�ɕ\�����鐅���ڐ���\���܂�.
     */
    public static final int HORIZONTAL=0;
    /**
     *RowHeader�ɕ\�����鐂���ڐ���\���܂�.
     */
    public static final int VERTICAL=1;
    /**
     *JDrawRuler�̍������͕�<br>
     *orientation��HOLIZONTAL�̏ꍇ�͍����AVERTICAL�̏ꍇ�͕���\���܂��B
     */
    public static final int SIZE=20;
    /**Inch�P�ʂ̏ꍇ�̏��ڐ��Ԋu*/
    private static final int[] unitIntervalM={1, 2, 5,10, 20,50,100,200,400};
    /**Inch�P�ʂ̏ꍇ�̑�ڐ��Ԋu*/
    private static final int[] gaugeIntervalM={5,10,20,50,100,200,500,1000,2000};
    /**mm�P�ʂ̏ꍇ�̏��ڐ��Ԋu*/
    private static final int[] unitIntervalI={1,2,3,4,6,12,36,36,72,144,288,576,1152};
    /**mm�P�ʂ̏ꍇ�̑�ڐ��Ԋu*/
    private static final int[] gaugeIntervalI={6,12,24,36,72,72,72,144,288,576,1152};
    
    private MyGlassPane glassPane;
    private int orientation;
    private int gauge;
    private int unitInterval;
    private double unitLength;
    private int gaugeInterval;
    private JScroller scroller;
//    private double gridOffsetByMetric,gridOffsetByPoint;
    /**
     *JDrawRuler���\�z���܂�.
     *@param orientation Ruler�̕��� HORIZONTAL ����Ruler,VERTICAL ����Ruler<br>
     *scroller Ruler�����L����JDrawScroller
     */
    public JRuler(int orientation,JScroller scroller){
        this.scroller=scroller;
        this.orientation=orientation;
        gauge=scroller.getEnvironment().getGuageUnit();
        glassPane=new MyGlassPane();
        glassPane.setBounds(0, 0, SIZE, SIZE);
        this.add(glassPane);       
        setIncrementAndUnits();
    }
    /**Ruler�̕���ݒ肵�܂��B
     *@param ph �ݒu���镝.
     */
    public void setPreferredWidth(int ph){
        Dimension d=new Dimension(ph,SIZE);
        setPreferredSize(d);
        setSize(d);
        setIncrementAndUnits();
        this.validate();
    }
    /**Ruler�̍�����ݒ肵�܂�.
     *@param ph �ݒ肷�鍂��.
     */
    public void setPreferredHeight(int ph){
        Dimension d=new Dimension(SIZE,ph);
        setPreferredSize(d);
        setSize(d);
        setIncrementAndUnits();
        this.validate();
    }
    /**Ruler��̃J�[�\���̈ʒu��ݒ肵�܂�.
     *@param p �J�[�\���̈ʒu
     */
    public void setCursor(Point2D p){
        if (orientation==HORIZONTAL){
            glassPane.setBounds((int)p.getX(),0, SIZE, SIZE);
        }else{
            glassPane.setBounds(0,(int) p.getY(),SIZE,SIZE);
        }
    }
    /**
     *Ruler���_�ʒu�̗p������[����̋�����1/72�C���`�P�ʂŎw�肵�܂�.
     *@param offset �w�肷����W�̈ʒn
     */
    public void setGridOffset(double offset){
        if (this.orientation==HORIZONTAL)
            scroller.getEnvironment().setGaugeOffsetX(offset);
        else
            scroller.getEnvironment().setGaougeOffsetY(offset);
    }
    /**���݂�Ruler���_�ʒu�̗p������[����̋�����1/72�C���`�P�ʎ擾���܂�.
     *@return ���[���̌��_�ʒn�̗p������[����̈ʒn
     */
    public double getGridOffset(){
        if (this.orientation==HORIZONTAL)
            return scroller.getEnvironment().getGaugeOffset().getX();
        else
            return scroller.getEnvironment().getGaugeOffset().getY();
    }
    /**
     *���݂̕\���P�ʁA�\���{�������Ɋe�p�����[�^��ݒ肵�܂��B
     */
    private void setIncrementAndUnits(){
        JDocumentViewer viewer=scroller.getViewer();
        double magnification=scroller.getEnvironment().getMagnification();
        double pixcelPerPoint=viewer.getEnvironment().getScreenDPI()/72;
        double pixcelPerMil=pixcelPerPoint*72d/25.4d;
        double ui=0;
        int i;
        if (viewer.getEnvironment().getGuageUnit()==viewer.getEnvironment().METRIC_GAUGE){
            for (i=0;i<unitIntervalM.length;i++){
                ui=pixcelPerMil*magnification*unitIntervalM[i];
                if (ui >=4){
                    break;
                }
            }
            if (i==unitIntervalM.length){
                i--;
            }
            unitLength=ui;
            unitInterval=unitIntervalM[i];
            gaugeInterval=gaugeIntervalM[i];
        }
        else{
            for (i=0;i<unitIntervalI.length;i++){
                ui=pixcelPerPoint*magnification*unitIntervalI[i];
                if (ui >=4){
                    break;
                }
            }
            if (i==unitIntervalI.length){
                i--;
            }
            unitLength=ui;
            unitInterval=unitIntervalI[i];
            gaugeInterval=gaugeIntervalI[i];
        }
    }
    /**
     *����Ruler�����L����Scroller��Ԃ��܂�.
     *@return ����Ruler�����L����Scroller.
     */
    public JScroller getScroller(){
        return scroller;       
    }
    /**Ruler��`�悵�܂�.
     *@param g �O���t�B�b�N�R���e�L�X�g
     */
    @Override
    public void paintComponent(Graphics g){
        JDocumentViewer viewer=scroller.getViewer();
        setIncrementAndUnits();
        Rectangle drawHere=g.getClipBounds();
        Graphics2D g2=(Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(drawHere.x,drawHere.y,drawHere.width, drawHere.height);
        g.setColor(Color.DARK_GRAY);
        if (orientation==HORIZONTAL){
            g.drawLine(drawHere.x,SIZE-1,drawHere.x+drawHere.width, SIZE-1);
        }else{
            g.drawLine(SIZE-1,drawHere.y,SIZE-1,drawHere.y+drawHere.height);
        }
        //
        int end=0;
        int start=0;
        Point2D docP=new Point2D.Double(scroller.getEnvironment().getPaperRect().getX(),scroller.getEnvironment().getPaperRect().getY());
        scroller.getEnvironment().getToScreenTransform().transform(docP,docP);
        String text=null;
        double ratio;
        double gridOffset;
        ratio=viewer.getEnvironment().getToScreenRatio();
        if (viewer.getEnvironment().getGuageUnit()==viewer.getEnvironment().METRIC_GAUGE){
            gridOffset=getGridOffset()*viewer.getEnvironment().getScreenDPI()*72/viewer.getEnvironment().MIL_PER_INCH;
        }else{
            gridOffset=getGridOffset();
        }
         //
        if (orientation==HORIZONTAL){
            start=(int)((drawHere.x-docP.getX()-gridOffset*ratio)/unitLength);
            end=(int)((drawHere.x+drawHere.width-docP.getX()-gridOffset*ratio)/unitLength);
        }
        else{
            start=(int)((drawHere.y-docP.getY()-gridOffset*ratio)/unitLength);
            end=(int)((drawHere.y+drawHere.height-docP.getY()-gridOffset*ratio)/unitLength);
        }
        start=start-gaugeInterval*2;
        end=end+gaugeInterval*2;
        
        int x,y,tickLength;
        Font fnt=g.getFont();
        g.setFont(new Font(fnt.getName(), fnt.getStyle(),9));
        for(int i=start;i<=end;i++){
            tickLength=2;
            text=null;
            if (i*unitInterval % gaugeInterval==0){
                tickLength=6;
            }
            if (i*unitInterval % (gaugeInterval*2)==0){
                tickLength=SIZE;
                text=Integer.toString(i*unitInterval);
            }
            if(orientation==HORIZONTAL){
                x=(int)(unitLength*i+docP.getX()+gridOffset*ratio);
                g.drawLine(x, SIZE-1, x, SIZE-tickLength-1);
                if (text !=null){
                    g.drawString(text, x+2, 9);
                }
            }
            else{
                y=(int)(unitLength*i+docP.getY()+gridOffset*ratio);
                g.drawLine(SIZE-1, y, SIZE-tickLength-1, y);
                if (text!=null){
                    g.drawString(text, 0, y+9);
                }
            }
        }        
    }
    /**Ruler�ɕ`�悷��J�[�\����\���܂�.*/
    public class MyGlassPane extends JComponent{
        private BasicStroke stroke=new BasicStroke(1f);       
        public void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g; 
            g2.setStroke(stroke);
            g2.setColor(Color.RED);            
            if (orientation==HORIZONTAL){
                g2.drawLine(0,0, 0,SIZE);
            }
            else{
                g2.drawLine(0, 0, SIZE, 0);
            }
        }

    }
    
}
