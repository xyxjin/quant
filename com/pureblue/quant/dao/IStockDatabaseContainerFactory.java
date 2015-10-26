package com.pureblue.quant.dao;

public interface IStockDatabaseContainerFactory {
	IStockDatabaseContainer getInstance(String id) throws Exception;
}
