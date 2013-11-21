package simcity.LRestaurant.test.mock;


import simcity.LRestaurant.LCustomerRole;
import simcity.LRestaurant.LWaiterRole.CustomerState;
import simcity.LRestaurant.LWaiterRole.MyCustomers;
import simcity.LRestaurant.LWaiterRole.WaiterState;
import simcity.LRestaurant.interfaces.LCashier;
import simcity.LRestaurant.interfaces.LCustomer;
import simcity.LRestaurant.interfaces.LWaiter;

public class MockWaiter extends Mock implements LWaiter {

	public LCashier cashier;
	public EventLog log = new EventLog();

	public MockWaiter(String name) {
		super(name);

	}
	public void msgBreakReply(boolean reply){
		
	}
	
	public void msgSeatCustomer(LCustomer cust, int table){
		
	}

	public void msgReadyToOrder(LCustomer cust){
		
	}

	public void msgHereIsMyChoice(LCustomer cust, String choice){
		
	}

	public void msgRanOutofFood(int table, String choice){
		
	}

	public void msgOrderIsReady(int table, String choice){
		
	}

	public void msgReadyForCheck(LCustomer cust){
		
	}

	public void msgHereIsCheck(int check, LCustomer cust){
		log.add(new LoggedEvent("Received msgHereIsCheck from cashier."));
	}

	public void msgDoneEatingAndLeaving(LCustomer cust){
		
	}

	public void msgTask(){
		
	}
	

}
