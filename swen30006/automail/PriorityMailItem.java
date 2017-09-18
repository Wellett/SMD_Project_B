/* Group 19:
586703 Will Ellett, 587310 Will Brookes, 694419 Joshua Yang */
package automail;

public class PriorityMailItem extends MailItem{
	
	/** The priority of the mail item from 1 low to 100 high */
    private final int PRIORITY_LEVEL;
    
	public PriorityMailItem(int dest_floor, int priority_level, int arrival_time) {
		super(dest_floor, arrival_time);
        this.PRIORITY_LEVEL = priority_level;
	}
	
    /**
    *
    * @return the priority level of a mail item
    */
   public int getPriorityLevel(){
       return PRIORITY_LEVEL;
   }
   
   @Override
   public String toString(){
       return super.toString() +
               "| Priority Level: "+ PRIORITY_LEVEL
               ;
   }

}
