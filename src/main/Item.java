package main;

public class Item {
    private double strength;
    private double agility;
    private double dexterity;
    private double resistance;
    private double life;
    private int id;
    private String originFile;

    public Item(String originFile, int id, double strength, double agility, double dexterity, double resistance, double life) {
        this.originFile = originFile;
        this.id = id;
        this.strength = strength;
        this.agility = agility;
        this.dexterity = dexterity;
        this.resistance = resistance;
        this.life = life;
    }

    public double getStrength() {
        return strength;
    }

    public double getAgility() {
        return agility;
    }

    public double getDexterity() {
        return dexterity;
    }

    public double getResistance() {
        return resistance;
    }

    public double getLife() {
        return life;
    }

    @Override
    public String toString() {
        return "originFile: " + originFile + ", ID: " + id;
    }
}
