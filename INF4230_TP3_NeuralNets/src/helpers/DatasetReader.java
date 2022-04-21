package helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DatasetReader {
    public interface ClassLabelMapping {
        int process(final String _input);
    }

    public static ArrayList<LabelledRecord> load_records(final String _file_name, final String _csv_separator,
                                                         final ClassLabelMapping _mapping_values_to_class_labels){
        final ArrayList<LabelledRecord> all_records = new ArrayList<>();
        try (final BufferedReader br = new BufferedReader(new FileReader(_file_name))) {
            String line;
            while ((line = br.readLine()) != null) {
                final String[] row = line.trim().split(_csv_separator);
                final LabelledRecord record = new LabelledRecord();
                record.data = new double[row.length - 1];
                for(int i=0;i!=record.data.length;++i){
                    final double value = Double.parseDouble(row[i].trim());
                    record.data[i] = value;
                }
                record.label = _mapping_values_to_class_labels.process(row[row.length - 1]);
                all_records.add(record);
            }
        }
        catch(final IOException exception){
            System.out.println("We could not read the input file: "+exception.toString());
        }
        return all_records;
    }

    public static List<LabelledRecord> build_sample_set(final List<LabelledRecord> _dataset, final double _ratio, final RandomSampling _s){
        final int smallset_size = (int) Math.floor(_dataset.size() * _ratio);
        final List<LabelledRecord> samples = new ArrayList<>();

        int picked = 0;
        _s.initialize(_dataset);
        while(picked != smallset_size){
            int pickedSample = _s.sample();
            samples.add(_dataset.get(pickedSample));
            picked += 1;
        }
        return samples;
    }

    public static List<LabelledRecord> derive_test_set(final RandomSampling _r, final List<LabelledRecord> _records){
        final List<LabelledRecord> testSet = new ArrayList<>();
        for(int i=0;i!=_records.size();++i){
            if(!_r.samples.containsKey(i)){
                testSet.add(_records.get(i));
            }
        }
        return testSet;
    }

    public static class RandomSampling{
        private final HashMap<Integer, Integer> samples = new HashMap<>();
        private final List<Integer> orderedSamples = new ArrayList<>();
        private final Random random = new Random();

        public int sample(){
            int nextInt;
            do{
                nextInt = random.nextInt(bound);
            }
            while(samples.containsKey(nextInt));
            samples.put(nextInt, 1);
            orderedSamples.add(nextInt);
            return nextInt;
        }

        protected int bound = 0;

        public void initialize(final List<LabelledRecord> _dataset){
            bound = _dataset.size();
        }

    }
}
