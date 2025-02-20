import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class Stock {
    private String symbol;
    private double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

class Portfolio {
    private HashMap<String, Integer> stockPortfolio = new HashMap<>();
    private double cashBalance;

    public Portfolio(double initialCashBalance) {
        this.cashBalance = initialCashBalance;
    }

    public double getCashBalance() {
        return cashBalance;
    }

    public void updateCashBalance(double amount) {
        this.cashBalance += amount;
    }

    public void addStock(String symbol, int quantity) {
        stockPortfolio.put(symbol, stockPortfolio.getOrDefault(symbol, 0) + quantity);
    }

    public void removeStock(String symbol, int quantity) {
        if (stockPortfolio.containsKey(symbol)) {
            int currentQuantity = stockPortfolio.get(symbol);
            if (currentQuantity >= quantity) {
                stockPortfolio.put(symbol, currentQuantity - quantity);
            } else {
                System.out.println("Not enough stocks to sell.");
            }
        } else {
            System.out.println("You don't own this stock.");
        }
    }

    public void showPortfolio() {
        System.out.println("Your Portfolio:");
        for (String symbol : stockPortfolio.keySet()) {
            System.out.println(symbol + ": " + stockPortfolio.get(symbol) + " shares");
        }
        System.out.println("Cash Balance: $" + cashBalance);
    }

    public boolean canAffordStock(Stock stock, int quantity) {
        return cashBalance >= stock.getPrice() * quantity;
    }

    public boolean ownsStock(String symbol, int quantity) {
        return stockPortfolio.getOrDefault(symbol, 0) >= quantity;
    }
}

class StockMarket {
    private HashMap<String, Stock> stockList = new HashMap<>();

    public StockMarket() {
        // Simulate stock data
        stockList.put("AAPL", new Stock("AAPL", 150.00));
        stockList.put("GOOG", new Stock("GOOG", 2800.00));
        stockList.put("TSLA", new Stock("TSLA", 800.00));
        stockList.put("AMZN", new Stock("AMZN", 3400.00));
        stockList.put("MSFT", new Stock("MSFT", 299.00));
    }

    public Stock getStock(String symbol) {
        return stockList.get(symbol);
    }

    public void displayMarketData() {
        System.out.println("Market Data:");
        for (String symbol : stockList.keySet()) {
            Stock stock = stockList.get(symbol);
            System.out.println(symbol + ": $" + stock.getPrice());
        }
    }

    public void updateStockPrice(String symbol, double newPrice) {
        if (stockList.containsKey(symbol)) {
            stockList.get(symbol).setPrice(newPrice);
        }
    }
}

class TransactionHistory {
    private ArrayList<String> transactions = new ArrayList<>();

    public void recordTransaction(String transactionDetails) {
        transactions.add(transactionDetails);
    }

    public void showTransactionHistory() {
        System.out.println("Transaction History:");
        for (String transaction : transactions) {
            System.out.println(transaction);
        }
    }
}

public class StockTradingPlatform {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StockMarket market = new StockMarket();
        Portfolio portfolio = new Portfolio(10000); // Starting with $10,000
        TransactionHistory history = new TransactionHistory();

        while (true) {
            System.out.println("\n--- Stock Trading Platform ---");
            System.out.println("1. View Market Data");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Transaction History");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    market.displayMarketData();
                    break;
                case 2:
                    System.out.print("Enter stock symbol to buy: ");
                    String buySymbol = scanner.nextLine().toUpperCase();
                    Stock stockToBuy = market.getStock(buySymbol);
                    if (stockToBuy != null) {
                        System.out.print("Enter quantity to buy: ");
                        int buyQuantity = scanner.nextInt();
                        if (portfolio.canAffordStock(stockToBuy, buyQuantity)) {
                            portfolio.addStock(buySymbol, buyQuantity);
                            portfolio.updateCashBalance(-stockToBuy.getPrice() * buyQuantity);
                            history.recordTransaction("Bought " + buyQuantity + " shares of " + buySymbol);
                            System.out.println("Bought " + buyQuantity + " shares of " + buySymbol);
                        } else {
                            System.out.println("You don't have enough funds to buy this stock.");
                        }
                    } else {
                        System.out.println("Stock not found.");
                    }
                    break;
                case 3:
                    System.out.print("Enter stock symbol to sell: ");
                    String sellSymbol = scanner.nextLine().toUpperCase();
                    Stock stockToSell = market.getStock(sellSymbol);
                    if (stockToSell != null) {
                        System.out.print("Enter quantity to sell: ");
                        int sellQuantity = scanner.nextInt();
                        if (portfolio.ownsStock(sellSymbol, sellQuantity)) {
                            portfolio.removeStock(sellSymbol, sellQuantity);
                            portfolio.updateCashBalance(stockToSell.getPrice() * sellQuantity);
                            history.recordTransaction("Sold " + sellQuantity + " shares of " + sellSymbol);
                            System.out.println("Sold " + sellQuantity + " shares of " + sellSymbol);
                        } else {
                            System.out.println("You don't own enough shares to sell.");
                        }
                    } else {
                        System.out.println("Stock not found.");
                    }
                    break;
                case 4:
                    portfolio.showPortfolio();
                    break;
                case 5:
                    history.showTransactionHistory();
                    break;
                case 6:
                    System.out.println("Exiting the platform...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
