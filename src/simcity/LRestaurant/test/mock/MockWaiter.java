package simcity.LRestaurant.test.mock;


import restaurant.CustomerRole;
import restaurant.WaiterRole.CustomerState;
import restaurant.WaiterRole.MyCustomers;
import restaurant.WaiterRole.WaiterState;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

public class MockWaiter extends Mock implements Waiter {

	public Cashier cashier;
	public EventLog log = new EventLog();

	public MockWaiter(String name) {
		super(name);

	}
	public void msgBreakReply(boolean reply){
		
	}
	
	public void msgSeatCustomer(Customer cust, int table){
		
	}

	public void msgReadyToOrder(Customer cust){
		
	}

	public void msgHereIsMyChoice(Customer cust, String choice){
		
	}

	public void msgRanOutofFood(int table, String choice){
		
	}

	public void msgOrderIsReady(int table, String choice){
		
	}

	public void msgReadyForCheck(Customer cust){
		
	}

	public void msgHereIsCheck(int check, Customer cust){
		log.add(new LoggedEvent("Received msgHereIsCheck from cashier."));
	}

	public void msgDoneEatingAndLeaving(Customer cust){
		
	}

	public void msgTask(){
		
	}
	

}
