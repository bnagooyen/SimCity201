package simcity.Bank;

import java.util.*;
import java.util.concurrent.Semaphore;

import simcity.PersonAgent;
import simcity.Bank.gui.BankTellerGui;
//import simcity.Bank.BankManagerRole.MyCustomer;
//import simcity.Bank.BankManagerRole.MyEmployee;
import simcity.interfaces.*;
import simcity.test.mock.EventLog;
import agent.Role;

public class BankTellerRole extends Role implements BankTeller {
	
	//data
	public BankManager manager;
	public BankRobber robber;
	public MyCustomer customer=null;
	public Double requested=0.00;
	public Double transacted=0.00;
	
	//GUI
	private BankTellerGui banktellerGui;
	public enum cornerState{ coming, leaving };
	public cornerState corner=cornerState.coming;
	public boolean broke=false;
	
	public enum bankTellerState { arrived, working, success, error, finished };
	public bankTellerState state;
	private Semaphore atDest = new Semaphore(0,true);
	
	public enum accountState {none,requested,justMade,existing};
	
	public EventLog log;
	
	public class MyCustomer{
		public BankCustomer BC;
		public Integer accountNumber;
		public accountState state=accountState.existing;
		
		MyCustomer(BankCustomer BankCust){
			BC=BankCust;
			accountNumber=null;
		}
	}
		
	public BankTellerRole() {
		super();
		startHour=8;
		log=new EventLog();
		//print("BankTeller Created");
		state=bankTellerState.arrived;
		broke=false;
	}


	
	//Messages
	public void msgMakeAccount(BankCustomer BC){
		Do("Requesting account");
		customer=new MyCustomer(BC);
		customer.state=accountState.none;
		broke=false;
		stateChanged();
	}
	public void msgAccountCreated(int num){
		Do("Account created");
		customer.state=accountState.justMade;
		customer.accountNumber=num;
		stateChanged();
	}
	public void msgDeposit(BankCustomer BC, int actNum, double amount){
		Do("Receiving deposit");
		if(customer==null) customer= new MyCustomer(BC);
		customer.accountNumber=actNum;
		requested=amount;
		broke=false;
		stateChanged();
	}
	public void msgWithdrawal(BankCustomer BC, int actNum, double amount){
		Do("Receiving withdrawal $"+amount);
		if(customer==null) customer= new MyCustomer(BC);
		customer.accountNumber=actNum;
		requested=-amount;
		broke=false;
		stateChanged();
	}
	public void msgTransactionProcessed(double finalAmount){
		Do("Transaction processed");
		transacted=finalAmount;
		if(transacted==0.0) broke=true;
		stateChanged();
	}
	public void msgIAmRobbingYou(BankRobber BR){
		Do("Ahhh there is a robber");
		robber=BR;
		stateChanged();
	}
	public void msgIShotYou(){
		Do("Ahhh I got shot yo");
		//Tell person he was shot, Message? Directly add to state?
	}
	public void msgGoHome(double pay){
		Do("Going home");
		//myPerson.money+=pay;
		state=bankTellerState.finished;
		stateChanged();
	}
	
	public void msgGoToTellerPosition(){
		//state=bankTellerState.arrived;
		//stateChanged();
	}
	


	@Override
	public void msgAnimationFinishedGoToCorner() {
		if(corner==cornerState.coming){
			banktellerGui.goToTellerPos();
		}
		else if(corner==cornerState.leaving){
			banktellerGui.DoExitBank();
		}
		atDest.release();
	}



	@Override
	public void msgAnimationFinishedLeaveBank() {
		//this.isActive=false;
		atDest.release();
		state=bankTellerState.arrived;
	}



	@Override
	public void msgAtTellerPos() {
		atDest.release();
	}
	
	
	//SCHEDULER	
	
		@Override
		public boolean pickAndExecuteAnAction() {
			// TODO Auto-generated method stub	
		if(state==bankTellerState.arrived){
			arriveAtBank();
		}
		if(customer!=null && customer.state==accountState.none){
			createNewAccount();
			return true;
		}
		if(customer!=null && customer.state==accountState.justMade){
			hereIsYourAccount();
			return true;
		}
		if(requested!=0.00 && transacted==0.00){
			executeTransaction();
			return true;
		}
		if(transacted!=0.00||broke){
			closeTransaction();
			return true;
		}
		if(robber!=null){
			dealWithRobbery();
			return true;
		}
		if(state==bankTellerState.finished) {
			leaveBank();
			return true;
		}
			return false;
		}

		
		//ACTIONS
		private void createNewAccount(){
			Do("Asking manager for a new account");
			manager.msgCreateAccount("BankTeller");
			customer.state=accountState.requested;
		}
		
		private void hereIsYourAccount(){
			Do("Giving customer the account"+customer.accountNumber);
			customer.BC.msgAccountMade(customer.accountNumber);
			customer.state=accountState.existing;
		}
		private void executeTransaction(){
			Do("About to process transaction");
			manager.msgProcessTransaction(customer.accountNumber, requested);
			requested=0.00;
		}
		private void closeTransaction(){
			Do("Closing completed transaction");
			customer.BC.msgTransactionComplete(transacted);
			customer=null;
			transacted=requested=0.00;
			manager.msgAvailable(this);
			broke=false;
		}
		private void dealWithRobbery(){
			Random generator=new Random();
			int choice = generator.nextInt(2);
			if(choice==0){
				Do("Call the Po-Po. We was robbed");
				robber.msgHereIsMoney(1000.00);
			}
			else if(choice==1){
				Do("Oh heck no! No money fo' you");
				robber.msgIRefuseToPay();
			}
			else if(choice==2){
				Do("I killed the robber");
				robber.msgIShotYou();
				manager.msgAvailable(this);
			}
		}
		
		private void leaveBank() {
			banktellerGui.goToCorner();
			corner=cornerState.leaving;
			finishTask();
			this.isActive=false;
		}
		private void arriveAtBank() {
			Do("Telling Manager I am Here");
			state=bankTellerState.working;
			manager.msgIAmHere(this, "BankTeller");
			banktellerGui.goToCorner();
			corner=cornerState.coming;
			finishTask();
		}

		public void setGui(BankTellerGui BG){
			banktellerGui=BG;
		}
		
		private void finishTask(){			//Semaphore to make waiter finish task before running scheduler
			try {
				atDest.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void setManager(BankManager Bman){
			manager=Bman;
		}
		
			
	
		
}
