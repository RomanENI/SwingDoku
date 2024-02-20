import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CustomDialog
{
    private List<JComponent> components;

    private String title;
    private int messageType;
    private JRootPane rootPane;
    private String[] options;
    private int optionIndex;

    public CustomDialog()
    {
        components = new ArrayList<JComponent>();

        setTitle("Custom dialog");
        setMessageType(JOptionPane.PLAIN_MESSAGE);
        setRootPane(null);
        setOptions(new String[] { "Refaire cette grille", "Créer une nouvelle grille" });
        setOptionSelection(0);


    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setMessageType(int messageType)
    {
        this.messageType = messageType;
    }

    public void addComponent(JComponent component)
    {
        components.add(component);
    }

    public void addMessageText(String messageText)
    {
        JLabel label = new JLabel("<html>" + messageText + "</html>");

        components.add(label);
    }

    public void setRootPane(JRootPane rootPane)
    {
        this.rootPane = rootPane;

    }

    public void setOptions(String[] options)
    {
        this.options = options;
    }

    public void setOptionSelection(int optionIndex)
    {
        this.optionIndex = optionIndex;
    }

    public int show()
    {
        int optionType = JOptionPane.OK_CANCEL_OPTION;
        Object optionSelection = null;

        if(options.length != 0)
        {
            optionSelection = options[optionIndex];
        }

        int selection = JOptionPane.showOptionDialog(rootPane,
                components.toArray(), title, optionType, messageType, null,
                options, optionSelection);

        return selection;
    }

    public static String getLineBreak()
    {
        return "<br>";
    }



    // https://stackoverflow.com/questions/942056/remove-x-button-in-swing-jdialog

}