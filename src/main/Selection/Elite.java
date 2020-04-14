package main.Selection;

import main.Character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Elite implements Selector {
    @Override
    public List<Character> select(List<Character> characters, int K) {
        if(K <= 0)
            return new ArrayList<>();

        List<Character> orderedCharacters = new ArrayList<>(characters);

        Collections.sort(orderedCharacters);

        List<Character> l = new ArrayList<>(orderedCharacters.subList(0, Math.min(orderedCharacters.size(), K)));

        if(K > orderedCharacters.size())
            l.addAll(this.select(characters, K - characters.size()));

        return l;
    }
}
