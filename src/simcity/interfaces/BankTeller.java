package simcity.interfaces;

//import simcity.Bank.BankTellerRole.MyCustomer;
//import simcity.Bank.BankTellerRole.accountState;
//import simcity.Bank.BankTellerRole.bankTellerState;

public interface BankTeller {
	
	public void msgMakeAccount(BankCustomer BC);
	
	public void msgAccountCreated(int num);
	
	public void msgDeposit(BankCustomer BC, int actNum, double amount);
	
	public void msgWithdrawal(BankCustomer BC, int actNum, double amount);
	
	public void msgTransactionProcessed(double finalAmount);
	
	public void msgIAmRobbingYou(BankRobber BR);
	
	public void msgIShotYou();
	
	public void msgGoHome();
	
	public void msgGoToTellerPosition();
}
