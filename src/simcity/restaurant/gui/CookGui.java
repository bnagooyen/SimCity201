package simcity.restaurant.gui;


import simcity.restaurant.CookRole;
import simcity.restaurant.CustomerRole;
import simcity.restaurant.gui.CookGui.CookLabel.LabelState;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.swing.JLabel;

public class CookGui implements Gui {

    private CookRole agent = null;

    public boolean waiterAtFront()
    {
    	if(xPos==-20 && yPos==-20) return true;
    	else return false;
    }
    
    RestaurantGui gui;
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
    
    static List<CookLabel> foodz = Collections.synchronizedList(new ArrayList<CookLabel>());
    
    public static final int TABLESZ_xy = 50;
    
    public static final int restaurantFront_x = -20;
    public static final int restaurantFront_y = -20;
    
    public static final int frontline_x=50;
    public static final int frontline_y=TABLE_gap;
   
    private static int numPlated=1;
    private int hangout_x = 650, hangout_y=50;
    
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
    private CustomerRole takingOrderFrom;//, orderFrom;
    
    private int seatingAt_x, seatingAt_y;
    
    private int tablegoingto_x, tablegoingto_y;
    
   //f private void setSeatingAt(int t) { seatingAt=t; }
    
    public CookGui(CookRole agent, RestaurantGui g) {
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
        		if (command==Command.GoToFridge) {
        			agent.msgAnimationArrivedAtFridge();
        			
        			//DoGoToHangout();
        		}
        		else if(command==Command.GoToChickenGrill) {
        			synchronized(foodz) {
            			for(CookLabel foo: foodz) {
            				if(foo.food.equals("Ch?")) {
            					//System.err.println("foundf");
            					foo.isFollowing=true;
            					foo.state=LabelState.plating;
            					foo.food="Ch";
            				}
            			}
            			}
            			synchronized(foodz) {
            			for(CookLabel foo: foodz) {
            				if(foo.isFollowing && foo.state==LabelState.ingredient) {
            					foo.isFollowing=false;
            					foo.state=LabelState.cooking;
            					foo.food="Ch?";
            				}
            			}
            			}
            			
            			
            			agent.msgAnimationArrivedAtGrill();
        			
        			//tablegoingto_x=0; tablegoingto_y=0; //reset values so don't sent msg continuously
        		}
        		else if(command==Command.GoToPizzaGrill) {
        			synchronized(foodz) {
            			for(CookLabel foo: foodz) {
            				if(foo.food.equals("Pi?")) {
            					//System.err.println("foundf");
            					foo.isFollowing=true;
            					foo.state=LabelState.plating;
            					foo.food="Pi";
            				}
            			}
            			}
            			synchronized(foodz) {
            			for(CookLabel foo: foodz) {
            				if(foo.isFollowing && foo.state==LabelState.ingredient) {
            					foo.isFollowing=false;
            					foo.state=LabelState.cooking;
            					foo.food="Pi?";
            				}
            			}
            			}

        			agent.msgAnimationArrivedAtGrill();
        			//DoLeaveCustomer();
//        			foodIsFollowingWaiter=true;
//        			tablegoingto_x=0; tablegoingto_y=0;
        		}
        		else if(command==Command.GoToSaladGrill) {
        			synchronized(foodz) {
        			for(CookLabel foo: foodz) {
        				if(foo.food.equals("Sa?")) {
        					//System.err.println("foundf");
        					foo.isFollowing=true;
        					foo.state=LabelState.plating;
        					foo.food="Sa";
        				}
        			}
        			}
        			synchronized(foodz) {
        			for(CookLabel foo: foodz) {
        				if(foo.isFollowing && foo.state==LabelState.ingredient) {
        					foo.isFollowing=false;
        					foo.state=LabelState.cooking;
        					foo.food="Sa?";
        				}
        			}
        			}
        			
        			
        			agent.msgAnimationArrivedAtGrill();
        		}
        		
        		else if(command==Command.GoToSteakGrill) {
        			synchronized(foodz) {
            			for(CookLabel foo: foodz) {
            				if(foo.food.equals("St?")) {
            					//System.err.println("foundf");
            					foo.isFollowing=true;
            					foo.state=LabelState.plating;
            					foo.food="St";
            				}
            			}
            			}
            			synchronized(foodz) {
            			for(CookLabel foo: foodz) {
            				if(foo.isFollowing && foo.state==LabelState.ingredient) {
            					foo.isFollowing=false;
            					foo.state=LabelState.cooking;
            					foo.food="St?";
            				}
            			}
            			}
            			
            			
            			agent.msgAnimationArrivedAtGrill();
        		}
        		else if(command==Command.GoToPlating) {
        			synchronized(foodz) {
        			for(CookLabel foo: foodz) {
        				if(foo.isFollowing) {
//        					System.err.println("&");
        					foo.isFollowing=false;
        					foo.state=LabelState.plated;
        				}
        			}
        			}
        			agent.msgAnimationArrivedAtPlating();
        		}
        		command=Command.none;
        		
        }
        //tell food to follow
        synchronized(foodz) {
        for(CookLabel foo: foodz) {
        if(foo.isFollowing) {
			    foo.xPos=xPos-20;
			    foo.yPos=yPos+10;
		    }
        }
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
        synchronized(foodz) {
		    for(CookLabel food: foodz) {
		    	g.setColor(Color.BLACK);
		    	g.drawString(food.food, food.xPos, food.yPos);	
		    }
        }
        
        
    }

    @Override
	public boolean isPresent() {
        return true;
    }
    
   public static void DoClearPlating(String foo) {
//    	System.err.println(foo);
//    	System.err.println("called");
    	try {
    	synchronized(foodz) {
	    	for(int i=0; i<foodz.size(); i++) {
	    		if(foodz.get(i).state==LabelState.plated && foodz.get(i).food.equals(foo)) {
	    			foodz.remove(foodz.get(i));
//	    			System.err.println("removed");
	    		}
	    	}
	    	}
    	} 
    	catch(ConcurrentModificationException e) {
    		DoClearPlating(foo);
//    		System.err.println("trying again");
    	}
    	
    	numPlated--;
    }
    
    public void DoGoToRefrigerator() {
    	command=Command.GoToFridge;
    	xDestination=allKitchenItems_x;
    	yDestination=refrig_y;
    	
    }
    
    public void DoGoToPizzaGrill() {
    	
    	command=Command.GoToPizzaGrill;
    	xDestination=allKitchenItems_x;
    	yDestination=grillPizza_y;
    	
    	foodz.add(new CookLabel("(Pi)", xPos, yPos-20));
    }
  
   public void DoGoToPizzaGrill2() {
//    	System.err.println("*");
		synchronized(foodz) {
		for(CookLabel food: foodz) {
			if(food.food.equals("Pi?"))
				food.state=LabelState.cooked;
		}
		}
	   
    	command=Command.GoToPizzaGrill;
    	xDestination=allKitchenItems_x;
    	yDestination=grillPizza_y;
    	
    }
    
    
    public void DoGoToSaladGrill() {
//    	System.err.println("going to salad grill");
    	command=Command.GoToSaladGrill;
    	xDestination=allKitchenItems_x;
    	yDestination=grillSalad_y;
    	
    	foodz.add(new CookLabel("(Sa)", xPos, yPos-20));
    }
    
    public void DoGoToSaladGrill2() {
//    	System.err.println("going to salad grill");
    	synchronized(foodz) {
    	for(CookLabel food: foodz) {
    		if(food.food.equals("Sa?"))
    			food.state=LabelState.cooked;
    	}
    	}
    	command=Command.GoToSaladGrill;
    	xDestination=allKitchenItems_x;
    	yDestination=grillSalad_y;
    	
    }
 
    public void DoGoToSteakGrill() {
    	command=Command.GoToSteakGrill;
    	xDestination=allKitchenItems_x;
    	yDestination=grillSteak_y;	
    	
    	foodz.add(new CookLabel("(St)", xPos, yPos-20));
    }
    
    public void DoGoToSteakGrill2() {
    	
    	synchronized(foodz) {
    	for(CookLabel food: foodz) {
    		if(food.food.equals("St?"))
    			food.state=LabelState.cooked;
    	}
    	}
    	
    	command=Command.GoToSteakGrill;
    	xDestination=allKitchenItems_x;
    	yDestination=grillSteak_y;	

    }
    
    public void DoGoToChickenGrill() {
    	command=Command.GoToChickenGrill;
    	xDestination=allKitchenItems_x;
    	yDestination=grillChicken_y;	
    	
    	foodz.add(new CookLabel("(Ch)", xPos, yPos-20));
    }
    
    public void DoGoToChickenGrill2() {
    	
    	synchronized(foodz) {
    	for(CookLabel food: foodz) {
    		if(food.food.equals("Ch?"))
    			food.state=LabelState.cooked;
    	}
    	}
    	
    	command=Command.GoToChickenGrill;
    	xDestination=allKitchenItems_x;
    	yDestination=grillChicken_y;	

    }
    
    public void DoGoToPlating() {
    	command=Command.GoToPlating;
    	xDestination=allKitchenItems_x;
    	numPlated++;
    	yDestination=30+15*numPlated;
    }
    
//    public void DoGoToFrontLine(){
//    	command=Command.GoToFront;
//    	xDestination=frontline_x;
//    	yDestination=frontline_y;
//    }
//
//    public void DoBringToTable(CustomerAgent customer, int table) {
//        //seatingAt=table;
//    	command=Command.GoSeatCustomer;
//    	
//    	xDestination = ((table -1)%TABLES_perRow*TABLESZ_xy*2) + TABLE_gap + x_Offset;
//    	seatingAt_x= xDestination;
//       
//        yDestination = ((table-1)/TABLES_perRow)*TABLESZ_xy*2+TABLE_gap;
//        seatingAt_y= yDestination;
//        
//      
//    }
//    
//    public void DoGoToTable(CustomerAgent customer, int table) {
//    	//madeToFront=false; // reset
//    	//System.out.println("do go to table called");
// 
//    	command=Command.GoToTable;
//
//    	xDestination = ((table -1)%TABLES_perRow*TABLESZ_xy*2) + TABLE_gap - 20 + x_Offset;
//        yDestination = ((table-1)/TABLES_perRow)*TABLESZ_xy*2+TABLE_gap;     
//    	
//    }
//    
//    public void DoGoToCashier() {
//    	command=Command.GoToCashier;
//    	xDestination = cashier_x;
//    	yDestination = cashier_y;
//    }
//
////    public void DoClearTable(int t) {
////    	System.out.println("doclear called");
////    	labelIsShowing=false;
////    	//System.out.println(" label[" + (t-1) +"] set to false");
////    }
//    
//    public void DoGoToHangout() {
////    	System.out.println("*1*");
//    	command=Command.GoToHangout;
//        xDestination = hangout_x;
//        yDestination = hangout_y;
//    }
////    public void DoGoToFront() {
////    	
////    	  xDestination = restaurantFront_x;
////          yDestination = restaurantFront_y;
////    	
////    }
//    public void DoGoToCook() {
//    	command=Command.GoToCook;
//    	xDestination = cook_x;
//    	yDestination = cook_y;
//    }
//    
//    public void DoShowCookedLabel(String food, int table) {
//    	tableGoingTo=table;
//    	foodReady=food;
//    	labelIsShowing=true;
//    	foodIsFollowingWaiter=true;
//    	xFood=xPos;
//    	yFood=yPos+10;
//    }

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
    
   public static class CookLabel {
    	String food;
    	int xPos, yPos;
    	boolean isFollowing;
    	public enum LabelState {ingredient, cooking, cooked, plating, plated};
    	LabelState state;
    	CookLabel(String f, int x, int y) {
//    		System.err.println(f);
    		food=f;
    		xPos=x;
    		yPos=y;
    		isFollowing=true;
    		state=LabelState.ingredient;
//    		System.err.println("added");
    	}
    }
}
