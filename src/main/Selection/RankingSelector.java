package main.Selection;

import main.GeneticSubject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RankingSelector implements Selector {
    @Override
    public List<GeneticSubject> select(List<GeneticSubject> characters, int K) {
        List<GeneticSubject> l = new ArrayList<>();
        if(K == 0)
            return l;

        List<GeneticSubject> orderedCharacters = new ArrayList<>(characters);
        Collections.sort(orderedCharacters);

        double f[] = new double[orderedCharacters.size()];
        double sum = 0;
        for(int i = 0; i < orderedCharacters.size(); i++) {
            f[i] = (double)i / orderedCharacters.size();
            sum += f[i];
        }
        for(int i = 0; i < orderedCharacters.size(); i++)
            f[i] = f[i] / sum;

        double r[] = new double[K];
        for(int i = 0; i < K; i++)
            r[i] = GeneticSubject.random.nextFloat();
        Arrays.sort(r);

        double currQ = 0;
        for(int i = 0; l.size() < K && i < characters.size(); i++) {
            currQ += f[i];
            while(l.size() < K && currQ > r[l.size()])
                l.add(characters.get(i));
        }

        return l;
    }
}
