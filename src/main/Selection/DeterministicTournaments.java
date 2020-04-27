package main.Selection;

import main.GeneticSubject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//TODO da cualquier cosa
public class DeterministicTournaments implements Selector {
    private int randomSubsetSize;

    public DeterministicTournaments(int randomSubsetSize) {
        this.randomSubsetSize = randomSubsetSize;
    }

    @Override
    public List<GeneticSubject> select(List<GeneticSubject> subjects, int K) {
        List<GeneticSubject> l = new ArrayList<>();
        List<GeneticSubject> copy = new ArrayList<>(subjects);

        while(l.size() < K)
            l.add(Collections.min(selectRandomM(copy, randomSubsetSize)));

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
