package simcity.BRestaurant;



public class BCheck {
        
        public String choice;
        public int price=0;
        
        
        public boolean paidbyCashier=false;        
        public boolean paidbyCustomer=false;
        public int amount=0;
        
        

        
        public BCheck(String choice)
        {
                
                
                this.choice = choice;
                
                if(choice=="steak")
                        this.price=16;
                
                
                else if (choice=="chicken")
                        this.price=11;
                
                else if (choice=="salad")
                        this.price=6;
                
                else if (choice=="pizza")
                        this.price=9;
                
        }
        
        public BCheck(String choice, int amount){
                if (choice=="steak")
                        this.price=16*amount;
                
                else if (choice=="chicken")
                        this.price=11*amount;
                
                else if (choice=="salad")
                        this.price=6*amount;
                
                else if (choice=="pizza")
                        this.price=9*amount;
        
                }
        }