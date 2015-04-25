package dao;

public interface IStockDatabaseContainerFactory {
	IStockDatabaseContainer getInstance(String id) throws Exception;
}
