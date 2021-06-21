package com.nagarro.task;

import static org.mockito.Mockito.when;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;
import com.nagarro.task.controller.response.StatementResponse;
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

	@Test
	public void testGetAccountStatmentWithNullDateFrom() {

		List<StatementResponse> response = new ArrayList<StatementResponse>();
		response.add(new StatementResponse(1, LocalDate.now().minusDays(5L), 100.0d));
		response.add(new StatementResponse(1, LocalDate.now().minusDays(1L), 100.0d));

		when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(response);

		List<StatementResponse> response1 = statementService.getAccountStatment(1L, null, LocalDate.now().minusDays(3L),
				3.0d, 3.0d);

		Assert.assertEquals(1, response1.size());
	}

	@Test
	public void testGetAccountStatmentWithNullDateTo() {

		List<StatementResponse> response = new ArrayList<StatementResponse>();
		response.add(new StatementResponse(1, LocalDate.now().minusDays(2L), 100.0d));
		response.add(new StatementResponse(1, LocalDate.now().minusDays(1L), 100.0d));

		RowMapper<StatementResponse> mapper = new RowMapper<StatementResponse>() {

			@Override
			public StatementResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
		};

		when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(response);

		List<StatementResponse> response1 = statementService.getAccountStatment(1L, LocalDate.now().minusDays(3L), null,
				3.0d, 3.0d);

		Assert.assertEquals(2, response1.size());
	}

}