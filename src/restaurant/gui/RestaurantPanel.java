package restaurant.gui;

import restaurant.CustomerRole;
import simcity.PersonAgent;
import simcity.PersonGui;
import restaurant.CashierRole;
import restaurant.CookRole;
import restaurant.HostRole;
import restaurant.CookRole;
//import restaurant.MarketAgent;
import restaurant.CashierRole;
import restaurant.WaiterRole;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
	private PersonAgent host = new PersonAgent("Host");
    private HostRole h = new HostRole(host);
    //private HostGui hostGui = new HostGui(host);
    
    private PersonAgent c1 = new PersonAgent("c1");
    
    private PersonAgent waiter = new PersonAgent("Waiter");
    private WaiterRole w = new WaiterRole(waiter);
    
    private PersonAgent cook = new PersonAgent("Cook");
    private CookRole c = new CookRole(cook);   
    
    private PersonAgent cashier = new PersonAgent("Cashier");
    private CashierRole cash = new CashierRole(cashier);
    
    private Vector<PersonAgent> people = new Vector<PersonAgent>();
    //private CookGui cookGui = new CookGui(cook);
       
    private int waiterIndex = 1;
    private Vector<WaiterRole> waiters = new Vector<WaiterRole>();
    private Vector<CustomerRole> customers = new Vector<CustomerRole>();
    
    private final int numMarkets = 3;
//    private Vector<MarketAgent> markets = new Vector<MarketAgent>();
    
    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters");

    private RestaurantGui gui;

    

    public RestaurantPanel(RestaurantGui gui) {
        //this.gui = gui;

        
        c1.startThread();
        
        host.addRole(h);
        h.isActive = true;
        host.startThread();
        c1.setHost(host.getHostRole());

        waiter.addRole(w);
        w.isActive = true;
        waiter.startThread();
        
        cook.addRole(c);
        c.isActive = true;
        cook.startThread();
        
        cashier.addRole(cash);
        cash.isActive = true;
        cashier.startThread();
        
        c1.gotHungry();

    }
        //host.setGui(hostGui);
        //waiter.setGui(waiterGui);
//        cashier.startThread();
        
        //waiter.startThread();
//        host.startThread();

        
        //cook.msgAddWaiter(waiter);
        
        //waiter.msgAddCook(cook);
        //waiter.msgAddHost(host);
        //need to loop this once have multiple
        
        
       // gui.animationPanel.addGui(waiterGui);
        
//        setLayout(new GridLayout(1,3));
//        //group.setLayout(new GridLayout(1, 2));
//
//        add(customerPanel);
//        add(waiterPanel);
//
//        initRestLabel();
//        add(restLabel);
        
        //add markets and start their threads
//        for(int i=1; i<=numMarkets; i++) {
//        	MarketAgent myMarket = new MarketAgent(i);
//        	cook.msgAddMarket(myMarket);
//        	myMarket.msgAddCook(cook);
//        	markets.add(myMarket);
//        	myMarket.startThread();
//        }
        
        
        
        //cook.AddHost(host);
        //cook.AddCashier(cashier);
        
//        cook.startThread();
        
//        personGui=new PersonGui(person, gui);
//        person.setGui(personGui);
//        gui.simCityPanel.addGui(personGui);
//        person.startThread();

       // add(group);
//    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
     
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");
        
        restLabel.add(label, BorderLayout.CENTER);
   
    }

    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    public Object showInfo(String type, String name) {
//
//        if (type.equals("Customers")) {
//
//            for (int i = 0; i < customers.size(); i++) {
//                CustomerRole temp = customers.get(i);
//                if (temp.getText() == name) {
//                	return temp;
//                }
//            }
//        }
//        
//        if (type.equals("Waiters")) {
//
//            for (int i = 0; i < waiters.size(); i++) {
//                WaiterRole temp = waiters.get(i);
//                if (temp.getName() == name)
//                    return temp;
//            }
//        }
//        
//        if (type.equals("Person")) {
//
//            for (int i = 0; i < people.size(); i++) {
//                PersonAgent temp = people.get(i);
//                if (temp.getName() == name) {
//                	return temp;
//                }
//            }
//        }
//        
//        
        return new Object();
    }

//    public void msgTogglePause() {
//    	if(host.isPaused) {
//    		host.isPaused=false;
//    		host.Restart();
//    	}
//    	else host.isPaused=true;
//    	for(WaiterAgent waiter: waiters) {
//	    	if(waiter.isPaused) {
//	    		waiter.isPaused=false;
//	    		waiter.Restart();
//	    	}
//	    	else waiter.isPaused=true;
//    	}
//    	for(CustomerAgent customer: customers) {
//    		if(customer.isPaused) {
//    			customer.isPaused=false;
//    			customer.Restart();
//    		}
//    		else customer.isPaused=true;
//    	}
//    	
//    	if(cook.isPaused) {
//    		cook.isPaused=false;
//    		cook.Restart();
//    	}
//    	else cook.isPaused=true;
//   }
    /*
     *         if(e.getSource()==kitchenThresholdInc) {
        	restPanel.msgIncreaseKitchenThreshold();
        }
        if(e.getSource()==kitchenThresholdDec) {
        	restPanel.msgDecreaseKitchenThreshold();
        }
        if(e.getSource()==kitchenAmntInc) {
        	restPanel.msgIncreaseKitchenAmount();
        }
        if(e.getSource()==kitchenAmountDec) {
        	restPanel.msgDecreaseKitchenAmount();
        }
        if(e.getSource()==marketAmntInc) {
        	restPanel.msgIncreaseMarketAmount();
        }
        if(e.getSource()==kitchenAmountDec) {
        	restPanel.msgDecreaseMarketAmount();
        }
     * 
     */
    
//<<<<<<< HEAD
//    public void msgInventoryValsSet() {
//    	cook.msgCheckInventoryValsForOpen();
//    }
//    
//    public void msgIncreaseKitchenThreshold() {
//    	cook.msgIncKitchenThreshold();
//    }
//    
//    public void msgDecreaseKitchenThreshold() {
//    	cook.msgDecKitchenThreshold();
//    }
//    
//    public void msgIncreaseKitchenAmount() {
//    	cook.msgIncKitchenAmnt();
//    }
//    
//    public void msgDecreaseKitchenAmount() {
//    	cook.msgDecKitchenAmnt();
//    }
//=======
//    public void msgInventoryValsSet() {
//    	cook.msgCheckInventoryValsForOpen();
//    }
//    
//    public void msgIncreaseKitchenThreshold() {
//    	cook.msgIncKitchenThreshold();
//    }
//    
//    public void msgDecreaseKitchenThreshold() {
//    	cook.msgDecKitchenThreshold();
//    }
//    
//    public void msgIncreaseKitchenAmount() {
//    	cook.msgIncKitchenAmnt();
//    }
//    
//    public void msgDecreaseKitchenAmount() {
//    	cook.msgDecKitchenAmnt();
//    }
//    public void msgIncreaseMarketAmount() {
//    	for(MarketAgent market: markets) {
//    		market.msgIncMarketAmnt();
//    	}
//    }
//    
//    public void msgDecreaseMarketAmount() {
//    	for(MarketAgent market: markets) {
//    		market.msgDecMarketAmnt();
//    	}
//    }
//>>>>>>> d91644cc014bfaf6ae424eeaee615c251913ed82
    
    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    
    public void addPerson(String type, String name) {

//    	if (type.equals("Customers")) {
//    		PersonAgent p = new PersonAgent(name);
//    		//CustomerRole c = new CustomerRole(p, host);	
//    		CustomerGui g = new CustomerGui(c, gui);
//
//   		gui.animationPanel.addGui(g);// dw
//    		//c.setHost(host);
//    		c.setCashier(cashier);
//    		c.setGui(g);
//    		customers.add(c);
//    		//c.startThread();
//    		System.out.println("added");
//    	}
//    	
//    	if(type.equals("Waiters")) {
//    		PersonAgent p = new PersonAgent(name);
//    		WaiterRole w = new WaiterRole(p);
//    		WaiterGui g= new WaiterGui(w, gui, waiterIndex);
//    		waiterIndex++;
//    		
//    		gui.animationPanel.addGui(g);
//    		w.setGui(g);
//    		
//    		
//    		//w.msgAddCook(cook);
//          //  w.msgAddHost(host);
//          //  w.msgAddCashier(cashier);
//           // host.msgAddWaiter(w);
//            waiters.add(w);
//    		
//    		
//    	}
//    	
//    	if(type.equals("Person")) {
//    		PersonAgent p = new PersonAgent(name);
//    		PersonGui g = new PersonGui(p, gui);
//    		p.setGui(g);
//    		gui.simCityPanel.addGui(g);
//    		p.startThread();
//    		people.add(p);
//    	}
    }
    
    public void setCustomerEnabled(String name, double val) {
    	
    	for(CustomerRole c: customers) {
    		if(c.getText()==name) {
    			String s[] = name.split(", ");
    			String temp = s[0].trim()+", "+ String.valueOf(val)+ ", "+ s[2].trim()+", "+s[3].trim();
    			c.setText(temp);
    			customerPanel.setCustomerEnabled(name, temp);
    		}
    	}
    	
    }
    
    public void setWaiterEnabled(String name) {
    	waiterPanel.setWaiterEnabled(name);
    }
    
    public void setWaiterDisabled(String name) {
    	waiterPanel.setWaiterDisabled(name);
    }
    
    public void setWaiterToBreak(String name) {
    	waiterPanel.setWaiterToBreak(name);
    }

}
