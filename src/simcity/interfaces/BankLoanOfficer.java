package simcity.interfaces;

import simcity.Bank.BankLoanOfficerRole.MyCustomer;
import simcity.Bank.BankLoanOfficerRole.accountState;

//import simcity.Bank.BankLoanOfficerRole.MyCustomer;
//import simcity.Bank.BankLoanOfficerRole.bankLoanState;

public interface BankLoanOfficer {
	
	
	public void msgMakeAccount(BankCustomer BC);
	
	public void msgAccountCreated(int num);
	
	public void msgINeedALoan(BankCustomer BC, Integer AN, double amt, String Job);
	
	public void msgLoanDenied();
	
	public void msgLoanComplete();
	
}
