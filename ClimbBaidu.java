package org.main;
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
	private static int all_line_size = 0;
	private static int all_cheating_line_size = 0;
	
	public static void main(String[] args)throws Exception{
		String file_path = "E:\\MyFiles\\Working\\Unity\\TechinicalReview_1\\Unity_标准教程5月25日\\Unity 5.X标准教程5月25日\\Unity 5.X标准教程5月25日\\TXT_查重\\查重使用文件";
		
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
		//output check result list
		outputList();
		
	}
	
	public static void outputList() throws Exception{
		String file_path = "E:\\MyFiles\\Working\\Unity\\TechinicalReview_1\\Unity_标准教程5月25日\\Unity 5.X标准教程5月25日\\Unity 5.X标准教程5月25日\\TXT_查重\\统计结果\\cons.txt";
		PrintWriter printWriter = new PrintWriter(file_path,"utf-8");
		for(int i =0;i!=check_result.size();i++){
			String current_line = check_result.get(i);
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
			//getting rid of 中文全角空格
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
				Thread.sleep(3000);
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
		System.out.println("check file name : "+file.getName());
		
		//output statistic values
		int whole_line_count = current_line_list.size();
		all_line_size += whole_line_count;
		all_cheating_line_size += cheat_line_count;
		
		check_result.add("cheating percentage : "+((double)cheat_line_count/whole_line_count));
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
	
	//baidu cheat checker
	public static boolean checkCheating(String current_line,int line_number){
		try{
			boolean is_cheating = false;
			//out_put
			System.out.println("["+line_number+"]");
			System.out.println(current_line);
			
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
			
			//check if div_1 is null
			if(div_1==null){
				//if it is, stands for no result, which means not cheating
				is_cheating = false;
				System.out.println("NOT_CHEATING");
				return is_cheating;
			}
			
			//if div_1 is not null stands for at least one result, we should check the percentage
			
			//get children of div_1
			Elements children = div_1.children();
			
			//extract h3 which is the first child of div_1
			Element h3 = children.get(0);
			
			//get h3's first child which is a <a></a> tag
			Element h3_a = h3.child(0);
			
			//get all <em></em> tag from tag <a></a>
			Elements h3_a_em = h3_a.children();
			
			//count all em length, which <em></em> stands for the common part of question and the searching result
			int all_em_length = 0;
			for(int i =0;i!=h3_a_em.size();i++){
				//get current_em_text
				String current_em_text = h3_a_em.get(i).text();
				
				//deal with current_em_text
				//replace all blanks
				current_em_text = current_em_text.replaceAll("\\s", "");
				//get to lower case
				current_em_text = current_em_text.toLowerCase();
				
				all_em_length+=current_em_text.length();
			}
			
			//title percentage
			double h3_percentage = (double)all_em_length/all_length;
			
			//if more than 80%, then seen as cheating from internet
			if(h3_percentage>CHEAT_CHECK_VALUE){
				is_cheating = true;
				//out_put
				System.out.println("cheating");
				return is_cheating;
			}
			
			//if title not fit for cheating, search for the searching result's content
			Elements div_content_ems = null;
			for(int i =1;i < children.size();i++){
				
				//get one child from children, but not h3
				Element current_no_h3 = children.get(i);
				
				//get div_content_ems which stands for <em></em>s in div_content
				div_content_ems = current_no_h3.select("em");
				//check whether the em list is size 0
				if(div_content_ems!=null&&div_content_ems.size()!=0){
					break;
				}
			}
			//check div_content_ems is still null
			if(div_content_ems == null){
				//if it is still null,which stands for no ems
				is_cheating = false;
				System.out.println("NOT_CHEATING");
				return is_cheating;
			}
			
			/*
			//get div_content by query
			Elements div_contents = children.select("div[class=c-abstract]");
			
			//get div_content
			Element div_content = div_contents.first();
			
			//check if div_content is null
			if(div_content == null){
				//if it is means may be english
				div_contents = children.select("div[class=c-abstract c-abstract-en]");
				div_content = div_contents.first();
				//check if div_content is null
				if(div_content == null){
					//if still null
					is_cheating = false;
					System.out.println("NOT_CHEATING");
					return is_cheating;
				}
			}*/
			

			
			//we can use another integer or just reset it to 0, count all <em></em> length
			//for output debug
			StringBuffer output_debug_string = new StringBuffer("");
			all_em_length = 0;
			for(int i =0;i!=div_content_ems.size();i++){
				String current_em_text = div_content_ems.get(i).text();
				
				//deal with current_em_text
				//replace all blanks
				current_em_text = current_em_text.replaceAll("\\s", "");
				//get to lower case
				current_em_text = current_em_text.toLowerCase();
				//append current
				output_debug_string.append(current_em_text+" ");
				//add to length
				all_em_length+=current_em_text.length();
			}
			//output for debugging
			System.out.println(output_debug_string.toString());
			
			double content_percentage = (double)all_em_length/all_length;
			if(content_percentage>CHEAT_CHECK_VALUE){
				is_cheating = true;
				System.out.println("cheating");
				return is_cheating;
			}
			System.out.println("NOT_CHEATING");
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
}
