package ATM;

public class Account {
	String clientid;
	String accNumber;
	int money;
	public Account(String clientid, String accNumber, int money) {
		this.clientid = clientid;
		this.accNumber = accNumber;
		this.money = money;
	}
	@Override
	public String toString() {
		return clientid + " " + accNumber + " " + money;
	}
	
	public String dataList() {
		return clientid + "/" + accNumber + "/" + money;
	}
}
