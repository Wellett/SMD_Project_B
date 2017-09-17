package strategies;

import automail.IMailDelivery;
import automail.Robot;

public class Automail {
	      
    public Robot robot;
    public IMailPool mailPool;
    
    public Automail(IMailDelivery delivery, String robotType) {
    	// Swap between simple provided strategies and your strategies here
    	    	
    	/** Initialize the MailPool */
    	mailPool = new MailPool();
    	
        /** Initialize the RobotAction */
    	IRobotBehaviour robotBehaviour = null;
    	switch(robotType) {
    		case "Small_Comms_Simple":
    			robotBehaviour = new SimpleRobotBehaviour();
    			break;
    		
    		case "Small_Comms_Smart":
    			robotBehaviour = new SmartRobotBehaviour();
    			break;
    			
    		case "Big_Smart":
    			robotBehaviour = new BigSmartBehaviour();
    			
    		case "null":
    			robotBehaviour = new SimpleRobotBehaviour();
    			break;
    	
    	}
    	    	
    	/** Initialize robot */
    	robot = new Robot(robotBehaviour, delivery, mailPool, robotType); 
    	
    }
    
}
