import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileManager {



    private static File loadFileComplete(){
            File theFile = null;

        try {

            theFile = new File("Resources/puzzles2_17_clue");

        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return theFile;
    }


    public static List<String> fromFileToList() {
        ArrayList<String> list = new ArrayList<String>();
        File theFile = loadFileComplete();

        Scanner kurz = null;
        try {
            kurz = new Scanner(theFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


        while (kurz.hasNextLine()){
            String data = kurz.nextLine();
            if(data.charAt(0) != '#'){
                list.add(data);
//                System.out.println(data);
            }

        }


        return list;
    }

    public static String[] giveArrayFromFile(){
        ArrayList<String> theList = (ArrayList<String>) fromFileToList();
        int size = theList.size();
        String[] theArray = new String[size];
        for (int i = 0; i < size; i++) {
            theArray[i] = theList.get(i);
        }
        return theArray;
    }



}
