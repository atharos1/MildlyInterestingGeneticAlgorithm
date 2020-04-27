package main.Selection;

import main.GeneticSubject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EliteSelector implements Selector {
    @Override
    public List<GeneticSubject> select(List<GeneticSubject> subjects, int K) {
        List<GeneticSubject> l = new ArrayList<>();
        if(K <= 0)
            return l;

        List<GeneticSubject> orderedCharacters = new ArrayList<>(subjects);
        Collections.sort(orderedCharacters);

        for(int i = 0; l.size() <= K && i < orderedCharacters.size(); i++) {
            int timesToAdd = (int)Math.ceil((float)(K - i)/orderedCharacters.size());
            for(int j = 0; l.size() <= K && j < timesToAdd; j++)
                l.add(orderedCharacters.get(i));
        }

        return l;
    }
}
