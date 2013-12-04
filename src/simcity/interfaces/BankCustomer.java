package simcity.interfaces;

import simcity.Bank.gui.BankCustomerGui;

//import simcity.Bank.BankCustomerRole.bankCustomerState;

public interface BankCustomer {

	public void msgGoToLoanOfficer(BankLoanOfficer BL);
	
	public void msgGoToTeller(BankTeller BT);
	
	public void msgTransactionComplete(double amount);
	
	public void msgHeresLoan(double amount);
	
	public void msgLoanDenied();
	
	public void msgAccountMade(int AN);

	public void msgLeaveBank();

	public void msgAtTellerPos();

	public void msgAnimationFinishedLeaveBank();

	public void msgAtLoanPos();
	
	public void setManager(BankManager m);
	
	public void setGui(BankCustomerGui BC);
}
