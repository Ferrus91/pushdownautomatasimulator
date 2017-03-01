package Model.AnimationModel;

import Utils.StackIOString;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Stack;

public class PushDownStack extends Stack<Character> implements IPushDownStack  {
    static Logger logger = Logger.getLogger(PushDownStack.class);

    /**
     * inherits a normal Java collection stack to handle specific properties needed in this application
     */

    public PushDownStack()
    {
        super();
        this.push('\0');
    }

    public PushDownStack(IPushDownStack pushDownStack) {
        logger.trace("Adding on stack to another");
        ((Stack<Character>)this).addAll((Stack<Character>)pushDownStack);
    }

    /**
     *
     * @param symbols pushes symbols onto the stack from a string
     */

	public void push(StackIOString symbols)
	{
		logger.trace("Pushing " + symbols.toString());
        for(Character symbol : symbols)
		{
            this.push(symbol);
		}
	}

    /**
     *
     * @return the full stack as a string in order
     */

	public String fullContents(){
		logger.trace("Getting full content");
        String returnString = "";
		ArrayList<Character> charList = new ArrayList<Character>();
			
		Object[] undiffChars = this.toArray();

        for (Object undiffChar : undiffChars) {
            if (undiffChar != null)
                charList.add((Character) undiffChar);
        }
		
		for(Character stackChar : charList)
		{
			returnString += stackChar;
		}
		return returnString;
	}

    /**
     *
     * @return is the last field the empty symbol?
     */

    @Override
    public boolean empty() {
        logger.trace("Checking if empty");
        return this.peek().equals('\0');
    }
}
