package main.Selection;

import main.GeneticSubject;

import java.util.ArrayList;
import java.util.List;

public class UniversalSelector implements Selector {
    @Override
    public List<GeneticSubject> select(List<GeneticSubject> subjects, int K) {
        List<GeneticSubject> l = new ArrayList<>();
        if(K == 0)
            return l;

        int fitnessSum = 0;
        for(GeneticSubject c : subjects)
            fitnessSum += c.getFitness();

        double randomR = GeneticSubject.random.nextFloat();
        double r[] = new double[K];
        for(int i = 0; i < K; i++)
            r[i] = (randomR + i) / K;

        double currQ = 0;
        double lastQ = 0;
        for(int i = 0; l.size() < K && i < subjects.size(); i++) {
            currQ = (subjects.get(i).getFitness() / fitnessSum) + lastQ;
            while(l.size() < K && currQ > r[l.size()])
                l.add(subjects.get(i));
            lastQ = currQ;
        }

        return l;
    }
}
