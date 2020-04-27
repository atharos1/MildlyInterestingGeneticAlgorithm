package main.FinishCriteria;

import main.GeneticSubject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContentFinishCriteria implements FinishCriteria {
    int generationsWithoutImprovementToFinish = 10;
    int currentGenerationsWithoutImprovement = 0;
    GeneticSubject bestSubject = null;

    public ContentFinishCriteria(int generationsWithoutImprovementToFinish) {
        this.generationsWithoutImprovementToFinish = generationsWithoutImprovementToFinish;
    }

    @Override
    public boolean shouldFinish(List<GeneticSubject> population) {
        GeneticSubject bestCurrent = Collections.min(population);
        if(bestSubject == null || bestCurrent.compareTo(bestSubject) > 0) {
            bestSubject = bestCurrent;
            currentGenerationsWithoutImprovement = 0;
        } else if(bestCurrent.compareTo(bestSubject) < 0) {
            bestSubject = null;
            currentGenerationsWithoutImprovement = 0;
        } else {
            currentGenerationsWithoutImprovement++;
        }

        return currentGenerationsWithoutImprovement == generationsWithoutImprovementToFinish;
    }
}
