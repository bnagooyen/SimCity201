package simcity.housing.gui;


import javax.swing.*;

import simcity.PersonAgent;
import simcity.interfaces.Person;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class HousingPanel extends JPanel implements ActionListener {

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
	private PersonAgent p = new PersonAgent("Person"); 
	
    private Vector<Person> people = new Vector<Person>();
    
    //buttons
    //private JPanel group = new JPanel();
    //private JLabel label = new JLabel();
	
    private HouseGui gui; //reference to main gui

	HomePersonGui pg = new HomePersonGui(p);

    public HousingPanel(HouseGui gui) {
        this.gui = gui;

        gui.animationPanel.addGui(pg);
        p.setGui(pg);
        p.startThread();
        
        //layouts
        int row = 1; 
        int column = 2; 
        int horizontalWidth= 20; 
        int verticalWidth = 20;
        setLayout(new GridLayout(row, column, horizontalWidth, verticalWidth));
        //group.setLayout(new GridLayout(1, 2, 10, 10));
        //add(group);
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    /**
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

        if (type.equals("People")) {

            for (int i = 0; i < people.size(); i++) {
                Person temp = people.get(i);
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
    /**
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
    	//}
    	
    	//else if (type.equals("Waiters")) {
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
    	//}
    //}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}