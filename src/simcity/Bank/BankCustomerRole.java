package simcity.Bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import simcity.PersonAgent;
import simcity.Bank.gui.BankCustomerGui;
import simcity.Bank.gui.BankManagerGui;
import simcity.PersonAgent.MoneyState;
import simcity.gui.SimCityGui;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.BankCustomer;
import simcity.interfaces.BankLoanOfficer;
import simcity.interfaces.BankManager;
import simcity.interfaces.BankTeller;
import agent.Role;

public class BankCustomerRole extends Role implements BankCustomer {
	//data
	public BankManager manager; 
	public BankTeller teller; 
	public BankLoanOfficer loanOfficer; 
	public enum bankCustomerState { arrived, waiting, inProgress, exiting, done};
	public bankCustomerState state=bankCustomerState.arrived;
	//public String purpose;
	public Integer accountNum=null;
	
	//Gui
	private Semaphore atDest = new Semaphore(0,true);
	private BankCustomerGui bankcustomerGui;
	private SimCityGui gui; 
	
	
	//messages
	public void msgGoToLoanOfficer(BankLoanOfficer BL){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankCustomer", "Told to go to loan officer");

		Do("Told to go to loan officer");
		loanOfficer=BL;
		stateChanged();
	}
	
	public void msgGoToTeller(BankTeller BT){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankCustomer", "Told to go to teller");
		Do("Told to go to teller");
		teller=BT;
		stateChanged();
	}
	
	public void msgTransactionComplete(double amount){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankCustomer", "Finished transaction");
		Do("Finished transaction");
		myPerson.money-=amount;
		state=bankCustomerState.done;
		stateChanged();
	}
	
	public void msgHeresLoan(double amount){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankCustomer", "Received loan");
		Do("Received loan");
		myPerson.money+=amount;
		stateChanged();
	}
	
	public void msgLoanDenied(){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankCustomer", "Denied loan");
		Do("Denied loan");
		state=bankCustomerState.done;
		stateChanged();
	}
	
	public void msgBalance(Double Balance){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankCustomer", "Got my balance");
		Do("Got my balance");
		if(Balance<1.0) myPerson.moneystate=MoneyState.poor;
		else if(Balance>1000) myPerson.moneystate=MoneyState.rich;
		else myPerson.moneystate=MoneyState.middle;
	}
	
	public void msgAccountMade(int AN){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankCustomer", "Got an account");
		Do("Got an account");
		accountNum=AN;
		state=bankCustomerState.waiting;
		stateChanged();
	}
	
	@Override
	public void msgLeaveBank() {
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankCustomer", "Going to leave");
		Do("Going to leave");
		state=bankCustomerState.done;
		stateChanged();
	}
	
	public BankCustomerRole(SimCityGui G) {
		super();
		gui=G;
		state=bankCustomerState.arrived;		
		//purpose="transaction";						//NEED A GOOD WAY FOR PERSON TO DECIDE
	}
	
	public void msgAtTellerPos() {
		atDest.release();
		
	}

	public void msgAnimationFinishedLeaveBank() {
		atDest.release();
		state=bankCustomerState.arrived;
		this.isActive=false;
		myPerson.msgLeftBuilding();
	}

	public void msgAtLoanPos() {
		atDest.release();
	}
	


	@Override
	public boolean pickAndExecuteAnAction() {
		if(state==bankCustomerState.arrived){
			tellManagerArrived();
			return true;
		}
		if((loanOfficer!=null || teller!=null) && accountNum==null && state==bankCustomerState.waiting){
			makeAccount();
			return true;
		}
		if(loanOfficer!=null && accountNum!=null && state==bankCustomerState.waiting){
			requestLoan();
			return true;
		}
		if(teller!=null && state==bankCustomerState.waiting){
			makeTransaction();
			return true;
		}
		if(state==bankCustomerState.done){
			leaveBank();
			return true;
		}
		return false;
	}
	
	//actions
	private void tellManagerArrived(){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankCustomer", "Arriving at bank with $"+ myPerson.money);
		Do("Arriving at bank with $"+ myPerson.money);
		if(bankcustomerGui == null) {
			bankcustomerGui = new BankCustomerGui(this, manager);
			gui.myPanels.get("Bank 1").panel.addGui(bankcustomerGui);
		}
		bankcustomerGui.setPresent(true);
		
		if(purpose.equals("withdraw")||purpose.equals("deposit")||purpose.equals("rob")){
			manager.msgIAmHere(this,"transaction");
			bankcustomerGui.goToTeller();
			finishTask();
		}
		else if(purpose.equals("loan")){
			manager.msgIAmHere(this,"loan");
			bankcustomerGui.goToLoanPos();
			finishTask();
		}
		state=bankCustomerState.waiting;
	}
	
	private void makeAccount(){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankCustomer", "Making account");
		Do("Making account");
		if(loanOfficer!=null){
			loanOfficer.msgMakeAccount(this);
		}
		else if(teller!=null){
			teller.msgMakeAccount(this);
		}
		state=bankCustomerState.inProgress;
	}
	
	private void requestLoan(){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankCustomer", "Requesting for loan");
		Do("Requesting for loan");
		String job=jobString();
		loanOfficer.msgINeedALoan(this, accountNum, 1000, job);		//Need a way to decide how much $$ to request  
		state=bankCustomerState.inProgress;
	}
	
	private String jobString(){
		if(myPerson.jobLocation.contains("restaurant")) return "waiter";
		else if(myPerson.jobLocation.contains("Bank")) return "bankteller";
		else return "";
	}
	
	private void makeTransaction(){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankCustomer", "Making a transaction");
		Do("Making a transaction");
		
		//HACK TO TEST
		if(myPerson.money>myPerson.depositThreshold){
			teller.msgDeposit(this, accountNum, myPerson.money-myPerson.depositThreshold);
		}
		if(myPerson.money<myPerson.withdrawalThreshold){
			teller.msgWithdrawal(this, accountNum, myPerson.withdrawalThreshold-myPerson.money);
		}
		
		state=bankCustomerState.inProgress;
	}
	
	private void leaveBank(){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankCustomer", "Leaving bank with $"+myPerson.money);
		Do("Leaving bank with $"+myPerson.money);
		manager.msgCheckBalance(this, accountNum);
		bankcustomerGui.DoExitBank();
		state=bankCustomerState.exiting;
	}
	

	public void setManager(BankManager m){
		manager=m;
	}

	//GUI
	public void setGui(BankCustomerGui BC){
		bankcustomerGui=BC;
	}
	
	private void finishTask(){			//Semaphore to make waiter finish task before running scheduler
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setMarketManager(BankManagerRole bankManager) {
		manager = bankManager;
	}
}
