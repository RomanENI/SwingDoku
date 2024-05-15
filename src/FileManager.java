import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileManager {



    private static List<String> fromFileToList(){
            File theFile = null;

//        try {
//
//            theFile = new File("Resources/puzzles2_17_clue");
//
//        } catch (Exception e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }

        String path = "/puzzles2_17_clue";
        try {
            InputStream stream = FileManager.class.getResourceAsStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            List<String> list = new ArrayList<>();

            int i = 0;
            String line;
            while ((line = reader.readLine()) != null) {

                if(line.charAt(0) != '#'){
                    list.add(line);

                }
            }

            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }



        return null;
    }


//    public static List<String> fromFileToList() {
//        ArrayList<String> list = new ArrayList<String>();
//        File theFile = loadFileComplete();
//
//        Scanner kurz = null;
//        try {
//            kurz = new Scanner(theFile);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//
//        while (kurz.hasNextLine()){
//            String data = kurz.nextLine();
//            if(data.charAt(0) != '#'){
//                list.add(data);
////                System.out.println(data);
//            }
//
//        }
//
//
//        return list;
//    }

    public static String[] giveArrayFromFile(){
        ArrayList<String> theList = (ArrayList<String>) fromFileToList();
        int size = theList.size();
        System.out.println(size);
        String[] theArray = new String[size];
        for (int i = 0; i < size; i++) {
            theArray[i] = theList.get(i);
        }
        return theArray;
    }



}
