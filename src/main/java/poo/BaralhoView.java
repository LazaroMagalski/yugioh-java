package poo;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class BaralhoView extends HBox implements CardViewListener, GameListener {
	private int jogador;
	private CardBaralho cDeck;
	private Card selectedCard;
	private TextField numberCards;

	public BaralhoView(int nroJog) {
		super(4);
		this.setAlignment(Pos.CENTER);

		jogador = nroJog;
		selectedCard = null;

		cDeck = null;
		if (jogador == 1) {
			cDeck = Game.getInstance().getBaralhoJ1();
		} else {
			cDeck = Game.getInstance().getBaralhoJ2();
		}
		cDeck.addGameListener(this);
		CardView topo = null;
		for (Card card : cDeck.getCards()) {
			CardView cv = new CardView(card);
			cv.setCardViewObserver(this);
			topo = new CardView(card);
		}
		numberCards = new TextField();
		numberCards.setText("" + cDeck.getNumberOfCards());
		this.getChildren().add(topo);
		this.getChildren().add(new Label("Cards Restantes: "));
		this.getChildren().add(numberCards);
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

	@Override
	public void notify(GameEvent event) {
		if (event.getTarget() != GameEvent.Target.DECK) {
			return;
		}
		if (event.getAction() == GameEvent.Action.REMOVESEL) {
			removeSel();
		}
	}

	@Override
	public void handle(CardViewEvent event) {
		CardView cv = event.getCardView();
		selectedCard = cv.getCard();
		cDeck.setSelectedCard(selectedCard);
		// Game.getInstance().play(cDeck);
	}
}
