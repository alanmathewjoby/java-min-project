abstract class Book{
	String title;
	double price;
	
	Book(){
		title = "Bible";
		price = 1000;
	}
	
	Book(String title, double price){
		this.title = title;
		this.price = price;
	}
	
	String getTitle(){
		return title;
	}
	
	double getPrice(){
		return price;
	}
	
	abstract public String toString();
	
}

class Fiction extends Book{
	int numPages;
	
	Fiction(String title, int numPages){
		super(title, 100.0);
		this.numPages = numPages;
	}
	
	Fiction(String title, int numPages, double price){
		super(title, price);
		this.numPages = numPages;
	}
	
	int getNumPages(){
		return numPages;
	}
	
	@Override
	public String toString(){
		return "Title: " + title + "," + "Price: " + price + "," + "Pages: " + numPages;
	}
	
}

class main{
	public static void main(String[] args){
		Fiction f1 = new Fiction("Quran", 500);
		System.out.println(f1.getPrice() + "\n" + f1.getTitle() + "\n" + f1.getNumPages() + "\n" + f1.toString());
		
		Fiction f2 = new Fiction("Anime", 200, 1200);
		System.out.println(f2.getPrice() + "\n" + f2.getTitle() + "\n" + f2.getNumPages() + "\n" + f2.toString());
		
	}
}
