////////////////////////////////////////////////////////////////
// Hoffman Encoding Project - observing characters A-G
// James W. Kellenberger
////////////////////////////////////////////////////////////////
//
// 
// demonstrates heaps - for priority queues
// demonstrates binary tree - for determining character encoding
// - Goals A: Show binary tree representation of A-G Hoffman encoding
//            Depicting the relative abundance of chars A-G
// - Goals B: Show the code table of the Hoffman Encoding 
// - Goals C: Show the encoded A-G portion of a file
// - Goals D: Retract A-G encoded string to recreate A-G order
////////////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;               // for Stack class

////////////////////////////////////////////////////////////////
// heap.java
// - I changed the node to comply with the Heap and Tree, meaning
// more data is public than necessary. This was done for ease of
// access. But, I have a better example of private practices
// below in the HoffmanEncoderStruc
////////////////////////////////////////////////////////////////

class Node //heap node class
   {                              
   public char cData;             // character storred
   public int iData;              // character frequency (key)
   public char pathDirection;     // for path tracing. "0"=Left, "1"=Right
   public Node leftChild;         // this node's left child
   public Node rightChild;        // this node's right child
   public Node rootParent;        // this node's parent

// -------------------------------------------------------------
   public Node(int key, char letter)           // constructor
      { iData = key; cData = letter; }
// -------------------------------------------------------------
   public Node getKey()
      { return this; }
// -------------------------------------------------------------
   public void setKey(int key, char letter)
   { iData = key; cData = letter; }
// -------------------------------------------------------------
   public void displayNode()      // display ourself
      {
      System.out.print('{');
      System.out.print(cData);
      System.out.print("} ");
      }
// -------------------------------------------------------------
   }  // end class Node
////////////////////////////////////////////////////////////////
class HoffmanEncoderStruc //heap node class
   {
   private String aCode;
   private String bCode;
   private String cCode;
   private String dCode;
   private String eCode;
   private String fCode;
   private String gCode;
// -------------------------------------------------------------
   public HoffmanEncoderStruc(){}                             // empty parameter constructor
// -------------------------------------------------------------
   public HoffmanEncoderStruc(String aCode, String bCode, String cCode, String dCode, String eCode, String fCode, String gCode)           
   { this.aCode = aCode; this.bCode = bCode; this.cCode = cCode; this.dCode = dCode;
     this.eCode = eCode; this.fCode = fCode; this.gCode = gCode;} // full parameter constructor
// -------------------------------------------------------------
   public String getACode(){ return aCode;}
   public String getBCode(){ return bCode;}
   public String getCCode(){ return cCode;}
   public String getDCode(){ return dCode;}
   public String getECode(){ return eCode;}
   public String getFCode(){ return fCode;}
   public String getGCode(){ return gCode;}
// -------------------------------------------------------------
   public void setACode(String code){ aCode = code;}
   public void setBCode(String code){ bCode = code;}
   public void setCCode(String code){ cCode = code;}
   public void setDCode(String code){ dCode = code;}
   public void setECode(String code){ eCode = code;}
   public void setFCode(String code){ fCode = code;}
   public void setGCode(String code){ gCode = code;}
// -------------------------------------------------------------
   public HoffmanEncoderStruc clone(){ return new HoffmanEncoderStruc(aCode,bCode, cCode, dCode, eCode, fCode, gCode); }
// -------------------------------------------------------------
   public String toString(){
      return "\nA:" + aCode + " \n" +
             "B:" + bCode + " \n" +
             "C:" + cCode + " \n" +
             "D:" + dCode + " \n" +
             "E:" + eCode + " \n" +
             "F:" + fCode + " \n" +
             "G:" + gCode + " \n";
   }
// -------------------------------------------------------------
   }  // end class Node
////////////////////////////////////////////////////////////////
class Heap
   {
   private Node[] heapArray;
   private int maxSize;           // size of array
   private int currentSize;       // number of nodes in array
// -------------------------------------------------------------
   public Heap(int mx)            // constructor
      {
      maxSize = mx;
      currentSize = 0;
      heapArray = new Node[maxSize];  // create array
      }
// -------------------------------------------------------------
   public boolean isEmpty()
      { return currentSize==0; }
// -------------------------------------------------------------
   public boolean insert(int key, char letter)
      {
      if(currentSize==maxSize)
         return false;
      Node newNode = new Node(key, letter);
      heapArray[currentSize] = newNode;
      trickleUp(currentSize++);
      return true;
      }  // end insert()
// -------------------------------------------------------------
    public boolean insert(Node parent) // replicate insert for tree construction
    {
        if(currentSize==maxSize)
            return false;
        heapArray[currentSize] = parent;
        trickleUp(currentSize++);
        return true;
    }  // end insert()
// -------------------------------------------------------------
   public void trickleUp(int index) // Push minimum to the top
      {
      int parent = (index-1) / 2;
      Node bottom = heapArray[index];

      while( index > 0 &&
             heapArray[parent].getKey().iData > bottom.getKey().iData )
         {
         heapArray[index] = heapArray[parent];  // move it down
         index = parent; 
         parent = (parent-1) / 2;
         }  // end while
      heapArray[index] = bottom;
      }  // end trickleUp()
// -------------------------------------------------------------
   public Node remove()           // delete item with max key
      {                           // (assumes non-empty list)
      Node root = heapArray[0];
      heapArray[0] = heapArray[--currentSize];
      trickleDown(0);
      return root;
      }  // end remove()
// -------------------------------------------------------------
   public void trickleDown(int index)
      {
      int smallerChild;
      Node top = heapArray[index];       // save root
      while(index < currentSize/2)       // while node has at
         {                               //    least one child,
         int leftChild = 2*index+1;
         int rightChild = leftChild+1;
                                         // find larger child
         if(rightChild < currentSize &&  // (rightChild exists?)
                             heapArray[leftChild].getKey().iData >
                             heapArray[rightChild].getKey().iData)
            smallerChild = rightChild;
         else
            smallerChild = leftChild;
                                         // top >= largerChild?
         if( top.getKey().iData <= heapArray[smallerChild].getKey().iData )
            break;
                                         // shift child up
         heapArray[index] = heapArray[smallerChild];
         index = smallerChild;            // go down
         }  // end while
      heapArray[index] = top;            // root to index
      }  // end trickleDown()
// -------------------------------------------------------------
   public boolean change(int index, int newValue)
      {
      if(index<0 || index>=currentSize)
         return false;
      int oldValue = heapArray[index].getKey().iData; // remember old
      heapArray[index].setKey(newValue, heapArray[index].cData);  // change to new

      if(oldValue > newValue)             // if raised,
         trickleUp(index);                // trickle it up
      else                                // if lowered,
         trickleDown(index);              // trickle it down
      return true;
      }  // end change()
// -------------------------------------------------------------
   public void displayHeap()
      {
      System.out.print("heapArray: ");    // array format
      for(int m=0; m<currentSize; m++)
         if(heapArray[m] != null)
            System.out.print( heapArray[m].getKey().cData + " ");
         else
            System.out.print( "-- ");
      System.out.println();
                                          // heap format
      int nBlanks = 32;
      int itemsPerRow = 1;
      int column = 0;
      int j = 0;                          // current item
      String dots = "...............................";
      System.out.println(dots+dots);      // dotted top line

      while(currentSize > 0)              // for each heap item
         {
         if(column == 0)                  // first item in row?
            for(int k=0; k<nBlanks; k++)  // preceding blanks
               System.out.print(' ');
                                          // display item
         System.out.print(heapArray[j].getKey().cData);

         if(++j == currentSize)           // done?
            break;

         if(++column==itemsPerRow)        // end of row?
            {
            nBlanks /= 2;                 // half the blanks
            itemsPerRow *= 2;             // twice the items
            column = 0;                   // start over on
            System.out.println();         //    new row
            }
         else                             // next item on row
            for(int k=0; k<nBlanks*2-2; k++)
               System.out.print(' ');     // interim blanks
         }  // end for
      System.out.println("\n"+dots+dots); // dotted bottom line
      }  // end displayHeap()
// -------------------------------------------------------------
   }  // end class Heap


////////////////////////////////////////////////////////////////
// tree.java
////////////////////////////////////////////////////////////////
class Tree
   {
   private Node root;             // first node of tree
   private HoffmanEncoderStruc hoffmanCodes;
// -------------------------------------------------------------
   public Tree()                  // constructor
      { root = null; }            // no nodes in tree yet
// -------------------------------------------------------------
   public Tree(Node root)                  // constructor
      { this.root = root; }            // no nodes in tree yet
// -------------------------------------------------------------
   public HoffmanEncoderStruc getHoffmanCodes(){return hoffmanCodes.clone();}
// -------------------------------------------------------------
   public Node find(int key)      // find node with given key
      {                           // (assumes non-empty tree)
      Node current = root;               // start at root
      while(current.iData != key)        // while no match,
         {
         if(key < current.iData)         // go left?
            current = current.leftChild;
         else                            // or go right?
            current = current.rightChild;
         if(current == null)             // if no child,
            return null;                 // didn't find it
         }
      return current;                    // found it
      }  // end find()
// -------------------------------------------------------------
   public Tree hoffmanTree(Heap heap){
    Node firstChild = null;
    Node secondChild = null;
    Node newParent;
    while(!heap.isEmpty()){
        firstChild = heap.remove();
        secondChild = null;
        if(!heap.isEmpty()){
            secondChild = heap.remove();
            newParent = hoffmanParent(firstChild, secondChild);
            heap.insert(newParent);
        }
    }
    return new Tree(firstChild);
}
// -------------------------------------------------------------
   public Node hoffmanParent(Node firstChild, Node secondChild){
    Node nodeParent = new Node((firstChild.iData + secondChild.iData), '-');
    if(firstChild.iData < secondChild.iData){
        firstChild.pathDirection = 0;
        nodeParent.leftChild = firstChild;
        secondChild.pathDirection = 1;
        nodeParent.rightChild = secondChild;
        firstChild.rootParent = nodeParent;
        secondChild.rootParent = nodeParent;
    } else{
        firstChild.pathDirection = 1;
        nodeParent.rightChild = firstChild;
        secondChild.pathDirection = 0;
        nodeParent.leftChild = secondChild;
        firstChild.rootParent = nodeParent;
        secondChild.rootParent = nodeParent;
    }
    return nodeParent;
}
// -------------------------------------------------------------
   public void hoffmanCodeGenerator(){ 
      hoffmanCodes = new HoffmanEncoderStruc();
      traverse(2);
  }
  // -------------------------------------------------------------
   public void insert(int id, char dd)
      {
      Node newNode = new Node(id, dd);// make new node
      //newNode.iData = id;           // insert data
      //newNode.dData = dd;
      if(root==null)                // no node in root
         root = newNode;
      else                          // root occupied
         {
         Node current = root;       // start at root
         Node parent;
         while(true)                // (exits internally)
            {
            parent = current;
            if(id < current.iData)  // go left?
               {
               current = current.leftChild;
               if(current == null)  // if end of the line,
                  {                 // insert on left
                  parent.leftChild = newNode;
                  return;
                  }
               }  // end if go left
            else                    // or go right?
               {
               current = current.rightChild;
               if(current == null)  // if end of the line
                  {                 // insert on right
                  parent.rightChild = newNode;
                  return;
                  }
               }  // end else go right
            }  // end while
         }  // end else not root
      }  // end insert()
// -------------------------------------------------------------
   public boolean delete(int key) // delete node with given key
      {                           // (assumes non-empty list)
      Node current = root;
      Node parent = root;
      boolean isLeftChild = true;

      while(current.iData != key)        // search for node
         {
         parent = current;
         if(key < current.iData)         // go left?
            {
            isLeftChild = true;
            current = current.leftChild;
            }
         else                            // or go right?
            {
            isLeftChild = false;
            current = current.rightChild;
            }
         if(current == null)             // end of the line,
            return false;                // didn't find it
         }  // end while
      // found node to delete

      // if no children, simply delete it
      if(current.leftChild==null &&
                                   current.rightChild==null)
         {
         if(current == root)             // if root,
            root = null;                 // tree is empty
         else if(isLeftChild)
            parent.leftChild = null;     // disconnect
         else                            // from parent
            parent.rightChild = null;
         }

      // if no right child, replace with left subtree
      else if(current.rightChild==null)
         if(current == root)
            root = current.leftChild;
         else if(isLeftChild)
            parent.leftChild = current.leftChild;
         else
            parent.rightChild = current.leftChild;

      // if no left child, replace with right subtree
      else if(current.leftChild==null)
         if(current == root)
            root = current.rightChild;
         else if(isLeftChild)
            parent.leftChild = current.rightChild;
         else
            parent.rightChild = current.rightChild;

      else  // two children, so replace with inorder successor
         {
         // get successor of node to delete (current)
         Node successor = getSuccessor(current);

         // connect parent of current to successor instead
         if(current == root)
            root = successor;
         else if(isLeftChild)
            parent.leftChild = successor;
         else
            parent.rightChild = successor;

         // connect successor to current's left child
         successor.leftChild = current.leftChild;
         }  // end else two children
      // (successor cannot have a left child)
      return true;                                // success
      }  // end delete()
// -------------------------------------------------------------
   // returns node with next-highest value after delNode
   // goes to right child, then right child's left descendents
   private Node getSuccessor(Node delNode)
      {
      Node successorParent = delNode;
      Node successor = delNode;
      Node current = delNode.rightChild;   // go to right child
      while(current != null)               // until no more
         {                                 // left children,
         successorParent = successor;
         successor = current;
         current = current.leftChild;      // go to left child
         }
                                           // if successor not
      if(successor != delNode.rightChild)  // right child,
         {                                 // make connections
         successorParent.leftChild = successor.rightChild;
         successor.rightChild = delNode.rightChild;
         }
      return successor;
      }
// -------------------------------------------------------------
   public void traverse(int traverseType)
      {
      
      switch(traverseType)
         {
         case 1: System.out.print("\nPreorder traversal: ");
                 preOrder(root);
                 break;
         case 2: System.out.print("\nInorder traversal:  ");
                 inOrder(root);
                 break;
         case 3: System.out.print("\nPostorder traversal: ");
                 postOrder(root);
                 break;
         }
      System.out.println();
      }
// -------------------------------------------------------------
   private void preOrder(Node localRoot)
      {
      if(localRoot != null)
         {
         System.out.print(localRoot.iData + " ");
         preOrder(localRoot.leftChild);
         preOrder(localRoot.rightChild);
         }
      }
// -------------------------------------------------------------
   private void inOrder(Node localRoot)
      {
      if(localRoot != null)
         {
         inOrder(localRoot.leftChild);
         System.out.print(localRoot.iData + " ");
         if(localRoot.cData != '-'){
               determineHoffmanCode(localRoot);
         }
         inOrder(localRoot.rightChild);
         }
      }
// -------------------------------------------------------------
   private void postOrder(Node localRoot)
      {
      if(localRoot != null)
         {
         postOrder(localRoot.leftChild);
         postOrder(localRoot.rightChild);
         System.out.print(localRoot.iData + " ");
         }
      }
// -------------------------------------------------------------
   private void determineHoffmanCode(Node localRoot)
   {
      char hoffmanHost = localRoot.cData;
      String hoffPath = "";
      while(localRoot.rootParent != null){
         hoffPath = Character.toString(hoffmanHost) + hoffPath;
         localRoot = localRoot.rootParent;
      }

      switch(hoffmanHost){
            case 'A':
                  hoffmanCodes.setACode(hoffPath); break;
            case 'B':
                  hoffmanCodes.setBCode(hoffPath); break;
            case 'C':
                  hoffmanCodes.setCCode(hoffPath); break;
            case 'D':
                  hoffmanCodes.setDCode(hoffPath); break;
            case 'E':
                  hoffmanCodes.setECode(hoffPath); break;
            case 'F':
                  hoffmanCodes.setFCode(hoffPath); break;
            case 'G':
                  hoffmanCodes.setGCode(hoffPath); break;
      }
   }     
// -------------------------------------------------------------
   public void displayTree()
      {
      Stack globalStack = new Stack();
      globalStack.push(root);
      int nBlanks = 32;
      boolean isRowEmpty = false;
      System.out.println(
      "......................................................");
      while(isRowEmpty==false)
         {
         Stack localStack = new Stack();
         isRowEmpty = true;

         for(int j=0; j<nBlanks; j++)
            System.out.print(' ');

         while(globalStack.isEmpty()==false)
            {
            Node temp = (Node)globalStack.pop();
            if(temp != null)
               {
               System.out.print(temp.cData);////////////////////////////////
               localStack.push(temp.leftChild);
               localStack.push(temp.rightChild);

               if(temp.leftChild != null ||
                                   temp.rightChild != null)
                  isRowEmpty = false;
               }
            else
               {
               System.out.print("--");
               localStack.push(null);
               localStack.push(null);
               }
            for(int j=0; j<nBlanks*2-2; j++)
               System.out.print(' ');
            }  // end while globalStack not empty
         System.out.println();
         nBlanks /= 2;
         while(localStack.isEmpty()==false)
            globalStack.push( localStack.pop() );
         }  // end while isRowEmpty is false
      System.out.println(
      "......................................................");
      }  // end displayTree()
// -------------------------------------------------------------

   }  // end class Tree
////////////////////////////////////////////////////////////////


public class Hoffman {

      public static void main(String[] args) throws IOException
      {
         //Tree theTree = new Tree();
         Heap priorityQueue = new Heap(8);
         priorityQueue.insert(4,'C');
         priorityQueue.insert(4,'G');
         priorityQueue.insert(25,'F');
         priorityQueue.insert(100,'E');
         priorityQueue.insert(1,'D');
         priorityQueue.insert(10,'B');
         priorityQueue.insert(16,'A');
         priorityQueue.displayHeap();

        //  while(!priorityQueue.isEmpty()){
        //        System.out.println(priorityQueue.remove().cData);
        //        priorityQueue.displayHeap();
        //  }

         Tree hoffman = new Tree().hoffmanTree(priorityQueue);
         hoffman.displayTree();
      }

      // theTree.insert(160, '-');
      // theTree.insert(60, '-');
      // theTree.insert(161, 'E');
      // theTree.insert(25, 'F');
      // theTree.insert(95, '-');
      // theTree.insert(70, 'A');
      // theTree.insert(110, '-');
      // theTree.insert(105, '-');
      // theTree.insert(115, 'B');

      // //theTree.insert(75, 'c');
      // //theTree.insert(50, 'a');
      // //theTree.insert(43, 'f');
      // //theTree.insert(37, 'e');
      // //theTree.insert(30, 'g');
      // //theTree.insert(25, 'b');
      // //theTree.insert(12, 'd');

      // while(true)
      //    {
      //    System.out.print("Enter first letter of show, ");
      //    System.out.print("insert, find, delete, or traverse: ");
      //    int choice = getChar();
      //    switch(choice)
      //       {
      //       case 's':
      //          theTree.displayTree();
      //          break;
      //       case 'i':
      //          System.out.print("Enter value to insert: ");
      //          value = getInt();
      //          theTree.insert(value, 'a');
      //          break;
      //       case 'f':
      //          System.out.print("Enter value to find: ");
      //          value = getInt();
      //          Node found = theTree.find(value);
      //          if(found != null)
      //             {
      //             System.out.print("Found: ");
      //             found.displayNode();
      //             System.out.print("\n");
      //             }
      //          else
      //             System.out.print("Could not find ");
      //             System.out.print(value + '\n');
      //          break;
      //       case 'd':
      //          System.out.print("Enter value to delete: ");
      //          value = getInt();
      //          boolean didDelete = theTree.delete(value);
      //          if(didDelete)
      //             System.out.print("Deleted " + value + '\n');
      //          else
      //             System.out.print("Could not delete ");
      //             System.out.print(value + '\n');
      //          break;
      //       case 't':
      //          System.out.print("Enter type 1, 2 or 3: ");
      //          value = getInt();
      //          theTree.traverse(value);
      //          break;
      //       default:
      //          System.out.print("Invalid entry\n");
      //       }  // end switch
      //    }  // end while
      // }  // end main()
// -------------------------------------------------------------
   public static String getString() throws IOException
      {
      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(isr);
      String s = br.readLine();
      return s;
      }
// -------------------------------------------------------------
   public static char getChar() throws IOException
      {
      String s = getString();
      return s.charAt(0);
      }
//-------------------------------------------------------------
   public static int getInt() throws IOException
      {
      String s = getString();
      return Integer.parseInt(s);
      }
// -------------------------------------------------------------
}
