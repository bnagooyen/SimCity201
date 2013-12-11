package simcity.interfaces;

//import simcity.Bank.BankRobberRole.bankRobberState;

public interface BankRobber {
	public void msgGoToTeller(BankTeller t);
	
	public void msgHereIsMoney(double amount);
	
	public void msgIRefuseToPay();
	
	public void msgIShotYou();

	public void setManager(BankManager m);

	public void msgAtTellerPos();

	public void msgAnimationFinishedLeaveBank();
}
