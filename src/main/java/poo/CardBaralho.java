package poo;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class CardBaralho {
	public static final int NCARDS = 5;
	private Stack<Card> cartas;
	private Card selected;
	private List<GameListener> observers;

	public CardBaralho(int nroJogador) {
		cartas = new Stack<Card>();
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
				Card c = new Card(elements[0],elements[0],Integer.parseInt(elements[2]));
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
