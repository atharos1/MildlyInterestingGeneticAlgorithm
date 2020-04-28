package main.FinishCriteria;

import main.GeneticSubject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StructureFinishCriteria implements FinishCriteria {
    public StructureFinishCriteria(int numberOfSubjectsToCompare, int comparableGenerationsBeforeFinish) {
        this.comparableGenerationsBeforeFinish = comparableGenerationsBeforeFinish;
        this.numberOfSubjectsToCompare = numberOfSubjectsToCompare;
    }

    private int comparableGenerationsBeforeFinish = 0;
    private int currComparableGeneration = 0;
    private int numberOfSubjectsToCompare = 0;
    private List<GeneticSubject> lastGeneration = null;
    @Override
    public boolean shouldFinish(List<GeneticSubject> population) {
        List<GeneticSubject> thisGeneration = new ArrayList<>(population);
        Collections.sort(thisGeneration);

        if(lastGeneration != null) {
            for(int i = 0; i < numberOfSubjectsToCompare; i++) {
                GeneticSubject s1 = thisGeneration.get(i);
                GeneticSubject s2 = lastGeneration.get(i);
                for(int prop = 0; prop < thisGeneration.get(0).getPropertyCount(); prop++) {
                    if(!s1.isPropertySimilarWith(s2, prop)) {
                        currComparableGeneration = 0;
                        lastGeneration = thisGeneration;
                        return false;
                    }
                }
            }

            return ++currComparableGeneration == comparableGenerationsBeforeFinish + 1;
        } else {
            currComparableGeneration = 0;
            lastGeneration = thisGeneration;
            return false;
        }
    }

    @Override
    public String toString() {
        return "Finished because the best " + numberOfSubjectsToCompare + " remained similar after " + comparableGenerationsBeforeFinish + " generations.";
    }

    @Override
    public String getName() {
        return "StructureFinishCriteria";
    }
}
