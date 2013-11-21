package simcity.Drew_restaurant.gui;

import simcity.Drew_restaurant.CustomerRole;
import simcity.Drew_restaurant.HostRole;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JButton addPersonB = new JButton("Add");
    private JTextField addPersonC = new JTextField();

    private RestaurantPanel restPanel;
    private String type;
    
    int namesShown=3;
    int paneSubtract=20;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;
        
        int rows=4;
        int cols=1;
        int hgap=1; 
        int vgap=1;

        setLayout(new GridLayout(rows,cols,hgap,vgap));
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));

        addPersonB.addActionListener(this);
        add(addPersonC);
        add(addPersonB);

        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        add(pane);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB) {
        	String nameCheck = addPersonC.getText().trim();     //Gets name from Text field
        	if(!nameCheck.equals(""))  addPerson(nameCheck);	//Does not add empty string
        	addPersonC.setText("");
        }
       /* else {
        	// Isn't the second for loop more beautiful?
            /*for (int i = 0; i < list.size(); i++) {
                JButton temp = list.get(i);*/
        	/*for (JButton temp:list){
                if (e.getSource() == temp)
                    restPanel.showInfo(type, temp.getText());
            }
        }*/
    }

    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name name of new person
     */
    public void addPerson(String name) {
        if (name != null) {
            JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - paneSubtract,
                    (int) (paneSize.height / namesShown));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            //if(type.equals("Waiters"))button.setEnabled(false);
            list.add(button);
            view.add(button);
            //restPanel.addPerson(type, name);//puts customer on list
            //restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        }
    }
}
