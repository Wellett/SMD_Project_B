/* Group 19:
586703 Will Ellett, 587310 Will Brookes, 694419 Joshua Yang */
package automail;

// import exceptions.RobotNotInMailRoomException;
import exceptions.TubeFullException;
import java.util.Comparator;
import java.util.Stack;

/**
 * The storage tube carried by the robot.
 */
public class StorageTube {

    private int maxCapacity; /* default size */
    private Stack<MailItem> tube;


     /* Constructor for the storage tube */
    public StorageTube(int tubeCapacity){
      this.tube = new Stack<MailItem>();
      this.maxCapacity = tubeCapacity;
    }

    /**
     * @return if the storage tube is full
     */
    public boolean isFull(){
        return tube.capacity() == maxCapacity;
    }

    /**
     * @return if the storage tube is empty
     */
    public boolean isEmpty(){
        return tube.isEmpty();
    }

    /**
     * @return the first item in the storage tube (without removing it)
     */
    public MailItem peek() {
    	return tube.peek();
    }

    /**
     * Add an item to the tube
     * @param item The item being added
     * @throws TubeFullException thrown if an item is added which exceeds the capacity
     */
    public void addItem(MailItem item) throws TubeFullException {
        int current = getSize();
        if(current + 1 <= maxCapacity){
        	tube.add(item);
        } else {
            throw new TubeFullException();
        }
    }

    /** @return the size of the tube **/
    public int getSize(){
    	return tube.size();
    }

    //returns a specific MailItem from the tube
    public MailItem getMailItem(int index){
      return tube.get(index);
    }

    /**
     * @return the first item in the storage tube (after removing it)
     */
    public MailItem pop(){
        return tube.pop();
    }

    public void arrivalSort(){
      tube.sort(new ArrivalComparer());
    }

    private class ArrivalComparer implements Comparator<MailItem>{

      @Override
      public int compare(MailItem m1, MailItem m2) {
        return m1.compareArrival(m2);
      }
    }
}
