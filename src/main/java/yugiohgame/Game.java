package yugiohgame;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import yugiohgame.Views.FieldView.CardType;
import yugiohgame.Cards.Card;
import yugiohgame.Cards.MonsterCard;
import yugiohgame.Cards.TrapCard;
import yugiohgame.Components.Deck;
import yugiohgame.Components.Field;
import yugiohgame.Components.Hand;
import yugiohgame.Events.GameEvent;
import yugiohgame.Listeners.GameListener;
import yugiohgame.Views.FieldView;

public class Game {
	private static Game game = new Game();
	private int ptsJ1, ptsJ2;
	private Field spellJ1, monsterJ1, spellJ2, monsterJ2;
	private Hand handJ1, handJ2;
	private Deck baralhoJ1, baralhoJ2;
	private int player;
	private int rodada;
	private List<GameListener> observers;
	private MonsterCard mc1;
	private MonsterCard mc2;

	public static Game getInstance() {
		return game;
	}

	private Game() {
		ptsJ1 = 8000;
		ptsJ2 = 8000;
		spellJ1 = new Field();
		monsterJ1 = new Field();

		spellJ2 = new Field();
		monsterJ2 = new Field();

		handJ1 = new Hand();
		handJ2 = new Hand();

		baralhoJ1 = new Deck(1);
		baralhoJ2 = new Deck(2);
		handJ1.addCardHand(baralhoJ1, 6);
		handJ2.addCardHand(baralhoJ2, 5);

		player = 1;
		rodada = 1;
		observers = new LinkedList<>();
	}

	public MonsterCard getMC1() {
		return this.mc1;
	}

	public MonsterCard getMC2() {
		return this.mc2;
	}

	public void setMC1(MonsterCard mc1) {
		this.mc1 = mc1;
	}

	public void setMC2(MonsterCard mc2) {
		this.mc2 = mc2;
	}

	public int nextPlayer() {
		GameEvent gameEvent = new GameEvent(this, GameEvent.Target.BARALHO, GameEvent.Action.PLAYERCHANGE, null);

		rodada++;
		player++;
		if (player == 3) {
			player = 1;
		}

		switch (player) {
			case 1:
				addHand(baralhoJ1);
				break;
			case 2:
				addHand(baralhoJ2);
				break;
			default:
				break;

		}

		for (Card c : monsterJ1.getCards()) {
			MonsterCard m = (MonsterCard) c;
			m.setAttack(true);
		}

		for (Card c : monsterJ2.getCards()) {
			MonsterCard m = (MonsterCard) c;
			m.setAttack(true);
		}
		handJ1.setCondition(true);
		handJ2.setCondition(true);
		for (var observer : observers) {
			observer.notify(gameEvent);
		}
		return player;
	}

	public int getPlayer() {
		return this.player;
	}

	public int getPtsJ1() {
		return ptsJ1;
	}

	public int getPtsJ2() {
		return ptsJ2;
	}

	public Field getFieldJ1(CardType cardType) {
		if (cardType == FieldView.CardType.MONSTERCARD)
			return monsterJ1;
		if (cardType == FieldView.CardType.SPELLCARD)
			return spellJ1;
		return null;
	}

	public Field getFieldJ2(CardType cardType) {
		if (cardType == FieldView.CardType.MONSTERCARD)
			return monsterJ2;
		if (cardType == FieldView.CardType.SPELLCARD)
			return spellJ2;
		return null;
	}

	public Hand getHandJ1() {
		return handJ1;
	}

	public Hand getHandJ2() {
		return handJ2;
	}

	public Deck getBaralhoJ1() {
		return baralhoJ1;
	}

	public Deck getBaralhoJ2() {
		return baralhoJ2;
	}

	public void reduceLP(int modifier, int jogador) {
		GameEvent gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SUMMONCARD, "");
		if (jogador == 1) {
			ptsJ1 -= modifier;
		} else {
			ptsJ2 -= modifier;
		}
		for (var observer : observers) {
			observer.notify(gameEvent);
		}
	}

	public void addLP(int modifier, int jogador) {
		GameEvent gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SUMMONCARD, "");
		if (jogador == 1) {
			ptsJ1 += modifier;
		} else {
			ptsJ2 += modifier;
		}
		for (var observer : observers) {
			observer.notify(gameEvent);
		}
	}

	public void play(Field field) {
		GameEvent gameEvent = null;
		MonsterCard monster1 = (MonsterCard) monsterJ1.getSelectedCard();
		MonsterCard monster2 = (MonsterCard) monsterJ2.getSelectedCard();
		Boolean negateAttack = false;

		if (rodada == 1) {
			for (var observer : observers) {
				observer.notify(new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY,
						"NÃO É POSSÍVEL REALIZAR ATAQUES NA PRIMEIRA RODADA"));
			}
			return;
		}
		switch (player) {
			case 1:
				if (field == monsterJ1) {
					if (monster1.canAttack()) {
						if (monsterJ2.getNumberOfCards() == 0) {
							setMC1(monster1);
							if (spellJ2.getNumberOfCards() > 0) {
								int opc = JOptionPane.showConfirmDialog(null, "Acionar Trap", "Trap",
										JOptionPane.YES_NO_OPTION);
								if (opc == 0) {
									if (spellJ2.getNumberOfCards() == 0) {
										TrapCard t = (TrapCard) spellJ2.getCards().get(0);
										negateAttack = spellJ2.activateTrap(t.getEffect(), t, 2);
										spellJ2.setSelectedCard(t);
									} else {
										int trapNum = Integer
												.parseInt(JOptionPane.showInputDialog("Qual trap adicionar? "));
										TrapCard t = (TrapCard) spellJ2.getCards().get(trapNum);
										negateAttack = spellJ2.activateTrap(t.getEffect(), t, 2);
										spellJ2.setSelectedCard(t);
									}
									spellJ2.removeSel();
									if (negateAttack) {
										monster1.changePosition();
										return;
									}
									gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SUMMONCARD,
									"");
								}
							}
							ptsJ2 -= monster1.getAtkPoints();
							monster1.changePosition();
							gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SUMMONCARD, "");
						}
					} else {
						for (var observer : observers) {
							observer.notify(new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY,
									"ESSE MONSTRO JÁ ATACOU, ESCOLHA OUTRO"));
						}
						return;
					}
				} else if (field == monsterJ2) {
					if (monster1 == null) {
						for (var observer : observers) {
							observer.notify(new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY,
									"ESCOLHA PRIMEIRO UM MONSTRO ATACANTE"));
						}
						return;
					} else if (monster1.canAttack()) {
						setMC1(monster1);
						if (spellJ2.getNumberOfCards() > 0) {
							int opc = JOptionPane.showConfirmDialog(null, "Acionar Trap", "Trap",
									JOptionPane.YES_NO_OPTION);
							if (opc == 0) {
								if (spellJ2.getNumberOfCards() == 0) {
									TrapCard t = (TrapCard) spellJ2.getCards().get(0);
									negateAttack = spellJ2.activateTrap(t.getEffect(), t, 2);
									spellJ2.setSelectedCard(t);
								} else {
									int trapNum = Integer
											.parseInt(JOptionPane.showInputDialog("Qual trap adicionar? "));
									TrapCard t = (TrapCard) spellJ2.getCards().get(trapNum);
									negateAttack = spellJ2.activateTrap(t.getEffect(), t, 2);
									spellJ2.setSelectedCard(t);
								}
								spellJ2.removeSel();
								if (negateAttack) {
									return;
								}
								gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SUMMONCARD, "");
							}
						}
						if (monster1.getAtkPoints() > monster2.getAtkPoints()) {
							ptsJ2 -= monster1.getAtkPoints() - monster2.getAtkPoints();
							monsterJ2.removeSel();
							monster1.changePosition();
							gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SUMMONCARD, "");
						} else if (monster1.getAtkPoints() < monster2.getAtkPoints()) {
							ptsJ1 -= monster2.getAtkPoints() - monster1.getAtkPoints();
							monsterJ1.removeSel();
							monster1.changePosition();
							gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SUMMONCARD, "");
						} else {
							gameEvent = null;
						}

					} else {
						for (var observer : observers) {
							observer.notify(new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY,
									"ESSE MONSTRO JÁ ATACOU, ESCOLHA OUTRO"));
						}
						return;
					}
				}
				break;

			case 2:
				if (field == monsterJ2) {
					if (monster2.canAttack()) {
						if (monsterJ1.getNumberOfCards() == 0) {
							setMC2(monster2);

							if (spellJ1.getNumberOfCards() > 0) {
								int opc = JOptionPane.showConfirmDialog(null, "Acionar Trap", "Trap",
										JOptionPane.YES_NO_OPTION);
								if (opc == 0) {
									if (spellJ1.getNumberOfCards() == 1) {
										TrapCard t = (TrapCard) spellJ1.getCards().get(0);
										negateAttack = spellJ1.activateTrap(t.getEffect(), t, 1);
										spellJ1.setSelectedCard(t);
									} else {
										int trapNum = Integer
												.parseInt(JOptionPane.showInputDialog("Qual trap acionar ?"));
										TrapCard t = (TrapCard) spellJ1.getCards().get(trapNum);
										negateAttack = spellJ1.activateTrap(t.getEffect(), t, 1);
										spellJ1.setSelectedCard(t);
									}
									spellJ1.removeSel();
									if (negateAttack) {
										return;
									}
									gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SUMMONCARD,
											"");
								}
							}
							ptsJ1 -= monster2.getAtkPoints();
							gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SUMMONCARD, "");
							monster2.changePosition();
						}
					} else {
						for (var observer : observers) {
							observer.notify(new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY,
									"ESSE MONSTRO JÁ ATACOU, ESCOLHA OUTRO"));
						}
						return;
					}
				} else if (field == monsterJ1) {
					if (monster2 == null) {
						for (var observer : observers) {
							observer.notify(new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY,
									"ESCOLHA PRIMEIRO UM MONSTRO ATACANTE"));
						}
						return;
					} else if (monster2.canAttack()) {
						setMC2(monster2);

							if (spellJ1.getNumberOfCards() > 0) {
								int opc = JOptionPane.showConfirmDialog(null, "Acionar Trap", "Trap",
										JOptionPane.YES_NO_OPTION);
								if (opc == 0) {
									if (spellJ1.getNumberOfCards() == 1) {
										TrapCard t = (TrapCard) spellJ1.getCards().get(0);
										negateAttack = spellJ1.activateTrap(t.getEffect(), t, 1);
										spellJ1.setSelectedCard(t);
									} else {
										int trapNum = Integer
												.parseInt(JOptionPane.showInputDialog("Qual trap acionar ?"));
										TrapCard t = (TrapCard) spellJ1.getCards().get(trapNum);
										negateAttack = spellJ1.activateTrap(t.getEffect(), t, 1);
										spellJ1.setSelectedCard(t);
									}
									spellJ1.removeSel();
									if (negateAttack) {
										return;
									}
									gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SUMMONCARD,
											"");
								}
							}
							if (monster1.getAtkPoints() > monster2.getAtkPoints()) {
								ptsJ2 -= monster1.getAtkPoints() - monster2.getAtkPoints();
								monsterJ2.removeSel();
								monster2.changePosition();
								gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SUMMONCARD, "");
							} else if (monster1.getAtkPoints() < monster2.getAtkPoints()) {
								ptsJ1 -= monster2.getAtkPoints() - monster1.getAtkPoints();
								monsterJ1.removeSel();
								monster2.changePosition();
								gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SUMMONCARD, "");
							} else {
								gameEvent = null;
							}
						
					} else {
						for (var observer : observers) {
							observer.notify(new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY,
									"ESSE MONSTRO JÁ ATACOU, ESCOLHA OUTRO"));
						}
						return;
					}
				}
				break;
			default:
				break;

		}
		if (ptsJ1 <= 0 || baralhoJ1.getNumberOfCards() == 0) {
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.ENDGAME, "2");
		}

		if (ptsJ2 <= 0 || baralhoJ2.getNumberOfCards() == 0) {
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.ENDGAME, "1");
		}

		if (gameEvent != null) {
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
		}
	}

	public void getHandDetails(String player) {
		GameEvent gameEvent = null;
		gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.SEEDETAILS, player);
		for (var observer : observers) {
			observer.notify(gameEvent);
		}
	}

	public void getFieldDetails(String player) {
		GameEvent gameEvent = null;
		gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.SEEFIELD, player);
		for (var observer : observers) {
			observer.notify(gameEvent);
		}
	}

	public void addHand(Deck c) {
		GameEvent gameEvent = null;
		if (c == baralhoJ1) {
			if (handJ1.getNumberOfCards() < 7) {
				handJ1.addCardHand(c, 1);
				gameEvent = new GameEvent(this, GameEvent.Target.HAND, GameEvent.Action.DRAWCARD, "1");
			} else {
				return;
			}
		}
		if (c == baralhoJ2) {
			if (handJ2.getNumberOfCards() < 7) {
				handJ2.addCardHand(c, 1);
				gameEvent = new GameEvent(this, GameEvent.Target.HAND, GameEvent.Action.DRAWCARD, "2");
			} else {
				return;
			}
		}

		for (var observer : observers) {
			observer.notify(gameEvent);
		}
	}

	public void addField(Card c, int j, CardType cardType) {
		GameEvent gameEvent = null;
		if (j == 1) {

			getFieldJ1(cardType).addCard(c);
			gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SUMMONCARD, "1");
			handJ1.removeSel();

		}
		if (j == 2) {

			getFieldJ2(cardType).addCard(c);
			gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SUMMONCARD, "2");
			handJ2.removeSel();
		}

		if (gameEvent != null) {
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
		}
	}

	public void addGameListener(GameListener listener) {
		observers.add(listener);
	}
}
