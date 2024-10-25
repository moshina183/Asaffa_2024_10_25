package com.asf.van.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.asf.van.exception.VanSalesException;
import com.asf.van.model.Item;
import com.asf.van.model.Status;
import com.asf.van.model.VehicleDetails;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

@Service("orderDao")
public class OrderDao {

    private static final Logger LOGGER = Logger.getLogger(OrderDao.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Status callStoredProcedure(VehicleDetails customer, String invoiceId, String orderType)
            throws VanSalesException {
        Status status = new Status();
        long totProcStartTime = System.currentTimeMillis();
        Map<String, Object> inParamMap = null;
        SimpleJdbcCall simpleJdbcCall = null;
        System.out.println("insertdetailsreturns: " +customer.getReferenceNo());
        try {
            simpleJdbcCall = new SimpleJdbcCall(this.jdbcTemplate).withCatalogName("XXASF_VANSALES_PACKAGE").withProcedureName("insertdetailsreturns");

            String orderCode = null;
            String subInvFrom = null;
            String subInvTo = null;
            if (orderType.equalsIgnoreCase("MOVE_ORDER")) {
                orderCode = "MOVE_ORDER";
                subInvTo = customer.getUserName();
            } else {
                orderCode = orderType;
            }
            inParamMap = new HashMap();
            inParamMap.put("P_CUST_ID", customer.getCustomerName());
            inParamMap.put("P_TRANS_TYPE", customer.getTransactionType());
            inParamMap.put("P_INV_NUM", invoiceId);
            inParamMap.put("P_REF_NUM", customer.getReferenceNo());
            inParamMap.put("P_CUST_SITE", customer.getCustomerSiteId());
            inParamMap.put("P_PMT_METHOD", customer.getPaymentMethod());
            inParamMap.put("P_ORDER_CODE", orderCode);
            inParamMap.put("P_ORG_ID", Integer.valueOf(customer.getOrgId()));
            inParamMap.put("P_TRANS_CURR", "OMR");
            inParamMap.put("P_CUST_ACCOUNT_ID", customer.getCustAccountId());
            inParamMap.put("P_ORDER_TYPE_ID", customer.getTransactionTypeId());
            inParamMap.put("P_PROCESS_FLAG", Character.valueOf('N'));
            inParamMap.put("P_SUBINV_FROM", subInvFrom);
            inParamMap.put("P_SUBINV_TO", subInvTo);
            inParamMap.put("P_LOC_ID", null);
            
            for (Item listItem : customer.getItems()) {
                long startTime = System.currentTimeMillis();
                inParamMap.put("P_HEADER_ID", Long.valueOf(listItem.getHeaderId()));
                inParamMap.put("P_SOLD_QTY", listItem.getQuantity());
                inParamMap.put("P_FOC_FLAG", listItem.getFocFlag());
                inParamMap.put("P_PRICE", Float.valueOf(listItem.getPrice()));
                inParamMap.put("P_VAN_TAX_RATE", Float.valueOf(listItem.getPerVATRate())); 
                
                SqlParameterSource in = new MapSqlParameterSource(inParamMap);
                Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);
                String result = (String) simpleJdbcCallResult.get("P_STATUS");
                long endTime = System.currentTimeMillis();
                System.out.println("time taken for proc execution for item :: " + listItem.getItemCode() + "  ::" + (endTime - startTime) + " ms");
                status.setStatus(result);
            }

            long totProcEndTime = System.currentTimeMillis();
            System.out.println("total time taken for proc execution for all item :: " + (totProcEndTime - totProcStartTime) + " ms");
        } catch (Exception exp) {
            Set<String> inParams = inParamMap.keySet();
            StringBuilder inParm = new StringBuilder();
            for (String key : inParams) {
                inParm.append(key + " Value =" + inParamMap.get(key) + "\n");
            }
            LOGGER.error("IN PARAMS OF " + simpleJdbcCall.getProcedureName() + " " + inParm.toString());
            LOGGER.error(exp.getMessage());
            throw new VanSalesException(exp.getMessage());
        }
        return status;
    }

//    public Status reviewCallStoredProcedure(VehicleDetails customer, String invoiceNum, String orderType, String invoiceCreateDate)
//            throws VanSalesException {
//        Status status = new Status();
//        long totProcStartTime = System.currentTimeMillis();
//        Map<String, Object> inParamMap = null;
//        SimpleJdbcCall simpleJdbcCall = null;
//        try {
//            simpleJdbcCall = new SimpleJdbcCall(this.jdbcTemplate).withCatalogName("XXASF_VANSALES_PACKAGE").withProcedureName("ReviewInvoiceInsert");
//            String orderCode = orderType;
//            String subInvFrom = null;
//            String subInvTo = null;
//            inParamMap = new HashMap();
//            if (orderType.equalsIgnoreCase("MOVE_ORDERINV")) {
//                orderCode = "MOVE_ORDER";
//                subInvFrom = customer.getUserName();
//                subInvTo = customer.getSubInvCode();
//            } else {
//                orderCode = orderType;
//                // Always Passing current date
//                inParamMap.put("P_DATE", null);
//                inParamMap.put("P_INVOICE_DATE", null);
////                inParamMap.put("P_DATE", invoiceCreateDate);
//            }
//
//            inParamMap.put("P_CUST_ID", customer.getCustomerName());
//            inParamMap.put("P_TRANS_TYPE", customer.getTransactionType());
//            inParamMap.put("P_INV_NUM", invoiceNum);
//            inParamMap.put("P_CUST_SITE", customer.getCustomerSiteId());
//            inParamMap.put("P_PMT_METHOD", customer.getPaymentMethod());
//            inParamMap.put("P_ORDER_CODE", orderCode);
//            inParamMap.put("P_ORG_ID", customer.getOrgId());
//            inParamMap.put("P_TRANS_CURR", "OMR");
//            inParamMap.put("P_CUST_ACCOUNT_ID", customer.getCustAccountId());
//            inParamMap.put("P_ORDER_TYPE_ID", customer.getTransactionTypeId());
//            inParamMap.put("P_FOC_FLAG", null);
//            inParamMap.put("P_PROCESS_FLAG", 'N');
//            inParamMap.put("P_SUBINV_FROM", subInvFrom);
//            inParamMap.put("P_SUBINV_TO", subInvTo);
//            inParamMap.put("P_LOC_ID", customer.getLocationId());
//            inParamMap.put("P_REASON_CODE", customer.getRentalKnockOff());
//            for (Item listItem : customer.getItems()) {
//                long startTime = System.currentTimeMillis();
//                inParamMap.put("P_HEADER_ID", listItem.getHeaderId());
//                inParamMap.put("P_SOLD_QTY", listItem.getQuantity());
//                inParamMap.put("P_PRICE", listItem.getPrice());
//                SqlParameterSource in = new MapSqlParameterSource(inParamMap);
//                Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);
//                String result = (String) simpleJdbcCallResult.get("P_STATUS");
//                long endTime = System.currentTimeMillis();
//                System.out.println("time taken for proc execution for item :: " + listItem.getItemCode() + "  ::" + (endTime - startTime) + " ms");
//                status.setStatus(result);
//            }
//            long totProcEndTime = System.currentTimeMillis();
//            System.out.println("total time taken for proc execution for all item :: " + (totProcEndTime - totProcStartTime) + " ms");
//        } catch (Exception exp) {
//            Set<String> inParams = inParamMap.keySet();
//            StringBuilder inParm = new StringBuilder();
//            for (String key : inParams) {
//                inParm.append("\n" + key + " = " + inParamMap.get(key));
//            }
//            LOGGER.error("IN PARAMS OF " + simpleJdbcCall.getProcedureName() + " " + inParm.toString());
//            LOGGER.error(exp.getMessage());
//            throw new VanSalesException(exp.getMessage());
//        }
//        return status;
//    }
    public Status reviewCallStoredProcedure(VehicleDetails customer, String invoiceNum, String orderType, String invoiceCreateDate)
            throws VanSalesException {
        System.out.println("ReviewInvoiceInsert: " +customer.getReferenceNo());
        Status status = new Status();
        long totProcStartTime = System.currentTimeMillis();
        Map<String, Object> inParamMap = null;
        SimpleJdbcCall simpleJdbcCall = null;
        try {
            simpleJdbcCall = new SimpleJdbcCall(this.jdbcTemplate).withCatalogName("XXASF_VANSALES_PACKAGE").withProcedureName("ReviewInvoiceInsert");
            String orderCode = orderType;
            String subInvFrom = null;
            String subInvTo = null;
            inParamMap = new HashMap();
            if (orderType.equalsIgnoreCase("MOVE_ORDERINV")) {
                orderCode = "MOVE_ORDER";
                subInvFrom = customer.getUserName();
                subInvTo = customer.getSubInvCode();
            } else {
                orderCode = orderType;
                // Always Passing current date
                inParamMap.put("P_DATE", null);
                inParamMap.put("P_INVOICE_DATE", null);
//                inParamMap.put("P_DATE", invoiceCreateDate);
            }

//            ObjectMapper mapper = new ObjectMapper();
//            System.out.println(mapper.readValue(customer.toString(), VehicleDetails.class));
//            mapper.

            inParamMap.put("P_CUST_ID", customer.getCustomerName());
            inParamMap.put("P_TRANS_TYPE", customer.getTransactionType());
            inParamMap.put("P_INV_NUM", invoiceNum);
            inParamMap.put("P_REF_NUM", customer.getReferenceNo());
            inParamMap.put("P_CUST_SITE", customer.getCustomerSiteId());
            inParamMap.put("P_PMT_METHOD", customer.getPaymentMethod());
            inParamMap.put("P_ORDER_CODE", orderCode);
            inParamMap.put("P_ORG_ID", customer.getOrgId());
            inParamMap.put("P_TRANS_CURR", "OMR");
            inParamMap.put("P_CUST_ACCOUNT_ID", customer.getCustAccountId());
            inParamMap.put("P_ORDER_TYPE_ID", customer.getTransactionTypeId());
            inParamMap.put("P_FOC_FLAG", null);
            inParamMap.put("P_PROCESS_FLAG", 'N');
            inParamMap.put("P_SUBINV_FROM", subInvFrom);
            inParamMap.put("P_SUBINV_TO", subInvTo);
            inParamMap.put("P_LOC_ID", customer.getLocationId());
            inParamMap.put("P_REASON_CODE", customer.getRentalKnockOff());
            
            
            for (Item listItem : customer.getItems()) {
                long startTime = System.currentTimeMillis();
                inParamMap.put("P_HEADER_ID", listItem.getHeaderId());
                inParamMap.put("P_SOLD_QTY", listItem.getQuantity());
                inParamMap.put("P_PRICE", listItem.getPrice());
                inParamMap.put("P_VAN_TAX_RATE", listItem.getPerVATRate());

                SqlParameterSource in = new MapSqlParameterSource(inParamMap);
                Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);
                String result = (String) simpleJdbcCallResult.get("P_STATUS");
                long endTime = System.currentTimeMillis();
                System.out.println("time taken for proc execution for item :: " + listItem.getItemCode() + "  ::" + (endTime - startTime) + " ms");
                status.setStatus(result);
            }
            long totProcEndTime = System.currentTimeMillis();
            System.out.println("total time taken for proc execution for all item :: " + (totProcEndTime - totProcStartTime) + " ms");
        } catch (Exception exp) {
            Set<String> inParams = inParamMap.keySet();
            StringBuilder inParm = new StringBuilder();
            for (String key : inParams) {
                inParm.append("\n" + key + " = " + inParamMap.get(key));
            }
            LOGGER.error("IN PARAMS OF " + simpleJdbcCall.getProcedureName() + " " + inParm.toString());
            LOGGER.error(exp.getMessage());
            throw new VanSalesException(exp.getMessage());
        }
        return status;
    }

    public Status salesCallStoredProcedure(VehicleDetails customer, String invoiceNum, String orderType, String invoiceCreateDate)
            throws VanSalesException {
        System.out.println("InvoiceInsertDetails: " +customer.getReferenceNo());
        Status status = new Status();
        long totProcStartTime = System.currentTimeMillis();
        Map<String, Object> inParamMap = null;
        SimpleJdbcCall simpleJdbcCall = null;
        try {
            simpleJdbcCall = new SimpleJdbcCall(this.jdbcTemplate).withCatalogName("XXASF_VANSALES_PACKAGE").withProcedureName("InvoiceInsertDetails");
            String orderCode = orderType;
            String subInvFrom = null;
            String subInvTo = null;
            inParamMap = new HashMap();
            if (orderType.equalsIgnoreCase("MOVE_ORDERINV")) {
                orderCode = "MOVE_ORDER";
                subInvFrom = customer.getUserName();
                subInvTo = customer.getSubInvCode();
            } else {
                orderCode = orderType;
                // Always Passing current date
                inParamMap.put("P_DATE", null);
                inParamMap.put("P_INVOICE_DATE", null);
//                inParamMap.put("P_DATE", invoiceCreateDate);
            }

            inParamMap.put("P_CUST_ID", customer.getCustomerName());
            inParamMap.put("P_TRANS_TYPE", customer.getTransactionType());
            inParamMap.put("P_INV_NUM", invoiceNum);
            inParamMap.put("P_REF_NUM", customer.getReferenceNo());
            inParamMap.put("P_CUST_SITE", customer.getCustomerSiteId());
            inParamMap.put("P_PMT_METHOD", customer.getPaymentMethod());
            inParamMap.put("P_ORDER_CODE", orderCode);
            inParamMap.put("P_ORG_ID", Integer.valueOf(customer.getOrgId()));
            inParamMap.put("P_TRANS_CURR", "OMR");
            inParamMap.put("P_CUST_ACCOUNT_ID", customer.getCustAccountId());
            inParamMap.put("P_ORDER_TYPE_ID", customer.getTransactionTypeId());
            inParamMap.put("P_FOC_FLAG", null);
            inParamMap.put("P_PROCESS_FLAG", Character.valueOf('N'));
            inParamMap.put("P_SUBINV_FROM", subInvFrom);
            inParamMap.put("P_SUBINV_TO", subInvTo);
            inParamMap.put("P_LOC_ID", customer.getLocationId());
            inParamMap.put("P_REASON_CODE", customer.getRentalKnockOff());
            
            for (Item listItem : customer.getItems()) {
                long startTime = System.currentTimeMillis();
                System.out.println("P_HEADER_ID - "+Long.valueOf(listItem.getHeaderId()));
                inParamMap.put("P_HEADER_ID", Long.valueOf(listItem.getHeaderId()));
                inParamMap.put("P_SOLD_QTY", listItem.getQuantity());
                inParamMap.put("P_PRICE", Float.valueOf(listItem.getPrice()));
                inParamMap.put("P_VAN_TAX_RATE", Float.valueOf(listItem.getPerVATRate())); 
                SqlParameterSource in = new MapSqlParameterSource(inParamMap);
                Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);

                String result = (String) simpleJdbcCallResult.get("P_STATUS");
                long endTime = System.currentTimeMillis();
                System.out.println("time taken for proc execution for item :: " + listItem.getItemCode() + "  ::" + (endTime - startTime) + " ms");
                status.setStatus(result);
            }
            long totProcEndTime = System.currentTimeMillis();
            System.out.println("total time taken for proc execution for all item :: " + (totProcEndTime - totProcStartTime) + " ms");
        } catch (Exception exp) {
            Set<String> inParams = inParamMap.keySet();
            StringBuilder inParm = new StringBuilder();
            for (String key : inParams) {
                inParm.append(key + " Value =" + inParamMap.get(key) + "\n");
            }
            LOGGER.error("IN PARAMS OF " + simpleJdbcCall.getProcedureName() + " " + inParm.toString());
            LOGGER.error(exp.getMessage());
            throw new VanSalesException(exp.getMessage());
        }
        return status;
    }

    public void reviewReportCreation(String invoiceId) {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(this.jdbcTemplate).withCatalogName("XXASF_VANSALES_PACKAGE").withProcedureName("asaffa_vansales_invoice_PV");
        Map<String, Object> inParamMap = new HashMap();
        inParamMap.put("P_Invoice_num", invoiceId);
        inParamMap.put("P_vechicle_number", null);
        SqlParameterSource in = new MapSqlParameterSource(inParamMap);
        simpleJdbcCall.execute(in);
    }

    public void callMoveOrderProcess(String invoiceId, String orderType) {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(this.jdbcTemplate).withCatalogName("XXASF_VANSALES_INT_PKG").withProcedureName("CREATE_MSCA_ORDER");
        Map<String, Object> inParamMap = new HashMap();
        inParamMap.put("p_inv_order_num", invoiceId);
        inParamMap.put("p_order_category_code", orderType);

        System.out.println("p_inv_order_num" + invoiceId);
        System.out.println("p_order_category_code" + orderType);

        SqlParameterSource in = new MapSqlParameterSource(inParamMap);
        simpleJdbcCall.execute(in);
    }

    public void callCancelOrderProcess(String invoiceNum) {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(this.jdbcTemplate).withProcedureName("XXASF_VANSALES_D_INV_UPDATE");
        Map<String, Object> inParamMap = new HashMap();
        inParamMap.put("P_INVOICE_NUM", invoiceNum);
        SqlParameterSource in = new MapSqlParameterSource(inParamMap);
        simpleJdbcCall.execute(in);
    }

    public void runPromotions(Item listItem, VehicleDetails customer, String invoiceId, String orderType) {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(this.jdbcTemplate).withCatalogName("XXASF_VANSALES_PACKAGE").withProcedureName("asf_vansales_promo");
        String orderCode = null;
        String subInvFrom = null;
        String subInvTo = null;
        if (orderType.equalsIgnoreCase("MOVE_ORDER")) {
            orderCode = "MOVE_ORDER";
            subInvTo = customer.getUserName();
        }
        if (orderType.equalsIgnoreCase("MOVE_ORDERINV")) {
            orderCode = "MOVE_ORDER";
            subInvFrom = customer.getUserName();
            subInvTo = customer.getSubInvCode();
        } else {
            orderCode = orderType;
        }
        Map<String, Object> inParamMap = new HashMap();
        inParamMap.put("p_party_id", customer.getCustomerSiteId());
        inParamMap.put("p_inp_item_id", listItem.getItemId());
        inParamMap.put("p_inp_qty", listItem.getQuantity());
        inParamMap.put("P_VEHICLE_NO", customer.getUserName());
        inParamMap.put("P_HEADER_ID", listItem.getHeaderId());
        inParamMap.put("P_TRANS_TYPE", customer.getTransactionType());
        inParamMap.put("P_INV_NUM", invoiceId);
        inParamMap.put("P_CUST_SITE", customer.getCustomerSite());
        inParamMap.put("P_PMT_METHOD", customer.getPaymentMethod());
        inParamMap.put("P_ORDER_CODE", orderCode);
        inParamMap.put("P_ORG_ID", customer.getOrgId());
        inParamMap.put("P_TRANS_CURR", "OMR");
        inParamMap.put("P_CUST_ACCOUNT_ID", customer.getCustAccountId());
        inParamMap.put("P_ORDER_TYPE_ID", customer.getTransactionTypeId());
        inParamMap.put("P_CUST_ID", customer.getCustomerName());
        inParamMap.put("P_SOLD_QTY", listItem.getQuantity());
        inParamMap.put("P_FOC_FLAG", null);
        inParamMap.put("P_PRICE", listItem.getPrice());
        inParamMap.put("P_PROCESS_FLAG", 'N');
        inParamMap.put("P_SUBINV_FROM", subInvFrom);
        inParamMap.put("P_SUBINV_TO", subInvTo);
        inParamMap.put("P_LOC_ID", null);
        SqlParameterSource in = new MapSqlParameterSource(inParamMap);
        simpleJdbcCall.execute(in);
    }

    public Status cancelItems(String invoiceNumber, String orderType)
            throws VanSalesException {
        Status status = new Status();
        Map<String, Object> inParamMap = null;
        SimpleJdbcCall simpleJdbcCall = null;
        String procedureName = null;
        try {
            if ((orderType != null) && (orderType.equalsIgnoreCase("MOVE_ORDER_INV"))) {
                procedureName = "VANSALES_DELETE_TRANSHDR_QTY";
            } else {
                procedureName = "VANSALES_DELETE_RTNSHDR_QTY";
            }
            simpleJdbcCall = new SimpleJdbcCall(this.jdbcTemplate).withCatalogName("XXASF_VANSALES_PACKAGE").withProcedureName(procedureName);
            inParamMap = new HashMap();
            inParamMap.put("P_INVOICE_NUM", invoiceNumber);
            SqlParameterSource in = new MapSqlParameterSource(inParamMap);
            Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);
            String result = (String) simpleJdbcCallResult.get("P_STATUS");
            status.setStatus(result);
        } catch (Exception exp) {
            LOGGER.error(exp.getMessage());
            throw new VanSalesException(exp.getMessage());
        }
        return status;
    }
}
