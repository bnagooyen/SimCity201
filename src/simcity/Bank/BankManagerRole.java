package simcity.Bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simcity.PersonAgent;
import simcity.Bank.BankManagerRole.MyCustomer.MyCustomerState;
import simcity.Bank.BankManagerRole.MyLoanOfficer.MyOfficerState;
import simcity.Bank.BankManagerRole.MyTeller.MyTellerState;
import simcity.housing.LandlordRole;
import simcity.interfaces.BankCustomer;
import simcity.interfaces.BankLoanOfficer;
import simcity.interfaces.BankManager;
import simcity.interfaces.BankTeller;
import simcity.interfaces.Landlord;
import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;
import agent.Role;

public class BankManagerRole extends Role implements BankManager {
	
	//data
	public EventLog log = new EventLog();
	private List<MyTeller> tellers = Collections.synchronizedList(new ArrayList<MyTeller>());
	private List<MyLoanOfficer> officers = Collections.synchronizedList(new ArrayList<MyLoanOfficer>());
	private List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	public List<MyClient> clients = Collections.synchronizedList(new ArrayList<MyClient>()); 
	public Map<Integer, MyAccount> accounts = new HashMap<Integer, MyAccount>();
	int hour;
	private boolean bankIsOpen;
	public enum BankState {open, closed, newDay};
	BankState bankState;
	private double employeePayPerHour=10;
	private double vault = 1000000000;
	
	public BankManagerRole(PersonAgent p) {
		super(p);
		// TODO Auto-generated constructor stub
		startHour=7;
		bankState=BankState.newDay;
		hour=0;
		officers.clear();
		tellers.clear();
		customers.clear();
		
	}

	
	//MESSAGES
	
	public void msgTimeUpdate(int hr) {
		hour=hr;
		if(hr==1) bankState=BankState.newDay;
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
			customers.add(new MyCustomer((BankCustomer)person, MyCustomer.MyCustomerState.loan));
			stateChanged();
		}
		else if(type.equals("transaction")) {
			customers.add(new MyCustomer((BankCustomer)person, MyCustomer.MyCustomerState.transaction));
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
		if(type.equals("BankLoanOfficer")) {
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
//		System.err.println(officers.get(0).accountNum + " "+ officers.get(0).requested);
		stateChanged();
	}
	
	public void msgGaveALoan(double cash) {
		vault -= cash;
		stateChanged();
	}
	
	public void msgHereIsYourRentBill(Landlord l, Integer account, double rentBill) {
		clients.add(new MyClient((Landlord) l, account, rentBill));
		stateChanged(); 
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		
//		if(!isActive) {
//			return false;
//		}
		
		if(officers.size()==1 && officers.get(0).state==MyOfficerState.justArrived) {
			AddLoanOfficer();
			return true;
		}
		
		if(tellers.size()==1 && tellers.get(0).state==MyTellerState.justArrived) {
			AddTeller();
			return true;
		}
		
		if(hour>=8 && bankState==BankState.newDay && officers.size()>=1 && tellers.size()>=1) {
			OpenBank();
			return true;
		}
		
		
		if(hour>=19 && bankState==BankState.open) {
			CloseBank();
			return true;
		}
		
		if(tellers.size()>1) {
			SwapTellers();
			return true;
		}
		
		if(officers.size()>1) {
			SwapLoanOfficers();
			return true;
		}
	
		
		if(!tellers.isEmpty() && tellers.get(0).needsAccount) {
			NewAccountForTeller();
			return true;
		}
		
		if(!tellers.isEmpty() && tellers.get(0).requested!=0) {
			CompleteTransaction();
			return true;
		}
		
		if(!officers.isEmpty() && tellers.get(0).needsAccount) {
			NewAccountForOfficer();
			return true;
		}
		
		if(!officers.isEmpty() && officers.get(0).requested!=0) {
			CompleteLoan();
			return true;
		}
		
		if(!clients.isEmpty()) {
			TransferMoneyToLandlord(); 
			return true; 
		}

		
		if(customers.size()>0 && (tellers.isEmpty() || officers.isEmpty())) {
			BankIsClosed();
			return true;
		}
		
		if(!tellers.isEmpty() && tellers.get(0).state==MyTellerState.available) {
			for(MyCustomer cust: customers) {
				if (cust.state==MyCustomerState.transaction) {
					SendCustomerToTeller(cust);
					return true;
				}
			}
		}
		
		if( !officers.isEmpty() && officers.get(0).state==MyOfficerState.available) {
			for(MyCustomer cust: customers) {
				if (cust.state==MyCustomerState.loan) {
					SendCustomerToLoanOfficer(cust);
					return true;
				}
			}
		}
		
		return false;
	}
	
	//actions
	
	private void OpenBank() {
		bankState=BankState.open;
	}
	private void SwapTellers() {
		tellers.get(0).emp.msgGoHome((hour-tellers.get(0).startHr)*employeePayPerHour);
		tellers.get(1).emp.msgGoToTellerPosition();
		tellers.get(1).startHr=hour;
		tellers.get(1).state=MyTellerState.available;
		tellers.remove(tellers.get(0));
	}
	
	private void SwapLoanOfficers() {
		officers.get(0).emp.msgGoHome((hour-officers.get(0).startHr)*employeePayPerHour);
		officers.get(1).emp.msgGoToLoanOfficerPosition();
		officers.get(1).startHr=hour;
		officers.get(1).state=MyOfficerState.available;
		officers.remove(officers.get(0));
	}
	
	private void AddTeller() {
		tellers.get(0).emp.msgGoToTellerPosition();
		tellers.get(0).state=MyTellerState.available;
		tellers.get(0).startHr=hour;
	}
	
	private void AddLoanOfficer() {
		officers.get(0).emp.msgGoToLoanOfficerPosition();
		officers.get(0).state=MyOfficerState.available;
		officers.get(0).startHr=hour;
	}
	
	private void SendCustomerToTeller(MyCustomer c) {
		tellers.get(0).state=MyTellerState.occupied;
		c.customer.msgGoToTeller(tellers.get(0).emp);
		customers.remove(c);
	}
	
	private void SendCustomerToLoanOfficer(MyCustomer c) {
		officers.get(0).state=MyOfficerState.occupied;
		c.customer.msgGoToLoanOfficer(officers.get(0).emp);
		customers.remove(c);
	}
	
	private void TransferMoneyToLandlord() {
		if (accounts.get(clients.get(0).AN).balance >= clients.get(0).bill) {
			accounts.get(clients.get(0).AN).balance -= clients.get(0).bill;
			clients.get(0).client.HereIsARentPayment(clients.get(0).AN, clients.get(0).bill);
		}
		else {
			clients.get(0).client.CannotPayForRent(clients.get(0).AN);
		}		
		clients.remove(0); 
	}
	
	private void BankIsClosed() {
//		System.out.println("bankisclosed");
		for(MyCustomer c: customers) {
			c.customer.msgLeaveBank();
		}
		customers.clear();
	}
	
	private void CloseBank() {
		officers.get(0).emp.msgGoHome((hour-officers.get(0).startHr)*employeePayPerHour);
		officers.clear();
		tellers.get(0).emp.msgGoHome((hour-tellers.get(0).startHr)*employeePayPerHour);
		tellers.clear();
		bankState=BankState.closed;
		BankIsClosed();
		isActive = false;
	}
	
	private void NewAccountForTeller() {
		accounts.put(accounts.size()+1, new MyAccount(0,0));
		tellers.get(0).needsAccount=false;
		tellers.get(0).emp.msgAccountCreated(accounts.size());
	}
	
	private void NewAccountForOfficer() {
		accounts.put(accounts.size()+1, new MyAccount(0,0));
		officers.get(0).needsAccount=false;
		officers.get(0).emp.msgAccountCreated(accounts.size());
	}
	
	private void CompleteTransaction() {
		if(accounts.get(tellers.get(0).accountNum)==null) { // customer doesn't exist
//			tellers.get(0).msgAccountInexistant();
			return;
		}
		if(tellers.get(0).requested<-1*(accounts.get(tellers.get(0).accountNum)).balance) {
			tellers.get(0).requested=-1*(accounts.get(tellers.get(0).accountNum).balance);
		}
		MyAccount update = accounts.get(tellers.get(0).accountNum);
		update.balance+=tellers.get(0).requested;
		accounts.put(tellers.get(0).accountNum, update);
		tellers.get(0).emp.msgTransactionProcessed(tellers.get(0).requested);
		tellers.get(0).requested=0.0;
		tellers.get(0).accountNum=0;
		
	}
	
	private void CompleteLoan() {
//		System.err.println(accounts.get(officers.get(0).accountNum));
		if(accounts.get(officers.get(0).accountNum)==null) { //means account doesn't exist
			officers.get(0).emp.msgLoanDenied();
			officers.get(0).requested=0; //reset val 
			return;
		}
		if(accounts.get(officers.get(0).accountNum).loan!=0.0) { // fix based on drews answer
			officers.get(0).emp.msgLoanDenied();
			officers.get(0).requested=0; //reset val 
			return;
		}
		else {
			MyAccount update = accounts.get(officers.get(0).accountNum);
			update.loan=officers.get(0).requested;
			accounts.put(officers.get(0).accountNum, update);
			officers.get(0).emp.msgLoanComplete();
			vault-=officers.get(0).requested;
			officers.get(0).requested=0;
		}
	}
	
	//accessors for testing
	public List<MyLoanOfficer> getOfficer() {
		return officers;
	}
	
	public List<MyTeller> getTeller() {
		return tellers;
	}
	
	public List<MyCustomer> getCustomers() {
		return customers;
	}
	
	public Map<Integer, MyAccount> getAccounts() {
		return accounts;
	}
	
	public int getHour() {
		return hour;
	}
	
	public double getVault() {
		return vault;
	}
	
	public BankState getBankStatus() {
		return bankState;
	}
	
	//classes
	
	public static class MyTeller {
		BankTeller emp;
		boolean needsAccount=false;
		int startHr=0;
		int accountNum = 0;
		double requested=0.0;
		public enum MyTellerState {justArrived, available, occupied};
		public MyTellerState state;
		
		public MyTeller (BankTeller p) {
			emp = p;
			state= MyTellerState.justArrived;
		}
		
	}

	public static class MyLoanOfficer {
		BankLoanOfficer emp;
		boolean needsAccount=false;
		int startHr=0;
		int accountNum = 0;
		double requested=0.0;
		public enum MyOfficerState {justArrived, available, occupied};
		public MyOfficerState state;
		enum EmployeeType {LoanOfficer, Teller};
		EmployeeType type;
		
		public MyLoanOfficer (BankLoanOfficer p) {
			emp = p;
			state= MyOfficerState.justArrived;
		}
		
	}
	
	public static class MyCustomer {
		BankCustomer customer;
		public enum MyCustomerState {transaction, loan};
		MyCustomerState state;
		
		public MyCustomer(BankCustomer b, MyCustomerState st) {
			state=st;
			customer = b;
		}
		
	}
	
	class MyClient {
		public MyClient(Landlord l, Integer account, double rentBill) {
			client = l; 
			AN = account; 
			bill = rentBill; 
		}

		Landlord client;
		Integer AN; 
		double bill; 	
		
	}
	
	public class MyAccount{
	
		public double balance;
		double loan;
		
		public MyAccount (double bal, double lo){
			balance=bal;
			loan=lo;
		}
		
		public double getBalance() {
			return balance;
		}
		
		public double getLoan() {
			return loan;
		}
		
		public void setBalance(double val) {
			balance = val;
		}
		
		public void setLoan(double val) {
			loan = val;
		}
	}
	
}
