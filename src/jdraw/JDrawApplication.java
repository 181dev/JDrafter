/*
 * JDrawApplication.java
 *
 * Created on 2008/05/18, 14:33
 */
package jdraw;

import com.lowagie.text.FontFactory;
import jactions.JCAGCulcActions;
import jactions.JEditActions;
import jactions.JObjectActions;
import jactions.JUndoRedoAction;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.undo.UndoableEdit;
import jdraw.typemenus.JTypeMenu;
import jedit.JInsertObjectsEdit;
import jedit.layeredit.PageSetupEdit;
import jobject.JDocument;
import jobject.JImageObject;
import jobject.JLeaf;
import jobject.JPage;
import jprinter.JPageFormat;
import jscreen.JDocumentViewer;
import jui.JPageNavigator;
import jui.JToolPanel;
import jui.layer.JLayerBrowser;
import plugins.AbstractPlugin;

/**
 *
 * @author  takashi
 */
public class JDrawApplication extends javax.swing.JFrame implements JFrameStateListener {

    //PDF�h�L�������g
    public static com.lowagie.text.pdf.PdfContentByte pdfContentByte=null;
    public static JFileChooser fileChooser = null;
    //�E�B���h�E�̃}�[�W��
    private static final int MERGIN = 4;
    //�C���^�[�i���t���[���̃}�[�W��
    private static final int FRAME_MERGIN_X = 4;
    //�t���[���}�[�W��
    private static final int FRAME_MERGIN_Y = 2;
    //�A���h�D�[�A�N�V����;
    private JUndoRedoAction undoRedoAction;
    //�G�f�B�b�g�A�N�V����
    private JEditActions editActions;
    //�I�u�W�F�N�g�A�N�V����
    private JObjectActions objectAction;
    //CAG���Z�A�N�V����
    private JCAGCulcActions cagActions;
    //View���j���[�A�N�V����
    private ViewMenus viewMenus;
    //ToolWindow;
    private JToolPanel toolPanel;
    //�y�[�W�i�r�Q�[�^;
    public JPageNavigator pageNavigator;
    //���C���[�u���E�U
    public JLayerBrowser layerBrowser;
    //�C���^�[�i���t���[���A�_�v�^
    private InternalFrameAdapter ifAdapter;
    //�|�b�v�A�b�v���j���[
    private JPopupMenu popup;
    //
    private FileMenus fileMenus;
    //�t�@�C�����j���[
    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenu objectMenu;
    private JTypeMenu typeMenu;
    public JMenu viewMenu;
    //�C���[�W�A�C�R��
    private ImageIcon appIcon = null;
    //�v���O�C��
    private Vector<AbstractPlugin> plugins = new Vector<AbstractPlugin>();

    /** Creates new form JDrawApplication */
    @SuppressWarnings("static-access")
    public JDrawApplication() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //

        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("JDocument�t�@�C��(*.jdoc)",
                "jdoc"));
        initComponents();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Dimension d = toolkit.getScreenSize();
        Insets insets = toolkit.getScreenInsets(gc);
        this.setBounds(insets.left + MERGIN, insets.top + MERGIN,
                d.width - insets.left - insets.right - 2 * MERGIN, d.height - insets.top - insets.bottom - 2 * MERGIN);
        //
        this.setTitle("JDrafter 1.2.4");
        //���j���[�\�z
        fileMenu = new JMenu("�t�@�C��(F)");
        editMenu = new JMenu("�ҏW(E)");
        objectMenu = new JMenu("�I�u�W�F�N�g(O)");
        typeMenu = new JTypeMenu();
        viewMenu = new JMenu("�\��(V)");
        setUpFileMenus();
        setUpEditMenu();
        setUpObjectMenu();
        setUpToolPanel();
        setUpPageNavigator();
        setUpLayerBrowser();
        setUpPopupMenu();
        //      setUpTypePalette();
        setUpViewMenu();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(objectMenu);
        menuBar.add(typeMenu);
        menuBar.add(viewMenu);
        readPlugins();
        setUpAbout();

        typeMenu.addSeparator();
        typeMenu.add(cagActions.outlineTextAction);
        //�C���^�[�i���t���[���A�_�v�^
        ifAdapter = new InternalFrameAdapter() {

            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                frameActivated(e);
            }

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                frameClosing(e);
            }
        };
        //�N���[�Y����
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing(e);

            }
        });
        //ContainerListener
        jDesktopPane1.addContainerListener(new InnerContainerListener());
        //
        appIcon = new ImageIcon(getClass().getResource("/jdicon.png"));
        this.setIconImage(appIcon.getImage());

    }

    public JDrawApplication(String[] args) {
        this();
        if (args != null && args.length > 0.) {
            File f = new File(args[0]);
            JDocument doc = null;
            try {
                ObjectInputStream stream = new ObjectInputStream(new FileInputStream(f));
                doc = (JDocument) stream.readObject();
                if (doc == null) {
                    throw new Exception();
                }
                stream.close();
            } catch (Exception e) {
                JOptionPane.showConfirmDialog(this, e.getLocalizedMessage(), "�t�@�C���G���[", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE);
                return;
            }
            JDocumentFrame frame = new JDocumentFrame();
            frame.setDocument(doc);
            frame.setFilePath(f.getParent());
            frame.setBounds(defaultWindowBounds());
            frame.addInternalFrameListener(ifAdapter);
            jDesktopPane1.add(frame, javax.swing.JLayeredPane.DEFAULT_LAYER, 0);
            frame.setVisible(true);
            frame.requestFocus();
            frame.addFrameStateListener(getApplication());
            frame.setChanged(false);
            jDesktopPane1.setSelectedFrame(frame);
        }
    }
    //���̃E�C���h�E������Ƃ��̏���
    private void thisWindowClosing(WindowEvent e) {
        JInternalFrame[] frames = jDesktopPane1.getAllFrames();
        for (int i = 0; i < frames.length; i++) {
            JDocumentFrame f = (JDocumentFrame) frames[i];
            if (!frameClose(f)) {
                return;
            }
        }
        this.dispose();
    }
    //�t���[�����A�N�e�B�u�ɂȂ�Ƃ��̏���.
    private void frameActivated(InternalFrameEvent e) {
        JDocumentFrame source = (JDocumentFrame) e.getSource();
        setDocumentState(source.getViewer());
        source.getViewer().getDragPane().setComponentPopupMenu(popup);
    }
    //�R���g���[���̃h�L�������g�ݒ�
    private void setDocumentState(JDocumentViewer viewer) {
        editActions.setViewer(viewer);
        objectAction.setViewer(viewer);
        cagActions.setViewer(viewer);
        typeMenu.setViewer(viewer);
        typePanel.setViewer(viewer);
        for (AbstractPlugin p : plugins) {
            p.setViewer(viewer);
        }
        if (viewer != null) {
            undoRedoAction.setDocument(viewer.getDocument());
            toolPanel.setDragPane(viewer.getDragPane());
            pageNavigator.setDocument(viewer.getDocument());
            layerBrowser.setDocument(viewer.getDocument());
        } else {
            undoRedoAction.setDocument(null);
            toolPanel.setDragPane(null);
            pageNavigator.setDocument(null);
            layerBrowser.setDocument(null);

        }
        toolPanel.repaint();
        pageNavigator.repaint();
        layerBrowser.repaint();
//        typePalette.repaint();
        fileMenus.updateStates();
        if (viewer != null) {
            viewer.getDocument().addenvironmentChangeListener(viewMenus);
            //viewer.getEnvironment().addChangeListener(viewMenus);
            viewer.getDocument().addItemListener(viewMenus);
        }
        viewMenus.changeStetes();
    }
    //�t���[�������
    private void frameClosing(InternalFrameEvent e) {
        JDocumentFrame f = (JDocumentFrame) e.getInternalFrame();
        frameClose(f);
    }
    //�t���[���N���[�Y����
    private boolean frameClose(JDocumentFrame frame) {
        boolean ret = true;
        if (frame.isChanged()) {
            switch (JOptionPane.showConfirmDialog(this, frame.getDocument().getName() + "�͕ύX����Ă��܂�.�ۑ����܂���?",
                    "", JOptionPane.YES_NO_CANCEL_OPTION)) {
                case JOptionPane.YES_OPTION:
                    if (frame.getFilePath().equals("") || frame.getFilePath() == null) {
                        ret = saveDocumentAs();
                    } else {
                        ret = saveDocument();
                    }
                    break;
                case JOptionPane.NO_OPTION:
                    ret = true;
                    break;
                case JOptionPane.CANCEL_OPTION:
                    ret = false;
                    break;
                default:
                    ret = false;
                    break;
            }
        }
        if (!ret) {
            return false;
        }
        if (jDesktopPane1.getAllFrames().length == 1) {
            setDocumentState(null);
        }
        frame.removeFrameStateListener(this);
        frame.dispose();
        return true;
    }
    //�t�@�C�����j���[�\�z
    private void setUpFileMenus() {
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenus = new FileMenus();
        fileMenu.add(fileMenus.newDocument);
        fileMenu.add(fileMenus.openDocument);
        fileMenu.addSeparator();
        fileMenu.add(fileMenus.diployImage);
        fileMenu.addSeparator();
        fileMenu.add(fileMenus.saveDocument);
        fileMenu.add(fileMenus.saveDocumentAs);
        fileMenu.add(fileMenus.exportAsPDF);
        fileMenu.add(fileMenus.exportAsImage);
        fileMenu.addSeparator();
        fileMenu.add(fileMenus.pageSetup);
        fileMenu.add(fileMenus.print);
        fileMenu.addSeparator();
        fileMenu.add(fileMenus.quit);
    }
    //�G�f�B�b�g���j���[�\�z
    private void setUpEditMenu() {
        editMenu.setMnemonic(KeyEvent.VK_E);
        undoRedoAction = new JUndoRedoAction();
        editMenu.add(undoRedoAction.undoAction);
        editMenu.add(undoRedoAction.redoAction);
        editMenu.addSeparator();
        editActions = new JEditActions();
        editMenu.add(editActions.cutAction);
        editMenu.add(editActions.copyAction);
        editMenu.add(editActions.pasteAction);
        editMenu.add(editActions.clearAction);
        editMenu.addSeparator();
        editMenu.add(editActions.selectAllAction);
    }
    //�I�u�W�F�N�g���j���[�\�z
    @SuppressWarnings({"static-access", "static-access"})
    private void setUpObjectMenu() {
        objectMenu.setMnemonic(KeyEvent.VK_O);
        objectAction = new JObjectActions();
        objectMenu.add(objectAction.reshapeAgain);
        JMenu transform = new JMenu("�ό`(T)");
        transform.setMnemonic(KeyEvent.VK_T);
        transform.add(objectAction.translateAction);
        transform.add(objectAction.scaleAction);
        transform.add(objectAction.shearAction);
        transform.add(objectAction.rotateAction);
        transform.add(objectAction.reflectAction);
        objectMenu.add(transform);
        objectMenu.addSeparator();
        objectMenu.add(objectAction.doGroup);
        objectMenu.add(objectAction.unGroup);
        objectMenu.addSeparator();
        JMenu arrange = new JMenu("�d�ˏ��iA)");
        arrange.setMnemonic(KeyEvent.VK_A);
        arrange.add(objectAction.bringToTop);
        arrange.add(objectAction.bringFront);
        arrange.add(objectAction.sendBack);
        arrange.add(objectAction.sendToBottom);
        objectMenu.add(arrange);
        JMenu sort = new JMenu("����(S)");
        sort.setMnemonic(KeyEvent.VK_S);
        sort.add(objectAction.alignHCenter);
        sort.add(objectAction.alignTop);
        sort.add(objectAction.alignBottom);
        sort.addSeparator();
        sort.add(objectAction.alignVCenter);
        sort.add(objectAction.alignLeft);
        sort.add(objectAction.alignRight);
        sort.addSeparator();
        sort.add(objectAction.vJustify);
        sort.add(objectAction.hJustify);
        objectMenu.add(sort);
        cagActions = new JCAGCulcActions();
        JMenu CAGCulc = new JMenu("CAG���Z(C)");
        CAGCulc.setMnemonic(KeyEvent.VK_C);
        CAGCulc.add(cagActions.addAction);
        CAGCulc.add(cagActions.subAction);
        CAGCulc.add(cagActions.multAction);
        CAGCulc.add(cagActions.xorAction);
        objectMenu.addSeparator();
        objectMenu.add(CAGCulc);
        JMenu compoundPath = new JMenu("�����p�X(M)");
        compoundPath.setMnemonic(KeyEvent.VK_M);
        compoundPath.add(cagActions.compoundPathAction);
        compoundPath.add(cagActions.releaseCompoundPathAction);
        objectMenu.add(compoundPath);
        objectMenu.add(cagActions.joinPathAction);
        objectMenu.add(cagActions.uniformJoinAction);
        objectMenu.add(cagActions.reversePathAction);
        objectMenu.addSeparator();
        JMenu effectMenu = new JMenu("�G�t�F�N�g(E)");
        effectMenu.setMnemonic(KeyEvent.VK_E);
        effectMenu.add(cagActions.releaseEffectAction);
        effectMenu.addSeparator();
        effectMenu.add(cagActions.makeBlurAction);
        effectMenu.add(cagActions.dropShadowAction);
        effectMenu.add(cagActions.makeArrowAction);
        objectMenu.add(effectMenu);
        //
        JMenu outlineMenu = new JMenu("�A�E�g���C��(O)");
        outlineMenu.setMnemonic(KeyEvent.VK_O);
        outlineMenu.add(cagActions.outlineTextAction);
        outlineMenu.add(cagActions.strokeOutlineAction);
        objectMenu.add(outlineMenu);
        //
        objectMenu.addSeparator();
        objectMenu.add(objectAction.makePattern);
        //
        JMenu trimingMenu=new JMenu("�C���[�W�g���~���O(I)");
        trimingMenu.setMnemonic(KeyEvent.VK_I);
        trimingMenu.add(objectAction.makeTriming);
        trimingMenu.add(objectAction.releaseTriming);
        objectMenu.add(trimingMenu);
        /*
        JMenu patternMenu=new JMenu("�p�^�[���쐬(P)");
        patternMenu.setMnemonic(KeyEvent.VK_P);
        patternMenu.add(objectAction.makePattern);
        patternMenu.add(objectAction.makeClipedPattern);
        objectMenu.add(patternMenu);
         */
    }
    //�\�����j���[�\�z
    private void setUpViewMenu() {
        viewMenu.setMnemonic(KeyEvent.VK_V);
        viewMenus = new ViewMenus(this);
        viewMenu.add(viewMenus.layerVisible);
        viewMenu.add(viewMenus.pageVisible);
        viewMenu.addSeparator();
        viewMenu.add(viewMenus.snapGrid);
        viewMenu.add(viewMenus.gridVisible);
        viewMenu.add(viewMenus.gridGauge);
        viewMenu.add(viewMenus.gridForeAction);
        viewMenu.addSeparator();
        JMenu guidMenu = new JMenu("�K�C�h���C��(I)");
        guidMenu.setMnemonic(KeyEvent.VK_I);
        guidMenu.add(viewMenus.sendGuidAction);
        guidMenu.add(viewMenus.releaseGuidAction);
        guidMenu.add(viewMenus.lockGuidAction);
        guidMenu.add(viewMenus.showGuidAction);
        guidMenu.add(viewMenus.clearGuidAction);
        viewMenu.add(guidMenu);
        viewMenu.addSeparator();
        viewMenu.add(viewMenus.snapAncurMenu);
        viewMenu.addSeparator();
        viewMenu.add(viewMenus.gridOption);
        viewMenu.addSeparator();
        jDesktopPane1.addContainerListener(viewMenus);
    }
    //�|�b�v�A�b�v���j���[�\�z
    @SuppressWarnings("static-access")
    private void setUpPopupMenu() {
        popup = new JPopupMenu();
        popup.add(undoRedoAction.undoAction);
        popup.add(undoRedoAction.redoAction);
        popup.addSeparator();
        popup.add(editActions.cutAction);
        popup.add(editActions.copyAction);
        popup.add(editActions.pasteAction);
        popup.addSeparator();
        popup.add(objectAction.doGroup);
        popup.add(objectAction.unGroup);
        popup.addSeparator();
        JMenu itm = new JMenu("�d�ˏ�");
        itm.add(objectAction.bringToTop);
        itm.add(objectAction.bringFront);
        itm.add(objectAction.sendBack);
        itm.add(objectAction.sendToBottom);
        popup.add(itm);
        itm = new JMenu("����");
        itm.add(objectAction.alignHCenter);
        itm.add(objectAction.alignTop);
        itm.add(objectAction.alignBottom);
        itm.addSeparator();
        itm.add(objectAction.alignVCenter);
        itm.add(objectAction.alignLeft);
        itm.add(objectAction.alignRight);
        itm.addSeparator();
        itm.add(objectAction.hJustify);
        itm.add(objectAction.vJustify);
        popup.add(itm);
        popup.addSeparator();
        popup.add(objectAction.reshapeAgain);
        itm = new JMenu("�ό`");
        itm.add(objectAction.translateAction);
        itm.add(objectAction.rotateAction);
        itm.add(objectAction.scaleAction);
        itm.add(objectAction.shearAction);
        popup.add(itm);


    }
    //�c�[���E�C���h�E����
    private void setUpToolPanel() {
        toolPanel = new JToolPanel(this);
        Insets inset = this.getInsets();
        Window f = (Window) toolPanel.getRootPane().getParent();
        int y = getY() + inset.top + (getHeight() - inset.top - inset.bottom - f.getHeight()) / 2;
        f.setLocation(this.getX() + inset.left, y);
    }
    //�y�[�W�i�r�Q�[�^����
    private void setUpPageNavigator() {
        pageNavigator = new JPageNavigator(this, false);
        Insets inset = this.getInsets();
        Window f = (Window) pageNavigator.getRootPane().getParent();
        f.setLocation(this.getX() + this.getWidth() - inset.right - f.getWidth(), this.getY() + this.getHeight() - inset.bottom - f.getHeight());
        pageNavigator.setVisible(true);
    }
    //���C���[�u���E�U����
    private void setUpLayerBrowser() {
        layerBrowser = new JLayerBrowser(this, false);
        Insets inset = this.getInsets();
        Window f = (Window) layerBrowser.getRootPane().getParent();
        int y = getY() + getHeight() - inset.bottom - pageNavigator.getRootPane().getParent().getHeight() - f.getHeight();
        int x = getX() + getWidth() - inset.right - f.getWidth();
        f.setLocation(x, y);
        layerBrowser.setVisible(true);

    }
    //�C���^�[�i���t���[���̐V�K�T�C�Y�擾
    private Rectangle defaultWindowBounds() {
        int width = Math.max(jDesktopPane1.getWidth() - FRAME_MERGIN_X * 2, 800);
        int height = Math.max(jDesktopPane1.getHeight() - FRAME_MERGIN_Y * 2, 600);
        return new Rectangle(FRAME_MERGIN_X, FRAME_MERGIN_Y, width, height);
    }
    //�h�L�������g�ɖ��O�����ĕۑ�
    private boolean saveDocumentAs() {
        JDocumentFrame frame = (JDocumentFrame) jDesktopPane1.getSelectedFrame();
        if (frame == null) {
            return false;
        }
        fileChooser.setDialogTitle("���O�����ĕۑ�");
        File f = fileChooser.getCurrentDirectory();
        f = new File(f.getPath(), frame.getDocument().getName() + ".jdoc");
        fileChooser.setSelectedFile(f);
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return false;
        }
        f = fileChooser.getSelectedFile();
        if (f.exists() &&
                JOptionPane.showConfirmDialog(this, f.getName() + "�͊��ɑ��݂��܂�.�㏑�����܂���(Y/N)") != JOptionPane.OK_OPTION) {
            return false;
        }
        frame.setFilePath(f.getParent());
        frame.getDocument().setName(f.getName().replace(".jdoc", ""));
        frame.setTitle(frame.getDocument().getName());
        ViewMenus.DocumentSelector v = viewMenus.getDcomSelector(frame);
        if (v != null) {
            v.setText(frame.getDocument().getName());
        }
        return saveDocument();
    }
    //�h�L�������g��ۑ�
    private boolean saveDocument() {
        JDocumentFrame frame = (JDocumentFrame) jDesktopPane1.getSelectedFrame();
        File f = new File(frame.getFilePath(), frame.getDocument().getName() + ".jdoc");
        try {
            ObjectOutputStream ostream = new ObjectOutputStream(new FileOutputStream(f));
            //XMLEncoder ostream=new XMLEncoder(new BufferedOutputStream(new FileOutputStream(f)));
            ostream.writeObject(frame.getDocument());
            frame.setChanged(false);
            ostream.close();

        } catch (Exception ex) {
            JOptionPane.showConfirmDialog(this, ex.getLocalizedMessage(), "�t�@�C���G���[", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        fileMenus.updateStates();
        return true;
    }
    //�h�L�������g���J��
    private boolean openDocument() {
        fileChooser.setDialogTitle("�h�L�������g���J��");
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return false;
        }
        File f = fileChooser.getSelectedFile();
        JDocument doc = null;
        try {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(f));
            doc = (JDocument) stream.readObject();
            if (doc == null) {
                throw new Exception();
            }
            stream.close();
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(this, e.getMessage(), "�t�@�C���G���[", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        JDocumentFrame frame = new JDocumentFrame();
        doc.setName(f.getName().replace(".jdoc", ""));
        frame.setDocument(doc);
        frame.setFilePath(f.getParent());
        frame.setBounds(defaultWindowBounds());
        frame.addInternalFrameListener(ifAdapter);
        jDesktopPane1.add(frame, javax.swing.JLayeredPane.DEFAULT_LAYER, 0);
        frame.setVisible(true);
        frame.requestFocus();
        frame.addFrameStateListener(getApplication());
        frame.setChanged(false);
        jDesktopPane1.setSelectedFrame(frame);
        //
        return true;
    }
    //PDF�ɏo��
    private void saveAsPDF() {
        JDocumentFrame frame = (JDocumentFrame) jDesktopPane1.getSelectedFrame();
        if (frame == null) {
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("PDF�t�@�C��", "pdf"));
        chooser.setDialogTitle("PDF�ɏ����o��");
        File f = chooser.getCurrentDirectory();
        f = new File(f.getPath(), frame.getDocument().getName() + ".pdf");
        chooser.setSelectedFile(f);
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        JDocument doc = frame.getDocument();
        f = chooser.getSelectedFile();
        if (f.exists() &&
                JOptionPane.showConfirmDialog(this, f.getName() + "�͊��ɑ��݂��܂�.�㏑�����܂���(Y/N)") != JOptionPane.OK_OPTION) {
            return;
        }
        frame.getViewer().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        com.lowagie.text.Document pDoc = new com.lowagie.text.Document();
        try {
            FileOutputStream wt = new FileOutputStream(f);
            BufferedOutputStream bout = new BufferedOutputStream(wt);
            com.lowagie.text.pdf.PdfWriter pwriter = com.lowagie.text.pdf.PdfWriter.getInstance(pDoc, bout);
            pDoc.open();
            FontFactory.registerDirectories();
            Set set=FontFactory.getRegisteredFonts();

            for (int i = 0; i < doc.size(); i++) {
                JPage cPage = doc.get(i);
                PageFormat pFormat = cPage.getPageFormat();
                com.lowagie.text.Rectangle rc = new com.lowagie.text.Rectangle((float) pFormat.getWidth(), (float) pFormat.getHeight());
                float left = (float) (pFormat.getImageableX());
                float right = (float) (pFormat.getWidth() - pFormat.getImageableWidth() - left);
                float top = (float) pFormat.getImageableX();
                float bottom = (float) (pFormat.getHeight() - pFormat.getImageableHeight() - top);
                pDoc.newPage();

                
                com.lowagie.text.pdf.PdfContentByte cb =pdfContentByte= pwriter.getDirectContent();
                cb.saveState();
              
                Graphics2D g2 = (com.lowagie.text.pdf.PdfGraphics2D) cb.createGraphics((float) pFormat.getWidth(), (float) pFormat.getHeight());

                boolean vg = cPage.getGuidLayer().isVisible();
                cPage.getGuidLayer().setVisible(false);
                cPage.paint(new Rectangle.Double(0, 0, pFormat.getWidth(), pFormat.getHeight()), g2);
                cPage.getGuidLayer().setVisible(vg);
                g2.dispose();
                cb.restoreState();
            }
            pDoc.close();
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(this, e.getMessage(), "�t�@�C���G���[", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE);
        }
        frame.getViewer().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }
    //�C���[�W��z�u
    private void diployImage() {
        JDocumentFrame frame = (JDocumentFrame) jDesktopPane1.getSelectedFrame();
        if (frame == null) {
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("�C���[�W�t�@�C��(jpeg,png,gif)", "jpg", "png", "gif"));
        chooser.setDialogTitle("�C���[�W�̔z�u");
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File f = chooser.getSelectedFile();
        ImageIcon img = null;
        try {
            img = new ImageIcon(f.toURI().toURL());
        } catch (Exception ex) {
            JOptionPane.showConfirmDialog(this, ex.getLocalizedMessage(), "�t�@�C���G���[", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE);
            return;
        }
        PageFormat pg = frame.getViewer().getCurrentPage().getPageFormat();
        double x = (pg.getWidth() - img.getIconWidth()) / 2;
        double y = (pg.getHeight() - img.getIconHeight()) / 2;
        JImageObject jio = new JImageObject(img.getImage(), new Point2D.Double(x, y));
        Vector<JLeaf> objs = new Vector<JLeaf>(1);
        objs.add(jio);
        UndoableEdit edt = new JInsertObjectsEdit(frame.getViewer(), objs);
        frame.getViewer().getDocument().fireUndoEvent(edt);
        frame.getViewer().repaint();
    }
    //�A�v���P�[�V�����擾
    private JDrawApplication getApplication() {
        return this;
    }
    //�t�@�C�����j���[�A�N�V����
    private class FileMenus {

        public NewDocument newDocument;
        public OpenDocument openDocument;
        public DiployImage diployImage;
        public SaveDocument saveDocument;
        public SaveDocumentAs saveDocumentAs;
        public ExportAsPDF exportAsPDF;
        public ExportAsImage exportAsImage;
        public PageSetup pageSetup;
        public Print print;
        public Quit quit;

        public FileMenus() {
            newDocument = new NewDocument();
            openDocument = new OpenDocument();
            diployImage = new DiployImage();
            saveDocument = new SaveDocument();
            saveDocumentAs = new SaveDocumentAs();
            exportAsPDF = new ExportAsPDF();
            exportAsImage = new ExportAsImage();
            pageSetup = new PageSetup();
            print = new Print();
            quit = new Quit();
        }

        public void updateStates() {
            JDocumentFrame frame = (JDocumentFrame) jDesktopPane1.getSelectedFrame();
            if (frame == null) {
                diployImage.setEnabled(false);
                saveDocument.setEnabled(false);
                saveDocumentAs.setEnabled(false);
                exportAsPDF.setEnabled(false);
                exportAsImage.setEnabled(false);
                pageSetup.setEnabled(false);
                print.setEnabled(false);


            } else {
                diployImage.setEnabled(true);
                saveDocument.setEnabled(frame.isChanged());
                saveDocumentAs.setEnabled(true);
                exportAsPDF.setEnabled(true);
                exportAsImage.setEnabled(true);
                pageSetup.setEnabled(true);
                print.setEnabled(true);
            }
        }

        private class NewDocument extends AbstractAction {

            public NewDocument() {
                putValue(NAME, "�V�K(N)");
                putValue(MNEMONIC_KEY, KeyEvent.VK_N);
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
                setEnabled(true);
            }

            @Override
            @SuppressWarnings("static-access")
            public void actionPerformed(ActionEvent e) {
                JDocument doc = new JDocument();
                getApplication().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                PageFormat pageFormat = doc.getCurrentPage().getPageFormat();
                PageFormat np = doc.getPrinterJob().pageDialog(pageFormat);
                if (np == pageFormat) {
                    getApplication().setCursor(Cursor.getDefaultCursor());
                    return;
                }
                pageFormat = np;
                getApplication().setCursor(Cursor.getDefaultCursor());
                doc.getCurrentPage().setPageFormat(new JPageFormat(pageFormat));
                doc.setName(getProperName("Untitled-"));
                JDocumentFrame frame = new JDocumentFrame();
                frame.setVisible(true);
                frame.setDocument(doc);
                doc.getCurrentPage().getCurrentLayer().setPreviewColor(frame.getViewer().getEnvironment().PREVIEW_COLORS[0]);
                frame.setBounds(defaultWindowBounds());
                frame.addInternalFrameListener(ifAdapter);
                jDesktopPane1.add(frame, javax.swing.JLayeredPane.DEFAULT_LAYER, 0);
                frame.requestFocus();
                frame.addFrameStateListener(getApplication());
                jDesktopPane1.setSelectedFrame(frame);
            }
        }

        private class OpenDocument extends AbstractAction {

            public OpenDocument() {
                putValue(NAME, "�J��(O)");
                putValue(MNEMONIC_KEY, KeyEvent.VK_O);
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
                setEnabled(true);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                openDocument();

            }
        }

        private class DiployImage extends AbstractAction {

            public DiployImage() {
                putValue(NAME, "�C���[�W�̔z�u(D)");
                putValue(MNEMONIC_KEY, KeyEvent.VK_D);
                setEnabled(false);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                diployImage();
            }
        }

        private class SaveDocument extends AbstractAction {

            public SaveDocument() {
                putValue(NAME, "�ۑ�(S)");
                putValue(MNEMONIC_KEY, KeyEvent.VK_S);
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
                setEnabled(false);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                JDocumentFrame frame = (JDocumentFrame) jDesktopPane1.getSelectedFrame();
                if (frame == null) {
                    return;
                }
                if (frame.getFilePath().equals("")) {
                    saveDocumentAs();
                } else {
                    saveDocument();
                }
            }
        }

        private class SaveDocumentAs extends AbstractAction {

            public SaveDocumentAs() {
                putValue(NAME, "���O�����ĕۑ�(A)");
                putValue(MNEMONIC_KEY, KeyEvent.VK_A);
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
                setEnabled(false);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                JDocumentFrame frame = (JDocumentFrame) jDesktopPane1.getSelectedFrame();
                if (frame == null) {
                    return;
                }
                saveDocumentAs();
            }
        }

        private class ExportAsPDF extends AbstractAction {

            public ExportAsPDF() {
                putValue(NAME, "PDF�ɕۑ�(E)");
                putValue(MNEMONIC_KEY, KeyEvent.VK_E);
                setEnabled(false);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                JDocumentFrame frame = (JDocumentFrame) jDesktopPane1.getSelectedFrame();
                if (frame != null) {
                    saveAsPDF();
                }
            }
        }

        private class ExportAsImage extends AbstractAction {

            public ExportAsImage() {
                putValue(NAME, "�C���[�W�ɕۑ�(I)");
                putValue(MNEMONIC_KEY, KeyEvent.VK_I);
                setEnabled(false);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                JDocumentFrame frame = (JDocumentFrame) jDesktopPane1.getSelectedFrame();
                if (frame != null) {
                    ImageSaveDialog.showAsDialog(getApplication(), frame.getViewer().getCurrentPage());
                }
            }
        }

        private class PageSetup extends AbstractAction {

            public PageSetup() {
                putValue(NAME, "�y�[�W�ݒ�(U)");
                putValue(MNEMONIC_KEY, KeyEvent.VK_U);
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
                setEnabled(false);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                JDocumentFrame frame = (JDocumentFrame) jDesktopPane1.getSelectedFrame();
                if (frame == null) {
                    return;
                }
                JPage page = frame.getDocument().getCurrentPage();
                PageFormat pageFormat = page.getPageFormat();
                getApplication().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                PageFormat np = frame.getDocument().getPrinterJob().pageDialog(pageFormat);
                getApplication().setCursor(Cursor.getDefaultCursor());
                if (np == pageFormat) {
                    return;
                }
                UndoableEdit anEdit = new PageSetupEdit(frame.getViewer(), page, new JPageFormat(np));
                frame.getDocument().fireUndoEvent(anEdit);
            }
        }

        private class Print extends AbstractAction {

            public Print() {
                putValue(NAME, "���(P)");
                putValue(MNEMONIC_KEY, KeyEvent.VK_P);
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
                setEnabled(false);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                JDocumentFrame frame = (JDocumentFrame) jDesktopPane1.getSelectedFrame();
                if (frame == null) {
                    return;
                }
                JDocument doc = frame.getDocument();
                PrinterJob pjob = frame.getDocument().getPrinterJob();
                pjob.setPageable(doc);
                if (pjob.printDialog()) {
                    try {
                        pjob.print();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        private class Quit extends AbstractAction {

            public Quit() {
                putValue(NAME, "�I��(X)");
                putValue(MNEMONIC_KEY, KeyEvent.VK_X);
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
                setEnabled(true);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                thisWindowClosing(new WindowEvent(getApplication(), WindowEvent.WINDOW_CLOSING));
            }
        }
    }
    //���C�Z���X�\��
    private class LicenceAction extends AbstractAction {

        public LicenceAction() {
            putValue(NAME, "���C�Z���X(L)");
            putValue(MNEMONIC_KEY, KeyEvent.VK_L);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Licence.showAsDialog(getApplication());
        }
    }
    //�A�o�E�g�\��
    private class AboutAction extends AbstractAction {

        public AboutAction() {
            putValue(NAME, "JDrafter�ɂ���(A)");
            putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            AboutDialog.showAbout(getApplication());
        }
    }
    //�w���v
    private class HelpAction extends AbstractAction {

        private URI helpURI = null;

        public HelpAction(URI uri) {
            putValue(NAME, "�w���v(F1)");
            putValue(MNEMONIC_KEY, KeyEvent.VK_F1);
            helpURI = uri;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (helpURI != null) {
                try {
                    Desktop.getDesktop().browse(helpURI);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    //�V�K�h�L�������g���̎擾
    private String getProperName(String prefixer) {
        int index = 0;
        String regex = prefixer + "\\d+$";
        JInternalFrame[] frames = jDesktopPane1.getAllFrames();
        for (int i = 0; i < frames.length; i++) {
            String nm = ((JDocumentFrame) frames[i]).getViewer().getDocument().getName();
            if (nm.matches(regex)) {
                index = Integer.valueOf(nm.replace(prefixer, ""));
            }
        }
        index++;
        return prefixer + String.valueOf(index).trim();
    }
    //�t���[���̕ύX
    @Override
    public void frameStateChanged(JDocumentFrame f) {
        if (fileMenus != null) {
            fileMenus.updateStates();
        }
    }
    //�ŋߎg�����t�H���g�̓o�^
    public void addResentFont(AttributeSet attr) {
        typeMenu.getResetnFonts().addItem(attr);
    }
    //�v���O�C���̓ǂݏo
    URLClassLoader classLoader = null;

    private void readPlugins() {
        File f = new File(System.getProperty("user.dir") + File.separator + "extention" + File.separator + "plugins");
        if (!f.exists() || !f.isDirectory()) {
            return;
        }
        JMenu menu = new JMenu("�v���O�C��(P)");
        menu.setMnemonic('P');
        //
        URL[] urls = new URL[1];
        try {
            urls[0] = f.getParentFile().toURI().toURL();
        } catch (MalformedURLException ex) {
            Logger.getLogger(JDrawApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
        classLoader = new URLClassLoader(urls);
        String prefix = "plugins";
        addPlugins(f, prefix, menu);
        this.menuBar.add(menu);
    }

    private void addPlugins(File f, String prefix, JMenu menu) {
        File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                if (files[i].getName().contains(".class")) {
                    String className = prefix + "." + files[i].getName().replace(".class", "");
                    Object o;
                    try {
                        o = classLoader.loadClass(className).newInstance();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        continue;
                    }
                    if (o instanceof AbstractPlugin) {
                        plugins.add((AbstractPlugin) o);
                        menu.add((AbstractPlugin) o);
                    }
                }
            } else {
                String nm = files[i].getName();
                JMenu sb = new JMenu(nm);
                addPlugins(files[i], prefix + "." + nm, sb);
                menu.add(sb);
            }
        }
    }
    //�A�o�E�g���j���[
    private void setUpAbout() {
        JMenu menu = new JMenu("�A�o�E�g(A)");
        menu.setMnemonic(KeyEvent.VK_A);
        File f = new File(System.getProperty("user.dir") + File.separator + "help" + File.separator + "index.html");
        if (f.exists()) {
            menu.add(new HelpAction(f.toURI()));
            menu.addSeparator();
        }
        menu.add(new LicenceAction());
        menu.addSeparator();
        menu.add(new AboutAction());
        this.menuBar.add(menu);
    }
    //ContainerListener
    public class InnerContainerListener implements ContainerListener {

        @Override
        public void componentAdded(ContainerEvent e) {
            if (fileMenus != null) {
                fileMenus.updateStates();
            }
        }

        @Override
        public void componentRemoved(ContainerEvent e) {
            if (fileMenus != null) {
                fileMenus.updateStates();
            }
        }
    }
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        magCombo = new jdraw.MagnifyComboBox();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jToolBar1 = new javax.swing.JToolBar();
        typePanel = new jdraw.textpalette.TypePanel();
        jPagePanel1 = new jdraw.JPagePanel();
        menuBar = new javax.swing.JMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("JDraw"); // NOI18N

        jPanel1.setMinimumSize(new java.awt.Dimension(20, 22));
        jPanel1.setPreferredSize(new java.awt.Dimension(20, 22));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 1));

        magCombo.setPreferredSize(new java.awt.Dimension(90, 19));
        jPanel1.add(magCombo);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jDesktopPane1.setBackground(new java.awt.Color(102, 102, 102));
        getContentPane().add(jDesktopPane1, java.awt.BorderLayout.CENTER);

        jToolBar1.setFloatable(false);
        jToolBar1.add(typePanel);
        jToolBar1.add(jPagePanel1);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.NORTH);
        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * @param args the command line arguments
     */
    public static void main(final String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JDrawApplication(args).setVisible(true);
            }
        });
    }
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JDesktopPane jDesktopPane1;
    public jdraw.JPagePanel jPagePanel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToolBar jToolBar1;
    public jdraw.MagnifyComboBox magCombo;
    private javax.swing.JMenuBar menuBar;
    public jdraw.textpalette.TypePanel typePanel;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>

}
