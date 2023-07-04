public class Transition {
    private int fromState;
    private char symbol;
    private int toState;

    public Transition(int fromState, char symbol, int toState) {
        this.fromState = fromState;
        this.symbol = symbol;
        this.toState = toState;
    }
}
