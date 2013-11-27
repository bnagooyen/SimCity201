package simcity.BRestaurant.gui;



import simcity.PersonAgent;
import simcity.interfaces.*;
import simcity.BRestaurant.*;


import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class BRestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	JFrame animationFrame = new JFrame("Restaurant Animation");
	BAnimationPanel animationPanel = new BAnimationPanel();

    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private BRestaurantPanel restPanel = new BRestaurantPanel(this);
   
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel;
    private JCheckBox stateCB;//part of infoLabel
    public int counter;
    private JPanel waiterPanel;
    private JLabel waiterLabel;
    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public BRestaurantGui() {
        int WINDOWX = 450;
        int WINDOWY = 650;

    	
    	setBounds(50, 50, WINDOWX, WINDOWY);

        setLayout(new BoxLayout((Container) getContentPane(), 
        		BoxLayout.Y_AXIS));
    	//setLayout(new BorderLayout (0,0));

        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .3));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        add(restPanel);
        
        
        
       
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .1));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);

        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Holds Information</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        
        
        
        add(infoPanel);
        
        
        
        
       
        
        
        Dimension animDim = new Dimension(WINDOWX, (int) (WINDOWY * .7));
        animationPanel.setPreferredSize(animDim);
        animationPanel.setMinimumSize(animDim);
        animationPanel.setMaximumSize(animDim);
        add(animationPanel);
        
        
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        stateCB.setVisible(true);
        currentPerson = person;

        if (person instanceof BCustomerRole) {
            BCustomerRole customer = (BCustomerRole) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        
        if (person instanceof BWaiterRole) {
            BWaiterRole waiter = (BWaiterRole) person;
            stateCB.setText("Break?");
            stateCB.setSelected(waiter.requestBreak());
            stateCB.setEnabled(!waiter.requestBreak());
            
            infoLabel.setText(
               "<html><pre>     Name: " + waiter.getName() + " </pre></html>");
            
        }
        infoPanel.validate();
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stateCB) {
            if (currentPerson instanceof BCustomerRole) {
                BCustomerRole c = (BCustomerRole) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(true);
            }
            
            else if(currentPerson instanceof BWaiterRole){
        		BWaiterRole w = (BWaiterRole) currentPerson;
        		stateCB.setEnabled(true);
        		stateCB.setSelected(true);
        		w.setRequestingBreak();
        	    }
        }
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(BCustomerRole c) {
        if (currentPerson instanceof BCustomerRole) {
            BCustomerRole cust = (BCustomerRole) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        BRestaurantGui gui = new BRestaurantGui();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}