package yugiohgame.Components;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import yugiohgame.Cards.Card;
import yugiohgame.Cards.MonsterCard;
import yugiohgame.Cards.SpellCard;
import yugiohgame.Cards.TrapCard;
import yugiohgame.Events.GameEvent;
import yugiohgame.Listeners.GameListener;

public class Deck {
	public static final int NCARDS = 7;
	private Stack<Card> cartas;
	private Card selected;
	private List<GameListener> observers;

	public Deck(int nroJogador) {
		cartas = new Stack<>();
		selected = null;

		String caminhoAtual = "";

		if (nroJogador == 1){
			caminhoAtual = Paths.get("src\\main\\resources\\baralhos\\KaibaDeck.csv").toAbsolutePath().toString();
		}else{
			caminhoAtual = Paths.get("src\\main\\resources\\baralhos\\YugiDeck.csv").toAbsolutePath().toString();
		}

        Path caminho = Paths.get(caminhoAtual);
		try (Scanner sc = new Scanner(Files.newBufferedReader(caminho, Charset.defaultCharset()))) {
			String lineHeader = sc.nextLine();
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
				String[] elements;
				elements = line.split(",");
				
				String cardType = elements[0];
				String cardName = elements[1];

				Card c = null;

				switch (cardType){
					case "MonsterCard":
						int level = Integer.parseInt(elements[2]);
						int atk = Integer.parseInt(elements[3]);
						int def = Integer.parseInt(elements[4]);
		
						c = new MonsterCard(cardName,cardName,level,atk,def);
						break;
					
					case "SpellCard":
						String spellEffect = elements[2];
						int modifier = Integer.parseInt(elements[3]);
						c = new SpellCard(cardName,cardName, spellEffect, modifier);
						break;
					case "TrapCard":
						String trapEffect = elements[2];
						
						c = new TrapCard(cardName, cardName);
						break;
					default:
				}

				cartas.push(c);
            }
        } catch (IOException x) {
            System.err.format("Erro de E/S: %s%n", x);
        }
		Collections.shuffle(cartas);
		observers = new LinkedList<>();
	}

	public List<Card> getCards() {
		return Collections.unmodifiableList(cartas);
	}

	public int getNumberOfCards() {
		return cartas.size();
	}

	public void removeSel() {
		if (selected == null) {
			return;
		}
		cartas.remove(selected);
		selected = null;
		GameEvent gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.REMOVESEL, "");
		for (var observer : observers) {
			observer.notify(gameEvent);
		}
	}

	public void setSelectedCard(Card card) {
		selected = card;
	}

	public Card getSelectedCard() {
		return selected;
	}
	public Card drawCard(){
		return cartas.pop();
	}
	public void addGameListener(GameListener listener) {
		observers.add(listener);
	}


}
