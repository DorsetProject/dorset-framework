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
    }

    @Override
    public Report[] retrieve(ReportQuery query) {
        // TODO Auto-generated method stub
        return null;
    }
}
