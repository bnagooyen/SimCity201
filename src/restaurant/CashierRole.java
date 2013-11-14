package restaurant;

import agent.Role;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Market;
import restaurant.interfaces.Waiter;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;
import restaurant.test.mock.MockMarket;
import restaurant.CashierRole.InventoryBill.InventoryBillState;
import restaurant.Check.CheckState;
import simcity.PersonAgent;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class CashierRole extends Role implements Cashier {
	//static final int NTABLES = 12;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	//public List<CustomerAgent> waitingCustomers
	//= new ArrayList<CustomerAgent>();

	//list of waiters
	//public List<WaiterAgent> waiters = new ArrayList<WaiterAgent>();

	public EventLog log = new EventLog();
	
	public static final double MKT_interestRate=0.10;
	
	//public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	Timer timer = new Timer();
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	private Map<String, Double> prices = new HashMap<String, Double>();
	private Waiter waiterAtRegister=null;
	private double registerAmnt;
	//public HostGui hostGui = null;
	//Map<String, Double> blacklist = new HashMap<String, Double>();
	List<Check> myBills = Collections.synchronizedList(new ArrayList<Check>());
	List<InventoryBill> inventoryBills = Collections.synchronizedList(new ArrayList<InventoryBill>());
	public List<Check> getBills() {
		return myBills;
	}

	public CashierRole(PersonAgent p) {
		super(p);

		this.name = name;
		// make some tables
		
		//adding one waiter.. to be changed
		//waiters.add(new WaiterAgent("Joe"));
		
		prices.put("Steak", 15.99);
		prices.put("Chicken", 10.99);
		prices.put("Salad", 5.99);
		prices.put("Pizza", 8.99);
		
		registerAmnt=300;
		//inventoryBills.clear();

	}

	public String getMaitreDName() {
		return name;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setRegisterAmnt(double amnt) {
		registerAmnt=amnt;
	}

	public double getRegisterAmnt() {
		
		return registerAmnt;
	}
	
	public List<InventoryBill> getInventoryBill() {
		return inventoryBills;
	}
	

	// Messages
	
	public void msgHereIsAnInventoryBill(double amnt, Market market1) {
		System.out.print("received bill from "+ market1+" for "+ amnt);
		inventoryBills.add(new InventoryBill(amnt, market1));
		//log.add(new LoggedEvent("Received msgHereIsAnInventoryBill"));
		stateChanged();
	}
	
	public void msgComputeBill(String choice, Customer cust, String name, int tnum, Waiter wa) {
		//System.out.println("received request for bill for table "+ (char)tnum);
		myBills.add(new Check(choice, cust, name, tnum, wa)); // is that ok?
		System.out.println("bill reqest added for customer "+ cust+ " at table"+ tnum);
		stateChanged();
	}
	
	public void msgHereForMyBills(Waiter w) {
		waiterAtRegister=w;
		stateChanged();
	}
	
	@Override
	public void msgHereIsAPayment(Customer cust, int tnum, double valCustPaid) {
//		System.err.println(tnum);
		for (int i=0; i<myBills.size(); i++) {
			if (cust==myBills.get(i).getCustomer() && !(myBills.get(i).state==CheckState.debt)) {
//				System.out.println("foudnit");
				myBills.get(i).setCustomerPaid(valCustPaid);
				myBills.get(i).setCustomer(cust);
				myBills.get(i).state=CheckState.paid;
				stateChanged();
			}
		}
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	@Override
	public boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		//System.out.println('');
		for(Check b: myBills) {
			if(b.state==CheckState.processing) {
				ProcessBill(b);
				return true;
			}
		}
		
		for(Check b: myBills) {
			if(b.state==CheckState.paid) {
				ComputeChange(b);
				return true;
			}
		}
		
		if(waiterAtRegister!=null) {
			GiveCheckToWaiter();
			return true;
		}
		
		for(InventoryBill bill: inventoryBills) {
			if(bill.state==InventoryBillState.couldNotAfford && bill.amnt<=registerAmnt) {
				//System.out.println("called proccess");
				ProcessInventoryBill(bill);
				return true;	
			}
		}
		
		
		for(InventoryBill bill: inventoryBills) {
			if(bill.state==InventoryBillState.processing) {
				//System.out.println("called proccess");
				ProcessInventoryBill(bill);
				return true;	
			}
		}

	
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	
	private void ProcessInventoryBill(InventoryBill bi) {
		System.out.println("processing... "+ bi.amnt+ "  "+ registerAmnt);
		DecimalFormat df = new DecimalFormat("###.##");

		if(bi.amnt>registerAmnt) {
			log.add(new LoggedEvent("Could not afford"));
			bi.state=InventoryBillState.couldNotAfford;
			bi.amnt=Double.parseDouble(df.format((1+ MKT_interestRate)*bi.amnt));
//			System.err.println(bi.amnt);
			Do("Could not afford this inventory bill, bill value updated to "+ bi.amnt);
			log.add(new LoggedEvent("Bill = "+ bi.amnt));
			return;
		}

		registerAmnt-=bi.amnt;
		bi.state=InventoryBillState.processed;
		final Market ma=bi.ma;
		final double amt=bi.amnt;
//		System.out.println(bi.amnt);
		log.add(new LoggedEvent("Could afford. Paid "+ df.format(bi.amnt)));
//		System.out.println(registerAmnt);
		log.add(new LoggedEvent("Register = "+ registerAmnt));
		inventoryBills.remove(bi);
		ma.msgHereIsAPayment(amt, this);
				//isHungry = false;
				//stateChanged();
		
	}
	private void ProcessBill(Check bi) {
		//Do("processing bill.. "+ prices.get(bi.getChoice()));
		/*
		double amntdue=0;
		if(blacklist.get(bi.name)!=null) {
			amntdue=blacklist.get(bi.name);
			System.out.print(bi.name+ " has a debt on my records! adding "+amntdue+"\n");
			System.out.println();
		}*/
		
		bi.setBillAmnt(prices.get(bi.getChoice()));

		//System.out.println(bi.getBillAmnt());
		bi.state=CheckState.processed;

		log.add(new LoggedEvent("Processed Bill "+ bi.BillAmnt));
		
		bi.getWaiter().msgCheckIsReady();
		
		/*got rid of timer for junit testing*/
		
//		final Check biForTimer=bi;
//		timer.schedule(new TimerTask() {
//			@Override
//			public void run() {
//				biForTimer.getWaiter().msgCheckIsReady();
//				//isHungry = false;
//				//stateChanged();
//			}
//		},
//		5000); // arbitrary num.. takes 8 seconds
//		
	}
	private void GiveCheckToWaiter() {
		
		DecimalFormat df = new DecimalFormat("###.##");
		
		for (Check bill: myBills) {
			if(bill.waiter==waiterAtRegister && bill.state==CheckState.processed) {
				
				for(Check findDebt: myBills) {
					if (findDebt.state==CheckState.debt && findDebt.getCustomer()== bill.getCustomer()) {
						//found some debt!
						System.out.println("found debt of "+findDebt.debt+" for this customer");
						bill.BillAmnt+=findDebt.debt;
						bill.BillAmnt=Double.parseDouble(df.format(bill.BillAmnt));
					}
				}
				
				waiterAtRegister.msgHereIsABill(bill);
				System.out.println("bill added");
				bill.state=CheckState.sent;
				//myBills.remove(bill);
				break;
			}
		}
		
		//remove all debt
		
		waiterAtRegister=null;
	}
	
	private void ComputeChange(Check bi) {
		Do("computing change");
		
		log.add(new LoggedEvent("Received Payment "+ bi.CustomerPaid));
		
		DecimalFormat df = new DecimalFormat("###.##");
		double changeval = Double.parseDouble(df.format(bi.getCustomerPaid()-bi.getBillAmnt()));
		if(changeval>=0) {
			
			//check and see if acquired debt
			for(Check bill: myBills) {
				if(bill.state==CheckState.debt && bill.customer==bi.getCustomer()) {
					if(bill.debt<=changeval) { // customer is trying to pay debt
						System.out.println("Customer's debt of "+ bill.debt+ " is added on to current bill");
						log.add(new LoggedEvent("Debt of "+bill.debt+" is added to current bill"));
//						System.err.println(changeval);
						myBills.remove(bill);
					}
				}
			}
			bi.getCustomer().msgHereIsYourReceiptAndChange(changeval);
			registerAmnt+=bi.getCustomerPaid()-changeval;
			log.add(new LoggedEvent("Computed Change "+ changeval));
			myBills.remove(bi);
		}
		else {
			//blacklist.put(bi.getCustomer().getName(), (-1.0)*changeval);
			bi.getCustomer().msgHereIsYourReceiptAndChange(0);
			registerAmnt+=bi.getCustomerPaid();
			log.add(new LoggedEvent("Computed Change 0.0"));
			log.add(new LoggedEvent("Customer Acquired Debt of " + (bi.BillAmnt- bi.CustomerPaid)));
//			System.err.println("Customer Acquired Debt of " + Double.parseDouble(df.format(bi.BillAmnt- bi.CustomerPaid)));
			bi.state=CheckState.debt;
			bi.debt=Double.parseDouble(df.format(bi.BillAmnt- bi.CustomerPaid));
//			System.err.println(bi.debt);
		}
	


		log.add(new LoggedEvent("Register = "+ registerAmnt));
		
		
		
		//System.out.println("cash inflow.. regAmnt = "+ registerAmnt);
		
	}
	

	public static class InventoryBill {
		Market ma;
		double amnt;
		enum InventoryBillState {processing, couldNotAfford, processed, sent};
		InventoryBillState state;
		
		InventoryBill(double a, Market market1) {
			ma = market1;
			amnt = a;
			state=InventoryBillState.processing;
			
		}
		
		public Market getMarket() {
			return ma;
		}
		public double getAmnt() {
			return amnt;
		}
	}
	//utilities


}


