/*
 * Copyright 2016 The Johns Hopkins University Applied Physics Laboratory LLC
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.jhuapl.dorset.reporting;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import edu.jhuapl.dorset.reporting.Report;
import edu.jhuapl.dorset.reporting.ReportQuery;
import edu.jhuapl.dorset.reporting.Reporter;

/**
 * SQL Reporter
 *
 * Stores reports of request handling to an SQL database.
 */
public class SqlReporter implements Reporter {

    private SessionFactory sessionFactory;

    /**
     * Create a SQL Reporter
     * 
     * @param conf Hibernate configuration
     */
    public SqlReporter(Configuration conf) {
        conf.addResource("report.hbm.xml");
        sessionFactory = conf.buildSessionFactory();
    }

    @Override
    public void store(Report report) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(report);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Report[] retrieve(ReportQuery query) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query hql = session.createQuery(buildQuery(query))
                        .setParameter("ts_start", query.getStartDate())
                        .setParameter("ts_stop", query.getEndDate());
        if (query.getAgentNames() != null) {
            hql.setParameterList("agents", query.getAgentNames());
        }
        if (query.getLimit() != ReportQuery.NO_LIMIT) {
            hql.setMaxResults(query.getLimit());
        }
        @SuppressWarnings("unchecked")
        List<Report> reports = hql.list();
        session.getTransaction().commit();
        session.close();
        return reports.toArray(new Report[reports.size()]);
    }

    private String buildQuery(ReportQuery query) {
        String hql = "from Report where (:ts_start is null or timestamp > :ts_start)" 
                        + " and (:ts_stop is null or timestamp < :ts_stop)";
        if (query.getAgentNames() != null) {
            hql += " and selectedAgentName in (:agents)";
        }
        return hql;
    }
}
