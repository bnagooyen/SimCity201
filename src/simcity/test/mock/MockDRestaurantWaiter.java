package simcity.test.mock;

import simcity.DRestaurant.DCheck;
import simcity.DRestaurant.DCustomerRole;
import simcity.DRestaurant.DOrder;
import simcity.interfaces.DCustomer;
import simcity.interfaces.DWaiter;
import simcity.interfaces.KCashier;
import simcity.interfaces.KCustomer;
import simcity.interfaces.KWaiter;
import simcity.interfaces.RestaurantCashier;

public class MockDRestaurantWaiter extends Mock implements DWaiter{

	public RestaurantCashier cashier; 
	public EventLog log;
	
	public MockDRestaurantWaiter(String name) {
		super(name);
		log = new EventLog();
	}

	@Override
	public void msgSitAtTable(int t, DCustomerRole cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgImReadyToOrder(DCustomerRole cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyChoice(DCustomerRole cust, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCheckIsReady() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfFood(DOrder o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneEatingAndLeaving(DCustomerRole cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCantAffordNotStaying(DCustomerRole cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWantABreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBreakReply(Boolean yn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsACheck(int tnum, double amnt) {
		// TODO Auto-generated method stub
		LoggedEvent e = new LoggedEvent("received check from cashier");
		log.add(e);
	}

	@Override
	public void msgHereIsABill(DCheck bill) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderIsReady(int tablenum) {
		// TODO Auto-generated method stub
		
	}


}
