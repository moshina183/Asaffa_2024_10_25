package com.asf.van.controller;

import java.util.List;
import java.util.Map;

import com.asf.van.dao.OrderDao;
import com.asf.van.dao.PrintInvoiceDAO;
import com.asf.van.dao.VanDao;
import com.asf.van.exception.VanSalesException;
import com.asf.van.model.Customer;
import com.asf.van.model.CustomerSite;
import com.asf.van.model.Item;
import com.asf.van.model.Organization;
import com.asf.van.model.PaymentMethod;
import com.asf.van.model.RentalKnockOff;
import com.asf.van.model.Status;
import com.asf.van.model.SubInvLocation;
import com.asf.van.model.SubInventory;
import com.asf.van.model.TransType;
import com.asf.van.model.TransactionType;
import com.asf.van.model.VatInfo;
import com.asf.van.model.VehicleDetails;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({ "/rest-api" })
public class VANController {

    private static final Logger logger = Logger.getLogger(VANController.class);
    @Autowired
    private VanDao vanDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private PrintInvoiceDAO printInvoiceDAO;

    @PostMapping(value = "/organizations")

    public List<Organization> getOrganizations() {
        logger.info("/rest-api/organizations invoked");
        List<Organization> organizations = null;
        try {
            organizations = this.vanDao.getOrganizations();
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/organizations finished");
        return organizations;
    }

    /**
     * To get Invoice Type in Invoice Entry Form
     *
     * @return
     */
    @PostMapping(value = "/invTransactionTypes")
    public List<TransactionType> getInvTransactionTypes() {
        logger.info("/rest-api/invTransactionTypes Invoked");
        List<TransactionType> transactionTypes = null;
        try {
            transactionTypes = this.vanDao.getInvTransactionTypes();
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/invTransactionTypes finished");
        return transactionTypes;
    }

    @PostMapping(value = "/transactionTypes")
    public List<TransactionType> getTransactionTypes() {
        logger.info("/rest-api/transactionTypes invoked");
        List<TransactionType> transactionTypes = null;
        try {
            transactionTypes = this.vanDao.getTransactionTypes();
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/transactionTypes finished");
        return transactionTypes;
    }

    @PostMapping(value = "/orderTransactionTypes")
    public List<TransactionType> getOrderTransactionTypes(@RequestParam String trans) {
        logger.info("/rest-api/transactionTypes invoked");
        List<TransactionType> transactionTypes = null;
        try {
            transactionTypes = this.vanDao.getTransactionTypes(trans);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/transactionTypes finished");
        return transactionTypes;
    }

    /**
     * To get Customer name in invoice entry
     *
     * @param userName
     * @return
     */
    @PostMapping(value = "/customers")
    public List<Customer> getCustomers(@RequestParam String userName) {
        logger.info("/rest-api/customers invoked");
        List<Customer> customers = null;
        try {
            customers = this.vanDao.getCustomers(userName);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/customers finished");
        return customers;
    }

    @PostMapping(value = "/customerSites")

    public List<CustomerSite> getCustomerSites(@RequestParam String customerId, @RequestParam String userName) {
        logger.info("/rest-api/customerSites invoked");
        List<CustomerSite> customersites = null;
        try {
            customersites = this.vanDao.getCustomerSites(customerId, userName);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/customersites finished");
        return customersites;
    }

    @PostMapping(value = "/transTypes")

    public List<TransType> getTransTypes() {
        logger.info("/rest-api/transTypes invoked");
        List<TransType> transTypes = null;
        try {
            transTypes = this.vanDao.getTransTypes();
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/transTypes finished");
        return transTypes;
    }

    @PostMapping(value = "/paymentMethod")
    public PaymentMethod getPaymentMethod(@RequestParam int locationId) {
        logger.info("/rest-api/paymentMethod invoked");
        PaymentMethod paymentMethod = null;
        try {
            paymentMethod = this.vanDao.getPaymentMethod(locationId);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/paymentMethod finished");
        return paymentMethod;
    }
// Dinesh: Get Vat value post Services 
    // @PostMapping(value = "/vatValue")
    // public VatInfo getVatMethod(@RequestParam String userName, 
    //                             @RequestParam String custName) {
    //     logger.info("/rest-api/vatValue invoked");
    //     VatInfo vatRate = null;
    //     try {
    //         vatRate = this.vanDao.getVatRate(userName,custName);

    //     } catch (VanSalesException exp) {
    //         logger.info(" error while getting bytes" + exp.getMessage());
    //     } catch (Exception exp) {
    //         logger.info(" error while getting bytes" + exp.getMessage());
    //     }
    //     logger.info("/rest-api/vatValue finished");
    //     return vatRate;
    // }


    @PostMapping(value = "/OrderToInventory/subInventory")

    public List<SubInventory> getSubinventory(@RequestParam String organizationId) {
        logger.info("/rest-api/OrderToVehicle/subInventory invoked");
        List<SubInventory> subInvs = null;
        try {
            subInvs = this.vanDao.getSubInventory(organizationId);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/OrderToVehicle/subInventory finished");
        return subInvs;
    }

    @PostMapping(value = "/OrderToInventory/subInvLocations")

    public List<SubInvLocation> getSubinventoryLocation(@RequestParam String subInvCode,
            @RequestParam String organizationId) {
        logger.info("/rest-api/OrderToVehicle/subInvLocations invoked");
        List<SubInvLocation> subInvLocs = null;
        try {
            subInvLocs = this.vanDao.getSubInventoryLocations(subInvCode, organizationId);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/OrderToVehicle/subInvLocations finished");
        return subInvLocs;
    }

    /**
     * To get details in "Material Request from Stores"
     *
     * @param userName
     * @return
     */
    @PostMapping(value = "/OrderToVehicle/items")
    public List<Item> getItems(@RequestParam String userName) {
        logger.info("/rest-api/OrderToVehicle/items invoked");
        List<Item> items = null;
        try {
            items = this.vanDao.getItems(userName);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/OrderToVehicle/items finished");
        return items;
    }

    @PostMapping(value = "/PrintOnMaterial/orders")
    public Status getPrintOnMaterialRequest(@RequestParam String orderNumber) {
        logger.info("/rest-api/PrintOnMaterial/orders invoked");
        String printOnMaterialStatus = null;
        Status status = null;
        try {
            printOnMaterialStatus = this.printInvoiceDAO.callPrintOnMaterialRequest(orderNumber);

            status = new Status();
            status.setPrintInvoiceFlag(printOnMaterialStatus);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/PrintOnMaterial/orders finished");
        return status;
    }

    @PostMapping(value = "/OrderReturns/items")
    public List<Item> getOrderReturnsItems(@RequestParam String userName, @RequestParam String customerId,
            @RequestParam String partySiteId, @RequestParam String organizationId) {
        logger.info("/rest-api/OrderReturns/items invoked");
        List<Item> items = null;
        try {
            items = this.vanDao.getOrderItems(userName, customerId, partySiteId, organizationId);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/OrderReturns/items finished");
        return items;
    }

    /**
     * updated on 12-02-2019 - Balaji Manickam - 4i Apps method to return items
     * based on customer name and site
     *
     * @return
     */
    @PostMapping(value = "/OrderCancel/items")
    public List<Item> getOrderCancelItems(@RequestParam String userName, @RequestParam String customerId,
            @RequestParam String partySiteId) {
        logger.info("/rest-api/OrderCancel/items invoked");
        List<Item> items = null;
        try {
            items = this.vanDao.getCancelItems(userName, customerId, partySiteId);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/OrderCancel/items finished");
        return items;
    }

    @PostMapping(value = "/sample")
    public VehicleDetails getItems() {
        logger.info("/rest-api/sample");
        VehicleDetails itemCase = null;
        List<Item> items = null;
        try {
            itemCase = new VehicleDetails();
            items = this.vanDao.getItems("");
            itemCase.setItems(items);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/OrderToVehicle/items finished");
        return itemCase;
    }

    @PostMapping(value = "/OrderReturns/confirmItems")
    public Status putOrderReturnsItems(@RequestBody VehicleDetails customer, @RequestParam String rtnRefNoParam) {
        logger.info("/rest-api/OrderReturns/confirmItems invoked");
        String invoiceNum = null;
        Status status = null;
        System.out.println("rtnRefNoParam: "+rtnRefNoParam);
        customer.setReferenceNo(rtnRefNoParam);
        System.out.println("customer: "+customer.getCustomerName());
        System.out.println("customer.getReferenceNo: "+customer.getReferenceNo());
        try {
            invoiceNum = this.vanDao.getInvoiceSequenceNumber();
            status = this.orderDao.callStoredProcedure(customer, invoiceNum, customer.getTransType());

            status.setInvoiceNum(invoiceNum);

            if ((customer.getTransType().equalsIgnoreCase("MOVE_ORDER"))
                    || (customer.getTransType().equalsIgnoreCase("MOVE_ORDERINV"))) {

                this.orderDao.callMoveOrderProcess(invoiceNum, customer.getTransType());
            }

            status.setResult(true);
        } catch (VanSalesException exp) {
            if (status == null) {
                status = new Status();
                status.setInvoiceNum(invoiceNum);
                status.setResult(false);
                status.setStatus("Error while inserting data into Data Store");
            }
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            if (status == null) {
                status = new Status();
                status.setInvoiceNum(invoiceNum);
                status.setResult(false);
                status.setStatus("Error while inserting data into Data Store");
            }
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/OrderReturns/confirmItems finished");
        return status;
    }

    /**
     * updated on 13-02-2018 - Balaji Manickam - 4i apps method to cancel invoice
     *
     * @param invoiceNum
     */
    @PostMapping(value = "/OrderReturns/cancelItems")
    public void putOrderCancelItems(@RequestParam String invoiceNum) {
        logger.info("/rest-api/OrderReturns/cancelItems invoked");
        try {
            this.orderDao.callCancelOrderProcess(invoiceNum);
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/OrderReturns/cancelItems finished");
    }

    /**
     * updated on 19-02-2018 - Balaji Manickam - 4i apps method to check limit
     *
     * @param vehicleNo
     * @return
     */
    @PostMapping(value = "/OrderReturns/cancellation-limit")
    public boolean getCancellationLimit(@RequestParam String vehicleNo) {
        logger.info("/rest-api/OrderReturns/cancellation-limit invoked");
        Item item = new Item();
        try {
            item = this.vanDao.getCancellationLimit(vehicleNo);
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/OrderReturns/cancellation-limit finished");
        return item.getStatus();
    }

    /**
     * Method for Material Confirmation from stores
     *
     * @param customer
     * @return
     */
    @PostMapping(value = "/OrderToVehicle/confirmItems")
    public Status putItems(@RequestBody VehicleDetails customer) {
        logger.info("/rest-api/OrderToVehicle/confirmItems invoked");
        long startTime = System.currentTimeMillis();
        String invoiceNum = null;
        Status status = null;
        String orderNumber = null;
        try {
            invoiceNum = this.vanDao.getInvoiceSequenceNumber();
            status = this.orderDao.callStoredProcedure(customer, invoiceNum, customer.getTransType());
            long endTime = System.currentTimeMillis();
            System.out.println("total time taken for proc execution :: " + (endTime - startTime) + " ms");
            status.setInvoiceNum(invoiceNum);
            if ((customer.getTransType().equalsIgnoreCase("MOVE_ORDER"))
                    || (customer.getTransType().equalsIgnoreCase("MOVE_ORDERINV"))) {
                this.orderDao.callMoveOrderProcess(invoiceNum, customer.getTransType());
            }
            orderNumber = this.vanDao.getOrderNumber(invoiceNum);
            status.setOrderNumber(orderNumber);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/OrderToVehicle/confirmItems finished");
        return status;
    }

    @PostMapping(value = "/SalesEntry/items")

    public List<Item> getSalesEntryItems(@RequestParam String userName, @RequestParam String customerId,
            @RequestParam String partySiteId, 
            @RequestParam String organizationId) {
        logger.info("/rest-api/SalesEntry/items invoked");
        List<Item> items = null;
        try {
            items = this.vanDao.getSalesEntryItems(userName, customerId, partySiteId, organizationId);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/SalesEntry/items finished");
        return items;
    }

    @PostMapping(value = "/SalesEntry/item/check/discount")
    public Boolean checkSalesEntryItemDisc(@RequestParam String customerId, @RequestParam String partySiteId,
            @RequestParam String itemId, @RequestParam String organizationId) {
        logger.info("/rest-api/SalesEntry/check/discount invoked");
        Boolean isDiscounted = null;
        try {
            isDiscounted = this.vanDao.checkSalesEntryItemDisc(customerId, partySiteId, itemId, organizationId);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/SalesEntry/check/discount finished");
        return isDiscounted;
    }

    @PostMapping(value = "/SalesEntry/confirmItems")

    public Status confirmSalesItems(@RequestBody VehicleDetails customer, @RequestParam String manualInvoiceCreate,
            @RequestParam String manualInvoiceCreateDate, @RequestParam String refNoParam) {
        logger.info("/rest-api/SalesEntry/confirmItems invoked");
        String invoiceNum = null;
        Status status = null;
        String orderNumber = null;
        String invoiceCreateDate = null;
        long startTime = System.currentTimeMillis();
        
System.out.println("RefNoParam: "+refNoParam);
customer.setReferenceNo(refNoParam);
System.out.println("customer: "+customer.getCustomerName());
System.out.println("customer.getReferenceNo: "+customer.getReferenceNo());
        try {
            if ((manualInvoiceCreate != null) && (!manualInvoiceCreate.isEmpty())) {
                invoiceNum = "M" + manualInvoiceCreate;
                invoiceCreateDate = manualInvoiceCreateDate;
            } else {
                invoiceNum = this.vanDao.getInvoiceSequenceNumber();
            }
            status = this.orderDao.salesCallStoredProcedure(customer, invoiceNum, customer.getTransType(),
                    invoiceCreateDate);

            long endTime = System.currentTimeMillis();
            System.out.println(" In Van Controller");
            System.out.println("total time taken for proc execution :: " + (endTime - startTime) + " ms");

            if ((customer.getTransType().equalsIgnoreCase("MOVE_ORDER"))
                    || (customer.getTransType().equalsIgnoreCase("MOVE_ORDERINV"))) {
                this.orderDao.callMoveOrderProcess(invoiceNum, "MOVE_ORDER");
                orderNumber = this.vanDao.getOrderNumber(invoiceNum);
                status.setOrderNumber(orderNumber);
            }

            status.setInvoiceNum(invoiceNum);
            status.setResult(true);
        } catch (VanSalesException exp) {
            if (status == null) {
                status = new Status();
                status.setInvoiceNum(invoiceNum);
                status.setResult(false);
                status.setStatus("Error while inserting data into Data Store");
            }
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            if (status == null) {
                status = new Status();
                status.setResult(false);
                status.setInvoiceNum(invoiceNum);
                status.setStatus("Error while inserting data into Data Store");
            }
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/SalesEntry/confirmItems finished");
        return status;
    }
//Credit limit req 2
    @PostMapping(value = "/SalesEntry/ReviewItems")
    public Status reviewItems(@RequestBody VehicleDetails customer, @RequestParam String manualInvoiceCreate,
            @RequestParam String manualInvoiceCreateDate, @RequestParam String refNoParam) {
        logger.info("/rest-api/SalesEntry/ReviewItems invoked");
        String invoiceNum = null;
        Status status = null;
        String invoiceCreateDate = null;
        String creditBalance = null;
        float totalAmount = 0;
        long startTime = System.currentTimeMillis();
        System.out.println("RefNoParam: "+refNoParam);
        customer.setReferenceNo(refNoParam);
        System.out.println("customer: "+customer.getCustomerName());
        System.out.println("customer.getReferenceNo: "+customer.getReferenceNo());
        for(Item item:customer.getItems()){
            totalAmount = totalAmount + (Float.valueOf(item.getQuantity()) * 
                            (item.getPrice()+item.getPrice()*(item.getPerVATRate())/100));
        }
        try {
            creditBalance = vanDao.getCreditBalance(customer.getCustAccountId());
        } catch (VanSalesException e) {
            logger.info("Error while getting creditBalance" + e.getMessage());
            e.printStackTrace();
        }
        try {
            if ((manualInvoiceCreate != null) && (!manualInvoiceCreate.isEmpty())) {
                invoiceNum = "M" + manualInvoiceCreate;
                invoiceCreateDate = manualInvoiceCreateDate;
            } else {
                invoiceNum = this.vanDao.getReviewInvoiceSequenceNumber();
            }
            System.out.println("Invoice creation date " + invoiceCreateDate);
            System.out.println("ManualInvoice Num " + invoiceNum);
            System.out.println("totalAmountBE"+totalAmount);
            System.out.println("creditBalancebEF"+creditBalance);
            this.vanDao.deleteExistInvoiceReview(invoiceNum);
            if((creditBalance!= null && totalAmount <= Float.parseFloat(creditBalance) )
            || customer.getPaymentMethod().equalsIgnoreCase("CASH")){
                System.out.println("inside");
                status = this.orderDao.reviewCallStoredProcedure(customer, invoiceNum, customer.getTransType(),
                invoiceCreateDate);
                status.setBalanceFlag(true);
                System.out.println("Status" +status);
            }
            long endTime = System.currentTimeMillis();
            System.out.println(" In Van Controller");
            System.out.println("total time taken for proc execution :: " + (endTime - startTime) + " ms");
            status.setInvoiceNum(invoiceNum);
            status.setResult(true);
            status.setPaymentMethod(customer.getPaymentMethod());
        } catch (VanSalesException exp) {
            if (status == null) {
                status = new Status();
                status.setInvoiceNum(invoiceNum);
                status.setPaymentMethod(customer.getPaymentMethod());
                status.setCreditBalance(creditBalance);
                status.setResult(false);
                status.setStatus("Error while inserting data into Data Store");
            }
            logger.info("HIOL error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            if (status == null) {
                status = new Status();
                status.setResult(false);
                status.setPaymentMethod(customer.getPaymentMethod());
                status.setCreditBalance(creditBalance);
                status.setInvoiceNum(invoiceNum);
                status.setStatus("Error while inserting data into Data Store");
            }
            logger.info("SEC error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/SalesEntry/ReviewItems finished");
        return status;
    }

    @PostMapping(value = "/MoveOrder/confirmtems")

    public VehicleDetails getConfirmedItems(@RequestParam String userName, @RequestParam String invoiceNum,
            @RequestParam String orderType, @RequestParam String moveRefNoParam) {
        logger.info("/rest-api/MoveOrder/confirmtems invoked");
        VehicleDetails vehicleDetails = null;
        try {
            vehicleDetails = this.vanDao.getConfirmedItems(userName, invoiceNum, orderType);
            vehicleDetails.setReferenceNo(moveRefNoParam);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/MoveOrder/confirmtems finished");
        return vehicleDetails;
    }

    @PostMapping(value = "/MoveOrder/reviewitems")
    public VehicleDetails getReviewedItems(@RequestParam String userName, @RequestParam String invoiceNum,
                                        @RequestParam String moveRefNoParam) {
        logger.info("/rest-api/MoveOrder/reviewitems invoked");
        VehicleDetails vehicleDetails = null;
        //vehicleDetails.setReferenceNo(moveRefNoParam);
        System.out.println("moveRefNoParam: "+moveRefNoParam);
        try {
            vehicleDetails = this.vanDao.getReviewedItems(userName, invoiceNum);
            vehicleDetails.setReferenceNo(moveRefNoParam);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/MoveOrder/reviewitems finished");
        
        return vehicleDetails;
    }

    @PostMapping(value = "/MoveOrder/cancelItems")
    public Status cancelItems(@RequestParam String invoiceNumber, @RequestParam String orderType) {
        logger.info("/rest-api/MoveOrder/cancelItems invoked");
        Status status = new Status();
        try {
            status = this.orderDao.cancelItems(invoiceNumber, orderType);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/MoveOrder/cancelItems finished");
        return status;
    }

    /**
     * To get data in "On hand quantity"
     *
     * @param userName
     * @return
     */
    @PostMapping(value = "/OrderToInventory/items")
    public List<Item> getOrdertoInventoryItems(@RequestParam String userName) {
        logger.info("/rest-api/OrderToInventory/items invoked");
        List<Item> items = null;
        try {
            items = this.vanDao.getOrderToInventoryItems(userName);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/OrderToInventory/items finished");
        return items;
    }

    @PostMapping(value = "/PrintInvoice/items")
    public Status getPrintInvoiceStatus(@RequestParam String invoiceType, @RequestParam String userName,
            @RequestParam String invoiceNum) {
        logger.info("/rest-api/PrintInvoice/items invoked");
        String printInvoiceStatus = null;
        String receiptNum = null;
        Status status = null;
        try {
            printInvoiceStatus = this.printInvoiceDAO.callPrintInvoiceStatus(userName, invoiceNum, invoiceType);

            status = new Status();
            status.setPrintInvoiceFlag(printInvoiceStatus);
            receiptNum = invoiceNum;
            this.vanDao.updateReceiptNum(receiptNum, invoiceNum);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/PrintInvoice/items finished");
        return status;
    }

    @PostMapping(value = "/SalesInvoiceReceipt/updateCustomer")

    public Status updateCustomer(@RequestParam String chequeNum, @RequestParam String chequeAmt,
            @RequestParam String invoiceNumber) {
        logger.info("/rest-api/SalesInvoiceReceipt/updateCustomer invoked");

        boolean result = this.vanDao.updateCustomer(chequeNum, chequeAmt, invoiceNumber);
        logger.info("/rest-api/SalesInvoiceReceipt/updateCustomer finished" + result);
        Status status = new Status();
        status.setResult(result);
        return status;
    }

    @PostMapping(value = "/rentalKnockOff")
    public List<RentalKnockOff> getRentalKnockOff() {
        logger.info("/rest-api/rentalKnockOff Invoked");
        List<RentalKnockOff> rentalKnockOff = null;
        try {
            rentalKnockOff = this.vanDao.getRentalKnockOff();
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/rentalKnockOff finished");
        return rentalKnockOff;
    }

    @PostMapping(value = "/PrintVanSalesReport/items")

    public Status getPrintVanSalesReport(@RequestParam String userName, @RequestParam String vanSalesCreateDate) {
        logger.info("/rest-api/PrintVanSalesReport/items invoked");
        String printInvoiceStatus = null;
        Status status = null;
        try {
            printInvoiceStatus = this.printInvoiceDAO.callPrintVanSalesReport(userName, vanSalesCreateDate);

            status = new Status();
            status.setPrintInvoiceFlag(printInvoiceStatus);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/PrintVanSalesReport/items finished");
        return status;
    }

    @PostMapping(value = "SalesInvoiceBrowser/items")
    public List<Item> getSalesInvoiceBrowser(@RequestParam String invoiceType, @RequestParam String userName,
            @RequestParam String loadInvoiceData) {
        logger.info("/rest-api/SalesInvoiceBrowser/items invoked");
        List<Item> items = null;
        try {
            items = this.vanDao.getSalesInvoiceBrowser(invoiceType, userName, loadInvoiceData);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/SalesInvoiceBrowser/items finished");
        return items;
    }

    /**
     * updated on 13-02-2018 - Balaji Manickam - 4i apps method to return cancelled
     * items on current date
     *
     * @param userName
     * @return
     */
    @PostMapping(value = "SalesInvoiceBrowser/cancelledItems")
    public List<Item> getSalesInvoiceBrowserCancelledItems(@RequestParam String userName,
            @RequestParam String loadInvoiceDate) {
        logger.info("/rest-api/SalesInvoiceBrowser/cancelledItems invoked");
        List<Item> items = null;
        try {
            items = this.vanDao.getSalesInvoiceBrowserCancelledItems(userName, loadInvoiceDate);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/SalesInvoiceBrowser/cancelledItems finished");
        return items;
    }

    @PostMapping(value = "SalesInvoiceBrowser/moveOrder")
    public List<Map<String, Object>> getSalesInvoiceBrowserMoveOrders(@RequestParam String menuType,
            @RequestParam String vehicleNo, @RequestParam String orderReqDate) {
        logger.info("/rest-api/SalesInvoiceBrowser/moveOrder invoked");
        List<Map<String, Object>> orders = null;
        try {
            orders = this.vanDao.getSalesInvoiceBrowserMoveOrders(menuType, vehicleNo, orderReqDate);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/SalesInvoiceBrowser/moveOrder finished");
        return orders;
    }

    @PostMapping(value = "SalesInvoiceBrowser/moveOrder/items")
    public List<Map<String, Object>> getSalesInvoiceBrowserMoveOrderItems(@RequestParam String orderNo) {
        logger.info("/rest-api/SalesInvoiceBrowser/moveOrderItems invoked");
        List<Map<String, Object>> items = null;
        try {
            items = this.vanDao.getSalesInvoiceBrowserMoveOrderItems(orderNo);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/SalesInvoiceBrowser/moveOrderItems finished");
        return items;
    }

    @PostMapping(value = "/PrintOnHandsReport/items")
    public Status getPrintOnHandsReport(@RequestParam String userName) {
        logger.info("/rest-api/PrintOnHandsReport/items invoked");
        String printOnHandsStatus = null;
        Status status = null;
        try {
            printOnHandsStatus = this.printInvoiceDAO.callPrintOnHandStatus(userName);

            status = new Status();
            status.setPrintInvoiceFlag(printOnHandsStatus);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/PrintOnHandsReport/items finished");
        return status;
    }

    @PostMapping(value = "/PrintOrderReturn/items")

    public Status getPrintOrderReturnStatus(@RequestParam String userName, @RequestParam String invoiceNum) {
        logger.info("/rest-api/PrintOrderReturn/items invoked");
        String printInvoiceStatus = null;
        String receiptNum = null;
        Status status = null;
        try {
            printInvoiceStatus = this.printInvoiceDAO.callPrintOrderReturnStatus(userName, invoiceNum);

            status = new Status();
            status.setPrintInvoiceFlag(printInvoiceStatus);
            receiptNum = invoiceNum;
            this.vanDao.updateReceiptNum(receiptNum, invoiceNum);
        } catch (VanSalesException exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        } catch (Exception exp) {
            logger.info(" error while getting bytes" + exp.getMessage());
        }
        logger.info("/rest-api/PrintOrderReturn/items finished");
        return status;
    }

    @PostMapping(value = "/SalesEntry/invoiceNo")
    public boolean checkManualInvoiceExist(@RequestParam String invoiceNum) {
        logger.info("/rest-api/SalesEntry/invoiceNo invoked");
        boolean result = true;
        try {
            result = this.vanDao.checkInvoiceExist(invoiceNum);
        } catch (Exception exp) {
            logger.info("error while getting invoice number" + exp.getMessage());
        }
        logger.info("/rest-api/SalesEntry/invoiceNo finished");
        return result;
    }

    @PostMapping(value = "/SalesEntry/referencNo")
    public boolean checkReferenceNoExist(@RequestParam String referenceNum) {
        logger.info("/rest-api/SalesEntry/referencNo invoked");
        boolean result = true;
        try {
            result = this.vanDao.checkRefNoExist(referenceNum);
        } catch (Exception exp) {
            logger.info("error while getting referencNo number" + exp.getMessage());
        }
        logger.info("/rest-api/SalesEntry/referencNo finished");
        return result;
    }






}
