package simcity.interfaces;

import java.util.List;

import simcity.Market.MFoodOrder;
import simcity.Transportation.DeliveryTruckAgent.truckState;
import simcity.test.mock.EventLog;

public interface DeliveryTruck{

	public abstract void msgGoToDestination(MarketCashier cashier, List<MFoodOrder>deliver, String location, double bill, Cook c);
	
	public abstract void msgAtDestination();
	
}