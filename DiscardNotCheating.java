package main;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DiscardNotCheating {

	public static void main(String[] args)throws Exception{
		// TODO Auto-generated method stub
		List<String> cons = new ArrayList<String>();
		File file = new File("C:\\MyFiles\\Working\\Unity\\SixFloor\\TechinicalReview_1\\Unity_Standard_≤È÷ÿ\\_RESULT\\check_result.txt");
		Scanner scanner = new Scanner(file,"utf-8");
		while(scanner.hasNextLine()){
			String line = scanner.nextLine();
			if(line.equals("NOT_CHEATING")){
				cons.remove(cons.size()-1);
				cons.remove(cons.size()-1);
				continue;
			}
			cons.add(line);
		}
		scanner.close();
		PrintWriter printWriter = new PrintWriter(file,"utf-8");
		for(int i =0;i!=cons.size();i++){
			String current = cons.get(i);
			printWriter.println(current);
		}
		printWriter.close();
	}

}
