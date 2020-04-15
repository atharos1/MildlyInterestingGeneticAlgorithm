package main;

import main.Character.Character;
import main.Character.CharacterFactory;
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
        int MAX_GENERATIONS = 10000;

        int N = 100;
        int CANT_CHILDREN = 70;

        float MUTATION_PROBABILITY = 0.2f;

        boolean FILL_ALL = true;

        float PARENT_SELECTOR_1_PROBABILITY = 0.5f; //a
        String PARENT_SELECTOR_1_NAME = "elite";
        String PARENT_SELECTOR_2_NAME = "universal";

        String MUTATION_NAME = "limitedmultigene";

        String CROSSOVER_NAME = "twopoints";

        float REPLACEMENT_SELECTOR_1_PROBABILITY = 0.5f; //b
        String REPLACEMENT_SELECTOR_1_NAME = "roulette";
        String REPLACEMENT_SELECTOR_2_NAME = "ranking";
        //PARÁMETROS

        //Busca archivos TSV en "./args[0]". Cada archivo representa un tipo de objeto. Se puede equipar uno de cada tipo.
        try (Stream<Path> paths = Files.walk(Paths.get(args[0]))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(CharacterFactory::registerItemType);
        }

        //LISTADO DE MÉTODOS DE SELECCIÓN
        Map<String, Selector> selectors = new HashMap<>();
        selectors.put("elite", new EliteSelector());
        selectors.put("roulette", new RouletteSelector());
        selectors.put("universal", new UniversalSelector());
        selectors.put("ranking", new RankingSelector());

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

        //GENERACIÓN DE LA POBLACIÓN INICIAL
        List<Character> population = new ArrayList<>();
        //TODO Se permiten repetidos?
        for (int i = 0; i < N; i++)
            population.add(CharacterFactory.getRandomCharacter());





        Selector parentSelector1 = selectors.get(PARENT_SELECTOR_1_NAME);
        Selector parentSelector2 = selectors.get(PARENT_SELECTOR_2_NAME);

        Mutation mutation = mutations.get(MUTATION_NAME);
        mutation.setProbability(MUTATION_PROBABILITY);

        Crossover crossoverMethod = crossovers.get(CROSSOVER_NAME);

        Selector replacementSelector1 = selectors.get(REPLACEMENT_SELECTOR_1_NAME);
        Selector replacementSelector2 = selectors.get(REPLACEMENT_SELECTOR_2_NAME);

        for (int generation = 0; generation < MAX_GENERATIONS; generation++) {
            //Selecciono CANT_CHILDREN padres
            //TODO un mismo padre se puede reproducir por los dos metodos? Es decir, Method2 evalua a los padres que eligio Method1? Mismo vale para seleccion
            List<Character> parents = parentSelector1.select(population, (int) Math.ceil(CANT_CHILDREN * PARENT_SELECTOR_1_PROBABILITY));
            //population.removeAll(parents);
            parents.addAll(parentSelector2.select(population, (int) Math.floor(CANT_CHILDREN * (1 - PARENT_SELECTOR_1_PROBABILITY))));
            Collections.shuffle(parents);

            //TODO Un personaje que no fue elegido como padre nunca puede reproducirse?
            //TODO Puede ser que cantidad de hijos (K) > N? Que sentido tiene FILL-PARENT?
            List<Character> nextGenCandidates = new ArrayList<>();

            //Genero CANT_CHILDREN hijos. Si CANT_CHILDREN es impar, el último se reproduce con un elemento al azar
            for (int i = 0; i < CANT_CHILDREN; i += 2) {
                Character parent1 = parents.get(i);
                Character parent2 = (i != CANT_CHILDREN - 1) ? parents.get(i+1) : parents.get(CharacterFactory.random.nextInt(CANT_CHILDREN - 1));
                List<Character> children = crossoverMethod.cross(parent1, parent2);

                //Muto
                children.replaceAll(mutation::mutate);

                //Si CANT_CHILDREN impar, agrego solo el primero de los hijos
                if(i != CANT_CHILDREN - 1)
                    nextGenCandidates.addAll(children);
                else
                    nextGenCandidates.add((children.get(0)));
            }

            if(FILL_ALL) {
                nextGenCandidates.addAll(parents);
                population = replacementSelector1.select(nextGenCandidates, (int) Math.ceil(N * REPLACEMENT_SELECTOR_1_PROBABILITY));
                //nextGenCandidates.removeAll(population);
                population.addAll(replacementSelector2.select(nextGenCandidates, (int) Math.floor(N * (1 - REPLACEMENT_SELECTOR_1_PROBABILITY))));
            } else {
                population = replacementSelector1.select(nextGenCandidates, (int) Math.ceil(CANT_CHILDREN * REPLACEMENT_SELECTOR_1_PROBABILITY));
                //nextGenCandidates.removeAll(population);
                population.addAll(replacementSelector2.select(nextGenCandidates, (int) Math.floor(CANT_CHILDREN * (1 - REPLACEMENT_SELECTOR_1_PROBABILITY))));

                population.addAll(replacementSelector1.select(parents, (int) Math.floor((N-CANT_CHILDREN) * REPLACEMENT_SELECTOR_1_PROBABILITY)));
                //parents.removeAll(population);
                population.addAll(replacementSelector2.select(parents, (int) Math.floor((N-CANT_CHILDREN) * (1 - REPLACEMENT_SELECTOR_1_PROBABILITY))));
            }
        }

        System.out.println();
        System.out.println();
        System.out.println();

        Collections.sort(population);

        System.out.println(population.get(0).toString());
    }
}