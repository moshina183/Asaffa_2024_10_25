package com.asf.van.dao;

import com.asf.van.model.TransactionType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

public class VanJDBCTemplate extends NamedParameterJdbcDaoSupport {

    private static final Logger LOGGER = Logger.getLogger(VanJDBCTemplate.class);

    @Autowired
    public void setDataSource(JdbcTemplate jdbcTemplate) {
        super.setDataSource(jdbcTemplate.getDataSource());
    }

    public List<TransactionType> getTransactionTypes() {
        List<TransactionType> transactionTypes = null;
        try {
            transactionTypes = getNamedParameterJdbcTemplate().query(SQLStatementsLookup.TRANSACTION_TYPES_QUERY,
                    new RowMapper() {
                @Override
                public TransactionType mapRow(ResultSet rs, int rowNum)
                        throws SQLException {
                    TransactionType transaction = new TransactionType();
                    transaction.setTransactionId(rs.getLong("transaction_id"));
                    transaction.setTransactionName(rs.getString("transaction_name"));
                    return transaction;
                }
            }
            );
        } catch (DataAccessException exp) {
            LOGGER.error(exp.getMessage(), exp);
        }
        return transactionTypes;
    }
}
