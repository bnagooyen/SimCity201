package simcity.Bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.Semaphore;

import simcity.PersonAgent;
import simcity.Bank.BankManagerRole.MyCustomer.MyCustomerState;
import simcity.Bank.BankManagerRole.MyLoanOfficer.MyOfficerState;
import simcity.Bank.BankManagerRole.MyTeller.MyTellerState;
import simcity.Bank.gui.BankManagerGui;
import simcity.Bank.gui.BankTellerGui;
import simcity.gui.SimCityGui;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
//import simcity.housing.LandlordRole;
import simcity.interfaces.BankCustomer;
import simcity.interfaces.BankLoanOfficer;
import simcity.interfaces.BankManager;
import simcity.interfaces.BankRobber;
import simcity.interfaces.BankTeller;
import simcity.interfaces.Landlord;
//import simcity.interfaces.Landlord;
import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;
import agent.Role;

public class BankManagerRole extends Role implements BankManager {
	
	
	/*
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 * ALL LANDLORD STUFF COMMENTED OUT
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 * ALL LANDLORD STUFF COMMENTED OUT
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 * COMMENTED OUT HEREISRENT IN INTERFACE TOO
	 */
	
	
	
	
	
	
	//data
	public EventLog log = new EventLog();
	private List<MyTeller> tellers = Collections.synchronizedList(new ArrayList<MyTeller>());
	private List<MyLoanOfficer> officers = Collections.synchronizedList(new ArrayList<MyLoanOfficer>());
	private List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	private List<MyCustomer> newJobs = Collections.synchronizedList(new ArrayList<MyCustomer>());
	public List<MyClient> clients = Collections.synchronizedList(new ArrayList<MyClient>()); 
	public Map<Integer, MyAccount> accounts = new HashMap<Integer, MyAccount>();
	int hour;
	int checkAccount;
	BankCustomerRole accountCheckCust;
	private boolean bankIsOpen;
	public enum BankState {open, closed, arriving, newDay};
	BankState bankState;
	private double employeePayPerHour=10;
	private double vault = 1000000000;
	
	//Robber
	BankRobber robber=null;
	
	//gui
	private Semaphore atDest = new Semaphore(0,true);
	public enum cornerState{ coming, leaving };
	public cornerState corner=cornerState.coming;
	private BankManagerGui bankmanagerGui;
	private SimCityGui gui; 
	private Timer timer=new Timer();
	private int instance; //which bank to paint in
	
	public BankManagerRole(SimCityGui G, int num) {
		super();
		gui=G;
		instance=num;
		// TODO Auto-generated constructor stub
		//startHour=7;
		bankState=BankState.arriving;
		hour=0;
		officers.clear();
		tellers.clear();
		customers.clear();
		checkAccount=0;
	}

	
	//MESSAGES
	
	public void msgTimeUpdate(int hr) {
		hour=hr;
		Do("Time is "+hr);
		if(hr==1) bankState=BankState.arriving;
		stateChanged();
	}
	
	public void msgCheckBalance(BankCustomerRole c, int AN){
		checkAccount=AN;
		accountCheckCust=c;
		stateChanged();
	}
	
	public void msgIAmHere(Role person, String type) {
		//if(person.instanceof(BankTeller))
		if(type.equals("BankTeller")) {
			AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "BankTeller is here");
			Do("BankTeller is here");
			tellers.add(new MyTeller((BankTeller)person));
			stateChanged();
		}
		else if (type.equals("BankLoanOfficer")) {
			AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "BankLoanOfficer is here");
			Do("BankLoanOfficer is here");
			officers.add(new MyLoanOfficer((BankLoanOfficer)person));
			stateChanged();
		}
		else if(type.equals("loan")) {
			AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "Customer with a loan is here");
			Do("Customer with a loan is here");
			customers.add(new MyCustomer((BankCustomer)person, MyCustomer.MyCustomerState.loan));
			stateChanged();
		}
		else if(type.equals("transaction")) {
			AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "Customer with a transaction is here");
			Do("Customer with a transaction is here");
			customers.add(new MyCustomer((BankCustomer)person, MyCustomer.MyCustomerState.transaction));
			stateChanged();
		}
		else if(type.equals("robbery")) {
			AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "THERE IS A BANKROBBER!");
			Do("ROBBERY!!!!!!");
			robber=(BankRobber) person;
			stateChanged();
		}
		//Fire Loan Officer and Hire new Applicant
		else if(type.equals("job")) {
			AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "Hiring a new loan officer");
			Do("New Loan officer is here. Old Loan Officer is fired");
			if(person.myPerson.jobString.contains("Loan")){
				newJobs.add(new MyCustomer((BankCustomer)person, MyCustomer.MyCustomerState.loanjob));
			}
			stateChanged();
		}
		else{
			Do("*****************************INVALID STRING PASSED*****************************");
		}
	}
	
	public void msgAvailable(BankTeller t) {
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "BankTeller available");
		Do("BankTeller available");
		tellers.get(0).state=MyTeller.MyTellerState.available;
		stateChanged();	
	}

	public void msgAvailable(BankLoanOfficer t) {
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "BankLoanOfficer available");
		Do("BankLoanOfficer available");
		officers.get(0).state=MyLoanOfficer.MyOfficerState.available;
		stateChanged();		
	}
	
	public void iAmDead() {
		tellers.remove(0);
		Do("Teller was killed, no more teller");
	}
	
	public void msgCreateAccount(String type) {
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "Going to create an account");
		Do("Going to create account");
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
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "Processing transaction");
		Do("Processing transaction");
		tellers.get(0).requested=amount;
		tellers.get(0).accountNum=AN;
		stateChanged();
	}
	
	public void msgNewLoan(int AN, double amount) {
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "New loan request");
		Do("New loan request");
		officers.get(0).requested=amount;
		officers.get(0).accountNum=AN;
//		System.err.println(officers.get(0).accountNum + " "+ officers.get(0).requested);
		stateChanged();
	}
	
	public void msgGaveALoan(double cash) {
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "Gave loan");
		Do("Gave loan");
		vault -= cash;
		stateChanged();
	}
	
	@Override
	public void msgAnimationFinishedGoToCorner() {
		if(corner==cornerState.coming){
//			bankManagerGui.goToTellerPos();
		}
		else if(corner==cornerState.leaving){
//			bankManagerGui.DoExitBank();
		}
		atDest.release();
	}


	@Override
	public void msgAnimationFinishedLeaveBank() {
		//this.isActive=false;
		atDest.release();
	}


	@Override
	public void msgAtTellerPos() {
		atDest.release();
		
	}


	@Override
	public void msgAtManagerPos() {
		// TODO Auto-generated method stub
		atDest.release();
	}


	@Override
	public void msgAtLoanPos() {
		// TODO Auto-generated method stub
		atDest.release();
	}
	
	public void msgDoneAnimation(){
		atDest.release();
	}
	
	public void msgHereIsYourRentBill(Landlord l, Integer account, double rentBill) {				
		Do("Receiving rent bill");
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
		
		if(!newJobs.isEmpty()){
			if(newJobs.get(0).state==MyCustomerState.loanjob){
				hireNewLoanOfficer();
			}
		}
		
		if(accountCheckCust!=null){
			accountCheckCust.msgBalance(accounts.get(checkAccount).balance);
			accountCheckCust=null;
			checkAccount=0;
			return true;
		}
		
		if(bankState==BankState.arriving) {
			goToDesk();
			return true;
		}
		
		if(hour>=myPerson.directory.get(myPerson.jobLocation).openHour  && bankState==BankState.newDay && officers.size()>=1 && tellers.size()>=1) {
			OpenBank();
			return true;
		}
		
		
		if(hour>=myPerson.directory.get(myPerson.jobLocation).closeHour  && bankState==BankState.open) {
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
		
		if(!officers.isEmpty() && officers.get(0).needsAccount) {
			NewAccountForOfficer();
			return true;
		}
		
		if(!officers.isEmpty() && officers.get(0).requested!=0) {
			CompleteLoan();
			return true;
		}
		
		/*if(!clients.isEmpty()) {
			TransferMoneyToLandlord(); 
			return true; 
		}*/

		
		if(customers.size()>0 && (tellers.isEmpty() || officers.isEmpty())) {
			BankIsClosed();
			return true;
		}
		
		if(robber!=null&&tellers.get(0).state==MyTellerState.available){
				SendRobberToTeller(robber);
		}
		
		if(!tellers.isEmpty() && tellers.get(0).state==MyTellerState.available) {
			synchronized(customers) {
				for(MyCustomer cust: customers) {
					if (cust.state==MyCustomerState.transaction) {
						SendCustomerToTeller(cust);
						return true;
					}
				}
			}
		}
		
		if( !officers.isEmpty() && officers.get(0).state==MyOfficerState.available) {
			synchronized(customers) {
				for(MyCustomer cust: customers) {
					if (cust.state==MyCustomerState.loan) {
						SendCustomerToLoanOfficer(cust);
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	//actions
	
	private void OpenBank() {
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "Opening bank");
		Do("Opening bank");
		bankState=BankState.open;
	}
	private void SwapTellers() {
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "Switching out tellers");
		Do("Switching out tellers");
		bankmanagerGui.goToTellerPos();
		finishTask();
		tellers.get(0).emp.msgGoHome((hour-tellers.get(0).startHr)*employeePayPerHour);
		tellers.get(1).emp.msgGoToTellerPosition();
		tellers.get(1).startHr=hour;
		tellers.get(1).state=MyTellerState.available;
		tellers.remove(tellers.get(0));
		bankmanagerGui.goToManagerPos();
		finishTask();
	}
	
	private void SwapLoanOfficers() {
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "Switching out loan officers");
		Do("Switching out loan officers");
//		bankManagerGui.goToLoanPos();
//		finishTask();
		officers.get(0).emp.msgGoHome((hour-officers.get(0).startHr)*employeePayPerHour);
		officers.get(1).emp.msgGoToLoanOfficerPosition();
		officers.get(1).startHr=hour;
		officers.get(1).state=MyOfficerState.available;
		officers.remove(officers.get(0));
//		bankManagerGui.goToManagerPos();
//		finishTask();
	}
	
	private void hireNewLoanOfficer(){
		officers.get(0).emp.msgGoHome(50.0);
		PersonAgent newOfficer=newJobs.get(0).customer.getPerson();
		PersonAgent oldOfficer=officers.get(0).emp.getPerson();
		newOfficer.SetJob((Role) officers.get(0).emp, newOfficer.jobLocation, "Bank Loan Officer");
		oldOfficer.myJob=null;
		officers.get(0).myPerson=newOfficer;
	}
	
	private void AddTeller() {
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "Adding teller");
		Do("Adding teller");
		tellers.get(0).emp.msgGoToTellerPosition();
		tellers.get(0).state=MyTellerState.available;
		tellers.get(0).startHr=hour;
	}
	
	private void AddLoanOfficer() {
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "Adding loan officer");
		Do("Adding loan officer");
		officers.get(0).emp.msgGoToLoanOfficerPosition();
		officers.get(0).state=MyOfficerState.available;
		officers.get(0).startHr=hour;
	}
	
	private void SendCustomerToTeller(MyCustomer c) {
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "Telling customer to go to teller");
		Do("Telling customer to go to teller");
		tellers.get(0).state=MyTellerState.occupied;
		c.customer.msgGoToTeller(tellers.get(0).emp);
		customers.remove(c);
	}
	
	private void SendRobberToTeller(BankRobber R) {
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "Telling customer to go to teller");
		Do("Telling customer to go to teller");
		tellers.get(0).state=MyTellerState.occupied;
		R.msgGoToTeller(tellers.get(0).emp);
		robber=null;
	}
	
	private void SendCustomerToLoanOfficer(MyCustomer c) {
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "Telling customer to go to loan officer");
		Do("Telling customer to go to loan officer");
		officers.get(0).state=MyOfficerState.occupied;
		c.customer.msgGoToLoanOfficer(officers.get(0).emp);
		customers.remove(c);
	}
	
	/*private void TransferMoneyToLandlord() {
		Do("Transferring money");
		if (accounts.get(clients.get(0).AN).balance >= clients.get(0).bill) {
			accounts.get(clients.get(0).AN).balance -= clients.get(0).bill;
			clients.get(0).client.msgHereIsARentPayment(clients.get(0).AN, clients.get(0).bill);
		}
		else {
			clients.get(0).client.msgCannotPayForRent(clients.get(0).AN);
		}		
		clients.remove(0); 
	}*/
	
	private void BankIsClosed() {
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "Telling customer bank is closed");
		Do("Telling customer bank is closed");
//		System.out.println("bankisclosed");
		synchronized(customers) {
			for(MyCustomer c: customers) {
				c.customer.msgLeaveBank();
			}
		}
		customers.clear();
	}
	
	private void CloseBank() {
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "Closing bank");
		Do("Closing bank");
		bankState=BankState.closed;
		bankmanagerGui.goToLoanPos();
		finishTask();
		officers.get(0).emp.msgGoHome((hour-officers.get(0).startHr)*employeePayPerHour);
		officers.clear();
		bankmanagerGui.goToTellerPos();
		finishTask();		
		tellers.get(0).emp.msgGoHome((hour-tellers.get(0).startHr)*employeePayPerHour);
		tellers.clear();
		bankmanagerGui.goToManagerPos();
		finishTask();
		bankmanagerGui.goToCorner();
		finishTask();
		bankmanagerGui.DoExitBank();
		finishTask();
		BankIsClosed();
		this.isActive = false;
	}
	
	private void NewAccountForTeller() {
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "Making new account");
		Do("Making new account");
		accounts.put(accounts.size()+1, new MyAccount(0,0));
		tellers.get(0).needsAccount=false;
		tellers.get(0).emp.msgAccountCreated(accounts.size());
	}
	
	private void NewAccountForOfficer() {
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "Making new account");
		Do("Making new account");
		accounts.put(accounts.size()+1, new MyAccount(0,0));
		officers.get(0).needsAccount=false;
		officers.get(0).emp.msgAccountCreated(accounts.size());
	}
	
	private void CompleteTransaction() {
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "Doing and completing transaction");
		Do("Doing and completing transaction");
		if(accounts.get(tellers.get(0).accountNum)==null) { // customer doesn't exist
//			tellers.get(0).msgAccountInexistant();
			Do("NO CUSTOMER");
			return;
		}
		if(tellers.get(0).requested<-1*(accounts.get(tellers.get(0).accountNum)).balance) {
			tellers.get(0).requested=-1*(accounts.get(tellers.get(0).accountNum).balance);
		}
		MyAccount update = accounts.get(tellers.get(0).accountNum);
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager","Account=$"+update.balance);
		Do("Account=$"+update.balance);
		update.balance+=tellers.get(0).requested;
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager","Account=$"+update.balance);
		Do("Account=$"+update.balance);
		vault-=tellers.get(0).requested;
		accounts.put(tellers.get(0).accountNum, update);
		tellers.get(0).emp.msgTransactionProcessed(tellers.get(0).requested);
		tellers.get(0).requested=0.0;
		tellers.get(0).accountNum=0;
		
	}
	
	private void CompleteLoan() {
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankManager", "Doing and completing loan");
		Do("Doing and completing loan");
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
	
	public void setGui(BankManagerGui Bgui){
		bankmanagerGui = Bgui;
	}
	
	private void goToDesk(){
		if(bankmanagerGui == null) {
			bankmanagerGui = new BankManagerGui(this);
			//System.err.println(gui);
			//System.err.println(gui.myPanels);
			//System.err.println(gui.myPanels.get("Bank 1").panel);
			gui.myPanels.get("Bank "+instance).panel.addGui(bankmanagerGui);
		}
		bankmanagerGui.setPresent(true);
		bankmanagerGui.goToCorner();
		finishTask();
		bankmanagerGui.goToManagerPos();
		finishTask();
		bankState=BankState.newDay;
	}
	
	private void finishTask(){			//Semaphore to make waiter finish task before running scheduler
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		public PersonAgent myPerson;
		public BankLoanOfficer emp;
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
		public enum MyCustomerState {transaction, loan, loanjob, tellerJob};
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
