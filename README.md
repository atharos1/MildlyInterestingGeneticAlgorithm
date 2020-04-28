# Mildly Interesting Genetic Algorithm (MIGA)
This algorithm uses genetic evolution to optimize any object that extends the _GeneticSubject_ abstract class.

# Compile instructions
From the project's root folder, run:
> _$>_ mvn clean install

# Run instructions
Locate the diretory that contains the executable and run:
> _$>_ java -jar _executable-name_ _algorithm-config-file_ [_custom-implementation-config-file_]

# Configuration files
The algorithm accepts up to two json configuration files through parameters. 
The first has a defined structure and is used to configure algorithm specific settings. It can be shared across different _GeneticSubject_ implementations.
The second one is passed to the custom _GeneticSubject_ implementation's _loadConfigurationFromFile_ method, and should be defined and interpreted by the implementation itself.

## Algorithm configuration file
>_**generationSize**_ (Integer > 0): defines the size of every generation across the algorithm's execution.

>_**cantChildren**_ (Integer > 0): defines the number of parents selected to bred and the children they'll produce.

>_**printBestOnEachGeneration**_ (Boolean): defines wheter each generation's best subject must be printed.

>_**crossoverMethod**_ (Object): defines the crossover method to be used and it's parameters.
>>_**name**_ (String, ["_uniform_", | "_singlePoint_" | "_twoPoints_" | "_anular_"]): name of the method as defined by the _crossovers_ map in the _Main_ class method _loadGeneticAlgorithmDependencies_.
>> When _name_ equals _uniform_, an object named **_parameters_**, containing the following properties, is also required:
>>>_**exchangeProbability**_ (1 >= Float > 0): Defines the likeness of a gene being swapped by the _uniform_ crossover method.

> **_mutationMethods_** (Array of Objects): defines the mutation method's to be applied to each new subject upon creation. Each child object is defined as follows.
> > _**name**_ (String, [_singleGene_", "_uniformMultiGene_", "_complete_", "_limitedMultiGene_"]): name of the method as defined by the _mutations_ map in the _Main_ class method _loadGeneticAlgorithmDependencies_.
>
> >_**probability**_ (1 >= Float > 0): Defines the likeness of the mutation method executing when called upon.

> **_parentSelectionMethods_** (Object): defines up to two selection methods that will be used to select parents before breeding.
> > _**method1**_ (Object): defines the first of the potentially two methods to be used.
> > > _**name**_ (String, ["_elite_", "_roulette_", "_ranking_", "_boltzmann_", "_universal_", "_deterministicTournaments_", "_probabilisticTournaments_"]): name of the method as defined by the _selectors_ map in the _Main_ class method _loadGeneticAlgorithmDependencies_.
>> When _name_ equals _boltzmann_, an object named **_parameters_**, containing the following properties, is also required:
>>>>_**T0**_ (Double > 0): Value used for the decreasing temperature function defined inside the _BoltzmannSelector_ class.
>>>
>>>>_**Tc**_ (Double > 0): Value used for the decreasing temperature function defined inside the _BoltzmannSelector_ class.
>>>
>>> When _name_ equals _deterministicTournaments_, an object named **_parameters_**, containing the following properties, is also required:
>>>>_**randomSubsetSize**_ (Integer > 0): Defines the size of the population's subset to be selected by each iteration of the selection method.
>>>
>>> When _name_ equals _probabilisticTournaments_, an object named **_parameters_**, containing the following properties, is also required:
>>>>_**threshold**_ (1 >= Float >= 0.5): Defines the odds of the method selecting the best-fit subject on each iteration.
>>
>> _**method2**_ (Object, optional, same definition as _method1_): defines the second of the potentially two methods to be used.
>When _method2_ is defined, the following property is also required:
>>>_**method1Proportion**_ (1 >= Float >= 0): Defines the proportion of subjects to be chosen by the first method. The rest will be selected by the second one.

> **_nextGenerationSelectionMethods_** (Object, same definition as _parentSelectionMethods_ with extra _fillMethod_ property): defines up to two selection methods that will be used to select the next generation's composition.
> > _**fillMethod**_ (String, ["_parent_", "_all_"]): name of the fill method to be used.

> **_finishCriteria_** (Array of Objects): defines one or more algorithm's finish criteria. Each child object is defined as follows.
>  > _**name**_ (String, ["_generationsCount_", "_time_", "_acceptableSolution_", "_content_", "_structure_"]): name of the method as defined by the _finishCriteria_ map in the _Main_ class method _loadGeneticAlgorithmDependencies_.
> When _name_ equals _structure_, an object named **_parameters_**, containing the following properties, is also required:
>>>_**numberOfSubjectsToCompare**_ (Integer > 0): Defines how many of the population's top performers will be analized to determine similarity across generations.
>>
>>>_**comparableGenerationsBeforeFinish**_ (Integer > 0): Defines how many generations will analized subject's similarity have to persist the criteria is met.
>>
>>> _**Note**: The algorithm's default tolerance is 0, which mean that, by default, two properties will only be considered similar when they are equal. Custom property-specific tolerance must be difined either in the implementation-specific configuration file (as in the _Character_ example implementation provided) or in the implementation itself, through the _isPropertySimilarWith_ method._
>>
>> When _name_ equals _acceptableSolution_, an object named **_parameters_**, containing the following properties, is also required:
>>>_**acceptableFitness**_ (Double): Defines the minimum fitness that has to be archived for the criteria to be met.
>>
>> When _name_ equals _content_, an object named **_parameters_**, containing the following properties, is also required:
>>>_**generationsWithoutImprovementToFinish**_ (Integer >= 0): Defines the number of generations in which the best fitness can persist unchallenged before the criteria is met.
>
>> When _name_ equals _generationsCount_, an object named **_parameters_**, containing the following properties, is also required:
>>>_**maxGenerations**_ (Integer >= 0): Defines the number of generations to process before the criteria is met.
>
>> When _name_ equals _time_, an object named **_parameters_**, containing the following properties, is also required:
>>>_**durationSeconds**_ (Integer >= 0): Defines the number of seconds from the start of the algorithm's evolving state until the criteria is met.

## Character Generator configuration file

This is an implementation-specific second configuration file structure, written to work with the _Character_ example _GeneticSubject_ implementation.
>_**implementationParameters**_ (Object): defines _Character_ class implementation parameters.
>
>>_**itemsPath**_ (String, A valid path from the program's working directory): Path to the folder that contains items's _tsb_ files. Each file will account from an item type, and consequently, a new _Character_ property.
>
>>_**fixedProperties**_ (Array of Objects): Allows for _Character_ properties to be fixed to a value through the program's execution.
>>> _**class**_ (String, ["_warrior_", "_archer_", "_defender_", "_spy_"]: The name of the desidered class as defined in the _ClassEnum_ enum.

>_**propertiesComparatorDeltas**_ (Array of Objects, one per custom-implementation _GeneticSubject_ property): defines the tolerance, or delta, that is used to decide if two non-equal values of the same property are similar. Each object in the array is defined as follows.
>>_**propertyIndex**_ (_#Properties_ > Integer > 0): defines the property that the tolerance is defined for.
>
>>_**delta**_ (Double > 0): defines the tolerance value.
