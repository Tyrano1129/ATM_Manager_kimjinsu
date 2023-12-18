package ATM;

public class Client {
	int clientNo;
	String id;
	String pw;
	String name;
	public Client(int clientNo, String id, String pw, String name) {
		this.clientNo = clientNo;
		this.id = id;
		this.pw = pw;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return clientNo + " " + id + " " + pw + " " + name;
	}
	
	public String dataList() {
		return clientNo + "/" + id + "/" + pw + "/" + name;
	}
}
