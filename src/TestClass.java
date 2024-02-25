import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class TestClass {


    public static void main(String args[]) {
        SDLogicCenter logic = new SDLogicCenter();
        TileSolveInfoHolder testTile = new TileSolveInfoHolder(0, 0);
        int[][] aGrid = new int[9][9];
        aGrid = logic.giveGridMoreThan24Clues(26);

        logic.displayAbstractBoard(aGrid);

        GridSolver solver = new GridSolver(aGrid);
        solver.showStateOfCandidates();
        solver.simpleUnicityUpdate();
        solver.showStateOfCandidates();
        solver.selectInfoHolderWithonly1Candidate();


    }

}

