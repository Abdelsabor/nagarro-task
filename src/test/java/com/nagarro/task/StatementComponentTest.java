package com.nagarro.task;

import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;
import com.nagarro.task.exceptions.CommonNagarroException;
import com.nagarro.task.service.StatementService;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.anyString;

@SpringBootTest
@RunWith(SpringRunner.class)
public class StatementComponentTest {

	@MockBean
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private StatementService statementService;

	@SuppressWarnings({ "unchecked" })
	@Test(expected = CommonNagarroException.class)
	public void testGetAccountStatmentWillThrowCommonNagarroException() {

		when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenThrow(RuntimeException.class);

		statementService.getAccountStatment(1L, LocalDate.now(), LocalDate.now(), 3.0d, 3.0d);

	}

}