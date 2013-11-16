package simcity.Bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simcity.PersonAgent;
import simcity.Bank.BankManagerRole.MyLoanOfficer.MyOfficerState;
import simcity.Bank.BankManagerRole.MyTeller.MyTellerState;
import simcity.interfaces.BankLoanOfficer;
import simcity.interfaces.BankManager;
import simcity.interfaces.BankTeller;
import agent.Role;

public class BankManagerRole extends Role implements BankManager {
	
	//data
	private List<MyTeller> tellers = Collections.synchronizedList(new ArrayList<MyTeller>());
	private List<MyLoanOfficer> officers = Collections.synchronizedList(new ArrayList<MyLoanOfficer>());
	private List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	private Map<Integer, MyAccount> accounts = new HashMap<Integer, MyAccount>();
	int hour;
	private double vault = 1000000000;
	
	protected BankManagerRole(PersonAgent p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	
	//MESSAGES
	
	public void msgTimeUpdate(int hr) {
		hour=hr;
		stateChanged();
	}
	
	public void msgIAmHere(Role person, String type) {
		//if(person.instanceof(BankTeller))
		if(type.equals("BankTeller")) {
			tellers.add(new MyTeller((BankTeller)person));
			stateChanged();
		}
		else if (type.equals("BankLoanOfficer")) {
			officers.add(new MyLoanOfficer((BankLoanOfficer)person));
			stateChanged();
		}
		else if(type.equals("loan")) {
			customers.add(new MyCustomer(person, MyCustomer.MyCustomerState.loan));
			stateChanged();
		}
		else if(type.equals("transaction")) {
			customers.add(new MyCustomer(person, MyCustomer.MyCustomerState.transaction));
			stateChanged();
		}
	}
	
	public void msgAvailable(BankTeller t) {
		tellers.get(0).state=MyTeller.MyTellerState.available;
		stateChanged();	
	}

	public void msgAvailable(BankLoanOfficer t) {
		officers.get(0).state=MyLoanOfficer.MyOfficerState.available;
		stateChanged();		
	}
	
	public void msgCreateAccount(String type) {
		if(type.equals("BankTeller")) {
			tellers.get(0).needsAccount=true;
			stateChanged();	
		}
		
		if(type.equals("LoanAgent")) {
			officers.get(0).needsAccount=true;
			stateChanged();
		}
	}
	public void msgProcessTransaction(int AN, double amount) {
		tellers.get(0).requested=amount;
		tellers.get(0).accountNum=AN;
		stateChanged();
	}
	
	public void msgNewLoan(int AN, double amount) {
		officers.get(0).requested=amount;
		officers.get(0).accountNum=AN;
		stateChanged();
	}
	
	public void msgGaveALoan(double cash) {
		vault -= cash;
		stateChanged();
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		
		if(tellers.size()>1) {
			SwapTellers();
			return true;
		}
		
		if(officers.size()>1) {
			SwapLoanOfficers();
			return true;
		}
		if(officers.size()==1 && officers.get(0).state==MyOfficerState.justArrived) {
			AddLoanOfficer();
			return true;
		}
		
		if(tellers.size()==1 && tellers.get(0).state==MyTellerState.justArrived) {
			AddTeller();
			return true;
		}
		
		if(tellers.get(0).needsAccount) {
			NewAccount();
			return true;
		}
		
		if(tellers.get(0).requested!=0) {
			CompleteTransaction();
			return true;
		}
		
		if(officers.get(0).requested!=0) {
			CompleteLoan();
			return true;
		}
		
		if(hour>19) {
			CloseBank();
			return true;
		}
		
		
		return false;
	}
	
	//actions
	private void SwapTellers() {
		tellers.get(0).emp.msgGoHome();
		tellers.get(1).emp.msgGoToTellerPosition();
		tellers.get(1).state=MyTellerState.available;
		tellers.remove(tellers.get(0));
	}
	
	private void SwapLoanOfficers() {
		officers.get(0).emp.msgGoHome();
		officers.get(1).emp.msgToToLoanOfficerPosition();
		officers.get(1).state=MyOfficerState.available;
		officers.remove(officers.get(0));
	}
	
	private void AddTeller() {
		tellers.get(0).emp.msgGoToTellerPosition();
		tellers.get(0).state=MyTellerState.available;
	}
	
	private void AddLoanOfficer() {
		officers.get(0).emp.msgGoToLoanOfficerPosition();
		officers.get(0).state=MyOfficerState.available;
	}
	
	private void SendCustomerToTeller(MyCustomer c) {
		tellers.get(0).state=MyTellerState.occupied;
		c.customer.msgGoToTeller(tellers.get(0));
		customers.remove(c);
	}
	
	private void SendCustomerToLoanOfficer(MyCustomer c) {
		officers.get(0).state=MyOfficerState.occupied;
		c.customer.msgToToLoanOfficer(officers.get(0));
		customers.remove(c);
	}
	
	public static class MyTeller {
		BankTeller emp;
		boolean needsAccount=false;
		int accountNum = 0;
		double requested=0.0;
		enum MyTellerState {justArrived, available, occupied};
		MyTellerState state;
		
		MyTeller (BankTeller p) {
			emp = p;
			state= MyTellerState.justArrived;
		}
		
	}

	public static class MyLoanOfficer {
		BankLoanOfficer emp;
		boolean needsAccount=false;
		int accountNum = 0;
		double requested=0.0;
		enum MyOfficerState {justArrived, available, occupied};
		MyOfficerState state;
		enum EmployeeType {LoanOfficer, Teller};
		EmployeeType type;
		
		MyLoanOfficer (BankLoanOfficer p) {
			emp = p;
			state= MyOfficerState.justArrived;
		}
		
	}
	
	public static class MyCustomer {
		Role customer;
		enum MyCustomerState {transaction, loan};
		MyCustomerState state;
		
		MyCustomer(Role b, MyCustomerState st) {
			state=st;
			customer = b;
		}
		
	}
	
	class MyAccount{
	
		Double balance;
		Double Loan;
	}
	
}
