package com.pluralsight;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AccountingLedger {

    static Scanner scan = new Scanner(System.in);

    static HashMap<String, Logs> transactions = new HashMap<>();
    static List<Logs> logsList = new ArrayList<>(transactions.values()); // used to make the values more accessible

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


        System.out.println("Hello! What would you like to do today? \nPlease select one of the following options: " + // Prompt user for response by displaying options
                "\n D. Add a deposit \n P. Make a Payment (Debit) \n L. Display Ledger \n X. Exit");

        String choice = scan.nextLine();

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        if (choice.equalsIgnoreCase("D")) {   // create condition to receive information for deposit

            System.out.println("Please enter deposit information: ");


            System.out.print("Description: ");
            String description = scan.nextLine();
            System.out.print("Vendor: ");
            String vendor = scan.nextLine();
            System.out.print("Amount: ");
            double amount = scan.nextDouble();

            Logs log = new Logs(date, time, description, vendor, amount);
            buffWriter.write(log.getDate() + " | " + log.getTime() + " | " + log.getDescription() + " | " + log.getVendor() + " | " + log.getAmount());
            buffWriter.newLine();

            System.out.println("You have added this deposit to your ledger.");
            buffWriter.flush();
        } else if (choice.equalsIgnoreCase("P")) { // create condition to receive information for payment
            System.out.println("Please enter payment information: ");


            System.out.print("Description: ");
            String description = scan.nextLine();
            System.out.print("Vendor: ");
            String vendor = scan.nextLine();
            System.out.print("Amount: ");
            double amount = scan.nextDouble();


            Logs log = new Logs(date, time, description, vendor, amount);


            buffWriter.write(log.getDate() + " | " + log.getTime() + " | " + log.getDescription() + " | " + log.getVendor() + " | " + -1 * log.getAmount());
            buffWriter.newLine();


            buffWriter.flush();
            System.out.println("You have added this payment to your ledger.");
        } else if (choice.equalsIgnoreCase("L")) {  // option to display the ledger screen
            ledgerScreen();

        } else if (choice.equalsIgnoreCase("X")) { // option to exit app
            System.exit(0);

        } else {
            System.out.println("Invalid. Please select one of the options provided.");  // a way of handling input that is not a given option
            homeScreenDisplay();

        }


        buffWriter.close(); // close writer
    }


    public static void formattedLogsList(List<Logs> logsList) {  //method to sort displays by date
        logsList.sort((d1, d2) -> {  // lambda expression used for sorting
            LocalDateTime first = LocalDateTime.of(d1.getDate(), d1.getTime());
            LocalDateTime second = LocalDateTime.of(d2.getDate(), d2.getTime());

            return second.compareTo(first);
        });


    }

    public static void ledgerScreen() throws IOException {

        System.out.println("Select an option: \n A. All \n D. Deposits \n P. Payments \n R. Reports \n H. Home"); // display options for ledger screen


        String input;

        while ((input = buffReader.readLine()) != null) {   //loop to read through all lines in file

            String[] info = input.split("\\|"); // split using delimiter to create array

            // assigning all values of the above array to variables
            String logDate = info[0].trim();
            DateTimeFormatter form = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // specifying date format that is in file
            LocalDate Date = LocalDate.parse(logDate, form);                    // parsing through date in input using format

            String logTime = info[1].trim();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss"); // specifying time format that is in file
            LocalTime Time = LocalTime.parse(logTime, format);                  // parsing through time in input using format


            String logDescription = info[2].trim();
            String logVendor = info[3].trim();
            double logAmount = Double.parseDouble(info[4].trim());              //parsing to turn string input into type double


            Logs log = new Logs(Date, Time, logDescription, logVendor, logAmount);


            //if (log.getDescription() != null) {     // use loop to add all values to HashMap

            transactions.put(log.getDescription(), log);  // add products to transactions with descriptions as keys

            //}


        }


        logsList.addAll(transactions.values()); // add hashmap values to an arraylist
        formattedLogsList(logsList); // use sorting method to sort through array

        String choice = scan.nextLine();
        for (Logs t : logsList)    //loop through all values in arraylist
            switch (choice.toUpperCase()) {
                case "A":
                    // prints out all values

                    System.out.printf("Date: %s | Time: %s | Description: %s | Vendor: %s | Amount $%.2f\n",
                            t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                    break;
                case "D":
                    // prints out all values that are deposits

                    if (t.getAmount() > 0) {
                        System.out.printf("Date: %s | Time: %s | Description: %s | Vendor: %s | Amount $%.2f\n",
                                t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());

                    }
                    break;

                case "P":
                    // prints out all values that are payments

                    if (t.getAmount() < 0) {
                        System.out.printf("Date: %s | Time: %s | Description: %s | Vendor: %s | Amount $%.2f\n",
                                t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                    }
                    break;
                case "R":
                    //displays the report screen options
                    reportScreen();
                    break;

                case "H":
                    // goes back to the first (home) screen
                    homeScreenDisplay();
                    break;

                default: // a way of handling input that is not a given option
                    System.out.println("Invalid option. Please select one of the options provided");
                    ledgerScreen(); // gives user a chance to give another input
            }
        buffReader.close(); //closing reader

    }

    public static void reportScreen() {


        System.out.println("Please select an option: ");         //prompt user for input from options given in the reports screen
        System.out.println(" 1. Month to Date \n 2. Previous Month \n 3. Year To Date " +
                "\n 4. Previous Year \n 5. Search by Vendor \n 0. Back");

        while (true) {
            Scanner myScanner = new Scanner(System.in);
            int answer = myScanner.nextInt();

            switch (answer) {   // use answer variable to accept input
                case 1:

                    for (Logs t : logsList) { //loops through each value in logsList
                        LocalDate today = LocalDate.now();
                        LocalDate tDate = t.getDate();

                        // specify conditions to filter values in current month
                        if (tDate.getMonth() == today.getMonth() && tDate.getYear() == today.getYear() && !tDate.isAfter(today)) {
                            System.out.printf("Date: %s | Time: %s | Description: %s | Vendor: %s | Amount $%.2f\n",
                                    t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                        }
                    }
                    break;
                case 2:
                    for (Logs t : logsList) {
                        LocalDate today = LocalDate.now();
                        Month monthBefore = Month.from(today.minusMonths(1));
                        LocalDate tDate = t.getDate();

                        // specify conditions to filter values in previous month
                        if (tDate.getMonth().equals(monthBefore) && tDate.getYear() == today.getYear() && !tDate.isAfter(today)) {
                            System.out.printf("Date: %s | Time: %s | Description: %s | Vendor: %s | Amount $%.2f\n",
                                    t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());

                        }
                    }
                    break;
                case 3:
                    for (Logs t : logsList) {
                        LocalDate today = LocalDate.now();
                        LocalDate tDate = t.getDate();

                        // specify conditions to filter values in current year
                        if (tDate.getYear() == today.getYear() && !tDate.isAfter(today)) {
                            System.out.printf("Date: %s | Time: %s | Description: %s | Vendor: %s | Amount $%.2f\n",
                                    t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                        }
                    }
                    break;

                case 4:
                    for (Logs t : logsList) {
                        LocalDate today = LocalDate.now();
                        Year yearNow = Year.from(today);
                        Year yearBefore = yearNow.minusYears(1);
                        LocalDate tDate = t.getDate();
                        Year tYear = Year.from(tDate);

                        // specify conditions to filter values in previous year
                        if (tYear.equals(yearBefore)) {
                            System.out.printf("Date: %s | Time: %s | Description: %s | Vendor: %s | Amount $%.2f\n",
                                    t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                        }
                    }
                    break;
                case 5:

                    //prompts user for vendor to filter values from that vendor
                    System.out.println("Please enter the name of the vendor: ");
                    String vendorName = scan.nextLine();

                    for (Logs t : logsList) {

                        if (t.getVendor().equalsIgnoreCase(vendorName)) { // filters by vendor name(case insensitive)
                            System.out.printf("Date: %s | Time: %s | Description: %s | Vendor: %s | Amount $%.2f\n",
                                    t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                        }
                    }
                    break;

                case 0:

                    // takes user back to the report screen options
                    reportScreen();
                    break;
                default:

                    //handles wrong input
                    System.out.println("Invalid option. Please select one of the options provided");
                    reportScreen();

            }

        }
    }


}
