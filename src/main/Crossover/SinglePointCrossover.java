package main.Crossover;

import main.GeneticSubject;

import java.util.ArrayList;
import java.util.List;

public class SinglePointCrossover implements Crossover {
    @Override
    public List<GeneticSubject> cross(GeneticSubject p1, GeneticSubject p2) {
        int point = GeneticSubject.random.nextInt(p1.getPropertyCount() - 1);

        GeneticSubject c1 = p1.cloneSubject();
        GeneticSubject c2 = p2.cloneSubject();

        for(int i = point; i < p1.getPropertyCount(); i++) {
            c1.setProperty(i, p2.getProperty(i));
            c2.setProperty(i, p1.getProperty(i));
        }

        List<GeneticSubject> l = new ArrayList<>();
        l.add(c1);
        l.add(c2);

        return l;
    }
}
