package com.alam.portofolio.customer;

import com.alam.portofolio.util.Constants;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CustomerRowMapperTest {


    @Test
    void mapRowSuccess() throws SQLException {
        // Given
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("age")).thenReturn(19);
        when(resultSet.getString("name")).thenReturn("alam");
        when(resultSet.getString("email")).thenReturn("alam@gmail.com");
        when(resultSet.getString("gender")).thenReturn("MALE");

        // When
        Customer actual = customerRowMapper.mapRow(resultSet, 1);

        // Then
        Customer expectedCustomer = new Customer(1, "alam", "alam@gmail.com", 19, Constants.Gender.MALE);

        assertThat(actual).isEqualTo(expectedCustomer);
    }

}