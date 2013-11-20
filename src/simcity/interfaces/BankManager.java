package simcity.interfaces;

//import simcity.Bank.BankManagerRole.MyCustomer;
//import simcity.Bank.BankManagerRole.MyLoanOfficer;
//import simcity.Bank.BankManagerRole.MyTeller;

import simcity.housing.LandlordRole;
import agent.Role;

public interface BankManager {

	abstract public void msgTimeUpdate(int hr);
	
	abstract public void msgIAmHere(Role person, String type);
	
	abstract public void msgAvailable(BankTeller t);
	
	abstract public void msgAvailable(BankLoanOfficer t);
	
	abstract public void msgCreateAccount(String type);
	
	abstract public void msgProcessTransaction(int AN, double amount);
	
	abstract public void msgNewLoan(int AN, double amount);
	
	abstract public void msgGaveALoan(double cash);
	
	abstract public void msgHereIsYourRentBill(Landlord l, Integer account, double rentBill);
}
