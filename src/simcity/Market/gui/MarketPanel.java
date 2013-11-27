package simcity.Market.gui;


import simcity.Market.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.*;


/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class MarketPanel extends JPanel {

	// animation
    static int gridX = 20;
    static int gridY = 15;
	
    Semaphore[][] grid = new Semaphore[gridX+1][gridY+1]; 
    //Table[] tables = new Table[gridX * gridY];


    //Host, cook, waiters and customers
    private MarketGui gui; //reference to main gui

    
    private JPanel restLabel = new JPanel();
    private JPanel group = new JPanel();

    private int numWaiters;
    

    public MarketPanel(MarketGui gui) {
//    	 MarketManagerRole m = new MarketManagerRole("M");
//         MManagerGui mg = new MManagerGui(m);
//         m.setGui(mg);
//         gui.animationPanel.addGui(mg);
//    	MarketCashierRole r = new MarketCashierRole("C");
//    	MCashierGui g = new MCashierGui(r);
//        r.setMarketManager(m);
//        r.setGui(g);
//        gui.animationPanel.addGui(g);
//       
//
//        InventoryBoyRole b = new InventoryBoyRole("ib");
//        IBGui bg = new IBGui(b);
//        b.setGui(bg);
//        b.setMarketCashier(r);
//        b.setMarketManager(m);
//        gui.animationPanel.addGui(bg);
//        
//        r.setInventoryBoy(b);
//        
//        m.startThread();
//        r.startThread();
//        b.startThread();
//        
//        MarketCustomerRole c = new MarketCustomerRole("Cust");
//        MCustomerGui cg = new MCustomerGui(c);
//        c.setGui(cg);
//        c.setMarketManager(m);
//        gui.animationPanel.addGui(cg);
//        c.populateOrderList("Pizza", 1);
//        c.populateOrderList("Chicken", 1);
//        
//        MarketCustomerRole c2 = new MarketCustomerRole("Cust2");
//        MCustomerGui cg2 = new MCustomerGui(c2);
//        c2.setGui(cg2);
//        c2.setMarketManager(m);
//        gui.animationPanel.addGui(cg2);
//        c2.populateOrderList("Steak", 1);
//        c2.populateOrderList("Chicken", 1);
//        
//        setLayout(new BorderLayout(5,0));
//        
//        initRestLabel();
//        add(restLabel, BorderLayout.WEST);
//        
//    
//        c.startThread();
//        c2.startThread();
    }
    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + "Power Rangers" + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

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
    public void showInfo(String type, String name) {

      
        
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name, boolean hungry) {

    }
    
    public void addWaiter(String type, String name) {
    	
    	
    }
  

}
