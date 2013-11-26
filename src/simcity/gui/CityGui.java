package simcity.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import simcity.Transportation.BusAgent;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.TracePanel;
import simcity.interfaces.Person;

public class CityGui extends JFrame {
	
	CityPanel city;
	TListPanel info;
	private SimCityPanel simcityPanel = new SimCityPanel(this);
	private ListPanel addPersonPanel=new ListPanel(simcityPanel, "Person");
	private ArrayList<Person> people = new ArrayList<Person>();
	BusAgent b = new BusAgent();
	CityView view;
	//CityControlPanel CP;
	TracePanel tracePanel;
	GridBagConstraints c = new GridBagConstraints();
	JFrame globalLog = new JFrame("SimCity Log");
	JFrame innerBuildingFrame = new JFrame("InnerBuilding");

	public CityGui() throws HeadlessException {
		//CP = new CityControlPanel(this);
		
		setBounds(20, 0, 875, 444);
		tracePanel = new TracePanel();
		//tracePanel.setPreferredSize(new Dimension(CP.getPreferredSize().width, (int)(1.4*CP.getPreferredSize().height)));
		tracePanel.showAlertsForAllLevels();
		tracePanel.showAlertsForAllTags();

		info = new TListPanel(this);
		
		
		globalLog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		globalLog.setBounds(900,0,350, 735);
		globalLog.setResizable(true);
		globalLog.add(tracePanel);
		globalLog.pack();
		globalLog.setVisible(true);
		
		
		city = new CityPanel(this);
		
		view = new CityView(this);
		
		innerBuildingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		innerBuildingFrame.setBounds(20, 430, 875, 325);
		//innerBuildingFrame.setBounds(0,444,740, 300);
		innerBuildingFrame.setResizable(false);
		innerBuildingFrame.setLayout(new BorderLayout());
		innerBuildingFrame.add(info, BorderLayout.WEST);
		innerBuildingFrame.add(view, BorderLayout.CENTER);

		//innerBuildingFrame.add(info);
		//innerBuildingFrame.add(view);
		innerBuildingFrame.pack();
		innerBuildingFrame.setVisible(true);
		
		this.setLayout(new BorderLayout());
		
//		cityInfo = new TListPanel(this);
//		
//		add(cityInfo, BorderLayout.WEST);
		
//		c.gridx = 0; c.gridy = 0;
//		c.gridwidth = 6; c.gridheight = 6;
		
		
		
		add(city, BorderLayout.CENTER);
		
		
        JPanel cityPanels = new JPanel();
        cityPanels.setLayout(new GridLayout(1,1));
//        cityPanels.setMaximumSize(new Dimension((int)(SIMCITYX*0.25), (200)));
//        cityPanels.setPreferredSize(new Dimension((int)(SIMCITYX*0.25), (200)));
//        cityPanels.setMinimumSize(new Dimension((int)(SIMCITYX*0.25), (200)));
        cityPanels.add(addPersonPanel);
        
        add(cityPanels, BorderLayout.WEST);
		

//		c.gridx = 0; c.gridy = 6;
//		c.gridwidth = 11; c.gridheight = 1;
//		this.add(CP, c);
		
//		c.gridx = 0; c.gridy = 7;
//		c.gridwidth = 11; c.gridheight = 3;
//		c.fill = GridBagConstraints.BOTH;
//		this.add(tracePanel, c);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CityGui test = new CityGui();
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.setResizable(false);
		test.pack();
		test.setVisible(true);

	}

}
