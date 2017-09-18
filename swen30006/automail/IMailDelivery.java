/* Group 19:
586703 Will Ellett, 587310 Will Brookes, 694419 Joshua Yang */
package automail;

/**
 * a MailDelivery is used by the Robot to deliver mail once it has arrived at the correct location
 */
public interface IMailDelivery {

	/**
     * Delivers an item at its floor
     * @param mailItem the mail item being delivered.
     */
	void deliver(MailItem mailItem);
    
}