package main.Selection;

import main.Character.Character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EliteSelector implements Selector {
    @Override
    public List<Character> select(List<Character> characters, int K) {
        List<Character> l = new ArrayList<>();
        if(K <= 0)
            return l;

        List<Character> orderedCharacters = new ArrayList<>(characters);
        Collections.sort(orderedCharacters);

        for(int i = 0; l.size() <= K && i < orderedCharacters.size(); i++) {
            int timesToAdd = (int)Math.ceil((float)(K - i)/orderedCharacters.size());
            for(int j = 0; l.size() <= K && j < timesToAdd; j++)
                l.add(orderedCharacters.get(i));
        }

        return l;
    }
}
