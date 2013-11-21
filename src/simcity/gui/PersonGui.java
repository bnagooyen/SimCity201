package simcity.gui;

import simcity.DRestaurant.DCookRole;
import simcity.DRestaurant.DCustomerRole;
import simcity.DRestaurant.gui.DGui;
import simcity.DRestaurant.gui.DRestaurantGui;
import simcity.DRestaurant.gui.DCookGui.CookLabel.LabelState;
import simcity.PersonAgent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.swing.JLabel;

public class PersonGui implements DGui {

    private PersonAgent agent = null;

    public boolean waiterAtFront()
    {
    	if(xPos==-20 && yPos==-20) return true;
    	else return false;
    }
    private boolean isPresent = false;
    private boolean isHungry = false;
    DRestaurantGui gui;
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
    private int xFood = cook_x+40, yFood = cook_y+20;
    public static final int TABLE_gap=50;
    
//    static List<CookLabel> foodz = Collections.synchronizedList(new ArrayList<CookLabel>());
    
    public static final int TABLESZ_xy = 50;
    
    public static final int restaurantFront_x = -20;
    public static final int restaurantFront_y = -20;
    
    public static final int frontline_x=50;
    public static final int frontline_y=TABLE_gap;
   
    private static int numPlated=1;
    private int hangout_x = 50, hangout_y=50;
    
    public static final int cook_x=630;
    public static final int cook_y = TABLE_gap;
    
    public static final int cashier_x = 0;
    public static final int cashier_y=-20;
    
    public static final int nTABLES = 12;
    public static final int TABLES_perRow = 4;
    
 public static final int allKitchenItems_x = 610;
    
    public static final int refrig_y = 330;
    public static final int refrig_xsz=40;
    public static final int refrig_ysz=60;

    public static final int grill_xsz= 40;
    public static final int grill_ysz = 30;
  
    
//    public static final int grillPizza_y =260;
//    public static final int grillChicken_y =220;
//    public static final int grillSteak_y =180;
//    public static final int grillSalad_y =140;
//    public static final int plating_ysz=80;
//    public static final int plating_x=50;
    
    public static final int grillPizza_y =265;
    public static final int grillChicken_y =225;
    public static final int grillSteak_y =185;
    public static final int grillSalad_y =145;
    public static final int plating_ysz=80;
    public static final int plating_x=50;
    
    //int numPlating=1;
    
    enum Command {none, GoToFridge, GoToPizzaGrill, GoToSteakGrill, GoToChickenGrill, GoToSaladGrill, GoToPlating};
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
    
    public PersonGui(PersonAgent agent, DRestaurantGui g) {
    	gui=g;
        this.agent = agent;
        madeToFront=true;
//        for(int i=0; i<labelIsShowing.length;i++)
//        	labelIsShowing[i]=false;
        
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

        else if (xPos == xDestination && yPos == yDestination)
        {
        	
        		command=Command.none;
        		
        }

        
    }

    @Override
	public void draw(Graphics2D g) {
        g.setColor(Color.darkGray);
        g.fillRect(xPos, yPos, 20, 20);
 
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
		agent.gotHungry();
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
