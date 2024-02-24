import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SwingDokuWindow extends JFrame {


    SwingDokuWindow zis = this;
    public Game currentGame;
    //    JMenuBar sdMenuBar = new JMenuBar();
    SDMenuBar sdMenuBar;
    public int[][] abstractBoard;
    public MainPanel mainPanel;

    private String currentTheme = "boring";

    public int[][] getAbstractPlayBoard() {
        return abstractPlayBoard;
    }
    public int[][] abstractPlayBoard;


    public int[][] getAbstractBuildBoard() {
        return abstractBuildBoard;
    }
    public int[][] abstractBuildBoard;


    public int[][] getAbstractBoard(){
        return abstractBoard;
    }


    public SwingDokuWindow(){

        super("SwingDoku");
        this.abstractBoard = new int[9][9];
        this.abstractPlayBoard = new int[9][9];
        this.abstractBuildBoard = new int[9][9];
        abstractBoard = abstractPlayBoard;



        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Dimension mainWindowDim = new Dimension(1400,900);
        sdMenuBar = new SDMenuBar();
        this.setJMenuBar(sdMenuBar);
        initMainPanel();
        populateThemeMenuBarItem();

        this.setSize(mainWindowDim);
        this.setLocationRelativeTo(null);
        this.loadTheme(currentTheme);
        this.pack();
        this.setVisible(true);
    } // fin du constructeur SwingDokuWindow



    private void populateThemeMenuBarItem(){
        this.sdMenuBar.getMenuItemBoring().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTheme("boring");
                currentTheme = "boring";
            }
        });
        this.sdMenuBar.getMenuItemAncientRome().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTheme("rome antique");
                currentTheme = "rome antique";
            }
        });


    }

    public SDMenuBar getSdMenuBar() {
        return sdMenuBar;
    }

    public void setSdMenuBar(SDMenuBar sdMenuBar) {
        this.sdMenuBar = sdMenuBar;
    }

    public MainPanel getMainPanel() {
        return mainPanel;
    }


    private class MainPanel extends JPanel{


        JPanel panelLeft = new JPanel();
        PanelOnRight panelRight;

        public JPanel getPanelCenter() {
            return panelCenter;
        }

        JPanel panelCenter = new JPanel();

        public void setPanelBottom(JPanel panelBottom) {
            this.panelBottom = panelBottom;
        }

        public JPanel getPanelBottom() {
            return panelBottom;
        }

        public void setPanelRight(PanelOnRight panelRight) {
            this.panelRight = panelRight;
        }

        JPanel panelBottom = new JPanel();


        private PanelGrid panelWithGrid;

        public Dimension getDimLeftPanel() {
            return dimLeftPanel;
        }

        private final int coteCarreLength = 648;

        private Dimension dimLeftPanel = new Dimension(370, 0);
        Dimension dimCenterPanel = new Dimension(coteCarreLength, 0);
        Dimension dimPanelWithGrid = new Dimension(coteCarreLength, coteCarreLength);

        public Dimension getDimBottomPanel() {
            return dimBottomPanel;
        }

        public void setDimBottomPanel(Dimension dimBottomPanel) {
            this.dimBottomPanel = dimBottomPanel;
        }

        Dimension dimBottomPanel = new Dimension(coteCarreLength, 0);


        private JButton submitButton;



        MainPanel(){
            setupSubmitButton();
            panelRight = new PanelOnRight();
            createPanelWithGrid();
            panelLeft.setBackground(Color.WHITE);
            panelWithGrid.setBackground(Color.BLUE);
            initPanelBottom();

            panelWithGrid.setPreferredSize(dimPanelWithGrid);
            panelWithGrid.setMaximumSize(dimPanelWithGrid);

            panelCenter.setLayout(new BorderLayout(0, 5));
            panelCenter.add(panelWithGrid, BorderLayout.PAGE_START);
            panelCenter.add(panelBottom, BorderLayout.CENTER);
            panelLeft.setPreferredSize(dimLeftPanel);

            buildLayout(panelLeft, panelCenter, panelRight);



        }

        public JPanel createPanelWithGrid() {
            panelWithGrid = new PanelGrid(getSdMenuBar(), getAbstractBoard(), getRightPanel(), this.submitButton);
            return panelWithGrid;
        }

        public void replacePanelGrid(JPanel newPanel){
            BorderLayout layout = (BorderLayout) panelCenter.getLayout();
            panelCenter.remove(layout.getLayoutComponent(BorderLayout.PAGE_START));
            panelCenter.add(newPanel, BorderLayout.PAGE_START);
            panelCenter.revalidate();
            panelCenter.repaint();
        }

        private void setupSubmitButton() {
            this.submitButton = new JButton();
            this.submitButton.setText("Vérifier ma solution");
            this.submitButton.setToolTipText("La grille est incomplète");
            this.submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SDLogicCenter logic = new SDLogicCenter();
                    if (logic.didWeWin(abstractBoard, 11)){
                        System.out.println("we won, apparently from the submit button");
                        initiateWinSequence();
                    }else {
                        System.out.println("hello this is submit button and submitted grid is incorrect");
                    }
                }
            });
            this.submitButton.setEnabled(false);

        }

        public void buildLayout(JPanel leftPan, JPanel panelCenter, PanelOnRight panelRight){
            this.removeAll();
            GroupLayout newLayout = new GroupLayout(this);
            this.setLayout(newLayout);
            newLayout.setAutoCreateGaps(true);
            newLayout.setAutoCreateContainerGaps(false);

            newLayout.setHorizontalGroup(
                    newLayout.createSequentialGroup()
                            .addComponent(leftPan, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelCenter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelRight, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );

            newLayout.setVerticalGroup(
                    newLayout.createSequentialGroup()
                            .addGroup(newLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(leftPan)
                                    .addComponent(panelCenter)
                                    .addComponent(panelRight, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            pack();
        }


        private void initPanelBottom() {

            panelBottom.setBackground(Color.MAGENTA);
            panelBottom.setPreferredSize(dimBottomPanel);
            panelBottom.setLayout(new FlowLayout());
            loadButtonsPlayMode();
        }


        public void loadButtonsPlayMode() {
            JButton buttonTestDisplay = new JButton("Display abstractboard");
            buttonTestDisplay.addActionListener(this::displayAbstractBoard);
            panelBottom.add(buttonTestDisplay);
            JButton buttonGameStartDisplay = new JButton("Display game start board");
            buttonGameStartDisplay.addActionListener(this::displayGameStartAction);
            panelBottom.add(buttonGameStartDisplay);


            JButton buttonReset = new JButton("Reset");
            buttonReset.addActionListener(this::resetBoardAction);
            panelBottom.add(buttonReset);
            JButton buttonFullClear = new JButton("Clear");
            buttonFullClear.addActionListener(this::clearBoard);
            panelBottom.add(buttonFullClear);
            JButton customGridButton = new JButton("Faire une grille");
            customGridButton.addActionListener(this::goToGridMakingMod);
            panelBottom.add(customGridButton);
            panelBottom.add(this.submitButton);

        }

        private void displayGameStartAction(ActionEvent actionEvent) {
            displayGameStart();
        }

        private void displayGameStart() {
            SDLogicCenter logic = new SDLogicCenter();
            logic.displayAbstractBoard(currentGame.getStartingPosition());





            System.out.println("heyoooooooo");
        }

        private void displayAbstractBoard(ActionEvent evt){
            SDLogicCenter logic = new SDLogicCenter();
            System.out.println("abstractBoard");
            logic.displayAbstractBoard(abstractBoard);
            System.out.println("abstractPlayBoard");
            logic.displayAbstractBoard(abstractPlayBoard);
            System.out.println("abstractBuildBoard");
            logic.displayAbstractBoard(abstractBuildBoard);
        }

        public void resetBoardAction(ActionEvent e){
            resetBoard();
        }

        public void resetBoard(){
            initAbstractBoard(abstractBoard,11);
            updateConcreteBoard();
            this.getPanelWithGrid().revalidate();
            this.getPanelWithGrid().repaint();
            this.getPanelWithGrid().playActionHandler.setActionDepthMeter(0);

            sdMenuBar.disableUndo();
            sdMenuBar.disableRedo();
        }



        public void updateConcreteBoard(){
            SDLogicCenter logic = new SDLogicCenter();
            for(int j =0;j<9;j++){
                for(int i=0;i<9;i++){
                    int value = abstractBoard[j][i];
                    PanelNumber panel = this.panelWithGrid.getPanelFromCoords(i, j);
                    panel.changeNumber(value);
                    panel.setValue(value);
                  }
            }
        }

        private void clearBoard(ActionEvent actionEvent) {
            clearBoard();
        }

        private void clearBoard() {
            initAbstractBoard(abstractBoard,0);
            updateConcreteBoard();
            this.panelWithGrid.revalidate();
            this.panelWithGrid.repaint();
            this.getPanelWithGrid().playActionHandler.setActionDepthMeter(0);
            this.getPanelWithGrid().playActionHandler.clearActions();
        }

        private void goToGridMakingMod(ActionEvent event ) {


            saveLockedPanels();
            this.panelWithGrid.unlockAllPanels();
            switchAbstractBoards();
            panelRight.switchModes();


            initAbstractBoard(abstractBoard, 0);
            updateConcreteBoard();
            //all PanelNumbers will now check grid coherence before changing value
            setGridCoherenceStatus(true);

            PanelGrid.GridActionsHandler handler = this.panelWithGrid.actionHandler;
            handler = this.panelWithGrid.buildActionHandler;
            this.panelWithGrid.setCheckCoherence(true);

            panelBottom.setLayout(new FlowLayout(FlowLayout.CENTER));
            initAbstractBoard(abstractBoard, 0);
            goBuildOptionPanGridMakingMod();
        }

        private void saveLockedPanels() {
            for (PanelNumber pan : this.getPanelWithGrid().getPanels()){
                if (pan.isLocked()){
                    this.getPanelWithGrid().getLockedPanels().add(pan);
                }
            }

        }

        private void goBuildOptionPanGridMakingMod() {
            SDLogicCenter logic = new SDLogicCenter();


            //disable undo menu
            sdMenuBar.disableUndo();
            sdMenuBar.disableRedo();

            clearOptions();
            getBottomPanel().setLayout(new FlowLayout());
            //buttons management
            JButton validationButton = new JButton("Jouer cette grille");
            validationButton.addActionListener(this::goPlay);
            panelBottom.add(validationButton);
            JButton cancelButton = new JButton("Annuler");
            cancelButton.addActionListener(this::cancelGridmaking);
            panelBottom.add(cancelButton);


            JButton buttonMultipleSolutions = new JButton("Au moins 2 solutions");
            buttonMultipleSolutions.addActionListener(this::testGridMultiplePossibilities);
            panelBottom.add(buttonMultipleSolutions);
            JButton printPossibilities = new JButton("Voir les solutions");
            printPossibilities.addActionListener(this::displayGridPossibilities);
            panelBottom.add(printPossibilities);
            JButton randomNumberPlacer = new JButton("placer un nombre au hasard");
            randomNumberPlacer.addActionListener(this::placeOneRandomNumber);
            panelBottom.add(randomNumberPlacer);
            JButton gridSuggest = new JButton("Assistance automatique");
            gridSuggest.addActionListener(this::goAutoGridMakingModAction);
            panelBottom.add(gridSuggest);
            JButton randomRemove = new JButton("Vider une case au hasard");
            randomRemove.addActionListener(this::removeRandomNumber);
            panelBottom.add(randomRemove);
//            JButton buttonPainter = new JButton("Paint Grid");
//            buttonPainter.addActionListener(this::gridPainter);
//            panelBottom.add(buttonPainter);

            JButton buttonGridSetupForTesting = new JButton("Position test exclusion");
            buttonGridSetupForTesting.addActionListener(this::setGridToTestPosition);
            panelBottom.add(buttonGridSetupForTesting);


            JButton buttonGridSetupForNPTesting = new JButton("Position test DNP");
            buttonGridSetupForNPTesting.addActionListener(this::setGridToNPTestPosition);
            panelBottom.add(buttonGridSetupForNPTesting);


            JButton buttonHumanSolver = new JButton("Résolution à l'humaine");
            buttonHumanSolver.addActionListener(this::humanSolver);
            panelBottom.add(buttonHumanSolver);

            JButton buttonComputerSolver = new JButton("Résolution brutale");
            buttonComputerSolver.addActionListener(this::computerSolver);
            panelBottom.add(buttonComputerSolver);

//            JButton buttonTest = new JButton("Test");
//            buttonTest.addActionListener(this::reducedGrid);
//            panelBottom.add(buttonTest);
//            JButton buttonHandling = new JButton("Test handling");
//            buttonHandling.addActionListener(this::handlingGridTest);
//            panelBottom.add(buttonHandling);

            JButton buttonTestDisplay = new JButton("Display abstractboard");
            buttonTestDisplay.addActionListener(this::displayAbstractBoard);
            panelBottom.add(buttonTestDisplay);

            JButton buttonEasy = new JButton("Facile");
            buttonEasy.addActionListener(this::makeEasyGrid);
            panelBottom.add(buttonEasy);

            JButton buttonReduce = new JButton("Réduire");
            buttonReduce.addActionListener(this::reduceGrid);
            panelBottom.add(buttonReduce);

            JButton buttonReduceMore = new JButton("Réduire plus");
            buttonReduceMore.addActionListener(this::reduceGridMore);
            panelBottom.add(buttonReduceMore);


            JButton buttonLoadAGameFromFile = new JButton("Grille 17");
            buttonLoadAGameFromFile.addActionListener(this::load17GridAction);
            panelBottom.add(buttonLoadAGameFromFile);

            JButton buttonLoadAGame18 = new JButton("Grille 18");
            buttonLoadAGame18.addActionListener(this::load18GridAction);
            panelBottom.add(buttonLoadAGame18);

            panelBottom.revalidate();
            panelBottom.repaint();



        }


        private void goAutoGridMakingModAction(ActionEvent e){
            goAutoGridMakingMod();
        }


        private void goAutoGridMakingMod(){
            clearOptions();


            getBottomPanel().setLayout(new BorderLayout());
            JPanel upperPan = new JPanel();
            upperPan.setOpaque(false);
            JPanel centerPan = new JPanel();
            centerPan.setOpaque(false);
            upperPan.setLayout(new BoxLayout(upperPan, BoxLayout.Y_AXIS));
            centerPan.setLayout(new BoxLayout(centerPan, BoxLayout.Y_AXIS));

            panelBottom.add(upperPan, BorderLayout.LINE_START);
            panelBottom.add(centerPan, BorderLayout.CENTER);


            JButton goBackButton = new JButton("Retour");
            goBackButton.addActionListener(this::returnToNormalGridMaking);
            goBackButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            upperPan.add(goBackButton);

            JButton buttonVeryEasyGrid = new JButton("Très facile");
            buttonVeryEasyGrid.addActionListener(this::createGridVeryEasyAction);
            buttonVeryEasyGrid.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPan.add(buttonVeryEasyGrid);

            JButton buttonEasyGrid = new JButton("Facile");
            buttonEasyGrid.addActionListener(this::createGridEasyAction);
            buttonEasyGrid.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPan.add(buttonEasyGrid);

            JButton buttonNormalGrid = new JButton("Normal");
            buttonNormalGrid.addActionListener(this::createGridNormalAction);
            buttonNormalGrid.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPan.add(buttonNormalGrid);

            JButton buttonHardGrid = new JButton("Dur");
            buttonHardGrid.addActionListener(this::giveHardGrid);
            buttonHardGrid.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPan.add(buttonHardGrid);

            JButton buttonVeryHardGrid = new JButton("Très dur");
            buttonVeryHardGrid.addActionListener(this::giveVeryHardGrid);
            buttonVeryHardGrid.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPan.add(buttonVeryHardGrid);

            JButton buttonDiabolicGrid = new JButton("Diabolic");
            buttonDiabolicGrid.addActionListener(this::giveDiabolicalGrid);
            buttonDiabolicGrid.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPan.add(buttonDiabolicGrid);

            JButton buttonXtremeGrid = new JButton("Non.");
            buttonXtremeGrid.addActionListener(this::giveXtremeGrid);
            buttonXtremeGrid.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPan.add(buttonXtremeGrid);

        }

        private void returnToNormalGridMaking(ActionEvent actionEvent) {
            getPanelBottom().removeAll();

            goBuildOptionPanGridMakingMod();
        }


        private void load17GridAction(ActionEvent actionEvent) {
            //TODO
            load17Grid();
        }

        private void load17Grid() {
            String[] anArray = FileManager.giveArrayFromFile();

            Random rand = new Random();
            String chosenString = anArray[rand.nextInt(anArray.length)];
            //sout
            System.out.println(chosenString);
            int[][] grid = makeGridFromExtractedString(chosenString);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    abstractBoard[j][i] = grid[j][i];
                }
            }
            updateConcreteBoard();




        }



        private void load18GridAction(ActionEvent actionEvent) {
            load18Grid();
        }


        private void giveHardGrid(ActionEvent actionEvent){
            makeGridFromFile(23);
        }
        private void giveVeryHardGrid(ActionEvent actionEvent){
            makeGridFromFile(20);
        }
        private void giveDiabolicalGrid(ActionEvent actionEvent){
            makeGridFromFile(18);
        }
        private void giveXtremeGrid(ActionEvent actionEvent){
            makeGridFromFile(17);
        }

        private void makeGridFromFile(int totalClues){
            int toAdd = totalClues - 17;
            loadGridAdjusted(toAdd);

        }


        private void loadGridAdjusted(int toAdd){
            String[] anArray = FileManager.giveArrayFromFile();

            Random rand = new Random();
            String chosenString = anArray[rand.nextInt(anArray.length)];
            //sout
            System.out.println(chosenString);
            int[][] grid = makeGridFromExtractedString(chosenString);

            SDLogicCenter logic = new SDLogicCenter();
            int[][] auxiliary = logic.copyOfAbstractBoard(grid);
            logic.humanSolver(auxiliary);
            logic.possibleGrid(auxiliary);

            makeGridFromA17(rand, grid, auxiliary, toAdd);
            logic.shuffleGrid(grid);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    abstractBoard[j][i] = grid[j][i];
                }
            }
            updateConcreteBoard();
        }


        private void load18Grid() {
            String[] anArray = FileManager.giveArrayFromFile();

            Random rand = new Random();
            String chosenString = anArray[rand.nextInt(anArray.length)];
            //sout
            System.out.println(chosenString);
            int[][] grid = makeGridFromExtractedString(chosenString);

            SDLogicCenter logic = new SDLogicCenter();
            int[][] auxiliary = logic.copyOfAbstractBoard(grid);
            logic.humanSolver(auxiliary);
            logic.possibleGrid(auxiliary);

            makeGridFromA17(rand, grid, auxiliary, 6);

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    abstractBoard[j][i] = grid[j][i];
                }
            }
            updateConcreteBoard();
        }



        private void makeGridFromA17(Random rand, int[][] grid, int[][] auxiliary, int numberToAdd) {
            int u = rand.nextInt(9);
            int v = rand.nextInt(9);

            for (int i = 0; i < numberToAdd; i++) {

                while(grid[u][v] != 0){
                    u = rand.nextInt(9);
                    v = rand.nextInt(9);
                }
                grid[u][v] = auxiliary[u][v];
            }

        }

        private int[][] makeGridFromExtractedString(String str){
            //TODO
            int[][] newGrid = new int[9][9];
            int[] asArrayNumber = new int[81];
            for (int i = 0; i < str.length(); i++) {

                if(str.charAt(i) == '.'){
                    asArrayNumber[i] = 0;
                }else {
                    asArrayNumber[i] = Integer.parseInt(String.valueOf(str.charAt(i)));
                }
            }


            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    newGrid[i][j] = asArrayNumber[i*9 + j];
                }
            }

            SDLogicCenter logic = new SDLogicCenter();
            logic.displayAbstractBoard(newGrid);

            return newGrid;

        }


        private void computerSolver(ActionEvent event) {
            solveGridAsComputer(abstractBoard);


        }

        private void solveGridAsComputer(int[][] abstractBoard) {
            SDLogicCenter logic = new SDLogicCenter();
            logic.possibleGrid(abstractBoard);
            updateConcreteBoard();
        }

        private void reduceGrid(ActionEvent event) {
            reduceGridd();
        }

        private void reduceGridd() {
            SDLogicCenter logic = new SDLogicCenter();
            int[] coordsForMethod = logic.randomList1to81();
            int number = 36;
            int clueNumber = number;
            boolean bool = false;
            do {

                bool = logic.reduceGridtoXClues(abstractBoard, coordsForMethod, clueNumber);
                clueNumber++;
            }while (!bool);

            if (clueNumber != number+1){
                reduceGridd();
            }


            System.out.println("in the end we had "+(logic.nbNotEmptySquare(abstractBoard))+" clues placed");
            updateConcreteBoard();
        }


        private void reduceGridd(int[][] grid, int downTo) {
            SDLogicCenter logic = new SDLogicCenter();
            int[] coordsForMethod = logic.randomList1to81();
            int number = downTo;
            int clueNumber = number;
            boolean bool = false;
            do {

                bool = logic.reduceGridtoXClues(grid, coordsForMethod, clueNumber);
                clueNumber++;
            }while (!bool);

            if (clueNumber != number+1){
                reduceGridd();
            }


            System.out.println("in the end we had "+(logic.nbNotEmptySquare(grid))+" clues placed");
            updateConcreteBoard();
        }


        public void reduceGridMore(ActionEvent event){

            reduceGridFailSafe(24);
        }

        private void reduceGridFailSafe(int numberToGoDownTo){

            int[] counter = new int[1];
            reduceGridCountered(counter, numberToGoDownTo);



        }

        private void reduceGridCountered(int[] counter, int goDownTo) {

            SDLogicCenter logic = new SDLogicCenter();
            int[] coordsForMethod = logic.randomList1to81();
            int clueNumber = goDownTo;
            boolean bool = false;
            do {

                bool = logic.reduceGridtoXClues(abstractBoard, coordsForMethod, clueNumber);
                clueNumber++;
            }while (!bool);

            if (clueNumber != goDownTo+1){
                System.out.println("couter : "+counter[0]);
                if (counter[0] > 10){
                    counter[0] = 0;
                    giveEasyGrid();
                    System.out.println("we tried a different grid to reduce");
                }
                counter[0]++;
                reduceGridCountered(counter, goDownTo);
            }


            System.out.println("in the end we had "+(logic.nbNotEmptySquare(abstractBoard))+" clues placed");
            updateConcreteBoard();

        }


        private void makeEasyGrid(ActionEvent event) {
            System.out.println("hé");
            giveEasyGrid();
        }

        private void giveEasyGrid(){

            //TODO HERE
            System.out.println("there");
            SDLogicCenter logic = new SDLogicCenter();
            int[][] madeGrid = new int[9][9];
            logic.generateGridEasy(madeGrid);
            int[] coords = logic.randomList1to81();


            abstractBoard = logic.copyOfAbstractBoard(madeGrid);
            updateConcreteBoard();
        }

        private void createGridVeryEasyAction(ActionEvent e){
            createGridMoreThan24Clues(36);
        }
        private void createGridMoreThan24Clues(int finalClueNumber){
            SDLogicCenter logic = new SDLogicCenter();
            int[][] madeGrid = new int[9][9];
            logic.generateGridEasy(madeGrid);
            reduceSome(madeGrid, finalClueNumber);
            logic.shuffleGrid(madeGrid);
            abstractBoard = madeGrid;
            System.out.println("in the end we had "+(logic.nbNotEmptySquare(abstractBoard))+" clues placed");
            updateConcreteBoard();
        }

        private void reduceSome(int[][] gridToReduce, int finalClueNumber){
            SDLogicCenter logic = new SDLogicCenter();
            int[] coordsForMethod = logic.randomList1to81();
            int number = finalClueNumber;
            int clueNumber = number;
            boolean bool = false;
            do {

                bool = logic.reduceGridtoXClues(gridToReduce, coordsForMethod, clueNumber);
                clueNumber++;
            }while (!bool);

            if (clueNumber != number+1){
                reduceSome(gridToReduce, finalClueNumber);
            }
        }


        private void createGridEasyAction(ActionEvent e){
            createGridMoreThan24Clues(31);
        }

        private void createGridNormalAction(ActionEvent e){
            createGridMoreThan24Clues(26);
        }


        private boolean checkGridPurity(){

            SDLogicCenter logic = new SDLogicCenter();
            boolean movingOn = true;

            //if grid is impure we warn user
            ArrayList<int[][]> list = new ArrayList<>();
            boolean check = false;
            logic.twoOrMorePossibleGrids(abstractBuildBoard, list);
            check = list.size() == 2;
//            logic.displayAbstractBoard(abstractBuildBoard);


            if (check) {
                //Custom button text
                System.out.println("waddup");
                Object[] options = {"Jouer quand même",
                        "Ah mince, il faut régler ça",
                };
                int n = JOptionPane.showOptionDialog(mainPanel.getPanelWithGrid(),
                        "Attention ! Vous vous apprêtez à jouer une grille \"impure\", c'est à dire qu'au moins 2 solutions existent pour la grille. ",
                        "Solutions multiples au puzzle",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[1]);

                if (n == 0) {
                    movingOn = true;

                } else if (n == 1) {
                    movingOn = false;
                }
            }
            return movingOn;
        }

        private void goPlay(ActionEvent event) {
            //TODO


            this.getPanelWithGrid().getLockedPanels().clear();
            SDLogicCenter logic = new SDLogicCenter();

            if (checkGridPurity()){

                //we go to playMode

                clearOptions();
                loadButtonsPlayMode();

                //we lock panels that are not at 0

                switchAbstractBoards();
                setGridCoherenceStatus(false);
                panelRight.goModePlay();
                resetActions();

                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        abstractBoard[j][i] = abstractBuildBoard[j][i];
                    }
                }
                for (PanelNumber pan : this.getPanelWithGrid().getPanels()){
                    pan.setLocked(false);
                }
                updateConcreteBoard();
                this.getPanelWithGrid().lockNonEmptyPanels();

                //TODO
                //we store solution(s) somewhere
                currentGame = new Game(abstractBoard);

            }


        }

        private void resetActions() {
            this.getPanelWithGrid().getGridActionsHandler().clearActions();
        }

        private void handlingGridTest(ActionEvent event) {
            SDLogicCenter logic = new SDLogicCenter();
            logic.testingGridHandlingOrder(abstractBoard);
        }

        private void clearOptions( ){
            Component[] components = panelBottom.getComponents();
            for (Component comp : components){
                if (comp instanceof JButton){
                    panelBottom.remove(comp);

                }
            }
            panelBottom.revalidate();
            panelBottom.repaint();

        }

        private void testMethod(ActionEvent event){
            for (int j = 0; j < 9; j++) {
                for (int i = 0; i < 9; i++) {
                    System.out.print(this.panelWithGrid.getPanelFromCoords(i, j).value);
                }
                System.out.println();
            }
            System.out.println(panelLeft.getHeight());
        }

        private void setGridCoherenceStatus(boolean newStatus){
            for(Component panel : getPanelWithGrid().getComponents()){
                if (panel instanceof PanelNumber){
                    ((PanelNumber) panel).setGridCoherenceStatus(newStatus);
                }
            }
        }

        private void cancelGridmaking(ActionEvent event) {

            clearOptions();
            loadButtonsPlayMode();
           //PanelNumbers will no longer check grid coherence before changing value
            setGridCoherenceStatus(false);

            switchAbstractBoards();
            panelRight.switchModes();
            updateConcreteBoard();
            for (PanelNumber pan : this.getPanelWithGrid().getLockedPanels()){
                pan.lockPanel();
            }


            panelWithGrid.revalidate();
            panelWithGrid.repaint();


            if(this.panelWithGrid.actionHandler.getActionDepthMeter() != 0){
                sdMenuBar.enableRedo();
            }
            if(this.panelWithGrid.actionHandler.getActionList().size() != 0){
                sdMenuBar.enableUndo();
            }


        }


        private void setAllButtonsTo(boolean mode){
            Component[] buttons = this.getBottomPanel().getComponents();
            for (Component item : buttons){
                if (item instanceof JButton){
                    item.setEnabled(mode);
                }
            }
        }


        private boolean testGridMultiplePossibilities(ActionEvent event) {
            //make temporary copy of board and attempt to solve it
            SDLogicCenter logic = new SDLogicCenter();
            int[][] temporaryCopyOfAbstractBoard = new int[9][9];
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    temporaryCopyOfAbstractBoard[i][j] = abstractBoard[i][j];
                }
            }
            System.out.println("testGridMultiplePossibilities");
            ArrayList<int[][]> solutionHolder = new ArrayList<>();
            logic.twoOrMorePossibleGrids(temporaryCopyOfAbstractBoard, solutionHolder);
            for (int[][] solution : solutionHolder){
                logic.displayAbstractBoard(solution);
            }
            System.out.println((solutionHolder.size() == 2));
            return (solutionHolder.size() == 2);
        }

        private void displayGridPossibilities(ActionEvent event) {
            SDLogicCenter logic = new SDLogicCenter();
            //make temporary copy of board and attempt to solve it
            int[][] temporaryCopyOfAbstractBoard = new int[9][9];
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    temporaryCopyOfAbstractBoard[i][j] = abstractBoard[i][j];
                }
            }

//        ArrayList<int[][]> solutionHolder = new ArrayList<>();
            ArrayList<int[][]> solutionHolder = new ArrayList<>();
            solutionHolder = getAllSolutions(temporaryCopyOfAbstractBoard);
            System.out.println("We found " + solutionHolder.size() + " solutions");

            for (int[][] validGrid : solutionHolder) {
                logic.displayAbstractBoard(validGrid);
            }
            System.out.println("solutions in holder : " + solutionHolder.size());
            System.out.println("made it out");
        }


        private void placeOneRandomNumber(ActionEvent event) {

            placeOneRandomNumber(abstractBoard);
        }

        private void placeOneRandomNumber(int[][] board) {
            SDLogicCenter logic = new SDLogicCenter();
            Random rand = new Random();
            int coordX = rand.nextInt(9);
            int coordY = rand.nextInt(9);
            int value = rand.nextInt(9)+1;
            if (board[coordY][coordX] == 0 && logic.possibleFit(coordX,coordY,value, board)){
//            System.out.println("current value is : "+ getPanelFromCoords(coordX, coordY).value +" on abcisse : "+ (coordX+1) +", ordonnée : "+ (coordY+1));
//            System.out.println("Current value according to abstractBoard : "+board[coordY][coordX]);
//            System.out.println("and we are changing it to : "+ value);

                this.panelWithGrid.getPanelFromCoords(coordX, coordY).changeNumber(board, value);
//            System.out.println("Placed value "+value+" at position Abcisse : "+(coordX+1)+", ordonnée : "+(coordY+1));
            }else {
                placeOneRandomNumber(board);
            }
        }


        private ArrayList<int[][]> getAllSolutions(int[][] currentBoard){
            SDLogicCenter logic = new SDLogicCenter();

            //copying currentBoard
            int[][] temporaryCopyOfAbstractBoard = new int[9][9];
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    temporaryCopyOfAbstractBoard[i][j] = currentBoard[i][j];
                }
            }
            //We need an ArrayList
            ArrayList<int[][]> allSolutions = new ArrayList<>();
            logic.putSolutionsIn(temporaryCopyOfAbstractBoard, allSolutions);
            return allSolutions;
        }


        private void AutoGridMaker(ActionEvent event){
            SDLogicCenter logic = new SDLogicCenter();
            int[][] gridMade = validRandomCompleteGrid2();
            abstractBoard = gridMade;
//        displayAbstractBoard(gridMade);
            updateConcreteBoard();
        }

        private int[][] validRandomCompleteGrid2(){
            SDLogicCenter logic = new SDLogicCenter();
            int[] xAxis = logic.randomSequence0to8();
            int[] yAxis = logic.randomSequence0to8();
            int[] coords = logic.randomList1to81();
            int[][] board = new int[9][9];
//        initializeAbstractBoard(board, 0);

            while (logic.nbNotEmptySquare(board) < 16){
                placeOneRandomNumber(board);
//            System.out.println("hi");
//            System.out.println(16 - nbNotEmptySquare(board)+" clues yet to place");

            }
            System.out.println("We placed "+(logic.nbNotEmptySquare(board))+" clues");

            int[] counter = new int[1];
            counter[0] = 0;
//        possibleGridRandomly(board, xAxis, yAxis, counter);
//        possibleGridRandomly(board, coords, counter);
//        possibleGridRandomly3(board, coords, counter);
            logic.possibleGridCounter(board, counter);
//            System.out.println("ya "+counter[0]);
//        possibleGrid(board);

            return board;
        }

        private void removeRandomNumber(ActionEvent event){
            Random rand = new Random();
            PanelNumber pan = this.panelWithGrid.getPanelFromCoords(rand.nextInt(9), rand.nextInt(9));
            if(pan.value != 0){
//                this.panelWithGrid.playActionHandler.addActionToList( pan, pan.getValue(), 0);
                this.panelWithGrid.playActionHandler.addActionToList( pan, pan.getValue(), 0);
                pan.changeNumber(0);

            }else {
                removeRandomNumber(event);
            }
        }

        private void gridPainter(ActionEvent event){
            int counter = 1;
            for (int i = 0; i <9; i++) {
                for (int j = 0; j < 9; j++) {
                    abstractBoard[i][j] = counter % 10;
                    counter++;
                    updateConcreteBoard();

                }
            }
        }

        private void setGridToTestPosition(ActionEvent event) {

            abstractBoard[0][1] = 8;
            abstractBoard[0][2] = 5;
            abstractBoard[0][3] = 2;
            abstractBoard[0][4] = 4;
            abstractBoard[1][1] = 6;
            abstractBoard[1][3] = 3;
            abstractBoard[1][6] = 9;
            abstractBoard[1][7] = 8;
            abstractBoard[2][8] = 4;
            abstractBoard[3][3] = 1;
            abstractBoard[3][8] = 6;
            abstractBoard[3][7] = 7;
            abstractBoard[3][6] = 4;
            abstractBoard[4][3] = 4;
            abstractBoard[4][4] = 5;
            abstractBoard[4][8] = 8;
            abstractBoard[4][7] = 3;
            abstractBoard[4][6] = 1;
            abstractBoard[5][0] = 4;
            abstractBoard[5][1] = 1;
            abstractBoard[5][2] = 7;
            abstractBoard[5][3] = 8;
            abstractBoard[5][4] = 6;
            abstractBoard[5][5] = 3;
            abstractBoard[5][6] = 2;
            abstractBoard[5][7] = 9;
            abstractBoard[5][8] = 5;
            abstractBoard[6][0] = 6;
            abstractBoard[6][3] = 9;
            abstractBoard[6][6] = 3;
            abstractBoard[7][0] = 1;
            abstractBoard[7][4] = 8;
            abstractBoard[8][1] = 7;

            updateConcreteBoard();
        }

        private void setGridToNPTestPosition(ActionEvent event) {

            abstractBoard[0][1] = 7;
            abstractBoard[0][2] = 9;
            abstractBoard[0][6] = 6;
            abstractBoard[0][8] = 1;
            abstractBoard[1][2] = 1;
            abstractBoard[1][4] = 6;
            abstractBoard[1][5] = 7;
            abstractBoard[2][0] = 6;
            abstractBoard[2][1] = 2;
            abstractBoard[2][2] = 4;
            abstractBoard[2][3] = 9;
            abstractBoard[2][6] = 3;
            abstractBoard[2][7] = 7;

            abstractBoard[3][2] = 6;
            abstractBoard[3][5] = 4;
            abstractBoard[4][3] = 1;
            abstractBoard[4][5] = 5;
            abstractBoard[4][7] = 6;
            abstractBoard[5][0] = 9;
            abstractBoard[5][1] = 3;

            abstractBoard[6][1] = 6;
            abstractBoard[6][2] = 5;
            abstractBoard[6][6] = 8;
            abstractBoard[6][7] = 9;
            abstractBoard[6][8] = 3;
            abstractBoard[7][0] = 7;
            abstractBoard[7][1] = 9;
            abstractBoard[7][2] = 8;
            abstractBoard[7][5] = 3;
            abstractBoard[7][6] = 5;
            abstractBoard[8][2] = 3;
            abstractBoard[8][3] = 5;
            abstractBoard[8][4] = 8;
            abstractBoard[8][5] = 9;

            updateConcreteBoard();
        }

        public void humanSolver(ActionEvent event){
            SDLogicCenter logic = new SDLogicCenter();
            logic.humanSolver(abstractBoard);
            updateConcreteBoard();
        }

        private void reducedGrid(ActionEvent event) {
            SDLogicCenter logic = new SDLogicCenter();
            int[][] fullGrid = validRandomCompleteGrid2();
            logic.displayAbstractBoard(fullGrid);
            int[][] modifiedGrid = logic.copyOfAbstractBoard(fullGrid);
            int[] xAxis = logic.randomSequence0to8();
            int[] yAxis = logic.randomSequence0to8();
            int[] ind = logic.randomList1to81();
//        gridReducer(modifiedGrid, xAxis, yAxis);
            logic.gridReducer2(modifiedGrid, ind);
            abstractBoard = modifiedGrid;
            logic.displayAbstractBoard(modifiedGrid);
            updateConcreteBoard();

        }

        //--end of play option panel




        //setters for panels
        public void setPanelLeft(JPanel panelLeft) {
            this.panelLeft = panelLeft;
        }


        //getters for panels

        public JPanel getLeftPanel(){
            return this.panelLeft;
        }
        public PanelOnRight getRightPanel(){
            return this.panelRight;
        }
        public JPanel getCenterPanel(){
            return this.panelCenter;
        }
        public JPanel getBottomPanel(){
            return this.panelBottom;
        }
        public PanelGrid getPanelWithGrid(){return this.panelWithGrid;}

        public JButton getSubmitButton() {
            return submitButton;
        }

        public void setSubmitButton(JButton submitButton) {
            this.submitButton = submitButton;
        }

        public int getCoteCarreLength() {
            return coteCarreLength;
        }

    }//--MainPanel------

    private void initiateWinSequence() {
        SwingDokuWindow currentWindow = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                Theme theme = new Theme("boring");

                JPanel panelWin = new JPanel();

                panelWin.setPreferredSize(new Dimension(currentWindow.getMainPanel().getCoteCarreLength(), currentWindow.getMainPanel().getCoteCarreLength()));
                panelWin.setLayout(new BorderLayout());

                JLabel lbelle = new JLabel(theme.getImgWinScreen());
                panelWin.add(lbelle, BorderLayout.CENTER);

                JPanel removed = currentWindow.getMainPanel().getPanelWithGrid();
                removed.removeAll();
                removed.add(panelWin);
                removed.revalidate();
                removed.repaint();

            }

        }).start();



        new Thread(new Runnable() {
            @Override
            public void run() {

//                //here we have to disable all actionable buttons for the user in order to prevent monstruous bugs
////
//                sdMenuBar.setAllMenusTo(false);
//                mainPanel.setAllButtonsTo(false);
//
//                //TODO WE MUST ALSO DISABLE BUTTONS ON RIGHT PANE
//
//
//                final JOptionPane optionPane = new JOptionPane(
//                        "Bravo, vous avez résolu la grille.",
//                        JOptionPane.PLAIN_MESSAGE,
//                        JOptionPane.YES_NO_CANCEL_OPTION);
//
//
//
//
//
//                Window window = SwingUtilities.windowForComponent( mainPanel );
//
//                final JDialog dialog = new JDialog((Frame) window,
//                        "Click a button",
//                        true);
//                final int IDEAL_DIALOG_X_SIZE = 400;
//                final int IDEAL_DIALOG_y_SIZE = 400;
//                dialog.setPreferredSize(new Dimension(IDEAL_DIALOG_X_SIZE, IDEAL_DIALOG_y_SIZE));
//                Point centerPoint = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
//                centerPoint.x -= IDEAL_DIALOG_X_SIZE/2;
//                centerPoint.y -= IDEAL_DIALOG_y_SIZE/2;
//
//                int a=optionPane.showConfirmDialog(dialog,"Are you sure?");
//
//
//                dialog.setLocation(centerPoint);
//                dialog.setContentPane(optionPane);
//                dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
//                for (WindowListener wl : dialog.getWindowListeners()) {
//                    dialog.removeWindowListener(wl);
//                }
//                dialog.addWindowListener(new WindowAdapter() {
//                    @Override
//                    public void windowClosing(WindowEvent e) {
//                        JOptionPane.showMessageDialog(null, "Vous devez obligatoirement choisir une option : )");
//                    }
//                });
//                dialog.pack();
//                dialog.setVisible(true);







                try {
                    Thread.sleep(0001);
                    ActionListener restartFunction = new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JPanel pan = mainPanel.createPanelWithGrid();
                            mainPanel.replacePanelGrid(pan);
                            loadTheme(currentTheme);
                            mainPanel.displayGameStartAction(e);
                            for (int i = 0; i < 9; i++) {
                                for (int j = 0; j < 9; j++) {
                                    abstractBoard[j][i] = currentGame.getStartingPosition()[j][i];
                                }
                            }
                            SDLogicCenter logic = new SDLogicCenter();
                            logic.displayAbstractBoard(abstractBoard);
                            logic.displayAbstractBoard(currentGame.getStartingPosition());
                            mainPanel.updateConcreteBoard();
                            mainPanel.submitButton.setEnabled(false);
                            //reset actions
                            getMainPanel().getPanelWithGrid().playActionHandler.clearActions();
                            getMainPanel().getRightPanel().goModePlay();
                        }
                    };

                    ActionListener newGameFunction = new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            //go back to grid display
                            JPanel pan = mainPanel.createPanelWithGrid();
                            mainPanel.replacePanelGrid(pan);
                            loadTheme(currentTheme);

                            switchAbstractBoards();
                            mainPanel.clearBoard();
                            mainPanel.resetActions();

                            mainPanel.submitButton.setEnabled(false);
                            mainPanel.goBuildOptionPanGridMakingMod();
                            getMainPanel().getRightPanel().goModeBuild();
                        }
                    };
                    ModalRestartOrReplayDialog modal = new ModalRestartOrReplayDialog(restartFunction, newGameFunction);
                    modal.createAndShowGUI(zis);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


                System.out.println("waited three second");


                // https://stackoverflow.com/questions/942056/remove-x-button-in-swing-jdialog



//                Object[] options = {"Rejouer cette grille",
//                        "Faire une nouvelle partie", "option 3"
//                };
//                int n = 666;
//                n = JOptionPane.showOptionDialog(mainPanel.getPanelWithGrid(),
//                        "Bravo",
//                        "Immense triomphe",
//                        JOptionPane.YES_NO_CANCEL_OPTION,
//                        JOptionPane.QUESTION_MESSAGE,
//                        null,
//                        options,
//                        options[2]);
//
//
//                optionChoice[0] = n;
//                if (optionChoice[0] == 0){
//                    mainPanel.resetActions();
//                    //We need a method to remove panel messages
//                    for (JPanel pan : mainPanel.getRightPanel().getListPanels()){
//                        mainPanel.getRightPanel().getPanelInScroller().remove(pan);
//
//                    }
//                    mainPanel.getRightPanel().getPanelInScroller().revalidate();
//                    mainPanel.getRightPanel().getPanelInScroller().repaint();
//                    mainPanel.getRightPanel().revalidate();
//                    mainPanel.getRightPanel().repaint();
//                    mainPanel.getPanelWithGrid().setAllPansToCertitude();
//                    mainPanel.clearBoard();
//                    mainPanel.getPanelWithGrid().unlockAllPanels();
//                    SDLogicCenter logic = new SDLogicCenter();
//                    //copie manuelle de abstractBoard
//                    int[][] temporaryCopy = currentGame.getStartingPosition();
//                    for (int j = 0; j < 9; j++) {
//                        for (int i = 0; i < 9; i++) {
//                            abstractBoard[j][i] = temporaryCopy[j][i];
//                        }
//                    }
//
//
//
//
//
//                    mainPanel.getPanelWithGrid().displayModeToPanelNumbers();
//                    mainPanel.updateConcreteBoard();
//                    mainPanel.getPanelWithGrid().lockNonEmptyPanels();
//
//                    System.out.println("option 1 was picked");
//
//
//                }else if (optionChoice[0] == 1){
//                    System.out.println("option 2 was picked");
//                }else if (optionChoice[0] == 2){
//                    System.out.println("option 3 was picked");
//                }
//
//                sdMenuBar.setAllMenusTo(true);
//                mainPanel.setAllButtonsTo(true);
            }

        }).start();





        //TODO COMPLETE END GAME MENU AND THREAD MANAGEMENT


    }
//-----------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------



    private void switchAbstractBoards() {
        PanelGrid panelGrid = this.getMainPanel().getPanelWithGrid();
        if (abstractBoard == abstractPlayBoard){
            abstractBoard = abstractBuildBoard;
            panelGrid.setCheckCoherence(true);
            panelGrid.setAbstractBoard(abstractBuildBoard);
            for (PanelNumber pan : panelGrid.getPanels()){
                pan.setAbstractBoard(abstractBuildBoard);
            }
            switchActionHandlers();

        }else{
            abstractBoard = abstractPlayBoard;
            panelGrid.setAbstractBoard(abstractPlayBoard);
            panelGrid.setCheckCoherence(false);
            for (PanelNumber pan : panelGrid.getPanels()){
                pan.setAbstractBoard(abstractPlayBoard);
            }
            switchActionHandlers();
        }
    }

    private void switchActionHandlers() {
        PanelGrid panelGrid = this.mainPanel.getPanelWithGrid();
        panelGrid.swapActionHandler();

    }


    private void initMainPanel() {
        initGridPanel() ;
        mainPanel = new MainPanel();
        mainPanel.setBackground(Color.red);
        mainPanel.getCenterPanel().setBackground(Color.red);

        this.add(mainPanel);

    }

    private void initGridPanel() {
        //initialize abstractBoard
        if (abstractPlayBoard == null){
            abstractPlayBoard = new int[9][9];
        }
        abstractBoard = abstractPlayBoard;
        initAbstractBoard(abstractBoard, 11);

    }



    private void initAbstractBoard(int[][] board, int valueForAllTiles) {
        SDLogicCenter logic = new SDLogicCenter();
        for(int j = 0; j<9;j++){
            for(int i = 0; i<9; i++){
                if (valueForAllTiles == 11){
                    Random rand = new Random();
                    board[i][j] = rand.nextInt(10);
                }else {
                    board[i][j] = valueForAllTiles;
                }
            }
        }

    }

    public void loadTheme(String valideThemeName){

        Theme theme = new Theme(valideThemeName);
        //left panel image
        JPanel panLeft = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                theme.setImgIconLeftPan(theme.imgLeftPanelResizer(this.getWidth(), this.getHeight()));
                g.drawImage(theme.getImgIconLeftPan().getImage(), 0, 0, null);
            }
        };
        panLeft.setPreferredSize(new Dimension(this.getMainPanel().getLeftPanel().getWidth(), this.getMainPanel().getLeftPanel().getHeight()));
        this.getMainPanel().setPanelLeft(panLeft);
//        this.getMainPanel().buildLayout(panLeft, this.getMainPanel().getPanelCenter(), this.getMainPanel().getRightPanel());


//        JLabel labelTest = new JLabel("coucou");
//        labelTest.setBackground(Color.WHITE);
//        panLeft.add(labelTest);


        panLeft.revalidate();
        panLeft.repaint();

        //central panel gaps
        this.getMainPanel().getCenterPanel().setBackground(theme.getCentralPanelGapsColor());
        this.getMainPanel().setBackground(theme.getCentralPanelGapsColor());


        //grid numbers
        this.getMainPanel().getPanelWithGrid().setLoadedNumberIcons(theme.getImgsNormal());
        this.getMainPanel().getPanelWithGrid().setLoadedNumberIconsFocussed(theme.getImgsFocussed());
        for (PanelNumber pan : this.getMainPanel().getPanelWithGrid().getPanels()){
            pan.setImgsNormal(theme.getImgsNormal());
            pan.setImgsFocus(theme.getImgsFocussed());
            pan.setImgsSmall(theme.getImgsSmall());
            pan.setImgsLocked(theme.getImgsLocked());
            pan.setImgsSmallFocussed(theme.getImgsSmallAndFocussed());
            pan.updateTheme();
            pan.setFrameColorIn(theme.getFrameColor());
            pan.setFrameColorOut(theme.getCentralPanelGapsColor());
            pan.frame(pan.coordX, pan.coordY, theme.getFrameColor(), theme.getCentralPanelGapsColor());
            if (!pan.isLocked()){
                pan.getPopupMenu().removeAll();
                pan.buildPopupMenuCertitudeMain();
                pan.setupListeners();
            }
        }
        this.getMainPanel().getPanelWithGrid().reloadPanelsForTheme();


        //option panel
        JPanel panBot = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                theme.setImgIconOptionPan(theme.imgOptionPanelResizer(this.getWidth(), this.getHeight()));
                g.drawImage(theme.getImgIconOptionPan().getImage(), 0, 0, null);
            }
        };

        this.getMainPanel().getPanelCenter().remove(this.getMainPanel().getBottomPanel());
        this.getMainPanel().setPanelBottom(panBot);
        this.getMainPanel().getPanelCenter().add(this.getMainPanel().getBottomPanel(), BorderLayout.CENTER);
        this.getMainPanel().getPanelBottom().setPreferredSize(new Dimension(this.getMainPanel().getBottomPanel().getWidth(), this.getMainPanel().getBottomPanel().getHeight()));

        if (this.getMainPanel().getPanelWithGrid().isCheckCoherence()){
            this.getMainPanel().goBuildOptionPanGridMakingMod();
        }else{
            this.getMainPanel().loadButtonsPlayMode();
        }

        this.getMainPanel().revalidate();
        this.getMainPanel().repaint();
        panBot.revalidate();
        panBot.repaint();
        
        //panel right backGround
        this.getMainPanel().getRightPanel().setBackgroundImage(theme.getImgIconRightPan());
        this.getMainPanel().getRightPanel().getPanelInScroller().revalidate();
        this.getMainPanel().getRightPanel().getPanelInScroller().repaint();
        this.getMainPanel().getRightPanel().setPreferredSize(new Dimension(this.getMainPanel().getRightPanel().getWidth(), this.getMainPanel().getRightPanel().getHeight()));
        this.getMainPanel().buildLayout(panLeft, this.getMainPanel().getPanelCenter(), this.getMainPanel().getRightPanel());


        //panel right messages




        PanelOnRight PRight = this.getMainPanel().getRightPanel();
        PRight.setPanMsgBorderMotif(theme.getImgIconMsgeBorder());
        PRight.setMsgBackgroundColor(theme.getPanRightMsgColor());
        ArrayList<PanelOnRight.PanelMessage> listPan = PRight.getListPanels();
        for (PanelOnRight.PanelMessage p : listPan){
            p.setBorder(BorderFactory.createMatteBorder(0,7,5,7, theme.getImgIconMsgeBorder()));
            p.setBackground(theme.getPanRightMsgColor());
            p.getCloseBtnToggle().getPanelInvis().setBackground(theme.getPanRightMsgColor());
        }

    }




    public static void main(String args[]){
        SwingDokuWindow windowTest = new SwingDokuWindow();

    }




}
