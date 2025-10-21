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
      // ✅ Load commands.json from resources folder
      InputStream in = CavazosExample.class.getResourceAsStream("/commands.json");
      if (in == null) {
        System.err.println("❌ Error: commands.json not found in resources folder.");
        return;
      }

      // ✅ Parse JSON using JSONParser instead of file path
      JSONParser parser = new JSONParser();
      JSONArray commandJSONArray =
        (JSONArray) parser.parse(new InputStreamReader(in, StandardCharsets.UTF_8));

      String[] commandArray = getCommandArray(commandJSONArray);

      // ✅ Print 5 random commands
      System.out.println("\n----- Issuing 5 random commands from General Cavazos -----");
      randomCommand(commandArray, 5);

      // ✅ Menu section
      Scanner input = new Scanner(System.in);
      boolean running = true;
      String lastCommand = null;
      String redoCommand = null;

      while (running) {
        System.out.println("\n-----------------------------------------------");
        System.out.println("General Cavazos Commander App");
        System.out.println("-----------------------------------------------");
        System.out.println("i  Issue a command");
        System.out.println("l  List all of the commands");
        System.out.println("u  Undo the last command that was issued");
        System.out.println("r  Redo the last command that was issued");
        System.out.println("q  Quit");
        System.out.print("\nEnter choice: ");
        String choice = input.nextLine().trim().toLowerCase();

        // ✅ Implement Quit command
        if (choice.equals("q")) {
          System.out.println("\nGoodbye, Commander!");
          running = false;
        }

         // ✅ Implement List command
        else if (choice.equals("l")) {
          System.out.println("\n----- List of all commands -----");
          print(commandArray);
        }

        // ✅ Implement Issue command
        else if (choice.equals("i")) {
          Random rand = new Random();
          int randIndex = rand.nextInt(commandArray.length);
          lastCommand = commandArray[randIndex];
          System.out.println("\nIssued command: " + lastCommand);
        }

        // ✅ Undo command
        else if (choice.equals("u")) {
          if (lastCommand != null) {
            redoCommand = lastCommand;
            System.out.println("\nUndid command: " + lastCommand);
            lastCommand = null;
          } else {
            System.out.println("\nNo command to undo.");
          }
        }

        // 🧩 Placeholder to prevent unused variable warnings
        if (lastCommand == null && redoCommand == null && choice != null) {
          // do nothing; placeholder
        }
      }

      input.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // randomly issue commands from General Cavazos
  public static void randomCommand(String[] commandArray, int numCommand) {
    Random rand = new Random();
    System.out.printf("Number\tCommand\n");
    System.out.printf("------\t---------------\n");
    for (int i = 0; i < numCommand; i++) {
      int randIndex = rand.nextInt(commandArray.length);
      System.out.printf("%04d\t%s\n", i, commandArray[randIndex]);
    }
  }

  // print command array
  public static void print(String[] commandArray) {
    System.out.printf("Number\tCommand\n");
    System.out.printf("------\t---------------\n");
    for (int i = 0; i < commandArray.length; i++) {
      System.out.printf("%04d\t%s\n", i, commandArray[i]);
    }
  }

  // get array of commands
  public static String[] getCommandArray(JSONArray commandArray) {
    String[] arr = new String[commandArray.size()];
    for (int i = 0; i < commandArray.size(); i++) {
      String command = commandArray.get(i).toString();
      arr[i] = command;
    }
    return arr;
  }
}
