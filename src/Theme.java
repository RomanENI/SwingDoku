import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Theme {


    private static final int gridSquareWidthHeight = 648;
    private String path;
    private JLabel imgLeftPanel;
    private JLabel lblOptionPanel;

    private ImageIcon imgIconOptionPan;
    private ImageIcon imgIconLeftPan;
    private ImageIcon imgIconRightPan;
    private ImageIcon imgIconMsgeBorder;
    private ImageIcon imgWinScreen;


    private ArrayList<ImageIcon> imgsNormal;
    private ArrayList<ImageIcon> imgsFocussed;
    private ArrayList<ImageIcon> imgsSmall;
    private ArrayList<ImageIcon> imgsSmallAndFocussed;


    private ArrayList<ImageIcon> imgsLocked;

    private final int imgScaleInstance = 72;
    private final int imgScaleInstanceSmall = 24;

    private Color frameColor;
    private Color panRightMsgColor;
    private Color centralPanelGapsColor;

    public Theme(String themeName){
        this.path = pathSetter(themeName);
        loadImageLeftPanel(this.path);
        loadImageOptionPanel(this.path);
        loadImageWinScreen(this.path);
        loadImageRightPanel();


        loadBorders();

        loadImageLists(this.path);
        loadColors();


    }

    private void loadImageWinScreen(String pathStart) {
        this.imgWinScreen = new ImageIcon(pathStart+"/winScreen/win.gif");
    }

    private BufferedImage getBufferedImgWinScreen(String pathStart){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(pathStart+"/winScreen/win.gif"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return img;
    }

    public ImageIcon imgWinScreenResizer(int width, int height){
        BufferedImage bImg = getBufferedImgWinScreen(this.path);
        Image img = bImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);

    }


    private void loadBorders() {
        loadImgIconMsgeBorder(this.path);

    }


    private void loadImageOptionPanel(String pathStart) {
        BufferedImage img = null;
        if (this.lblOptionPanel == null){
            this.lblOptionPanel = new JLabel();
        }
        try {
            img = ImageIO.read(new File(pathStart+"/panels/optionPanel/optionPanelPicture.jpg"));
            Image dimg = img.getScaledInstance(540, 216,Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(dimg);
            this.imgIconOptionPan = imageIcon;
            lblOptionPanel.setIcon(imageIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadImgIconMsgeBorder(String pathStart) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(pathStart+"/panels/rightPanel/messages/borders/omnidirectionnel.jpg"));
            Image dimg = img.getScaledInstance(20, 10,Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(dimg);
            this.imgIconMsgeBorder = imageIcon;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public ImageIcon imgLeftPanelResizer(int width, int height){
        BufferedImage bImg = getBufferedImgLeftPanel(this.path);
        Image img = bImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);


    }

    private BufferedImage getBufferedImgLeftPanel(String pathStart){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(pathStart+"/panels/leftPanel/leftPanImg.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return img;
    }



    public ImageIcon imgOptionPanelResizer(int width, int height){
        BufferedImage bImg = getBufferedImgOptionPanel(this.path);
        Image img = bImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }



    private BufferedImage getBufferedImgOptionPanel(String pathStart){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(pathStart+"/panels/optionPanel/optionPanelPicture.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return img;
    }


    public void loadImageRightPanel(){

        this.imgIconRightPan = new ImageIcon(getBufferedImgRightPanel(this.path));
    }


    private BufferedImage getBufferedImgRightPanel(String pathStart){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(pathStart+"/panels/rightPanel/rightPanelPicture.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return img;
    }


    private void loadImageLists(String path) {
        loadNumberIconsNormal();
        loadNumberIconsFocussed();
        loadNumberIconsSmall();
        loadNumberIconsLocked();
        loadNumberIconsFocussedAndSmall();

    }

    private void loadNumberIconsLocked() {
        this.imgsLocked = new ArrayList<>();
        for (int i = 1;i<=9;i++){
            BufferedImage img = null;
            try {
                img = ImageIO.read(new File(this.path+"/Images/Locked/"+i+".png"));
                Image dimg = img.getScaledInstance(imgScaleInstance, imgScaleInstance,Image.SCALE_SMOOTH);
                ImageIcon imageIcon = new ImageIcon(dimg);
                this.imgsLocked.add(imageIcon);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadNumberIconsSmall() {

        this.imgsSmall = new ArrayList<>();
        for (int i = 0;i<=9;i++){
            BufferedImage img = null;
            try {
                img = ImageIO.read(new File(this.path+"/Images/Normal/"+i+".png"));
                Image dimg = img.getScaledInstance(imgScaleInstanceSmall, imgScaleInstanceSmall,Image.SCALE_SMOOTH);
                ImageIcon imageIcon = new ImageIcon(dimg);
                this.imgsSmall.add(imageIcon);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadColors(){
        this.frameColor = loadFramingColor();
        this.centralPanelGapsColor = loadCentralPanelGapsColor();
        this.panRightMsgColor = loadMsgColor();
    }

    private Color loadMsgColor() {
        String pathToFramingColor = this.path+"/colors/msgColor.txt";
        return loadColorFromFile(pathToFramingColor);
    }

    private Color loadColorFromFile(String pathToColor){
        Path filePath = Path.of(pathToColor);
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filePath.toUri()), StandardCharsets.UTF_8)) {

            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            //handle exception
        }

        String fileContent = contentBuilder.toString();
        String[] array = fileContent.split("\n");
        int red = Integer.parseInt(array[0]);
        int green = Integer.parseInt((array[1]));
        int blue = Integer.parseInt((array[2]));
        Color theColor = new Color(red, green, blue);
        return theColor;
    }


    private Color loadFramingColor() {
        String pathToFramingColor = this.path+"/colors/frameColor.txt";
        return loadColorFromFile(pathToFramingColor);
    }

    private Color loadCentralPanelGapsColor() {
        String pathToFramingColor = this.path+"/colors/gapsColor.txt";
        return loadColorFromFile(pathToFramingColor);
    }

    private void loadNumberIconsNormal() {
        this.imgsNormal = new ArrayList<>();
        for (int i = 0;i<=9;i++){
            BufferedImage img = null;
            try {
                img = ImageIO.read(new File(this.path+"/Images/Normal/"+i+".png"));
                Image dimg = img.getScaledInstance(imgScaleInstance, imgScaleInstance,Image.SCALE_SMOOTH);
                ImageIcon imageIcon = new ImageIcon(dimg);
                this.imgsNormal.add(imageIcon);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadNumberIconsFocussed() {
        this.imgsFocussed = new ArrayList<>();
        for (int i = 0;i<=9;i++){
            BufferedImage img = null;
            try {
                img = ImageIO.read(new File(this.path+"/Images/Focussed/"+i+".png"));
                Image dimg = img.getScaledInstance(imgScaleInstance, imgScaleInstance,Image.SCALE_SMOOTH);
                ImageIcon imageIcon = new ImageIcon(dimg);
                this.imgsFocussed.add(imageIcon);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadNumberIconsFocussedAndSmall() {
        this.imgsSmallAndFocussed = new ArrayList<>();
        for (int i = 0;i<=9;i++){
            BufferedImage img = null;
            try {
                img = ImageIO.read(new File(this.path+"/Images/Focussed/"+i+".png"));
                Image dimg = img.getScaledInstance(imgScaleInstanceSmall, imgScaleInstanceSmall,Image.SCALE_SMOOTH);
                ImageIcon imageIcon = new ImageIcon(dimg);
                this.imgsSmallAndFocussed.add(imageIcon);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    public JLabel getImgLeftPanel() {
        return imgLeftPanel;
    }

    public ArrayList<ImageIcon> getImgsNormal() {
        return imgsNormal;
    }

    public JLabel getLblOptionPanel() {
        return lblOptionPanel;
    }

    public ImageIcon getImgIconOptionPan() {
        return imgIconOptionPan;
    }

    public void setImgIconOptionPan(ImageIcon imgIconOptionPan) {
        this.imgIconOptionPan = imgIconOptionPan;
    }

    public ImageIcon getImgIconLeftPan() {
        return imgIconLeftPan;
    }

    public void setImgIconLeftPan(ImageIcon imgIconLeftPan) {
        this.imgIconLeftPan = imgIconLeftPan;
    }

    public Color getCentralPanelGapsColor() {
        return centralPanelGapsColor;
    }

    public ImageIcon getImgIconRightPan() {
        return imgIconRightPan;
    }

    public void setImgIconRightPan(ImageIcon imgIconRightPan) {
        this.imgIconRightPan = imgIconRightPan;
    }

    public ImageIcon getImgIconMsgeBorder() {
        return imgIconMsgeBorder;
    }

    public ArrayList<ImageIcon> getImgsSmall() {
        return imgsSmall;
    }

    public ArrayList<ImageIcon> getImgsFocussed() {
        return imgsFocussed;
    }

    public Color getPanRightMsgColor() {
        return panRightMsgColor;
    }

    public void setPanRightMsgColor(Color panRightMsgColor) {
        this.panRightMsgColor = panRightMsgColor;
    }

    public ArrayList<ImageIcon> getImgsLocked() {
        return imgsLocked;
    }

    public ImageIcon getImgWinScreen() {
        return imgWinScreen;
    }

    public void setImgWinScreen(ImageIcon imgWinScreen) {
        this.imgWinScreen = imgWinScreen;
    }

    public ArrayList<ImageIcon> getImgsSmallAndFocussed() {
        return imgsSmallAndFocussed;
    }

    public void setImgsSmallAndFocussed(ArrayList<ImageIcon> imgsSmallAndFocussed) {
        this.imgsSmallAndFocussed = imgsSmallAndFocussed;
    }


    public String pathSetter(String validThemeName){
        String returnValue = "";

        if (validThemeName.equals("boring")){
            returnValue = "Resources/Theme Boring";
        } else if (validThemeName.equals("rome antique")) {
            returnValue = "Resources/Theme Rome Antique";
        }


        return returnValue;
    }


    private void loadImageLeftPanel(String pathStart){
        BufferedImage img = null;
        if (this.imgLeftPanel == null){
            this.imgLeftPanel = new JLabel();
        }
        try {
            img = ImageIO.read(new File(pathStart+"/panels/leftPanel/leftPanImg.jpg"));
            Image dimg = img.getScaledInstance(320, 761,Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(dimg);
            this.imgIconLeftPan = imageIcon;
            imgLeftPanel.setIcon(imageIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public Color getFrameColor() {
        return frameColor;
    }

}
