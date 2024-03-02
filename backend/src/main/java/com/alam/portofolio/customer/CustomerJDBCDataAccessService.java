package com.alam.portofolio.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDAO{

  private final JdbcTemplate jdbcTemplate;
  private final CustomerRowMapper customerRowMapper;

  public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
    this.jdbcTemplate = jdbcTemplate;
    this.customerRowMapper = customerRowMapper;
  }

  @Override
  public List<Customer> selectAllCustomers() {
    var sql = """
        SELECT id,name,email,age
        FROM customer
        """;
    return jdbcTemplate.query(sql, customerRowMapper);
  }

  @Override
  public Optional<Customer> selectCustomerById(Integer customerId) {
    var sql = """
        SELECT * FROM customer WHERE id = ?
        """;

    return jdbcTemplate.query(sql, customerRowMapper, customerId).stream().findFirst();
  }

  @Override
  public void insertCustomer(Customer customer) {
    var sql = """
        INSERT INTO customer (name,email,age)
        VALUES (?,?,?)
        """;
    jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge());
  }

  @Override
  public boolean existsCustomerWithEmail(String email) {
    var sql = "SELECT COUNT(*) FROM customer WHERE email = ?";

    Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
    return count != null && count > 0;
  }

  @Override
  public void deleteCustomerById(Integer customerId) {
    var sql = "DELETE FROM customer WHERE id = ?";
    jdbcTemplate.update(sql, customerId);
  }

  @Override
  public boolean existCustomerWithId(Integer customerId) {
    var sql = "SELECT COUNT(*) FROM customer WHERE id = ?";

    Integer count = jdbcTemplate.queryForObject(sql, Integer.class, customerId);
    return count != null;
  }

  @Override
  public void updateCustomer(Customer customer) {
    var sql = "UPDATE customer SET name = ?, email = ?, age = ? WHERE id = ?";
    jdbcTemplate.update(sql,
        customer.getName(),
        customer.getEmail(),
        customer.getAge(),
        customer.getId()
    );
  }
}
