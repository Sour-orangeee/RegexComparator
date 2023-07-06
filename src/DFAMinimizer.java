import java.util.*;

public class DFAMinimizer {

    public static Set<Set<Integer>> minimizeDFA(Set<Integer> states, Set<Character> alphabet, Map<Integer, Map<Character, Integer>> transitions, Set<Integer> finalStates) {
        Set<Set<Integer>> partitions = new HashSet<>();
        Set<Integer> nonFinalStates = new HashSet<>(states);
        nonFinalStates.removeAll(finalStates);
        partitions.add(finalStates);
        partitions.add(nonFinalStates);

        boolean partitionRefined = true;

        while (partitionRefined) {
            partitionRefined = false;
            Set<Set<Integer>> newPartitions = new HashSet<>();

            for (Set<Integer> partition : partitions) {
                for (Character symbol : alphabet) {
                    Set<Integer> newPartition = new HashSet<>();

                    for (Integer state : partition) {
                        Integer nextState = transitions.get(state).get(symbol);
                        for (Set<Integer> existingPartition : partitions) {
                            if (existingPartition.contains(nextState)) {
                                newPartition.addAll(existingPartition);
                                break;
                            }
                        }
                    }

                    if (!newPartition.isEmpty() && !newPartition.equals(partition)) {
                        newPartitions.add(newPartition);
                        newPartitions.add(SetUtils.difference(partition, newPartition));
                        partitionRefined = true;
                    }
                }
            }

            if (partitionRefined) {
                partitions = newPartitions;
            }
        }

        return partitions;
    }

    // Helper class for set operations
    static class SetUtils {
        public static <T> Set<T> difference(Set<T> set1, Set<T> set2) {
            Set<T> difference = new HashSet<>(set1);
            difference.removeAll(set2);
            return difference;
        }
    }

    public static void main(String[] args) {
        // Define DFA states
        Set<Integer> states = new HashSet<>();
        states.add(1);
        states.add(2);
        states.add(3);
        states.add(4);

        // Define DFA alphabet
        Set<Character> alphabet = new HashSet<>();
        alphabet.add('0');
        alphabet.add('1');

        // Define DFA transitions
        Map<Integer, Map<Character, Integer>> transitions = new HashMap<>();
        transitions.put(1, Map.of('0', 2, '1', 1));
        transitions.put(2, Map.of('0', 3, '1', 4));
        transitions.put(3, Map.of('0', 3, '1', 4));
        transitions.put(4, Map.of('0', 4, '1', 4));

        // Define final states
        Set<Integer> finalStates = new HashSet<>();
        finalStates.add(3);
        finalStates.add(4);

        // Minimize the DFA
        Set<Set<Integer>> minimizedDFA = minimizeDFA(states, alphabet, transitions, finalStates);

        // Print the minimized DFA partitions
        int partitionNumber = 1;
        for (Set<Integer> partition : minimizedDFA) {
            System.out.println("Partition " + partitionNumber + ": " + partition);
            partitionNumber++;
        }
    }
}
