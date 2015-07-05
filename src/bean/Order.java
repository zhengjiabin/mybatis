package bean;

public class Order {
	private int id;
	private String no;
	private int price;

	public Order(int id, String no, int price) {
		super();
		this.id = id;
		this.no = no;
		this.price = price;
	}

	public Order() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
}
