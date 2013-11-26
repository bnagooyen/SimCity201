package simcity.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

import simcity.PersonAgent;
import simcity.Bank.BankManagerRole;
import simcity.gui.ListPanel;
import simcity.gui.SimCityAnimationPanel;
import simcity.interfaces.Person;

public class SimCityGui extends JFrame implements ActionListener, MouseListener {

	public static final int SIMCITYX=985;
	public static final int SIMCITYY=462;
	SimCityAnimationPanel simCityAnimationPanel = new SimCityAnimationPanel(this);
	
	private SimCityPanel simcityPanel = new SimCityPanel(this);
	private ListPanel addPersonPanel=new ListPanel(simcityPanel, "Person");
	private ArrayList<Person> people = new ArrayList<Person>();

	JPanel buildingPanels;
	CardLayout cl;
	
	/**
	 * Constructor for SimCityGui
	 * Sets up all the gui components
	 */
	public SimCityGui() {
		setBounds(5,20, SIMCITYX, SIMCITYY);
		
		setLayout(new BorderLayout()); 
	    add(simCityAnimationPanel, BorderLayout.CENTER);
	
	    PersonAgent person = new PersonAgent("Drew");
	    BankManagerRole bm = new BankManagerRole(person); 
	    person.SetJob(bm);
	    person.startThread(); 
		people.add(person);
		startTimer();
	    
	         
	         JPanel cityPanels = new JPanel();
	         cityPanels.setLayout(new GridLayout(1,1));
	         cityPanels.setMaximumSize(new Dimension((int)(SIMCITYX*0.25), (200)));
	         cityPanels.setPreferredSize(new Dimension((int)(SIMCITYX*0.25), (200)));
	         cityPanels.setMinimumSize(new Dimension((int)(SIMCITYX*0.25), (200)));
	         cityPanels.add(addPersonPanel);
	         
	         add(cityPanels, BorderLayout.WEST);
	         
	         /*buildingPanels = new JPanel();
             buildingPanels.setLayout( cardLayout );
             buildingPanels.setMinimumSize( new Dimension( 500, 250 ) );
             buildingPanels.setMaximumSize( new Dimension( 500, 250 ) );
             buildingPanels.setPreferredSize( new Dimension( 500, 250 ) );
             buildingPanels.setBackground(Color.yellow);*/
	         
	         validate();
	        
	       // animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	       // animationFrame.setBounds(100+WINDOWX, 50 , WINDOWX+100, WINDOWY+100);
	       // animationFrame.setVisible(true);
	    	//animationFrame.add(animationPanel); 
//	       
	    	
	    	//setLayout(new FlowLayout(FlowLayout.LEFT, 5,5));
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void addPeople(ArrayList<Person> people) {
		addPersonPanel.AddPeople(people);
	}
	
	public static void main(String[] args) {
		SimCityGui gui = new SimCityGui();
		gui.simcityPanel.LoadScenario("config1");
		gui.setTitle("SimCity");
		gui.setVisible(true);
		gui.setResizable(false);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void displayBuildingPanel( BuildingPanel bp ) {
        System.out.println("abc");
        System.out.println( bp.getName() );
        cl.show( buildingPanels, bp.getName() );
}
	
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void startTimer() {
		Timer timer;
	    
		 class RemindTask extends TimerTask {
			 int counter= 5;
		        @Override
				public void run() {
		        	if(counter <25) {
		                 System.out.println("hour is " + counter);
		                 for(Person p: people) {
		                	 p.msgTimeUpdate(counter);
		                 }
		                 counter++;
		                 if (counter == 25) {
		                	 counter = 1;
		                 }
		        	}
		        }
		 }
	     timer = new Timer();
	     timer.schedule(new RemindTask(),
	                       0,        //initial delay
	                       1*12000);  //subsequent rate		
	}
}
