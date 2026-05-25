# Luigi's Picture Poker

Un rifacimento in Java del celebre minigioco "Luigi's Picture Poker" presente in Super Mario 64 DS / New Super Mario Bros.

## Come si gioca

L'obiettivo del gioco è ottenere una mano di carte migliore di quella di Luigi. 
Il mazzo è composto da 30 carte: 5 copie per ciascuno dei 6 simboli disponibili.

### Svolgimento del turno
1. **Inizio**: Clicca su **"Inizia"** per distribuire le carte. Riceverai 5 carte scoperte, mentre le 5 carte di Luigi rimarranno coperte.
2. **Scarto**: Valuta la tua mano. Sotto ogni carta troverai un pulsante **"Scarta"**. Cliccalo per selezionare le carte di cui vuoi liberarti (il bordo diventerà rosso e il testo cambierà in "Tieni" nel caso cambiassi idea).
3. **Pesca**: Una volta decise le carte da scartare, clicca su **"Pesca"**. Le carte scartate verranno sostituite con nuove carte dal mazzo. (Attenzione: anche Luigi scarterà e pescherà nuove carte in base alla sua strategia!).
4. **Risultato**: Le carte di Luigi verranno svelate e le mani riordinate. Chi ha la combinazione dal valore più alto vince la partita. In caso di parità di combinazione, vince chi possiede le carte col simbolo di maggior valore.

### Combinazioni Vincenti
Dalla più debole alla più forte:
1. **Niente** (Carta singola più alta)
2. **Coppia** (2 carte uguali)
3. **Doppia Coppia** (2 coppie di carte uguali)
4. **Tris** (3 carte uguali)
5. **Full House** (Un Tris e una Coppia)
6. **Poker** (4 carte uguali)
7. **Cinque Uguali** (5 carte uguali, la combinazione massima!)

### Gerarchia delle Carte
In caso di parità di combinazione (es. entrambi hanno una Coppia), si guarda il valore del simbolo.
L'ordine dei simboli, dal più debole al più forte, è consultabile in ogni momento nella legenda a destra della schermata di gioco:
1. Stelle
2. Picche
3. Fiori
4. Cuori
5. Quadri
6. Nuvole

## Come avviare il gioco

**Su Windows:**
Fai doppio click sul file `Run.bat` presente nella cartella.

**Da Terminale (tutti i sistemi operativi):**
1. Apri il terminale nella cartella del gioco.
2. Compila il codice con: `javac *.java`
3. Avvia il gioco con: `java GameController`
