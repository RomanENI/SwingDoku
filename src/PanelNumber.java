import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static javax.swing.UIManager.get;

public class PanelNumber extends JPanel {

    private final PanelOnRight panelMessages;
    int coordX = 0;
    int coordY = 0;
    int value = 0;

    JPopupMenu popupMenu;
    private SDLogicCenter logic;




    private Color frameColorIn;
    private Color frameColorOut;

    private boolean gridCoherenceStatus;

    private boolean isLocked;
    private boolean clueMode;

    private final int squareSize = 72;
    Dimension panelNumberDim = new Dimension(squareSize, squareSize);

    private ImageIcon imageNormal;
    private ImageIcon imageFocus;
    private ImageIcon imageLocked;


    MouseAdapter focusListener;
    MouseAdapter mouseReleasedAdapter;
    PopupMenuListener deFocusListener;
    KeyListener keyListener;


    private CluesHolder clueHolder;


    @Override
    public BorderLayout getLayout() {
        return layout;
    }

    private BorderLayout layout = new BorderLayout();



    private ArrayList<ImageIcon> imgsNormal;
    private ArrayList<ImageIcon> imgsSmall;
    private ArrayList<ImageIcon> imgsFocus;
    private ArrayList<ImageIcon> imgsLocked;
    private ArrayList<ImageIcon> imgsSmallFocussed;


    public int[][] getAbstractBoard() {
        return abstractBoard;
    }

    public void setAbstractBoard(int[][] abstractBoard) {
        this.abstractBoard = abstractBoard;
    }

    private int[][] abstractBoard;

    public ObservableActionThing getObs() {
        return obs;
    }

    private ObservableActionThing obs;

    private PanelGrid.PanelCloseListenerGenerator generator;



    public PanelNumber(int Abcisse, int Ordonne, int value, ArrayList<ImageIcon> imageNumber, ArrayList<ImageIcon> imageNumberSmall, ArrayList<ImageIcon> imageNumberFocus
        , ArrayList<ImageIcon> imageNumberLocked, ArrayList<ImageIcon> imageNumbersSmallAndFocussed, int[][] abstractBoard, ObservableActionThing observer, boolean gridCoherenceStatus, PanelOnRight panelMessages, PanelGrid.PanelCloseListenerGenerator generator){

        this.panelMessages = panelMessages;
        this.imgsSmallFocussed = imageNumbersSmallAndFocussed;
        this.generator = generator;
        this.isLocked = false;

        this.gridCoherenceStatus = gridCoherenceStatus;
        this.obs = observer;
        this.imgsNormal = imageNumber;
        this.imgsSmall = imageNumberSmall;
        this.imgsFocus = imageNumberFocus;
        this.imgsLocked = imageNumberLocked;
        this.abstractBoard = abstractBoard;
        this.logic = new SDLogicCenter();
        this.imageNormal = new ImageIcon();
        this.imageNormal = imageNumber.get(value);
        this.imageFocus = new ImageIcon();
        this.imageFocus = imageNumberFocus.get(value);


        this.setBackground(Color.WHITE);
        this.setPreferredSize(panelNumberDim);
        this.coordX = Abcisse;
        this.coordY = Ordonne;
        this.value = value;
        this.clueMode = false;
        this.setLayout(layout);
        JLabel labelImage = new JLabel(imageNormal);
        this.add(labelImage, BorderLayout.CENTER);
        this.setBackground(Color.WHITE);
        this.frame(coordX,coordY);
        this.setFocusable(true);


        this.clueHolder = new CluesHolder();



        this.popupMenu = new JPopupMenu();
        this.updateTheme();
        buildPopupMenuCertitudeMain();
        setupListeners();


    }//panelNumber constructor


    public void setupListeners() {


        deFocusListener = new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                PanelNumber panelInvoker = (PanelNumber)((JPopupMenu)e.getSource()).getInvoker();
                panelInvoker.losePanelFocus();
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        };
        this.popupMenu.addPopupMenuListener(deFocusListener);


        this.mouseReleasedAdapter = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent event) {
                PanelNumber invoker = (PanelNumber) event.getSource();
                if (event.isPopupTrigger()){

                    invoker.gainPanelFocus();
                    popupMenu.show(event.getComponent(),event.getX(),event.getY());
                }
                else {
                    invoker.gainPanelFocus();
                }
            }
        };
        this.addMouseListener(mouseReleasedAdapter);



        focusListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                PanelNumber panelInvoker = (PanelNumber) e.getSource();
                panelInvoker.gainPanelFocus();
            }
        };

        this.addMouseListener(focusListener);

        this.keyListener = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {

                    PanelNumber panel = (PanelNumber) e.getSource();
                    char[] availableValues = new char[10];
                    final int RADIX = 10;
                    for(int i = 0; i<=9; i++){
                        availableValues[i] = Character.forDigit(i, RADIX);
                    }
                    int charValue = e.getKeyChar() - 48;
                    if (charValue != panel.value){
                        for (char value : availableValues){
                            if (e.getKeyChar() == value){
                                int charToInt = Character.getNumericValue(value);

//                            addActionToList(panel, value);
                                panel.getObs().panelChangeValue(charToInt);
                                System.out.println("charToTint : "+charToInt);
                                panel.changeNumber(charToInt);
                            }
                        }
                    }

                    panel.losePanelFocus();
//                System.out.println(panel.value);
                }
            };
        this.addKeyListener(keyListener);

    }

    public void removeListeners(){

//        this.removeMouseListener(mouseReleasedAdapter);
//        this.removeMouseListener(focusListener);
//        this.removeKeyListener(keyListener);
//        this.popupMenu.removePopupMenuListener(deFocusListener);
//        this.remove(popupMenu);
        for (MouseListener mouse : this.getMouseListeners()){
            this.removeMouseListener(mouse);
        }
        for (KeyListener key : this.getKeyListeners()){
            this.removeKeyListener(key);
        }


    }


    public void updateTheme(){
        this.setImageNormal(this.imgsNormal.get(this.value));
        this.setImageFocus(this.imgsFocus.get(this.value));
        if (this.value != 0){

            this.setImageLocked(this.imgsLocked.get(this.value - 1));
        }

        updateClueHolderTheme();

    }

    public void updateClueHolderTheme(){
        this.getClueHolder().updateImgs();

        setClueHolderToNormal();
    }


    private void frame( int i, int j) {
    //warning will only work if called by a panel already set to BorderLayout

            JPanel frameLeft = new JPanel();
            JPanel frameRight = new JPanel();
            JPanel frameTop = new JPanel();
            JPanel frameBot = new JPanel();
            int thickness = 1;


            //frameLeft
            if(i==0){
                thickness = 4;
            }

            if(i==3 || i==6){
                thickness = 2;
            }

            frameLeft.setPreferredSize(new Dimension(thickness,0));
            frameLeft.setBackground(Color.BLUE);

            thickness = 1;
            if(i == 8){
                thickness=4;
            }
            if (i==2 || i==5){
                thickness=2;
            }

            //frameRight
            frameRight.setPreferredSize(new Dimension(thickness,0));
            frameRight.setBackground(Color.BLUE);

            thickness =1;
            if(j==0){
                thickness =4;
            }
            if (j==3 || j==6){
                thickness=2;
            }
            frameTop.setPreferredSize(new Dimension(0,thickness));
            frameTop.setBackground(Color.BLUE);

            thickness = 1;
            if (j==8){
                thickness=4;
            }
            if(j==2 || j==5){
                thickness=2;
            }
            frameBot.setPreferredSize(new Dimension(0,thickness));
            frameBot.setBackground(Color.BLUE);

            this.add(frameLeft,BorderLayout.LINE_START);
            this.add(frameRight,BorderLayout.LINE_END);
            this.add(frameTop,BorderLayout.PAGE_START);
            this.add(frameBot,BorderLayout.PAGE_END);
    }

    public void frame( int i, int j, Color colorIn, Color colorOut) {
        //use this for Theme updates


        if (this.getLayout() == null){
            this.setLayout(new BorderLayout());
        }

        //clearing
        BorderLayout layout = (BorderLayout)this.getLayout();

        if (layout.getLayoutComponent(BorderLayout.PAGE_START) != null){
            this.remove(layout.getLayoutComponent(BorderLayout.PAGE_START));
        }
        if (layout.getLayoutComponent(BorderLayout.LINE_START) != null){
            this.remove(layout.getLayoutComponent(BorderLayout.LINE_START));
        }
        if (layout.getLayoutComponent(BorderLayout.LINE_END) != null){
            this.remove(layout.getLayoutComponent(BorderLayout.LINE_END));
        }
        if (layout.getLayoutComponent(BorderLayout.PAGE_END) != null){
            this.remove(layout.getLayoutComponent(BorderLayout.PAGE_END));
        }





        //making the new frameParts
        JPanel frameLeft = new JPanel();
        JPanel frameRight = new JPanel();
        JPanel frameTop = new JPanel();
        JPanel frameBot = new JPanel();
        int thickness = 1;


        //manual settings I was bad at %3 at the time
        //frameLeft
        if(i==0){
            thickness = 4;
            frameLeft.setPreferredSize(new Dimension(thickness,0));
            frameLeft.setBackground(colorIn);

        }else if(i==3 || i==6){
            thickness = 2;
            frameLeft.setPreferredSize(new Dimension(thickness,0));
            frameLeft.setBackground(colorIn);
        }else {
            frameLeft.setPreferredSize(new Dimension(thickness,0));
            frameLeft.setBackground(colorIn);
        }


        //frameRight
        thickness = 1;
        if(i == 8){
            thickness=4;
            frameRight.setPreferredSize(new Dimension(thickness,0));
            frameRight.setBackground(colorIn);

        }else if (i==2 || i==5){
            thickness=2;
            frameRight.setPreferredSize(new Dimension(thickness,0));
            frameRight.setBackground(colorIn);
        }else{
            frameRight.setPreferredSize(new Dimension(thickness,0));
            frameRight.setBackground(colorIn);
        }




        //frameTop
        thickness =1;
        if(j==0){
            thickness =4;
            frameTop.setPreferredSize(new Dimension(0,thickness));
            frameTop.setBackground(colorOut);
        }else if (j==3 || j==6){
            thickness=2;
            frameTop.setPreferredSize(new Dimension(0,thickness));
            frameTop.setBackground(colorIn);
        }else{
            frameTop.setPreferredSize(new Dimension(0,thickness));
            frameTop.setBackground(colorIn);
        }



        //frameBot
        thickness = 1;
        if (j==8){
            thickness=4;
            frameBot.setPreferredSize(new Dimension(0,thickness));
            frameBot.setBackground(colorOut);
        }else if(j==2 || j==5){
            thickness=2;
            frameBot.setPreferredSize(new Dimension(0,thickness));
            frameBot.setBackground(colorIn);
        }else{
            frameBot.setPreferredSize(new Dimension(0,thickness));
            frameBot.setBackground(colorIn);
        }


        this.add(frameLeft,BorderLayout.LINE_START);
        this.add(frameRight,BorderLayout.LINE_END);
        this.add(frameTop,BorderLayout.PAGE_START);
        this.add(frameBot,BorderLayout.PAGE_END);
        this.revalidate();
        this.repaint();
    }


    private void gainPanelFocus(){

        //remove all previous focus
        JPanel grid = (JPanel) this.getParent();
        Component[] components = grid.getComponents();

        for (Component otherPanels : components) {
            if (otherPanels instanceof PanelNumber && !((PanelNumber) otherPanels).isLocked){

                ((PanelNumber) otherPanels).losePanelFocus();
            }

        }


        if (!this.clueMode){
            BorderLayout layout = (BorderLayout) this.getLayout();
            this.remove(layout.getLayoutComponent(BorderLayout.CENTER));
            //panel image is changed to focus image
            JLabel focussedValueImage = new JLabel();
            focussedValueImage.setIcon(imageFocus);
            this.add(focussedValueImage, BorderLayout.CENTER);

            this.requestFocus();
//        this.frame(this.coordX, this.coordY);

            //grid update
            grid.revalidate();
            grid.repaint();
        }else{

             this.getClueHolder().goToFocusMode();

        }

    }

    private void losePanelFocus(){
        if (!this.isClueMode()){
            KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner();
            BorderLayout layout = (BorderLayout) this.getLayout();
            this.remove(layout.getLayoutComponent(BorderLayout.CENTER));
            this.add(getImageNumberNormal(this.value),BorderLayout.CENTER);
            JPanel grid = (JPanel) this.getParent();
            this.revalidate();
            this.repaint();
        }else{


            //clueMode is true
            setClueHolderToNormal();
        }

    }

    private void setClueHolderToNormal() {
        ArrayList<JPanel> currentlyDisplayed = this.getClueHolder().getCurrentlyDisplayedPanels();
        boolean[] displayedClues = this.getClueHolder().getDisplayedClues();

        for (int i = 0; i < 9; i++) {
            JPanel panel = currentlyDisplayed.get(i);
            BorderLayout layout = (BorderLayout) panel.getLayout();
            panel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
            JLabel replaceLbl;
            if (displayedClues[i]){
                replaceLbl = new JLabel(imgsSmall.get(i + 1));
            }else{
                replaceLbl = new JLabel(imgsSmall.get(0));
            }
            panel.add(replaceLbl, BorderLayout.CENTER);
            this.revalidate();
            this.repaint();
        }
    }


    public void buildPopupMenuCertitudeMain(){

        JMenu submenu = new JMenu("Conjectures");

        this.popupMenu.add(submenu);

        createCluePlacerMenu(submenu);

        buildPopUpMenuTestItems();
        //test button candidates after naked pairs?



        // ---- TEST -----------------


        createFullNumberMenuItems(this.popupMenu);


    }


    public void buildPopUpMenuTestItems(){

        JMenuItem itemClueMode = new JMenuItem("toggle Cluemode");
        itemClueMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelNumber panelsource = (PanelNumber) ((JPopupMenu)((JMenuItem)e.getSource()).getParent()).getInvoker();

                panelsource.toggleClueMode();

            }
        });
        this.popupMenu.add(itemClueMode);

        JMenuItem itemTestClues = new JMenuItem("go clueTestPanel");
        itemTestClues.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelNumber panelsource = (PanelNumber) ((JPopupMenu)((JMenuItem)e.getSource()).getParent()).getInvoker();

                panelsource.remove(panelsource.getLayout().getLayoutComponent(BorderLayout.CENTER));
                panelsource.add(panelsource.getClueHolder(), BorderLayout.CENTER);
                panelsource.setClueMode(true);




                panelsource.revalidate();
                panelsource.repaint();

            }
        });
        this.popupMenu.add(itemTestClues);

        JMenuItem itemtestRemove = new JMenuItem("remove some");
        itemtestRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelNumber panelsource = (PanelNumber) ((JPopupMenu)((JMenuItem)e.getSource()).getParent()).getInvoker();

                CluesHolder holder = panelsource.getClueHolder();
                holder.removeClue(3);
                holder.removeClue(5);
                holder.removeClue(9);




                panelsource.revalidate();
                panelsource.repaint();

            }
        });
        this.popupMenu.add(itemtestRemove);
//            //test button give your coords
//            JMenuItem coordGiver = new JMenuItem("coordTest");
//            coordGiver.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    PanelNumber panelsource = (PanelNumber) ((JPopupMenu)((JMenuItem)e.getSource()).getParent()).getInvoker();
//                    System.out.println("X : "+ panelsource.coordX +"   Y : "+panelsource.coordY);
//                }
//            });
//            popUpMenu.add(coordGiver);
//            // ---- TEST -----------------

        //test button can you deduce this line ?
        JMenuItem simpleDeduces = new JMenuItem("Déduction Unicité");
        simpleDeduces.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelNumber panelsource = (PanelNumber) ((JPopupMenu)((JMenuItem)e.getSource()).getParent()).getInvoker();
                int[] nbDeduced = logic.candidatesUnicityMethod(panelsource.coordX, panelsource.coordY, abstractBoard);
                for (int i = 0; i < nbDeduced.length; i++) {
                    System.out.print(nbDeduced[i]+"  ");
                }
                System.out.println();
            }
        });
        this.popupMenu.add(simpleDeduces);
        // ---- TEST -----------------

        //test button can you deduce this line with neighbour unicity?
        JMenuItem simpleDeducesExclusion = new JMenuItem("Déduction Unicité et Exclusion");
        simpleDeducesExclusion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelNumber panelsource = (PanelNumber) ((JPopupMenu)((JMenuItem)e.getSource()).getParent()).getInvoker();
                int[] nbDeduced = logic.candidatesUnicityAndExclusions(panelsource.coordX, panelsource.coordY, abstractBoard);
                System.out.println("Super final candidate : ");
                for (int i = 0; i < nbDeduced.length; i++) {
                    System.out.print(nbDeduced[i]+"  ");
                }
                System.out.println();
//                    System.out.println("pi che toute");
                if (nbDeduced.length == 1){
                    panelsource.changeNumber(nbDeduced[0]);
                }
            }

        });
        this.popupMenu.add(simpleDeducesExclusion);
        // ---- TEST -----------------
        JMenuItem itemCandidatesAfterNP = new JMenuItem("Candidats après double NP");
        itemCandidatesAfterNP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelNumber panelsource = (PanelNumber) ((JPopupMenu)((JMenuItem)e.getSource()).getParent()).getInvoker();
                int[] candidatesUnicity = logic.candidatesUnicityMethod(panelsource.coordX, panelsource.coordY, abstractBoard);
                int[] nbDeduced = logic.candidatesAfterEliminatingNakedPairs(panelsource.coordX, panelsource.coordY, abstractBoard, candidatesUnicity);
                System.out.println("Candidats après double NP : ");
                for (int i = 0; i < nbDeduced.length; i++) {
                    System.out.print(nbDeduced[i]+"  ");
                }
                System.out.println();

            }

        });
        this.popupMenu.add(itemCandidatesAfterNP);


    }


    public void buildPopupMenuCluesMain(){


        JMenu submenu = new JMenu("Certitude");
        this.popupMenu.add(submenu);
        createFullNumberMenuItems(submenu);
        buildPopUpMenuTestItems();

        //test button candidates after naked pairs?



        // ---- TEST -----------------


        createCluePlacerMenu(this.popupMenu);


    }

    private void createFullNumberMenuItems(JComponent destinationMenu) {
        for (int i = 0;i<=9;i++){
            JMenuItem item = new JMenuItem();
            item.setIcon(imgsSmall.get(i));
            int menuItemVal = i;
            PanelNumber panel = this;
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int value = menuItemVal;
                    if ((panel.value != menuItemVal) ){

                        panel.getObs().panelChangeValue(menuItemVal);
                            changeNumber(e,value, gridCoherenceStatus, panel);

                    }

                }
            });

            item.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    JPopupMenu popUpMenu =  ((JPopupMenu)((JMenuItem)e.getSource()).getParent());
                    if (e.getX() <= 0 || e.getX() >= ((JMenuItem) e.getSource()).getWidth()){
                        popUpMenu.setVisible(false);
                    }
                }
            });
            destinationMenu.add(item);

        }
    }

    private void createCluePlacerMenu(JComponent destinationMenu) {
        for (int i = 1;i<=9;i++){
            JMenuItem item = new JMenuItem();
            item.setIcon(imgsSmall.get(i));
            int menuItemVal = i;
            PanelNumber panele = this;
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int value = menuItemVal;
                    PanelNumber panelInvoker = panele;


                    panele.changeDisplayToClues();
                    panele.setValue(0);
                    if (panelInvoker.getClueHolder().getDisplayedClues()[value-1]){
                        panelInvoker.getClueHolder().removeClue(value);
                    }else{
                        panelInvoker.placeClue(value);
                        panele.setClueMode(true);

                    }
                }


            });

            item.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    JPopupMenu popUpMenu =  ((JPopupMenu)((JMenuItem)e.getSource()).getParent());
                    if (e.getX() <= 0 || e.getX() >= ((JMenuItem) e.getSource()).getWidth()){
                        popUpMenu.setVisible(false);
                    }
                }
            });
            destinationMenu.add(item);

        }
    }

    private void toggleClueMode() {
        if (this.isClueMode()){
//            this.remove(this.getLayout().getLayoutComponent(BorderLayout.CENTER));
//
//            JLabel label = new JLabel(this.getImageNormal());
//            this.add(label, BorderLayout.CENTER);
            this.changeDisplayToValue();
            this.setClueMode(false);
            this.revalidate();
            this.repaint();
        }else {
//            this.remove(this.getLayout().getLayoutComponent(BorderLayout.CENTER));
//            this.add(this.getClueHolder(), BorderLayout.CENTER);
            this.changeDisplayToClues();
            this.setClueMode(true);
            this.revalidate();
            this.repaint();
        }
    }

    private void placeClue(int value) {


        ObservableActionThing observer = this.getObs();
        //if we were not yet in clueMode, we are setting up the visuals with the empty panels on clueHolder.
        if (!this.isClueMode()) {
            ArrayList<JPanel> currentlyDisplayed = this.getClueHolder().getCurrentlyDisplayedPanels();
            boolean[] displayedClues = this.getClueHolder().getDisplayedClues();

            for (int i = 0; i < 9; i++) {
                JPanel panel = currentlyDisplayed.get(i);
                BorderLayout layout = (BorderLayout) panel.getLayout();
                panel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
                JLabel replaceLbl;
                replaceLbl = new JLabel(imgsSmall.get(0));
                panel.add(replaceLbl, BorderLayout.CENTER);
                this.revalidate();
                this.repaint();

            }
        }
        this.getClueHolder().getDisplayedClues()[value-1] = true;
        this.getClueHolder().placeClue(value);
        observer.panelClueHandling(value, true);



    }


    private void changeNumber(ActionEvent actionEvent, int value, boolean checkingCoherence, PanelNumber panel) {

        boolean okSign = true;
        if (checkingCoherence){
            okSign = logic.possibleFit(panel.coordX, panel.coordY, value, abstractBoard);

        }
        if (okSign){
            BorderLayout layout = (BorderLayout)panel.getLayout();
            panel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
            panel.add(getImageNumberNormal(value), BorderLayout.CENTER);

            abstractBoard[panel.coordY][panel.coordX] = value;
            panel.value = value;
            panel.imageNormal = imgsNormal.get(value);
            panel.imageFocus = imgsFocus.get(value);



            panel.revalidate();
            panel.repaint();
            //message if grid is possible or no after adding a number
//                if (checkGridCoherence){
//                    int[][] temporaryCopyOfAbstractBoard = new int[9][9];
//                    for (int i = 0; i < 9; i++) {
//                        for (int j = 0; j < 9; j++) {
//                            temporaryCopyOfAbstractBoard[i][j] = abstractBoard[i][j];
//                        }
//                    }
//                    if (possibleGrid(temporaryCopyOfAbstractBoard)){
//                        System.out.println("Grid is still feasible");
//                    }else {
//                        System.out.println("Grid has no solution");
//                    }
//                }
        }else {
            ArrayList<Object> list = logic.checkPossibleFitError(coordX, coordY, value, abstractBoard);
            JButton button = new JButton("voir les cases");
            PanelNumber pan = this;
            String msgText = "";
            int msgLength = 0;

            for(Object item : list) {
                if (item instanceof String) {
                    msgText = (String) item;
                    System.out.println(item);
                }else {
                    msgLength++;
                }
            }
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {


                            JPanel grid = (JPanel) pan.getParent();
                            Component[] components = grid.getComponents();
                            for (Component pan : components){
                                if (pan instanceof PanelNumber){
                                    ((PanelNumber) pan).losePanelFocus();
                                }

                            }

                            for (Object item : list) {
                                if (item instanceof ArrayList<?>) {
                                    for (Component otherPanels : components) {
                                        if (otherPanels instanceof PanelNumber) {
                                            int xCord = (int) ((ArrayList<?>) item).get(0);
                                            int yCord = (int) ((ArrayList<?>) item).get(1);
                                            if (((PanelNumber) otherPanels).getCoordX() == xCord && ((PanelNumber) otherPanels).getCoordY() == yCord) {
                                                BorderLayout layout;
                                                layout = ((PanelNumber) otherPanels).getLayout();
                                                ((PanelNumber) otherPanels).remove(layout.getLayoutComponent(BorderLayout.CENTER));
                                                int panValue = ((PanelNumber) otherPanels).getValue();

                                                JLabel focussedValueImage = new JLabel();
                                                focussedValueImage.setIcon(imgsFocus.get(panValue));
                                                ((PanelNumber) otherPanels).add(focussedValueImage, BorderLayout.CENTER);
                                                otherPanels.revalidate();
                                                otherPanels.repaint();
                                            }
                                        }
                                    }
                                }
                            }

                        }

                    }).start();

                }
            });


            msgText = "<html><body>" + msgText + "</body></html>";
            Message message = new Message(msgText, msgLength, button);


            this.panelMessages.addMessage(message, this.generator.generate());

        }
    }





    public void changeNumber(int newValue){
//       SDLogicCenter logic = new SDLogicCenter();
//       logic.displayAbstractBoard(abstractBoard);

        //change number handles value update
        abstractBoard[this.coordY][this.coordX] = newValue;
        BorderLayout layout = (BorderLayout)this.getLayout();
        this.remove(layout.getLayoutComponent(BorderLayout.CENTER));

        this.setValue(newValue);
        this.setImageNormal(imgsNormal.get(this.value));
        JLabel lab = new JLabel(this.imageNormal);
        this.add(lab, BorderLayout.CENTER);
        this.setImageFocus(imgsFocus.get(this.value));
        this.obs.setValue(newValue);

        this.revalidate();
        this.repaint();
    }

    public void changeNumberFromPan(int newValue){
        abstractBoard[this.coordY][this.coordX] = newValue;
        BorderLayout layout = (BorderLayout)this.getLayout();
        this.remove(layout.getLayoutComponent(BorderLayout.CENTER));

        this.setValue(newValue);
        this.setImageNormal(imgsNormal.get(this.value));
        JLabel lab = new JLabel(this.imageNormal);
        this.add(lab, BorderLayout.CENTER);
        this.setImageFocus(imgsFocus.get(this.value));
        this.obs.setValue(newValue);


        this.revalidate();
        this.repaint();




    }





    void changeNumber(int[][] board, int newValue){
        PanelNumber panel = this;
        board[panel.coordY][panel.coordX] = newValue;
        BorderLayout layout = (BorderLayout)panel.getLayout();
        panel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
        panel.add(getImageNumberNormal(newValue), BorderLayout.CENTER);
        panel.value = newValue;
        panel.revalidate();
        panel.repaint();

    }


    public void lockPanel(){
        this.removeAll();
        this.removeListeners();


        this.updateTheme();
        this.frame(this.getCoordX(), this.getCoordY(), this.frameColorIn, this.frameColorOut);
        JLabel labelImage = new JLabel(getImageLocked());
        this.add(labelImage, BorderLayout.CENTER);
        this.setFocusable(false);
        this.setLocked(true);
        this.revalidate();
        this.repaint();

    }

    public void unlockPanel(){

        this.removeAll();
        this.popupMenu = new JPopupMenu();
        this.buildPopupMenuCertitudeMain();
        this.add(this.popupMenu);

        this.frame(this.getCoordX(), this.getCoordY(), this.frameColorIn, this.frameColorOut);
        this.setLocked(false);
        this.setFocusable(true);
        this.updateTheme();
        JLabel labelImage = new JLabel(getImageNormal());
        this.add(labelImage, BorderLayout.CENTER);

        setupListeners();
    }


    public void changeDisplayToClues(){

        this.remove(this.getLayout().getLayoutComponent(BorderLayout.CENTER));
        this.add(this.getClueHolder(), BorderLayout.CENTER);



        this.revalidate();
        this.repaint();


    }

    public void changeDisplayToValue(){
        this.remove(this.getLayout().getLayoutComponent(BorderLayout.CENTER));

        this.add(this.getImageNumberNormal(this.value), BorderLayout.CENTER);
        this.revalidate();
        this.repaint();


    }


    public int getCoordX(){
        return this.coordX;
    }

    public void setCoordX(int x){
        this.coordX = x;
    }

    public int getCoordY(){
        return this.coordY;
    }

    public void setCoordY(int y){
        this.coordY = y;
    }

    public int getValue(){
        return this.value;
    }

    public void setValue(int newValue){
        this.value = newValue;
    }

    public void setPopupMenu(JPopupMenu popupMenu) {
        this.popupMenu = popupMenu;
    }

    public ImageIcon getImageNormal(){
        return imageNormal;
    }

    public void setImageNormal(ImageIcon newImageNormal){
        this.imageNormal = newImageNormal;
    }

    public ImageIcon getImageFocus(){
        return imageFocus;
    }

    public void setImageFocus(ImageIcon newImageFocus){
        this.imageFocus = newImageFocus;
    }

    public boolean isGridCoherenceStatus() {
        return gridCoherenceStatus;
    }

    public void setGridCoherenceStatus(boolean gridCoherenceStatus) {
        this.gridCoherenceStatus = gridCoherenceStatus;
    }

    public void setLogic(SDLogicCenter logic) {
        this.logic = logic;
    }

    public void setImgsFocus(ArrayList<ImageIcon> imgsFocus) {
        this.imgsFocus = imgsFocus;
    }

    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }

    public ImageIcon getImageLocked() {
        return imageLocked;
    }

    public void setImageLocked(ImageIcon imageLocked) {
        this.imageLocked = imageLocked;
    }


    private JLabel getImageNumberNormal(int value){
        return new JLabel(imgsNormal.get(value));
    }

    public void setImgsNormal(ArrayList<ImageIcon> imgsNormal) {
        this.imgsNormal = imgsNormal;
    }
    public ArrayList<ImageIcon> getImgsSmall() {
        return imgsSmall;
    }

    public void setFrameColorIn(Color frameColor) {
        this.frameColorIn = frameColor;
    }

    public void setFrameColorOut(Color frameColor) {
        this.frameColorOut = frameColor;
    }
    public void setImgsSmall(ArrayList<ImageIcon> imgsSmall) {
        this.imgsSmall = imgsSmall;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }
    public void setImgsLocked(ArrayList<ImageIcon> imgsLocked) {
        this.imgsLocked = imgsLocked;
    }

    public CluesHolder getClueHolder() {
        return clueHolder;
    }

    public void setClueHolder(CluesHolder clueHolder) {
        this.clueHolder = clueHolder;
    }

    public ArrayList<ImageIcon> getImgsSmallFocussed() {
        return imgsSmallFocussed;
    }

    public void setImgsSmallFocussed(ArrayList<ImageIcon> imgsSmallFocussed) {
        this.imgsSmallFocussed = imgsSmallFocussed;
    }




    public boolean isClueMode() {
        return clueMode;
    }

    public void setClueMode(boolean clueMode) {
        this.clueMode = clueMode;
    }




    private class CluesHolder extends JPanel{

        private JPanel clue1;
        private JPanel clue2;
        private JPanel clue3;
        private JPanel clue4;
        private JPanel clue5;
        private JPanel clue6;
        private JPanel clue7;
        private JPanel clue8;
        private JPanel clue9;
        private ArrayList<ImageIcon> imgsSmallForHolder;
        private ArrayList<ImageIcon> imgsSmallFocusForHolder;

        private boolean[] displayedClues;



        private ArrayList<JPanel> currentlyDisplayedPanels;


        public CluesHolder(){
            this.imgsSmallForHolder = new ArrayList<>();
            updateImgs();
            this.currentlyDisplayedPanels = new ArrayList<>();

            this.displayedClues = new boolean[9];
            this.setLayout(new GridBagLayout());
            this.setPreferredSize(new Dimension(75, 75));


            this.clue1 = new JPanel();
            this.clue2 = new JPanel();
            this.clue3 = new JPanel();
            this.clue4 = new JPanel();
            this.clue5 = new JPanel();
            this.clue6 = new JPanel();
            this.clue7 = new JPanel();
            this.clue8 = new JPanel();
            this.clue9 = new JPanel();



            setupSmallCluePan(this.clue1, 0, 0);
            setupSmallCluePan(this.clue2, 1, 0);
            setupSmallCluePan(this.clue3, 2, 0);
            setupSmallCluePan(this.clue4, 0, 1);
            setupSmallCluePan(this.clue5, 1, 1);
            setupSmallCluePan(this.clue6, 2, 1);
            setupSmallCluePan(this.clue7, 0, 2);
            setupSmallCluePan(this.clue8, 1, 2);
            setupSmallCluePan(this.clue9, 2, 2);


        }

        private void setupSmallCluePan(JPanel clue, int gridx, int gridy) {
            GridBagConstraints gb = new GridBagConstraints();
            int deducedClueValue = (gridy * 3)+gridx+1;
            clue.setPreferredSize(new Dimension(24, 24));
            clue.setBackground(SystemColor.GREEN);
            JLabel lblClue1 = new JLabel();
            lblClue1.setIcon(this.imgsSmallForHolder.get(0));
            clue.setLayout(new BorderLayout());
            clue.add(lblClue1, BorderLayout.CENTER);
            gb.gridx = gridx;
            gb.gridy = gridy;
            this.add(clue, gb);
            currentlyDisplayedPanels.add(clue);
            displayedClues[deducedClueValue-1] = false;
        }

        public void updateImgs(){
            this.imgsSmallForHolder = imgsSmall;
            this.imgsSmallFocusForHolder = imgsSmallFocussed;
            this.revalidate();
            this.repaint();
        }


        public void removeClue(int val){

            JPanel pan = currentlyDisplayedPanels.get(val-1);
            BorderLayout layout = (BorderLayout) pan.getLayout();
            pan.remove(layout.getLayoutComponent(BorderLayout.CENTER));
            JLabel lblEmpty = new JLabel(imgsSmallForHolder.get(0));
            pan.add(lblEmpty, BorderLayout.CENTER);
            displayedClues[val-1] = false;
            pan.revalidate();
            pan.repaint();

        }


        public void placeClue(int val){
            JPanel pan = currentlyDisplayedPanels.get(val-1);
            BorderLayout layout = (BorderLayout) pan.getLayout();
            pan.remove(layout.getLayoutComponent(BorderLayout.CENTER));
            JLabel lblEmpty = new JLabel(this.imgsSmallForHolder.get(val));
            pan.add(lblEmpty, BorderLayout.CENTER);
            displayedClues[val-1] = true;
            pan.revalidate();
            pan.repaint();

        }




        public void goToFocusMode(){
            for (int i = 0; i < 9; i++) {
                JPanel panel = this.currentlyDisplayedPanels.get(i);
                BorderLayout layout = (BorderLayout) panel.getLayout();
                if (displayedClues[i]){
                    panel.remove(layout.getLayoutComponent(BorderLayout.CENTER));

                    JLabel replaceLbl = new JLabel(imgsSmallFocusForHolder.get(i+1));
                    panel.add(replaceLbl, BorderLayout.CENTER);
//                    this.remove(currentlyDisplayedPanels.get(i));
                    this.revalidate();
                    this.repaint();
                }else{
                    panel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
                    JLabel replaceLbl = new JLabel(imgsSmallFocusForHolder.get(0));
                    panel.add(replaceLbl, BorderLayout.CENTER);
//                    this.remove(currentlyDisplayedPanels.get(i));
                    this.revalidate();
                    this.repaint();
                }
            }
        }


        public boolean[] getDisplayedClues() {
            return displayedClues;
        }

        public JPanel getClue1() {
            return clue1;
        }

        public void setClue1(JPanel clue1) {
            this.clue1 = clue1;
        }

        public JPanel getClue2() {
            return clue2;
        }

        public void setClue2(JPanel clue2) {
            this.clue2 = clue2;
        }

        public JPanel getClue3() {
            return clue3;
        }

        public void setClue3(JPanel clue3) {
            this.clue3 = clue3;
        }

        public JPanel getClue4() {
            return clue4;
        }

        public void setClue4(JPanel clue4) {
            this.clue4 = clue4;
        }

        public JPanel getClue5() {
            return clue5;
        }

        public void setClue5(JPanel clue5) {
            this.clue5 = clue5;
        }

        public JPanel getClue6() {
            return clue6;
        }

        public void setClue6(JPanel clue6) {
            this.clue6 = clue6;
        }

        public JPanel getClue7() {
            return clue7;
        }

        public void setClue7(JPanel clue7) {
            this.clue7 = clue7;
        }

        public JPanel getClue8() {
            return clue8;
        }

        public void setClue8(JPanel clue8) {
            this.clue8 = clue8;
        }

        public JPanel getClue9() {
            return clue9;
        }

        public void setClue9(JPanel clue9) {
            this.clue9 = clue9;
        }

        public ArrayList<JPanel> getCurrentlyDisplayedPanels() {
            return currentlyDisplayedPanels;
        }

        public void setCurrentlyDisplayedPanels(ArrayList<JPanel> currentlyDisplayedPanels) {
            this.currentlyDisplayedPanels = currentlyDisplayedPanels;
        }

    }




}//end of class panelNumber

