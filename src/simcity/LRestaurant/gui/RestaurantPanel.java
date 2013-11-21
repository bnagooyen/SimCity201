package simcity.LRestaurant.gui;

import simcity.LRestaurant.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {
   
//    private HostRole host = new HostRole("Host");
//    private HostGui hostGui = new HostGui(host);
//    
//    private CookAgent cook = new CookAgent("Chef");
//    
//    private CashierAgent cashier = new CashierAgent("Cashier");
//    
//    private MarketAgent market1 = new MarketAgent("Market1");
//    private MarketAgent market2 = new MarketAgent("Market2");
//    private MarketAgent market3 = new MarketAgent("Market3");
//
//    private Vector<CustomerRole> customers = new Vector<CustomerRole>();
//    private Vector<WaiterRole> waiters = new Vector<WaiterRole>();
//
//    private JPanel restLabel = new JPanel();
//    private ListPanel customerPanel = new ListPanel(this, "Customers");
//    private ListPanel WaiterPanel = new ListPanel(this, "Waiters");
//    private JPanel group = new JPanel();
//    
//    private int gXpos = 1;
//    private int gYpos = 2;
//    private int gHgap = 10;
//    private int gVgap = 10;
//
//    private RestaurantGui gui; //reference to main gui

    public RestaurantPanel(RestaurantGui gui) {
//        this.gui = gui;
//
//        gui.animationPanel.addGui(hostGui);
//        host.startThread();
//        host.setGui(hostGui);
//        
//        cook.startThread();
//        
//        CookGui gc = new CookGui(cook, cook.getName(), gui);
//        cook.setGui(gc);
//        gui.animationPanel.addGui(gc);
//        
//        cashier.startThread();
//        
//        market1.startThread();
//        market2.startThread();
//        market3.startThread();
//        
//        market1.setCook(cook);
//        market2.setCook(cook);
//        market3.setCook(cook);
//        
//        market1.setCashier(cashier);
//        market2.setCashier(cashier);
//        market3.setCashier(cashier);
//        
//        cook.addMarket(market1);
//        cook.addMarket(market2);
//        cook.addMarket(market3);
//        
//        //setLayout(new GridLayout(1, 2, 20, 20));
//        setLayout(new BorderLayout());
//        group.setLayout(new GridLayout(gXpos, gYpos, gHgap, gVgap));
//        group.setBorder(BorderFactory.createTitledBorder("Create"));
//
//         group.add(WaiterPanel);
//         group.add(customerPanel);
//       
//        
//        initRestLabel();
//        add(group, BorderLayout.EAST);
//        add(restLabel, BorderLayout.WEST);
        
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
//        JLabel label = new JLabel();
//        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
//        //restLabel.setSize(40, 50);
//        restLabel.setLayout(new BorderLayout());
//        label.setText(
//    		"<html><table><tr><td>Steak</td><td>$15</td></tr><tr><td>Chicken</td><td>$10</td></tr><tr><td>Salad</td><td>$5</td></tr><tr><td>Pizza</td><td>$8</td></tr></table><br></html>");
//
//        //restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
//        restLabel.setBorder(BorderFactory.createTitledBorder("Menu"));
//        restLabel.add(label, BorderLayout.CENTER);
//        restLabel.add(new JLabel("            "), BorderLayout.EAST);
//        restLabel.add(new JLabel("            "), BorderLayout.WEST);
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

//        if (type.equals("Customers")) {
//            for (int i = 0; i < customers.size(); i++) {
//                CustomerAgent temp = customers.get(i);
//                if (temp.getName() == name)
//                    gui.updateInfoPanel(temp);
//            }
//        }
//        else if (type.equals("Waiters")) {
//            for (int i = 0; i < waiters.size(); i++) {
//                WaiterRole temp = waiters.get(i);
//                if (temp.getName() == name)
//                    gui.updateInfoPanel(temp);
//            }
//        }
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name, boolean hungry) {
    	
//    	if (type.equals("Customers")) {
//    		CustomerAgent c = new CustomerAgent(name);	
//    		CustomerGui g = new CustomerGui(c, name,gui);
//    		
//    		if(hungry){
//    			g.setHungry();
//    		}
//
//    		gui.animationPanel.addGui(g);
//    		c.setHost(host);
//    		c.setCashier(cashier);
//    		c.setGui(g);
//    		customers.add(c);
//    		c.startThread();
//    	}
    }
    
public void addWaiter(String type, String name) {
    	
//    	if (type.equals("Waiters")) {
//    		WaiterRole w = new WaiterRole(name);	
//    		WaiterGui gw = new WaiterGui(w, name, gui);
//
//    		gui.animationPanel.addGui(gw);
//    		w.setHost(host);
//    		w.setGui(gw);
//    		w.setCook(cook);
//            w.setHost(host);
//            w.setCashier(cashier);
//    		waiters.add(w);
//    		host.addWaiter(w);
//    		w.startThread();
//    	}
    }
    
    public void pauseAgent(){
//    	cook.pause();
//    	host.pause();
//    	for(CustomerAgent c: customers){
//    		c.pause();
//    	}
//    	for(WaiterRole w: waiters){
//    		w.pause();
//    	}
    }
    
    public void restartAgent(){
//    	cook.restart();
//    	host.restart();
//    	for(CustomerAgent c: customers){
//    		c.restart();
//    	}
//    	for(WaiterRole w: waiters){
//    		w.restart();
//    	}
    }

}