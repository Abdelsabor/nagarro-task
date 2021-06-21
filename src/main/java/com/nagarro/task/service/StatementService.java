package com.nagarro.task.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.nagarro.task.controller.response.StatementResponse;
import com.nagarro.task.exceptions.CommonNagarroException;

@Service
public class StatementService {

	private static final Logger logger = LoggerFactory.getLogger(StatementService.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<StatementResponse> getAccountStatment(Long accountId, LocalDate dateFrom, LocalDate dateTo,
			Double amoutFrom, Double amountTo) {

		String sql = queryBulider(accountId, amoutFrom, amountTo);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		try {
			List<StatementResponse> fullStatement = jdbcTemplate.query(sql, new RowMapper<StatementResponse>() {

				@Override
				public StatementResponse mapRow(ResultSet rs, int rowNum) throws SQLException {

					String dateStr = rs.getString("datefield");
					dateStr = dateStr.replace('.', '/');
					LocalDate statementDate = LocalDate.parse(dateStr, formatter);

					Long accountId = rs.getLong("account_id");
					return new StatementResponse(hash(accountId.intValue()), statementDate,
							Double.parseDouble(rs.getString("amount")));
				}

			});

			fullStatement = filterStatementDate(fullStatement, dateFrom, dateTo);
			return filterStatementAmount(fullStatement, amoutFrom, amountTo);
		} catch (Exception e) {
			logger.error("Error while calling DB {} ", e);
			throw new CommonNagarroException("Error While Calling DB");
		}

	}

	private List<StatementResponse> filterStatementDate(List<StatementResponse> fullStatement, LocalDate dateFrom,
			LocalDate dateTo) {

		List<StatementResponse> result = new ArrayList<>();

		if (dateFrom == null && dateTo == null) {
			dateFrom = LocalDate.now().minusMonths(3L);
			dateTo = LocalDate.now();
		}

		if (dateFrom != null && dateTo != null) {

			for (StatementResponse i : fullStatement) {
				if (i.getDate().isAfter(dateFrom) && i.getDate().isBefore(dateTo)) {
					result.add(i);
				}
			}
		} else if (dateFrom != null) {
			for (StatementResponse i : fullStatement) {
				if (i.getDate().isAfter(dateFrom)) {
					result.add(i);
				}
			}
		} else if (dateTo != null) {
			for (StatementResponse i : fullStatement) {
				if (i.getDate().isBefore(dateTo)) {
					result.add(i);
				}
			}
		}
		return result;
	}

	private List<StatementResponse> filterStatementAmount(List<StatementResponse> fullStatement, Double amountFrom,
			Double amountTo) {

		if (amountFrom == null && amountTo == null) {
			return fullStatement;
		}

		List<StatementResponse> result = new ArrayList<>();

		if (amountFrom != null && amountTo != null) {

			for (StatementResponse i : fullStatement) {
				if (i.getAmount() >= amountFrom && i.getAmount() <= amountTo) {
					result.add(i);
				}
			}
		} else if (amountFrom != null) {
			for (StatementResponse i : fullStatement) {
				if (i.getAmount() >= amountFrom) {
					result.add(i);
				}
			}
		} else if (amountTo != null) {
			for (StatementResponse i : fullStatement) {
				if (i.getAmount() <= amountTo) {
					result.add(i);
				}
			}
		}
		return result;
	}

	private String queryBulider(Long accountId, Double amountFrom, Double amountTo) {
		StringBuilder sb = new StringBuilder("select * from statement");

		if (accountId != null) {
			sb.append(" where account_id = ").append(accountId);
		}

		return sb.toString();

	}

	private int hash(int num) {
		return num * 123;
	}

}
