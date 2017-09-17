package strategies;

import java.util.ArrayList;

import automail.Clock;
import automail.MailItem;
import automail.PriorityMailItem;
import automail.StorageTube;
import exceptions.TubeFullException;

public class SmartRobotBehaviour implements IRobotBehaviour{

	private int newPriorityArrival;

	public SmartRobotBehaviour(){
		newPriorityArrival = 0;
	}
	@Override
	public boolean returnToMailRoom(StorageTube tube) {
		// Check if my tube contains only priority items
		if(!tube.isEmpty()){
			int priorityCount = 0;
			int nonPriorityCount = 0;
			// There has to be more priority than non-priority to keep going
			for(MailItem m : tube.tube){
				if(m instanceof PriorityMailItem){
					priorityCount++;
				}
				else{
					nonPriorityCount++;
				}
			}

			if(priorityCount >= nonPriorityCount){
				return false;
			}
			else{
				// Check if there is more than 1 priority arrival and the size of the tube is greater than or equal to half
				if(newPriorityArrival > 1 && tube.getSize() >= tube.MAXIMUM_CAPACITY/2){

					return true;
				}
				else{
					return false;
				}

			}
		}
		else{
			return true;
		}
	}

	@Override
	public void priorityArrival(int priority) {
    	// Record that a new one has arrived
		newPriorityArrival++;
    	System.out.println("T: "+Clock.Time()+" | Priority arrived");

	}

	@Override
	public boolean fillStorageTube(IMailPool mailPool, StorageTube tube) {

		try{
			// Empty my tube
			while(!tube.isEmpty()){
				mailPool.addToPool(tube.pop());
			}

			// Grab priority mail
			while(tube.getSize() < tube.MAXIMUM_CAPACITY){
				if(containMail(mailPool,MailPool.PRIORITY_POOL)){
					tube.addItem(mailPool.getHighestPriorityMail());
				}
				else{
					// Fill it up with non priority
					if(containMail(mailPool,MailPool.NON_PRIORITY_POOL)){
						tube.addItem(mailPool.getNonPriorityMail());
					}
					else{
						break;
					}
				}
			}
		}
		catch(TubeFullException e){
			e.printStackTrace();
		}
		// Sort tube based on floor
		// This actually sorts based on arrival time?
		//make a note of this in report, DON'T change behaviour
		tube.arrivalSort();

		// Check if there is anything in the tube
		if(!tube.isEmpty()){
			newPriorityArrival = 0;
			return true;
		}
		return false;
	}

	private boolean containMail(IMailPool m, String mailPoolIdentifier){
		if(mailPoolIdentifier.equals(MailPool.PRIORITY_POOL) && m.getPriorityPoolSize() > 0){
			return true;
		}
		else if(mailPoolIdentifier.equals(MailPool.NON_PRIORITY_POOL) && m.getNonPriorityPoolSize() > 0){
			return true;
		}
		else{
			return false;
		}
	}

}
