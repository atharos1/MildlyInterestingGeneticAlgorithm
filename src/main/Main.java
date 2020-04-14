package main;

import main.Crossover.Crossover;
import main.Crossover.SinglePoint;
import main.Mutations.Mutation;
import main.Mutations.SingleGene;
import main.Selection.Elite;
import main.Selection.Selector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
        int N = 4;
        int MAX_GENERATIONS = 10;

        boolean FILL_ALL = true;

        float PARENT_SELECTOR_1_PROBABILITY = 0.5f; //a
        String PARENT_SELECTOR_1_NAME = "elite";
        String PARENT_SELECTOR_2_NAME = "elite";

        String MUTATION_NAME = "gene";

        String CROSSOVER_NAME = "singlepoint";

        float REPLACEMENT_SELECTOR_1_PROBABILITY = 0.5f; //b
        String REPLACEMENT_SELECTOR_1_NAME = "elite";
        String REPLACEMENT_SELECTOR_2_NAME = "elite";

        try (Stream<Path> paths = Files.walk(Paths.get(args[0]))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(CharacterFactory::registerItemType);
        }

        List<Character> population = new ArrayList<>();

        //TODO Se permiten repetidos?
        for (int i = 0; i < N; i++)
            population.add(CharacterFactory.Random());

        /*for(Character c : population)
            System.out.println(c.calculateFitness());*/

        Map<String, Selector> selectors = new HashMap<>();
        selectors.put("elite", new Elite());

        Map<String, Crossover> crossovers = new HashMap<>();
        crossovers.put("singlepoint", new SinglePoint());

        Map<String, Mutation> mutations = new HashMap<>();
        mutations.put("gene", new SingleGene());

        Selector parentSelector1 = selectors.get(PARENT_SELECTOR_1_NAME);
        Selector parentSelector2 = selectors.get(PARENT_SELECTOR_2_NAME);

        Mutation mutation = mutations.get(MUTATION_NAME);

        Crossover crossoverMethod = crossovers.get(CROSSOVER_NAME);

        Selector replacementSelector1 = selectors.get(REPLACEMENT_SELECTOR_1_NAME);
        Selector replacementSelector2 = selectors.get(REPLACEMENT_SELECTOR_2_NAME);

        for (int generation = 0; generation < MAX_GENERATIONS; generation++) {
            Collections.sort(population);
            System.out.println("Máximo fitness de la generación #" + (generation) + ": " + population.get(0).calculateFitness());

            //Selecciono padres
            //TODO un mismo padre se puede reproducir por los dos metodos? Es decir, Method2 evalua a los padres que eligio Method1? Mismo vale para seleccion
            List<Character> parents = parentSelector1.select(population, (int) Math.ceil(N * PARENT_SELECTOR_1_PROBABILITY));
            population.removeAll(parents);
            parents.addAll(parentSelector2.select(population, (int) Math.floor(N * (1 - PARENT_SELECTOR_1_PROBABILITY))));

            //TODO Un personaje que no fue elegido como padre nunca puede reproducirse?
            //TODO Puede ser que cantidad de hijos (K) > N? Que sentido tiene FILL-PARENT?
            List<Character> children = FILL_ALL ? parents : new ArrayList<>();

            //TODO si N es impar? El ultimo con quien se reproduce?
            for (int i = 0; i < N; i += 2) {
                //TODO Cada cruza genera 2 hijos? Los padres invertidos muatuamente segun el criterio? O solo tienen dos hijos?
                //TODO se mutan todos los hijos, o con una determinada probabilidad?
                Character child1 = crossoverMethod.cross(parents.get(i), parents.get(i + 1));
                //child1 = mutation.mutate(child1);

                Character child2 = crossoverMethod.cross(parents.get(i + 1), parents.get(i));
                //child2 = mutation.mutate(child2);

                children.add(child1);
                children.add(child2);
            }

            population = replacementSelector1.select(children, (int) Math.ceil(N * REPLACEMENT_SELECTOR_1_PROBABILITY));
            children.removeAll(population);
            population.addAll(replacementSelector2.select(children, (int) Math.floor(N * (1 - REPLACEMENT_SELECTOR_1_PROBABILITY))));
        }

        System.out.println();
        System.out.println();
        System.out.println();

        Collections.sort(population);

        System.out.println(population.get(0).toString());
    }
}