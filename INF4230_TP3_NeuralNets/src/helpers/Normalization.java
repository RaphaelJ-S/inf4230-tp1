package helpers;

import java.util.List;

public class Normalization {

    public static void normalize_zero_one(final List<LabelledRecord> _records, final int _nbr_features){
        double[] min_max = new double[2];
        for(int i=0;i!=_nbr_features;++i){
            min_max_bounds(_records, i, min_max);
            min_max_scalling(_records, i, min_max);
        }
    }

    public static void min_max_bounds(final List<LabelledRecord> _records, final int _column, final double[] _min_max){
        double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
        for(final LabelledRecord l : _records){
            if(l.data[_column] < min){
                min = l.data[_column];
            }
            if(l.data[_column] > max){
                max = l.data[_column];
            }
        }
        _min_max[0] = min;
        _min_max[1] = max;
    }

    public static void min_max_scalling(final List<LabelledRecord> _records, final int _column, final double[] _min_max){
        double range = _min_max[1] - _min_max[0];
        for(final LabelledRecord l : _records){
            l.data[_column] = (l.data[_column] - _min_max[0]) / range;
        }
    }
}
