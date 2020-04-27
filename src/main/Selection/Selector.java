package main.Selection;

import main.GeneticSubject;

import java.util.List;

public interface Selector {
    List<GeneticSubject> select(List<GeneticSubject> subjects, int K);
}
