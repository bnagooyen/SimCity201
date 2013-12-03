package simcity.gui;


import simcity.interfaces.Person;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

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
    private JComboBox scenarioSelection = new JComboBox();
    private JComboBox houseOrApt = new JComboBox();
    private SimCityPanel simcityPanel;
    private String type;
    private Object currentPerson;/* Holds the agent that the info is about.
	Seems like a hack */

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public PersonListPanel(SimCityGui rp) {
    	
    	gui= rp;
    	
    	//JLabel text= new JLabel(txt);
    	final int INFO_WIDTH = 300;
		final int INFO_HEIGHT = 150;
    	


        this.setPreferredSize(new Dimension(INFO_WIDTH, INFO_HEIGHT));
        
        JPanel scenarioSelector = new JPanel();
        scenarioSelector.setLayout(new GridLayout(2,2));
        scenarioSelector.add(new JLabel("Select a scenario:"));
        scenarioSelector.add(new JLabel());
       
        scenarioSelection.addItem("Scenario 1");
        scenarioSelection.addItem("Scenario 2");
        scenarioSelection.addItem("Scenario 3");
        
        scenarioSelection.addActionListener(this);
        
        scenarioSelector.add(scenarioSelection);
        setScenario.addActionListener(this);
        scenarioSelector.add(setScenario);
        
        add(scenarioSelector, BorderLayout.NORTH);
        
        
        JPanel addPerson = new JPanel();
        addPerson.setLayout(new GridLayout(4,2));

        myPerson.setPreferredSize(new Dimension(100,24));
        myPersonMoneyVal.setPreferredSize(new Dimension(40,24)); 
        Dimension dim2 = new Dimension(100, 24);
        roleSelection.setPreferredSize(dim2);
        
      	roleSelection.addItem("Role..");
    	roleSelection.addItem("Restaurant Waiter");
//    	roleSelection.addItem("Market Inventory");
//    	roleSelection.addItem("Bank Robber");
    	
    	roleSelection.addActionListener(this);
    	
    	addPerson.add(new JLabel("<html><u>" + type + "</u></html>"));
    	addPersonB.addActionListener(this);
    	addPerson.add(addPersonB);
    	addPerson.add(new JLabel("Name"));
    	addPerson.add(new JLabel("MoneyValue"));
    	addPerson.add(myPerson);
    	addPerson.add(myPersonMoneyVal);
    	addPerson.add(roleSelection);
//    	JPanel housingAndStart = new JPanel();
//    	housingAndStart.setLayout(new GridLayout(1,2));
//    	houseOrApt.setPreferredSize(new Dimension(70, 10));
    	houseOrApt.addItem("Apartment");
    	houseOrApt.addItem("House");
    	addPerson.add(houseOrApt);
    	
//    	addPersonB.setPreferredSize(new Dimension(50,10));
    	  
    	
//        
        Dimension dim = new Dimension(250, 150);
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
        if(e.getSource() == scenarioSelection){
//        	System.err.println("changed to "+ scenarioSelection.getSelectedItem());
//        	System.out.println((String)scenarioSelection.getSelectedItem());
        	//UpdateToScenario((String)scenarioSelection.getSelectedItem());
        }
    	if(e.getSource()==setScenario) {
    		scenarioSelection.setEnabled(false);
    		setScenario.setEnabled(false);
    		//simcityPanel.startTimer();
    		//start execution
    	}
    	if (e.getSource() == addPersonB) {
        	// Chapter 2.19 describes showInputDialog()
            //addPerson(JOptionPane.showInputDialog("Please enter a name:"));
        	String userInput1=(myPerson.getText()).trim();
        	String userInput2 = (myPersonMoneyVal).getText().trim();
        	double moneyVal=Double.parseDouble(userInput2);
        	System.out.println((String)roleSelection.getSelectedItem());
        	System.out.println(userInput1);
        	if(!userInput1.isEmpty() && !userInput2.isEmpty() && !((String)roleSelection.getSelectedItem()).equals("Role.."))
        		addPerson(userInput1, moneyVal, (String)roleSelection.getSelectedItem(), (String)houseOrApt.getSelectedItem());
        	else return;
        }
//        else if(e.getSource() == addPersonAndSetHungryB) {
//        	String userInput=(myPerson.getText()).trim();
//        	if(!userInput.isEmpty())
//        		addPerson(myPerson.getText(), true);
//        	else return;
//        }
//        else {
//        	// Isn't the second for loop more beautiful?
//            /*for (int i = 0; i < list.size(); i++) {
//                JButton temp = list.get(i);*/
//        	for (int i=0; i<list.size(); i++){
//                if (e.getSource() == list.get(i)) {
////                	System.out.println("yup");
////                	System.err.println(pplList.get(i).getText());
//                    currentPerson=simCityPanel.showInfo(type, pplList.get(i).getText());
////                    System.err.println(currentPerson);
////                    if (currentPerson instanceof CustomerAgent) {
////                    	list.get(i).setEnabled(false);
////                        CustomerAgent c = (CustomerAgent) currentPerson;
////                        c.getGui().setHungry();
////                        //System.out.println("found!");
////                        
////                    }
////                    else 
//                    if (currentPerson instanceof PersonAgent) {
////                    	System.out.println("yupyup");
//                    	list.get(i).setEnabled(false);
//                        PersonAgent p = (PersonAgent) currentPerson;
//                        p.getGui().setHungry();
//                 
////                        System.err.println("found!");
//                        
//                    }
////                    else if(currentPerson instanceof WaiterAgent) {
////                    	WaiterAgent w = (WaiterAgent) currentPerson;
////                    	if(((AbstractButton) e.getSource()).getText()=="Break?") {
////                    		w.msgIWantABreak();
////                    		//list.get(i).setEnabled(false);
////                    	}
////                    	else {
////                    		w.msgOutOfBreak();
////                    		list.get(i).setText("Break?");
////                    		list.get(i).setEnabled(true);
////                    		list.get(i).setSelected(false);
////                    		
////                    	}
////                    }
//                }
//            }
//        }
    }

    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name name of new person
     */
    
    public void addPerson(String name, double moneyVal, String role, String houseOrApt) {
    		JPanel myPersonControls = new JPanel();
        	JPanel adding = new JPanel();
            JLabel button = new JLabel(name, SwingConstants.CENTER);
//            System.err.println("addPerson");
            button.setBorder(BorderFactory.createLineBorder(Color.black));


	        pplList.add(button);
	           
//	       	 	System.err.println("added button");
	            
            button.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 50,
                    40);
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            //button.addActionListener(this);
            //stateCB.addActionListener(this);
            //list.add(stateCB);
            pplList.add(button);
            
            adding.add(button, BorderLayout.CENTER);
            view.add(adding);
            //simcityPanel.addPerson(name, role, moneyVal, houseOrApt);//puts customer on list
            gui.restPanel.addPerson("Person", name, moneyVal, role, houseOrApt);
            //restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        
    }
    
    public void AddPeople(ArrayList<Person> people) {
    	pplList.clear();
    	for(Component c: view.getComponents())
    	{
    		view.remove(c);
    	}
    	view.validate();
    	for(Person p: people) {
    		  JLabel button = new JLabel(p.getName(), SwingConstants.CENTER);
    		  JPanel adding = new JPanel();
//            System.err.println("addPerson");
            button.setBorder(BorderFactory.createLineBorder(Color.black));


	        pplList.add(button);
	           
//	       	 	System.err.println("added button");
	            
            button.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width-50,
                    40);
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            //button.addActionListener(this);
            //stateCB.addActionListener(this);
            //list.add(stateCB);
            pplList.add(button);
            
            adding.add(button, BorderLayout.CENTER);
            view.add(adding);
    	}
    	validate();
    }
    
//    public void UpdateToScenario(String type) {
//    		//add load scenario method here
//    	if(type.equals("Scenario 1"))
//    		simcityPanel.LoadScenario("config1");
//    	else if(type.equals("Scenario 2"))
//    		simcityPanel.LoadScenario("config2");
//    	else if(type.equals("Scenario 3"))
//    		simcityPanel.LoadScenario("config3");
//    }
//    
//    public void setCustomerEnabled(String old, String n) {
//    	for(int i=0; i<pplList.size(); i++) {
//    		if(pplList.get(i).getText()==old) {
//    			list.get(i).setEnabled(true);
//    			list.get(i).setSelected(false);
//    			
//    			
//    			pplList.get(i).setText(n);
//    		}
//    	}
//    }
//    
//    public void setWaiterEnabled(String name) {
//    	for(int i=0; i<pplList.size(); i++) {
//    		if(pplList.get(i).getText()==name) {
//    			list.get(i).setEnabled(true);
//    			list.get(i).setSelected(false);
//    		}
//    	}
//    }
//    
//    public void setWaiterDisabled(String name) {
//    	for(int i=0; i<pplList.size(); i++) {
//    		if(pplList.get(i).getText()==name) {
//    			list.get(i).setEnabled(false);
//    		}
//    	}
//    }
//    
//    public void setWaiterToBreak(String name) {
//    	for(int i=0; i<pplList.size(); i++) {
//    		if(pplList.get(i).getText()==name) {
//    			list.get(i).setEnabled(true);
//    			list.get(i).setText(" Back?");
//    			list.get(i).setSelected(false);
//    		}
//    	}
//    }
}
