package simcity.BRestaurant.gui;



import simcity.PersonAgent;
import simcity.interfaces.*;
import simcity.BRestaurant.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.List;
import java.util.ArrayList;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class BRestaurantPanel extends JPanel {
	
	private List<BMarketRole> markets = new ArrayList<BMarketRole>();
	
	private BWaiterRole waiter = new BWaiterRole("Sarah");
	private BHostRole host = new BHostRole("Cool Guy");
	private BCookRole cook= new BCookRole("Cook guy", markets);
	
    private BCashierRole cashier = new BCashierRole();
	


	private Vector<BCustomerRole> customers = new Vector<BCustomerRole>();
	public Vector<BWaiterRole> waiters = new Vector<BWaiterRole>();

	private JPanel restLabel = new JPanel();
	private BListPanel customerPanel = new BListPanel(this, "Customers");
	private BListPanel waiterPanel=new BListPanel(this, "Waiters");

	private JPanel group = new JPanel();

	private BRestaurantGui gui; //reference to main gui

	public BRestaurantPanel(BRestaurantGui gui) {
		this.gui = gui;
		
		
	
		//cashier.startThread();
		
		BMarketRole market1 = new BMarketRole("market1");
		market1.startThread();
		market1.setCashier(cashier);
		markets.add(market1);

		BMarketRole market2 = new BMarketRole("market2");
		market2.startThread();
		market2.setCashier(cashier);
		markets.add(market2);

		BMarketRole market3 = new BMarketRole("market3");
		market3.startThread();
		market3.setCashier(cashier);
		markets.add(market3);
		
		
		

		waiter.startThread();
		

		cook.startThread();
		host.startThread();

		setLayout(new GridLayout(1, 2, 20, 20));
		group.setLayout(new GridLayout(1, 2, 10, 10));

		group.add(customerPanel);
		group.add(waiterPanel);

		initRestLabel();
		add(restLabel);
		add(group);
	}

	/**
	 * Sets up the restaurant label that includes the menu,
	 * and host and cook information
	 */
	private void initRestLabel() {
		JLabel label = new JLabel();
		//restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
		restLabel.setLayout(new BorderLayout());
		label.setText(
				"<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + waiter.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

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

		if (type.equals("Customers")) {

			for (int i = 0; i < customers.size(); i++) {
				BCustomerRole temp = customers.get(i);
				if (temp.getName() == name)
					gui.updateInfoPanel(temp);
			}
		}

		if (type.equals("Waiters")) {

			for (int i = 0; i < waiters.size(); i++) {
				BWaiterRole temp = waiters.get(i);
				if (temp.getName() == name)
					gui.updateInfoPanel(temp);
			}
		}
	}



	public BWaiterRole getWaiter(){
		return waiter;
	}

	public BCookRole getCook(){
		return cook;
	}

	public void addPerson(String type, String name, boolean isHungry) {

		if (type.equals("Customers")) {
			BCustomerRole c = new BCustomerRole(name);	
			BCustomerGui g = new BCustomerGui(c, gui);

			if (isHungry==true)
				g.setHungry();

			gui.animationPanel.addGui(g);// dw
			c.setHost(host);
			
			c.setCashier(cashier);
			c.setGui(g);
			customers.add(c);
			c.startThread();
		}
	}

	public void addWaiter(String type, String name, int counter) {

		
		if (type.equals("Waiters")) {
			BWaiterRole w = new BWaiterRole(name);	
			BHostGui g = new BHostGui(w, gui, counter);


			gui.animationPanel.addGui(g);
 
			w.setHost(host);
			host.setWaiter(w);
			w.setCook(cook);
			w.setCashier(cashier);
			w.setGui(g);
			waiters.add(w);
			w.startThread();
		}
	}

}