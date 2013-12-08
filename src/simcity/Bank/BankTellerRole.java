package simcity.Bank;

import java.util.*;
import java.util.concurrent.Semaphore;

import simcity.PersonAgent;
import simcity.Bank.gui.BankTellerGui;
import simcity.Market.gui.IBGui;
import simcity.gui.SimCityGui;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
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
	
	private SimCityGui gui;
	
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
		
	public BankTellerRole(SimCityGui G) {
		super();
		startHour=8;
		log=new EventLog();
		//print("BankTeller Created");
		state=bankTellerState.arrived;
		gui=G;
		broke=false;
	}


	
	//Messages
	public void msgMakeAccount(BankCustomer BC){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankTeller", "Requesting account");
		Do("Requesting account");
		customer=new MyCustomer(BC);
		customer.state=accountState.none;
		broke=false;
		stateChanged();
	}
	public void msgAccountCreated(int num){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankTeller", "Account created");
		Do("Account created");
		customer.state=accountState.justMade;
		customer.accountNumber=num;
		stateChanged();
	}
	public void msgDeposit(BankCustomer BC, int actNum, double amount){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankTeller", "Receiving deposit");
		Do("Receiving deposit");
		if(customer==null) customer= new MyCustomer(BC);
		customer.accountNumber=actNum;
		requested=amount;
		broke=false;
		stateChanged();
	}
	public void msgWithdrawal(BankCustomer BC, int actNum, double amount){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankTeller", "Receiving withdrawal $"+amount);
		Do("Receiving withdrawal $"+amount);
		if(customer==null) customer= new MyCustomer(BC);
		customer.accountNumber=actNum;
		requested=-amount;
		broke=false;
		stateChanged();
	}
	public void msgTransactionProcessed(double finalAmount){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankTeller", "Transaction processed");
		Do("Transaction processed");
		transacted=finalAmount;
		if(transacted==0.0) broke=true;
		stateChanged();
	}
	public void msgIAmRobbingYou(BankRobber BR){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankTeller", "Ahhh there is a robber");
		Do("Ahhh there is a robber");
		robber=BR;
		stateChanged();
	}
	public void msgIShotYou(){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankTeller", "Ahhh I got shot yo");
		Do("Ahhh I got shot yo");
		//Tell person he was shot, Message? Directly add to state?
	}
	public void msgGoHome(double pay){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankTeller", "going home");
		Do("Going home");
		myPerson.money+=pay;
		state=bankTellerState.finished;
		stateChanged();
	}
	
	public void msgGoToTellerPosition(){
		//state=bankTellerState.arrived;
		//stateChanged();
	}
	


	@Override
	public void msgAnimationFinishedGoToCorner() {
		Do("ATCORNER");
		if(corner==cornerState.coming){
			banktellerGui.goToTellerPos();
		}
		else if(corner==cornerState.leaving){
			Do("DOEXIT");
			banktellerGui.DoExitBank();
		}
		atDest.release();
	}



	@Override
	public void msgAnimationFinishedLeaveBank() {
		this.isActive=false;
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
			AlertLog.getInstance().logMessage(AlertTag.Bank, "BankTeller", "Asking manager for a new account");
			Do("Asking manager for a new account");
			manager.msgCreateAccount("BankTeller");
			customer.state=accountState.requested;
		}
		
		private void hereIsYourAccount(){
			AlertLog.getInstance().logMessage(AlertTag.Bank, "BankTeller", "Giving customer the account " +customer.accountNumber);
			Do("Giving customer the account"+customer.accountNumber);
			customer.BC.msgAccountMade(customer.accountNumber);
			customer.state=accountState.existing;
		}
		private void executeTransaction(){
			AlertLog.getInstance().logMessage(AlertTag.Bank, "BankTeller", "About to process transaction");
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
				AlertLog.getInstance().logMessage(AlertTag.Bank, "BankTeller", "Call the Po-Po. We was robbed");
				Do("Call the Po-Po. We was robbed");
				robber.msgHereIsMoney(1000.00);
			}
			else if(choice==1){
				AlertLog.getInstance().logMessage(AlertTag.Bank, "BankTeller", "Oh heck no! No money fo' you");
				Do("Oh heck no! No money fo' you");
				robber.msgIRefuseToPay();
			}
			else if(choice==2){
				AlertLog.getInstance().logMessage(AlertTag.Bank, "BankTeller", "I killed the robber");
				Do("I killed the robber");
				robber.msgIShotYou();
				manager.msgAvailable(this);
			}
		}
		
		private void leaveBank() {
			banktellerGui.goToCorner();
			corner=cornerState.leaving;
			finishTask();
			//this.isActive=false;
		}
		private void arriveAtBank() {
			AlertLog.getInstance().logMessage(AlertTag.Bank, "BankTeller", "Telling Manager I am here");
			Do("Telling Manager I am Here");
			if(banktellerGui == null) {
				banktellerGui = new BankTellerGui(this, manager);
				gui.myPanels.get("Bank 1").panel.addGui(banktellerGui);
			}
			banktellerGui.setPresent(true);
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
