package simcity.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import simcity.gui.ListPanel;
import simcity.gui.SimCityAnimationPanel;

public class SimCityGui extends JFrame implements ActionListener, MouseListener {

	public static final int SIMCITYX=985;
	public static final int SIMCITYY=462;
	SimCityAnimationPanel simCityAnimationPanel = new SimCityAnimationPanel(this);
	SimCityPanel simCityPanel = new SimCityPanel(this);
	ListPanel addPersonPanel = null;
	
	private SimCityPanel simcityPanel = new SimCityPanel(this);
	
	/**
	 * Constructor for SimCityGui
	 * Sets up all the gui components
	 */
	public SimCityGui() {
		setBounds(5,20, SIMCITYX, SIMCITYY);
		
		setLayout(new BorderLayout()); 
	    add(simCityAnimationPanel, BorderLayout.CENTER);
		
		addPersonPanel=new ListPanel(simCityPanel, "Person");
	         
	         JPanel cityPanels = new JPanel();
	         cityPanels.setLayout(new GridLayout(1,1));
	         cityPanels.setMaximumSize(new Dimension((int)(SIMCITYX*0.25), (200)));
	         cityPanels.setPreferredSize(new Dimension((int)(SIMCITYX*0.25), (200)));
	         cityPanels.setMinimumSize(new Dimension((int)(SIMCITYX*0.25), (200)));
	         cityPanels.add(addPersonPanel);
	         
	         add(cityPanels, BorderLayout.WEST);
	         
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
	
	
	public static void main(String[] args) {
		SimCityGui gui = new SimCityGui();
		gui.setTitle("SimCity");
		gui.setVisible(true);
		gui.setResizable(false);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
}
