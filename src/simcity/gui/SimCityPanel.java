package simcity.gui;

import simcity.BRestaurant.BCashierRole;
import simcity.BRestaurant.BCookRole;
import simcity.BRestaurant.BCustomerRole;
import simcity.BRestaurant.BHostRole;
import simcity.BRestaurant.BWaiterNormalRole;
import simcity.BRestaurant.BWaiterRole;
import simcity.BRestaurant.BWaiterSharedDataRole;
import simcity.Bank.BankCustomerRole;
import simcity.Bank.BankLoanOfficerRole;
import simcity.Bank.BankManagerRole;
import simcity.Bank.BankRobberRole;
import simcity.Bank.BankTellerRole;
import simcity.Bank.gui.BankTellerGui;
import simcity.Drew_restaurant.Drew_CashierRole;
import simcity.Drew_restaurant.Drew_CookRole;
import simcity.Drew_restaurant.Drew_CustomerRole;
import simcity.Drew_restaurant.Drew_HostRole;
import simcity.Drew_restaurant.Drew_WaiterNormalRole;
import simcity.Drew_restaurant.Drew_WaiterRole;
import simcity.Drew_restaurant.Drew_WaiterSharedDataRole;
import simcity.Drew_restaurant.gui.Drew_CookGui;
import simcity.Drew_restaurant.gui.Drew_WaiterGui;
import simcity.KRestaurant.KCashierRole;
import simcity.KRestaurant.KCookRole;
import simcity.KRestaurant.KCustomerRole;
import simcity.KRestaurant.KHostRole;
import simcity.KRestaurant.KWaiterNormalRole;
import simcity.KRestaurant.KWaiterRole;
import simcity.KRestaurant.KWaiterSharedDataRole;
import simcity.LRestaurant.LCashierRole;
import simcity.LRestaurant.LCookRole;
import simcity.LRestaurant.gui.*;
import simcity.LRestaurant.LCustomerRole;
import simcity.LRestaurant.LHostRole;
import simcity.LRestaurant.LWaiterNormalRole;
import simcity.LRestaurant.LWaiterRole;
import simcity.LRestaurant.LWaiterSharedDataRole;
import simcity.Market.InventoryBoyRole;
import simcity.Market.MarketCashierRole;
import simcity.Market.MarketCustomerRole;
import simcity.Market.MarketManagerRole;
import simcity.TRestaurant.TCashierRole;
import simcity.TRestaurant.TCookRole;
import simcity.TRestaurant.TCustomerRole;
import simcity.TRestaurant.THostRole;
import simcity.TRestaurant.TWaiterRole;
import simcity.TRestaurant.TWaiterSharedDataRole;
import simcity.TRestaurant.gui.TCookGui;
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

import agent.Role;
import simcity.DRestaurant.DCashierRole;
import simcity.DRestaurant.DCookRole;
import simcity.DRestaurant.DCustomerRole;
import simcity.DRestaurant.DHostRole;
//import simcity.DRestaurant.DMarketAgent;
import simcity.DRestaurant.DWaiterNormalRole;
import simcity.DRestaurant.DWaiterRole;
import simcity.DRestaurant.DWaiterSharedDataRole;
import simcity.interfaces.*;

import java.awt.*;
import java.util.ArrayList;
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
    
	private ArrayList<RestaurantPlace> myRestaurants=new ArrayList<RestaurantPlace>();
	
	// make restaurants w/roles
	RestaurantPlace DrewRestaurant;
	RestaurantPlace BRestaurant;
	RestaurantPlace DRestaurant;
	RestaurantPlace KRestaurant;
	RestaurantPlace LRestaurant;
	RestaurantPlace TRestaurant;
	
	// make bank
	BankPlace bank=null;
	
	// make market 
	MarketPlace market=null;
    //FOR TESTING WORK
    boolean first=true;
	
	//Host, cook, waiters and customers
   // private DHostRole host = new DHostRole();
    //private HostGui hostGui = new HostGui(host);
    
    
    //private WaiterAgent waiter = new WaiterAgent("Joe");
    //private WaiterGui waiterGui = new WaiterGui(waiter);
    
    //private DCookRole cook = new DCookRole();
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
    
    
    //Brian's Restaurant
    private Vector<BWaiterRole> BWaiters= new Vector <BWaiterRole>();
    private Vector<BCustomerRole> Bcustomers= new Vector<BCustomerRole>();
    private BCashierRole Bcashier= new BCashierRole();
    private BCookRole Bcook=new BCookRole();
   
    private BHostRole Bhost=new BHostRole();
  
  
    
    
    
    
    //Tiff's Restaurant
    private Vector<TWaiterRole> Twaiters = new Vector<TWaiterRole>();
    private Vector<TCustomerRole> Tcustomers = new Vector<TCustomerRole>();
    private TCustomerRole tCustomer = new TCustomerRole();
    private TCashierRole tCashier = new TCashierRole(); 
    private TWaiterRole tWaiter = new TWaiterRole(); 
    private TWaiterSharedDataRole tsWaiter = new TWaiterSharedDataRole(); 
    private TCookRole tCook = new TCookRole();
    private TCookGui tCookGui = new TCookGui(tCook);
    private THostRole tHost = new THostRole();
    
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
   // private MarketManagerRole manager;
   // private MarketCashierRole mcashier;
   // private InventoryBoyRole ib;
    
    //Bank people workers
    private BankManagerRole Bmanager;
    //~~~private BankTellerRole Bteller;
    private BankLoanOfficerRole Bloanofficer;


    public SimCityPanel(SimCityGui gui) {
        this.gui = gui;
        
        bank= new BankPlace();
        market = new MarketPlace();
        
		DrewRestaurant = new RestaurantPlace(2);
		BRestaurant = new RestaurantPlace(5);
		DRestaurant = new RestaurantPlace(3);
		KRestaurant = new RestaurantPlace(4);
		LRestaurant = new RestaurantPlace(1);
		TRestaurant = new RestaurantPlace(6);
		
		
		myRestaurants.add(DrewRestaurant);
		myRestaurants.add(BRestaurant);
		myRestaurants.add(DRestaurant);
		myRestaurants.add(KRestaurant);
		myRestaurants.add(LRestaurant);
		myRestaurants.add(TRestaurant);

        
        //Bank
        //Bmanager = new BankManagerRole(gui);
        //Bteller = new BankTellerRole(gui);
        //Bloanofficer = new BankLoanOfficerRole(gui);
        
        //Bmanager.isActive=true;
        PersonAgent bManagerPerson = new PersonAgent("BankManager");
        bManagerPerson.hungerLevel = 0;
        bManagerPerson.SetJob(bank.bankManager, "Bank 1");
        bank.bankManager.myPerson = bManagerPerson;
        
        //Bteller.isActive=true;
        //~~~PersonAgent btellerPerson = new PersonAgent("Bankteller");     //REMEMBER TO START THREAD!!!!
        //~~btellerPerson.hungerLevel = 0;
        //~~~btellerPerson.SetJob(bank.bankTeller, "Bank 1");
        //~~~bank.bankTeller.myPerson=btellerPerson;
        //Bteller.manager=Bmanager;
        //Bteller.myPerson = btellerPerson;
        
        //Bloanofficer.isActive=true;
        PersonAgent bloanofficerPerson = new PersonAgent("Bankloanofficer");
        bloanofficerPerson.hungerLevel = 0;
        bloanofficerPerson.SetJob(bank.loanOfficer, "Bank 1");
        //Bloanofficer.manager=Bmanager;
        bank.loanOfficer.myPerson = bloanofficerPerson;
        
        //Start Threads
        bManagerPerson.startThread();
        bloanofficerPerson.startThread();
        //~~btellerPerson.startThread();
        
//        //Hack Bank Customer
//        /*Bmanager.msgTimeUpdate(8);
//        PersonAgent bcustomer = new PersonAgent("bcustomer");
//        BankCustomerRole bc = new BankCustomerRole(gui);
//        bc.myPerson = bcustomer;
//        bc.setManager(Bmanager);
//        bcustomer.addCustomerRoles(bc);
//        bcustomer.bankTime = true;
//        bcustomer.hungerLevel = 0;
//        bcustomer.startThread();*/
//        
//        
        //Market
       // manager = new MarketManagerRole(gui);
       // mcashier = new MarketCashierRole(gui);
       // ib = new InventoryBoyRole(gui);
        
        market.mManager.isActive = true;
        PersonAgent mManagerPerson = new PersonAgent("Manager");
        mManagerPerson.hungerLevel = 0;
        mManagerPerson.SetJob(market.mManager, "Market 1");
        market.mManager.myPerson = mManagerPerson;
//        
//        cook.msgAddMarket(manager);
//        cook.setMonitor(host.getMonitor());
//        
        market.mCashier.isActive = true;
        PersonAgent mCashierPerson = new PersonAgent("mCashier");
        mCashierPerson.hungerLevel = 0;
        mCashierPerson.SetJob(market.mCashier, "Market 1");
        market.mCashier.myPerson = mCashierPerson;
        
        market.ib.isActive = true;
        PersonAgent ibPerson = new PersonAgent("ib");
        ibPerson.hungerLevel = 0;
        ibPerson.SetJob(market.ib, "Market 1");
        market.ib.myPerson = ibPerson;
        
        //DeliveryTruckAgent dtruck = new DeliveryTruckAgent(manager);
        // set market role pointers
        //mcashier.setInventoryBoy(ib);
        //mcashier.setMarketManager(manager);
       // ib.setMarketManager(manager);
       // ib.setMarketCashier(mcashier);
       // manager.setDeliveryTruck(dtruck);
//        
//        // start threads of market stuff
//        dtruck.startThread();
      mManagerPerson.startThread();
      mCashierPerson.startThread();
      ibPerson.startThread();
//
////        // hack market customer
////        PersonAgent mcustomer = new PersonAgent("mcustomer");
////        MarketCustomerRole mc = new MarketCustomerRole(gui);
////        mc.myPerson = mcustomer;
////        mc.setMarketManager(manager);
////        mcustomer.addCustomerRoles(mc);
////        mcustomer.marketTime = true;
////        mcustomer.hungerLevel = 0;
////        //mcustomer.startThread();
////        
////        
        //Doreen's Restaurant setup
        //host.isActive=true;
        for(RestaurantPlace r: myRestaurants) {
        	if(r.restNum==3) {
        		r.host.isActive=true;
        		 PersonAgent hostPerson = new PersonAgent("Host");
        		 hostPerson.hungerLevel=0; //hack so won't go to restaurant
        	        hostPerson.SetJob(r.host, "Restaurant 3");
        	        r.host.myPerson=hostPerson;
        	        hostPerson.startThread();
        	        // host.setGui(hostGui);
        	        //waiter.setGui(waiterGui);
        	        //System.err.println(cook);
        	        r.cashier.isActive=true;
        	        PersonAgent cashierPerson = new PersonAgent("Cashier");
        	        cashierPerson.hungerLevel=0; //hack so won't go to restaurant
        	        cashierPerson.SetJob(r.cashier, "Restaurant 3");
        	        r.cashier.myPerson=cashierPerson;
        	        //r.cashi.AddCook(cook);
        	        //cashier.startThread();
        	        cashierPerson.startThread();
        	        
        	        
        	        
        	        //need this for checking if kitchen has enough food
        	        //cookGui= new DCookGui(r.cook, gui);
        	        //cook.setGui(cookGui);
        	        //gui.myPanels.get("Restaurant 3").panel.addGui(cookGui);
        	        
        	        r.cook.isActive=true;
        	        PersonAgent cookPerson = new PersonAgent("cook");
        	        cookPerson.hungerLevel=0; //hack so won't go to restaurant
        	        cookPerson.SetJob(r.cook, "Restaurant 3");
        	        r.cook.myPerson=cookPerson;
        	        cookPerson.startThread();
        	        
        	        
        	        
        	        //coding in waiters to test simulation
        	        PersonAgent nWaiter = new PersonAgent("Head Waiter");
        	        nWaiter.hungerLevel=0; //hack so won't be hungry
        	        DWaiterNormalRole headWaiter = (DWaiterNormalRole)(r.AddNormalWaiter());
        	        headWaiter.isActive=true;
        	        headWaiter.myPerson=nWaiter;
        	        //headWaiter.msgAddHost(host);
        	        //headWaiter.msgAddCook(cook);
        	        //headWaiter.msgAddCashier(cashier);
        	        //DWaiterGui wGui= new DWaiterGui(headWaiter, gui);
        	        //headWaiter.setGui(wGui);
        	        nWaiter.startThread();
        	        //gui.myPanels.get("Restaurant 3").panel.addGui(wGui);
        	        //headWaiter.isActive=true;
        	        //headWaiter.myPerson = nWaiter;
        	        nWaiter.SetJob(headWaiter, "Restaurant 3");
        	        //host.msgAddWaiter(headWaiter);
//        			w.msgAddCook(cook);
//        	      w.msgAddHost(host);
//        	      w.msgAddCashier(cashier);
//        	      host.msgAddWaiter(w);
//        	      waiters.add(w);
        	}
        }
       
       
        
      
        
        
       // cook.startThread();
        
//        personGui=new PersonGui(person, gui);
//        person.setGui(personGui);
//        gui.simCityPanel.addGui(personGui);
//        person.startThread();
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
        
        //tiff's restaurant setup
        /**
        tHost.isActive = true; 
        PersonAgent THost = new PersonAgent("THost");
        THost.hungerLevel = 0; 
        THost.SetJob(tHost, "Restaurant 6");
        tHost.myPerson = THost; 
        THost.startThread(); 
        
        tCashier.isActive = true; 
        PersonAgent TCashier = new PersonAgent("TCashier");
        TCashier.hungerLevel = 0; 
        TCashier.SetJob(tCashier, "Restaurant 6");
        tCashier.myPerson = TCashier; 
        TCashier.startThread();
        
        tWaiter.isActive = true; 
        PersonAgent TWaiter = new PersonAgent("TWaiter");
        TWaiter.hungerLevel = 0; 
        TWaiter.SetJob(tWaiter, "Restaurant 6");
        tWaiter.myPerson = TWaiter; 
        TWaiter.startThread();
        Twaiters.add(tWaiter); 
        
        tsWaiter.isActive = true; 
        PersonAgent TSWaiter = new PersonAgent("TSWaiter");
        TSWaiter.hungerLevel = 0; 
        TSWaiter.SetJob(tsWaiter, "Restaurant 6");
        tsWaiter.myPerson = TSWaiter; 
        TSWaiter.startThread(); 
        Twaiters.add(tsWaiter); 
        
        tCook.isActive = true; 
        PersonAgent TCook = new PersonAgent("TCook"); 
        TCook.hungerLevel = 0; 
        TCook.SetJob(tCook, "Restaurant 6");
        tCook.myPerson = TCook; 
        TCookGui tcg = new TCookGui(tCook);
        tCook.setGui(tcg);
        TCook.startThread();
        
        tHost.setCashier(tCashier);
        tHost.setCook(tCook);
        tHost.addWaiter(tWaiter);
        tHost.addWaiter(tsWaiter); 
        
        tWaiter.setCashier(tCashier);
        tWaiter.setCook(tCook);
        tWaiter.setHost(tHost);
        
        tsWaiter.setCashier(tCashier);
        tsWaiter.setCook(tCook);
        tsWaiter.setHost(tHost); 
        
        tCook.setCashier(tCashier);
        tCook.setHost(tHost);
        
        tCashier.setCook(tCook);
        tCashier.setHost(tHost);
        
        gui.myPanels.get("Restaurant 6").panel.addGui(tcg);
        */
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */


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
    	if(type.equals("Person")) {
//    		System.out.println("added");
    		PersonAgent p = new PersonAgent(name);
    		p.setMoney(money);
    		p.SetTravelPreference(transport);
    		//DCustomerRole restCustomer = new DCustomerRole(gui);
    		//restCustomer.host=host;
    		//restCustomer.cashier=cashier;
    		ArrayList<Role> tempRoles = GenerateAllCustomerRoles();
    		for(Role r: tempRoles) {
    			r.myPerson=p;
    		}
    		p.addCustomerRoles(tempRoles);
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
	    		
	    		//Add Job
	    		if(role.equals("Waiter")){
	    			Drew_WaiterRole DW=(Drew_WaiterRole) DrewRestaurant.AddNormalWaiter();
	    			DW.myPerson=p;
	    			p.SetJob(DW,"Restaurant 2");
	    			p.msgTimeUpdate(8);
	    		}
	    		if(role.equals("Bank Teller")){
	    			BankTellerRole BT= new BankTellerRole(gui);
	    			BT.setManager(bank.bankManager);
	    			BT.myPerson=p;
	    			p.SetJob(BT,"Bank 1");
	    			p.myJob.isActive=false;
	    			p.hungerLevel=0;
	    		}
	    		if(role.equals("Inventory Person")){
	    			bank.bankManager.msgTimeUpdate(20);
	    		}
//	    		Bmanager.msgTimeUpdate(8);
	            //BankCustomerRole bc = new BankCustomerRole(gui);
	            //bc.myPerson = p;
	            //bc.setManager(Bmanager);
	            //p.addCustomerRoles(bc);
	            
	            
            	//test going to work
	            /*if(first){
	            	p.bankTime=false;
	            	p.hungerLevel = 0;
	            	p.SetJob(Bteller, "Bank 1");
	            	Bteller.manager=Bmanager;
	            	Bteller.myPerson = p;
	            	Bteller.isActive=false;
	            	first=false;
	            }*/
	            
	            
	    		p.startThread();
	    		p.msgTimeUpdate(7);
			 
    		people.add(p);
    	}
    }

    /**************************ROLE FACTORY******************************/
    
    class Business {
        public String name;
        public int x;
        public int y;
}
//waiter needs host
//customer needs hot
//waiter needs cook setCook(cook)

    
 private ArrayList<Role> GenerateAllCustomerRoles() {
	 ArrayList<Role> myCustomerRoles= new ArrayList<Role>();
	 /*******turn this into a loop to make more efficient*********/
	 myCustomerRoles.add(bank.addCustomer());
	 myCustomerRoles.add(market.addCustomer());
	 myCustomerRoles.add(DrewRestaurant.AddCustomer());
	 myCustomerRoles.add(BRestaurant.AddCustomer());
	 myCustomerRoles.add(DRestaurant.AddCustomer());
	 myCustomerRoles.add(KRestaurant.AddCustomer());
	 myCustomerRoles.add(LRestaurant.AddCustomer());
	 myCustomerRoles.add(TRestaurant.AddCustomer());

	 return myCustomerRoles;


	 
	 //return null;
 }
    

class MarketPlace extends Business {
       // public ArrayList<InventoryBoyRole> ibs;
	 DeliveryTruckAgent dtruck;
		public InventoryBoyRole ib;
        public MarketManagerRole mManager;
        public MarketCashierRole mCashier;
        ArrayList<MarketCustomer> mCustomers;
        DeliveryTruckAgent truck;

        public MarketPlace() {
        		
                ib = new InventoryBoyRole(gui);
                mManager = new MarketManagerRole(gui);
                mCashier = new MarketCashierRole(gui);
                mCustomers = new ArrayList<MarketCustomer>();
                truck = new DeliveryTruckAgent(mManager);
                
                mManager.dTruck = (DeliveryTruck)truck;
                
                ib.setMarketCashier(mCashier);
                ib.setMarketManager(mManager);
                
                mCashier.setInventoryBoy(ib);
                mCashier.setMarketManager(mManager);
                
                dtruck = new DeliveryTruckAgent(mManager);
                mManager.setDeliveryTruck(dtruck);
                dtruck.startThread();
        
        }
        
        public MarketCustomerRole addCustomer() {
                MarketCustomerRole c = new MarketCustomerRole(gui);
                c.setMarketManager(mManager);
                mCustomers.add(c);
                return c;
        }
}

public class BankPlace extends Business {
	//JPanel animationPanel = new JPanel();
	
	public BankLoanOfficerRole loanOfficer;
	public BankManagerRole bankManager;
	//~~~public BankTellerRole bankTeller;
	public BankRobberRole robber;
	
	
	ArrayList<BankCustomerRole> bankCustomers;
	
	public BankPlace() {
		loanOfficer = new BankLoanOfficerRole(gui);
		bankManager = new BankManagerRole(gui);
		//~~~bankTeller = new BankTellerRole(gui);
		bankCustomers = new ArrayList<BankCustomerRole>();
		
		loanOfficer.setManager(bankManager);
		//~~~bankTeller.setManager(bankManager);
		
	}
	public BankCustomerRole addCustomer() {
		BankCustomerRole b = new BankCustomerRole(gui);
		b.setManager(bankManager);
		bankCustomers.add(b);
		return b;
	}
}

class RestaurantPlace extends Business {
        public int restNum;
        public Role host = null;
        public Role cook = null;
        public Role cashier = null;
        ArrayList<Role> waiters = new ArrayList<Role>();
        ArrayList<Role> customers = new ArrayList<Role>();
        RestaurantPlace(int restNum) {
                this.restNum = restNum;
                switch(restNum) {
                

                case 2: host = new Drew_HostRole();
                cook = new Drew_CookRole();
                ((Drew_CookRole)cook).addMarket(market.mManager);
                ((Drew_HostRole)host).setCook((Drew_Cook)cook);
                cashier = new Drew_CashierRole();
                break;
				case 5: host = new BHostRole();
				                cook = new BCookRole();
				                ((BHost)host).setCook((BCookRole)cook);
				               // ((BCookRole)cook).addMarket(market.mManager);
				                cashier = new BCashierRole();
				                break;
				case 3: host = new DHostRole();
				                cook = new DCookRole();
				                ((DCookRole)cook).msgAddMarket(market.mManager);
				                cashier = new DCashierRole();
				                ((DHostRole)host).addCook((DCookRole)cook);
				                ((DCookRole) cook).AddHost((DHostRole)host);
				                ((DCookRole)cook).AddCashier((DCashier)cashier);
				                ((DCashierRole)cashier).AddCook((DCook)cook);
				                ((DCashierRole)cashier).AddHost((DHostRole)host);
				                break;
				case 4: host = new KHostRole();
				                cook = new KCookRole(gui);
				                ((KCookRole)cook).addMarket(market.mManager);
				                cashier = new KCashierRole();
				                ((KHostRole)host).setCook((KCookRole) cook);
				                ((KHostRole)host).setCashier((KCashierRole) cashier);
				                ((KCookRole)cook).setHost((KHostRole) host);
				                ((KCookRole)cook).setCashier((KCashierRole) cashier);
				                ((KCashierRole)cashier).setHost((KHostRole) host);
				                ((KCashierRole)cashier).setCook((KCookRole) cook);
				                break;
				case 1:        host = new LHostRole();
				                cook = new LCookRole();
				                ((LCookRole)cook).addMarket(market.mManager);
				                System.out.println("LCookRole is "+cook);
				                cashier = new LCashierRole();
				                ((LHostRole) host).setCook((LCook)cook);
				                ((LCookRole) cook).setCashier((LCashierRole)cashier);
				                ((LCookRole) cook).setHost((LHostRole)host);
				                ((LCashierRole)cashier).setHost((LHostRole)host);
				                ((LCashierRole)cashier).setCook((LCookRole)cook);
				                break;
				case 6: host = new THostRole(); 
				                cook = new TCookRole();
				                //((TCookRole)cook).addMarket(market.mManager);
				                cashier = new TCashierRole();
				                ((THostRole) host).setCook((TCookRole)cook);
				                ((THostRole) host).setCashier((TCashierRole)cashier); 
				                ((TCashierRole)cashier).setHost((THostRole)host); 
				                ((TCookRole) cook).setCashier((TCashierRole)cashier);
//                                
                }
        }
        
        public Role AddSharedDataWaiter() {
                switch(restNum) {
                
                case 2: Drew_WaiterRole aw = new Drew_WaiterSharedDataRole();
                                aw.setCook((Drew_Cook)cook);
                                aw.addCashier((Drew_Cashier)cashier);
                                aw.setHost((Drew_Host)host);
                                Drew_WaiterGui g = new Drew_WaiterGui(aw, 1);
                                aw.setGui(g);
                                waiters.add(aw);
                                ((Drew_Host)host).addWaiter(aw);
                                return aw;
                case 5: 
                                BWaiterRole bw = new BWaiterSharedDataRole();
                                bw.setHost((BHostRole)host);
                                bw.setCook((BCook)cook);
                                bw.setCashier((BCashier)cashier);
                                //Drew_WaiterGui g = new Drew_WaiterGui(w, , waiters.size()+1);
                                waiters.add(bw);
                                ((BHost)host).setWaiter(bw);
                                return bw;
                case 3: 
                                DWaiterRole dw = new DWaiterSharedDataRole(gui);
                                dw.msgAddCashier((DCashierRole)cashier);
                                dw.msgAddCook((DCookRole)cook);
                                dw.msgAddHost((DHostRole)host);
                                ((DHostRole)host).msgAddWaiter((DWaiterRole)dw);
                                System.out.println("DHost is "+dw);
                                waiters.add(dw);
                                return dw;
                case 4:
                                KWaiterRole kw = new KWaiterSharedDataRole(gui);
                                kw.setCook((KCookRole)cook);
                                kw.setCashier((KCashier)cashier);
                                kw.setHost((KHostRole)host);
                                ((KHostRole)host).addWaiter((KWaiterRole)kw);
                                return kw;
                                
                case 1:        
                                LWaiterRole lw = new LWaiterSharedDataRole();
                                lw.setCashier((LCashier)cashier);
                                lw.setCook((LCook)cook);
                                lw.setHost((LHost)host);
                                ((LHost)host).addWaiter(lw);
                                waiters.add(lw);
                                return lw;
                
                case 6:
                                TWaiterRole tw = new TWaiterSharedDataRole();
                                tw.setCashier((TCashierRole)cashier); 
                                tw.setCook((TCookRole)cook);
                                tw.setHost((THostRole)host);
                                ((THostRole)host).addWaiter(tw); 
                                waiters.add(tw);
                                return tw; 
                                
                default: return null;
                }
        }
        public Role AddNormalWaiter() {
switch(restNum) {
			
			case 2: Drew_WaiterRole aw = new Drew_WaiterNormalRole();
					aw.setCook((Drew_Cook)cook);
					aw.addCashier((Drew_Cashier)cashier);
					aw.setHost((Drew_Host)host);
                    Drew_WaiterGui g = new Drew_WaiterGui(aw, 1);
                    aw.setGui(g);
					//Drew_WaiterGui g = new Drew_WaiterGui(w, , waiters.size()+1);
					waiters.add(aw);
					((Drew_Host)host).addWaiter(aw);
					return aw;
			case 5: 
					BWaiterRole bw = new BWaiterNormalRole();
					bw.setHost((BHostRole)host);
					bw.setCook((BCook)cook);
					bw.setCashier((BCashier)cashier);
					//Drew_WaiterGui g = new Drew_WaiterGui(w, , waiters.size()+1);
					waiters.add(bw);
					((BHost)host).setWaiter(bw);
					return bw;
			case 3: 
					DWaiterRole dw = new DWaiterNormalRole(gui);
					dw.msgAddCashier((DCashierRole)cashier);
					dw.msgAddCook((DCookRole)cook);
					dw.msgAddHost((DHostRole)host);
					((DHostRole)host).msgAddWaiter((DWaiterRole)dw);
					System.out.println("DHost is "+dw);
					waiters.add(dw);
					return dw;
			case 4:
					KWaiterRole kw = new KWaiterNormalRole(gui);
					kw.setCook((KCookRole)cook);
					kw.setCashier((KCashier)cashier);
					kw.setHost((KHostRole)host);
					((KHostRole)host).addWaiter((KWaiterRole)kw);
					return kw;
					
			case 1:	
					LWaiterRole lw = new LWaiterNormalRole();
					lw.setCashier((LCashier)cashier);
					lw.setCook((LCook)cook);
					lw.setHost((LHost)host);
					((LHost)host).addWaiter(lw);
					waiters.add(lw);
					return lw;
			
			case 6:
					TWaiterRole tw = new TWaiterRole();
					tw.setCashier((TCashierRole)cashier); 
					tw.setCook((TCookRole)cook);
					tw.setHost((THostRole)host);
					((THostRole)host).addWaiter(tw); 
					waiters.add(tw);
					return tw; 
					
			default: return null;
			}


        }
        public Role AddCustomer() {
                switch(restNum) {
                
                        case 2: Drew_CustomerRole ac = new Drew_CustomerRole();
                        ac.setHost((Drew_HostRole)host);
                        customers.add(ac);
                        return ac;
                        
                        case 5: BCustomerRole bc = new BCustomerRole();
                        bc.setCashier((BCashierRole)cashier);
                        bc.setHost((BHostRole)host);
                        customers.add(bc);
                        return bc;
                        
                        case 3: DCustomerRole dc = new DCustomerRole(gui);
                        dc.setCashier((DCashierRole)cashier);
                        dc.setHost((DHostRole)host);
                        customers.add(dc);
                        return dc;
                        
                        case 4: KCustomerRole kc = new KCustomerRole(gui);
                        kc.setHost((KHostRole)host);
                        kc.setCashier((KCashierRole)cashier);
                        customers.add(kc);
                        return kc;
                                
                        case 1:        LCustomerRole lc = new LCustomerRole();
                        lc.setCashier((LCashierRole)cashier);
                        lc.setHost((LHostRole)host);
                        customers.add(lc);
                        return lc;
                        
                        case 6:        TCustomerRole tc = new TCustomerRole();
                        tc.setCashier((TCashierRole)cashier);
                        tc.setHost((THostRole)host);
                        customers.add(tc);
                        return tc;
                                
                        
                        default: return null;

                        
                }
        }
}
    
    

}
