package simcity.Transportation;


import simcity.PersonAgent;

import java.util.*;

import agent.Agent;
import simcity.Transportation.BusAgent;
import simcity.interfaces.Bus;
import simcity.interfaces.BusStop;
import simcity.interfaces.Person;


public class BusStopAgent extends Agent implements BusStop{
        
        public List<Bus> buses=new ArrayList<Bus>();
        public List<PersonAgent> waitingPassengers=new ArrayList<PersonAgent>();
        public String myStop;
        
        
        //Messages
        
        public void msgWaitingForBus(PersonAgent p){
                waitingPassengers.add(p);
                System.out.println(waitingPassengers.size());
                stateChanged();
        }
        
        public void msgAnyPassengers(Bus b){
              System.out.println("any passengers?");
                buses.add(b);
                stateChanged();
        }
        
        
        //SCHEDULER
        
        public boolean pickAndExecuteAnAction(){
        
                if(buses.size()>0){
                        givePassengerList(buses.get(0));
                       
                        return true;
                }
                
                return false;
        }
        
        
        //ACTIONS
        
        private void givePassengerList(Bus bus){
                bus.msgHereArePassengers(waitingPassengers);
                
                waitingPassengers.clear();
                buses.remove(bus);
        }
        
        
}