/* Group 19:
586703 Will Ellett, 587310 Will Brookes, 694419 Joshua Yang */
package strategies;

import automail.MailItem;
import automail.StorageTube;
import exceptions.TubeFullException;

import java.util.ArrayList;
import java.util.Comparator;

public class BigSmartBehaviour implements IRobotBehaviour {
	private static final int TUBE_CAPACITY = 6;

	public int getTubeCapacity(){
		return TUBE_CAPACITY;
	}

	public boolean returnToMailRoom(StorageTube tube) {
		// Simple robot can't receive notifications, only return once all mail is delivered

		if (tube.isEmpty()) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void priorityArrival(int priority) {
		// Simple robot can't receive notifications, so method is useless

	}

	@Override
	public boolean fillStorageTube(IMailPool mailPool, StorageTube tube) {
		// New robot has smart behaviour but no notification ability, therefore using essentially the same code as regular smart robot
		//as there is no notification ability, no need to empty tube

		// Grab priority mail first
		try{

			while(tube.getSize() < TUBE_CAPACITY){
				if(mailPool.getPriorityPoolSize() > 0){
					tube.addItem(mailPool.getHighestPriorityMail());
				}
				else{
					// Fill it up with non priority
					if(mailPool.getNonPriorityPoolSize() > 0){
						tube.addItem(mailPool.getNonPriorityMail());
					}
					else{
						break;
					}
				}
			}
		}
		catch (TubeFullException e) {
				e.printStackTrace();
		}

		// Sort tempTube based on floor
		//Actually sorts based on arrival
		tube.arrivalSort();

		// Check if there is anything in the tube
		if(!tube.isEmpty()){
			return true;
		}
		return false;
	}

}
