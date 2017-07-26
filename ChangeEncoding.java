package main;

import java.io.*;
import java.util.*;

public class ChangeEncoding {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String  pathname = "C:\\MyFiles\\Working\\Unity\\SixFloor\\TechinicalReview_1\\Unity_Standard_≤È÷ÿ\\TXT";
		File folder = new File(pathname);
		File[] files = folder.listFiles();
		for(int i =0;i!=files.length;i++){
			File current_file = files[i];
			readFile(current_file);
			outputFile(current_file);
		}
	}
	
	private static List<String> current_file_list = new ArrayList<String>();
	
	public static void readFile(File file){
		current_file_list.clear();
		InputStream inputStream = null;
		Scanner scanner = null;
		try{
			inputStream = new FileInputStream(file);
			//default charset
			scanner = new Scanner(inputStream);
			while(scanner.hasNextLine()){
				String line = scanner.nextLine();
				current_file_list.add(line);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally {
			try{
				if(scanner != null){
					scanner.close();
				}
				if(inputStream != null){
					inputStream.close();
				}
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	public static void outputFile(File location){
		PrintWriter printWriter = null;
		try{
			printWriter = new PrintWriter(location,"utf-8");
			for(int i =0;i!=current_file_list.size()-1;i++){
				String current_line = current_file_list.get(i);
				printWriter.println(current_line);
			}
			printWriter.print(current_file_list.get(current_file_list.size()-1));
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally {
			try{
				if(printWriter !=null){
					printWriter.close();
				}
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

}
