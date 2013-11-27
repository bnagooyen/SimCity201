package simcity.interfaces;

import simcity.DRestaurant.DWaiterRole;

public interface DHost {

	void msgAddWaiter(DWaiter dw);

	void addCook(DCook cook);

	void msgGoOnBreakPlease(DWaiterRole dWaiterRole);

	void msgBackToWork(DWaiterRole dWaiterRole);

	void msgHereToTakeMyCustomer(DWaiter dWaiterRole);

	void msgTableIsClear(int tablenum, DWaiterRole dWaiterRole);

	void msgIWantFood(DCustomer dCustomerRole);

	void msgIDontWantToWait(DCustomer dCustomerRole);

	void msgHereToGetSeated(DCustomer dCustomerRole);

}
