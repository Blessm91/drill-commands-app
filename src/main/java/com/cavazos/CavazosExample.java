package com.cavazos;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class CavazosExample {

  public static void main(String[] args) {
    try {
      // Load commands.json from resources
      InputStream in = CavazosExample.class.getResourceAsStream("/commands.json");
      if (in == null) {
        System.err.println("❌ Error: commands.json not found in resources folder.");
        return;
      }

      // Parse JSON file
      JSONParser parser = new JSONParser();
      JSONArray commandJSONArray =
        (JSONArray) parser.parse(new InputStreamReader(in, StandardCharsets.UTF_8));

      String[] commandArray = getCommandArray(commandJSONArray);

      // Display all commands once at startup
      System.out.println("----- List of all commands -----");
      print(commandArray);

      // Display five random commands
      System.out.println("\n----- Issuing 5 random commands from General Cavazos -----");
      randomCommand(commandArray, 5);

      // Menu loop
      Scanner input = new Scanner(System.in);
      boolean userQuit = false;
      String lastCommand = null;
      String redoCommand = null;

      while (!userQuit) {
        printMenu();
        System.out.print("\nEnter a command: ");
        String choice = input.nextLine().trim().toLowerCase();

        switch (choice) {
          case "i":
            Random rand = new Random();
            int randIndex = rand.nextInt(commandArray.length);
            lastCommand = commandArray[randIndex];
            redoCommand = null;
            System.out.println("\nIssued command: " + lastCommand);
            break;

          case "l":
            System.out.println("\n----- List of all commands -----");
            print(commandArray);
            break;

          case "u":
            if (lastCommand != null) {
              redoCommand = lastCommand;
              System.out.println("\nUndid command: " + lastCommand);
              lastCommand = null;
            } else {
              System.out.println("\nNo command to undo.");
            }
            break;

          case "r":
            if (redoCommand != null) {
              lastCommand = redoCommand;
              System.out.println("\nRedid command: " + redoCommand);
              redoCommand = null;
            } else {
              System.out.println("\nNo command to redo.");
            }
            break;

          case "q":
            System.out.println("\nGoodbye, Commander!");
            userQuit = true;
            break;

          default:
            System.out.println("\nInvalid command. Please try again.");
        }
      }

      input.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // Displays the interactive menu
  public static void printMenu() {
    System.out.println("\n-----------------------------------------------");
    System.out.println("General Cavazos Commander App");
    System.out.println("-----------------------------------------------");
    System.out.println("i  Issue a command");
    System.out.println("l  List all of the commands");
    System.out.println("u  Undo the last command that was issued");
    System.out.println("r  Redo the last command that was issued");
    System.out.println("q  Quit");
    System.out.println("-----------------------------------------------");
  }

  // Randomly issues five commands from the list
  public static void randomCommand(String[] commandArray, int numCommand) {
    Random rand = new Random();
    System.out.printf("Number\tCommand\n");
    System.out.printf("------\t---------------\n");
    for (int i = 0; i < numCommand; i++) {
      int randIndex = rand.nextInt(commandArray.length);
      System.out.printf("%04d\t%s\n", i, commandArray[randIndex]);
    }
  }

  // Prints all commands
  public static void print(String[] commandArray) {
    System.out.printf("Number\tCommand\n");
    System.out.printf("------\t---------------\n");
    for (int i = 0; i < commandArray.length; i++) {
      System.out.printf("%04d\t%s\n", i, commandArray[i]);
    }
  }

  // Converts JSONArray into String array
  public static String[] getCommandArray(JSONArray commandArray) {
    String[] arr = new String[commandArray.size()];
    for (int i = 0; i < commandArray.size(); i++) {
      String command = commandArray.get(i).toString();
      arr[i] = command;
    }
    return arr;
  }
}
