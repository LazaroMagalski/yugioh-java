package yugiohgame.Cards;

public class TrapCard extends Card {
        private String effect;
        private int modifier;
        public TrapCard(String anId, String anImageId, String effect,int modifier) {
            super(anId,anImageId);
            this.effect = effect;
            this.modifier = modifier;
        }
        public String getEffect() {
            return effect;
        }
        public int getModifier() {
            return modifier;
        }

        @Override
        public String toString() {
            return super.toString()+" "+effect+"\n";
        }
}
