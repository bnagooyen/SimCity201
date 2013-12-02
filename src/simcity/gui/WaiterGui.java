package simcity.gui;


import simcity.DCustomerRole;
import simcity.DWaiterRole;

import java.awt.*;

public class WaiterGui implements Gui {

    private DWaiterRole agent = null;

    public boolean waiterAtFront()
    {
    	if(xPos==-20 && yPos==-20) return true;
    	else return false;
    }
    
    SimCityGui gui;
    private int tableGoingTo;
    public static final int x_Offset = 25;
    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position
   // private int xFoodDestination, yFoodDestination;
    private boolean cookedLabelVisible=false;
    private boolean foodIsFollowingWaiter=false;
    private boolean madeToCashier=false;
    private boolean tryingToGetToFront=false;
    private boolean madeToFront=true;
    //private String foodReady;
    private int xFood = cook_x+40, yFood = cook_y+20;
    public static final int TABLE_gap=30;
    
    public static final int TABLESZ_xy = 50;
    
    public static final int restaurantFront_x = -20;
    public static final int restaurantFront_y = -20;
    
    public static final int frontline_x=20;
    public static final int frontline_y=TABLE_gap;
   
    private int hangout_x, hangout_y=300;
    
    public static final int cook_x=450;
    public static final int cook_y = TABLE_gap;
    
    public static final int cashier_x = 0;
    public static final int cashier_y=-20;
    
    public static final int nTABLES = 4;
    public static final int TABLES_perRow = 4;
    
    enum Command {none, GoToFront, GoSeatCustomer, GoToTable, GoToCook, GoToCashier, GoToHangout};
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
    
    public WaiterGui(DWaiterRole agent, SimCityGui g, int startPos) {
    	gui=g;
        this.agent = agent;
        madeToFront=true;
//        for(int i=0; i<labelIsShowing.length;i++)
//        	labelIsShowing[i]=false;
        
        hangout_x=40*startPos+x_Offset;
        
        xPos = hangout_x;
        yPos = hangout_y;
        
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

        else if(xPos==xDestination+40 && yPos == yDestination&&foodIsFollowingWaiter) {
        	foodIsFollowingWaiter=false;
        	//labelIsShowing[tableGoingTo-1]=false;
        	labelIsShowing=false;
        	
        }
        else if (xPos == xDestination && yPos == yDestination)
        {
        		if (command==Command.GoSeatCustomer) {
        			agent.msgAnimationDoneSeating(); 
        			//DoGoToHangout();
        		}
        		else if(command==Command.GoToTable) {
        			agent.msgAnimationArrivedForOrder();
        			//tablegoingto_x=0; tablegoingto_y=0; //reset values so don't sent msg continuously
        		}
        		else if(command==Command.GoToCook) {
        			agent.msgAnimationArrivedAtKitchen();
        			//DoLeaveCustomer();
//        			foodIsFollowingWaiter=true;
//        			tablegoingto_x=0; tablegoingto_y=0;
        		}
        		else if(command==Command.GoToFront) {
        			agent.msgAnimationArrivedAtFront();
        		}
        		
        		else if(command==Command.GoToCashier) {
        			agent.msgAnimationArrivedAtCashier();
        		}
        		command=Command.none;
        		
        }
        //tell food to follow
        if(foodIsFollowingWaiter) {
        xFood=xPos;
        yFood=yPos+10;
        }
        
        
    }

    @Override
	public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
 
        if(labelIsShowing) {
        	g.setColor(Color.BLACK);
        	g.drawString(foodReady.substring(0,2),xFood, yFood);
        	
        	}
        
    }

    @Override
	public boolean isPresent() {
        return true;
    }
    
    public void DoGoToFrontLine(){
    	command=Command.GoToFront;
    	xDestination=frontline_x;
    	yDestination=frontline_y;
    }

    public void DoBringToTable(DCustomerRole customer, int table) {
        //seatingAt=table;
    	command=Command.GoSeatCustomer;
    	
    	xDestination = ((table -1)%TABLES_perRow*TABLESZ_xy*2) + TABLE_gap + x_Offset;
    	seatingAt_x= xDestination;
       
        yDestination = ((table-1)/TABLES_perRow)*TABLESZ_xy*2+TABLE_gap;
        seatingAt_y= yDestination;
        
      
    }
    
    public void DoGoToTable(DCustomerRole customer, int table) {
    	//madeToFront=false; // reset
    	//System.out.println("do go to table called");
 
    	command=Command.GoToTable;

    	xDestination = ((table -1)%TABLES_perRow*TABLESZ_xy*2) + TABLE_gap - 20 + x_Offset;
        yDestination = ((table-1)/TABLES_perRow)*TABLESZ_xy*2+TABLE_gap;     
    	
    }
    
    public void DoGoToCashier() {
    	command=Command.GoToCashier;
    	xDestination = cashier_x;
    	yDestination = cashier_y;
    }

//    public void DoClearTable(int t) {
//    	System.out.println("doclear called");
//    	labelIsShowing=false;
//    	//System.out.println(" label[" + (t-1) +"] set to false");
//    }
    
    public void DoGoToHangout() {
//    	System.out.println("*1*");
    	command=Command.GoToHangout;
        xDestination = hangout_x;
        yDestination = hangout_y;
    }
//    public void DoGoToFront() {
//    	
//    	  xDestination = restaurantFront_x;
//          yDestination = restaurantFront_y;
//    	
//    }
    public void DoGoToCook() {
    	command=Command.GoToCook;
    	xDestination = cook_x;
    	yDestination = cook_y;
    }
    
    public void DoShowCookedLabel(String food, int table) {
    	CookGui.DoClearPlating(food.substring(0,2));
    	tableGoingTo=table;
    	foodReady=food;
    	labelIsShowing=true;
    	foodIsFollowingWaiter=true;
    	xFood=xPos;
    	yFood=yPos+10;
    }

//    public void DoResetCheckBox() {
//    	gui.setWaiterEnabled(agent);
//    }
//    
//    public void DoDisableCheckbox() {
//    	gui.setWaiterDisabled(agent);
//    }
//    
//    public void DoSetBoxToReturn() {
//    	gui.setWaiterToBreak(agent);
//    }
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
