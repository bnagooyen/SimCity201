package simcity.interfaces;

import simcity.KRestaurant.KMenu;
import simcity.KRestaurant.KWaiterRole;


public interface KCustomer {
	/**
	 * @param total The cost according to the cashier
	 *
	 * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
	 */
	//public abstract void HereIsYourTotal(double total);

	/**
	 * @param total change (if any) due to the customer
	 *
	 * Sent by the cashier to end the transaction between him and the customer. total will be >= 0 .
	 */
	
	public KCashier c = null;
	
	//public abstract void HereIsYourChange(double total);
	public abstract void msgChange(double change);
	public abstract void msgHereIsYourFood();
	public abstract void msgOutOfChoice(KMenu m);
	public abstract void msgSitAtTable(int table, KWaiterRole w, KMenu m);
	public abstract void msgWhatWouldYouLike();
	public abstract void msgHereIsCheck(double check);
	public abstract void msgOkLeave();
	public abstract void msgRestaurantFull(int spot);
	/**
	 * @param remaining_cost how much money is owed
	 * Sent by the cashier if the customer does not pay enough for the bill (in lieu of sending {@link #HereIsYourChange(double)}
	 */
	//public abstract void YouOweUs(double remaining_cost);
	public abstract void msgGoToSeeWaiter();

}