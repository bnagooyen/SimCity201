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
import simcity.Transportation.BusAgent;
import simcity.Transportation.BusStopAgent;
import simcity.Transportation.CarAgent;
import simcity.Transportation.DeliveryTruckAgent;
import simcity.gui.DGui.DCookGui;
import simcity.gui.DGui.DWaiterGui;
import simcity.housing.gui.ResidentGui;
import simcity.housing.gui.TenantGui;
import simcity.interfaces.BankLoanOfficer;
import simcity.interfaces.BankManager;
import simcity.interfaces.BankTeller;
import simcity.interfaces.DCook;
import simcity.interfaces.Drew_Cook;
import simcity.interfaces.KCashier;
import simcity.interfaces.RestaurantCashier;
import simcity.PersonAgent;

import javax.swing.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class SimCityPanel extends JPanel {

	Scanner in;
	public static final int NUMAPTS = 12;
	public static final int NUMHOUSES = 15;
	public int houseNumCounter=1;
	public int ApartmentsPerLandlord;
	public int aptNumCounter=1;
	public char aptLetCounter='A';

	private ArrayList<RestaurantPlace> myRestaurants=new ArrayList<RestaurantPlace>();
	public Map<String, BusStopAgent> busStops=new HashMap<String, BusStopAgent>();

	public ArrayList<BankPlace> myBanks = new ArrayList<BankPlace>();
	public ArrayList<MarketPlace> myMarkets = new ArrayList<MarketPlace>();

	// make restaurants w/roles
	RestaurantPlace DrewRestaurant;
	RestaurantPlace BRestaurant;
	RestaurantPlace DRestaurant;
	RestaurantPlace KRestaurant;
	RestaurantPlace LRestaurant;
	RestaurantPlace TRestaurant;

	// make bank
	BankPlace bank1=null;
	BankPlace bank2 = null;
	// make market 
	MarketPlace market1=null;
	//FOR TESTING WORK
	boolean first=true;


	/********************** make directory********************/
	public  Map<String, Business> directory = new HashMap<String, Business>();
	public Map<String, Integer> storebalance = new HashMap<String, Integer>();
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
	//private Vector<DWaiterRole> waiters = new Vector<DWaiterRole>();
	private Vector<DCustomerRole> customers = new Vector<DCustomerRole>();

	//Drews Restaurant
	/*private Vector<Drew_WaiterRole> Drew_waiters = new Vector<Drew_WaiterRole>();
	private Vector<Drew_CustomerRole> Drew_customers = new Vector<Drew_CustomerRole>();
	private Drew_CashierRole Drew_cashier = new Drew_CashierRole();
	private Drew_WaiterRole Drew_waiter = new Drew_WaiterNormalRole();
	private Drew_CookRole Drew_cook = new Drew_CookRole();
	private Drew_HostRole Drew_host = new Drew_HostRole();
	private Drew_CustomerRole Drew_customer = new Drew_CustomerRole();
	private Drew_CookGui Drewcookgui;*/

	//Linda's Restaurant
	/*private Vector<LWaiterRole> Lwaiters = new Vector<LWaiterRole>();
	private Vector<LCustomerRole> Lcustomers = new Vector<LCustomerRole>();
	private LCashierRole Lcashier = new LCashierRole();
	private LCookRole Lcook = new LCookRole();
	private LCookGui LcookGui = new LCookGui(Lcook, "LcookGui");
	private LHostRole Lhost = new LHostRole();*/


	//Brian's Restaurant
	/*private Vector<BWaiterRole> BWaiters= new Vector <BWaiterRole>();
	private Vector<BCustomerRole> Bcustomers= new Vector<BCustomerRole>();
	private BCashierRole Bcashier= new BCashierRole();
	private BCookRole Bcook=new BCookRole();
	private BHostRole BHost=new BHostRole();*/

	






	//Tiff's Restaurant
	/*private Vector<TWaiterRole> Twaiters = new Vector<TWaiterRole>();
	private Vector<TCustomerRole> Tcustomers = new Vector<TCustomerRole>();
	private TCustomerRole tCustomer = new TCustomerRole();
	private TCashierRole tCashier = new TCashierRole(); 
	private TWaiterRole tWaiter = new TWaiterRole(); 
	private TWaiterSharedDataRole tsWaiter = new TWaiterSharedDataRole(); 
	private TCookRole tCook = new TCookRole();
	private TCookGui tCookGui = new TCookGui(tCook);
	private THostRole tHost = new THostRole();*/

	private final int numMarkets = 4;
	private final int numBanks = 2;
	//private Vector<DMarketAgent> markets = new Vector<DMarketAgent>();

	private JPanel restLabel = new JPanel();
	//customer drop down!
	//private ListPanel customerPanel = new ListPanel(this, "Customers");
	//private ListPanel waiterPanel = new ListPanel(this, "Waiters");
	//private JPanel group = new JPanel();

	private SimCityGui gui; //reference to main gui



	//Transport
	BusAgent bus=new BusAgent();
	BusGui bgui=new BusGui(bus, gui);
	BusStopAgent bs1;
	BusStopAgent bs2;
	BusStopAgent bs3;
	BusStopAgent bs4;

	//Person

	// market people workers
	// private MarketManagerRole manager;
	// private MarketCashierRole mcashier;
	// private InventoryBoyRole ib;

	//Bank people workers
	private BankManagerRole Bmanager;
	private BankTellerRole Bteller;
	private BankLoanOfficerRole Bloanofficer;


	public SimCityPanel(SimCityGui gui) {
		this.gui = gui;


		//Populate the map for balancing people
		storebalance.put("Bank Manager", 0);
		storebalance.put("Bank Teller", 0);
		storebalance.put("Bank Loan Officer", 0);
		storebalance.put("Market Manager", 0);
		storebalance.put("Inventory Person", 0);
		storebalance.put("Market Cashier", 0);

		for(int i=1; i<=numBanks; i++ ) {
			myBanks.add(new BankPlace(i));
		}

		for(int i=1; i<=numMarkets; i++) {
			myMarkets.add(new MarketPlace(i));
		}

		DrewRestaurant = new RestaurantPlace(2);
		BRestaurant = new RestaurantPlace(5);
		DRestaurant = new RestaurantPlace(3);
		KRestaurant = new RestaurantPlace(4);
		LRestaurant = new RestaurantPlace(1);
		TRestaurant = new RestaurantPlace(6);


		myRestaurants.add(LRestaurant);
		myRestaurants.add(DrewRestaurant);
		myRestaurants.add(DRestaurant);
		myRestaurants.add(KRestaurant);
		myRestaurants.add(BRestaurant);
		myRestaurants.add(TRestaurant);


		addPerson("Person", "Bman1", 100.0, "Bank Manager", "Apartment", "Walk"); 
		addPerson("Person", "Bman2", 100.0, "Bank Manager", "Apartment", "Walk"); 
		addPerson("Person", "Mman1", 100.0, "Market Manager", "Apartment", "Walk"); 
		addPerson("Person", "Mman2", 100.0, "Market Manager", "Apartment", "Walk"); 
		addPerson("Person", "Mman3", 100.0, "Market Manager", "Apartment", "Walk"); 
		addPerson("Person", "Mman4", 100.0, "Market Manager", "Apartment", "Walk"); 
		addPerson("Person", "Dhost", 100.0, "Doreen Host", "Apartment", "Walk"); 
		addPerson("Person", "Drewhost", 100.0, "Drew Host", "Apartment", "Walk"); 
		addPerson("Person", "Thost", 100.0, "Tiffany Host", "Apartment", "Walk"); 
		addPerson("Person", "BHost", 100.0, "Brian Host", "Apartment", "Walk"); 
		addPerson("Person", "LHost", 100.0, "Linda Host", "Apartment", "Walk"); 
		addPerson("Person", "KHost", 100.0, "Kim Host", "Apartment", "Walk"); 
		
		//this.LoadScenario("config1");

		/*PersonAgent bManagerPerson = new PersonAgent("BankManager");
        bManagerPerson.hungerLevel = 0;
        bManagerPerson.SetJob(myBanks.get(0).bankManager, "Bank 1");
        myBanks.get(0).bankManager.myPerson = bManagerPerson;*/

		//Bteller.isActive=true;
		//~~~PersonAgent btellerPerson = new PersonAgent("Bankteller");     //REMEMBER TO START THREAD!!!!
		//~~btellerPerson.hungerLevel = 0;
		//~~~btellerPerson.SetJob(bank.bankTeller, "Bank 1");
		//~~~bank.bankTeller.myPerson=btellerPerson;
		//Bteller.manager=Bmanager;
		//Bteller.myPerson = btellerPerson;

		//Bloanofficer.isActive=true;
		/* PersonAgent bloanofficerPerson = new PersonAgent("Bankloanofficer");
        bloanofficerPerson.hungerLevel = 0;
        bloanofficerPerson.SetJob(myBanks.get(0).loanOfficer, "Bank 1");
        //Bloanofficer.manager=Bmanager;
        myBanks.get(0).loanOfficer.myPerson = bloanofficerPerson;*/

		//Start Threads
		//bManagerPerson.startThread();
		//bloanofficerPerson.startThread();
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
		//Transport
		bs1=new BusStopAgent();
		bs2=new BusStopAgent();
		bs3=new BusStopAgent();
		bs4=new BusStopAgent();

		bs1.startThread();
		bs2.startThread();
		bs3.startThread();
		bs4.startThread();

		busStops.put("Stop1", bs1);
		busStops.put("Stop2", bs2);
		busStops.put("Stop3", bs3);
		busStops.put("Stop4", bs4);

		bus.startThread();
		bus.setGui(bgui);
		gui.city.addGui(bgui);
		bus.setStops(busStops);


		//Market
		// manager = new MarketManagerRole(gui);
		// mcashier = new MarketCashierRole(gui);
		// ib = new InventoryBoyRole(gui);

		/*myMarkets.get(0).mManager.isActive = true;
        PersonAgent mManagerPerson = new PersonAgent("Manager");
        mManagerPerson.hungerLevel = 0;
        mManagerPerson.SetJob(myMarkets.get(0).mManager, "Market 1");
        myMarkets.get(0).mManager.myPerson = mManagerPerson;*/
		//        
		//        cook.msgAddMarket(manager);
		//        cook.setMonitor(host.getMonitor());
		//        
		/* myMarkets.get(0).mCashier.isActive = true;
        PersonAgent mCashierPerson = new PersonAgent("mCashier");
        mCashierPerson.hungerLevel = 0;
        mCashierPerson.SetJob(myMarkets.get(0).mCashier, "Market 1");
        myMarkets.get(0).mCashier.myPerson = mCashierPerson;*/

		/*myMarkets.get(0).ib.isActive = true;
        PersonAgent ibPerson = new PersonAgent("ib");
        ibPerson.hungerLevel = 0;
        ibPerson.SetJob(myMarkets.get(0).ib, "Market 1");
        myMarkets.get(0).ib.myPerson = ibPerson;*/

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
		//   mManagerPerson.startThread();
		//   mCashierPerson.startThread();
		//   ibPerson.startThread();
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
		/*  for(RestaurantPlace r: myRestaurants) {
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
        }*/






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

		/******************* populate directory*******************************/
		/*
        directory.put("Market 1", market1);
        directory.put("Market 2", market2);
        directory.put("Market 3", market3);
        directory.put("Market 4", market4);
		 */

		for(MarketPlace m: myMarkets) {
			directory.put("Market "+ Integer.toString(m.num), m);
			//System.err.println("Market "+ Integer.toString(m.num));
		}
		//directory.put("Bank", bank);
		for(BankPlace b: myBanks) {
			directory.put("Bank "+ Integer.toString(b.num), b);
		}

		/*
        directory.put("Bank 1", bank1);
        directory.put("Bank 2", bank2);
<<<<<<< HEAD
        */
        
        directory.put("Restaurant 5", BRestaurant);
        directory.put("Restaurant 3", DRestaurant);
        directory.put("Restaurant 2", DrewRestaurant);
        directory.put("Restaurant 4", KRestaurant);
        directory.put("Restaurant 1", LRestaurant);
        directory.put("Restaurant 6", TRestaurant);
        
        // set nearest bus stop of all the buildings
        directory.get("Market 1").nearestBusStop = bs1;
        directory.get("Market 2").nearestBusStop = bs4;
        directory.get("Market 3").nearestBusStop = bs2;
        directory.get("Market 4").nearestBusStop = bs3;
        directory.get("Bank 1").nearestBusStop = bs4;
        directory.get("Bank 2").nearestBusStop = bs2;
        directory.get("Restaurant 1").nearestBusStop = bs1;
        directory.get("Restaurant 3").nearestBusStop = bs1;
        directory.get("Restaurant 4").nearestBusStop = bs2;
        directory.get("Restaurant 5").nearestBusStop = bs3;
        directory.get("Restaurant 6").nearestBusStop = bs3;
        directory.get("Restaurant 2").nearestBusStop = bs4;
        
        // set all market places to not down
        Iterator it = directory.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Business b = (Business) pairs.getValue();
            b.down = false;
        }
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
	/*public Object showInfo(String type, String name) {

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
	}*/



	public void LoadScenario(String type){
		people.clear();
		String name, role, transport, houseOrApt; double money;
		houseNumCounter=1;
		aptNumCounter=1;
		aptLetCounter='A';
		try {	
			
			in  = new Scanner(new FileReader("config"+File.separator+type+".txt"));
			
			in.next();
			int numItems = in.nextInt();
			//clear input template;
			in.next();
			in.next();
			in.next();
			in.next();
			in.next();
			in.next();
			for(int i=0; i<numItems; i++) {
				name = in.next();
				money = in.nextDouble();
				role = in.next().trim();
				transport = in.next().trim();
				houseOrApt = in.next().trim();
						
				addPerson("Person", name, money, role, houseOrApt, transport);
			}
			gui.AddPeople(people);
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			CarAgent car=new CarAgent();
			CarGui cgui=new CarGui(car, gui);
			car.setGui(cgui);
			car.startThread();
			gui.city.addGui(cgui);
			p.setCar(car);
			p.setStops(busStops);

			//DCustomerRole restCustomer = new DCustomerRole(gui);
			//restCustomer.host=host;
			//restCustomer.cashier=cashier;
			ArrayList<Role> tempRoles = GenerateAllCustomerRoles();
			for(Role r: tempRoles) {
				r.myPerson=p;
				r.isActive=false;
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



			//Add Job 

			//Managers&Hosts
			if(role.equals("Inventory Person")){
//				this.LoadScenario("config1");
			}
			if(role.equals("Bank Manager")){
				Integer bankChoice;
				bankChoice=(storebalance.get(role)%2);
				storebalance.put(role, storebalance.get(role)+1);
				p.SetJob(myBanks.get(bankChoice).bankManager, "Bank "+Integer.toString(bankChoice+1));
				myBanks.get(bankChoice).bankManager.myPerson = p;
			}
			if(role.equals("Market Manager")){
				Integer marketChoice;
				marketChoice=(storebalance.get(role)%4);
				storebalance.put(role, storebalance.get(role)+1);
				p.SetJob(myMarkets.get(marketChoice).mManager, "Market "+Integer.toString(marketChoice+1));
				myMarkets.get(marketChoice).mManager.myPerson = p;
				for(MarketPlace m:myMarkets){
					if(m.mManager.myPerson==null) System.out.println("(in role.equals(Market Manager) Simcity Pnale) NO PERSON IN "+m.num);
				}
			}
			if(role.equals("Doreen Host")){
				p.SetJob(myRestaurants.get(2).host, "Restaurant 3");
				myRestaurants.get(2).host.myPerson=p;
			}
			if(role.equals("Drew Host")){
				p.SetJob(myRestaurants.get(1).host, "Restaurant 2");
				myRestaurants.get(1).host.myPerson=p;
			}
			if(role.equals("Linda Host")){
				p.SetJob(myRestaurants.get(0).host, "Restaurant 1");
				myRestaurants.get(0).host.myPerson=p;
			}
			if(role.equals("Kim Host")){
				p.SetJob(myRestaurants.get(3).host, "Restaurant 4");
				//p.SetJob(((RestaurantPlace)directory.get("Restaurant 4")).host, "Restaurant 4");
				//((RestaurantPlace)directory.get("Restaurant 4")).host.myPerson = p;
				//System.out.println("Restaurant 4's host's person is "+ ((RestaurantPlace)directory.get("Restaurant 4")).host);
				//System.out.println("it should be "+p.getName());
				myRestaurants.get(3).host.myPerson=p;
			}
			if(role.equals("Brian Host")){
				p.SetJob(myRestaurants.get(4).host, "Restaurant 5");
				myRestaurants.get(4).host.myPerson=p;
			}
			if(role.equals("Tiffany Host")){
				p.SetJob(myRestaurants.get(5).host, "Restaurant 6");
				myRestaurants.get(5).host.myPerson=p;
			}

			//All other people

			//This adds waiters to city. Adds waiter to restaurant with fewest waiters
			//First one is a normal waiter, subsequent waiters are shared data

			if(role.equals("Waiter")){

				//Load balances waiters, rest is restaurant with fewest waiters
				RestaurantPlace rest=myRestaurants.get(0);
				for(RestaurantPlace r:myRestaurants){
					if(r.waiters.size()<rest.waiters.size()){
						rest=r;
					}
				}
				System.out.println("QWERTYUI ------ "+rest.restNum);

				if(rest.restNum==1){
					LWaiterRole LW;
					if(rest.waiters.size()==0){
						LW=(LWaiterRole) LRestaurant.AddNormalWaiter();
					}
					else{
						LW=(LWaiterRole) LRestaurant.AddSharedDataWaiter();
					}
					LW.myPerson=p;
					p.SetJob(LW, "Restaurant 1");
				}
				else if(rest.restNum==2){
					Drew_WaiterRole DW;
					if(rest.waiters.size()==0){
						DW=(Drew_WaiterRole) DrewRestaurant.AddNormalWaiter();
					}
					else{
						DW=(Drew_WaiterRole) DrewRestaurant.AddNormalWaiter();
					}
					DW.myPerson=p;
					p.SetJob(DW,"Restaurant 2");
				}
				else if(rest.restNum==3){
					DWaiterRole DW;
					if(rest.waiters.size()==0){
						DW=(DWaiterRole) DRestaurant.AddNormalWaiter();
					}
					else{
						DW=(DWaiterRole) DRestaurant.AddSharedDataWaiter();
					}
					DW.myPerson=p;
					p.SetJob(DW,"Restaurant 3");
				}
				else if(rest.restNum==4){
					KWaiterRole KW;
					if(rest.waiters.size()==0){
						KW=(KWaiterRole) KRestaurant.AddNormalWaiter();
					}
					else{
						KW=(KWaiterRole) KRestaurant.AddSharedDataWaiter();
					}
					KW.myPerson=p;
					p.SetJob(KW,"Restaurant 4");
				}
				else if(rest.restNum==5){
					BWaiterRole BW;
					if(rest.waiters.size()==0){
						BW=(BWaiterRole) BRestaurant.AddNormalWaiter();
					}
					else{
						BW=(BWaiterRole) BRestaurant.AddSharedDataWaiter();
					}
					BW.myPerson=p;
					p.SetJob(BW,"Restaurant 5");
				}
				else if(rest.restNum==6){
					System.out.println("qwertyuiop");
					TWaiterRole TW;
					if(rest.waiters.size()==0){
						TW=(TWaiterRole) TRestaurant.AddNormalWaiter();
					}
					else{
						TW=(TWaiterRole) TRestaurant.AddSharedDataWaiter();
					}
					TW.myPerson=p;
					p.SetJob(TW,"Restaurant 6");
				}
			}


			//Add Restaurant Cashiers
			if(role.equals("RestaurantCashier1")){
				LCashierRole LC= (LCashierRole) LRestaurant.cashier;
				LC.myPerson=p;
				p.SetJob(LC, "Restaurant 1");
			}
			if(role.equals("RestaurantCashier2")){
				Drew_CashierRole DC= (Drew_CashierRole) DrewRestaurant.cashier;
				DC.myPerson=p;
				p.SetJob(DC, "Restaurant 2");
			}
			if(role.equals("RestaurantCashier3")){
				DCashierRole DC= (DCashierRole) DRestaurant.cashier;
				DC.myPerson=p;
				p.SetJob(DC, "Restaurant 3");
			}
			if(role.equals("RestaurantCashier4")){
				KCashierRole KC= (KCashierRole) KRestaurant.cashier;
				KC.myPerson=p;
				p.SetJob(KC, "Restaurant 4");
			}
			if(role.equals("RestaurantCashier5")){
				BCashierRole BC= (BCashierRole) BRestaurant.cashier;
				BC.myPerson=p;
				p.SetJob(BC, "Restaurant 5");
			}
			if(role.equals("RestaurantCashier6")){
				TCashierRole TC= (TCashierRole) TRestaurant.cashier;
				TC.myPerson=p;
				p.SetJob(TC, "Restaurant 6");
			}

			//Add Cooks
			if(role.equals("Cook1")){
				LCookRole LC= (LCookRole) LRestaurant.cook;
				LC.myPerson=p;
				p.SetJob(LC, "Restaurant 1");
			}
			if(role.equals("Cook2")){
				Drew_CookRole DC= (Drew_CookRole) DrewRestaurant.cook;
				DC.myPerson=p;
				p.SetJob(DC, "Restaurant 2");
			}
			if(role.equals("Cook3")){
				DCookRole DC= (DCookRole) DRestaurant.cook;
				DC.myPerson=p;
				p.SetJob(DC, "Restaurant 3");
			}
			if(role.equals("Cook4")){
				KCookRole KC= (KCookRole) KRestaurant.cook;
				KC.myPerson=p;
				KC.setHost((KHostRole) KRestaurant.host);
				KC.setCashier((RestaurantCashier) KRestaurant.cashier);
				p.SetJob(KC, "Restaurant 4");
			}
			if(role.equals("Cook5")){
				BCookRole BC= (BCookRole) BRestaurant.cook;
				BC.myPerson=p;
				p.SetJob(BC, "Restaurant 5");
			}
			if(role.equals("Cook6")){
				TCookRole TC= (TCookRole) TRestaurant.cook;
				TC.myPerson=p;
				p.SetJob(TC, "Restaurant 6");
			}




			//Add Market Cashiers
			if(role.equals("MarketCashier1")){
				MarketCashierRole LC= (MarketCashierRole) myMarkets.get(0).mCashier;
				LC.myPerson=p;
				p.SetJob(LC, "Market 1");
			}
			if(role.equals("MarketCashier2")){
				MarketCashierRole LC= (MarketCashierRole) myMarkets.get(1).mCashier;
				LC.myPerson=p;
				p.SetJob(LC, "Market 2");
			}
			if(role.equals("MarketCashier3")){
				MarketCashierRole LC= (MarketCashierRole) myMarkets.get(2).mCashier;
				LC.myPerson=p;
				p.SetJob(LC, "Market 3");
			}
			if(role.equals("MarketCashier4")){
				MarketCashierRole LC= (MarketCashierRole) myMarkets.get(3).mCashier;
				LC.myPerson=p;
				p.SetJob(LC, "Market 4");
			}




			//Add Inventory Boys
			if(role.equals("InventoryBoy1")){
				InventoryBoyRole LC= (InventoryBoyRole) myMarkets.get(0).ib;
				LC.myPerson=p;
				p.SetJob(LC, "Market 1");
			}
			if(role.equals("InventoryBoy2")){
				InventoryBoyRole LC= (InventoryBoyRole) myMarkets.get(1).ib;
				LC.myPerson=p;
				p.SetJob(LC, "Market 2");
			}
			if(role.equals("InventoryBoy3")){
				InventoryBoyRole LC= (InventoryBoyRole) myMarkets.get(2).ib;
				LC.myPerson=p;
				p.SetJob(LC, "Market 3");
			}
			if(role.equals("InventoryBoy4")){
				InventoryBoyRole LC= (InventoryBoyRole) myMarkets.get(3).ib;
				LC.myPerson=p;
				p.SetJob(LC, "Market 4");
			}


			//Add Bank Tellers
			/*if(role.equals("Bank Teller")){
	    			BankTellerRole BT= new BankTellerRole(gui);
	    			BT.setManager(bank1.bankManager);
	    			BT.myPerson=p;
	    			p.SetJob(BT,"Bank 1");
	    			p.myJob.isActive=false;
	    			p.hungerLevel=0;
	    		}*/
			if(role.equals("BankTeller1")){
				BankTellerRole BT=  myBanks.get(0).bankTeller;
				BT.myPerson=p;
				p.SetJob(BT,"Bank 1");
			}
			if(role.equals("BankTeller2")){
				BankTellerRole BT=  myBanks.get(1).bankTeller;
				BT.myPerson=p;
				p.SetJob(BT,"Bank 2");
			}

			//Add Bank Loan Officers
			/*if(role.equals("Bank Loan Officer")){
	    			BankTellerRole BT= new BankTellerRole(gui);
	    			BT.setManager(bank1.bankManager);
	    			BT.myPerson=p;
	    			p.SetJob(BT,"Bank 1");
	    			p.myJob.isActive=false;
	    			p.hungerLevel=0;
	    		}*/
			if(role.equals("BankLoanOfficer1")){
				BankLoanOfficerRole BT=  myBanks.get(0).loanOfficer;
				BT.myPerson=p;
				p.SetJob(BT,"Bank 1");
			}
			if(role.equals("BankLoanOfficer2")){
				BankLoanOfficerRole BT=  myBanks.get(1).loanOfficer;
				BT.myPerson=p;
				p.SetJob(BT,"Bank 2");
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

			if(p.myJob!=null)p.myJob.isActive=false;
		


			//TAKE THIS TIME UPDATE AWAY, JUST FOR TESTING 
			if(p.myJob!=null)p.msgTimeUpdate(p.myJob.startHour-1);
			else{
				System.out.println(role+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			}

			PersonGui g = new PersonGui(p, gui);
			//g.setPresent(true);
			p.setGui(g);
			gui.city.addGui(g);
			p.startThread();
			people.add(p);
		}
	}

	/**************************ROLE FACTORY******************************/

	public class Business {
		public String name;
		public int x;
		public int y;
		public boolean down;
		public BusStop nearestBusStop;

	}
	//waiter needs host
	//customer needs hot
	//waiter needs cook setCook(cook)


	private ArrayList<Role> GenerateAllCustomerRoles() {
		ArrayList<Role> myCustomerRoles= new ArrayList<Role>();
		/*******turn this into a loop to make more efficient*********/

		//myCustomerRoles.add(bank.addCustomer());
		//myCustomerRoles.add(market1.addCustomer());
		for(BankPlace b: myBanks) {
			myCustomerRoles.add(b.addCustomer());
		}
		for(MarketPlace m: myMarkets) {
			myCustomerRoles.add(m.addCustomer());
		}

		// myCustomerRoles.add(bank1.addCustomer());
		// myCustomerRoles.add(market1.addCustomer());

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
		int num;
		public MarketPlace(int num) {
			this.num = num;
			ib = new InventoryBoyRole(gui, num);
			mManager = new MarketManagerRole(gui);
			mCashier = new MarketCashierRole(gui,num);
			mCustomers = new ArrayList<MarketCustomer>();
			truck = new DeliveryTruckAgent(mManager);

			mManager.dTruck = (DeliveryTruck)truck;

			ib.setMarketCashier(mCashier);
			ib.setMarketManager(mManager);

			mCashier.setInventoryBoy(ib);
			mCashier.setMarketManager(mManager);
			mCashier.setMarketManager(mManager);

			dtruck = new DeliveryTruckAgent(mManager);
			mManager.setDeliveryTruck(dtruck);
			dtruck.startThread();

		}

		public MarketCustomerRole addCustomer() {
			MarketCustomerRole c = new MarketCustomerRole(gui, num);
			c.setMarketManager(mManager);
			c.num = num;
			mCustomers.add(c);
			return c;
		}
	}

	public class BankPlace extends Business {
		//JPanel animationPanel = new JPanel();

		public BankLoanOfficerRole loanOfficer;
		public BankManagerRole bankManager;
		public BankTellerRole bankTeller;
		public BankRobberRole robber;
		int num;


		ArrayList<BankCustomerRole> bankCustomers;

		public BankPlace(int num) {
			this.num = num;
			loanOfficer = new BankLoanOfficerRole(gui,num);
			bankManager = new BankManagerRole(gui, num);
			bankTeller = new BankTellerRole(gui,num);
			bankCustomers = new ArrayList<BankCustomerRole>();

			loanOfficer.setManager(bankManager);
			bankTeller.setManager(bankManager);

		}
		public BankCustomerRole addCustomer() {
			BankCustomerRole b = new BankCustomerRole(gui,num);
			b.setManager(bankManager);
			b.num=num;
			bankCustomers.add(b);
			return b;
		}
	}

	class RestaurantPlace extends Business {
		public int restNum;
		public Role host = null;
		public Role cook = null;
		public Role cashier = null;
		public ArrayList<Role> waiters = new ArrayList<Role>();
		ArrayList<Role> customers = new ArrayList<Role>();
		RestaurantPlace(int restNum) {
			this.restNum = restNum;
			switch(restNum) {


			case 2: host = new Drew_HostRole();
			cook = new Drew_CookRole(gui);
			((Drew_CookRole) cook).setHost((Drew_HostRole) host);
			for(MarketPlace m: myMarkets) {
				((Drew_CookRole)cook).addMarket(m.mManager);
			}
			((Drew_HostRole)host).setCook((Drew_Cook)cook);
			cashier = new Drew_CashierRole();
			((Drew_CashierRole) cashier).setHost(host);
			break;
			case 5: host = new BHostRole();
			cook = new BCookRole();
			((BHostRole)host).setCook((BCookRole)cook);
			
			
			cashier = new BCashierRole();
			((BHostRole)host).setCashier((BCashierRole)cashier);
			((BCookRole) cook).setHost((BHostRole)host);
			((BCookRole)cook).setCashier((BCashierRole)cashier);
			for(MarketPlace m: myMarkets) {
				((BCookRole)cook).addMarket(m.mManager);
			}
			
			((BCashierRole)cashier).setHost((BHostRole)host);
			((BCashierRole)cashier).setCook((BCookRole)cook);
			
			break;
			case 3: host = new DHostRole();
			cook = new DCookRole(gui);
			for(MarketPlace m: myMarkets) {
				((DCookRole)cook).msgAddMarket(m.mManager);
			}
			cashier = new DCashierRole();
			((DHostRole)host).addCook((DCookRole)cook);
			((DCookRole) cook).AddHost((DHostRole)host);
			((DCookRole)cook).AddCashier((DCashier)cashier);
			((DCashierRole)cashier).AddCook((DCook)cook);
			((DCashierRole)cashier).AddHost((DHostRole)host);
			break;
			case 4: host = new KHostRole();
			cook = new KCookRole(gui);
			for(MarketPlace m: myMarkets) {
				((KCookRole)cook).addMarket(m.mManager);
			}
			cashier = new KCashierRole();
			((KHostRole)host).setCook((KCookRole) cook);
			((KHostRole)host).setCashier((KCashierRole) cashier);
			((KCookRole)cook).setHost((KHostRole) host);
			((KCookRole)cook).setCashier((KCashierRole) cashier);
			((KCashierRole)cashier).setHost((KHostRole) host);
			((KCashierRole)cashier).setCook((KCookRole) cook);
			break;
			case 1:        host = new LHostRole();
			cook = new LCookRole(gui);
			for(MarketPlace m: myMarkets) {
				((LCookRole)cook).addMarket(m.mManager);
			}
			System.out.println("LCookRole is "+cook);
			cashier = new LCashierRole();
			((LHostRole) host).setCook((LCook)cook);
			((LCookRole) cook).setCashier((LCashierRole)cashier);
			((LCookRole) cook).setHost((LHostRole)host);
			((LCashierRole)cashier).setHost((LHostRole)host);
			((LCashierRole)cashier).setCook((LCookRole)cook);
			break;
			case 6: host = new THostRole(); 
			cook = new TCookRole(gui);
			for(MarketPlace m: myMarkets) {
				((TCookRole)cook).addMarket(m.mManager);
			}
			cashier = new TCashierRole();
			((THostRole) host).setCook((TCookRole)cook);
			((THostRole) host).setCashier((TCashierRole)cashier); 
			((TCashierRole)cashier).setHost((THostRole)host);
			((TCashierRole)cashier).setCook((TCookRole)cook);
			((TCookRole) cook).setCashier((TCashierRole)cashier);
			((TCookRole) cook).setHost((THostRole)host);                          
			}
		}

		public Role AddSharedDataWaiter() {
			switch(restNum) {

			case 2: Drew_WaiterRole aw = new Drew_WaiterSharedDataRole(gui);
			aw.setCook((Drew_Cook)cook);
			((Drew_WaiterSharedDataRole)aw).setMonitor(Drew_Cook.theMonitor);
			aw.addCashier((Drew_Cashier)cashier);
			aw.setHost((Drew_Host)host);
			waiters.add(aw);
			System.out.println("zxcvbnm"+host);
			return aw;
			
			case 5: 
				BWaiterRole bw = new BWaiterSharedDataRole(gui);
				bw.setHost((BHostRole)host);
				bw.setCook((BCook)cook);
				bw.setCashier((BCashier)cashier);
				waiters.add(bw);
				//((BHost)host).setWaiter(bw);
				return bw;
			case 3: 
				DWaiterRole dw = new DWaiterSharedDataRole(gui);
				dw.msgAddCashier((DCashierRole)cashier);
				dw.msgAddCook((DCookRole)cook);
				dw.msgAddHost((DHostRole)host);
				//((DHostRole)host).msgAddWaiter((DWaiterRole)dw);
				System.out.println("DHost is "+dw);
				waiters.add(dw);
				return dw;
			case 4:
				KWaiterRole kw = new KWaiterSharedDataRole(gui);
				kw.setCook((KCookRole)cook);
				kw.setCashier((RestaurantCashier)cashier);
				kw.setHost((KHostRole)host);
				waiters.add(kw);
				//((KHostRole)host).addWaiter((KWaiterRole)kw);
				return kw;

			case 1:        
				LWaiterRole lw = new LWaiterSharedDataRole(gui);
				lw.setCashier((LCashier)cashier);
				lw.setCook((LCook)cook);
				lw.setHost((LHost)host);
				//((LHost)host).addWaiter(lw);
				waiters.add(lw);
				return lw;

			case 6:
				TWaiterRole tw = new TWaiterSharedDataRole(gui);
				tw.setCashier((TCashierRole)cashier); 
				tw.setCook((TCookRole)cook);
				tw.setHost((THostRole)host);
				//((THostRole)host).addWaiter(tw); 
				waiters.add(tw);
				return (tw); 

			default: return null;
			}
		}
		public Role AddNormalWaiter() {
			switch(restNum) {

			case 2: Drew_WaiterRole aw = new Drew_WaiterNormalRole(gui);
			aw.setCook((Drew_Cook)cook);
			aw.addCashier((Drew_Cashier)cashier);
			aw.setHost((Drew_Host)host);
			waiters.add(aw);
			return aw;
			
			case 5: 
				BWaiterRole bw = new BWaiterNormalRole(gui);
				bw.setHost((BHostRole)host);
				bw.setCook((BCook)cook);
				bw.setCashier((BCashier)cashier);
				waiters.add(bw);
				//((BHost)host).setWaiter(bw);
				return bw;
			case 3: 
				DWaiterRole dw = new DWaiterNormalRole(gui);
				dw.msgAddCashier((DCashierRole)cashier);
				dw.msgAddCook((DCookRole)cook);
				dw.msgAddHost((DHostRole)host);
				//((DHostRole)host).msgAddWaiter((DWaiterRole)dw);
				System.out.println("DHost is "+dw);
				waiters.add(dw);
				return dw;
			case 4:
				KWaiterRole kw = new KWaiterNormalRole(gui);
				kw.setCook((KCookRole)cook);
				kw.setCashier((RestaurantCashier)cashier);
				kw.setHost((KHostRole)host);
				waiters.add(kw);
				//((KHostRole)host).addWaiter((KWaiterRole)kw);
				return kw;

			case 1:	
				LWaiterRole lw = new LWaiterNormalRole(gui);
				lw.setCashier((LCashier)cashier);
				lw.setCook((LCook)cook);
				lw.setHost((LHost)host);
				//((LHost)host).addWaiter(lw);
				waiters.add(lw);
				return lw;

			case 6:
				TWaiterRole tw = new TWaiterRole(gui);
				tw.setCashier((TCashierRole)cashier); 
				tw.setCook((TCookRole)cook);
				tw.setHost((THostRole)host);
				//((THostRole)host).addWaiter(tw); 
				waiters.add(tw);
				return tw; 

			default: return null;
			}


		}
		public Role AddCustomer() {
			switch(restNum) {

			case 2: Drew_CustomerRole ac = new Drew_CustomerRole(gui);
			ac.setHost((Drew_HostRole)host);
			customers.add(ac);
			return ac;

			case 5: BCustomerRole bc = new BCustomerRole(gui);
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

			case 1:        LCustomerRole lc = new LCustomerRole(gui);
			lc.setCashier((LCashierRole)cashier);
			lc.setHost((LHostRole)host);
			customers.add(lc);
			return lc;

			case 6:        TCustomerRole tc = new TCustomerRole(gui);
			tc.setCashier((TCashierRole)cashier);
			tc.setHost((THostRole)host);
			customers.add(tc);
			return tc;


			default: return null;


			}
		}
	}
	public void startTimer() {
		Timer timer;

		class RemindTask extends TimerTask {
			int counter= 0;
			@Override
			public void run() {
				if(counter <24) {
					System.out.println("hour is " + counter);
					for(Person p: people) {
						p.msgTimeUpdate(counter);
					}
					counter++;
					if (counter == 24) {
						counter = 1;
					}
				}
			}
		}
		timer = new Timer();
		timer.schedule(new RemindTask(),
				0,        //initial delay
				1*2000);  //subsequent rate                
	}





}

