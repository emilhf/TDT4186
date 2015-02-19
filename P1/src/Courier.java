public class Courier extends Thread {
    private Bank bank;
    private int nofVisits;
    private int moneyDeposited;

    public Courier(int nofVisits, Bank bank) {
        this.nofVisits = nofVisits;
        this.bank = bank;
    }

    /**
     * Visit the bank nofVisits times, and each time
     * deposit between 50 and 149 dollars.
     * Keep records of how much we have deposited, as the
     * bank has been known to keep faulty records.
     */
    public void run () {
        for(int i = 0; i < nofVisits; i++) {
            int sum = 50+(int)(Math.random()*100);
            bank.depositMoney(sum);
            moneyDeposited += sum;
        }
        bank.courierDone();
    }

    public int getAmountDeposited() {
        return moneyDeposited;
    }
}
