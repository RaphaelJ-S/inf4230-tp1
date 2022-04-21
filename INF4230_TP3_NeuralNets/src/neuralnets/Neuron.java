package neuralnets;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

public class Neuron {
    public double output;
    public double[] weights;
    public double bias;
    public int layer;
    public int pos;

    private static DecimalFormat df = new DecimalFormat("0.000");//

    public Neuron(final int _layer, final int _pos){
        this.layer = _layer;
        this.pos = _pos;
    }

    @Override
    public String toString(){
        final StringBuilder sb = new StringBuilder();
        for(double d : this.weights){
            if(sb.length() != 0){
                sb.append(", ");
            }
            sb.append(df.format(d));
        }
        return "["+this.layer+"-"+this.pos+"] => a_i="+df.format(this.output)+" w_ij=["+ sb.toString() +"] b="+df.format(this.bias);
    }

    public void initialize_weights_and_bias(final int _nbrInputs, final Random _r, final double _multiplier){
        this.weights = new double[_nbrInputs];
        // initializes weights randomly between 0-1 (with multiplier of 1)
        for(int z=0;z!=this.weights.length;++z){
            this.weights[z] = _r.nextDouble() * _multiplier;
        }
        // initializes bias randomly too
        this.bias = _r.nextDouble() * _multiplier;
    }

    public void initialize_weights_and_bias(final Map<Integer, Double> _weights, final double _bias){
        //Map<Integer, Double> lol = _config.weights.get(i+1).get(j).get(0);
        if(_weights != null) {
            final Integer[] integers = _weights.keySet().toArray(new Integer[0]);
            Arrays.sort(integers);
            this.weights = new double[integers.length];
            for (int z = 0; z != integers.length; ++z) {
                this.weights[z] = _weights.get(integers[z]);
            }
        }
        this.bias = _bias;
    }

    public static double aggregate(final Neuron[] _inputs, final Neuron _n){
        double activation = _n.bias;
        for(int i=0;i!=_inputs.length; ++i){
            activation += _n.weights[i] * _inputs[i].output;
        }
        return activation;
    }

    public static double activate(final double _input){
        return 1.0d / (1.0d + Math.exp(-_input));
    }

    public static double derivative(final double _value){
        return (_value * (1.0d - _value));
    }
}
