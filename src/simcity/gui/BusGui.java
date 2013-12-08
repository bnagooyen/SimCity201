package simcity.gui;


import simcity.PersonAgent;
import simcity.Transportation.BusAgent;

import java.awt.*;
import java.util.HashMap;

public class BusGui implements Gui {

    private BusAgent agent = null;
    private boolean isPresent = false;
    
    
    SimCityGui gui;
    
    public static final int x_Offset = 100;
    private int xPos = 20, yPos = 20;//default waiter position
    private int xDestination = 20, yDestination = 20;//default start position
   // private int xFoodDestination, yFoodDestination;
    private boolean cookedLabelVisible=false;
    private boolean foodIsFollowingWaiter=false;
    private boolean madeToCashier=false;
    private boolean tryingToGetToFront=false;
    private boolean madeToFront=true;
    //private String foodReady;

//    static List<CookLabel> foodz = Collections.synchronizedList(new ArrayList<CookLabel>());
    

    //private int hangout_x = 50, hangout_y=50;
    
    public static final int streetWidth = 30;
    public static final int sidewalkWidth = 20;
    public static final int housingWidth=30;
    public static final int housingLength=35;
    public static final int parkingGap = 22;
    public static final int yardSpace=11;
    
    HashMap<String, Point> myMap = new HashMap<String, Point>();
    
    //int numPlating=1;
    
    enum Command {none, GoToRestaurant, GoHome, GoToBusStop, GoToNextStop};
    Command command= Command.none;
    
    //public String[] foodReady= new String[nTABLES];
    //public boolean[] labelIsShowing = new boolean[nTABLES];
    private boolean labelIsShowing=false;
    String foodReady;

   // private int seatingAt;
   
    
    private int seatingAt_x, seatingAt_y;
    
    private int tablegoingto_x, tablegoingto_y;
    
   //f private void setSeatingAt(int t) { seatingAt=t; }
    
    public BusGui(BusAgent agent, SimCityGui g) {
            gui=g;
        this.agent = agent;
        madeToFront=true;
//        for(int i=0; i<labelIsShowing.length;i++)
//                labelIsShowing[i]=false;
        
        //coordinates are from citypanel, find the building you want to ppl to go to and copy/paste coordinates to this map
        
       
        myMap.put("Stop1", new Point(75,15));
        myMap.put("Stop2", new Point(485,15));
        myMap.put("Stop3", new Point(485,360));
        myMap.put("Stop4", new Point(75,360));
        
        
        
    
     /*   String personAddress=agent.homeAddress;
        if(personAddress.contains("Apartment")) {
//                System.err.println("need to truncate");
                personAddress=personAddress.substring(0, personAddress.length()-1);
                        //System.err.println(personAddress);
        }
        xPos = myMap.get(personAddress).x;
        yPos = myMap.get(personAddress).y;*/
        
        xDestination=xPos;
        yDestination=yPos;
    }

    @Override
        public void updatePosition() {
            //System.out.println("x pos: "+ xPos + " // y pos: "+ yPos+" // xDestination: " + xDestination + " // yDestination: " + yDestination);
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        else if (xPos == xDestination && yPos == yDestination)
        {
                        
                        
                         if(command==Command.GoToBusStop) {
                                 if(agent.currentStop=="Stop1"){
                                agent.msgAtStop("Stop2");
                                
                                 }
                                 else if(agent.currentStop=="Stop2"){
                                     agent.msgAtStop("Stop3");
                                     
                                 }
                                 else if(agent.currentStop=="Stop3"){
                                     agent.msgAtStop("Stop4");
                                     
                                 }
                                 else if(agent.currentStop=="Stop4"){
                                     agent.msgAtStop("Stop1");
                                     
                                 }
                        }
                
                        command=Command.none;
                        
        }

        
    }

    @Override
        public void draw(Graphics2D g) {
        g.setColor(Color.yellow);
        g.fillRect(xPos, yPos, 15, 15);
 
//        if(labelIsShowing) {
//                g.setColor(Color.BLACK);
//                g.drawString(foodReady.substring(0,2),xFood, yFood);
//                
//                }
//       
        
        
    }

    @Override
        public boolean isPresent() {
        return true;
    }
    
  
    public void setPresent(boolean p) {
                isPresent = p;
        }
    
        
    
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public void DoGoTo(String destination) {
            if(destination.contains("Restaurant")) {
                    Point myDest = myMap.get(destination);
                    xDestination = myDest.x;
                    yDestination = myDest.y;
                    command=Command.GoToRestaurant;
            }
            
            if(destination.contains("Stop")) {
                    Point myDest = myMap.get(destination);
                    xDestination = myDest.x;
                    yDestination = myDest.y;
                    command=Command.GoToBusStop;
            }
            
            if(destination.contains("House") || destination.contains("Apartment")) {
                    if(destination.contains("Apartment")) {
                            destination=destination.substring(0, destination.length()-1);
                            System.err.println(destination);
                    }
                    Point myDest = myMap.get(destination);
                    xDestination = myDest.x;
                    yDestination = myDest.y;
                    command=Command.GoHome;
            }
    }
    
//   static class CookLabel {
//            String food;
//            int xPos, yPos;
//            boolean isFollowing;
//            enum LabelState {ingredient, cooking, cooked, plating, plated};
//            LabelState state;
//            CookLabel(String f, int x, int y) {
////                    System.err.println(f);
//                    food=f;
//                    xPos=x;
//                    yPos=y;
//                    isFollowing=true;
//                    state=LabelState.ingredient;
////                    System.err.println("added");
//            }
//    }
}