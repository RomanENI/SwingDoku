import javax.swing.*;
import java.awt.event.*;

public class ModalDialogExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Main Frame");
        JButton openButton = new JButton("Open Modal Dialog");
        openButton.addActionListener(e -> openModalDialog(frame));

        frame.getContentPane().add(openButton);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void openModalDialog(JFrame parentFrame) {
        JDialog modalDialog = new JDialog(parentFrame, "Modal Dialog", true);

        // Disable the default close operation
        modalDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        modalDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Handle the close operation based on your requirements
                // For example, show a confirmation dialog
                int option = JOptionPane.showConfirmDialog(modalDialog,
                        "Are you sure you want to close this dialog?",
                        "Confirmation", JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    modalDialog.dispose(); // Close the dialog if the user chooses 'Yes'
                }
                // If 'No' is chosen, the dialog will remain open
            }
        });

        modalDialog.setSize(200, 150);
        modalDialog.setLocationRelativeTo(parentFrame);
        modalDialog.setVisible(true);
    }
}