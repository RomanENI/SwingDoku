import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class PlayPanelBtnStateWatcher {

    private boolean isUndoState;
    private PropertyChangeSupport support;

    public JButton getPanelButton() {
        return panelButton;
    }

    private JButton panelButton;

    public PlayPanelBtnStateWatcher(JButton panButton){
        isUndoState = true;
        support = new PropertyChangeSupport(this);
        this.panelButton = panButton;


    }

    public void addPropertyChangeListener(PropertyChangeListener pcl){
        support.addPropertyChangeListener(pcl);
    }

    public void buttonChangeState(boolean newState){
        support.firePropertyChange("button clicked", this.isUndoState, newState);
        this.isUndoState = newState;

    }


}