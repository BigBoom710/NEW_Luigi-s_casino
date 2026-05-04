# NEW_Luigi-s_casino

Una **bruttacopia** del gioco della DS di Luigi's Casino, implementata in Java con la metodologia **MVC** (Model-View-Controller).

## Struttura del progetto

```
src/
├── main/java/it/casino/
│   ├── Main.java                          # Punto di ingresso
│   ├── model/                             # Model (logica di gioco)
│   │   ├── Player.java                    # Giocatore (nome + saldo)
│   │   ├── Symbol.java                    # Simboli della slot machine (enum)
│   │   ├── Reel.java                      # Singolo rullo
│   │   ├── SlotMachine.java               # Slot machine a 3 rulli
│   │   ├── Card.java                      # Carta da gioco
│   │   ├── Deck.java                      # Mazzo di 52 carte
│   │   ├── BlackjackGame.java             # Logica Blackjack
│   │   └── RouletteGame.java              # Logica Roulette europea
│   ├── view/                              # View (interfaccia utente)
│   │   ├── GameView.java                  # Interfaccia della vista
│   │   └── ConsoleView.java               # Implementazione console
│   └── controller/                        # Controller
│       ├── GameController.java            # Controller principale / menu
│       ├── SlotMachineController.java     # Controller slot machine
│       ├── BlackjackController.java       # Controller blackjack
│       └── RouletteController.java        # Controller roulette
└── test/java/it/casino/model/
    ├── PlayerTest.java
    ├── SlotMachineTest.java
    ├── BlackjackGameTest.java
    └── RouletteGameTest.java
```

## Giochi disponibili

| Gioco | Descrizione | Payout |
|-------|-------------|--------|
| 🎰 Slot Machine | 3 rulli con 7 simboli (Ciliegia, Arancia, Limone, Stella, BAR, Luigi, 7) | 2× – 150× la puntata |
| 🃏 Blackjack | Giocatore vs Banco, pesca fino a stare o sballare | 2× (3× per Blackjack naturale) |
| 🎡 Roulette | Roulette europea (0-36), puntate su numero/colore/parità/altezza | 2× o 36× |

## Prerequisiti

- **Java 17+**
- **Maven 3.6+**

## Come eseguire

```bash
# Compilare e creare il JAR
mvn package -DskipTests

# Avviare il gioco
java -jar target/luigis-casino-1.0-SNAPSHOT.jar
```

## Come eseguire i test

```bash
mvn test
```

Output atteso:

```
Tests run: 37, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Pattern MVC applicato

- **Model** – contiene tutta la logica di gioco; nessuna dipendenza da View o Controller.
- **View** (`GameView` interface + `ConsoleView`) – si occupa esclusivamente dell'I/O con l'utente; non contiene logica di gioco.
- **Controller** – coordina Model e View, gestisce il ciclo di gioco e le azioni dell'utente.
