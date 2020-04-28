package main.SubjectImplementation.CharacterCreator;

import main.GeneticSubject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Character extends GeneticSubject {
    @Override
    public int compareTo(GeneticSubject gs) {
        return Double.compare(gs.getFitness(), this.getFitness());
    }

    public enum PropertiesEnum {
        HEIGHT(0),
        CLASS(1),
        FIRST_ITEM(2);

        public int val;
        PropertiesEnum(int value) {
            this.val = value;
        }

        public int getVal() {
            return val;
        }
    }

    private static Map<Integer, Object> fixedProperties = new HashMap<>();
    private static Map<Integer, Double> propertyComparatorDeltas = new HashMap<>();

    private ClassEnum charClass = null;

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

    private static final float MIN_HEIGHT = 1.3f;
    private static final float MAX_HEIGHT = 2.0f;

    private double fitness = 0;

    private Character(boolean randomize) {
        items = new Item[Item.getTypeCount()];
    };

    public Character() {
        items = new Item[Item.getTypeCount()];

        for(int i = 0; i < getPropertyCount(); i++) {
            if(isPropertyFixed(i))
                setProperty(i, fixedProperties.get(i));
            else
                randomizeProperty(i);
        }
    }

    private void onHeightChanged() {
        this.ATM = calculateATM();
        this.DEM = calculateDEM();

        this.attack = calculateAttack(agility, dexterity, strength, ATM);
        this.defense = calculateDefense(resistance, dexterity, life, DEM);
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
        this.strength = processStrength(this.strength);
        this.agility = processAgility(this.agility);
        this.dexterity = processDexterity(this.dexterity);
        this.resistance = processResistance(this.resistance);
        this.life = processLife(this.life);

        this.attack = calculateAttack(agility, dexterity, strength, ATM);
        this.defense = calculateDefense(resistance, dexterity, life, DEM);
    }

    private double processStrength(double strength) {
        return 100 * Math.tanh(0.01*strength);
    }

    private double processAgility(double agility) {
        return Math.tanh(0.01*agility);
    }

    private double processDexterity(double dexterity) {
        return 0.6 * Math.tanh(0.01*dexterity);
    }

    private double processResistance(double resistance) {
        return Math.tanh(0.01*resistance);
    }

    private double processLife(double life) {
        return 100 * Math.tanh(0.01*life);
    }

    private void onPropertyChanged(int index) {
        if(index == PropertiesEnum.HEIGHT.val)
            onHeightChanged();
        else if(index >= PropertiesEnum.FIRST_ITEM.val)
            onItemChanged();

        fitness = calculateFitness();
    }

    public static float getHeightRandom() {
        return MIN_HEIGHT + random.nextFloat() * (MAX_HEIGHT - MIN_HEIGHT);
    }

    public static ClassEnum getClassRandom() {
        return ClassEnum.values()[random.nextInt(ClassEnum.values().length)];
    }

    //Métodos internos para cáculo de atributos
    private double calculateFitness() {
        if(charClass == null)
            return -1;

        return charClass.getFitness(attack, defense);
    }

    //Métodos internos para el cálculo de atributos
    private double calculateAttack(double agility, double dexterity, double strength, double factor) {
        return (agility + dexterity) * strength * factor;
    }

    private double calculateDefense(double resistance, double dexterity, double life, double factor) {
        return (resistance + dexterity) * life * factor;
    }

    private double calculateATM() {
        return 0.7 - Math.pow(3*height - 5, 4) + Math.pow(3*height - 5, 2) + height/4;
    }

    private double calculateDEM() {
        return 1.9 + Math.pow(2.5*height - 4.16, 4) - Math.pow(2.5*height - 4.16, 2) - 3*height/10;
    }
    //Métodos internos para el cálculo de atributos

    @Override
    public int getPropertyCount() {
        return PropertiesEnum.values().length - 1 + Item.getTypeCount();
    }

    @Override
    public void setFixedProperty(int propertyIndex, Object value) {
        if(propertyIndex >= getPropertyCount())
            return;

        fixedProperties.put(propertyIndex, value);
    }

    @Override
    public int getFixedPropertyCount() {
        return fixedProperties.size();
    }

    @Override
    public boolean isPropertyFixed(int propertyIndex) {
        return fixedProperties.containsKey(propertyIndex);
    }

    @Override
    public boolean isEveryPropertyFixed() {
        return fixedProperties.size() == getPropertyCount();
    }

    @Override
    public int getRandomUnfixedPropertyIndex() {
        if(isEveryPropertyFixed())
            return -1;

        while(true) {
            int propertyIndex = random.nextInt(getPropertyCount());
            if(!isPropertyFixed(propertyIndex))
                return propertyIndex;
        }
    }

    @Override
    public double comparePropertyWith(GeneticSubject gs, int propertyIndex) {
        if(!(gs instanceof Character) || propertyIndex >= getPropertyCount())
            return 0;

        if(propertyIndex == PropertiesEnum.HEIGHT.val)
            return Double.compare((double)getProperty(propertyIndex), (double)gs.getProperty(propertyIndex));
        else if(propertyIndex == PropertiesEnum.CLASS.val)
            return (ClassEnum)getProperty(propertyIndex) == (ClassEnum)gs.getProperty(propertyIndex) ? 0 : 1;
        else if(propertyIndex >= PropertiesEnum.FIRST_ITEM.val) {
            Item i1 = (Item)getProperty(propertyIndex);
            double fitness1 = charClass.getFitness(calculateAttack(i1.getAgility(), i1.getDexterity(), i1.getStrength(), 1.0), calculateDefense(i1.getResistance(), i1.getDexterity(), i1.getLife(), 1.0));

            Item i2 = (Item)gs.getProperty(propertyIndex);
            double fitness2 = charClass.getFitness(calculateAttack(i2.getAgility(), i2.getDexterity(), i2.getStrength(), 1.0), calculateDefense(i2.getResistance(), i2.getDexterity(), i2.getLife(), 1.0));

            return Double.compare(fitness1, fitness2);
        }

        return 0;
    }

    @Override
    public boolean isPropertySimilarWith(GeneticSubject gs, int propertyIndex) {
        if(!(gs instanceof Character) || propertyIndex >= getPropertyCount())
            return false;

        double propertyDelta = propertyComparatorDeltas.getOrDefault(propertyIndex, 0.0);

        return comparePropertyWith(gs, propertyIndex) <= propertyDelta;
    }

    @Override
    public double getFitness() {
        return fitness;
    }

    @Override
    public GeneticSubject getRandom() {
        return new Character();
    }

    @Override
    public void setProperty(int propertyIndex, Object value) {
        if(propertyIndex == PropertiesEnum.HEIGHT.val)
            this.height = (float)value;
        else if(propertyIndex == PropertiesEnum.CLASS.val)
            this.charClass = (ClassEnum)value;
        else if(propertyIndex >= PropertiesEnum.FIRST_ITEM.val)
            items[propertyIndex - PropertiesEnum.FIRST_ITEM.val] = (Item)value;

        onPropertyChanged(propertyIndex);
    }

    @Override
    public void randomizeProperty(int propertyIndex) {
        if(propertyIndex >= getPropertyCount() || isPropertyFixed(propertyIndex))
            return;

        setProperty(propertyIndex, generateRandomProperty(propertyIndex));
    }

    public Object generateRandomProperty(int propertyIndex) {
        if(propertyIndex >= getPropertyCount() || isPropertyFixed(propertyIndex))
            return null;

        if(propertyIndex == PropertiesEnum.HEIGHT.val)
            return getHeightRandom();
        else if(propertyIndex == PropertiesEnum.CLASS.val)
            return getClassRandom();
        else
            return Item.getRandom(propertyIndex - PropertiesEnum.FIRST_ITEM.val);
    }

    @Override
    public Object getProperty(int index) {
        if(index == PropertiesEnum.HEIGHT.val)
            return height;

        if(index == PropertiesEnum.CLASS.val)
            return charClass;

        if(index >= PropertiesEnum.FIRST_ITEM.val)
            return items[index - PropertiesEnum.FIRST_ITEM.val];

        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Class: " + charClass.name() + "\n");
        sb.append("Height: " + height + "\n");
        for(int i = 0; i < items.length; i++)
            sb.append("Item " + i + ". " + items[i].toString() + "\n");
        sb.append("Fitness: " + fitness);

        return sb.toString();
    }

    @Override
    public GeneticSubject cloneSubject() {
        GeneticSubject n = new Character(false);
        for(int i = 0; i < getPropertyCount(); i++)
            n.setProperty(i, getProperty(i));

        return n;
    }

    @Override
    public void loadConfigurationFromFile(String configurationFile) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(configurationFile)));
        JSONObject o = new JSONObject(json);

        JSONObject implementationParameters = o.getJSONObject("implementationParameters");

        Item.loadItemsFromTSB(implementationParameters.getString("itemsPath"));

        if(implementationParameters.has("fixedProperties")) {
            JSONObject fixedProperties = implementationParameters.getJSONObject("fixedProperties");
            if(fixedProperties.has("class")) {
                ClassEnum c = ClassEnum.getByName(fixedProperties.getString("class"));
                if(c == null) throw new IllegalArgumentException("Invalid class name");

                setFixedProperty(PropertiesEnum.CLASS.val, c);
            }
            if(fixedProperties.has("height"))
                setFixedProperty(PropertiesEnum.HEIGHT.val, fixedProperties.getFloat("height"));
            if(fixedProperties.has("items")) {
                JSONArray fixedItems = fixedProperties.getJSONArray("items");
                for(int i = 0; i < fixedItems.length(); i++) {
                    JSONObject item = fixedItems.getJSONObject(i);
                    setFixedProperty(PropertiesEnum.FIRST_ITEM.val + item.getInt("typeId"), item.getInt("itemId"));
                }
            }
        }

        if(!o.has("propertiesComparatorDeltas"))
            return;

        JSONArray propertiesComparatorDeltasArray = o.getJSONArray("propertiesComparatorDeltas");
        for(int i = 0; i < propertiesComparatorDeltasArray.length(); i++) {
            JSONObject propertyComparatorDelta = propertiesComparatorDeltasArray.getJSONObject(i);
            propertyComparatorDeltas.put(propertyComparatorDelta.getInt("propertyIndex"), propertyComparatorDelta.getDouble("delta"));
        }
    }
}
