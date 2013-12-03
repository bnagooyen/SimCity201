package simcity.interfaces;

import java.util.List;

import simcity.Market.MFoodOrder;

public interface Cook {

	public abstract void msgHereIsDelivery(List<MFoodOrder> canGive, double check, MarketManager manager, MarketCashier mc);

	public abstract void msgGoToCashier(MarketCashier c);

	public abstract void msgMarketClosed();

}
