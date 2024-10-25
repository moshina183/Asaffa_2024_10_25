package com.asf.van.dao;

public class SQLStatementsLookup {

    public static final String USER_ID = "user_id";
    public static final String TRANSACTION_ID = "transaction_id";
    public static final String TRANSACTION_CODE = "transaction_name";
    public static final String CUSTOMER_ID = "customer_id";
    public static final String CUSTOMER_NAME = "customer_name";
    public static final String LOCATION_ID = "LOCATION_ID";
    public static final String ADDRESS = "ADDRESS";
    public static final String ROW_ID = "row_id";
    public static final String TRANS_NAME = "Trans";
    public static final String PAYMENT_METHOD = "payment_method";
    public static final String ITEM_CODE = "item_num";
    public static final String ITEM_ID = "item_id";
    public static final String ITEM_DESCRIPTION = "description";
    public static final String ITEM_PRICE = "price";
    public static final String ITEM_UOM = "item_uom";
    public static final String ITEM_USERNAME = "vehicle_no";
    public static final String ITEM_HEADER_ID = "header_id";
    public static final String ORGANIZATION_CODE = "ORGANIZATION_CODE";
    public static final String ORGANIZATION_ID = "ORGANIZATION_ID";
    public static final String ORGANIZATION_NAME = "ORGANIZATION_NAME";
    public static final String SUBINVENTORY_CODE = "SUBINVENTORY_CODE";
    public static final String CONCATENATED_SEGMENTS = "CONCATENATED_SEGMENTS";
    public static final String REMAINING_QTY = "remaining_qty";
    public static final String RENTAL_KNOCK_OFF_DESC = "Description";
    public static final String RENTAL_KNOCK_OFF_MEANING = "Meaning";
    public static final String PARTY_NAME = "PARTY_NAME";
    public static final String ADDRESS_LOC = "ADDRESS_LOC";
    public static final String INVOICE_NUM = "INVOICE_NUM";
    public static final String CUSTOMER_SITE = "CUSTOMER_SITE";
    public static final String RECEIPT_NUM = "RECEIPT_NUM";
    public static final String PARTY_ID = "PARTY_ID";
    public static final String CANCELLATION_LIMIT_QUERY = "SELECT COUNT(*) TOTAL_COUNT FROM (SELECT COUNT(*) TOTAL_COUNT FROM XXASF_VANSALES_TRANS_DTL WHERE TRUNC(CREATION_DATE) = TRUNC(SYSDATE) AND PROCESS_FLAG = 'C' AND VEHICLE_NO = :vehicleNo GROUP BY INVOICE_NUM)";
    public static final String BUSINESS_HOURS_QUERY = "select LOOKUP_CODE,Meaning TIMING from FND_LOOKUP_VALUES_VL where lookup_type ='XX_VANSALES_TIMING' AND ENABLED_FLAG='Y'";
    public static final String SELECT_USER_QUERY = "SELECT SECONDARY_INVENTORY_NAME vehicle_no, msi.attribute3 Salesman_name, ORGANIZATION_ID, usr.user_name, usr.user_id,get_pwd.decrypt ((SELECT (SELECT get_pwd.decrypt (fnd_web_sec.get_guest_username_pwd, usertable.encrypted_foundation_password  ) FROM DUAL) AS apps_password FROM fnd_user usertable WHERE usertable.user_name = (SELECT SUBSTR (fnd_web_sec.get_guest_username_pwd, 1, INSTR (fnd_web_sec.get_guest_username_pwd, '/' ) - 1 ) FROM DUAL) ), usr.encrypted_user_password ) PASSWORD FROM MTL_SECONDARY_INVENTORIES msi, fnd_user usr WHERE usr.user_name = :username AND msi.attribute1  = usr.user_name";
    public static final String TRANSACTION_TYPES_QUERY = "SELECT B.TRANSACTION_TYPE_ID transaction_id,  T.NAME transaction_name FROM OE_TRANSACTION_TYPES_TL T,   OE_TRANSACTION_TYPES_all B WHERE B.TRANSACTION_TYPE_ID = T.TRANSACTION_TYPE_ID AND T.LANGUAGE              = 'US' and b.order_category_code ='ORDER'  and b.TRANSACTION_TYPE_CODE='ORDER' ";
//    public static final String TRANSACTION_TYPES_RETURN_QUERY = "select B.TRANSACTION_TYPE_ID transaction_id, T.NAME transaction_name from OE_TRANSACTION_TYPES_TL T, OE_TRANSACTION_TYPES_all B WHERE B.TRANSACTION_TYPE_ID = T.TRANSACTION_TYPE_ID AND T.LANGUAGE  = 'US' and b.order_category_code ='RETURN' and b.TRANSACTION_TYPE_CODE='ORDER' and b.attribute1 = 'Y' ";

    // Added by balaji manickam - 4i apps -21-may-2019-04.34pm
    // To remove "Foods Van Good Returns Order" option
    public static final String TRANSACTION_TYPES_RETURN_QUERY = "select B.TRANSACTION_TYPE_ID transaction_id, T.NAME transaction_name from OE_TRANSACTION_TYPES_TL T, OE_TRANSACTION_TYPES_all B WHERE B.TRANSACTION_TYPE_ID = T.TRANSACTION_TYPE_ID AND T.LANGUAGE  = 'US' and b.order_category_code ='RETURN' and b.TRANSACTION_TYPE_CODE='ORDER' and b.attribute1 = 'Y' and B.TRANSACTION_TYPE_ID != 1026";

    public static final String TRANSACTION_TYPES_PRE_INQ__QUERY = "select B.TRANSACTION_TYPE_ID transaction_id, T.NAME transaction_name from OE_TRANSACTION_TYPES_TL T, OE_TRANSACTION_TYPES_all B WHERE B.TRANSACTION_TYPE_ID = T.TRANSACTION_TYPE_ID AND T.LANGUAGE = 'US' and b.order_category_code <> 'RETURN' and upper(T.NAME)  <> 'VAN SALES' and b.TRANSACTION_TYPE_CODE='ORDER'  ";
    public static final String TRANSACTION_TYPES_INV_QUERY = "SELECT B.TRANSACTION_TYPE_ID transaction_id,  T.NAME transaction_name FROM OE_TRANSACTION_TYPES_TL T,   OE_TRANSACTION_TYPES_all B WHERE B.TRANSACTION_TYPE_ID = T.TRANSACTION_TYPE_ID AND T.LANGUAGE              = 'US' and b.order_category_code ='ORDER'  and b.TRANSACTION_TYPE_CODE='ORDER' and T.NAME  = 'Van Sales'";
    public static final String CUSTOMERS_QUERY = "SELECT unique cust.cust_account_id customer_id,  PARTY.PARTY_NAME ,party.party_id FROM HZ_CUST_ACCOUNTS CUST, HZ_PARTIES PARTY  WHERE cust.party_id = party.party_id  AND cust.status = 'A' AND party.status = 'A' AND EXISTS (SELECT '1'  FROM FND_LOOKUP_VALUES_VL fnd  WHERE LOOKUP_TYPE = 'XXASF_OM_VAN_SALES'  AND attribute1 = :userName   AND fnd.DESCRIPTION = PARTY.PARTY_NAME) order by PARTY.PARTY_NAME asc";
    public static final String CUSTOMERSITE_QUERY = "SELECT UNIQUE LOC.CITY ADDRESS, PARTYSITE.PARTY_SITE_NAME ADDRESS_LOC,PARTYSITE.PARTY_SITE_ID LOCATION_ID FROM HZ_CUST_ACCOUNTS CUST, HZ_PARTIES PARTY,  HZ_PARTY_SITES PARTYSITE,  hz_locations loc WHERE cust.party_id = party.party_id AND PARTYSITE.PARTY_ID = PARTY.PARTY_ID AND PARTYSITE.LOCATION_ID = LOC.LOCATION_ID and party.party_name = :party_name  AND PARTYSITE.status = 'A' AND cust.status = 'A' AND party.status = 'A' AND LOC.CITY IN (SELECT UNIQUE TAG  FROM FND_LOOKUP_VALUES_VL WHERE LOOKUP_TYPE = 'XXASF_OM_VAN_SALES'  AND ATTRIBUTE1= :userName  AND DESCRIPTION =:party_name) AND EXISTS (SELECT '1'  FROM FND_LOOKUP_VALUES_VL fnd  WHERE LOOKUP_TYPE = 'XXASF_OM_VAN_SALES'  AND ATTRIBUTE1= :userName  AND FND.DESCRIPTION = PARTY.PARTY_NAME)";
    public static final String PAYMENTMETHOD_QUERY = "SELECT DISTINCT(NVL(PARTYSITE.attribute2,'Cash')) payment_method  FROM HZ_PARTY_SITES PARTYSITE  WHERE partysite.party_site_id = :locationId ";
    public static final String TRANSTYPE_QUERY = "select LOOKUP_CODE Trans from FND_LOOKUP_VALUES_VL where lookup_type='XX_VANSALES_TYPE'";
    public static final String MOV_ITEMS_QUERY = "select * from XXASF_VANSALES_RETURNS_HDR where vehicle_no=:vehicleNo order by description";
    public static final String SALESENTRY_ITEMS_QUERY = "SELECT item_num, item_id,item_uom,header_id,(on_hand_qty - sold_qty) AS qtyOnHand,XXASF_VANSALES_PACKAGE.asf_discount_price ( :p_customer_id,:p_cust_site_id,:p_organization_id,item_id) price,description || ' ' || item_uom || ' ' || (on_hand_qty - sold_qty) || '@' || XXASF_VANSALES_PACKAGE.asf_discount_price ( :p_customer_id,:p_cust_site_id, :p_organization_id, item_id) || '/' || NVL ((SELECT TAX_RATE FROM xxasf_vansales_item_tax_v WHERE organization_id = :p_organization_id AND inventory_item_id = item_id),5) || '%' description, NVL ((SELECT TAX_RATE FROM xxasf_vansales_item_tax_v WHERE organization_id = :p_organization_id AND inventory_item_id = item_id), 5) AS itemVat, (SELECT COUNT (qp.list_header_id) FROM hz_cust_acct_sites_all hcas, hz_cust_site_uses_all hcsu, qp_secu_list_headers_v qp WHERE hcas.cust_acct_site_id = hcsu.cust_acct_site_id AND hcsu.price_list_id = qp.list_header_id AND NVL (qp.end_date_active, TRUNC (SYSDATE)) >= TRUNC (SYSDATE) AND active_flag = 'Y' AND hcas.party_site_id = :p_cust_site_id AND site_use_code = 'SHIP_TO') price_cnt FROM XXASF_VANSALES_TRANS_HDR WHERE vehicle_no = :vehicleNo AND ((on_hand_qty - sold_qty) > 0) ORDER BY description";
    public static final String CHECK_SALESENTRY_ITEM_DISCOUNT_QUERY = "SELECT\n" +
"    CASE\n" +
"        WHEN ( trunc(list_price, 3) -  xxasf_vansales_package.asf_discount_price(:p_customer_id, :p_cust_site_id, :p_organization_id, :p_item_id)) > 0 THEN\n" +
"            'true'\n" +
"        ELSE\n" +
"            'false'\n" +
"    END status\n" +
"FROM\n" +
"    oe_price_list_lines\n" +
"WHERE\n" +
"    nvl(end_date_active, SYSDATE) >= SYSDATE\n" +
"    AND inventory_item_id = :p_item_id\n" +
"    AND price_list_id IN (\n" +
"        SELECT\n" +
"            hcsu.price_list_id\n" +
"        FROM\n" +
"            hz_party_sites           hps,\n" +
"            hz_cust_acct_sites_all   hcas,\n" +
"            hz_cust_accounts         hca,\n" +
"            hz_cust_site_uses_all    hcsu\n" +
"        WHERE\n" +
"            1 = 1\n" +
"            AND hps.party_site_id = hcas.party_site_id\n" +
"            AND hca.cust_account_id = hcas.cust_account_id\n" +
"            AND hps.status != 'M'\n" +
"            AND hcas.cust_acct_site_id = hcsu.cust_acct_site_id\n" +
"            AND hcsu.site_use_code = 'SHIP_TO'\n" +
"    AND hps.status = 'A'\n" +
"            AND hcas.status = 'A'\n" +
"            AND hcsu.status = 'A'\n" +
"            AND hca.cust_account_id = :p_customer_id\n" +
"            AND hps.party_site_id = :p_cust_site_id\n" +
"    )";
    public static final String ORGANIZATIONS_QUERY = "select ORGANIZATION_CODE,ORGANIZATION_ID,ORGANIZATION_name from ORG_ORGANIZATION_DEFINITIONS";
    public static final String SUBINVENTORY_QUERY = "select  mil.SUBINVENTORY_CODE from MTL_ITEM_LOCATIONS mil where  mil.enabled_flag='Y'  and mil.organization_id = :p_organization_id and mil.status_id=1";
    public static final String SUBINV_LOCATION_QUERY = "select milk.CONCATENATED_SEGMENTS ,milk.INVENTORY_LOCATION_ID from MTL_ITEM_LOCATIONS mil,MTL_ITEM_LOCATIONS_KFV milk  where  mil.enabled_flag='Y'  and mil.INVENTORY_LOCATION_ID=milk.INVENTORY_LOCATION_ID  and milk.organization_id=mil.organization_id  and mil.status_id=1  and milk.organization_id = :organization_id and mil.SUBINVENTORY_CODE=:subinventory_code";
    static final String INVOICE_SEQUENCE_QUERY = "Select XXASF_VS_INVOICE_NUM_S.nextval from dual";
    // Dinesh Vat
    // dtl.ORDER_CATEGORY_CODE- Duplicate column
    // public static final String CONFIRM_ITEMS_QUERY = "SELECT dtl.CUST_ACCOUNT_ID, dtl.CUSTOMER_NAME, dtl.TRANSACTION_TYPE,dtl.ORDER_TYPE_ID, dtl.CUSTOMER_SITE, dtl.party_site_id, dtl.ORDER_CATEGORY_CODE, dtl.PAYMENT_METHOD, dtl.header_id, dtl.description, dtl.item_uom, (hdr.on_hand_qty-hdr.sold_qty) AS qtyOnHand, dtl.item_price, dtl.foc_item_flag, dtl.item_num, dtl.sold_qty, dtl.TAX_RATE as VATRate, ((dtl.TAX_RATE/100)*dtl.item_price) as per_vat_rate, (dtl.item_price*dtl.sold_qty)as Item_amount,(dtl.TAX_RATE/100)*(dtl.item_price*dtl.sold_qty) as vat_amount FROM XXASF_VANSALES_TRANS_DTL dtl, XXASF_VANSALES_TRANS_HDR hdr WHERE hdr.item_id =dtl.item_id AND dtl.header_id=hdr.header_id AND hdr.vehicle_no =:userName AND dtl.invoice_num=:invoiceNum order by description";
    public static final String CONFIRM_ITEMS_QUERY = 
    "SELECT CUST_ACCOUNT_ID, CUSTOMER_NAME, TRANSACTION_TYPE, ORDER_TYPE_ID, CUSTOMER_SITE, PARTY_SITE_ID, ORDER_CATEGORY_CODE, PAYMENT_METHOD, HEADER_ID, DESCRIPTION, ITEM_UOM, QTYONHAND, ITEM_PRICE, FOC_ITEM_FLAG, ITEM_NUM, VEHICLE_NO, INVOICE_NUM, SOLD_QTY, VATRATE, PER_VAT_RATE, ITEM_AMOUNT, VAT_AMOUNT FROM XXASF_CONFIRM_ITEMS_V where vehicle_no =:userName AND invoice_num=:invoiceNum";

    // dtl.ORDER_CATEGORY_CODE, - Duplicate column
    // public static final String CONFIRM_ITEMS_RETURN_QUERY = "SELECT dtl.CUST_ACCOUNT_ID, dtl.CUSTOMER_NAME, dtl.TRANSACTION_TYPE,dtl.ORDER_TYPE_ID, dtl.CUSTOMER_SITE, dtl.party_site_id, dtl.ORDER_CATEGORY_CODE, dtl.PAYMENT_METHOD, dtl.header_id, dtl.description, dtl.item_uom, (hdr.on_hand_qty-hdr.sold_qty) AS qtyOnHand, dtl.item_price, dtl.foc_item_flag, dtl.item_num, dtl.sold_qty , dtl.TAX_RATE as VATRate, ((dtl.TAX_RATE/100)*dtl.item_price) as per_vat_rate, (dtl.item_price*dtl.sold_qty)as Item_amount,(dtl.TAX_RATE/100)*(dtl.item_price*dtl.sold_qty) as vat_amount FROM XXASF_VANSALES_TRANS_DTL dtl, XXASF_VANSALES_RETURNS_HDR hdr WHERE hdr.item_id =dtl.item_id AND dtl.header_id=hdr.header_id AND hdr.vehicle_no =:userName AND dtl.invoice_num=:invoiceNum order by description";
    public static final String CONFIRM_ITEMS_RETURN_QUERY ="SELECT CUST_ACCOUNT_ID, CUSTOMER_NAME, TRANSACTION_TYPE, ORDER_TYPE_ID, CUSTOMER_SITE, PARTY_SITE_ID, ORDER_CATEGORY_CODE, PAYMENT_METHOD, HEADER_ID, DESCRIPTION, VEHICLE_NO, INVOICE_NUM, ITEM_UOM, QTYONHAND, ITEM_PRICE, FOC_ITEM_FLAG, ITEM_NUM, SOLD_QTY, VATRATE, PER_VAT_RATE, ITEM_AMOUNT, VAT_AMOUNT FROM XXASF_CONFIRM_ITEMS_RETURN_V where vehicle_no = :userName AND invoice_num = :invoiceNum";
    
    // --Dinesh vat column added
    // public static final String REVIEW_ITEMS_QUERY = "SELECT dtl.CUST_ACCOUNT_ID, dtl.CUSTOMER_NAME, dtl.TRANSACTION_TYPE,dtl.ORDER_TYPE_ID, dtl.CUSTOMER_SITE, dtl.party_site_id, dtl.ORDER_CATEGORY_CODE, dtl.PAYMENT_METHOD, dtl.header_id, dtl.item_id, dtl.ORDER_CATEGORY_CODE, dtl.description, dtl.item_uom, (hdr.on_hand_qty-hdr.sold_qty) AS qtyOnHand, dtl.item_price, dtl.foc_item_flag, dtl.item_num, dtl.sold_qty, xxasf_vansales_package.vansales_tax_rate(hdr.vehicle_no,dtl.customer_name) AS VATRate, ((dtl.TAX_RATE/100)*dtl.item_price) as per_vat_rate, (dtl.item_price*dtl.sold_qty)as Item_amount,(dtl.TAX_RATE/100)*(dtl.item_price*dtl.sold_qty) as vat_amount FROM XXASF_VANSALES_TRANS_DTL_TEMP dtl, XXASF_VANSALES_TRANS_HDR hdr WHERE hdr.item_id =dtl.item_id AND dtl.header_id=hdr.header_id AND hdr.vehicle_no =:userName AND dtl.invoice_num=:invoiceNum order by description";
    public static final String REVIEW_ITEMS_QUERY = "SELECT CUST_ACCOUNT_ID, CUSTOMER_NAME, TRANSACTION_TYPE, ORDER_TYPE_ID, CUSTOMER_SITE, PARTY_SITE_ID, ORDER_CATEGORY_CODE, PAYMENT_METHOD, HEADER_ID, ITEM_ID, DESCRIPTION, ITEM_UOM, QTYONHAND, ITEM_PRICE, FOC_ITEM_FLAG, ITEM_NUM, VEHICLE_NO, INVOICE_NUM, SOLD_QTY, VATRATE, PER_VAT_RATE, ITEM_AMOUNT, VAT_AMOUNT FROM XXASF_REVIEW_ITEMS_QUERY_V where vehicle_no = :userName AND invoice_num = :invoiceNum";
    
    // public static final String REVIEW_ITEMS_RETURN_QUERY = "SELECT CUST_ACCOUNT_ID, CUSTOMER_NAME, TRANSACTION_TYPE,ORDER_TYPE_ID, CUSTOMER_SITE, party_site_id, ORDER_CATEGORY_CODE, PAYMENT_METHOD, header_id, ORDER_CATEGORY_CODE, description, item_uom, (on_hand_qty-sold_qty) AS qtyOnHand, item_price, foc_item_flag, item_num, sold_qty FROM XXASF_VANSALES_TRANS_DTL_TEMP WHERE vehicle_no=:userName AND invoice_num=:invoiceNum order by description";
    public static final String DELETE_REVIEW_ITEMS_EXIST = "DELETE FROM XXASF_VANSALES_TRANS_DTL_TEMP WHERE invoice_num = ?";
    public static final String CONFIRM_TOTAL_AMOUNT_QUERY = "select sum(round(NVL(sold_qty,0)*NVL(item_price,0),3)) from XXASF_VANSALES_TRANS_DTL where vehicle_no = :userName and invoice_num = :invoiceNum order by description";
    //Dinesh VAT Column
    public static final String REVIEW_TOTAL_AMOUNT_QUERY = "select sum(round(NVL(sold_qty,0)*NVL(item_price,0),3)) from XXASF_VANSALES_TRANS_DTL_TEMP where vehicle_no = :userName and invoice_num = :invoiceNum order by description";
    public static final String ORDER_INVENTORY_ITEMS_QUERY = "select Item_num,description,item_uom,header_id,item_id,(on_hand_qty-sold_qty) remaining_qty  from XXASF_VANSALES_TRANS_HDR where vehicle_no=:vehicleNo AND ((on_hand_qty-sold_qty) > 0) order by description";
    public static final String DELETE_ITEMS_QUERY = "delete from XXASF_VANSALES_TRANS_DTL where INVOICE_NUM=?";
    //Dinesh VAT Column
    public static final String ORDER_ITEMS_QUERY = "SELECT item_num, item_id, item_uom, header_id, XXASF_VANSALES_PACKAGE.asf_discount_price ( :p_customer_id, :p_cust_site_id, :p_organization_id, item_id) price, description || ' '|| item_uom || ' ' || '@' || XXASF_VANSALES_PACKAGE.asf_discount_price ( :p_customer_id, :p_cust_site_id, :p_organization_id, item_id) || '/' || NVL ((SELECT TAX_RATE FROM xxasf_vansales_item_tax_v WHERE organization_id = :p_organization_id AND inventory_item_id = item_id), 5) || '%' description, NVL ((SELECT TAX_RATE FROM xxasf_vansales_item_tax_v WHERE organization_id = :p_organization_id AND inventory_item_id = item_id), 5) AS itemVat, (SELECT COUNT (qp.list_header_id) FROM hz_cust_acct_sites_all hcas, hz_cust_site_uses_all hcsu, qp_secu_list_headers_v qp WHERE hcas.cust_acct_site_id = hcsu.cust_acct_site_id AND hcsu.price_list_id = qp.list_header_id AND NVL (qp.end_date_active, TRUNC (SYSDATE)) >= TRUNC (SYSDATE) AND active_flag = 'Y' AND hcas.party_site_id = :p_cust_site_id AND site_use_code = 'SHIP_TO') price_cnt FROM XXASF_VANSALES_RETURNS_HDR WHERE vehicle_no = :vehicleNo ORDER BY description";
    public static final String CANCEL_ITEMS_QUERY = "SELECT UNIQUE TRUNC(CREATION_DATE) CREATION_DATE, VEHICLE_NO, CUSTOMER_NAME, (SELECT hl.city FROM hz_parties hp, hz_locations hl, hz_party_sites hps, hz_cust_acct_sites_all hcas, hz_cust_accounts hca, hz_cust_site_uses_all hcsu WHERE 1=1 AND hp.party_id= hps.party_id AND hl.location_id=hps.location_id AND hps.party_site_id = hcas.party_site_id AND hca.cust_account_id = hcas.cust_account_id AND hps.status!= 'M' AND hcas.cust_acct_site_id = hcsu.cust_acct_site_id AND hcsu.site_use_code = 'SHIP_TO' AND hps.status = 'A' AND hcas.status = 'A' AND hp.status = 'A' AND hcsu.status = 'A' AND hca.cust_account_id = dtl.cust_account_id AND hps.party_site_id = DTL.customer_site) CUSTOMER_SITE, INVOICE_NUM, RECEIPT_NUM FROM XXASF_VANSALES_TRANS_DTL DTL WHERE DTL.PROCESS_FLAG = 'N' AND DTL.CUST_ACCOUNT_ID = :p_customer_id AND DTL.CUSTOMER_SITE = :p_cust_site_id AND VEHICLE_NO =:vehicleNo AND CREATION_DATE LIKE SYSDATE ORDER BY INVOICE_NUM";
    public static final String UPDATE_CUSTOMER_QUERY = "UPDATE XXASF_VANSALES_TRANS_DTL SET AR_INVOICE_NUM=?, RECEIPT_AMT=?  WHERE INVOICE_NUM=?";
    public static final String ORDER_NUMBER_QUERY = "select distinct order_number from XXASF_VANSALES_TRANS_DTL where invoice_num =:invoice_num ";
    public static final String RENTAL_KNOCK_OFF_QUERY = "SELECT lookup_code Code, Description, Meaning FROM fnd_lookup_values WHERE ENABLED_FLAG='Y' and lookup_type = 'XX_VANSALES_REASON' AND language = 'US'";
    public static final String DELETE_HRD_ITEMS_QUERY = "Delete from XXASF_VANSALES_TRANS_HDR where vehicle_no =?";
    public static final String SALES_INVOICE_BROWSER_QUERY = "SELECT UNIQUE TRUNC(CREATION_DATE) CREATION_DATE, VEHICLE_NO, CUSTOMER_NAME, (SELECT hl.city FROM hz_parties hp, hz_locations hl, hz_party_sites hps, hz_cust_acct_sites_all hcas, hz_cust_accounts hca, hz_cust_site_uses_all hcsu WHERE 1=1 AND hp.party_id= hps.party_id AND hl.location_id=hps.location_id AND hps.party_site_id = hcas.party_site_id AND hca.cust_account_id = hcas.cust_account_id AND hps.status!= 'M' AND hcas.cust_acct_site_id = hcsu.cust_acct_site_id AND hcsu.site_use_code = 'SHIP_TO' AND hps.status = 'A' AND hcas.status = 'A' AND hp.status = 'A' AND hcsu.status = 'A' AND hca.cust_account_id = dtl.cust_account_id AND hps.party_site_id = dtl.customer_site) CUSTOMER_SITE, INVOICE_NUM, RECEIPT_NUM FROM XXASF_VANSALES_TRANS_DTL DTL WHERE DTL.PROCESS_FLAG != 'C' AND ORDER_CATEGORY_CODE = :invoiceType AND VEHICLE_NO =:vehicleNo AND TRUNC(CREATION_DATE)=TO_DATE(:loadInvoiceDate,'DD-MON-YYYY') ORDER BY CREATION_DATE DESC";
    public static final String SALES_INVOICE_BROWSER_QUERY_CANCELLED_ITEMS = "SELECT UNIQUE TRUNC(CREATION_DATE) CREATION_DATE, VEHICLE_NO, CUSTOMER_NAME, (SELECT hl.city FROM hz_parties hp, hz_locations hl, hz_party_sites hps, hz_cust_acct_sites_all hcas, hz_cust_accounts hca, hz_cust_site_uses_all hcsu WHERE 1=1 AND hp.party_id= hps.party_id AND hl.location_id=hps.location_id AND hps.party_site_id = hcas.party_site_id AND hca.cust_account_id = hcas.cust_account_id AND hps.status!= 'M' AND hcas.cust_acct_site_id = hcsu.cust_acct_site_id AND hcsu.site_use_code = 'SHIP_TO' AND hps.status = 'A' AND hcas.status = 'A' AND hp.status = 'A' AND hcsu.status = 'A' AND hca.cust_account_id = dtl.cust_account_id AND hps.party_site_id = DTL.customer_site) CUSTOMER_SITE, INVOICE_NUM, RECEIPT_NUM FROM XXASF_VANSALES_TRANS_DTL DTL WHERE  DTL.VEHICLE_NO = :vehicleNo AND DTL.PROCESS_FLAG = 'C' AND TRUNC(CREATION_DATE)=TO_DATE(:loadInvoiceDate,'DD-MON-YYYY') ORDER BY INVOICE_NUM";
    public static final String SALES_INVOICE_BROWSER_MOVE_ORDER = ""
            + "select distinct order_number from XXASF_VANSALES_TRANS_DTL\n"
            + "where \n"
            + "order_number is not null\n"
            + "and order_category_code = :menuType\n"
            + "and trunc(CREATION_DATE) = trunc(to_date(:orderReqDate))\n"
            + "and vehicle_no = :vehicleNo\n"
            + "order by order_number";
    public static final String SALES_INVOICE_BROWSER_MOVE_ORDER_ITEMS = ""
            + "select description, sold_qty from apps.xxasf_vansales_trans_dtl \n"
            + "where order_number = trunc(:orderNo)\n"
            + "order by description";
    public static final String UPDATE_RECEIPT_NUM_QUERY = "UPDATE XXASF_VANSALES_TRANS_DTL SET RECEIPT_NUM=? WHERE INVOICE_NUM=?";
    public static final String CHECK_REFERENCE_NUM_QUERY = "SELECT REFERENCE_NO FROM XXASF_VANSALES_TRANS_DTL WHERE REFERENCE_NO=:referenceNumber ";
    public static final String CHECK_INVOICE_NUM_QUERY = "SELECT INVOICE_NUM FROM XXASF_VANSALES_TRANS_DTL WHERE INVOICE_NUM=:invoiceNumber OR INVOICE_NUM='M'||:invoiceNumber";
    public static final String POP_UP_MESSAGE_TEXT_AR = "SELECT MESSAGE_TEXT FROM FND_NEW_MESSAGES WHERE message_name =:messageName and  message_number ='1' and language_code ='AR'";
    public static final String POP_UP_MESSAGE_TEXT_US = "SELECT MESSAGE_TEXT FROM FND_NEW_MESSAGES WHERE message_name =:messageName and  message_number ='1' and language_code ='US'";

    // Dinesh
    public static final String VAN_DRIVER_TAX="SELECT xxasf_vansales_package.vansales_tax_rate(:p_username,:p_cust_name) as TAX_RATE FROM dual";
    

    public static final String REVIEW_TOTAL_VAT_QUERY = 
    "SELECT round(sum(( NVL(tax_rate,0) / 100 ) * ( NVL(item_price,0) * NVL(sold_qty,0) )),3) as INV_VAT_AMT "+
    " FROM xxasf_vansales_trans_dtl_temp WHERE "+
    " vehicle_no = :userName AND invoice_num = :invoiceNum ORDER BY description";

    public static final String REVIEW_TOTAL_NET_QUERY =
    "SELECT SUM(NVL(sold_qty,0) * NVL(item_price,0)) + round(sum(( NVL(tax_rate,0) / 100 ) * ( NVL(item_price,0) * NVL(sold_qty,0) )),3) AS  INV_NET_AMT "+
    " FROM xxasf_vansales_trans_dtl_temp WHERE "+
    " vehicle_no = :userName AND invoice_num = :invoiceNum ORDER BY description";

    public static final String CONFIRM_TOTAL_VAT_QUERY = 
    "SELECT round(sum(( NVL(tax_rate,0) / 100 ) * ( NVL(item_price,0) * NVL(sold_qty,0) )),3) as INV_VAT_AMT "+
    " FROM XXASF_VANSALES_TRANS_DTL WHERE "+
    " vehicle_no = :userName AND invoice_num = :invoiceNum ORDER BY description";

    public static final String CONFIRM_TOTAL_NET_QUERY =
    "SELECT SUM(NVL(sold_qty,0) * NVL(item_price,0)) + round(sum(( NVL(tax_rate,0) / 100 ) * ( NVL(item_price,0) * NVL(sold_qty,0) )),3) AS  INV_NET_AMT "+
    " FROM XXASF_VANSALES_TRANS_DTL WHERE "+
    " vehicle_no = :userName AND invoice_num = :invoiceNum ORDER BY description";




}
