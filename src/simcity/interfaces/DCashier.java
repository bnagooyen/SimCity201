package simcity.interfaces;

public interface DCashier {

	public abstract void msgHereIsAPayment(DCustomer cust, int tnum, double valCustPaid);
	void msgComputeBill(String choice, DCustomer cust, String name, int tnum,
			DWaiter wa);
	public abstract void setRegisterAmnt(double amnt);
	public abstract double getRegisterAmnt();
	void msgBillFromMarket(double check, MarketCashier marketCashier);
}
