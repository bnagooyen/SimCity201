##SimCity Project Repository

# TEAM POWER RANGERS
![alt text](http://in10words.files.wordpress.com/2013/08/mmpr_rangers.jpg "Team Power Ranger")


###Team Information
  + Doreen Hakimi
  + Linda Char
  + Kimberly Santiago
  + Drew Appleman
  + Tiffany Tran
  + Brian Nguyen 
  
###Anything missing or implemented incorrectly?
	+ Everything is implemented
	+ Semaphores are added
	+ Everything should work! Sometimes a waiter will get stuck getting an order, but I've only seen that when I tested many customers/waiters.


###How To Compile and Run
	+ Download files and open onto eclipse, then press 'run'!
	+ NOTE 1: In order to add customers properly, MUST be entered in the following way:
	+ 		Name, Money, FoodChoice, Stay/Leave 
	+		(Stay/Leave will determine whether they stay or leave if restaurant runs out of choice, or they can't afford anything on the menu, as well as whether they'll Stay or Leave if the restaurant is full)
	+			Is disregarded in the commit for Issue #2 (Scenario 1), just enter it anyway.
	+		FoodChoice is either "Steak", "Chicken", "Pizza", "Salad"
	+		EXAMPLE: Doreen, 23.55, Steak, Leave
	+ NOTE 2: Initializing kitchen threshold values/current inventory values/market values MUST be set at first.
	+		Can increase/decrease as you like, once happy with numbers hit 'SET' so cook can check if restaurant is ready for open
	

	For our v1 SimCity deliverable, each of our agents and roles work independently of eachother and this is evidenced by our working Unit Tests.  However, with the addition of the PersonAgent to unify all of our roles and agents together, there were many errors that occured.  Through Person's errors, our separate working roles and agents were unable to operate well in the whole city.  

	
###Resources
  + [Restaurant v1](http://www-scf.usc.edu/~csci201/readings/restaurant-v1.html)
  + [Agent Roadmap](http://www-scf.usc.edu/~csci201/readings/agent-roadmap.html)
