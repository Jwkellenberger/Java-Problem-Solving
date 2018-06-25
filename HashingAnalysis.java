// Assignment 6
// hash.java
// demonstrates hash table with linear probing

import java.io.*;
import java.util.Scanner;
import java.lang.StringBuffer;
import java.text.NumberFormat;
import java.text.DecimalFormat;
////////////////////////////////////////////////////////////////
class DataItem
   {                                // (could have more data)
   private String sData;               // data item (key)
//--------------------------------------------------------------
   public DataItem(String sData)          // constructor
      { this.sData = sData; }
//--------------------------------------------------------------
   public String getKey()
      { return sData; }
//--------------------------------------------------------------
   }  // end class DataItem
////////////////////////////////////////////////////////////////
class HashTable
   {
   private int stringCount = 0;
   private boolean isQuadratic = false;// not quadratic unless stated
   private DataItem[] hashArray;       // array holds hash table
   private int arraySize;
   private DataItem nonItem;           // for deleted items
// -------------------------------------------------------------
   public int getStringCount(){
      return stringCount;   
   }
// -------------------------------------------------------------
   public HashTable(int size)       // constructor
      {
      arraySize = size;
      hashArray = new DataItem[arraySize];
      nonItem = new DataItem("_DEL_");   // deleted item key is -1
      }
// -------------------------------------------------------------
    public HashTable(int size, boolean isQuadratic)       // constructor
      {
     this.isQuadratic = isQuadratic;
     arraySize = size;
      hashArray = new DataItem[arraySize];
      nonItem = new DataItem("_DEL_");   // deleted item key is -1
      }
// -------------------------------------------------------------
   public void displayTable()
      {
      if(isQuadratic){
         System.out.print("Quadratic Hash Table: \t[");
      } else
      System.out.print("Linear Hash Table: \t\t[");
      for(int j=0; j<arraySize; j++)
         {
         if(j>0)
            System.out.print(", ");
         if(hashArray[j] != null)
            System.out.print(hashArray[j].getKey());
         else
            System.out.print(j);
         }
      if(isQuadratic){
         System.out.println("]\n\n");      
      } else
         System.out.println("]");
      }
// -------------------------------------------------------------
   public int hashFunc(String key)
      {
        int hash = key.charAt(0);
        for(int i =1; i< key.length(); i++){
           hash = hash*26 + key.charAt(i);
           hash %= arraySize;
        }
        //System.out.println(key + " " + hash);
        return hash;
      //return 0 % arraySize;       // hash function
      }
// -------------------------------------------------------------
    public int[] insert(String key)   // insert a DataItem
    // (assumes table not full)
    {
    DataItem item = new DataItem(key);
    int hashVal = hashFunc(key);       // hash the key
    int probeLength = 0;
    int[] resultArray = new int[2];
      while(hashArray[hashVal] != null &&
                      hashArray[hashVal].getKey() != "_DEL_")
         {
            if(isQuadratic){
                hashVal += 2*probeLength +1;
             }                          
             else{++hashVal; }
             probeLength++;    // go to next cell                
             hashVal %= arraySize;   // wraparound if necessary
         }      
         resultArray[0] = ++probeLength;
         resultArray[1] = hashVal;                    
         hashArray[hashVal] = item;      // insert item
         stringCount++;
     
      return resultArray;
    }
// -------------------------------------------------------------
   public int delete(String key)  // delete a DataItem
      {
      int hashVal = hashFunc(key);  // hash the key
      int probeLength = 0;
      while(hashArray[hashVal] != null)  // until empty cell,
         {                               // found the key?
         if(hashArray[hashVal].getKey().equals(key))
            {
            DataItem temp = hashArray[hashVal]; // save item
            hashArray[hashVal] = nonItem; // delete item
            stringCount--;
            return probeLength + 1;       // return item
            }
            if(isQuadratic){
                hashVal += 2*probeLength +1;
            }                          
            else{++hashVal; }           // go to next cell
            probeLength++;                 // go to next cell
         hashVal %= arraySize;      // wraparound if necessary
         }
      if(hashArray[hashVal] == null){
         return (probeLength+1)*-1;
      }else
      return probeLength+1;                    // can't find item
      }
// -------------------------------------------------------------
   public int find(String key)    // find item with key
      {
      int hashVal = hashFunc(key);  // hash the key
      int probeLength = 0;
      while(hashArray[hashVal] != null)  // until empty cell,
         {                               // found the key?
         if(hashArray[hashVal].getKey().equals(key))
            return probeLength + 1;   // yes, return item
        
            if(isQuadratic){
                hashVal += 2*probeLength +1;
            }                          
            else{++hashVal; }           // go to next cell
            probeLength++;                 // go to next cell
         hashVal %= arraySize;      // wraparound if necessary
         }
      if(hashArray[hashVal] == null){
         return (probeLength+1)*-1;
      }else
      return probeLength+1;                    // can't find item
      }
// -------------------------------------------------------------
   }  // end class HashTable
////////////////////////////////////////////////////////////////
class n00624794
   {
   public static void main(String[] args) throws IOException
    {
    HashTable[] theHashTable = fileStringCountHeapConstructor(args[0]);

    fileStringCountHashTableFindMethod(args[1], theHashTable);
    
    fileStringCountHashTableDeleteMethod(args[2], theHashTable);

    System.out.print("Done: ");

    }  // end main()
//--------------------------------------------------------------
    public static HashTable[] fileStringCountHeapConstructor(String filename)
    {
        methodTitleGenerator("Insert Methods" , "", "=====");
        String content = null;
        File file = new File(filename); // For example, foo.txt
        Scanner reader = null;
        String readerString;
        int heapSize =0;
        HashTable[] hashTables = {null, null};
        DataItem hashItem = null;
        int[] tracker;
        String linearHashFinalPrint = appendInsertData("Linear HashTable Insertion:");
        String quadraticHashFinalPrint = appendInsertData("Quadratic HashTable Insertion:");
        double linearInsertProbeLengthSum = 0;
        double quadraticInsertProbeLengthSum = 0;

        try {
            reader = new Scanner(file);
            while(reader.hasNext()){
                readerString = reader.next();
                heapSize++;
            }
            //System.out.println(heapSize);
            heapSize = findLargerPrime(heapSize);
            hashTables[0] = new HashTable(heapSize);
            hashTables[1] = new HashTable(heapSize,true);
            
            reader = new Scanner(file);
            
            while(reader.hasNext()){
                readerString = reader.next();
                tracker = hashTables[0].insert(readerString);
                linearInsertProbeLengthSum += tracker[0];
                linearHashFinalPrint += appendInsertData(tracker, readerString);
                
                tracker = hashTables[1].insert(readerString);
                quadraticInsertProbeLengthSum += tracker[0];
                quadraticHashFinalPrint += appendInsertData(tracker, readerString);

            }
            linearHashFinalPrint += appendAverageProbeLength(hashTables[0].getStringCount(), linearInsertProbeLengthSum);
            quadraticHashFinalPrint += appendAverageProbeLength(hashTables[1].getStringCount(), quadraticInsertProbeLengthSum);
            System.out.print(linearHashFinalPrint);
            System.out.print(quadraticHashFinalPrint);
            
            hashTables[0].displayTable();
            //System.out.println(hashTables[0].getStringCount());
            hashTables[1].displayTable();
            //System.out.println(hashTables[1].getStringCount());
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader != null){
                reader.close();
            }
        }
        return hashTables;
    }
//--------------------------------------------------------------
    public static void fileStringCountHashTableFindMethod(String filename, HashTable[] hashTables)
    {
        methodTitleGenerator("Find Methods" , "=============================", "====================================");
        String content = null;
        File file = new File(filename); // For example, foo.txt
        Scanner reader = null;
        String readerString;
        int heapSize =0;
        DataItem hashItem = null;
        int tracker;
        String linearHashFinalPrint = appendFindOrDeleteData("Linear HashTable Find:");
        String quadraticHashFinalPrint = appendFindOrDeleteData("Quadratic HashTable Find:");

        double linProbeFailCt = 0;
        double linProbeSuccCt = 0;
        double linProbeFailSum = 0;
        double linProbeSuccSum = 0;

        double quadProbeFailCt = 0;
        double quadProbeSuccCt = 0;
        double quadProbeFailSum = 0;
        double quadProbeSuccSum = 0;

        try {
            reader = new Scanner(file);
            
            while(reader.hasNext()){
                readerString = reader.next();
                tracker = hashTables[0].find(readerString);
                if(tracker < 0){
                    linProbeFailCt++;
                    linProbeFailSum += tracker*(-1);
                }else{
                    linProbeSuccCt++;
                    linProbeSuccSum += tracker;
                }
                linearHashFinalPrint += appendFindOrDeleteData(tracker, readerString);
                
                tracker = hashTables[1].find(readerString);
                if(tracker < 0){
                    quadProbeFailCt++;
                    quadProbeFailSum += tracker*(-1);
                }else{
                    quadProbeSuccCt++;
                    quadProbeSuccSum += tracker;
                }
                quadraticHashFinalPrint += appendFindOrDeleteData(tracker, readerString);

            }
            linearHashFinalPrint += appendFindOrDeleteAverageProbeLength(linProbeSuccCt, linProbeSuccSum, linProbeFailCt, linProbeFailSum);
            quadraticHashFinalPrint += appendFindOrDeleteAverageProbeLength(quadProbeSuccCt, quadProbeSuccSum, quadProbeFailCt, quadProbeFailSum);
            System.out.print(linearHashFinalPrint);
            System.out.print(quadraticHashFinalPrint);
            
            hashTables[0].displayTable();
            //System.out.println(hashTables[0].getStringCount());
            hashTables[1].displayTable();
            //System.out.println(hashTables[1].getStringCount());
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader != null){
                reader.close();
            }
        }
    }
//--------------------------------------------------------------
    public static void fileStringCountHashTableDeleteMethod(String filename, HashTable[] hashTables)
    {
        methodTitleGenerator("Delete Methods" , "=============================", "==================================");
        String content = null;
        File file = new File(filename); // For example, foo.txt
        Scanner reader = null;
        String readerString;
        int heapSize =0;
        DataItem hashItem = null;
        int tracker;
        String linearHashFinalPrint = appendFindOrDeleteData("Linear HashTable Delete:");
        String quadraticHashFinalPrint = appendFindOrDeleteData("Quadratic HashTable Delete:");

        double linProbeFailCt = 0;
        double linProbeSuccCt = 0;
        double linProbeFailSum = 0;
        double linProbeSuccSum = 0;

        double quadProbeFailCt = 0;
        double quadProbeSuccCt = 0;
        double quadProbeFailSum = 0;
        double quadProbeSuccSum = 0;

        try {
            reader = new Scanner(file);
            
            while(reader.hasNext()){
                readerString = reader.next();
                tracker = hashTables[0].delete(readerString);
                if(tracker < 0){
                    linProbeFailCt++;
                    linProbeFailSum += tracker*(-1);
                }else{
                    linProbeSuccCt++;
                    linProbeSuccSum += tracker;
                }
                linearHashFinalPrint += appendFindOrDeleteData(tracker, readerString);
                
                tracker = hashTables[1].delete(readerString);
                if(tracker < 0){
                    quadProbeFailCt++;
                    quadProbeFailSum += tracker*(-1);
                }else{
                    quadProbeSuccCt++;
                    quadProbeSuccSum += tracker;
                }
                quadraticHashFinalPrint += appendFindOrDeleteData(tracker, readerString);

            }
            linearHashFinalPrint += appendFindOrDeleteAverageProbeLength(linProbeSuccCt, linProbeSuccSum, linProbeFailCt, linProbeFailSum);
            quadraticHashFinalPrint += appendFindOrDeleteAverageProbeLength(quadProbeSuccCt, quadProbeSuccSum, quadProbeFailCt, quadProbeFailSum);
            System.out.print(linearHashFinalPrint);
            System.out.print(quadraticHashFinalPrint);
            
            hashTables[0].displayTable();
            //System.out.println(hashTables[0].getStringCount());
            hashTables[1].displayTable();
            //System.out.println(hashTables[1].getStringCount());
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader != null){
                reader.close();
            }
        }
    }
//--------------------------------------------------------------
   public static String appendInsertData(String tableTitle){
      return "" +tableTitle + "\nTable:\tIndex\t\tString Id\t\t\t\t\t\t\t\t\t\t\t\tProbe\n";
   }  
    
//--------------------------------------------------------------
   public static String appendInsertData(int[] insertData, String key){
      StringBuffer sbSpace = new StringBuffer();
      for (int i = 0; i <= 43-key.length(); i++) {
      sbSpace.append(" ");
      }
      return "\t\t\t" + insertData[1] + "\t\t\t" + key + sbSpace.toString() + "\t" + insertData[0] +"\n";   
   }  
    
//--------------------------------------------------------------
   public static String appendAverageProbeLength(double count, double sum){
      NumberFormat nf = new DecimalFormat("###.00");
      String average = "" + nf.format((sum/count));
      return "Average:\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + average+"\n\n";
   }  
    
//--------------------------------------------------------------
    public static String appendFindOrDeleteAverageProbeLength(double succCount, double succSum, double failCount, double failSum){
        NumberFormat nf = new DecimalFormat("###.00");
        String succAverage = "" + nf.format((succSum/succCount));
        String failAverage = "" + nf.format((failSum/failCount));
        return "Average:\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + succAverage + "\t\t\t"+ failAverage +"\n\n";
    }  
  
//--------------------------------------------------------------
    public static String appendFindOrDeleteData(String tableTitle){
        return "" +tableTitle + "\nTable:\tString Id\t\t\t\t\t\t\t\t\t\t\t\tSuccess\tFailure\tProbe:Success\tProbe:Fail\n";
    }  
  
//--------------------------------------------------------------
    public static void methodTitleGenerator(String methodTitle, String surroundingBorder, String methodBorder){
        System.out.println("====================================================================" + surroundingBorder);
        System.out.println("=================  " + methodTitle + "  ============================" + methodBorder);
        System.out.println("====================================================================" + surroundingBorder);
    }  
  
//--------------------------------------------------------------
    public static String appendFindOrDeleteData(int insertData, String key){
        boolean successfulFoD = true;
        String successOrFailString = " Success\t\t\t\t";
        String probeString = "" + insertData;

        StringBuffer sbSpace = new StringBuffer();
        for (int i = 0; i <= 43-key.length(); i++) {
            sbSpace.append(" ");
        }
        if(insertData<0){
            insertData *= -1;
            successfulFoD = false;
            probeString = "\t\t\t\t\t"+insertData;
        }

        if(!successfulFoD){
            successOrFailString = "\t\t\t\tFail\t";
        }



        //return "\t\t\t" + insertData[1] + "\t\t\t" + key + sbSpace.toString() + "\t" + insertData[0] +"\n"; 
        return "\t\t\t" + key + sbSpace.toString() + successOrFailString + "\t\t" + probeString + "\n";
    }  

//--------------------------------------------------------------
    public static int findLargerPrime(int num)
    {
        boolean prime = false;
        num = num *2;
        while(!prime){
            if(num ==2){ return 2;}
            num+=1;
            prime = true;
            for(int i=2; i<num/2; i++){
                if(num % i == 0){
                    prime = false;
                }
            }
        }
        return num;
    }
   }  // end class HashTableApp
////////////////////////////////////////////////////////////////
