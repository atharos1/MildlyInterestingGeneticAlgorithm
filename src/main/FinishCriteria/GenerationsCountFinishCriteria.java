package main.FinishCriteria;

import main.GeneticSubject;

import java.util.List;

public class GenerationsCountFinishCriteria implements FinishCriteria {
    int maxGeneration = 1000;
    int currGeneration = 0;

    public GenerationsCountFinishCriteria(int maxGeneration) {
        this.maxGeneration = maxGeneration;
    }

    @Override
    public boolean shoundFinish(List<GeneticSubject> population) {
        return maxGeneration == currGeneration++;
    }
}
