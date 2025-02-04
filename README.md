# Yu-Gi-Oh Java Project

## Description

This project is the final assignment for the **Object-Oriented Programming (OOP)** class, inspired by the popular trading card game **Yu-Gi-Oh**. Developed in **Java** using **Maven**, it aims to simulate core aspects of the Yu-Gi-Oh game, providing a foundation for further expansion and learning in OOP principles.

### Game: Card Battle
#### Instructions:
- The deck at the top of the window belongs to Player 1. The bottom deck belongs to Player 2.
- Player 1 always plays first.
- Each player can **summon one monster card per turn** in either **attack mode** or **defense mode**.
- During their turn, a player can also choose to **attack the opponent's monster**:
  - **If both monsters are in attack mode**, the one with the **higher attack wins**, the **losing monster is destroyed**, and the difference in attack values is deducted from the losing player's life points.
  - **If the defending monster is in defense mode**:
    - If its **defense is higher than the attacker's attack**, the difference (defense - attack) is deducted from the **attacker's life points**.
    - If **defense is equal to attack**, nothing happens.
    - If **defense is lower than attack**, the defending monster is destroyed, but the defending player takes **no damage**.
- A player can also **use spell cards**, each having different effects.
- The game **ends when a player's life points reach 0**.
- The decks are predefined as the **Antagonist Seto Kaiba's Deck** (`src/main/resources/baralhos/KaibaDeck.csv`) and the **Protagonist Yugi Moto's Deck** (`src/main/resources/YugiDeck.csv`).
- To start the next round, click the "clean" button.

### Implementation:
- The game logic is controlled by the **Game** class.
- The **Game** class uses:
  - **Card** (represents a card)
  - **CardDeck** (represents a deck of cards)
- All these classes are derived from **Observer**, allowing them to be observed by UI components.
- Whenever an event occurs, the game classes send an instance of **GameEvent** to the observing interface.
- **GameEvent** instances specify the target, action, and an extra string parameter.
- The main UI class is **GameWindow**.
- **CardView** displays an individual card.
- **DeckView** displays a deck of cards.
- **GameWindow** and **DeckView** observe **Game** and **CardDeck**, respectively.
- **CardView** observes **Card** (to update the display when flipped) and is observed by **DeckView** (to detect selected cards).
- The **CardSelected** interface facilitates interaction between **DeckView** and **CardView**.
- **ImageFactory** ensures that only the necessary card images are loaded into memory and prevents duplicate loads.

## Features

- **Card Representation**: Models various types of Yu-Gi-Oh cards with attributes such as name, type, attack, defense, and special abilities.
- **Basic Gameplay Mechanics**: Implements fundamental game mechanics, including:
  - Drawing cards
  - Summoning monsters
  - Basic turn structure

## Installation

### Prerequisites

- **Java Development Kit (JDK)**: Version 8 or higher.
- **Maven**: Ensure Maven is installed and configured in your system's PATH.

### Steps

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/LazaroMagalski/yugioh-java.git
   cd yugioh-java
   ```

2. **Build the Project**:
   ```bash
   mvn clean install
   ```

## Usage

After building the project, you can run the application using:

```bash
mvn exec:java -Dexec.mainClass="com.yourpackage.MainClass"
```

Replace `com.yourpackage.MainClass` with the actual main class path.

## Project Structure

- `src/main/java`: Contains the Java source files.
- `src/main/resources`: Includes resources like `cards.json`.
- `docs/`: Documentation and related resources.
- `pom.xml`: Maven configuration file.

## Contributing

Contributions are welcome! Feel free to fork the repository and submit pull requests.

## License

This project is licensed under the [MIT License](LICENSE).

## Acknowledgments

- Inspired by the Yu-Gi-Oh trading card game.
- Developed as a final project for the OOP class.

