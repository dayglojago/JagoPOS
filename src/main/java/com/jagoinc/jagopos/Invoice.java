package com.jagoinc.jagopos;


import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jago
 */
public class Invoice {
    
    private Customer _attachedCustomer;
    private ArrayList<Product> _productArray;
    private ArrayList<Double> _discountArray;
    private final long _invoiceNumber;
    private String _paymentType;
    private double _invoiceSubtotal, _invoiceDiscountTotal, _invoiceTax, _invoiceFinalTotal, _amountTendered, _change, _outstandingBalance;
    private boolean _invoiceClosed;
    private static long _invoiceCounter = 0;
    
    public Invoice(){
        _invoiceCounter++;
        _invoiceNumber = _invoiceCounter;
        _discountArray = new ArrayList();
        _productArray = new ArrayList();        
        _attachedCustomer = null;
        _invoiceClosed = false;
        _invoiceSubtotal = 0;
        _invoiceDiscountTotal = 0;
        _invoiceTax = 0;
        _invoiceFinalTotal = 0;
        _amountTendered = 0;
        _change = 0;
    }
    
    public long getInvoiceNumber(){
        return _invoiceNumber;
    }
    
    public double getSubtotal(){
        return _invoiceSubtotal;
    }
    
    public double getDiscountTotal(){
        return _invoiceDiscountTotal;
    }
    
    public double getTax(){
        return _invoiceTax;
    }
    public double getTotal(){
        return _invoiceFinalTotal;
    }
    public double getAmountTendered(){
        return _amountTendered;
    }
    public double getChange(){
        return _change;
    }
    
    public void attachNewCustomer(Customer newCustomer){
        _attachedCustomer = newCustomer;
    }
    //=================================
    
    
    private void recalculateInvoiceSubtotal(){
        
    }
}
