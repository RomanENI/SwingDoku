import java.util.ArrayList;

public class GridSolver {




    private int[][] gridToSolve;

    public int[][] getGridToSolve() {
        return gridToSolve;
    }

    public ArrayList<TileSolveInfoHolder> getUnsolvedTiles() {
        return unsolvedTiles;
    }

    public ArrayList<int[][]> getSuccessivePositions() {
        return successivePositions;
    }

    private ArrayList<TileSolveInfoHolder> unsolvedTiles;

    private ArrayList<int[][]> successivePositions;


    public GridSolver(int[][] grid){
        successivePositions = new ArrayList<>();
        unsolvedTiles = new ArrayList<>();
        this.gridToSolve = grid;
        copyPositionToSuccessivePositions();




        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 9; i++) {
                if(gridToSolve[j][i] == 0){
                    TileSolveInfoHolder holder = new TileSolveInfoHolder(i, j);
                    unsolvedTiles.add(holder);
                }
            }
        }








    }


    private void copyPositionToSuccessivePositions(){
        SDLogicCenter logic = new SDLogicCenter();
        int[][] copy = logic.copyOfAbstractBoard(gridToSolve);
        successivePositions.add(copy);
    }


    public void simpleUnicityUpdate(){
        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 9; i++) {
                if(gridToSolve[j][i] != 0){
//                    System.out.println("aGrid = " + aGrid[j][i]+"    avec j = " + j + " , et i = " + i);
                    for(TileSolveInfoHolder handler : unsolvedTiles){
                        ArrayList<Integer> toRemove = new ArrayList<>();
                        if(handler.getxCoord() == i && handler.getCandidates().contains(gridToSolve[j][i])){
                            if(!toRemove.contains(gridToSolve[j][i])){
                                toRemove.add(gridToSolve[j][i]);
                            }

                        }
                        if (handler.getyCoord() == j && handler.getCandidates().contains(gridToSolve[j][i])){
//                            System.out.println("grid ji : " + aGrid[j][i]);
//                            System.out.println("j : " + j);
                            if(!toRemove.contains(gridToSolve[j][i])){
                                toRemove.add(gridToSolve[j][i]);
                            }

                        }
                        for(int index : toRemove){
                            handler.removeCandidate(index);
                        }
                    }
                }
            }
        }
    }


    public void showStateOfCandidates(){
        for (TileSolveInfoHolder holder : unsolvedTiles){
            System.out.println("x : "+holder.getxCoord()+", y : "+holder.getyCoord());
            holder.showCandidates();
        }
    }


    public void selectInfoHolderWithonly1Candidate(){
        ArrayList<TileSolveInfoHolder> solvedHolders = new ArrayList<>();
        for(TileSolveInfoHolder holder : unsolvedTiles){
            if (holder.getCandidates().size() == 1){
                solvedHolders.add(holder);
            }
        }
        if (solvedHolders.size() > 0){
            for(TileSolveInfoHolder holder : solvedHolders){
                System.out.println("holder : "+holder.getxCoord()+ " " + holder.getyCoord()+", deduced value : " );
                holder.showCandidates();
            }
        }else{
            System.out.println("inconclusive");
        }

    }


}
