public class Bank {
    private int nightSafeAmount;
    private int vaultAmount;
    private Courier[] couriers;
    private long startTime;
    private int nofActiveCouriers;

    /**
     * Creates a new bank, with a number of couriers, and
     * starts all the couriers.
     */
    public Bank(int nofCouriers, int nofVisits) {
        startTime = System.currentTimeMillis();
        couriers = new Courier[nofCouriers];
        nofActiveCouriers = nofCouriers;
        for(int i = 0; i < nofCouriers; i++) {
            couriers[i] = new Courier(nofVisits, this);
            couriers[i].start();
        }
    }

    /**
     * Deposit a given amount of money. The money is put in the
     * night safe. If there is too much money in the night safe,
     * all the money is transferred to the vault.
     */
    public synchronized void depositMoney(int amount) {
        nightSafeAmount += amount;
        if(nightSafeAmount > 200) {
            // Transfer the money in the night safe to the vault:
            vaultAmount += nightSafeAmount;
            nightSafeAmount = 0;
        }
    }

    /**
     * Called by a courier when he is done with his deposits.
     */
    public void courierDone() {
        nofActiveCouriers--;
        if(nofActiveCouriers == 0) {
            System.out.println("All couriers are done.");
            // Check if the money deposited equals the money in the bank:
            int moneyInBank = nightSafeAmount + vaultAmount;
            int moneyDeposited = calculateMoneyDeposited();
            System.out.println("Money in the bank: "+moneyInBank);
            System.out.println("Total money deposited: "+moneyDeposited);
            System.out.println("Discrepancy: "+(moneyDeposited-moneyInBank));
            System.out.println("Elapsed time: "+(System.currentTimeMillis()-startTime)+" milliseconds.");
        }
    }

    /**
     * Sum up all the reported deposits to find out how much
     * the couriers claim that they have deposited.
     */
    public int calculateMoneyDeposited() {
        int result = 0;
        for(int i = 0; i < couriers.length; i++)
            result += couriers[i].getAmountDeposited();
        return result;
    }

    /**
     * Reads the number of couriers and the number of visits to the
     * bank for each courier from the command line parameters, and
     * starts the simulation.
     */
    public static void main(String[] args) {
        if(args.length < 2) {
            System.out.println("Usage: java Bank <number of couriers> <number of visits>");
            System.exit(0);
        }
        int nofCouriers = new Integer(args[0]);
        int nofVisits = new Integer(args[1]).intValue();
        System.out.println("Starting simulation of "+nofCouriers+" couriers with "+nofVisits+" deposits each.");
        Bank b = new Bank(nofCouriers, nofVisits);
    }
}