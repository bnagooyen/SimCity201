package simcity.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class SimCityGui extends JFrame implements ActionListener{

	private SimCityPanel simcityPanel = new SimCityPanel(this);
	
	/**
	 * Constructor for SimCityGui
	 * Sets up all the gui componentss
	 */
	public SimCityGui() {
		
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
}
