package simcity.test;

import java.util.ArrayList;
import java.util.List;

import simcity.PersonAgent;
import simcity.Market.InventoryBoyRole;
import simcity.Market.MFoodOrder;
import simcity.Market.MOrder;
import simcity.housing.LandlordRole;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.Person;
import simcity.mockrole.MockRole;
import simcity.test.mock.MockBus;
import simcity.test.mock.MockBusStop;
import simcity.test.mock.MockCar;
import simcity.test.mock.MockMarketCashier;
import simcity.test.mock.MockMarketCustomer;
import simcity.test.mock.*;
import simcity.test.mock.MockRepairMan;
import simcity.Market.MarketCashierRole.orderState;
import simcity.PersonAgent.EnergyState;
import simcity.PersonAgent.LocationState;
import simcity.PersonAgent.TransitState;
import simcity.Transportation.BusAgent;
import simcity.Transportation.BusAgent.busState;
import simcity.Transportation.BusStopAgent;
import simcity.Transportation.CarAgent;
import simcity.Transportation.CarAgent.carState;
import junit.framework.TestCase;

public class PersonTest extends TestCase{
	PersonAgent person;
	MockRole role;
	MockBus bus;
	MockBusStop busStop;
	MockCar car;
	
	
	

	
	public void setUp() throws Exception{
		role=new MockRole("mockrole", person);
		person=new PersonAgent("person");
		bus=new MockBus("bus");
		busStop=new MockBusStop("mock busStop");
		person.SetJob(role);
		car=new MockCar("car");
		

	}
	
	public void testnormativePersonTest(){
		MockBankTeller bankTeller=new MockBankTeller("teller",person);
		person.SetJob(bankTeller);
		person.myCar=car;
		person.msgTimeUpdate(7);
		
		assertEquals("Persons Job should have been BankTeller", person.myJob,bankTeller);
		assertEquals("Persons Job location should be the bank", person.jobLocation,"bank");
		assertEquals("Should not have a destination", person.mydestination,null);
		assertEquals("Starts with $200", person.money,200.0);
		assertEquals("Person's locationState should be atHome", LocationState.atHome,person.locationState);
		assertTrue("Person's energy state should be awake", person.energyState==EnergyState.awake);
		
		assertTrue("Deposit should be called by scheduler", person.pickAndExecuteAnAction());
		assertFalse("There shouldnt be any activated roles", person.activatedRole);
		
		assertEquals("Person's locationState should be atHome", LocationState.inTransit,person.locationState);

		
		assertEquals("Destination should be bank", person.mydestination,"bank");
		assertTrue("Person's locationState should be inTransit", person.locationState==LocationState.inTransit);
		assertTrue("Person's locationState should be inTransit", person.locationState==LocationState.inTransit);
		
		assertTrue("Person's scheduler should run transit stuff", person.pickAndExecuteAnAction());
		
		assertEquals("Car should have received message from person, but it did not", car.log.size(),1);
		
		person.msgAtDestination("bank");
		
		assertEquals("My destination should be updated", "bank", person.mydestination);
		assertEquals("My location should be updated", "bank", person.mylocation);
		assertTrue("Person should have 1 role, job", person.roles.size()==2);
		assertTrue("Person's transit should be getOutCar", person.transitState==TransitState.getOutCar);
		
		assertTrue("Person's scheduler should run transit stuff", person.pickAndExecuteAnAction());
		
		assertTrue("Person's transit should be getOutCar", person.transitState==TransitState.atDestination);
		assertTrue("A second role (BankCustomer) should be added to roles", person.roles.size()==3);
		assertTrue("BankCustomer should be active", person.roles.get(2).isActive);
		assertEquals("BankCustomer purpose should be deposit", person.roles.get(2).purpose,"deposit");
		
		
		
		//assertTrue("Person's scheduler should run transit stuff", person.pickAndExecuteAnAction());
	}

	public void testPersonBusTransit(){
		
		//preconditions
		assertTrue("In order to use bus, person must have no car, but they do. ", person.myCar==null);
		assertEquals("Bus Stop should not have received message from person yet that it is at the station, but it had", busStop.log.size(),0);
		assertEquals("Bus should not have received any messages from Bus, but it has", bus.log.size(),0);
		
		
		person.msgTimeUpdate(7);
		person.msgTimeUpdate(10);
		//person.transitState=TransitState.walkingToBus;
		
		
		assertTrue("Person's pickAndExecuteAnAction should be active because he is awake and in transit", person.pickAndExecuteAnAction());
		assertTrue("Person should have no car and thus their myCar should be null", person.myCar==null);
		assertTrue("Person's energy state should be awake", person.energyState==EnergyState.awake);
		assertTrue("Person's transitState should be walking to bus", person.transitState==TransitState.justLeaving);
		assertTrue("Person's locationState should be inTransit", person.locationState==LocationState.inTransit);
		
		person.pickAndExecuteAnAction();
		assertTrue("Person's transit State should be set to tell the bus", person.transitState==TransitState.atBusStop);
		
		person.setBus(bus);
		person.msgBusIsHere(bus);
		
		
		assertTrue("Person's transitState should be get on bus, but it isnt", person.transitState==TransitState.getOnBus);
		person.pickAndExecuteAnAction();
		assertEquals("Bus should have a message in its log after person gets on, but it does not", bus.log.size(), 1);
		
		person.msgAtStop("destination");
		
		assertTrue("Person's transitState should be get off bus but it is not", person.transitState==TransitState.getOffBus);
		person.pickAndExecuteAnAction();
		assertTrue("Person's transitState should be walkingToDestination", person.transitState==TransitState.walkingtoDestination);
		
		
		
		
		
		

	}

	public void testCarInteraction(){
		person.myCar=car;
		assertEquals("Car should have no messages sent to it, but it has messages in log", car.log.size(), 0);
		
		person.msgTimeUpdate(7);
		person.msgTimeUpdate(10);
		person.pickAndExecuteAnAction();
		
		assertTrue("Person's locationState should be inTransit", person.locationState==LocationState.inTransit);
		assertTrue("Person's energy state should be awake", person.energyState==EnergyState.awake);
		
		//person.transitState=TransitState.goToCar;
		person.pickAndExecuteAnAction();
		
		assertEquals("Car should have received message from person, but it did not", car.log.size(),1);
		
		person.msgAtDestination("destination");
		
		assertTrue("Person's state should be to get out the car, but it is not", person.transitState==TransitState.getOutCar);
		assertTrue("Person's locationState should be inTransit", person.locationState==LocationState.inTransit);
		assertTrue("Person's locationState should be inTransit", person.energyState!=EnergyState.asleep);

		//person.pickAndExecuteAnAction();
		assertTrue("Person's scheduler should have returned true, but didn't.", person.pickAndExecuteAnAction());

		assertTrue("Person's state should be at destination", person.transitState==TransitState.atDestination);
		
		
		
	}

}

