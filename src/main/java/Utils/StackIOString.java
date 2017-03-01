package Utils;

import org.apache.log4j.Logger;

import java.util.Iterator;

/**
 * This class allows an iterated string, for use when adding multiple items to stack in a transition
 */
public class StackIOString implements Iterable<Character> {
    static Logger logger = Logger.getLogger(StackIOString.class);

    private String baseString;

    public StackIOString(String baseString) {
        this.baseString = baseString;
    }

    public StackIOString() {
		this.baseString = "";
	}

	@Override
    public String toString()
    {
    	return baseString;
    }
    
	@Override
	public Iterator<Character> iterator() {
		return new StringIterator(baseString);
	}

    public void reverse() {
        logger.trace("Reversing string");
        baseString =  new StringBuffer(baseString).reverse().toString();
    }


    private class StringIterator implements Iterator<Character> {

		private Integer currPos;
		private String iteratedString;
		
		public StringIterator(String baseString)
		{
			this.currPos = 0;
			this.iteratedString = baseString;
		}
		
		@Override
		public boolean hasNext() {
			return currPos < iteratedString.length();
		}

		@Override
		public Character next() {
			return iteratedString.charAt(currPos++);
		}

		@Override
		public void remove() {
			if(currPos == 0)
				iteratedString = iteratedString.substring(1);
			else
				iteratedString= iteratedString.substring(0, currPos - 1) 
					+ iteratedString.substring(currPos + 1, iteratedString.length());
		}

	}


	@Override
	public int hashCode() {
		return baseString.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
        return obj.toString().equals(baseString);
    }
	
}
