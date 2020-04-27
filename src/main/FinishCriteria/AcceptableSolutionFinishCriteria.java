package main.FinishCriteria;

import main.GeneticSubject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AcceptableSolutionFinishCriteria implements FinishCriteria {
    double acceptableFitness = 24.0;

    public AcceptableSolutionFinishCriteria(double acceptableFitness) {
        this.acceptableFitness = acceptableFitness;
    }

    @Override
    public boolean shoundFinish(List<GeneticSubject> population) {
        return Collections.max(population).getFitness() >= acceptableFitness;
    }
}
