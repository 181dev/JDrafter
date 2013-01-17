/*
 * JDHuePaint.java
 *
 * Created on 2007/02/13, 13:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package jpaint;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

/**
 *Saturation(�ʓx�j�@Brightness(���x)�@�I��p��Paint�ł��B
 *�O100%
 *|��
 *|�x
 *|
 *|�@�@�@�@�@�@�@�@�ʓx
 *-----------------------------------��
 *0���@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@ 100%
 * @author i002060
 */
public class JDHSV2Paint implements Paint{
    /**�����ɍʓx�A�c���ɖ��x���v���b�g����Paint�ł�*/
    public static final int SB_MODE=0;
    /**�����ɐF���A�c���ɖ��x���v���b�g���܂�.*/
    public static final int HB_MODE=1;
    /**�����ɐF���A�c���ɍʓx���v���b�g���܂�.*/
    public static final int HS_MODE=3;
    private Rectangle2D rect;
    private Color baseColor;
    private int mode;
    
    /**
     * Creates a new instance of JDHuePaint
     */
    public JDHSV2Paint(Color c,Rectangle2D rect,int mode) {
        this.baseColor=c;
        this.rect=rect;
        this.mode=mode;
    }    
    public PaintContext createContext(ColorModel cm, Rectangle deviceBounds,
            Rectangle2D userBounds, AffineTransform xform, RenderingHints hints) {
        return new JDHSV2PaintContext(baseColor,rect,mode,xform);
    }

    public int getTransparency() {
        return OPAQUE;
    }
}
