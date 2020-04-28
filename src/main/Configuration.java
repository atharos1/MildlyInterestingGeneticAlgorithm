package main;

import main.Crossover.Crossover;
import main.FinishCriteria.FinishCriteria;
import main.Mutations.Mutation;
import main.Selection.Selector;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.channels.SelectableChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Configuration {
    public int generationSize;

    public int cantChildren;

    public List<Mutation> mutationMethods = null;

    public Crossover crossoverMethod = null;

    public List<FinishCriteria> finishCriteria = null;

    public Selector parentSelector1 = null;
    public Selector parentSelector2 = null;
    public float parentSelector1Proportion = 1;

    public Selector nextGenerationSelector1 = null;
    public Selector nextGenerationSelector2 = null;
    public float nextGenerationSelector1Proportion = 1;
    public boolean fillAll = false;

    public boolean printBestOnEachGeneration = false;
    public boolean printEndingInformation = true;
    public boolean printBestFitnessEvolution = false;

    public Configuration(String configurationFile) {
        String json = null;
        try {
            json = new String(Files.readAllBytes(Paths.get(configurationFile)));
        } catch (IOException e) {
            System.err.println("Error opening algorithm configuration file.");
            System.exit(-1);
        }
        JSONObject o = new JSONObject(json);

        try {
            generationSize = o.getInt("generationSize");
        }  catch (Exception e) {
            System.err.println("Error reading generation size configuration.");
            System.exit(-1);
        }

        if(o.has("printBestOnEachGeneration"))
            printBestOnEachGeneration = o.getBoolean("printBestOnEachGeneration");

        if(o.has("printEndingInformation"))
            printEndingInformation = o.getBoolean("printEndingInformation");

        if(o.has("printBestFitnessEvolution"))
            printBestFitnessEvolution = o.getBoolean("printBestFitnessEvolution");

        try {
            cantChildren = o.getInt("cantChildren");
        }  catch (Exception e) {
            System.err.println("Error reading children quantity configuration.");
            System.exit(-1);
        }


        try {
            mutationMethods = new ArrayList<>();
            if(o.has("mutationMethods")) {
                JSONArray mutationMethodsJson = o.getJSONArray("mutationMethods");
                for (int i = 0; i < mutationMethodsJson.length(); i++) {
                    JSONObject jsonM = mutationMethodsJson.getJSONObject(i);

                    String name = jsonM.getString("name");
                    float probability = jsonM.getFloat("probability");
                    if(probability > 1 || probability <= 0)
                        throw new IllegalArgumentException("Mutation probability must belong in (0, 1]");

                    Mutation m = (Mutation) Main.mutations.get(name).getDeclaredConstructor(float.class).newInstance(probability);
                    mutationMethods.add(m);
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading mutation configuration.");
            System.exit(-1);
        }

        try {
            JSONObject crossoverJSON = o.getJSONObject("crossoverMethod");
            String crossoverName = crossoverJSON.getString("name");
            if(crossoverName.equals("uniform")) {
                JSONObject parameters = crossoverJSON.getJSONObject("parameters");
                float exchangeProbability = parameters.getFloat("exchangeProbability");
                if(exchangeProbability > 1 || exchangeProbability <= 0)
                    throw new IllegalArgumentException("Uniform Crossover's exchange probability must belong in (0, 1]");

                crossoverMethod = (Crossover)Main.crossovers.get(crossoverName).getDeclaredConstructor(float.class).newInstance(exchangeProbability);
            } else {
                crossoverMethod = (Crossover) Main.crossovers.get(crossoverName).getDeclaredConstructor().newInstance();
            }
        } catch (Exception e) {
            System.err.println("Error reading crossover configuration.");
            System.exit(-1);
        }

        try {
            JSONObject parentSelectionMethodsJson = o.getJSONObject("parentSelectionMethods");
            parentSelector1 = getSelector(parentSelectionMethodsJson, 1);
            parentSelector2 = getSelector(parentSelectionMethodsJson, 2);
            parentSelector1Proportion = parentSelector2 == null ? 1 : parentSelectionMethodsJson.getFloat("method1Proportion");

            if(parentSelector1Proportion > 1 || parentSelector1Proportion < 0)
                throw new IllegalArgumentException("Parent selection proportion must belong in [0, 1]");
        } catch (Exception e) {
            System.err.println("Error reading parent selector configuration.");
            System.exit(-1);
        }

        try {
            JSONObject nextGenerationSelectionMethodsJson = o.getJSONObject("nextGenerationSelectionMethods");
            nextGenerationSelector1 = getSelector(nextGenerationSelectionMethodsJson, 1);
            nextGenerationSelector2 = getSelector(nextGenerationSelectionMethodsJson, 2);
            nextGenerationSelector1Proportion = nextGenerationSelector2 == null ? 1 : nextGenerationSelectionMethodsJson.getFloat("method1Proportion");
            if(nextGenerationSelector1Proportion > 1 || nextGenerationSelector1Proportion < 0)
                throw new IllegalArgumentException("Next gen selection proportion must belong in [0, 1]");

            String fillMethod = nextGenerationSelectionMethodsJson.getString("fillMethod");
            if(fillMethod.equals("all"))
                fillAll = true;
            else if(fillMethod.equals("parent"))
                fillAll = false;
            else
                throw new IllegalArgumentException("Fill Method must be either 'all' or 'parent'");
        } catch (Exception e) {
            System.err.println("Error reading next gen selector configuration.");
            System.exit(-1);
        }

        try {
            finishCriteria = new ArrayList<>();
            JSONArray finishCriteriaJson = o.getJSONArray("finishCriteria");
            for(int i = 0; i < finishCriteriaJson.length(); i++) {
                JSONObject jsonM = finishCriteriaJson.getJSONObject(i);

                String name = jsonM.getString("name");
                JSONObject parameters = jsonM.getJSONObject("parameters");
                FinishCriteria f = null;
                if(name.equals("time")) {
                    long durationSeconds = parameters.getLong("durationSeconds");
                    if(durationSeconds <= 0)
                        throw new IllegalArgumentException("durationSeconds must be greater than 0");

                    f = (FinishCriteria)Main.finishCriteria.get(name).getDeclaredConstructor(long.class).newInstance(durationSeconds);
                } else if(name.equals("generationsCount")) {
                    int maxGenerations = parameters.getInt("maxGenerations");
                    if(maxGenerations <= 0)
                        throw new IllegalArgumentException("maxGenerations must be greater than 0");
                    f = (FinishCriteria)Main.finishCriteria.get(name).getDeclaredConstructor(int.class).newInstance(maxGenerations);
                } else if(name.equals("content")) {
                    int generationsWithoutImprovementToFinish = parameters.getInt("generationsWithoutImprovementToFinish");
                    if(generationsWithoutImprovementToFinish <= 0)
                        throw new IllegalArgumentException("generationsWithoutImprovementToFinish must be greater than 0");
                    f = (FinishCriteria)Main.finishCriteria.get(name).getDeclaredConstructor(int.class).newInstance(generationsWithoutImprovementToFinish);
                } else if(name.equals("acceptableSolution")) {
                    double acceptableFitness = parameters.getDouble("acceptableFitness");
                    f = (FinishCriteria)Main.finishCriteria.get(name).getDeclaredConstructor(double.class).newInstance(acceptableFitness);
                } else if(name.equals("structure")) {
                    int numberOfSubjectsToCompare = parameters.getInt("numberOfSubjectsToCompare");
                    int comparableGenerationsBeforeFinish = parameters.getInt("comparableGenerationsBeforeFinish");
                    f = (FinishCriteria)Main.finishCriteria.get(name).getDeclaredConstructor(int.class, int.class).newInstance(numberOfSubjectsToCompare, comparableGenerationsBeforeFinish);
                }
                finishCriteria.add(f);
            }
        } catch (Exception e) {
            System.err.println("Error reading criteria configuration.");
            System.exit(-1);
        }
    }

    private Selector getSelector(JSONObject parentSelectionMethodsJson, int index) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //Method 1
        if(index == 2 && !parentSelectionMethodsJson.has("method" + index))
            return null;

        JSONObject parentSelectionMethod1Json = parentSelectionMethodsJson.getJSONObject("method" + index);
        if(parentSelectionMethod1Json == null)
            return null;

        String name = parentSelectionMethod1Json.getString("name");
        Selector s = null;
        if(name.equals("boltzmann")) {
            JSONObject parameters = parentSelectionMethod1Json.getJSONObject("parameters");
            double T0 = parameters.getDouble("T0");
            double Tc = parameters.getDouble("Tc");
            if(T0 <= 0 || Tc <= 0)
                throw new IllegalArgumentException("T0 and Tc must be greater than 0");

            s = (Selector) Main.selectors.get(name).getDeclaredConstructor(double.class, double.class).newInstance(T0, Tc);
        } else if(name.equals("deterministicTournaments")) {
            JSONObject parameters = parentSelectionMethod1Json.getJSONObject("parameters");
            int randomSubsetSize = parameters.getInt("randomSubsetSize");
            if(randomSubsetSize <= 0)
                throw new IllegalArgumentException("randomSubsetSize must be greater than 0");

            s = (Selector) Main.selectors.get(name).getDeclaredConstructor(int.class).newInstance(randomSubsetSize);
        } else if(name.equals("probabilisticTournaments")) {
            JSONObject parameters = parentSelectionMethod1Json.getJSONObject("parameters");
            float threshold = parameters.getFloat("threshold");
            if(threshold < 0.5 || threshold > 1)
                throw new IllegalArgumentException("threshold must belong in [0.5, 1]");

            s = (Selector) Main.selectors.get(name).getDeclaredConstructor(float.class).newInstance(threshold);
        } else {
            s = (Selector) Main.selectors.get(name).getConstructor().newInstance();
        }

        return s;
    }
}
