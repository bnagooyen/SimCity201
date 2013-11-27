package simcity.Drew_restaurant;

import agent.Role;
import simcity.PersonAgent;
import simcity.Bank.BankLoanOfficerRole.bankLoanState;
import simcity.Drew_restaurant.Drew_CashierRole.BillState;
//import restaurant.WaiterAgent.CustomerState;
import simcity.Drew_restaurant.gui.Bill;
import simcity.Drew_restaurant.interfaces.*;
import simcity.Drew_restaurant.test.mock.*;
import simcity.interfaces.MarketCashier;

import java.util.*;

//import restaurant.HostAgent.MyWaiter;
//import restaurant.MarketRole;
import simcity.PersonAgent;


/**
 * Restaurant Cook Agent
 */


public class Drew_CashierRole extends Role implements Drew_Cashier {
	
	//Data	
	
	private String name;
	
	private Double Cash=100.0;
	
	public List<MyBill> bills
	= new ArrayList<MyBill>();
	
	public List<owedBill> owedBills
	= new ArrayList<owedBill>();
	
	public enum BillState
	{calculated, givenToWaiter, recievedFromCustomer, done};
	 
	private  Map<String,Double> prices = new HashMap<String, Double>();

	private Drew_CookRole cook;
	private boolean onDuty;
	
	public EventLog log;
	public Drew_CashierRole() {
		this.name = name;
		
		onDuty=true;
		
		//Initialize price list
		prices.put("chicken", 10.99);
		prices.put("steak", 15.99);
		prices.put("salad", 5.99);
		prices.put("pizza", 8.99);
	}

	public String getName() {
		return name;
	}
	
	public double getCash(){
		return Cash;
	}

	// Messages
	
	public void msgGoHome(double pay){
		myPerson.money+=pay;
		onDuty=false;
		stateChanged();
	}
	
	public void calculateBill(Drew_Waiter w, String choice, int table, Double debt){
		bills.add(new MyBill(w,table, prices.get(choice.toLowerCase()),debt));
		stateChanged();
	}
	
	public void payBill(Double b, Double Money, Drew_Customer cust){
		MyBill mb = null;
		synchronized(bills) {
		for(MyBill bill : bills){
			if(bill.b.equals(b)){
				mb= bill;
			}
		}
		}
		mb.c=cust;
		mb.payment=Money;
		mb.s=BillState.recievedFromCustomer;
		stateChanged();
	}
	
	public void marketBill(Double cost, MarketCashier m){
		owedBill OB= new owedBill(cost,m);
		owedBills.add(OB);
	}


	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		/* Think of this next   rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		synchronized(owedBills) {
		for (owedBill bill : owedBills) {
			if(bill.getState()==BillState.calculated){
				payMarket(bill);
				return true;
			}
		}
		}
		synchronized(bills) {
		for (MyBill bill : bills) {
			if(bill.getState()==BillState.calculated){
				giveToWaiter(bill);
				return true;
			}
		}
		}
		synchronized(bills) {
		for (MyBill bill : bills) {
			if(bill.getState()==BillState.recievedFromCustomer){
				getChange(bill);
				return true;
			}
		}
		}
		if(!onDuty){
			leaveBank();
		}
		return false;
	}

	// Actions

	private void giveToWaiter(MyBill bill){
		bill.w.heresBill(bill.b, bill.t);
		print("You Owe: $"+bill.b);
		bill.s=BillState.givenToWaiter;
	} 
	
	private void getChange(MyBill bill){
		Double change = bill.payment-bill.b;
		Cash+=bill.payment;
		bill.c.giveChange(change);
		Cash-=change;
		bill.s=BillState.done;
	}
	
	private void payMarket(owedBill bill){
		Cash-=bill.b;
		bill.market.msgHereIsPayment(cook, bill.b);
		bill.s=BillState.done;
		print("Paid market $"+bill.b+". Cash remaining $"+Cash);
	}
	
	private void leaveBank(){
		//GUI
		this.isActive=false;
	}


	//utilities

	/*public void setGui(WaiterGui gui) {					GUI
		waiterGui = gui;
	}

	public WaiterGui getGui() {
		return waiterGui;
	}*/
	
	public void setCook(Drew_CookRole c) {
		cook = c;
	}
	public class MyBill {
		
		public Drew_Customer c;
		Drew_Waiter w;
		public Double b;
		Double payment;
		public int t;
		BillState s;
		
		MyBill(Drew_Waiter waiter, int table, Double bill, Double debt){
			w=waiter;
			t=table;
			b=bill+debt;
			s=BillState.calculated;
		}
		
		public BillState getState(){
			return s;
		}
	}
}
	
	 class owedBill {
		public Double b;
		BillState s;
		public MarketCashier market;
		
		owedBill(Double cost, MarketCashier m){
			b=cost;
			market=m;
			s=BillState.calculated;
		}
		public BillState getState(){
			return s;
		}
	}
	




