package main.FinishCriteria;

import main.GeneticSubject;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeFinishCriteria implements FinishCriteria {
    Long startTimeMillis = null;
    Long durationMillis = TimeUnit.SECONDS.toMillis(15);

    public TimeFinishCriteria(long durationSeconds) {
        durationMillis = TimeUnit.SECONDS.toMillis(durationSeconds);
    }

    @Override
    public boolean shoundFinish(List<GeneticSubject> population) {
        if(startTimeMillis == null)
            startTimeMillis = System.currentTimeMillis();

        return System.currentTimeMillis() - startTimeMillis >= durationMillis;
    }
}
