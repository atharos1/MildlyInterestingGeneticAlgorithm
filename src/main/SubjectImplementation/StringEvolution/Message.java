package main.SubjectImplementation.StringEvolution;

import main.GeneticSubject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Message extends GeneticSubject {
    private static char[] desiredString = "No soy Elliot, hijo consentido de Mira y Jack Christensen. Tampoco soy David Morales, actor de reparto en la perversa ficción de Crisanto Rojas. No soy ninguno, y soy ambos.\nSoy yo.\nY voy a estar bien.".toCharArray();
    //private static char[] desiredString = "Hey!".toCharArray();
    private char[] currentString;

    private static Map<Integer, Double> propertyComparatorDeltas = new HashMap<>();

    private static final char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','ñ','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','Ñ','O','P','Q','R','S','T','U','V','W','X','Y','Z','á','é','i','ó','ú','Á','É','Í','Ó','Ú','¿','?','¡','!','.',',',' ', '\n'};

    private double fitness;

    public Message() {
        currentString = new char[desiredString.length];
    }

    @Override
    public boolean isPropertyFixed(int propertyIndex) {
        return false;
    }

    @Override
    public boolean isEveryPropertyFixed() {
        return false;
    }

    @Override
    public int getPropertyCount() {
        return desiredString.length;
    }

    @Override
    public void setFixedProperty(int propertyIndex, Object value) {

    }

    @Override
    public int getFixedPropertyCount() {
        return 0;
    }

    @Override
    public int getRandomUnfixedPropertyIndex() {
        return GeneticSubject.random.nextInt(currentString.length);
    }

    @Override
    public double comparePropertyWith(GeneticSubject gs, int propertyIndex) {
        if(!(gs instanceof Message) || propertyIndex >= getPropertyCount())
            return 0;

        char char1 = (char)getProperty(propertyIndex);
        char char2 = (char)gs.getProperty(propertyIndex);
        return char1 - char2;
    }

    @Override
    public boolean isPropertySimilarWith(GeneticSubject gs, int propertyIndex) {
        if(!(gs instanceof Message) || propertyIndex >= getPropertyCount())
            return false;

        double propertyDelta = propertyComparatorDeltas.getOrDefault(propertyIndex, 0.0);

        return Double.compare(Math.abs(comparePropertyWith(gs, propertyIndex)), propertyDelta) <= 0;
    }

    @Override
    public double getFitness() {
        //fitness = calculateFitness();
        return 1000 / (1 + calculateFitness());
    }

    @Override
    public GeneticSubject getRandom() {
        Message n = new Message();
        for(int i = 0; i < getPropertyCount(); i++)
            n.setProperty(i, getRandomLetter());

        return n;
    }

    @Override
    public void setProperty(int propertyIndex, Object value) {
        if(propertyIndex >= getPropertyCount())
            return;

        fitness -= Math.abs(desiredString[propertyIndex] - currentString[propertyIndex]) * 500;

        currentString[propertyIndex] = (char)value;

        fitness += Math.abs(desiredString[propertyIndex] - currentString[propertyIndex]) * 500;
    }

    @Override
    public void randomizeProperty(int propertyIndex) {
        setProperty(propertyIndex, getRandomLetter());
    }

    @Override
    public Object getProperty(int index) {
        if(index >= getPropertyCount())
            return -1;

        return currentString[index];
    }

    @Override
    public String toString() {
        return new String(currentString);
    }

    @Override
    public GeneticSubject cloneSubject() {
        Message n = new Message();
        for(int i = 0; i < getPropertyCount(); i++)
            n.setProperty(i, getProperty(i));

        return n;
    }

    @Override
    public void loadConfigurationFromFile(String configurationFile) throws IOException {

    }

    @Override
    public int compareTo(GeneticSubject gs) {
        return Double.compare(gs.getFitness(), this.getFitness());
    }

    private char getRandomLetter() {
        return alphabet[GeneticSubject.random.nextInt(alphabet.length)];
    }

    private double calculateFitness() {
        double fitness = 0;
        for(int i = 0; i < getPropertyCount(); i++)
            fitness += Math.abs(desiredString[i] - currentString[i]);

        return fitness;
    }
}
