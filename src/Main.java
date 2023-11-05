import javax.swing.*;

public class Main {
    public static void main(String[] args) {




        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
//                new LoadingScreen(null, true).setVisible(true);

                new SplashJava().setVisible(true);
//                SwingDokuWindow theSwingDokuWindow = new SwingDokuWindow();
//
//                theSwingDokuWindow.display();

//                lod.byeee();

            }
        });


    }
}