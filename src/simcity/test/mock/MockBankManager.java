package simcity.test.mock;

import agent.Role;
import simcity.housing.LandlordRole;
import simcity.interfaces.*;

public class MockBankManager extends Mock implements BankManager{

        public EventLog log;
        public MockBankManager(String name) {
                super(name);
                // TODO Auto-generated constructor stub
                log = new EventLog();
        }
        
        public void msgTimeUpdate(int hr){
                
        }
        
        public void msgIAmHere(Role person, String type){
                
        }
        
        public void msgAvailable(BankTeller t){
                
        }
        
        public void msgAvailable(BankLoanOfficer t){
                log.add(new LoggedEvent("Officer now available"));
        }
        
        public void msgCreateAccount(String type){
                LoggedEvent m = new LoggedEvent ("Received a message to create a new account"); 
                log.add(m);        
        }
        
        public void msgProcessTransaction(int AN, double amount){
                LoggedEvent m = new LoggedEvent ("Received a message to withdraw "+amount); 
                log.add(m);        
        }
        
        public void msgNewLoan(int AN, double amount){
                log.add(new LoggedEvent("Received loan request"));
        }
        
        public void msgGaveALoan(double cash){
                
        }

        @Override
        public void msgHereIsYourRentBill(Landlord l, Integer account, double rentBill) {
            LoggedEvent m = new LoggedEvent ("Received from landlord for account " + account); 
            log.add(m);                  
        }

}