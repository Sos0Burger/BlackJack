import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static ArrayList<String> croupierDeck = new ArrayList<>();
    public static ArrayList<String> playerDeck = new ArrayList<>();
    public static int cardsCounter = 0;

    public static boolean isCroupierReady;
    public static boolean isPlayerReady;
    public static String[] deck = new String[52];
    public static int money = 10000;
    public static int bet;
    public static void main(String[] args) {
        String[] cards = new String[]{"2 spade","2 club","2 diamond","2 heart","3 spade","3 club","3 diamond","3 heart","4 spade","4 club","4 diamond","4 heart","5 spade","5 club","5 diamond","5 heart","6 spade","6 club","6 diamond","6 heart","7 spade","7 club","7 diamond","7 heart","8 spade","8 club","8 diamond","8 heart","9 spade","9 club","9 diamond","9 heart","10 spade","10 club","10 diamond","10 heart","J spade","J club","J diamond","J heart","Q spade","Q club","Q diamond","Q heart","K spade","K club","K diamond","K heart","A spade","A club","A diamond","A heart"};

        Scanner in = new Scanner(System.in);




        while(true){
            isCroupierReady = false;
            isPlayerReady=false;
            croupierDeck = new ArrayList<>();
            playerDeck = new ArrayList<>();
            cardsCounter = 0;
            System.out.println("У вас: "+money);
            System.out.println("Введите ставку: ");
            bet = in.nextInt();
            if(bet>money){
                System.out.println("Ваша ставка превышает баланс");
                continue;
            }
            else{
                money=money-bet;
            }
            deck = BlackjackUtils.shuffleCards(cards);

            addCard(playerDeck);
            addCard(croupierDeck);
            addCard(playerDeck);
            addCard(croupierDeck);

            BlackjackUtils.showCards(croupierDeck, playerDeck, isPlayerReady);
            checkBlackJack();

            int choice;
            while(!isPlayerReady){
                if(Integer.parseInt(BlackjackUtils.cardCount(playerDeck))>21){
                    lose();
                    continue;
                }
                System.out.println("1) Добавить карту\n 2)Удвоить\n 3)Достаточно 4) Сдаться");
                if(playerDeck.size()==2 && playerDeck.get(0).equals( playerDeck.get(1))){
                    System.out.println("5) Разделить");
                }
                choice = in.nextInt();
                switch (choice){
                    case (1):
                        addCard(playerDeck);
                        cardsCounter++;
                        break;
                    case (2):
                        money-=bet;
                        bet*=2;
                        addCard(playerDeck);
                        isPlayerReady = true;
                        break;
                    case (3):
                        isPlayerReady = true;
                        break;
                    case(4):
                        money+=bet/2;
                        isPlayerReady=true;
                        break;
                }

            }

            while(!isCroupierReady&&Integer.parseInt(BlackjackUtils.cardCount(croupierDeck))<17){
                addCard(croupierDeck);
            }
            if(Integer.parseInt(BlackjackUtils.cardCount(croupierDeck))>21){
                System.out.println("У крупье перебор");
                win();
            }
            if(!isCroupierReady){
                checkCards();
            }
        }

    }
    public static void checkCards(){
        int croupierCardSum = Integer.parseInt(BlackjackUtils.cardCount(croupierDeck));
        int playerCardSum = Integer.parseInt(BlackjackUtils.cardCount(playerDeck));
        if(playerCardSum == croupierCardSum){
            draw();
        } else if (playerCardSum > croupierCardSum) {
            win();
        } else if (playerCardSum < croupierCardSum) {
            lose();
        }
    }
    public static void draw(){
        BlackjackUtils.showCards(croupierDeck, playerDeck, isPlayerReady);
        System.out.println("Ничья");
        money+=bet;
    }
    public static void checkBlackJack() {
        if (BlackjackUtils.cardCount(playerDeck).equals("21") && BlackjackUtils.cardCount(croupierDeck).equals("21")) {
            //ничья с блэкджеком
            System.out.println("Блэкджек у обоих");
            draw();
        } else if (BlackjackUtils.cardCount(playerDeck).equals("21") && !BlackjackUtils.cardCount(croupierDeck).equals("21")) {
            System.out.println("Блэкджек у игрока");
            money+=bet*2.5 - bet;
            win();
        } else if (BlackjackUtils.cardCount(croupierDeck).equals("21") && !BlackjackUtils.cardCount(playerDeck).equals("21")) {
            System.out.println("Блэкджек у крупье");
            lose();
        }
    }
    public static void lose(){
        bet=0;
        BlackjackUtils.showCards(croupierDeck, playerDeck, isPlayerReady);
        System.out.println("Крупье побеждает");
        isPlayerReady = true;
        isCroupierReady = true;
    }
    public static void win(){
        BlackjackUtils.showCards(croupierDeck, playerDeck, isPlayerReady);
        System.out.println("Игрок побеждает");
        money+=bet*2;
        isPlayerReady = true;
        isCroupierReady = true;
    }
    public static void addCard(ArrayList<String> playingDeck){
        playingDeck.add(deck[cardsCounter]);
        cardsCounter++;
        if(cardsCounter>=4) {
            BlackjackUtils.showCards(croupierDeck, playerDeck, isPlayerReady);
        }
    }
}
abstract class BlackjackUtils {
    public static void showCards(ArrayList<String> croupierDeck, ArrayList<String> playerDeck, boolean isPlayerReady) {
        System.out.println("\n\n\n");
        if (isPlayerReady) {

            System.out.println("Карты крупье: ");
            for (String card : croupierDeck
            ) {
                System.out.print(card + " ");
            }
            System.out.println("Сумма карт крупье: " + cardCount(croupierDeck));
            System.out.println("\nКарты игрока: ");
            for (String card : playerDeck
            ) {
                System.out.print(card + " ");
            }
            System.out.println("Сумма ваших карт: " + cardCount(playerDeck));
        }
        if (!isPlayerReady) {
            System.out.println("Карты крупье: ");
            System.out.print(croupierDeck.get(0) + " *");
            System.out.println();

            System.out.println("Карты игрока: ");
            for (String card : playerDeck
            ) {
                System.out.print(card + " ");
            }
            System.out.println("Сумма ваших карт: " + cardCount(playerDeck));
            System.out.println();
        }
    }

    public static String cardCount(ArrayList<String> deck) {
        int cardSum = 0;
        int AceCounter = 0;
        for (String card : deck
        ) {
            switch (card.split(" ")[0]){
                case ("A"):
                    cardSum += 11;
                    AceCounter++;
                    break;
                case ("K"):
                case ("Q"):
                case ("J"):
                case ("10"):
                    cardSum += 10;
                    break;
                case ("9"):
                case ("8"):
                case ("7"):
                case ("6"):
                case ("5"):
                case ("4"):
                case ("3"):
                case ("2"):
                    cardSum += Integer.parseInt(card.split(" ")[0]);
                    break;
            }
        }
        for (int i = 0; i < AceCounter; i++) {
            if (cardSum > 21) {
                cardSum -= 10;
            }
        }
        return String.valueOf(cardSum);

    }


    public static String[] shuffleCards(String[] cards){
        String[] deck = new String[52];
        Random rnd = new Random();
        for(int i=0;i< cards.length;i++){
            deck[i] = cards[rnd.nextInt(0,52)];
            for(int j = 0; j < deck.length;j++){
                if(deck[i].equals(deck[j]) && i!=j){
                    i--;
                    break;
                }
            }
        }
        return deck;
    }
}