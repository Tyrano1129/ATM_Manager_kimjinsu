package ATM;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientDAO {
	private Client[] clList;
	private int number;
	//프린트
	public void print() {
		for(Client c : clList) {
			System.out.println(c);
		}
	}
	//초기값
	public void init(Client c) {
		int cnt = clList == null? 0 : clList.length;
		if(cnt != 0) {
			Client[] temp = clList;
			clList = new Client[cnt+1];
			for(int i = 0; i < temp.length; i+=1) {
				clList[i] = temp[i];
			}
		}else {
			clList = new Client[1];
		}
		
		clList[cnt] = c;
	}
	public void maxNumberinit() {
		int max =0;
		for(Client c : clList) {
			if(max < c.clientNo) {
				max = c.clientNo;
			}
		}
		number = max+1;
	}
	//아이디 인덱스
	private int checkIndexID(String id) {
		if(clList == null) return -1;
		for(int i = 0; i < clList.length; i+=1){
			if(clList[i].id.equals(id)) {
				return i;
			}
		}
		return -1;
	}
	//아이디 pw 와 같은 인덱스
	private int checkuserIdPwIdx(String id,String pw) {
		for(int i = 0; i < clList.length; i+=1) {
			if(clList[i].id.equals(id) && clList[i].pw.equals(pw)) {
				return i;
			}
		}
		return -1;
	}
	//수정
	public void userRepair() {
		System.out.println("[회원 수정]");
		String id = Util.getValueString("id 입력 : ");
		int idx = checkIndexID(id);
		if(idx == -1) {
			System.out.println("입력한 회원은 없습니다.");
			return;
		}
		userRepWord(idx);
		print();
	}
	//수정할 값 입력
	private void userRepWord(int idx) {
		System.out.println("[1]이름 수정");
		System.out.println("[2]비밀번호 수정");
		int sel = Util.getValue("메뉴 입력 : ",1,2);
		if(sel == 1) {
			String name = Util.getValueString("이름 수정입력 : ");
			if(clList[idx].name.equals(name)) {
				System.out.println("이미 같은 이름으로 되어있습니다.");
				return;
			}
			clList[idx].name = name;
		}else if(sel == 2) {
			String pw = Util.getValueString("비밀번호 수정입력 : ");
			if(clList[idx].pw.equals(pw)) {
				System.out.println("이미 같은 비밀번호로 되어있습니다.");
				return;
			}
			clList[idx].pw = pw;
		}
	}
	//삭제할 유저 입력
	public void userDelete(AccountDAO acc) {
		System.out.println("[회원 삭제]");
		String id = Util.getValueString("id 입력 : ");
		String pw = Util.getValueString("비밀번호 입력 : ");
		int idx = checkuserIdPwIdx(id,pw);
		if(idx == -1) {
			System.out.println("아이디와 비밀번호가 틀렷습니다.");
			return;
		}
		acc.userOneAcountDelete(clList[idx]);
		delete(idx);
	}
	//삭제 
	private void delete(int delidx) {
		if(clList.length == 1) {
			clList = null;
		}else {
			Client[] temp = clList;
			clList = new Client[temp.length-1];
			int idx = 0;
			for(int i = 0; i < temp.length; i+=1) {
				if(i != delidx) {
					clList[idx++] = temp[i];
				}
			}
		}
	}// 패턴 아이디
	private boolean idpattern(String id) {
		String idPatter = "^[a-z]{1}[a-z0-9]{5,10}$";
		Pattern p = Pattern.compile(idPatter);
		Matcher m = p.matcher(id);
		if(m.matches()) {
			System.out.println("id 맞는 표현입니다.");
			return true;
		}
		System.out.println("id 틀린표현 표현입니다.");
		return false;
	}
	// 패턴 비밀번호
	private boolean pwpattern(String pw) {
		String pwPatter = "^[a-z0-9]{4,10}$";
		Pattern p = Pattern.compile(pwPatter);
		Matcher m = p.matcher(pw);
		if(m.matches()) {
			System.out.println("pw 맞는 표현입니다.");
			return true;
		}
		System.out.println("pw 틀린표현 표현입니다.");
		return false;
	}
	//추가
	//추가할 유저 이름 아이디 비밀번호
	public void userJoin() {
		System.out.println("[회원 가입]");
		String id = Util.getValueString("id 입력 : ");
		int idx = checkIndexID(id);
		if(idx != -1) {
			System.out.println("id가 중복되었습니다.");
			return;
		}
		if(!idpattern(id)) {
			return;
		}
		String pw = Util.getValueString("비밀번호 입력 : ");
		if(!pwpattern(pw)) {
			return;
		}
		String name = Util.getValueString("이름 입력 : ");
		Client c = new Client(number,id,pw,name);
		init(c);
	}
	//로그인
	public Client userLogin() {
		if(clList == null) {
			System.out.println("데이터가없습니다. 가입후에 이용해주세요");
			return null;
		}
		System.out.println("[로그인]");
		String id = Util.getValueString("id 입력 : ");
		String pw = Util.getValueString("비밀번호 입력 : ");
		int idx = checkuserIdPwIdx(id,pw);
		if(idx == -1) {
			System.out.println("아이디와 비밀번호가없거나 틀렷습니다.");
			return null;
		}
		
		return clList[idx];
	}
	public void initLoad(String temp) {
		String[] tempuser = temp.split("\n");
		for(String t : tempuser) {
			String[] info = t.split("/");
			
			Client c = new Client(Integer.parseInt(info[0]),info[1],info[2],info[3]);
			
			init(c);
		}
	}
	//로그인 후 회원이 탈퇴
	public boolean secession(Client c) {
		System.out.println("[탈퇴]");
		int idx = checkuserIdPwIdx(c.id,Util.getValueString("비밀번호 입력 : "));
		if(idx == -1) {
			System.out.println("비밀번호를 틀렸습니다.");
			return false;
		}
		delete(idx);
		return true;
	}
	
	public String cldata() {
		if(clList == null) {
			return null;
		}
		String data = "";
		for(int i = 0; i < clList.length; i+=1) {
			data += clList[i].dataList() + "\n";
		}
		return data;
	}
}
