package simcity.interfaces;

import java.util.List;

import simcity.Market.MFoodOrder;
import simcity.Market.MarketCashierRole;
import simcity.Market.MarketCustomerRole.customerState;
//import simcity.Transportation.CarAgent;

public interface MarketCustomer {

	public void msgGoToCashier(MarketCashier c);
	
	public void msgHereIsOrder(List<MFoodOrder> canGive);

	public void msgMarketClosed();

	public void msgHereIsCar(Car car);

}
