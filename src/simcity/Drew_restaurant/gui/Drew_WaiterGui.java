package simcity.Drew_restaurant.gui;


import simcity.Drew_restaurant.Drew_WaiterRole;
import simcity.Drew_restaurant.Drew_HostRole.Table;
import simcity.gui.Gui;

import java.util.HashMap;
import java.util.Map;
import java.awt.*;

public class Drew_WaiterGui implements Gui {

    private Drew_WaiterRole agent = null;
	public CustomerGui customerGui=null;
	//public RestaurantGui restGui=null;

    private int xPos=0;
    private int yPos = 0;//default waiter position
    private int count;
    

	private boolean justArrived=false;
	private boolean wantsBreak=false;
	private boolean onBreak=false;
	public boolean idle=false;	//So that idle waiter will stop inthe middle of heading off screen
	
	//Set Text to follow food
	public String carrying="";
	public String tableOneFood="";
	public String tableTwoFood="";
	public String tableThreeFood="";
	public String tableFourFood="";
	
	static Dimension D = new Dimension(0,0);
	
	private static Map<Integer,Dimension> tableMap = new HashMap<Integer, Dimension>();
	static {
		tableMap.put(1,new Dimension(50,50));
		tableMap.put(2,new Dimension(150,50));
		tableMap.put(3,new Dimension(250,50));
		tableMap.put(4,new Dimension(50,150));
	}
	
    Table table;
    public static int xTable = 200;
    public static int yTable = 250;
    public static final int xOffScreen = 300;
    public static final int yOffScreen = 180;
    public static final int xKitchen = 200;
    public static final int yKitchen = 205;
    public static final int xCashier = 50;
    public static final int yCashier = 275;
    public final int xHome;
    public final int yHome;
    public static final int xCust = 350;
    public static final int yCust = 120;
    
    public static final int hostSize = 20; //Size of one side of square table
    
    private int xDestination;//default start position
	private int yDestination;
	
    public Drew_WaiterGui(Drew_WaiterRole agent, /*RestaurantGui RG,*/ int waiterCount) {
        this.agent = agent;
        //restGui= RG;
        count=waiterCount;
        xHome=300+(waiterCount%4)*25;
        if(waiterCount<4) yHome=175;
        else yHome=200;
        xPos=xHome;
        yPos=yHome;
        xDestination = xHome;
        yDestination = yHome;
    }
    
    public void setWantsBreak(boolean wb){
    	wantsBreak=wb;
    	if(wb){
    		agent.askForBreak();
    	}
    	//restGui.updateInfoPanel(agent);
    }
    
    public void setOnBreak(boolean ob){
    	onBreak=ob;
    	if(!ob) agent.doneWithBreak();
    	//restGui.updateInfoPanel(agent);
    }
    
    public boolean onBreak(){
    	return onBreak;
    }
    
    public boolean wantsBreak(){
    	return wantsBreak;
    }

    public void updatePosition() {
    	if(!idle & (xPos!=xDestination || yPos!=yDestination))justArrived=true;
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        if (xPos == xDestination && yPos == yDestination
        		 & justArrived) {
        	agent.msgAtDest();
           justArrived=false;
        }
        
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xOffScreen) & (yDestination == yOffScreen)) {
        		//agent.msgAtHome();
        }

    }

    public void draw(Graphics2D g) {	//Draws Waiter & strings on tables
    	int tableTextOffset=25;
    	int xWaiterTextOffset=5;
    	int yWaiterTextOffset=12;    	
    	
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, hostSize, hostSize);
        
        g.setColor(Color.WHITE);
        g.drawString(carrying, xPos+xWaiterTextOffset, yPos+yWaiterTextOffset);
        
        g.drawString(tableOneFood, tableMap.get(1).width+tableTextOffset, tableMap.get(1).height+tableTextOffset);
        g.drawString(tableTwoFood, tableMap.get(2).width+tableTextOffset, tableMap.get(2).height+tableTextOffset);
        g.drawString(tableThreeFood, tableMap.get(3).width+tableTextOffset, tableMap.get(3).height+tableTextOffset);
        g.drawString(tableFourFood, tableMap.get(4).width+tableTextOffset, tableMap.get(4).height+tableTextOffset);
    }
    
    public void pickUpFood(String choice){
    	if(choice=="steak")	carrying=""+choice.charAt(0)+choice.charAt(1);
    	else carrying=""+choice.charAt(0);
    }
    
    public void pickUpOrder(){
    	carrying="O";
    }
    
    public void dropOff(){
    	carrying="";
    }
    
    public void dropOffFood(int table, String choice){	//puts food icon on table
    	String disp="";
    	carrying="";
    	if(choice=="steak")	disp=""+choice.charAt(0)+choice.charAt(1);
    	else disp=""+choice.charAt(0);
    	
    	if(table==1) tableOneFood=disp;
    	else if(table==2) tableTwoFood=disp;
    	else if(table==3) tableThreeFood=disp;
    	else if(table==4) tableFourFood=disp;
    }
    
    public void clearTable(int table){		//Removes food icon from table
    	if(table==1) tableOneFood="";
    	else if(table==2) tableTwoFood="";
    	else if(table==3) tableThreeFood="";
    	else if(table==4) tableFourFood="";
    }

    public boolean isPresent() {
        return true;
    }
    
    public void goToTable(int tableNum) {
        xDestination = tableMap.get(tableNum).width + 20;
        yDestination = tableMap.get(tableNum).height - 20;
    	justArrived=true;
    }

    public void DoBringToTable(int tableNum) {
        xDestination = tableMap.get(tableNum).width + 20;
        yDestination = tableMap.get(tableNum).height - 20;
    	customerGui.goTo(tableMap.get(tableNum));
    	justArrived=true;
    }
    
    public void customerFollow(Dimension D){
    	customerGui.goTo(D);
    }

   /* public void doLeaveScreen() {
    	if(xDestination==xOffScreen && yDestination==yOffScreen) agent.msgAtDest();
        xDestination = xOffScreen;
        yDestination = yOffScreen;
    }*/
    
    
    public void goToKitchen() {
    	if(xDestination==xKitchen && yDestination==yKitchen) agent.msgAtDest(); //In case this is called when waiter is already in kitchen
        xDestination = xKitchen;
        yDestination = yKitchen;
    }
    
    public void goToCashier() {
    	if(xDestination==xCashier && yDestination==yCashier) agent.msgAtDest(); //In case this is called when waiter is already in kitchen
        xDestination = xCashier;
        yDestination = yCashier;
    }
    
    public void goHome() {
    	if(xDestination==xHome && yDestination==yHome) agent.msgAtDest(); //In case this is called when waiter is already in kitchen
        xDestination = xHome;
        yDestination = yHome;
    }
    
    public void getCustomer() {
    	if(xDestination==xHome && yDestination==yHome) agent.msgAtDest(); //In case this is called when waiter is already in kitchen
        xDestination = xCust;
        yDestination = yCust;
        justArrived=true;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}
}
