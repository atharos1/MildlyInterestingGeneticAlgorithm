package main;

import main.SubjectImplementation.*;
import main.SubjectImplementation.Character;
import main.Crossover.*;
import main.Mutations.*;
import main.Selection.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
        //PARÁMETROS
        String CLASS_NAME = "archer";

        int MAX_GENERATIONS = 10000;

        int N = 100;
        int CANT_CHILDREN = 150;

        float PARENT_SELECTOR_1_PROBABILITY = 0.6f; //a
        String PARENT_SELECTOR_1_NAME = "elite";
        String PARENT_SELECTOR_2_NAME = "ranking";

        float MUTATION_PROBABILITY = 0.4f;
        String MUTATION_NAME = "singlegene";

        String CROSSOVER_NAME = "uniform";

        boolean FILL_ALL = false;
        float REPLACEMENT_SELECTOR_1_PROBABILITY = 0.8f; //b
        String REPLACEMENT_SELECTOR_1_NAME = "roulette";
        String REPLACEMENT_SELECTOR_2_NAME = "universal";
        //PARÁMETROS

        //Busca archivos TSV en "./args[0]". Cada archivo representa un tipo de objeto. Se puede equipar uno de cada tipo.
        try (Stream<Path> paths = Files.walk(Paths.get(args[0]))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(Item::registerItemType);
        }

        //LISTADO DE CLASES
        Map<String, ClassEnum> classes = new HashMap<>();
        for(ClassEnum c : ClassEnum.values())
            classes.put(c.name().toLowerCase(), c);

        //LISTADO DE MÉTODOS DE SELECCIÓN
        Map<String, Selector> selectors = new HashMap<>();
        selectors.put("elite", new EliteSelector());
        selectors.put("roulette", new RouletteSelector());
        selectors.put("universal", new UniversalSelector());
        selectors.put("ranking", new RankingSelector());
        selectors.put("boltzmann", new BoltzmannSelector());

        //LISTADO DE MÉTODOS DE CRUZA
        Map<String, Crossover> crossovers = new HashMap<>();
        crossovers.put("singlepoint", new SinglePointCrossover());
        crossovers.put("twopoints", new TwoPointsCrossover());
        crossovers.put("anular", new AnularCrossover());
        crossovers.put("uniform", new UniformCrossover());

        //LISTADO DE MÉTODOS DE MUTACIÓN
        Map<String, Mutation> mutations = new HashMap<>();
        mutations.put("none", new NoMutation());
        mutations.put("singlegene", new SingleGeneMutation());
        mutations.put("limitedmultigene", new LimitedMultiGeneMutation());
        mutations.put("uniformmultigene", new UniformMultiGeneMutation());
        mutations.put("complete", new CompleteMutation());

        //Witness explicita la clase que implementa GeneticSubject
        GeneticSubject witness = new Character();
        ClassEnum charClass = classes.get(CLASS_NAME);
        witness.setFixedProperty(Character.PropertiesEnum.CLASS.val, charClass);
        //GENERACIÓN DE LA POBLACIÓN INICIAL
        List<GeneticSubject> population = new ArrayList<>();
        for (int i = 0; i < N; i++)
            population.add(witness.getRandom());

        Selector parentSelector1 = selectors.get(PARENT_SELECTOR_1_NAME);
        Selector parentSelector2 = selectors.get(PARENT_SELECTOR_2_NAME);

        Mutation mutation = mutations.get(MUTATION_NAME);
        mutation.setProbability(MUTATION_PROBABILITY);

        Crossover crossoverMethod = crossovers.get(CROSSOVER_NAME);

        Selector replacementSelector1 = selectors.get(REPLACEMENT_SELECTOR_1_NAME);
        Selector replacementSelector2 = selectors.get(REPLACEMENT_SELECTOR_2_NAME);

        for (int generation = 0; generation < MAX_GENERATIONS; generation++) {
            //Selecciono CANT_CHILDREN padres
            List<GeneticSubject> parentList = parentSelector1.select(population, (int) Math.ceil(CANT_CHILDREN * PARENT_SELECTOR_1_PROBABILITY));
            parentList.addAll(parentSelector2.select(population, (int) Math.floor(CANT_CHILDREN * (1 - PARENT_SELECTOR_1_PROBABILITY))));
            Collections.shuffle(parentList);

            List<GeneticSubject> nextGenCandidates = new ArrayList<>();

            //Genero CANT_CHILDREN hijos. Si CANT_CHILDREN es impar, el último se reproduce con un elemento al azar
            int parentIndex = 0;
            GeneticSubject[] selectedParents = new GeneticSubject[2];
            while(nextGenCandidates.size() < CANT_CHILDREN) {
                for(int i = 0; i < selectedParents.length; i++) {
                    if(parentIndex == parentList.size()) {
                        parentIndex = 0;
                        Collections.shuffle(parentList);
                    }
                    selectedParents[i] = parentList.get(parentIndex);
                    parentIndex++;
                }

                List<GeneticSubject> children = crossoverMethod.cross(selectedParents[0], selectedParents[1]);

                //Muto
                children.replaceAll(mutation::mutate);

                //Si CANT_CHILDREN impar, agrego solo el primero de los hijos
                if(nextGenCandidates.size() < CANT_CHILDREN - 1)
                    nextGenCandidates.addAll(children);
                else
                    nextGenCandidates.add((children.get(0)));
            }


            if(FILL_ALL) {
                nextGenCandidates.addAll(population);
                population = replacementSelector1.select(nextGenCandidates, (int) Math.ceil(N * REPLACEMENT_SELECTOR_1_PROBABILITY));
                population.addAll(replacementSelector2.select(nextGenCandidates, (int) Math.floor(N * (1 - REPLACEMENT_SELECTOR_1_PROBABILITY))));
            } else {
                population = replacementSelector1.select(nextGenCandidates, (int) Math.ceil(CANT_CHILDREN * REPLACEMENT_SELECTOR_1_PROBABILITY));
                population.addAll(replacementSelector2.select(nextGenCandidates, (int) Math.floor(CANT_CHILDREN * (1 - REPLACEMENT_SELECTOR_1_PROBABILITY))));

                if(CANT_CHILDREN < N) {
                    population.addAll(replacementSelector1.select(parentList, (int) Math.floor((N - CANT_CHILDREN) * REPLACEMENT_SELECTOR_1_PROBABILITY)));
                    population.addAll(replacementSelector2.select(parentList, (int) Math.floor((N - CANT_CHILDREN) * (1 - REPLACEMENT_SELECTOR_1_PROBABILITY))));
                }
            }
        }
        
        Collections.sort(population);

        System.out.println(population.get(0).toString());
    }
}