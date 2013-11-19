package simcity.Bank;

import java.util.*;

import simcity.PersonAgent;
import simcity.interfaces.*;
import agent.Role;

public class BankRobberRole extends Role implements BankRobber {
	//data
	public BankManager manager; 
	public BankTeller teller; 
	
	public enum bankRobberState { arrived, waiting, inProgress, unsucessful, done};
	public bankRobberState state=bankRobberState.arrived;
	
	public BankRobberRole(PersonAgent p) {
		super(p);
		// TODO Auto-generated constructor stub
	}
	
	//messages
	public void msgGoToTeller(BankTeller t){
		teller=t;
	}
	
	public void msgHereIsMoney(double amount){
		myPerson.money+=amount;
		state=bankRobberState.done;
	}
	
	public void msgIRefuseToPay(){
		state=bankRobberState.unsucessful;
	}
	
	public void msgIShotYou(){
		state=bankRobberState.done;
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
		manager.msgIAmHere(this, "transaction");
		state=bankRobberState.waiting;
	}
	
	private void robBank(){
		//doRobBank     GUI
		teller.msgIAmRobbingYou(this);
		state=bankRobberState.inProgress;
	}
	
	private void shootTeller(){
		teller.msgIShotYou();
		state=bankRobberState.done;
	}
	
	private void leaveBank(){
		this.isActive=false;
		state=bankRobberState.arrived;
		//doLeaveBank       GUI
	}
}
