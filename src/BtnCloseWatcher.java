import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class BtnCloseWatcher {

    private PropertyChangeSupport support;
    private boolean closed;

    public PanelOnRight.PanelCloseButton getBtnCloseInPanel() {
        return btnCloseInPanel;
    }

    public void setBtnCloseInPanel(PanelOnRight.PanelCloseButton btnCloseInPanel) {
        this.btnCloseInPanel = btnCloseInPanel;
    }

    private PanelOnRight.PanelCloseButton btnCloseInPanel;

    public BtnCloseWatcher(PanelOnRight.PanelCloseButton panButton){

        support = new PropertyChangeSupport(this);
        this.btnCloseInPanel = panButton;
        closed = false;
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl){
        support.addPropertyChangeListener(pcl);
    }

    public void buttonChangeState() {
        support.firePropertyChange("button clicked", closed, this.getBtnCloseInPanel().getParent());
        this.closed = true;
    }
}
