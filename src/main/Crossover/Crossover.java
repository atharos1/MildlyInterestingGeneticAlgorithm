package main.Crossover;
import main.Character;

import java.util.List;

public interface Crossover {
    List<Character> cross(Character c1, Character c2);
}
