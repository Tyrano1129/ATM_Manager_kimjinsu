package ATM;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Util {
	private static Scanner sc = new Scanner(System.in);
	private static final String CUR_PATH = System.getProperty("user.dir") + "\\src\\" + new Util().getClass().getPackageName()+"\\";
	// account.txt ,  client.txt
	public static String getValueString(String msg) {
		System.out.println(msg);
		return sc.next();
	}
	public static int getValue(String msg,int start,int end) {
		int num = 0;
		while(true) {
			System.out.println(msg);
			try {
				num = sc.nextInt();
				if(num < start || num > end) {
					System.out.printf("[%d ~ %d] 사이 입력%n",start,end);
					continue;
				}
			}catch(Exception e) {
				System.out.println("숫자만 입력해주세요.");
			}finally {
				sc.nextLine();
			}
			return num;
		}
	}
	//파일 저장
	private static void save(String file,String data) {
		
		try(FileWriter fw = new FileWriter(CUR_PATH+file)) {
			fw.write(data);
			System.out.printf("%s 저장 완료%n",file);
		} catch (IOException e) {
			System.out.printf("%s 저장 실패%n",file);
		}
		
	}
	//파일 세이브 메서드
	public static void fileSave(ClientDAO cl, AccountDAO acc) {
		
		String cldata = cl.cldata();
		String accdata = acc.accdata();
		
		save("client.txt",cldata);
		save("account.txt",accdata);
		
	}
	//파일 불러오기
	private static String load(String fileName) {
		
		if(!fileNullcheck(CUR_PATH+fileName)) {
			return null;
		}
		String data = "";
		try(FileReader fr = new FileReader(CUR_PATH+fileName); BufferedReader br = new BufferedReader(fr);) {
			while(true) {
				String temp = br.readLine();
				if(temp == null)break;
				data += temp+"\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
		
	}
	//파일 찾기
	private static boolean fileNullcheck(String filename) {
		
		File file = new File(filename);
		if(!file.exists()) {
			System.out.printf("%s 데이터가 없습니다.%n",filename);
			return false;
		}
		return true;
		
	}
	//파일 불러오기 메서드
	public static void fileload(ClientDAO cl, AccountDAO acc) {
		
		String cldata = load("client.txt");
		String accdata = load("account.txt");
		if(cldata != null) {
			cl.initLoad(cldata);
			cl.maxNumberinit();
		}
		if(accdata != null) {
			acc.initLoad(accdata);
		}
		System.out.println("불러오기 완료...");
		
	}
	
}
