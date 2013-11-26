package simcity.Drew_restaurant.gui;

import simcity.Drew_restaurant.Drew_CustomerRole;
import simcity.Drew_restaurant.interfaces.*;
import agent.Agent;

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
	JFrame animationFrame = new JFrame("Restaurant Animation");
	//JFrame panelFrame = new JFrame("Panel");
	public AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    //private RestaurantPanel restPanel = new RestaurantPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */
    
    
    //Creates Pause Button
    private JButton pauseButton;

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
        int windowXSize=650;		//Sets window size
        int windowYSize=350;
        int xStart=250;				//Sets starting spot for panel
        int yStart=450;
        
        
        double restSizeMod= 0.65;	//Sets Vertical size of panels relative to Frame
        double infoSizeMod= 0.2;
        
        int infoRows=1;		//Set up grid Layout for info panel
        int infoCols=2;
        int infoHgap=30;
        int infoVgap=0;
        
        int GUIWINDOWX = windowXSize;		//Sets X Size for GUI frame
        int GUIWINDOWY = windowYSize;		//Sets Y Size for GUI frame

        int GUIStartX=xStart;			//Set starting X pos for GUI frame
        int GUIStartY=yStart;			//Set starting Y pos for GUI frame
        
        animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        animationFrame.setBounds(GUIStartX+100, 20 , animationPanel.getWidth(), animationPanel.getHeight());
        animationFrame.setVisible(true);
        animationFrame.setResizable(false);
            animationFrame.add(animationPanel); 
    	
       // add(animationPanel);	//Add animation to restaurant panel
    	
    	setBounds(GUIStartX, GUIStartY, GUIWINDOWX, GUIWINDOWY);

        setLayout(new BoxLayout((Container) getContentPane(), 
        		BoxLayout.Y_AXIS));
        
        Dimension restDim = new Dimension(GUIWINDOWX, (int) (GUIWINDOWY * restSizeMod));
      /*  restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);					Restaurant Panel Setup!!!!!!!!!!!!!!!!!!!!!!
        restPanel.setMaximumSize(restDim);
        add(restPanel);*/
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension(GUIWINDOWX, (int) (GUIWINDOWY * infoSizeMod));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);

        infoPanel.setLayout(new GridLayout(infoRows, infoCols, infoHgap, infoVgap));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        add(infoPanel);
        
        
        //Set Up Pause Button
        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(this);
        
        add(pauseButton);
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

        if (person instanceof Drew_CustomerRole) {
            Drew_CustomerRole customer = (Drew_CustomerRole) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
            if(customer.getName().trim().equals(""))stateCB.setEnabled(false);
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        
        if (person instanceof Drew_Waiter) {
            Drew_Waiter waiter = (Drew_Waiter) person;
            if(waiter.getGui().wantsBreak()){
            	stateCB.setEnabled(false);
            	stateCB.setText("Waiting on Host");
            }
            else stateCB.setEnabled(true);
            if(!waiter.getGui().onBreak()) stateCB.setText("Go on break?");
            else stateCB.setText("Done With break?");
          //Should checkmark be there? 
            stateCB.setSelected(waiter.getGui().onBreak());
          //Is customer hungry? Hack. Should ask customerGui
            //stateCB.setEnabled(!waiter.getGui().wantsBreak());
            if(waiter.getName().trim().equals(""))stateCB.setEnabled(false);
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
            if (currentPerson instanceof Drew_CustomerRole) {
                Drew_CustomerRole c = (Drew_CustomerRole) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
            else if (currentPerson instanceof Drew_Waiter) {
                Drew_Waiter w = (Drew_Waiter) currentPerson;
                if(!w.getGui().onBreak() && !w.getGui().wantsBreak()){
                	w.getGui().setWantsBreak(true);
                    //stateCB.setText("Done with break?");
                }
                else if(!w.getGui().wantsBreak()){
                	w.getGui().setOnBreak(false);
                    stateCB.setText("Go on break?");
                }
            }
        }
        /*if (e.getSource() == pauseButton && !Agent.P) {
        	Agent.P=true;
        	pauseButton.setText("Resume");
        }
        else if (e.getSource() == pauseButton && Agent.P) {
        	Agent.P=false;
        	pauseButton.setText("Pause");*/
        //}
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(Drew_CustomerRole c) {
        if (currentPerson instanceof Drew_CustomerRole) {
            Drew_CustomerRole cust = (Drew_CustomerRole) currentPerson;
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
