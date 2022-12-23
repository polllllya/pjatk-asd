import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ASD2 {
    LetterNode root;
    String longest;

    ASD2() {
        root = null;
        longest = "";
    }

    public String getLongest() {
        find(root, "");
        return longest;
    }

    public void find(LetterNode tmp, String word) {
        word = tmp.getLetter() + word;
        if (tmp.getLeftNode() != null)
            find(tmp.getLeftNode(), word);
        if (tmp.getRightNode() != null)
            find(tmp.getRightNode(), word);
        if (tmp.right == null && tmp.left == null) {
            if (longest.compareTo(word) < 0) {
                longest = word;
            }
        }
    }


    public static void main(String[] args) {
        ASD2 tree = new ASD2();
        try {
            Scanner scanner = new Scanner(new File(args[0]));
            String string;
            char letter;
            char[] path;

            while (scanner.hasNextLine()) {
                string = scanner.nextLine();
                String[] words = string.split(" ");

                if (!words[0].equals("")) {
                    letter = words[0].charAt(0);

                    if (words.length == 1) {
                        if (tree.root == null) tree.root = new LetterNode();
                        tree.root.setLetter(letter);
                    } else {
                        path = words[1].toCharArray();

                        if (tree.root == null) tree.root = new LetterNode();
                        LetterNode tmp = tree.root;

                        for (char c : path) {
                            if (c == 'R') {
                                if (tmp.right == null) tmp.right = new LetterNode();
                                tmp = tmp.right;
                            }
                            if (c == 'L') {
                                if (tmp.left == null) tmp.left = new LetterNode();
                                tmp = tmp.left;
                            }
                        }
                        tmp.setLetter(letter);
                    }
                }

            }
            scanner.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(tree.getLongest());
    }
}

class LetterNode {
    char letter;
    LetterNode left, right;

    public LetterNode() {
    }

    public void setLetter(char l) {
        letter = l;
    }

    public char getLetter() {
        return letter;
    }

    public LetterNode getLeftNode() {
        return left;
    }

    public LetterNode getRightNode() {
        return right;
    }
}



