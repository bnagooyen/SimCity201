package simcity.BRestaurant.gui;



import simcity.PersonAgent;
import simcity.gui.Gui;
import simcity.interfaces.*;
import simcity.BRestaurant.*;

import java.awt.*;

public class BCustomerGui implements BGui,Gui{

        private BCustomerRole agent = null;
        private boolean isPresent = false;
        private boolean isHungry = false;

        private String name;
        //private HostAgent host;
//        BRestaurantGui gui;

        private int xPos, yPos;
        private int xDestination, yDestination;
        private enum Command {noCommand, GoToSeat, LeaveRestaurant};
        private Command command=Command.noCommand;

        public static final int xTable = 50;
        public static final int yTable = 150;
        public static final int xTable1 = 200;
        public static final int yTable1 = 150;
        public static final int xTable2 = 350;
        public static final int yTable2 = 150;

        public BCustomerGui(BCustomerRole c, String string){ //HostAgent m) {
                agent = c;
                xPos = 220;
                yPos = 10;
                xDestination = 220;
                yDestination = 10;
                name = string;
                //maitreD = m;
//                this.gui = string;
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

                if (xPos == xDestination && yPos == yDestination) {
                        if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
                        else if (command==Command.LeaveRestaurant) {
                                agent.msgAnimationFinishedLeaveRestaurant();
                                System.out.println("about to call gui.setCustomerEnabled(agent);");
                                isHungry = true;
//                                gui.setCustomerEnabled(agent);
                        }
                        command=Command.noCommand;
                }
        }

        public void draw(Graphics2D g) {
                g.setColor(Color.GREEN);
                g.fillRect(xPos, yPos, 20, 20);
        }

        public boolean isPresent() {
                return isPresent;
        }
        public void setHungry() {
                isHungry = true;
                agent.gotHungry();
                setPresent(true);
        }
        public boolean isHungry() {
                return isHungry;
        }

        public void setPresent(boolean p) {
                isPresent = p;
        }

        public void DoGoToSeat(int seatnumber) {//later you will map seatnumber to table coordinates.

                if (seatnumber==1){
                        xDestination = xTable;
                        yDestination = yTable;
                }

                else if (seatnumber==2){
                        xDestination = xTable1;
                        yDestination = yTable1;
                }

                else if (seatnumber==3){
                        xDestination = xTable2;
                        yDestination = yTable2;
                }

                command = Command.GoToSeat;
        }

        public void DoExitRestaurant() {
                xDestination = -40;
                yDestination = -40;
                command = Command.LeaveRestaurant;
        }
}