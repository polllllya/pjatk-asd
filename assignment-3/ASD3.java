import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import static java.lang.Math.max;

public class ASD3 {
    public static void main(String[] args) {
        AVL tree = new AVL();
        try {
            Scanner scanner = new Scanner(new File(args[0]));
            String string;
            String[] numbers;
            int line = 0;
            int index = 0;

            while (scanner.hasNextLine() && line < 2) {
                if (line == 0) tree.operations = Integer.parseInt(scanner.nextLine());

                if (line == 1) {
                    string = scanner.nextLine();
                    numbers = string.split(" ");

                    for (String number : numbers) {
                        if (tree.root == null) {
                            tree.root = new Node(Integer.parseInt(number), null);
                        } else {
                            tree.root = tree.makeTree(tree.root, number);
                        }
                    }
                    numbers = null;

                    tree.sizeOfTree = tree.root.size;

                    while (tree.operations > 0) {
                        if (tree.root != null) {
                            tree.sizeOfTree = tree.root.size;
                            index = index % tree.sizeOfTree;
                            tree.p = tree.findPosition(tree.root, index);
                            if (tree.p.number % 2 != 0) {
                                tree.root = tree.addNode(tree.p, tree.p.number - 1);
                                index += tree.p.number;
                            } else {
                                int indexToDelete;
                                if (index + 1 != tree.sizeOfTree) {
                                    indexToDelete = index + 1;
                                } else {
                                    indexToDelete = 0;
                                    index--;
                                }

                                Node nodeToDelete = tree.findPosition(tree.root, indexToDelete);
                                index += nodeToDelete.number;

                                tree.root = tree.deleteNode(tree.root, indexToDelete);
                            }

                            //tree.print(tree.root);
                            //System.out.println("--------------------------------");
                        }
                        tree.operations--;
                    }
                }
                line++;
            }
            tree.sizeOfTree = (tree.root != null) ? tree.root.size : 0;
            index = (tree.sizeOfTree > 0) ? index % tree.sizeOfTree : index;
            tree.printAVL(tree.root, tree.sizeOfTree, index);

            scanner.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

class AVL {
    Node p;
    Node root;
    int operations;
    int sizeOfTree;


    public void printAVL(Node root, int size, int p) {
        if (root == null) {
            System.out.println("Tree is empty");
        } else {
            Node index;
            for (int i = p; i < size; i++) {
                index = findPosition(root, i);
                System.out.print(index.number + " ");
            }
            for (int i = 0; i < p; i++) {
                index = findPosition(root, i);
                System.out.print(index.number + " ");
            }
        }
    }

    public void print(Node start) {
        printRec(start, 0);
    }

    public void printRec(Node tmp, int space) {
        if (tmp == null)
            return;
        space += 2;
        printRec(tmp.getRightNode(), space);
        for (int i = 0; i < space - 2; i++)
            System.out.print("\t");
        System.out.print(tmp + "\n");
        printRec(tmp.getLeftNode(), space);
    }

    public Node findPosition(Node root, int findMe) {
        int tmpIndex;
        Node tmpNode = root;
        if (root.left != null)
            tmpIndex = root.left.size;
        else
            tmpIndex = 0;

        if (tmpIndex > findMe) {
            tmpNode = tmpNode.left;
            tmpNode = findPosition(tmpNode, findMe);
        } else if (tmpIndex < findMe) {
            tmpNode = tmpNode.right;
            findMe = findMe - tmpIndex - 1;
            tmpNode = findPosition(tmpNode, findMe);
        }
        return tmpNode;
    }

    public Node makeTree(Node root, String number) {
        Node tmp = root;
        while (tmp.right != null) {
            tmp = tmp.right;
        }
        tmp.right = new Node(Integer.parseInt(number), tmp);
        tmp = tmp.right;

        while (tmp.parent != null) {
            tmp = tmp.parent;
            tmp.updateHeight();
            tmp.updateSize();
            tmp = tmp.checkRotation();
        }
        return tmp;
    }

    public Node addNode(Node node, int number) {
        Node tmp;

        Node rightNode = node.right;
        node.right = new Node(number, node);
        node.right.parent = node;
        if (rightNode != null) {
            rightNode.parent = node.right;
            node.right.right = rightNode;
            tmp = rightNode;
        } else
            tmp = node.right;


        while (tmp.parent != null) {
            tmp = tmp.parent;
            tmp.updateHeight();
            tmp.updateSize();
            tmp = tmp.checkRotation();
        }
        return tmp;
    }

    public Node deleteNode(Node root, int index) {
        Node node = findPosition(root, index);

        Node tmp = node;
        if (node.right != null || node.left != null) {
            if (node.right != null) {
                tmp = node.right;

                while (tmp.left != null) {
                    tmp = tmp.left;
                }


                if (tmp.right != null) {
                    tmp.right.parent = tmp.parent;
                    if (tmp.parent.left == tmp) {
                        tmp.parent.left = tmp.right;
                    } else if (tmp.parent.right == tmp) {
                        tmp.parent.right = tmp.right;
                    }
                } else {
                    if (tmp.parent.left == tmp) {
                        tmp.parent.left = null;
                    } else if (tmp.parent.right == tmp) {
                        tmp.parent.right = null;
                    }
                }

            } else {
                tmp = node.left;
                if(tmp.left != null){
                    tmp.left.parent = node;
                }
                if(tmp.right != null) {
                    tmp.right.parent = node;
                }
                node.left = tmp.left;
                node.right = tmp.right;
            }

        } else {
            if (tmp.parent != null) {
                if (tmp.parent.left == tmp) {
                    tmp.parent.left = null;
                } else if (tmp.parent.right == tmp) {
                    tmp.parent.right = null;
                }

            } else
                return null;
        }

        node.number = tmp.number;

        while (tmp.parent != null) {
            tmp = tmp.parent;
            tmp.updateHeight();
            tmp.updateSize();
            tmp = tmp.checkRotation();
        }
        return tmp;
    }
}

class Node {
    int number, size, height;
    Node left, right, parent;

    public Node(int number, Node parent) {
        this.number = number;
        this.parent = parent;
        this.height = 0;
        this.size = 1;
    }

    @Override
    public String toString() {
        return "N" + number + "-" +
                "S" + size + "-" +
                "H" + height ;
    }

    public void updateSize() {
        size = 0;
        if (left != null) size += left.size;
        if (right != null) size += right.size;
        size += 1;
    }

    public int getHeight(Node node) {
        return node != null ? node.height : -1;
    }

    public void updateHeight() {
        int leftChildHeight = getHeight(left);
        int rightChildHeight = getHeight(right);
        height = max(leftChildHeight, rightChildHeight) + 1;
    }

    public int balanceFactor(Node node) {
        return getHeight(node.right) - getHeight(node.left);
    }

    public Node checkRotation() {
        Node node = this;
        int balance = balanceFactor(node);

        if (balance < -1) {
            if (balanceFactor(node.left) <= 0) {
                node = node.rotateRight();
            } else {
                node.left = node.left.rotateLeft();
                node = node.rotateRight();
            }
        }

        if (balance > 1) {
            if (balanceFactor(node.right) >= 0) {
                node = node.rotateLeft();
            } else {
                node.right = node.right.rotateRight();
                node = node.rotateLeft();
            }
        }
        return node;
    }

    Node rotateLeft() {
        Node rightChild = right;

        right = rightChild.left;
        if (rightChild.left != null) rightChild.left.parent = this;

        rightChild.left = this;
        rightChild.parent = parent;

        if(parent != null) {
            if (parent.left == this) {
                parent.left = rightChild;
            } else if (parent.right == this) {
                parent.right = rightChild;
            }
        }
        parent = rightChild;

        this.updateSize();
        this.updateHeight();
        rightChild.updateSize();
        rightChild.updateHeight();

        return rightChild;
    }

    Node rotateRight() {
        Node leftChild = left;

        left = leftChild.right;
        if (leftChild.right != null) leftChild.right.parent = this;

        leftChild.right = this;
        leftChild.parent = parent;

        if(parent != null) {
            if (parent.left == this) {
                parent.left = leftChild;
            } else if (parent.right == this) {
                parent.right = leftChild;
            }
        }

        parent = leftChild;
        this.updateSize();
        this.updateHeight();
        leftChild.updateSize();
        leftChild.updateHeight();

        return leftChild;
    }

    public Node getLeftNode() {
        return left;
    }

    public Node getRightNode() {
        return right;
    }
}
