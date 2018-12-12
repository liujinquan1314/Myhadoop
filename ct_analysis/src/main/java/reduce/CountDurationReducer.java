package reduce;

import kv.key.CommDimension;
import kv.value.CountDurationValue;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class CountDurationReducer extends Reducer<CommDimension,
        Text,CommDimension,CountDurationValue> {
CountDurationValue durationValue=new CountDurationValue();
    @Override
    protected void reduce(CommDimension key, Iterable<Text> values,
                          Context context) throws IOException, InterruptedException {

        int callsum=0;
        int calldurtionsum=0;

        for(Text t:values){
            callsum++;
            calldurtionsum += Integer.valueOf(t.toString());

        }

        durationValue.setCallSum(String.valueOf(callsum));
        durationValue.setCallDurationSum(String.valueOf(calldurtionsum));
        context.write(key,durationValue);

    }
}
