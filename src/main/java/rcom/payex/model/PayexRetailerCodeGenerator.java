package rcom.payex.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class PayexRetailerCodeGenerator implements IdentifierGenerator{


    private static Logger log = LogManager.getLogger(PayexRetailerCodeGenerator.class);

	
	public Serializable generate(SessionImplementor session, Object arg1) throws HibernateException {

        String prefix = "MH0MUM0";
        Connection connection = session.connection();
        // ps = null;
        ResultSet rs=null;
        try(PreparedStatement ps = connection
        		.prepareStatement("SELECT SEQ_PAYEX_RETAILER_CODE.nextval as nextval from dual")) {
           /* ps = connection
            		.prepareStatement("SELECT SEQ_PAYEX_RETAILER_CODE.nextval as nextval from dual");*/
                    //.prepareStatement("SELECT seq_payment_ref_no') as nextval");

            rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("nextval");
                String code = prefix + id;
                
                 log.debug("Generated Stock Code: " + code);
                return code;
            }
        } catch (SQLException e) {
            log.error(e);
            throw new HibernateException("Unable to generate Stock Code Sequence");
        }finally {
        	try {
        		if(rs!=null)
        			rs.close();
        		
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}        
        return null;
    }

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object arg1) throws HibernateException {
		String prefix = "MH0MUM0";
        Connection connection = session.connection();
        // ps = null;
        ResultSet rs=null;
        try(PreparedStatement ps = connection
        		.prepareStatement("SELECT SEQ_PAYEX_RETAILER_CODE.nextval as nextval from dual")) {
           /* ps = connection
            		.prepareStatement("SELECT SEQ_PAYEX_RETAILER_CODE.nextval as nextval from dual");*/
                    //.prepareStatement("SELECT seq_payment_ref_no') as nextval");

            rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("nextval");
                String code = prefix + id;
                
                 log.debug("Generated Stock Code: " + code);
                return code;
            }
        } catch (SQLException e) {
            log.error(e);
            throw new HibernateException("Unable to generate Stock Code Sequence");
        }finally {
        	try {
        		if(rs!=null)
        			rs.close();
        		
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}        
        return null;
	}


}
