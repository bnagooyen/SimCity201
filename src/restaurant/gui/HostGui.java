package restaurant.gui;


import restaurant.CustomerRole;
import restaurant.HostRole;
import restaurant.HostRole.Table;

import java.awt.*;
import java.util.ArrayList;

public class HostGui implements Gui {

    private HostRole agent = null;

    public boolean hostAtFront()
    {
    	if(xPos==-20 && yPos==-20) return true;
    	else return false;
    }
    
    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position

    public static final int TABLE_gap=50;
    
    public static final int TABLESZ_xy = 50;
    
    public static final int restaurantFront_x = -20;
    public static final int restaurantFront_y = -20;
   
    public static final int nTABLES = 12;
    public static final int TABLES_perRow = 4;
    
    public ArrayList<Table> Tables = new ArrayList<Table>();

    private int seatingAt;
    private int seatingAt_x, seatingAt_y;
    
    private void setSeatingAt(int t) { seatingAt=t; }
    
    public HostGui(HostRole agent) {
        this.agent = agent;
    }

    @Override
	public void updatePosition() {
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;
        /*
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == seatingAt_x) & (yDestination == seatingAt_y)) {
           agent.msgAtTable();
        } */
    }

    @Override
	public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
    }

    @Override
	public boolean isPresent() {
        return false;
    }

    public void DoBringToTable(CustomerRole customer, int table) {
        seatingAt=table;
    	xDestination = ((table -1)%TABLES_perRow*TABLESZ_xy*2) + TABLE_gap;
    	seatingAt_x= xDestination;
       
        yDestination = ((table-1)/TABLES_perRow)*TABLESZ_xy*2+TABLE_gap;
        seatingAt_y= yDestination;
        
      
    }

    public void DoLeaveCustomer() {
        xDestination = restaurantFront_x;
        yDestination = restaurantFront_y;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
