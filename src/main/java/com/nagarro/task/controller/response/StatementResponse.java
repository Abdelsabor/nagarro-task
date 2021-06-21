package com.nagarro.task.controller.response;

import java.time.LocalDate;

public class StatementResponse {

	private Integer accountId;
	private LocalDate date;
	private Double amount;

	public StatementResponse(Integer accountId, LocalDate date, Double amount) {
		this.accountId = accountId;
		this.date = date;
		this.amount = amount;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}
