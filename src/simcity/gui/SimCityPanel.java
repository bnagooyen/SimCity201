package simcity.gui;

import simcity.Bank.BankCustomerRole;
import simcity.Bank.BankLoanOfficerRole;
import simcity.Bank.BankManagerRole;
import simcity.Bank.BankTellerRole;
import simcity.Market.InventoryBoyRole;
import simcity.Market.MarketCashierRole;
import simcity.Market.MarketCustomerRole;
import simcity.Market.MarketManagerRole;
import simcity.Transportation.DeliveryTruckAgent;
import simcity.gui.DGui.DCookGui;
import simcity.gui.DGui.DWaiterGui;
import simcity.housing.gui.ResidentGui;
import simcity.housing.gui.TenantGui;
import simcity.interfaces.BankLoanOfficer;
import simcity.interfaces.BankManager;
import simcity.interfaces.BankTeller;
import simcity.interfaces.DCook;
import simcity.DCashierRole;
import simcity.DCookRole;
import simcity.DCustomerRole;
import simcity.DHostRole;
import simcity.DMarketAgent;
import simcity.PersonAgent;
import simcity.DWaiterNormalRole;
import simcity.DWaiterRole;
import simcity.DWaiterSharedDataRole;

import javax.swing.*;

import java.awt.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class SimCityPanel extends JPanel {

   
    public static final int NUMAPTS = 12;
    public static final int NUMHOUSES = 15;
    public int houseNumCounter=1;
    public int ApartmentsPerLandlord;
    public int aptNumCounter=1;
    public char aptLetCounter='A';
	
	//Host, cook, waiters and customers
    private DHostRole host = new DHostRole();
    //private HostGui hostGui = new HostGui(host);
    
    
    //private WaiterAgent waiter = new WaiterAgent("Joe");
    //private WaiterGui waiterGui = new WaiterGui(waiter);
    
    private DCookRole cook = new DCookRole();
    private DCookGui cookGui = null;
    
//    private PersonAgent person = new PersonAgent("Doreen");
//    private PersonGui personGui=null;
    
    private Vector<PersonAgent> people = new Vector<PersonAgent>();
    //private CookGui cookGui = new CookGui(cook);
    
    private DCashierRole cashier = new DCashierRole();
    private int waiterIndex = 1;
    private Vector<DWaiterRole> waiters = new Vector<DWaiterRole>();
    private Vector<DCustomerRole> customers = new Vector<DCustomerRole>();
    
    private final int numMarkets = 3;
    private Vector<DMarketAgent> markets = new Vector<DMarketAgent>();
    
    private JPanel restLabel = new JPanel();
   //customer drop down!
    //private ListPanel customerPanel = new ListPanel(this, "Customers");
    //private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    //private JPanel group = new JPanel();

    private SimCityGui gui; //reference to main gui
    
  //Person

    // market people workers
    private MarketManagerRole manager;
    private MarketCashierRole mcashier;
    private InventoryBoyRole ib;
    
    //Bank people workers
    private BankManagerRole Bmanager;
    private BankTellerRole Bteller;
    private BankLoanOfficerRole Bloanofficer;


    public SimCityPanel(SimCityGui gui) {
        this.gui = gui;
        
        //Bank
        Bmanager = new BankManagerRole(gui);
        Bteller = new BankTellerRole(gui);
        Bloanofficer = new BankLoanOfficerRole(gui);
        
        Bmanager.isActive=true;
        PersonAgent bManagerPerson = new PersonAgent("BankManager");
        bManagerPerson.hungerLevel = 0;
        bManagerPerson.SetJob(Bmanager);
        Bmanager.myPerson = bManagerPerson;
        
        Bteller.isActive=true;
        PersonAgent btellerPerson = new PersonAgent("Bankteller");
        btellerPerson.hungerLevel = 0;
        btellerPerson.SetJob(Bteller);
        Bteller.manager=Bmanager;
        Bteller.myPerson = btellerPerson;
        
        Bloanofficer.isActive=true;
        PersonAgent bloanofficerPerson = new PersonAgent("Bankloanofficer");
        bloanofficerPerson.hungerLevel = 0;
        bloanofficerPerson.SetJob(Bloanofficer);
        Bloanofficer.manager=Bmanager;
        Bloanofficer.myPerson = bloanofficerPerson;
        
        //Start Threads
        bManagerPerson.startThread();
        btellerPerson.startThread();
        bloanofficerPerson.startThread();
        
        //Hack Bank Customer
        Bmanager.msgTimeUpdate(8);
        PersonAgent bcustomer = new PersonAgent("bcustomer");
        BankCustomerRole bc = new BankCustomerRole(gui);
        bc.myPerson = bcustomer;
        bc.setManager(Bmanager);
        bcustomer.addCustomerRoles(bc);
        bcustomer.bankTime = true;
        bcustomer.hungerLevel = 0;
        bcustomer.startThread();
        
        
        //Market
        manager = new MarketManagerRole(gui);
        mcashier = new MarketCashierRole(gui);
        ib = new InventoryBoyRole(gui);
        
        manager.isActive = true;
        PersonAgent mManagerPerson = new PersonAgent("Manager");
        mManagerPerson.hungerLevel = 0;
        mManagerPerson.SetJob(manager);
        manager.myPerson = mManagerPerson;
        
        cook.msgAddMarket(manager);
        cook.setMonitor(host.getMonitor());
        
        mcashier.isActive = true;
        PersonAgent mCashierPerson = new PersonAgent("mCashier");
        mCashierPerson.hungerLevel = 0;
        mCashierPerson.SetJob(mcashier);
        mcashier.myPerson = mCashierPerson;
        
        ib.isActive = true;
        PersonAgent ibPerson = new PersonAgent("ib");
        ibPerson.hungerLevel = 0;
        ibPerson.SetJob(ib);
        ib.myPerson = ibPerson;
        
        DeliveryTruckAgent dtruck = new DeliveryTruckAgent(manager);
        // set market role pointers
        mcashier.setInventoryBoy(ib);
        mcashier.setMarketManager(manager);
        ib.setMarketManager(manager);
        ib.setMarketCashier(mcashier);
        manager.setDeliveryTruck(dtruck);
        
        // start threads of market stuff
        dtruck.startThread();
        mManagerPerson.startThread();
        mCashierPerson.startThread();
        ibPerson.startThread();

        // hack market customer
        PersonAgent mcustomer = new PersonAgent("mcustomer");
        MarketCustomerRole mc = new MarketCustomerRole(gui);
        mc.myPerson = mcustomer;
        mc.setMarketManager(manager);
        mcustomer.addCustomerRoles(mc);
        mcustomer.marketTime = true;
        mcustomer.hungerLevel = 0;
        //mcustomer.startThread();
        
        
        
        host.isActive=true;
        PersonAgent hostPerson = new PersonAgent("Host");
        hostPerson.hungerLevel=0; //hack so won't go to restaurant
        hostPerson.SetJob(host);
        host.myPerson=hostPerson;
        hostPerson.startThread();
        
       // host.setGui(hostGui);
        //waiter.setGui(waiterGui);
        //System.err.println(cook);
        cashier.isActive=true;
        PersonAgent cashierPerson = new PersonAgent("Cashier");
        cashierPerson.hungerLevel=0; //hack so won't go to restaurant
        cashierPerson.SetJob(cashier);
        cashier.myPerson=cashierPerson;
        cashier.AddCook(cook);
        //cashier.startThread();
        cashierPerson.startThread();
        
        
        //waiter.startThread();
       // host.startThread();

        
        //cook.msgAddWaiter(waiter);
        
        //waiter.msgAddCook(cook);
        //waiter.msgAddHost(host);
        //need to loop this once have multiple
        
        
       // gui.animationPanel.addGui(waiterGui);
        
        //setLayout(new GridLayout(1,3));
        //group.setLayout(new GridLayout(1, 2));

        //add(customerPanel);
        //add(waiterPanel);

        //initRestLabel();
       // add(restLabel);
        
        //add markets and start their threads
//        for(int i=1; i<=numMarkets; i++) {
//        	DMarketAgent myMarket = new DMarketAgent(i);
//        	cook.msgAddMarket(myMarket);
//        	myMarket.msgAddCook(cook);
//        	host.addCook(cook);
//        	markets.add(myMarket);
//        	myMarket.startThread();
//        }
        
        
        
        //need this for checking if kitchen has enough food
        cookGui= new DCookGui(cook, gui);
        cook.setGui(cookGui);
        gui.myPanels.get("Restaurant 3").panel.addGui(cookGui);
        
        cook.isActive=true;
        PersonAgent cookPerson = new PersonAgent("cook");
        cookPerson.hungerLevel=0; //hack so won't go to restaurant
        cookPerson.SetJob(cook);
        cook.myPerson=cookPerson;
        cook.AddHost(host);
        cook.AddCashier(cashier);
        cookPerson.startThread();
        
        //coding in waiters to test simulation
        PersonAgent nWaiter = new PersonAgent("Head Waiter");
        nWaiter.hungerLevel=0; //hack so won't be hungry
        DWaiterNormalRole headWaiter = new DWaiterNormalRole();
        headWaiter.msgAddHost(host);
        headWaiter.msgAddCook(cook);
        headWaiter.msgAddCashier(cashier);
        DWaiterGui wGui= new DWaiterGui(headWaiter, gui, waiterIndex);
        headWaiter.setGui(wGui);
        nWaiter.startThread();
        gui.myPanels.get("Restaurant 3").panel.addGui(wGui);
        headWaiter.isActive=true;
        headWaiter.myPerson = nWaiter;
        nWaiter.SetJob(headWaiter);
        host.msgAddWaiter(headWaiter);
//		w.msgAddCook(cook);
//      w.msgAddHost(host);
//      w.msgAddCashier(cashier);
//      host.msgAddWaiter(w);
//      waiters.add(w);
        
        
       // cook.startThread();
        
//        personGui=new PersonGui(person, gui);
//        person.setGui(personGui);
//        gui.simCityPanel.addGui(personGui);
//        person.startThread();

       // add(group);
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
     
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        //restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

        //label.setText("aaaah");
        //restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        //restLabel.add(new JLabel("               "), BorderLayout.EAST);
        //restLabel.add(new JLabel("       "), BorderLayout.WEST);
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

        if (type.equals("Customers")) {

            for (int i = 0; i < customers.size(); i++) {
                DCustomerRole temp = customers.get(i);
                if (temp.getText() == name) {
                	return temp;
                }
            }
        }
        
        if (type.equals("Waiters")) {

            for (int i = 0; i < waiters.size(); i++) {
                DWaiterRole temp = waiters.get(i);
                if (temp.getName() == name)
                    return temp;
            }
        }
        
        if (type.equals("Person")) {
//        	System.err.println("lsdfjlkasdjf");
      
            for (int i = 0; i < people.size(); i++) {
                PersonAgent temp = people.get(i);
                if (temp.getName() == name) {
//                	System.out.println("sdljflskjdf");
                	return temp;
                }
            }
        }
        
        
        return new Object();
    }

//    public void msgTogglePause() {
//    	if(host.isPaused) {
//    		host.isPaused=false;
//    		host.Restart();
//    	}
//    	else host.isPaused=true;
//    	for(WaiterRole waiter: waiters) {
//	    	if(waiter.isPaused) {
//	    		waiter.isPaused=false;
//	    		waiter.Restart();
//	    	}
//	    	else waiter.isPaused=true;
//    	}
//    	for(CustomerRole customer: customers) {
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
//    }
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
//    
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
    
    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    
    public void addPerson(String type, String name, double money, String role, String houseOrApt) {

//    	if (type.equals("Customers")) {
//    		CustomerRole c = new CustomerRole(name);	
//    		CustomerGui g = new CustomerGui(c, gui);
//
//    		gui.myPanels.get("Restaurant 3").panel.addGui(g);// dw
//    		c.setHost(host);
//    		c.setCashier(cashier);
//    		c.setGui(g);
//    		customers.add(c);
//    		c.startThread();
////    		System.out.println("added");
//    	}
//    	
//    	if(type.equals("Waiters")) {
//    		WaiterRole w;
//    		if(waiterIndex%2==0) {
//    			w= new WaiterNormalRole(name);
//    		}
//    		else {
//    			w = new WaiterSharedDataRole(name);
//    		}
//    		
//    		WaiterGui g= new WaiterGui(w, gui, waiterIndex);
//    		waiterIndex++;
//    		
//    		gui.myPanels.get("Restaurant 3").panel.addGui(g);
//    		w.setGui(g);
//    		
//    		w.startThread();
//    		//System.out.println("called thread start for + " + name);
//    		
//    		w.msgAddCook(cook);
//            w.msgAddHost(host);
//            w.msgAddCashier(cashier);
//            host.msgAddWaiter(w);
//            waiters.add(w);
//    		
//    		
//    	}
    	
    	if(type.equals("Person")) {
//    		System.out.println("added");
    		PersonAgent p = new PersonAgent(name);
    		p.setMoney(money);
    		DCustomerRole restCustomer = new DCustomerRole(gui);
    		restCustomer.host=host;
    		restCustomer.cashier=cashier;
    		p.addCustomerRoles(restCustomer);
//    		System.err.println("**** "+ name);
    		
			 if(houseOrApt.equals("House") && houseNumCounter<=NUMHOUSES) {
	             p.SetHomeAddress("House "+ Integer.toString(houseNumCounter));
	             houseNumCounter++;
	             
			 }
    		 else if(aptNumCounter<=aptNumCounter) {
                 p.SetHomeAddress("Apartment "+ Integer.toString(aptNumCounter)+ aptLetCounter);
                 if(aptLetCounter=='C') {
                         aptLetCounter='A';
                         aptNumCounter++;
                 }
                 else {
                         aptLetCounter++;
                 }
    		 }
    		 else {
                 p.SetHomeAddress("Homeless Shelter");
    		 }
    		
	    		PersonGui g = new PersonGui(p, gui);
	    		if(houseOrApt.equals("House")) {
	    			ResidentGui rg = new ResidentGui(p);
	    			p.setGui(rg);
	    			gui.myPanels.get(p.homeAddress).panel.addGui(rg);
	    		}
	    		else if(houseOrApt.equals("Apartment")) {
	    			TenantGui tg = new TenantGui(p, p.homeAddress.substring(p.homeAddress.length()-1));
	    			p.setGui(tg);
	    			String tempAddress = p.homeAddress.substring(0, p.homeAddress.length()-1);
	    			gui.myPanels.get(tempAddress).panel.addGui(tg);
	    		}
	    		g.setPresent(true);
	    		p.setGui(g);
	    		gui.city.addGui(g);
	    		p.startThread();
			 
    		people.add(p);
    	}
    }
    
//    public void setCustomerEnabled(String name, double val) {
//    	
//    	for(CustomerRole c: customers) {
//    		if(c.getText()==name) {
//    			String s[] = name.split(", ");
//    			String temp = s[0].trim()+", "+ String.valueOf(val)+ ", "+ s[2].trim()+", "+s[3].trim();
//    			c.setText(temp);
//    			customerPanel.setCustomerEnabled(name, temp);
//    		}
//    	}
//    	
//    }
//    
//    public void setWaiterEnabled(String name) {
//    	waiterPanel.setWaiterEnabled(name);
//    }
//    
//    public void setWaiterDisabled(String name) {
//    	waiterPanel.setWaiterDisabled(name);
//    }
//    
//    public void setWaiterToBreak(String name) {
//    	waiterPanel.setWaiterToBreak(name);
//    }

}
