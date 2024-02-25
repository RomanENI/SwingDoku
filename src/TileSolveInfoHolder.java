import java.util.ArrayList;
import java.util.List;

public class TileSolveInfoHolder {


    public int getxCoord() {
        return xCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }





    private int xCoord;
    private int yCoord;

    public List<Integer> getCandidates() {
        return candidates;
    }

    private List<Integer> candidates;

    public void removeCandidate(int number){


        for (int i = 0; i < candidates.size(); i++) {
            if (candidates.get(i) == number && candidates.contains(number)){
                this.candidates.remove(i);
            }
        }



    }

    public TileSolveInfoHolder(int coordX, int coordY){
        this.candidates = new ArrayList<Integer>();
        for (int i = 1; i < 10; i++) {
            candidates.add(i);
        }
        this.xCoord = coordX;
        this.yCoord = coordY;

    }

    public void showCandidates(){
        for(int candidate : candidates){
            System.out.print(candidate + " ");
        }
        System.out.println();
    }





}
