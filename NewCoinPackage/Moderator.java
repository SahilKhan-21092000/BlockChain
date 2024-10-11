//package NewCoinPackage;
//
//import HelperClasses.*;
//
//public class Moderator {
//
//  // Honest Initializing
//
//  public void initializeNewCoin(NewCoin_Honest NewCObj, int coinCount) {
//    int k = 100000;
//
//    // Setting the source for the coins
//
//    Members obj = new Members();
//    obj.UID = "Moderator";
//
//    // Initialising the coins and distributing in Round Robin fashion
//
//    for (int i = 0; i < coinCount; i++) {
//
//      Transaction temp = new Transaction();
//      temp.coinID = Integer.toString(k);
//      k++;
//      temp.Source = obj;
//      temp.Destination = NewCObj.memberlist[i % NewCObj.memberlist.length];
//      temp.coinsrc_block = null;
//      NewCObj.pendingTransactions.AddTransactions(temp);
//    }
//
//    // k-1 because in the last iteration k was incremented
//
//    NewCObj.latestCoinID = Integer.toString(k - 1);
//
//    int j = 0;
//
//    // Forming TransactionBlocks
//
//    while (true) {
//
//      Transaction[] arr = new Transaction[NewCObj.bChain.tr_count];
//
//      for (int q = 0; q < NewCObj.bChain.tr_count; q++) {
//        try {
//          arr[q] = NewCObj.pendingTransactions.RemoveTransaction();
//        } catch (EmptyQueueException e) {
//          e.printStackTrace();
//        }
//      }
//
//      TransactionBlock temp = new TransactionBlock(arr);
//
//      // Adding the coins in mycoins of the destination of transaction
//
//      for (int i = 0; i < arr.length; i++) {
//        arr[i].Destination.mycoins.add(new Pair<String, TransactionBlock>(arr[i].coinID, temp));
//      }
//      j += NewCObj.bChain.tr_count;
//
//      NewCObj.bChain.InsertBlock_Honest(temp);
//
//      // Condition to check if all the coins have been distributed
//
//      if (j == coinCount)
//        break;
//
//    }
//  }
//
//  // Honest Initializing
//
//  public void initializeNewCoin(NewCoin_Malicious NewCObj, int coinCount) {
//    int k = 100000;
//
//    // Setting the source for the coins
//
//    Members obj = new Members();
//    obj.UID = "Moderator";
//
//    // Initialising the coins and distributing in Round Robin fashion
//
//    for (int i = 0; i < coinCount; i++) {
//
//      Transaction temp = new Transaction();
//      temp.coinID = Integer.toString(k);
//      k++;
//      temp.Source = obj;
//      temp.Destination = NewCObj.memberlist[i % NewCObj.memberlist.length];
//      temp.coinsrc_block = null;
//      NewCObj.pendingTransactions.AddTransactions(temp);
//    }
//
//    // k-1 because in the last iteration k was incremented
//
//    NewCObj.latestCoinID = Integer.toString(k - 1);
//
//    int j = 0;
//
//    // Forming TransactionBlocks
//
//    while (true) {
//
//      Transaction[] arr = new Transaction[NewCObj.bChain.tr_count];
//      for (int q = 0; q < NewCObj.bChain.tr_count; q++) {
//        try {
//          arr[q] = NewCObj.pendingTransactions.RemoveTransaction();
//        } catch (EmptyQueueException e) {
//          e.printStackTrace();
//        }
//      }
//
//      TransactionBlock temp = new TransactionBlock(arr);
//
//      // Adding the coins in mycoins of the destination of transaction
//
//      for (int i = 0; i < arr.length; i++) {
//        arr[i].Destination.mycoins.add(new Pair<String, TransactionBlock>(arr[i].coinID, temp));
//      }
//
//      j += NewCObj.bChain.tr_count;
//
//      NewCObj.bChain.InsertBlock_Malicious(temp);
//
//      // Condition to check if all the coins have been distributed
//
//      if (j == coinCount)
//        break;
//
//    }
//  }
//
//}


package NewCoinPackage;

import HelperClasses.Pair;

public class Moderator {

    public void initializeNewCoin(NewCoin_Honest NewCObj, int coinCount) {
        // The initializeNewCoin method sets up the initial state of a NewCoin_Honest blockchain
        // by creating a specified number of coins and distributing them among the members.
        //int coinCount: The total number of coins to create.
        //NewCoin_Honest NewCObj: The object representing the honest blockchain system.
        //rounds is calculated based on the total number of coins divided by the number of transactions
        //per block. This determines how many blocks will be created.

        int rounds = coinCount / NewCObj.bChain.tr_count;
        int next_coin = 100000;
        int next_member = 0;
        Members mod = new Members();
        mod.UID = "Moderator";
// next_coin: Start from coin ID 100000 and increment for each new coin.
//next_member: Index to keep track of which member will receive the coin.
//mod: A Members object representing the moderator who will create the transactions.
  
// Outer Loop (Rounds): Iterates through the number of rounds needed to create all coins.
//Inner Loop (Transactions per Block): Creates transactions for each block.        
        for (int round = 0; round < rounds; round++) {
            Transaction[] trarray = new Transaction[NewCObj.bChain.tr_count];
            int idx = 0;
            for (int i = 0; i < NewCObj.bChain.tr_count; i++) {
                Transaction tr = new Transaction();
                tr.coinID = Integer.toString(next_coin);
                tr.Source = mod;
                tr.coinsrc_block = null;
                tr.Destination = NewCObj.memberlist[next_member];
                trarray[idx] = tr;

                NewCObj.latestCoinID = tr.coinID;
                next_member++;
                next_member %= NewCObj.memberlist.length;
                idx++;
                next_coin++;
            }
            TransactionBlock tB = new TransactionBlock(trarray);
            NewCObj.bChain.InsertBlock_Honest(tB);
            for (Transaction tr : trarray) {
                tr.Destination.mycoins.add(new Pair<>(tr.coinID, tB));
            }
        }
    }
// Moderator Role: The Moderator is responsible for generating all transactions. It essentially acts as the source for all coins.
//Round-Based Block Creation: Coins are distributed in blocks, with each block containing a number of transactions equal to the tr_count.
//Member Distribution: Coins are assigned to members in a round-robin fashion, ensuring each member receives an equal number of coins.
//Blockchain Update: Each block containing the transactions is added to the blockchain, and each member’s coin list is updated with their new coins.
    public void initializeNewCoin(NewCoin_Malicious NewCObj, int coinCount) {

        int rounds = coinCount / NewCObj.bChain.tr_count;
        int next_coin = 100000;
        int next_member = 0;
        Members mod = new Members();
        mod.UID = "Moderator";

        for (int round = 0; round < rounds; round++) {
            Transaction[] trarray = new Transaction[NewCObj.bChain.tr_count];
            int idx = 0;
            for (int i = 0; i < NewCObj.bChain.tr_count; i++) {
                Transaction tr = new Transaction();
                tr.coinID = Integer.toString(next_coin);
                tr.Source = mod;
                tr.coinsrc_block = null;
                tr.Destination = NewCObj.memberlist[next_member];
                trarray[idx] = tr;

                NewCObj.latestCoinID = tr.coinID;
                next_member++;
                next_member %= NewCObj.memberlist.length;
                idx++;
                next_coin++;
            }
            TransactionBlock tB = new TransactionBlock(trarray);
            NewCObj.bChain.InsertBlock_Malicious(tB);
            for (Transaction tr : trarray) {
                tr.Destination.mycoins.add(new Pair<>(tr.coinID, tB));
            }
        }
    }
}
