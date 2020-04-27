package main.Selection;

import main.GeneticSubject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//TODO da cualquier cosa
public class ProbabilisticTournaments implements Selector {
    private float threshold;

    public ProbabilisticTournaments(float threshold) {
        this.threshold = threshold;
    }

    @Override
    public List<GeneticSubject> select(List<GeneticSubject> subjects, int K) {
        List<GeneticSubject> l = new ArrayList<>();
        List<GeneticSubject> copy = new ArrayList<>(subjects);

        while(l.size() < K) {
            List<GeneticSubject> selected = selectRandomM(copy, 2);
            GeneticSubject fittest = Collections.min(selected);
            selected.remove(fittest);
            if(GeneticSubject.random.nextFloat() < threshold)
                l.add(fittest);
            else
                l.add(selected.get(0));
        }

        return l;
    }

    private List<GeneticSubject> selectRandomM(List<GeneticSubject> subjects, int M) {
        List<GeneticSubject> l = new ArrayList<>();
        while(l.size() < M) {
            Collections.shuffle(subjects);
            for (int i = 0; l.size() < M && i < subjects.size(); i++)
                l.add(subjects.get(i));
        }

        return l;
    }
}
