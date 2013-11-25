package simcity.housing.gui;

import simcity.TTRestaurant.TCashierRole;
import simcity.TTRestaurant.TCookRole;
import simcity.TTRestaurant.TCustomerRole;
import simcity.TTRestaurant.THostRole;
import simcity.TTRestaurant.TWaiterRole;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel implements ActionListener {

    //Host, cook, waiters and customers
    //private HostRoleTT host;
    //private HostGuiTT hostGui; 
    //private CookRoleTT cook;
    //private CookGuiTT cookGui; 
    //private CashierRoleTT cashier; 
    
    //creating markets
    //private MarketAgent mart1 = new MarketAgent();
    //private MarketAgent mart2 = new MarketAgent(); 
    //private MarketAgent mart3 = new MarketAgent(); 

    private Vector<TCustomerRole> customers = new Vector<TCustomerRole>();
    private Vector <TWaiterRole> waiters = new Vector<TWaiterRole>();
    
    //buttons
    private JButton pauseButton = new JButton("Pause");
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private TWaiterPanel waiterPanel = new TWaiterPanel(this, "Waiters"); 
    private JPanel group = new JPanel();
    private JLabel label = new JLabel();
    private boolean IsPaused = false; 
	private JTabbedPane tabbedPane = new JTabbedPane();
	
	private int numCust = -2; 
    private RestaurantGui gui; //reference to main gui

    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
        //starting threads for agents
        //host.startThread();
        //cook.startThread();
        //cook.addFood();
        //cashier.addFood();
        //cashier.startThread();
        //mart1.startThread(); 
        //mart2.startThread(); 
        //mart3.startThread(); 
        
        //setting guis
        //cook.setGui(cookGui); 
        //gui.animationPanel.addGui(cookGui);
        
        //setting initial variables for markets and references to the markets for cook
        //mart1.addFood(); 
        //mart2.addFood();
        //mart3.addFood();
        //cook.addMarket(mart1);
        //cook.addMarket(mart2);
        //cook.addMarket(mart3);
        //mart1.setCashier(cashier);
        //mart2.setCashier(cashier); 
        //mart3.setCashier(cashier); 
        
        //layouts
        int row = 1; 
        int column = 2; 
        int horizontalWidth= 20; 
        int verticalWidth = 20;
        setLayout(new GridLayout(row, column, horizontalWidth, verticalWidth));
        group.setLayout(new GridLayout(1, 2, 10, 10));
        initRestLabel();
        tabbedPane.add("Waiters", waiterPanel); 
        tabbedPane.add("Customers", customerPanel);
        pauseButton.addActionListener(this);
        add(pauseButton);
        group.add(tabbedPane);
        add(group);
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        //label.setText(
                //"<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");
        tabbedPane.add("Host", label); 
    }

    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    public void showInfo(String type, String name) {

        if (type.equals("Customers")) {

            for (int i = 0; i < customers.size(); i++) {
                TCustomerRole temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        if (type.equals("Waiters")) {
        	for (int i = 0; i < waiters.size(); i++) {
                TWaiterRole temp = waiters.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
        	}
        }
    }
    

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name) {

    	if (type.equals("Customers")) {
    		/*
    		CustomerRoleTT c = new CustomerRoleTT(name);	
    		CustomerGuiTT g = new CustomerGuiTT(c, gui);
    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setCashier(cashier);
    		c.setGui(g);
    		customers.add(c);
    		if(customerPanel.CB.isSelected()) {
    			c.getGui().setHungry();
    			numCust++; 
    		}

    		if (numCust > 8) {
    			numCust = 1; 
    		}
    		if (numCust > 0) {
    			g.setWaitPosition(numCust);
    		}
    		//c.startThread(); 
    		*/
    	}
    	
    	else if (type.equals("Waiters")) {
    		/**
    		WaiterRoleTT w = new WaiterRoleTT(name);	
    		WaiterGuiTT g = new WaiterGuiTT(w, gui);
    		gui.animationPanel.addGui(g);// dw
    		w.setHost(host);
    		w.setCook(cook);
    		w.setCashier(cashier); 
    		w.setGui(g);
    		waiters.add(w);
    		host.setWaiter(w); 
    		//w.startThread();
    		 */
    	}
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}