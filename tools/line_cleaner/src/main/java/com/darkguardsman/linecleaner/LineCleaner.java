package com.darkguardsman.linecleaner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * Created by Dark(DarkGuardsman, Robert) on 1/16/19.
 */
public class LineCleaner
{

    public static final HashMap<String, BiFunction<String, String, String>> COMMANDS = new HashMap();

    static
    {
        COMMANDS.put("remove", (string, args) ->
        {
            return string.replace(args, "");
        });
        COMMANDS.put("trim", (string, args) ->
        {
            return string.trim();
        });
        COMMANDS.put("remove-until", (string, args) ->
        {
            int index = string.indexOf(args);
            if (index == -1)
            {
                return string;
            }
            return string.substring(index + args.length());
        });
        COMMANDS.put("remove-after", (string, args) ->
        {
            int index = string.indexOf(args);
            if (index == -1)
            {
                return string;
            }
            return string.substring(0, index);
        });
    }

    public static void main(String[] args)
    {
        final String fileName = args[0];

        if (args.length == 1)
        {
            System.out.println("Error: No commands found");
            System.exit(-1);
        }

        //Collect commands to run on each line
        final List<String> commandOrder = new ArrayList();
        final HashMap<String, String> commandsToRun = new HashMap();
        for (int i = 1; i < args.length; i++)
        {
            final String arg = args[i].trim();
            int indexLeft = arg.indexOf("[");
            int indexRight = arg.indexOf("]");
            if (indexLeft == -1 || indexRight == -1)
            {
                System.out.println("Error: Commands require format as follows: 'command[args]'");
                System.out.println("\tExample: remove[tree]");
                System.out.println("\tExample: remove-until[:]");
                System.exit(-1);
            }

            String command = arg.substring(0, indexLeft).trim();
            String param = arg.substring(indexLeft + 1, indexRight);

            //Add command to map, : is to prevent overlaps
            String key = command + ":" + System.currentTimeMillis();
            commandsToRun.put(key, param);
            commandOrder.add(key);
        }

        //read file into stream
        try (Stream<String> fileStream = Files.lines(Paths.get(fileName)))
        {
            //Store stream so we can optional apply logic
            Stream<String> editStream = fileStream;
            for (final String key : commandOrder)
            {

                //Get command name
                String commandName = key;
                if (commandName.contains(":"))
                {
                    commandName = commandName.split(":")[0];
                }

                //Get args
                final String arg = commandsToRun.get(key);

                //Get command
                final BiFunction<String, String, String> command = COMMANDS.get(commandName);
                if (command == null)
                {
                    System.out.println("Error: Could not find command '" + commandName + "'");
                    System.exit(-1);
                }

                //Run command
                try
                {
                    editStream = editStream.map(string -> command.apply(string, arg));
                } catch (Exception e)
                {
                    System.out.println("Error: failed to run command '" + key + "' with args '" + arg + "'");
                    e.printStackTrace();
                    System.exit(-1);
                }
            }

            //Write file
            System.out.println("Writing File: " + fileName + "-out.txt");
            Files.write(Paths.get(fileName + "-out.txt"), (Iterable<String>) editStream::iterator);

        } catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
