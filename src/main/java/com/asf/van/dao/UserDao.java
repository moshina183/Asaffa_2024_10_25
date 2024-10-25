package com.asf.van.dao;

import com.asf.van.model.BusinessHours;
import com.asf.van.model.Time;
import com.asf.van.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends NamedParameterJdbcDaoSupport {

    private static final Logger LOGGER = Logger.getLogger(UserDao.class);

    @Autowired
    public void setDataSource(JdbcTemplate jdbcTemplate) {
        super.setDataSource(jdbcTemplate.getDataSource());
    }

    public User getUserDetails(String username, final String password) {
        Map<String, Object> params = new HashMap();
        params.put("username", username);
        final User user = new User();
        try {
            getNamedParameterJdbcTemplate().query(SQLStatementsLookup.SELECT_USER_QUERY, params, new RowMapper() {
                @Override
                public User mapRow(ResultSet rs, int rowNum)
                        throws SQLException {
                    if (password.equals(rs.getString("PASSWORD"))) {
                        user.setUserId(rs.getLong("USER_ID"));
                        user.setUserName(rs.getString("VEHICLE_NO"));
                        user.setSalesManName(rs.getString("SALESMAN_NAME"));
                        user.setOrganizationId(rs.getLong("ORGANIZATION_ID"));
                        user.setValid(true);
                        LOGGER.info("User is a valid user");
                        return user;
                    }
                    return null;
                }
            });
        } catch (DataAccessException e) {
            LOGGER.error("Error is coming", e);
        }
        return user;
    }

    /**
     * Updated on 17-02-2019 - Balaji Manickam - 4i Apps method to return
     * business hours
     *
     * @return
     */
    public BusinessHours getBusinessHours() {
        List<BusinessHours> businessHours = null;
        try {
            businessHours
                    = getNamedParameterJdbcTemplate().query(SQLStatementsLookup.BUSINESS_HOURS_QUERY,
                            new RowMapper() {
                        BusinessHours hours
                                = new BusinessHours();

                        @Override
                        public BusinessHours mapRow(ResultSet rs, int rowNum)
                                throws SQLException {
                            if (rs.getString("LOOKUP_CODE").equals("START")) {
                                hours.setStartTime(rs.getString("TIMING"));
                            } else if (rs.getString("LOOKUP_CODE").equals("END")) {
                                hours.setEndTime(rs.getString("TIMING"));
                            }
                            return hours;
                        }
                    });
            System.out.println(businessHours.isEmpty());
            if (businessHours.isEmpty()) {
                businessHours.add(new BusinessHours());
            }

        } catch (DataAccessException e) {
            LOGGER.error("Error is coming", e);
            return null;
        }
        return businessHours.get(0);
    }

    public Time getLoginTime(java.util.Date date) {
        Time time = new Time();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int calHour = calendar.get(Calendar.HOUR_OF_DAY);
        int calMin = calendar.get(Calendar.MINUTE);
        String hour = String.valueOf(calHour > 9 ? calHour : "0" + Integer.toString(calHour));
        String min = String.valueOf(calMin > 9 ? calMin : "0" + Integer.toString(calMin));
        time.setHour(hour); // 24-Hours
        time.setMinute(min);
        logger.info("Login Time => " + time.getHour() + ":" + time.getMinute());
        return time;
    }
}
