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

    public Configuration(String configurationFile) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String json = new String(Files.readAllBytes(Paths.get(configurationFile)));
        JSONObject o = new JSONObject(json);

        generationSize = o.getInt("generationSize");

        cantChildren = o.getInt("cantChildren");

        mutationMethods = new ArrayList<>();
        JSONArray mutationMethodsJson = o.getJSONArray("mutationMethods");
        for(int i = 0; i < mutationMethodsJson.length(); i++) {
            JSONObject jsonM = mutationMethodsJson.getJSONObject(i);

            String name = jsonM.getString("name");
            float probability = jsonM.getFloat("probability");
            Mutation m = (Mutation)Main.mutations.get(name).getDeclaredConstructor(float.class).newInstance(probability);
            mutationMethods.add(m);
        }

        JSONObject crossoverJSON = o.getJSONObject("crossoverMethod");
        String crossoverName = crossoverJSON.getString("name");
        if(crossoverName.equals("uniform")) {
            JSONObject parameters = crossoverJSON.getJSONObject("parameters");
            float exchangeProbability = parameters.getFloat("exchangeProbability");
            crossoverMethod = (Crossover)Main.crossovers.get(crossoverName).getDeclaredConstructor(float.class).newInstance(exchangeProbability);
        } else {
            crossoverMethod = (Crossover) Main.crossovers.get(crossoverName).getDeclaredConstructor().newInstance();
        }

        JSONObject parentSelectionMethodsJson = o.getJSONObject("parentSelectionMethods");
        parentSelector1 = getSelector(parentSelectionMethodsJson, 1);
        parentSelector2 = getSelector(parentSelectionMethodsJson, 2);
        parentSelector1Proportion = parentSelector2 == null ? 1 : parentSelectionMethodsJson.getFloat("method1Proportion");

        JSONObject nextGenerationSelectionMethodsJson = o.getJSONObject("nextGenerationSelectionMethods");
        nextGenerationSelector1 = getSelector(nextGenerationSelectionMethodsJson, 1);
        nextGenerationSelector2 = getSelector(nextGenerationSelectionMethodsJson, 2);
        nextGenerationSelector1Proportion = nextGenerationSelector2 == null ? 1 : nextGenerationSelectionMethodsJson.getFloat("method1Proportion");
        fillAll = nextGenerationSelectionMethodsJson.getString("fillMethod") == "all";

        finishCriteria = new ArrayList<>();
        JSONArray finishCriteriaJson = o.getJSONArray("finishCriteria");
        for(int i = 0; i < finishCriteriaJson.length(); i++) {
            JSONObject jsonM = finishCriteriaJson.getJSONObject(i);

            String name = jsonM.getString("name");
            JSONObject parameters = jsonM.getJSONObject("parameters");
            FinishCriteria f = null;
            if(name.equals("time")) {
                long durationSeconds = parameters.getLong("durationSeconds");
                f = (FinishCriteria)Main.finishCriteria.get(name).getDeclaredConstructor(long.class).newInstance(durationSeconds);
            } else if(name.equals("generationsCount")) {
                int maxGenerations = parameters.getInt("maxGenerations");
                f = (FinishCriteria)Main.finishCriteria.get(name).getDeclaredConstructor(int.class).newInstance(maxGenerations);
            } else if(name.equals("content")) {
                int generationsWithoutImprovementToFinish = parameters.getInt("generationsWithoutImprovementToFinish");
                f = (FinishCriteria)Main.finishCriteria.get(name).getDeclaredConstructor(int.class).newInstance(generationsWithoutImprovementToFinish);
            } else if(name.equals("acceptableSolution")) {
                double acceptableFitness = parameters.getDouble("acceptableFitness");
                f = (FinishCriteria)Main.finishCriteria.get(name).getDeclaredConstructor(double.class).newInstance(acceptableFitness);
            }
            finishCriteria.add(f);
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

            s = (Selector) Main.selectors.get(name).getDeclaredConstructor(double.class, double.class).newInstance(T0, Tc);
        } else {
            s = (Selector) Main.selectors.get(name).getConstructor().newInstance();
        }

        return s;
    }
}
