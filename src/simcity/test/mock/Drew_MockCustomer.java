package simcity.test.mock;


import simcity.Drew_restaurant.gui.CustomerGui;
import simcity.Drew_restaurant.gui.Menu;
import simcity.Drew_restaurant.interfaces.Drew_Cashier;
import simcity.Drew_restaurant.interfaces.Drew_Customer;
import simcity.Drew_restaurant.interfaces.Drew_Waiter;
import simcity.Drew_restaurant.interfaces.Drew_Host;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class Drew_MockCustomer extends Mock implements Drew_Customer {
	


	//Data
	public Drew_Cashier cashier;
	public Drew_Host host;
	public Drew_Waiter waiter;
	public EventLog log;
	public String name;
	public double debt;
	public double change=-1.0;
	public CustomerGui customergui;
	
	
	
	//functions
	public Drew_MockCustomer(String n) {
		super(n);
		//name=n;
	}
	
	public void gotHungry(){
		log.add(new LoggedEvent("Sent message msgIWantFood"));
		host.msgIWantFood(this);
	}
	
	public void wait(int numAH){
		log.add(new LoggedEvent("Received wait message from host. There are "+numAH+" people ahead"));
	}

	public void followMeToTable(Menu m){
	}
	
	public void msgAnimationFinishedGoToSeat(){
	}
	
	public void whatWouldYouLike(){
	}
	
	public void hereIsYourFood(){
		
	}
	
	//From waiter when initial choice is out of stock
	public void outOfChoice(String choice){
	}
	
	public void msgAnimationFinishedLeaveRestaurant(){
	}
	
	public void giveCheck(Double b, Drew_Cashier c){
		System.out.println("Got check from cashier for $"+b);
	}
	
	public void giveChange(Double ch){
		if (ch>=0){
			change=ch;
			System.out.println("Customer: Received Change: $"+ch);
		}
		else if (ch<0){
			debt=ch*-1;
			System.out.println("Customer: Accrued Debt: $"+ch*-1);
		}
		
	}
	
	public void atCashier(){
	}
	
	
	//accessors
	public void setWaiter(Drew_Waiter w){
	}

	public String getName(){
		return name;
	}
	
	public CustomerGui getGui(){
		return customergui;
	}
	
	public double getDebt(){
		return debt;
	}
	
}
/*
	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	/*public Cashier cashier;

	public MockCustomer(String name) {
		super(name);

	}

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
	
	public CustomerGui getGui() {
		return customerGui;
	}

}*/
