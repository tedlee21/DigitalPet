package ui;

import model.*;
import persistence.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

//Digital Pet Application        //Console interface code based off TellerApp file from 210 class
                                 //Data persistence implementations based off JsonSerializationDemo file
public class PetApp {
    private static final String JSON_STORE = "./data/profile.json";
    private Profile user;
    private Pet myPet;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private static final Food COOKIE = new Food("Cookie", 2);
    private static final Food ICE_CREAM = new Food("Ice Cream", 5);
    private static final Food MAC_N_CHEESE = new Food("Mac & Cheese", 10);


    // EFFECTS : runs the digital pet application
    public PetApp() {
        runPet();
    }

    // MODIFIES: this
    // EFFECTS : processes the users input
    private void runPet() {
        boolean keepGoing = true;
        String command = "";

        setup();

        while (keepGoing) {
            displayMainMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        petSays();
        System.out.println("Goodbye for now " + user.getName() + "!");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("p")) {
            doPlay();
        } else if (command.equals("f")) {
            doFeed();
        } else if (command.equals("s")) {
            doShop();
        } else if (command.equals("b")) {
            printBag();
        } else if (command.equals("c")) {
            doBank();
        } else if (command.equals("w")) {
            saveProfile();
        } else if (command.equals("l")) {
            loadProfile();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS : initializes user Profile
    private void setup() {
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        System.out.println("Hello! What's your name?");
        String name = input.next();
        user = new Profile(name, 50);

        System.out.println("Hi " + user.getName() + ", what type of pet would you like?");
        PetType petType = selectPetType();
        System.out.println("What would you like to name your " + petType.toString().toLowerCase() + "?");
        String petName = input.next();
        myPet = new Pet(petName, petType);
    }

    // EFFECTS: displays main menu of options to user
    private void displayMainMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tp -> Play");
        System.out.println("\tf -> Feed");
        System.out.println("\ts -> Shop");
        System.out.println("\tb -> Bag");
        System.out.println("\tc -> Coin Bank");
        System.out.println("\tq -> Quit");
    }

    // EFFECTS: displays menu of pet choices of options to user
    private void displayPetChoices() {
        System.out.println("\nSelect from:");
        System.out.println("\td -> Dog");
        System.out.println("\tc -> Cat");
        System.out.println("\tb -> Bird");
        System.out.println("\th -> Hedgehog");
    }

    // EFFECTS: displays menu of moods that reflect the user
    private void displayMoodChoices() {
        System.out.println("\nSelect from:");
        System.out.println("\th -> Happy");
        System.out.println("\ts -> Sad");
        System.out.println("\ta -> Angry");
        System.out.println("\tt -> Tired");
    }

    // EFFECTS: displays menu of pet activities to the user
    private void displayPlayChoices() {
        System.out.println("\nSelect from:");
        System.out.println("\tt -> Talk");
        System.out.println("\tp -> Pet");
        System.out.println("\te -> Exercise");
        System.out.println("\tb -> Ball");
    }

    // EFFECTS: displays menu of food to buy from the shop
    private void displayShopChoices() {
        System.out.println("\nWe have:");
        System.out.println("\tc -> Cookies, " + COOKIE.getPrice() + " coins each");
        System.out.println("\ti -> Ice Cream, " + ICE_CREAM.getPrice() + " coins each");
        System.out.println("\tm -> Mac and Cheese, " + MAC_N_CHEESE.getPrice() + " coins each");
        System.out.println("\tl -> leave Shop");
    }

    // EFFECTS: prompts user to answer their mood then displays supportive message for that mood
    private void giveSupport() {
        petSays();
        System.out.println("How are you feeling today " + user.getName() + "?");
        String selection = "";  // force entry into loop

        while (!(selection.equals("h") || selection.equals("s") || selection.equals("a") || selection.equals("t"))) {
            displayMoodChoices();
            selection = input.next();
            selection = selection.toLowerCase();
        }

        petSays();
        if (selection.equals("h")) {
            System.out.println("YAY! I'm happy because you're happy! Let's continue to have a great day!");
        } else if (selection.equals("s")) {
            System.out.println("Aw " + user.getName() + ", I'm so sorry you're feeling down, try having some tea!");
        } else if (selection.equals("a")) {
            System.out.println("Who made you upset! Whoever it is, I'll beat them up!!!");
        } else {
            System.out.println("Life can be a lot sometimes. Better days will come, but until then, you have me!");
        }
    }

    // EFFECTS : prompts user to play with pet and displays results
    private void doPlay() {
        System.out.print("What would you like to do with " + myPet.getPetName() + "?");
        String selection = "";  // force entry into loop

        while (!(selection.equals("t") || selection.equals("p") || selection.equals("e") || selection.equals("b"))) {
            displayPlayChoices();
            selection = input.next();
            selection = selection.toLowerCase();
        }

        petSays();
        if (selection.equals("t")) {
            System.out.println("Hi " + user.getName() + "!!");
            giveSupport();
        } else if (selection.equals("p")) {
            System.out.println("Oh yeah, that's the spot!!");
        } else if (selection.equals("e")) {
            System.out.println("Whew! Ha! Whew! Ha! Working up a sweat!!");
        } else {
            playBall();
        }
    }

    // MODIFIES: this
    // EFFECTS : prompts user to select a slot from the bag with food to feed the pet;
    //           method ends if storage is empty; otherwise feeds pet 1 of the selected food,
    //           updates user storage, and displays updated storage and pets reaction
    private void doFeed() {
        System.out.println(myPet.getPetName() + " is hungry!");
        if (user.isEmpty()) {
            System.out.println("Your bag is empty! Go buy some food for " + myPet.getPetName() + "...");
        } else {
            int slot = -1;
            //forces user to select valid slot
            while (!(slot > -1) || !(slot < Profile.MAX_SIZE) || (user.readSlot(slot).getQuantity() == 0)) {
                printBag();
                System.out.println("Enter the slot number of the food you want to feed " + myPet.getPetName() + ".");
                slot = input.nextInt() - 1;
            }
            Food eatenFood = user.readSlot(slot).getFood();
            user.removeFood(eatenFood, 1);
            printBag();
            System.out.println(myPet.getPetName() + " really enjoyed the " + eatenFood.getType() + "!");
            petSays();
            System.out.println("Thanks " + user.getName() + "!! Yum, Yum!");
        }
    }

    // EFFECTS : prompts user to buy food or leave the shop; valid selection leads to check out
    private void doShop() {
        System.out.println("Welcome to the shop! We have safe foods for your pet to eat!");
        printBalance();
        System.out.println("What would you like to buy?");
        String selection = "";  // force entry into loop
        int count = 0;

        while (!(selection.equals("c") || selection.equals("i") || selection.equals("m") || selection.equals("l"))) {
            displayShopChoices();
            selection = input.next();
            selection = selection.toLowerCase();
        }

        if (!(selection.equals("l"))) {
            while (!(count > 0)) {
                System.out.println("How many would you like?");
                count = input.nextInt();
            }

            checkout(selection, count);
        }
    }

    // MODIFIES: this
    // EFFECTS : prompts user to choose if a loan is wanted; if yes, 20 coins
    //           are added to the users balance and updated balance is displayed;
    //           else, balance remains the same and is displayed
    private void doBank() {
        System.out.println("Welcome to the Coin Bank!");
        printBalance();

        String answer = "";   // force entry into loop

        while (!(answer.equals("y") || answer.equals("n"))) {
            System.out.println("Are you in need of a Loan, " + user.getName() + "?");
            System.out.println("\ty -> yes");
            System.out.println("\tn -> no");
            answer = input.next();
            answer = answer.toLowerCase();
        }

        if (answer.equals("y")) {
            System.out.println("Well, alright then, here's a little something to get you going.");
            user.addBalance(20);
            System.out.println("20 coins were added to your wallet!");
        } else {
            System.out.println("Alright then, you better be going now.");
        }
        printBalance();
    }


    // EFFECTS : displays results of playing with a ball depending on user's pet type
    private void playBall() {
        PetType type = myPet.getPetType();

        if (type.equals(PetType.DOG)) {
            System.out.println("Ooh Ooh! Ball!! Throw it far for me!!");
            System.out.println("*" + myPet.getPetName() + " runs for the ball and brings it back*");
            petSays();
            System.out.println("I got it!! I got it!!");
        } else if (type.equals(PetType.CAT)) {
            System.out.println("Hmmm a Ball?");
            System.out.println("*" + myPet.getPetName() + " bats the ball back and forth*");
            petSays();
            System.out.println("Amusing.");
        } else if (type.equals(PetType.BIRD)) {
            System.out.println("I.. I don't know how to play with this!");
            System.out.println("*" + myPet.getPetName() + " tries to roll the ball but falls over!*");
        } else {
            System.out.println("This ball is as big as I am!!");
            System.out.println("*" + myPet.getPetName() + " curls up next to the ball*");
            petSays();
            System.out.println("My new best friend! ..Other than you of course!!");
        }
    }

    // MODIFIES: this
    // EFFECTS : checks out selected amount of food and adds it to user storage,
    //           and updates balance; insufficient balance results in no purchase;
    //           returns user to Shop once done
    private void checkout(String foodLetter, int amount) {
        if (foodLetter.equals("c")) {
            if (amount * COOKIE.getPrice() <= user.getBalance()) {
                user.subBalance(amount * COOKIE.getPrice());
                user.addFood(COOKIE, amount);
            } else {
                System.out.println("You do not have enough coins!");
            }
        } else if (foodLetter.equals("i")) {
            if (amount * ICE_CREAM.getPrice() <= user.getBalance()) {
                user.subBalance(amount * ICE_CREAM.getPrice());
                user.addFood(ICE_CREAM, amount);
            } else {
                System.out.println("You do not have enough coins!");
            }
        } else {
            if (amount * MAC_N_CHEESE.getPrice() <= user.getBalance()) {
                user.subBalance(amount * MAC_N_CHEESE.getPrice());
                user.addFood(MAC_N_CHEESE, amount);
            } else {
                System.out.println("You do not have enough coins!");
            }
        }
        doShop();
    }

    // EFFECTS: prompts user to select type of pet and returns it as a String
    private PetType selectPetType() {
        String selection = "";  // force entry into loop

        while (!(selection.equals("d") || selection.equals("c") || selection.equals("b") || selection.equals("h"))) {
            displayPetChoices();
            selection = input.next();
            selection = selection.toLowerCase();
        }

        if (selection.equals("d")) {
            return PetType.DOG;
        } else if (selection.equals("c")) {
            return PetType.CAT;
        } else if (selection.equals("b")) {
            return PetType.BIRD;
        } else {
            return PetType.HEDGEHOG;
        }
    }

    // EFFECTS : displays pets name and ":" to indicate following statement
    //           is said by the pet
    private void petSays() {
        System.out.println(myPet.getPetName() + ": ");
    }

    // EFFECTS: prints current user balance to the screen
    private void printBalance() {
        System.out.println("You have: " + user.getBalance() + " coins.");
    }

    // EFFECTS: prints out contents of user storage; If the storage slot is empty, prints "empty"
    private void printBag() {
        System.out.println("\nBAG");
        for (int i = 0; i < Profile.MAX_SIZE; i++) {
            Slot f = user.readSlot(i);

            System.out.print("Slot " + (i + 1) + ": ");
            if (f.getQuantity() != 0) {
                System.out.println(f.getFood().getType() + " x " + f.getQuantity());
            } else {
                System.out.println(" empty ");
            }
        }
    }

    // EFFECTS: saves the profile to file
    private void saveProfile() {
        try {
            jsonWriter.open();
            jsonWriter.write(user);
            jsonWriter.close();
            System.out.println("Saved " + user.getName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads profile from file
    private void loadProfile() {
        try {
            user = jsonReader.read();
            System.out.println("Loaded " + user.getName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
