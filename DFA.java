package R2D_CD;

import java.util.*;

import javax.swing.undo.StateEdit;

// import java.util.Set;
import R2D_CD.Components.State;
import R2D_CD.Components.SynTreeNode;

public class DFA {
    SynTreeNode root;
    Map<Integer, Character> map;
    Map<Integer, Map<Character, Set<Integer>>> followPosTable;
    Map<State, Map<Character, State>> transition;

    void operate() {

    }

    void constrcuctDFA(String regEx) {
        FollowPos fp = new FollowPos();
        transition = new HashMap<>();
        fp.operate(regEx);
        map = fp.getMap();
        // System.out.println("Map : " + map);
        root = fp.getRoot();
        // System.out.println("Root : " + root);
        followPosTable = fp.getFP();
        // System.out.println("Follow Pos Table : " + followPosTable);
        State initState = new State("Q", root.fisrtPos);
        // System.out.println(initState);
        int flag = 0;
        Set<Character> symbolSet = new HashSet<>(map.values());
        symbolSet.remove('#');
        State trap_state = new State("X", new HashSet<>());
        trap_state.isTrap = true;
        do {
            flag = 0;
            if (transition.isEmpty()) {
                transition.put(initState, new HashMap<>());
                flag = 1;
                for (Integer i : initState.val) {
                    Map<Character, State> temp = transition.get(initState);
                    char token = map.get(i);
                    if (!temp.containsKey(token)) {
                        temp.put(token, new State("Q", followPosTable.get(i).get(token)));
                    } else {
                        State s = temp.get(token);
                        s.val.addAll(followPosTable.get(i).get(token));
                        temp.put(token, s);
                    }
                }
            } else {
                Set<State> currentStates = new HashSet<>();
                for (Map.Entry<State, Map<Character, State>> entry : transition.entrySet()) {
                    for (Map.Entry<Character, State> entry1 : entry.getValue().entrySet()) {
                        if (!currentStates.contains(entry1.getValue()))
                            currentStates.add(entry1.getValue());
                    }
                }
                // System.out.println(
                // "====================================================================================================");
                // System.out.println("Current State List : " + currentStates);
                for (State state : currentStates) {
                    // System.out.println("Operating state : " + state);
                    if (!transition.containsKey(state)) {
                        transition.put(state, new HashMap<>());
                        flag = 1;
                        for (Integer i : state.val) {
                            if (i != -1) {
                                Map<Character, State> temp = transition.get(state);
                                char token = map.get(i);
                                if (token != '#') {
                                    // System.out.println("Index : " + i);
                                    // System.out.println("Token : " + token);
                                    if (!temp.containsKey(token)) {
                                        temp.put(token, new State("Q", followPosTable.get(i).get(token)));
                                    } else {
                                        State s = temp.get(token);
                                        s.val.addAll(followPosTable.get(i).get(token));
                                        temp.put(token, s);
                                    }
                                }
                            }
                        }

                    }
                }
            }
        } while (flag == 1);
        // Set<Map.Entry<State, Map<Character, State>>> entrySet =
        // transition.entrySet();
        Set<State> totalStates = transition.keySet();
        // Iterator<State> it = .iterator();
        // int ind = 0;
        for (State state : totalStates) {
            Map<Character, State> t = transition.get(state);
            Set<Character> trap = new HashSet<>(t.keySet());
            for (Character symbol : symbolSet) {
                if (!trap.contains(symbol)) {
                    t.put(symbol, trap_state);
                }
            }
        }
        int finalStateIndex = 0;

        for (Map.Entry<Integer, Character> entry : map.entrySet()) {
            if (entry.getValue() == '#')
                finalStateIndex = entry.getKey();
        }
        for (Map.Entry<State, Map<Character, State>> entry : transition.entrySet()) {
            State t = entry.getKey();
            if (t.val.contains(finalStateIndex)) {
                t.isFinal = true;
            }
            // System.out.println(t + " : ");
            for (Map.Entry<Character, State> entry1 : entry.getValue().entrySet()) {
                State t1 = entry1.getValue();
                if (t1.val.contains(finalStateIndex)) {
                    t1.isFinal = true;
                }
                // System.out.print(entry1.getKey() + " --> " + entry1.getValue() + " ");

            }
            // System.out.println();
        }
    }

    boolean validString(String str, String regex) {
        constrcuctDFA(regex);
        for (Map.Entry<State, Map<Character, State>> entry : transition.entrySet()) {
            State t = entry.getKey();
            System.out.println(t + " : ");
            for (Map.Entry<Character, State> entry1 : entry.getValue().entrySet()) {
                System.out.print(entry1.getKey() + " --> " + entry1.getValue() + " ");

            }
            System.out.println();
        }
        boolean finalReached = false;
        for (Map.Entry<State, Map<Character, State>> entry : transition.entrySet()) {
            State t = entry.getKey();
            // System.out.println(t + " : ");
            for (Map.Entry<Character, State> entry1 : entry.getValue().entrySet()) {
                if (entry1.getValue().isFinal == true) {
                    finalReached = true;
                    System.out.print("Final " + entry1.getValue());
                    break;
                }
                if (finalReached == true)
                    break;
            }
            System.out.println();
        }
        char[] testString = str.toCharArray();
        Set<Map.Entry<State, Map<Character, State>>> entrySet = transition.entrySet();
        Iterator<Map.Entry<State, Map<Character, State>>> iterator = entrySet.iterator();
        State init_state = new State("Init", new HashSet<>());
        // Check if there is at least one entry in the map
        for (int i = 0; i < 1; i++) {
            if (iterator.hasNext()) {
                // Get the first entry
                Map.Entry<State, Map<Character, State>> temp = iterator.next();
                init_state = temp.getKey();
            }
        }
        System.out.println("Starting  " + init_state);
        State currentState = transition.get(init_state).get(testString[0]);
        System.out.println("Next Character  : " + testString[0]);
        System.out.println("Next : " + currentState);
        if (currentState.isTrap == true)
            return false;
        int i = 1;
        while (i < testString.length) {
            System.out.println("Next : " + currentState);
            if (currentState.isTrap == true)
                return false;
            System.out.println("Next Character  : " + testString[i]);
            currentState = transition.get(currentState).get(testString[i++]);
        }
        System.out.println("Next : " + currentState);
        return currentState.isFinal;

    }

    public static void main(String[] args) {
        DFA dfa = new DFA();
        Scanner sc = new Scanner(System.in);
        // System.out.println("Enter the Regular Expression : ");
        // String reg_ex = sc.nextLine();
        // System.out.println("Enter the the test string : ");
        // String input_string = sc.nextLine();

        System.out.println(dfa.validString("abb", "ab*"));
        // System.out.println(dfa.validString(input_string, reg_ex));

    }
}
