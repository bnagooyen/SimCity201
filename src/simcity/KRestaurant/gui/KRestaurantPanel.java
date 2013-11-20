package simcity.KRestaurant.gui;


import simcity.KRestaurant.*;

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
public class KRestaurantPanel extends JPanel {

	// animation
    static int gridX = 20;
    static int gridY = 15;
	
    Semaphore[][] grid = new Semaphore[gridX+1][gridY+1]; 
    //Table[] tables = new Table[gridX * gridY];


    //Host, cook, waiters and customers
    private KRestaurantGui gui; //reference to main gui

//    private KHostAgent host = new KHostAgent("Host", gui);;
    private KCookGui cookGui;
    private KCookRole cook;

//    private KCashierAgent cashier = new KCashierAgent("Cashier");
//    private MarketAgent market1 = new MarketAgent("market 1");
//    private MarketAgent market2 =new MarketAgent("market 2");
//    private MarketAgent market3 =new MarketAgent("market 3");
    
    private Vector<KCustomerRole> customers = new Vector<KCustomerRole>();
    private Vector<KWaiterRole> waiters = new Vector<KWaiterRole>();
    
    private JPanel restLabel = new JPanel();
    private KListPanel customerPanel = new KListPanel(this, "Customers");
    private JPanel group = new JPanel();

    private int numWaiters;
    

    public KRestaurantPanel(KRestaurantGui gui) {
    	
    	//intialize the semaphore grid
        for (int i=0; i<gridX+1 ; i++)
            for (int j = 0; j<gridY+1; j++)
                grid[i][j]=new Semaphore(1,true);
        try {
            //make the 0-th row and column unavailable
            System.out.println("making row 0 and col 0 unavailable.");
            for (int i=0; i<gridY+1; i++) grid[0][0+i].acquire();
            for (int i=1; i<gridX+1; i++) grid[0+i][0].acquire();
            System.out.println("adding wait area");
            for (int i=0; i<13; i++) grid[2][2+i].acquire();
            System.out.println("adding counter area");
            for (int i=0; i<13; i++) grid[17][2+i].acquire();
            System.out.println("adding grill area");
            for (int i=0; i<10; i++) grid[19][3+i].acquire();
            //Let's just put the four static tables in for now
            System.out.println("adding table 1");
         
            for (int i=0; i<3; i++)
                for (int j=0; j<3; j++)
                    grid[5+i][3+j].acquire();// because grid is 0-based
            System.out.println("adding table 2");
            for (int i=0; i<3; i++)
                for (int j=0; j<3; j++)
                    grid[5+i][8+j].acquire();// because grid is 0-based
            System.out.println("adding table 3");
            
            for (int i=0; i<3; i++)
                for (int j=0; j<3; j++)
                    grid[10+i][3+j].acquire();// because grid is 0-based
            System.out.println("adding table 4");
           
            for (int i=0; i<3; i++)
                for (int j=0; j<3; j++)
                    grid[10+i][8+j].acquire();// because grid is 0-based
        }catch (Exception e) {
            System.out.println("Unexpected exception caught in during setup:"+ e);
        }
    	
//    	cook = new KCookRole("mr. cook", gui);
//        this.gui = gui;
//        cookGui = new KCookGui(cook, gui);
//        gui.animationPanel.addGui(cookGui);
//        cook.setGui(cookGui);
//        numWaiters = 0;
        
//        host.setCook(cook);
//        cook.setCashier(cashier);
//        
//        host.startThread();
//        cook.startThread();
//        cashier.startThread();
//        market1.startThread();
//        market2.startThread();
//        market3.startThread();
//        
//        cook.addMarket(market1);
//        cook.addMarket(market2);
//        cook.addMarket(market3);
//        
//        market1.setCook(cook);
//        market2.setCook(cook);
//        market3.setCook(cook);
//        
//        market1.setCashier(cashier);
//        market2.setCashier(cashier);
//        market3.setCashier(cashier);
        
        setLayout(new BorderLayout(5,0));
        
        initRestLabel();
        add(restLabel, BorderLayout.WEST);
        add(customerPanel, BorderLayout.CENTER);
    }
    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
//        JLabel label = new JLabel();
//        restLabel.setLayout(new BorderLayout());
//        label.setText(
//                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");
//
//        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
//        restLabel.add(label, BorderLayout.CENTER);
//        restLabel.add(new JLabel("               "), BorderLayout.EAST);
//        restLabel.add(new JLabel("               "), BorderLayout.WEST);
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
                KCustomerRole temp = customers.get(i);
                if (temp.getName() == name)
                {
                    gui.updateInfoPanel(temp);	
                }
            }
        }
        if (type.equals("Waiter")) {
        	for (int i = 0; i < waiters.size(); i++) {
                KWaiterRole temp = waiters.get(i);
                if (temp.getName() == name)
                {
                    gui.updateInfoPanel(temp);
                }
            }
        }
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name, boolean hungry) {
//
//    	if (type.equals("Customers")) {
//    		KCustomerAgent c = new KCustomerAgent(name, gui, hungry, cashier);	
//    		KCustomerGui g = new KCustomerGui(c, gui);
//    		
//    		gui.animationPanel.addGui(g);
//    		c.setHost(host);
//    		c.setGui(g);
//    		customers.add(c);
//    		c.startThread();
//    		if(hungry){
//    			c.getGui().setHungry();
//    		}
//    	}
     }
//    
    public void addWaiter(String type, String name) {
//    	if (type.equals("Waiter")) {
//    		Random rand = new Random();
//    		int choice = rand.nextInt(2);
//    		KWaiterAgent w = null;
//    		//System.out.println("made a waiter");
//    		if(choice == 0) {
//    			w = new KWaiterNormal(name, cook, gui, host, cashier, numWaiters);
//    			System.out.println("made a normal waiter");
//    		}
//    		else{
//    			w = new KWaiterSharedData(name, cook, gui, host, cashier, numWaiters);
//    			System.out.println("made a shared data waiter");
//
//    		}
//	    	KWaiterGui g = new KWaiterGui(w, numWaiters);
//	    	numWaiters++;
//	    	
//            //AStarTraversal aStarTraversal = new AStarTraversal(grid);
//
//	    	
//	    	gui.animationPanel.addGui(g);
//	    	host.addWaiter(w);
//	    	w.setGui(g);
//	    	w.startThread();
//	    	waiters.add(w);
//    	}
    }
}
