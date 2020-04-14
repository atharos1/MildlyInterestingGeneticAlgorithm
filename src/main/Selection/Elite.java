package main.Selection;

import main.Character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Elite implements Selector {

    @Override
    public List<Character> select(List<Character> characters, int K) {
        List<Character> l = new ArrayList<>(characters);
        Collections.sort(l);
        return l.subList(0, K);
    }
}
