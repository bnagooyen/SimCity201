package simcity.gui;
import simcity.gui.BuildingPanel;
import simcity.gui.CityPanel;
import simcity.gui.CityView;
import simcity.gui.BuildingListPanel;
import simcity.gui.trace.TracePanel;

import javax.swing.*;

import simcity.DRestaurant.DCustomerRole;
import simcity.DRestaurant.DWaiterRole;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class SimCityGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	//JFrame animationFrame = new JFrame("Restaurant Animation");
	//JFrame simCityFrame = new JFrame("Sim City");
	
	//AnimationPanel animationPanel = new AnimationPanel(this);
	//SimCityAnimationPanel simCityPanel = new SimCityAnimationPanel(this);
	ListPanel addPersonPanel = null;
	
	CityPanel city;
	BuildingListPanel info;
	PersonListPanel cityInfo;
	CityView view;
	//CityControlPanel CP;
	TracePanel tracePanel;
	//GridBagConstraints c = new GridBagConstraints();
	JFrame globalLog = new JFrame("SimCity Log");
	JFrame innerBuildingFrame = new JFrame("InnerBuilding");
	static final int NUMRESTAURANTS = 6;
	static final int NUMBANKS = 2;
	static final int NUMMARKETS =4;
	static final int NUMHOUSES =15;
	static final int NUMAPTS= 12;
	static final int NUMHOMELESSSHELTER = 1;
	public HashMap<String, BuildingPanel> myPanels;
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    public SimCityPanel simcityPanel;
    //private AnimationPanel myRestaurant = new AnimationPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JPanel addingID; //adding stuffs!
    private JLabel myID;
    private JCheckBox stateCB;//part of infoLabel
    private JButton pause;
    
    //added buttons for inventory management/testing
//    private JButton kitchenThresholdInc;
//    private JButton kitchenThresholdDec;
//    private JButton kitchenAmntInc;
//    private JButton kitchenAmntDec;
//    private JButton marketAmntInc;
//    private JButton marketAmntDec;
    
    //private JButton setAmounts;
    
    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public SimCityGui() {
        int WINDOWX = 750;
        int WINDOWY = 750;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
//         simCityFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         simCityFrame.setBounds(50, 25 , WINDOWX+275, 462);
//         simCityFrame.setVisible(true);
//         simCityFrame.setLayout(new BorderLayout());
////     	animationFrame.add(animationPanel); 
//         simCityFrame.add(simCityPanel, BorderLayout.CENTER);
//         addPersonPanel=new ListPanel(restPanel, "Person");
//         
//         JPanel cityPanels = new JPanel();
//         cityPanels.setLayout(new GridLayout(1,1));
//         cityPanels.setMaximumSize(new Dimension((int)(WINDOWX*0.4), (200)));
//         cityPanels.setPreferredSize(new Dimension((int)(WINDOWX*0.4), (200)));
//         cityPanels.setMinimumSize(new Dimension((int)(WINDOWX*0.4), (200)));
//         cityPanels.add(addPersonPanel);
//         
//         simCityFrame.add(cityPanels, BorderLayout.WEST);
//         
//         simCityFrame.validate();
        
       // animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       // animationFrame.setBounds(100+WINDOWX, 50 , WINDOWX+100, WINDOWY+100);
       // animationFrame.setVisible(true);
    	//animationFrame.add(animationPanel); 
//        animationPanel.setBounds(100+WINDOWX, 50 , WINDOWX+100, WINDOWY+100);
//    	
//    	setBounds(50, 50, WINDOWX, WINDOWY);
//
//        setLayout(new BorderLayout());
//    	
//    	//setLayout(new FlowLayout(FlowLayout.LEFT, 5,5));
//
//    	
//        Dimension restDim = new Dimension(WINDOWX, (WINDOWY));
//        
//        JPanel utilitiesPanel= new JPanel();
//        utilitiesPanel.setLayout(new BorderLayout());
//        
//        //restPanel.setPreferredSize(restDim);
//        restPanel.setMinimumSize(restDim);
//        //restPanel.setMaximumSize(restDim);
//        utilitiesPanel.add(restPanel, BorderLayout.CENTER);
//        
//    
//        
//        //JPanel utSubPanel = new JPanel();
//        //utSubPanel.setLayout(new GridLayout(2,1));
//        
//        // Now, setup the info panel
//        Dimension infoDim = new Dimension(WINDOWX*(int)0.3, (int) (WINDOWY * .02));
//        infoPanel = new JPanel();
////        infoPanel.setLayout(new BorderLayout());
//        //infoPanel.setPreferredSize(infoDim);
//        //infoPanel.setMinimumSize(infoDim);
//        //infoPanel.setMaximumSize(infoDim);
//        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
//    
//        //stateCB = new JCheckBox();
//        //stateCB.setVisible(false);
//        //stateCB.addActionListener(this);
//
//        infoPanel.setLayout(new BorderLayout());
//        
//        infoLabel = new JLabel();
//        infoLabel.setText("How to add customers: (Name), (WalletAmount), (Choice), (\"Stay\"/\"Leave\")    |    Example: Doreen, 23.55, Steak, Leave");
//        infoPanel.add(infoLabel, BorderLayout.NORTH);
//        //infoPanel.add(stateCB);
//        
//        pause = new JButton();
//        pause.setText("Pause!");
//        pause.addActionListener(this);
//        infoPanel.add(pause, BorderLayout.EAST);
//        
//        JPanel updateValsPanel = new JPanel();
//        updateValsPanel.setLayout(new GridLayout(3,3));
//  
//        private JButton kitchenThresholdInc;
//        private JButton kitchenThresholdDec;
//        private JButton kitchenAmntInc;
//        private JButton kitchenAmntDec;
//        private JButton marketAmntInc;
//        private JButton marketAmntDec;
        
//        updateValsPanel.add(new JLabel("Update Kitchen Threshold"));
//        kitchenThresholdInc= new JButton("+");
//        kitchenThresholdInc.addActionListener(this);
//        updateValsPanel.add(kitchenThresholdInc);
//        kitchenThresholdDec= new JButton("-");
//        kitchenThresholdDec.addActionListener(this);
//        updateValsPanel.add(kitchenThresholdDec);
//        
//        updateValsPanel.add(new JLabel("Update Kitchen Amount"));
//        kitchenAmntInc= new JButton("+");
//        kitchenAmntInc.addActionListener(this);
//        updateValsPanel.add(kitchenAmntInc);
//        kitchenAmntDec= new JButton("-");
//        kitchenAmntDec.addActionListener(this);
//        updateValsPanel.add(kitchenAmntDec);
//        
//        updateValsPanel.add(new JLabel("Update Market Amount"));
//        marketAmntInc= new JButton("+");
//        marketAmntInc.addActionListener(this);
//        updateValsPanel.add(marketAmntInc);
//        marketAmntDec= new JButton("-");
//        marketAmntDec.addActionListener(this);
//        updateValsPanel.add(marketAmntDec);
//        
//        infoPanel.add(updateValsPanel, BorderLayout.WEST);
        
//        setAmounts= new JButton("SET");
//        setAmounts.addActionListener(this);
//        infoPanel.add(setAmounts,BorderLayout.CENTER);
//        
        
        
        
        
        // add(infoPanel, BorderLayout.CENTER);
        
        //utilitiesPanel.add(utSubPanel);
//        utilitiesPanel.add(infoPanel, BorderLayout.SOUTH);
//        add(utilitiesPanel, BorderLayout.NORTH);
//        
//        add(animationPanel, BorderLayout.CENTER);
//        
        /*
        
        */
        //my panel
        /* lab1
         
        
        addingID = new JPanel();
        addingID.setPreferredSize(infoDim);
        addingID.setMinimumSize(infoDim);
        addingID.setMaximumSize(infoDim);
        addingID.setBorder(BorderFactory.createTitledBorder("MyInfo"));

        addingID.setLayout(new GridLayout(1, 2, 30, 0));
        
        myID = new JLabel(); 
        myID.setText("Doreen Hakimi!");
        addingID.add(myID);
        add(addingID);
	*/
        
        setBounds(20, 0, 875, 444);
		tracePanel = new TracePanel();
		//tracePanel.setPreferredSize(new Dimension(CP.getPreferredSize().width, (int)(1.4*CP.getPreferredSize().height)));
		tracePanel.showAlertsForAllLevels();
		tracePanel.showAlertsForAllTags();

		//info = new TListPanel(this);
		
		
		globalLog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		globalLog.setBounds(900,0,350, 735);
		globalLog.setResizable(true);
		globalLog.add(tracePanel);
		globalLog.pack();
		globalLog.setVisible(false);
		
		myPanels = new HashMap<String, BuildingPanel>();
		myPanels.clear();
		
		city = new CityPanel(this);

		
		
		for(int i=1; i<=NUMRESTAURANTS; i++) {
			String name = "Restaurant "+ Integer.toString(i);
			myPanels.put(name, new BuildingPanel(this, name));
			//System.err.println(name);
		}
		
		for(int i=1; i<=NUMMARKETS; i++) {
			String name = "Market "+ Integer.toString(i);
			myPanels.put(name, new BuildingPanel(this, name));
		}

		for(int i=1; i<=NUMBANKS; i++) {
			String name = "Bank "+ Integer.toString(i);
			myPanels.put(name, new BuildingPanel(this, name));
		}
		
		for(int i=1; i<=NUMAPTS; i++) {
			String name = "Apartment "+ Integer.toString(i);
			myPanels.put(name, new BuildingPanel(this, name));
		}
		
		for(int i=1; i<=NUMHOUSES; i++) {
			String name = "House "+ Integer.toString(i);
			myPanels.put(name, new BuildingPanel(this, name));
			//System.err.println(name);
		}
		
		
		String name = "Homeless Shelter";
		myPanels.put(name, new BuildingPanel(this, name));
		//System.err.println(name);
		
		view = new CityView(this, myPanels);
		
		innerBuildingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		innerBuildingFrame.setBounds(20, 430, 875, 444);
		//innerBuildingFrame.setBounds(0,444,740, 300);
		innerBuildingFrame.setResizable(false);
		//innerBuildingFrame.add(info, BorderLayout.WEST);
		innerBuildingFrame.add(view);

		//innerBuildingFrame.add(info);
		//innerBuildingFrame.add(view);
		innerBuildingFrame.pack();
		innerBuildingFrame.setVisible(true);
		
		this.setLayout(new BorderLayout());
		
		//cityInfo = new TListPanel(this, "Person");
		
		cityInfo  = new PersonListPanel(this);
		
		add(cityInfo, BorderLayout.WEST);
//		c.gridx = 0; c.gridy = 0;
//		c.gridwidth = 6; c.gridheight = 6;
		add(city, BorderLayout.CENTER);
		
		 simcityPanel = new SimCityPanel(this);
        
        
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

        if (person instanceof DCustomerRole) {
            DCustomerRole customer = (DCustomerRole) person;
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
    @Override
	public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stateCB) {
            if (currentPerson instanceof DCustomerRole) {
                DCustomerRole c = (DCustomerRole) currentPerson;
                //c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
        }
//        if(e.getSource()== pause) {
//        	restPanel.msgTogglePause();
//        }
//        if(e.getSource()==)
//      private JButton kitchenThresholdInc;
//      private JButton kitchenThresholdDec;
//      private JButton kitchenAmntInc;
//      private JButton kitchenAmntDec;
//      private JButton marketAmntInc;
//      private JButton marketAmntDec;
//        if(e.getSource()==kitchenThresholdInc) {
//        	restPanel.msgIncreaseKitchenThreshold();
//        }
//        if(e.getSource()==kitchenThresholdDec) {
//        	restPanel.msgDecreaseKitchenThreshold();
//        }
//        if(e.getSource()==kitchenAmntInc) {
//        	restPanel.msgIncreaseKitchenAmount();
//        }
//        if(e.getSource()==kitchenAmntDec) {
//        	restPanel.msgDecreaseKitchenAmount();
//        }
//        if(e.getSource()==marketAmntInc) {
//        	restPanel.msgIncreaseMarketAmount();
//        }
//        if(e.getSource()==marketAmntDec) {
//        	restPanel.msgDecreaseMarketAmount();
//        }
//        
//        if(e.getSource()==setAmounts) {
//        	kitchenThresholdInc.setEnabled(false);
//        	kitchenThresholdDec.setEnabled(false);
//        	kitchenAmntDec.setEnabled(false);
//        	kitchenAmntInc.setEnabled(false);
//        	marketAmntDec.setEnabled(false);
//        	marketAmntInc.setEnabled(false);
//        	setAmounts.setEnabled(false);
//        	restPanel.msgInventoryValsSet();
//        }
//        
        
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
//     */
//    public void setCustomerEnabled(CustomerRole c) {
//       /* if (currentPerson instanceof CustomerAgent) {
//            CustomerAgent cust = (CustomerAgent) currentPerson;
//            if (c.equals(cust)) {
//                stateCB.setEnabled(true);
//                stateCB.setSelected(false);
//            }
//        }*/
//    		restPanel.setCustomerEnabled(c.getText(), c.getWallet());
//    		
//    }
//    
//    public void setWaiterEnabled(WaiterRole w) {
//    	restPanel.setWaiterEnabled(w.getName());
//    }
//    
//    public void setWaiterDisabled(WaiterRole w) {
//    	restPanel.setWaiterDisabled(w.getName());
//    }
//    
//    public void setWaiterToBreak(WaiterRole w) {
//    	restPanel.setWaiterToBreak(w.getName());
//    }
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        SimCityGui gui = new SimCityGui();
        gui.setTitle("csci201 Restaurant");
        gui.setResizable(false);
        gui.pack();
        gui.setVisible(true);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
