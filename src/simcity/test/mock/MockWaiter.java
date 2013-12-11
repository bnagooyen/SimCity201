package simcity.test.mock;

import java.util.List;

import simcity.DRestaurant.DCheck;
import simcity.DRestaurant.DCustomerRole;
import simcity.DRestaurant.DOrder;

public class MockWaiter extends MockRole implements Waiter {
	private String name;
	public EventLog log = new EventLog();
	public MockWaiter(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgSitAtTable(int t, DCustomerRole cust) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgSitAtTable"));
	}

	@Override
	public void msgImReadyToOrder(DCustomerRole cust) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgImReadyToOrder"));	
	}

	@Override
	public void msgHereIsMyChoice(DCustomerRole cust, String choice) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgHereIsMyChoice"));		
	}

	@Override
	public void msgOutOfFood(DOrder o) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgOutOfFood"));	
	}

	@Override
	public void msgOrderIsReady(int o) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgOrderIsReady"));	
		
	}

	@Override
	public void msgDoneEatingAndLeaving(DCustomerRole cust) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgDoneEatingAndLeaving"));	
	}

	@Override
	public void msgCantAffordNotStaying(DCustomerRole cust) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgCantAffordNotStaying"));	
	}

	@Override
	public void msgAtTable() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgAtTable"));	
	}

	@Override
	public void msgIWantABreak() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgIWantABreak"));	
	}

	@Override
	public void msgBreakReply(Boolean yn) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgBreakReply"));	
	}

	@Override
	public void msgOutOfBreak() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgOutOfBreak"));	
	}

	@Override
	public void msgHereIsACheck(int tnum, double amnt) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgHereIsACheck"));	
	}

	@Override
	public void msgCheckIsReady() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgCheckIsReady"));	
	}

	@Override
	public void msgHereIsABill(DCheck bill) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgHereIsABill"));	

		
	}
}
