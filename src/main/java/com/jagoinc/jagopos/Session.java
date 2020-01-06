package com.jagoinc.jagopos;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Statement;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author jago
 */
public class Session {
    
    //settings data and methods
    private double _taxRate, _currencyMultiplier;
    private String _businessPhone, _businessAddress, _businessName, _businessEmail, _businessWebsite, _salesTaxName;
    private char _currencyChar;
    private PrintWriter _writer;
    private FileReader _reader;
    private Scanner _readerScanner;
    private Connection _con;
    
    
    public Session() throws IOException, ClassNotFoundException{
        
        try{
            
            _con = openDatabase();
            loadSettingsIntoSession(_con);
            
        }catch(SQLException e){
            System.out.println("Connection Failed! "+e.getMessage());
            //System.exit(143);
        }
        
        //Old file-bases settings code
        //writeFreshSettingsFile("Jago_POS_Settings.dat");
        //openSettingsFile("Jago_POS_Settings.dat");
        /*_taxRate=0.105;
        _businessPhone="(323) 934-3373";
        _businessAddress="7301 Melrose Ave\nLos Angeles CA 90046";
        _businessName="Mega City One";
        _businessEmail="info@megacity.one";
        _businessWebsite="http://www.megacity.one";
        _salesTaxName="CA Sales Tax";
        _currencyChar='$';
        _currencyMultiplier=1;*/
        
    }
    //old file IO-bases settings file. Left here for backup purposes.
    private void writeFreshSettingsFile(String filename) throws IOException{
        _writer = new PrintWriter("Jago_POS_Settings.dat", "UTF-8");
        _writer.println("_businessName=Mega City One");
        _writer.println("_businessAddress=7301 Melrose Ave<br />Los Angeles CA 90046");
        _writer.println("_businessWebsite=http://www.megacity.one");
        _writer.println("_businessEmail=info@megacity.one");
        _writer.println("_businessPhone=(323) 934-3373");
        _writer.println("_currencyChar=$");
        _writer.println("_currencyMultiplier=1");
        _writer.println("_taxRate=0.105");
        _writer.println("_salesTaxName=CA Sales Tax");
        _writer.close();
    }
    
    public boolean saveSettings() throws IOException, SQLException{
        saveSettingsInDatabase();
        return true;
    }
    
    private void openSettingsFile(String filename) throws FileNotFoundException{
        _reader = new FileReader(filename);
        _readerScanner = new Scanner(_reader);
    }
    
    @SuppressWarnings("empty-statement")
    private void loadSettingsIntoSession(Connection con) throws SQLException, IOException{
        String settingsQueryString = "SELECT * FROM Settings ORDER BY session_id DESC LIMIT 1";
        
        Statement settingsQuery = con.createStatement();  
        
        ResultSet settingsResults = settingsQuery.executeQuery(settingsQueryString);
        settingsResults.next();
        
//        for(int i = 1; i < 11; i++){
//            
//            System.out.println(settingsResults.getString(i));
//            
//        }
        
        _businessName = settingsResults.getString(2);
        _businessAddress = settingsResults.getString(3);
        _businessEmail = settingsResults.getString(4);
        _businessPhone = settingsResults.getString(5);
        _businessWebsite = settingsResults.getString(6);
        _salesTaxName = settingsResults.getString(7);
        _taxRate = settingsResults.getDouble(8);
        
        
        switch(settingsResults.getInt(9)){
            case 0:
                setCurrency('$');
                break;
            case 1:
                setCurrency('€');
                break;
            case 2:
                setCurrency('£');
                break;
            case 3:
                setCurrency('¥');
                break;
            default:
                setCurrency('$');
                break;
                
        };
        
        /* OLD FILE-BASED WAY throws IOException
        _readerScanner.useDelimiter("=|\\n");
        _readerScanner.next();
        _businessName = _readerScanner.next();
        _readerScanner.next();
        _businessAddress = _readerScanner.next();
        _readerScanner.next();
        _businessWebsite = _readerScanner.next();
        _readerScanner.next();
        _businessEmail = _readerScanner.next();
        _readerScanner.next();
        _businessPhone = _readerScanner.next();
        _readerScanner.next();
        _currencyChar = _readerScanner.next().charAt(0);
        _readerScanner.next();
        _currencyMultiplier = _readerScanner.nextDouble();
        _readerScanner.next();
        _taxRate = _readerScanner.nextDouble();
        _readerScanner.next();
        _salesTaxName = _readerScanner.next();
        _readerScanner.close();*/
        //System.out.println(""+_businessName+", "+_businessAddress+", "+_businessWebsite+", "+_businessEmail+", "+_businessPhone+", "+_currencyChar+", "+_currencyMultiplier+", "+_taxRate+", "+_salesTaxName+"");
    }
    

    
    private void saveSettingsInDatabase() throws SQLException{
        String settingsQueryString = "SELECT * FROM Settings ORDER BY session_id DESC LIMIT 1";
        
        Statement settingsQuery = _con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        
        ResultSet settingsResults = settingsQuery.executeQuery(settingsQueryString);
        settingsResults.next();

        
        settingsResults.updateString(2, _businessName);
        settingsResults.updateString(3, _businessAddress);
        settingsResults.updateString(6, _businessWebsite);
        settingsResults.updateString(4, _businessEmail);
        settingsResults.updateString(5, _businessPhone);
        settingsResults.updateString(7, _salesTaxName);
        settingsResults.updateDouble(8, _taxRate);
        
        
        switch(_currencyChar){
            case '$':
                setCurrency('$');
                settingsResults.updateInt(9, 0);
                break;
            case '€':
                setCurrency('€');
                settingsResults.updateInt(9, 1);
                break;
            case '£':
                setCurrency('£');
                settingsResults.updateInt(9, 2);
                break;
            case '¥':
                setCurrency('¥');
                settingsResults.updateInt(9, 3);
                break;
            default:
                setCurrency('$');
                settingsResults.updateInt(9, 0);
                break;
        }
        
        settingsResults.updateRow();
    }
    
    private void writeToSettingsFile() throws IOException{
        _writer = new PrintWriter("Jago_POS_Settings.dat", "UTF-8");
        _writer.println("_businessName="+_businessName);
        _writer.println("_businessAddress="+_businessAddress);
        _writer.println("_businessWebsite="+_businessWebsite);
        _writer.println("_businessEmail="+_businessEmail);
        _writer.println("_businessPhone="+_businessPhone);
        _writer.println("_currencyChar="+_currencyChar);
        _writer.println("_currencyMultiplier="+_currencyMultiplier);
        _writer.println("_taxRate="+_taxRate);
        _writer.println("_salesTaxName="+_salesTaxName);
        _writer.close();
    }
    
    private void closeSettingsFile() throws IOException{
        _writer.close();
        _reader.close();
    }
    
    public void setTaxRate(double percentage){
        _taxRate = percentage;
        
    }
    
    public void setPhone(String phone){
        _businessPhone = phone;
    }
    
    public void setAddress(String address){
        _businessAddress = address;
    }
    
    public void setName(String name){
        _businessName = name;
    }
        
    public void setEmail(String email){
        _businessEmail = email;
    }
    
    public void setTaxName(String taxName){
        _salesTaxName = taxName;
    }
    
    public void setWebsite(String site){
        _businessWebsite = site;
    }
    
    
    public double getTaxRate(){
        return _taxRate;
    }
    
    public String getPhone(){
        return _businessPhone;
    }
    
    public String getAddress(){
        return _businessAddress;
    }
    
    public String getName(){
        return _businessName;
    }
        
    public String getEmail(){
        return _businessEmail;
    }
    
    public String getTaxName(){
        return _salesTaxName;
    }
    
    public String getWebsite(){
        return _businessWebsite;
    }
    
    public int getCurrency(){
        
        switch(_currencyChar){
            case '$':
                return 0;
            case '€':
                return 1;
            case'£':
                return 2;
            case '¥':
                return 3;
            default:
                return 0;
                
        }
        
    }
    
    public void setCurrency(char currencyChange){
        
        switch(currencyChange){
            case '$':
                _currencyChar = '$';
                _currencyMultiplier = 1;
                break;
            case '€':
                _currencyChar = '€';
                _currencyMultiplier = .898154;
                break;
            case'£':
                _currencyChar = '£';
                _currencyMultiplier = .807650;
                break;
            case '¥':
                _currencyChar = '¥';
                _currencyMultiplier = 108.485;
                break;
            default:
                _currencyChar = '$';
                _currencyMultiplier = 1;
                break;
            }
        
        }
        
        //end settings data and methods
        //start database data and methods
        private Connection openDatabase() throws SQLException{
            Connection con = DriverManager.getConnection("jdbc:mysql://173.255.241.100:3306/jagoposDB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=PST","jagoPOS","NHhffClm4iIe6vTk");
            //System.out.println("Connection Successful!");
            return con;
        }
        
        public ArrayList<Product> searchProductDatabase(String searchTerm) throws SQLException{
            searchTerm = searchTerm.toLowerCase();
            ArrayList<Product> results = new ArrayList();
            Product matchingProduct;
            int rowLength = 5, productInventory;
            long productUPC;
            String searchQueryString = "SELECT * FROM Product ORDER BY product_UPC", productName, searchAgainst;
            double productCost, productPrice;
        
            _con = openDatabase();
        
            Statement searchQuery = _con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        
            ResultSet searchResults = searchQuery.executeQuery(searchQueryString);
        
            while(searchResults.next()){
                //System.out.println(searchResults.getString(2));
                searchAgainst = searchResults.getString(2).toLowerCase();
                
                if(searchAgainst.contains(searchTerm) || searchResults.getString(1).contains(searchTerm)){
                    
                    productUPC = Long.parseLong(searchResults.getString(1));
                    productName = searchResults.getString(2);
                    productCost = Double.parseDouble(searchResults.getString(3));
                    productPrice = Double.parseDouble(searchResults.getString(4));
                    productInventory = Integer.parseInt(searchResults.getString(5));
                
                    matchingProduct = new Product(productUPC, productName, productPrice, productCost, productInventory);
                
                    results.add(matchingProduct);
                }
            
            }
        
            return results;
        }
        
        public ArrayList<Customer> searchCustomerDatabase(String searchTerm) throws SQLException{
            searchTerm = searchTerm.toLowerCase();
            ArrayList<Customer> results = new ArrayList();
            Customer matchingCustomer;
            int rowLength = 5, productInventory;
            long customerID;
            String searchQueryString = "SELECT * FROM Customer ORDER BY customer_id";
            String customerName, customerPhone, customerEmail, customerPostalCode, searchAgainst;
            
            _con = openDatabase();
        
            Statement searchQuery = _con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        
            ResultSet searchResults = searchQuery.executeQuery(searchQueryString);
        
            while(searchResults.next()){
                //System.out.println(searchResults.getString(3));
                searchAgainst = searchResults.getString(3).toLowerCase();
                
                if(searchAgainst.contains(searchTerm) || searchResults.getString(1).contains(searchTerm)){
                    
                    customerID = Long.parseLong(searchResults.getString(1));
                    customerPostalCode = searchResults.getString(2);
                    customerName = searchResults.getString(3);
                    customerPhone = searchResults.getString(4);
                    customerEmail = searchResults.getString(5);
                
                    matchingCustomer = new Customer(customerName, customerPostalCode, customerEmail, customerPhone, customerID);
                
                    results.add(matchingCustomer);
                }
            
            }
        
            return results;
        }
        
        public ArrayList<Invoice> searchInvoiceDatabase(String searchTerm) throws SQLException{
        
        ArrayList<Invoice> results = new ArrayList();
        
        int rowLength = 10;
        
        String searchQueryString = "SELECT * FROM Invoice ORDER BY invoice_ID", productName;
        int  invoiceID;
        
        _con = openDatabase();
        
        Statement searchQuery = _con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        
        ResultSet searchResults = searchQuery.executeQuery(searchQueryString);
        
        while(searchResults.next()){
            for(int i=1; i <= rowLength; i++){
                searchResults.getString(i);
            }
        }
        
        searchResults.next();
        
        return results;
    }
        
        
        //end database methods
        private Invoice _workingInvoice;
        private double _runningTotal;
        
        public void setRunningTotal(double newRunningTotal){
            _runningTotal = newRunningTotal;
        }
        
        public double getRunningTotal(){
            return _runningTotal;
        }
}
