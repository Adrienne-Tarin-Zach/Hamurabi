package hammurabi.src.main;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Hammurabi {
    Random rand = new Random();
    Scanner scanner = new Scanner(System.in);
    String badJob = "\nYou suck!";
    boolean reign = true;
    int count = 0;

    public static void main(String[] args) {
        new Hammurabi().playGame();
    }

    void playGame() {
        int people = 100;
        int grain = 2800;
        int acres = 1000;
        int landValue = 19;
        int totalDead = 0;
        int yearlyDead = 0;
        int plagueDead = 0;
        int newPeople = 0;
        int cropsByRats = 0;
        printWelcome();

        while (reign) {
            printSummary(yearlyDead, people, grain, acres, landValue, newPeople, cropsByRats);

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
            int peopleFed = grain/20;

            int plant = askHowManyAcresToPlant(acres, people, grain);
            grain -= (plant*2);

            int peopleStarved = starvationDeaths(people, grain);
            if (peopleFed<people) {
                peopleStarved = people - peopleFed;
            }

            int harvest = harvest(plant);
            grain+=harvest;

            landValue = newCostOfLand();

            //uprising
            if (uprising(people, peopleStarved)) {
                System.out.println(badJob + "\nYou starved " + peopleStarved + " people in one year! To the gallows!");
                System.exit(0);
            }

            //plague
            plagueDead = plagueDeaths(people);

            yearlyDead = peopleStarved + plagueDead;
            totalDead += peopleStarved + plagueDead;

            //rats
            cropsByRats = grainEatenByRats(grain);
            grain -= cropsByRats/grain;

            //immigration (if statement)
            if (peopleStarved==0) newPeople = immigrants(people, grain, acres);

            people = people - peopleStarved - plagueDead + newPeople;
            count++;
            if (count==10) reign=false;
        }
        int percentDied = totalDead / 10;
        printPerformance(totalDead, acres, people, percentDied);
    }




    private void printWelcome() {
        System.out.println("Congratulations, you are the newest ruler of ancient Sumer, elected for a ten year term of office."+
                "\nYour duties are to dispense food, direct farming, and buy and sell land as needed to support your people."+
                "\nWatch out for rat infestations and the plague! Grain is the general currency, measured in bushels."+
                "\nThe following will help you in your decisions:" +
                "\n\tEach person needs at least 20 bushels of grain per year to survive"+
                "\n\tEach person can farm at most 10 acres of land"+
                "\n\tIt takes 2 bushels of grain to farm an acre of land"+
                "\n\tThe market price for land fluctuates yearly"+
                "\nRule wisely and you will be showered with appreciation at the end of your term. Rule poorly and you will be kicked out of office!"+
                "\n***************\n");
    }

    private void printSummary(int yearlyDead, int people, int grain, int acres, int landValue, int immigrants, int rats) {
        String sb = "O great Hammurabi\n" +
                String.format("You are in year %s of your ten year rule.\n", count+1) +
                String.format("In the previous year %s people died.\n", yearlyDead) +
                String.format("%s people immigrated to your land.\n", immigrants) +
                String.format("The population is now %s.\n", people) +
                String.format("Rats destroyed %s bushels, leaving the city with %s bushels in storage.\n",rats, grain) +
                String.format("The city owns %s acres of land.\n", acres) +
                String.format("Land is currently worth %s bushels per acre.\n", landValue);

        System.out.println(sb);
    }

    private int askHowManyAcresToBuy(int landValue, int grain) {
        int buy = getNumber("O great one, how many acres would you like to purchase? ");
        if (buy*landValue>grain) {
            buy = getNumber(String.format("O you jest, my great liege. We only have %s bushels of grain. ", grain));
        }
        return buy;
    }

    private int askHowManyAcresToSell(int acres) {
        int sell = getNumber("O great one, how many acres would you like to sell? ");
        if (sell>acres) {
            sell = getNumber(String.format("O you jest, my great liege. We only have %s acres. ", acres));
        }
        return sell;
    }

    private int askHowMuchGrainToFeedPeople(int grain, int people) {
        int feed = getNumber("Hammurabi, how much grain should each person be allowed? ");
        if (feed*people>grain) {
            feed = getNumber(String.format("Surely you're joking. We only have %s bushels of grain ", grain));
        }
        return feed;
    }
    private int askHowManyAcresToPlant(int acres, int people, int grain) {
        int plant = getNumber("Sir, how many acres of land would you like to plant with grain? ");
        if (plant>people*10 || plant>grain/2*acres) {
            plant = getNumber("We cannot plant that many acres. ");
        }
        return plant;
    }

    public int newCostOfLand() {
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

    public int starvationDeaths(int people, int grain) {
        if ((people * 20) > grain) {
            return people - (grain/20);
        }
        return 0;
    }

    public int harvest(int plant) {
        int yield = rand.nextInt(6)+1;
        return plant*yield;
    }

    private void printPerformance(int total, int acres, int people, int percentDied) {
        String result = "\nDuring your 10-year term as King, " + percentDied + " percent of the\n" +
                "population starved per year on average, for a total of " + total + " dead!\n" +
                "You started with 10 acres per person and ended with " +acres/people+ " acres per person.\n\n";
        if (percentDied > 33 || acres / people < 7) {
            result += badJob;
        }
        else if (percentDied> 10 || acres / people < 9) {
            result += "You're very unliked";
        }
        else if (percentDied > 3 || acres / people < 10) {
            int toHang = (int) ((rand.nextInt(people)+1)*.8);
            result += "You did OK, but " + toHang + "people still want to hang you.";
        }
        else
            result += "You did great, Hammurabi!";
        result += "\n\n\n\nSo long for now.";
        System.out.println(result);
    }

    public int plagueDeaths(int people) {
        if (Math.random() <= .15) {
            System.out.println("The coronavirus has killed half the population!");
            return people / 2;
        }
        return 0;
    }

    public int grainEatenByRats(int grain) {
        if (Math.random() <= .4) {
            return rand.nextInt(30-10+1)+10;
        }
        return 0;
    }

    public int immigrants(int people, int acres, int grain) {
        return (20 * acres + grain) / (100 * people) + 1;
    }

    public boolean uprising(int people, int dead) {
        return (dead>people*.45);
    }
}

