package main;
import java.io.*;
import java.util.*;
import java.util.regex.*;
//good
//difference
//difference2
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
//modification
class MyComparator implements Comparator<File>{

	@Override
	public int compare(File o1, File o2) {
		// TODO Auto-generated method stub
		String name1 = o1.getName();
		String name2 = o2.getName();
		int name_int_1 = Integer.parseInt(name1.substring(1,name1.length()-5));
		int name_int_2 = Integer.parseInt(name2.substring(1,name2.length()-5));
		if(name_int_1<name_int_2){
			return -1;
		}
		else if(name_int_1 == name_int_2){
			return 0;
		}
		else{
			return 1;
		}
	}
	
}

public class ClimbBaidu {
	private static String baidu_url="http://www.baidu.com/s?wd=";
	private static final double CHEAT_CHECK_VALUE = 0.8;
	private static List<String> check_result;
	private static List<String> statistics_result;
	private static int all_line_size = 0;
	private static int all_cheating_line_size = 0;
	
	private static String FOLDER_PATH = "C:\\MyFiles\\Working\\Unity\\SixFloor\\TechinicalReview_1\\Unity_Standard_查重\\TXT"; 
	private static String STATISTIC_RESULT_PATH = "C:\\MyFiles\\Working\\Unity\\SixFloor\\TechinicalReview_1\\Unity_Standard_查重\\_RESULT\\statistics_result.txt";
	private static String CHECK_RESULT_PATH = "C:\\MyFiles\\Working\\Unity\\SixFloor\\TechinicalReview_1\\Unity_Standard_查重\\_RESULT\\check_result.txt";
	public static void main(String[] args)throws Exception{
		String file_path = FOLDER_PATH;
		
		File directory = new File(file_path);
		
		File[] files = directory.listFiles();
		
		//sort files by file name
		Arrays.sort(files, new MyComparator());
		
		//check
		for(int i =0;i!=files.length;i++){
			System.out.println(files[i].getName());
		}
		
		//init check_result list
		check_result = new ArrayList<String>();
		statistics_result = new ArrayList<String>();
		
		for(int i =0;i!=files.length;i++){
			//check is_directory
			if(files[i].isDirectory()){
				continue;
			}
			//check is txt
			if(!(files[i].getName().endsWith(".txt"))){
				continue;
			}
			
			//check current txt file
			outputChecking(files[i]);
		}
		
		//calculate all percentage
		double whole_percentage = (double)all_cheating_line_size/all_line_size;
		//add to check result
		check_result.add("whole precentage : "+whole_percentage);
		statistics_result.add("whole precentage : "+whole_percentage);
		//output check result list
		outputList();
		
	}
	
	public static void outputList() throws Exception{
		String file_path = CHECK_RESULT_PATH;
		PrintWriter printWriter = new PrintWriter(file_path,"utf-8");
		for(int i =0;i!=check_result.size();i++){
			String current_line = check_result.get(i);
			printWriter.println(current_line);
		}
		printWriter.close();
		file_path = STATISTIC_RESULT_PATH;
		printWriter = new PrintWriter(file_path,"utf-8");
		for(int i =0;i!=statistics_result.size();i++){
			String current_line = statistics_result.get(i);
			printWriter.println(current_line);
		}
		printWriter.close();
	}
	
	//output_checking
	public static void outputChecking(File file) throws Exception{
		InputStream inputStream = new FileInputStream(file);
		Scanner scanner = new Scanner(inputStream,"utf-8");
		String line = null;
		
		//get file to String list
		List<String> current_line_list = new ArrayList<String>();
		while(scanner.hasNextLine()){
			line = scanner.nextLine();
			
			//getting rid of all blanks
			line = line.replaceAll("\\s", "");
			//getting rid of 涓枃鍏ㄨ绌烘牸
			line = line.replaceAll("[\u3000]", "");
			line = line.toLowerCase();
			//if current line is code and not comment of the code
			if(!isContainChinese(line)){
				continue;
			}
			//if current line is less than 20 words
			if(line.length()<=20){
				continue;
			}
			current_line_list.add(line);
		}
		
		//count cheat line 
		int cheat_line_count = 0;
		for(int i =0;i!=current_line_list.size();i++){
			
			if((i+1)%10==0){
				//sleep 3s per 10 subjects
				Thread.sleep(1000);
			}
			else{
				//sleep 1s per subject
				Thread.sleep(1000);
			}
			
			String current_line = current_line_list.get(i);
			if(checkCheating(current_line, (i+1))){
				cheat_line_count++;
			}
		}
		
		//out_put
		check_result.add("check file name : "+file.getName());
		statistics_result.add("check file name : "+file.getName());
		System.out.println("check file name : "+file.getName());
		
		//output statistic values
		int whole_line_count = current_line_list.size();
		all_line_size += whole_line_count;
		all_cheating_line_size += cheat_line_count;
		
		check_result.add("cheating percentage : "+((double)cheat_line_count/whole_line_count));
		statistics_result.add("cheating percentage : "+((double)cheat_line_count/whole_line_count));
		System.out.println("cheating percentage : "+((double)cheat_line_count/whole_line_count));
		
		//output list for recording
		outputList();
		
		//close scanner
		scanner.close();
	}
	
	public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
	public static boolean checkCheating(String current_line,int line_number){
		try{
			boolean is_cheating = false;
			//out_put
			System.out.println("["+line_number+"]");
			//add into check result
			check_result.add("["+line_number+"]");
			System.out.println(current_line);
			check_result.add(current_line);
			//get current line as question combining with baidu url
			String question = current_line;
			
			//get length of the question
			int all_length = question.length();
			
			//set the url to get html file
			String url = baidu_url + question;
			
			//get html file directly
			Document document = Jsoup.connect(url).get();
			
			//extract the first result of Baidu searching
			Element div_1 = document.getElementById("1");
			
			//check if div_1 is null, means nothing searched
			if(div_1==null){
				//if it is, stands for no result, which means not cheating
				is_cheating = false;
				System.out.println("NOT_CHEATING");
				check_result.add("NOT_CHEATING");
				return is_cheating;
			}
			
			//if div_1 is not null stands for at least one result, we should check the percentage
			
			//get children of div_1
			Elements children = div_1.children();

			//use children to get children text
			for(int i =0;i!=children.size();i++){
				Element current_child = children.get(i);
				String current_child_text = current_child.text();
				String question_string = question;
				//question_string and current_child_text lcs
				String lcs_cons = LCS_DP(current_child_text, question_string);
				//<debug_output>
//				System.out.println("text() : "+current_child_text);
//				System.out.println("lcs_cons : "+lcs_cons);
				//</debug_output>
				int common_part_length = lcs_cons.length();
				double percentage = (double)common_part_length/all_length;
				if(percentage>CHEAT_CHECK_VALUE){
					System.out.println(lcs_cons);
					check_result.add(lcs_cons);
					
					//get url
					Elements a_tags = div_1.getElementsByTag("a");
					if(a_tags != null&&a_tags.size()!=0){
						for(int j = 0;j!=a_tags.size();j++){
							Element current_a = a_tags.get(j);
							String current_a_url = current_a.attr("href");
							System.out.println(current_a_url);
							check_result.add(current_a_url);
							break;
						}
					}
					
					is_cheating = true;
					System.out.println("cheating");
					check_result.add("cheating");
					return is_cheating;
				}
			}
			is_cheating = false;
			System.out.println("NOT_CHEATING");
			check_result.add("NOT_CHEATING");
			return is_cheating;
		}
		catch(Exception e){
			e.printStackTrace();
			//output current line number and content
			System.out.println("Error occured at : "+line_number);
			System.out.println("Line is:"+current_line);
		}
		//if error occured, we should not put it into cheating count
		return false;
	}
	
	
	//use dp O(m*n) to implement LCS algorithm, return lcs part
	private static String LCS_DP(String a,String b){
		int a_length = a.length();
		int b_length = b.length();
		int[][] p = new int[b_length+1][a_length+1];
		for(int i =0;i!= b_length+1;i++){
			for(int j =0;j!=a_length+1;j++){
				p[i][j] = 0;
			}
		}
		/*check use*/
		/*
		for(int i =0;i!= b_length+1;i++){
			for(int j =0;j!=a_length+1;j++){
				System.out.print(p[i][j]);
			}
			System.out.println();
		}
		*/
		
		for(int i =1;i!=b_length+1;i++){
			for(int j =1;j!=a_length+1;j++){
				if(a.charAt(j-1)==b.charAt(i-1)){
					p[i][j] = p[i-1][j-1]+1;
				}
				else if(p[i-1][j]>=p[i][j-1]){
					p[i][j] = p[i-1][j];
				}
				else{
					p[i][j] = p[i][j-1];
				}
			}
		}
		StringBuffer cons = new StringBuffer("");
		int m = b_length;
	    int n = a_length;
	    while(m!=0&&n!=0){
	        if(a.charAt(n-1)==b.charAt(m-1)){
	            cons.append(a.charAt(n-1));
	            n--;
	            m--;
	        }
	        else if(p[m-1][n]>=p[m][n-1]){
	            m--;
	        }
	        else{
	            n--;
	        }
	    }
	    String cons_string = cons.reverse().toString();
	    
	    return cons_string;
	}
}
