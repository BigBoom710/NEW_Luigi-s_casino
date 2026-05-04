package it.casino;

import it.casino.controller.GameController;
import it.casino.view.ConsoleView;
import it.casino.view.GameView;

import java.util.Scanner;

/**
 * Punto di ingresso dell'applicazione Luigi's Casino.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci il tuo nome: ");
        String name = scanner.nextLine().trim();
        if (name.isBlank()) {
            name = "Giocatore";
        }

        GameView view = new ConsoleView();
        GameController controller = new GameController(view, name);
        controller.start();
    }
}
