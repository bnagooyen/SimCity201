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
import simcity.mockrole.MockRoleBankTeller;
import simcity.mockrole.MockRoleCar;
import simcity.mockrole.MockRoleMarketCashier;
import simcity.test.mock.*;
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
	MockRoleCar car;
	MockRoleBankTeller teller;
	
	

	
	public void setUp() throws Exception{
		person=new PersonAgent("person");
		
		bus=new MockBus("bus");
		busStop=new MockBusStop("mock busStop");
		person.SetJob(role);
		car=new MockRoleCar("car");
		

	}
	
	public void testnormativePersonToBank(){
		teller=new MockRoleBankTeller("teller");
		person.SetJob(teller);
		person.myCar=car;
		person.msgTimeUpdate(6);
		
		assertEquals("Persons Job should have been BankTeller", person.myJob,teller);
		assertEquals("Persons Job location should be the bank", person.jobLocation,"bank");
		assertEquals("Should not have a destination", person.mydestination,null);
		assertEquals("Starts with $200", person.money,200.0);
		assertEquals("Person's locationState should be atHome", LocationState.atHome,person.locationState);
		assertTrue("Person's energy state should be awake", person.energyState==EnergyState.awake);
		
		assertTrue("Deposit should be called by scheduler", person.pickAndExecuteAnAction());
		assertFalse("There shouldnt be any activated roles", person.activatedRole);
		
		assertEquals("Person's locationState should be inTransit", LocationState.inTransit,person.locationState);

		
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
		
		assertTrue("Person's locationState should be atHome", person.locationState==LocationState.atHome);
		
		
		
		//assertTrue("Person's scheduler should run transit stuff", person.pickAndExecuteAnAction());
	}
	
	public void testnormativePersonToWork(){
		MockRoleMarketCashier marketCashier=new MockRoleMarketCashier("Mcashier");
		marketCashier.myPerson=person;
		person.SetJob(marketCashier);
		person.SetJob(marketCashier);
		person.myCar=car;
		
		assertFalse("NeedToGoToWork should be true (7AM time update Called)", person.needToGoToWork);
		
		person.msgTimeUpdate(6);
		person.msgTimeUpdate(10);
		
		assertEquals("Persons Job should have been BankTeller", person.myJob,marketCashier);
		assertEquals("Persons Job location should be the bank", person.jobLocation,"market");
		assertEquals("Should not have a destination", person.mydestination,null);
		assertEquals("Starts with $200", person.money,200.0);
		assertEquals("Person's locationState should be atHome", LocationState.atHome,person.locationState);
		assertTrue("Person's energy state should be awake", person.energyState==EnergyState.awake);
		assertTrue("NeedToGoToWork should be true (7AM time update Called)", person.needToGoToWork);
		
		assertTrue("Go to work should be called by scheduler", person.pickAndExecuteAnAction());
		assertFalse("There shouldnt be any activated roles", person.activatedRole);
		
		assertEquals("Person's locationState should be inTransit", LocationState.inTransit,person.locationState);

		
		assertEquals("Destination should be bank", person.mydestination,"market");
		assertTrue("Person's locationState should be inTransit", person.locationState==LocationState.inTransit);
		assertTrue("Person's locationState should be inTransit", person.locationState==LocationState.inTransit);
		
		assertTrue("Person's scheduler should run transit stuff", person.pickAndExecuteAnAction());
		
		assertEquals("Car should have received message from person, but it did not", car.log.size(),1);
		
		person.msgAtDestination("market");
		
		assertEquals("My destination should be updated", "market", person.mydestination);
		assertEquals("My location should be updated", "market", person.mylocation);
		assertTrue("Person should have 1 role, job", person.roles.size()==2);
		assertTrue("Person's transit should be getOutCar", person.transitState==TransitState.getOutCar);
		
		assertTrue("Person's scheduler should run transit stuff", person.pickAndExecuteAnAction());
		
		assertTrue("Person's transit should be getOutCar", person.transitState==TransitState.atDestination);
		assertTrue("No new role added (Already has Job)", person.roles.size()==2);
		assertTrue("MarketCashier should be active", person.roles.get(1).isActive);
		

		assertFalse("Transit should be called by scheduler", person.pickAndExecuteAnAction());
		assertTrue("There shouldnt be any activated roles", person.activatedRole);
		
		assertTrue("Person's locationState should be atHome", person.locationState==LocationState.atHome);
		
		person.roles.get(1).isActive=false;
		
		assertTrue("Deposit should be called by scheduler", person.pickAndExecuteAnAction());
		
		assertEquals("Should not have a destination", person.mydestination,"bank");
		assertEquals("Starts with $200", person.money,200.0);
		assertEquals("Person's locationState should be atHome", LocationState.inTransit,person.locationState);
		assertTrue("Person's energy state should be awake", person.energyState==EnergyState.awake);
		
		assertFalse("There shouldnt be any activated roles", person.activatedRole);
		
		
		assertTrue("Deposit should be called by scheduler", person.pickAndExecuteAnAction());
	}

	public void testPersonBusTransit(){
		
		//preconditions
		assertTrue("In order to use bus, person must have no car, but they do. ", person.myCar==null);
		assertEquals("Bus Stop should not have received message from person yet that it is at the station, but it had", busStop.log.size(),0);
		assertEquals("Bus should not have received any messages from Bus, but it has", bus.log.size(),0);
		
		
		person.msgTimeUpdate(6);
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
		//assertTrue("Person's transitState should be walkingToDestination", person.transitState==TransitState.walkingtoDestination);
		
		
		
		
		
		

	}

	public void testCarInteraction(){
		car.myPerson = person;
		person.myCar=car;
		teller = new MockRoleBankTeller("teller");
		teller.myPerson= person;
		person.SetJob(teller);
		person.myJob.isActive = true;
		
		System.out.println("JOB "+person.myJob);
		System.out.println("Job's active "+person.myJob.isActive);
		
		assertEquals("Car should have no messages sent to it, but it has messages in log", car.log.size(), 0);
		
		person.msgTimeUpdate(6);
		person.msgTimeUpdate(10);
		person.pickAndExecuteAnAction();
		
		assertTrue("Person's locationState should be inTransit", person.locationState==LocationState.inTransit);
		assertTrue("Person's energy state should be awake", person.energyState==EnergyState.awake);
		
		//person.transitState=TransitState.goToCar;
		assertTrue("Pick and exectuct should return true",person.pickAndExecuteAnAction());
		assertEquals("Transist State Should be just Leaving", TransitState.justLeaving,person.transitState);
		
		assertEquals("Car should have received message from person, but it did not", 1, car.log.size());
		
		person.msgAtDestination("destination");
		
		assertTrue("Person's state should be to get out the car, but it is not", person.transitState==TransitState.getOutCar);
		assertTrue("Person's locationState should be inTransit "+person.locationState, person.locationState==LocationState.inTransit);
		assertTrue("Person's locationState should be inTransit", person.energyState!=EnergyState.asleep);

		person.pickAndExecuteAnAction();
		assertTrue("Person's scheduler should have returned true, but didn't.", person.pickAndExecuteAnAction());

		assertTrue("Person's state should be at destination", person.transitState==TransitState.atDestination);
		
		
		
	}

}



