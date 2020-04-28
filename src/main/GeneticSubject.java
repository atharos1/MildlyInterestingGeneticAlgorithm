package main;

import java.io.IOException;
import java.util.Random;

public abstract class GeneticSubject implements Comparable<GeneticSubject> {
    public static final Random random = new Random(System.currentTimeMillis());

    public abstract double getFitness();

    public abstract GeneticSubject getRandom();
    public abstract GeneticSubject cloneSubject();

    public abstract boolean isPropertyFixed(int propertyIndex);
    public abstract boolean isEveryPropertyFixed();
    public abstract void setFixedProperty(int propertyIndex, Object value);
    public abstract int getFixedPropertyCount();
    public abstract int getRandomUnfixedPropertyIndex();

    public abstract double comparePropertyWith(GeneticSubject gs, int propertyIndex);
    public abstract boolean isPropertySimilarWith(GeneticSubject gs, int propertyIndex);
    public abstract int getPropertyCount();
    public abstract void randomizeProperty(int propertyIndex);
    public abstract void setProperty(int propertyIndex, Object value);
    public abstract Object getProperty(int index);
    public abstract String toString();

    public abstract void loadConfigurationFromFile(String configurationFile) throws IOException;
}
