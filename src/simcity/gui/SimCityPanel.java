package simcity.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import agent.Role;
import simcity.PersonAgent;
import simcity.PersonAgent.HomeType;
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
import simcity.DRestaurant.DCashierRole;
import simcity.DRestaurant.DCookRole;
import simcity.DRestaurant.DCustomerRole;
import simcity.DRestaurant.DHostRole;
import simcity.DRestaurant.DWaiterNormalRole;
import simcity.DRestaurant.DWaiterRole;
import simcity.DRestaurant.DWaiterSharedRole;
import simcity.Drew_restaurant.Drew_CashierRole;
import simcity.Drew_restaurant.Drew_CookRole;
import simcity.Drew_restaurant.Drew_CustomerRole;
import simcity.Drew_restaurant.Drew_HostRole;
import simcity.Drew_restaurant.Drew_RestaurantOrder;
import simcity.Drew_restaurant.Drew_WaiterNormalRole;
import simcity.Drew_restaurant.Drew_WaiterRole;
import simcity.Drew_restaurant.Drew_WaiterSharedDataRole;
import simcity.Drew_restaurant.gui.Drew_WaiterGui;
import simcity.Drew_restaurant.interfaces.Drew_Cashier;
import simcity.Drew_restaurant.interfaces.Drew_Cook;
import simcity.Drew_restaurant.interfaces.Drew_Host;
import simcity.KRestaurant.KCashierRole;
import simcity.KRestaurant.KCookRole;
import simcity.KRestaurant.KCustomerRole;
import simcity.KRestaurant.KHostRole;
import simcity.KRestaurant.KWaiterNormalRole;
import simcity.KRestaurant.KWaiterRole;
import simcity.KRestaurant.KWaiterSharedDataRole;
import simcity.LRestaurant.LCashierRole;
import simcity.LRestaurant.LCookRole;
import simcity.LRestaurant.LCustomerRole;
import simcity.LRestaurant.LHostRole;
import simcity.LRestaurant.LWaiterNormalRole;
import simcity.LRestaurant.LWaiterRole;
import simcity.LRestaurant.LWaiterSharedDataRole;
import simcity.Market.InventoryBoyRole;
import simcity.Market.MarketCashierRole;
import simcity.Market.MarketCustomerRole;
import simcity.Market.MarketManagerRole;
//import simcity.SimCityPanel.Business;
import simcity.TTRestaurant.TCashierRole;
import simcity.TTRestaurant.TCookRole;
import simcity.TTRestaurant.TCustomerRole;
import simcity.TTRestaurant.THostRole;
import simcity.TTRestaurant.TWaiterRole;
import simcity.TTRestaurant.TWaiterSharedDataRole;
import simcity.Transportation.CarAgent;
import simcity.housing.LandlordRole;
import simcity.housing.RepairManRole;
import simcity.interfaces.BCashier;
import simcity.interfaces.BCook;
import simcity.interfaces.BHost;
import simcity.interfaces.BankManager;
import simcity.interfaces.DCashier;
import simcity.interfaces.DCook;
import simcity.interfaces.DHost;
import simcity.interfaces.DWaiter;
import simcity.interfaces.Host;
import simcity.interfaces.KCashier;
import simcity.interfaces.KCook;
import simcity.interfaces.KWaiter;
import simcity.interfaces.LCashier;
import simcity.interfaces.LCook;
import simcity.interfaces.LHost;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.MarketManager;
import simcity.interfaces.Person;
import simcity.interfaces.TCashier;
import simcity.interfaces.TCook;
import simcity.restaurant.interfaces.Cashier;

public class SimCityPanel extends JPanel implements MouseListener{

	public static final int NUMAPTS = 12;
	public static final int NUMHOUSES = 15;
	public static final int NUMBANKS = 2;
	public static final int NUMMARKETS = 4;
	public static final int NUMRESTAURANTS = 6;
	public int mktCounter=1;
	public int bankCounter = 1;
	public int houseNumCounter=1;
	public int ApartmentsPerLandlord;
	public int aptNumCounter=1;
	public char aptLetCounter='A';
	private SimCityGui gui;
	public Map<String, List<Location>> buildings = new HashMap<String, List<Location>>();
	public Map<String, List<Location>> busStops=new HashMap<String, List<Location>>(); //STILL BLANK
	private List<Location> restaurants = new ArrayList<Location>();
	private List<Location> banks = new ArrayList<Location>();
	private List<Location> markets = new ArrayList<Location>();
	
	private ArrayList<Person> people = new ArrayList<Person>();
	private ArrayList<MyLandlord> landlords = new ArrayList<MyLandlord>();
	static Scanner in;
	
	// make restaurants w/roles
			RestaurantPlace DrewRestaurant = new RestaurantPlace(1);
			RestaurantPlace BRestaurant = new RestaurantPlace(2);
			RestaurantPlace DRestaurant = new RestaurantPlace(3);
			RestaurantPlace KRestaurant = new RestaurantPlace(4);
			RestaurantPlace LRestaurant = new RestaurantPlace(5);
			RestaurantPlace TRestaurant = new RestaurantPlace(6);
						

			// make bank
			BankPlace bank = new BankPlace();
			
			// make market 
			MarketPlace market = new MarketPlace();
			
	public SimCityPanel(SimCityGui gui) {
		this.gui = gui;
		
		
		
		
		//*********Remember to add in delivery truck**********
		
//		// populate buildings map 
//		PersonAgent p1 = new PersonAgent("bankmanager");
//		BankManagerRole bm = new BankManagerRole(p1);
//		p1.SetJob(bm);
//		banks.add(new Bank("Bank1", bm));
//		
//		PersonAgent p2 = new PersonAgent("marketmanager");
//		MarketManagerRole mm =  new MarketManagerRole(p2);
//		p2.SetJob(mm);
//		markets.add(new Market("Market1", mm));
//
//		PersonAgent p3 = null;
//		KHostRole kh = new KHostRole(p3);
//		p3 = new PersonAgent("khost");
//		restaurants.add(new Restaurant("KRestaurant", kh, "normal"));
//
//		PersonAgent p4 = null;
//		BHostRole bh = new BHostRole(p4);
//		p4 = new PersonAgent("bhost");
//		restaurants.add(new Restaurant("BRestaurant", bh, "narmal"));
//
//		PersonAgent p5 = null;
//		Drew_HostRole drewh =  new Drew_HostRole(p5);
//		p5 = new PersonAgent("drew_host");
//		restaurants.add(new Restaurant("DrewRestaurant", drewh, "normal"));
//
//		PersonAgent p6 = null;
//		DHostRole dh = new DHostRole(p6);
//		p6 = new PersonAgent("dhost");
//		restaurants.add(new Restaurant("DRestaurant", dh, "normal"));
//
//		PersonAgent p7 = null;
//		LHostRole lh =  new LHostRole(p7);
//		p7 = new PersonAgent("lhost", lh);
//		restaurants.add(new Restaurant("LRestaurant", lh, "normal"));
//
//		PersonAgent p8 = null;
//		THostRole th = new THostRole(p8);
//		p8 = new PersonAgent("thost", th);
//		restaurants.add(new Restaurant("TRestaurant", th, "normal"));
//
//		buildings.put("Bank", banks);
//		buildings.put("Market", markets);
//		buildings.put("Restaurant", restaurants);
//		
		
		
		
//		DWaiterRole w = null;
//		PersonAgent waiter = new PersonAgent("waiter", w);
//		w = new DWaiterRole(waiter);
		
//		PersonAgent cust = new PersonAgent("cust", null);
//		cust.setPanel(this);
//		
//		p6.startThread();
//		waiter.startThread();
//		cust.startThread();
		restaurants.add(new Restaurant("DrewRestaurant", DrewRestaurant.host, "normal"));
		restaurants.add(new Restaurant("BRestaurant", BRestaurant.host, "normal"));
		restaurants.add(new Restaurant("DRestaurant", DRestaurant.host, "normal"));
		restaurants.add(new Restaurant("KRestaurant", KRestaurant.host, "normal"));
		restaurants.add(new Restaurant("LRestaurant", LRestaurant.host, "normal"));
		restaurants.add(new Restaurant("TRestaurant", TRestaurant.host, "normal"));
		
		banks.add(new Bank("Bank1", bank.bankManager));
		markets.add(new Market("Market1", market.mManager));

}
	   
	
	
	
//	public Role jobFactory(String job, PersonAgent p) {
//		Role j = null;
//		// bank 
//		if (job.equals("BankLoanOfficer")) {
//			j = new BankLoanOfficerRole(p);
//		}
//		else if (job.equals("BankManager")) {
//			j = new BankManagerRole(p);
//		}
//		else if (job.equals("BankRobber")) {
//			j = new BankRobberRole(p);
//		}
//		else if (job.equals("BankTeller")) {
//			j = new BankTellerRole(p);
//		}
//		
//		// market
//		else if (job.equals("InventoryBoy")) {
//			j = new InventoryBoyRole(p);
//		}
//		else if (job.equals("MarketCashier")) {
//			j = new MarketCashierRole(p);
//		}
//		else if (job.equals("MarketManager")) {
//			j = new MarketManagerRole(p);
//		}
//
//		// house
//		else if (job.equals("Landlord")) {
//			j = new LandlordRole(p);
//		}
//		else if (job.equals("RepairMan")) {
//			j = new RepairManRole(p);
//		}
//		
//		// BRestaurant
//		else if (job.equals("RestCashier2")) {
//			j = new BCashierRole(p);
//		}
//		else if (job.equals("Cook2")) {
//			j = new BCookRole(p);
//		}
//		else if (job.equals("Host2")) {
//			j = new BHostRole(p);
//		}
//		else if (job.equals("Waiter2")) {
//			j = new BWaiterNormalRole(p);
//		}
//		
//		// DRestaurant
//		else if (job.equals("RestCashier3")) {
//			j = new DCashierRole(p);
//		}
//		else if (job.equals("Cook3")) {
//			j = new DCookRole(p);
//		}
//		else if (job.equals("Host3")) {
//			j = new DHostRole(p);
//		}
//		else if (job.equals("Waiter3")) {
//			j = new DWaiterNormalRole(p);
//		}
//		
//		// Drew_Restaurant
//		else if (job.equals("RestCashier1")) {
//			j = new Drew_CashierRole(p);
//		}
//		else if (job.equals("Cook1")) {
//			j = new Drew_CookRole(p);
//		}
//		else if (job.equals("Host1")) {
//			j = new Drew_HostRole(p);
//		}
//		else if (job.equals("Waiter1")) {
//			j = new Drew_WaiterRole(p);
//		}
//		
//		// KRestaurant
//		else if (job.equals("RestCashier4")) {
//			j = new KCashierRole(p);
//		}
//		else if (job.equals("Cook4")) {
//			j = new KCookRole(p);
//		}
//		else if (job.equals("Host4")) {
//			j = new KHostRole(p);
//		}
//		else if (job.equals("Waiter4")) {
//			j = new KWaiterNormalRole(p);
//		}
//		else if (job.equals("WaiterSharedData4")) {
//			j = new KWaiterSharedDataRole(p);
//		}
//		
//		// LRestaurant
//		else if (job.equals("RestCashier5")) {
//			j = new LCashierRole(p);
//		}
//		else if (job.equals("Cook5")) {
//			j = new LCookRole(p);
//		}
//		else if (job.equals("Host5")) {
//			j = new LHostRole(p);
//		}
//		else if (job.equals("Waiter5")) {
//			j = new LWaiterNormalRole(p);
//		}
//		
//		// TTRestaurant
//		else if (job.equals("RestCashier6")) {
//			j = new TCashierRole(p);
//		}
//		else if (job.equals("Cook6")) {
//			j = new TCookRole(p);
//		}
//		else if (job.equals("Host6")) {
//			j = new THostRole(p);
//		}
//		else if (job.equals("Waiter6")) {
//			j = new TWaiterRole(p);
//		}
//		return j;
//	}
//	
//	public Role customerFactory(String place, PersonAgent p) {
//		Role c = null;
//		if (place.equals("Bank")) {
//			System.out.println("making bank customer");
//			c = new BankCustomerRole(p);
//		}
//		else if (place.equals("Market")) {
//			c = new MarketCustomerRole(p);
//		}
//		else if (place.equals("KRestaurant")) {
//			c = new KCustomerRole(p);
//		}
//		else if (place.equals("BRestaurant")) {
//			c = new BCustomerRole(p);
//		}
//		else if (place.equals("Drew_restaurant")) {
//			c = new Drew_CustomerRole(p);
//		}
//		else if (place.equals("DRestaurant")) {
//			c = new DCustomerRole(p);
//			System.out.println("made drestaurantcust");
//		}
//		else if (place.equals("LRestaurant")) {
//			c = new LCustomerRole(p);
//		}
//		else if (place.equals("TRestaurant")) {
//			c = new TCustomerRole(p);
//		}
//		return c;
//	}
	
	public SimCityPanel(CityGui cityGui) {
		// TODO Auto-generated constructor stub
		this.gui = gui;
	}




	public void LoadScenario(String type) {
		people.clear();
		landlords.clear();
		houseNumCounter=1;
		aptNumCounter=1;
		aptLetCounter='A';
		
		
		try {
			in  = new Scanner(new FileReader("config"+File.separator+type+".txt"));
//			System.out.println(in.next());
			in.next();
			int numItems = in.nextInt();
			//clear input template;
			in.next();
			in.next();
			in.next();
			in.next();
			in.next();
			in.next();
			in.next();
			for(int i=0; i<numItems; i++) {
				PersonAgent p = new PersonAgent(in.next());
				p.setMoney(in.nextDouble());
				String job = in.next().trim();
				//Role myJob = jobFactory(job,p);
				//p.SetJob(myJob);
				if(job.equals("Landlord")) {
					landlords.add(new MyLandlord(p));
					System.out.println("landlord added");
				}
				
				if(job.equals("BankLoanOfficer")) {
					p.SetJob(bank.loanOfficer);
				}
				
				else if(job.equals("BankTeller")) {
					p.SetJob(bank.bankTeller);
				}
				
				else if(job.equals("InventoryBoy")) {
					p.SetJob(market.ib);
				}
				else if(job.equals("MarketCashier")) {
					p.SetJob(market.mCashier);
				}
				//add to map
				if(job.equals("BankManager") && bankCounter<=NUMBANKS) {
					//banks.add(new Bank("Bank"+Integer.toString(bankCounter), myJob));
					p.SetJob(bank.bankManager);
					bankCounter++;
				}
				else if(job.equals("MarketManager") && mktCounter<=NUMMARKETS) {
					//markets.add(new Market("Market"+Integer.toString(mktCounter), myJob));
					p.SetJob(market.mManager);
					mktCounter++;
				}
				else if(job.equals("Host1")) {
					//restaurants.add(new Restaurant("Restaurant1", myJob, "normal"));
					p.SetJob(DrewRestaurant.host);
				}
				else if(job.equals("Cook1")) {
					p.SetJob(DrewRestaurant.cook);
				}
				else if(job.equals("Cashier1")) {
					p.SetJob(DrewRestaurant.cashier);
				}
				else if(job.equals("Host2")) {
					//restaurants.add(new Restaurant("Restaurant2", myJob, "normal"));
					p.SetJob(BRestaurant.host);
				}
				else if(job.equals("Cook2")) {
					p.SetJob(BRestaurant.cook);
				}
				else if(job.equals("Cashier2")) {
					p.SetJob(BRestaurant.cashier);
				}
				else if(job.equals("Host3")) {
					//restaurants.add(new Restaurant("Restaurant3", myJob, "normal"));
					p.SetJob(DRestaurant.host);
				}
				else if(job.equals("Cook3")) {
					p.SetJob(DRestaurant.cook);
				}
				else if(job.equals("Cashier3")) {
					p.SetJob(DRestaurant.cashier);
				}
				else if(job.equals("Host4")) {
					//restaurants.add(new Restaurant("Restaurant4", myJob, "normal"));
					p.SetJob(KRestaurant.host);

				}
				else if(job.equals("Cook4")) {
					p.SetJob(KRestaurant.cook);
				}
				else if(job.equals("Cashier4")) {
					p.SetJob(KRestaurant.cashier);
				}
				else if(job.equals("Host5")) {
					//restaurants.add(new Restaurant("Restaurant5", myJob, "normal"));
					p.SetJob(LRestaurant.host);

				}
				else if(job.equals("Cook5")) {
					p.SetJob(LRestaurant.cook);
				}
				else if(job.equals("Cashier5")) {
					p.SetJob(LRestaurant.cashier);
				}
				else if(job.equals("Host6")) {
					//restaurants.add(new Restaurant("Restaurant6", myJob, "normal"));
					p.SetJob(TRestaurant.host);

				}
				else if(job.equals("Cook6")) {
					p.SetJob(TRestaurant.cook);
				}
				else if(job.equals("Cashier6")) {
					p.SetJob(TRestaurant.cashier);
				}
				//for load balancing waiters when they are added on through gui
				else if(job.equals("Waiter1")) {
//					for(Location r: restaurants) {
//						if(r.name.equals("Restaurant1")) {
							//System.out.println(r.numEmployees);
							//r.numEmployees++;
							Drew_WaiterNormalRole w = (Drew_WaiterNormalRole) DrewRestaurant.AddHeadWaiter();
							p.SetJob(w);

							//break;
						//}
					//}
				}
				else if(job.equals("Waiter2")) {
//					for(Location r: restaurants) {
//						if(r.name.equals("Restaurant2")){
//							r.numEmployees++;
						BWaiterNormalRole w = (BWaiterNormalRole) BRestaurant.AddHeadWaiter();
						p.SetJob(w);

					
//							break;
//						}	
//					}
				}
				else if(job.equals("Waiter3")) {
//					for(Location r: restaurants) {
//						if(r.name.equals("Restaurant3")){
//							r.numEmployees++;
//							break;
					p.SetJob(DRestaurant.AddNormalWaiter());

//						}		
//					}
				}
				else if(job.equals("Waiter4")) {
//					for(Location r: restaurants) {
//						if(r.name.equals("Restaurant4")){
//							r.numEmployees++;
//							break;
					p.SetJob(KRestaurant.AddNormalWaiter());

//						}		
//					}
				}
				else if(job.equals("Waiter5")) {
//					for(Location r: restaurants) {
//						if(r.name.equals("Restaurant5")) {
//							r.numEmployees++;
//							break;
					p.SetJob(LRestaurant.AddNormalWaiter());

//						}		
//					}
				}
				else if(job.equals("Waiter6")) {
//					for(Location r: restaurants) {
//						if(r.name.equals("Restaurant6")) {
//							r.numEmployees++;
//							break;
					p.SetJob(TRestaurant.AddNormalWaiter());

//						}
//					}
				}
				
				boolean hasACar = in.nextBoolean();
				if(hasACar) {
					p.setCar(new CarAgent());
				}
				String home = in.next();
				
				if(home.equals("House") && houseNumCounter<=NUMHOUSES) {
					p.SetHome(HomeType.house);
					p.SetHouseLocation(houseNumCounter);
					houseNumCounter++;
					
				}
				else if(aptNumCounter<=aptNumCounter) {
					p.SetHome(HomeType.apartment);
					p.SetApatmentLocation(aptNumCounter, aptLetCounter);
					if(aptLetCounter=='C') {
						aptLetCounter='A';
						aptNumCounter++;
					}
					else {
						aptLetCounter++;
					}
				}
				else {
					p.SetHome(HomeType.homeless);
				}
				people.add(p);
			}
			
			ApartmentsPerLandlord = NUMAPTS/landlords.size();
			for(Person p: people) {
				if(p.GetHomeState()==HomeType.apartment) {
					landlords.get((p.getAptNum()-1)/ApartmentsPerLandlord).tenants.add(p);
				}
			}
			
			buildings.put("Bank", banks);
			buildings.put("Market", markets);
			buildings.put("Restaurant", restaurants);
			
			for (Person p: people) {
				p.msgSetBuildingDirectory(buildings);
				p.msgSetBusDirectory(busStops);
			}
//			
			for(MyLandlord l: landlords) {
//				System.out.println(l.tenants.size());
//				l.p.SetTenants(l.tenants);
			}
//			
			//TEST CODE
//			System.out.println(people.size());
//			for(Person person: people) {
//				HomeType h= person.GetHomeState();
//				if(h==HomeType.house) {
//					System.out.println("house");
//					System.out.println(person.getHouseNum());
//				}
//				if(h==HomeType.apartment) {
//					System.out.println("apartment");
//					System.out.print(person.getAptNum());
//					System.out.println(person.getAptLet());
//				}
//			}
//			int i=1;
//			for(Person person: people) {
//				System.out.print(i);
//				i++;
//				System.out.println(person.GetJob().isActive);
//			}
//			System.out.println(landlords.size());
			
			in.close();
			gui.addPeople(people);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	  public void addPerson(String name, String role, double moneyVal, String houseOrApt) {

    		PersonAgent p = new PersonAgent(name);
    		int minWaiterIndex=0;
//    		for(Location r: restaurants) {
//    			System.err.println(r.numEmployees);
//    		}
    		if(role.equals("Restaurant Waiter")) {
    			for (int i=0; i<restaurants.size(); i++) {
    				if(restaurants.get(i).numEmployees<restaurants.get(minWaiterIndex).numEmployees)
    					minWaiterIndex=i;		
    			}	
    		}
    		Role w = DrewRestaurant.AddNormalWaiter();
    		//System.out.println(Integer.parseInt(restaurants.get(minWaiterIndex).name.substring(restaurants.get(minWaiterIndex).name.length()-1)));
    		int restAssignedTo = Integer.parseInt(restaurants.get(minWaiterIndex).name.substring(restaurants.get(minWaiterIndex).name.length()-1));
    		
   /*****************call add waiter*********************/ 
    		
    		//Role myJob=jobFactory("Waiter"+Integer.toString(restAssignedTo), p);
    		//System.out.println(myJob);
    		for(Location r: restaurants) {
    			if(r.name.equals("Restaurant"+Integer.toString(restAssignedTo))) {
    				r.numEmployees++;
    				break;
    			}
    		}
    		//System.out.println(restAssignedTo);
//    		p.SetJob(myJob);
//    		System.out.println(p.getJob());
    		if(houseOrApt.equals("House") && houseNumCounter<=NUMHOUSES) {
				p.SetHome(HomeType.house);
				//System.err.println(houseNumCounter);
				p.SetHouseLocation(houseNumCounter);
				houseNumCounter++;
				
			}
			else if(aptNumCounter<=aptNumCounter) {
				p.SetHome(HomeType.apartment);
				p.SetApatmentLocation(aptNumCounter, aptLetCounter);
				//System.err.println(aptNumCounter + "  "+ aptLetCounter);
				if(aptLetCounter=='C') {
					aptLetCounter='A';
					aptNumCounter++;
				}
				else {
					aptLetCounter++;
				}
			}
			else {
				p.SetHome(HomeType.homeless);
			}
 
    		//give person directory
    		p.msgSetBuildingDirectory(buildings);
			p.msgSetBusDirectory(busStops);
			
    		PersonGui g = new PersonGui(p, gui);
    		p.setGui(g);
    		gui.simCityAnimationPanel.addGui(g);
    		p.startThread();
    		people.add(p);
//    		System.err.println("added person in restpanel");
	    	
	    }
	
	// Location classes
	public abstract class Location {
		// Location l
		public String name;
		public int numEmployees=0;
		public int x;
		public int y;
	}
	
	public class Restaurant extends Location{
		Role host;
		String foodType;
		
		public Restaurant(String n, Role h, String type) {
			name = n;
			host = h;
			foodType = type;
		}
		
		
	}
//	
//
	
	public class Bank extends Location{
		Role manager;
		
		public Bank(String n, Role myJob){
			name = n;
			manager = myJob;
		}
	}
	
	public class Market extends Location {
		Role manager;
		
		public Market(String n, Role m) {
			name = n;
			manager = m;
		}
	}
	
	class Business {
		public String name;
		public int x;
		public int y;
	}
	//waiter needs host
	//customer needs hot
	//waiter needs cook setCook(cook)
	
	class MarketPlace extends Business {
		public InventoryBoyRole ib;
		public MarketManagerRole mManager;
		public MarketCashierRole mCashier;
		List<MarketCustomer> mCustomers;
		
		public MarketPlace() {
			ib = new InventoryBoyRole();
			mManager = new MarketManagerRole();
			mCashier = new MarketCashierRole();
			mCustomers = new ArrayList<MarketCustomer>();
			
			ib.setMarketCashier(mCashier);
			ib.setMarketManager(mManager);
			
			mCashier.setInventoryBoy(ib);
			mCashier.setMarketManager(mManager);
		
		}
		
		public MarketCustomer addCustomer() {
			MarketCustomerRole c = new MarketCustomerRole();
			c.setMarketManager(mManager);
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
		
		
		List<BankCustomerRole> bankCustomers;
		
		public BankPlace() {
			loanOfficer = new BankLoanOfficerRole();
			bankManager = new BankManagerRole();
			bankTeller = new BankTellerRole();
			bankCustomers = new ArrayList<BankCustomerRole>();
			
			loanOfficer.setManager(bankManager);
			bankTeller.setManager(bankManager);
			
		}
		public BankCustomerRole addCustomer() {
			BankCustomerRole b = new BankCustomerRole();
			b.setMarketManager(bankManager);
			bankCustomers.add(b);
			return b;
		}
	}
	class RestaurantPlace extends Business {
		//JPanel animationPanel = new JPanel();
		public int restNum;
		public Role host = null;
		public Role cook = null;
		public Role cashier = null;
		List<Role> waiters = new ArrayList<Role>();
		List<Role> customers = new ArrayList<Role>();
		RestaurantPlace(int restNum) {
			this.restNum = restNum;
			switch(restNum) {
			
			case 1: host = new Drew_HostRole();
					cook = new Drew_CookRole();
					((Drew_HostRole)host).setCook((Drew_Cook)cook);
					cashier = new Drew_CashierRole();
					break;
			case 2: host = new BHostRole();
					cook = new BCookRole();
					((BHost)host).setCook((BCookRole)cook);
					cashier = new BCashierRole();
					break;
			case 3: host = new DHostRole();
					cook = new DCookRole();
					cashier = new DCashierRole();
					((DHost)host).addCook((DCook)cook);
					((DCookRole) cook).AddHost((DHostRole)host);
					((DCookRole)cook).AddCashier((DCashier)cashier);
					((DCashierRole)cashier).AddCook((DCook)cook);
					break;
			case 4: host = new KHostRole();
					cook = new KCookRole();
					cashier = new KCashierRole();
					((KHostRole)host).setCook((KCookRole) cook);
					((KHostRole)host).setCashier((KCashierRole) cashier);
					((KCookRole)cook).setHost((KHostRole) host);
					((KCookRole)cook).setCashier((KCashierRole) cashier);
					((KCashierRole)cashier).setHost((KHostRole) host);
					((KCashierRole)cashier).setCook((KCookRole) cook);
					break;
			case 5:	host = new LHostRole();
					cook = new LCookRole();
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
					cashier = new TCashierRole();
					((THostRole) host).setCook((TCookRole)cook);
					((THostRole) host).setCashier((TCashierRole)cashier); 
					((TCashierRole)cashier).setHost((THostRole)host); 
					((TCookRole) cook).setCashier((TCashierRole)cashier);
					
			}
		}
		
		public Role AddNormalWaiter() {
			switch(restNum) {
			
			case 1: Drew_WaiterRole aw = new Drew_WaiterSharedDataRole();
					aw.setCook((Drew_Cook)cook);
					aw.addCashier((Drew_Cashier)cashier);
					aw.setHost((Drew_Host)host);
					//Drew_WaiterGui g = new Drew_WaiterGui(w, , waiters.size()+1);
					waiters.add(aw);
					((Drew_Host)host).addWaiter(aw);
					return aw;
			case 2: 
					BWaiterRole bw = new BWaiterSharedDataRole();
					bw.setHost((BHostRole)host);
					bw.setCook((BCook)cook);
					bw.setCashier((BCashier)cashier);
					//Drew_WaiterGui g = new Drew_WaiterGui(w, , waiters.size()+1);
					waiters.add(bw);
					((BHost)host).setWaiter(bw);
					return bw;
			case 3: 
					DWaiterRole dw = new DWaiterSharedRole();
					dw.msgAddCashier((DCashier)cashier);
					dw.msgAddCook((DCook)cook);
					dw.msgAddHost((DHost)host);
					((DHost)host).msgAddWaiter((DWaiter)dw);
					waiters.add(dw);
					return dw;
			case 4:
					KWaiterRole kw = new KWaiterSharedDataRole();
					kw.setCook((KCookRole)cook);
					kw.setCashier((KCashier)cashier);
					kw.setHost((KHostRole)host);
					((KHostRole)host).addWaiter((KWaiterRole)kw);
					return kw;
					
			case 5:	
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
		public Role AddHeadWaiter() {
			switch(restNum) {
			
			case 1:
			Drew_WaiterRole aw = new Drew_WaiterNormalRole();
			aw.setCook((Drew_Cook)cook);
			waiters.add(aw);
			return aw;
			
			case 2:
				BWaiterRole bw = new BWaiterNormalRole();
				bw.setHost((BHostRole)host);
				bw.setCook((BCook)cook);
				bw.setCashier((BCashier)cashier);
				//Drew_WaiterGui g = new Drew_WaiterGui(w, , waiters.size()+1);
				waiters.add(bw);
				return bw;
				
			case 3: 
				DWaiterRole dw = new DWaiterNormalRole();
				dw.msgAddCashier((DCashier)cashier);
				dw.msgAddCook((DCook)cook);
				dw.msgAddHost((DHost)host);
				waiters.add(dw);
				return dw;
			
			case 4:
				KWaiterRole kw = new KWaiterNormalRole();
				kw.setCook((KCookRole)cook);
				kw.setCashier((KCashier)cashier);
				kw.setHost((KHostRole)host);
				((KHostRole)host).addWaiter((KWaiterRole)kw);
				return kw;
			case 5:	
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
			
				case 1: Drew_CustomerRole ac = new Drew_CustomerRole();
				ac.setHost((Drew_Host)host);
				customers.add(ac);
				return ac;
				
				case 2: BCustomerRole bc = new BCustomerRole();
				bc.setCashier((BCashier)cashier);
				bc.setHost((BHostRole)host);
				customers.add(bc);
				return bc;
				
				case 3: DCustomerRole dc = new DCustomerRole();
				dc.setCashier((DCashier)cashier);
				dc.setHost((DHost)host);
				customers.add(dc);
				return dc;
				
				case 4: KCustomerRole kc = new KCustomerRole();
				kc.setHost((KHostRole)host);
				kc.setCashier((KCashier)cashier);
				customers.add(kc);
				return kc;
					
				case 5:	LCustomerRole lc = new LCustomerRole();
				lc.setCashier((LCashier)cashier);
				lc.setHost((LHost)host);
				customers.add(lc);
				return lc;
				
				case 6:	TCustomerRole tc = new TCustomerRole();
				tc.setCashier((TCashierRole)cashier);
				tc.setHost((THostRole)host);
				customers.add(tc);
				return tc;
					
				
				default: return null;

				
			}
		}
	}
	  
	public class MyLandlord {
		Person p;
		List<Person> tenants= new ArrayList<Person>();
		public MyLandlord(Person per) {
			p=per;
		}
	}

	public void startTimer() {
		Timer timer;

		 class RemindTask extends TimerTask {
			 int counter= 0;
		        @Override
				public void run() {
		        	if(counter <25) {
		                 System.out.println("hour is " + counter);
		                 for(Person p: people) {
		                	 p.msgTimeUpdate(counter);
		                 }
		                 counter++;
		                 if (counter == 25) {
		                	 counter = 1;
		                 }
		        	}
		        }
		 }
	     timer = new Timer();
	     timer.schedule(new RemindTask(),
	                       0,        //initial delay
	                       1*12000);  //subsequent rate		
			}




	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	}

	
	/*public void mouseClicked(MouseEvent e) {
		for (int i=0; i<restaurants.size();i++) {
			if (e.getX()==restaurants.get(i).x && e.getY()==restaurants.get(i).y)
             //location.get(i).showbuilding();
            
    }
		for (int j=0; j<markets.size();j++) {
			if (e.getX()==markets.get(i).x && e.getY()==markets.get(i).y)
             //location.get(i).showbuilding();
            
    }
		
		for (int k=0; k<banks.size();k++) {
			if (e.getX()==banks.get(i).x && e.getY()==banks.get(i).y)
             //location.get(i).showbuilding();
            
    }
   
		
    
		
	}
*/

	

