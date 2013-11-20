package simcity.test.mock;


import simcity.KRestaurant.KMenu;
import simcity.KRestaurant.KWaiterRole;
import simcity.interfaces.KCashier;
import simcity.interfaces.KCustomer;

public class MockKRestaurantCustomer extends Mock implements KCustomer {
	
	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public KCashier cashier;
	public EventLog log;
	
	public MockKRestaurantCustomer(String name) {
		super(name);
		log = new EventLog();
	}

	@Override
	public void msgChange(double change) {
		LoggedEvent e = new LoggedEvent("Received msgChange from cashier. Total = " + change);
		log.add(e);
	}

	@Override
	public void msgHereIsYourFood() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfChoice(KMenu m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSitAtTable(int table, KWaiterRole w, KMenu m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatWouldYouLike() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsCheck(double check) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOkLeave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRestaurantFull(int spot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGoToSeeWaiter() {
		// TODO Auto-generated method stub
		
	}

	//@Override
//	public void HereIsYourTotal(double total) {
//		log.add(new LoggedEvent("Received HereIsYourTotal from cashier. Total = "+ total));
//
//		if(this.name.toLowerCase().contains("thief")){
//			//test the non-normative scenario where the customer has no money if their name contains the string "theif"
//			cashier.IAmShort(this, 0);
//
//		}else if (this.name.toLowerCase().contains("rich")){
//			//test the non-normative scenario where the customer overpays if their name contains the string "rich"
//			cashier.HereIsMyPayment(this, Math.ceil(total));
//
//		}else{
//			//test the normative scenario
//			cashier.HereIsMyPayment(this, total);
//		}
//	}

//	@Override
//	public void HereIsYourChange(double total) {
//		log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ total));
//	}
//
//	@Override
//	public void YouOweUs(double remaining_cost) {
//		log.add(new LoggedEvent("Received YouOweUs from cashier. Debt = "+ remaining_cost));
//	}

}
