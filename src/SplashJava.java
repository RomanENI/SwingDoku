import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class SplashJava extends JFrame {
    Image splashScreen;
    ImageIcon imageIcon;

    public SplashJava() {
        splashScreen = Toolkit.getDefaultToolkit().getImage("Resources/loadScreen/swingdoku loading.jpg");
        // Create ImageIcon from Image
        imageIcon = new ImageIcon(splashScreen);
        // Set JWindow size from image size
        setSize(imageIcon.getIconWidth(),imageIcon.getIconHeight());
        // Get current screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Get x coordinate on screen for make JWindow locate at center
        int x = (screenSize.width-getSize().width)/2;
        // Get y coordinate on screen for make JWindow locate at center
        int y = (screenSize.height-getSize().height)/2;
        // Set new location for JWindow
        setLocation(x,y);
        // Make JWindow visible
//        setVisible(true)


        initMethods();
    }

    private void initMethods() {

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("closed");
//                System.exit(0);
                SwingDokuWindow sdw = new SwingDokuWindow();
//                sdw.display();
                System.out.println("displayed : =)");

            }
        });

    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Make JWindow appear for 10 seconds before disappear
                    Thread.sleep(3000);
//                    setVisible(false);
                    dispose();
                } catch(Exception e) {

                    e.printStackTrace();
                }
            }
        }).start();
    }//GEN-LAST:event_formWindowOpened

    // Paint image onto JWindow
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(splashScreen, 0, 0, this);
    }
    public static void main(String[]args) {
        SplashJava splash = new SplashJava();


        splash.setVisible(true);

    }
}