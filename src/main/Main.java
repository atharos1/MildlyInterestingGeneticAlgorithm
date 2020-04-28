package main;

import main.FinishCriteria.*;
import main.Crossover.*;
import main.Mutations.*;
import main.Selection.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import main.SubjectImplementation.CharacterCreator.Character;
import main.SubjectImplementation.CharacterCreator.ClassEnum;
import main.SubjectImplementation.StringEvolution.Message;

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
        finishCriteria.put("structure", StructureFinishCriteria.class);

        classes = new HashMap<>();
        for(ClassEnum c : ClassEnum.values())
            classes.put(c.name().toLowerCase(), c);

        selectors = new HashMap<>();
        selectors.put("elite", EliteSelector.class);
        selectors.put("roulette", RouletteSelector.class);
        selectors.put("universal", UniversalSelector.class);
        selectors.put("ranking", RankingSelector.class);
        selectors.put("boltzmann", BoltzmannSelector.class);
        selectors.put("deterministicTournaments", DeterministicTournaments.class);
        selectors.put("probabilisticTournaments", ProbabilisticTournaments.class);

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

    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if(args.length < 1) {
            System.err.println("Algorithm configuration file path is not optional.");
            System.exit(-1);
        }

        System.out.println("Loading dependencies...");
        loadGeneticAlgorithmDependencies();
        System.out.println("Dependencies loaded.\n");

        System.out.println("Loading algorithm configuration...");
        Configuration configuration = new Configuration(args[0]);
        System.out.println("Algorithm configuration loaded.\n");

        //Witness explicita la clase que implementa GeneticSubject
        //GeneticSubject witness = new Message();
        GeneticSubject witness = new Character();
        if(args.length >= 2) {
            System.out.println("Loading implementation-specific GeneticSubject configuration...");
            witness.loadConfigurationFromFile(args[1]);
            System.out.println("Implementation-specific GeneticSubject configuration loaded.\n");
        }

        System.out.println("Creating random initial population...");
        //GENERACIÓN DE LA POBLACIÓN INICIAL
        List<GeneticSubject> population = new ArrayList<>();
        for (int i = 0; i < configuration.generationSize; i++)
            population.add(witness.getRandom());
        System.out.println("Random initial population created.\n");

        System.out.println("Evolution started.\n");
        long processingStartTime = System.currentTimeMillis();

        long currGen = 0;
        boolean shouldContinue = true;
        while(shouldContinue) {
            if(configuration.printBestOnEachGeneration) {
                GeneticSubject bestSubject = Collections.min(population);
                System.out.println("Generation " + currGen + ". Best archived fitness: " + bestSubject.getFitness() + ".");
                System.out.println(bestSubject.toString());
            }

            //Selecciono configuration.cantChildren padres
            List<GeneticSubject> parentList = configuration.parentSelector1.select(population, (int) Math.ceil(configuration.cantChildren * configuration.parentSelector1Proportion));

            if(configuration.parentSelector2 != null)
                parentList.addAll(configuration.parentSelector2.select(population, (int) Math.floor(configuration.cantChildren * (1 - configuration.parentSelector1Proportion))));
            Collections.shuffle(parentList);

            List<GeneticSubject> nextGenCandidates = new ArrayList<>();

            //Genero configuration.cantChildren hijos. Si CANT_CHILDREN es impar, el último se reproduce con un elemento al azar
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

                List<GeneticSubject> generatedChildren = configuration.crossoverMethod.cross(selectedParents[0], selectedParents[1]);

                for(Mutation mutation : configuration.mutationMethods)
                    generatedChildren.replaceAll(mutation::mutate);

                //Si configuration.cantChildren impar, agrego solo el primero de los hijos
                if(nextGenCandidates.size() < configuration.cantChildren - 1)
                    nextGenCandidates.addAll(generatedChildren);
                else
                    nextGenCandidates.add((generatedChildren.get(0)));
            }

            if(configuration.fillAll) {
                nextGenCandidates.addAll(population);
                population = configuration.nextGenerationSelector1.select(nextGenCandidates, (int) Math.ceil(configuration.generationSize * configuration.nextGenerationSelector1Proportion));

                if(configuration.nextGenerationSelector2 != null)
                    population.addAll(configuration.nextGenerationSelector2.select(nextGenCandidates, (int) Math.floor(configuration.generationSize * (1 - configuration.nextGenerationSelector1Proportion))));
            } else {
                int selectedFromChildren = Math.min(configuration.cantChildren, configuration.generationSize);

                population = configuration.nextGenerationSelector1.select(nextGenCandidates, (int) Math.ceil(selectedFromChildren * configuration.nextGenerationSelector1Proportion));

                if(configuration.nextGenerationSelector2 != null)
                    population.addAll(configuration.nextGenerationSelector2.select(nextGenCandidates, (int) Math.floor(selectedFromChildren * (1 - configuration.nextGenerationSelector1Proportion))));

                if(selectedFromChildren < configuration.generationSize) {
                    population.addAll(configuration.nextGenerationSelector1.select(parentList, (int) Math.floor((configuration.generationSize - selectedFromChildren) * configuration.nextGenerationSelector1Proportion)));

                    if(configuration.nextGenerationSelector2 != null)
                        population.addAll(configuration.nextGenerationSelector2.select(parentList, (int) Math.floor((configuration.generationSize - selectedFromChildren) * (1 - configuration.nextGenerationSelector1Proportion))));
                }
            }

            for(FinishCriteria f : configuration.finishCriteria) {
                if (f.shouldFinish(population)) {
                    shouldContinue = false;

                    if(configuration.printBestOnEachGeneration)
                        System.out.println("--------------------------------\n");

                    System.out.println("Finishing criteria " + f.getName() + " was met. Processing stopped.");
                    System.out.println(f.toString());

                    break;
                }
            }

            currGen++;
        }

        GeneticSubject bestSubject = Collections.min(population);
        long endTime = System.currentTimeMillis() - processingStartTime;
        String endTimeString = String.format("%d minutes, %d seconds",
                TimeUnit.MILLISECONDS.toMinutes(endTime),
                TimeUnit.MILLISECONDS.toSeconds(endTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime))
        );
        System.out.println("Processed generations: " + currGen + ". Processing time: " + endTimeString + ".\nBest archived fitness: " + bestSubject.getFitness() + ".");

        System.out.println("Best individual in population:");
        System.out.println(bestSubject.toString());
    }
}