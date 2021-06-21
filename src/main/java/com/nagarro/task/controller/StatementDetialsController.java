package com.nagarro.task.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nagarro.task.config.AuthoritiesConstants;
import com.nagarro.task.config.ExceptionHandlerResolver;
import com.nagarro.task.controller.response.StatementResponse;
import com.nagarro.task.exceptions.BadRequestParamterException;
import com.nagarro.task.service.StatementService;
import io.swagger.annotations.Api;

@RestController
@Api(tags = "Statement Detials")
public class StatementDetialsController extends ExceptionHandlerResolver {

	private static final Logger logger = LoggerFactory.getLogger(StatementDetialsController.class);

	@Autowired
	private StatementService service;

	@GetMapping("/api/statement/account/{accountId}/search")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<StatementResponse>> getAccountStatment(
			@RequestHeader(value = "Authorization") String authorization, @PathVariable Long accountId,
			@RequestParam(value = "amountFrom", required = false) Double amountFrom,
			@RequestParam(value = "amountTo", required = false) Double amountTo,
			@RequestParam(value = "dateFrom", required = false) String dateFrom,
			@RequestParam(value = "dateTo", required = false) String dateTo) {

		logger.info("Request to search for account Id {}", accountId);

		DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		LocalDate fromDateValue = null;

		try {
			if (dateFrom != null && !dateFrom.isEmpty()) {
				fromDateValue = LocalDate.parse(dateFrom, dateTimeFormat);
			}

		} catch (Exception e) {
			throw new BadRequestParamterException("Date from value is Wrong should be dd-mm-yyyy");
		}

		LocalDate toDateValue = null;

		try {
			if (dateTo != null && !dateTo.isEmpty()) {
				toDateValue = LocalDate.parse(dateTo, dateTimeFormat);
			}

		} catch (Exception e) {
			throw new BadRequestParamterException("Date to value is Wrong should be dd-mm-yyyy");
		}

		return ResponseEntity
				.ok(service.getAccountStatment(accountId, fromDateValue, toDateValue, amountFrom, amountTo));

	}

	@GetMapping("/api/statement")
	@PreAuthorize("hasAnyRole(\"" + AuthoritiesConstants.ROLE_ADMIN + "\") or  hasAnyRole(\""
			+ AuthoritiesConstants.ROLE_USER + "\")")
	public ResponseEntity<List<StatementResponse>> getAccountStatmentForUser(
			@RequestHeader(value = "Authorization") String authorization) {

		logger.info("Request to get statment for last three months");

		return ResponseEntity
				.ok(service.getAccountStatment(null, LocalDate.now().minusMonths(3L), LocalDate.now(), null, null));

	}

}
