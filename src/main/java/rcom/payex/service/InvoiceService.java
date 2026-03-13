package rcom.payex.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rcom.payex.dao.InvoiceTransactionDao;
import rcom.payex.model.TransactionCore;

@Service
public class InvoiceService {
	
	@Autowired
	InvoiceTransactionDao invoiceTransactionDao;

	@Transactional(readOnly = true)
	public List<TransactionCore> getTransactions(String payexPaymentId) {
		List<Long> list = Arrays.stream(payexPaymentId.split(","))
                .map(s -> Long.parseLong(s.trim()))
                .collect(Collectors.toList());
		return invoiceTransactionDao.getTransactions(list);		
	}
}
