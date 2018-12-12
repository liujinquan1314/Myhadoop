package kv.value;

import kv.base.BaseValue;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CountDurationValue extends BaseValue {

    //某个维度通话次数总和
    private String callSum;
    //某个维度通话时间总和
    private String callDurationSum;

    public CountDurationValue() {
        super();
    }

    public CountDurationValue(String callSum, String callDurationSum) {
        super();
        this.callSum = callSum;
        this.callDurationSum = callDurationSum;
    }

    public String getCallSum() {
        return callSum;
    }
    public void setCallSum(String callSum) {
        this.callSum = callSum;
    }
    public String getCallDurationSum() {
        return callDurationSum;
    }
    public void setCallDurationSum(String callDurationSum) {
        this.callDurationSum = callDurationSum;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(callSum);
        dataOutput.writeUTF(callDurationSum);
    }
    @Override
    public void readFields(DataInput dataInput) throws IOException {

        this.callSum=dataInput.readUTF();
        this.callDurationSum=dataInput.readUTF();
    }
}

