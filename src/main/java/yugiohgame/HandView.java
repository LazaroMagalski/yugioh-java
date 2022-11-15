package yugiohgame;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class HandView extends HBox implements CardViewListener, GameListener {
	private int jogador;
	private CardHand cDeck;
	private Card selectedCard;

	public HandView(int nroJog) {
		super(4);
		this.setAlignment(Pos.CENTER);

		jogador = nroJog;
		selectedCard = null;

		cDeck = null;
		if (jogador == 1) {
			cDeck = Game.getInstance().getHandJ1();
		} else {
			cDeck = Game.getInstance().getHandJ2();
		}
		cDeck.addGameListener(this);

		for (Card card : cDeck.getCards()) {
			CardView cv = new CardView(card);
			card.flip();
			cv.setCardViewObserver(this);
			this.getChildren().add(cv);
		}
	}

	private void removeSel() {
		ObservableList<Node> cards = getChildren();
		for (int i = 0; i < cards.size(); i++) {
			CardView cv = (CardView) cards.get(i);
			if (cv.getCard() == selectedCard) {
				getChildren().remove(cv);
				selectedCard = null;
			}
		}
	}

	private void drawCard() {
		CardHand hand = null;
		CardBaralho deck = null;
		if (jogador == 1) {
			hand = Game.getInstance().getHandJ1();
			deck = Game.getInstance().getBaralhoJ1();

		} else {
			hand = Game.getInstance().getHandJ2();
			deck = Game.getInstance().getBaralhoJ2();
		}
		Card c = deck.drawCard();
		hand.addHand(c);
		CardView cv = new CardView(c);
		this.getChildren().add(cv);
			
	}

	@Override
	public void notify(GameEvent event) {
		if (event.getTarget() != GameEvent.Target.HAND) {
			return;
		}
		if (event.getAction() == GameEvent.Action.DRAWCARD) {
			drawCard();
		}
	}

	@Override
	public void handle(CardViewEvent event) {
		CardView cv = event.getCardView();
		selectedCard = cv.getCard();
		System.out.println(selectedCard.getValue());
		drawCard();
		cDeck.setSelectedCard(selectedCard);
		Game.getInstance().playHand(cDeck);
	}
}
