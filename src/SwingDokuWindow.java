import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class SwingDokuWindow extends JFrame {


    private static SwingDokuWindow SwingDokuInstance;
    SwingDokuWindow zis = this;
    public Game currentGame;

    SDMenuBar sdMenuBar;
    public int[][] abstractBoard;
    public MainPanel mainPanel;

    private String currentTheme = "Comte Dooku";

    public int[][] abstractPlayBoard;

    public int[][] abstractBuildBoard;


    public int[][] getAbstractBoard(){
        return abstractBoard;
    }


    private SwingDokuWindow(){

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

        ArrayList icons = loadIcons();
        this.setIconImages(icons);


        this.setSize(mainWindowDim);
        this.setLocationRelativeTo(null);
        this.loadTheme(currentTheme);
        this.pack();

    } // fin du constructeur SwingDokuWindow

    private ArrayList loadIcons() {
        String[] keys = new String[] {"1616", "2424", "3232", "6464"};

        ArrayList<Image> list = new ArrayList<>();
        for(String key : keys){
            BufferedImage img;
            String fullPath = "/windowIcons/lauchIcon" + key + ".png";
            try {
                img = ImageIO.read(getClass().getResourceAsStream(fullPath));
                Image image = img;
                list.add(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        return list;

    }

    private void populateThemeMenuBarItem(){
        this.sdMenuBar.getMenuItemBoring().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                currentTheme = "boring";
                loadTheme(currentTheme);
            }
        });
        this.sdMenuBar.getMenuItemAncientRome().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTheme = "rome antique";
                loadTheme(currentTheme);
            }
        });


        this.sdMenuBar.getMenuItemUnicorn1().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTheme = "Licorne 1";
                loadTheme(currentTheme);
            }
        });

         this.sdMenuBar.getMenuItemUnicorn2().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTheme = "Licorne 2";
                loadTheme(currentTheme);
            }
        });
        this.sdMenuBar.getMenuItemDooku().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTheme = "Comte Dooku";
                loadTheme(currentTheme);
            }
        });

        this.sdMenuBar.getMenuItemUnicorn3().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTheme = "Licorne 3";
                loadTheme(currentTheme);
            }
        });

        this.sdMenuBar.getMenuItemPokemonStarter().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTheme = "Pokemon Starter";
                loadTheme(currentTheme);
            }
        });
        this.sdMenuBar.getMenuItemLeaves().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTheme = "Feuilles";
                loadTheme(currentTheme);
            }
        });
        this.sdMenuBar.getMenuItemFruitsAndVegetables().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTheme = "Fruits et Legumes";
                loadTheme(currentTheme);
            }
        });

    }

    public SDMenuBar getSdMenuBar() {
        return sdMenuBar;
    }


    public MainPanel getMainPanel() {
        return mainPanel;
    }


    public class MainPanel extends JPanel{


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


        JPanel panelBottom = new JPanel();

        private PanelGrid panelWithGrid;

        private final int coteCarreLength = 648;

        Dimension dimPanelWithGrid = new Dimension(coteCarreLength, coteCarreLength);

        Dimension dimBottomPanel = new Dimension(coteCarreLength, 0);

        private JButton submitButton;

        private JButton playConstructedGridButton;


        MainPanel(){
            setupSubmitButton();
            setupPlayConstructedGridButton();
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
            Dimension dimLeftPanel = new Dimension(370, 0);
            panelLeft.setPreferredSize(dimLeftPanel);

            buildLayout(panelLeft, panelCenter, panelRight);
        }

        private void lockGridAndPlay(){
            getPanelWithGrid().getLockedPanels().clear();

            getPanelWithGrid().lockNonEmptyPanels();
            setGridCoherenceStatus(false);
            goPlay();
        }

        private void setupPlayConstructedGridButton() {
            this.playConstructedGridButton = new JButton();

            playConstructedGridButton.setText("Jouer cette grille");
            playConstructedGridButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getPanelWithGrid().getLockedPanels().clear();


                    if (checkGridPurity()){
                        //we go to playMode

                        procedeToPlay();
                        for (PanelNumber pan : getPanelWithGrid().getPanels()){
                            pan.setLocked(false);
                        }
                        updateConcreteBoard();
                        getPanelWithGrid().lockNonEmptyPanels();

                        //we store solution(s) somewhere
                        currentGame = new Game(abstractBoard);

                    }
                }
            });

        }

        private void procedeToPlay() {
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
        }

        public JPanel createPanelWithGrid() {
            panelWithGrid = new PanelGrid(getSdMenuBar(), getAbstractBoard(), getRightPanel(), this.submitButton, this.playConstructedGridButton);
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
                        initiateWinSequence();
                    }else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1500);
                                    JOptionPane.showMessageDialog(getMainPanel(),
                                            "<html>Ahhhh la la la la la la <br>" +
                                                    "Terriiiiiiiiiiiiiiiiiiiible....<br>" +
                                                    "C'est pas bon :-(</html>");

                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }

                            }

                        }).start();

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
                    newLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(newLayout.createSequentialGroup()
                                    .addGroup(newLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(leftPan)
                                            .addComponent(panelCenter)
                                            .addComponent(panelRight, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGap(0, 1, Short.MAX_VALUE))
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

            JButton customGridButton = new JButton("Faire une nouvelle grille");
            customGridButton.addActionListener(this::goToGridMakingMod);
            panelBottom.add(customGridButton);
            panelBottom.add(this.submitButton);

        }






        public void updateConcreteBoard(){
            for(int j =0;j<9;j++){
                for(int i=0;i<9;i++){
                    int value = abstractBoard[j][i];
                    PanelNumber panel = this.panelWithGrid.getPanelFromCoords(i, j);
                    panel.changeNumber(value);
                    panel.setValue(value);
                  }
            }
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
            //disable undo menu
            sdMenuBar.disableUndo();
            sdMenuBar.disableRedo();

            clearOptions();
            getBottomPanel().setLayout(new FlowLayout());

            panelBottom.add(playConstructedGridButton);
            JButton cancelButton = new JButton("Annuler");
            cancelButton.addActionListener(this::cancelGridmaking);
            panelBottom.add(cancelButton);

            JButton gridSuggest = new JButton("Faire une Grille automatique");
            gridSuggest.addActionListener(this::goAutoGridMakingModAction);
            panelBottom.add(gridSuggest);

            JButton randomNumberPlacer = new JButton("placer un nombre au hasard");
            randomNumberPlacer.addActionListener(this::placeOneRandomNumber);
            panelBottom.add(randomNumberPlacer);

            JButton randomRemove = new JButton("Vider une case au hasard");
            randomRemove.addActionListener(this::removeRandomNumber);
            panelBottom.add(randomRemove);

            JButton buttonEasy = new JButton("Donner une grille complète aléatoire");
            buttonEasy.addActionListener(this::makeEasyGrid);
            panelBottom.add(buttonEasy);

            JButton clearButton = new JButton("Repartir de Zéro");
            clearButton.addActionListener(this::restartBuildAction);
            panelBottom.add(clearButton);


            JButton buttonComputerSolver = new JButton("Résoudre la grille");
            buttonComputerSolver.addActionListener(this::computerSolver);
            panelBottom.add(buttonComputerSolver);

            panelBottom.revalidate();
            panelBottom.repaint();
        }

        private void restartBuildAction(ActionEvent actionEvent) {
            restartBuild();
        }

        private void restartBuild() {
            initAbstractBoard(abstractBoard, 0);
            updateConcreteBoard();
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
            JPanel playOnRightPan = new JPanel();
            playOnRightPan.setOpaque(false);
            upperPan.setLayout(new BoxLayout(upperPan, BoxLayout.Y_AXIS));
            centerPan.setLayout(new BoxLayout(centerPan, BoxLayout.Y_AXIS));
            playOnRightPan.setLayout(new BoxLayout(playOnRightPan, BoxLayout.Y_AXIS));

            panelBottom.add(upperPan, BorderLayout.LINE_START);
            panelBottom.add(centerPan, BorderLayout.CENTER);
            panelBottom.add(playOnRightPan, BorderLayout.LINE_END);

            playOnRightPan.add(playConstructedGridButton);

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

            JButton buttonDiabolicGrid = new JButton("Diabolique");
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
            load17Grid();
        }

        private void load17Grid() {
            String[] anArray = FileManager.giveArrayFromFile();

            Random rand = new Random();
            String chosenString = anArray[rand.nextInt(anArray.length)];
            //sout
            int[][] grid = makeGridFromExtractedString(chosenString);
            SDLogicCenter logic = new SDLogicCenter();
            logic.shuffleGrid(grid);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    abstractBoard[j][i] = grid[j][i];
                }
            }
            updateConcreteBoard();
        }


        private void giveHardGrid(ActionEvent actionEvent){
            makeGridFromFile(23);
            playConstructedGridButton.setEnabled(true);
        }
        private void giveVeryHardGrid(ActionEvent actionEvent){
            makeGridFromFile(20);
            playConstructedGridButton.setEnabled(true);
        }
        private void giveDiabolicalGrid(ActionEvent actionEvent){
            makeGridFromFile(18);
            playConstructedGridButton.setEnabled(true);
        }
        private void giveXtremeGrid(ActionEvent actionEvent){
            load17GridAction(actionEvent);
            playConstructedGridButton.setEnabled(true);
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

            return newGrid;
        }


        private void computerSolver(ActionEvent event) {
            solveGridAsComputer(abstractBoard);
            playConstructedGridButton.setEnabled(false);
        }

        private void solveGridAsComputer(int[][] abstractBoard) {
            SDLogicCenter logic = new SDLogicCenter();
            logic.possibleGrid(abstractBoard);
            updateConcreteBoard();
        }

        private void makeEasyGrid(ActionEvent event) {
            giveEasyGrid();
            playConstructedGridButton.setEnabled(false);
        }

        private void giveEasyGrid(){
            SDLogicCenter logic = new SDLogicCenter();
            int[][] madeGrid = new int[9][9];
            logic.generateGridEasy(madeGrid);
            logic.shuffleGrid(madeGrid);
            int[] coords = logic.randomList1to81();

            abstractBoard = logic.copyOfAbstractBoard(madeGrid);
            updateConcreteBoard();
        }

        private void createGridVeryEasyAction(ActionEvent e){
            createGridMoreThan24Clues(36);
            playConstructedGridButton.setEnabled(true);
        }
        private void createGridMoreThan24Clues(int finalClueNumber){
            SDLogicCenter logic = new SDLogicCenter();
            int[][] madeGrid = new int[9][9];
            logic.generateGridEasy(madeGrid);
            reduceSome(madeGrid, finalClueNumber);
            logic.shuffleGrid(madeGrid);

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    abstractBoard[i][j] = madeGrid[i][j];
                }
            }
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
            playConstructedGridButton.setEnabled(true);
        }

        private void createGridNormalAction(ActionEvent e){
            createGridMoreThan24Clues(26);
            playConstructedGridButton.setEnabled(true);
        }


        private boolean checkGridPurity(){

            SDLogicCenter logic = new SDLogicCenter();
            boolean movingOn = true;

            //if grid is impure we warn user
            ArrayList<int[][]> list = new ArrayList<>();
            boolean check = false;
            logic.twoOrMorePossibleGrids(abstractBuildBoard, list);
            check = list.size() == 2;

            if (check) {
                //Custom button text
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


        private void goPlay() {
            this.getPanelWithGrid().getLockedPanels().clear();
            SDLogicCenter logic = new SDLogicCenter();

            if (checkGridPurity()){

                //we go to playMode

                procedeToPlay();
                for (PanelNumber pan : this.getPanelWithGrid().getPanels()){
                    pan.setLocked(false);
                }
                updateConcreteBoard();
                this.getPanelWithGrid().lockNonEmptyPanels();

                //we store solution(s) somewhere
                currentGame = new Game(abstractBoard);

            }
        }

        private void resetActions() {
            this.getPanelWithGrid().getGridActionsHandler().clearActions();
        }


        private void clearOptions( ){
            Component[] components = panelBottom.getComponents();
            for (Component comp : components){
                if (comp instanceof JButton){
                    panelBottom.remove(comp);

                }
                if(comp instanceof JPanel && ((JPanel) comp).getLayout() instanceof BoxLayout){
                    panelBottom.remove(comp);
                }
            }
            panelBottom.setLayout(new FlowLayout());
            panelBottom.revalidate();
            panelBottom.repaint();

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
                this.panelWithGrid.getPanelFromCoords(coordX, coordY).changeNumber(board, value);
            }else {
                placeOneRandomNumber(board);
            }
        }


        private void removeRandomNumber(ActionEvent event){
            Random rand = new Random();
            PanelNumber pan = this.panelWithGrid.getPanelFromCoords(rand.nextInt(9), rand.nextInt(9));
            if(pan.value != 0){
                this.panelWithGrid.playActionHandler.addActionToList( pan, pan.getValue(), 0);
                pan.changeNumber(0);

            }else {
                removeRandomNumber(event);
            }
        }



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

        public int getCoteCarreLength() {
            return coteCarreLength;
        }

    }//--MainPanel------

    private void initiateWinSequence() {
        SwingDokuWindow currentWindow = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                Theme theme = new Theme(currentTheme);

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




                try {
                    Thread.sleep(3000);
                    ActionListener restartFunction = new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JPanel pan = mainPanel.createPanelWithGrid();
                            mainPanel.replacePanelGrid(pan);
                            loadTheme(currentTheme);
                            for (int i = 0; i < 9; i++) {
                                for (int j = 0; j < 9; j++) {
                                    abstractBoard[j][i] = currentGame.getStartingPosition()[j][i];
                                }
                            }
                            SDLogicCenter logic = new SDLogicCenter();
                            mainPanel.updateConcreteBoard();
                            getMainPanel().getPanelWithGrid().lockNonEmptyPanels();
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
                            for(Object object : getMainPanel().getBottomPanel().getComponents()){
                                if(object instanceof JButton ){
                                    if(((JButton) object).getText().equals("Annuler")){
                                        getMainPanel().getBottomPanel().remove((JButton)object);
                                    }
                                }
                            }
                            getMainPanel().getRightPanel().goModeBuild();
                        }
                    };
                    ModalRestartOrReplayDialog modal = new ModalRestartOrReplayDialog(restartFunction, newGameFunction);
                    modal.createAndShowGUI(zis);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }





            }

        }).start();



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
        mainPanel = new MainPanel();
        initGridPanel() ;
        mainPanel.setBackground(Color.red);
        mainPanel.getCenterPanel().setBackground(Color.red);

        this.add(mainPanel);

    }

    private void initGridPanel() {
        //initialize abstractBoard
        if (abstractPlayBoard == null){
            abstractPlayBoard = new int[9][9];
        }
        abstractBoard = abstractBuildBoard;
        getMainPanel().createGridMoreThan24Clues(26);


        mainPanel.lockGridAndPlay();
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


    public static SwingDokuWindow giveInstance(){
        if(SwingDokuInstance == null){
            SwingDokuInstance = new SwingDokuWindow();
        }
        return SwingDokuInstance;
    }




}
