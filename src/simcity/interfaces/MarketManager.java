package simcity.interfaces;

import simcity.Market.MarketManagerRole.MyCustomer;
import simcity.Market.MarketManagerRole.MyMarketCashier;
import simcity.Market.MarketManagerRole.workerState;
import agent.Role;

public interface MarketManager {

	public void msgTimeUpdate(int hour);
	
	public void msgIAmHere(Role r, String type);
	
	public void msgCustomerDone(MarketCashier mc, MarketCustomer c);
}
