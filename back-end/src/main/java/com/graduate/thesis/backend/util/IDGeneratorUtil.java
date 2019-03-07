package com.graduate.thesis.backend.util;


import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

/**
 * @author Huy Pham
 */
public class IDGeneratorUtil extends UUIDGenerator {

    private String entityName;

    @Override
    public void configure(Type type, Properties params, ServiceRegistry dialect) {
        entityName = params.getProperty(ENTITY_NAME);
        super.configure(type, params, dialect);
    }


    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {

        Serializable id = session.getEntityPersister(entityName, object).getIdentifier(object, session);

        if (id == null) {
            return CommonUtil.generateUUID();
        } else {
            return id;
        }
    }
}
