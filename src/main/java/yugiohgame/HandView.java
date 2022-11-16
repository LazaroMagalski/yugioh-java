package yugiohgame;

import java.util.List;

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
		Game.getInstance().addGameListener(this);
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
		int numberCards = cDeck.getNumberOfCards();
		List<Card> hand = cDeck.getCards();
		Card drawedCard = hand.get(numberCards-1);
		CardView cv = new CardView(drawedCard);
		drawedCard.flip();
		cv.setCardViewObserver(this);
		this.getChildren().add(cv);
		
			
	}

	@Override
	public void notify(GameEvent event) {
		if (event.getTarget() != GameEvent.Target.HAND) {
			System.out.println("Não Entrou no notify");
			return;
		}
		if (event.getAction() == GameEvent.Action.DRAWCARD) {
			System.out.println("Entrou no notify");
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
