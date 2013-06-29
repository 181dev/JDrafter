/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plugins;

import jactions.JUndoRedoEvent;
import jactions.JUndoRedoListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractAction;
import jscreen.JDocumentViewer;

/**
 *JDrafter �A�v���P�[�V�����̃v���O�C���̊��N���X�ł��B<BR>
 * �v���O�C���́A�S�Ă��̒��ۃN���X���p�����Ȃ���΂Ȃ�܂���B
 * �R���p�C�������N���X�̓A�v���P�[�V�����Ɠ����p�X��plugins�t�H���_��
 * �z�u���܂��B�܂��Aplugins�t�H���_�ɃT�u�t�H���_��z�u����ƃA�v���P�[�V������
 * �T�u�t�H���_���Ɠ����̃T�u���j���[���\�z���܂��B�T�u���j���[�Ƀj�[���j�b�N���w��
 * �������ꍇ�̓T�u�t�H���_��������"("+�j�[���j�b�N+")"���t�H���_���Ƃ��Ďw�肵�܂��B
 * 
 * @author Ikita 
 */
public abstract class AbstractPlugin extends AbstractAction {

    private JDocumentViewer viewer = null;
    private InnerListener innerListener = null;

    /**
     * �f�t�H���g�R���X�g���N�^�[�ł��B
     */
    public AbstractPlugin() {
        innerListener = new InnerListener();
    }

    /**
     * �A�v���P�[�V�����ɂ��Viewer���ύX���ꂽ�Ƃ��ɌĂ΂�܂�.
     * @param v �A�N�e�B�u��JDocumentViewer �A�N�e�B�u��viewer���Ȃ��ꍇ��null;
     */
    public final void setViewer(JDocumentViewer v) {
        if (viewer != v) {
            if (viewer != null) {
                viewer.getDocument().removeUndoRedoListener(innerListener);
                viewer.getDocument().removeItemListener(innerListener);
            }
            viewer = v;
            if (viewer != null) {
                viewer.getDocument().addItemListener(innerListener);
                viewer.getDocument().addUndoRedoListener(innerListener);
            }
            changeStates();
        }
    }

    /**
     * �A�N�e�B�u��JDocumentViewer���擾���܂�.
     * @return �A�N�e�B�u��Viewer.�A�N�e�B�u�ȃr���A�[���Ȃ��ꍇ��null
     */
    public final JDocumentViewer getViewer() {
        return viewer;
    }

    /**
     * �h�L�������g�ɉ��炩�̕ύX��������ꂽ�ꍇ�A�J�����g�h�L�������g���ύX���ꂽ�ꍇ
     * �������͑I�����ύX���ꂽ�ꍇ�̏������L�q���܂�.
     * @param viewer �A�N�e�B�u��DocumentViewer�B�A�N�e�B�u��DocumentViewer���Ȃ��ꍇ��null
     */
    public abstract void changeStates();

    private class InnerListener implements ItemListener, JUndoRedoListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            changeStates();
        }
        @Override
        public void undoRedoEventHappened(JUndoRedoEvent e) {
            changeStates();
        }
    }
}
