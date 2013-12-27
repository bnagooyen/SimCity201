/*
 * SimCity201, Released December 2013
 * Contributors to file: Doreen Hakimi
 */

package simcity.DRestaurant;

import simcity.DRestaurant.DCashierRole.InventoryBill.InventoryBillState;
import simcity.DRestaurant.DCheck.CheckState;
import simcity.DRestaurant.DCheck;
import agent.Role;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.DCashier;
import simcity.interfaces.DCook;
import simcity.interfaces.DCustomer;
import simcity.interfaces.DHost;
import simcity.interfaces.DWaiter;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketManager;
import simcity.interfaces.RestaurantCashier;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Cashier Role
 */

public class DCashierRole extends Role implements DCashier, RestaurantCashier {

	DCook myCook = null;
	enum CashierState {arrived, working, offDuty};
	CashierState state;
	public static final double MKT_interestRate=0.10;
	
	//public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	Timer timer = new Timer();
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	private Map<String, Double> prices = new HashMap<String, Double>();
	private DWaiter waiterAtRegister=null;
	private DHostRole host = null;
	private double registerAmnt;

	public List<DCheck> myBills = Collections.synchronizedList(new ArrayList<DCheck>());
	List<InventoryBill> inventoryBills = Collections.synchronizedList(new ArrayList<InventoryBill>());
	
	public List<DCheck> getBills() {
		return myBills;
	}

	public DCashierRole() {
		super();
		
		prices.put("Steak", 15.99);
		prices.put("Chicken", 10.99);
		prices.put("Salad", 5.99);
		prices.put("Pizza", 8.99);
		
		registerAmnt=300;
		//inventoryBills.clear();
		state=CashierState.arrived;

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
	
	//utilities


	public void AddHost(DHostRole host2) {
		// TODO Auto-generated method stub
		this.host=host2;
	}
	public void AddCook(DCook tba) {
		myCook = tba;
	}
	
	public void msgOffDuty(double pay) {
		myPerson.money+=pay;
		state=CashierState.offDuty;
		stateChanged();
	}
	public void msgRegisterAmount(double amt) {
		registerAmnt=amt;
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCashierRole", "Register is loaded");
		Do("Register is loaded: "+registerAmnt);
	}
	@Override
	public void msgAnswerVerificationRequest(boolean yn) {
		System.out.println("\n cashier received answer verification " + (yn ? "yes" : "no"));
		synchronized(inventoryBills) {
		for(InventoryBill i: inventoryBills) {
			if(i.state==InventoryBillState.pendingResponse) {
				if(yn) i.state=InventoryBillState.processing;
				else i.state=InventoryBillState.fraud;
				stateChanged();
			}
		}}
		
	}
	
	@Override
	public void msgBillFromMarket(double check, MarketCashier marketCashier, MarketManager manager) {
		// TODO Auto-generated method stub
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCashierRole", "Received bill from market");
			System.out.print("received bill from "+ marketCashier+" for "+ check);
	//	inventoryBills.add(new InventoryBill(amnt, market1));
		//log.add(new LoggedEvent("Received msgHereIsAnInventoryBill"));
		inventoryBills.add(new InventoryBill(check, marketCashier, manager));
		stateChanged();
	}
	

	
	@Override
	public void msgComputeBill(String choice, DCustomer cust, String name, int tnum, DWaiter wa) {
		//System.out.println("received request for bill for table "+ (char)tnum);
		myBills.add(new DCheck(choice, cust, name, tnum, wa)); // is that ok?
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCashierRole", "Bill request added for customer");
		System.out.println("bill request added for customer "+ cust+ " at table"+ tnum);
		stateChanged();
	}
	
	public void msgHereForMyBills(DWaiter w) {
		waiterAtRegister=w;
		stateChanged();
	}
	
	@Override
	public void msgHereIsAPayment(DCustomer cust, int tnum, double valCustPaid) {
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

		if(state==CashierState.arrived) {
			tellHost();
			return true;
		}
		if(state==CashierState.offDuty) {
			leaveRestaurant();
			return true;
		}
		synchronized(myBills) {
		for(DCheck b: myBills) {
			if(b.state==CheckState.processing) {
				ProcessBill(b);
				return true;
			}
		}
		}
		synchronized(myBills) {
		for(DCheck b: myBills) {
			if(b.state==CheckState.paid) {
				ComputeChange(b);
				return true;
			}
		}
		}
		
		
		if(waiterAtRegister!=null) {
			GiveCheckToWaiter();
			return true;
		}
		synchronized(inventoryBills) {
		for(InventoryBill bill: inventoryBills) {
			if(bill.state==InventoryBillState.justReceived) {
				System.out.println(bill);
				AskCookForValidation(bill);
				return true;
			}
		}
		}
		synchronized(inventoryBills) {
		for(InventoryBill bill: inventoryBills) {
			if(bill.state==InventoryBillState.couldNotAfford && bill.amnt<=registerAmnt) {
				//System.out.println("called proccess");
				ProcessInventoryBill(bill);
				return true;	
			}
		}
		}
		synchronized(inventoryBills) {
		for(InventoryBill bill: inventoryBills) {
			if(bill.state==InventoryBillState.fraud) {
				//System.out.println("called proccess");
				RemoveFraudulentBill(bill);
				return true;	
			}
		}
		}
		
		synchronized(inventoryBills) {
		for(InventoryBill bill: inventoryBills) {
			if(bill.state==InventoryBillState.processing) {
				//System.out.println("called proccess");
				ProcessInventoryBill(bill);
				return true;	
			}
		}
		}
	
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	private void tellHost(){
		host.msgIAmHere(this, "cashier");
		state=CashierState.working;
	}
	private void leaveRestaurant() {
		host.msgRegisterMoney(registerAmnt);
		registerAmnt=0;
		state=CashierState.arrived;
		isActive=false;
	}
	private void RemoveFraudulentBill(InventoryBill bi) {
		inventoryBills.remove(bi);
	}
	private void AskCookForValidation(InventoryBill bi) {
		myCook.msgShouldIPayThisBill(bi.amnt, bi.manager);
		bi.state=InventoryBillState.pendingResponse;
		return;
	}
	private void ProcessInventoryBill(InventoryBill bi) {
		//System.out.println("processing... "+ bi.amnt+ "  "+ registerAmnt);
		DecimalFormat df = new DecimalFormat("###.##");

		if(bi.amnt>registerAmnt) {
			bi.state=InventoryBillState.couldNotAfford;
			bi.amnt=Double.parseDouble(df.format((1+ MKT_interestRate)*bi.amnt));
			AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCashierRole", "Could not afford this inventory bill");
			Do("Could not afford this inventory bill, bill value updated to "+ bi.amnt);
			return;
		}

		registerAmnt-=bi.amnt;
		bi.state=InventoryBillState.processed;
		bi.cashier.msgHereIsPayment(this, bi.amnt);
		inventoryBills.remove(bi);
		
			
		
	}
	private void ProcessBill(DCheck bi) {

		bi.setBillAmnt(prices.get(bi.getChoice()));
		bi.state=CheckState.processed;
		bi.getWaiter().msgCheckIsReady();
		
	}
	private void GiveCheckToWaiter() {
		
		DecimalFormat df = new DecimalFormat("###.##");
		
		for (DCheck bill: myBills) {
			if(bill.waiter==waiterAtRegister && bill.state==CheckState.processed) {
				
				for(DCheck findDebt: myBills) {
					if (findDebt.state==CheckState.debt && findDebt.getCustomer()== bill.getCustomer()) {
						//found some debt!
						AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCashierRole", "Found debt");
						System.out.println("found debt of "+findDebt.debt+" for this customer");
						bill.BillAmnt+=findDebt.debt;
						bill.BillAmnt=Double.parseDouble(df.format(bill.BillAmnt));
					}
				}
				
				waiterAtRegister.msgHereIsABill(bill);
				AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCashierRole", "Bill added");
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
		AlertLog.getInstance().logInfo(AlertTag.DRestaurant, "DCashierRole", "Computing change");
		Do("computing change");
		
		
		DecimalFormat df = new DecimalFormat("###.##");
		double changeval = Double.parseDouble(df.format(bi.getCustomerPaid()-bi.getBillAmnt()));
		if(changeval>=0) {
			
			//check and see if acquired debt
			for(DCheck bill: myBills) {
				if(bill.state==CheckState.debt && bill.customer==bi.getCustomer()) {
					if(bill.debt<=changeval) { // customer is trying to pay debt
						System.out.println("Customer's debt of "+ bill.debt+ " is added on to current bill");
						myBills.remove(bill);
					}
				}
			}
			bi.getCustomer().msgHereIsYourReceiptAndChange(changeval);
			registerAmnt+=bi.getCustomerPaid()-changeval;
		
			myBills.remove(bi);
		}
		else {
			bi.getCustomer().msgHereIsYourReceiptAndChange(0);
			registerAmnt+=bi.getCustomerPaid();
			bi.state=CheckState.debt;
			bi.debt=Double.parseDouble(df.format(bi.BillAmnt- bi.CustomerPaid));
		}
	


		
		
		
		//System.out.println("cash inflow.. regAmnt = "+ registerAmnt);
		
	}
	

	public static class InventoryBill {
		public MarketManager manager;
		public MarketCashier cashier;
		double amnt;
		public enum InventoryBillState {justReceived, pendingResponse, processing, couldNotAfford, processed, sent, fraud};
		public InventoryBillState state;
		
		InventoryBill(double a, MarketCashier csh, MarketManager m) {
			cashier = csh;
			manager = m;
			amnt = a;
			state=InventoryBillState.justReceived;
			
		}
		
		public MarketCashier getMarketCashier() {
			return cashier;
		}
		public double getAmnt() {
			return amnt;
		}
	}








}


