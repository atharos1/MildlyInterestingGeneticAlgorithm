package main.SubjectImplementation.CharacterCreator;

public enum ClassEnum {

    WARRIOR {
        @Override
        public double getFitness(double attack, double defense) {
            return 0.6 * attack + 0.6 * defense;
        }
    },
    ARCHER {
        @Override
        public double getFitness(double attack, double defense) {
            return 0.9 * attack + 0.1 * defense;
        }
    },
    DEFENDER {
        @Override
        public double getFitness(double attack, double defense) {
            return 0.3 * attack + 0.8 * defense;
        }
    },
    SPY {
        @Override
        public double getFitness(double attack, double defense) {
            return 0.8 * attack + 0.3 * defense;
        }
    };

    public static ClassEnum getByName(String className) {
        if(className.equals("warrior")) return WARRIOR;
        if(className.equals("archer")) return ARCHER;
        if(className.equals("defender")) return DEFENDER;
        if(className.equals("spy")) return SPY;

        return null;
    }

    public abstract double getFitness(double attack, double defense);
}