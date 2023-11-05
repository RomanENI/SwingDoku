import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SDLogicCenter {


    public boolean possibleFit(int xCord, int yCord, int value, int[][] board){
        //We are checking on the abstract board
        //Checking possible positioning of value board[j][i]
        //i : abcisse xCord
        //j : ordonnée yCord
        // donc board[yCord][xCord]
//        System.out.println("checking fit for number : "+ value +" abcisse : "+ (xCord+1) +" and ordonnée : "+ (yCord+1));
        return (checkColumn(xCord, value, board) && checkLine(yCord, value, board) && checkInQuadrant(xCord, yCord, value, board));
    }

    public boolean checkLine(int yCord, int value, int[][] board){
        //pour check la Line, c'est l'ordonnée yCord qui sera fixe et l'abcisse qui va être itérée.
        for (int xCord = 0; xCord < 9; xCord++) {
            if (value != 0 && board[yCord][xCord] == value){
//                System.out.println("Line error : value "+value+" already present at abcisse : "+(xCord+1)+ " ordonnée : "+(yCord+1));
                return false;
            }
        }
        return true;
    }

    public boolean checkColumn(int xCord, int value, int[][] board){
        //pour check la column, l'abcisse est fixe xCord et c'est l'ordonnée qui sera itérée.
        for (int yCord = 0; yCord < 9; yCord++) {
            if (value != 0 && board[yCord][xCord] == value){
//                System.out.println("Column error : value "+value+" already present at abcisse : "+(xCord+1)+ " ordonnée : "+(yCord+1));
                return false;
            }
        }
        return true;
    }




    public boolean checkInQuadrant(int xCord, int yCord, int value, int[][] board){
        if (value != 0) {
            int x0  = xCord - xCord % 3;
            int y0  = yCord - yCord % 3;
//            System.out.println("Checking Region where x0 = "+(x0+1)+", and y0 = "+(y0+1));

            for (int j = y0; j < y0 + 3; j++) {
                for (int i = x0; i < x0 + 3; i++) {
//                    System.out.println("xCord checking : "+ (i+1) +" yCord checking : "+ (j+1));
                    if (board[j][i] == value){
//                        System.out.println("Region error: value "+value+" already present in region at abcisse : "+(i+1)+" and ordonnée : "+(j+1));
                        return false;
                    }
                }
            }
        }
        return true;
    }


    public ArrayList<Object> checkLineError(int yCord, int value, int[][] board){

        ArrayList<Object> list = new ArrayList<>();
        for (int xCord = 0; xCord < 9; xCord++) {
            if (value != 0 && board[yCord][xCord] == value){
                String abcisse = String.valueOf(xCord+1);
                String ordonnee = String.valueOf(yCord+1);
                String message = "Erreur ligne<br> "+value+" est déjà sur cette ligne :<br>case "+abcisse+ "; "+ordonnee+"<br>";
                list.add(0, message);
                list.add(1, xCord);
                list.add(2, yCord);
            }
        }
        return list;
    }

    public ArrayList<Object> checkColumnError(int xCord, int value, int[][] board){
        ArrayList<Object> list = new ArrayList<>();
        for (int yCord = 0; yCord < 9; yCord++) {
            if (value != 0 && board[yCord][xCord] == value){
                String abcisse = String.valueOf(xCord+1);
                String ordonnee = String.valueOf(yCord+1);
                String message = "Erreur colonne<br> "+value+" est déjà sur cette colonne :<br>case "+abcisse+ "; "+ordonnee+"<br>";
                list.add(0, message);
                list.add(1, xCord);
                list.add(2, yCord);
            }
        }
        return list;
    }

    public ArrayList<Object> checkInQuadrantError(int xCord, int yCord, int value, int[][] board){
        ArrayList<Object> list = new ArrayList<>();
        if (value != 0) {
            int x0  = xCord - xCord % 3;
            int y0  = yCord - yCord % 3;
//            System.out.println("Checking Region where x0 = "+(x0+1)+", and y0 = "+(y0+1));

            for (int j = y0; j < y0 + 3; j++) {
                for (int i = x0; i < x0 + 3; i++) {
//                    System.out.println("xCord checking : "+ (i+1) +" yCord checking : "+ (j+1));
                    if (board[j][i] == value){
                        String abcisse = String.valueOf(i);
                        String ordonnee = String.valueOf(j);
                        String message = "Erreur région<br>"+value+" déjà dans la région :<br>case "+abcisse+ "; "+ordonnee+"<br>";
                        list.add(message);
                        list.add(i);
                        list.add(j);
                    }
                }
            }
        }
        return list;
    }

    public ArrayList<Object> checkPossibleFitError(int xCord, int yCord, int value, int[][] board){
        ArrayList<Object> list = new ArrayList<>();
        ArrayList<Object> listFromLine = new ArrayList<>();
        ArrayList<Object> listFromColumn = new ArrayList<>();
        ArrayList<Object> listFromQuadrant = new ArrayList<>();

        listFromLine = checkLineError(yCord, value, board);
        listFromColumn = checkColumnError(xCord, value, board);
        listFromQuadrant = checkInQuadrantError(xCord, yCord, value, board);

        String finalMessage ="";

        if(listFromLine != null && listFromLine.size() != 0){
            finalMessage += listFromLine.get(0);
            finalMessage += "\n";
            ArrayList<Integer> coords = new ArrayList<Integer>();
            coords.add((Integer) listFromLine.get(1));
            coords.add((Integer) listFromLine.get(2));
            list.add(coords);
        }

        if(listFromColumn != null && listFromColumn.size() != 0){
            finalMessage += listFromColumn.get(0);
            finalMessage += "\n";
            ArrayList<Integer> coords = new ArrayList<Integer>();
            coords.add((Integer) listFromColumn.get(1));
            coords.add((Integer) listFromColumn.get(2));
            list.add(coords);
        }


        if(listFromQuadrant!= null && listFromQuadrant.size() != 0){

            for (int i = 0; i < listFromQuadrant.size(); i++) {
                if (i % 3 == 0){
                    finalMessage += listFromQuadrant.get(i);
                    finalMessage += "\n";
                    ArrayList<Integer> coords = new ArrayList<Integer>();
                    coords.add((Integer) listFromQuadrant.get(i+1));
                    coords.add((Integer) listFromQuadrant.get(i+2));
                    list.add(coords);
                }
            }

        }

        list.add(finalMessage);
        return list;
    }


    public boolean checkValidSolution(int[][] grid){

        boolean validity = true;
        int counter = 0;
        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 9; i++) {
                if (!(validRegion(i, j, grid[j][i], grid) && validLine(i, j, grid[j][i], grid) && validColumn(i, j, grid[j][i], grid))){
                    validity = false;
                    counter++;
                    System.out.println("found false "+counter);
                }
            }
        }

        return validity;
    }

    private boolean validColumn(int coordX, int coordY, int value, int[][] board) {
        int[][] temp = copyOfAbstractBoard(board);
        temp[coordY][coordX] = 0;
        return checkColumn(coordX, value, temp);
    }

    private boolean validLine(int coordX, int coordY, int value, int[][] board) {
        int[][] temp = copyOfAbstractBoard(board);
        temp[coordY][coordX] = 0;
        return checkLine(coordY, value, temp);
    }

    private boolean validRegion(int coordX, int coordY, int value, int[][] board) {
        int[][] temp = copyOfAbstractBoard(board);
        temp[coordY][coordX] = 0;
        return checkInQuadrant(coordX, coordY, value, temp);
    }

    int[][] copyOfAbstractBoard(int[][] boardToCopy){
        int[][] board = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = boardToCopy[i][j];
            }
        }
        return board;
    }

    public boolean possibleGrid(int[][] grid){
        for (int j= 0; j< 9; j++) {
            for (int i = 0; i < 9; i++) {
                //On est sur une case
                //Véfification que la case est vide
                if (grid[j][i] == 0){
                    //On va  tester toutes les valeurs de 1 à 9 inclu
                    for (int k = 1; k < 10; k++) {
                        //Si le coup est légal et qu'il conduit à une solution valide de la grille, on retourne true
                        if (possibleFit(i,j,k,grid)){
                            //On met la valeur dans le board
                            grid[j][i] = k;
                            if (possibleGrid(grid)){
                                return true;
                            } else {
                                //sinon on vide la case et on recommence
                                grid[j][i] = 0;
                            }
                        }
                    }
                    //On arrive à ce point si on a testé toutes les valeurs sur une case et qu'aucune valeur n'a fonctionné
                    return false;
                }
            }
        }
        return true;
    }

    private boolean gridMultiplePossibilities(int[][] grid) {
        //make temporary copy of board and attempt to solve it
        int[][] temporaryCopyOfAbstractBoard = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                temporaryCopyOfAbstractBoard[i][j] = grid[i][j];
            }
        }
//        System.out.println("GridMultiplePossibilities");
        ArrayList<int[][]> solutionHolder = new ArrayList<>();
        twoOrMorePossibleGrids(temporaryCopyOfAbstractBoard,solutionHolder);
//        System.out.println((solutionHolder.size() == 2));
        return (solutionHolder.size() == 2);

    }






    public boolean possibleGridCounter(int[][] grid, int[] counter){
        for (int j = 0; j < 9; j++)
        {for (int i= 0; i< 9; i++) {
            //On est sur une case
            //Véfification que la case est vide
            if (grid[j][i] == 0){
                counter[0] += 1;
                //On va  tester toutes les valeurs de 1 à 9 inclu
                for (int k = 1; k < 10; k++) {
                    //Si le coup est légal et qu'il conduit à une solution valide de la grille, on retourne true
                    if (possibleFit(i,j,k,grid)){
                        //On met la valeur dans le board
                        grid[j][i] = k;
                        if (possibleGridCounter(grid, counter)){
                            return true;
                        } else {
                            //sinon on vide la case et on recommence
                            grid[j][i] = 0;
                        }
                    }
                }
                //On arrive à ce point si on a testé toutes les valeurs sur une case et qu'aucune valeur n'a fonctionné
                return false;
            }
        }
        }
        return true;
    }


    public boolean possibleGridCounterRandomK(int[][] grid, int[] counter){
        for (int j = 0; j < 9; j++)
        {for (int i= 0; i< 9; i++) {
            //On est sur une case
            //Véfification que la case est vide
            if (grid[j][i] == 0){
                counter[0] += 1;
                //On va  tester toutes les valeurs de 1 à 9 inclu
                int[] randomlist = randomSequence0to8();
                for (int k = 0; k < 9; k++) {
                    randomlist[k] += 1;
                }
                for (int k : randomlist) {
                    //Si le coup est légal et qu'il conduit à une solution valide de la grille, on retourne true
                    if (possibleFit(i,j,k,grid)){
                        //On met la valeur dans le board
                        grid[j][i] = k;
                        if (possibleGridCounter(grid, counter)){
                            return true;
                        } else {
                            //sinon on vide la case et on recommence
                            grid[j][i] = 0;
                        }
                    }
                }
                //On arrive à ce point si on a testé toutes les valeurs sur une case et qu'aucune valeur n'a fonctionné
                return false;
            }
        }
        }
        return true;
    }

    public boolean gridReducer(int[][] grid, int[] xAxis, int[] yAxis){
        for (int j : yAxis) {
            for (int i : xAxis) {

                //On est sur une case
                //Véfification que la case n'est pas vide
                if (grid[i][j] != 0 && nbNotEmptySquare(grid) > 40){
                    int temp = grid[i][j];
                    grid[i][j] = 0;

                    //Si vider la case conduit à une grille avec une unique solution, on retourne true;
                    if (gridReducer(grid, xAxis, yAxis)){
                        return true;
                    } else {
                        //sinon on restaure case et on recommence
                        grid[i][j] = temp;
                        return false;
                    }

                }

            }
        }
        return true;
    }


    public boolean gridReducer2(int[][] grid, int[] coords){
        for (int ind : coords) {

            //i : abcisse
            //j : ordonnee

            int i = (ind % 9) - 1;
            int j = (ind / 9);

            if(ind % 9 == 0){
                i = 8;
                j = (ind / 9) - 1;
            }
            //On est sur une case
            //Véfification que la case n'est pas vide
            if (grid[i][j] != 0 && nbNotEmptySquare(grid) > 35 ){
                int temp = grid[i][j];
                grid[i][j] = 0;
                //Si vider la case conduit à une grille avec une unique solution, on retourne true;
                if (gridReducer2(grid, coords) && !gridMultiplePossibilities(grid)){
                    return true;
                } else {
                    //sinon on restaure case et on recommence
                    grid[i][j] = temp;
                    return false;
                }
            }
        }
        return true;
    }



    public boolean reduceGridtoXClues(int[][] grid, int[] coords, int finalNumberOfClues){

        for (int ind : coords) {

            //i : abcisse
            //j : ordonnee

            int i = (ind % 9) - 1;
            int j = (ind / 9);

            if(ind % 9 == 0){
                i = 8;
                j = (ind / 9) - 1;
            }
            //On est sur une case
            //Véfification que la case n'est pas vide
            if (grid[j][i] != 0 && nbNotEmptySquare(grid) > finalNumberOfClues ){
                int temp = grid[j][i];
                grid[j][i] = 0;
                //Si vider la case conduit à une grille avec une unique solution, on retourne true;
                if (reduceGridtoXClues(grid, coords, finalNumberOfClues) && !gridMultiplePossibilities(grid)){
                    return true;
                } else {
                    //sinon on restaure case et on recommence
                    grid[j][i] = temp;
                    return false;
                }
            }
        }
        return true;
    }


    public int nbNotEmptySquare(int[][] abstractGrid){
        int count = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(abstractGrid[j][i] != 0){
                    count ++;
                }
            }
        }
        return count;
    }

    int[] randomSequence0to8(){
        int[] tab = new int[9];
        ArrayList<Integer> inter = new ArrayList<Integer>();
        for (int i = 0; i < 9; i++) {
            inter.add(i);
        }
        Random rand = new Random();
        for (int i = 0; i < 9; i++) {
            int ind = rand.nextInt(inter.size());
            tab[i] = inter.get(ind);
            inter.remove(ind);
        }
        return tab;
    }

    int[] randomList1to81(){
        int[] list = new int[81];

        ArrayList<Integer> ar = new ArrayList<>();
        for (int i = 1; i <= 81; i++) {
            ar.add(i);
        }
        Random rand = new Random();
        for (int i = 0; i < 81; i++) {
            int ind = rand.nextInt(ar.size());
            list[i] = ar.get(ind);
            ar.remove(ind);
        }
        return list;

    }

    public boolean possibleGridRandomly(int[][] grid, int[] xAxis, int[] yAxis, int[]counter){
        for (int j : yAxis) {
            for (int i : xAxis) {
                if (grid[j][i] == 0){
                    counter[0] += 1;
                    if (counter[0] > 140000){
                        return false;
                    }
                    System.out.println(counter[0]);
                    int[] randomlist = randomSequence0to8();
                    for (int k = 0; k < 9; k++) {
                        randomlist[k] += 1;
                    }
                    for (int k : randomlist){
                        if (possibleFit(i,j,k,grid)){
                            grid[j][i] = k;
                            if (possibleGridRandomly(grid, xAxis, yAxis, counter)){
                                return true;
                            } else {
                                grid[j][i] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        System.out.println("found a grid");
        return true;
    }

    public boolean possibleGridRandomly(int[][] grid, int[] coords, int[]counter){
        for (int ind : coords) {
            int j;
            int i;
            //i : abcisse
            //j : ordonnee
            if (ind % 9 == 0){
                j = (ind / 9) - 1;
                i = 9 - 1;
            }else{
                j = ind / 9;
                i = (ind % 9) - 1;
            }
            if (grid[i][j] == 0){
                counter[0] += 1;
//                    System.out.println(counter[0]);
                int[] randomlist = randomSequence0to8();
                for (int k = 0; k < 9; k++) {
                    randomlist[k] += 1;
                }
                for (int k : randomlist){
                    if (possibleFit(i,j,k,grid)){

                        grid[i][j] = k;
                        if (possibleGridRandomly(grid, coords, counter)){
                            return true;
                        } else {
                            grid[i][j] = 0;
                        }
                    }
                }
                return false;
            }
        }
        System.out.println("found a grid");
        return true;
    }


    public boolean possibleGridRandomly3(int[][] grid,int[] coords, int[] counter ){
        for (int ind : coords) {
            int i = (ind % 9) - 1;
            int j = (ind / 9);

            if(ind % 9 == 0){
                i = 8;
                j = (ind / 9) - 1;
            }
            if (grid[j][i] == 0){
                counter[0] += 1;
                System.out.println(counter[0]);
                int[] randomlist = randomSequence0to8();
                for (int k = 0; k < 9; k++) {
                    randomlist[k] += 1;
                }
                for (int k : randomlist){
                    if (possibleFit(i,j,k,grid)){
                        grid[j][i] = k;
                        if (possibleGridRandomly3(grid, coords, counter)){
                            return true;
                        } else {
                            grid[j][i] = 0;
                        }
                    }
                }
                return false;
            }
        }
        System.out.println("found a grid");
        return true;
    }

    public boolean putOneSolutionInArray(int[][] grid, ArrayList<int[][]> solutionList){
        for (int i = 0; i < 9; i++) {

            for (int j = 0; j < 9; j++) {

                if (grid[i][j] == 0 && solutionList.size()<1){
                    for (int k = 1; k < 10; k++) {
                        if (possibleFit(i,j,k,grid)){
                            grid[i][j] = k;
                            if (putOneSolutionInArray(grid, solutionList) && solutionList.size() < 1){
                                return true;
                            } else {
                                grid[i][j] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        //we are adding
        int[][] solutionBoard = new int[9][9];
        boolean adding = true;
        for (int p = 0; p < 9; p++) {
            for (int q = 0; q < 9; q++) {
                solutionBoard[p][q] = grid[p][q];
                if (grid[p][q] == 0){
                    adding = false;
                }
            }
        }
        if (adding){
            solutionList.add(solutionBoard);
//            System.out.println("ouch");
//            displayAbstractBoard(solutionBoard);
//            System.out.println("saloute");
        }

        return true;
    }


    public void testingGridHandlingOrder(int[][] grid){
        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 9; i++) {
                System.out.print("i : "+i+" j : "+j+"  "+grid[j][i]+"   ");
            }
            System.out.println();
        }
    }


    public boolean twoOrMorePossibleGrids(int[][] grid, ArrayList<int[][]> solutionList){
        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 9; i++) {
                //test bellow proved unsuccessful so far
//                System.out.println("i = "+i+" j = "+j+" "+grid[i][j]);

                if (grid[j][i] == 0 && solutionList.size() < 2){
                    for (int k = 1; k < 10; k++) {
                        if (possibleFit(i, j, k, grid)){
                            grid[j][i] = k;
                            if (twoOrMorePossibleGrids(grid, solutionList) && solutionList.size() < 2 && !checkSolutionInArray(grid, solutionList)){
                                return true;
                            } else {
                                grid[j][i] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        //we are adding
        int[][] solutionBoard = new int[9][9];
        boolean adding = true;
        for (int p = 0; p < 9; p++) {
            for (int q = 0; q < 9; q++) {
                solutionBoard[p][q] = grid[p][q];
                if (grid[p][q] == 0){
                    adding = false;
                }
            }
        }
        if (adding){
            solutionList.add(solutionBoard);
//            System.out.println("ouch");
//            displayAbstractBoard(solutionBoard);
//            System.out.println("saloute");
        }

        return true;
    }


    public boolean upToXSolutions(int[][] grid, ArrayList<int[][]> solutionList, int maxNumberOfSolutionsBeforeStopTest){
        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 9; i++) {

                if (grid[j][i] == 0 && solutionList.size() < maxNumberOfSolutionsBeforeStopTest){
                    for (int k = 1; k < 10; k++) {
                        if (possibleFit(i, j, k, grid)){
                            grid[j][i] = k;
                            if (upToXSolutions(grid, solutionList, maxNumberOfSolutionsBeforeStopTest) && solutionList.size() < maxNumberOfSolutionsBeforeStopTest && !checkSolutionInArray(grid, solutionList)){
                                return true;
                            } else {
                                grid[j][i] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        //we are adding
        int[][] solutionBoard = new int[9][9];
        boolean adding = true;
        for (int p = 0; p < 9; p++) {
            for (int q = 0; q < 9; q++) {
                solutionBoard[p][q] = grid[p][q];
                if (grid[p][q] == 0){
                    adding = false;
                }
            }
        }
        if (adding){
            solutionList.add(solutionBoard);
//            System.out.println("ouch");
//            displayAbstractBoard(solutionBoard);
//            System.out.println("saloute");
        }

        return true;
    }


    public boolean putSolutionsIn(int[][] grid, ArrayList<int[][]> solutionList){
        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 9; i++) {
                //On est sur une case
                //Véfification que la case est vide
                if (grid[j][i] == 0){
                    //On va  tester toutes les valeurs de 1 à 9 inclu
                    for (int k = 1; k < 10; k++) {
                        //Si le coup est légal et qu'il conduit à une solution valide de la grille, on retourne true
                        if (possibleFit(i, j, k, grid)){
                            //On met la valeur dans le board
                            grid[j][i] = k;
                            if (putSolutionsIn(grid, solutionList) && !checkSolutionInArray(grid, solutionList)){
                                return true;
                            } else {
                                //sinon on vide la case et on recommence
                                grid[j][i] = 0;
                            }
                        }
                    }
                    //On arrive à ce point si on a testé toutes les valeurs sur une case et qu'aucune valeur n'a fonctionné
                    //
                    return false;
                }
            }
        }
        //we are adding
        int[][] solutionBoard = new int[9][9];
        for (int p = 0; p < 9; p++) {
            for (int q = 0; q < 9; q++) {
                solutionBoard[p][q] = grid[p][q];
            }
        }
        solutionList.add(solutionBoard);
        displayAbstractBoard(solutionBoard);
        return true;
    }

    public void displayAbstractBoard(int[][] board){
        for(int i = 0; i<9;i++){
            for(int j = 0; j<9; j++){
                System.out.print(board[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }

    public boolean checkSolutionInArray(int[][] solution, ArrayList<int[][]> solutions){
        boolean result = false;
        for (int[][] solutionAlreadyInArray : solutions){
            for (int j = 0; j < 9; j++) {
                for (int i = 0; i < 9; i++) {
                    if (solutionAlreadyInArray[i][j] == solution[i][j]){
                        result = true;
                    }
                }
            }
        }
        return result;
    }


    public void humanSolver(ActionEvent event, int[][] board){
        humanSolver(board);
//        updateConcreteBoard();

    }

    public void humanSolver(int[][] grid){
        boolean foundNothing = false;
        ArrayList<HashMap<String, Object>> actionsToUpdate = new ArrayList<>();

        while (nbNotEmptySquare(grid) < 80 && !foundNothing){
            actionsToUpdate.clear();

            //
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (grid[j][i] == 0){
                        int[] candidatesUnicity = candidatesUnicityMethod(i, j, grid);

                        if (candidatesUnicity.length == 1){
//                            System.out.println("We have a candidate unicity");
                            HashMap<String, Object> action = new HashMap<>();

                            action.put("coordX", i);
                            action.put("coordY", j);
                            action.put("formerValue", 0);
                            action.put("newValue", candidatesUnicity[0]);
                            actionsToUpdate.add(action);
                        }
                    }
                }
            }
            if (actionsToUpdate.size() > 0){
                System.out.println(actionsToUpdate.size()+" cases résolues par unicité");
            }


            //if nothing with unicity, we try with exclusion
            if (actionsToUpdate.size() == 0){
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (grid[j][i] == 0) {
                            int[] candidatesUnicityExclusion = candidatesUnicityAndExclusions(i, j, grid);
                            if (candidatesUnicityExclusion.length == 1){
//                                System.out.println("We have a candidate exclusion");
                                HashMap<String, Object> action = new HashMap<>();

                                action.put("coordX", i);
                                action.put("coordY", j);
                                action.put("formerValue", 0);
                                action.put("newValue", candidatesUnicityExclusion[0]);
                                actionsToUpdate.add(action);
                            }
                        }
                    }
                }
                System.out.println(actionsToUpdate.size()+" cases résolues par exclusion");
                for (HashMap<String, Object> action : actionsToUpdate){
                    System.out.println((action.get("coordX"))+"  "+action.get("coordY")+"  vers : "+action.get("newValue"));
                }
            }

            if (actionsToUpdate.size() == 0){
                foundNothing = true;
            }

            if (actionsToUpdate.size() > 0){
                updateActions(actionsToUpdate, grid);
            }
        }
    }



    public void updateActions(ArrayList<HashMap<String, Object>> list, int[][] grid){
//        System.out.println("Changement de "+list.size()+" cases");
        for (HashMap<String, Object> action : list){
//            PanelNumber pan = getPanelFromCoords((int)action.get("coordX"), (int)action.get("coordY"));
//            int newVal = (int) action.get("newValue");
//            addActionToList(pan, newVal);
//            pan.changeNumber(newVal);
            int coordX = (int)action.get("coordX");
            int coordY = (int)action.get("coordY");
            int newVal = (int) action.get("newValue");
            grid[coordY][coordX] = newVal;

        }
    }


    public int[] candidatesUnicityMethod(int coordX, int coordY, int[][] grid){
        //recherche de la valeur de la case par le principe d'unicité le plus simple
        // à ne pas utiliser sur une case non vide
        ArrayList<Integer> candidates = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            candidates.add(i);
        }
        // removing candidates with line
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < candidates.size(); j++) {
                if (candidates.get(j) == grid[coordY][i]){
                    candidates.remove(j);
                }
            }
        }
        // removing candidates with column
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < candidates.size(); j++) {
                if (candidates.get(j) == grid[i][coordX]){
                    candidates.remove(j);
                }
            }
        }
        // removing candidates with region
        int x0 = coordX - (coordX % 3);
        int y0 = coordY - (coordY % 3);
//        System.out.println("x0 : "+x0+", y0 : "+y0);
        for (int i = x0; i < x0 + 3; i++) {
            for (int j = y0; j < y0 + 3; j++) {
                for (int k = 0; k < candidates.size(); k++) {
                    if (candidates.get(k) == grid[j][i]){
                        candidates.remove(k);
                    }
                }
            }
        }
        //returning array
        int[] remainingCandidates = candidates.stream()
                .mapToInt(Integer::intValue)
                .toArray();
        return remainingCandidates;
    }

    public int[] candidatesUnicityAndExclusions(int coordX, int coordY, int[][] grid){

//        System.out.println("coordX : "+coordX+"  , coordY : "+coordY);
        //multiple candidates but only 1 is valid regarding current grid context
        ArrayList<Integer> candidatesUnicite = new ArrayList<>();
        int[] candidatesAsArray = candidatesUnicityMethod(coordX, coordY, grid);
        for (int i = 0; i < candidatesAsArray.length; i++) {
            candidatesUnicite.add(candidatesAsArray[i]);
        }
        int x0 = coordX - (coordX % 3);
        int y0 = coordY - (coordY % 3);
//        System.out.println("x0 : "+x0+", y0 : "+y0);
        for (int j = y0; j < y0 + 3; j++) {
            for (int i = x0; i < x0 + 3; i++) {
//                System.out.println("We are at abcisse : "+i+" ,ordonnée : "+j+" et i != coordX est "+(i != coordX)+" et j != coordY est "+ (j != coordY));
                if ((i != coordX || j != coordY) && grid[j][i] == 0){
//                    System.out.println(i+",  "+j+" est vide et n'est pas la case testée"+" valeur de grid["+j+"]["+i+"] : "+grid[j][i]);
                    int[] candidates = candidatesUnicityMethod(i, j, grid);
                    if (candidates.length > 0) {
                        for (int l = 0; l < candidates.length; l++) {
                            for (int k = 0; k < candidatesUnicite.size(); k++) {
                                if (candidatesUnicite.get(k) == candidates[l]){
                                    candidatesUnicite.remove(k);
                                }
                            }
                        }
                    }
                }
            }
        }
//        System.out.println("Candidats par Exclusion : ");
//        for (int cand : candidatesUnicite){
//            System.out.print(cand+"  ");
//        }
//        System.out.println();

        if (candidatesUnicite.size() == 0){
            return new int[0];
        }

        //returning array
        int[] remainingCandidates = candidatesUnicite.stream()
                .mapToInt(Integer::intValue)
                .toArray();

        return remainingCandidates;
    }


    public int[] candidatesAfterEliminatingNakedPairs(int coordX, int coordY, int[][] grid, int[] inputCandidates){

        //setup arraylist
        ArrayList<Integer> candidates = new ArrayList<>();
        for (int i = 0; i < inputCandidates.length; i++) {
            candidates.add(inputCandidates[i]);
        }

        //naked pairs on same line
        for (int i = 0; i < 9; i++) {
            if (grid[coordY][i] == 0 && i != coordX){
                int[] candFirstCell = candidatesUnicityMethod(i, coordY, grid);
                if (candFirstCell.length == 2){

                    for (int j = 0; j < 9; j++) {
                        if ((j != i && j != coordX) && grid[coordY][j] == 0){
                            int[] candSecondCell = candidatesUnicityMethod(j, coordY, grid);
                            if (comparePairs(candFirstCell, candSecondCell)){
                                for (int l = 0; l < 2  ; l++) {
                                    for (int k = 0; k < candidates.size(); k++) {
                                        if (candFirstCell[l] == candidates.get(k)){
                                            candidates.remove(k);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }



        for (int j = 0; j < 9; j++) {
            if (grid[j][coordX] == 0 && j != coordY){
                int[] candFirstCell = candidatesUnicityMethod(j, coordX, grid);
                if (candFirstCell.length == 2){

                    for (int i = 0; i < 9; i++) {
                        if ((i != j && i != coordY) && grid[i][coordX] == 0){
                            int[] candSecondCell = candidatesUnicityMethod(i, coordX, grid);
                            if (comparePairs(candFirstCell, candSecondCell)){
                                for (int l = 0; l < 2  ; l++) {
                                    for (int k = 0; k < candidates.size(); k++) {
                                        if (candFirstCell[l] == candidates.get(k)){
                                            candidates.remove(k);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        int x0 = coordX - (coordX % 3);
        int y0 = coordY - (coordY % 3);

        for (int j = y0; j < y0+3; j++) {
            for (int i = x0; i < x0+3; i++) {
                if (grid[j][i] == 0 && (j != coordY || i != coordX)){
                    int[] candFirstCell = candidatesUnicityMethod(i, j, grid);
                    if (candFirstCell.length == 2){
                        for (int y = y0; y < y0+3; y++) {
                            for (int x = x0; x < x0+3; x++) {
                                if (((y != coordY || x != coordX) && (y != j || x != i)) && grid[coordY][coordX] == 0){
                                    updateCandidates(grid, candidates, candFirstCell, y, x);
                                }
                            }
                        }
                    }
                }
            }



        }





        return candidates.stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }

    private void updateCandidates(int[][] grid, ArrayList<Integer> candidates, int[] candFirstCell, int y, int x) {
        int[] candSecondCell = candidatesUnicityMethod(x, y, grid);
        if (comparePairs(candFirstCell, candSecondCell)){
            for (int l = 0; l < 2  ; l++) {
                for (int k = 0; k < candidates.size(); k++) {
                    if (candFirstCell[l] == candidates.get(k)){
                        candidates.remove(k);
                    }
                }
            }
        }
    }


    public boolean comparePairs(int[] firstBoard, int[] secondBoard){
        if ((firstBoard.length != 2 || secondBoard.length !=2) ){
            return false;
        }
        boolean equals = true;
        for (int i = 0; i < 2; i++) {
            if (firstBoard[i] != secondBoard[0] && firstBoard[i] != secondBoard[1]){
                equals = false;
            }
        }
        return equals;
    }



    public int[] candidatesAfterPointingPairs(int coordX, int coordY, int[][] grid, int[] inputCandidates){
        ArrayList<Integer> candidates = new ArrayList<>();
        for (int i = 0; i < inputCandidates.length; i++) {
            candidates.add(inputCandidates[i]);
        }





        return candidates.stream()
                .mapToInt(Integer::intValue)
                .toArray();

    }






    public boolean didWeWin(int[][] abstractBoard, int lastEnteredValue) {
        SDLogicCenter logic = new SDLogicCenter();
        boolean bool = true;
        int[][] temporaryCopy = new int[9][9];
        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 9; i++) {
                temporaryCopy[j][i] = abstractBoard[j][i];
                if (abstractBoard[j][i]==0){
                    temporaryCopy[j][i] = lastEnteredValue;
                }
            }
        }



        displayAbstractBoard(temporaryCopy);
        bool = logic.checkValidSolution(temporaryCopy);


        return bool;
    }

    public void generateGridEasy(int[][] board) {

        //generate valid grid randomly
        int[] yAxis = new int[9];
        yAxis = randomSequence0to8();
        int[] xAxis = new int[9];
        xAxis = randomSequence0to8();
        int[] counter = new int[1];

        boolean bool = false;

        do {
            counter[0] = 0;
            bool = possibleGridRandomly(board, xAxis, yAxis, counter);

        }while (!bool);

        shuffleGrid(board);

//        possibleGridCounterRandomK(board, counter);


    }

    public void shuffleGrid(int[][] grid){
        shuflleGridTriColumn(grid);
        shuffleGridTriLine(grid);

    }

    private void shuffleGridTriLine(int[][] grid) {
        Random rand = new Random();
        for (int i = 0; i < 30; i++) {
            int p = rand.nextInt(2)+1;
            int q = p;
//            System.out.println("p : "+p);
            do {
                q = rand.nextInt(p+1)+1;
//                System.out.println("ha");
            }while (p == q);

//            System.out.println("q : "+q);
            swapTriLine(p, q, grid);
            swapTriColumn(p, q, grid);
        }


    }

    private void shuflleGridTriColumn(int[][] grid) {
        Random rand = new Random();
        for (int i = 0; i < 30; i++) {
            int p = rand.nextInt(2)+1;
            int q = p;
//            System.out.println("p : "+p);
            do {
                q = rand.nextInt(p+1)+1;
//                System.out.println("ha");
            }while (p == q);

//            System.out.println("q : "+q);

        }




    }

    private void swapTriColumn(int p, int q, int[][] grid) {
        int min = min(p, q);
        int max = max(p, q);

        int[] inter = new int[3];
        min = (min*3)-3;
        max = (max*3)-3;

        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 3; i++) {
                inter[i] = grid[j][min + i];
                grid[j][min + i] = grid[j][max + i];
                grid[j][max + i] = inter[i];
            }
        }

        shuffleTriColumn(1, grid);


    }

    private void swapTriLine(int p, int q, int[][] grid) {
        int min = min(p, q);
        int max = max(p, q);

        int[] inter = new int[3];
        min = (min*3)-3;
        max = (max*3)-3;

//        displayAbstractBoard(grid);
        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 3; i++) {
                inter[i] = grid[min + i][j];
                grid[min + i][j] = grid[max + i][j];
                grid[max + i][j] = inter[i];
            }
        }
//        displayAbstractBoard(grid);
    }

    private void shuffleTriColumn(int p, int[][] grid){
        int start = (p*3)-3;

        //selecting the two column that will be switched
        Random rand = new Random();
        int colOne = rand.nextInt(3);
        int colTwo = rand.nextInt(3);

//        System.out.println("Tree "+p+" shuffle col "+colOne+" and "+colTwo);
//        displayAbstractBoard(grid);
        if (colOne != colTwo){

            for (int j = 0; j < 9; j++) {
                int inter = grid[j][start+colOne];
                grid[j][start+colOne] = grid[j][start+colTwo];
                grid[j][start+colTwo] = inter;

            }
        }
//        displayAbstractBoard(grid);



    }


    private int min(int a, int b){
        int min = a;
        if (a < b){
            min = a;
        }else {
            min = b;
        }
        return min;
    }

    private int max(int a, int b){
        int max = a;
        if (a > b){
            max = a;
        }else {
            max = b;
        }
        return max;
    }


}
