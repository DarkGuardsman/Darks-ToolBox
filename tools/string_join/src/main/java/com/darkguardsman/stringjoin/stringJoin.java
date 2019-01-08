package com.darkguardsman.stringjoin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class stringJoin
{
    public static void main(String[] args)
    {
        final String fileName = args[0];
        final boolean unique = Boolean.parseBoolean(args[1]);
        final int limit = args.length > 2 ? Integer.parseInt(args[2]) : -1;

        //read file into stream, try-with-resources
        try (Stream<String> fileStream = Files.lines(Paths.get(fileName)))
        {
            //Store stream so we can optional apply logic
            Stream<String> editStream = fileStream;

            //Filter unqiue
            if (unique)
            {
                editStream = editStream.filter(distinctByKey(s -> s));
            }

            //Reduce bulk
            if (limit > 0)
            {
                editStream = editStream.limit(limit);
            }

            //Create output
            String output = "(" + editStream.collect(Collectors.joining(",")) + ")";

            //output
            System.out.println(output);

            //Check how many entries we actually joined
            String[] split = output.split(",");
            System.out.println("Count: " + split.length);

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor)
    {
        final Set<Object> alreadyFound = ConcurrentHashMap.newKeySet();
        return entry -> alreadyFound.add(keyExtractor.apply(entry));
    }
}
