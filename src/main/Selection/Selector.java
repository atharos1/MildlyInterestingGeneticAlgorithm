package main.Selection;

import main.Character;

import java.util.List;

public interface Selector {
    List<Character> select(List<Character> characters, int K);
}
