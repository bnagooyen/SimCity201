package simcity.interfaces;

public interface Cashier {

	public abstract void msgHereIsAPayment(Customer cust, int tnum, double valCustPaid);
	void msgComputeBill(String choice, Customer cust, String name, int tnum,
			Waiter wa);
	public abstract void setRegisterAmnt(double amnt);
	public abstract double getRegisterAmnt();
//	public abstract void msgMadeInventoryOrder(int oRDER_ID, double billAmnt, Market a);
	public abstract void msgAnswerVerificationRequest(boolean b);
}
