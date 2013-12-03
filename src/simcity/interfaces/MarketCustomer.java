package simcity.interfaces;

import java.util.List;

import simcity.Market.MFoodOrder;
import simcity.Market.MarketCashierRole;
import simcity.Market.MarketCustomerRole.customerState;
//import simcity.Transportation.CarAgent;

public interface MarketCustomer {

	public void msgGoToCashier(MarketCashier c);
	
	public void msgHereIsOrderAndCheck(List<MFoodOrder> canGive, double check);

	public void msgMarketClosed();

	public void msgHereIsCarAndCheck(Car car, double check);

}
