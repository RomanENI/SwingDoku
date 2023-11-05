import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ObservableActionThing {


        private int value;
        private int xCord;
        private int yCord;


        private PropertyChangeSupport support;

        public ObservableActionThing(int constructorValue, int panelCoordX, int panelCoordY) {

            support = new PropertyChangeSupport(this);
            this.xCord = panelCoordX;
            this.yCord = panelCoordY;
            this.value = constructorValue;
        }

        public void addPropertyChangeListener(PropertyChangeListener pcl) {
            support.addPropertyChangeListener(pcl);
        }

        public void removePropertyChangeListener(PropertyChangeListener pcl) {
            support.removePropertyChangeListener(pcl);
        }

        public void panelChangeValue(int newValue) {
            support.firePropertyChange("panel modified", this.value, newValue);
            this.value = newValue;
        }

        public void panelChangeValueFromButton(int formerValue, int newValue){
            support.firePropertyChange("modified from button", formerValue, newValue);
            this.value = newValue;
        }

        public void panelGoesToClueMode(){
            support.firePropertyChange("clue", false, true);
        }

        public void panelClueHandling(int clueValue, boolean newState){
            support.firePropertyChange("clueHandling", clueValue, newState);
        }


        public int getValue(){
            return this.value;
        }

        public int getxCord(){
            return this.xCord;
        }

        public int getyCord(){
            return this.yCord;
        }

        public void setValue(int newValue){
            this.value = newValue;
        }


}
