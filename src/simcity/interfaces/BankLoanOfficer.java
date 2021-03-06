package simcity.interfaces;

import simcity.PersonAgent;
import simcity.Bank.gui.BankLoanGui;

//import simcity.Bank.BankLoanOfficerRole.MyCustomer;
//import simcity.Bank.BankLoanOfficerRole.accountState;

//import simcity.Bank.BankLoanOfficerRole.MyCustomer;
//import simcity.Bank.BankLoanOfficerRole.bankLoanState;

public interface BankLoanOfficer {
	
	public void msgMakeAccount(BankCustomer BC);
	
	public void msgAccountCreated(int num);
	
	public void msgINeedALoan(BankCustomer BC, Integer AN, double amt, String Job);
	
	public void msgLoanDenied();
	
	public void msgLoanComplete();
	
	public void msgGoHome(double pay); // add this method!

	public void msgGoToLoanOfficerPosition();

	void msgAnimationFinishedGoToCorner();

	void msgAnimationFinishedLeaveBank();

	void msgAtLoanPos();

	public void setManager(BankManager m);

	public void setGui(BankLoanGui lOGui);

	public PersonAgent getPerson();


}
