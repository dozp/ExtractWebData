
public class Stock {
	private String symbol;
	private String code;
	private String name;
	private double current;
	private double percent;
	private double change;
	private double high;
	private double low;
	private double high52w;
	private double low52w;
	private double marketcapital;
	private double amount;
	private double pettm;
	private double volume;
	private boolean hasexist;
	public String getSymbol() {
		return symbol;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getCurrent() {
		return current;
	}
	public void setCurrent(double current) {
		this.current = current;
	}
	public double getChange() {
		return change;
	}
	public void setChange(double change) {
		this.change = change;
	}
	public double getHigh() {
		return high;
	}
	public void setHigh(double high) {
		this.high = high;
	}
	public double getLow() {
		return low;
	}
	public void setLow(double low) {
		this.low = low;
	}
	public double getHigh52w() {
		return high52w;
	}
	public void setHigh52w(double high52w) {
		this.high52w = high52w;
	}
	public double getLow52w() {
		return low52w;
	}
	public void setLow52w(double low52w) {
		this.low52w = low52w;
	}
	public double getMarketcapital() {
		return marketcapital;
	}
	public void setMarketcapital(double marketcapital) {
		this.marketcapital = marketcapital;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getPettm() {
		return pettm;
	}
	public void setPettm(double pettm) {
		this.pettm = pettm;
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}
	public boolean isHasexist() {
		return hasexist;
	}
	public void setHasexist(boolean hasexist) {
		this.hasexist = hasexist;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public double getPercent() {
		return percent;
	}
	public void setPercent(double percent) {
		this.percent = percent;
	}
	
	public String getInsertRow(){
		String res="";
		res += "'" + getSymbol() + "'"
				+ ",'" + getCode() + "'"
				+ ",'" + getName() + "'"
				+ "," + getCurrent();
				
		return res;
	}
}
