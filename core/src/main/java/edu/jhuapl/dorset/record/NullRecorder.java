package edu.jhuapl.dorset.record;

/**
 * A stub recorder that does nothing
 */
public class NullRecorder implements Recorder {

    @Override
    public void store(Record record) {
    }

    @Override
    public Record[] retrieve(RecordQuery query) {
        return new Record[0];
    }

}
