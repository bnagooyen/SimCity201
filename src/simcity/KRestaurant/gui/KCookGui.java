package simcity.KRestaurant.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import simcity.KRestaurant.KCookRole;
import simcity.KRestaurant.KCustomerRole;
import simcity.KRestaurant.KCashierRole;
import simcity.gui.Gui;
import simcity.gui.SimCityGui;

public class KCookGui implements Gui{
	private KCookRole agent = null;
	SimCityGui gui;

	private int grill;
	private int plate;
	
	private int xPos, yPos;
	private int xDestination, yDestination;
	
	 private final int grillx = 550;
	 private final int grilly = 0;
	 private final int grillw = 30;
	 private final int grillh = 100;
	 
	 private final int grill1y =5;
	 private final int grill2y = 25;
	 private final int grill3y = 45;
	 private final int grill4y = 65;

	 private final int platex = 450;
	 private final int platey = 0;
	 private final int platew = 30;
	 private final int plateh = 100;
	    
	 private final int plate1y =5;
	 private final int plate2y = 25;
	 private final int plate3y = 45;
	 private final int plate4y = 65;

	 private final int fridgex = 500;
	 private final int fridgey = 85;
	 
     public final int width = 20;
     public final int height = 20;

     private boolean isPresent;
     
     public KCookGui(KCookRole c, SimCityGui gui){ //HostAgent m) {
 		agent = c;
 		xPos = 0;
 		yPos = 0;
 		xDestination = 500;
 		yDestination = 0;
 		//maitreD = m;
 		this.gui = gui;
 		grill = -1;
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

         if(grill == 0)
         {
 	        if (xPos == xDestination && yPos == yDestination && xDestination == grillx -20 && yDestination == grill1y) {
 	           agent.msgAtGrill();
 	        }
 	        if(xPos == xDestination && yPos == yDestination && xDestination == platex +32 && yDestination == plate1y) {
 	        	agent.msgAtPlating();
 	        }
         }
         if(grill == 1)
         {
         	 if (xPos == xDestination && yPos == yDestination && xDestination == grillx -20 && yDestination == grill2y) {
  	           agent.msgAtGrill();                  
  	        }
  	        if(xPos == xDestination && yPos == yDestination && xDestination == platex +32 && yDestination == plate2y) {
 	        	agent.msgAtPlating();
 	        }
         }
         if(grill == 2)
         {
         	 if (xPos == xDestination && yPos == yDestination && xDestination == grillx -20 && yDestination == grill3y) {
  	           agent.msgAtGrill();
  	        }
  	        if(xPos == xDestination && yPos == yDestination && xDestination == platex +32 && yDestination == plate3y) {
 	        	agent.msgAtPlating();
 	        }
         }
         if(grill == 3)
         {
         	if (xPos == xDestination && yPos == yDestination && xDestination == grillx -20 && yDestination == grill4y) {
  	           agent.msgAtGrill();
         	}
 	        if(xPos == xDestination && yPos == yDestination && xDestination == platex +32 && yDestination == plate4y) {
 	        	agent.msgAtPlating();
 	        }
         }
         if(xPos == xDestination && yPos == yDestination && xDestination ==fridgex && yDestination == fridgey) {
        	 agent.msgAtFridge();
         }

     }
     public void draw(Graphics2D g) {
         g.setColor(Color.cyan);
         g.fillRect(xPos, yPos, 20, 20);
         g.setColor(Color.BLACK);
         g.drawString("Cook", xPos-5, yPos + 15);
     }
     
     public void DoGoHome() {
    	 xDestination = 500;
  		 yDestination = 0;
     }
     public boolean isPresent() {
         return true;
     }
     
     public void DoGoToFridge() {
    	 xDestination = fridgex;
    	 yDestination = fridgey;
     }
     
     public void DoCookFood(int g) {
    	 grill = g;
    	 switch(g) {
	    	 case 0: yDestination = grill1y;
	    	 	break;
	    	 case 1: yDestination = grill2y;
	    	 	break;
	    	 case 2: yDestination = grill3y;
	    	 	break;
	    	 case 3: yDestination = grill4y;
	    	 	break;
    	 }
    	 xDestination = grillx-20;
     }
     
     public void DoPLateFood(int p) {
    	 plate = p;
    	 switch(p) {
    	 case 0: yDestination = plate1y;
    	 	break;
    	 case 1: yDestination = plate2y;
    	 	break;
    	 case 2: yDestination = plate3y;
    	 	break;
    	 case 3: yDestination = plate4y;
    	 	break;
    	 }
    	 xDestination = platex +32;
     }
	public void DoLeaveRestaurant() {
		xDestination = -20;
 		 yDestination = 0;		
	}
	public void DoGoToWork() {
		xPos = -5;
		yPos = -5;
		xDestination = 500;
		yDestination = 0;
	}
	public void setPresent(boolean b) {
		isPresent = b;
	}
}
