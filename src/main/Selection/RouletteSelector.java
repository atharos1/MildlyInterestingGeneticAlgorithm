package main.Selection;

import main.Character.Character;
import main.Character.CharacterFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RouletteSelector implements Selector {
    @Override
    public List<Character> select(List<Character> characters, int K) {
        List<Character> l = new ArrayList<>();
        if(K == 0)
            return l;

        int fitnessSum = 0;
        for(Character c : characters)
            fitnessSum += c.getFitness();

        double r[] = new double[K];
        for(int i = 0; i < K; i++)
            r[i] = CharacterFactory.random.nextFloat();
        Arrays.sort(r);

        double currQ = 0;
        double lastQ = 0;
        for(int i = 0; l.size() < K && i < characters.size(); i++) {
            currQ = (characters.get(i).getFitness() / fitnessSum) + lastQ;
            while(l.size() < K && currQ > r[l.size()])
                l.add(characters.get(i));
            lastQ = currQ;
        }

        return l;
    }
}
