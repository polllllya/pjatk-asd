import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ASD1 {
    public static void main(String[] args) {
        longestMonotonicSubsequence(args[0]);
    }

    public static void longestMonotonicSubsequence(String fname) {
        try {
            Path path = Paths.get(fname);
            Scanner scanner = new Scanner(path);

            int actNumber, prevNumber = 0,
                    count = 0, sum = 0,
                    maxCount = 0, maxSum = 0;

            int repeat = 1;
            boolean isIncrease = true;

            while (scanner.hasNext()) {
                actNumber = Integer.parseInt(scanner.next());

                if (actNumber >= prevNumber && isIncrease) {
                    count++;
                    sum += actNumber;
                } else if (actNumber <= prevNumber && !isIncrease) {
                    count++;
                    sum += actNumber;
                } else {
                    if (maxCount < count) {
                        maxCount = count;
                        maxSum = sum;
                    }
                    count = 1 + repeat;
                    sum = actNumber + (repeat * prevNumber);
                    isIncrease = !isIncrease;
                }

                if (actNumber == prevNumber) repeat++;
                else repeat = 1;

                if (maxCount < count) {
                    maxCount = count;
                    maxSum = sum;
                }

                prevNumber = actNumber;
            }
            scanner.close();

            System.out.println(maxCount + " " + maxSum);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
