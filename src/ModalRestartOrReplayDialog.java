import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ModalRestartOrReplayDialog {
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> createAndShowGUI());
//    }

    private ActionListener restartListener;
    private ActionListener newGameListener;


    ModalRestartOrReplayDialog(ActionListener restartListener, ActionListener newGameListener){
        this.restartListener = restartListener;
        this.newGameListener = newGameListener;
    }
    public void createAndShowGUI(SwingDokuWindow swingDoku) {
//        JFrame frame = new JFrame("Main Frame");
//        JButton openButton = new JButton("Open Modal Dialog");
//        openButton.addActionListener(e -> openModalDialog(jframe));
//
//        frame.getContentPane().add(openButton);
//        frame.setSize(300, 200);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);

        openModalDialog(swingDoku);

    }

    private void openModalDialog(SwingDokuWindow parentFrame) {

        JDialog modalDialog = new JDialog(parentFrame, "Modal Dialog", true);

        modalDialog.setTitle("Custom dialog");

        JPanel btnPan = new JPanel();
        JPanel textPan = new JPanel();
        btnPan.setLayout(new FlowLayout());
        JLabel msg = new JLabel("Bravo, vous avez résolu la grille.");
        textPan.add(msg, BorderLayout.CENTER);

        JButton replayBtn = new JButton("Rejouer cette grille");
        replayBtn.addActionListener(restartListener);
        btnPan.add(replayBtn);
        JButton nextGridBtn = new JButton("Créer une nouvelle grille");
        nextGridBtn.addActionListener(newGameListener);
        btnPan.add(nextGridBtn);

        modalDialog.add(btnPan, BorderLayout.PAGE_END);
        modalDialog.add(textPan, BorderLayout.CENTER);

        // Disable the default close operation
        modalDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);





        for (WindowListener wl : modalDialog.getWindowListeners()) {
            modalDialog.removeWindowListener(wl);
        }
        modalDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                JOptionPane.showMessageDialog(null, "Vous devez obligatoirement choisir une option : )");
            }
        });
        replayBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modalDialog.dispose();
            }
        });

        nextGridBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modalDialog.dispose();
            }
        });

        modalDialog.setSize(338, 150);
        modalDialog.setLocationRelativeTo(parentFrame);
        modalDialog.setVisible(true);
    }
}