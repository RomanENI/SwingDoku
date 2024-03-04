import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SDMenuBar extends JMenuBar{


    private final JMenu edit;
    private JMenuItem undo;
    private JMenuItem redo;

    private final JMenu menuTheme;


    private JMenuItem menuItemBoring;
    private JMenuItem menuItemAncientRome;
    private JMenuItem menuItemUnicorn1;
    private JMenuItem menuItemUnicorn2;



    private JMenuItem menuItemUnicorn3;



    private JMenuItem menuItemPokemonStarter;



    private JMenuItem menuItemLeaves;



    private JMenuItem menuItemFruitsAndVegetables;


    private JMenuItem menuItemDooku;
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

        menuItemUnicorn1 = new JMenuItem("Licornes 1");
        menuTheme.add(menuItemUnicorn1);
        menuItemUnicorn2 = new JMenuItem("Licornes 2");
        menuTheme.add(menuItemUnicorn2);
        menuItemUnicorn3 = new JMenuItem("Licornes 3");
        menuTheme.add(menuItemUnicorn3);

        menuItemDooku = new JMenuItem("Comte Dooku");
        menuTheme.add(menuItemDooku);

        menuItemPokemonStarter = new JMenuItem("Pokemon Starter");
        menuTheme.add(menuItemPokemonStarter);
        menuItemLeaves = new JMenuItem("Feuilles");
        menuTheme.add(menuItemLeaves);
        menuItemFruitsAndVegetables = new JMenuItem("Fruits et Légumes");
        menuTheme.add(menuItemFruitsAndVegetables);

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


    public void setAllMenusTo(boolean mode){
        Component[] menus = this.getComponents();
        for (Component item : menus){
            if (item instanceof JMenu){
                item.setEnabled(mode);
            }
        }
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

    public JMenu getMenuMode() {
        return menuMode;
    }

    public JMenuItem getMenuItemUnicorn1() {
        return menuItemUnicorn1;
    }

    public JMenuItem getMenuItemUnicorn2() {
        return menuItemUnicorn2;
    }
    public JMenuItem getMenuItemDooku() {
        return menuItemDooku;
    }
    public JMenuItem getMenuItemUnicorn3() {
        return menuItemUnicorn3;
    }

    public JMenuItem getMenuItemPokemonStarter() {
        return menuItemPokemonStarter;
    }
    public JMenuItem getMenuItemLeaves() {
        return menuItemLeaves;
    }
    public JMenuItem getMenuItemFruitsAndVegetables() {
        return menuItemFruitsAndVegetables;
    }
}
