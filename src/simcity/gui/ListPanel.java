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
public class ListPanel extends JPanel implements ActionListener {

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
    public ListPanel(SimCityPanel rp, String type) {
        simcityPanel = rp;
        this.type = type;

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
    	roleSelection.addItem("Restaurant Host");
    	roleSelection.addItem("Restaurant Cashier");
    	roleSelection.addItem("Restaurant Cook");
    	roleSelection.addItem("Bank Teller");
    	roleSelection.addItem("Bank Loan Officer");
    	roleSelection.addItem("Market Teller");
    	roleSelection.addItem("Market Inventory");
        
    	roleSelection.addActionListener(this);
    	
    	addPerson.add(new JLabel("<html><u>" + type + "</u></html>"));
    	addPerson.add(new JLabel());
    	addPerson.add(new JLabel("Name"));
    	addPerson.add(new JLabel("MoneyValue"));
    	addPerson.add(myPerson);
    	addPerson.add(myPersonMoneyVal);
    	addPerson.add(roleSelection);
    	  addPersonB.addActionListener(this);
    	addPerson.add(addPersonB);
        
        Dimension dim = new Dimension(250, 250);
        pane.setSize(dim);
        pane.setPreferredSize(pane.getSize());
        pane.setMinimumSize(pane.getSize());
        pane.setMaximumSize(pane.getSize());
        
        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));

       pane.setViewportView(view);
       add(addPerson, BorderLayout.CENTER);
       
        add(pane, BorderLayout.SOUTH);
        
        UpdateToScenario("Scenario1");
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    @Override
	public void actionPerformed(ActionEvent e) {
        if(e.getSource() == scenarioSelection){
//        	System.err.println("changed to "+ scenarioSelection.getSelectedItem());
//        	UpdateToScenario(scenarioSelection.getSelectedItem());
        }
    	if(e.getSource()==setScenario) {
    		scenarioSelection.setEnabled(false);
    		setScenario.setEnabled(false);
    		//start execution
    	}
    	if (e.getSource() == addPersonB) {
        	// Chapter 2.19 describes showInputDialog()
            //addPerson(JOptionPane.showInputDialog("Please enter a name:"));
        	String userInput1=(myPerson.getText()).trim();
        	String userInput2 = (myPersonMoneyVal).getText().trim();
        	
        	if(!userInput1.isEmpty() && !userInput2.isEmpty())
        		addPerson(myPerson.getText());
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
    
    public void addPerson(String name) {
    		JPanel myPersonControls = new JPanel();
        	JPanel adding = new JPanel();
//        	myPerson.setLayout(new BorderLayout());
            JLabel button = new JLabel(name, SwingConstants.CENTER);
//            System.err.println("addPerson");
            button.setBorder(BorderFactory.createLineBorder(Color.black));


	        pplList.add(button);
	           
//	       	 	System.err.println("added button");
	            
            button.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 50,
                    30);
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            //button.addActionListener(this);
            //stateCB.addActionListener(this);
            //list.add(stateCB);
            pplList.add(button);
            
            adding.add(button, BorderLayout.CENTER);
            view.add(adding);
            simcityPanel.addPerson(type, name);//puts customer on list
            //restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        
    }
    
    public void AddPeople(ArrayList<Person> people) {
    	pplList.clear();
    	view.repaint();
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
    
    public void UpdateToScenario(String type) {
    		//add load scenario method here
    }
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
