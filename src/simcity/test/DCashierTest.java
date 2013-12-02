package simcity.test;

import java.text.DecimalFormat;
import java.util.ArrayList;

import simcity.test.mock.MockCook;
import simcity.test.mock.MockCustomer;
import simcity.test.mock.MockMarket;
import simcity.test.mock.MockWaiter;
import simcity.DCashierRole;
import simcity.DCheck;
import simcity.DFoodOrder;
import simcity.DCashierRole.InventoryBill.InventoryBillState;
import simcity.DCheck.CheckState;
import simcity.PersonAgent;
import junit.framework.*;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Monroe Ekilah
 */
public class DCashierTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	DCashierRole cashier;
	MockCook cook;
	MockWaiter waiter;
	MockMarket market1;
	MockMarket market2;
	MockCustomer customer1;
	MockCustomer customer2;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	@Override
	public void setUp() throws Exception{
		super.setUp();		
		PersonAgent p = new PersonAgent("Doreen");
		cashier = new DCashierRole();
		cashier.myPerson=p;
		p.SetJob(cashier);
		customer1 = new MockCustomer("mockcustomer1");	
		customer2 = new MockCustomer("mockcustomer2");
		market1= new MockMarket("mockmarket1");
		market2= new MockMarket("mockmarket2");
		waiter = new MockWaiter("mockwaiter");
		cook = new MockCook("mockcook");
	}	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
//	public void testOneNormalCustomerScenario()
//	{
//		//setUp() runs first before this test!
//		customer1.cashier = cashier;//You can do almost anything in a unit test.			
//		
//		//check preconditions
//		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.getBills().size(), 0);		
//		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
//						+ waiter.log.toString(), 0, waiter.log.size());
//		
//		//step 1 of the test
//		//public Bill(Cashier, Customer, int tableNum, double price) {
//		Check bill = new Check(customer1, 2, 15.99);
//		cashier.msgComputeBill("Steak", 2, "Betty", new MockWaiter("Bill"));//send the message from a waiter
//
//		//check postconditions for step 1 and preconditions for step 2
//		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
//						+ waiter.log.toString(), 0, waiter.log.size());
//		
//		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.getBills().size(), 1);
//		
//		assertTrue("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
//		//cashier.pickAndExecuteAnAction();
//		
//		assertEquals(
//				"MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
//						+ waiter.log.toString(), 0, waiter.log.size());
//		
//		assertEquals(
//				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
//						+ waiter.log.toString(), 0, waiter.log.size());
//		
//		//step 2 of the test
//		cashier.msgHereIsAPayment(customer, 2, bill.getCustomerPaid());
//		
//		//check postconditions for step 2 / preconditions for step 3
//		//assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.",
//			//	cashier.getBills().get(0).state == cashierBillState.customerApproached);
//		
//		
//		assertTrue("CashierBill should contain a bill of price = $7.98. It contains something else instead: $" 
//				+ cashier.getBills().get(0).getBillAmnt(), cashier.getBills().get(0).getBillAmnt() == 15.99);
//		
//		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
//					cashier.getBills().get(0).getCustomer() == customer);
//		
//		
//		//step 3
//		//NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
//		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
//					cashier.pickAndExecuteAnAction());
//		
//		//check postconditions for step 3 / preconditions for step 4
//		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourTotal\" with the correct balance, but his last event logged reads instead: " 
//				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourTotal from cashier. Total = 7.98"));
//	
//			
//		//assertTrue("Cashier should have logged \"Received HereIsMyPayment\" but didn't. His log reads instead: " 
//		//		+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received HereIsMyPayment"));
//		
//		
//		assertTrue("CashierBill should contain changeDue == 0.0. It contains something else instead: $" 
//				+ cashier.getBills().get(0).getChange(), cashier.getBills().get(0).getChange() == 0);
//		
//		
//		
//		//step 4
//		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
//					cashier.pickAndExecuteAnAction());
//		
//		//check postconditions for step 4
//		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
//				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourChange from cashier. Change = 0.0"));
//	
//		
//		
//		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
//				cashier.pickAndExecuteAnAction());
//		
//	
//	}//end one normal customer scenario
	
	//this test checks that cashier can pay inventory bill from one market
	public void testPaying1MarketInventoryBill() {
		System.out.println("Testing Paying One Market in Full");
		DecimalFormat df = new DecimalFormat("###.##");
		
		// Set restaurant money amount
		cashier.setRegisterAmnt(300);
		cashier.AddCook(cook);
		market1.setInventory(5);
		// Preconditions: cashier has $300 in the register
		assertEquals("Cashier should have $300.", 300, (int)cashier.getRegisterAmnt());
		
		// Preconditions: Cashier shouldn't have any invoices from the markets
		assertEquals("Cashier should have 0 invoices. It doesn't.", 0, cashier.getInventoryBill().size());
		
		// Precondition: Market should have an empty event long
		assertEquals("Market should have 0 logs.", 0, market1.log.size());
		
		ArrayList<DFoodOrder> myOrder = new ArrayList<DFoodOrder>();
		myOrder.add(new DFoodOrder("Salad", 5));
		
		market1.msgHereIsAnInventoryOrder(myOrder, 1, cashier);
		
		assertEquals("Market should have 2 logs.", 2, market1.log.size());
		
		//make sure received correct message.
		assertTrue(market1.log.containsString("Received msgHereIsAnInventoryOrder"));
		
		//make sure market can fullfill
		assertTrue(market1.log.containsString("Can fullfill fully"));
		
		//send bill to cashier
		cashier.msgHereIsAnInventoryBill(5.99*5, market1);
		
		//test msg was received..cashier should have one inventory bill in list
		assertEquals("Should have one inventory bill now", 1, cashier.getInventoryBill().size());
		
		//test that bill is from the right market
		assertEquals("Market sending bill doesn't match", market1, cashier.getInventoryBill().get(0).getMarket());
		
		//inventory bill should be 6.50
		assertEquals("Cashier's bill should be correct. Is not.", df.format(5.99*5), df.format(cashier.getInventoryBill().get(0).getAmnt()));
		
		cashier.pickAndExecuteAnAction();
		
		//no more inventory bills after this iterations
		assertEquals("should have 1 inventory bills. does.", 1, cashier.getInventoryBill().size());

		//should have sent request for approval
		assertEquals("cook should have one log", cook.log.size(), 1);
		assertTrue("cook received correct msg", cook.log.containsString("Received should I accept"));
		//should have right amount now
//		System.out.println(df.format(cashier.getRegisterAmnt()));
		
		//say it returns true
		cashier.msgAnswerVerificationRequest(false);
		
		assertEquals("order still there", cashier.getInventoryBill().size(), 1);
		assertTrue("order set to fraud", cashier.getInventoryBill().get(0).state==InventoryBillState.fraud);
		assertTrue(cashier.pickAndExecuteAnAction());
		assertEquals("fraud order should have been removed", cashier.getInventoryBill().size(), 0);
	}
	
	public void testRejectingFaultyPayment() {
		System.out.println("Testing Paying One Market in Full");
		DecimalFormat df = new DecimalFormat("###.##");
		
		// Set restaurant money amount
		cashier.setRegisterAmnt(300);
		cashier.AddCook(cook);
		market1.setInventory(5);
		// Preconditions: cashier has $300 in the register
		assertEquals("Cashier should have $300.", 300, (int)cashier.getRegisterAmnt());
		
		// Preconditions: Cashier shouldn't have any invoices from the markets
		assertEquals("Cashier should have 0 invoices. It doesn't.", 0, cashier.getInventoryBill().size());
		
		// Precondition: Market should have an empty event long
		assertEquals("Market should have 0 logs.", 0, market1.log.size());
		
		ArrayList<DFoodOrder> myOrder = new ArrayList<DFoodOrder>();
		myOrder.add(new DFoodOrder("Salad", 5));
		
		market1.msgHereIsAnInventoryOrder(myOrder, 1, cashier);
		
		assertEquals("Market should have 2 logs.", 2, market1.log.size());
		
		//make sure received correct message.
		assertTrue(market1.log.containsString("Received msgHereIsAnInventoryOrder"));
		
		//make sure market can fullfill
		assertTrue(market1.log.containsString("Can fullfill fully"));
		
		//send bill to cashier
		cashier.msgHereIsAnInventoryBill(5.99*5, market1);
		
		//test msg was received..cashier should have one inventory bill in list
		assertEquals("Should have one inventory bill now", 1, cashier.getInventoryBill().size());
		
		//test that bill is from the right market
		assertEquals("Market sending bill doesn't match", market1, cashier.getInventoryBill().get(0).getMarket());
		
		//inventory bill should be 6.50
		assertEquals("Cashier's bill should be correct. Is not.", df.format(5.99*5), df.format(cashier.getInventoryBill().get(0).getAmnt()));
		
		cashier.pickAndExecuteAnAction();
		
		//no more inventory bills after this iterations
		assertEquals("should have 1 inventory bills. does.", 1, cashier.getInventoryBill().size());

		//should have sent request for approval
		assertEquals("cook should have one log", cook.log.size(), 1);
		assertTrue("cook received correct msg", cook.log.containsString("Received should I accept"));
		//should have right amount now
//		System.out.println(df.format(cashier.getRegisterAmnt()));
		
		//say it returns true
		cashier.msgAnswerVerificationRequest(true);
		
		assertTrue(cashier.pickAndExecuteAnAction());
		
		//should have 300 - 6.50 now
		assertEquals("reg amont not reduced properly", df.format(300-5.99*5), df.format(cashier.getRegisterAmnt()));
		
		
		//check if market received bill
			//market has 3 logs now
			assertEquals("market should have 3 logs", 3, market1.log.size());
			//market received payment
			assertTrue("should receive payment msg", market1.log.containsString("Received msgHereIsAPayment"));
		
	}
	
	//this test checks to see that two inventory bills are paid from two markets, one that could not fulfill order 
	public void testPaying2MarketInventoryBills () {
		System.out.println("Testing Paying Two Markets in Full");
		DecimalFormat df = new DecimalFormat("###.##");
		cashier.AddCook(cook);
		// Set restaurant money amount
		cashier.setRegisterAmnt(300);
		market1.setInventory(5);
		market2.setInventory(7);
		// Preconditions: cashier has $300 in the register
		assertEquals("Cashier should have $300.", 300, (int)cashier.getRegisterAmnt());
		
		// Preconditions: Cashier shouldn't have any invoices from the markets
		assertEquals("Cashier should have 0 invoices. It doesn't.", 0, cashier.getInventoryBill().size());
		
		// Precondition: Market should have an empty event long
		assertEquals("Market should have 0 logs.", 0, market1.log.size());
		
		ArrayList<DFoodOrder> myOrder = new ArrayList<DFoodOrder>();
		myOrder.add(new DFoodOrder("Salad", 7));
		
		market1.msgHereIsAnInventoryOrder(myOrder, 1, cashier);
		
		assertEquals("Market should have 2 logs.", 2, market1.log.size());
		
		//make sure received correct message.
		assertTrue(market1.log.containsString("Received msgHereIsAnInventoryOrder"));
		
		//make sure market can't fullfill
		assertFalse(market1.log.containsString("Can fullfill fully"));
		
		//make sure market can fullfill partially
		assertTrue(market1.log.containsString("Can fullfill partially: 5"));
		
		//send bill to cashier
		cashier.msgHereIsAnInventoryBill(5*(5.99), market1);
		
		//test msg was received..cashier should have one inventory bill in list
		assertEquals("Should have one inventory bill now", 1, cashier.getInventoryBill().size());
		
		//test that bill is from the right market
		assertEquals("Market sending bill doesn't match", market1, cashier.getInventoryBill().get(0).getMarket());
		
		//inventory bill should be 6.50
		assertEquals("Cashier's bill should be 5*(5.99). Is not.", 5*(5.99), cashier.getInventoryBill().get(0).getAmnt());
		assertTrue("should be just added", cashier.getInventoryBill().get(0).state==InventoryBillState.justReceived);
		assertTrue(cashier.pickAndExecuteAnAction());
		
		//should still have bill
		assertEquals("should have 1 inventory bills. does.", 1, cashier.getInventoryBill().size());
		
		//should have sent request for approval
		assertEquals("cook should have one log", cook.log.size(), 1);
		assertTrue("cook received correct msg", cook.log.containsString("Received should I accept"));
		//should have right amount now
//		System.out.println(df.format(cashier.getRegisterAmnt()));
		
		//say it returns true
		cashier.msgAnswerVerificationRequest(true);
		
		assertTrue(cashier.pickAndExecuteAnAction());
		
		//assertEquals("reg amont not reduced properly", df.format(300-5*(5.99)), df.format(cashier.getRegisterAmnt()));

		//check if market received bill
			//market has 3 logs now
			assertEquals("market should have 3 logs", 3, market1.log.size());
			//market received payment
			assertTrue("should receive payment msg", market1.log.containsString("Received msgHereIsAPayment 29.95"));
		
		//NEW ORDER---------
		
		ArrayList<DFoodOrder> myOrder2 = new ArrayList<DFoodOrder>();
		myOrder2.add(new DFoodOrder("Salad", 2));
		
		// Precondition: Market should have an empty event long
		assertEquals("Market should have 0 logs.", 0, market2.log.size());
		
		market2.msgHereIsAnInventoryOrder(myOrder2, 2, cashier);
		assertEquals("Market should have 2 logs.", 2, market2.log.size());
		
		//make sure received correct message.
		assertTrue(market2.log.containsString("Received msgHereIsAnInventoryOrder"));
		
		//make sure market can fullfill
		assertTrue(market2.log.containsString("Can fullfill fully"));
		
		//send bill to cashier
		cashier.msgHereIsAnInventoryBill(2*(5.99), market2);
		
		//test msg was received..cashier should have one inventory bill in list
		assertEquals("Should have one inventory bill now", 1, cashier.getInventoryBill().size());
		
		//test that bill is from the right market
		assertEquals("Market sending bill doesn't match", market2, cashier.getInventoryBill().get(0).getMarket());
		
//		System.out.println(df.format(5.99*2)+ "  "+ df.format(cashier.getInventoryBill().get(0).getAmnt()));
		//inventory bill should be 13.0
		assertEquals("Cashier's bill should be 13. Is not.", df.format(5.99*2), df.format(cashier.getInventoryBill().get(0).getAmnt()));
		
		cashier.pickAndExecuteAnAction();
		
		//should still have bill
		assertEquals("should have 1 inventory bills. does.", 1, cashier.getInventoryBill().size());
		
		//should have sent request for approval
		assertEquals("cook should have two log", cook.log.size(), 2);
		assertTrue("cook received correct msg", cook.log.containsString("Received should I accept"));
		//should have right amount now
//		System.out.println(df.format(cashier.getRegisterAmnt()));
		
		//say it returns true
		cashier.msgAnswerVerificationRequest(true);
		
		assertTrue(cashier.pickAndExecuteAnAction());
		
		
		//no more inventory bills after this iterations
		assertEquals("should have no more inventory bills. does.", 0, cashier.getInventoryBill().size());
		
		//should have 300 - 6.50 now
//		System.out.println(cashier.getRegisterAmnt());
		assertEquals("reg amont not reduced properly", df.format(300-7*(5.99)), df.format(cashier.getRegisterAmnt()));
		

		//check if market received bill
			//market has 3 logs now
			assertEquals("market should have 3 logs", 3, market2.log.size());
			//market received payment
			assertTrue("should receive payment msg", market2.log.containsString("Received msgHereIsAPayment"));
	
		
	}
	
	//dealing with can't afford: 10% late fee to be paid when can afford
	//this test demonstrates how my implementation deal with cashier unable to pay inventory bills.. bill increases by 10% original value as a late fee
	//once the cashier receives enough customer payments that makes them able to afford this bill, will pay the greater amount
	public void testInventoryBillCashierCantAfford() {
		
		DecimalFormat df = new DecimalFormat("###.##");
		
		System.out.println("Testing how cashier deals wih Inventory bill it can't afford");
		
		// Set restaurant money amount
		cashier.AddCook(cook);
		cashier.setRegisterAmnt(0);
		market1.setInventory(5);
		// Preconditions: cashier has $300 in the register
		assertEquals("Cashier should have $0.", 0, (int)cashier.getRegisterAmnt());
		
		// Preconditions: Cashier shouldn't have any invoices from the markets
		assertEquals("Cashier should have 0 invoices. It doesn't.", 0, cashier.getInventoryBill().size());
		
		// Precondition: Market should have an empty event long
		assertEquals("Market should have 0 logs.", 0, market1.log.size());
		
		ArrayList<DFoodOrder> myOrder = new ArrayList<DFoodOrder>();
		myOrder.add(new DFoodOrder("Salad", 5));
		
		market1.msgHereIsAnInventoryOrder(myOrder, 1, cashier);
		
		assertEquals("Market should have 2 logs.", 2, market1.log.size());
		
		//make sure received correct message.
		assertTrue(market1.log.containsString("Received msgHereIsAnInventoryOrder"));
		
		//make sure market can fullfill
		assertTrue(market1.log.containsString("Can fullfill fully"));
		
		//send bill to cashier
		cashier.msgHereIsAnInventoryBill(Double.parseDouble(df.format(5.99*5)), market1);
		
		//test msg was received..cashier should have one inventory bill in list
		assertEquals("Should have one inventory bill now", 1, cashier.getInventoryBill().size());
		
		//test that bill is from the right market
		assertEquals("Market sending bill doesn't match", market1, cashier.getInventoryBill().get(0).getMarket());
		
		//inventory bill should be 6.50
		assertEquals("Cashier's bill should be correct. Is not.", df.format(5.99*5), df.format(cashier.getInventoryBill().get(0).getAmnt()));
		
		cashier.pickAndExecuteAnAction();
		
		//no more inventory bills after this iterations
		assertEquals("should have 1 bill still. does.", 1, cashier.getInventoryBill().size());
		

		assertEquals("should have 1 inventory bills. does.", 1, cashier.getInventoryBill().size());
		
		//should have sent request for approval
		assertEquals("cook should have one log", cook.log.size(), 1);
		assertTrue("cook received correct msg", cook.log.containsString("Received should I accept"));
		//should have right amount now
//		System.out.println(df.format(cashier.getRegisterAmnt()));
		
		//say it returns true
		cashier.msgAnswerVerificationRequest(true);
		
		assertTrue(cashier.pickAndExecuteAnAction());		
		
		/***checking proper interest accumulation calculation**/
//		System.err.println(df.format((1.10)*Double.parseDouble(df.format(5*5.99))));
//		System.err.println(cashier.getInventoryBill().get(0).getAmnt());
//		System.err.println(cashier.log.getLastLoggedEvent());
		assertEquals("bill amount should increase", Double.parseDouble(df.format((1.10)*Double.parseDouble(df.format(5*5.99)))), cashier.getInventoryBill().get(0).getAmnt());

		//receive a payment from customer.. register updated
		cashier.getBills().add(new DCheck(customer1, "Doreen", 1, 50)); //adding an arbitrary bill 
		
		cashier.msgHereIsAPayment(customer1, 1, 55.50);
		
		//cashier should now have one payment
		assertEquals("cashier has one payment", 1, cashier.getBills().size());
		
		cashier.pickAndExecuteAnAction();
		
	
		//cashier should have 35.5 - 5.50
//		System.err.println(cashier.getRegisterAmnt());
		assertEquals("should have 50 dollars in register", 50.0, cashier.getRegisterAmnt());
		
		cashier.pickAndExecuteAnAction();
//	
		//check that bill was paid off
		assertEquals("should have no more bills", 0, cashier.getInventoryBill().size());
//		
		//check that register reflects paying that amount
		assertEquals("should have lower register value", 50-32.94, cashier.getRegisterAmnt());
		
	
		
		//check that market log increased and has correct log
		assertEquals("should have 3 logs", 3, market1.log.size());
		assertTrue("should have received payment", market1.log.containsString("Received msgHereIsAPayment 32.94"));
	
	}
	
	//this test makes sure a bill value is actually computed correctly (instead of arbitrary amount above), and customer pays it fully
	public void testComputeBillAndReceivePayment(){
//		assertTrue("", , );
//		assertEquals("", , );
		System.out.println("Testing that a bill is computed correctly that a custoemr can pay fully as well as that change is computed correctly");
		
		// Set restaurant money amount
		cashier.setRegisterAmnt(300);
		
		// Preconditions: cashier has $300 in the register
		assertEquals("Cashier should have $300.", 300, (int)cashier.getRegisterAmnt());
		

		
		//customer should have no logs
		assertEquals("customer has no logs", 0, customer1.log.size());		
		
		//waiter should have no logs
		assertEquals("waiter has no logs", 0, waiter.log.size());
		
		cashier.msgComputeBill("Steak", customer1, "Doreen", 1, waiter);
		
		//cashier should have one bill
		assertEquals("cashier has one bill now", 1, cashier.getBills().size());
		
		assertTrue(cashier.pickAndExecuteAnAction());

		
		//cashier should have calculated a bill of 15.99
		assertEquals("cashier has bill amount of 15.99", 15.99, cashier.getBills().get(0).getBillAmnt());
		
		//waiter should have 1 log
		assertEquals("waiter have 1 log", 1, waiter.log.size());
		
		//waiter should have received the right msg
		assertTrue("waiter has received checkisready", waiter.log.containsString("Received msgCheckIsReady"));
		
		cashier.msgHereIsAPayment(customer1, 1, 16);
		
		//check that bill value is updated to what customer paid
		assertEquals("cashier updated valCustPaid to 16", 16.0, cashier.getBills().get(0).getCustomerPaid());
		
		//check that bill state updated to paid
		assertEquals("bill is in paid state", CheckState.paid, cashier.getBills().get(0).getState());
		
		//some action was taken
		assertTrue(cashier.pickAndExecuteAnAction());
	
		//check register val is now 300+15.99
		assertEquals("cashier register has been updated", 300+15.99, cashier.getRegisterAmnt());
		
		
		//check that customer received the change
		assertEquals("customer should have 1 log", 1, customer1.log.size());
		
		assertTrue("correct msgHereIsYourReceiptAndChange has been called", customer1.log.containsString("Received msgHereIsYourReceiptAndChange"));
		
	}
	
	//testing non-normative... customer received a bill that they couldn't pay
	//customer keeps bill of what customer couldn't pay
	public void testNonNormativeCustomerAcquiresDebt() {
		
		System.out.println("Testing that a bill is computed correctly that a custoemr can pay fully as well as that change is computed correctly");
		
		// Set restaurant money amount
		cashier.setRegisterAmnt(300);
		
		// Preconditions: cashier has $300 in the register
		assertEquals("Cashier should have $300.", 300, (int)cashier.getRegisterAmnt());
	
		//customer should have no logs
		assertEquals("customer has no logs", 0, customer1.log.size());		
		
		//waiter should have no logs
		assertEquals("waiter has no logs", 0, waiter.log.size());
		
		cashier.msgComputeBill("Steak", customer1, "Doreen", 1, waiter);
		
		//cashier should have one bill
		assertEquals("cashier has one bill now", 1, cashier.getBills().size());
		
		assertTrue(cashier.pickAndExecuteAnAction());

		//cashier should have calculated a bill of 15.99
		assertEquals("cashier has bill amount of 15.99", 15.99, cashier.getBills().get(0).getBillAmnt());
		
		//waiter should have 1 log
		assertEquals("waiter have 1 log", 1, waiter.log.size());
		
		//waiter should have received the right msg
		assertTrue("waiter has received checkisready", waiter.log.containsString("Received msgCheckIsReady"));
		
		cashier.msgHereIsAPayment(customer1, 1, 10);
		
		//check that bill value is updated to what customer paid
		assertEquals("cashier updated valCustPaid to 10", 10.0, cashier.getBills().get(0).getCustomerPaid());
		
		//check that bill state updated to paid
		assertEquals("bill is in paid state", CheckState.paid, cashier.getBills().get(0).getState());
		
		//some action was taken
		assertTrue(cashier.pickAndExecuteAnAction());

		//check register val is now 300+15.99
		assertEquals("cashier register has been updated", 300+10.0, cashier.getRegisterAmnt());
		
		
		//check that customer received the change
		assertEquals("customer should have 1 log", 1, customer1.log.size());
		
		assertTrue("correct msgHereIsYourReceiptAndChange has been called", customer1.log.containsString("Received msgHereIsYourReceiptAndChange"));
	
		//because customer recognized that, debt variable updated
		customer1.setDebt(5.99);
		
		//in my implementation, customer adds on debt value to another order. because that entire implementation works, lets
		//just cut to the chase...
		
		//say the customer comes back, sits at table 2, and orders a salad.. bill is 5.99..debt is 5.99
		cashier.msgHereIsAPayment(customer1, 2, 11.98);
	
	}
	
	//test: receive customer payment and market inventory bill, process customer bill then pay out market bill
	public void testCustomerPaymentAndMarketBill() {
		DecimalFormat df = new DecimalFormat("###.##");
		System.out.println("Testing that a bill is computed correctly that a custoemr can pay fully as well as that change is computed correctly");
		
		// Set restaurant money amount
		cashier.setRegisterAmnt(300);
		market1.setInventory(5);
		customer1.setWallet(20);
		
		/********CUSTOMER PAYMENT COMING IN*****************/
		
		cashier.AddCook(cook);
		// Preconditions: cashier has $300 in the register
		assertEquals("Cashier should have $300.", 300, (int)cashier.getRegisterAmnt());
		
		//cook should have no logs
		assertEquals("customer has no logs", 0, cook.log.size());	
		
		//customer should have no logs
		assertEquals("customer has no logs", 0, customer1.log.size());		
		
		//waiter should have no logs
		assertEquals("waiter has no logs", 0, waiter.log.size());
		
		//market should have no logs
		assertEquals("waiter has no logs", 0, market1.log.size());
		
		cashier.msgComputeBill("Steak", customer1, "Doreen", 1, waiter);
		
		//cashier should have one bill
		assertEquals("cashier has one bill now", 1, cashier.getBills().size());
		
		assertTrue(cashier.pickAndExecuteAnAction());
		
		assertTrue(cashier.getBills().get(0).getBillAmnt()==15.99);
		
		/********CUSTOMER PAYMENT COMING IN*****************/
		
		cashier.msgHereIsAPayment(customer1, 1, 16);
		
		/********MARKET BILL COMING IN*****************/
		
		ArrayList<DFoodOrder> myOrder = new ArrayList<DFoodOrder>();
		myOrder.add(new DFoodOrder("Salad", 5));
		
		market1.msgHereIsAnInventoryOrder(myOrder, 1, cashier);
		
		assertEquals("Market should have 2 logs.", 2, market1.log.size());
		
		//make sure received correct message.
		assertTrue(market1.log.containsString("Received msgHereIsAnInventoryOrder"));
		
		//make sure market can fullfill
		assertTrue(market1.log.containsString("Can fullfill fully"));
		
		//send bill to cashier
		cashier.msgHereIsAnInventoryBill(5.99*5, market1);
		
		//test msg was received..cashier should have one inventory bill in list
		assertEquals("Should have one inventory bill now", 1, cashier.getInventoryBill().size());
		
		//test that bill is from the right market
		assertEquals("Market sending bill doesn't match", market1, cashier.getInventoryBill().get(0).getMarket());
		
		//inventory bill should be 6.50
		assertEquals("Cashier's bill should be correct. Is not.", df.format(5.99*5), df.format(cashier.getInventoryBill().get(0).getAmnt()));
		
		/********MARKET BILL COMING IN*****************/
		
		
		//check that bill value is updated to what customer paid
		assertEquals("cashier updated valCustPaid to 16", 16.0, cashier.getBills().get(0).getCustomerPaid());
		
		//check that bill state updated to paid
		assertEquals("bill is in paid state", CheckState.paid, cashier.getBills().get(0).getState());
		
		//some action was taken bill paid first
		assertTrue(cashier.pickAndExecuteAnAction());

		//check register val is now 300+15.99
		assertEquals("cashier register has been updated", 300+15.99, cashier.getRegisterAmnt());
		
		//check that customer received the change
		assertEquals("customer should have 1 log", 1, customer1.log.size());
		
		assertTrue("correct msgHereIsYourReceiptAndChange has been called", customer1.log.containsString("Received msgHereIsYourReceiptAndChange"));
	
		assertTrue(cashier.getInventoryBill().size()==1);
		assertTrue(cashier.pickAndExecuteAnAction());
		
		/*******CASHIER SHOULD DEAL WITH INVENTORY BILL NEXT**********/
		

		/*******CASHIER SHOULD PROCESS PAYMENT FIRST*************/

		//should still have bill
				assertEquals("should have 1 inventory bills. does.", 1, cashier.getInventoryBill().size());
				
				//should have sent request for approval
				assertEquals("cook should have one log", cook.log.size(), 1);
				assertTrue("cook received correct msg", cook.log.containsString("Received should I accept"));
				//should have right amount now
//				System.out.println(df.format(cashier.getRegisterAmnt()));
				
				//say it returns true
				cashier.msgAnswerVerificationRequest(true);
				
				assertTrue(cashier.pickAndExecuteAnAction());
				
		
		//should have 300+15.99 - 5.99 now
		assertEquals("reg amont not reduced properly", df.format(300+15.99-5.99*5), df.format(cashier.getRegisterAmnt()));
		
		//check if market received bill
			//market has 3 logs now
			assertEquals("market should have 3 logs", 3, market1.log.size());
			//market received payment
			assertTrue("should receive payment msg", market1.log.containsString("Received msgHereIsAPayment"));
		
		/*******CASHIER SHOULD DEAL WITH INVENTORY BILL NEXT**********/
		
		
		
	}
}
