import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SDLogicCenter {


    public boolean possibleFit(int xCord, int yCord, int value, int[][] board){

        return (checkColumn(xCord, value, board) && checkLine(yCord, value, board) && checkInQuadrant(xCord, yCord, value, board));
    }

    public boolean checkLine(int yCord, int value, int[][] board){
        //pour check la Line, c'est l'ordonnée yCord qui sera fixe et l'abcisse qui va être itérée.
        for (int xCord = 0; xCord < 9; xCord++) {
            if (value != 0 && board[yCord][xCord] == value){
                return false;
            }
        }
        return true;
    }

    public boolean checkColumn(int xCord, int value, int[][] board){
        //pour check la column, l'abcisse est fixe xCord et c'est l'ordonnée qui sera itérée.
        for (int yCord = 0; yCord < 9; yCord++) {
            if (value != 0 && board[yCord][xCord] == value){
                return false;
            }
        }
        return true;
    }



    public boolean checkInQuadrant(int xCord, int yCord, int value, int[][] board){
        if (value != 0) {
            int x0  = xCord - xCord % 3;
            int y0  = yCord - yCord % 3;

            for (int j = y0; j < y0 + 3; j++) {
                for (int i = x0; i < x0 + 3; i++) {
                    if (board[j][i] == value){
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

            for (int j = y0; j < y0 + 3; j++) {
                for (int i = x0; i < x0 + 3; i++) {
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

        ArrayList<int[][]> solutionHolder = new ArrayList<>();
        twoOrMorePossibleGrids(temporaryCopyOfAbstractBoard,solutionHolder);

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

        return true;
    }


    public boolean twoOrMorePossibleGrids(int[][] grid, ArrayList<int[][]> solutionList){
        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 9; i++) {

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

        }

        return true;
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


            //if nothing with unicity, we try with exclusion
            if (actionsToUpdate.size() == 0){
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (grid[j][i] == 0) {
                            int[] candidatesUnicityExclusion = candidatesUnicityAndExclusions(i, j, grid);
                            if (candidatesUnicityExclusion.length == 1){
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
                for (HashMap<String, Object> action : actionsToUpdate){
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

        for (HashMap<String, Object> action : list){

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

        //multiple candidates but only 1 is valid regarding current grid context
        ArrayList<Integer> candidatesUnicite = new ArrayList<>();
        int[] candidatesAsArray = candidatesUnicityMethod(coordX, coordY, grid);
        for (int i = 0; i < candidatesAsArray.length; i++) {
            candidatesUnicite.add(candidatesAsArray[i]);
        }
        int x0 = coordX - (coordX % 3);
        int y0 = coordY - (coordY % 3);
        for (int j = y0; j < y0 + 3; j++) {
            for (int i = x0; i < x0 + 3; i++) {
                if ((i != coordX || j != coordY) && grid[j][i] == 0){
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

        if (candidatesUnicite.size() == 0){
            return new int[0];
        }

        //returning array
        int[] remainingCandidates = candidatesUnicite.stream()
                .mapToInt(Integer::intValue)
                .toArray();

        return remainingCandidates;
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



    }

    public void shuffleGrid(int[][] grid){
        for (int i = 0; i < 3; i++) {
            shuffleTriColumn(i, grid);
            shuffleTriLine(i, grid);
        }
        shuffleGridTriLine(grid);
        shuflleGridTriColumn(grid);
        gridPermutation(grid);
    }

    //shuffle stacks of 3 lines
    public void shuffleGridTriLine(int[][] grid) {
        Random rand = new Random();
        int indexFirstTriLine;
        int indexSecondTriline;
        for (int i = 0; i < 3; i++) {
            indexFirstTriLine = i;
            indexSecondTriline = rand.nextInt(3-i)+i;
            swapTriLine(indexFirstTriLine, indexSecondTriline, grid);
        }

    }

    //shuffles stacks of 3 columns
    public void shuflleGridTriColumn(int[][] grid) {
        Random rand = new Random();
        int indexFirstTriColumn;
        int indexSecondTriColumn;
        for (int i = 0; i < 3; i++) {
            indexFirstTriColumn = i;
            indexSecondTriColumn = rand.nextInt(3-i)+i;
            swapTriColumn(indexFirstTriColumn, indexSecondTriColumn, grid);

        }

    }

    //exchange stack of 3 columns p with stack of 3 columns q
    public void swapTriColumn(int p, int q, int[][] grid) {
        int min = min(p, q);
        int max = max(p, q);

        int[] inter = new int[3];
        min = (min*3);
        max = (max*3);

        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 3; i++) {
                inter[i] = grid[j][min + i];
                grid[j][min + i] = grid[j][max + i];
                grid[j][max + i] = inter[i];
            }
        }
    }

    //exchange stack of 3 lines  p with stack of 3 lines q
    public void swapTriLine(int p, int q, int[][] grid) {
        int min = min(p, q);
        int max = max(p, q);

        int[] inter = new int[3];
        min = (min*3);
        max = (max*3);

        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 3; i++) {
                inter[i] = grid[min + i][j];
                grid[min + i][j] = grid[max + i][j];
                grid[max + i][j] = inter[i];
            }
        }
    }

    //shuffles the columns in the stack of 3 columns p.
    public void shuffleTriColumn(int p, int[][] grid){
        int start = (p*3);
        Random rand = new Random();
        int colOne;
        int colTwo;
        for (int i = 0; i < 3; i++) {
            colOne = i;
            colTwo = rand.nextInt(3-i)+i;
            if (colOne != colTwo){
                for (int j = 0; j < 9; j++) {
                    int inter = grid[j][start+colOne];
                    grid[j][start+colOne] = grid[j][start+colTwo];
                    grid[j][start+colTwo] = inter;
                }
            }
        }
    }

    //shuffles the lines in the stack of 3 lines p.
    public void shuffleTriLine(int p, int[][] grid){
        int start = (p*3);
        Random rand = new Random();
        int lineOne;
        int lineTwo;
        for (int i = 0; i < 3; i++) {
            lineOne = i;
            lineTwo = rand.nextInt(3-i)+i;
            if (lineOne != lineTwo){
                for (int j = 0; j < 9; j++) {
                    int inter = grid[start+lineOne][j];
                    grid[start+lineOne][j] = grid[start+lineTwo][j];
                    grid[start+lineTwo][j] = inter;
                }
            }
        }
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

    public void gridPermutation(int[][] grid){
        Random rand = new Random();
        int[] firstArray = new int[9];
        for (int i = 0; i < 9; i++) {
            firstArray[i] = i+1;
        }
        int[] secondArray = randomSequence0to8();
        for (int i = 0; i < secondArray.length; i++) {
            secondArray[i] += 1;
        }

        HashMap<Integer, Integer> permutationMapper = new HashMap<Integer, Integer>();
        for (int i = 0; i < 9; i++) {
            permutationMapper.put(firstArray[i], secondArray[i]);
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(grid[j][i] != 0){
                    grid[j][i] = permutationMapper.get(grid[j][i]);
                }
            }
        }



    }



}
