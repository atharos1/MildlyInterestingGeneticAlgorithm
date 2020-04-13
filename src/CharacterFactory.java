import java.util.Random;

public class CharacterFactory {
    public static final Random random = new Random();
    public static final float MIN_HEIGHT = 1.3f;
    public static final float MAX_HEIGHT = 2.0f;
    public static Item[][] itemList;

    public static Character CreateRandom() {
        float height = CharacterFactory.MIN_HEIGHT + CharacterFactory.random.nextFloat() * (CharacterFactory.MAX_HEIGHT - CharacterFactory.MIN_HEIGHT);

        Item[] items = new Item[itemList.length];
        for(int i = 0; i < items.length; i++)
            items[i] = itemList[i][CharacterFactory.random.nextInt(itemList[i].length)];

        ClassEnum c = ClassEnum.values()[CharacterFactory.random.nextInt(ClassEnum.values().length - 1)];

        return new Character(c, height, items);
    }

    public static class Crossover {
        public static Character SinglePoint(Character c1, Character c2) {
            int p1 = CharacterFactory.random.nextInt(c1.getItems().length + 1);

            float height = (p1 > 0 ? c1.getHeight() : c2.getHeight());
            ClassEnum c = (p1 > 1 ? c1.getCharClass() : c2.getCharClass());

            Item[] items = new Item[c1.getItems().length];
            for(int i = 0; i < p1 - 2; i++)
                items[i] = c1.getItems()[i];
            for(int i = p1 - 2; i < items.length; i++)
                items[i] = c2.getItems()[i];

            return new Character(c, height, items);
        }
    }

    public static class Mutation {
        public static Character OneGene(Character c1) {
            int p1 = CharacterFactory.random.nextInt(c1.getItems().length + 1);

            float height = p1 != 0 ? c1.getHeight()
                    : CharacterFactory.MIN_HEIGHT + CharacterFactory.random.nextFloat() * (CharacterFactory.MAX_HEIGHT - CharacterFactory.MIN_HEIGHT);
            
            ClassEnum c = p1 != 1 ? c1.getCharClass()
                    : ClassEnum.values()[CharacterFactory.random.nextInt(ClassEnum.values().length - 1)];

            Item[] items = new Item[c1.getItems().length];
            for(int i = 0; i < items.length; i++)
                items[i] = p1 - 2 != i ? c1.getItems()[i] : itemList[i][CharacterFactory.random.nextInt(itemList[i].length)];

            return new Character(c, height, items);
        }
    }
}
