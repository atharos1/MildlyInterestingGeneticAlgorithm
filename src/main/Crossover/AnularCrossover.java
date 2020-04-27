package main.Crossover;

import main.GeneticSubject;

import java.util.ArrayList;
import java.util.List;

public class AnularCrossover implements Crossover {
    public AnularCrossover() {}

    @Override
    public List<GeneticSubject> cross(GeneticSubject p1, GeneticSubject p2) {
        int point = GeneticSubject.random.nextInt(p1.getPropertyCount() - 1);
        int length = GeneticSubject.random.nextInt((int)Math.ceil((float)p1.getPropertyCount() / 2));

        GeneticSubject c1 = p1.cloneSubject();
        GeneticSubject c2 = p2.cloneSubject();

        for(int i = 0; length > 0; length--) {
            int prop = (i + point) % p1.getPropertyCount();
            c1.setProperty(prop, p2.getProperty(prop));
            c2.setProperty(prop, p1.getProperty(prop));
        }

        List<GeneticSubject> l = new ArrayList<>();
        l.add(c1);
        l.add(c2);

        return l;
    }
}
