package main;

import java.util.Random;

public abstract class GeneticSubject implements Comparable<GeneticSubject> {
    public static final Random random = new Random();

    protected double fitness = 0;

    public double getFitness() {
        return fitness;
    }

    public abstract boolean isPropertyFixed(int propertyIndex);
    public abstract boolean isEveryPropertyFixed();
    public abstract int getPropertyCount();
    public abstract void setFixedProperty(int propertyIndex, Object value);
    public abstract int getFixedPropertyCount();
    public abstract int getRandomUnfixedPropertyIndex();
    public abstract GeneticSubject getRandom();
    public abstract void setProperty(int propertyIndex, Object value);
    public abstract void randomizeProperty(int propertyIndex);
    public abstract Object getProperty(int index);
    public abstract String toString();
    public abstract GeneticSubject cloneSubject();

    public int compareTo(GeneticSubject gs) {
        return Double.compare(gs.getFitness(), this.fitness);
    }
}
