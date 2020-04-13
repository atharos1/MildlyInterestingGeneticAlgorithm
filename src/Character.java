public class Character {

    /* REPRESENTACIÓN :
        CLASE
        ALTURA
        N ITEMS
     */

    private ClassEnum c;

    private float height;
    private double ATM;
    private double DEM;

    private Item[] items;
    private double strength = 0;
    private double agility = 0;
    private double dexterity = 0;
    private double resistance = 0;
    private double life = 0;

    private double attack;
    private double defense;

    private double fitness = 0;

    public Character(ClassEnum c, float height, Item[] items) {
        this.c = c;

        this.height = height;
        this.ATM = calculateATM();
        this.DEM = calculateDEM();

        this.items = items;

        for(Item i : items) {
            this.strength += i.getStrength();
            this.agility += i.getAgility();
            this.dexterity += i.getDexterity();
            this.resistance += i.getResistance();
            this.life += i.getLife();
        }

        this.strength = 100 * Math.tanh(0.01*this.strength);
        this.agility = Math.tanh(0.01*this.agility);
        this.dexterity = 0.6 * Math.tanh(0.01*this.dexterity);
        this.resistance = Math.tanh(0.01*this.resistance);
        this.life = 100 * Math.tanh(0.01*this.life);

        this.attack = calculateAttack();
        this.defense = calculateDefense();

        this.fitness = calculateFitness();
    }

    public Item[] getItems() {
        return items;
    }

    public float getHeight() {
        return height;
    }

    public ClassEnum getCharClass() {
        return c;
    }

    public double calculateFitness() {
        return c.getFitness(attack, defense);
    }

    public double calculateAttack() {
        return (agility + dexterity) * strength * ATM;
    }

    public double calculateDefense() {
        return (resistance + dexterity) * life * DEM;
    }

    public double calculateATM() {
        return 0.7 - Math.pow(3*height - 5, 4) + Math.pow(3*height - 5, 2) + height/4;
    }

    public double calculateDEM() {
        return 1.9 + Math.pow(2.5*height - 4.16, 4) + Math.pow(2.5*height - 4.16, 2) + 3*height/10;
    }

}
