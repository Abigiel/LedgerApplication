package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Scanner;

public class AccountingLedger {

    static Scanner scan = new Scanner(System.in);

    static HashMap<String, Logs> transactions = new HashMap<>();

    static FileWriter writer;
    static FileReader reader;

    static {
        try {
            writer = new FileWriter("src/main/resources/transactions.csv", true);

            reader = new FileReader("src/main/resources/transactions.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static BufferedReader buffReader = new BufferedReader(reader);
    static BufferedWriter buffWriter = new BufferedWriter(writer);


    public static void main(String[] args) throws IOException {
        homeScreenDisplay();

    }

    public static void homeScreenDisplay() throws IOException {


        if (buffReader.readLine() == null) {
            buffWriter.write("Date | Time | Description | Vendor | Amount");
        }


        System.out.println("Select an option: \n D. Add deposits \n P. Make Payment (Debit) \n L. Display Ledger \n X. Exit");

        String choice = scan.nextLine();

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        if (choice.equals("D")) {

            System.out.print("Please enter deposit information: ");


            System.out.print("\nDescription: ");
            String description = scan.nextLine();
            System.out.print("\nVendor: ");
            String vendor = scan.nextLine();
            System.out.print("\nAmount: ");
            double amount = scan.nextDouble();

            Logs log = new Logs(date, time, description, vendor, amount);
            buffWriter.write(log.getDate() + " | " + log.getTime() + " | " + log.getDescription() + " | " + log.getVendor() + " | " + log.getAmount());
            buffWriter.newLine();

            System.out.println("You have added this deposit to your ledger.");
            buffWriter.flush();
        } else if (choice.equals("P")) {
            System.out.println("Please enter payment information: ");


            System.out.print("\nDescription: ");
            String description = scan.nextLine();
            System.out.print("\nVendor: ");
            String vendor = scan.nextLine();
            System.out.print("\nAmount: ");
            double amount = scan.nextDouble();


            Logs log = new Logs(date, time, description, vendor, amount);

            buffWriter.write(log.getDate() + " | " + log.getTime() + " | " + log.getDescription() + " | " + log.getVendor() + " | " + -1 * log.getAmount());
            buffWriter.newLine();

            buffWriter.flush();
            System.out.println("You have added this payment to your ledger.");
        } else if (choice.equals("L")) {
            ledgerScreen();

        } else if (choice.equals("X")) {
            System.exit(0);

        } else {
            System.out.println("Invalid. Please select one of the options provided.");
            homeScreenDisplay();

        }


        writer.close();
    }

    public static void ledgerScreen() throws IOException {

        System.out.println("Select an option: \n A. All \n D. Deposits \n P. Payments \n R. Reports \n H. Home");


        String input;

        while ((input = buffReader.readLine()) != null) {

            String[] info = input.split("\\|"); // split using delimiter to create array


            String logDate = info[0].trim();
            DateTimeFormatter form = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate Date = LocalDate.parse(logDate, form);

            String logTime = info[1].trim();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime Time = LocalTime.parse(logTime, format);


            String logDescription = info[2].trim();
            String logVendor = info[3].trim();
            double logAmount = Double.parseDouble(info[4].trim());


            Logs log = new Logs(Date, Time, logDescription, logVendor, logAmount);

            if (log.getDescription() != null) {     // use loop to add all values to HashMap

                transactions.put(log.getDescription(), log);  // add products to inventory with descriptions as keys

            }

        }


        String choice = scan.nextLine();
        for (Logs t : transactions.values())
            switch (choice) {
                case "A":

                    System.out.printf("Date: %s | Time: %s | Description: %s | Vendor: %s | Amount %.2f\n",
                            t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                    break;
                case "D":

                    if (t.getAmount() > 0) {
                        System.out.printf("Date: %s | Time: %s | Description: %s | Vendor: %s | Amount $%.2f\n",
                                t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());

                    }
                    break;

                case "P":

                    if (t.getAmount() < 0) {
                        System.out.printf("Date: %s | Time: %s | Description: %s | Vendor: %s | Amount $%.2f\n",
                                t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                    }
                    break;
                case "R":
                    reportScreen();
                    break;

                case "H":
                    homeScreenDisplay();
                    break;

                default:
                    System.out.println("Invalid option. Please select one of the options provided");
                    ledgerScreen();
            }

    }

    public static void reportScreen() throws IOException {
        System.out.println("Please select an option: ");
        System.out.println(" 1. Month to Date \n 2. Previous Month \n 3. Year To Date \n 4. Previous Year \n 5. Search by Vendor \n 0. Back");


        Scanner myScanner = new Scanner(System.in);
        int answer = myScanner.nextInt();

        switch (answer) {
            case 1:
                for (Logs t : transactions.values()) {
                    LocalDate today = LocalDate.now();
                    LocalDate tDate = t.getDate();
                    if (tDate.getMonth() == today.getMonth() && !tDate.isAfter(today)) {
                        System.out.printf("Date: %s | Time: %s | Description: %s | Vendor: %s | Amount %.2f\n",
                                t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                    }
                }
                break;
            case 2:
                for (Logs t : transactions.values()) {
                    LocalDate today = LocalDate.now();
                    LocalDate monthBefore = today.minusMonths(1);
                    LocalDate tDate = t.getDate();
                    if (tDate.getMonth().equals(monthBefore.getMonth())) {
                        System.out.printf("Date: %s | Time: %s | Description: %s | Vendor: %s | Amount %.2f\n",
                                t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                    }
                }
                break;
            case 3:
                for (Logs t : transactions.values()) {
                    LocalDate today = LocalDate.now();
                    LocalDate tDate = t.getDate();
                    if (tDate.getYear() == today.getYear() && !tDate.isAfter(today)) {
                        System.out.printf("Date: %s | Time: %s | Description: %s | Vendor: %s | Amount %.2f\n",
                                t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                    }
                }
                break;

            case 4:
                for (Logs t : transactions.values()) {
                    LocalDate today = LocalDate.now();
                    LocalDate yearBefore = today.minusYears(1);
                    LocalDate tDate = t.getDate();
                    if (tDate.getYear() == (yearBefore.getYear())) {
                        System.out.printf("Date: %s | Time: %s | Description: %s | Vendor: %s | Amount %.2f\n",
                                t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                    }
                }
                break;
            case 5:
                System.out.println("Please enter the name of the vendor: ");
                String vendorName = scan.nextLine();

                for (Logs t : transactions.values()) {

                    if (t.getVendor().equalsIgnoreCase(vendorName)) {
                        System.out.printf("Date: %s | Time: %s | Description: %s | Vendor: %s | Amount %.2f\n",
                                t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                    }
                }
                break;

            case 0:
                homeScreenDisplay();
                break;
            default:
                System.out.println("Invalid option. Please select one of the options provided");
                reportScreen();

        }

    }

}
