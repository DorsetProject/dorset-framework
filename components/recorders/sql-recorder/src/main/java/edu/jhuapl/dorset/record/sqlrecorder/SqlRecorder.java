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
package edu.jhuapl.dorset.record.sqlrecorder;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import edu.jhuapl.dorset.record.Record;
import edu.jhuapl.dorset.record.RecordQuery;
import edu.jhuapl.dorset.record.Recorder;

/**
 * SQL Recorder
 *
 * Stores records of request handling to a sql database.
 */
public class SqlRecorder implements Recorder {

    private SessionFactory sessionFactory;

    public SqlRecorder(Configuration conf) {
        this.sessionFactory = conf.buildSessionFactory();

    }

    @Override
    public void store(Record record) {
        // TODO Auto-generated method stub
        Session session = this.sessionFactory.openSession();
        session.beginTransaction();
        session.save(record);
        session.getTransaction().commit();
    }

    @Override
    public Record[] retrieve(RecordQuery query) {
        // TODO Auto-generated method stub
        return null;
    }
}
