package simcity.test.mock;

import simcity.interfaces.TCashier;
import simcity.interfaces.TCustomer;
import simcity.interfaces.TWaiter;



/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class TMockWaiter extends Mock implements TWaiter {
	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public TCashier cashier;
	public EventLog log = new EventLog(); 
	public LoggedEvent event; 

	public TMockWaiter(String name) {
		super(name);

	}
	
	public void msgSeatAtTable(TCustomer cust, int t) {
		
	}

	public void msgReadyToOrder(TCustomer cust) {
		
	}

	public void msgHeresMyChoice(TCustomer cust, String order) {
		
	}

	public void msgOutOfFood(int t) {
		
	}

	public void msgOrderIsReady(int t) {
		
	}
	
	public void msgReadyForCheck(TCustomer cust) {
		
	}
	
	public void msgHereIsCheck(TCustomer cust, double c) {
		LoggedEvent m = new LoggedEvent ("Received check from cashier to give to customer. Bill = " + c); 
		log.add(m);
	}
	
	public void msgLeavingTable(TCustomer cust) {
		
	}

	public void msgNoBreak() {
		
	}

	public void msgOnBreak() {
		
	}

	@Override
	public void msgGoHome(double moneys) {
		// TODO Auto-generated method stub
		
	}
	
	/**

	@Override
	public void msgHereIsYourCheck(double total) {
		log.add(new LoggedEvent("Received HereIsYourTotal from cashier. Total = "+ total));

		if(this.name.toLowerCase().contains("thief")){
			//test the non-normative scenario where the customer has no money if their name contains the string "theif"
			cashier.IAmShort(this, 0);

		}else if (this.name.toLowerCase().contains("rich")){
			//test the non-normative scenario where the customer overpays if their name contains the string "rich"
			cashier.HereIsMyPayment(this, Math.ceil(total));

		}else{
			//test the normative scenario
			cashier.HereIsMyPayment(this, total);
		}
	}

	@Override
	public void msgChange(double total) {
		log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ total));
	}
    **/

}
