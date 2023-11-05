import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SDMenuBar extends JMenuBar{


    private final JMenu edit;
    private JMenuItem undo;
    private JMenuItem redo;

    private final JMenu menuTheme;


    private JMenuItem menuItemBoring;


    private JMenuItem menuItemAncientRome;

    private final JMenu menuMode;


    private JCheckBoxMenuItem conjecture;
    private JCheckBoxMenuItem certitude;

    SDMenuBar(){

        edit = new JMenu("Edit");
        undo = new JMenuItem("Anuler");
        redo = new JMenuItem("Rétablir");

        menuTheme = new JMenu("Thème");
        menuItemBoring = new JMenuItem("Boring");

        menuTheme.add(menuItemBoring);
        this.add(menuTheme);
        menuItemAncientRome = new JMenuItem("Rome Antique");

        menuTheme.add(menuItemAncientRome);

        menuMode = new JMenu("Placement");



        disableUndo();
        disableRedo();
        edit.add(undo, 0);
        edit.add(redo, 1);
        this.add(edit, 0);

        this.add(menuMode);



        this.conjecture = new JCheckBoxMenuItem("Conjecture");
        this.certitude = new JCheckBoxMenuItem("Certitude");

        menuMode.add(conjecture);
        menuMode.add(certitude);
        certitude.setSelected(true);


        setupMenuModeLinks();

    }

    private void setupMenuModeLinks() {
        conjecture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                certitude.setSelected(false);
            }
        });
        certitude.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                conjecture.setSelected(false);
            }
        });
    }


    public void enableUndo(){
        this.getUndo().setEnabled(true);
    }

    public void disableUndo(){
        this.getUndo().setEnabled(false);
    }

    public void enableRedo(){
        this.getRedo().setEnabled(true);
    }

    public void disableRedo(){
        this.getRedo().setEnabled(false);
    }


    public JMenuItem getMenuItemAncientRome() {
        return menuItemAncientRome;
    }
    public JMenuItem getUndo() {
        return undo;
    }

    public void setUndo(JMenuItem undo) {
        this.undo = undo;
    }

    public JMenuItem getRedo() {
        return redo;
    }

    public void setRedo(JMenuItem redo) {
        this.redo = redo;
    }

    public JMenu getEdit() {
        return edit;
    }

    public JMenu getMenuTheme() {
        return menuTheme;
    }
    public JMenuItem getMenuItemBoring() {
        return menuItemBoring;
    }

    public JCheckBoxMenuItem getConjecture() {
        return conjecture;
    }

    public JCheckBoxMenuItem getCertitude() {
        return certitude;
    }


}
