package main.Termination;

import java.util.List;



public class Time implements Termination {
    @Override
    public boolean terminate(List<Character> currentGeneration) {
        return false;
    }
}
