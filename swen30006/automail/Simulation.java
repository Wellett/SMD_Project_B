/* Group 19:
586703 Will Ellett, 587310 Will Brookes, 694419 Joshua Yang */
package automail;

import exceptions.ExcessiveDeliveryException;
import exceptions.MailAlreadyDeliveredException;
import strategies.Automail;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 * This class simulates the behaviour of AutoMail
 */
public class Simulation {

	/* initialise to default parameters */
    private static int MAIL_TO_CREATE = 60;
    private	static int seed;
    private static String Robot_Type = "Small_Comms_Smart";
    private static double Delivery_Penalty = 1.1;
    
    private static ArrayList<MailItem> MAIL_DELIVERED;
    private static double total_score = 0;

    public static void main(String[] args) throws IOException{

    	
    	Properties automailProperties = new Properties();
		FileReader inStream = null;
		
		try {
			inStream = new FileReader("automail.properties");
			automailProperties.load(inStream);
			
			/* set properties */
			seed = Integer.parseInt(automailProperties.getProperty("Seed"));
			
			//building properties 
			Building.FLOORS = Integer.parseInt(automailProperties.getProperty("Number_of_Floors"));
			Building.LOWEST_FLOOR = Integer.parseInt(automailProperties.getProperty("Lowest_Floor"));
			Building.MAILROOM_LOCATION = Integer.parseInt(automailProperties.getProperty("Location_of_MailRoom")); 
			
			/* Delivery Score Parameter */
			Delivery_Penalty = Double.parseDouble(automailProperties.getProperty("Delivery_Penalty"));
			
			// Clock Parameter
			Clock.LAST_DELIVERY_TIME = Integer.parseInt(automailProperties.getProperty("Last_Delivery_Time"));
			
			/*# Mail Generation Parameters
			Mail_Count_Percentage_Variation=20
			Priority_Mail_is_One_in=6
			Not sure where these are...
			*/
			
			
			MAIL_TO_CREATE = Integer.parseInt(automailProperties.getProperty("Mail_to_Create"));
			
			/* Robot Type */
			Robot_Type = automailProperties.getProperty("Robot_Type");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			seed = 0;
			e.printStackTrace();
		} finally {
			 if (inStream != null) {
	                inStream.close();
	            }
		}
		
		
        MAIL_DELIVERED = new ArrayList<MailItem>();
        

                
        /** Used to see whether a seed is initialized or not */
        HashMap<Boolean, Integer> seedMap = new HashMap<>();
        seedMap.put(true, seed);
        
        /** Read the first argument and save it as a seed if it exists */
        if(args.length != 0){
        	seed = Integer.parseInt(args[0]);
        	seedMap.put(true, seed);
        } else{
        	seedMap.put(false, 0);
        }
        
        /* if parameters imported, create automail with robot type, otherwise default */
        Automail automail = new Automail(new ReportDelivery(), Robot_Type);
       
       
        MailGenerator generator = new MailGenerator(MAIL_TO_CREATE, automail.mailPool, seedMap);
        
        /** Initiate all the mail */
        generator.generateAllMail();
        int priority;
        while(MAIL_DELIVERED.size() != generator.MAIL_TO_CREATE) {
        	//System.out.println("-- Step: "+Clock.Time());
            priority = generator.step();
            if (priority > 0) automail.robot.behaviour.priorityArrival(priority);
            try {
				automail.robot.step();
			} catch (ExcessiveDeliveryException e) {
				e.printStackTrace();
				System.out.println("Simulation unable to complete..");
				System.exit(0);
			}
            Clock.Tick();
        }
        printResults();
    }
    
    static class ReportDelivery implements IMailDelivery {
    	
    	/** Confirm the delivery and calculate the total score */
    	public void deliver(MailItem deliveryItem){
    		if(!MAIL_DELIVERED.contains(deliveryItem)){
    			System.out.println("T: "+Clock.Time()+" | Delivered " + deliveryItem.toString());
    			MAIL_DELIVERED.add(deliveryItem);
    			// Calculate delivery score
    			total_score += calculateDeliveryScore(deliveryItem);
    		}
    		else{
    			try {
    				throw new MailAlreadyDeliveredException();
    			} catch (MailAlreadyDeliveredException e) {
    				e.printStackTrace();
    			}
    		}
    	}

    }
    
    private static double calculateDeliveryScore(MailItem deliveryItem) {
    	// Penalty for longer delivery times
    	final double penalty = Delivery_Penalty;
    	double priority_weight = 0;
        // Take (delivery time - arrivalTime)**penalty * (1+sqrt(priority_weight))
    	if(deliveryItem instanceof PriorityMailItem){
    		priority_weight = ((PriorityMailItem) deliveryItem).getPriorityLevel();
    	}
        return Math.pow(Clock.Time() - deliveryItem.getArrivalTime(),penalty)*(1+Math.sqrt(priority_weight));
    }

    public static void printResults(){
        System.out.println("T: "+Clock.Time()+" | Simulation complete!");
        System.out.println("Final Delivery time: "+Clock.Time());
        System.out.printf("Final Score: %.2f%n", total_score);
    }
}
