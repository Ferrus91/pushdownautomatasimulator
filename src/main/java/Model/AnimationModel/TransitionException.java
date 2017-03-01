package Model.AnimationModel;

import lombok.Getter;

@SuppressWarnings("serial")
public class TransitionException extends Exception {

    @Getter
    private final Character failedChar;
    @Getter
    private final Character failedStack;
    @Getter
    private final String label;

    public TransitionException(Character failedChar, Character failedStack, String label)
	{
        super("The transition for symbol " + failedChar + " and stack symbol " + failedStack + " at node "
                + label + " does not exist");
        this.failedChar = failedChar;
        this.failedStack = failedStack;
        this.label = label;
	}

}
