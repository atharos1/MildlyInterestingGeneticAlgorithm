package main.Crossover;
import main.GeneticSubject;

import java.util.List;

public interface Crossover {
    List<GeneticSubject> cross(GeneticSubject p1, GeneticSubject p2);
}
