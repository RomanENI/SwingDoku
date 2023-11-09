import javax.swing.*;
import java.util.ArrayList;

public class Game {

    private ArrayList<int[][]> gridsolutions;
    private int[][] startingPosition;

    private boolean isExtremelyImpure;
    private final int extremelyImpureThreshold = 850;


    private JPanel panelWin;


    public Game(int[][] board){

        SDLogicCenter logic = new SDLogicCenter();
        this.gridsolutions = new ArrayList<>();


        //here we MUST manually copy board
        this.startingPosition = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.startingPosition[j][i] = board[j][i];
            }
        }

        logic.upToXSolutions(board, gridsolutions, extremelyImpureThreshold);
        if (this.gridsolutions.size() == extremelyImpureThreshold){
            this.isExtremelyImpure = true;
        }






    }



    public JPanel getPanelWin() {
        return panelWin;
    }

    public void setPanelWin(JPanel panelWin) {
        this.panelWin = panelWin;
    }
    public ArrayList<int[][]> getGridsolutions() {
        return gridsolutions;
    }

    public void setGridsolutions(ArrayList<int[][]> gridsolutions) {
        this.gridsolutions = gridsolutions;
    }

    public int[][] getStartingPosition() {
        return startingPosition;
    }

    public void setStartingPosition(int[][] startingPosition) {
        this.startingPosition = startingPosition;
    }

}
