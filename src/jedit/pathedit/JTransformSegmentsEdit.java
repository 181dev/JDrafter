/*
 * JTransformSegmentsEdit.java
 *
 * Created on 2007/09/03, 11:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package jedit.pathedit;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.Vector;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import jedit.*;
import jgeom.JSegment;
import jobject.JPathObject;
import jscreen.JDocumentViewer;
import jscreen.JEnvironment;

/**
 *
 * @author i002060
 */
public class JTransformSegmentsEdit extends JAbstractEdit{
    JPathObject target;
    AffineTransform af;
    Vector<JSegment> segments;
    /**
     * Creates a new instance of JTransformSegmentsEdit
     */
    public JTransformSegmentsEdit(JDocumentViewer v,JPathObject target,Vector<JSegment> segments,AffineTransform af){
        super(v);
        this.segments =(Vector<JSegment>)segments.clone();
        this.target=target;
        this.af=(AffineTransform)af.clone();
        presentationName="セグメント移動";
        redo();
    }
    public void redo() throws CannotRedoException{
        super.redo();
        JEnvironment env=viewer.getEnvironment();
        env.addClip(target.getBounds());
        for (int i=0;i<segments.size();i++){
            segments.get(i).transform(af);
        }
        target.updatePath();
        env.addClip(target.getBounds());
        
    }
    public void undo() throws CannotUndoException{
        super.undo();
        JEnvironment env=viewer.getEnvironment();
        AffineTransform inv=null;
        try{
            inv=af.createInverse();
        }catch(NoninvertibleTransformException e){
            return;
        }
        env.addClip(target.getBounds());
        for (int i=0;i<segments.size();i++){
            segments.get(i).transform(inv);
        }
        target.updatePath();
        env.addClip(target.getBounds());
        
    }    
}
