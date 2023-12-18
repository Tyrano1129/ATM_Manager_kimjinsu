package ATM;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountDAO {
	private Account[] accList;
	
	public void print() {
		for(Account a : accList) {
			System.out.println(a);
		}
	}
	public void init(Account a) {
		int cnt = accList == null? 0 : accList.length;
		if(cnt != 0) {
			Account[] temp = accList;
			accList = new Account[cnt+1];
			for(int i = 0; i < temp.length; i+=1) {
				accList[i] = temp[i];
			}
		}else {
			accList = new Account[1];
		}
		
		accList[cnt] = a;
	}
	public void printOneClient(Client c) {
		System.out.println("----마이 페이지----");
		for(Account a : accList) {
			if(c.id.equals(a.clientid)) {
				System.out.println(a);
			}
		}
		System.out.println("----------------");
	}
	//user 삭제 될때 모든 account 사라지게.
	public void userOneAcountDelete(Client c) {
		int cnt = 0;
		for(Account a : accList) {
			if(a.clientid.equals(c.id)) {
				cnt+=1;
			}
		}
		if(cnt == 0) {
			return;
		}
		if(accList.length-cnt == 0) {
			accList = null;
			return;
		}
		Account[] temp = accList;
		accList = new Account[temp.length-cnt];
		int idx = 0;
		for(int i = 0; i < temp.length; i+=1) {
			if(!temp[i].clientid.equals(c.id)) {
				accList[idx++] = temp[i];
			}
		}
		print();
	}
	//자신 계좌 찾기
	private int checkIdxAccId(String accNum,String id) {
		for(int i = 0; i < accList.length; i+=1) {
			if(accNum.equals(accList[i].accNumber) && id.equals(accList[i].clientid)) {
				return i;
			}
		}
		return -1;
	}
	//계좌 찾기
	private int checkIdxAcc(String accNum) {
		for(int i = 0; i < accList.length; i+=1) {
			if(accNum.equals(accList[i].accNumber)) {
				return i;
			}
		}
		return -1;
	}
	//계좌번호 예외찾기
	private boolean accountCheck(String accNum) {
		String accPattern = "^\\d{4}-\\d{4}-\\d{4}$";
		Pattern p = Pattern.compile(accPattern);
		Matcher m = p.matcher(accNum);
		if(m.matches()) {
			return true;
		}else {
			System.out.println("1111-1111-1111형식으로 입력해주세요.");
			return false;
		}
	}
	//자신의 계좌가 있는지 없는지 확인
	private boolean checkAcccount(String id) {
		for(int i = 0; i < accList.length; i+=1) {
			if(id.equals(accList[i].clientid)) {
				return true;
			}
		}
		System.out.println("자신계좌가 없습니다.");
		return false;
	}
	//자신 계좌 갯수
	private int checkMyAccCnt(String id) {
		int cnt = 0;
		for(int i = 0; i < accList.length; i+=1) {
			if(id.equals(accList[i].clientid)) {
				cnt+=1;
			}
		}
		return cnt;
	}
	//계좌추가
	public void addAccount(Client c) {
		System.out.println("[계좌 추가]");
		int cnt = checkMyAccCnt(c.id);
		if(cnt == 3) {
			System.out.println("더이상 계좌 생성 불가능 합니다.");
			return;
		}
		String accNumber = Util.getValueString("계좌입력 : ");
		if(!accountCheck(accNumber)) {
			return;
		}
		int idx = checkIdxAcc(accNumber);
		if(idx != -1) {
			System.out.println("이미 가지고계신 계좌입니다.");
			return;
		}
		Account a = new Account(c.id,accNumber,0);
		init(a);
	}
	//계좌 삭제
	public void accountDelete(Client c) {
		if(!checkAcccount(c.id))return;
		System.out.println("[계좌 삭제]");
		String accNumber = Util.getValueString("삭제계좌입력 : ");
		if(!accountCheck(accNumber)) {
			return;
		}
		int idx = checkIdxAccId(accNumber,c.id);
		if(idx == -1) {
			System.out.println("입력하신 계좌는 없습니다.");
			return;
		}
		delete(idx);
		
	}
	//삭제
	public void delete(int idx) {
		if(accList.length == 1) {
			accList = null;
			return;
		}
		
		Account[] temp = accList;
		accList = new Account[temp.length-1];
		int index = 0;
		for(int i = 0; i < temp.length; i+=1) {
			if(i != idx) {
				accList[index++] = temp[i];
			}
		}
	}
	//입금
	public void deposit(Client c) {
		if(!checkAcccount(c.id)) return;
		System.out.println("[입금]");
		String accNumber = Util.getValueString("계좌입력 : ");
		if(!accountCheck(accNumber)) {
			return;
		}
		int idx = checkIdxAccId(accNumber,c.id);
		int money = Util.getValue("입금할금액 입력 ",1,100000);
		accList[idx].money += money;
	}
	//출금
	public void withdraw(Client c) {
		if(!checkAcccount(c.id)) return;
		System.out.println("[출금]");
		String accNumber = Util.getValueString("계좌입력 : ");
		if(!accountCheck(accNumber)) {
			return;
		}
		int idx = checkIdxAccId(accNumber,c.id);
		if(idx == -1) {
			System.out.println("입력하신 계좌는 없습니다.");
			return;
		}
		int money = Util.getValue("출금할금액 입력 ",1,accList[idx].money);
		accList[idx].money -= money;
		
	}
	//이체
	public void transfer(Client c) {
		if(!checkAcccount(c.id)) return;
		System.out.println("[이체]");
		String accNumber1 = Util.getValueString("이체할계좌입력 : ");
		if(!accountCheck(accNumber1)) {
			return;
		}
		int idx1 = checkIdxAccId(accNumber1,c.id);
		if(idx1 == -1) {
			System.out.println("잘못입력하셨습니다.");
			return;
		}
		if(accList[idx1].money == 0) {
			System.out.println("가지고 계신 금액이없습니다.");
			return;
		}
		String accNumber2 = Util.getValueString("받을계좌입력 : ");
		if(!accountCheck(accNumber2)) {
			return;
		}
		int idx2 = checkIdxAcc(accNumber2);
		if(accNumber1.equals(accNumber2)) {
			System.out.println("같은계좌로 이체가 불가능합니다.");
			return;
		}
		if(idx2 == -1) {
			System.out.println("잘못입력하셨습니다.");
			return;
		}
		int money = Util.getValue("이체할금액 입력 ",1,accList[idx1].money);
		accList[idx1].money -= money;
		accList[idx2].money += money;
		
	}
	public void initLoad(String temp) {
		String[] tempaccount = temp.split("\n");
		for(String t : tempaccount) {
			String[] info = t.split("/");
			Account a = new Account(info[0],info[1],Integer.parseInt(info[2]));
			
			init(a);
		}
	}
	//파일 저장
	public String accdata() {
		if(accList == null) {
			return null;
		}
		String data = "";
		for(Account a : accList) {
			data += a.dataList() + "\n";
		}
		return data;
	}
}
