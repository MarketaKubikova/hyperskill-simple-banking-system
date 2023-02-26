package banking;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Application {
    private boolean loggedIn = false;

    Scanner scanner = new Scanner(System.in);
    Insert insert = new Insert();
    Select select = new Select();
    Update update = new Update();
    Delete delete = new Delete();

    boolean exit = false;
    Account loggedAccount = null;

    public void run() {

        while (!exit) {
            System.out.println();
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "0" -> {
                    exit();
                }
                case "1" -> {
                    System.out.println();
                    createAccount();
                }
                case "2" -> {
                    System.out.println();
                    login();
                    if (loggedIn) {
                        System.out.println();
                        handleAccountMenu();
                    }
                }
                default -> System.out.println("Wrong choice!");
            }
        }
    }

    private void printMenu() {
        String menu = "1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit";

        System.out.println(menu);
    }

    private void createAccount() {
        String cardNumber = generateCardNumber();
        String pin = generatePin();

        insert.insert(cardNumber, pin);

        Account account = new Account(cardNumber, pin);

        account.printAccountCreated();
    }

    private String generatePin() {
        Random random = new Random();
        StringBuilder pin = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            pin.append(random.nextInt(10));
        }

        return String.valueOf(pin);
    }

    private String generateCardNumber() {
        Random random = new Random();

        StringBuilder accountNumber = new StringBuilder();
        accountNumber.append(400000);

        for (int i = 0; i < 9; i++) {
            accountNumber.append(random.nextInt(10));
        }

        int[] numberArray = new int[15];

        for (int i = 0; i <numberArray.length; i++) {
            numberArray[i] = Integer.parseInt(String.valueOf(accountNumber.charAt(i)));
        }

        for (int i = 0; i < numberArray.length; i += 2) {
            numberArray[i] *= 2;
        }

        IntStream.range(0, numberArray.length).filter(i -> numberArray[i] > 9).forEach(i -> numberArray[i] -= 9);

        int controlNumber = Arrays.stream(numberArray).sum();

        int checkDigit = controlNumber % 10 != 0 ? 10 - controlNumber % 10 : 0;

        return accountNumber + String.valueOf(checkDigit);
    }

    private void login() {
        System.out.println("Enter your card number:");
        String cardNumber = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String pin = scanner.nextLine();

        Account account = select.getCardEqualsToCardNumberAndPin(cardNumber, pin);

        boolean match = account != null && account.getCardNumber().equals(cardNumber) && account.getPin().equals(pin);

        if (match) {
            loggedIn = true;
            loggedAccount = account;
            System.out.println();
            System.out.println("You have successfully logged in!");
        } else {
            System.out.println();
            System.out.println("Wrong card number or PIN!");
        }
    }

    private void handleAccountMenu() {
        while (!exit && loggedIn) {
            printAccountMenu();

            String choice = scanner.nextLine();

            switch (choice) {
                case "0" -> {
                    exit();
                }
                case "1" -> {
                    System.out.println();
                    printBalance();
                    System.out.println();
                }
                case "2" -> {
                    System.out.println();
                    addIncome();
                    System.out.println();
                }
                case "3" -> {
                    System.out.println();
                    doTransfer();
                    System.out.println();
                }
                case "4" -> {
                    System.out.println();
                    closeAccount();
                    System.out.println();
                }
                case "5" -> {
                    System.out.println();
                    logout();
                }
                default -> System.out.println("Wrong choice!");
            }
        }
    }

    private void printAccountMenu() {
        String menu = "1. Balance\n" +
                "2. Add income\n" +
                "3. Do transfer\n" +
                "4. Close account\n" +
                "5. Log out\n" +
                "0. Exit";

        System.out.println(menu);
    }

    private void printBalance() {
        Integer balance = select.getBalance(loggedAccount.getCardNumber(), loggedAccount.getPin());

        if (balance != null) {
            System.out.println("Balance: " + balance);
        } else System.out.println("Not found!");
    }

    private void addIncome() {
        System.out.println("Enter income:");
        int income = Integer.parseInt(scanner.nextLine());

        update.addIncome(income, loggedAccount.getCardNumber());
        System.out.println("Income was added!");
    }

    private void doTransfer() {
        System.out.println("Transfer");
        System.out.println("Enter card number:");
        String recipentCardNumber = scanner.nextLine();
        boolean cardExist = select.findCard(recipentCardNumber) != null;
        boolean isOwnAccount = recipentCardNumber.equals(loggedAccount.getCardNumber());

        if (!isOwnAccount) {
            if (luhnsCheck(recipentCardNumber)) {
                if (cardExist) {
                    System.out.println("Enter how much money you want to transfer:");
                    int amount = Integer.parseInt(scanner.nextLine());
                    boolean isEnoughMoney = select.getBalance(loggedAccount.getCardNumber(), loggedAccount.getPin()) >= amount;
                    if (isEnoughMoney) {
                        update.transferMoney(amount, loggedAccount.getCardNumber(), recipentCardNumber);
                        System.out.println("Success!");
                    } else System.out.println("Not enough money!");
                } else System.out.println("Such a card does not exist.");
            } else System.out.println("Probably you made a mistake in the card number. Please try again!");
        } else System.out.println("You can't transfer money to the same account!");
    }

    private void closeAccount() {
        delete.deleteAccount(loggedAccount.getCardNumber());
        System.out.println("The account has been closed!");
        loggedAccount = null;
        loggedIn = false;
    }

    private void logout() {
        loggedIn = false;
        loggedAccount = null;

        System.out.println("You have successfully logged out!");
    }

    private void exit() {
        exit = true;
        System.out.println();
        System.out.println("Bye!");
    }

    private boolean luhnsCheck(String cardNumber) {
        int[] numberArray = new int[16];

        for (int i = 0; i <numberArray.length; i++) {
            numberArray[i] = Integer.parseInt(String.valueOf((cardNumber.charAt(i))));
        }

        for (int i = 0; i < numberArray.length - 1; i += 2) {
            numberArray[i] *= 2;
        }

        IntStream.range(0, numberArray.length - 1).filter(i -> numberArray[i] > 9).forEach(i -> numberArray[i] -= 9);

        int sum = Arrays.stream(numberArray).sum();

        return sum % 10 == 0;
    }
}
