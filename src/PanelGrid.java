import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PanelGrid extends JPanel {

    private int[][] abstractBoard;

    private ArrayList<ImageIcon> loadedNumberIcons;
    private ArrayList<ImageIcon> loadedNumberIconsSmall;
    private ArrayList<ImageIcon> loadedNumberIconsFocussed;
    private ArrayList<ImageIcon> loadedNumberIconsLocked;


    private ArrayList<ImageIcon> loadedNumberIconsSmallAndFocussed;


    private final String defaultTheme = "boring";

    private PanelOnRight panelMessages;

    private ArrayList<PanelNumber> panels;

    private ArrayList<PanelNumber> lockedPanels = new ArrayList<>();

    private boolean checkCoherence;

    GridActionsHandler playActionHandler;
    GridActionsHandler buildActionHandler;
    GridActionsHandler actionHandler;


    private SDMenuBar referenceToMenu;

    private final int imgScaleInstance = 60;

    private actionManager actionMngr;



    private Game game;

    private JButton submitButton;





    PanelGrid(SDMenuBar menuBar, int[][] board, PanelOnRight panelMessages, JButton submitButton){
        this.submitButton = submitButton;
        this.lockedPanels = new ArrayList<>();
        this.panelMessages = panelMessages;
        this.checkCoherence = false;
        playActionHandler = new GridActionsHandler();
        buildActionHandler = new GridActionsHandler();
        actionHandler = playActionHandler;
        this.abstractBoard = board;

//        this.playPanelButtonListener = new PlayPanelButtonListener();
        this.actionMngr = new actionManager();

        initGridPanel();
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        this.panels = new ArrayList<>();
        createPanelNumbers();
        this.referenceToMenu = menuBar;
        activateMenuItems(menuBar);

    }//PanelGrid constructor ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void weWin(){
        System.out.println("we won");

        System.out.println("yet again");




    }

    private void activateMenuItems(SDMenuBar menuBar) {
                menuBar.getUndo().setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK ));
        menuBar.getUndo().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionHandler.undoPreviousAction();

                if (actionHandler.actionDepthMeter == actionHandler.actionList.size()){
                    menuBar.disableUndo();
                }
            }
        });

        menuBar.getRedo().setAccelerator(KeyStroke.getKeyStroke("control shift Z"));
        menuBar.getRedo().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionHandler.redoNextAction();

                if (actionHandler.actionDepthMeter == 0){
                    menuBar.disableRedo();
                }
            }
        });
        menuBar.getUndo().setEnabled(false);
        menuBar.getUndo().setEnabled(false);
        menuBar.getEdit().add(menuBar.getUndo(), 0);
        menuBar.getEdit().add(menuBar.getRedo(), 1);



        menuBar.getCertitude().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (PanelNumber pan : getPanels()){
                    if (!pan.isLocked()){
                        MouseListener[] listeners = pan.getMouseListeners();
                        JPopupMenu menu = pan.getPopupMenu();
                        menu.removeAll();
                        pan.buildPopupMenuCertitudeMain();
                    }
                }
            }
        });


        menuBar.getConjecture().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (PanelNumber pan : getPanels()){
                    if (!pan.isLocked()){
                        MouseListener[] listeners = pan.getMouseListeners();
                        JPopupMenu menu = pan.getPopupMenu();
                        menu.removeAll();
                        pan.buildPopupMenuCluesMain();
                    }
                }
            }
        });

//        ActionListener listenerCertitude = new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                for (PanelNumber pan : getPanels()){
//
//                }
//
//
//
//
//            }
//        };


    }




    private void createPanelNumbers() {
        GridBagConstraints gbc = new GridBagConstraints();
        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 9; i++) {
                ObservableActionThing observable = new ObservableActionThing(abstractBoard[j][i], i, j);
                PanelCloseListenerGenerator generator = new PanelCloseListenerGenerator();
                observable.addPropertyChangeListener(actionMngr);
                PanelNumber pan = new PanelNumber(i, j, abstractBoard[j][i], getImagesAllNumbersNormal(), getImagesAllNumbersSmall()
                        , getImagesAllNumbersFocussed(), getLoadedNumberIconsLocked(), getLoadedNumberIconsSmallAndFocussed() , abstractBoard, observable, checkCoherence, panelMessages, generator);

                gbc.gridx = i;
                gbc.gridy = j;
                this.add(pan, gbc);
                panels.add(pan);
            }
        }
    }

    private ArrayList<ImageIcon> getImagesAllNumbersNormal(){
        return loadedNumberIcons;
    }
    public SDMenuBar getReferenceToMenu() {
        return referenceToMenu;
    }
    private ImageIcon getImageOneNumberNormal(int number){
        return loadedNumberIcons.get(number);
    }
    private ArrayList<ImageIcon> getImagesAllNumbersSmall(){
        return loadedNumberIconsSmall;
    }

    public ArrayList<ImageIcon> getLoadedNumberIcons() {
        return loadedNumberIcons;
    }

    public void setLoadedNumberIcons(ArrayList<ImageIcon> loadedNumberIcons) {
        this.loadedNumberIcons = loadedNumberIcons;
    }
    private ImageIcon getImageOneNumberSmall(int number){
        return loadedNumberIconsSmall.get(number);
    }
    private ArrayList<ImageIcon> getImagesAllNumbersFocussed(){
        return loadedNumberIconsFocussed;
    }

    public ArrayList<ImageIcon> getLoadedNumberIconsLocked() {
        return loadedNumberIconsLocked;
    }
    private ImageIcon getImageOneNumberFocussed(int number){
        return loadedNumberIconsFocussed.get(number);
    }

    public boolean isCheckCoherence() {
        return checkCoherence;
    }

    public ArrayList<PanelNumber> getLockedPanels() {
        return lockedPanels;
    }
    public void setCheckCoherence(boolean checkCoherence) {
        this.checkCoherence = checkCoherence;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setLoadedNumberIconsSmall(ArrayList<ImageIcon> loadedNumberIconsSmall) {
        this.loadedNumberIconsSmall = loadedNumberIconsSmall;
    }

    public ArrayList<ImageIcon> getLoadedNumberIconsFocussed() {
        return loadedNumberIconsFocussed;
    }

    public void setLoadedNumberIconsFocussed(ArrayList<ImageIcon> loadedNumberIconsFocussed) {
        this.loadedNumberIconsFocussed = loadedNumberIconsFocussed;
    }

    //Careful its a pointer method
    public void setAbstractBoard(int[][] board){
        abstractBoard = board;
    }

    public int[][] getAbstractBoard(){
        return abstractBoard;
    }

    public ArrayList<PanelNumber> getPanels() {
        return panels;
    }

    public JButton getSubmitButton() {
        return submitButton;
    }

    public void setSubmitButton(JButton submitButton) {
        this.submitButton = submitButton;
    }

    public ArrayList<ImageIcon> getLoadedNumberIconsSmallAndFocussed() {
        return loadedNumberIconsSmallAndFocussed;
    }

    public void setLoadedNumberIconsSmallAndFocussed(ArrayList<ImageIcon> loadedNumberIconsSmallAndFocussed) {
        this.loadedNumberIconsSmallAndFocussed = loadedNumberIconsSmallAndFocussed;
    }

    private void initGridPanel() {

        Theme themeForFirstLoad = new Theme(defaultTheme);

        loadedNumberIconsFocussed = new ArrayList<>();
        loadedNumberIconsLocked = new ArrayList<>();
        loadedNumberIcons = new ArrayList<>();
        loadedNumberIconsSmall = new ArrayList<>();
        loadedNumberIconsSmallAndFocussed = new ArrayList<>();


        loadedNumberIconsFocussed.addAll(themeForFirstLoad.getImgsFocussed());
        loadedNumberIconsLocked.addAll(themeForFirstLoad.getImgsLocked());
        loadedNumberIcons.addAll(themeForFirstLoad.getImgsNormal());
        loadedNumberIconsSmall.addAll(themeForFirstLoad.getImgsSmall());
        loadedNumberIconsSmallAndFocussed.addAll(themeForFirstLoad.getImgsSmallAndFocussed());


    }


    public void displayModeToPanelNumbers(){
        this.removeAll();
        this.setLayout(new GridBagLayout());
        GridBagConstraints gb = new GridBagConstraints();
        for (PanelNumber pan : panels){
            gb.gridy = pan.getCoordY();
            gb.gridx = pan.getCoordX();
            this.add(pan, gb);
        }
        this.revalidate();
        this.repaint();



    }



    public void swapActionHandler() {
        if(this.actionHandler == playActionHandler){
            this.setActionHandler(this.buildActionHandler);
        }else if(this.actionHandler == actionHandler){
            this.setActionHandler(this.playActionHandler);
        }
    }


    public void reloadPanelsForTheme(){

        for (PanelNumber pan : this.getPanels()){
            BorderLayout layout = (BorderLayout)pan.getLayout();
            pan.remove(layout.getLayoutComponent(BorderLayout.CENTER));
            if (pan.isLocked()) {
                JLabel lbl = new JLabel(pan.getImageLocked());
                pan.add(lbl, BorderLayout.CENTER);
            }else if(pan.isClueMode()){
                pan.updateClueHolderTheme();
                pan.add(pan.getClueHolder(), BorderLayout.CENTER);



            }else{
                JLabel lbl = new JLabel(getLoadedNumberIcons().get(pan.getValue()));
                pan.add(lbl, BorderLayout.CENTER);
            }

            pan.revalidate();
            pan.repaint();


        }

    }


    public void unlockAllPanels(){
        for (PanelNumber pan : panels){
            if (pan.isLocked()){
                pan.unlockPanel();
            }
        }
    }

    public void lockNonEmptyPanels() {
        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 9; i++) {
                if (abstractBoard[j][i] !=0){
                    getPanelFromCoords(i, j).lockPanel();
                }
            }
        }
    }

    public class PanelCloseListener implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            //we just deleted a panel message.
            //in play mode, we must also delete actions associated with this panel, or we will have a ton of bug in the unde redo departement
            if (!isCheckCoherence()){               //mode play

                PanelOnRight.PanelMessage source = (PanelOnRight.PanelMessage) evt.getNewValue();
                if (source instanceof PanelOnRight.PlayMovePan && ((PanelOnRight.PlayMovePan) source).getAction().get("actionSource").equals("panel")){
                    removePanActions((PanelOnRight.PlayMovePan) source);

                }
            }




        }

        public void removePanActions(PanelOnRight.PlayMovePan panelSource) {
            ArrayList<HashMap> toRemove = new ArrayList<>();
//            for (HashMap<String, Object> action : actionHandler.getActionList()){
//                for (PanelOnRight.PanelMessage panelMessage : panelMessages.getListPanels()){
//                    if (panelMessage instanceof PanelOnRight.PlayMovePan && (action.get("source") == panelMessage)){
//                        toRemove.add(action);
//                    }
//                }
//            }
            for (HashMap<String, Object> action : actionHandler.getActionList()){

                    if ((action.get("source") == panelSource)){
                        toRemove.add(action);
                    }

            }




            if (toRemove.size() != 0){
                for (HashMap<String, Object> actionToRemove : toRemove){

                    removePanAction(actionToRemove);
                    System.out.println("removed");
                }
                System.out.println(actionHandler.getActionDepthMeter());
            }
        }

        public void removePanAction(HashMap<String, Object> action) {
            int counter = 0;
            HashMap<String, Object> compared;
            compared = actionHandler.getActionList().get(counter);
            while (action != compared && counter <= actionHandler.getActionList().size()){
                counter ++;
                compared = actionHandler.getActionList().get(counter);
            }

            if (actionHandler.getActionList().size() - counter <= actionHandler.getActionDepthMeter()){
                System.out.println("list size : "+actionHandler.getActionList().size());
                System.out.println("counter : "+ counter);
                System.out.println("adm before "+actionHandler.getActionDepthMeter());


                actionHandler.setActionDepthMeter(actionHandler.getActionDepthMeter() - 1);
                System.out.println("adm after "+actionHandler.getActionDepthMeter());
            }
            actionHandler.getActionList().remove(counter);


        }

        public PanelCloseListener generateOne(){
            return new PanelCloseListener();
        }
    }

//    private void removePanAction(HashMap<String, Object> action) {
//        int counter = 0;
//        HashMap<String, Object> compared;
//        compared = actionHandler.getActionList().get(counter);
//        while (action != compared && counter <= actionHandler.getActionList().size()){
//            counter ++;
//            compared = actionHandler.getActionList().get(counter);
//        }
//
//        if (actionHandler.getActionList().size() - counter <= actionHandler.getActionDepthMeter()){
//            System.out.println("list size : "+actionHandler.getActionList().size());
//            System.out.println("counter : "+ counter);
//            System.out.println("adm before "+actionHandler.getActionDepthMeter());
//
//
//            actionHandler.setActionDepthMeter(actionHandler.getActionDepthMeter() - 1);
//            System.out.println("adm after "+actionHandler.getActionDepthMeter());
//        }
//        actionHandler.getActionList().remove(counter);
//
//
//    }

    public class PanelCloseListenerGenerator {



        public PanelCloseListener generate(){
            return new PanelCloseListener();
        }
    }



    public class PlayPanelButtonListener implements PropertyChangeListener{


        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            PlayPanelBtnStateWatcher watcherSource = (PlayPanelBtnStateWatcher) evt.getSource();
            JButton buttonInPanel = watcherSource.getPanelButton();
            boolean isModeUndo = (boolean) evt.getNewValue();
            PanelOnRight.PlayMovePan trueSource = (PanelOnRight.PlayMovePan) watcherSource.getPanelButton().getParent();

            PanelOnRight.PlayMovePan panSource = (PanelOnRight.PlayMovePan) ((PlayPanelBtnStateWatcher) evt.getSource()).getPanelButton().getParent();

            PanelNumber panelSource = (PanelNumber) panSource.getAction().get("panel");
            int formerVal;
            int newValue;
            if (panSource.isModeUndo()){
                newValue = (int) panSource.getAction().get("newValue");
                formerVal = (int) panSource.getAction().get("formerValue");
            }else {
                formerVal = (int) panSource.getAction().get("newValue");
                newValue = (int) panSource.getAction().get("formerValue");
            }

            addActionToList(buttonInPanel, newValue, formerVal, trueSource);

            updatePanelsEnabling2(newValue, formerVal, panelSource, isModeUndo);


        }

        private void updatePanelsEnabling2(int newValue, int formerValue, PanelNumber panelSource, boolean mode) {
            for(PanelOnRight.PanelMessage listpan : panelMessages.getListPanels()){
                if (listpan instanceof PanelOnRight.PlayMovePan && ((PanelOnRight.PlayMovePan) listpan).getAction().get("panel") == panelSource){
                    JButton button = ((PanelOnRight.PlayMovePan) listpan).getBtnPan();
                    if (((PanelOnRight.PlayMovePan) listpan).isModeUndo()){
                        int formerValueInAction = (int)((PanelOnRight.PlayMovePan) listpan).getAction().get("newValue");

                        if (formerValueInAction == newValue){

                            listpan.getBtnPan().setEnabled(true);
                            listpan.getBtnPan().setToolTipText(null);
                        }else{
                            button.setEnabled(false);
                            if (button.getToolTipText() == null){
                                button.setToolTipText("<html><body>La valeur de la case a été changé depuis cette action,<br>impossible de revenir en arrière tant que la case ne contient pas "+ formerValue +"</body></html>");
                            }
                        }


                    } else if (!((PanelOnRight.PlayMovePan) listpan).isModeUndo()) {
                        if ((int) ((PanelOnRight.PlayMovePan) listpan).getAction().get("formerValue") == newValue){
                            listpan.getBtnPan().setEnabled(true);
                            listpan.getBtnPan().setToolTipText(null);
                        }else{
                            button.setEnabled(false);
                            if (button.getToolTipText() == null){
                                button.setToolTipText("<html><body>La valeur de la case a été changé depuis cette action,<br>impossible de rétablir tant que la case ne contient pas "+ formerValue +"</body></html>");
                            }
                        }
                    }
                }
            }
        }

        public void addActionToList(JButton btn, int newValue, int formerValue, PanelOnRight.PlayMovePan trueSource){
            HashMap<String, Object> newAction = new HashMap<>();
            newAction.put("actionSource", "btnInPanel");
            newAction.put("button", btn);
            newAction.put("newValue", newValue);
            newAction.put("formerValue", formerValue);
            newAction.put("source", trueSource);

            actionHandler.actionList.add(newAction);

        }
    }



    public class actionManager implements PropertyChangeListener {

        private int value;
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
//                System.out.println("S A I");

            if (evt.getPropertyName().equals("panel modified")){

                int newValue = (int) evt.getNewValue();
                ObservableActionThing obsSource = (ObservableActionThing) evt.getSource();
                int formerValue = obsSource.getValue();
                PanelNumber panelSource = getPanelFromCoords(obsSource.getxCord(), obsSource.getyCord());
                actionHandler.addActionToList(panelSource, formerValue, newValue);
                this.setValue((int) evt.getNewValue());


                //value change goes to message panel if we are in playmode
                if (!checkCoherence){
                    String msg = "Changement case "+(panelSource.coordX+1)+", "+(panelSource.coordY+1);
                    msg = "<html><body>" + msg;
                    msg = msg + "<br>";
                    if (formerValue == 0){
                        msg = msg + "vide  ==> ";
                    }else{
                        msg = msg + formerValue+"  ==> ";
                    }
                    if (newValue == 0){
                        msg = msg + "vide";
                    }else {
                        msg = msg + newValue;
                    }
                    msg = msg + "</body></html>";



                    //checking and greying up already existing PlayPanels
                    updatePanelsEnabling(newValue, formerValue, panelSource);


                    Message message = new Message(msg, 1, null);

                    //we get last element that we just added to put in button method

                    HashMap<String, Object> action = actionHandler.actionList.get(actionHandler.actionList.size()-1);

                    PlayPanelButtonListener watcher = new PlayPanelButtonListener();
                    PanelCloseListener closeListener = new PanelCloseListener();
                    panelMessages.addMessageUndo(message, action, watcher, closeListener);

                    //check here for gamestate

                    System.out.println(panelSource.getValue());
                    System.out.println(evt.getNewValue());
                    checkGridState(panelSource.getValue(), (int)evt.getNewValue());




                }
            }else if (evt.getPropertyName().equals("modified from button")){



                int formerValue = (int) evt.getOldValue();
                int newValue = (int) evt.getNewValue();
                
                System.out.println("old :"+formerValue+" new : "+newValue);

                checkGridState(formerValue, newValue);
            }else if(evt.getPropertyName().equals("clueHandling")){
                ObservableActionThing obsSource = (ObservableActionThing) evt.getSource();
                PanelNumber panelSource = getPanelFromCoords(obsSource.getxCord(), obsSource.getyCord());
                System.out.println("we are there apparently");
                System.out.println("we were clue mode already : "+panelSource.isClueMode());
                int clueValue = (int) evt.getOldValue();
                boolean newState = (boolean) evt.getNewValue();


                //TODO WE NEED A NEW ADDACTION METHOD TO ADD CLUEMETHODS
//                actionHandler.addActionToList(panelSource, formerValue, newValue);


                if (!checkCoherence){
                    String msg = "Indice case "+(panelSource.coordX+1)+", "+(panelSource.coordY+1);
                    msg = "<html><body>" + msg;
                    msg = msg + "<br>";
                    if (newState == true){
                        msg = msg + "ajout de l'indice : "+clueValue;
                    }else{
                        msg = msg + " retrait de l'indice : "+clueValue;
                    }

                    msg = msg + "</body></html>";
//
//
//
//                    //checking and greying up already existing PlayPanels
//                    updatePanelsEnabling(newValue, formerValue, panelSource);
//
//
//                    Message message = new Message(msg, 1, null);
////
////                    //we get last element that we just added to put in button method
////
//                    HashMap<String, Object> action = actionHandler.actionList.get(actionHandler.actionList.size()-1);
////
//                    PlayPanelButtonListener watcher = new PlayPanelButtonListener();
//                    PanelCloseListener closeListener = new PanelCloseListener();
//                    panelMessages.addMessageUndo(message, action, watcher, closeListener);
//
//                    //check here for gamestate
//
//                    System.out.println(panelSource.getValue());
//                    System.out.println(evt.getNewValue());
//                    checkGridState(panelSource.getValue(), (int)evt.getNewValue());
//
//
//
//
                }
//


            }
        }






        public void updatePanelsEnabling(int newValue, int formerValue, PanelNumber panelSource) {
            for(PanelOnRight.PanelMessage listpan : panelMessages.getListPanels()){
                if (listpan instanceof PanelOnRight.PlayMovePan && ((PanelOnRight.PlayMovePan) listpan).getAction().get("panel") == panelSource){
                    boolean panelMode = ((PanelOnRight.PlayMovePan) listpan).isModeUndo();
                    JButton button = ((PanelOnRight.PlayMovePan) listpan).getBtnPan();
                    if (panelMode){
                        int formerValueInAction = (int)((PanelOnRight.PlayMovePan) listpan).getAction().get("newValue");
                        if (formerValueInAction == newValue){
                            listpan.getBtnPan().setEnabled(true);
                            listpan.getBtnPan().setToolTipText(null);
                        }else{
                            button.setEnabled(false);
                            if (button.getToolTipText() == null){
                                button.setToolTipText("<html><body>La valeur de la case a été changé depuis cette action,<br>impossible de revenir en arrière tant que la case ne contient pas "+ formerValue +"</body></html>");
                            }
                        }


                    } else if (!panelMode) {
                        if ((int) ((PanelOnRight.PlayMovePan) listpan).getAction().get("formerValue") == newValue){
                            listpan.getBtnPan().setEnabled(true);
                            listpan.getBtnPan().setToolTipText(null);
                        }else{
                            button.setEnabled(false);
                            if (button.getToolTipText() == null){
                                button.setToolTipText("<html><body>La valeur de la case a été changé depuis cette action,<br>impossible de rétablir tant que la case ne contient pas "+ formerValue +"</body></html>");
                            }
                        }
                    }
                }
            }
        }

        public void setValue(int newValue){
            this.value = newValue;
        }

    }

    private void checkGridState(int formerValue, int newValue) {
//        System.out.println("former "+formerValue);
//        System.out.println("new "+newValue);
        SDLogicCenter logic = new SDLogicCenter();
        if (newValue != 0  && gridCompleteCheckEvent()){
//            System.out.println("grid is complete !");

            this.submitButton.setEnabled(true);
            this.submitButton.setToolTipText(null);

            //TODO DID WE WIN THOUGH
//            System.out.println("did we win? :");
//            System.out.println(logic.didWeWin(abstractBoard, newValue));


//            if (logic.didWeWin(abstractBoard, newValue)){
//                weWin();
//            }else {
//                System.out.println("did not win this time");
//            }
        }else {
            this.submitButton.setEnabled(false);
            this.submitButton.setToolTipText("La grille est incomplète");
        }
    }

    private boolean gridCompleteCheckEvent() {
        int counter = 0;
        for (PanelNumber pan : panels){
            if (pan.getValue() == 0){
               counter++;
            }
        }
        return counter <= 1;
    }

    public void setAllPansToCertitude(){
        for (PanelNumber pan : panels){
            if (pan.isClueMode()){
                pan.setClueMode(false);
            }
        }
    }


    public PanelNumber getPanelFromCoords(int coordX, int coordY){

        for (PanelNumber panel: panels) {
            PanelNumber pan = (PanelNumber)panel;
            if (pan.coordX == coordX && pan.coordY == coordY) {
                return pan;
            }
        }
        return null;
    }

    public void switchCoherenceChecking(){
        this.checkCoherence = !this.checkCoherence;
    }





    public class GridActionsHandler {

        public ArrayList<HashMap<String, Object>> getActionList() {
            return actionList;
        }

        private ArrayList<HashMap<String, Object>> actionList;

        public int getActionDepthMeter() {
            return actionDepthMeter;
        }

        public void setActionDepthMeter(int actionDepthMeter) {
            this.actionDepthMeter = actionDepthMeter;
        }

        private int actionDepthMeter;

        GridActionsHandler(){
            actionDepthMeter = 0;
            actionList = new ArrayList<>();
        }


        public void addActionToList(PanelNumber panelSource, int formerValue, int newValue){
            HashMap<String, Object> gridModif = new HashMap<String, Object>();

            gridModif.put("actionSource", "panel");
            gridModif.put("panel", panelSource);
//            gridModif.put("coordX", panelSource.coordX);
//            gridModif.put("coordY", panelSource.coordY);
            gridModif.put("formerValue", formerValue);
            gridModif.put("newValue", newValue);

            referenceToMenu.enableUndo();

            //no more redo after you did a new action
            if (actionDepthMeter != 0){
                for (int i = 0; i < actionDepthMeter; i++) {
                    actionList.remove(actionList.size()-1);
                }
                actionDepthMeter = 0;
                referenceToMenu.disableRedo();
            }
            actionList.add(gridModif);

        }

        public void undoPreviousAction(){
            HashMap<String, Object> lastElementInMap = actionList.get(actionList.size() - 1 - actionDepthMeter);

            if (lastElementInMap.get("actionSource").equals("panel")){
                PanelNumber panel = (PanelNumber) lastElementInMap.get("panel");

                int formerValue = (int) lastElementInMap.get("formerValue");
                panel.value = (formerValue);
                panel.changeNumber(formerValue);
                if (!checkCoherence){
                    actionMngr.updatePanelsEnabling((Integer) lastElementInMap.get("formerValue"), (Integer)lastElementInMap.get("newValue"), panel);
//                    System.out.println("Sometimes I m alone");
                    checkGridState((Integer)lastElementInMap.get("newValue"), formerValue);
                }
                actionDepthMeter++;
                referenceToMenu.enableRedo();

            }else if (lastElementInMap.get("actionSource").equals("btnInPanel")){
//                System.out.println("Undoing from button");
                JButton btn = (JButton) lastElementInMap.get("button");
                PanelOnRight.PlayMovePan playPan = (PanelOnRight.PlayMovePan) btn.getParent();
                playPan.updateBtnListener();
//                btn.firePropertyChange("button clicked", this.isUndoState, newState);

                HashMap<String, Object> action = playPan.getAction();
                PanelNumber pan = (PanelNumber) action.get("panel");
                if (!playPan.isModeUndo()){
                    btn.setText("Rétablir");
                    int formerValue = (Integer) action.get("formerValue");
                    pan.setValue(formerValue);
                    pan.changeNumber(formerValue);
                    if (!checkCoherence){
                        actionMngr.updatePanelsEnabling((Integer) lastElementInMap.get("newValue"), (Integer)lastElementInMap.get("formerValue"), pan);
                        checkGridState((Integer)lastElementInMap.get("newValue"), formerValue);
//                        System.out.println("not mode undo btn Source undo action");
                    }

                }else {
                    btn.setText("Anuler");
                    int newValue = (Integer) action.get("newValue");
                    pan.setValue(newValue);
                    pan.changeNumber(newValue);
                    if (!checkCoherence){
                        actionMngr.updatePanelsEnabling((Integer) lastElementInMap.get("formerValue"), (Integer)lastElementInMap.get("newValue"), pan);
//                        System.out.println("mode undo  btn Source undo action");

                        checkGridState((Integer) lastElementInMap.get("newValue"), newValue);
                    }
                }
                actionDepthMeter ++;
                referenceToMenu.enableRedo();
//                System.out.println("undoing");
            }

        }


        public void redoNextAction(){
            HashMap<String, Object> nextElementInMap = actionList.get(actionList.size()- actionDepthMeter);
            if (nextElementInMap.get("actionSource").equals("panel")) {
                PanelNumber panel = (PanelNumber) nextElementInMap.get("panel");
                int nextValue = (int) nextElementInMap.get("newValue");
                panel.value = (nextValue);
                panel.changeNumber(nextValue);
                actionDepthMeter--;
                if (!checkCoherence){
                    actionMngr.updatePanelsEnabling((Integer) nextElementInMap.get("newValue"), (Integer)nextElementInMap.get("formerValue"), panel);
//                    System.out.println("panel Source redo action");
                    checkGridState((Integer)nextElementInMap.get("formerValue"), (Integer) nextElementInMap.get("newValue"));
                }
                referenceToMenu.enableUndo();
            }else if (nextElementInMap.get("actionSource").equals("btnInPanel")){
//                System.out.println("Redoing");
                JButton btn = (JButton) nextElementInMap.get("button");
                PanelOnRight.PlayMovePan playPan = (PanelOnRight.PlayMovePan) btn.getParent();
                playPan.updateBtnListener();
                HashMap<String, Object> action = playPan.getAction();
                PanelNumber pan = (PanelNumber) action.get("panel");
                if (!playPan.isModeUndo()){
                    btn.setText("Rétablir");
                    int formerValue = (Integer) action.get("formerValue");
                    pan.setValue(formerValue);
//                    System.out.println("redo new Value : "+formerValue);
                    pan.changeNumber(formerValue);
                    if (!checkCoherence){
                        actionMngr.updatePanelsEnabling((Integer) nextElementInMap.get("formerValue"), (Integer)nextElementInMap.get("newValue"), pan);
//                        System.out.println("not mode undo btn Source redo action");
//                        System.out.println("boom boop");
//                        checkGridState((Integer)nextElementInMap.get("formerValue"), (Integer) nextElementInMap.get("newValue"));
                        checkGridState((Integer) nextElementInMap.get("formerValue"), formerValue);
                    }

                }else {
                    btn.setText("Anuler");
                    int newValue = (Integer) action.get("newValue");
                    pan.setValue(newValue);
//                    System.out.println("redo new Value : "+newValue);
                    pan.changeNumber(newValue);
                    if (!checkCoherence){
                        actionMngr.updatePanelsEnabling((Integer) nextElementInMap.get("newValue"), (Integer)nextElementInMap.get("formerValue"), pan);
//                        System.out.println("Smode undo btn Source redo action");
//                        System.out.println("plif plaf");
                        checkGridState((Integer)nextElementInMap.get("formerValue"), newValue);
                    }

                }

                actionDepthMeter --;
                referenceToMenu.enableUndo();
//                System.out.println("redoing/");

            }
        }


        public void actionBtnReUnDo(HashMap<String, Object> nextElementInMap){


        }



        public void clearActions(){
            actionList.clear();
            referenceToMenu.disableUndo();
            referenceToMenu.disableRedo();
        }







    }//end ---ActionHandler-------

    public void setActionHandler(GridActionsHandler handler){
        this.actionHandler = handler;
    }

    public GridActionsHandler getGridActionsHandler(){
        return this.actionHandler;
    }





}//end panelGrid