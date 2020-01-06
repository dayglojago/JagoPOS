package com.jagoinc.jagopos;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jago
 */
public class Customer {
    
    private String _name, _postalCode, _emailAddress, _phoneNumber;
    private long  _customerNumber;
    
    public Customer() throws SQLException{
        setCustomerCount();
        _name = "Rando";
        _postalCode = "";
        _emailAddress = "";
        _phoneNumber = "";
    }
    
    public Customer(boolean placeHolder){
        _customerNumber = 0;
        _name = "";
        _postalCode = "";
        _emailAddress = "";
        _phoneNumber = "";
    }
    
    
    public Customer(String name) throws SQLException{
        setCustomerCount();
        _name = name;
        _postalCode = "";
        _emailAddress = "";
        _phoneNumber = "";
    }
    
    public Customer(String name, String phoneNumber) throws SQLException{
        setCustomerCount();
        _name = name;
        _postalCode = "";
        _emailAddress = "";
        _phoneNumber = phoneNumber;
    }
    public Customer(String name, String postalCode, String emailAddress) throws SQLException{
        setCustomerCount();
        _name = name;
        _postalCode = postalCode;
        _emailAddress = emailAddress;
        _phoneNumber = "";
    }
    public Customer(String name, String postalCode, String emailAddress, String phoneNumber, long customerID) throws SQLException{
        _customerNumber = customerID;
        _name = name;
        _postalCode = postalCode;
        _emailAddress = emailAddress;
        _phoneNumber = phoneNumber;
    }
    
    public String getName(){
        return _name;
    }
    public String getPostalCode(){
        return _postalCode;
    }
    public String getEmail(){
        return _emailAddress;
    }
    public String getPhone(){
        return _phoneNumber;
    }
    public long getCustomerID(){
        return _customerNumber;
    }
    
    //===============================
    public void setName(String newName){
        _name = newName;
    }
    public void setPostalCode(String newPostal){
        _postalCode = newPostal;
    }
    public void setEmail(String newEmail){
        _emailAddress = newEmail;
    }
    public void setPhone(String newPhone){
        _phoneNumber = newPhone;
    }
        public boolean saveCustomer() throws SQLException{
        return writeCustomerToDatabase();
    }
        
    private void setCustomerCount() throws SQLException{
        Connection con = DriverManager.getConnection("jdbc:mysql://173.255.241.100:3306/jagoposDB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=PST",
                                                        "jagoPOS","NHhffClm4iIe6vTk");
        String customerQueryString = "SELECT * FROM Customer ORDER BY customer_id DESC";
        Statement customerQuery = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);  
        ResultSet customerResults = customerQuery.executeQuery(customerQueryString);
        customerResults.next();
        //System.out.println(customerResults.getString(1));
        _customerNumber = 1+Long.parseLong(customerResults.getString(1));
        
    }
    
    private boolean writeCustomerToDatabase() throws SQLException{
        
        Connection con = DriverManager.getConnection("jdbc:mysql://173.255.241.100:3306/jagoposDB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=PST",
                                                        "jagoPOS","NHhffClm4iIe6vTk");
        try{
            
            String addCustomerString = "insert into Customer (customer_id, postal_code, customer_name, customer_phone, customer_email) values";
            addCustomerString += "("+_customerNumber+", '"+_postalCode+"', '"+_name+"', '"+_phoneNumber+"', '"+_emailAddress+"')";
            Statement addProductRequest = con.createStatement();
            addProductRequest.executeUpdate(addCustomerString);
            con.close();
            return true;
            
        }catch(SQLException e){
            
            System.out.println(e.getMessage());
            return false;
            
        }
    }

    public ResultSet retrieveCustomer(long customerID) throws SQLException {
        return retrieveCustomerInDatabase(customerID);
    }
    
    private ResultSet retrieveCustomerInDatabase(long customerID) throws SQLException{
        
        Connection con = DriverManager.getConnection("jdbc:mysql://173.255.241.100:3306/jagoposDB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=PST","jagoPOS","NHhffClm4iIe6vTk");
        
        String customerQueryString = "SELECT * FROM Customer WHERE customer_id="+customerID+"";
        
        Statement customerQuery = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);  
        
        ResultSet customerResults = customerQuery.executeQuery(customerQueryString);
        
        return customerResults;

    }
}

