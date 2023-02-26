package banking;

import java.math.BigDecimal;

public class Account {

    private String cardNumber;
    private String pin;
    private int balance;

    public Account() {
    }

    public Account(String cardNumber, String pin) {
        this.cardNumber = cardNumber;
        this.pin = pin;
    }

    public Account(String cardNumber, String pin, int balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Your card number:\n" +
                cardNumber + '\n' +
                "Your card PIN:\n" +
                pin;
    }

    public void printAccountCreated() {
        System.out.println("Your card has been created\n" +
                this);
    }
}
