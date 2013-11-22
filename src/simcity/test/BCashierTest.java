package simcity.test;

import simcity.PersonAgent;
import simcity.interfaces.*;
import simcity.BRestaurant.*;


import simcity.test.mock.BMockCustomer;
//import restaurant.test.mock.MockMarket;
import junit.framework.*;

public class BCashierTest extends TestCase
{
//	//these are instantiated for each test separately via the setUp() method.
//	BCashierRole cashier;
//	MockMarket market1;
//	MockMarket market2;
//	MockMarket market3;
//	MockCustomer customer1;
//	MockCustomer customer2;
//	MockCustomer customer3;
//	BCheck check1;
//	BCheck check2;
//	BCheck check3;
//	BCheck check4;
//
//
//	/**
//	 * This method is run before each test. You can use it to instantiate the class variables
//	 * for your agent and mocks, etc.
//	 */
//	public void setUp() throws Exception{
//		super.setUp();                
//		cashier = new BCashierRole(null);
//		
//		
//		
//		
//		check1=new BCheck("steak");
//		check2=new BCheck("pizza");
//		check3=new BCheck("salad");
//		check4=new BCheck("chicken");
//
//		market1= new MockMarket("market1");
//		market2= new MockMarket("market2"); //this market would not have enough items in it.  
//		market3= new MockMarket("market3"); 
//
//		customer1 = new MockCustomer("mockcustomer1"); 
//		customer2 = new MockCustomer("mockcustomer2");
//		customer3 = new MockCustomer("mockcustomer3");
//
//	}        
//	/**
//	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
//	 */
//	public void testOneNormalMarketScenario()
//	{
//		//tests one market one order 
//
//
//		//check preconditions
//		assertEquals("Cashier should have 0 checks in it. It doesn't.",cashier.myChecks.size(), 0);
//		assertEquals("Cashier starts with 500 dollars for deficits.  It does not.", cashier.totalMoney, 500);
//		assertEquals("Cashier should have no Market Checks needed to be paid.  It does not", cashier.marketChecks.size(), 0);
//
//		assertEquals("Market should have an empty log because no messages have been sent yet",  market1.log.size(), 0);
//		//messaging
//		BCheck scenarioOneCheck=new BCheck ("pizza", 10);
//		cashier.msgHereisCheckfromMarket(scenarioOneCheck, market1);
//
//		assertEquals("Cashier should have 1 check its market Checks to be paid", cashier.marketChecks.size(), 1);
//		assertTrue("Cashier's one check should be ReceivedByMarket", cashier.marketChecks.get(0).state==paidState.ReceivedByMarket);
//
//		assertTrue("Cashier's scheduler should be active because of the check received", cashier.pickAndExecuteAnAction());
//
//		//post messaging
//
//		assertEquals("Cashier should have 410 money after scheduler is activated because 500-(9*10)=410", cashier.totalMoney, 410);
//		assertTrue("Cashier should have market Check state of Paid by Cashier", cashier.marketChecks.get(0).state==paidState.PaidByCashier);
//		assertEquals("Market should have received message from cashier and logged", market1.log.size(), 1);
//
//
//
//	}
//
//
//	public void testTwoMarketScenario()
//	{
//		//tests two markets under the same under
//
//		//First must do first test 
//		assertEquals("Cashier should have 0 checks in it. It doesn't.",cashier.myChecks.size(), 0);
//		assertEquals("Cashier starts with 500 dollars for deficits.  It does not.", cashier.totalMoney, 500);
//		assertEquals("Cashier should have no Market Checks needed to be paid.  It does not", cashier.marketChecks.size(), 0);
//
//		assertEquals("Market should have an empty log because no messages have been sent yet",  market2.log.size(), 0);
//		assertEquals("Market should have an empty log because no messages have been sent yet",  market3.log.size(), 0);
//		//messaging
//		BCheck scenarioTwoCheck=new BCheck ("pizza", 10);
//		cashier.msgHereisCheckfromMarket(scenarioTwoCheck, market2);
//
//		assertEquals("Cashier should have 1 check in its market Checks to be paid", cashier.marketChecks.size(), 1);
//		assertTrue("Cashier's one check should be ReceivedByMarket", cashier.marketChecks.get(0).state==paidState.ReceivedByMarket);
//
//		assertTrue("Cashier's scheduler should be active because of the check received", cashier.pickAndExecuteAnAction());
//
//
//
//		assertEquals("Cashier should have 410 money after scheduler is activated because 500-(9*10)=410", cashier.totalMoney, 410);
//		assertTrue("Cashier should have market Check state of Paid by Cashier", cashier.marketChecks.get(0).state==paidState.PaidByCashier);
//		assertEquals("Market should have received message from cashier and logged", market2.log.size(), 1);
//
//		assertFalse("Cashier's scheduler should not activate because there should be no Checks with a state needing to be acted upon", cashier.pickAndExecuteAnAction());
//
//		//cook will then move to next market whether or not this would work accurately would need a MarketTest, this begins second payment of the bill
//
//		cashier.msgHereisCheckfromMarket(scenarioTwoCheck, market3);
//
//		assertEquals("Cashier should have 2 checks in its market Checks, one has been paid already", cashier.marketChecks.size(),2);
//		assertTrue("Cashier's second check should have a state of RecievedByMarket", cashier.marketChecks.get(1).state==paidState.ReceivedByMarket);
//
//		assertTrue("Cashier's scheduler shoudl be active because of second check received", cashier.pickAndExecuteAnAction());
//
//		assertEquals("Cashier should have 320 because full order is made after 1st order", cashier.totalMoney, 320);
//		assertTrue("Cashier should have second market Check state of Paid by Cashier", cashier.marketChecks.get(1).state==paidState.PaidByCashier);
//		assertEquals("Second Market should have received message from cashier and logged", market3.log.size(), 1);
//
//		assertFalse("Cashier's scheduler should not activate again because there should be no Checks with a state needing to be acted upon", cashier.pickAndExecuteAnAction());
//
//	}
//
//
//	public void testOneCustomerNormScenario(){
//
//		//tests one customer with one food item 
//		
//		assertEquals("Cashier should have no checks in myChecks to begin with", cashier.myChecks.size(), 0);
//		assertEquals("Cashier should have initial 500 dollars", cashier.totalMoney, 500);
//		assertEquals("List of customers should be empty because there are no customers paying yet", cashier.myCustomers.size(),0);
//
//
//		assertEquals("Customer1 Log should be empty because messages have no been passed around", customer1.log.size(),0);
//		cashier.msgCashierCheck(check1, customer1); //message received from waiter
//		customer1.setCashier(cashier);
//
//
//
//		assertEquals("Cashier should have one check", cashier.myChecks.size(), 1);
//		assertEquals("Cashier should have one customer that needs his check paid", cashier.myCustomers.size(), 1);
//		assertFalse("Cashier's check should not have been paid yet", check1.paidbyCustomer);
//
//		assertEquals("Customer 1 Log should now have received the message from cashier", customer1.log.size(), 1);
//
//		//customer1's scheduler would activate, pay, and send back the check  to the cashier, this would be unit tested in a CustomerTest
//
//		cashier.msgPayCheck(check1);
//
//		assertTrue("Cashier's check paidbyCustomer should be true as it was sent back", cashier.myChecks.get(0).paidbyCustomer);
//		assertTrue("Cashier's pickAndExecute should be active due to the change in state of the Check", cashier.pickAndExecuteAnAction());
//		assertTrue("Customer should have logged event of receiving message from cashier", customer1.log.containsString("Received msgHereisYourCheck from the Cashier!"));
//
//		assertEquals("Cashier's totalMoney should increase as it received the money from the customer.  It should be $516 because he ordered a steak", cashier.totalMoney, 516);
//		assertEquals("Cashier's Checks should be empty once again because the check has been paid and removed from Checks", cashier.myChecks.size(), 0);
//		assertFalse("Cashier's pickAndExecute should be inactive because there is nothing to act on", cashier.pickAndExecuteAnAction());
//
//	}
//
//	public void testTwoCustomerScenario(){
//		
//		//two customer test with two different food items 
//
//		assertEquals("Cashier should have no checks in myChecks to begin with", cashier.myChecks.size(), 0);
//		assertEquals("Cashier should have initial 500 dollars", cashier.totalMoney, 500);
//		assertEquals("List of customers should be empty because there are no customers paying yet", cashier.myCustomers.size(),0);
//
//
//		assertEquals("Customer1 Log should be empty because messages have no been passed around", customer1.log.size(),0);
//		cashier.msgCashierCheck(check1, customer1); //message received from waiter
//		customer1.setCashier(cashier);
//
//
//
//		assertEquals("Cashier should have one check", cashier.myChecks.size(), 1);
//		assertEquals("Cashier should have one customer that needs his check paid", cashier.myCustomers.size(), 1);
//		assertFalse("Cashier's check should not have been paid yet", check1.paidbyCustomer);
//
//		assertEquals("Customer 1 Log should now have received the message from cashier", customer1.log.size(), 1);
//
//		//customer1's scheduler would activate, pay, and send back the check  to the cashier, this would be unit tested in a CustomerTest
//
//		cashier.msgPayCheck(check1);
//
//		assertTrue("Cashier's check paidbyCustomer should be true as it was sent back", cashier.myChecks.get(0).paidbyCustomer);
//		assertTrue("Cashier's pickAndExecute should be active due to the change in state of the Check", cashier.pickAndExecuteAnAction());
//
//		assertEquals("Cashier's totalMoney should increase as it received the money from the customer.  It should be $516 because he ordered a steak", cashier.totalMoney, 516);
//		assertEquals("Cashier's Checks should be empty once again because the check has been paid and removed from Checks", cashier.myChecks.size(), 0);
//		assertFalse("Cashier's pickAndExecute should be inactive because there is nothing to act on", cashier.pickAndExecuteAnAction());
//
//		assertEquals("Customer2 Log should be empty because messages have no been passed around", customer2.log.size(),0);
//
//		cashier.msgCashierCheck(check2, customer2);
//		customer2.setCashier(cashier);
//
//		assertEquals("Cashier should have initial 516 dollars", cashier.totalMoney, 516);
//		assertEquals("List of customers should have more than one person because there has already been a customer", cashier.myCustomers.size(),2);
//
//		assertEquals("Customer2 Log should not be empty because messages have  been passed around", customer2.log.size(),1);
//
//		assertEquals("Cashier should have one check", cashier.myChecks.size(), 1);
//		assertEquals("Cashier should have two customers, one has already paid", cashier.myCustomers.size(), 2);
//		assertFalse("Cashier's check should not have been paid yet", check2.paidbyCustomer);
//
//		assertEquals("Customer 2 Log should now have received the message from cashier", customer2.log.size(), 1);
//
//		//customer1's scheduler would activate, pay, and send back the check  to the cashier, this would be unit tested in a CustomerTest
//
//		cashier.msgPayCheck(check2);
//
//		assertTrue("Cashier's check paidbyCustomer should be true as it was sent back", cashier.myChecks.get(0).paidbyCustomer);
//		assertTrue("Cashier's pickAndExecute should be active due to the change in state of the Check", cashier.pickAndExecuteAnAction());
//
//		assertEquals("Cashier's totalMoney should increase as it received the money from the customer.  It should be $525 because he ordered a pizza", cashier.totalMoney, 525);
//		assertEquals("Cashier's Checks should be empty once again because the check has been paid and removed from Checks", cashier.myChecks.size(), 0);
//		assertFalse("Cashier's pickAndExecute should be inactive because there is nothing to act on", cashier.pickAndExecuteAnAction());
//
//	}
//
//	public void testOneCustomerRepeatScenario(){
//		
//		//tests one customer who comes back and orders different things at each trip 
//
//		assertEquals("Cashier should have no checks in myChecks to begin with", cashier.myChecks.size(), 0);
//		assertEquals("Cashier should have initial 500 dollars", cashier.totalMoney, 500);
//		assertEquals("List of customers should be empty because there are no customers paying yet", cashier.myCustomers.size(),0);
//
//
//		assertEquals("Customer1 Log should be empty because messages have no been passed around", customer1.log.size(),0);
//		cashier.msgCashierCheck(check1, customer1); //message received from waiter
//		customer1.setCashier(cashier);
//
//
//
//		assertEquals("Cashier should have one check", cashier.myChecks.size(), 1);
//		assertEquals("Cashier should have one customer that needs his check paid", cashier.myCustomers.size(), 1);
//		assertFalse("Cashier's check should not have been paid yet", check1.paidbyCustomer);
//
//		assertEquals("Customer 1 Log should now have received the message from cashier", customer1.log.size(), 1);
//
//		//customer1's scheduler would activate, pay, and send back the check  to the cashier, this would be unit tested in a CustomerTest
//
//		cashier.msgPayCheck(check1);
//
//		assertTrue("Cashier's check paidbyCustomer should be true as it was sent back", cashier.myChecks.get(0).paidbyCustomer);
//		assertTrue("Cashier's pickAndExecute should be active due to the change in state of the Check", cashier.pickAndExecuteAnAction());
//
//		assertEquals("Cashier's totalMoney should increase as it received the money from the customer.  It should be $516 because he ordered a steak", cashier.totalMoney, 516);
//		assertEquals("Cashier's Checks should be empty once again because the check has been paid and removed from Checks", cashier.myChecks.size(), 0);
//		assertFalse("Cashier's pickAndExecute should be inactive because there is nothing to act on", cashier.pickAndExecuteAnAction());
//
//		//customer will then come back 
//		cashier.msgRepeatCashierCheck(check3, customer1);
//
//		assertEquals("Cashier should have one check", cashier.myChecks.size(), 1);
//		assertEquals("List of customers should remain the same because the customer is coming back in again but is the same customer", cashier.myCustomers.size(), 1); //this has not changed because same customer
//		assertFalse("Cashier's check should not have been paid yet", check3.paidbyCustomer);// should remain the same 
//
//		assertEquals("Customer 1 Log should now have received the message from cashier", customer1.log.size(), 2); //the difference is here there should be another message received because the customer is orders again and receives an additional message
//
//		//customer1's scheduler would activate, pay, and send back the check  to the cashier, this would be unit tested in a CustomerTest
//
//		cashier.msgPayCheck(check3);
//
//		assertTrue("Cashier's check paidbyCustomer should be true as it was sent back", cashier.myChecks.get(0).paidbyCustomer);
//		assertTrue("Cashier's pickAndExecute should be active due to the change in state of the Check", cashier.pickAndExecuteAnAction());
//
//		assertEquals("Cashier's totalMoney should increase as it received the money from the customer.  It should be $522 because he ordered a steak and then a salad", cashier.totalMoney, 522); 
//		assertEquals("Cashier's Checks should be empty once again because the check has been paid and removed from Checks", cashier.myChecks.size(), 0);
//		assertFalse("Cashier's pickAndExecute should be inactive because there is nothing to act on", cashier.pickAndExecuteAnAction());
//
//
//
//	}
//
//
//	public void testThreeCustomerScenario(){
//		
//		//will test three customer scenario all with different food orders.  
//
//		assertEquals("Cashier should have no checks in myChecks to begin with", cashier.myChecks.size(), 0);
//		assertEquals("Cashier should have initial 500 dollars", cashier.totalMoney, 500);
//		assertEquals("List of customers should be empty because there are no customers paying yet", cashier.myCustomers.size(),0);
//
//
//		assertEquals("Customer1 Log should be empty because messages have no been passed around", customer1.log.size(),0);
//		cashier.msgCashierCheck(check1, customer1); //message received from waiter
//		customer1.setCashier(cashier);
//
//
//
//		assertEquals("Cashier should have one check", cashier.myChecks.size(), 1);
//		assertEquals("Cashier should have one customer that needs his check paid", cashier.myCustomers.size(), 1);
//		assertFalse("Cashier's check should not have been paid yet", check1.paidbyCustomer);
//
//		assertEquals("Customer 1 Log should now have received the message from cashier", customer1.log.size(), 1);
//
//		//customer1's scheduler would activate, pay, and send back the check  to the cashier, this would be unit tested in a CustomerTest
//
//		cashier.msgPayCheck(check1);
//
//		assertTrue("Cashier's check paidbyCustomer should be true as it was sent back", cashier.myChecks.get(0).paidbyCustomer);
//		assertTrue("Cashier's pickAndExecute should be active due to the change in state of the Check", cashier.pickAndExecuteAnAction());
//
//		assertEquals("Cashier's totalMoney should increase as it received the money from the customer.  It should be $516 because he ordered a steak", cashier.totalMoney, 516);
//		assertEquals("Cashier's Checks should be empty once again because the check has been paid and removed from Checks", cashier.myChecks.size(), 0);
//		assertFalse("Cashier's pickAndExecute should be inactive because there is nothing to act on", cashier.pickAndExecuteAnAction());
//
//		assertEquals("Customer2 Log should be empty because messages have no been passed around", customer2.log.size(),0);
//
//		cashier.msgCashierCheck(check2, customer2);
//		customer2.setCashier(cashier);
//
//		assertEquals("Cashier should have initial 516 dollars", cashier.totalMoney, 516);
//		assertEquals("List of customers should have two people because there has already been a customer", cashier.myCustomers.size(),2);
//
//		assertEquals("Customer2 Log should not be empty because messages have  been passed around", customer2.log.size(),1);
//
//		assertEquals("Cashier should have one check", cashier.myChecks.size(), 1);
//		assertEquals("Cashier should have two customers, one has already paid", cashier.myCustomers.size(), 2);
//		assertFalse("Cashier's check should not have been paid yet", check2.paidbyCustomer);
//
//		assertEquals("Customer 2 Log should now have received the message from cashier", customer2.log.size(), 1);
//
//		
//		cashier.msgPayCheck(check2);
//
//		assertTrue("Cashier's check paidbyCustomer should be true as it was sent back", cashier.myChecks.get(0).paidbyCustomer);
//		assertTrue("Cashier's pickAndExecute should be active due to the change in state of the Check", cashier.pickAndExecuteAnAction());
//
//		assertEquals("Cashier's totalMoney should increase as it received the money from the customer.  It should be $525 because he ordered a pizza", cashier.totalMoney, 525);
//		assertEquals("Cashier's Checks should be empty once again because the check has been paid and removed from Checks", cashier.myChecks.size(), 0);
//		assertFalse("Cashier's pickAndExecute should be inactive because there is nothing to act on", cashier.pickAndExecuteAnAction());
//		
//		assertEquals("Customer3 log should be empty because has not received messages from the Cashier", customer3.log.size(), 0);
//		
//		cashier.msgCashierCheck(check4, customer3);
//		customer3.setCashier(cashier);
//
//		assertEquals("Cashier should have initial 525 dollars because of previous orders", cashier.totalMoney, 525);
//		assertEquals("List of customers should have three because there has already been a customer", cashier.myCustomers.size(),3);
//
//		assertEquals("Customer3 Log should not be empty because messages have  been passed around", customer3.log.size(),1);
//
//		assertEquals("Cashier should have one check", cashier.myChecks.size(), 1);
//		assertEquals("Cashier should have two customers, one has already paid", cashier.myCustomers.size(), 3);
//		assertFalse("Cashier's check should not have been paid yet", check4.paidbyCustomer);
//
//		assertEquals("Customer 3 Log should now have received the message from cashier", customer3.log.size(), 1);
//
//		
//		cashier.msgPayCheck(check4);
//
//		assertTrue("Cashier's check paidbyCustomer should be true as it was sent back", cashier.myChecks.get(0).paidbyCustomer);
//		assertTrue("Cashier's pickAndExecute should be active due to the change in state of the Check", cashier.pickAndExecuteAnAction());
//
//		assertEquals("Cashier's totalMoney should increase as it received the money from the customer.  It should be $536 because he ordered a pizza", cashier.totalMoney, 536);
//		assertEquals("Cashier's Checks should be empty once again because the check has been paid and removed from Checks", cashier.myChecks.size(), 0);
//		assertFalse("Cashier's pickAndExecute should be inactive because there is nothing to act on", cashier.pickAndExecuteAnAction());
//
//	}
//
//


}