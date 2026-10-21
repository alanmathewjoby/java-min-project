class BankAccount{
	int accountNumber;
	static double balance = 0;
	final double interestRate = 5.0;
	
	void deposit(double amount){
		balance += amount;
	}
}

class SavingsAccount extends BankAccount{
	double minimumBalance;
	
	void withdraw(double amount){
		balance -= amount;
	}
}

class FixedDepositAccount extends SavingsAccount{
	int term;
	
	double getInterest(int term){
		return balance*interestRate*term; 
	}
}

class main{
	public static void main(String[] args){
		BankAccount b = new BankAccount();
		b.deposit(1000);
		System.out.println("Balance: " + b.balance);
		
		SavingsAccount s = new SavingsAccount();
		s.withdraw(200);
		System.out.println("Balance: " + s.balance);
		
		FixedDepositAccount f = new FixedDepositAccount();
		System.out.println(f.getInterest(10));
	}
}
