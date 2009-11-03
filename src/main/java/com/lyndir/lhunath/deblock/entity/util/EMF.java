package com.lyndir.lhunath.deblock.entity.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;


public abstract class EMF {

    private static final EntityManagerFactory emfInstance = Persistence.createEntityManagerFactory( "transactions-optional" );
    private static EntityManager              em;


    public static EntityManager getEm() {

        if (em == null || !em.isOpen())
            // No valid entity manager instance yet, create one.
            em = emfInstance.createEntityManager();

        return em;
    }

    /**
     * Clean up and close an active entity manager.
     * 
     * <p>
     * Any active transaction will be terminated according to the <code>success</code> parameter.
     * </p>
     * 
     * @param success
     *            Indicates whether the logic completed successfully. If <code>true</code>, an active transaction will
     *            be committed, if <code>false</code> an active transaction will be rolled back.
     */
    public static void closeEm(boolean success) {

        if (em != null) {
            if (em.isOpen()) {

                // Commit active transaction.
                EntityTransaction transaction = em.getTransaction();
                if (transaction != null && transaction.isActive())
                    if (success)
                        transaction.commit();
                    else
                        transaction.rollback();

                // Close the entity manager.
                em.close();
            }

            // Unset the entity manager instance, it must not be used again.
            em = null;
        }
    }
}
