package simcity.test.mock;

import java.util.List;

import simcity.Market.MFoodOrder;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;

public class MockMarketCustomer extends Mock implements MarketCustomer{

	EventLog log;
	public MockMarketCustomer(String name) {
		super(name);
		log = new EventLog();
	}

	@Override
	public void msgGoToCashier(MarketCashier c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsOrderAndCheck(List<MFoodOrder> canGive, double check) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgMarketClosed() {
		// TODO Auto-generated method stub
		
	}

}
