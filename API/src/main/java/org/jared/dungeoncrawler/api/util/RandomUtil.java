package org.jared.dungeoncrawler.api.util;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil
{
    public static Object getRandomElementFromList(List<Object> objects)
    {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return objects.get(random.nextInt(objects.size()));
    }

    public static Object getRandomElementFromArray(Object[] objects)
    {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return objects[random.nextInt(objects.length)];
    }

    public static Integer generatePreferredNumbers(int[] listOfNotPreffered, int max, int min)
    {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        int randomNum;
        int retry = 1; //increasing this lessons the likely of our non-preferred numbers to show up
        HashSet<Integer> notPrefer = new HashSet<>();

        //add all the numbers we don't want to generate into a HashSet for easy lookup
        for(int index = 0; index < listOfNotPreffered.length; index++)
            notPrefer.add(listOfNotPreffered[index]);

        do {
            randomNum = random.nextInt((max - min) + 1) + min;
            if(notPrefer.contains(randomNum))
            {
                retry--;
            }
            //we found a good value, let's return it
            else{
                retry = 0;
            }
        } while (retry > 0);

        return randomNum;
    }
}
