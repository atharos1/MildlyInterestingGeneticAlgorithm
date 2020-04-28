package main.Selection;

import main.GeneticSubject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BoltzmannSelector implements Selector {
    private double T0 = 10.0;
    private double Tc = 2.0;
    private int t = 0;

    public BoltzmannSelector(double T0, double Tc) {
        this.T0 = T0;
        this.Tc = Tc;
    }

    @Override
    public List<GeneticSubject> select(List<GeneticSubject> subjects, int K) {
        List<GeneticSubject> l = new ArrayList<>();
        if(K == 0)
            return l;

        double r[] = new double[K];
        for(int i = 0; i < K; i++)
            r[i] = GeneticSubject.random.nextFloat();
        Arrays.sort(r);

        double f[] = new double[subjects.size()];
        double sum = 0;
        double T = getT();
        for(int i = 0; i < subjects.size(); i++) {
            f[i] = Math.exp(subjects.get(i).getFitness() / T);
            sum += f[i];
        }
        for(int i = 0; i < subjects.size(); i++)
            f[i] = f[i] / sum;

        double currQ = 0;
        for(int i = 0; l.size() < K && i < subjects.size(); i++) {
            currQ += f[i];
            while(l.size() < K && currQ > r[l.size()]) {
                l.add(subjects.get(i));
            }
        }

        return l;
    }

    private double getT() {
        double T = Tc + (T0 - Tc) * Math.exp(-0.1 * t);
        t++;
        return T;
    }
}
