package simcity.LRestaurant.gui;

import simcity.LRestaurant.LCustomerRole;
import simcity.LRestaurant.LWaiterRole;
import agent.Agent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class LRestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	JFrame animationFrame = new JFrame("Restaurant 2013");
	LAnimationPanel animationPanel = new LAnimationPanel();

    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private LRestaurantPanel restPanel = new LRestaurantPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JPanel allPanel = new JPanel();
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    private JButton pauseButton = new JButton("Pause");
    private boolean paused;
    public Agent a;
    //private ImageIcon myself; //picture
    //private JPanel nPanel;//new panel under infoPanel

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    private final int infoRow = 1;
    private final int infoCol = 2;
    private final int infoHgap = 10;
    private final int infoVgap = 0;
    
    private final int xBound = 50;
    private final int yBound = 50;
    
    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public LRestaurantGui() {
        int WINDOWX = 1000;
        int WINDOWY = 750;
        int fRow = 2;
        int fCol = 0;

        
        animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        animationFrame.setBounds(100+WINDOWX, WINDOWY-50 , WINDOWX+100, WINDOWY+100);//offset of frame
        animationFrame.setLayout(new GridLayout(fRow, fCol, infoHgap, infoVgap));
        animationFrame.setVisible(true);
    	animationFrame.add(animationPanel);
    	
    	setBounds(xBound, yBound, WINDOWX, WINDOWY);

        setLayout(new BoxLayout((Container) getContentPane(), 
        		BoxLayout.Y_AXIS));

        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .6));//size window
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        
        //Pause Button setup
        pauseButton.setEnabled(true);
        pauseButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		if(paused){
        			paused = false;
        			restPanel.restartAgent();
        			pauseButton.setText("Pause");
        		}
        		else if(!paused){
        			paused = true;
        			restPanel.pauseAgent();
        			pauseButton.setText("Resume");
        		}
        	}
        });
        
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .2));//size window
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);

        //infoPanel.setLayout(new GridLayout(infoRow, infoCol, infoHgap, infoVgap));
        //infoPanel.add(namePanel);
        
        infoLabel = new JLabel(); 
        //infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        
       /* Dimension nDim = new Dimension(WINDOWX, (int) (WINDOWY * .5)); 
        nPanel = new JPanel();
        nPanel.setPreferredSize(nDim);
        nPanel.setMinimumSize(nDim);
        nPanel.setMaximumSize(nDim);
        nPanel.setBorder(BorderFactory.createTitledBorder("Linda Char"));
        myself = new ImageIcon("images/smiley.jpg");
        */
        
        Dimension allDim = new Dimension(WINDOWX, (int) (WINDOWY * .8));//size window
        allPanel.setPreferredSize(allDim);
        allPanel.setMinimumSize(allDim);
        allPanel.setMaximumSize(allDim);
        allPanel.setLayout(new GridLayout(2, 1, infoHgap, infoVgap));
        
        infoPanel.add(infoLabel);
        infoPanel.add(pauseButton);
        infoPanel.add(stateCB);

        allPanel.add(restPanel);
        allPanel.add(infoPanel);
        animationFrame.add(allPanel);
        animationFrame.setVisible(true);
        //nPanel.add(new JLabel(myself));
        //add(infoPanel);
        //add(nPanel);
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

        //System.out.println("person "+person);
        
        if (person instanceof LCustomerRole) {
            LCustomerRole customer = (LCustomerRole) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        
        
        else if (person instanceof LWaiterRole) {
           LWaiterRole waiter = (LWaiterRole) person;
            stateCB.setText("Go on Break");
          //Should checkmark be there? 
            stateCB.setSelected(waiter.isOnBreak());
          //Is waiter on break? Hack. Should ask waiterGui
            stateCB.setEnabled(!waiter.isOnBreak());
          // Hack. Should ask waiterGui
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
            if (currentPerson instanceof LCustomerRole) {
                LCustomerRole c = (LCustomerRole) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
            
            else if (currentPerson instanceof LWaiterRole) {
            	LWaiterRole w = (LWaiterRole) currentPerson;
                w.getGui().setBreak();
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
    public void setCustomerEnabled(LCustomerRole c) {
        if (currentPerson instanceof LCustomerRole) {
            LCustomerRole cust = (LCustomerRole) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    
    public void setWaiterEnabled(LWaiterRole w) {
        if (currentPerson instanceof LWaiterRole) {
            LWaiterRole wait = (LWaiterRole) currentPerson;
            if (w.equals(wait)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        LRestaurantGui gui = new LRestaurantGui();
        gui.setVisible(false);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}