package main.SubjectImplementation;

import main.GeneticSubject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Character extends GeneticSubject {
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

    protected static Map<Integer, Object> fixedProperties = new HashMap<Integer, Object>();

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

        super.fitness = calculateFitness();
    }

    public static float getHeightRandom() {
        return MIN_HEIGHT + random.nextFloat() * (MAX_HEIGHT - MIN_HEIGHT);
    }

    public static ClassEnum getClassRandom() {
        return ClassEnum.values()[random.nextInt(ClassEnum.values().length - 1)];
    }

    //Métodos internos para cáculo de atributos
    private double calculateFitness() {
        if(charClass == null)
            return -1;

        return charClass.getFitness(attack, defense);
    }

    //Métodos internos para el cálculo de atributos
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
            int propertyIndex = random.nextInt(getPropertyCount() - 1);
            if(!isPropertyFixed(propertyIndex))
                return propertyIndex;
        }
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

        if(propertyIndex == PropertiesEnum.HEIGHT.val)
            height = getHeightRandom();
        else if(propertyIndex == PropertiesEnum.CLASS.val)
            charClass = getClassRandom();
        else
            items[propertyIndex - PropertiesEnum.FIRST_ITEM.val] = Item.getRandom(propertyIndex - PropertiesEnum.FIRST_ITEM.val);
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

        Item.loadItemsFromTSB(o.getString("itemsPath"));

        if(!o.has("fixedProperties"))
            return;

        JSONObject fixedProperties = o.getJSONObject("fixedProperties");
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
}
