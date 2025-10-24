package com.cavazos;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class CavazosExample {

  public static void main(String[] args) {
    try {
      InputStream in = CavazosExample.class.getResourceAsStream("/commands.json");
      if (in == null) {
        System.err.println(" Error: commands.json not found in resources folder.");
        return;
      }

      JSONParser parser = new JSONParser();
      JSONArray commandJSONArray =
          (JSONArray) parser.parse(new InputStreamReader(in, StandardCharsets.UTF_8));

      String[] commandArray = getCommandArray(commandJSONArray);

      Scanner input = new Scanner(System.in);
      boolean userQuit = false;

      // Two stacks for true undo/redo
      Deque<String> issuedHistory = new ArrayDeque<>();
      Deque<String> redoStack = new ArrayDeque<>();

      printMenu();

      while (!userQuit) {
        try {
          System.out.print("Enter a command: ");
          String choice = input.nextLine().trim().toLowerCase();

          switch (choice) {
            case "i": {
              if (commandArray.length == 0) {
                System.out.println("\nNo commands available to issue.");
                break;
              }

              Random rand = new Random();
              int randIndex = rand.nextInt(commandArray.length);
              String newCommand = commandArray[randIndex];

              issuedHistory.push(newCommand);
              redoStack.clear();

              System.out.println("\nGeneral Cavazos has issued the command: " + newCommand);
              break;
            }

            case "u": {
              if (!issuedHistory.isEmpty()) {
                String undone = issuedHistory.pop();
                redoStack.push(undone);
                System.out.println("\nGeneral Cavazos has ordered troops to undo: " + undone);
              } else {
                System.out.println("\nNo command to undo.");
              }
              break;
            }

            case "r": {
              if (!redoStack.isEmpty()) {
                String redone = redoStack.pop();
                issuedHistory.push(redone);
                System.out.println("\nGeneral Cavazos has ordered troops to redo: " + redone);
              } else {
                System.out.println("\nNo previous command to redo.");
              }
              break;
            }

            case "l": {
              System.out.println("\n----- List of all available commands -----");
              print(commandArray);
              break;
            }

            case "h": {
              System.out.println("\n----- Command History (most recent first) -----");
              if (issuedHistory.isEmpty()) {
                System.out.println("No commands have been issued yet.");
              } else {
                int index = 1;
                for (String cmd : issuedHistory) {
                  System.out.println(index++ + ". " + cmd);
                }
              }
              break;
            }

            case "q": {
              System.out.println("\nGoodbye, General Cavazos!");
              userQuit = true;
              break;
            }

            default: {
              System.out.println("\nInvalid option. Please enter i, l, u, r, h, or q.");
            }
          }

          // Reprint the menu after every command except quit
          if (!userQuit) {
            System.out.println();
            printMenu();
          }

        } catch (Exception innerError) {
          System.out.println("\nError: " + innerError.getMessage());
          System.out.println("Please try again.\n");
        }
      }

      input.close();

    } catch (Exception e) {
      System.out.println("\nFatal Error: Unable to start the General Cavazos Commander App.");
      System.out.println("Reason: " + e.getMessage());
      System.out.println("Please check that your commands.json file is valid and try again.");
    }
  }

  public static void printMenu() {
    System.out.println();
    System.out.println("------------------------------------------------------------");
    System.out.println("Welcome to General Cavazos Commander App");
    System.out.println("------------------------------------------------------------");
    System.out.println("i  Issue a random command");
    System.out.println("u  Undo the last command");
    System.out.println("r  Redo the last undone command");
    System.out.println("l  List all possible commands from JSON");
    System.out.println("h  Show command history");
    System.out.println("q  Quit");
    System.out.println("------------------------------------------------------------");
  }

  public static void print(String[] commandArray) {
    try {
      if (commandArray == null || commandArray.length == 0) {
        System.out.println("No commands to display.");
        return;
      }

      System.out.printf("Number\tCommand\n");
      System.out.printf("------\t---------------\n");
      for (int i = 0; i < commandArray.length; i++) {
        System.out.printf("%02d\t%s\n", i, commandArray[i]);
      }

    } catch (Exception e) {
      System.out.println("\nError printing command list: " + e.getMessage());
    }
  }

  public static String[] getCommandArray(JSONArray commandArray) {
    if (commandArray == null) {
      System.out.println("\nWarning: Command list is empty.");
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
