package simcity.Drew_restaurant.gui;

import simcity.PersonAgent;
import simcity.Drew_restaurant.Drew_CookRole;
import simcity.Drew_restaurant.Drew_CustomerRole;
import simcity.Drew_restaurant.Drew_HostRole;
//import simcity.Drew_restaurant.MarketRole;
import simcity.Drew_restaurant.Drew_CashierRole;
import simcity.Drew_restaurant.Drew_WaiterRole;
import simcity.interfaces.*;

import javax.swing.*;

import java.awt.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
    //private HostRole host = new HostRole("HOST");
    
    //private MarketRole market = new MarketRole("MARKET 1");
    //private MarketRole market2 = new MarketRole("MARKET 2");
    //private MarketRole market3 = new MarketRole("MARKET 3");
    
    //private CookRole cook = new CookRole("COOK");

    
    //private CashierRole cashier = new CashierRole("CASHIER");
    
    private Vector<Drew_CustomerRole> customers = new Vector<Drew_CustomerRole>();
    private Vector<Drew_Waiter> waiters = new Vector<Drew_Waiter>();

    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private JPanel group = new JPanel();

    //private RestaurantGui gui; //reference to main gui
    //private CookGui Cgui = new CookGui(cook,gui);
    
   /* public RestaurantPanel(RestaurantGui gui) {
    	int VSpacing=20;
    	int HSpacing=20;
    	int frameRows=1;
    	int frameColumns=2;
    	int custPanelRows=1;
    	int custPanelCols=2;
    	int custPanelVgap=10;
    	int custPanelHgap=10;  
    
        //this.gui = gui;
        
        //host.startThread();
        
        //market.setCashier(cashier);
        //market2.setCashier(cashier);
        //market3.setCashier(cashier);
        
        //market.startThread();	//HACK to make market
        //cook.addMarket(market);
        
        //market2.startThread();
        //cook.addMarket(market2);
        
        //cook.startThread();
        
        //market3.startThread();
        //cook.addMarket(market3);
        
        //cashier.startThread();

        setLayout(new GridLayout(frameRows, frameColumns, HSpacing, VSpacing));
        group.setLayout(new GridLayout(custPanelRows, custPanelCols, custPanelHgap, custPanelVgap));

        group.add(customerPanel);
        group.add(waiterPanel);

        initRestLabel();
        add(restLabel);
        add(group);
        
        //Add Cook GUI
		gui.animationPanel.addGui(Cgui);
		//cook.setGui(Cgui);
    }*/

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
   /* private void initRestLabel() {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>steak</td><td>$15.99</td></tr><tr><td>chicken</td><td>$10.99</td></tr><tr><td>salad</td><td>$5.99</td></tr><tr><td>pizza</td><td>$8.99</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("               "), BorderLayout.EAST);
        restLabel.add(new JLabel("               "), BorderLayout.WEST);
    }

    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
   /* public void showInfo(String type, String name) {

        if (type.equals("Customers")) {

            for (int i = 0; i < customers.size(); i++) {
                CustomerRole temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        
        if (type.equals("Waiters")) {

            for (int i = 0; i < waiters.size(); i++) {
                Waiter temp = waiters.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
    }*/

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    /*public void addPerson(String type, String name) {

    	if (type.equals("Customers")) {
    		CustomerRole c = new CustomerRole(name);	
    		CustomerGui g = new CustomerGui(c, gui, host);

    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setGui(g);
    		customers.add(c);
    		//c.startThread();
    	}
    	if (type.equals("Waiters")) {
    		WaiterRole w = new WaiterRole(name);	
    		WaiterGui g = new WaiterGui(w, gui, host.waiters.size());

    		gui.animationPanel.addGui(g);
            w.setGui(g);
            w.setHost(host);
            w.setCook(cook);
            host.addWaiter(w);
            waiters.add(w);
            //w.startThread();
            w.addCashier(cashier);
    	}
    }*/

}
