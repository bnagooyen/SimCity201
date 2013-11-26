package simcity.Bank.gui;



import javax.swing.*;

import Bank.BankCustomerRole;
import Bank.BankLoanOfficerRole;
import Bank.BankManagerRole;
import Bank.BankTellerRole;
import simcity.interfaces.BankCustomer;
import simcity.interfaces.BankLoanOfficer;
import simcity.interfaces.BankTeller;

import java.awt.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class BankPanel extends JPanel {
	
	private BankTellerRole bankteller = new BankTellerRole();
	private BankLoanOfficerRole loanOfficer = new BankLoanOfficerRole();
	private BankManagerRole bankmanager = new BankManagerRole();
	private BankCustomerRole bankcustomer = new BankCustomerRole();
    
    private Vector<BankCustomer> customers = new Vector<BankCustomer>();
    private Vector<BankTeller> tellers = new Vector<BankTeller>();
    private Vector<BankLoanOfficer> loanOfficers = new Vector<BankLoanOfficer>();

    private JPanel restLabel = new JPanel();
    //private ListPanel BankTellersPanel = new ListPanel(this, "Customers");
    //private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private JPanel group = new JPanel();

    private BankGui gui; //reference to main gui
    private BankTellerGui Bgui= new BankTellerGui(bankteller,gui, bankmanager);
    private BankManagerGui ManGui= new BankManagerGui(bankmanager, gui);
    private BankLoanGui LOGui= new BankLoanGui(loanOfficer, gui, bankmanager);
    private BankCustomerGui BCGui= new BankCustomerGui(bankcustomer, gui, bankmanager);

   
    public BankPanel(BankGui gui) {
    
        this.gui = gui;

        tellers.add(bankteller);
        customers.add(bankcustomer);
        
		gui.animationPanel.addGui(Bgui);
		gui.animationPanel.addGui(ManGui);
		gui.animationPanel.addGui(LOGui);
		gui.animationPanel.addGui(BCGui);
		bankmanager.setGui(ManGui);
		bankteller.setGui(Bgui);
		loanOfficer.setGui(LOGui);
		bankcustomer.setGui(BCGui);
		
		bankcustomer.manager=bankmanager;
		
        bankteller.startThread();
        bankmanager.startThread();
        loanOfficer.startThread();
        bankcustomer.startThread();
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    /*public void addPerson(Role type) {

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
