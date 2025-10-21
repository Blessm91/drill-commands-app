package com.cavazos;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class CavazosExample {

  public static void main(String[] args) {
    try {
      InputStream in = CavazosExample.class.getResourceAsStream("/commands.json");
      if (in == null) {
        System.err.println("❌ Error: commands.json not found in resources folder.");
        return;
      }

      JSONParser parser = new JSONParser();
      JSONArray commandJSONArray =
          (JSONArray) parser.parse(new InputStreamReader(in, StandardCharsets.UTF_8));

      String[] commandArray = getCommandArray(commandJSONArray);

      Scanner input = new Scanner(System.in);
      boolean userQuit = false;
      boolean firstRun = true;

      ArrayList<String> issuedHistory = new ArrayList<>();
      int currentIndex = -1;

      printMenu();

      while (!userQuit) {
        try {
          if (firstRun) {
            System.out.print("Enter a command: ");
            firstRun = false;
          } else {
            System.out.print("Enter next command: ");
          }

          String choice = input.nextLine().trim().toLowerCase();

          switch (choice) {
            case "i":
              if (commandArray.length == 0) {
                System.out.println("\n⚠️ No commands available to issue.");
                break;
              }

              Random rand = new Random();
              int randIndex = rand.nextInt(commandArray.length);
              String newCommand = commandArray[randIndex];

              // If issuing after undo, clear any commands ahead
              if (currentIndex < issuedHistory.size() - 1) {
                issuedHistory.subList(currentIndex + 1, issuedHistory.size()).clear();
              }

              issuedHistory.add(newCommand);
              currentIndex = issuedHistory.size() - 1;

              System.out.println("\nIssued command: " + newCommand);
              break;

            case "u":
              if (currentIndex >= 0) {
                System.out.println("\nUndid command: " + issuedHistory.get(currentIndex));
                currentIndex--;
              } else {
                System.out.println("\n⚠️ No command to undo.");
              }
              break;

            case "r":
              if (currentIndex < issuedHistory.size() - 1) {
                currentIndex++;
                System.out.println("\nRedid command: " + issuedHistory.get(currentIndex));
              } else {
                System.out.println("\n⚠️ No command to redo.");
              }
              break;

            case "l":
              System.out.println("\n----- List of all commands -----");
              print(commandArray);
              break;

            case "q":
              System.out.println("\nGoodbye, Commander!");
              userQuit = true;
              break;

            default:
              System.out.println("\n❌ Invalid option. Please enter i, l, u, r, or q.");
          }

        } catch (Exception innerError) {
          System.out.println("\n⚠️ Error: " + innerError.getMessage());
          System.out.println("Please try again.\n");
        }
      }

      input.close();

    } catch (Exception e) {
      System.out.println("\n❌ Fatal Error: Unable to start the General Cavazos Commander App.");
      System.out.println("Reason: " + e.getMessage());
      System.out.println("Please check that your commands.json file is valid and try again.");
    }
  }

  public static void printMenu() {
    System.out.println();
    System.out.println("------------------------------------------------------------");
    System.out.println("General Cavazos Commander App");
    System.out.println("------------------------------------------------------------");
    System.out.println("i  Issue a command");
    System.out.println("l  List all of the commands");
    System.out.println("u  Undo the last command that was issued");
    System.out.println("r  Redo the last command that was issued");
    System.out.println("q  Quit");
    System.out.println("------------------------------------------------------------");
  }

  public static void randomCommand(String[] commandArray, int numCommand) {
    try {
      if (commandArray == null || commandArray.length == 0) {
        System.out.println("\n⚠️ No commands found to issue.");
        return;
      }

      Random rand = new Random();
      System.out.printf("Number\tCommand\n");
      System.out.printf("------\t---------------\n");

      for (int i = 0; i < numCommand; i++) {
        int randIndex = rand.nextInt(commandArray.length);
        System.out.printf("%04d\t%s\n", i, commandArray[randIndex]);
      }

    } catch (Exception e) {
      System.out.println("\n⚠️ Error issuing random commands: " + e.getMessage());
    }
  }

  public static void print(String[] commandArray) {
    try {
      if (commandArray == null || commandArray.length == 0) {
        System.out.println("⚠️ No commands to display.");
        return;
      }

      System.out.printf("Number\tCommand\n");
      System.out.printf("------\t---------------\n");
      for (int i = 0; i < commandArray.length; i++) {
        System.out.printf("%04d\t%s\n", i, commandArray[i]);
      }

    } catch (Exception e) {
      System.out.println("\n⚠️ Error printing command list: " + e.getMessage());
    }
  }

  public static String[] getCommandArray(JSONArray commandArray) {
    if (commandArray == null) {
      System.out.println("\n⚠️ Warning: Command list is empty.");
      return new String[0];
    }

    String[] arr = new String[commandArray.size()];
    for (int i = 0; i < commandArray.size(); i++) {
      Object obj = commandArray.get(i);
      arr[i] = (obj != null) ? obj.toString() : "Unknown Command";
    }
    return arr;
  }
}
