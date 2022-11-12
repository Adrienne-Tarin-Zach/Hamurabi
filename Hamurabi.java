package hammurabi;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Hamurabi {
    Random rand = new Random();
    Scanner scanner = new Scanner(System.in);
    boolean reign = true;
    int count = 0;

    public static void main(String[] args) {
        new Hamurabi().playGame();
    }

    void playGame() {
        int people = 100;
        int grain = 2800;
        int acres = 1000;
        int landValue = 19;
        int totalDead = 0;
        int yearlyDead = 0;
        int acresPlanted = 0;

        while (reign) {
            printSummary(yearlyDead, people, grain, acres, landValue);

            int landBought = askHowManyAcresToBuy(landValue, grain);
            if (landBought==0) {
                int landSold = askHowManyAcresToSell(acres);
                acres -= landSold;
                grain += landSold*landValue;
            }
            if (landBought>0) {
                acres += landBought;
                grain -= landBought*landValue;
            }
            int foodPP = askHowMuchGrainToFeedPeople(grain, people);
            grain -= (foodPP*people);
            int plant = askHowManyAcresToPlant(acres, people, grain);
            grain -= (plant*2);

            int peopleFed = grain/20;
            int peopleStarved=0;
            if (peopleFed<people) {
                peopleStarved = people - peopleFed;
            }
            yearlyDead = peopleStarved;
            totalDead+=peopleStarved;
            int harvest = harvest(plant);
            grain+=harvest;
            landValue = newCostOfLand();
            count++;
            if (count==10) reign=false;
        }
        printPerformance();
    }

    private void printSummary(int yearlyDead, int people, int grain, int acres, int landValue) {
        String sb = "O great Hamurabi\n" +
                String.format("You are in year %s of your ten year rule.\n", count+1) +
                String.format("In the previous year %s people died.\n", yearlyDead) +
                String.format("The population is now %s.\n", people) +
                String.format("The city has %s bushels in storage.\n", grain) +
                String.format("The city owns %s acres of land.\n", acres) +
                String.format("Land is currently worth %s bushels per acre.\n", landValue);

        System.out.println(sb);
    }

    private int askHowManyAcresToBuy(int landValue, int grain) {
        int buy = getNumber("O great one, how many acres would you like to purchase?");
        if (buy*landValue>grain) {
            buy = getNumber(String.format("O you jest, my great liege. We only have %s bushels of grain.", grain));
        }
        return buy;
    }

    private int askHowManyAcresToSell(int acres) {
        int sell = getNumber("O great one, how many acres would you like to sell?");
        if (sell>acres) {
            sell = getNumber(String.format("O you jest, my great liege. We only have %s acres.", acres));
        }
        return sell;
    }

    private int askHowMuchGrainToFeedPeople(int grain, int people) {
        int feed = getNumber("Hamurabi, how much grain should each person be allowed?");
        if (feed*people>grain) {
            feed = getNumber(String.format("Surely you're joking. We only have %s bushels of grain", grain));
        }
        return feed;
    }
    private int askHowManyAcresToPlant(int acres, int people, int grain) {
        int plant = getNumber("Sir, how many acres of land would you like to plant with grain?");
        if (plant>people*10 || plant>grain/2*acres) {
            plant = getNumber("We cannot plant that many acres.");
        }
        return plant;
    }

    private int newCostOfLand() {
        return rand.nextInt(23-17 + 1) + 17;
    }

    int getNumber(String message) {
        while (true) {
            System.out.print(message);
            try {
                return scanner.nextInt();
            }
            catch (InputMismatchException e) {
                System.out.println("\"" + scanner.next() + "\" isn't a number!");
            }
        }
    }

    private int harvest(int plant) {
        int yield = rand.nextInt(6);
        return plant*yield;
    }

    private void printPerformance() {
        System.out.println("You did great");
    }
}

