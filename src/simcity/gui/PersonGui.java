package simcity.gui;

import simcity.DCustomerRole;
import simcity.PersonAgent;

import java.awt.*;
import java.util.HashMap;

public class PersonGui implements Gui {

    private PersonAgent agent = null;

    public boolean waiterAtFront()
    {
    	if(xPos==-20 && yPos==-20) return true;
    	else return false;
    }
    private boolean isPresent = false;
    private boolean isHungry = false;
    SimCityGui gui;
    private int tableGoingTo;
    public static final int x_Offset = 100;
    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position
   // private int xFoodDestination, yFoodDestination;
    private boolean cookedLabelVisible=false;
    private boolean foodIsFollowingWaiter=false;
    private boolean madeToCashier=false;
    private boolean tryingToGetToFront=false;
    private boolean madeToFront=true;
    //private String foodReady;

//    static List<CookLabel> foodz = Collections.synchronizedList(new ArrayList<CookLabel>());
    

    private int hangout_x = 50, hangout_y=50;
    
    public static final int streetWidth = 30;
    public static final int sidewalkWidth = 20;
    public static final int housingWidth=30;
    public static final int housingLength=35;
    public static final int parkingGap = 22;
    public static final int yardSpace=11;
    
    HashMap<String, Point> myMap = new HashMap<String, Point>();
    
    //int numPlating=1;
    
    enum Command {none, GoToRestaurant, GoHome};
    Command command= Command.none;
    
    //public String[] foodReady= new String[nTABLES];
    //public boolean[] labelIsShowing = new boolean[nTABLES];
    private boolean labelIsShowing=false;
    String foodReady;

   // private int seatingAt;
    private DCustomerRole takingOrderFrom;//, orderFrom;
    
    private int seatingAt_x, seatingAt_y;
    
    private int tablegoingto_x, tablegoingto_y;
    
   //f private void setSeatingAt(int t) { seatingAt=t; }
    
    public PersonGui(PersonAgent agent, SimCityGui g) {
    	gui=g;
        this.agent = agent;
        madeToFront=true;
//        for(int i=0; i<labelIsShowing.length;i++)
//        	labelIsShowing[i]=false;
        
        xPos = hangout_x;
        yPos = hangout_y;
        
        xDestination=xPos;
        yDestination=yPos;
        
        myMap.put("Restaurant 3", new Point(2*yardSpace+3*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+sidewalkWidth));
        myMap.put("House 1", new Point(yardSpace, streetWidth+sidewalkWidth));
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
        		if(command==Command.GoToRestaurant) {
        			agent.msgAnimationArivedAtRestaurant();
        		}
        	
        		command=Command.none;
        		
        }

        
    }

    @Override
	public void draw(Graphics2D g) {
        g.setColor(Color.magenta);
        g.fillRect(xPos, yPos, 10, 10);
 
//        if(labelIsShowing) {
//        	g.setColor(Color.BLACK);
//        	g.drawString(foodReady.substring(0,2),xFood, yFood);
//        	
//        	}
//       
        
        
    }

    @Override
	public boolean isPresent() {
        return true;
    }
    
  
    public void setHungry() {
		isHungry = true;
//		agent.gotHungry();
		setPresent(true);
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
    	if(destination.equals("Restaurant 3")) {
    		Point myDest = myMap.get(destination);
    		xDestination = myDest.x;
    		yDestination = myDest.y;
    		command=Command.GoToRestaurant;
    	}
    	
    	if(destination.equals("House 1")) {
    		Point myDest = myMap.get(destination);
    		xDestination = myDest.x;
    		yDestination = myDest.y;
    		command=Command.GoHome;
    	}
    }
    
//   static class CookLabel {
//    	String food;
//    	int xPos, yPos;
//    	boolean isFollowing;
//    	enum LabelState {ingredient, cooking, cooked, plating, plated};
//    	LabelState state;
//    	CookLabel(String f, int x, int y) {
////    		System.err.println(f);
//    		food=f;
//    		xPos=x;
//    		yPos=y;
//    		isFollowing=true;
//    		state=LabelState.ingredient;
////    		System.err.println("added");
//    	}
//    }
}
