package main;

public class Character implements Comparable<Character> {

    /* REPRESENTACIÓN :
        ALTURA
        CLASE
        N ITEMS
     */

    public enum PropertiesEnum {
        HEIGHT(0),
        CLASS(1),
        FIRST_ITEM(2);

        private int val;
        PropertiesEnum(int value) {
            this.val = value;
        }

        public int getVal() {
            return val;
        }
    }

    private ClassEnum c = null;

    private float height = 0;
    private double ATM = 0;
    private double DEM = 0;

    private Item[] items;
    private double strength = 0;
    private double agility = 0;
    private double dexterity = 0;
    private double resistance = 0;
    private double life = 0;

    private double attack = 0;
    private double defense = 0;

    private double fitness = 0;

    public Character() {
        items = new Item[CharacterFactory.getItemTypeCount()];

        for(int i = 0; i < items.length; i++)
            items[i] = null;
    }

    private void onHeightChanged() {
        this.ATM = calculateATM();
        this.DEM = calculateDEM();

        this.attack = calculateAttack();
        this.defense = calculateDefense();
    }

    private void onItemChanged() {
        this.strength = this.agility = this.dexterity = this.resistance = this.life = 0;
        for(Item i : items) {
            if(i != null) {
                this.strength += i.getStrength();
                this.agility += i.getAgility();
                this.dexterity += i.getDexterity();
                this.resistance += i.getResistance();
                this.life += i.getLife();
            }
        }
        this.strength = 100 * Math.tanh(0.01*this.strength);
        this.agility = Math.tanh(0.01*this.agility);
        this.dexterity = 0.6 * Math.tanh(0.01*this.dexterity);
        this.resistance = Math.tanh(0.01*this.resistance);
        this.life = 100 * Math.tanh(0.01*this.life);

        this.attack = calculateAttack();
        this.defense = calculateDefense();
    }

    private void onPropertyChanged(int index) {
        if(index == PropertiesEnum.HEIGHT.val)
            onHeightChanged();
        else if(index >= PropertiesEnum.FIRST_ITEM.val)
            onItemChanged();

        this.fitness = calculateFitness();
    }

    public Object getProperty(int index) {
        if(index == PropertiesEnum.HEIGHT.val)
            return height;

        if(index == PropertiesEnum.CLASS.val)
            return c;

        if(index >= PropertiesEnum.FIRST_ITEM.val)
            return items[index - PropertiesEnum.FIRST_ITEM.val];

        return null;
    }

    //Setea una propiedad indexada según la representación
    //Si value es null, randomiza la propiedad
    public void setProperty(int index, Object value) {
        if(index == PropertiesEnum.HEIGHT.val)
            this.height = value != null ? (float)value : CharacterFactory.getHeightRandom();
        else if(index == PropertiesEnum.CLASS.val)
            this.c =  value != null ? (ClassEnum)value : CharacterFactory.getClassRandom();
        else if(index >= PropertiesEnum.FIRST_ITEM.val)
            items[index - PropertiesEnum.FIRST_ITEM.val] =  value != null ? (Item)value : CharacterFactory.getItemRandom(index - PropertiesEnum.FIRST_ITEM.val);

        onPropertyChanged(index);
    }

    public double getFitness() {
        return this.fitness;
    }

    @Override
    public int compareTo(Character c) {
        //TODO no está al revés?
        return Double.compare(c.getFitness(), this.fitness);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Class: " + c.name() + "\n");
        sb.append("Height: " + height + "\n");
        for(int i = 0; i < items.length; i++)
            sb.append("Item " + i + ". " + items[i].toString() + "\n");
        sb.append("Fitness: " + fitness);

        return sb.toString();
    }



    //Métodos internos para cáculo de atributos
    private double calculateFitness() {
        if(c == null)
            return -1;

        return c.getFitness(attack, defense);
    }

    private double calculateAttack() {
        return (agility + dexterity) * strength * ATM;
    }

    private double calculateDefense() {
        return (resistance + dexterity) * life * DEM;
    }

    private double calculateATM() {
        return 0.7 - Math.pow(3*height - 5, 4) + Math.pow(3*height - 5, 2) + height/4;
    }

    private double calculateDEM() {
        return 1.9 + Math.pow(2.5*height - 4.16, 4) - Math.pow(2.5*height - 4.16, 2) - 3*height/10;
    }
    //Métodos internos para el cálculo de atributos
}
