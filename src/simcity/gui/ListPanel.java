package simcity.gui;

import simcity.DCustomerRole;
import simcity.PersonAgent;
import simcity.DWaiterRole;

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
    private JPanel addCust = new JPanel();
    private JPanel addCustButtonPanel = new JPanel();
    private List<JCheckBox> list = new ArrayList<JCheckBox>();
    private List<JLabel> pplList = new ArrayList<JLabel>();
    private JButton addPersonB = new JButton("Add");
    private JButton addPersonAndSetHungryB = new JButton("Add + Set Hungry");
    private JTextField myPerson = new JTextField(); 
    private JComboBox roleSelection = new JComboBox();
    private SimCityPanel restPanel;
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
        restPanel = rp;
        this.type = type;

        setLayout(new BorderLayout());
        JPanel functionalities = new JPanel();
        
        functionalities.setLayout(new GridLayout(2,1));
        
        Dimension funcdim = new Dimension(100, 150);
        
        functionalities.setPreferredSize(funcdim);
        functionalities.setMaximumSize(funcdim);
        functionalities.setMinimumSize(funcdim);
        functionalities.add(new JLabel("<html><pre> <u>" + type + "</u></pre></html>"));
        
        
        Dimension dim = new Dimension(300, 150);
        pane.setSize(dim);
        pane.setPreferredSize(pane.getSize());
        pane.setMinimumSize(pane.getSize());
        pane.setMaximumSize(pane.getSize());
        
        addCustButtonPanel.setLayout(new BorderLayout());
        addPersonAndSetHungryB.setVisible(true);
    	addPersonAndSetHungryB.addActionListener(this);
    	
    	roleSelection.addItem("Select A Role..");
    	roleSelection.addItem("Restaurant Waiter");
    	roleSelection.addItem("Restaurant Host");
    	roleSelection.addItem("Restaurant Cashier");
    	roleSelection.addItem("Restaurant Cook");
    	roleSelection.addItem("Bank Teller");
    	roleSelection.addItem("Bank Loan Officer");
    	roleSelection.addItem("Market Teller");
    	roleSelection.addItem("Market Inventory Person");
    	
    	addCustButtonPanel.add(roleSelection);
    	
    	addCustButtonPanel.add(addPersonB,BorderLayout.EAST);
//    	addCustButtonPanel.add(addPersonAndSetHungryB, BorderLayout.CENTER);
    	
        if(type=="Waiters" || type=="Person") {
        	addPersonAndSetHungryB.setVisible(false);
        }
        
        if(type=="Waiters" || type=="Customers") {
        	roleSelection.setVisible(false);
        }
        
        /*
        else {
        	addPersonAndSetHungryB.setVisible(true);
        	addPersonAndSetHungryB.addActionListener(this);
        }*/
        
        addPersonB.addActionListener(this);
        myPerson.setPreferredSize(new Dimension(200,24));
        
        addCust.add(myPerson);
        addCust.add(addCustButtonPanel);
       // addCust.add(addPersonAndSetHungryB);
        
      
        
        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
        
        functionalities.add(addCust);
        
        add(functionalities, BorderLayout.NORTH);
        
       pane.setViewportView(view);
       
        add(pane, BorderLayout.CENTER);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    @Override
	public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB) {
        	// Chapter 2.19 describes showInputDialog()
            //addPerson(JOptionPane.showInputDialog("Please enter a name:"));
        	String userInput=(myPerson.getText()).trim();
        	if(!userInput.isEmpty())
        		addPerson(myPerson.getText(), false);
        	else return;
        }
        else if(e.getSource() == addPersonAndSetHungryB) {
        	String userInput=(myPerson.getText()).trim();
        	if(!userInput.isEmpty())
        		addPerson(myPerson.getText(), true);
        	else return;
        }
        else {
        	// Isn't the second for loop more beautiful?
            /*for (int i = 0; i < list.size(); i++) {
                JButton temp = list.get(i);*/
        	for (int i=0; i<list.size(); i++){
                if (e.getSource() == list.get(i)) {
//                	System.out.println("yup");
//                	System.err.println(pplList.get(i).getText());
                    currentPerson=restPanel.showInfo(type, pplList.get(i).getText());
//                    System.err.println(currentPerson);
                    if (currentPerson instanceof DCustomerRole) {
                    	list.get(i).setEnabled(false);
                        DCustomerRole c = (DCustomerRole) currentPerson;
                        //c.getGui().setHungry();
                        //System.out.println("found!");
                        
                    }
                    else if (currentPerson instanceof PersonAgent) {
//                    	System.out.println("yupyup");
                    	list.get(i).setEnabled(false);
                        PersonAgent p = (PersonAgent) currentPerson;
                        p.getGui().setHungry();
                 
//                        System.err.println("found!");
                        
                    }
                    else if(currentPerson instanceof DWaiterRole) {
                    	DWaiterRole w = (DWaiterRole) currentPerson;
                    	if(((AbstractButton) e.getSource()).getText()=="Break?") {
                    		w.msgIWantABreak();
                    		//list.get(i).setEnabled(false);
                    	}
                    	else {
                    		w.msgOutOfBreak();
                    		list.get(i).setText("Break?");
                    		list.get(i).setEnabled(true);
                    		list.get(i).setSelected(false);
                    		
                    	}
                    }
                }
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
    public void addPerson(String name, boolean isHungry) {
        if (name != null) {
        	JPanel myPerson = new JPanel();
        	myPerson.setLayout(new BorderLayout());
            JLabel button = new JLabel(name, SwingConstants.CENTER);
//            System.err.println("addPerson");
            button.setBorder(BorderFactory.createLineBorder(Color.black));
            if(type == "Customers") {
//            	System.err.println("eeeeee");
	            JCheckBox custStateCB = new JCheckBox();
	            custStateCB.setText("Hungry?");
	            custStateCB.addActionListener(this);
	            if(isHungry) {
	            	custStateCB.setSelected(true);
	            	custStateCB.setEnabled(false);
	            	 button.setBackground(Color.white);

	                 Dimension paneSize = pane.getSize();
	                 Dimension buttonSize = new Dimension(paneSize.width - 110,
	                         paneSize.height / 7);
	                 button.setPreferredSize(buttonSize);
	                 button.setMinimumSize(buttonSize);
	                 button.setMaximumSize(buttonSize);
	                 //button.addActionListener(this);
	                 //stateCB.addActionListener(this);
	                 //list.add(stateCB);
	                 pplList.add(button);
	                 list.add(custStateCB);
	                
	 	             myPerson.add(custStateCB, BorderLayout.EAST);
	                 myPerson.add(button, BorderLayout.CENTER);
	                
	                 view.add(myPerson);
	                 //restPanel.addPerson(type, name);//puts customer on list
	                 //restPanel.showInfo(type, name);//puts hungry button on panel
	                 validate();
	                 
	                 currentPerson=restPanel.showInfo(type, button.getText());
	               
                	//list.get(i).setEnabled(false);
                    DCustomerRole c = (DCustomerRole) currentPerson;
                    //c.getGui().setHungry();
//                    System.out.println("found!");
                    return;
	                 
	            }
	            
	            list.add(custStateCB);
	            myPerson.add(custStateCB, BorderLayout.EAST);
//	       	 	System.err.println("added button");
            }
            else if(type == "Person") {
//            	System.out.println("ayayayayya");
	            JCheckBox custStateCB = new JCheckBox();
	            custStateCB.setText("Hungry?");
	            custStateCB.addActionListener(this);
	            if(isHungry) {
	            	custStateCB.setSelected(true);
	            	custStateCB.setEnabled(false);
	            	 button.setBackground(Color.white);

	                 Dimension paneSize = pane.getSize();
	                 Dimension buttonSize = new Dimension(paneSize.width - 110,
	                         paneSize.height / 7);
	                 button.setPreferredSize(buttonSize);
	                 button.setMinimumSize(buttonSize);
	                 button.setMaximumSize(buttonSize);
	                 //button.addActionListener(this);
	                 //stateCB.addActionListener(this);
	                 //list.add(stateCB);
	                 pplList.add(button);
	                 list.add(custStateCB);
	                
	 	             myPerson.add(custStateCB, BorderLayout.EAST);
	                 myPerson.add(button, BorderLayout.CENTER);
	                
	                 view.add(myPerson);
	                 //restPanel.addPerson(type, name);//puts customer on list
	                 //restPanel.showInfo(type, name);//puts hungry button on panel
	                 validate();
	                 
	                 currentPerson=restPanel.showInfo(type, button.getText());
	               
                	//list.get(i).setEnabled(false);
                    PersonAgent c = (PersonAgent) currentPerson;
                    c.getGui().setHungry();
//                    System.out.println("found!");
                    return;
	                 
	            }
	            else {
//	            	System.err.println("asdkljfldsjflk");
	            pplList.add(button);
	            list.add(custStateCB);
	            myPerson.add(custStateCB, BorderLayout.EAST);
//	       	 	System.err.println("added button");
	            }
            }
            else {
            	JCheckBox waiterStateCB = new JCheckBox();
            	waiterStateCB.setText("Break?");
            	waiterStateCB.addActionListener(this);
            	list.add(waiterStateCB);
            	myPerson.add(waiterStateCB, BorderLayout.EAST);
            }
            button.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 110,
                    paneSize.height / 7);
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            //button.addActionListener(this);
            //stateCB.addActionListener(this);
            //list.add(stateCB);
            pplList.add(button);
            
            myPerson.add(button, BorderLayout.CENTER);
            view.add(myPerson);
            //restPanel.addPerson(type, name);//puts customer on list
            //restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        }
    }
    
    public void setCustomerEnabled(String old, String n) {
    	for(int i=0; i<pplList.size(); i++) {
    		if(pplList.get(i).getText()==old) {
    			list.get(i).setEnabled(true);
    			list.get(i).setSelected(false);
    			
    			
    			pplList.get(i).setText(n);
    		}
    	}
    }
    
    public void setWaiterEnabled(String name) {
    	for(int i=0; i<pplList.size(); i++) {
    		if(pplList.get(i).getText()==name) {
    			list.get(i).setEnabled(true);
    			list.get(i).setSelected(false);
    		}
    	}
    }
    
    public void setWaiterDisabled(String name) {
    	for(int i=0; i<pplList.size(); i++) {
    		if(pplList.get(i).getText()==name) {
    			list.get(i).setEnabled(false);
    		}
    	}
    }
    
    public void setWaiterToBreak(String name) {
    	for(int i=0; i<pplList.size(); i++) {
    		if(pplList.get(i).getText()==name) {
    			list.get(i).setEnabled(true);
    			list.get(i).setText(" Back?");
    			list.get(i).setSelected(false);
    		}
    	}
    }
}
