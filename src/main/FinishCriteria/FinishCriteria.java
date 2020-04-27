package main.FinishCriteria;

import main.GeneticSubject;

import java.util.List;

public interface FinishCriteria {
    boolean shoundFinish(List<GeneticSubject> population);
}
