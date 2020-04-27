package main.Crossover;

import main.GeneticSubject;

import java.util.ArrayList;
import java.util.List;

public class UniformCrossover implements Crossover {
    private static final float exchangeProbability = 0.5f;

    @Override
    public List<GeneticSubject> cross(GeneticSubject p1, GeneticSubject p2) {
        GeneticSubject c1 = p1.cloneSubject();
        GeneticSubject c2 = p2.cloneSubject();

        for(int i = 0; i < p1.getPropertyCount(); i++) {
            if(GeneticSubject.random.nextFloat() > exchangeProbability) {
                c1.setProperty(i, p2.getProperty(i));
                c2.setProperty(i, p1.getProperty(i));
            } else {
                c1.setProperty(i, p1.getProperty(i));
                c2.setProperty(i, p2.getProperty(i));
            }
        }

        List<GeneticSubject> l = new ArrayList<>();
        l.add(c1);
        l.add(c2);

        return l;
    }
}
