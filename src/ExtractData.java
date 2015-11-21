
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import net.sf.json.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



public class ExtractData {

	public static void main(String[] args) throws IOException {
		String url = "www.baidu.com";
        print("Fetching %s...", url);
        
      //This will get you the response.
        Response res = Jsoup.connect("http://xueqiu.com/user/login")
        		.data("username", "18600640221", 
        		"password",   "z.n.2002137305")
        		.method(Method.POST)
        		.execute();
        
      //This will get you cookies
//        Map<String, String> cookies = res.cookies();

        //And this is the easieste way I've found to remain in session
//        Document doc = Jsoup.connect("http://xueqiu.com/hq#exchange=CN&firstName=1&secondName=1_0").cookies(cookies).get();
        
//        String sessionId = res.cookie("s"); // you will need to check what the right cookie name is
//        Document doc2 = Jsoup.connect("http://xueqiu.com")
//        	    .cookie("s", sessionId)
//        	    .get();
//        Document doc = Jsoup.connect("http://xueqiu.com/hq#exchange=CN").get();
        String str = Jsoup.connect("http://xueqiu.com/stock/cata/stocklist.json")
        		.data("page","1",
        		"size","30",
        		"order","desc",
        		"orderby","percent",
        		"type","11,12",
        		"_","1447929898922")
        		.cookies(res.cookies())
        		.ignoreContentType(true).execute().body();
        JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON( str );
        JsonConfig jsonConfig = new JsonConfig(); 
        //jsonConfig.setArrayMode( JsonConfig.MODE_OBJECT_ARRAY ); 
        jsonConfig.setRootClass( Stock.class );  
        JSONArray jsonArray = JSONArray.fromObject( jsonObject.get("stocks") );
//        Stock[] stockList = (Stock[]) JSONArray.toArray(jsonArray, Stock.class);  
       
       List<Stock> stockList = new ArrayList<Stock>();

        
        for(Object st : jsonArray)
    	{
    		Stock bean2 = (Stock) JSONSerializer.toJava( (JSONObject)st, jsonConfig );
    		stockList.add(bean2);
    	}
        
        
        //output to file
        Export2File(stockList, "stock_output");
        
        //output to excel
        Export2Excel(stockList, "workbook.xls");
        
        //export to db
        String urlstring = "jdbc:mysql://192.168.1.103/stockdb?" +
        		"useUnicode=true&characterEncoding=UTF-8&" +
                "user=dozp&password=dozp";
        Export2DB(stockList,urlstring);
        
        print("Fetching %s...", url);
	}
	
	private static void Export2DB(List<Stock> stockList, String urlstring) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sqlstr="";
		try {
		    conn = DriverManager.getConnection(urlstring);

		    stmt = conn.createStatement();
		    try{
		    	stmt.executeUpdate("DROP TABLE STOCKTAB");
		    }catch(Exception e){
		    	
		    }
		    
		    stmt.executeUpdate("CREATE TABLE STOCKTAB (symbol VARCHAR(20),code VARCHAR(10),name VARCHAR(20), current DOUBLE) charset utf8");
		    
		    for(Stock sk : stockList){
		    	sqlstr = "INSERT INTO STOCKTAB VALUES(" + sk.getInsertRow() + ")";
		    	stmt.executeUpdate(sqlstr);
		    }
		    

		} catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		finally {
		    // it is a good idea to release
		    // resources in a finally{} block
		    // in reverse-order of their creation
		    // if they are no-longer needed

		    if (rs != null) {
		        try {
		            rs.close();
		        } catch (SQLException sqlEx) { } // ignore

		        rs = null;
		    }

		    if (stmt != null) {
		        try {
		            stmt.close();
		        } catch (SQLException sqlEx) { } // ignore

		        stmt = null;
		    }
		}
		
	}

	private static void Export2File(List<Stock> stockList, String filePath) throws FileNotFoundException
	{
		FileOutputStream out = new FileOutputStream( filePath );
      try ( PrintWriter pw = new PrintWriter(
				new OutputStreamWriter( out ) ) ) {
			for(Stock st : stockList)
			{
		//		Stock s=(Stock)JSONObject.toBean(JSONObject.fromObject(st), Stock.class);
//				Stock bean2 = (Stock) JSONSerializer.toJava( (JSONObject)st, jsonConfig );

				print(st, pw);
			}
				
		}
	}
	
	private static void Export2Excel(List<Stock> stockList, String excelPath) throws IOException {
		Workbook wb = new HSSFWorkbook();  // or new XSSFWorkbook();
        Sheet sheet = wb.createSheet("new sheet");
        
     // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet.createRow((short)0);
        // Create a cell and put a value in it.
        row.createCell(1).setCellValue("symbol");
        row.createCell(2).setCellValue("code");
        row.createCell(3).setCellValue("name");
        row.createCell(4).setCellValue("current");
        
        for(int i=0; i<stockList.size();i++){
        	row = sheet.createRow((short)i+1);
        	Stock sk = stockList.get(i);
        	row.createCell(1).setCellValue(sk.getSymbol());
            row.createCell(2).setCellValue(sk.getCode());
            row.createCell(3).setCellValue(sk.getName());
            row.createCell(4).setCellValue(sk.getCurrent());
        }

        
        FileOutputStream fileOut = new FileOutputStream(excelPath);
        wb.write(fileOut);
        fileOut.close();
	}
	
	
	private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }
	
	private static void print(Stock st, PrintWriter pw){
		pw.println(st.getSymbol() 
					+ "\t" + st.getCode()
					+ "\t" + st.getCurrent()
					+ st.getPercent());
	}

	//http://xueqiu.com/hq#exchange=CN&firstName=1&secondName=1_0
}
