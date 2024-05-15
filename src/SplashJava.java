import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class SplashJava extends JFrame {
    Image splashScreen;
    ImageIcon imageIcon;

    SwingDokuWindow instance ;

    public SplashJava() {
        instance = SwingDokuWindow.giveInstance();
        BufferedImage img = null;
        String fullPath = "/loadScreen/swingdoku loading.JPG";
        try {
            img = ImageIO.read(getClass().getResourceAsStream(fullPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        splashScreen = img;
//        splashScreen = Toolkit.getDefaultToolkit().getImage("Resources/loadScreen/swingdoku loading.JPG");
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


        ArrayList icons = loadIcons();
        this.setIconImages(icons);
        initMethods();
    }

    private ArrayList loadIcons() {
        String[] keys = new String[] {"1616", "2424", "3232", "6464"};

        ArrayList<Image> list = new ArrayList<>();
        for(String key : keys){
            BufferedImage img;
            String fullPath = "/windowIcons/lauchIcon" + key + ".png";
            try {
                img = ImageIO.read(getClass().getResourceAsStream(fullPath));
                Image image = img;
                list.add(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        return list;

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
                    instance.setVisible(true);
//                System.exit(0);
//                SwingDokuWindow sdw = new SwingDokuWindow();

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
                    // Make JWindow appear for 3 seconds before disappear
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