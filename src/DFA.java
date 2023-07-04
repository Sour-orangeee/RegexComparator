import java.util.HashSet;
import java.util.Set;

public class DFA {

    public static final char EPSILON = '\0';

    private Set<Integer> states;
    private int startState;
    private int endState;
    private Set<Transition> transitions;

    public DFA() {
        states = new HashSet<>();
        transitions = new HashSet<>();
    }

    public int addState() {
        int state = states.size();
        states.add(state);
        return state;
    }

    public void setStartState(int startState) {
        this.startState = startState;
    }

    public void setEndState(int endState) {
        this.endState = endState;
    }

    public int getStartState() {
        return startState;
    }

    public int getEndState() {
        return endState;
    }

    public void addTransition(int fromState, char symbol, int toState) {
        transitions.add(new Transition(fromState, symbol, toState));
    }
}
