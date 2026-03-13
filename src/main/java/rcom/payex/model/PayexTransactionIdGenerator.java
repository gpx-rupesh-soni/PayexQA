package rcom.payex.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class PayexTransactionIdGenerator implements IdentifierGenerator {

	
	public Serializable generate(SessionImplementor session, Object object)
			throws HibernateException {
		Connection connection = session.connection();
		ResultSet rs = null;
		try (PreparedStatement ps = connection.prepareStatement("SELECT seq_payex_transaction_id.nextval as nextval from dual")) {
			rs = ps.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("nextval");
				return id;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public Serializable generate(SharedSessionContractImplementor session,	Object arg1) throws HibernateException {
		Connection connection = session.connection();
		ResultSet rs = null;
		try (PreparedStatement ps = connection
				.prepareStatement("SELECT seq_payex_transaction_id.nextval as nextval from dual")) {
			rs = ps.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("nextval");
				return id;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
