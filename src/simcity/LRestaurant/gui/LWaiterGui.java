package simcity.LRestaurant.gui;


import simcity.LRestaurant.LCustomerRole;
import simcity.LRestaurant.LWaiterRole;
//import restaurant.WaiterAgent.WaiterState;




import simcity.gui.Gui;

import java.awt.*;

public class LWaiterGui implements Gui {

    private LWaiterRole agent = null;
    private LCookGui cookGui = null;
    private boolean hasFood = false;
    private String food;
    private String name;
//    LRestaurantGui gui;
    
    public enum commandState{noCommand, moving};
    private commandState command =  commandState.noCommand;

//    private int xPos = -20, yPos = -20;//default waiter position
//    private int xDestination = -20, yDestination = -20;//default start position
//    private int xDpos = -20, yDpos = -20;
//    private int xGCpos = -21, yGCpos = -21;
    
    private int xPos = 35, yPos = 20;//default waiter position
    private int xDestination = 35, yDestination = 20;//default start position
    private int xDpos = 35, yDpos = 20;
    private int xGCpos = 35, yGCpos = 20;
    
    private int rectSize = 20;

    public static final int xTable1 = 300;
    public static final int yTable1 = 250;
    
    public static final int xTable2 = 100;
    public static final int yTable2 = 200;
    
    public static final int xTable3 = 350;
    public static final int yTable3 = 100;
    
    public static final int xTable4 = 170;
    public static final int yTable4 = 60;
    
    public static final int xCook = 75;
    public static final int yCook = 310;
    
    public static final int xCookP = 125;
    public static final int yCookP = 290;
    
    public static final int xCashier = -400;
    public static final int yCashier = 700;
    
    private int tableNum;

    public LWaiterGui(LWaiterRole agent, String name){ //, LRestaurantGui gui) {
        this.agent = agent;
        this.name = name;
        //this.gui = gui;
    }
    
    public void setCookGui(LCookGui c){
    	cookGui = c;
    }
    
    public void setBreak(){
    	agent.setOnBreak();
    }
    
    public void setWorking(){
    	//gui.setWaiterEnabled(agent);
    }
    
    public void setFood(String f){
    	food = f;
    }
    
    public void gotFood(){
    	hasFood = true;
    	cookGui.takeFood();
    }
    
    public void gaveFood(){
    	hasFood = false;
    }

    public void updatePosition() {
    	
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;
        
        //notify that animation job is done
        if(xPos == xDestination && yPos == yDestination){
    		if(command == commandState.moving){
    			command = commandState.noCommand;
    			agent.msgTask();
    		}
    	}
        
        
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.BLUE);
        g.fillRect(xPos, yPos, rectSize, rectSize);
        g.drawString(name, xPos, yPos-5);//offset so name doesn't overlap with waiter
        if(hasFood){
        	g.drawString(food, xPos-10, yPos+10);//offset so food label doesn't overlap with waiter
        }
    }

    public boolean isPresent() {
        return true;
    }
    
    public void DoGetCustomer() {
        xDestination = xGCpos;
        yDestination = yGCpos;
        command = commandState.moving;
    }
    
    public void DoGoOnBreak() {
    	xDestination = -20;
    	yDestination = -20;
    }
    
    public void DoGoHome() {
    	xDestination = -20;
    	yDestination = -20;
    }
    public void DoGoToTable(int tableNumber) {//animation to table
    	tableNum = tableNumber;
    	
    	if(tableNum == 1){
	    	xDestination = xTable1 + 20; //offset so waiter doesn't overlap with cust
	        yDestination = yTable1 - 20;
    	}
    	
    	else if(tableNum == 2){
	    	xDestination = xTable2 + 20;
	        yDestination = yTable2 - 20;
    	}
    	
    	else if(tableNum == 3){
	    	xDestination = xTable3 + 20;
	        yDestination = yTable3 - 20;
    	}
    	
    	else if(tableNum == 4){
	    	xDestination = xTable4 + 20;
	        yDestination = yTable4 - 20;
    	}
    	
    	command = commandState.moving;
    	
    }

    public void DoWait(int num){
    	yDpos = yDpos*(num+1)+(num*20);
		xDestination = xDpos;
		yDestination = yDpos;
	}
    
    public void DoLeaveCustomer() {//animation to home
        xDestination = xDpos;
        yDestination = yDpos;
        command = commandState.moving;
    }
    
    public void DoGoToPlating(){
    	xDestination = xCookP;
    	yDestination = yCookP;
    	command = commandState.moving;
    }
    
    public void DoGoToCook(){//animation to cook
    	xDestination = xCook;
        yDestination = yCook;
        command = commandState.moving;
    }
    
    public void DoGoToCashier(){//animation to cook
    	xDestination = xCashier;
        yDestination = yCashier;
        command = commandState.moving;
    }
    
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}