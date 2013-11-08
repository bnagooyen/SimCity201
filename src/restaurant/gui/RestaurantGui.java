package restaurant.gui;

import restaurant.CustomerAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	JFrame animationFrame = new JFrame("SimCity Animation");
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private RestaurantPanel restPanel = new RestaurantPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JPanel myPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
        int WINDOWX = 350;
        int WINDOWY = 650;
        
        myPanel = new JPanel();
        JPanel animPan=new JPanel();
        
        setLayout(new BorderLayout());
        
        animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        animationFrame.setBounds(WINDOWX+40, 30 , WINDOWX+520, WINDOWY);
        animationFrame.setVisible(true);
    	animationFrame.add(animationPanel); 
        
        Dimension animationDim = new Dimension((int)(WINDOWX * 0.2), (int) (WINDOWY));
//        animationPanel.setBounds(50, WINDOWY+100 , WINDOWX, WINDOWY+200);
        animationPanel.setMinimumSize(animationDim);
//    	 animPan.setMinimumSize(animationDim);
    	setBounds(30, 30, WINDOWX, WINDOWY);
    	
    	 Dimension e = new Dimension((int)(WINDOWX * 0.8), (int) (WINDOWY));

        setLayout(new BoxLayout((Container) getContentPane(), 
        		BoxLayout.Y_AXIS));

//        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY));
//        restPanel.setPreferredSize(restDim);
//        restPanel.setMinimumSize(restDim);
//        restPanel.setMaximumSize(restDim);
        
//        restPanel.setMaximumSize(animationDim);
        myPanel.setBorder(BorderFactory.createTitledBorder("SimCity Character Creator"));
       add(restPanel);
//        myPanel.setMaximumSize(animationDim);
        
        // Now, setup the info panel
//        Dimension infoDim = new Dimension((int)(WINDOWX * 0.2), WINDOWY);
        infoPanel = new JPanel();
//        infoPanel.setPreferredSize(infoDim);
//        infoPanel.setMinimumSize(infoDim);
//        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);

        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
//        infoPanel.setMaximumSize(animationDim);
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        add(infoPanel);
//        animPan.setLayout(new BorderLayout());
//        animPan.add(animationPanel, BorderLayout.CENTER);
//        add(animPan, BorderLayout.CENTER);
//        add(myPanel, BorderLayout.SOUTH);
       
     
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

        if (person instanceof CustomerAgent) {
            CustomerAgent customer = (CustomerAgent) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
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
        if (e.getSource() == stateCB) {
            if (currentPerson instanceof CustomerAgent) {
                CustomerAgent c = (CustomerAgent) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
        }
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(CustomerAgent c) {
        if (currentPerson instanceof CustomerAgent) {
            CustomerAgent cust = (CustomerAgent) currentPerson;
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
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
