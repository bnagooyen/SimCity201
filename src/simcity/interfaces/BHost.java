package simcity.interfaces;

import agent.Role;
import simcity.BRestaurant.BCookRole;
import simcity.BRestaurant.BWaiterRole;

public interface BHost {

        void setWaiter(BWaiterRole bw);

        void setCook(BCookRole cook);

}