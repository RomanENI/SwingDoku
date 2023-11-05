import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class PanelOnRight extends JPanel {

    JPanel panelInScroller;
    JScrollPane scrollPane;
    ArrayList<PanelMessage> listPanels;
    ArrayList<PanelMessage> listPanelsPlaying;
    ArrayList<PanelMessage> listPanelsBuilding;
    Dimension dimRightPanel = new Dimension(370, 0);
    JButton buttonRemoveFirst = new JButton();

    private Color msgBackgroundColor;



    private ImageIcon panMsgBorderMotif;
    private ImageIcon backgroundImage;

    BorderLayout RightPanelLayout;


    public void setScrollPane(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public JPanel getPanelInScroller() {
        return panelInScroller;
    }

    public void setPanelInScroller(JPanel panelInScroller) {
        this.panelInScroller = panelInScroller;
    }

    public ArrayList<PanelMessage> getListPanels() {
        return listPanels;
    }

    public void setListPanels(ArrayList<PanelMessage> listPanels) {
        this.listPanels = listPanels;
    }

    public BorderLayout getRightPanelLayout() {
        return RightPanelLayout;
    }

    public void setRightPanelLayout(BorderLayout rightPanelLayout) {
        RightPanelLayout = rightPanelLayout;
    }

    public Color getMsgBackgroundColor() {
        return msgBackgroundColor;
    }

    public void setMsgBackgroundColor(Color msgBackgroundColor) {
        this.msgBackgroundColor = msgBackgroundColor;
    }

    PanelOnRight(){

        this.setPreferredSize(dimRightPanel);
        this.panelInScroller = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                if (backgroundImage != null){
                    backGroundImageResizer(this.getWidth(), this.getHeight());
                    g.drawImage(backgroundImage.getImage(), 0, 0, null);
                }
            }
        };
        this.panelInScroller.setBackground(Color.yellow);
        this.panelInScroller.setLayout(new BoxLayout(panelInScroller, BoxLayout.PAGE_AXIS));
        this.listPanels = new ArrayList<>();
        this.listPanelsPlaying = this.listPanels;
        this.listPanelsBuilding = new ArrayList<>();
        this.RightPanelLayout = new BorderLayout();
        this.setLayout(this.RightPanelLayout);




        initScrollPane();
        setUpButtonRemoveFirst();

        setupPanelsResizer();
    }

    private void setupPanelsResizer() {
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
//                System.out.println(e.getComponent().getWidth());
                if (listPanels.size() != 0) {
                    for (PanelMessage pan : listPanels){
                        pan.setMaxWidth(e.getComponent().getWidth());
//                        System.out.println("max width set to "+e.getComponent().getWidth());
                        pan.updateMaxSize();
                    }

                }
            }
        });


    }

    private void setUpButtonRemoveFirst() {
        buttonRemoveFirst.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeFirst();
            }
        });
        buttonRemoveFirst.setText("Supprimer le message le plus ancien");
    }

    private void removeFirst() {
        Component[] allPanelsInScroller = getPanelInScroller().getComponents();
        boolean remove = false;
        Component toRemove = null;
        for (JPanel pan : listPanels){
            if (pan == allPanelsInScroller[0]){

                toRemove = pan;
                remove = true;

            }
        }

        if (remove){
            panelInScroller.remove(toRemove);
            panelInScroller.revalidate();
            panelInScroller.repaint();
            listPanels.remove(toRemove);
            if (toRemove instanceof PlayMovePan){
                ((PlayMovePan) toRemove).getCloseListener().removePanActions((PlayMovePan) toRemove);
            }
        }
    }

    public void removeMsg(JPanel msg){
        Component[] allPanelsInScroller = panelInScroller.getComponents();
        boolean remove = false;
        JPanel toRemove = null;
        for (JPanel pan : listPanels){
            if (pan == msg){
                toRemove = pan;
                remove = true;

            }
        }
        if (remove){
            panelInScroller.remove(toRemove);
            panelInScroller.revalidate();
            panelInScroller.repaint();
            listPanels.remove(toRemove);
        }
    }


    private void initScrollPane() {
        this.scrollPane = new JScrollPane(this.panelInScroller, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.scrollPane.setPreferredSize(new Dimension(323, 0));
        this.add(getScrollPane(), BorderLayout.CENTER);
        this.add(buttonRemoveFirst, BorderLayout.PAGE_START);
        this.scrollPane.setBorder(BorderFactory.createEmptyBorder());

    }



    public void addMessage(Message m, PanelGrid.PanelCloseListener listener){


        PanelMessage panelMessage = new PanelMessage(m, this.panMsgBorderMotif, this.msgBackgroundColor, listener);
        listPanels.add(panelMessage);
        getPanelInScroller().add(panelMessage);


        getPanelInScroller().revalidate();
        getPanelInScroller().repaint();

        scrollDown();

    }


    public void addMessageUndo(Message m, HashMap<String, Object> action, PanelGrid.PlayPanelButtonListener watcher, PanelGrid.PanelCloseListener closeListener){

        PlayMovePan newPan = new PanelOnRight.PlayMovePan(m, this.panMsgBorderMotif, this.msgBackgroundColor, action, watcher, closeListener);
        panelInScroller.add(newPan);
        this.getListPanels().add(newPan);
        scrollDown();

    }



    public void scrollDown() {
        Container contentPane = getScrollPane().getParent().getParent().getParent().getParent().getParent().getParent();
        contentPane.revalidate(); //Update the scrollbar size
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());


    }

    public void goModePlay(){
        for (JPanel pan : this.listPanels){
            this.getPanelInScroller().remove(pan);
        }
        this.listPanels.clear();
        this.listPanels = this.listPanelsPlaying;
        this.getPanelInScroller().revalidate();
        this.getPanelInScroller().repaint();
    }


    public void switchModes(){

         if (this.listPanels == this.listPanelsBuilding){
             for (JPanel pan : this.listPanels){
                 this.getPanelInScroller().remove(pan);

             }
             this.listPanels.clear();
             this.listPanels = this.listPanelsPlaying;
             restorePlayMessages();
             this.getPanelInScroller().revalidate();
             this.getPanelInScroller().repaint();
//             System.out.println("Went to play mode");
         }else if (this.listPanels == this.listPanelsPlaying){
             for (JPanel pan : this.listPanels){
                 this.getPanelInScroller().remove(pan);
                 this.getPanelInScroller().revalidate();
                 this.getPanelInScroller().repaint();
             }
             this.listPanels = this.listPanelsBuilding;
//             System.out.println("Went to build mode");
        }else {
             System.out.println("error switch");
         }

    }

    public void restorePlayMessages(){

        for (JPanel message : this.listPanelsPlaying){
            getPanelInScroller().add(message);
        }
        scrollDown();

    }

    public ImageIcon getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(ImageIcon backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public ImageIcon getPanMsgBorderMotif() {
        return panMsgBorderMotif;
    }

    public void setPanMsgBorderMotif(ImageIcon panMsgBorderMotif) {
        this.panMsgBorderMotif = panMsgBorderMotif;
    }



    private void backGroundImageResizer(int width, int height){
        BufferedImage bi = new BufferedImage(
                this.backgroundImage.getIconWidth(),
                this.backgroundImage.getIconHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.createGraphics();

        // paint the Icon to the BufferedImage.
        this.backgroundImage.paintIcon(null, g, 0,0);
        g.dispose();
        Image img = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(img);
        this.backgroundImage = newIcon;
    }

    public class PanelMessage extends JPanel{

        private Message msg;
        private JLabel lblPan;
        private JButton btnPan;

        private PanelCloseButton closeBtnToggle;
        private Color colorMsg;

        private GridBagLayout layout;

        public PanelGrid.PanelCloseListener getCloseListener() {
            return closeListener;
        }

        private PanelGrid.PanelCloseListener closeListener;
        private BtnCloseWatcher closeWatcher;

        private ImageIcon imgIconBorder;
        final int borderHorizontal = 7;
        final int borderVertical = 5;

        private int maxWidth = 1000000;

        private int maxHeight = 53;
        private int height = 64;
            Dimension maximumSize = new Dimension(maxWidth, height);


            PanelMessage(Message msge, ImageIcon panMsgBorderMotif, Color backgroundColor, PanelGrid.PanelCloseListener closeListener){

                this.colorMsg = backgroundColor;
                this.layout = new GridBagLayout();


                this.closeListener = closeListener;
                this.closeWatcher = new BtnCloseWatcher(null);
                this.closeBtnToggle = new PanelCloseButton(this.colorMsg, this.closeWatcher);
                this.closeBtnToggle.setPreferredSize(new Dimension(42,42));
                closeWatcher.addPropertyChangeListener(this.closeListener);
                this.closeWatcher.setBtnCloseInPanel(this.closeBtnToggle);
                this.setLayout(this.layout);
//                if (this.colorMsg != null){
//                    this.closeButtonStandin.setBackground(colorMsg);
//                }




                this.lblPan = new JLabel();
                this.msg = msge;
                this.imgIconBorder = panMsgBorderMotif;


                this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                lblPan.setText(msge.getMsgText());
                this.setPreferredSize(new Dimension(panelInScroller.getWidth(), height*msge.getMsgLength()));
                maxHeight = height * msge.getMsgLength();
                updateMaxSize();
                buildPanelLayout();

//                this.layout = new FlowLayout(FlowLayout.LEFT,30,0);

//                this.lblPan.setBorder(BorderFactory.createEmptyBorder(0,0,0, this.getWidth()/11));






             }////////////////////////////////////////////////////



        public void buildPanelLayout(){
            GridBagConstraints gb = new GridBagConstraints();
            int percentLabel = 60;
            int percentButton = 40;


            gb.gridy = 0;
            gb.gridx = 0;
            gb.gridheight = 3;
            gb.gridwidth = percentLabel;
            gb.insets.right = 0;
            gb.insets.left = 5;
            this.add(lblPan, gb);
            gb.insets.left = 0;
            gb.gridwidth = 1;
            gb.insets.right = 10;

            if (this.imgIconBorder != null){
                this.setBorder(BorderFactory.createMatteBorder(0, borderHorizontal, borderVertical, borderHorizontal, this.imgIconBorder));
            }
            if (this.colorMsg != null){
                this.setBackground(this.colorMsg);
            }

            if (this.msg.getMsgButton() != null){
                this.btnPan = this.msg.getMsgButton();

                gb.gridwidth = percentButton;
                gb.gridx = percentLabel;
                gb.gridy = 1;
                this.add(btnPan, gb);

            }
            gb.insets.right = 0;
            gb.gridwidth = 100-percentLabel;
            gb.gridx = percentLabel;
            gb.gridy = 0;
            gb.ipadx = 0;
            gb.weightx = 1;
            gb.weighty = 1;
            gb.anchor = GridBagConstraints.FIRST_LINE_END;
            this.add(this.closeBtnToggle, gb);
            JPanel standin = this.closeBtnToggle;





            listenerImplementation();


            this.setAlignmentX(Component.LEFT_ALIGNMENT);
            this.revalidate();
            this.repaint();


        }

        private void listenerImplementation() {
            PanelMessage pan = this;
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    pan.getCloseBtnToggle().getLayout().show(pan.getCloseBtnToggle(), "visible");
                    //all other panels hide the close button
                    for (PanelMessage otherPan : listPanels){
                        if (otherPan != pan){
                            otherPan.getCloseBtnToggle().getLayout().show(otherPan.getCloseBtnToggle(), "invisible");

                        }
                    }

                }
            });
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    pan.getCloseBtnToggle().getLayout().show(pan.getCloseBtnToggle(), "invisible");
                }
            });
            if (this.btnPan != null){
                this.btnPan.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        pan.getCloseBtnToggle().getLayout().show(pan.getCloseBtnToggle(), "visible");
                    }
                });
            }

            this.closeBtnToggle.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    pan.getCloseBtnToggle().getLayout().show(pan.getCloseBtnToggle(), "visible");
                }
            });



        }

        public void rebuildPanelLayout(){
            this.removeAll();
            buildPanelLayout();


        }






        public void updateMaxSize() {
            maximumSize = new Dimension(maxWidth, maxHeight);
            this.setMaximumSize(maximumSize);
            this.revalidate();
            this.repaint();
        }

        //getters and setters


        public int getMaxHeight() {
            return maxHeight;
        }

        public void setMaxHeight(int maxHeight) {
            this.maxHeight = maxHeight;
        }
        public PanelCloseButton getCloseBtnToggle() {
            return closeBtnToggle;
        }


        public int getMaxWidth() {
            return maxWidth;
        }

        public void setMaxWidth(int maxWidth) {
            this.maxWidth = maxWidth;
        }

        public Message getMsg() {
            return msg;
        }

        public void setMsg(Message msg) {
            this.msg = msg;
        }

        public JLabel getLblPan() {
            return lblPan;
        }

        public void setLblPan(JLabel lblPan) {
            this.lblPan = lblPan;
        }

        public JButton getBtnPan() {
            return btnPan;
        }

        public void setBtnPan(JButton btnPan) {
            this.btnPan = btnPan;
        }

        public Color getColorMsg() {
            return colorMsg;
        }
        //TODO







    }



    public class PanelCloseButton extends JPanel{
        @Override
        public CardLayout getLayout() {
            return layout;
        }

        Color colorOnHide;
        CardLayout layout;
        JPanel panelInvis;
        private BtnCloseWatcher closeWatcher;

        public PanelCloseButton(Color color, BtnCloseWatcher watcher) {
            layout = new CardLayout();
            this.panelInvis = new JPanel();
            this.closeWatcher = watcher;
            if (color != null){
                this.colorOnHide = color;
                this.panelInvis.setBackground(color);

            }
            setLayout(layout);
            JButton button = new JButton("X");
            PanelCloseButton pnou = this;
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    watcher.buttonChangeState();
                    PanelMessage toRemove = null;
                    for (PanelMessage pan : listPanels){
                        if (pan == pnou.getParent()){
                            toRemove = pan;


                            //il faut un accès aux listes d'actions
                        }
                    }
                    if (toRemove != null){

                        listPanels.remove(toRemove);
                        panelInScroller.remove(toRemove);
                        panelInScroller.revalidate();
                        panelInScroller.repaint();

                    }

                }
            });
            add(button, "visible");
            add(panelInvis, "invisible");
            layout.show(this, "invisible");
        }


        public JPanel getPanelInvis() {
            return panelInvis;
        }


    }

    public class PlayMovePan extends PanelMessage{

        private Message msg;
        private HashMap<String, Object> action;
        private JButton btnPan;
        private ActionListener listenerUndo;
        private ActionListener listenerRedo;
        private boolean modeUndo;
        private PlayPanelBtnStateWatcher watcher;
        private PanelGrid.PlayPanelButtonListener listener;





        PlayMovePan(Message msge, ImageIcon panMsgBorderMotif, Color msgBackgroundColor, HashMap<String, Object> action, PanelGrid.PlayPanelButtonListener obs
        , PanelGrid.PanelCloseListener closeListener){
            super(msge, panMsgBorderMotif, msgBackgroundColor, closeListener);

            GridBagConstraints gb = new GridBagConstraints();
            this.listener = obs;
            this.btnPan = new JButton();
            this.watcher = new PlayPanelBtnStateWatcher(btnPan);
            watcher.addPropertyChangeListener(this.listener);


            this.action = action;
            this.modeUndo = true;
//            System.out.println("this.layout "+this.getLayout());


            this.btnPan.setText("Anuler");



            gb.gridx = 1;
            gb.gridy = 0;
//            gb.ipadx = 25;
            this.add(btnPan, gb);
            this.btnPan.setPreferredSize(new Dimension(78, 26));
            playListenersImplementation();
            msge.setMsgButton(this.btnPan);
            this.rebuildPanelLayout();

        }//end of playMovePan



        private void playListenersImplementation(){
            PlayMovePan pan = this;
            int formerValue = (int) action.get("formerValue");
            int newValue = (int) action.get("newValue");
            this.listenerUndo = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    //TODO I don't think this works
                    JButton btnSource = (JButton) e.getSource();
                    PanelNumber panNumber = (PanelNumber) pan.getAction().get("panel");
                    int newValue = panNumber.getValue();
                    panNumber.changeNumber(formerValue);
                    btnSource.setText("Rétablir");
                    System.out.println("listener undo");
                    panNumber.getObs().panelChangeValueFromButton(newValue, panNumber.getValue());
//                    ActionListener[] listeners = btnSource.getActionListeners();
//
//                    for (ActionListener listener : listeners){
//                        btnSource.removeActionListener(listener);
//                        System.out.println("un tour");
//                    }
                }
            };

            this.listenerRedo = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton btnSource = (JButton) e.getSource();
                    PanelNumber panNumber = (PanelNumber) pan.getAction().get("panel");
                    int formerValue = panNumber.getValue();
                    panNumber.changeNumber(newValue);
                    btnSource.setText("Anuler");
                    System.out.println("listener redo");
                    panNumber.getObs().panelChangeValueFromButton(formerValue, panNumber.getValue());
//                    ActionListener[] listeners = btnSource.getActionListeners();
//                    for (ActionListener listener : listeners){
//                        btnSource.removeActionListener(listener);
//                    }

                }
            };


            this.btnPan.addActionListener(this.listenerUndo);
            this.btnPan.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton button = (JButton) e.getSource();

                    pan.updateBtnListener();
                    watcher.buttonChangeState(pan.isModeUndo());

                }
            });


        }


        public HashMap<String, Object> getAction() {
            return action;
        }

        public void setAction(HashMap<String, Object> action) {
            this.action = action;
        }


        public void updateBtnListener(){
            if (this.modeUndo){

                this.btnPan.removeActionListener(this.listenerUndo);
                this.btnPan.addActionListener(this.listenerRedo);
                this.setModeUndo(false);

            }else {
                this.btnPan.removeActionListener(this.listenerRedo);
                this.btnPan.addActionListener(this.listenerUndo);
                this.setModeUndo(true);


            }
        }



        public boolean isModeUndo() {
            return modeUndo;
        }

        public void setModeUndo(boolean modeUndo) {
            this.modeUndo = modeUndo;
        }

        public JButton getBtnPan() {
            return btnPan;
        }



    }










}
