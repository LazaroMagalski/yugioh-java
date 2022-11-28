package yugiohgame.Cards;

public class MonsterCard extends Card {
    private boolean canAttack;
    private int atkPoints, defPoints, level;

    public MonsterCard(String anId, String anImageId,int level, int atk, int def) {
        super(anId,anImageId);
        this.atkPoints = atk;
        this.defPoints = def;
        this.level = level;
        this.canAttack = true;
    }

    public boolean canAttack() {
        return canAttack;
    }

    public int getAtkPoints() {
        return atkPoints;
    }

    public int getDefPoints() {
        return defPoints;
    }

    public void changePosition() {
        this.canAttack = !this.canAttack;
    }

    public void setAttack(boolean attack) {
        this.canAttack = attack;
    }
    
    public void addAtkPoints(int addModifier) {
        this.atkPoints += addModifier;
    }

    @Override
    public String toString(){
        return super.toString()+" com "+atkPoints+" de ataque\n";
    }
}
