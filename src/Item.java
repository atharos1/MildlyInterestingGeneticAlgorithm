public class Item {
    private double strength;
    private double agility;
    private double dexterity;
    private double resistance;
    private double life;

    public Item(double strength, double agility, double dexterity, double resistance, double life) {
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
}
