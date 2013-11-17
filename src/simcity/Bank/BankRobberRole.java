package simcity.Bank;

import java.util.*;

import simcity.PersonAgent;
import simcity.interfaces.*;
import agent.Role;

public class BankRobberRole extends Role implements BankRobber {
	//data
	BankManager manager; 
	BankTeller teller; 
	
	enum bankRobberState { arrived, waiting, inProgress, unsucessful, done};
	bankRobberState state=bankRobberState.arrived;
	
	protected BankRobberRole(PersonAgent p) {
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
		//tell Person He was shot				*******************************
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		if(state==bankRobberState.arrived){
			tellManagerArrived();
		}
		if(teller!=null && state==bankRobberState.waiting){
			robBank();
		}
		if(state==bankRobberState.unsucessful){
			shootTeller();
		}
		if(state==bankRobberState.done){
			leaveBank();
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
