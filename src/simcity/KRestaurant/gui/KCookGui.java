package simcity.KRestaurant.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import simcity.KRestaurant.KCookRole;
import simcity.KRestaurant.KCustomerRole;
import simcity.KRestaurant.KCashierRole;

public class KCookGui implements KGui{
	private KCookRole agent = null;
	KRestaurantGui gui;

	private int grill;
	private int plate;
	
	private int xPos, yPos;
	private int xDestination, yDestination;
	
	 private final int grillx = 750;
	 private final int grilly = 0;
	 private final int grillw = 30;
	 private final int grillh = 100;
	 
	 private final int grill1y =5;
	 private final int grill2y = 25;
	 private final int grill3y = 45;
	 private final int grill4y = 65;

	 private final int platex = 650;
	 private final int platey = 0;
	 private final int platew = 30;
	 private final int plateh = 100;
	    
	 private final int plate1y =5;
	 private final int plate2y = 25;
	 private final int plate3y = 45;
	 private final int plate4y = 65;

	 private final int fridgex = 700;
	 private final int fridgey = 85;
	 
     public final int width = 20;
     public final int height = 20;

     public KCookGui(KCookRole c, KRestaurantGui gui){ //HostAgent m) {
 		agent = c;
 		xPos = 700;
 		yPos = 0;
 		xDestination = 700;
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
    	 xDestination = 700;
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
}
