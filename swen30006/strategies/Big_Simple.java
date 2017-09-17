package strategies;
import automail.MailItem;
import automail.StorageTube;
import exceptions.TubeFullException;

import java.util.ArrayList;
import java.util.Comparator;

public class Big_Simple implements IRobotBehaviour {

	private static final int MAX_TAKE = 6; // Larger tube size is 6

	@Override
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
		// New robot has smart behaviour but no notification ability, therefore using essentially the same code as
		// regular smart robot

		ArrayList<MailItem> tempTube = new ArrayList<MailItem>();

		// Grab priority mail first
		while(tempTube.size() < MAX_TAKE){
			if(containMail(mailPool,MailPool.PRIORITY_POOL)){
				tempTube.add(mailPool.getHighestPriorityMail());
			}
			else{
				// Fill it up with non priority
				if(containMail(mailPool,MailPool.NON_PRIORITY_POOL)){
					tempTube.add(mailPool.getNonPriorityMail());
				}
				else{
					break;
				}

			}
		}

		// Sort tempTube based on floor
		tempTube.sort(new ArrivalComparer());

		// Iterate through the tempTube
		while(tempTube.iterator().hasNext()){
			try {
				tube.addItem(tempTube.remove(0));
			} catch (TubeFullException e) {
				e.printStackTrace();
			}
		}

		// Check if there is anything in the tube
		if(!tube.tube.isEmpty()){
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



	private class ArrivalComparer implements Comparator<MailItem>{

		@Override
		public int compare(MailItem m1, MailItem m2) {
			return MailPool.compareArrival(m1, m2);
		}
	}

}
