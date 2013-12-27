/*
 * SimCity201, Released December 2013
 * Contributors to file: Doreen Hakimi
 * Description: Gui panel which allows for people to be created and scenarios to be loaded
 */

package simcity.gui;


import simcity.PersonAgent;
import simcity.interfaces.Person;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class PersonListPanel extends JPanel implements ActionListener {

	SimCityGui gui;
	public JScrollPane pane =
			new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JPanel view = new JPanel();
	//private List<JCheckBox> list = new ArrayList<JCheckBox>();
	private List<JLabel> pplList = new ArrayList<JLabel>();
	private JButton addPersonB = new JButton("Add");
	private JButton setScenario = new JButton("Start!");
	private JTextField myPerson = new JTextField(); 
	private JTextField myPersonMoneyVal = new JTextField(); 
	private JComboBox roleSelection = new JComboBox();

	//array list for switching roles
	private ArrayList<JComboBox> switchRoles = new ArrayList<JComboBox>();

	//array list for tracking
	private ArrayList<JCheckBox> peopleTrackerCBs = new ArrayList<JCheckBox>();

	private JComboBox scenarioSelection = new JComboBox();
	private JComboBox dayWeekendSelection = new JComboBox();
	private JComboBox houseOrApt = new JComboBox();
	private JComboBox typeTransport = new JComboBox();
	private String type;


	/**
	 * Constructor for ListPanel.  Sets up all the gui
	 *
	 * @param rp   reference to the restaurant panel
	 * @param type indicates if this is for customers or waiters
	 */
	public PersonListPanel(SimCityGui rp) {

		//this.setBackground(Color.black);

		gui= rp;

		//JLabel text= new JLabel(txt);
		final int INFO_WIDTH = 300;
		final int INFO_HEIGHT = 150;

		this.setBorder(BorderFactory.createLineBorder(Color.black));

		this.setPreferredSize(new Dimension(INFO_WIDTH, INFO_HEIGHT));

		JPanel scenarioSelector = new JPanel();
		//scenarioSelector.setBackground(Color.black);
		scenarioSelector.setLayout(new GridLayout(2,2));
		JLabel lbl = new JLabel("Select a scenario:");
		lbl.setFont(new Font("Arial", Font.BOLD, 12));
		//lbl.setForeground(Color.white);
		scenarioSelector.add(lbl);

		setScenario.addActionListener(this);
		scenarioSelector.add(setScenario);

		scenarioSelection.addItem("Scenario 1");
		scenarioSelection.addItem("Scenario 2");
		scenarioSelection.addItem("Scenario 3");
		scenarioSelection.addItem("Scenario 4");

		scenarioSelection.addActionListener(this);

		scenarioSelector.add(scenarioSelection);

		dayWeekendSelection.addItem("Day/Weekend");
		dayWeekendSelection.addItem("Weekday");
		dayWeekendSelection.addItem("Weekend");

		scenarioSelector.add(dayWeekendSelection);
		dayWeekendSelection.setEnabled(false);

		add(scenarioSelector, BorderLayout.NORTH);


		JPanel addPerson = new JPanel();
		//addPerson.setBackground(Color.black);
		Border whiteLine = BorderFactory.createLineBorder(Color.white);
		TitledBorder title = BorderFactory.createTitledBorder(whiteLine, "Add Person");
		title.setTitleFont(new Font("Arial", Font.BOLD, 10));
		//title.setTitleColor(Color.white);
		addPerson.setBorder(title);
		addPerson.setLayout(new GridLayout(4,2));

		myPerson.setPreferredSize(new Dimension(100,24));
		myPersonMoneyVal.setPreferredSize(new Dimension(40,24)); 
		Dimension dim2 = new Dimension(100, 24);

		roleSelection.setPreferredSize(dim2);


		roleSelection.addItem("Role..");
		roleSelection.addItem("Person");
		roleSelection.addItem("Visitor");
		roleSelection.addItem("Waiter");

		/***** roles that would have supported role switching (removed last minute due to shortage of time) ****/
		//roleSelection.addItem("Inventory Person 1");
		//roleSelection.addItem("Inventory Person 2");
		//roleSelection.addItem("Inventory Person 3");
		//roleSelection.addItem("Inventory Person 4");
		//roleSelection.addItem("Bank Teller 1");
		//roleSelection.addItem("Bank Teller 2");
		//roleSelection.addItem("Bank Loan Officer 1");
		//roleSelection.addItem("Bank Loan Officer 2");
		//roleSelection.addItem("Market Inventory");
		//roleSelection.addItem("Bank Robber");

		roleSelection.addItem("Bank Robber");

		roleSelection.addActionListener(this);


		JLabel lbl2 = new JLabel("Name");

		lbl2.setFont(new Font("Arial", Font.BOLD, 12));
		addPerson.add(lbl2);
		JLabel lbl3 = new JLabel("MoneyValue");
		lbl3.setFont(new Font("Arial", Font.BOLD, 12));

		addPerson.add(lbl3);
		addPerson.add(myPerson);
		addPerson.add(myPersonMoneyVal);
		addPerson.add(roleSelection);


		houseOrApt.addItem("Apartment");
		houseOrApt.addItem("House");
		addPerson.add(houseOrApt);

		typeTransport.addItem("Walk");
		typeTransport.addItem("Bus");
		typeTransport.addItem("Car");
		addPerson.add(typeTransport);

		addPersonB.addActionListener(this);
		addPerson.add(addPersonB);

		//    	addPersonB.setPreferredSize(new Dimension(50,10));


		//        
		Dimension dim = new Dimension(300, 172);
		pane.setSize(dim);
		pane.setPreferredSize(pane.getSize());
		pane.setMinimumSize(pane.getSize());
		pane.setMaximumSize(pane.getSize());

		view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));

		pane.setViewportView(view);
		add(addPerson, BorderLayout.CENTER);

		add(pane, BorderLayout.SOUTH);

		//UpdateToScenario("Scenario1");
	}

	/**
	 * Method from the ActionListener interface.
	 * Handles the event of the add button being pressed
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource()==setScenario) {
			scenarioSelection.setEnabled(false);
			setScenario.setEnabled(false);
			dayWeekendSelection.setEnabled(false);
			//simcityPanel.startTimer();
			UpdateToScenario((String)scenarioSelection.getSelectedItem());
			//start execution
		}
		if (e.getSource() == addPersonB) {

			String userInput1=(myPerson.getText()).trim();
			String userInput2 = (myPersonMoneyVal).getText().trim();
			double moneyVal=Double.parseDouble(userInput2);
			System.out.println((String)roleSelection.getSelectedItem());
			System.out.println(userInput1);
			if(!userInput1.isEmpty() && !userInput2.isEmpty() && !((String)roleSelection.getSelectedItem()).equals("Role.."))
				addPerson(userInput1, moneyVal, (String)roleSelection.getSelectedItem(), (String)houseOrApt.getSelectedItem(), (String)typeTransport.getSelectedItem());
			else return;
		}


		for(int i=0; i<peopleTrackerCBs.size(); i++) {
			if(e.getSource()==peopleTrackerCBs.get(i)) {
				System.err.println(pplList.get(i).getText() + " "+peopleTrackerCBs.get(i).isSelected());
				System.err.println("Want to track "+ pplList.get(i).getText());
				gui.simcityPanel.toggleTrackingOf(pplList.get(i).getText());
			}
		}
	}

	/**
	 * If the add button is pressed, this function creates
	 * a spot for it in the scroll pane, and tells the restaurant panel
	 * to add a new person.
	 *
	 * @param name name of new person
	 */

	public void addPerson(String name, double moneyVal, String role, String houseOrApt, String typeTransport) {

		//just in case there was no housing and adding fails
		boolean success=  gui.simcityPanel.addPerson("Person", name, moneyVal, role, houseOrApt, typeTransport);
		if(!success) return;

		JPanel myPersonControls = new JPanel();
		JPanel adding = new JPanel();
		JLabel button = new JLabel(name, SwingConstants.CENTER);

		adding.setBorder(BorderFactory.createRaisedBevelBorder());
		JComboBox switchRole = new JComboBox();
		JCheckBox trackPerson = new JCheckBox("Track?");
		Dimension dim3 = new Dimension(120, 24);
		switchRole.setPreferredSize(dim3);
		switchRole.addItem("Visitor");
		switchRole.addItem("Person");
		switchRole.addItem("Waiter");
		switchRole.addItem("InventoryPerson");
		switchRole.addItem("Bank Teller");
		switchRole.addItem("Bank Loan Officer");
		switchRole.addItem("Bank Robber");
		switchRole.addItem("MCashier");

		switchRole.setEnabled(false);
		switchRole.addActionListener(this);
		switchRole.setSelectedItem(role);
		switchRoles.add(switchRole);

		pplList.add(button);



		//	       	 	System.err.println("added button");

		button.setBackground(Color.white);

		Dimension paneSize = pane.getSize();
		Dimension buttonSize = new Dimension(paneSize.width - 200,
				20);
		button.setPreferredSize(buttonSize);
		button.setMinimumSize(buttonSize);
		button.setMaximumSize(buttonSize);


		trackPerson.addActionListener(this);
		trackPerson.setFont(new Font("Arial", Font.ITALIC, 8));

		peopleTrackerCBs.add(trackPerson);


		adding.add(trackPerson, BorderLayout.WEST);
		adding.add(button, BorderLayout.CENTER);
		adding.add(switchRole, BorderLayout.EAST);



		view.add(adding);
		validate();

	}

	public void AddPeople(Vector<PersonAgent> people) {
		pplList.clear();
		for(Component c: view.getComponents())
		{
			view.remove(c);
		}
		view.validate();
		for(Person p: people) {
			JLabel button = new JLabel(p.getName(), SwingConstants.CENTER);
			JPanel adding = new JPanel();

			adding.setBorder(BorderFactory.createRaisedBevelBorder());


			JComboBox switchRole = new JComboBox();
			JCheckBox trackPerson = new JCheckBox("Track?");
			Dimension dim3 = new Dimension(120, 24);
			switchRole.setPreferredSize(dim3);
			switchRole.addItem("Visitor");
			switchRole.addItem("Waiter");
			switchRole.addItem("MCashier");

			switchRole.addItem("InventoryPerson");
			switchRole.addItem("Bank Teller");
			switchRole.addItem("Bank Loan Officer");
			switchRole.addItem("Bank Robber");

			//                   switchRole.addItem("Repair Man");
			//                   switchRole.addItem("RCashier");
			//                   switchRole.addItem("Cook");

			switchRole.addActionListener(this);
			String myJob = p.getJob();

			if(myJob==null) { //Visitor's don't have jobs
				switchRole.addItem("Not Working");
				switchRole.setSelectedItem("Not Working");
				switchRole.setEnabled(false);
			}

			else if (myJob.contains("Host") || myJob.contains("Manager")) {
				switchRole.addItem(myJob);
				switchRole.setSelectedItem(myJob);
				switchRole.setEnabled(false);
			}
			else if (myJob.contains("Cook") || myJob.contains("Cashier")) {
				switchRole.addItem(myJob);
				switchRole.setSelectedItem(myJob);
				switchRole.setEnabled(false);
			}
			else {
				switchRole.setSelectedItem(p.getJob());
				switchRole.setEnabled(false);
			}
			switchRoles.add(switchRole);


			button.setBackground(Color.white);

			Dimension paneSize = pane.getSize();
			Dimension buttonSize = new Dimension(paneSize.width - 200,
					20);
			button.setPreferredSize(buttonSize);
			button.setMinimumSize(buttonSize);
			button.setMaximumSize(buttonSize);

			pplList.add(button);

			trackPerson.addActionListener(this);
			trackPerson.setFont(new Font("Arial", Font.ITALIC, 8));

			peopleTrackerCBs.add(trackPerson);

			adding.add(trackPerson, BorderLayout.WEST);
			adding.add(button, BorderLayout.CENTER);
			adding.add(switchRole, BorderLayout.EAST);


			view.add(adding);

		}
		validate();
	}

	public void UpdateToScenario(String type) {
		//add load scenario method here


		//normative with timer 
		if(type.equals("Scenario 1")) {
			dayWeekendSelection.setSelectedItem("Weekday");
			gui.simcityPanel.LoadScenario("config1");
			gui.simcityPanel.startTimer();
		}

		//visitor 1 without timer
		else if(type.equals("Scenario 2")) {
			dayWeekendSelection.setSelectedItem("Weekday");
			gui.simcityPanel.LoadScenario("config2");
			gui.simcityPanel.getWorkersToJob();
		}

		else if(type.equals("Scenario 3")) {
			dayWeekendSelection.setSelectedItem("Weekday");
			gui.simcityPanel.LoadScenario("config3");
			gui.simcityPanel.getWorkersToJob();
		}
		else if(type.equals("Scenario 4")) {
			dayWeekendSelection.setSelectedItem("Weekend");
			gui.simcityPanel.LoadScenario("config4");
			gui.simcityPanel.startTimer();
		}


	}

}
