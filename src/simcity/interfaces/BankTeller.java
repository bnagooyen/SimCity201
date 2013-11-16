package simcity.interfaces;

//import simcity.Bank.BankTellerRole.MyCustomer;
//import simcity.Bank.BankTellerRole.accountState;
//import simcity.Bank.BankTellerRole.bankTellerState;

public interface BankTeller {
	
	public void makeAccount(BankCustomer BC);
	
	public void accountCreated(int num);
	
	public void Deposit(BankCustomer BC, int actNum, double amount);
	
	public void Withdrawal(BankCustomer BC, int actNum, double amount);
	
	public void transactionProcessed(double finalAmount);
	
	public void iAmRobbingYou(BankRobber BR);
	
	public void iShotYou();
	
	public void goHome();
	
	public void goToTellerPosition();
}
