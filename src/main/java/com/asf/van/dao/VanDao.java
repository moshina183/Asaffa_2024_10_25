package com.asf.van.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asf.van.exception.VanSalesException;
import com.asf.van.model.Customer;
import com.asf.van.model.CustomerSite;
import com.asf.van.model.Item;
import com.asf.van.model.Organization;
import com.asf.van.model.PaymentMethod;
import com.asf.van.model.PopUp;
import com.asf.van.model.RentalKnockOff;
import com.asf.van.model.SubInvLocation;
import com.asf.van.model.SubInventory;
import com.asf.van.model.TransType;
import com.asf.van.model.TransactionType;
import com.asf.van.model.VatInfo;
import com.asf.van.model.VehicleDetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class VanDao extends NamedParameterJdbcDaoSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(VanDao.class);
    private String custid;

    @Autowired
    public void setDataSource(JdbcTemplate jdbcTemplate) {
        super.setDataSource(jdbcTemplate.getDataSource());
    }

    public List<TransactionType> getTransactionTypes() throws VanSalesException {
        List<TransactionType> transactionTypes = new ArrayList();
        try {
            transactionTypes = getNamedParameterJdbcTemplate().query(SQLStatementsLookup.TRANSACTION_TYPES_QUERY,
                    new RowMapper() {
                        @Override
                        public TransactionType mapRow(ResultSet rs, int rowNum) throws SQLException {
                            TransactionType transaction = new TransactionType();
                            transaction.setTransactionId(rs.getLong("TRANSACTION_ID"));
                            transaction.setTransactionName(rs.getString("transaction_name"));
                            return transaction;
                        }
                    });
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return transactionTypes;
    }

    public List<TransactionType> getTransactionTypes(String trans) throws VanSalesException {
        List<TransactionType> transactionTypes = null;
        try {
            if (trans.startsWith("RE")) {
                transactionTypes = getTransTypes(SQLStatementsLookup.TRANSACTION_TYPES_RETURN_QUERY);
            } else {
                transactionTypes = getTransTypes(SQLStatementsLookup.TRANSACTION_TYPES_PRE_INQ__QUERY);
            }
        } catch (VanSalesException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return transactionTypes;
    }

    private List<TransactionType> getTransTypes(String query) throws VanSalesException {
        List<TransactionType> transactionTypes = null;
        try {
            transactionTypes = getNamedParameterJdbcTemplate().query(query, new RowMapper() {
                @Override
                public TransactionType mapRow(ResultSet rs, int rowNum) throws SQLException {
                    TransactionType transaction = new TransactionType();
                    transaction.setTransactionId(rs.getLong("transaction_id"));
                    transaction.setTransactionName(rs.getString("transaction_name"));
                    return transaction;
                }
            });
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return transactionTypes;
    }

    public List<TransactionType> getInvTransactionTypes() throws VanSalesException {
        List<TransactionType> transactionTypes = null;
        try {
            transactionTypes = getNamedParameterJdbcTemplate().query(SQLStatementsLookup.TRANSACTION_TYPES_INV_QUERY,
                    new RowMapper() {
                        @Override
                        public TransactionType mapRow(ResultSet rs, int rowNum) throws SQLException {
                            TransactionType transaction = new TransactionType();
                            transaction.setTransactionId(rs.getLong("transaction_id"));
                            transaction.setTransactionName(rs.getString("transaction_name"));
                            return transaction;
                        }
                    });
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return transactionTypes;
    }

    public List<Customer> getCustomers(String userName) throws VanSalesException {
        List<Customer> customers = null;
        Map<String, Object> params = new HashMap();
        params.put("userName", userName);
        try {
            customers = getNamedParameterJdbcTemplate().query(SQLStatementsLookup.CUSTOMERS_QUERY, params,
                    new RowMapper() {
                        @Override
                        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
                            Customer customer = new Customer();
                            customer.setCustomerId(rs.getLong("customer_id"));
                            customer.setCustomerName(rs.getString("PARTY_NAME"));
                            customer.setPartyId(rs.getString("PARTY_ID"));
                            return customer;
                        }
                    });
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return customers;
    }

    public List<CustomerSite> getCustomerSites(String customerId, String userName) throws VanSalesException {
        List<CustomerSite> customersites = null;
        Map<String, Object> params = new HashMap();
        params.put("party_name", customerId);
        params.put("userName", userName);
        try {
            customersites = getNamedParameterJdbcTemplate().query(SQLStatementsLookup.CUSTOMERSITE_QUERY, params,
                    new RowMapper() {
                        @Override
                        public CustomerSite mapRow(ResultSet rs, int rowNum) throws SQLException {
                            System.out.println(rs.getString("ADDRESS_LOC"));
                            CustomerSite customersite = new CustomerSite();
                            customersite.setAddress(rs.getString("ADDRESS"));
                            customersite.setAddressLoc(rs.getString("ADDRESS_LOC"));
                            customersite.setLocationId(rs.getLong("LOCATION_ID"));
                            return customersite;
                        }
                    });
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return customersites;
    }

    public List<TransType> getTransTypes() throws VanSalesException {
        List<TransType> transTypes = null;
        try {
            transTypes = getNamedParameterJdbcTemplate().query(SQLStatementsLookup.TRANSTYPE_QUERY, new RowMapper() {
                @Override
                public TransType mapRow(ResultSet rs, int rowNum) throws SQLException {
                    TransType trans = new TransType();
                    trans.setTransName(rs.getString("Trans"));
                    return trans;
                }
            });
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return transTypes;
    }

    public PaymentMethod getPaymentMethod(int locationId) throws VanSalesException {
        PaymentMethod paymentMethod = null;
        Map<String, Object> params = new HashMap();
        params.put("locationId", locationId);
        List<PaymentMethod> payments = null;
        try {
            payments = getNamedParameterJdbcTemplate().query(SQLStatementsLookup.PAYMENTMETHOD_QUERY, params,
                    new RowMapper() {
                        @Override
                        public PaymentMethod mapRow(ResultSet rs, int rowNum) throws SQLException {
                            PaymentMethod payment = new PaymentMethod();
                            payment.setMethod(rs.getString("payment_method"));
                            return payment;
                        }
                    });
            if (payments.size() > 0) {
                paymentMethod = (PaymentMethod) payments.get(0);
            }
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return paymentMethod;
    }

    public List<Item> getItems(final String userName) throws VanSalesException {
        List<Item> items = null;
        Map<String, Object> params = new HashMap();
        params.put("vehicleNo", userName);
        try {
            items = getNamedParameterJdbcTemplate().query(SQLStatementsLookup.MOV_ITEMS_QUERY, params, new RowMapper() {
                @Override
                public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Item item = new Item();
                    item.setDescription(rs.getString("description") + " " + rs.getString("item_uom"));
                    item.setItemCode(rs.getLong("item_num"));
                    item.setItemId(rs.getLong("item_id"));
                    item.setUomCode(rs.getString("item_uom"));
                    item.setHeaderId(rs.getLong("header_id"));
                    item.setUserName(userName);
                    return item;
                }
            });
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return items;
    }

    // public String fetchFoc(String customerId, long itemId) {
    // List<String> focList = null;
    // try {
    // Calling procedure
    // String sql
    // = "SELECT XXASA_PROMO_ITMS('" + customerId + "','" + itemId
    // + "') FROM DUAL";
    // focList
    // = getNamedParameterJdbcTemplate().query(sql, new RowMapper() {
    // @Override
    // public String mapRow(ResultSet rs, int rowNum)
    // throws SQLException {
    // String foc
    // = rs.getString(1).isEmpty() ? "" : rs.getString(1).replaceAll("\\**",
    // "").trim();
    // LOGGER.info("foc value :" + foc);
    // return foc;
    // }
    // });
    //
    // } catch (DataAccessException e) {
    // LOGGER.error(e.getMessage(), e);
    // return "";
    // }
    // return focList.get(0);
    // }
    public List<Item> getSalesEntryItems(final String userName, String customerId, String partySiteId,
            String organizationId) throws VanSalesException {
        custid = customerId;
        Map<String, Object> params = new HashMap();
        params.put("p_customer_id", customerId);
        params.put("p_cust_site_id", partySiteId);
        params.put("p_organization_id", organizationId);
        params.put("vehicleNo", userName);
        List<Item> items = null;
        try {
            items = getNamedParameterJdbcTemplate().query(SQLStatementsLookup.SALESENTRY_ITEMS_QUERY, params,
                    new RowMapper() {
                        @Override
                        public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
                            Item item = new Item();
                            item.setDescription(rs.getString("description"));
                            item.setItemCode(rs.getLong("item_num"));
                            item.setItemId(rs.getLong("item_id"));
                            item.setUomCode(rs.getString("item_uom"));
                            item.setHeaderId(rs.getLong("header_id"));
                            item.setPerVATRate(rs.getFloat("itemVat"));
                            item.setPrice(rs.getFloat("price"));
                            item.setUserName(userName);
                            item.setQuantityOnHand(rs.getString("qtyOnHand"));
                            item.setPriceCnt(rs.getLong("PRICE_CNT"));
                            // For Foc
                            // String foc = fetchFoc(custid, item.getItemId());
                            // if (foc.length() > 0) {
                            // item.setFoc(foc);
                            // } else {
                            // item.setFoc("");
                            // }
                            return item;
                        }
                    });
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return items;
    }

    public Boolean checkSalesEntryItemDisc(String customerId, String partySiteId, String itemId, String organizationId)
            throws VanSalesException {
        Map<String, Object> params = new HashMap();
        params.put("p_customer_id", customerId);
        params.put("p_cust_site_id", partySiteId);
        params.put("p_item_id", itemId);
        params.put("p_organization_id", organizationId);
        Boolean isDiscounted = null;
        try {
            isDiscounted = Boolean.valueOf(getNamedParameterJdbcTemplate()
                    .queryForObject(SQLStatementsLookup.CHECK_SALESENTRY_ITEM_DISCOUNT_QUERY, params, String.class));
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return isDiscounted;
    }

    public List<Organization> getOrganizations() throws VanSalesException {
        List<Organization> organizations = getNamedParameterJdbcTemplate()
                .query(SQLStatementsLookup.ORGANIZATIONS_QUERY, new RowMapper() {
                    @Override
                    public Organization mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Organization organization = new Organization();
                        organization.setOrganizationCode(rs.getInt("ORGANIZATION_CODE"));
                        organization.setOrganizationId(rs.getInt("ORGANIZATION_ID"));
                        organization.setOrganizationName(rs.getString("ORGANIZATION_NAME"));
                        return organization;
                    }
                });
        return organizations;
    }

    public void deleteExistInvoiceReview(final String invoiceNum) throws VanSalesException {
        try {
            getJdbcTemplate().execute(SQLStatementsLookup.DELETE_REVIEW_ITEMS_EXIST, new PreparedStatementCallback() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                    ps.setString(1, invoiceNum);
                    return ps.execute();
                }
            });
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
    }

    public List<SubInventory> getSubInventory(String organizationId) throws VanSalesException {
        Map<String, Object> params = new HashMap();
        params.put("p_organization_id", organizationId);
        List<SubInventory> subInvs = getNamedParameterJdbcTemplate().query(SQLStatementsLookup.SUBINVENTORY_QUERY,
                params, new RowMapper() {
                    @Override
                    public SubInventory mapRow(ResultSet rs, int rowNum) throws SQLException {
                        SubInventory subInv = new SubInventory();
                        subInv.setSubInvCode(rs.getString("SUBINVENTORY_CODE"));
                        return subInv;
                    }
                });
        return subInvs;
    }

    public List<SubInvLocation> getSubInventoryLocations(String subInvCode, String organizationId)
            throws VanSalesException {
        Map<String, Object> params = new HashMap();
        params.put("subinventory_code", subInvCode);
        params.put("organization_id", organizationId);
        List<SubInvLocation> subInvs = null;
        try {
            subInvs = getNamedParameterJdbcTemplate().query(SQLStatementsLookup.SUBINV_LOCATION_QUERY, params,
                    new RowMapper() {
                        @Override
                        public SubInvLocation mapRow(ResultSet rs, int rowNum) throws SQLException {
                            SubInvLocation subInv = new SubInvLocation();
                            subInv.setLocation(rs.getString("CONCATENATED_SEGMENTS"));
                            subInv.setLocationId(rs.getString("INVENTORY_LOCATION_ID"));
                            return subInv;
                        }
                    });
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return subInvs;
    }

    public VehicleDetails getConfirmedItems(String userName, String invoiceNum, String orderType)
            throws VanSalesException {
        Map<String, Object> params = new HashMap();
        params.put("invoiceNum", invoiceNum);
        params.put("userName", userName);
        final VehicleDetails vehicleDetails = new VehicleDetails();
        List<Item> items = null;
        String Query = null;
        try {
            if ((orderType != null) && (orderType.equalsIgnoreCase("MOVE_ORDER"))) {
                Query = SQLStatementsLookup.CONFIRM_ITEMS_RETURN_QUERY;
                logger.info("111==>");
            } else {
                Query = SQLStatementsLookup.CONFIRM_ITEMS_QUERY;
                logger.info("222==>");
            }
            items = getNamedParameterJdbcTemplate().query(Query, params, new RowMapper() {
                @Override
                public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Item item = new Item();
                    vehicleDetails.setCustAccountId(rs.getString("CUST_ACCOUNT_ID"));
                    vehicleDetails.setCustomerName(rs.getString("CUSTOMER_NAME"));
                    vehicleDetails.setTransactionType(rs.getString("TRANSACTION_TYPE"));
                    vehicleDetails.setTransactionTypeId(rs.getString("ORDER_TYPE_ID"));
                    vehicleDetails.setCustomerSite(rs.getString("CUSTOMER_SITE"));
                    vehicleDetails.setCustomerSiteId(rs.getString("party_site_id"));
                    vehicleDetails.setTransType(rs.getString("ORDER_CATEGORY_CODE"));
                    vehicleDetails.setPaymentMethod(rs.getString("PAYMENT_METHOD"));
                    vehicleDetails.setVatRate(rs.getInt("VATRate"));
                    item.setHeaderId(rs.getLong("header_id"));
                    String orderType1 = rs.getString("ORDER_CATEGORY_CODE");
                    if (orderType1.equalsIgnoreCase("ORDER")) {
                        item.setDescription(rs.getString("description") + " " + rs.getString("item_uom") + " "
                                + rs.getFloat("qtyOnHand") + "@" + rs.getFloat("item_price")+"/"+rs.getFloat("per_vat_rate"));
                    } else {
                        item.setDescription(rs.getString("description") + " " + rs.getString("item_uom") + "@"
                                + rs.getFloat("item_price")+"/"+rs.getFloat("per_vat_rate"));
                    }
                    item.setFocFlag(rs.getString("foc_item_flag") == null ? "N" : rs.getString("foc_item_flag"));
                    item.setItemId(rs.getLong("item_num"));
                    item.setPrice(rs.getFloat("item_price"));
                    item.setQuantity(rs.getString("sold_qty"));
                    item.setPerVATRate(rs.getFloat("per_vat_rate"));
                    item.setItemAmount(rs.getFloat("item_amount"));
                    item.setVatAmount(rs.getFloat("vat_amount"));
                    return item;
                }
            });
            Map<String, Object> paramMap = new HashMap();
            paramMap.put("invoiceNum", invoiceNum);
            paramMap.put("userName", userName);
            String total = (String) getNamedParameterJdbcTemplate()
                    .queryForObject(SQLStatementsLookup.CONFIRM_TOTAL_AMOUNT_QUERY, paramMap, String.class);

            String vatTot=(String) getNamedParameterJdbcTemplate()
            .queryForObject(SQLStatementsLookup.CONFIRM_TOTAL_VAT_QUERY, paramMap, String.class); 

            String netTot=(String) getNamedParameterJdbcTemplate()
            .queryForObject(SQLStatementsLookup.CONFIRM_TOTAL_NET_QUERY, paramMap, String.class);

            vehicleDetails.setVatAmount(vatTot==null?0.0F:Float.parseFloat(vatTot));
            vehicleDetails.setNetAmount(netTot==null?0.0F:Float.parseFloat(netTot));
            vehicleDetails.setTotalAmount(total == null ? 0.0F : Float.parseFloat(total));
            vehicleDetails.setItems(items);
        } catch (NumberFormatException | DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return vehicleDetails;
    }

    public VehicleDetails getReviewedItems(String userName, String invoiceNum) throws VanSalesException {
        final Map<String, String> totalCount = new HashMap();

        Map<String, Object> params = new HashMap();
        params.put("invoiceNum", invoiceNum);
        params.put("userName", userName);
        final VehicleDetails vehicleDetails = new VehicleDetails();
        List<Item> items = null;
        String Query = null;
        float estimatedValue = 0;
        try {
            Query = SQLStatementsLookup.REVIEW_ITEMS_QUERY;
            items = getNamedParameterJdbcTemplate().query(Query, params, new RowMapper() {
                @Override
                public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
                    // Counting remaing quantity for items with promotion
                    if (totalCount.get(rs.getString("ITEM_ID")) == null) {
                        totalCount.put(rs.getString("ITEM_ID"), rs.getString("SOLD_QTY"));
                    } else {
                        totalCount.put(rs.getString("ITEM_ID"),
                                Float.toString(Float.valueOf(totalCount.get(rs.getString("ITEM_ID")))
                                        + Float.valueOf(rs.getString("SOLD_QTY"))));
                    }

                    Item item = new Item();
                    vehicleDetails.setCustAccountId(rs.getString("CUST_ACCOUNT_ID"));
                    vehicleDetails.setCustomerName(rs.getString("CUSTOMER_NAME"));
                    vehicleDetails.setTransactionType(rs.getString("TRANSACTION_TYPE"));
                    vehicleDetails.setTransactionTypeId(rs.getString("ORDER_TYPE_ID"));
                    vehicleDetails.setCustomerSite(rs.getString("CUSTOMER_SITE"));
                    vehicleDetails.setCustomerSiteId(rs.getString("party_site_id"));
                    vehicleDetails.setTransType(rs.getString("ORDER_CATEGORY_CODE"));
                    vehicleDetails.setPaymentMethod(rs.getString("PAYMENT_METHOD"));
                    // VAT
                    vehicleDetails.setVatRate(rs.getInt("VATRate"));
                    item.setHeaderId(rs.getLong("header_id"));

                    item.setOrderCategory(rs.getString("ORDER_CATEGORY_CODE"));
                    item.setDescription(rs.getString("description"));
                    item.setQuantityOnHand(rs.getString("qtyOnHand"));
                    item.setItemCode(rs.getLong("ITEM_ID"));
                    item.setUomCode(rs.getString("item_uom"));

                    item.setFocFlag(rs.getString("foc_item_flag") == null ? "N" : rs.getString("foc_item_flag"));
                    item.setItemId(rs.getLong("item_num"));
                    item.setPrice(rs.getFloat("item_price"));
                    item.setQuantity(rs.getString("sold_qty"));
                    item.setPerVATRate(rs.getFloat("per_vat_rate"));
                    item.setItemAmount(rs.getFloat("Item_amount"));
                    item.setVatAmount(rs.getFloat("vat_amount"));
                    return item;
                }
            });
            // Give Description with details, quantity, uom and price
            for (Item item : items) {
                if (item.getOrderCategory().equalsIgnoreCase("ORDER")) {
                    estimatedValue = Float.valueOf(item.getQuantityOnHand())
                            - Float.valueOf(totalCount.get(Long.toString(item.getItemCode())));
                    item.setDescription(item.getDescription() + ' ' + item.getUomCode() + ' ' + estimatedValue + '@'
                            + item.getPrice()+'/'+item.getPerVATRate());
                    if (estimatedValue < 0) {
                        item.setStatus(false);
                    } else {
                        item.setStatus(true);
                    }
                } else {
                    item.setDescription(item.getDescription() + ' ' + item.getUomCode() + '@' + item.getPrice()+'/'+item.getPerVATRate());
                }
            }

            Map<String, Object> paramMap = new HashMap();
            paramMap.put("invoiceNum", invoiceNum);
            paramMap.put("userName", userName);
            String total = (String) getNamedParameterJdbcTemplate()
                    .queryForObject(SQLStatementsLookup.REVIEW_TOTAL_AMOUNT_QUERY, paramMap, String.class);
            
            String vatTot=(String) getNamedParameterJdbcTemplate()
            .queryForObject(SQLStatementsLookup.REVIEW_TOTAL_VAT_QUERY, paramMap, String.class); 

            String netTot=(String) getNamedParameterJdbcTemplate()
            .queryForObject(SQLStatementsLookup.REVIEW_TOTAL_NET_QUERY, paramMap, String.class);

            vehicleDetails.setTotalAmount(total == null ? 0.0F : Float.parseFloat(total));
            vehicleDetails.setVatAmount(vatTot==null?0.0F:Float.parseFloat(vatTot));
            vehicleDetails.setNetAmount(netTot==null?0.0F:Float.parseFloat(netTot));
            vehicleDetails.setItems(items);
        } catch (NumberFormatException | DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return vehicleDetails;
    }

    private String getOrderType(String orderType) throws VanSalesException {
        String categoryCode = null;
        try {
            if ((orderType.equals("MOVE_ORDER")) || (orderType.equals("MOVE_ORDERINV"))) {
                categoryCode = "MOVE_ORDER";
            } else {
                categoryCode = orderType;
            }
        } catch (Exception exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return categoryCode;
    }

    public String getInvoiceSequenceNumber() throws VanSalesException {
        String result = null;

        try {
            result = (String) getJdbcTemplate().queryForObject(SQLStatementsLookup.INVOICE_SEQUENCE_QUERY,
                    String.class);
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return result;
    }

    public String getReviewInvoiceSequenceNumber() throws VanSalesException {
        String result = null;

        try {
            result = (String) getJdbcTemplate().queryForObject("Select XXASF_VS_INVOICE_PV_S.nextval from dual",
                    String.class);
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return result;
    }

    public List<Item> getOrderToInventoryItems(final String userName) throws VanSalesException {
        Map<String, Object> params = new HashMap();
        List<Item> items = null;
        try {
            params.put("vehicleNo", userName);
            items = getNamedParameterJdbcTemplate().query(SQLStatementsLookup.ORDER_INVENTORY_ITEMS_QUERY, params,
                    new RowMapper() {
                        @Override
                        public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
                            Item item = new Item();
                            item.setDescription(rs.getString("description") + " " + rs.getString("item_uom"));

                            item.setItemCode(rs.getLong("item_num"));

                            item.setItemId(rs.getLong("item_id"));
                            item.setUomCode(rs.getString("item_uom"));

                            item.setHeaderId(rs.getLong("header_id"));

                            item.setQuantity(rs.getString("remaining_qty"));

                            item.setUserName(userName);
                            return item;
                        }
                    });
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return items;
    }

    public boolean cancelItems(final String userName) throws VanSalesException {
        try {
            ((Boolean) getJdbcTemplate().execute(SQLStatementsLookup.DELETE_HRD_ITEMS_QUERY,
                    new PreparedStatementCallback() {
                        @Override
                        public Boolean doInPreparedStatement(PreparedStatement ps)
                                throws SQLException, DataAccessException {
                            ps.setString(1, userName);
                            return ps.execute();
                        }
                    })).booleanValue();
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
        }
        return false;
    }

    public List<Item> getOrderItems(final String userName, String customerId, String partySiteId, String organizationId)
            throws VanSalesException {
        Map<String, Object> params = new HashMap();
        List<Item> items = null;
        try {
            params.put("p_customer_id", customerId);
            params.put("p_cust_site_id", partySiteId);
            params.put("p_organization_id", organizationId);
            params.put("vehicleNo", userName);
            items = getNamedParameterJdbcTemplate().query(SQLStatementsLookup.ORDER_ITEMS_QUERY, params,
                    new RowMapper() {
                        @Override
                        public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
                            Item item = new Item();
                            item.setPrice(rs.getFloat("price"));
                            item.setDescription(rs.getString("description"));
                            item.setItemCode(rs.getLong("item_num"));
                            item.setItemId(rs.getLong("item_id"));
                            item.setUomCode(rs.getString("item_uom"));
                            item.setHeaderId(rs.getLong("header_id"));
                            item.setPerVATRate(rs.getFloat("itemVat"));
                            item.setPriceCnt(rs.getLong("PRICE_CNT"));
                            item.setUserName(userName);
                            return item;
                        }
                    });
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return items;
    }

    public List<Item> getCancelItems(final String userName, String customerId, String partySiteId)
            throws VanSalesException {
        Map<String, Object> params = new HashMap();
        List<Item> items = null;
        try {
            params.put("vehicleNo", userName);
            params.put("p_customer_id", customerId);
            params.put("p_cust_site_id", partySiteId);
            items = getNamedParameterJdbcTemplate().query(SQLStatementsLookup.CANCEL_ITEMS_QUERY, params,
                    new RowMapper() {
                        @Override
                        public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
                            Item item = new Item();
                            item.setDescription(rs.getString("customer_name"));
                            item.setLocation(rs.getString("CUSTOMER_SITE"));
                            item.setInvoiceNum(rs.getString("INVOICE_NUM"));
                            item.setReceiptNum(rs.getString("RECEIPT_NUM"));
                            item.setUserName(userName);
                            return item;
                        }
                    });
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return items;
    }

    public boolean updateCustomer(final String chequeNum, final String chequeAmt, final String invoiceNumber) {
        try {
            ((Boolean) getJdbcTemplate().execute(SQLStatementsLookup.UPDATE_CUSTOMER_QUERY,
                    new PreparedStatementCallback() {
                        @Override
                        public Boolean doInPreparedStatement(PreparedStatement ps)
                                throws SQLException, DataAccessException {
                            ps.setString(1, chequeNum);
                            ps.setString(2, chequeAmt);
                            ps.setString(3, invoiceNumber);
                            return ps.execute();
                        }
                    })).booleanValue();
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
        }
        return false;
    }

    // public String getOrderNumber(String invoiceNumber) throws VanSalesException {
    // String orderNumber = null;
    // Map<String, Object> params = new HashMap();
    // try {
    // params.put("invoice_num", invoiceNumber);
    // do {
    // orderNumber
    // = (String)
    // getNamedParameterJdbcTemplate().queryForObject(SQLStatementsLookup.ORDER_NUMBER_QUERY,
    // params,
    // String.class
    // );
    // } while (orderNumber == null);
    // } catch (DataAccessException exp) {
    // LOGGER.error("Exception while getting order number :: ", exp);
    // throw new VanSalesException(exp.getMessage());
    // }
    // return orderNumber;
    // }
    public String getCreditBalance(String customerId) throws VanSalesException {
        String creditBalance = null;
        try {
            Map<String, Object> paramMap = new HashMap();
            paramMap.put("p_customer_id", customerId);
            creditBalance = (String) getNamedParameterJdbcTemplate().queryForObject("select xxasf_vansales_package.xxasf_get_credit_bal(:p_customer_id) as bal from dual", paramMap, String.class);
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return creditBalance;
    }

    public String getOrderNumber(String invoiceNumber) throws VanSalesException {
        String orderNumber = null;
        Map<String, Object> params = new HashMap();
        try {
            params.put("invoice_num", invoiceNumber);
            do {
                orderNumber = (String) getNamedParameterJdbcTemplate()
                        .queryForObject(SQLStatementsLookup.ORDER_NUMBER_QUERY, params, String.class);
            } while (orderNumber == null);
        } catch (DataAccessException exp) {
            LOGGER.error("Exception while getting order number :: ", exp);
            throw new VanSalesException(exp.getMessage());
        }
        return orderNumber;
    }

    public List<RentalKnockOff> getRentalKnockOff() throws VanSalesException {
        List<RentalKnockOff> rentalKnockOff = null;
        try {
            rentalKnockOff = getNamedParameterJdbcTemplate().query(SQLStatementsLookup.RENTAL_KNOCK_OFF_QUERY,
                    new RowMapper() {
                        @Override
                        public RentalKnockOff mapRow(ResultSet rs, int rowNum) throws SQLException {
                            RentalKnockOff rentalKnockOff1 = new RentalKnockOff();
                            rentalKnockOff1.setRentalKnockOffCode(rs.getString("Code"));
                            rentalKnockOff1.setRentalKnockOffDesc(rs.getString("Description"));
                            rentalKnockOff1.setRentalKnockOffMeaning(rs.getString("Meaning"));
                            return rentalKnockOff1;
                        }
                    });
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return rentalKnockOff;
    }

    public List<Item> getSalesInvoiceBrowser(final String invoiceType, final String userName, String loadInoiveData)
            throws VanSalesException {
        List<Item> items = null;
        Map<String, Object> params = new HashMap();
        try {
            params.put("invoiceType", invoiceType);
            params.put("vehicleNo", userName);
            params.put("loadInvoiceDate", loadInoiveData);
            System.out.println("invoiceType" + invoiceType);
            System.out.println("vehicleNo" + userName);
            System.out.println("loadInvoiceDate" + loadInoiveData);
            items = getNamedParameterJdbcTemplate().query(SQLStatementsLookup.SALES_INVOICE_BROWSER_QUERY, params,
                    new RowMapper() {
                        @Override
                        public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
                            Item item = new Item();
                            item.setDescription(rs.getString("customer_name"));
                            item.setLocation(rs.getString("CUSTOMER_SITE"));
                            item.setInvoiceNum(rs.getString("INVOICE_NUM"));
                            item.setReceiptNum(rs.getString("RECEIPT_NUM"));
                            item.setUserName(userName);
                            return item;
                        }
                    });
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return items;
    }

    public List<Item> getSalesInvoiceBrowserCancelledItems(final String userName, String loadInvoiceDate)
            throws VanSalesException {
        List<Item> items = null;
        Map<String, Object> params = new HashMap();
        try {
            params.put("vehicleNo", userName);
            params.put("loadInvoiceDate", loadInvoiceDate);
            items = getNamedParameterJdbcTemplate()
                    .query(SQLStatementsLookup.SALES_INVOICE_BROWSER_QUERY_CANCELLED_ITEMS, params, new RowMapper() {
                        @Override
                        public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
                            Item item = new Item();
                            item.setDescription(rs.getString("customer_name"));
                            item.setLocation(rs.getString("CUSTOMER_SITE"));
                            item.setInvoiceNum(rs.getString("INVOICE_NUM"));
                            item.setReceiptNum(rs.getString("RECEIPT_NUM"));
                            item.setUserName(userName);
                            return item;
                        }
                    });
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return items;
    }

    public List<Map<String, Object>> getSalesInvoiceBrowserMoveOrders(String menuType, String vehicleNo,
            String orderReqDate) throws VanSalesException {
        List<Map<String, Object>> orders = null;
        Map<String, Object> params = new HashMap();
        try {
            params.put("menuType", menuType);
            params.put("vehicleNo", vehicleNo);
            params.put("orderReqDate", orderReqDate);
            orders = getNamedParameterJdbcTemplate().queryForList(SQLStatementsLookup.SALES_INVOICE_BROWSER_MOVE_ORDER,
                    params);
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return orders;
    }

    public List<Map<String, Object>> getSalesInvoiceBrowserMoveOrderItems(String orderNo) throws VanSalesException {
        List<Map<String, Object>> items = null;
        Map<String, Object> params = new HashMap();
        try {
            params.put("orderNo", orderNo);
            items = getNamedParameterJdbcTemplate()
                    .queryForList(SQLStatementsLookup.SALES_INVOICE_BROWSER_MOVE_ORDER_ITEMS, params);
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
            throw new VanSalesException(exp.getMessage());
        }
        return items;
    }

    public void updateReceiptNum(final String receiptNum, final String invoiceNumber) {
        try {
            getJdbcTemplate().execute(SQLStatementsLookup.UPDATE_RECEIPT_NUM_QUERY, new PreparedStatementCallback() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                    ps.setString(1, receiptNum);
                    ps.setString(2, invoiceNumber);
                    return ps.execute();
                }
            });
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
        }
    }

    public boolean checkRefNoExist(final String referenceNumber) throws VanSalesException {
        boolean result = false;
        List<VehicleDetails> vehicle = null;
        Map<String, Object> params = new HashMap();
        try {
            params.put("referenceNumber", referenceNumber);
            vehicle = getNamedParameterJdbcTemplate().query(SQLStatementsLookup.CHECK_REFERENCE_NUM_QUERY, params,
                    new RowMapper() {
                        @Override
                        public VehicleDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
                            VehicleDetails vehicleDetail = new VehicleDetails();
                            vehicleDetail.setReferenceNo(rs.getString("REFERENCE_NO"));
                            System.out.println("REFERENCE_NO inside exist: "+vehicleDetail.getReferenceNo());
                            return vehicleDetail;
                        }
                    });
            result = !vehicle.isEmpty();
        } catch (DataAccessException e) {
            LOGGER.info("Error in reference No exist", e);
            throw new VanSalesException(e.getMessage());
        }
        return result;
    }

    public boolean checkInvoiceExist(final String invoiceNumber) throws VanSalesException {
        boolean result = false;
        List<VehicleDetails> vehicle = null;
        Map<String, Object> params = new HashMap();
        try {
            params.put("invoiceNumber", invoiceNumber);
            vehicle = getNamedParameterJdbcTemplate().query(SQLStatementsLookup.CHECK_INVOICE_NUM_QUERY, params,
                    new RowMapper() {
                        @Override
                        public VehicleDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
                            VehicleDetails vehicleDetail = new VehicleDetails();
                            vehicleDetail.setInvoiceNum(rs.getString("INVOICE_NUM"));
                            return vehicleDetail;
                        }
                    });
            result = !vehicle.isEmpty();
        } catch (DataAccessException e) {
            LOGGER.info("Error in invoice exist", e);
            throw new VanSalesException(e.getMessage());
        }
        return result;
    }

    public Item getCancellationLimit(String vehicleNo) {
        boolean result = false;
        List<Item> items = null;
        Map<String, Object> params = new HashMap();
        try {
            params.put("vehicleNo", vehicleNo);
            items = getNamedParameterJdbcTemplate().query(SQLStatementsLookup.CANCELLATION_LIMIT_QUERY, params,
                    new RowMapper() {
                        Item item = new Item();

                        @Override
                        public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
                            System.out.println("Count => " + rs.getInt("TOTAL_COUNT"));
                            // Upto 3 limits, it will allow
                            if (rs.getInt("TOTAL_COUNT") < 3) {
                                item.setStatus(true);
                            } else {
                                item.setStatus(false);
                            }
                            return item;
                        }
                    });
        } catch (DataAccessException e) {
            LOGGER.info("Error is coming-", e);
        }
        return items.get(0);
    }

    public List<PopUp> getMessageText(String messageName) {
        List<PopUp> arabMessageText = null;
        List<PopUp> usMessageText = null;
        Map<String, Object> params = new HashMap();
        try {
            params.put("messageName", messageName);
            arabMessageText = getNamedParameterJdbcTemplate().query(SQLStatementsLookup.POP_UP_MESSAGE_TEXT_AR, params,
                    new RowMapper() {
                        @Override
                        public PopUp mapRow(ResultSet rs, int rowNum) throws SQLException {
                            PopUp popUp = new PopUp();
                            popUp.setMessageText(rs.getString("MESSAGE_TEXT"));
                            return popUp;
                        }
                    });
            usMessageText = getNamedParameterJdbcTemplate().query(SQLStatementsLookup.POP_UP_MESSAGE_TEXT_US, params,
                    new RowMapper() {
                        @Override
                        public PopUp mapRow(ResultSet rs, int rowNum) throws SQLException {
                            PopUp popUp = new PopUp();
                            popUp.setMessageText(rs.getString("MESSAGE_TEXT"));
                            return popUp;
                        }
                    });
        } catch (DataAccessException e) {
            LOGGER.info("Error is coming-", e);
        }
        arabMessageText.addAll(usMessageText);
        return arabMessageText;
    }

    // Dinesh VAT calling
    // public VatInfo getVatRate(String userName, String custName) throws VanSalesException {
    //     VatInfo vatRate=null;
    //     Map<String, Object> params = new HashMap();
    //     params.put("p_username", userName);
    //     params.put("p_cust_name",custName);
    //     List<VatInfo> vatValue = null;
    //     try {
    //         vatValue=getNamedParameterJdbcTemplate().query(SQLStatementsLookup.VAN_DRIVER_TAX, 
    //         params, new RowMapper(){
    //             @Override
    //             public VatInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
    //                 VatInfo vats = new VatInfo();
    //                 vats.setVatRate(rs.getInt("TAX_RATE"));
    //                 return vats;
    //             } 
    //         });
    //         if(vatValue.size()>0){
    //             vatRate=(VatInfo)vatValue.get(0);  
    //         }
    //     }catch(DataAccessException exp){
    //         LOGGER.error(exp.getMessage(), exp);
    //         throw new VanSalesException(exp.getMessage());
    //     }     
    //     return vatRate;
    // }
}
