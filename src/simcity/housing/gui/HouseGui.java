package simcity.housing.gui;

import simcity.TTRestaurant.TCustomerRole;
import simcity.TTRestaurant.TWaiterRole;
import simcity.interfaces.Person;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class HouseGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	//JFrame animationFrame = new JFrame("Restaurant Animation");
	HouseAnimationPanel animationPanel = new HouseAnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private HousingPanel restPanel = new HousingPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public HouseGui() {
        int WINDOWX = 1000;
        int WINDOWY = 600;
    	
    	setBounds(50, 50, WINDOWX, WINDOWY);

    	setLayout(new BorderLayout(5, 10));
        
        Dimension restDim = new Dimension(WINDOWX/2, (int) (WINDOWY)/3);
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        add(restPanel, BorderLayout.WEST);
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension(WINDOWX/4, (int) (WINDOWY/3));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
        
        add(animationPanel, BorderLayout.CENTER); 
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        currentPerson = person;

        if (person instanceof TCustomerRole) {
            TCustomerRole customer = (TCustomerRole) person;
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        infoPanel.validate();
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
    	
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(Person p) {
        if (currentPerson instanceof Person) {
            TCustomerRole cust = (TCustomerRole) currentPerson;
            /**
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
            */
        }
    }

    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        HouseGui gui = new HouseGui();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
