package simcity.Transportation;
import agent.Agent;
import simcity.PersonAgent;
import simcity.Transportation.*;
import simcity.gui.BusGui;
import simcity.gui.CarGui;
import simcity.interfaces.Car;
import simcity.interfaces.Person;

import java.util.*;
import java.util.concurrent.Semaphore;


public class CarAgent extends Agent implements Car {
        
        public Person driver=null;
        public String destination;
        public int xStart, yStart;
        Semaphore atDestination= new Semaphore(0, true);
        
        public enum carState
        {parked, receivedLocation, travelling, arrived};
        
        public carState state;
        public CarGui cgui;
        
        
        
        
        
public boolean pickAndExecuteAnAction(){
                
                if(state==carState.receivedLocation){
                        goToLocation();
                        
                        
                        return true;
                }
                
                if(state==carState.arrived){
                        HaveArrived();
                        
                        return true;
                }
                
                return false;
        }

//MESSAGES
        public void msgGoToDestination(String location, Person person){
                
                driver=person;
                destination=location;
                state=carState.receivedLocation;
                stateChanged();
                
                
        }
        
        public void msgAtDestination(){
                
                
                state=carState.arrived;
                stateChanged();
        }
        
        //SCHEDULER
        
        
        
        
        //ACTIONS
        
        private void goToLocation(){
                state=carState.travelling;
                DoGoTo(destination);
                
                cgui.setPresent(true);
                System.out.println("we goin fam");
                
        }
        
        private void HaveArrived(){
                state=carState.parked;
                Do("Arrived at destination");
                cgui.setPresent(false);
                driver.msgAtDestination(destination);
                
                
        }
        
        //utilities
        
        public void setGui(CarGui gui){
                cgui=gui;
        }

        public CarGui getGui() {
                return cgui;
        }

        private void DoGoTo(String dest) {
                cgui.DoGoTo(dest);
        }

}