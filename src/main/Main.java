package main;

import main.FinishCriteria.*;
import main.SubjectImplementation.*;
import main.SubjectImplementation.Character;
import main.Crossover.*;
import main.Mutations.*;
import main.Selection.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

    public static Map<String, Class> finishCriteria = null;
    public static Map<String, ClassEnum> classes = null;
    public static Map<String, Class> selectors = null;
    public static Map<String, Class> crossovers = null;
    public static Map<String, Class> mutations = null;

    static void loadGeneticAlgorithmDependencies() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        finishCriteria = new HashMap<>();
        finishCriteria.put("time", TimeFinishCriteria.class);
        finishCriteria.put("generationsCount", GenerationsCountFinishCriteria.class);
        finishCriteria.put("acceptableSolution", AcceptableSolutionFinishCriteria.class);
        finishCriteria.put("content", ContentFinishCriteria.class);

        classes = new HashMap<>();
        for(ClassEnum c : ClassEnum.values())
            classes.put(c.name().toLowerCase(), c);

        selectors = new HashMap<>();
        selectors.put("elite", EliteSelector.class);
        selectors.put("roulette", RouletteSelector.class);
        selectors.put("universal", UniversalSelector.class);
        selectors.put("ranking", RankingSelector.class);
        selectors.put("boltzmann", BoltzmannSelector.class);

        crossovers = new HashMap<>();
        crossovers.put("singlePoint", SinglePointCrossover.class);
        crossovers.put("twoPoints", TwoPointsCrossover.class);
        crossovers.put("anular", AnularCrossover.class);
        crossovers.put("uniform", UniformCrossover.class);

        mutations = new HashMap<>();
        mutations.put("singleGene", SingleGeneMutation.class);
        mutations.put("limitedMultiGene", LimitedMultiGeneMutation.class);
        mutations.put("uniformMultiGene", UniformMultiGeneMutation.class);
        mutations.put("complete", CompleteMutation.class);
    }



    static void loadConfiguration(String connfigurationFile) {

    }

    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //PARÁMETROS
        String CLASS_NAME = "archer";

        loadGeneticAlgorithmDependencies();

        Configuration configuration = new Configuration(args[0]);

        //Witness explicita la clase que implementa GeneticSubject
        GeneticSubject witness = new Character();
        witness.loadConfigurationFromFile(args[1]);

        //GENERACIÓN DE LA POBLACIÓN INICIAL
        List<GeneticSubject> population = new ArrayList<>();
        for (int i = 0; i < configuration.generationSize; i++)
            population.add(witness.getRandom());

        boolean shouldContinue = true;
        while(shouldContinue) {
            //Selecciono CANT_CHILDREN padres
            List<GeneticSubject> parentList = configuration.parentSelector1.select(population, (int) Math.ceil(configuration.cantChildren * configuration.parentSelector1Proportion));

            if(configuration.parentSelector2 != null)
                parentList.addAll(configuration.parentSelector2.select(population, (int) Math.floor(configuration.cantChildren * (1 - configuration.parentSelector1Proportion))));
            Collections.shuffle(parentList);

            List<GeneticSubject> nextGenCandidates = new ArrayList<>();

            //Genero CANT_CHILDREN hijos. Si CANT_CHILDREN es impar, el último se reproduce con un elemento al azar
            int parentIndex = 0;
            GeneticSubject[] selectedParents = new GeneticSubject[2];
            while(nextGenCandidates.size() < configuration.cantChildren) {
                for(int i = 0; i < selectedParents.length; i++) {
                    if(parentIndex == parentList.size()) {
                        parentIndex = 0;
                        Collections.shuffle(parentList);
                    }
                    selectedParents[i] = parentList.get(parentIndex);
                    parentIndex++;
                }

                List<GeneticSubject> children = configuration.crossoverMethod.cross(selectedParents[0], selectedParents[1]);

                for(Mutation mutation : configuration.mutationMethods)
                    children.replaceAll(mutation::mutate);

                //Si CANT_CHILDREN impar, agrego solo el primero de los hijos
                if(nextGenCandidates.size() < configuration.cantChildren - 1)
                    nextGenCandidates.addAll(children);
                else
                    nextGenCandidates.add((children.get(0)));
            }


            if(configuration.fillAll) {
                nextGenCandidates.addAll(population);
                population = configuration.nextGenerationSelector1.select(nextGenCandidates, (int) Math.ceil(configuration.generationSize * configuration.nextGenerationSelector1Proportion));

                if(configuration.nextGenerationSelector2 != null)
                    population.addAll(configuration.nextGenerationSelector2.select(nextGenCandidates, (int) Math.floor(configuration.generationSize * (1 - configuration.nextGenerationSelector1Proportion))));
            } else {
                population = configuration.nextGenerationSelector1.select(nextGenCandidates, (int) Math.ceil(configuration.cantChildren * configuration.nextGenerationSelector1Proportion));

                if(configuration.nextGenerationSelector2 != null)
                    population.addAll(configuration.nextGenerationSelector2.select(nextGenCandidates, (int) Math.floor(configuration.cantChildren * (1 - configuration.nextGenerationSelector1Proportion))));

                if(configuration.cantChildren < configuration.generationSize) {
                    population.addAll(configuration.nextGenerationSelector1.select(parentList, (int) Math.floor((configuration.generationSize - configuration.cantChildren) * configuration.nextGenerationSelector1Proportion)));

                    if(configuration.nextGenerationSelector2 != null)
                        population.addAll(configuration.nextGenerationSelector2.select(parentList, (int) Math.floor((configuration.generationSize - configuration.cantChildren) * (1 - configuration.nextGenerationSelector1Proportion))));
                }
            }

            for(FinishCriteria f : configuration.finishCriteria) {
                if (f.shoundFinish(population)) {
                    shouldContinue = false;
                    break;
                }
            }
        }

        Collections.sort(population);

        System.out.println(population.get(0).toString());
    }
}