package simcity.restaurant;

import agent.Role;
import simcity.Market.MarketCashierRole;
import simcity.restaurant.interfaces.Cashier;
import simcity.restaurant.interfaces.Customer;
//import simcity.restaurant.interfaces.Market;
import simcity.restaurant.interfaces.Waiter;
import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;
//import simcity.test.mock.MockMarket;
//import simcity.restaurant.CashierRole.InventoryBill.InventoryBillState;
import simcity.restaurant.Check.CheckState;
import simcity.PersonAgent;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
public class CashierRole extends Role implements Cashier {
	

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
	
	List<Check> myBills = Collections.synchronizedList(new ArrayList<Check>());
	//List<InventoryBill> inventoryBills = Collections.synchronizedList(new ArrayList<InventoryBill>());
	public List<Check> getBills() {
		return myBills;
	}

	public CashierRole(PersonAgent p) {
		super(p);

		this.name = name;
		
		prices.put("Steak", 15.99);
		prices.put("Chicken", 10.99);
		prices.put("Salad", 5.99);
		prices.put("Pizza", 8.99);
		
		registerAmnt=300;

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
	
//	public List<InventoryBill> getInventoryBill() {
//		return inventoryBills;
//	}
	

	// Messages
	
	public void msgMadeInventoryOrder(int orderid, double billAmt) {
		//add it to list for confirmation
	}
	
//	public void msgHereIsAnInventoryBill(double amnt, Market market1) {
//		System.out.print("received bill from "+ market1+" for "+ amnt);
//		inventoryBills.add(new InventoryBill(amnt, market1));
//		//log.add(new LoggedEvent("Received msgHereIsAnInventoryBill"));
//		stateChanged();
//	}
	
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
		for (int i=0; i<myBills.size(); i++) {
			if (cust==myBills.get(i).getCustomer() && !(myBills.get(i).state==CheckState.debt)) {
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
		
//		for(InventoryBill bill: inventoryBills) {
//			if(bill.state==InventoryBillState.couldNotAfford && bill.amnt<=registerAmnt) {
//				//System.out.println("called proccess");
//				ProcessInventoryBill(bill);
//				return true;	
//			}
//		}
//		
//		
//		for(InventoryBill bill: inventoryBills) {
//			if(bill.state==InventoryBillState.processing) {
//				ProcessInventoryBill(bill);
//				return true;	
//			}
//		}

	
		
		return false;
	}

	// Actions
	
//	private void ProcessInventoryBill(InventoryBill bi) {
//		System.out.println("processing... "+ bi.amnt+ "  "+ registerAmnt);
//		DecimalFormat df = new DecimalFormat("###.##");
//
//		if(bi.amnt>registerAmnt) {
//			log.add(new LoggedEvent("Could not afford"));
//			bi.state=InventoryBillState.couldNotAfford;
//			bi.amnt=Double.parseDouble(df.format((1+ MKT_interestRate)*bi.amnt));
////			System.err.println(bi.amnt);
//			Do("Could not afford this inventory bill, bill value updated to "+ bi.amnt);
//			log.add(new LoggedEvent("Bill = "+ bi.amnt));
//			return;
//		}
//
//		registerAmnt-=bi.amnt;
//		bi.state=InventoryBillState.processed;
//		final Market ma=bi.ma;
//		final double amt=bi.amnt;
//		log.add(new LoggedEvent("Could afford. Paid "+ df.format(bi.amnt)));
//		log.add(new LoggedEvent("Register = "+ registerAmnt));
//		inventoryBills.remove(bi);
//		ma.msgHereIsAPayment(amt, this);
//
//		
//	}
	private void ProcessBill(Check bi) {
		bi.setBillAmnt(prices.get(bi.getChoice()));

		bi.state=CheckState.processed;

		log.add(new LoggedEvent("Processed Bill "+ bi.BillAmnt));
		
		bi.getWaiter().msgCheckIsReady();
		
	}
	private void GiveCheckToWaiter() {
		
		DecimalFormat df = new DecimalFormat("###.##");
		
		for (Check bill: myBills) {
			if(bill.waiter==waiterAtRegister && bill.state==CheckState.processed) {
				
				for(Check findDebt: myBills) {
					if (findDebt.state==CheckState.debt && findDebt.getCustomer()== bill.getCustomer()) {
						System.out.println("found debt of "+findDebt.debt+" for this customer");
						bill.BillAmnt+=findDebt.debt;
						bill.BillAmnt=Double.parseDouble(df.format(bill.BillAmnt));
					}
				}
				
				waiterAtRegister.msgHereIsABill(bill);
				System.out.println("bill added");
				bill.state=CheckState.sent;
				break;
			}
		}
				
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
					if(bill.debt<=changeval) { 
						System.out.println("Customer's debt of "+ bill.debt+ " is added on to current bill");
						log.add(new LoggedEvent("Debt of "+bill.debt+" is added to current bill"));
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
			bi.getCustomer().msgHereIsYourReceiptAndChange(0);
			registerAmnt+=bi.getCustomerPaid();
			log.add(new LoggedEvent("Computed Change 0.0"));
			log.add(new LoggedEvent("Customer Acquired Debt of " + (bi.BillAmnt- bi.CustomerPaid)));
			bi.state=CheckState.debt;
			bi.debt=Double.parseDouble(df.format(bi.BillAmnt- bi.CustomerPaid));
		}
		log.add(new LoggedEvent("Register = "+ registerAmnt));
				
	}

	@Override
	public void msgBillFromMarket(double check,
			MarketCashierRole marketCashierRole) {
		// TODO Auto-generated method stub
		
	}
	

//	public static class InventoryBill {
//		Market ma;
//		double amnt;
//		enum InventoryBillState {processing, couldNotAfford, processed, sent};
//		InventoryBillState state;
//		
//		InventoryBill(double a, Market market1) {
//			ma = market1;
//			amnt = a;
//			state=InventoryBillState.processing;
//			
//		}
//		
//		public Market getMarket() {
//			return ma;
//		}
//		public double getAmnt() {
//			return amnt;
//		}
//	}

}


