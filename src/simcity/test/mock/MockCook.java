package simcity.test.mock;

import java.util.List;

import simcity.Market.MFoodOrder;
import simcity.interfaces.Cook;
import simcity.interfaces.MarketCashier;

public class MockCook extends Mock implements Cook{

	public EventLog log;
	
	public MockCook(String name) {
		super(name);
		log = new EventLog();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsDelivery(List<MFoodOrder> canGive) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGoToCashier(MarketCashier c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgMarketClosed() {
		// TODO Auto-generated method stub
		
	}
	
}