package com.pluralsight;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Scanner;

public class AccountingLedger {

    static Scanner scan = new Scanner(System.in);

    static HashMap<String, Logs> transactions  = new HashMap<>();

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

    static BufferedReader buffReader= new BufferedReader(reader);
    static BufferedWriter buffWriter = new BufferedWriter(writer);

    public AccountingLedger() throws IOException {
    }


    public static void main(String[] args) throws IOException {
        homeScreenDisplay();

    }

    public static void homeScreenDisplay() throws IOException{



            if (buffReader.readLine() == null) {
                buffWriter.write("Date | Time | Description | Vendor | Amount");
            }



            System.out.println("Select an option: \n D. Add deposits \n P. Make Payment (Debit) \n L. Display Ledger \n X. Exit");

            String choice = scan.nextLine();

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        if(choice.equals("D")) {

                System.out.print("Please enter deposit information: ");

//                LocalDate date = LocalDate.now();
//                LocalTime time = LocalTime.now();

                System.out.print("\nDescription: ");
                String description = scan.nextLine();
                System.out.print("\nVendor: ");
                String vendor = scan.nextLine();
                System.out.print("\nAmount: ");
                double amount = scan.nextDouble();

                Logs log = new Logs(date, time, description, vendor, amount);
                buffWriter.write(log.getDate() + " | " + log.getTime() + " | " + log.getDescription() + " | " + log.getVendor() + " | " + log.getAmount());
                buffWriter.newLine();
                transactions.put(log.getDescription(), log);
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
                transactions.put(log.getDescription(), log);
                System.out.println("You have added this payment to your ledger.");
        } else if(choice.equals("L")){
            ledgerScreen();

        } else if (choice.equals("X")) {
            System.exit(0);

        } else{
            System.out.println("Invalid. Please select one of the provided options.");
            homeScreenDisplay();

        }


    writer.close();
    }

    public static void ledgerScreen() throws IOException {

        System.out.println("Select an option: \n A. All \n D. Deposits \n P. Payments \n R. Reports \n H. Home");
        //static HashMap<String, Logs> transactions  = new HashMap<>();



//        while(true) {
//            transactions.put();
////        }
        String input = "";
//
        while ((input = buffReader.readLine()) != null) {
//
            String[] info = input.split("\\|"); // split using delimiter to create array


            String logDate= info[0];
            DateTimeFormatter form = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate Date =
            DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime formattedTime = LocalTime.parse("logTime",format);
            String logDescription = info[2];
            String logVendor = info[3];
            double logAmount = Double.parseDouble(info[4]);

//
//            Product p = new Product(sku, name, price, department);
            Logs log =  new Logs(formattedDate , formattedTime, logDescription, logVendor, logAmount);// maybe a new object

            if ( log.getDescription() != null) {     // use loop to add all values to HashMap

               transactions.put(log.getDescription(), log);  // add products to inventory with names as keys

            }
            else {
                continue;
            }

        }





        String choice = scan.nextLine();
        switch(choice){
            case "A":
                for (Logs  t : transactions.values()) {
                    System.out.printf("Date: %s | Time: %s | Description: %s | Vendor: %s | Amount $%.2f\n",
                            t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                }
            case "D":
                for (Logs  t : transactions.values()) {
                    if(t.getAmount()> 0 ) {
                        System.out.printf("Date: %s | Time: %s | Description: %s | Vendor: %s | Amount $%.2f\n",
                                t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                    }
                }
            case "P":
                for (Logs  t : transactions.values()) {
                    if(t.getAmount()< 0 ) {
                        System.out.printf("Date: %s | Time: %s | Description: %s | Vendor: %s | Amount $%.2f\n",
                                t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                    }
                }
            case "R":
            case "H":

        }

        }

}
