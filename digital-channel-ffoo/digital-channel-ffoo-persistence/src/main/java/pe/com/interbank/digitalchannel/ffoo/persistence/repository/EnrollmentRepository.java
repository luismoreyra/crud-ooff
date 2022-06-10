package pe.com.interbank.digitalchannel.ffoo.persistence.repository;

import pe.com.interbank.digitalchannel.ffoo.model.Enrollment;
import pe.com.interbank.digitalchannel.ffoo.persistence.exception.RepositoryException;

/**
 * Created by Robert Espinoza on 05/07/2017.
 */
public interface EnrollmentRepository {

    Enrollment findByClientCode(String clientCode) throws RepositoryException;

    void create(Enrollment enrollment) throws RepositoryException;

    void update(Enrollment enrollment) throws RepositoryException;

}
