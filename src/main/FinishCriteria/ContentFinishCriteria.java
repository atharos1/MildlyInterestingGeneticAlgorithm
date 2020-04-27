package main.FinishCriteria;

import main.GeneticSubject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContentFinishCriteria implements FinishCriteria {
    int generationsWithoutImprovementToFinish = 10;
    int currentGenerationsWithoutImprovement = 0;
    double bestCurrentFitness = 0;

    public ContentFinishCriteria(int generationsWithoutImprovementToFinish) {
        this.generationsWithoutImprovementToFinish = generationsWithoutImprovementToFinish;
    }

    @Override
    public boolean shoundFinish(List<GeneticSubject> population) {
        double generationFitness = Collections.max(population).getFitness();
        if(generationFitness > bestCurrentFitness) {
            bestCurrentFitness = generationFitness;
            currentGenerationsWithoutImprovement = 0;
        } else if(generationFitness < bestCurrentFitness) {
            bestCurrentFitness = 0;
            currentGenerationsWithoutImprovement = 0;
        } else {
            currentGenerationsWithoutImprovement++;
        }

        return currentGenerationsWithoutImprovement == generationsWithoutImprovementToFinish;
    }
}
