package simcity.KRestaurant.gui;

import simcity.KRestaurant.KCustomerRole;
import simcity.KRestaurant.KWaiterRole;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class KRestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	JFrame animationFrame = new JFrame("Restaurant Animation");
	public KAnimationPanel animationPanel = new KAnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private KRestaurantPanel restPanel = new KRestaurantPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    private JPanel panel = new JPanel();
        
    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    private final int boundx = 50;
    private final int boundy = 50;
    
    private final int infopLayoutr = 1;
    private final int infopLayoutc = 2;
    private final int infopLayouth = 30;
    private final int infopLayoutv = 0;
    
    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public KRestaurantGui() {
        int WINDOWX = 400;
        int WINDOWY = 700;

    	setBounds(boundx, boundy, 2*WINDOWX, WINDOWY+200);

        setLayout(new BorderLayout());
        panel.setLayout(new BorderLayout());
        
        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .6));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        panel.add(restPanel,BorderLayout.CENTER);
        
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

        infoPanel.setLayout(new GridLayout(infopLayoutr, infopLayoutc, infopLayouth, infopLayoutv));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);

        panel.add(infoPanel, BorderLayout.SOUTH);
        add(panel, BorderLayout.NORTH);
        add(animationPanel, BorderLayout.CENTER);
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
        if (person instanceof KCustomerRole) {
            KCustomerRole customer = (KCustomerRole) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        if (person instanceof KWaiterRole) {
        	KWaiterRole waiter = (KWaiterRole) person;
        	stateCB.setText("Break?");
            stateCB.setSelected(waiter.onBreak());
            stateCB.setEnabled(!waiter.onBreak());
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
            if (currentPerson instanceof KCustomerRole) {
                KCustomerRole c = (KCustomerRole) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
            
            if (currentPerson instanceof KWaiterRole) {
        		KWaiterRole w = (KWaiterRole) currentPerson;
            	if(stateCB.isSelected()) {
                	w.setWantBreak();
            	}
            	else if(!stateCB.isSelected()) {
            		w.setBackToWork();
                	stateCB.setText("Break?");
            	}
            	//stateCB.setEnabled(false);
            }
        }
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setBackToWorkCB() {
    	stateCB.setText("Go Back To Work?");
    }
    
    public void uncheckCB() {
    	stateCB.setSelected(false);
    }
    
    public void setCustomerEnabled(KCustomerRole c) {
        if (currentPerson instanceof KCustomerRole) {
            KCustomerRole cust = (KCustomerRole) currentPerson;
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
        KRestaurantGui gui = new KRestaurantGui();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
