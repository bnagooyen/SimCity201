package restaurant.test.mock;


import restaurant.Menu;
import restaurant.WaiterAgent;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;

public class MockCustomer extends Mock implements Customer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	public EventLog log = new EventLog();
	public double Wallet;
	public double Debt;
	public void setWallet(double w) {
		Wallet = w;
	}
	public double getWalled() {
		return Wallet;
	}
	public void setDebt(double d) {
		Debt=d;
	}
	
	public double getDebt(double d) {
		return Debt;
	}

	public MockCustomer(String name) {
		super(name);

	}
	/*
	@Override
	public void HereIsYourTotal(double total) {
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
	public void HereIsYourChange(double total) {
		log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ total));
	}

	@Override
	public void YouOweUs(double remaining_cost) {
		log.add(new LoggedEvent("Received YouOweUs from cashier. Debt = "+ remaining_cost));
	}
	*/

	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msggotHungry"));
	}

	@Override
	public void msgNoRoomForYou() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgNoRoomForYou"));
	}

	@Override
	public void msgFollowMe(Menu menu, int tnum, WaiterAgent w) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgFollowMe"));
	}

	@Override
	public void msgHereIsYourBill(int tnum, double d) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgHereIsYourBill"));
	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgAnimationFinishedGoToSeat"));
	}

	@Override
	public void msgWhatWouldYouLike() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received  msgWhatWouldYouLike"));
	}

	@Override
	public void msgWhatWouldYouLike(String foodOutOfStock) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgWhatWouldYouLike, out of my order"));
	}

	@Override
	public void msgFoodIsServed() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgFoodIsServed"));
	}

	@Override
	public void msgHereIsYourReceiptAndChange(double num) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgHereIsYourReceiptAndChange"));
	}

	@Override
	public void msgAnimationArrivedAtCashier() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received Animation msgAnimationArrivedAtCashier"));
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgAnimationFinishedLeaveRestaurant"));
	}

	@Override
	public void msgGoToHangout() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgGoToHangout"));
	}

	@Override
	public void msgYourTableIsReady() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgYourTableIsReady"));
	}

}
