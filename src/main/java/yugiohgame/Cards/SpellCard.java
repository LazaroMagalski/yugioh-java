package yugiohgame.Cards;

public class SpellCard extends Card {
        private String effect;
        public SpellCard(String anId, String anImageId, String effect) {
            super(anId,anImageId);
            this.effect = effect;
        }


        public String getEffect() { 
            System.out.println("Effect Card "+effect);
            return effect;   }

}
