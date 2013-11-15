package simcity.Bank;

import java.util.List;


import agent.Role;

public class BankManagerRole extends Role implements BankManager {
	private List<MyTeller> tellers;
	
	static class MyTeller {
		BankTeller teller;
		//boolean needsAccount=false;
		int accountNum = 0;
		enum MyTellerState {justArrived, available, occupied};
		MyTellerState state;
		
		MyTeller (BankTeller tel) {
			teller = tel;
			state= MyTellerState.justArrived;
		}
		
	}
}
