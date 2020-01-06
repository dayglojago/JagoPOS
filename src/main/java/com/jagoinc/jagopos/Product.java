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
public class Product {
    
    private double _baseSalePrice, _costOfGood;
    private long _itemUPC, _inventoryQty;
    private String _description;
    
    public Product(){
        _itemUPC = 0;
        _description = "";
        _baseSalePrice = 0;
        _costOfGood = 0;
        _inventoryQty = 0;
    }
    
    public Product(long itemUPC, String description){
        _itemUPC = itemUPC;
        _description = description;
        _baseSalePrice = 0;
        _costOfGood = 0;
        _inventoryQty = 0;
    }
    
    public Product(long itemUPC, String description, double baseSalePrice){
        _itemUPC = itemUPC;
        _description = description;
        _baseSalePrice = baseSalePrice;
        _costOfGood = 0;
        _inventoryQty = 0;
    }
    
    public Product(long itemUPC, String description, double baseSalePrice, double cost){
        _itemUPC = itemUPC;
        _description = description;
        _baseSalePrice = baseSalePrice;
        _costOfGood = cost;
        _inventoryQty = 0;
    }
    
    public Product(long itemUPC, String description, double baseSalePrice, double cost, long inventory){
        _itemUPC = itemUPC;
        _description = description;
        _baseSalePrice = baseSalePrice;
        _costOfGood = cost;
        _inventoryQty = inventory;
    }
    
    public double getPrice(){
        return _baseSalePrice;
    }
    
    public double getCost(){
        return _costOfGood;
    }
    
    public String getDescription(){
        return _description;
    }
    
    public long getInventory(){
        return _inventoryQty;
    }
    public long getUPC(){
        return _itemUPC;
    }
    //==============================
    public void setPrice(double newSalePrice){
        _baseSalePrice = newSalePrice;
    }
    
    public void setCost(double newCost){
        //coming back for fancier math on this one later
        _costOfGood = newCost;
    }
    
    public void setDescription(String newDescription){
        
        _description = newDescription;
    }
    
    public void setInventory(long newInventory){
        _inventoryQty = newInventory;
    }
    public void setUPC(long newUPC){
        _itemUPC = newUPC;
    }
    
    ///connect to database and make it happen
    public boolean saveProduct() throws SQLException{
        return writeProductToDatabase();
    }
    
    public ResultSet retrieveProduct(long productUPC) throws SQLException{
        return retrieveProductInDatabase(productUPC);
    }
    
    public boolean updateProduct(ResultSet updatedProduct) throws SQLException{
        return updateProductInDatabase(updatedProduct);
    }
    
    private boolean updateProductInDatabase(ResultSet updatedProduct) throws SQLException{
        try{
            
            updatedProduct.updateRow();
            return true;
            
           }catch(SQLException e){
               
                System.out.println(e.getMessage());
                return false;
                
           }
        
    }
    
    private ResultSet retrieveProductInDatabase(long productUPC) throws SQLException{
        
        Connection con = DriverManager.getConnection("jdbc:mysql://173.255.241.100:3306/jagoposDB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=PST","jagoPOS","NHhffClm4iIe6vTk");
        
        String productQueryString = "SELECT * FROM Product WHERE product_UPC="+productUPC+"";
        
        Statement productQuery = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);  
        
        ResultSet productResults = productQuery.executeQuery(productQueryString);
        
        return productResults;

    }
    
    private boolean writeProductToDatabase() throws SQLException{
        boolean uniqueUPC = true;
        long holder;
        
        Connection con = DriverManager.getConnection("jdbc:mysql://173.255.241.100:3306/jagoposDB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=PST","jagoPOS","NHhffClm4iIe6vTk");
        
        String productQueryString = "SELECT * FROM Product ORDER BY product_upc";
        
        Statement productQuery = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);  
        
        ResultSet productResults = productQuery.executeQuery(productQueryString);
        
        while(productResults.next()){
            
            holder = productResults.getLong(1);
            
            if(_itemUPC == holder){
                uniqueUPC=false;
            }
        }
        
        if(uniqueUPC){
            String addProductString = "insert into Product (product_UPC, product_description, product_cost, product_price, inventory_qty) values";
            addProductString += "("+_itemUPC+", '"+_description+"', "+_costOfGood+", "+_baseSalePrice+", "+_inventoryQty+")";
            Statement addProductRequest = con.createStatement();
            addProductRequest.executeUpdate(addProductString);
            con.close();
            return true;
        }else{
            //System.out.println("Duplicate UPC, cannot add file!");
            con.close();
            return false;
        }
    }

}
