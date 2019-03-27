package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.repository.JpaUtil;

abstract public class AbstractJpaDataJpaUserServiceTest extends AbstractUserServiceTest {

    @Autowired
    private JpaUtil jpaUtil;

    @Before
    public void setUpHibernateCache() throws Exception {
        jpaUtil.clear2ndLevelHibernateCache();
    }
}