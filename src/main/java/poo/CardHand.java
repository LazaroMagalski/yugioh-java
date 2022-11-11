package poo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class CardHand {
	public static final int NCARDS = 5;
	private List<Card> cartas;
	private Card selected;
	private List<GameListener> observers;

	public CardHand() {
		cartas = new ArrayList<>(NCARDS);
		selected = null;
		Random r = new Random();
		String caminhoAtual = Paths.get("D:\\OneDrive - PUCRS - BR\\Faculdade\\POO\\yugioh-java\\src\\main\\resources\\imagens\\KaibaDeck.csv").toAbsolutePath().toString();
        Path caminho = Paths.get(caminhoAtual);
		try (Scanner sc = new Scanner(Files.newBufferedReader(caminho, Charset.defaultCharset()))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
				System.out.print(line);
            }
        } catch (IOException x) {
            System.err.format("Erro de E/S: %s%n", x);
        }
		
		for (int i = 0; i < NCARDS; i++) {
			int n = r.nextInt(10) + 1;
			Card c = new Card("C" + n, "img" + n, n);
			c.flip();
			cartas.add(c);
		}
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

	public void addGameListener(GameListener listener) {
		observers.add(listener);
	}

}
