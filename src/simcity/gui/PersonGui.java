package simcity.gui;

import simcity.PersonAgent;

import java.awt.*;
import java.util.HashMap;

import simcity.DRestaurant.DCustomerRole;

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
    private int xPos = 0, yPos = 0;//default waiter position
    private int xDestination = 0, yDestination = 0;//default start position
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
    
    enum Command {none, GoToRestaurant, GoHome, other};
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
        
        //coordinates are from citypanel, find the building you want to ppl to go to and copy/paste coordinates to this map
        myMap.put("Restaurant 1", new Point(2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+sidewalkWidth));
        myMap.put("Restaurant 2", new Point(2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap));
        myMap.put("Restaurant 3", new Point(2*yardSpace+3*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+sidewalkWidth));
        myMap.put("Restaurant 4", new Point(3*yardSpace+4*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+sidewalkWidth));
        myMap.put("Restaurant 5", new Point(3*yardSpace+4*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap));
        myMap.put("Restaurant 6", new Point(3*yardSpace+5*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap));
        
        myMap.put("House 1", new Point(yardSpace, streetWidth+sidewalkWidth));
        myMap.put("House 2", new Point(yardSpace, streetWidth+sidewalkWidth+2*housingLength+ 2*parkingGap));
        myMap.put("House 3", new Point(yardSpace, streetWidth+sidewalkWidth+4*housingLength+ 5*parkingGap));
        myMap.put("House 4", new Point(yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+housingLength+ sidewalkWidth + parkingGap));
        myMap.put("House 5", new Point(yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap));
        myMap.put("House 6", new Point(2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap));
        myMap.put("House 7", new Point(2*yardSpace+3*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+housingLength+ sidewalkWidth+ parkingGap));
        myMap.put("House 8", new Point(2*yardSpace+3*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap));
        myMap.put("House 9", new Point(3*yardSpace+4*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap));
        myMap.put("House 10", new Point(3*yardSpace+5*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+housingLength+ sidewalkWidth+ parkingGap));
        myMap.put("House 11", new Point(3*yardSpace+5*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap));
        myMap.put("House 12", new Point(4*yardSpace+6*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+housingLength+ sidewalkWidth+ parkingGap));
        myMap.put("House 13", new Point(4*yardSpace+6*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap));
        myMap.put("House 14", new Point(4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+housingLength+ sidewalkWidth+ parkingGap));
        myMap.put("House 15", new Point(4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap));
        myMap.put("Apartment 1", new Point(yardSpace, streetWidth+sidewalkWidth+housingLength+ parkingGap));
        myMap.put("Apartment 2", new Point(yardSpace, streetWidth+sidewalkWidth+3*housingLength+ 3*parkingGap));
        myMap.put("Apartment 3", new Point(yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap));
        myMap.put("Apartment 4", new Point(2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+housingLength + sidewalkWidth+ parkingGap));
        myMap.put("Apartment 5", new Point(2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap));
        myMap.put("Apartment 6", new Point(2*yardSpace+3*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap));
        myMap.put("Apartment 7", new Point(3*yardSpace+4*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+housingLength+ sidewalkWidth+ parkingGap));
        myMap.put("Apartment 8", new Point(3*yardSpace+4*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap));
        myMap.put("Apartment 9", new Point(3*yardSpace+5*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap));
        myMap.put("Apartment 10", new Point(4*yardSpace+6*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap));
        myMap.put("Apartment 11", new Point(4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+sidewalkWidth));
        myMap.put("Apartment 12", new Point(4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap));
        myMap.put("Homeless Shelter", new Point(4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap));
        myMap.put("Bank 1", new Point(yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+sidewalkWidth+4*housingLength+ 5*parkingGap));
    

    
        String personAddress=agent.homeAddress;
        if(personAddress.contains("Apartment")) {
//        	System.err.println("need to truncate");
        	personAddress=personAddress.substring(0, personAddress.length()-1);
			//System.err.println(personAddress);
        }
        
        if (personAddress.equals("House 1") || personAddress.equals("House 2") || personAddress.equals("Apartment 1") 
        		|| personAddress.equals("House 3") || personAddress.equals("Apartment 2") ) {
        	xPos = myMap.get(personAddress).x + 30;
        	yPos = myMap.get(personAddress).y;
        }
        else if (personAddress.equals("Market 1") || personAddress.equals("House 4") || personAddress.equals("Apartment 3") 
        		|| personAddress.equals("House 5") || personAddress.equals("Bank 1") ) {
        	xPos = myMap.get(personAddress).x - 10;
        	yPos = myMap.get(personAddress).y;
        }
        else if (personAddress.equals("Restaurant 1") || personAddress.equals("Aparment 4") || personAddress.equals("Apartment 5") 
        		|| personAddress.equals("House 6") || personAddress.equals("Restaurant 2") ) {
        	xPos = myMap.get(personAddress).x + 30;
        	yPos = myMap.get(personAddress).y;
        }
        else if (personAddress.equals("Restaurant 3") || personAddress.equals("House 7") || personAddress.equals("Apartment 6") 
        		|| personAddress.equals("House 8") || personAddress.equals("Market 2") ) {
        	xPos = myMap.get(personAddress).x - 10;
        	yPos = myMap.get(personAddress).y;
        }
        else if (personAddress.equals("Restaurant 4") || personAddress.equals("Apartment 7") || personAddress.equals("House 9") 
        		|| personAddress.equals("Apartment 8") || personAddress.equals("Restaurant 5") ) {
        	xPos = myMap.get(personAddress).x + 30;
        	yPos = myMap.get(personAddress).y;
        }
        
        else if (personAddress.equals("Market 3") || personAddress.equals("House 10") || personAddress.equals("Apartment 9") 
        		|| personAddress.equals("House 11") || personAddress.equals("Restaurant 6") ) {
        	xPos = myMap.get(personAddress).x - 10;
        	yPos = myMap.get(personAddress).y;
        }
        
        else if (personAddress.equals("Bank 2") || personAddress.equals("House 12") || personAddress.equals("Apartment 10") 
        		|| personAddress.equals("House 13") || personAddress.equals("Market 4") ) {
        	xPos = myMap.get(personAddress).x + 30;
        	yPos = myMap.get(personAddress).y;
        }
        
        else if (personAddress.equals("Apartment 11") || personAddress.equals("House 14") || personAddress.equals("Apartment 12") 
        		|| personAddress.equals("House 15") || personAddress.equals("Homeless Shelter") ) {
        	xPos = myMap.get(personAddress).x - 10;
        	yPos = myMap.get(personAddress).y;
        }
        
        xDestination=xPos;
        yDestination=yPos;
    }

    @Override
	public void updatePosition() {
    	//System.out.println("x pos: "+ xPos + " // y pos: "+ yPos+" // xDestination: " + xDestination + " // yDestination: " + yDestination);
    	
    	/**
    	if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;
		*/
    	if (xPos != xDestination && yPos != yDestination) {
            if (yPos == 30 || yPos == 335) {
            	if (xPos < xDestination)
                    xPos++;
                else if (xPos > xDestination)
                    xPos--;
            }
            else if (335 - yPos < 0 ) 
		            yPos++;
		    else 
		            yPos--;
    	}
    	if (xPos == xDestination && yPos != yDestination) {
            if (yPos < yDestination)
                    yPos++;
            else if (yPos > yDestination)
                    yPos--;
    	}
    	
    	
        if (xPos == xDestination && yPos == yDestination)
        {
        		if(command==Command.GoToRestaurant ||command==Command.GoHome||command==Command.other) {
        			agent.msgAnimationArivedAtRestaurant();
        			System.out.println("msgArrivedat");
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
    	
    	if(destination.contains("Restaurant")) {
    		Point myDest = myMap.get(destination);
    		xDestination = myDest.x;
    		yDestination = myDest.y;
    		command=Command.GoToRestaurant;
    	}
    	
    	if(destination.contains("Bank")) {
    		Point myDest = myMap.get(destination);
    		xDestination = myDest.x;
    		yDestination = myDest.y;
    		command=Command.GoToRestaurant;
    	}
    	
    	if(destination.contains("House") || destination.contains("Apartment")) {
    		if(destination.contains("Apartment")) {
    			destination=destination.substring(0, destination.length()-1);
    			//System.err.println(destination);
    		}
    		Point myDest = myMap.get(destination);
    		xDestination = myDest.x;
    		yDestination = myDest.y;
    		command=Command.GoHome;
    	}
    	else {
    		Point myDest = myMap.get(destination);
    		xDestination = myDest.x;
    		yDestination = myDest.y;
    		command=Command.other;
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
