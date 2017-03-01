package Model.AnimationModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.log4j.Logger;

@NoArgsConstructor
public class TransitionRelation {
    static Logger logger = Logger.getLogger(TransitionRelation.class);

    @Override
	public String toString() {
            return transitionChar.toString() + ","
				+ transitionStackTop.toString();
	}

    @Getter
    @Setter
	private Character transitionChar;
    @Getter
    @Setter
	private Character transitionStackTop;

	public TransitionRelation(Character transitionChar,
                              Character onStack) {
		this.transitionChar = transitionChar;
		this.transitionStackTop = onStack;
	}

	@Override
	public boolean equals(Object obj) {
        return !(this == null || obj == null) && obj.toString().equals(this.toString());
	}

    @Override
    public int hashCode()
    {
       return (transitionChar.toString() + transitionStackTop.toString()).hashCode();
    }
}
