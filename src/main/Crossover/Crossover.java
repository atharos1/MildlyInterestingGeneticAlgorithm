package main.Crossover;
import main.Character.Character;

import java.util.List;

public interface Crossover {
    List<Character> cross(Character p1, Character p2);
}
