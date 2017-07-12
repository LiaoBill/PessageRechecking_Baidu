package org.main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class ClimbBaidu {
	private static String baidu_url="http://www.baidu.com/s?wd=";
	public static void main(String[] args)throws Exception{
		String file_path = "E:\\MyFiles\\Working\\Unity\\TechinicalReview_1\\Unity_标准教程5月25日\\Unity 5.X标准教程5月25日\\Unity 5.X标准教程5月25日\\TXT";
		String name = "第11章.txt";
		file_path+="\\"+name;
		File file = new File(file_path);
		InputStream inputStream = new FileInputStream(file);
		Scanner scanner = new Scanner(inputStream,"utf-8");
		String line = null;
		List<String> current_line_list = new ArrayList<String>();
		while(scanner.hasNextLine()){
			line = scanner.nextLine();
			line.replaceAll("\\s", "");
			//如果字符数高于20才进行查重分析
			if(line.length()>20){
				current_line_list.add(line);
			}
		}
		System.out.println("check_start : "+name);
		System.out.println("line_count : "+current_line_list.size());
		int cheat_line_count = 0;
		for(int i =0;i!=current_line_list.size();i++){
			String current_line = current_line_list.get(i);
			if(checkCheating(current_line, i)){
				cheat_line_count++;
			}
		}
		System.out.println("line_count : "+current_line_list.size());
		System.out.println("cheat_line_count = "+cheat_line_count);
		System.out.println("percentage : "+((double)cheat_line_count/current_line_list.size()));
	}
	//进行基于行的查重
	public static boolean checkCheating(String current_line,int line_number){
		try{
			System.out.println("["+line_number+"]");
			System.out.println(current_line);
			String question = current_line;
			int all_length = question.length();
			String url = baidu_url + question;
			Document document = Jsoup.connect(url).get();
			//要搜索结果的第一个
			Element div_1 = document.getElementById("1");
			//System.out.println(div_1.html());
			Elements children = div_1.children();
			Element h3 = children.get(0);
			Element h3_a = h3.child(0);
			//String h3_a_text = h3_a.text().replaceAll("[ \t]", "").toLowerCase();
			//需要去除一些特定的字符串,例如"_百度知道"
			//System.out.println(h3_a_text);
			Elements h3_a_em = h3_a.children();
			//System.out.println("{input length} = "+all_length);
			//System.out.println("headline_check");
			int all_em_length = 0;
			for(int i =0;i!=h3_a_em.size();i++){
				String current_em_text = h3_a_em.get(i).text().replaceAll("[ \t]", "").toLowerCase();
				//System.out.print(current_em_text+"+");
				all_em_length+=current_em_text.length();
			}
			//System.out.println();
			//System.out.println(all_em_length);
			
			double h3_percentage = (double)all_em_length/all_length;
			boolean is_cheating = false;
			if(h3_percentage>0.8){
				is_cheating = true;
				System.out.println("cheating");
				return is_cheating;
			}
			//System.out.println("content_check");
			Elements div_contents = children.select("div[class=c-abstract]");
			Element div_content = div_contents.first();
			Elements div_content_ems = div_content.select("em");
			all_em_length = 0; 
			for(int i =0;i!=div_content_ems.size();i++){
				String current_em_text = div_content_ems.get(i).text().replaceAll("[ \t]", "").toLowerCase();
				//System.out.println(current_em_text+"+");
				all_em_length+=current_em_text.length();
			}
			//System.out.println();
			//System.out.println(all_em_length);
			
			double content_percentage = (double)all_em_length/all_length;
			if(content_percentage>0.8){
				is_cheating = true;
			}
			if(is_cheating){
				System.out.println("cheating");
			}
			else{
				System.out.println("not_cheating");
			}
			return is_cheating;
		}
		catch(Exception e){
			e.printStackTrace();
			//如果百度什么也没查到或者百度因为请求过多而拒绝了请求的话,将会return false
			System.out.println("Error occured at : "+line_number);
			System.out.println("Line is : "+current_line);
		}
		return false;
	}
}
