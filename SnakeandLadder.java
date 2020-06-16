import java.util.*;

class Element
{
 int val;
 String player1; // ' ' or player1 symbol
 String player2; //' ' or player1 symbol
 char snake;    // Ö (153)  or  ¦ (177) or ¿ (168)
 char ladder;   // - (203) or + (215) or - (202)


 Element(int v, String p1, String p2, char s, char l)
 {
  val = v;
  player1 = p1;
  player2 = p2;
  snake = s;
  ladder = l;
 }
}//Element 

class SnakeandLadder
{
 static int dice()
 {
  Random rnd = new Random();
  int val=0;

  val = rnd.nextInt(150)%6 + 1;
  if(val == 6)
  {
   val += rnd.nextInt(150)%6 + 1;
   if(val == 12)
   {
    val += rnd.nextInt(150)%6 + 1;
    if(val == 18)
     return 0;
   }
  }
  return val;
 }

 static void intializeBoard(Element e[]) throws Exception
 {
  int i;
  
  for(i=0; i<e.length; i++)
  {
   e[i].val = i+1;
   e[i].player1 = " ";
   e[i].player2 = " ";
   e[i].snake = ' ';
   e[i].ladder = ' ';
  }
 }//initializeBoard

 static void intializeSnakes(Element e[])
 {
  //8 snakes planned
  
  //Snake 1: 99,82,79,62,59
  e[98].snake = 153; // Ö (153)
  e[81].snake = 177; // ¦ (177)
  e[78].snake = 177; // ¦ (177)
  e[61].snake = 177; // ¦ (177)
  e[58].snake = 168; // ¿ (153)

 //Snake 2: 63,58,43,38,23,18 
  e[62].snake = 153; // Ö (153)
  e[57].snake = 177; // ¦ (177)
  e[42].snake = 177; // ¦ (177)
  e[37].snake = 177; // ¦ (177)
  e[22].snake = 177; // ¦ (177)
  e[17].snake = 168; // ¿ (168)

 //Snake 3: 37,24, 17, 4
  e[36].snake = 153; // Ö
  e[23].snake = 177; // ¦
  e[16].snake = 177; // ¦
  e[3].snake = 168;  // ¿

 //Snake 4: 96,85,76,65,56,45
  e[95].snake = 153; // Ö
  e[84].snake = 177; // ¦
  e[75].snake = 177; // ¦
  e[64].snake = 177; // ¦
  e[55].snake = 177; // ¦
  e[44].snake = 168; // ¿

 //Snake 5: 75,66,55,46,35,26,15,6
  e[74].snake = 153; 
  e[65].snake = 177;
  e[54].snake = 177;
  e[45].snake = 177;
  e[34].snake = 177;
  e[25].snake = 177;
  e[14].snake = 177;
  e[5].snake  = 168;

 //Snake 6: 28,13,8
  e[27].snake = 153;
  e[12].snake = 177;
  e[7].snake = 168;

 //Snake 7: 89,72,69,52,49
  e[88].snake = 153;
  e[71].snake = 177;
  e[68].snake = 177;
  e[51].snake = 177;
  e[48].snake = 168;

 //Snake 8: 51,50,31,30,11
  e[50].snake = 153;
  e[49].snake = 177;
  e[30].snake = 177;
  e[29].snake = 177;
  e[10].snake = 168; 
 }//intializeSnakes

 static void intializeLadders(Element e[])
 {
  //8 ladders planned
  
  //Ladder 1: 80,61,60,41,40
   e[79].ladder = 203; // - (203)
   e[60].ladder = 215; // + (215)
   e[59].ladder = 215; // + (215)
   e[40].ladder = 215; // + (215)
   e[39].ladder = 202; // - (202)
 
  //Ladder 2: 98,83,78,63,58
   e[97].ladder = 203; // -
   e[82].ladder = 215; // +
   e[77].ladder = 215; // +
   e[62].ladder = 215; // +
   e[57].ladder = 202; // -
  
  //Ladder 3: 43,38,23,18
   e[42].ladder = 203; // -
   e[37].ladder = 215; // +
   e[22].ladder = 215; // +
   e[17].ladder = 202; // -

  //Ladder 4: 25,16,5
   e[24].ladder = 203;
   e[15].ladder = 215;
   e[4].ladder = 202;

  //Ladder 5: 74,67,54,47
   e[73].ladder = 203;
   e[66].ladder = 215;
   e[53].ladder = 215;
   e[46].ladder = 202;

  //Ladder 6: 33,28,13
   e[32].ladder = 203;
   e[27].ladder = 215;
   e[12].ladder = 202;

  //Ladder 7: 92,89,72
   e[91].ladder = 203;
   e[88].ladder = 215;
   e[71].ladder = 202;

  //Ladder 8: 69,52,49,32
   e[68].ladder = 203;
   e[51].ladder = 215;
   e[48].ladder = 215;
   e[31].ladder = 202;  
 }
 
 static void printElement(Element e)
 {
  System.out.print("   " +e.val); 
  System.out.print(e.player1);
  System.out.print(e.player2);
  System.out.print(e.snake);
  System.out.print(e.ladder);
 }

 static String getString() throws Exception
 {
  int n;
  
  System.in.skip(System.in.available());

  byte arr[] = new byte[10];
  n = System.in.read(arr);
  
  return new String(arr, 0, n-2);
 }//getString

 static void drawBoard(Element e[]) 
 {
  int i, j;
  int x, y;
  
  x = 100;
  y = x-10;

  for(i=1; i<11; i++)
  {
   System.out.println();
   if(i%2 == 1)
   {
    //print rev : 100 to 91
    for(j=x; j>y; j--)
     printElement(e[j-1]);

    x = x-19;
    y = x + 10;
   }
   else
   {
    //print forward: 81 to 90
    for(j=x; j<y; j++)
     printElement(e[j-1]);
 
    x = x-1;
    y = x-10;
   }
  }
 }

 static int advanceByLadder(int pos)
 {
  switch(pos)
  {
   case 40: System.out.println("Got Ladder at 40, player advances to 80!");
 	    return 80;
   case 58: System.out.println("Got Ladder at 58, player advances to 98!");
  	    return 98;
   case 18: System.out.println("Got Ladder at 18, player advances to 43!");
 	    return 43;
   case 5 : System.out.println("Got Ladder at 5, player advances to 25!");
  	    return 25;
   case 47: System.out.println("Got Ladder at 47, player advances to 74!");
	    return 74;
   case 13: System.out.println("Got Ladder at 13, player advances to 33!");
 	    return 33;
   case 72: System.out.println("Got Ladder at 72, player advances to 92!");
	    return 92;
   case 32: System.out.println("Got Ladder at 32, player advances to 69!");
	    return 69;
   default : return pos;
   }
 }

 static int bitBySnake(int pos)
 {
  switch(pos)
  {
   case 99: System.out.println(" Bit By Snake at 99, player drops to 59! ");
            return 59;
   case 63: System.out.println(" Bit By Snake at 63, player drops to 18! ");  
            return 18;
   case 37: System.out.println(" Bit By Snake at 37, player drops to 4! ");  
            return 4;
   case 96: System.out.println(" Bit By Snake at 96, player drops to 45! ");  
            return 45;
   case 75: System.out.println(" Bit By Snake at 75, player drops to 6! ");  
            return 6;
   case 28: System.out.println(" Bit By Snake at 28, player drops to 8! ");
  	    return 8;
   case 89: System.out.println(" Bit by Snake at 89, player drops to 49!");
            return 49;
   case 51: System.out.println(" Bit by Snake at 51, player drops to 11!");
  	    return 11;
   default: return pos;
  }
 }

 static void play() throws Exception
 {
  try
  {
   Element board[] = new Element[100];
   int i;
   String p1 = " ", p2= " ", dummy;
   int flag, end = 0, current;
   int pos1, pos2;

   //intializeBoard
   for(i=0; i<board.length; i++)
    board[i] = new Element(i+1, " ", " ", ' ', ' ');
   intializeSnakes(board);
   intializeLadders(board);
 
   flag = 0;
   System.out.println("Enter Player1 symbol : ");
   p1 = getString();

   do
   {
    System.out.println("Enter Player2 symbol : ");
    p2 = getString();

    if(p1 == p2)
    {
     System.out.println("Player1 has already taken the symbol! ");
     System.out.println("Enter another symbol");
     flag = 1;
    }
    else
     flag = 0;
   }while(flag == 1);
  
  
 
  flag = 1;
  pos1 = pos2 = 1;
  do
  {
   drawBoard(board);
  
   if(flag == 1)
   {
    System.out.println("\nPlayer1, Enter any alphabet to throw dice : ");
    dummy = getString();
    current = dice();
    System.out.println("Dice Value : " + current);
    if( (pos1 + current) > 100)
    {
     System.out.println("Dice Void, as you need " + (100-pos1));
    }
    else
    {
     //erase the old position
     board[pos1-1].player1 = " ";
     pos1 += current;
     pos1 = advanceByLadder(pos1);
     pos1 = bitBySnake(pos1);
     System.out.println("Position : " + pos1);
     //update board
     board[pos1-1].player1 = p1;
    }
   flag++;
   } 
   else if(flag == 2)
   {
    System.out.println("\n Player2, Enter any alphabet to throw dice : ");
    dummy = getString();
    current = dice();
    System.out.println("Dice Value : " + current);
    if( (pos2 + current) > 100)
    {
     System.out.println("Dice Void, as you need " + (100-pos2));
    }
    else
    {
     //erase the old position
     board[pos2-1].player2 = " ";
     pos2 += current;
     pos2 = advanceByLadder(pos2);
     pos2 = bitBySnake(pos2);
     System.out.println("Position : " + pos2);
     //update the board
     board[pos2-1].player2 = p2;
    }
   flag--;
   } 

   if(pos1 == 100)
   {
    drawBoard(board);
    System.out.println("Player 1 " + p1+"wins!");
    end = 1; 
   }
   
   if(pos2 == 100)
   {
    drawBoard(board);
    System.out.println("Player 2" + p2 + " wins!");
    end = 1; 
   }
  }while(end == 0);
 }
  catch(Exception ex)
  {
   System.out.println(ex);
  }
 }//play

 public static void main(String args[])
 {
  try
  {
   play();
  }
  catch(Exception ex)
  {
   System.out.println(ex);
  }
 }
}//Snake&Ladder

