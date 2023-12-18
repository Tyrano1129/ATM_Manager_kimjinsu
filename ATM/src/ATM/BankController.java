package ATM;

public class BankController {
	
	private AccountDAO accDAO;
	private ClientDAO  clDAO;
	public void init() {
		accDAO = new AccountDAO();
		clDAO = new ClientDAO();
	}
	//[1]관리자 [2]사용자 [0]종료
	public void run() {
		init();
		printMenu();
		
	}
	private void printMenu() {
		while(true) {
			System.out.println("[1] 관리자");
			System.out.println("[2] 사용자");
			System.out.println("[0] 종료");
			
			int sel = Util.getValue("메뉴 선택 : ",0,2);
			if(sel == 1) {
				adminMenu();
			}else if (sel == 2) {
				userMenu();
			}else if (sel == 0) {
				System.out.println("종료...");
				break;
			}
		}
	}

	//관리자
	//[1] 회원 목록 [2] 회원수정 [3] 회원삭제 [4] 데이터 저장 [5] 데이터 불러오기
	private void adminMenu() {
		while(true) {
			System.out.println("[관리자 메뉴]");
			System.out.println("[1]회원 목록");
			System.out.println("[2]회원 수정");
			System.out.println("[3]회원 삭제");
			System.out.println("[4]데이터 저장");
			System.out.println("[5]데이터 불러오기");
			System.out.println("[0]뒤로 가기");
			
			int sel = Util.getValue("관리자 메뉴 입력 : ",0,5);
			if(sel == 1) {
				clDAO.print();
			}else if(sel == 2) {
				clDAO.userRepair();
			}else if(sel == 3) {
				clDAO.userDelete(accDAO);
			}else if(sel == 4) {
				Util.fileSave(clDAO, accDAO);
			}else if(sel == 5) {
				Util.fileload(clDAO, accDAO);
			}else if(sel == 0) {
				System.out.println("뒤로가기...");
				break;
			}
		}
	}
	//사용자 메뉴
	//[1] 회원가입 [2]로그인  [0]뒤로가기
	// 회원가입 : 회원 아이디 중복 확인
	private void userMenu() {
		while(true) {
			System.out.println("[사용자 메뉴]");
			System.out.println("[1]회원 가입");
			System.out.println("[2]로그인");
			System.out.println("[0]뒤로가기");
			
			int sel = Util.getValue("사용자 메뉴 입력 : ",0,2);
			if(sel == 1) {
				clDAO.userJoin();
			}else if(sel == 2) {
				Client c = clDAO.userLogin();
				if(c == null)continue;
				loginMenu(c);
			}else if(sel == 0) {
				System.out.println("뒤로가기...");
				break;
			}
			
		}
	}
	//로그인 메뉴
	//[1]계좌추가 [2]계좌삭제 [3]입금 [4]출금 [5]이체 [6]탈퇴 [7]마이페이지 [0] 로그아웃
	
	// 계좌 추가 ( 숫자4개-숫자4개-숫자4개 ) 일치할때 추가 가능 : 중복확인
	// 계좌 삭제 : 본인 회원 계좌만 가능
	
	// 입금 : accList 에 계좌가 있을때만 입금 가능 : 100원이상 입금/이체/출금 : 계좌 잔고만큼만
	// 이체 : 이체할 계좌랑 이체받을 계좌만 일치 안하면 됨
	// 탈퇴  : 패스워드 다시 입력 -> 탈퇴가능
	
	//마이페이지 : 내 계좌 목록(+잔고) 확인
	private void loginMenu(Client c) {
		while(true) {
			System.out.printf("[%s 회원님 로그인 메뉴]%n",c.name);
			System.out.println("[1]계좌 추가");
			System.out.println("[2]계좌 삭제");
			System.out.println("[3]입금");
			System.out.println("[4]출금");
			System.out.println("[5]이체");
			System.out.println("[6]탈퇴");
			System.out.println("[7]마이페이지");
			System.out.println("[0]로그아웃");
			
			int sel = Util.getValue("로그인 메뉴 입력 : ", 0, 7);
			if(sel == 1) {
				accDAO.addAccount(c);
			}else if(sel == 2) {
				accDAO.accountDelete(c);
			}else if(sel == 3) {
				accDAO.deposit(c);
			}else if(sel == 4) {
				accDAO.withdraw(c);
			}else if(sel == 5) {
				accDAO.transfer(c);
			}else if(sel == 6) {
				if(!clDAO.secession(c)) continue;
				accDAO.userOneAcountDelete(c);
				System.out.println("탈퇴 완료...");
				break;
			}else if(sel == 7) {
				accDAO.printOneClient(c);
			}else if(sel == 0) {
				System.out.println("로그아웃");
				break;
			}
		}
	}
}
