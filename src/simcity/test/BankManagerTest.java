package simcity.test;


import simcity.PersonAgent;
import simcity.Bank.BankManagerRole;
import simcity.Bank.BankManagerRole.MyAccount;
import simcity.Bank.BankManagerRole.MyCustomer;
import simcity.Bank.BankManagerRole.MyLoanOfficer;
import simcity.Bank.BankManagerRole.MyTeller;
import simcity.housing.LandlordRole;
import simcity.mockrole.MockRole;
import simcity.test.mock.MockBankCustomer;
import simcity.test.mock.MockBankManager;
import simcity.test.mock.MockBankLoanOfficer;
import simcity.test.mock.MockBankTeller;
import simcity.test.mock.MockLandlord;
import junit.framework.TestCase;

public class BankManagerTest extends TestCase{

	PersonAgent p;
	BankManagerRole manager;
	MockBankTeller teller;
	MockBankTeller teller2;
	MockBankLoanOfficer loanOfficer;
	MockBankLoanOfficer loanOfficer2;
	MockBankManager mgr;
	MockBankCustomer customer1t;
	MockBankCustomer customer2t;
	MockBankCustomer customer1l;
	MockBankCustomer customer2l;
	MockLandlord landlord; 
	MockRole mockrole;
	
	@Override
	public void setUp() throws Exception{
		super.setUp();
		p = new PersonAgent("BankManager", mockrole);
		mockrole=new MockRole("mockrole", p);
		manager = new BankManagerRole(p);
		p.addRole(manager);
		teller = new MockBankTeller("mockTeller");
		
		loanOfficer = new MockBankLoanOfficer("mockOfficer");
		manager.isActive=true;
		
		teller2 = new MockBankTeller("mockTeller2");
		loanOfficer2 = new MockBankLoanOfficer("mockOfficer2");
		
		customer1t = new MockBankCustomer("transaction");
		customer2t = new MockBankCustomer("transaction");
		customer1l = new MockBankCustomer("transaction");
		customer2l = new MockBankCustomer("loan");
		
		landlord = new MockLandlord("Landlord"); 
		
	}
	
	public void testCreateAccount() {
		
		manager.getOfficer().add(new MyLoanOfficer(loanOfficer));
		manager.getTeller().add(new MyTeller(teller));
		
		System.out.println("this test ensures that an account is created");
		 // preconditions
		assertEquals("Teller should have 0 logs, ", teller.log.size(), 0);
	
		//reset state to newDay
		manager.msgTimeUpdate(1);
		assertEquals("bank should be open", manager.getBankStatus(), BankManagerRole.BankState.newDay);
		
		//bank should open
		manager.msgTimeUpdate(8);
		
		//loan and teller added
		assertTrue(manager.pickAndExecuteAnAction());
		assertEquals("loanofficer has one log that is on duty", loanOfficer.log.size(), 1);
		assertTrue("teller is on duty", loanOfficer.log.containsString("On duty"));
		

		assertTrue(manager.pickAndExecuteAnAction());
		assertEquals("teller has one log that is on duty", teller.log.size(), 1);
		assertTrue("teller is on duty", teller.log.containsString("On duty"));
		
		
		assertTrue(manager.pickAndExecuteAnAction());
		assertEquals("bank should be open", manager.getBankStatus(), BankManagerRole.BankState.open);
		
		//teller has a request to create account
		manager.msgCreateAccount("BankTeller");
		
		//call pickandexecute
		assertTrue(manager.pickAndExecuteAnAction());
		
		assertEquals("should have created an account", manager.getAccounts().size(),1);
		assertEquals("mockteller should have received a message", teller.log.size(),2);
		assertTrue("account created", teller.log.containsString("Account created"));
		
		assertEquals("should have 0 balance", manager.getAccounts().get(1).getBalance(), 0.0);
		assertEquals("should have 9 loan", manager.getAccounts().get(1).getLoan(), 0.0);
		
	}
//	
	public void testFullWorkDayNormative() {
		System.out.println("This test ensures that employees are paid correctly on a full workday "
				+ "with no swaps and the bank manager knows when to close the bank");
		
		//preconditions
		assertEquals("Teller should have 0 logs, ", teller.log.size(), 0);
		assertEquals("Manager should have 0 accounts", manager.getAccounts().size(), 0);
		
		manager.getOfficer().add(new MyLoanOfficer(loanOfficer));
		manager.getTeller().add(new MyTeller(teller));
		
		manager.msgTimeUpdate(1);
		assertEquals(manager.getBankStatus(), BankManagerRole.BankState.newDay);
		manager.msgTimeUpdate(8);
	
		//loan and teller added
		assertTrue(manager.pickAndExecuteAnAction());
		assertEquals("size of officerlist =1", manager.getOfficer().size(),1);
//		assertEquals("officer has one log that is on duty", loanOfficer.log.size(), 1);
		assertTrue("officer is on duty", loanOfficer.log.containsString("On duty"));
		assertTrue(manager.pickAndExecuteAnAction());
		assertEquals("size of tellerlist =1", manager.getTeller().size(),1);
		assertEquals("teller has one log that is on duty", teller.log.size(), 1);
		assertTrue("teller is on duty", teller.log.containsString("On duty"));
	

	
		
		
		//bank should open
		
		assertTrue(manager.pickAndExecuteAnAction());
		assertEquals("bank should be open", manager.getBankStatus(), BankManagerRole.BankState.open);
		
		
		//say a whole day goes by...
		manager.msgTimeUpdate(20);
		assertTrue(manager.pickAndExecuteAnAction());
		
		assertEquals("should be no tellers on list", manager.getTeller().size(), 0);
		assertEquals("should be no officers on list", manager.getOfficer().size(), 0);
		
		assertEquals("bank should now be closed", manager.getBankStatus(), BankManagerRole.BankState.closed);
		
		assertEquals("teller now has 2 logs", teller.log.size(), 2);
		assertEquals("loanofficer now has 2 logs", loanOfficer.log.size(), 2);
		
		assertTrue("should have been paid 120", teller.log.containsString("Off duty pay = 120"));
		assertTrue("should have been paid 120", loanOfficer.log.containsString("Off duty pay = 120"));
		
		assertFalse("should be deactive now", manager.isActive);
		
		assertFalse(manager.pickAndExecuteAnAction());
		
	}
	
	public void testSwapWorkers() {
		System.out.println("this test checks to see that officers swap and proper pay is given to leaving employee");
		
		
		
		//preconditions
		assertEquals("Teller should have 0 logs, ", teller.log.size(), 0);
		assertEquals("Manager should have 0 accounts", manager.getAccounts().size(), 0);
		
		
		manager.getOfficer().add(new MyLoanOfficer(loanOfficer));
		manager.getTeller().add(new MyTeller(teller));
		
		manager.msgTimeUpdate(1);
		assertEquals(manager.getBankStatus(), BankManagerRole.BankState.newDay);
		manager.msgTimeUpdate(8);
	
		//loan and teller added
		assertTrue(manager.pickAndExecuteAnAction());
		assertEquals("size of officerlist =1", manager.getOfficer().size(),1);
//		assertEquals("officer has one log that is on duty", loanOfficer.log.size(), 1);
		assertTrue("officer is on duty", loanOfficer.log.containsString("On duty"));
		assertTrue(manager.pickAndExecuteAnAction());
		assertEquals("size of tellerlist =1", manager.getTeller().size(),1);
		assertEquals("teller has one log that is on duty", teller.log.size(), 1);
		assertTrue("teller is on duty", teller.log.containsString("On duty"));
	

	
		
		
		//bank should open
		
		assertTrue(manager.pickAndExecuteAnAction());
		assertEquals("bank should be open", manager.getBankStatus(), BankManagerRole.BankState.open);
		
		//say teller 2 comes to work 4 hours later
		manager.msgTimeUpdate(12);
		manager.getTeller().add(new MyTeller(teller2));
		assertEquals("should have 2 tellers in list now", manager.getTeller().size(), 2);
		
		assertTrue(manager.pickAndExecuteAnAction());
		assertEquals("should have 1 teller in list now", manager.getTeller().size(), 1);
		
		assertEquals("teller should have 2 logs", teller.log.size(),2);
		assertTrue("should have been paid 40", teller.log.containsString("Off duty pay = 40"));
		
	
		assertEquals("teller2 should have 1 log that is on duty", teller2.log.size(),1);
		assertTrue("teller2 is on duty", teller2.log.containsString("On duty"));
		
		//say loanofficer2 comes now too
				
		manager.getOfficer().add(new MyLoanOfficer(loanOfficer2));
		assertEquals("should have 2 loan officers in list now", manager.getOfficer().size(), 2);
		
		assertTrue(manager.pickAndExecuteAnAction());
		assertEquals("should have 1 officer in list now", manager.getOfficer().size(), 1);
		
		assertEquals("teller should have 2 logs", teller.log.size(),2);
		assertTrue("should have been paid 40", loanOfficer.log.containsString("Off duty pay = 40"));
	
		assertEquals("officer2 should have 1 log that is on duty", loanOfficer2.log.size(),1);
		assertTrue("officer2 is on duty", loanOfficer2.log.containsString("On duty"));
				
		//say a whole day goes by...
		manager.msgTimeUpdate(20);
//		assertTrue("manager recognized time to close..", manager.log.containsString("Time update 20"));
		assertTrue(manager.pickAndExecuteAnAction());
		
		assertEquals("should be no tellers on list", manager.getTeller().size(), 0);
		assertEquals("should be no officers on list", manager.getOfficer().size(), 0);
		
		assertEquals("bank should now be closed", manager.getBankStatus(), BankManagerRole.BankState.closed);
		
		assertEquals("teller2 now has 2 logs", teller2.log.size(), 2);
		assertEquals("loanofficer2 now has 2 logs", loanOfficer2.log.size(), 2);
		
		assertTrue("should have been paid 80", teller2.log.containsString("Off duty pay = 80"));
		assertTrue("should have been paid 80", loanOfficer2.log.containsString("Off duty pay = 80"));
		
		assertFalse("should be deactive now", manager.isActive);
		
		assertFalse(manager.pickAndExecuteAnAction());
		
	}
	
	/*
	 * 	abstract public void msgProcessTransaction(int AN, double amount);
	
		abstract public void msgNewLoan(int AN, double amount);
	
		abstract public void msgGaveALoan(double cash);
	 * 
	 * 
	 */
	
	public void testTellerHandlesCustomer() {
		
		System.out.println("this test checks to see that manager deals with customers correctly");
		
		//preconditions
		assertEquals("Teller should have 0 logs, ", teller.log.size(), 0);
		assertEquals("Manager should have 0 accounts", manager.getAccounts().size(), 0);
		assertEquals("BankCustomer should have 0 logs", customer1t.log.size(), 0);
		
		//say a customer shows up before bank is open
		manager.getCustomers().add(new MyCustomer(customer1t, BankManagerRole.MyCustomer.MyCustomerState.transaction));
		
		assertEquals("manager has one customer", manager.getCustomers().size(), 1);
		assertTrue(manager.pickAndExecuteAnAction());
		
		assertEquals("manager should have no customers", manager.getCustomers().size(), 0);
		
		assertTrue("customer left bc bank closed", customer1t.log.containsString("Bank closed"));
	
		
		manager.getOfficer().add(new MyLoanOfficer(loanOfficer));
		manager.getTeller().add(new MyTeller(teller));
		
		manager.msgTimeUpdate(1);
		assertEquals(manager.getBankStatus(), BankManagerRole.BankState.newDay);
		manager.msgTimeUpdate(8);
	
		//loan and teller added
		assertTrue(manager.pickAndExecuteAnAction());
		assertEquals("size of officerlist =1", manager.getOfficer().size(),1);
//				assertEquals("officer has one log that is on duty", loanOfficer.log.size(), 1);
		assertTrue("officer is on duty", loanOfficer.log.containsString("On duty"));
		assertTrue(manager.pickAndExecuteAnAction());
		assertEquals("size of tellerlist =1", manager.getTeller().size(),1);
		assertEquals("teller has one log that is on duty", teller.log.size(), 1);
		assertTrue("teller is on duty", teller.log.containsString("On duty"));
		
		//bank should open
		
		assertTrue(manager.pickAndExecuteAnAction());
		assertEquals("bank should be open", manager.getBankStatus(), BankManagerRole.BankState.open);
		
		//hey look same customer comes back
		manager.getCustomers().add(new MyCustomer(customer1t, BankManagerRole.MyCustomer.MyCustomerState.transaction));
		assertTrue(manager.pickAndExecuteAnAction());
		assertEquals("customer shouldn't be on list anymore", manager.getCustomers().size(),0);
		assertTrue("customer serviced to teller", customer1t.log.containsString("Going to teller"));
		
		assertEquals("teller should be busy", manager.getTeller().get(0).state, BankManagerRole.MyTeller.MyTellerState.occupied);
		
		//say another customer shows up
		manager.getCustomers().add(new MyCustomer(customer2t, BankManagerRole.MyCustomer.MyCustomerState.transaction));
		assertFalse(manager.pickAndExecuteAnAction());
		
		//teller has a request to create account
		manager.msgCreateAccount("BankTeller");
		
		//call pickandexecute
		assertTrue(manager.pickAndExecuteAnAction());
		
		assertEquals("should have created an account", manager.getAccounts().size(),1);
		assertEquals("mockteller should have received a message", teller.log.size(),2);
		assertTrue("account created", teller.log.containsString("Account created"));
		
		assertEquals("should have 0 balance", manager.getAccounts().get(1).getBalance(), 0.0);
		assertEquals("should have 0 loan", manager.getAccounts().get(1).getLoan(), 0.0);
		
		//teller has a transaction request
		manager.msgProcessTransaction(1, 50);
		
		//call pickandexecute
		assertTrue(manager.pickAndExecuteAnAction());
		
		assertTrue("transaction successful, 50 should have been depositied", teller.log.containsString("Transaction processed 50"));
		
		//make sure not called again
		assertFalse(manager.pickAndExecuteAnAction());
		
		manager.msgAvailable(teller);
		assertTrue(manager.pickAndExecuteAnAction());
		assertEquals("customer shouldn't be on list anymore", manager.getCustomers().size(),0);
		assertTrue("customer serviced to teller", customer2t.log.containsString("Going to teller"));
		
		
	}
	
public void testLoanOfficerrHandlesCustomer() {
		
		System.out.println("this test checks to see that manager deals with customers correctly");
		
		//preconditions
		assertEquals("Teller should have 0 logs, ", teller.log.size(), 0);
		assertEquals("Manager should have 0 accounts", manager.getAccounts().size(), 0);
		assertEquals("BankCustomer should have 0 logs", customer1t.log.size(), 0);
		
		//say a customer shows up before bank is open
		manager.getCustomers().add(new MyCustomer(customer1t, BankManagerRole.MyCustomer.MyCustomerState.transaction));
		
		assertEquals("manager has one customer", manager.getCustomers().size(), 1);
		assertTrue(manager.pickAndExecuteAnAction());
		
		assertEquals("manager should have no customers", manager.getCustomers().size(), 0);
		
		assertTrue("customer left bc bank closed", customer1t.log.containsString("Bank closed"));
	
		
		manager.getOfficer().add(new MyLoanOfficer(loanOfficer));
		manager.getTeller().add(new MyTeller(teller));
		
		manager.msgTimeUpdate(1);
		assertEquals(manager.getBankStatus(), BankManagerRole.BankState.newDay);
		manager.msgTimeUpdate(8);
	
		//loan and teller added
		assertTrue(manager.pickAndExecuteAnAction());
		assertEquals("size of officerlist =1", manager.getOfficer().size(),1);
//				assertEquals("officer has one log that is on duty", loanOfficer.log.size(), 1);
		assertTrue("officer is on duty", loanOfficer.log.containsString("On duty"));
		assertTrue(manager.pickAndExecuteAnAction());
		assertEquals("size of tellerlist =1", manager.getTeller().size(),1);
		assertEquals("teller has one log that is on duty", teller.log.size(), 1);
		assertTrue("teller is on duty", teller.log.containsString("On duty"));
		
		//bank should open
		
		assertTrue(manager.pickAndExecuteAnAction());
		assertEquals("bank should be open", manager.getBankStatus(), BankManagerRole.BankState.open);
		
		//hey look same customer comes back
		manager.getCustomers().add(new MyCustomer(customer1t, BankManagerRole.MyCustomer.MyCustomerState.loan));
		assertTrue(manager.pickAndExecuteAnAction());
		assertEquals("customer shouldn't be on list anymore", manager.getCustomers().size(),0);
		assertTrue("customer serviced to loan officer", customer1t.log.containsString("Going to officer"));
		
		assertEquals("loanofficer should be busy", manager.getOfficer().get(0).state, BankManagerRole.MyLoanOfficer.MyOfficerState.occupied);
		
		//say another customer shows up
		manager.getCustomers().add(new MyCustomer(customer1l, BankManagerRole.MyCustomer.MyCustomerState.loan));
		assertFalse(manager.pickAndExecuteAnAction());
		
		//teller has a request to create account
		manager.msgCreateAccount("BankTeller");
		
		//call pickandexecute
		assertTrue(manager.pickAndExecuteAnAction());
		

		//teller has a transaction request
		manager.msgNewLoan(1, 150);
		//call pickandexecute
		assertTrue(manager.pickAndExecuteAnAction());
		
		assertTrue("loan processed", loanOfficer.log.containsString("Processed loan"));
		double orig=manager.getVault();
		
		manager.msgGaveALoan(150);
		assertEquals("vault dec by 150", manager.getVault(), orig-150);
		
		//make sure not called again
		assertFalse(manager.pickAndExecuteAnAction());
		
		manager.msgAvailable(loanOfficer);
		assertTrue(manager.pickAndExecuteAnAction());
		assertEquals("customer shouldn't be on list anymore", manager.getCustomers().size(),0);
		assertTrue("customer serviced to teller", customer1l.log.containsString("Going to officer"));
		
		
	}

	public void testTransferMoneyToLandlord() {
		//checking preconditions
		assertEquals("BankManager should have no clients right now. It doesn't.", manager.clients.size(), 0);
		assertEquals("Teller shouldn't have any logs right now. It doesn't. ", teller.log.size(), 0);
		assertEquals("Manager should have no accounts. It doesn't.", manager.getAccounts().size(), 0);
		assertEquals("BankCustomer should have no logs right now. It doesn't.", customer1t.log.size(), 0);
		assertFalse("BankManager's scheduler should have returned false now, since it has nothing to do. It didn't.", manager.pickAndExecuteAnAction());

		//hack to create account for tenant
		manager.accounts.put(8, manager.new MyAccount(100, 0));
		manager.msgHereIsYourRentBill(landlord, 8, 25);
		
		//checking postconditions
		assertEquals("BankManager should have one client right now. It doesn't.", manager.clients.size(), 1);
		
		//calling scheduler
		assertTrue("BankManager's scheduler should have returned true now, since it has to do something. It didn't.", manager.pickAndExecuteAnAction());
		assertTrue("Landlord should have logged an event for receiving a job but instead it's: " + landlord.log.getLastLoggedEvent().toString(), landlord.log.containsString("Received money for tenant"));
		assertFalse("BankManager's scheduler should have returned false now, since it has nothing to do. It didn't.", manager.pickAndExecuteAnAction());
		assertEquals("BankManager should have no clients right now. It doesn't.", manager.clients.size(), 0);
		assertEquals("The tenant should have less money now, but it doesn't.", manager.accounts.get(8).balance, 75.0);

		
	}
}