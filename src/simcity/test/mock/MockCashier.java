package simcity.test.mock;


import agent.Role;
import simcity.PersonAgent;
import simcity.Market.MarketCashierRole;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.MarketManager;
import simcity.restaurant.CashierRole;
import simcity.restaurant.interfaces.Cashier;
import simcity.restaurant.interfaces.Customer;
import simcity.restaurant.interfaces.Waiter;


public class MockCashier extends Mock implements Cashier {
	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	public EventLog log = new EventLog();
	public PersonAgent p;
	public CashierRole cr;
	
	public MockCashier(String name) {
		super(name);
		cr = new CashierRole(p);
	}
	
	@Override
	public void msgHereIsAPayment(Customer cust, int tnum, double valCustPaid) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void msgComputeBill(String choice, Customer cust, String name,
			int tnum, Waiter wa) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setRegisterAmnt(double amnt) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public double getRegisterAmnt() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void msgMadeInventoryOrder(int orderid, double billAmt) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void msgBillFromMarket(double check,
			MarketCashierRole marketCashierRole) {
		LoggedEvent e = new LoggedEvent("Received msgBillFromMarket from market manager");
		log.add(e);
		
	}


}
