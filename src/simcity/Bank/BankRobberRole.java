package simcity.Bank;

import java.util.*;
import java.util.concurrent.Semaphore;

import simcity.PersonAgent;
import simcity.Bank.BankCustomerRole.bankCustomerState;
import simcity.Bank.gui.BankCustomerGui;
import simcity.Bank.gui.BankRobberGui;
import simcity.PersonAgent.PersonState;
import simcity.gui.SimCityGui;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.*;
import agent.Role;

public class BankRobberRole extends Role implements BankRobber {
	//data
	public BankManager manager; 
	public BankTeller teller; 
	public int num;
	
	public enum bankRobberState { arrived, waiting, inProgress, unsucessful, done};
	public bankRobberState state=bankRobberState.arrived;
	
	//GUI
	private Semaphore atDest = new Semaphore(0,true);
	private BankRobberGui bankrobbergui;;
	private SimCityGui gui;
	
	public BankRobberRole(SimCityGui G, int N) {
		super();
		state=bankRobberState.arrived;
		gui=G;
		num=N;
	}
	
	//messages
	public void msgGoToTeller(BankTeller t){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankRobber", "I feel like robbing a bank today");
		Do("I feel like robbing a bank today");
		teller=t;
	}
	
	public void msgHereIsMoney(double amount){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankRobber", "got the money");
		Do("Got the money");
		myPerson.money+=amount;
		state=bankRobberState.done;
	}
	
	public void msgIRefuseToPay(){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankRobber", "Failed to get money");
		Do("Failed to get money");
		state=bankRobberState.unsucessful;
	}
	
	public void msgIShotYou(){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankRobber", "Shots fired");
		Do("Shots fired");
		bankrobbergui.shot=true;
		state=bankRobberState.done;
		myPerson.state=PersonState.dead;
		//tell Person He was shot				*******************************
	}
	

	@Override
	public boolean pickAndExecuteAnAction() {
		if(state==bankRobberState.arrived){
			tellManagerArrived();
			return true;
		}
		if(teller!=null && state==bankRobberState.waiting){
			robBank();
			return true;
		}
		if(state==bankRobberState.unsucessful){
			shootTeller();
			return true;
		}
		if(state==bankRobberState.done){
			leaveBank();
			return true;
		}
		return false;
	}
	
	//actions
	private void tellManagerArrived(){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankRobber", "Robber has arrived");
		Do("Robber has arrived");
		if(bankrobbergui == null) {
			bankrobbergui = new BankRobberGui(this, manager);
			gui.myPanels.get(myPerson.BankChoice).panel.addGui(bankrobbergui);
		}
		bankrobbergui.setPresent(true);
		manager.msgIAmHere(this, "robbery");
		bankrobbergui.goToTeller();
		finishTask();
		state=bankRobberState.waiting;
	}
	
	private void robBank(){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankRobber", "About to rob bank #YOLO");
		Do("About to rob bank #YOLO");
		//doRobBank     GUI
		teller.msgIAmRobbingYou(this);
		state=bankRobberState.inProgress;
	}
	
	private void shootTeller(){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankRobber", "I'm angry. Shots fired");
		Do("I'm angry. Shots fired");
		teller.msgIShotYou();
		state=bankRobberState.done;
	}
	
	private void leaveBank(){
		AlertLog.getInstance().logMessage(AlertTag.Bank, "BankRobber", "I'm out!");
		Do("I'm out!");
		this.isActive=false;
		state=bankRobberState.arrived;
		bankrobbergui.DoExitBank();
		finishTask();
	}

	@Override
	public void setManager(BankManager m) {
		// TODO Auto-generated method stub
		manager=m;
	}

	public void msgAtTellerPos() {
		Do("Robs at teller!");
		atDest.release();
		
	}

	public void msgAnimationFinishedLeaveBank() {
		atDest.release();
		state=bankRobberState.arrived;
		this.isActive=false;
		myPerson.msgLeftBuilding();
	}
	
	private void finishTask(){			//Semaphore to make waiter finish task before running scheduler
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
