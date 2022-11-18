package yugiohgame.Cards;

public class MonsterCard extends Card {
    private boolean isAttack;
    private int atkPoints, defPoints, level;

    public MonsterCard(String anId, String anImageId,int level, int atk, int def) {
        super(anId,anImageId);
        this.atkPoints = atk;
        this.defPoints = def;
        this.level = level;
    }

    public boolean isAttack() {
        return isAttack;
    }

    public int getAtkPoints() {
        return atkPoints;
    }

    public int getDefPoints() {
        return defPoints;
    }

    public void changePosition() {
        this.isAttack = !this.isAttack;
    }

    
}
