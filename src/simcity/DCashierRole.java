package simcity;

import agent.Agent;
import agent.Role;
import simcity.interfaces.DCashier;
import simcity.interfaces.DCook;
import simcity.interfaces.DCustomer;
import simcity.interfaces.Market;
import simcity.interfaces.DWaiter;
import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;
import simcity.DCashierRole.InventoryBill.InventoryBillState;
import simcity.DCheck.CheckState;
import simcity.DCookRole.InventoryOrder;

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
public class DCashierRole extends Role implements DCashier {
	//static final int NTABLES = 12;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	//public List<CustomerAgent> waitingCustomers
	//= new ArrayList<CustomerAgent>();

	//list of waiters
	//public List<WaiterAgent> waiters = new ArrayList<WaiterAgent>();
	DCook myCook = null;
	
	public static final double MKT_interestRate=0.10;
	
	//public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	Timer timer = new Timer();
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	private Map<String, Double> prices = new HashMap<String, Double>();
	private DWaiter waiterAtRegister=null;
	private double registerAmnt;
	//public HostGui hostGui = null;
	//Map<String, Double> blacklist = new HashMap<String, Double>();
	List<DCheck> myBills = Collections.synchronizedList(new ArrayList<DCheck>());
	List<InventoryBill> inventoryBills = Collections.synchronizedList(new ArrayList<InventoryBill>());
	public List<DCheck> getBills() {
		return myBills;
	}

	public DCashierRole() {
		super();

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

	@Override
	public void setRegisterAmnt(double amnt) {
		registerAmnt=amnt;
	}

	@Override
	public double getRegisterAmnt() {
		
		return registerAmnt;
	}
	
	public List<InventoryBill> getInventoryBill() {
		return inventoryBills;
	}
	

	// Messages
	public void AddCook(DCook tba) {
		myCook = tba;
	}
//	public void msgMadeInventoryOrder(int ORDER_ID, double billAmt, Market m) {
//		inventoryBills.add(new InventoryBill(billAmt, m));
//	}
	
	public void msgAnswerVerificationRequest(boolean yn) {
		System.out.println("\n cashier received answer verification " + (yn ? "yes" : "no"));
		for(InventoryBill i: inventoryBills) {
			if(i.state==InventoryBillState.pendingResponse) {
				if(yn) i.state=InventoryBillState.processing;
				else i.state=InventoryBillState.fraud;
				stateChanged();
			}
		}
	}
	public void msgHereIsAnInventoryBill(double amnt, Market market1) {
		System.out.print("received bill from "+ market1+" for "+ amnt);
//		inventoryBills.add(new InventoryBill(amnt, market1));
		//log.add(new LoggedEvent("Received msgHereIsAnInventoryBill"));
		inventoryBills.add(new InventoryBill(amnt, market1));
		stateChanged();
	}
	
	@Override
	public void msgComputeBill(String choice, DCustomer cust, String name, int tnum, DWaiter wa) {
		//System.out.println("received request for bill for table "+ (char)tnum);
		myBills.add(new DCheck(choice, cust, name, tnum, wa)); // is that ok?
		System.out.println("bill reqest added for customer "+ cust+ " at table"+ tnum);
		stateChanged();
	}
	
	public void msgHereForMyBills(DWaiter w) {
		waiterAtRegister=w;
		stateChanged();
	}
	
	@Override
	public void msgHereIsAPayment(DCustomer cust, int tnum, double valCustPaid) {
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
		
		for(DCheck b: myBills) {
			if(b.state==CheckState.processing) {
				ProcessBill(b);
				return true;
			}
		}
		
		for(DCheck b: myBills) {
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
			if(bill.state==InventoryBillState.justReceived) {
				System.out.println(bill);
				AskCookForValidation(bill);
				return true;
			}
		}
		
		for(InventoryBill bill: inventoryBills) {
			if(bill.state==InventoryBillState.couldNotAfford && bill.amnt<=registerAmnt) {
				//System.out.println("called proccess");
				ProcessInventoryBill(bill);
				return true;	
			}
		}
		
		for(InventoryBill bill: inventoryBills) {
			if(bill.state==InventoryBillState.fraud) {
				//System.out.println("called proccess");
				RemoveFraudulentBill(bill);
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
	 
	private void RemoveFraudulentBill(InventoryBill bi) {
		inventoryBills.remove(bi);
	}
	private void AskCookForValidation(InventoryBill bi) {
		myCook.msgShouldIPayThisBill(bi.amnt, bi.ma);
		bi.state=InventoryBillState.pendingResponse;
		return;
	}
	private void ProcessInventoryBill(InventoryBill bi) {
		//System.out.println("processing... "+ bi.amnt+ "  "+ registerAmnt);
		DecimalFormat df = new DecimalFormat("###.##");

		if(bi.amnt>registerAmnt) {
			bi.state=InventoryBillState.couldNotAfford;
			bi.amnt=Double.parseDouble(df.format((1+ MKT_interestRate)*bi.amnt));
//			System.err.println(bi.amnt);
			Do("Could not afford this inventory bill, bill value updated to "+ bi.amnt);
			return;
		}

		registerAmnt-=bi.amnt;
		bi.state=InventoryBillState.processed;
		final Market ma=bi.ma;
		final double amt=bi.amnt;

		inventoryBills.remove(bi);
		ma.msgHereIsAPayment(amt, this);
			
		
	}
	private void ProcessBill(DCheck bi) {
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
		
		for (DCheck bill: myBills) {
			if(bill.waiter==waiterAtRegister && bill.state==CheckState.processed) {
				
				for(DCheck findDebt: myBills) {
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
	
	private void ComputeChange(DCheck bi) {
		Do("computing change");
		
		
		DecimalFormat df = new DecimalFormat("###.##");
		double changeval = Double.parseDouble(df.format(bi.getCustomerPaid()-bi.getBillAmnt()));
		if(changeval>=0) {
			
			//check and see if acquired debt
			for(DCheck bill: myBills) {
				if(bill.state==CheckState.debt && bill.customer==bi.getCustomer()) {
					if(bill.debt<=changeval) { // customer is trying to pay debt
						System.out.println("Customer's debt of "+ bill.debt+ " is added on to current bill");
						
//						System.err.println(changeval);
						myBills.remove(bill);
					}
				}
			}
			bi.getCustomer().msgHereIsYourReceiptAndChange(changeval);
			registerAmnt+=bi.getCustomerPaid()-changeval;
		
			myBills.remove(bi);
		}
		else {
			//blacklist.put(bi.getCustomer().getName(), (-1.0)*changeval);
			bi.getCustomer().msgHereIsYourReceiptAndChange(0);
			registerAmnt+=bi.getCustomerPaid();
		
//			System.err.println("Customer Acquired Debt of " + Double.parseDouble(df.format(bi.BillAmnt- bi.CustomerPaid)));
			bi.state=CheckState.debt;
			bi.debt=Double.parseDouble(df.format(bi.BillAmnt- bi.CustomerPaid));
//			System.err.println(bi.debt);
		}
	


		
		
		
		//System.out.println("cash inflow.. regAmnt = "+ registerAmnt);
		
	}
	

	public static class InventoryBill {
		Market ma;
		double amnt;
		public enum InventoryBillState {justReceived, pendingResponse, processing, couldNotAfford, processed, sent, fraud};
		public InventoryBillState state;
		
		InventoryBill(double a, Market market1) {
			ma = market1;
			amnt = a;
			state=InventoryBillState.justReceived;
			
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


