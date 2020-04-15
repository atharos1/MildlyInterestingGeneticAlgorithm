package main.Selection;

import main.Character.Character;
import main.Character.CharacterFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//TODO Roulette y Universal promedian fitness 24, este promedia fitness 17???
public class RankingSelector implements Selector {
    @Override
    public List<Character> select(List<Character> characters, int K) {
        List<Character> l = new ArrayList<>();
        if(K == 0)
            return l;

        List<Character> orderedCharacters = new ArrayList<>(characters);
        Collections.sort(orderedCharacters);

        double f[] = new double[orderedCharacters.size()];
        for(int i = 0; i < orderedCharacters.size(); i++)
            f[i] = (double)i / orderedCharacters.size();

        double r[] = new double[K];
        for(int i = 0; i < K; i++)
            r[i] = CharacterFactory.random.nextFloat();
        Arrays.sort(r);

        double currQ = 0;
        double lastQ = 0;
        for(int i = 0; l.size() < K && i < characters.size(); i++) {
            currQ = f[i] + lastQ;
            while(l.size() < K && currQ > r[l.size()])
                l.add(characters.get(i));
            lastQ = currQ;
        }

        return l;
    }
}
