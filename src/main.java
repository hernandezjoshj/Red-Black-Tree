import java.io.*;
import java.util.*;

public class main {

    public static void main(String[] args) throws IOException {
        RedBlackTreeMap<String, Integer> players = new RedBlackTreeMap<>();

        File file = new File("/Users/josh/Desktop/328Lab4/players_homeruns.csv");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String temp;

        try {
            while ((temp = br.readLine()) != null) {
                String[] tempA = temp.split(",");

                String playerName = tempA[0];
                Integer homeruns = Integer.parseInt(tempA[1]);
                players.add(playerName, homeruns);

                if (players.getCount() == 4) {
                    System.out.println("The pre-order traversal of the tree with 5 players is: ");
                    players.printStructure();
                }

                if (players.getCount() == 9) {
                    System.out.println("The pre-order traversal of the tree with 10 players is: ");
                    players.printStructure();
                    System.out.println("\n" +
                            "Key that is a LEAF: " + players.find("Stan Musial") +
                            "Key that is a ROOT: " + players.find("Honus Wagner") +
                            "Key that has ONE NIL CHILD and ONE NON-NIL CHILD: " + players.find("Rogers Hornsby") +
                            "Key that is in a RED NODE : " + players.find("Ted Williams"));
                }
            }
        }
        catch (NullPointerException NPE) {
            System.out.println("All players have been added to the tree");
        }



    }
}