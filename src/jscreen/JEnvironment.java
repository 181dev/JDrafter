/*
 * JEnvironment.java
 *
 * Created on 2007/08/18, 18:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package jscreen;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.io.IOException;
import java.io.Serializable;
import java.util.Vector;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import jgeom.JIntersect;
import jgeom.JSegment;
import jgeom.JSimplePath;
import jobject.JGuidLayer;
import jobject.JPage;
import jtools.JCursor;
import jpaint.JPaint;
import jpaint.JPatternPaint;
import jpaint.JStroke;
import jtools.JAbstractTool;
import jui.JIcons;

/**
 *�X�N���[���ƃI�u�W�F�N�g�̕`��ɕK�v�ȏ���񋟂��邽�߂̃C���^�[�t�F�[�X��񋟂��܂�.
 * @author TI
 */
public class JEnvironment implements Serializable, Cloneable {
    //
    private static final long serialVersionUID = 110l;
    /**�}�E�X�J�[�\���ł�*/
    public static final JCursor MOUSE_CURSOR = new JCursor();
    /**�A�C�R���ł�*/
    public static final JIcons ICONS = new JIcons();
    /** mm(�~�����[�g���j�̕\���P�ʂ�\���܂�.*/
    public static final int METRIC_GAUGE = 0;
    /** Point(1/72�C���`)�̕\���P�ʂ�\���܂�.*/
    public static final int INCHI_GAUGE = 1;
    /**Point��mm�Ɋ��Z���邽�߂̌W���ł�.*/
    public static final double MIL_PER_POINT = 25.4d / 72d;
    /**mm���|�C���g�Ɋ��Z���邽�߂̌W���ł�.*/
    public static final double MIL_PER_INCH = 25.4d;
    /**�p�X�����������`�̈�ӂ̒����ł��B*/
    public static final float DEFAULT_PATH_SELECTOR_SIZE = 4;
    public static float PATH_SELECTOR_SIZE = DEFAULT_PATH_SELECTOR_SIZE;
    /**�n�C���C�g�A���J�[�̕\���䗦*/
    public static final float DEFAULT_HILIGHT_RATIO = 1.5f;
    public static float HILIGHT_RATIO = DEFAULT_HILIGHT_RATIO;
    /*�t���[�t�H�[���Z���N�^�̃R���g���[���̐����`�̈�ӂ̒����ł�.*/
    //public static  float OBJECT_SELECTOR_SIZE=4;
    //
    /** �f�t�H���g�̃K�C�h�̐F�ł�*/
    public static final Color DEFAULT_GUID_COLOR = Color.BLUE;
    /**�f�t�H���g�̃K�C�h�̃v���r���[�F�ł�.*/
    public static final Color DEFAULT_GUID_PREVIEW_COLOR = Color.CYAN;
    /**�y�C���g���̃A���`�G���A�t���O*/
    public static final boolean DEFAULT_PAINT_ANTI_AREASING = true;
    public static boolean PAINT_ANTI_AREASING = DEFAULT_PAINT_ANTI_AREASING;
    /**�v���r���[���̃A���`�G���A�t���O*/
    public static final boolean DEFAULT_PREVIEW_ANTI_AREASING = false;
    public static boolean PREVIEW_ANTI_AREASING = DEFAULT_PREVIEW_ANTI_AREASING;
    /**����I�����邽�߂̋��e�덷�ł��B*/
    public static final float DEFAULT_SELECTION_STROKE_SIZE = 3f;
    public static float SELECTION_STROKE_SIZE = DEFAULT_SELECTION_STROKE_SIZE;
    /**����I�����邽�߂�Stroke�ł��B*/
    public static Stroke SELECTION_STROKE = new BasicStroke(SELECTION_STROKE_SIZE);
    /**�|�C���g�X�i�b�v�t���O*/
    public static boolean SNAP_TO_ANCUR = true;
    /**�f�t�H���g�̓h��ł��B*/
    public static final JPaint DEFAULT_FILL = new JPaint(Color.WHITE);
    /**�f�t�H���g�̐��F�ł��B*/
    public static final JPaint DEFAULT_BORDER = new JPaint(Color.BLACK);
    /**�f�t�H���g�̐���ł�*/
    public static final JStroke DEFAULT_STROKE = new JStroke(new BasicStroke(1f));
    /**�f�t�H���g�̃e�L�X�g�F�ł�.*/
    public static final JPaint DEFAULT_TEXT_FILL = new JPaint(Color.BLACK);
    /**�f�t�H���g�̃e�L�X�g�A�E�g���C���F�ł�*/
    public static final JPaint DEFAULT_TEXT_BORDER = null;
    /**�f�t�H���g�̃e�L�X�g����ł�*/
    public static final JStroke DEFAULT_TEXT_STROKE = new JStroke(new BasicStroke(1f));
    /**�J�����g�̓h��ł��B*/
    public static JPaint currentFill = DEFAULT_FILL;
    /**�J�����g�̐��̓h��ł�.*/
    public static JPaint currentBorder = DEFAULT_BORDER;
    /**�J�����g�̐��ł�.*/
    public static JStroke currentStroke = DEFAULT_STROKE;
    /**�e�L�X�g�I�u�W�F�N�g�̃J�����g�̓h��ł�.*/
    public static JPaint currentTextFill = DEFAULT_TEXT_FILL;
    /**�e�L�X�g�I�u�W�F�N�g�̃J�����g�̐���ł��B*/
    public static JStroke currentTextStroke = DEFAULT_TEXT_STROKE;
    /**�e�L�X�g�I�u�W�F�N�g�̃J�����g�̐��F�ł��B*/
    public static JPaint currentTextBorder = DEFAULT_TEXT_BORDER;
    /**�v���r���[�̐F�ł��B*/
    public static Color PREVIEW_COLOR = new Color(0f, 0.8f, 0.8f);
    /**�f�t�H���g�̃v���r���[�F�������܂�*/
    public static final Color[] PREVIEW_COLORS = new Color[]{
        new Color(50, 100, 255),
        new Color(255, 0, 0),
        new Color(0, 255, 0),
        new Color(0, 0, 255),
        new Color(255, 255, 0),
        new Color(255, 0, 255),
        new Color(0, 255, 255),
        new Color(153, 153, 153),
        new Color(0, 0, 0),
        new Color(255, 102, 0),
        new Color(0, 153, 0),
        new Color(0, 204, 204),
        new Color(204, 153, 0),
        new Color(204, 0, 0),
        new Color(153, 0, 255),
        new Color(255, 204, 0),
        new Color(0, 0, 153),
        new Color(255, 153, 255),
        new Color(153, 153, 255),
        new Color(102, 0, 0),
        new Color(51, 102, 0),
        new Color(255, 153, 153),
        new Color(153, 153, 0),
        new Color(204, 255, 0),
        new Color(204, 204, 255),
        new Color(204, 102, 255),
        new Color(204, 204, 204),
        new Color(0, 153, 153),
        new Color(255, 255, 204),
        new Color(204, 204, 255),
        new Color(204, 255, 204),
        new Color(204, 204, 0)
    };
    /**�h���b�O���̃Z���N�V�������N�g�̃v���r���[�F�ł��B*/
    public static Color DRAG_AREA_COLOR = Color.BLACK;
    /**�h���b�O���̃Z���N�V�����v���r���[�̐��ł�.*/
    public static Stroke DRAG_AREA_STROKE = new BasicStroke(0f, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER, 10f, new float[]{1f, 1f}, 0f);
    /**�O���b�h���̐���������܂�.*/
    public static Stroke GUAGE_STROKE = new BasicStroke(0f);
    /**�O���b�h���̐F�������܂�.*/
    public static final Color DEFAULT_GRID_COLOR = new Color(0.85f, 0.85f, 0.85f);
    public Color GRID_COLOR = DEFAULT_GRID_COLOR;
    /**�����O���b�h���̐F�������܂�.*/
    public static final Color DEFAULT_DIVIDE_GRID_COLOR = new Color(0.95f, 0.95f, 0.95f);
    public Color DIVIDE_GRID_COLOR = DEFAULT_DIVIDE_GRID_COLOR;
    /**�O���b�h�̑O�ʕ\���t���O�ł��B*/
    public static boolean GRID_FOREGROUND = false;
    /**�X�P�[�����O���̍ŏ��X�P�[�����O�W���ł��B*/
    public static final double MINIMUM_SCALE_RATIO = 0.0001;
    /**�Z���N�V�������N�g�̍ŏ��l�������܂�.*/
    public static final double MINIMUM_SELECT_SIZE = 18d;
    /**�I�u�W�F�N�g�̃f�t�H���g�̔��a�������܂�*/
    public static double DEFAULT_RADIUS = 72d;
    /**�I�u�W�F�N�g�̂Ńf�t�H���g�̕��������܂�.*/
    public static double DEFAULT_WIDTH = 72d;
    /**�I�u�W�F�N�g�̃f�t�H���g�̍����������܂�.*/
    public static double DEFAULT_HEIGHT = 72d;
    /**�p�x�c�[���̃f�t�H���g�̊p�x�������܂�*/
    public static double DEFAULT_ANGLE = Math.PI / 2;
    /**�f�t�H���g�̊p�̊ۂߔ��a�������܂�.*/
    public static double DEFAULT_ROUNDRECT_RADIUS = 16d;
    /**�f�t�H���g�̂׃x���̔��a�������܂�.*/
    public static double DEFAULT_BEVEL_RADIUS = 12d;
    /**�f�t�H���g�̑��p�`�̒��_���������܂�.*/
    public static int DEFAULT_POLYGON_VERTEX = 6;
    /**�f�t�H���g�̐��^�}�`�̓ʒ��_���������܂�.*/
    public static int DEFAULT_STAR_VERTEX = 5;
    /**���^�}�`�̓ʒ��_�Ɖ����_�̔��a�̔䗦�������܂�.*/
    public static double DEFAULT_STAR_RADIUS_RATIO = 0.381966011;
    /**�f�t�H���g��X�����̃X�P�[�����O�W���������܂��B*/
    public static double DEFAULT_SCALE_X = 1d;
    /**�f�t�H���g��Y�����̃X�P�[�����O�W���������܂�.*/
    public static double DEFAULT_SCALE_Y = 1d;
    /**�f�t�H���g��X�������̃V�A�����O�W���������܂�.*/
    public static double DEFAULT_SHEER_X = 0d;
    /**�f�t�H���g��Y�������̃V�A�����O�W���������܂�.*/
    public static double DEFAULT_SHEER_Y = 1d;
    /**�f�t�H���g�̉�]�p�������܂�.*/
    public static double DEFAULT_THETA = 0d;
    /**�Ώ̈ړ����̊p�x�������܂�*/
    public static double DEFAULT_REFLECT_AXIS = 0d;
    /**�p�ۂ߁E�؂藎�����쎞�̃f�t�H���g�̔��a�������܂�.*/
    public static double DEFAULT_CUTCORNER_RADIUS = 12d;
    /**X���W�̕��s�ړ������������܂�.*/
    public static double DEFAULT_TRANSLATE_X = 0d;
    /**Y���W�̕��s�ړ������������܂�>*/
    public static double DEFAULT_TRANSLATE_Y = 0d;
    /**���O�̈ړ�������AffineTransform�ł�.*/
    public static AffineTransform LAST_TRANSFORM = null;
    /**���O�̉�]�p�������܂�.*/
    public static double LAST_ROTATION = 0;
    /**���O�̈ړ��̃R�s�[����̗L���������܂�.*/
    public static boolean LAST_COPY = false;
    /**�\���{���������܂�.*/
    private double magnification = 1.0d;//�\���{��
    /**���W�P�ʂ������܂�.*/
    public static int guageUnit = METRIC_GAUGE;//���W�P
    /**�y�[�p�[�̍��������A�ڐ���̌��_�܂ł�X�I�t�Z�b�g�������܂�.*/
    private double gaugeX = 0;
    /**�y�[�p�[���������A�ڐ���̌��_�܂ł�Y�I�t�Z�b�g�������܂�.*/
    private double gaugeY = 0;
    /**�O���b�h�\���̗L�����w�肷��t���O�ł�.*/
    private boolean isGridVisible = true;
    /**�O���b�h�z���̗L�����w�肷��t���O�ł�.*/
    private boolean isSnapGrid = false;
    /**�~���P�ʕ\���̏ꍇ�̃f�t�H���g�̃O���b�h�Ԋu�ł�.*/
    public static final double DEFAULT_GRIDSIZE_BYMIL = 10;
    private double gridSizeByMil = 10;
    /**�~���P�ʕ\���̏ꍇ�̃f�t�H���g�̃O���b�h�������ł�.*/
    public static final int DEFAULT_GRIDDIVISION_BYMIL = 10;
    private int gridDivisionByMil = 10;
    /**�|�C���g�P�ʕ\���̏ꍇ�̃f�t�H���g�̃O���b�h�Ԋu�ł�.*/
    public static final double DEFAULT_GRIDSIZE_BYPOINT = 36;
    private double gridSizeByPoint = 36;
    /**�|�C���g�P�ʕ\���̏ꍇ�̃f�t�H���g�̃O���b�h�������ł�.*/
    public static final int DEFAULT_GRIDDIVISION_BYPOINT = 6;
    /**�쐬���ꂽ�p�^�[�����ꎞ�I�ɃZ�[�u���܂�.
     */
    public static JPaint SAVED_PATTERN=null;
    private int gridDivisionByPoint = 6;
    /**�V�t�g���������̈ړ������p�ł�.*/
    public static final double DEFAULT_UNIT_ANGLE = 45d;
    private double unitAngle = DEFAULT_UNIT_ANGLE;
    private Point paperOffset = new Point();
    private Rectangle2D.Double paperRectangle = new Rectangle2D.Double();
    private Rectangle2D.Double imageRect = new Rectangle2D.Double();
    /**
     * ���݂̕\���f�o�C�X�̉𑜓x�ł��B
     */
    public static final double screenDPI = Toolkit.getDefaultToolkit().getScreenResolution();
    private transient Rectangle2D clip = null;
    private transient Vector<ChangeListener> listeners = null;

    /** �C���X�^���X���\�z���܂��B*/
    public JEnvironment() {
        currentFill = new JPaint(Color.WHITE);
        currentBorder = new JPaint(Color.BLACK);
        currentStroke = new JStroke(new BasicStroke(1.0f));
    }
    /**�`�F���W���X�i�[��ǉ����܂�*/
    ;

    public void addChangeListener(ChangeListener ls) {
        if (listeners == null) {
            listeners = new Vector<ChangeListener>();
        }
        if (!listeners.contains(ls)) {
            listeners.add(ls);
        }

    }

    /**�`�F���W���X�i�[���폜���܂�.*/
    public void removeChangeListener(ChangeListener ls) {
        if (listeners == null) {
            return;
        }
        listeners.remove(ls);
    }

    private void fireChangeEvent() {
        if (listeners == null) {
            return;
        }
        ChangeEvent e = new ChangeEvent(this);
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).stateChanged(e);
        }
    }

    /**�\����ʏ��1�s�N�Z���̒���(Point)��Ԃ��܂�.*/
    public double pixelPerPoint() {
        return 72d / screenDPI;
    }

    /**�\����ʂ̉𑜓x��Ԃ��܂�.*/
    public double getScreenDPI() {
        return screenDPI;
    }

    /**�\���{����ݒ肵�܂�*/
    public void setMagnification(double magnification) {
        this.magnification = magnification;
        fireChangeEvent();
    }

    /**�\���{�����擾���܂�*/
    public double getMagnification() {
        return magnification;
    }

    /**�R���|�[�l���g���_����Document���_�܂ł�offset���X�N���[�����W�Őݒ肵�܂��B*/
    public void setOffsetByScreen(int x, int y) {
        paperOffset.x = x;
        paperOffset.y = y;
        fireChangeEvent();
    }

    /**�R���|�[�l���g���_����Document���_�܂ł�offset���΍��W�Őݒ肵�܂�.*/
    public void setOffset(double x, double y) {
        paperOffset.x = (int) (x * getToScreenRatio());
        paperOffset.y = (int) (y * getToScreenRatio());
        fireChangeEvent();
    }

    /**�R���|�[�l���g���_����Document���_�܂ł�offset���΍��W�Ŏ擾���܂�.*/
    public Point2D getOffset() {
        return new Point2D.Double(paperOffset.x / getToScreenRatio(), paperOffset.y / getToScreenRatio());
    }

    /**�R���|�[�l���g���_����Document���_�܂ł�offset���΍��W�Ŏ擾���܂�.*/
    public Point getOffsetByScreen() {
        return paperOffset;
    }

    /**Document�̌��_���烁�������W�����_�܂ł̑��΋�����ݒ肵�܂�*/
    public void setGaugeOffset(double x, double y) {
        gaugeX = x;
        gaugeY = y;
        fireChangeEvent();
    }

    /**Document�̌��_����ڐ����W�����_�܂ł̐��������̃I�t�Z�b�g��ݒ肵�܂�.*/
    public void setGaugeOffsetX(double x) {
        this.gaugeX = x;
        fireChangeEvent();
    }

    /**Document�̌��_����ڐ����W�����_�܂ł̐��������̃I�t�Z�b�g��ݒ肵�܂�.*/
    public void setGaougeOffsetY(double y) {
        this.gaugeY = y;
        fireChangeEvent();
    }

    /**�O���b�h�z���̗L�����擾���܂�.*/
    public boolean isSnapGrid() {
        return isSnapGrid;
    }

    /**�O���b�h�̋z����ݒ肵�܂�.*/
    public void setSnapGrid(boolean b) {
        isSnapGrid = b;
        fireChangeEvent();
    }

    /**�O���b�h�\���̗L����ݒ肵�܂�.*/
    public void setGridVisible(boolean b) {
        isGridVisible = b;
        fireChangeEvent();
    }

    /**�O���b�h�\���̗L�����擾���܂�.*/
    public boolean isGridVisible() {
        return isGridVisible;
    }

    /**�O���b�h�T�C�Y��ݒ肵�܂�*/
    public void setGridSize(double grd) {
        if (guageUnit == METRIC_GAUGE) {
            gridSizeByMil = grd;
        } else {
            gridSizeByPoint = grd;
        }
        fireChangeEvent();
    }

    /**�O���b�h�T�C�Y��point�P�ʂŎ擾���܂�.*/
    public double getGridSize() {
        if (guageUnit == METRIC_GAUGE) {
            return gridSizeByMil * 72 / 25.4;
        } else {
            return gridSizeByPoint;
        }
    }

    /**�O���b�h�T�C�Y��mm�P�ʂŎ擾���܂�.*/
    public double getGridSizeForMil() {
        if (guageUnit == METRIC_GAUGE) {
            return gridSizeByMil;
        } else {
            return gridSizeByPoint / 72 * 25.4;
        }
    }

    /**�O���b�h�Ԋu��ݒ肵�܂�.*/
    public void setGridDivision(int dv) {
        if (guageUnit == METRIC_GAUGE) {
            gridDivisionByMil = dv;
        } else {
            gridDivisionByPoint = dv;
        }
        fireChangeEvent();
    }

    /**�O���b�h�Ԋu���擾���܂�.*/
    public int getGridDivision() {
        if (guageUnit == METRIC_GAUGE) {
            return gridDivisionByMil;
        } else {
            return gridDivisionByPoint;
        }
    }

    /**�ړ������p��ݒ肵�܂�.*/
    public void setUnitAngle(int ua) {
        unitAngle = ua;
        fireChangeEvent();
    }

    /**�ړ������p���擾���܂�.*/
    public double getUnitAngle() {
        return unitAngle;
    }

    /**Documento�̌��_���烁�������W�����_�܂ł̑��΋������擾���܂�*/
    public Point2D getGaugeOffset() {
        return new Point2D.Double(gaugeX, gaugeY);
    }

    /**��ʕ\���̂��߂�Scale���擾���܂�*/
    public double getToScreenRatio() {
        return magnification * screenDPI / 72;
    }

    /**��΍��W����X�N���[�����W�ɕϊ����邽�߂̕\���{�����܂�AffineTransform���\�z���܂�.*/
    public AffineTransform getToScreenTransform() {
        AffineTransform ret = new AffineTransform();
        Point2D p = getOffset();
        double ratio = getToScreenRatio();
        ret.setToScale(ratio, ratio);
        ret.translate(p.getX(), p.getY());
        return ret;
    }

    /**�X�N���[�����W�n�����΍��W�n�ɕϊ����邽�߂�AffineTransform���\�z���܂�.*/
    public AffineTransform getToAbsoluteTransform() {
        AffineTransform ret = new AffineTransform();
        double ratio = 1 / getToScreenRatio();
        Point2D p = getOffset();
        ret.setToTranslation(-p.getX(), -p.getY());
        ret.scale(ratio, ratio);
        return ret;
    }

    /**�p���̈��Rectangle��Ԃ��܂�*/
    public Rectangle2D getPaperRect() {
        return paperRectangle;
    }

    /**����\�̈��Rectangle��Ԃ��܂�*/
    public Rectangle2D getImageRect() {
        return imageRect;
    }

    /**PageFormat����p�����[�^��ݒ肵�܂�*/
    public void setPaper(PageFormat p) {
        paperRectangle.setFrame(0, 0, p.getWidth(), p.getHeight());
        imageRect.setFrame(p.getImageableX(), p.getImageableY(), p.getImageableWidth(), p.getImageableHeight());
    }

    /**�X�i�b�v�O���b�h�t���O���l�����`����W�n�̃}�E�X�|�C���g���΍��W�ɕϊ����܂�.*/
    public Point2D getAbsoluteMousePoint(Point2D p, JPage page) {
        Point2D.Double ret = new Point2D.Double();
        AffineTransform af = getToAbsoluteTransform();
        af.transform(p, ret);
        //
        if (page != null) {
            JGuidLayer jgl = page.getGuidLayer();
            if (!jgl.isEmpty()) {
                JRequest req = new JRequest(page);
                req.setSelectionMode(JRequest.DIRECT_MODE);
                int result = jgl.hitByPoint(this, req, ret);
                if (result == JRequest.HIT_ANCUR || result == JRequest.HIT_PATH) {
                    Point2D point = null;
                    JSimplePath path = null;
                    for (Object o : req.hitObjects) {
                        if (o instanceof JSegment) {
                            point = ((JSegment) o).getAncur();
                            if (path != null) {
                                break;
                            }
                        }
                        if (o instanceof JSimplePath) {
                            path = ((JSimplePath) o);
                            if (result == JRequest.HIT_PATH) {
                                point = getInterSection((JSimplePath) o, ret);
                                break;
                            } else if (point != null) {
                                break;
                            }
                        }
                    }
                    JAbstractTool tool = page.getDocument().getViewer().getDragPane().getDragger();
                    tool.setSnapPlace(result, point, path);
                    return point;
                }
            }
        }
        if (SNAP_TO_ANCUR) {
            JRequest req = new JRequest(page);
            req.hitResult = JRequest.HIT_NON;
            req.setSelectionMode(JRequest.DIRECT_MODE);
            int result = page.hitByPoint(this, req, ret);
            if (result == JRequest.HIT_ANCUR) {
                JSimplePath path = null;
                Point2D point = null;
                for (Object o : req.hitObjects) {
                    if (o instanceof JSegment) {
                        point = ((JSegment) o).getAncur();
                        if (path != null) {
                            break;
                        }
                    } else if (o instanceof JSimplePath) {
                        path = (JSimplePath) o;
                        if (point != null) {
                            break;
                        }
                    }
                }
                JAbstractTool tool = page.getDocument().getViewer().getDragPane().getDragger();
                tool.setSnapPlace(result, point, path);
                return point;
            }
        }
        if (isSnapGrid()) {
            double ival = getGridSize() / getGridDivision();
            double offsX = getGaugeOffset().getX();
            double offsY = getGaugeOffset().getY();
            ret.x = ival * Math.round((ret.x - offsX) / ival) + offsX;
            ret.y = ival * Math.round((ret.y - offsY) / ival) + offsY;
        }
        return ret;
    }

    /**�p�X�ƃ|�C���g�̌�_��Ԃ��܂�.
     * 
     */
    private Point2D getInterSection(JSimplePath pth, Point2D p) {
        JSimplePath addedPath = pth.clone();
        Point2D p0 = new Point2D.Double(),p1  = new Point2D.Double();
        double radius = PATH_SELECTOR_SIZE * HILIGHT_RATIO / getToScreenRatio() / 2;
        p0.setLocation(p.getX() - radius, p.getY() - radius);
        p1.setLocation(p.getX() + radius, p.getY() + radius);
        int k = JIntersect.addPath(p0, p1, addedPath);
        if (k == -1) {
            p0.setLocation(p.getX() - radius, p.getY() + radius);
            p1.setLocation(p.getX() + radius, p.getY() - radius);
            k = JIntersect.addPath(p0, p1, addedPath);
        }
        if (k != -1) {
            return addedPath.get(k).getAncur();
        }
        return null;
    }

    /**�V�t�g�ړ��̏ꍇ�̃|�C���g�ʒu���擾���܂�
     *@param source ��_
     *@param current ���ݓ_
     *@return �ϊ���̓_
     */
    public Point2D getShiftedMovePoint(Point2D source, Point2D current) {
        double unitTheta = unitAngle * Math.PI / 180d;
        double dx = current.getX() - source.getX();
        double dy = current.getY() - source.getY();

        double theta = unitTheta * Math.round(Math.atan2(dy, dx) / unitTheta);
        double length = Math.sqrt(dx * dx + dy * dy);
        return new Point2D.Double(length * Math.cos(theta) + source.getX(), length * Math.sin(theta) + source.getY());
    }

    /**�\���̒P�ʌn��ݒ肵�܂�
     *@param g �\���̒P�ʃ��[�g���P�ʌn:METRIC_GUAGE,�C���`�P�ʌn:INCHI_GUAGE;*/
    public void setGuageUnit(int g) {
        if (g != METRIC_GAUGE && g != INCHI_GAUGE) {
            return;
        }
        JEnvironment.guageUnit = g;
    }

    /**���݂̕\���P�ʌn��Ԃ��܂�.
     *@return  �\���̒P�ʃ��[�g���P�ʌn:METRIC_GUAGE,�C���`�P�ʌn:INCHI_GUAGE*/
    public int getGuageUnit() {
        return JEnvironment.guageUnit;
    }

    /**�f�[�^�ǂݍ���*/
    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject(); 
    //screenDPI=Toolkit.getDefaultToolkit().getScreenResolution();

    }

    /**�N���b�v�̈�Ɏw�肵��Shape�������܂�.*/
    public void addClip(Shape s) {
        if (clip == null) {
            clip = s.getBounds2D();
        }
        clip.add(s.getBounds2D());
    }

    /**�`����W�n��Shape���N���b�v�ɉ����܂�.*/
    public void addDrawClip(Shape s) {
        addClip(getToAbsoluteTransform().createTransformedShape(s));
    }

    /**�N���b�v�̈���擾���܂�.*/
    public Rectangle2D getClip() {
        return clip;
    }

    /**�N���b�v�̈��`����W�n�Ŏ擾���܂�*/
    public Rectangle getScreenClip() {
        return getToScreenTransform().createTransformedShape(clip).getBounds();
    }

    /**�N���b�s���O�̈���N���A���܂�.*/
    public void flushClip() {
        clip = null;
    }
    /**
     * ����JEnvironment�̕�����Ԃ��܂�.
     * @return
     */
    @Override
    @SuppressWarnings("static-access")
    public JEnvironment clone() {
        JEnvironment ret = new JEnvironment();
        ret.magnification = this.magnification;
        ret.gaugeX = this.gaugeX;
        ret.gaugeY = this.gaugeY;
        ret.guageUnit = JEnvironment.guageUnit;
        ret.imageRect = new Rectangle2D.Double(imageRect.x, imageRect.y, imageRect.width, imageRect.height);
        ret.paperOffset = new Point(paperOffset.x, paperOffset.y);
        ret.paperRectangle = new Rectangle2D.Double(paperRectangle.x, paperRectangle.y, paperRectangle.width, paperRectangle.height);
        return ret;
    }
}


