package simcity.gui;

import simcity.Bank.BankCustomerRole;
import simcity.Bank.BankLoanOfficerRole;
import simcity.Bank.BankManagerRole;
import simcity.Bank.BankTellerRole;
import simcity.Drew_restaurant.Drew_CashierRole;
import simcity.Drew_restaurant.Drew_CookRole;
import simcity.Drew_restaurant.Drew_CustomerRole;
import simcity.Drew_restaurant.Drew_HostRole;
import simcity.Drew_restaurant.Drew_WaiterNormalRole;
import simcity.Drew_restaurant.Drew_WaiterRole;
import simcity.Drew_restaurant.gui.Drew_CookGui;
import simcity.Drew_restaurant.gui.Drew_WaiterGui;
import simcity.KRestaurant.KCashierRole;
import simcity.KRestaurant.KCookRole;
import simcity.KRestaurant.KHostRole;
import simcity.KRestaurant.KWaiterNormalRole;
import simcity.LRestaurant.LCashierRole;
import simcity.LRestaurant.LCookRole;
import simcity.LRestaurant.gui.*;
import simcity.LRestaurant.LCustomerRole;
import simcity.LRestaurant.LHostRole;
import simcity.LRestaurant.LWaiterNormalRole;
import simcity.LRestaurant.LWaiterRole;
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
import simcity.interfaces.KCashier;
import simcity.PersonAgent;

import javax.swing.*;

import simcity.DRestaurant.DCashierRole;
import simcity.DRestaurant.DCookRole;
import simcity.DRestaurant.DCustomerRole;
import simcity.DRestaurant.DHostRole;
//import simcity.DRestaurant.DMarketAgent;
import simcity.DRestaurant.DWaiterNormalRole;
import simcity.DRestaurant.DWaiterRole;
import simcity.DRestaurant.DWaiterSharedDataRole;

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
    
    
    //FOR TESTING WORK
    boolean first=true;
	
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
    
    //Drews Restaurant
    private Vector<Drew_WaiterRole> Drew_waiters = new Vector<Drew_WaiterRole>();
    private Vector<Drew_CustomerRole> Drew_customers = new Vector<Drew_CustomerRole>();
    private Drew_CashierRole Drew_cashier = new Drew_CashierRole();
    private Drew_WaiterRole Drew_waiter = new Drew_WaiterNormalRole();
    private Drew_CookRole Drew_cook = new Drew_CookRole();
    private Drew_HostRole Drew_host = new Drew_HostRole();
    private Drew_CustomerRole Drew_customer = new Drew_CustomerRole();
    private Drew_CookGui Drewcookgui;
    
    //Linda's Restaurant
    private Vector<LWaiterRole> Lwaiters = new Vector<LWaiterRole>();
    private Vector<LCustomerRole> Lcustomers = new Vector<LCustomerRole>();
    private LCashierRole Lcashier = new LCashierRole();
    private LCookRole Lcook = new LCookRole();
    private LCookGui LcookGui = new LCookGui(Lcook, "LcookGui");
    private LHostRole Lhost = new LHostRole();
    
    private final int numMarkets = 3;
    //private Vector<DMarketAgent> markets = new Vector<DMarketAgent>();
    
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
        bManagerPerson.SetJob(Bmanager, "Bank 1");
        Bmanager.myPerson = bManagerPerson;
        
        //Bteller.isActive=true;
       // PersonAgent btellerPerson = new PersonAgent("Bankteller");     REMEMBER TO START THREAD!!!!
        //btellerPerson.hungerLevel = 0;
        //btellerPerson.SetJob(Bteller, "Bank 1");
        Bteller.manager=Bmanager;
        //Bteller.myPerson = btellerPerson;
        
        Bloanofficer.isActive=true;
        PersonAgent bloanofficerPerson = new PersonAgent("Bankloanofficer");
        bloanofficerPerson.hungerLevel = 0;
        bloanofficerPerson.SetJob(Bloanofficer, "Bank 1");
        Bloanofficer.manager=Bmanager;
        Bloanofficer.myPerson = bloanofficerPerson;
        
        //Start Threads
        bManagerPerson.startThread();
        //btellerPerson.startThread();
        bloanofficerPerson.startThread();
        
        //Hack Bank Customer
        /*Bmanager.msgTimeUpdate(8);
        PersonAgent bcustomer = new PersonAgent("bcustomer");
        BankCustomerRole bc = new BankCustomerRole(gui);
        bc.myPerson = bcustomer;
        bc.setManager(Bmanager);
        bcustomer.addCustomerRoles(bc);
        bcustomer.bankTime = true;
        bcustomer.hungerLevel = 0;
        bcustomer.startThread();*/
        
        
        //Market
        manager = new MarketManagerRole(gui);
        mcashier = new MarketCashierRole(gui);
        ib = new InventoryBoyRole(gui);
        
        manager.isActive = true;
        PersonAgent mManagerPerson = new PersonAgent("Manager");
        mManagerPerson.hungerLevel = 0;
        mManagerPerson.SetJob(manager, "Market 1");
        manager.myPerson = mManagerPerson;
        
        cook.msgAddMarket(manager);
        cook.setMonitor(host.getMonitor());
        
        mcashier.isActive = true;
        PersonAgent mCashierPerson = new PersonAgent("mCashier");
        mCashierPerson.hungerLevel = 0;
        mCashierPerson.SetJob(mcashier, "Market 1");
        mcashier.myPerson = mCashierPerson;
        
        ib.isActive = true;
        PersonAgent ibPerson = new PersonAgent("ib");
        ibPerson.hungerLevel = 0;
        ibPerson.SetJob(ib, "Market 1");
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

//        // hack market customer
//        PersonAgent mcustomer = new PersonAgent("mcustomer");
//        MarketCustomerRole mc = new MarketCustomerRole(gui);
//        mc.myPerson = mcustomer;
//        mc.setMarketManager(manager);
//        mcustomer.addCustomerRoles(mc);
//        mcustomer.marketTime = true;
//        mcustomer.hungerLevel = 0;
//        //mcustomer.startThread();
//        
//        
        //Doreen's Restaurant setup
        host.isActive=true;
        PersonAgent hostPerson = new PersonAgent("Host");
        hostPerson.hungerLevel=0; //hack so won't go to restaurant
        hostPerson.SetJob(host, "Restaurant 3");
        host.myPerson=hostPerson;
        hostPerson.startThread();
        
       // host.setGui(hostGui);
        //waiter.setGui(waiterGui);
        //System.err.println(cook);
        cashier.isActive=true;
        PersonAgent cashierPerson = new PersonAgent("Cashier");
        cashierPerson.hungerLevel=0; //hack so won't go to restaurant
        cashierPerson.SetJob(cashier, "Restaurant 3");
        cashier.myPerson=cashierPerson;
        cashier.AddCook(cook);
        //cashier.startThread();
        cashierPerson.startThread();
        
        
        
        //need this for checking if kitchen has enough food
        cookGui= new DCookGui(cook, gui);
        cook.setGui(cookGui);
        gui.myPanels.get("Restaurant 3").panel.addGui(cookGui);
        
        cook.isActive=true;
        PersonAgent cookPerson = new PersonAgent("cook");
        cookPerson.hungerLevel=0; //hack so won't go to restaurant
        cookPerson.SetJob(cook, "Restaurant 3");
        cook.myPerson=cookPerson;
        cook.AddHost(host);
        cook.AddCashier(cashier);
        cookPerson.startThread();
//        
//        
//        
//        //coding in waiters to test simulation
//        PersonAgent nWaiter = new PersonAgent("Head Waiter");
//        nWaiter.hungerLevel=0; //hack so won't be hungry
//        DWaiterNormalRole headWaiter = new DWaiterNormalRole();
//        headWaiter.msgAddHost(host);
//        headWaiter.msgAddCook(cook);
//        headWaiter.msgAddCashier(cashier);
//        DWaiterGui wGui= new DWaiterGui(headWaiter, gui, waiterIndex);
//        headWaiter.setGui(wGui);
//        nWaiter.startThread();
//        gui.myPanels.get("Restaurant 3").panel.addGui(wGui);
//        headWaiter.isActive=true;
//        headWaiter.myPerson = nWaiter;
//        nWaiter.SetJob(headWaiter, "Restaurant 3");
//        host.msgAddWaiter(headWaiter);
////		w.msgAddCook(cook);
////      w.msgAddHost(host);
////      w.msgAddCashier(cashier);
////      host.msgAddWaiter(w);
////      waiters.add(w);
//        
//        
//       // cook.startThread();
//        
////        personGui=new PersonGui(person, gui);
////        person.setGui(personGui);
////        gui.simCityPanel.addGui(personGui);
////        person.startThread();
//
//       // add(group);
//        
//      //Setting up people for Drew's Restaurant
//        Drew_host.isActive=true;
//        PersonAgent Drew_hostPerson = new PersonAgent("Drew Host");
//        Drew_hostPerson.hungerLevel=0; //hack so won't go to restaurant
//        Drew_hostPerson.SetJob(Drew_host, "Restaurant 2");
//        Drew_host.myPerson=Drew_hostPerson;
//        Drew_hostPerson.startThread();
//        
//        Drew_cashier.isActive=true;
//        PersonAgent Drew_cashierPerson = new PersonAgent("Drew Cashier");
//        Drew_cashierPerson.hungerLevel=0; //hack so won't go to restaurant
//        Drew_cashierPerson.SetJob(Drew_cashier, "Restaurant 2");
//        Drew_cashier.myPerson=Drew_cashierPerson;
//        Drew_cashier.setCook(Drew_cook);
//        cashierPerson.startThread();
//        
//        Drewcookgui= new Drew_CookGui(Drew_cook);
//        Drew_cook.setGui(Drewcookgui);
//        gui.myPanels.get("Restaurant 2").panel.addGui(Drewcookgui);
//        Drew_cook.isActive=true;
//        PersonAgent Drew_cookPerson = new PersonAgent("Drew cook");
//        Drew_cookPerson.hungerLevel=0; //hack so won't go to restaurant
//        Drew_cookPerson.SetJob(Drew_cook, "Restaurant 2");
//        Drew_cook.myPerson=cookPerson;
//        Drew_cook.setHost(Drew_host);
//        Drew_cook.setCashier(Drew_cashier);
//        Drew_cookPerson.startThread();
//        
//        PersonAgent Drew_Waiter = new PersonAgent("Drew's Waiter");
//        Drew_Waiter.hungerLevel=0;
//        Drew_WaiterNormalRole Drew_headWaiter = new Drew_WaiterNormalRole();
//        Drew_headWaiter.setHost(Drew_host);
//        Drew_headWaiter.setCook(Drew_cook);
//        Drew_headWaiter.addCashier(Drew_cashier);
//        Drew_WaiterGui Drew_wgui = new Drew_WaiterGui(Drew_headWaiter,1);
//        Drew_headWaiter.setGui(Drew_wgui);
//        Drew_Waiter.startThread();       
//        gui.myPanels.get("Restaurant 2").panel.addGui(Drew_wgui);
//        Drew_headWaiter.isActive=true;
//        Drew_headWaiter.myPerson = Drew_Waiter;
//        Drew_Waiter.SetJob(Drew_headWaiter, "Restaurant 2");
//        Drew_host.addWaiter(Drew_headWaiter);
//       
//        //Linda's Restaurant setup
//        Lhost.isActive = true;
//        PersonAgent LHostPerson = new PersonAgent("L Host");
//        LHostPerson.hungerLevel = 0;
//        LHostPerson.SetJob(Lhost, "Restaurant 1");
//        Lhost.myPerson = LHostPerson;
//        Lhost.setCook(Lcook);
//        LHostPerson.startThread();
//        
//        Lcashier.isActive = true;
//        PersonAgent LCashierPerson = new PersonAgent("L Cashier");
//        LCashierPerson.hungerLevel = 0;
//        LCashierPerson.SetJob(Lcashier, "Restaurant 1");
//        Lcashier.myPerson = LCashierPerson;
//        Lcashier.setCook(Lcook);
//        Lcashier.setHost(Lhost);
//        LCashierPerson.startThread();
//        
//        Lcook.setGui(LcookGui);
//        gui.myPanels.get("Restaurant 1").panel.addGui(LcookGui);
//        Lcook.isActive = true;
//        PersonAgent LCookPerson = new PersonAgent("L Cook");
//        LCookPerson.hungerLevel = 0;
//        LCookPerson.SetJob(Lcook, "Restaurant 1");
//        Lcook.myPerson = LCookPerson;
//        Lcook.setCashier(Lcashier);
//        Lcook.setHost(Lhost);
//        LCookPerson.startThread();
//        
//        
//        PersonAgent nLwaiterPerson = new PersonAgent("Normal Waiter");
//        nWaiter.hungerLevel=0;
//        LWaiterNormalRole nLwaiter = new LWaiterNormalRole();
//        nLwaiter.setHost(Lhost);
//        nLwaiter.setCook(Lcook);
//        nLwaiter.setCashier(Lcashier);
//        LWaiterGui LwGui = new LWaiterGui(nLwaiter, "LnWaiterGui");
//        nLwaiter.setGui(LwGui);
//        nLwaiterPerson.startThread();
//        gui.myPanels.get("Restaurant 1").panel.addGui(LwGui);
//        nLwaiter.isActive=true;
//        nLwaiter.myPerson = nWaiter;
//        nLwaiterPerson.SetJob(nLwaiter, "Restaurant 1");
//        Lhost.addWaiter(nLwaiter);
//        
        
//        // check kim's restaurant
//        KHostRole kh = new KHostRole();
//        PersonAgent host = new PersonAgent("host");
//        host.SetJob(kh, "Restaurant 4");
//        kh.myPerson = host;
//        kh.isActive = true;
////        
////        PersonAgent waiter = new PersonAgent("waiter");
////        KWaiterNormalRole w = (KWaiterNormalRole) KRestaurant.AddNormalWaiter();;
////        waiter.SetJob(w);
////        w.setPerson(waiter);
////        people.add(waiter);
//        
//        KCookRole kcook = new KCookRole(gui);
//        PersonAgent cook = new PersonAgent("cook");
//        cook.SetJob(kcook, "Restaurant 4");
//        kcook.myPerson = cook;
//        kcook.isActive = true;
//        
//        KCashierRole kc = new KCashierRole();
//        PersonAgent kcashier = new PersonAgent("cashier");
//        kcashier.SetJob(kc, "Restaurant 4");
//        kc.myPerson = kcashier;
//        kc.isActive = true;
//        
//        kcook.setCashier(kc);
//        kcook.setHost(kh);
//        kcook.addMarket(manager);
//        kc.setCook(kcook);
//        kc.setHost(kh);
//        kh.setCook(kcook);
//        
//        host.startThread();
//        cook.startThread();
//        kcashier.startThread();
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


    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    
    public void addPerson(String type, String name, double money, String role, String houseOrApt, String transport) {

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
    		p.SetTravelPreference(transport);
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
	    		p.bankTime=true;
	    		
//	    		Bmanager.msgTimeUpdate(8);
	            BankCustomerRole bc = new BankCustomerRole(gui);
	            bc.myPerson = p;
	            bc.setManager(Bmanager);
	            p.addCustomerRoles(bc);
	            
	            
            	//test going to work
	            if(first){
	            	p.bankTime=false;
	            	p.hungerLevel = 0;
	            	p.SetJob(Bteller, "Bank 1");
	            	Bteller.manager=Bmanager;
	            	Bteller.myPerson = p;
	            	Bteller.isActive=false;
	            	first=false;
	            }
	            
	            
	    		p.startThread();
	    		p.msgTimeUpdate(7);
			 
    		people.add(p);
    	}
    }


}
