package com.asf.van.dao;

import com.asf.van.exception.VanSalesException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

@Service("printInvoiceDAO")
public class PrintInvoiceDAO {

    private static final Logger LOGGER = Logger.getLogger(VanDao.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PrintInvoiceDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String callPrintInvoiceStatus(String userName, String invoiceId, String invoiceType)
            throws VanSalesException {
        SimpleJdbcCall simpleJdbcCall;

        if ((invoiceType != null) && (invoiceType.equalsIgnoreCase("ORDER"))) {
            simpleJdbcCall = new SimpleJdbcCall(this.jdbcTemplate).withCatalogName("XXASF_VANSALES_PACKAGE").withProcedureName("ASAFFA_VANSALES_INVOICE");
        } else {
            simpleJdbcCall = new SimpleJdbcCall(this.jdbcTemplate).withCatalogName("XXASF_VANSALES_PACKAGE").withProcedureName("ASAFFA_VANSALES_RETURN");
        }

        Map<String, Object> inParamMap = null;
        String printInvoiceStatus = null;
        try {
            inParamMap = new HashMap();
            inParamMap.put("P_VECHICLE_NUMBER", userName);
            inParamMap.put("P_Invoice_num", invoiceId);
            SqlParameterSource in = new MapSqlParameterSource(inParamMap);
            Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);

            printInvoiceStatus = (String) simpleJdbcCallResult.get("P_RETURN_FLAG");

            System.out.println(printInvoiceStatus);
        } catch (Exception exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return printInvoiceStatus;
    }

    public String callPrintVanSalesReport(String userName, String vanSalesCreateDate)
            throws VanSalesException {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(this.jdbcTemplate).withCatalogName("XXASF_VANSALES_PACKAGE").withProcedureName("ASAFFA_VANSALES_REPORT");

        Map<String, Object> inParamMap = null;
        String printInvoiceStatus = null;
        try {
            inParamMap = new HashMap();
            inParamMap.put("P_VECHICLE_NUMBER", userName);
            inParamMap.put("P_DATE", vanSalesCreateDate);
            SqlParameterSource in = new MapSqlParameterSource(inParamMap);
            Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);

            printInvoiceStatus = (String) simpleJdbcCallResult.get("P_RETURN_FLAG");

            System.out.println(printInvoiceStatus);
        } catch (Exception exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return printInvoiceStatus;
    }

    public String callPrintOnHandStatus(String userName)
            throws VanSalesException {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(this.jdbcTemplate).withCatalogName("XXASF_VANSALES_PACKAGE").withProcedureName("ASAFFA_VANSALES_ONHAND");

        Map<String, Object> inParamMap = null;
        String printInvoiceStatus = null;
        try {
            inParamMap = new HashMap();
            inParamMap.put("P_VECHICLE_NUMBER", userName);
            SqlParameterSource in = new MapSqlParameterSource(inParamMap);
            Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);

            printInvoiceStatus = (String) simpleJdbcCallResult.get("P_RETURN_FLAG");

            System.out.println(printInvoiceStatus);
        } catch (Exception exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return printInvoiceStatus;
    }

    public String callPrintOnMaterialRequest(String orderNumber)
            throws VanSalesException {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(this.jdbcTemplate).withCatalogName("XXASF_VANSALES_PACKAGE").withProcedureName("ASAFFA_VANSALES_MTRL_REQUEST");

        Map<String, Object> inParamMap = null;
        String printMaterialStatus = null;
        try {
            inParamMap = new HashMap();
            inParamMap.put("P_MOVE_ORDER_NUMBER", orderNumber);
            SqlParameterSource in = new MapSqlParameterSource(inParamMap);
            Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);

            printMaterialStatus = (String) simpleJdbcCallResult.get("P_RETURN_FLAG");

            System.out.println(printMaterialStatus);
        } catch (Exception exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return printMaterialStatus;
    }

    public String callPrintOrderReturnStatus(String userName, String invoiceId)
            throws VanSalesException {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(this.jdbcTemplate).withCatalogName("XXASF_VANSALES_PACKAGE").withProcedureName("ASAFFA_VANSALES_RETURN");

        Map<String, Object> inParamMap = null;
        String printInvoiceStatus = null;
        try {
            inParamMap = new HashMap();
            inParamMap.put("P_VECHICLE_NUMBER", userName);
            inParamMap.put("P_Invoice_num", invoiceId);
            SqlParameterSource in = new MapSqlParameterSource(inParamMap);
            Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);

            printInvoiceStatus = (String) simpleJdbcCallResult.get("P_RETURN_FLAG");

            System.out.println(printInvoiceStatus);
        } catch (Exception exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return printInvoiceStatus;
    }
}
